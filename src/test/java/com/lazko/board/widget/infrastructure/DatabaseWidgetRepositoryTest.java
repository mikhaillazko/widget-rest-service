package com.lazko.board.widget.infrastructure;

import com.lazko.board.widget.domain.Widget;
import com.lazko.board.widget.domain.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class DatabaseWidgetRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    DatabaseWidgetRepository widgetRepository;

    @Test
    public void testSaveNew_expectSuccessfulExecuted() {
        var id = widgetRepository.generateId();
        var newWidget = new Widget(id,1, 1, 1, 1, 1);
        // Act
        widgetRepository.save(newWidget);
        // Assert
        Widget actualWidget = widgetRepository.getById(id);
        Assert.assertEquals(actualWidget.getZ(), newWidget.getZ());
    }

    @Test
    public void testSaveExisting_expectSuccessfulExecuted() {
        var id = widgetRepository.generateId();
        var newWidget = new Widget(id,1, 1, 1, 1, 1);
        widgetRepository.save(newWidget);
        newWidget.setZ(2);
        // Act
        widgetRepository.save(newWidget);
        // Assert
        Widget actualWidget = widgetRepository.getById(id);
        Assert.assertEquals(actualWidget.getZ(), newWidget.getZ());
    }
}
