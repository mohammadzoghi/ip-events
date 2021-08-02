package com.mohammad.ipevents.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppEvents {

    private int count;

    @JsonProperty("good_ips")
    private Set<String> goodIps = new HashSet<>();

    @JsonProperty("bad_ips")
    private Set<String> badIps = new HashSet<>();

}
