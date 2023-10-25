/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketHeader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public abstract class PacketProtocol {
    private final Map<Integer, Class<? extends Packet>> incoming = new HashMap<Integer, Class<? extends Packet>>();
    private final Map<Class<? extends Packet>, Integer> outgoing = new HashMap<Class<? extends Packet>, Integer>();

    public abstract String getSRVRecordPrefix();

    public abstract PacketHeader getPacketHeader();

    public abstract PacketEncryption getEncryption();

    public abstract void newClientSession(Client var1, Session var2);

    public abstract void newServerSession(Server var1, Session var2);

    public final void clearPackets() {
        this.incoming.clear();
        this.outgoing.clear();
    }

    public final void register(int id, Class<? extends Packet> packet) {
        this.registerIncoming(id, packet);
        this.registerOutgoing(id, packet);
    }

    public final void registerIncoming(int id, Class<? extends Packet> packet) {
        this.incoming.put(id, packet);
        try {
            this.createIncomingPacket(id);
        }
        catch (IllegalStateException e) {
            this.incoming.remove(id);
            throw new IllegalArgumentException(e.getMessage(), e.getCause());
        }
    }

    public final void registerOutgoing(int id, Class<? extends Packet> packet) {
        this.outgoing.put(packet, id);
    }

    public final Packet createIncomingPacket(int id) {
        if (id < 0 || !this.incoming.containsKey(id) || this.incoming.get(id) == null) {
            throw new IllegalArgumentException("Invalid packet id: " + id);
        }
        Class<? extends Packet> packet = this.incoming.get(id);
        try {
            Constructor<? extends Packet> constructor = packet.getDeclaredConstructor(new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodError e) {
            throw new IllegalStateException("Packet \"" + id + ", " + packet.getName() + "\" does not have a no-params constructor for instantiation.");
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + id + ", " + packet.getName() + "\".", e);
        }
    }

    public final int getOutgoingId(Class<? extends Packet> packet) {
        if (!this.outgoing.containsKey(packet) || this.outgoing.get(packet) == null) {
            throw new IllegalArgumentException("Unregistered outgoing packet class: " + packet.getName());
        }
        return this.outgoing.get(packet);
    }
}

