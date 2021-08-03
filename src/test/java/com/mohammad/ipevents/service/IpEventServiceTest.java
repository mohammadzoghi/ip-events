package com.mohammad.ipevents.service;

import com.mohammad.ipevents.model.AppEvents;
import com.mohammad.ipevents.model.IpEvents;
import com.mohammad.ipevents.model.NetworkEvents;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class IpEventServiceTest {

    @Autowired
    private IpEventService ipEventService;

    @Test
    void deleteTest(){
        ipEventService.delete();
        ipEventService.getAllEvents().put("app-id", new ConcurrentHashMap<>());
        assertEquals(1, ipEventService.getAllEvents().size());
        ipEventService.delete();
        assertTrue(ipEventService.getAllEvents().isEmpty());
    }

    @Test
    void addEventTest(){
        ipEventService.delete();
        IpEvents.IpEvent event = IpEvents.IpEvent.newBuilder()
                .setIp(358992283L)
                .setAppSha256("appKey-3876347").build();
        ipEventService.addEvent(event);
        assertTrue(ipEventService.getAllEvents().containsKey("appKey-3876347"));
        assertTrue(ipEventService.getAllEvents().get("appKey-3876347").containsKey(358992272L));
        assertTrue(ipEventService.getAllEvents().get("appKey-3876347").get(358992272L).getIps().contains(358992283L));
    }

    @Test
    void getAppEvents(){
        ipEventService.delete();
        ConcurrentHashMap<String, ConcurrentHashMap<Long, NetworkEvents>> allEvents = ipEventService.getAllEvents();
        NetworkEvents goodEvents = new NetworkEvents(908937347L);
        goodEvents.add(908937358L);
        NetworkEvents badEvents = new NetworkEvents(358992283L);
        ConcurrentHashMap<Long, NetworkEvents> appEventsMap = new ConcurrentHashMap<>();
        appEventsMap.put(908937344L, goodEvents);
        appEventsMap.put(358992272L, badEvents);
        allEvents.put("appKey-3876347", appEventsMap);

        AppEvents appEvents = ipEventService.getAppEvents("appKey-3876347");
        assertEquals(3, appEvents.getCount());

        assertEquals(2, appEvents.getGoodIps().size());
        assertTrue(appEvents.getGoodIps().containsAll(Arrays.asList("54.45.72.142", "54.45.72.131")));

        assertEquals(1, appEvents.getBadIps().size());
        assertTrue(appEvents.getBadIps().contains("21.101.201.155"));
    }
}
