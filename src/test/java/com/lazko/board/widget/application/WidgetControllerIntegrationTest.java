package com.lazko.board.widget.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazko.board.widget.application.controller.InWidgetDTO;
import com.lazko.board.widget.domain.Widget;
import com.lazko.board.widget.domain.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WidgetControllerIntegrationTest extends AbstractTestNGSpringContextTests {
    private static final String ENDPOINT_URI = "/api/widgets";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper modelMapper;

    @Autowired
    private WidgetRepository widgetRepository;

    @Test
    public void testCreateWidgetWithAllFilledFields_expectCreatedAsIs() throws Exception {
        var widgetDTO = new InWidgetDTO(1, 1, 1, 1, 1);
        String json = modelMapper.writeValueAsString(widgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.x").value(widgetDTO.x))
                .andExpect(jsonPath("$.y").value(widgetDTO.y))
                .andExpect(jsonPath("$.z").value(widgetDTO.z))
                .andExpect(jsonPath("$.width").value(widgetDTO.width))
                .andExpect(jsonPath("$.height").value(widgetDTO.height));
    }

    @Test
    public void testCreateWidgetWithUndefinedZCoordinate_expectCreatedWidgetWithMaxZ() throws Exception {
        var widgetDTO = new InWidgetDTO(1, 1, null, 10, 10);
        String json = modelMapper.writeValueAsString(widgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.x").value(widgetDTO.x))
                .andExpect(jsonPath("$.y").value(widgetDTO.y))
                .andExpect(jsonPath("$.z").value(1))
                .andExpect(jsonPath("$.width").value(widgetDTO.width))
                .andExpect(jsonPath("$.height").value(widgetDTO.height));
    }

    @Test
    public void testCreateWidgetWithOccupiedZCoordinate_expectCreatedWidgetWithThisZ() throws Exception {
        var id = widgetRepository.generateId();
        var existingWidget = new Widget(id, 0, 0, 1, 10, 10);
        widgetRepository.save(existingWidget);

        var newWidgetDTO = new InWidgetDTO(1, 1, 1, 10, 10);
        String json = modelMapper.writeValueAsString(newWidgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.x").value(newWidgetDTO.x))
                .andExpect(jsonPath("$.y").value(newWidgetDTO.y))
                .andExpect(jsonPath("$.z").value(newWidgetDTO.z))
                .andExpect(jsonPath("$.width").value(newWidgetDTO.width))
                .andExpect(jsonPath("$.height").value(newWidgetDTO.height));
    }

    @Test
    public void testCreateWidgetAllParamsNull_expectedBadRequestWithErrors() throws Exception {
        var widgetDTO = new InWidgetDTO(null, null, null, null, null);
        String json = modelMapper.writeValueAsString(widgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid input arguments"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem("x: must not be null")))
                .andExpect(jsonPath("$.errors", hasItem("x: must not be null")))
                .andExpect(jsonPath("$.errors", hasItem("width: must not be null")))
                .andExpect(jsonPath("$.errors", hasItem("height: must not be null")));
    }

    @Test
    public void testCreateWidgetInvalidHeight_expectBadRequestWithMessageOfReason() throws Exception {
        var widgetDTO = new InWidgetDTO(1, 1, 1, 10, -10);
        String json = modelMapper.writeValueAsString(widgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .post(ENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid input arguments"))
                .andExpect(jsonPath("$.errors").value("height: must be greater than or equal to 1"));
    }

    @Test
    public void testUpdateWidgetWithAllNewValues_expectOkAllActualizedFields() throws Exception {
        var id = widgetRepository.generateId();
        var existingWidget = new Widget(id, 0, 0, 0, 1, 1);
        widgetRepository.save(existingWidget);

        var updatingWidgetDTO = new InWidgetDTO(5, 5, 5, 10, 10);
        String json = modelMapper.writeValueAsString(updatingWidgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .put(ENDPOINT_URI + '/' + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.x").value(updatingWidgetDTO.x))
                .andExpect(jsonPath("$.y").value(updatingWidgetDTO.y))
                .andExpect(jsonPath("$.z").value(updatingWidgetDTO.z))
                .andExpect(jsonPath("$.width").value(updatingWidgetDTO.width))
                .andExpect(jsonPath("$.height").value(updatingWidgetDTO.height));
    }

    @Test
    public void testUpdateWidgetWithUndefinedZCoordinate_expectOkAllActualizedFields() throws Exception {
        var id = widgetRepository.generateId();
        var existingWidget = new Widget(id, 0, 0, 0, 1, 1);
        widgetRepository.save(existingWidget);

        var updatingWidgetDTO = new InWidgetDTO(5, 5, null, 10, 10);
        String json = modelMapper.writeValueAsString(updatingWidgetDTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .put(ENDPOINT_URI + '/' + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.x").value(updatingWidgetDTO.x))
                .andExpect(jsonPath("$.y").value(updatingWidgetDTO.y))
                .andExpect(jsonPath("$.z").value(existingWidget.getZ()))
                .andExpect(jsonPath("$.width").value(updatingWidgetDTO.width))
                .andExpect(jsonPath("$.height").value(updatingWidgetDTO.height));
    }

    @Test
    public void testUpdateWidgetWithBusyZCoordinate_expectOkShiftedTail() throws Exception {
        for (var i = 1; i <= 5; i++) {
            var id = widgetRepository.generateId();
            var widget = new Widget(id, 0, 0, i, 1, 1);
            widgetRepository.save(widget);
        }
        var widget2 = widgetRepository.getById(2L);
        var updatingWidget2DTO = new InWidgetDTO(
            widget2.getX(), widget2.getY(), widget2.getZ() + 1, widget2.getWidth(), widget2.getHeight()
        );
        String json = modelMapper.writeValueAsString(updatingWidget2DTO);
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .put(ENDPOINT_URI + '/' + widget2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(widget2.getId()))
                .andExpect(jsonPath("$.x").value(updatingWidget2DTO.x))
                .andExpect(jsonPath("$.y").value(updatingWidget2DTO.y))
                .andExpect(jsonPath("$.z").value(updatingWidget2DTO.z))
                .andExpect(jsonPath("$.width").value(updatingWidget2DTO.width))
                .andExpect(jsonPath("$.height").value(updatingWidget2DTO.height));
        var zValues = new LinkedList<Integer>();
        for (var id = 1L; id <= 5L; id++) {
            var widget = widgetRepository.getById(id);
            zValues.add(widget.getZ());
        }
        var expectedZValues = List.of(1, 3, 4, 5, 6);
        Assert.assertEquals(zValues, expectedZValues);
    }

    @Test
    public void testDeleteExistingWidget_expectNoContent() throws Exception {
        var id = widgetRepository.generateId();
        var existingWidget = new Widget(id, 0, 0, 0, 1, 1);
        widgetRepository.save(existingWidget);

        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URI + '/' + id));
        // Assert
        response.andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistingWidget_expectNotFoundWithErrorFields() throws Exception {
        var id = 10L;
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URI + '/' + id));
        // Assert
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Entity with id=10 not found"))
                .andExpect(jsonPath("$.errors").value("10"));
    }

    @Test
    public void testGetExistingWidget_expectOkWithAllFields() throws Exception {
        var id = widgetRepository.generateId();
        var existingWidget = new Widget(id, 0, 0, 0, 10, 10);
        widgetRepository.save(existingWidget);

        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .get(ENDPOINT_URI + '/' + id));
        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingWidget.getId()))
                .andExpect(jsonPath("$.x").value(existingWidget.getX()))
                .andExpect(jsonPath("$.y").value(existingWidget.getY()))
                .andExpect(jsonPath("$.z").value(existingWidget.getZ()))
                .andExpect(jsonPath("$.width").value(existingWidget.getWidth()))
                .andExpect(jsonPath("$.height").value(existingWidget.getHeight()));
    }

    @Test
    public void testGetNonExistingWidget_expectNotFoundWithErrorFields() throws Exception {
        var id = 10L;

        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders
                .get(ENDPOINT_URI + '/' + id));
        // Assert
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Entity with id=10 not found"))
                .andExpect(jsonPath("$.errors").value("10"));
    }

    @Test
    public void testGetListWidgetsWithoutQueryParams_expectOkListOfWidgetsWithDefault10Elements() throws Exception {
        for (var i = 0; i < 20; i++) {
            var id = widgetRepository.generateId();
            Widget widget = new Widget(id, 0, 0, i, 10, 10);
            widgetRepository.save(widget);
        }

        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URI));
        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].x", is(0)))
                .andExpect(jsonPath("$.content[0].y", is(0)))
                .andExpect(jsonPath("$.content[0].z", is(0)))
                .andExpect(jsonPath("$.content[0].width", is(10)))
                .andExpect(jsonPath("$.content[0].height", is(10)));
    }

    @Test
    public void testGetListWidgetsWithQueryParams_expectOkListOfWidgetsWith2Elements() throws Exception {
        for (var i = 0; i < 20; i++) {
            var id = widgetRepository.generateId();
            Widget widget = new Widget(id, 0, 0, i, 10, 10);
            widgetRepository.save(widget);
        }
        // Act
        var response = mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URI + "?page=1&size=2"));
        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(3)))
                .andExpect(jsonPath("$.content[0].x", is(0)))
                .andExpect(jsonPath("$.content[0].y", is(0)))
                .andExpect(jsonPath("$.content[0].z", is(2)))
                .andExpect(jsonPath("$.content[0].width", is(10)))
                .andExpect(jsonPath("$.content[0].height", is(10)))
                .andExpect(jsonPath("$.content[1].id", is(4)))
                .andExpect(jsonPath("$.content[1].x", is(0)))
                .andExpect(jsonPath("$.content[1].y", is(0)))
                .andExpect(jsonPath("$.content[1].z", is(3)))
                .andExpect(jsonPath("$.content[1].width", is(10)))
                .andExpect(jsonPath("$.content[1].height", is(10)))
                .andExpect(jsonPath("$.totalPages", is(10)))
                .andExpect(jsonPath("$.totalElements", is(20)))
                .andExpect(jsonPath("$.pageable.pageNumber", is(1)))
                .andExpect(jsonPath("$.pageable.offset", is(2)))
                .andExpect(jsonPath("$.pageable.pageSize", is(2)))
                .andExpect(jsonPath("$.pageable.paged", is(true)));
    }
}
