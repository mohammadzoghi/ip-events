package com.mohammad.ipevents.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class IpUtil {
    private final Environment env;

    private long getBitMask(){
        int mask = Integer.parseInt(env.getProperty("ip-events.mask") == null ? "28" : Objects.requireNonNull(env.getProperty("ip-events.mask")));
         return Long.MAX_VALUE << (32 - mask);
    }

    public Set<String> convertToStringIps(Set<Long> longIps){
        Set<String> ips = new HashSet<>();
        longIps.forEach(ip -> ips.add(convertToStringIp(ip)));
        return ips;
    }

    private String convertToStringIp(long ip){
        return ((ip >> 24) & 0xFF)+ "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF) ;
    }

    public long getNetwork(long ip){
        return ip & getBitMask();
    }
}
