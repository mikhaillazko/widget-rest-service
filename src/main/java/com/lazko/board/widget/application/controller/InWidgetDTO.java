package com.lazko.board.widget.application.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InWidgetDTO {
    @NotNull()
    public Integer x;
    @NotNull()
    public Integer y;
    public Integer z;
    @NotNull()
    @Min(1)
    public Integer width;
    @NotNull()
    @Min(1)
    public Integer height;

    public InWidgetDTO(@NotNull() Integer x, @NotNull() Integer y, Integer z, @NotNull() @Min(1) Integer width, @NotNull() @Min(1) Integer height) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
    }
}
