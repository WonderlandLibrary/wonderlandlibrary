/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.client;

import com.mojang.realmsclient.dto.RegionPingResult;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ping {
    public static List<RegionPingResult> ping(Region ... regions) {
        for (Region region : regions) {
            Ping.ping(region.endpoint);
        }
        ArrayList<RegionPingResult> results = new ArrayList<RegionPingResult>();
        for (Region region : regions) {
            results.add(new RegionPingResult(region.name, Ping.ping(region.endpoint)));
        }
        Collections.sort(results, new Comparator<RegionPingResult>(){

            @Override
            public int compare(RegionPingResult o1, RegionPingResult o2) {
                return o1.ping() - o2.ping();
            }
        });
        return results;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int ping(String host) {
        int timeout = 700;
        long sum = 0L;
        Socket socket = null;
        for (int i = 0; i < 5; ++i) {
            try {
                InetSocketAddress sockAddr = new InetSocketAddress(host, 80);
                socket = new Socket();
                long t1 = Ping.now();
                socket.connect(sockAddr, 700);
                sum += Ping.now() - t1;
                Ping.close(socket);
                continue;
            }
            catch (Exception ignored) {
                sum += 700L;
                continue;
            }
            finally {
                Ping.close(socket);
            }
        }
        return (int)((double)sum / 5.0);
    }

    private static void close(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    public static List<RegionPingResult> pingAllRegions() {
        return Ping.ping(Region.values());
    }

    static enum Region {
        US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"),
        US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"),
        US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"),
        EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"),
        AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"),
        AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"),
        AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"),
        SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com");

        private final String name;
        private final String endpoint;

        private Region(String name, String endpoint) {
            this.name = name;
            this.endpoint = endpoint;
        }
    }
}

