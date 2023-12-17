// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntityEffect implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte flags;
    
    public SPacketEntityEffect() {
    }
    
    public SPacketEntityEffect(final int entityIdIn, final PotionEffect effect) {
        this.entityId = entityIdIn;
        this.effectId = (byte)(Potion.getIdFromPotion(effect.getPotion()) & 0xFF);
        this.amplifier = (byte)(effect.getAmplifier() & 0xFF);
        if (effect.getDuration() > 32767) {
            this.duration = 32767;
        }
        else {
            this.duration = effect.getDuration();
        }
        this.flags = 0;
        if (effect.getIsAmbient()) {
            this.flags |= 0x1;
        }
        if (effect.doesShowParticles()) {
            this.flags |= 0x2;
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        this.effectId = buf.readByte();
        this.amplifier = buf.readByte();
        this.duration = buf.readVarInt();
        this.flags = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        buf.writeByte(this.effectId);
        buf.writeByte(this.amplifier);
        buf.writeVarInt(this.duration);
        buf.writeByte(this.flags);
    }
    
    public boolean isMaxDuration() {
        return this.duration == 32767;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
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
    
    public boolean doesShowParticles() {
        return (this.flags & 0x2) == 0x2;
    }
    
    public boolean getIsAmbient() {
        return (this.flags & 0x1) == 0x1;
    }
}
