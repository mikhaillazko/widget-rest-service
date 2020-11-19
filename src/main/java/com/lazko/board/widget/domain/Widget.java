package com.lazko.board.widget.domain;

import com.lazko.board.widget.domain.exception.InvalidArgValueException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "widgets")
public class Widget {

    @Id
    private Long id;
    @Embedded
    private WidgetInfo info;
    @Column(name = "UPDATEDAT", table = "widgets")
    private Date updatedAt;

    @Embeddable
    public static class WidgetInfo implements Serializable {
        @Column(name = "X", table = "widgets")
        public final Integer x;
        @Column(name = "Y", table = "widgets")
        public final Integer y;
        @Column(name = "Z", table = "widgets")
        public final Integer z;
        @Column(name = "width", table = "widgets")
        public final Integer width;
        @Column(name = "height", table = "widgets")
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

        protected WidgetInfo() {
            x = null;
            y = null;
            z = null;
            height = null;
            width = null;
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

    public Widget(Long id, Integer x, Integer y, Integer z, Integer width, Integer height) {
        this.id = id;
        this.setInfo(x, y, z, width, height);
    }

    protected Widget() {}

    public Long getId() { return id; }

    public Integer getX() { return info.x; }

    public Integer getY() { return info.y; }

    public Integer getZ() {
        return info.z;
    }

    public Integer getWidth() {
        return info.width;
    }

    public Integer getHeight() {
        return info.height;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setInfo(Integer x, Integer y, Integer z, Integer width, Integer height){
        this.info = new WidgetInfo(x, y, z, width, height);
        this.updatedAt = new Date();
    }

    public void setZ(Integer z) {
        setInfo(info.x, info.y, z, info.width, info.height);
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
