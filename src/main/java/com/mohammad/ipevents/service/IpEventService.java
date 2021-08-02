package com.mohammad.ipevents.service;

import com.mohammad.ipevents.model.AppEvents;
import com.mohammad.ipevents.model.IpEvents;
import com.mohammad.ipevents.model.NetworkEvents;
import com.mohammad.ipevents.util.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IpEventService {

    private final IpUtil ipUtil;

    private Map<String, Map<Long, NetworkEvents>> allEvents = new HashMap<>();

    public void addEvent(IpEvents.IpEvent event){
        long networkIp = ipUtil.getNetwork(event.getIp());
        if (allEvents.containsKey(event.getAppSha256())){
            Map<Long, NetworkEvents> networkEventsMap  = allEvents.get(event.getAppSha256());
            if (networkEventsMap.containsKey(networkIp)){
                NetworkEvents networkEvents = networkEventsMap.get(networkIp);
                networkEvents.add(event.getIp());
            }else{
                NetworkEvents networkEvents = new NetworkEvents(event.getIp());
                networkEventsMap.put(networkIp, networkEvents);
            }
        }else{
            NetworkEvents networkEvents = new NetworkEvents(event.getIp());
            Map<Long, NetworkEvents> networkEventsMap = new HashMap<>();
            networkEventsMap.put(networkIp, networkEvents);
            allEvents.put(event.getAppSha256(), networkEventsMap);
        }
    }

    public void delete() {
        allEvents = new HashMap<>();
    }

    public AppEvents getAppEvents(String appSha256){
        Map<Long, NetworkEvents> networkEventsMap = allEvents.get(appSha256);
        int count = 0;
        int max = 0;
        Set<Long> goodIps = new HashSet<>();
        Set<Long> badIps = new HashSet<>();
        for (NetworkEvents networkEvents : networkEventsMap.values()) {
            if (networkEvents.getCount() > max){
                badIps.addAll(goodIps);
                goodIps = networkEvents.getIps();
                max = networkEvents.getCount();
            }else{
                badIps.addAll(networkEvents.getIps());
            }
            count += networkEvents.getCount();
        }
        return new AppEvents(count, ipUtil.convertToStringIps(goodIps), ipUtil.convertToStringIps(badIps));
    }
}
