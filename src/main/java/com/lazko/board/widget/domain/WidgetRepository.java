package com.lazko.board.widget.domain;

import com.lazko.board.widget.domain.exception.NotFoundEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface WidgetRepository {
    Long generateId();
    Integer getMaxZ();
    boolean isEmptyZIndex(Integer z);
    List<Widget> getTailZ(Integer z);
    Page<Widget> findAll(Pageable pageable);
    Widget getById(Long id) throws NotFoundEntityException;
    void save(Widget widget);
    void saveAll(List<Widget> widgets);
    void deleteById(Long id) throws NotFoundEntityException;
}
