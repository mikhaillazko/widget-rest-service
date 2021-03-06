package com.lazko.board.widget.domain;

import com.lazko.board.widget.domain.exception.NotFoundEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class BoardService {
    // relevant only for single process if we use external storage and have several processes then we need to use distributed lock
    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    WidgetRepository widgetRepository;

    public BoardService(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public Widget createWidget(Integer x, Integer y, Integer z, Integer width, Integer height){
        rwl.writeLock().lock();
        try{
            var id = widgetRepository.generateId();
            if (z == null)
                z = widgetRepository.getMaxZ() + 1;
            var isEmptyZ = widgetRepository.isEmptyZIndex(z);
            if (!isEmptyZ)
                shiftTail(z);
            var widget = new Widget(id, x, y, z, width, height);
            widgetRepository.save(widget);
            return widget;
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public Widget updateWidget(Long id, Integer x, Integer y, Integer z, Integer width, Integer height) throws NotFoundEntityException {
        rwl.writeLock().lock();
        try{
            var widget = widgetRepository.getById(id);
            var oldZValue = widget.getZ();
            if (z == null) {
                var maxZ = widgetRepository.getMaxZ();
                z = maxZ.equals(oldZValue) ? oldZValue : maxZ + 1;
            }
            if (!oldZValue.equals(z)) {
                var isEmptyZ = widgetRepository.isEmptyZIndex(z);
                if (!isEmptyZ)
                    shiftTail(z);
            }

            widget.setInfo(x, y, z, width, height);
            widgetRepository.save(widget);
            return widget;
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public void removeWidget(Long id) throws NotFoundEntityException {
        rwl.writeLock().lock();
        try{
            widgetRepository.deleteById(id);
        } finally {
            rwl.writeLock().unlock();
        }
    }

    public Widget getWidget(Long id) throws NotFoundEntityException {
        rwl.readLock().lock();
        try{
            return widgetRepository.getById(id);
        } finally {
            rwl.readLock().unlock();
        }
    }

    public Page<Widget> getWidgetByPage(Pageable pageable, ScreenArea screenArea) {
        rwl.readLock().lock();
        try{
            return widgetRepository.findAll(pageable, screenArea);
        } finally {
            rwl.readLock().unlock();
        }
    }

    private void shiftTail(Integer z) {
        var widgets = widgetRepository.getTailZ(z);
        Integer shiftedZ = z;
        for (var widget: widgets){
            shiftedZ += 1;
            widget.setZ(shiftedZ);
        }
        widgetRepository.saveAll(widgets);
    }

}
