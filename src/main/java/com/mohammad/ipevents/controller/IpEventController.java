package com.mohammad.ipevents.controller;

import com.mohammad.ipevents.model.AppEvents;
import com.mohammad.ipevents.model.IpEvents;
import com.mohammad.ipevents.service.IpEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@Slf4j
@RequiredArgsConstructor
public class IpEventController {

    private final IpEventService ipEventService;

    @PostMapping(value = "/events", consumes = "application/octet-stream")
    public void events(InputStream inputStream) throws IOException {
        try (inputStream){
        IpEvents.IpEvent event = IpEvents.IpEvent.parseFrom(inputStream);
        ipEventService.addEvent(event);
        log.trace("Read event:[app: {}, ip: {}]", event.getAppSha256(), event.getIp());
        }
    }

    @GetMapping(value = "/events/{appSha256}", produces = "application/json")
    public AppEvents getAppIps(@PathVariable String appSha256){
        log.info("Getting ips for app:{}", appSha256);
        return ipEventService.getAppEvents(appSha256);
    }

    @DeleteMapping("/events")
    public void delete(){
        log.info("Deleting previous events");
        ipEventService.delete();

    }
}
