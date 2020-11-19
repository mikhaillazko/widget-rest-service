package com.lazko.board.widget.infrastructure;

import com.lazko.board.widget.domain.Widget;
import com.lazko.board.widget.domain.WidgetRepository;
import com.lazko.board.widget.domain.exception.NotFoundEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryWidgetRepository implements WidgetRepository {
    private final AtomicLong counter = new AtomicLong();
    final TreeMap<Integer, Long> zIndexMap = new TreeMap<>();
    private final ConcurrentHashMap<Long, Widget> widgetCollection = new ConcurrentHashMap<>();

    public InMemoryWidgetRepository() {}

    @Override
    public Long generateId() {
        return counter.incrementAndGet();
    }

    @Override
    public Integer getMaxZ() {
        if (zIndexMap.size() == 0)
                return 0;
        return zIndexMap.lastKey();
    }

    @Override
    public boolean isEmptyZIndex(Integer z) {
        return !zIndexMap.containsKey(z);
    }

    @Override
    public Page<Widget> findAll(Pageable pageable) {
        var slicedZIndex = zIndexMap
                .values()
                .stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        var slicedWidgets = new LinkedList<Widget>();
        for (var widget_id: slicedZIndex)
            slicedWidgets.add(widgetCollection.get(widget_id));

        return new PageImpl(slicedWidgets, pageable, widgetCollection.size());
    }

    @Override
    public List<Widget> getTailZ(Integer z) {
        SortedMap<Integer, Long> zTail = zIndexMap.tailMap(z);
        List<Widget> tailWidgets = new LinkedList<>();
        for (var widgetId: zTail.values()) {
            Widget widget = widgetCollection.get(widgetId);
            tailWidgets.add(widget);
        }
        return tailWidgets;
    }

    @Override
    public Widget getById(Long id) throws NotFoundEntityException {
        Widget widget = widgetCollection.get(id);
        if (widget == null)
            throw new NotFoundEntityException(id);
        return widget;
    }

    @Override
    public Widget save(Widget entity) {
        widgetCollection.put(entity.getId(), entity);
        zIndexMap.put(entity.getZ(), entity.getId());
        return entity;
    }

    @Override
    public void deleteById(Long id) throws NotFoundEntityException {
        var widget = getById(id);
        zIndexMap.remove(widget.getZ());
        widgetCollection.remove(id);
    }

    @Override
    public Iterable<Widget> saveAll(List<Widget> entities) {
        for (var widget: entities)
            save(widget);
        return entities;
    }
}
