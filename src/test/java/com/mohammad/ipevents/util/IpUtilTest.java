package com.mohammad.ipevents.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the IpUtil class. The expected values are created using reliable tools.
 */
@SpringBootTest
public class IpUtilTest {

    @Autowired
    private IpUtil ipUtil;

    @Test
    void convertToStringIpsTest(){
        Set<Long> ips = new HashSet<>();
        ips.add(908937347L);
        ips.add(908937358L);
        ips.add(358992283L);
        Set<String> ipStrings = ipUtil.convertToStringIps(ips);

        assertEquals(3, ipStrings.size());
        assertTrue(ipStrings.containsAll(Arrays.asList("54.45.72.142", "54.45.72.131", "21.101.201.155")));
    }

    @ParameterizedTest
    @MethodSource("getIpAndNetwork")
    void getNetworkTest(long ip, long net){
        assertEquals(net, ipUtil.getNetwork(ip));
    }

    /**
     * In these Arguments network is retrieved by /28 mask
     */
    static Stream<Arguments> getIpAndNetwork(){
        return Stream.of(
                Arguments.of(908937347L, 908937344L),
                Arguments.of(908937358L, 908937344L),
                Arguments.of(358992283L, 358992272L)
        );
    }
}
