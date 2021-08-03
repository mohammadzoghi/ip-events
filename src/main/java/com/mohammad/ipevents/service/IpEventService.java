package com.mohammad.ipevents.service;

import com.mohammad.ipevents.model.AppEventsSummary;
import com.mohammad.ipevents.model.NetworkEvents;
import com.mohammad.ipevents.util.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.mohammad.ipevents.model.IpEvents.IpEvent;

/**
 * This service handles all the works and computation needed for categorizing and storing ip events.
 * Program state is saved in the thread safe structure of ConcurrentHashMap in this class.
 */
@Service
@RequiredArgsConstructor
public class IpEventService {

    private final IpUtil ipUtil;

    private ConcurrentHashMap<String, ConcurrentHashMap<Long, NetworkEvents>> allEvents = new ConcurrentHashMap<>();

    /**
     * This method adds a new ip event to the program state.
     */
    public void addEvent(IpEvent event) {
        long networkIp = ipUtil.getNetwork(event.getIp());
        if (allEvents.containsKey(event.getAppSha256())) {
            ConcurrentHashMap<Long, NetworkEvents> networkEventsMap = allEvents.get(event.getAppSha256());
            if (networkEventsMap.containsKey(networkIp)) {
                NetworkEvents networkEvents = networkEventsMap.get(networkIp);
                networkEvents.add(event.getIp());
            } else {
                networkEventsMap.put(networkIp, new NetworkEvents(event.getIp()));
            }
        } else {
            ConcurrentHashMap<Long, NetworkEvents> networkEventsMap = new ConcurrentHashMap<>();
            networkEventsMap.put(networkIp, new NetworkEvents(event.getIp()));
            allEvents.put(event.getAppSha256(), networkEventsMap);
        }
    }

    /**
     * This method clears the state of the program.
     */
    public void delete() {
        allEvents = new ConcurrentHashMap<>();
    }

    /**
     * This method creates the app event summary from current state.
     * This means it finds the set of good ips based on how many events were from each network,
     * cumulates all the other ips in a set as bad ips and calculates total number of events for the given app.
     */
    public AppEventsSummary getAppEvents(String appSha256) {
        Map<Long, NetworkEvents> networkEventsMap = allEvents.get(appSha256);
        int count = 0;
        int max = 0;
        Set<Long> goodIps = new HashSet<>();
        Set<Long> badIps = new HashSet<>();
        if (networkEventsMap != null) {
            for (NetworkEvents networkEvents : networkEventsMap.values()) {
                if (networkEvents.getCount() > max) {
                    badIps.addAll(goodIps);
                    goodIps = networkEvents.getIps();
                    max = networkEvents.getCount();
                } else {
                    badIps.addAll(networkEvents.getIps());
                }
                count += networkEvents.getCount();
            }
        }
        return new AppEventsSummary(count, ipUtil.convertToStringIps(goodIps), ipUtil.convertToStringIps(badIps));
    }
}
