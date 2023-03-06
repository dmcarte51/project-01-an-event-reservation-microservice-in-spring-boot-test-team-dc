package edu.msudenver.city;

import edu.msudenver.country.Country;
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
@WebMvcTest(value = CityController.class)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityRepository cityRepository;

    @SpyBean
    private CityService cityService;

    @Test
    public void testGetCities() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        City testCity = new City();
        testCity.setPostalCode("11112");
        testCity.setCityName("Denver");

        Mockito.when(cityRepository.findAll()).thenReturn(Arrays.asList(testCity));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Denver"));
    }

    @Test
    public void testGetCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/us/90210/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        City testCity = new City();
        Country testCountry = new Country();
        testCountry.setCountryCode("us");
        testCity.setPostalCode("90210");
        testCity.setCityName("Compton");
        testCity.setCountryCode(testCountry);

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(testCity));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("90210"));
    }
    @Test
    public void testGetCityNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityRepository.findById(new CityId("0","0"))).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"postalCode\":\"11111\", \"cityName\": \"Littleton\"}")
                .contentType(MediaType.APPLICATION_JSON);

        City littleton = new City();
        littleton.setCityName("Littleton");
        littleton.setPostalCode("11111");
        Mockito.when(cityRepository.save(Mockito.any())).thenReturn(littleton);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Littleton"));
    }

    @Test
    public void testCreateCityBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"cityName\": \"Littleton\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/us/11111")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"postalCode\":\"11111\", \"cityName\": \"littleton updated\", \"countryCode\": \"us\"}")
                .contentType(MediaType.APPLICATION_JSON);
        Country us = new Country();
        us.setCountryCode("us");
        City littleton = new City();
        littleton.setCityName("Littleton");
        littleton.setPostalCode("11111");
        littleton.setCountryCode(us);
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(littleton));

        City littletonUpdated = new City();
        littletonUpdated.setCityName("Littleton Updated");
        littletonUpdated.setPostalCode("11111");
        Mockito.when(cityRepository.save(Mockito.any())).thenReturn(littletonUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Littleton Updated"));
    }

    @Test
    public void testUpdateCityNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"postalCode\":\"notfound\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateCityBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/us/11112")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"postalCode\":\"11112\"}")
                .contentType(MediaType.APPLICATION_JSON);

        City denver = new City();
        denver.setCityName("Denver");
        denver.setPostalCode("11112");
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(denver));

        Mockito.when(cityRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteCity() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/cities/us/11112")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        Country us = new Country();
        us.setCountryCode("us");
        City denver = new City();
        denver.setCountryCode(us);
        denver.setCityName("Denver");
        denver.setPostalCode("11112");
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(denver));
        Mockito.when(cityRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteCityNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/cities/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(cityRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(cityRepository)
                .deleteById(Mockito.any());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
}
