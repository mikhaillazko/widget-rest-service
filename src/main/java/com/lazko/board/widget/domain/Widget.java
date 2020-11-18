package com.lazko.board.widget.domain;

import java.util.Date;
import java.util.Objects;

public class Widget {
    private final Long id;
    private WidgetInfo info;
    private Date updatedAt;

    public Widget(Long id, WidgetInfo info) {
        this.id = id;
        this.setInfo(info);
    }

    public void setInfo(WidgetInfo info){
        this.info = info;
        this.updatedAt = new Date();
    }

    public WidgetInfo getInfo(){
        return this.info;
    }

    public Long getId() { return id; }

    public Integer getX() {
        return getInfo().x;
    }

    public Integer getY() { return getInfo().y; }

    public Integer getZ() {
        return getInfo().z;
    }

    public Integer getWidth() {
        return getInfo().width;
    }

    public Integer getHeight() {
        return getInfo().height;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return id.equals(widget.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String template = "Widget{id=%d, x=%d, y=%d, z=%d, width=%d, height=%d, updatedAt=%s}";
        return String.format(template, id, info.x, info.y, info.z, info.width, info.height, updatedAt.toString());
    }
}
