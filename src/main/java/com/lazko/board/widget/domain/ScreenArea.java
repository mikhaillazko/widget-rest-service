package com.lazko.board.widget.domain;

import java.util.Objects;

public class ScreenArea {
    public final Integer x;
    public final Integer y;
    public final Integer width;
    public final Integer height;

    public ScreenArea(Integer x, Integer y, Integer width, Integer height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isIncluding(Widget widget){
        return x <= widget.getX() &&
                y >= widget.getY() &&
                x + width >= widget.getX() + widget.getWidth() &&
                y + height >= widget.getY()  + widget.getHeight();
    }

    public boolean isEmpty() {
        return Objects.isNull(x) ||
                Objects.isNull(y) ||
                Objects.isNull(width) ||
                Objects.isNull(height);
    }
}
