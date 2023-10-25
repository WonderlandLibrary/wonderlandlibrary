/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0BPacketEntityAction
implements Packet<INetHandlerPlayServer> {
    private int entityID;
    private Action action;
    private int auxData;

    public int getAuxData() {
        return this.auxData;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityID);
        packetBuffer.writeEnumValue(this.action);
        packetBuffer.writeVarIntToBuffer(this.auxData);
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processEntityAction(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityID = packetBuffer.readVarIntFromBuffer();
        this.action = packetBuffer.readEnumValue(Action.class);
        this.auxData = packetBuffer.readVarIntFromBuffer();
    }

    public C0BPacketEntityAction(Entity entity, Action action) {
        this(entity, action, 0);
    }

    public C0BPacketEntityAction() {
    }

    public Action getAction() {
        return this.action;
    }

    public C0BPacketEntityAction(Entity entity, Action action, int n) {
        this.entityID = entity.getEntityId();
        this.action = action;
        this.auxData = n;
    }

    public static enum Action {
        START_SNEAKING,
        STOP_SNEAKING,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        RIDING_JUMP,
        OPEN_INVENTORY;

    }
}

