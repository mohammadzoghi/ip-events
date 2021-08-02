package com.mohammad.ipevents.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class NetworkEvents {
    private int networkIp;
    private Set<Integer> ips = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkEvents that = (NetworkEvents) o;
        return getNetworkIp() == that.getNetworkIp();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNetworkIp());
    }
}
