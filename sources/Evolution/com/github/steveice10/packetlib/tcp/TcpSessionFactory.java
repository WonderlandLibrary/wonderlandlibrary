/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.ConnectionListener;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.SessionFactory;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import com.github.steveice10.packetlib.tcp.TcpConnectionListener;
import java.net.Proxy;

public class TcpSessionFactory
implements SessionFactory {
    private Proxy clientProxy;

    public TcpSessionFactory() {
    }

    public TcpSessionFactory(Proxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public Session createClientSession(Client client) {
        return new TcpClientSession(client.getHost(), client.getPort(), client.getPacketProtocol(), client, this.clientProxy);
    }

    @Override
    public ConnectionListener createServerListener(Server server) {
        return new TcpConnectionListener(server.getHost(), server.getPort(), server);
    }
}

