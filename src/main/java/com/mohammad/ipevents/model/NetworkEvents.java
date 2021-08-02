package com.mohammad.ipevents.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class NetworkEvents {
    private int count = 0;
    private Set<Long> ips = new HashSet<>();

    public void add(long ip){
        this.ips.add(ip);
        this.count ++;
    }

    public NetworkEvents(long ip){
        this.add(ip);
    }
}
