package com.lazko.board.widget.domain;

import com.lazko.board.widget.domain.exception.InvalidArgValueException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashSet;

public class WidgetInfoTest {

    @Test
    public void testConstructorWithInvalidValue_expectValidObjectConstructed() {
        // Act
        var info = new Widget.WidgetInfo(1, 2, 3, 4, 5);
        // Assert
        Assert.assertEquals(info.x, (Integer) 1);
        Assert.assertEquals(info.y, (Integer) 2);
        Assert.assertEquals(info.z, (Integer) 3);
        Assert.assertEquals(info.width, (Integer) 4);
        Assert.assertEquals(info.height, (Integer) 5);
    }

    @Test(expectedExceptions = InvalidArgValueException.class)
    public void testConstructorWithInvalidValue_throwInvalidArgValueException() {
        // Act
        new Widget.WidgetInfo(null, 2, 3, 4, 5);
    }

    @Test
    public void testToString() {
        var widgetInfo = new Widget.WidgetInfo(1, 2, 3, 4, 5);
        // Act
        var strRepresentation = widgetInfo.toString();
        // Assert
        var expectedRepresentation = "WidgetInfo{x=1, y=2, z=3, width=4, height=5}";
        Assert.assertEquals(strRepresentation, expectedRepresentation);
    }

    @Test
    public void testGetHashCode() {
        var widgetInfo = new Widget.WidgetInfo(1, 1, 1, 1, 1);
        var expectedRepresentation = 29583456;
        // Act
        var strRepresentation = widgetInfo.hashCode();
        // Assert
        Assert.assertEquals(strRepresentation, expectedRepresentation);
    }

    @Test
    public void testGetHashCodePutInSetAndSetSameZ_expectContainsTrue() {
        var widgetInfo = new Widget.WidgetInfo(1, 1, 1, 1, 1);
        var anyHashSet = new HashSet<Widget.WidgetInfo>();
        anyHashSet.add(widgetInfo);
        var anotherInfo = new Widget.WidgetInfo(1, 1, 1, 1, 1);
        // Act
        var containsResult = anyHashSet.contains(anotherInfo);
        // Assert
        Assert.assertTrue(containsResult);
    }

    @Test
    public void testGetHashCodePutInSetAndSetDifferentZ_expectContainsFalse() {
        var widgetInfo = new Widget.WidgetInfo(1, 1, 1, 1, 1);
        var anyHashSet = new HashSet<Widget.WidgetInfo>();
        anyHashSet.add(widgetInfo);
        var anotherInfo = new Widget.WidgetInfo(1, 1, 2, 1, 1);
        // Act
        var containsResult = anyHashSet.contains(anotherInfo);
        // Assert
        Assert.assertFalse(containsResult);
    }

    @DataProvider(name="equals-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), null, false},
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), new Widget.WidgetInfo(1, 1, 1, 1, 1), true},
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), new Widget.WidgetInfo(2, 1, 1, 1, 1), false},
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), new Widget.WidgetInfo(1, 2, 1, 1, 1), false},
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), new Widget.WidgetInfo(1, 1, 2, 1, 1), false},
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), new Widget.WidgetInfo(1, 1, 1, 2, 1), false},
            { new Widget.WidgetInfo(1, 1, 1, 1, 1), new Widget.WidgetInfo(1, 1, 1, 1, 2), false},
        };
    }

    @Test(dataProvider="equals-provider")
    public void testEquals(Widget.WidgetInfo widgetInfo1, Widget.WidgetInfo widgetInfo2, Boolean expectedResult) {
        // Act
        var actualResult = widgetInfo1.equals(widgetInfo2);
        // Assert
        Assert.assertEquals(actualResult, (boolean)expectedResult);
    }
}
