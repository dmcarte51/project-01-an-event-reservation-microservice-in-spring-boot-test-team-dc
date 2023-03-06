package edu.msudenver.event;

import edu.msudenver.event.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @SpyBean
    private EventService eventService;

    @Test
    public void testGetEvents() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(9999);
        testEvent.setEventTitle("Gaming Hours");

        Mockito.when(eventRepository.findAll()).thenReturn(Arrays.asList(testEvent));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Gaming Hours"));
    }

    @Test
    public void testGetEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/304")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event testEvent = new Event();
        testEvent.setEventId(304);
        testEvent.setEventTitle("gaming hours");

        Mockito.when(eventRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testEvent));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("gaming hours"));
    }

    @Test
    public void testGetEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventTitle\":\"gaming hours\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event gaming = new Event();
        gaming.setEventTitle("gaming hours");
        Mockito.when(eventRepository.save(Mockito.any())).thenReturn(gaming);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("gaming hours"));
    }

    @Test
    public void testCreateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventTitle\": \"Gaming hours\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"1\", \"eventTitle\": \"gaming Updated\", " +
                        "\"eventStart\": \"2022-03-04T12:00:00.000\", \"eventEnd\": \"2022-03-04T16:00:00.000\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event gaming = new Event();
        gaming.setEventTitle("Gaming hours");
        gaming.setEventId(1);
        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(gaming));

        Event gamingUpdated = new Event();
        gamingUpdated.setEventTitle("gaming hours Updated");
        gamingUpdated.setEventId(1);
        Mockito.when(eventRepository.save(Mockito.any())).thenReturn(gamingUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("gaming hours Updated"));
    }

    @Test
    public void testUpdateEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/0")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"0\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"eventId\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event gaming = new Event();
        gaming.setEventTitle("gaming hours");
        gaming.setEventId(1);
        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(gaming));

        Mockito.when(eventRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Event gaming = new Event();
        gaming.setEventTitle("oracle");
        gaming.setEventId(1);
        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(gaming));
        Mockito.when(eventRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/34573")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(eventRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(eventRepository)
                .deleteById(Mockito.any());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }


}
