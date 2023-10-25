/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib;

import com.github.steveice10.packetlib.ConnectionListener;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.SessionFactory;
import com.github.steveice10.packetlib.event.server.ServerBoundEvent;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.ServerClosingEvent;
import com.github.steveice10.packetlib.event.server.ServerEvent;
import com.github.steveice10.packetlib.event.server.ServerListener;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private String host;
    private int port;
    private Class<? extends PacketProtocol> protocol;
    private SessionFactory factory;
    private ConnectionListener listener;
    private List<Session> sessions = new ArrayList<Session>();
    private Map<String, Object> flags = new HashMap<String, Object>();
    private List<ServerListener> listeners = new ArrayList<ServerListener>();

    public Server(String host, int port, Class<? extends PacketProtocol> protocol, SessionFactory factory) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.factory = factory;
    }

    public Server bind() {
        return this.bind(true);
    }

    public Server bind(boolean wait) {
        this.listener = this.factory.createServerListener(this);
        this.listener.bind(wait, new Runnable(){

            @Override
            public void run() {
                Server.this.callEvent(new ServerBoundEvent(Server.this));
            }
        });
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public Class<? extends PacketProtocol> getPacketProtocol() {
        return this.protocol;
    }

    public PacketProtocol createPacketProtocol() {
        try {
            Constructor<? extends PacketProtocol> constructor = this.protocol.getDeclaredConstructor(new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodError e) {
            throw new IllegalStateException("PacketProtocol \"" + this.protocol.getName() + "\" does not have a no-params constructor for instantiation.");
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate PacketProtocol " + this.protocol.getName() + ".", e);
        }
    }

    public Map<String, Object> getGlobalFlags() {
        return new HashMap<String, Object>(this.flags);
    }

    public boolean hasGlobalFlag(String key) {
        return this.flags.containsKey(key);
    }

    public <T> T getGlobalFlag(String key) {
        Object value = this.flags.get(key);
        if (value == null) {
            return null;
        }
        try {
            return (T)value;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
        }
    }

    public void setGlobalFlag(String key, Object value) {
        this.flags.put(key, value);
    }

    public List<ServerListener> getListeners() {
        return new ArrayList<ServerListener>(this.listeners);
    }

    public void addListener(ServerListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ServerListener listener) {
        this.listeners.remove(listener);
    }

    public void callEvent(ServerEvent event) {
        for (ServerListener listener : this.listeners) {
            event.call(listener);
        }
    }

    public List<Session> getSessions() {
        return new ArrayList<Session>(this.sessions);
    }

    public void addSession(Session session) {
        this.sessions.add(session);
        this.callEvent(new SessionAddedEvent(this, session));
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
        if (session.isConnected()) {
            session.disconnect("Connection closed.");
        }
        this.callEvent(new SessionRemovedEvent(this, session));
    }

    public boolean isListening() {
        return this.listener != null && this.listener.isListening();
    }

    public void close() {
        this.close(true);
    }

    public void close(boolean wait) {
        this.callEvent(new ServerClosingEvent(this));
        for (Session session : this.getSessions()) {
            if (!session.isConnected()) continue;
            session.disconnect("Server closed.");
        }
        this.listener.close(wait, new Runnable(){

            @Override
            public void run() {
                Server.this.callEvent(new ServerClosedEvent(Server.this));
            }
        });
    }
}

