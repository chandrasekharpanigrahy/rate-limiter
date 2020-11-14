package com.sekhar.rate.limiter;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RateLimiterServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_OK_as_status_when_number_of_requests_are_with_in_limit() throws Exception {
        val request = get("/api/v1/sample")
                .header("X-API-Key", "client2")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void should_return_too_many_request_as_status_when_number_of_requests_are_out_of_limit() throws Exception {
        val request = get("/api/v1/sample")
                .header("X-API-Key", "client1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request);
        mockMvc.perform(request);
        mockMvc.perform(request);
        mockMvc.perform(request).andExpect(status().isTooManyRequests());
    }

    @Test
    public void should_return_OK_as_when_API_is_called_after_waiting_for_specified_time_if_number_of_request_are_exceeded() throws Exception {
        val request = get("/api/v1/sample")
                .header("X-API-Key", "client1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request);
        mockMvc.perform(request);
        mockMvc.perform(request);
        mockMvc.perform(request).andExpect(status().isTooManyRequests());
        Thread.sleep(1000);
        mockMvc.perform(request).andExpect(status().isOk());
    }

}