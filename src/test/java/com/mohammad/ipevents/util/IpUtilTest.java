package com.mohammad.ipevents.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.env.MockEnvironment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the IpUtil class.
 */
public class IpUtilTest {

    private static final long _54_45_72_142 = 908937358L;
    private static final long _54_45_72_131 = 908937347L;
    private static final long _21_101_201_155 = 358992283L;

    private static final long _54_45_72_128 = 908937344L;
    private static final long _21_101_201_144 = 358992272L;

    private static IpUtil ipUtil;

    @BeforeAll
    static void createIpUtil(){
        MockEnvironment env = new MockEnvironment();
        env.setProperty("ip-events.mask", "28");
        ipUtil = new IpUtil(env);
    }

    @Test
    void convertToStringIpsTest(){
        Set<Long> ips = new HashSet<>();
        ips.add(_54_45_72_131);
        ips.add(_54_45_72_142);
        ips.add(_21_101_201_155);
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
                Arguments.of(_54_45_72_131, _54_45_72_128),
                Arguments.of(_54_45_72_142 , _54_45_72_128),
                Arguments.of(_21_101_201_155, _21_101_201_144)
        );
    }
}
