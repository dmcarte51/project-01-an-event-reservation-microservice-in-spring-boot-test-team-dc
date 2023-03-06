package edu.msudenver.venue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@WebMvcTest(value = VenueController.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueRepository venueRepository;

    @SpyBean
    private VenueService venueService;

    @Test
    public void testGetVenues() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setId(9999);
        testVenue.setVenueName("Ball Arena");

        Mockito.when(venueRepository.findAll()).thenReturn(Arrays.asList(testVenue));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Ball Arena"));
    }

    @Test
    public void testGetVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/304")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setId(304);
        testVenue.setVenueName("Ball Arena");

        Mockito.when(venueRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testVenue));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Ball Arena"));
    }

    @Test
    public void testGetVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueName\":\"Oracle Arena\", " + "\"venueType\": \"Stadium\", " + "\"streetAddress\": \"4312 test street\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue oracle = new Venue();
        oracle.setVenueName("Oracle Arena");
        oracle.setVenueType("Stadium");
        oracle.setStreetAddress("4312 test street");
        Mockito.when(venueRepository.save(Mockito.any())).thenReturn(oracle);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Oracle Arena"));
    }

    @Test
    public void testCreateVenueBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueName\": \"Oracle\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"VenueId\":\"1\", \"venueName\": \"Updated\", " +
                        "\"streetAddress\": \"1234 updated street\", \"venueType\": \"Stadium\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue oracle = new Venue();
        oracle.setVenueName("Oracle");
        oracle.setId(1);
        oracle.setStreetAddress("1234 updated st");
        oracle.setVenueType("Stadium");
        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(oracle));

        Venue oracleUpdated = new Venue();
        oracleUpdated.setVenueName("Oracle Updated");
        oracleUpdated.setId(1);
        Mockito.when(venueRepository.save(Mockito.any())).thenReturn(oracleUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Oracle Updated"));
    }

    @Test
    public void testUpdateVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/0")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueId\":\"0\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateVenueBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"venueId\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue oracle = new Venue();
        oracle.setVenueName("Oracle");
        oracle.setId(1);
        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(oracle));

        Mockito.when(venueRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/venues/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Venue oracle = new Venue();
        oracle.setVenueName("oracle");
        oracle.setId(1);
        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(oracle));
        Mockito.when(venueRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/venues/34573")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(venueRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(venueRepository)
                .deleteById(Mockito.any());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }


}
