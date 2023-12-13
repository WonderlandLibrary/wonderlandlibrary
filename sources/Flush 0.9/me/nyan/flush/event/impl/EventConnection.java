package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventConnection extends Event {
    private final String ip;
    private final int port;

    public EventConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
