/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventType;
import me.Tengoku.Terror.event.events.EventUseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class C02PacketUseEntity
implements Packet<INetHandlerPlayServer> {
    private Action action;
    private int entityId;
    private Vec3 hitVec;

    public C02PacketUseEntity() {
    }

    public C02PacketUseEntity(Entity entity, Vec3 vec3) {
        this(entity, Action.INTERACT_AT);
        this.hitVec = vec3;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeEnumValue(this.action);
        if (this.action == Action.INTERACT_AT) {
            packetBuffer.writeFloat((float)this.hitVec.xCoord);
            packetBuffer.writeFloat((float)this.hitVec.yCoord);
            packetBuffer.writeFloat((float)this.hitVec.zCoord);
        }
    }

    public Action getAction() {
        return this.action;
    }

    public C02PacketUseEntity(Entity entity, Action action) {
        EventUseEntity eventUseEntity = new EventUseEntity(entity, entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch, entity.onGround);
        Exodus.onEvent(eventUseEntity);
        eventUseEntity.setType(EventType.PRE);
        eventUseEntity.call();
        entity.posX = EventUseEntity.getX();
        entity.posY = EventUseEntity.getY();
        entity.posZ = EventUseEntity.getZ();
        entity.rotationYaw = EventUseEntity.getYaw();
        entity.rotationPitch = EventUseEntity.getPitch();
        entity.onGround = eventUseEntity.isOnGround();
        this.entityId = entity.getEntityId();
        this.action = action;
        eventUseEntity.setType(EventType.POST);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.action = packetBuffer.readEnumValue(Action.class);
        if (this.action == Action.INTERACT_AT) {
            this.hitVec = new Vec3(packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat());
        }
    }

    public Entity getEntityFromWorld(World world) {
        return world.getEntityByID(this.entityId);
    }

    public Vec3 getHitVec() {
        return this.hitVec;
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processUseEntity(this);
    }

    public static enum Action {
        INTERACT,
        ATTACK,
        INTERACT_AT;

    }
}

