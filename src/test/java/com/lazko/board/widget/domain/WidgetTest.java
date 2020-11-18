package com.lazko.board.widget.domain;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashSet;

public class WidgetTest {

    @Test
    public void testGetHashCode() {
        var widget = new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1));
        var expectedRepresentation = 32;
        // Act
        var hashValue = widget.hashCode();
        // Assert
        Assert.assertEquals(hashValue, expectedRepresentation);
    }
    @Test
    public void testGetHashCodePutInSetNotChangeFields_expectContainsTrue() {
        var widget = new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1));
        var anyHashSet = new HashSet<Widget>();
        anyHashSet.add(widget);
        // Act
        var containsResult = anyHashSet.contains(widget);
        // Assert
        Assert.assertTrue(containsResult);
    }

    @Test
    public void testGetHashCodePutInSetAndSetAnotherCoordinate_expectContainsTrue() {
        var widget = new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1));
        var anyHashSet = new HashSet<Widget>();
        anyHashSet.add(widget);
        widget.setInfo(new WidgetInfo(2, 2, 2, 2, 2));
        // Act
        var containsResult = anyHashSet.contains(widget);
        // Assert
        Assert.assertTrue(containsResult);
    }

    @DataProvider(name="equals-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{
            { new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1)), null, false},
            { new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1)), new Widget(2L, new WidgetInfo(1, 1, 1, 1, 1)), false},
            { new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1)), new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1)), true},
            { new Widget(1L, new WidgetInfo(1, 1, 1, 1, 1)), new Widget(1L, new WidgetInfo(2, 2, 2, 2, 2)), true},
        };
    }

    @Test(dataProvider="equals-provider")
    public void testEquals(Widget widget1, Widget widget2, Boolean expectedResult) {
        // Act
        var actualResult = widget1.equals(widget2);
        // Assert
        Assert.assertEquals(actualResult, (boolean)expectedResult);
    }
}
