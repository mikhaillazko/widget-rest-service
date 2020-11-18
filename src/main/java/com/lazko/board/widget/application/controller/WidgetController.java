package com.lazko.board.widget.application.controller;

import com.lazko.board.widget.domain.BoardService;
import com.lazko.board.widget.domain.Widget;
import com.lazko.board.widget.domain.WidgetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/widgets")
public class WidgetController {

    final BoardService boardService;
    final WidgetRepository widgetRepository;
    final ModelMapper modelMapper;

    public WidgetController(BoardService boardService, WidgetRepository widgetRepository, ModelMapper modelMapper) {
        this.boardService = boardService;
        this.widgetRepository = widgetRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OutWidgetDTO createWidget(@Valid @RequestBody InWidgetDTO widgetDTO) {
        var widget = boardService.createWidget(widgetDTO.x, widgetDTO.y, widgetDTO.z, widgetDTO.width, widgetDTO.height);
        return convertToDto(widget);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public OutWidgetDTO updateWidget(@PathVariable Long id, @Valid @RequestBody InWidgetDTO widgetDTO) {
        var widget = boardService.updateWidget(id, widgetDTO.x, widgetDTO.y, widgetDTO.z, widgetDTO.width, widgetDTO.height);
        return convertToDto(widget);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWidget(@PathVariable Long id) {
        boardService.removeWidget(id);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public OutWidgetDTO getWidgetDetail(@PathVariable Long id) {
        var widget = boardService.getWidget(id);
        return convertToDto(widget);
    }

    @GetMapping()
    @ResponseBody
    public Page<OutWidgetDTO> getWidgetList(Pageable pageable) {
        var widgetByPage = boardService.getWidgetByPage(pageable);
        return widgetByPage.map(this::convertToDto);
    }

    private OutWidgetDTO convertToDto(Widget widget) {
        return modelMapper.map(widget, OutWidgetDTO.class);
    }
}
