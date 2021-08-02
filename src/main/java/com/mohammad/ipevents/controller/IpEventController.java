package com.mohammad.ipevents.controller;

import com.mohammad.ipevents.model.IpEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@Slf4j
public class IpEventController {

    @PostMapping(value = "/events", consumes = "application/octet-stream")
    public void events(InputStream inputStream) throws IOException {
        IpEvents.IpEvent event = IpEvents.IpEvent.parseFrom(inputStream);
        long ip = event.getIp();

        log.info("read event:[app:{}, ip:{}]", event.getAppSha256(), (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF));
    }

    @GetMapping(value = "/events/{appSha256}", produces = "application/json")
    public void getAppIps(@PathVariable String appSha256){
        log.info("getting ips for app:{}", appSha256);
    }

    @DeleteMapping("/events")
    public void delete(){
        log.info("delete");
    }
}
