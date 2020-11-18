package com.lazko.board.widget.domain;

import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;
import com.lazko.board.widget.infrastructure.InMemoryWidgetRepository;
import org.junit.After;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BoardServiceConcurrencyTest {

    private BoardService boardService;
    private InMemoryWidgetRepository widgetRepository;

    public BoardServiceConcurrencyTest(){
        widgetRepository = new InMemoryWidgetRepository();
        boardService = new BoardService(widgetRepository);
    }

    @Test
    @Concurrent(count = 5)
    @Repeating(repetition = 100)
    public void testWithConcurrencyCreatingWidgets() throws InterruptedException {
        boardService.createWidget(1, 1, 1, 1, 1);
    }

    @After
    public void annotatedTestRunsMultipleTimes() {
        Assert.assertEquals(widgetRepository.getMaxZ(), (Integer)100);
    }

}
