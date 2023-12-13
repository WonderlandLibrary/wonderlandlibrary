package com.ohare.client.event.events.game;

import com.ohare.client.event.Event;

/**
 * @author Xen for OhareWare
 * @since 7/29/2019
 **/
public class ServerJoinEvent extends Event {
    private String ip;
    private int port;

    public ServerJoinEvent(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
