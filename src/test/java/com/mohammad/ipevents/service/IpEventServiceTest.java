package com.mohammad.ipevents.service;

import com.mohammad.ipevents.model.AppEventsSummary;
import com.mohammad.ipevents.model.IpEvents;
import com.mohammad.ipevents.util.IpUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mohammad.ipevents.model.IpEvents.IpEvent;
import static org.junit.jupiter.api.Assertions.*;

public class IpEventServiceTest {

    private static final long _54_45_72_142 = 908937358L;
    private static final long _54_45_72_131 = 908937347L;
    private static final long _21_101_201_155 = 358992283L;

    private static IpEventService ipEventService;

    private static List<IpEvent> events;

    @BeforeAll
    static void populateEvents(){
        IpEvent event1 = IpEvents.IpEvent.newBuilder()
                .setIp(_54_45_72_142)
                .setAppSha256("appKey-3876347").build();
        IpEvent event2 = IpEvents.IpEvent.newBuilder()
                .setIp(_54_45_72_131)
                .setAppSha256("appKey-3876347").build();
        IpEvent event3 = IpEvents.IpEvent.newBuilder()
                .setIp(_21_101_201_155)
                .setAppSha256("appKey-3876347").build();
        events = new ArrayList<>(Arrays.asList(event1, event2, event3));
    }

    @BeforeAll
    static void createIpEventsService(){
        MockEnvironment env = new MockEnvironment();
        env.setProperty("ip-events.mask", "28");
        ipEventService = new IpEventService(new IpUtil(env));
    }

    @BeforeEach
    void clearState(){
        ipEventService.delete();
        events.forEach(event -> ipEventService.addEvent(event));
    }

    @Test
    void deleteTest(){
        assertFalse(ipEventService.getAppEvents("appKey-3876347").getGoodIps().isEmpty());

        ipEventService.delete();

        AppEventsSummary appEventsSummary = ipEventService.getAppEvents("appKey-3876347");
        assertTrue(appEventsSummary.getGoodIps().isEmpty());
        assertTrue(appEventsSummary.getBadIps().isEmpty());
        assertEquals(0, appEventsSummary.getCount());
    }

    @Test
    void getAppEventsTest(){

        AppEventsSummary appEvents = ipEventService.getAppEvents("appKey-3876347");
        assertEquals(3, appEvents.getCount());

        assertEquals(2, appEvents.getGoodIps().size());
        assertTrue(appEvents.getGoodIps().containsAll(Arrays.asList("54.45.72.142", "54.45.72.131")));

        assertEquals(1, appEvents.getBadIps().size());
        assertTrue(appEvents.getBadIps().contains("21.101.201.155"));
    }
}
