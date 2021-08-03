package com.mohammad.ipevents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohammad.ipevents.model.AppEvents;
import com.mohammad.ipevents.model.IpEvents;
import com.mohammad.ipevents.service.IpEventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(IpEventController.class)
public class IpEventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    IpEventService ipEventService;

    @Test
    void addEventContentTypeTest() throws Exception {
        mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addEventMethodTest() throws Exception {
        mockMvc.perform(get("/events").contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(put("/events").contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getAppIpsTest() throws Exception {
        Set<String> goodIps = new HashSet<>(Arrays.asList("54.45.72.139", "54.45.72.129", "54.45.72.137"));
        Set<String> badIps = new HashSet<>(Arrays.asList("116.185.111.50", "21.101.201.155"));
        AppEvents events = new AppEvents(50, goodIps, badIps);
        String responseJson = new ObjectMapper().writeValueAsString(events);

        when(ipEventService.getAppEvents(any(String.class))).thenReturn(events);

        mockMvc.perform(get("/events/appId-857452").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().json(responseJson, false));

        verify(ipEventService, times(1)).getAppEvents("appId-857452");
    }

    @Test
    void addEventTest() throws Exception {
        IpEvents.IpEvent event = IpEvents.IpEvent.newBuilder()
                .setIp(358992283L)
                .setAppSha256("appKey-3876347").build();

        doNothing().when(ipEventService).addEvent(any(IpEvents.IpEvent.class));

        mockMvc.perform(post("/events").contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .content(event.toByteArray()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(ipEventService, times(1)).addEvent(eq(event));
    }
}
