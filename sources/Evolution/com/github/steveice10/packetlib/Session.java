/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.event.session.SessionEvent;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public interface Session {
    public void connect();

    public void connect(boolean var1);

    public String getHost();

    public int getPort();

    public SocketAddress getLocalAddress();

    public SocketAddress getRemoteAddress();

    public PacketProtocol getPacketProtocol();

    public Map<String, Object> getFlags();

    public boolean hasFlag(String var1);

    public <T> T getFlag(String var1);

    public void setFlag(String var1, Object var2);

    public List<SessionListener> getListeners();

    public void addListener(SessionListener var1);

    public void removeListener(SessionListener var1);

    public void callEvent(SessionEvent var1);

    public int getCompressionThreshold();

    public void setCompressionThreshold(int var1);

    public int getConnectTimeout();

    public void setConnectTimeout(int var1);

    public int getReadTimeout();

    public void setReadTimeout(int var1);

    public int getWriteTimeout();

    public void setWriteTimeout(int var1);

    public boolean isConnected();

    public void send(Packet var1);

    public void disconnect(String var1);

    public void disconnect(String var1, boolean var2);

    public void disconnect(String var1, Throwable var2);

    public void disconnect(String var1, Throwable var2, boolean var3);
}

