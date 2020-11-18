package com.lazko.board.widget.domain;

import com.lazko.board.widget.domain.exception.InvalidArgValueException;

import java.util.Objects;

public final class WidgetInfo {
    public final Integer x;
    public final Integer y;
    public final Integer z;
    public final Integer width;
    public final Integer height;

    public WidgetInfo(Integer x, Integer y, Integer z, Integer width, Integer height) {
        if (x == null)
            throw new InvalidArgValueException("Widget.x");
        this.x = x;

        if (y == null)
            throw new InvalidArgValueException("Widget.y");
        this.y = y;

        if (z == null)
            throw new InvalidArgValueException("Widget.z");
        this.z = z;

        if (width == null || width < 0)
            throw new InvalidArgValueException("Widget.width");
        this.width = width;

        if (height == null || height < 0)
            throw new InvalidArgValueException("Widget.height");
        this.height = height;
    }

    public WidgetInfo setZ(Integer z){
        return new WidgetInfo(x, y, z, width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WidgetInfo that = (WidgetInfo) o;
        return x.equals(that.x) &&
                y.equals(that.y) &&
                Objects.equals(z, that.z) &&
                width.equals(that.width) &&
                height.equals(that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, width, height);
    }

    @Override
    public String toString() {
        String template = "WidgetInfo{x=%d, y=%d, z=%d, width=%d, height=%d}";
        return String.format(template, x, y, z, width, height);
    }
}
