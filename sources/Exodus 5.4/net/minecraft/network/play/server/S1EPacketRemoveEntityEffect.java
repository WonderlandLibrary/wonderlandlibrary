/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

public class S1EPacketRemoveEntityEffect
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int effectId;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleRemoveEntityEffect(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.effectId = packetBuffer.readUnsignedByte();
    }

    public S1EPacketRemoveEntityEffect(int n, PotionEffect potionEffect) {
        this.entityId = n;
        this.effectId = potionEffect.getPotionID();
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeByte(this.effectId);
    }

    public S1EPacketRemoveEntityEffect() {
    }

    public int getEffectId() {
        return this.effectId;
    }
}

