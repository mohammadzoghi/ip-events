package com.mohammad.ipevents.controller;

import com.mohammad.ipevents.model.AppEventsSummary;
import com.mohammad.ipevents.model.IpEvents;
import com.mohammad.ipevents.service.IpEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

import static com.mohammad.ipevents.model.IpEvents.IpEvent;

/**
 * This is a controller that exposes rest endpoints for events
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class IpEventController {

    private final IpEventService ipEventService;

    /**
     * This endpoint receives ip events sends them to IpEventService to be categorized and stored.
     *
     * @param inputStream IpEvent in protobuf format
     */
    @PostMapping(value = "/events", consumes = "application/octet-stream")
    public void events(InputStream inputStream) throws IOException {
        try (inputStream) {
            IpEvent event = IpEvents.IpEvent.parseFrom(inputStream);
            ipEventService.addEvent(event);
            log.trace("Read event:[app: {}, ip: {}]", event.getAppSha256(), event.getIp());
        }
    }

    /**
     * This endpoint returns the summary of ip events sent from a particular app in json format
     *
     * @param appSha256 Unique id for app
     * @return AppEvents
     */
    @GetMapping(value = "/events/{appSha256}", produces = "application/json")
    public AppEventsSummary getAppIps(@PathVariable String appSha256) {
        log.info("Getting ips for app:{}", appSha256);
        AppEventsSummary appEventsSummary = ipEventService.getAppEvents(appSha256);
        if (appEventsSummary.getCount() == 0) {
            log.warn("No events for app: {}", appSha256);
            throw new AppNotFoundException("No events for app: " + appSha256);
        }
        return appEventsSummary;
    }

    /**
     * This endpoint deletes previous entries of IpEvents
     */
    @DeleteMapping("/events")
    public void delete() {
        log.info("Deleting previous events");
        ipEventService.delete();

    }
}
