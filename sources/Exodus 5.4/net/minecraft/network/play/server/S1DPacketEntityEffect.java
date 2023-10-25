/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

public class S1DPacketEntityEffect
implements Packet<INetHandlerPlayClient> {
    private int duration;
    private int entityId;
    private byte hideParticles;
    private byte amplifier;
    private byte effectId;

    public int getDuration() {
        return this.duration;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityEffect(this);
    }

    public byte getEffectId() {
        return this.effectId;
    }

    public S1DPacketEntityEffect(int n, PotionEffect potionEffect) {
        this.entityId = n;
        this.effectId = (byte)(potionEffect.getPotionID() & 0xFF);
        this.amplifier = (byte)(potionEffect.getAmplifier() & 0xFF);
        this.duration = potionEffect.getDuration() > Short.MAX_VALUE ? Short.MAX_VALUE : potionEffect.getDuration();
        this.hideParticles = (byte)(potionEffect.getIsShowParticles() ? 1 : 0);
    }

    public boolean func_179707_f() {
        return this.hideParticles != 0;
    }

    public boolean func_149429_c() {
        return this.duration == Short.MAX_VALUE;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public S1DPacketEntityEffect() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeByte(this.effectId);
        packetBuffer.writeByte(this.amplifier);
        packetBuffer.writeVarIntToBuffer(this.duration);
        packetBuffer.writeByte(this.hideParticles);
    }

    public byte getAmplifier() {
        return this.amplifier;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.effectId = packetBuffer.readByte();
        this.amplifier = packetBuffer.readByte();
        this.duration = packetBuffer.readVarIntFromBuffer();
        this.hideParticles = packetBuffer.readByte();
    }
}

