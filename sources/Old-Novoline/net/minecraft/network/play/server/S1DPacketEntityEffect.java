package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

import java.io.IOException;

public class S1DPacketEntityEffect implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte hideParticles;
    private PotionEffect effect;

    public S1DPacketEntityEffect() {
    }

    public S1DPacketEntityEffect(int entityIdIn, PotionEffect effect) {
        this.effect = effect;
        this.entityId = entityIdIn;
        this.effectId = (byte) (effect.getPotionID() & 255);
        this.amplifier = (byte) (effect.getAmplifier() & 255);
        this.duration = Math.min(effect.getDuration(), Short.MAX_VALUE);
        this.hideParticles = (byte) (effect.getIsShowParticles() ? 1 : 0);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.effectId = buf.readByte();
        this.amplifier = buf.readByte();
        this.duration = buf.readVarIntFromBuffer();
        this.hideParticles = buf.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.effectId);
        buf.writeByte(this.amplifier);
        buf.writeVarIntToBuffer(this.duration);
        buf.writeByte(this.hideParticles);
    }

    public boolean func_149429_c() {
        return this.duration == 32767;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityEffect(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public byte getEffectId() {
        return this.effectId;
    }

    public byte getAmplifier() {
        return this.amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean func_179707_f() {
        return this.hideParticles != 0;
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setEffectId(byte effectId) {
        this.effectId = effectId;
    }

    public void setAmplifier(byte amplifier) {
        this.amplifier = amplifier;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setHideParticles(byte hideParticles) {
        this.hideParticles = hideParticles;
    }

    public void setEffect(PotionEffect effect) {
        this.effect = effect;
    }
}
