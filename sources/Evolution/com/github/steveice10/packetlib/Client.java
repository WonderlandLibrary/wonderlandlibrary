/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.SessionFactory;
import com.github.steveice10.packetlib.packet.PacketProtocol;

public class Client {
    private String host;
    private int port;
    private PacketProtocol protocol;
    private Session session;

    public Client(String host, int port, PacketProtocol protocol, SessionFactory factory) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.session = factory.createClientSession(this);
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public PacketProtocol getPacketProtocol() {
        return this.protocol;
    }

    public Session getSession() {
        return this.session;
    }
}

