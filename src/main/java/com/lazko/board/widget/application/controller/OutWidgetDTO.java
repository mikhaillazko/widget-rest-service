package com.lazko.board.widget.application.controller;

import java.util.Date;

public class OutWidgetDTO {
    private Long id;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer width;
    private Integer height;
    private Date updatedAt;

    public Long getId() { return id; }

    public Integer getX() { return x; }

    public Integer getY() { return y; }

    public Integer getZ() { return z; }

    public Integer getWidth() { return width; }

    public Integer getHeight() { return height;}

    public Date getUpdatedAt() { return updatedAt; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(Integer x) { this.x = x; }

    public void setY(Integer y) { this.y = y; }

    public void setZ(Integer z) {
        this.z = z;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
