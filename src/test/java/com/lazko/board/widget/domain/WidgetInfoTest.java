package com.lazko.board.widget.domain;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashSet;

public class WidgetInfoTest {

    @Test
    public void testConstructorWithInvalidValue_throwInvalidArgValueException() {
        // Act
        new WidgetInfo(1, 2, 3, 4, 5);
        // Assert
    }

    @Test
    public void testToString() {
        var widgetInfo = new WidgetInfo(1, 2, 3, 4, 5);
        // Act
        var strRepresentation = widgetInfo.toString();
        // Assert
        var expectedRepresentation = "WidgetInfo{x=1, y=2, z=3, width=4, height=5}";
        Assert.assertEquals(strRepresentation, expectedRepresentation);
    }

    @Test
    public void testGetHashCode() {
        var widgetInfo = new WidgetInfo(1, 1, 1, 1, 1);
        var expectedRepresentation = 29583456;
        // Act
        var strRepresentation = widgetInfo.hashCode();
        // Assert
        Assert.assertEquals(strRepresentation, expectedRepresentation);
    }

    @Test
    public void testGetHashCodePutInSetAndSetSameZ_expectContainsTrue() {
        var widgetInfo = new WidgetInfo(1, 1, 1, 1, 1);
        var anyHashSet = new HashSet<WidgetInfo>();
        anyHashSet.add(widgetInfo);
        var newInfo = widgetInfo.setZ(1);
        // Act
        var containsResult = anyHashSet.contains(newInfo);
        // Assert
        Assert.assertTrue(containsResult);
    }

    @Test
    public void testGetHashCodePutInSetAndSetDifferentZ_expectContainsFalse() {
        var widgetInfo = new WidgetInfo(1, 1, 1, 1, 1);
        var anyHashSet = new HashSet<WidgetInfo>();
        anyHashSet.add(widgetInfo);
        var newInfo = widgetInfo.setZ(2);
        // Act
        var containsResult = anyHashSet.contains(newInfo);
        // Assert
        Assert.assertFalse(containsResult);
    }

    @DataProvider(name="equals-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{
            { new WidgetInfo(1, 1, 1, 1, 1), null, false},
            { new WidgetInfo(1, 1, 1, 1, 1), new WidgetInfo(1, 1, 1, 1, 1), true},
            { new WidgetInfo(1, 1, 1, 1, 1), new WidgetInfo(2, 1, 1, 1, 1), false},
            { new WidgetInfo(1, 1, 1, 1, 1), new WidgetInfo(1, 2, 1, 1, 1), false},
            { new WidgetInfo(1, 1, 1, 1, 1), new WidgetInfo(1, 1, 2, 1, 1), false},
            { new WidgetInfo(1, 1, 1, 1, 1), new WidgetInfo(1, 1, 1, 2, 1), false},
            { new WidgetInfo(1, 1, 1, 1, 1), new WidgetInfo(1, 1, 1, 1, 2), false},
        };
    }

    @Test(dataProvider="equals-provider")
    public void testEquals(WidgetInfo widgetInfo1, WidgetInfo widgetInfo2, Boolean expectedResult) {
        // Act
        var actualResult = widgetInfo1.equals(widgetInfo2);
        // Assert
        Assert.assertEquals(actualResult, (boolean)expectedResult);
    }
}
