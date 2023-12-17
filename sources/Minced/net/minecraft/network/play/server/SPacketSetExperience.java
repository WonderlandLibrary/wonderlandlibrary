// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSetExperience implements Packet<INetHandlerPlayClient>
{
    private float experienceBar;
    private int totalExperience;
    private int level;
    
    public SPacketSetExperience() {
    }
    
    public SPacketSetExperience(final float experienceBarIn, final int totalExperienceIn, final int levelIn) {
        this.experienceBar = experienceBarIn;
        this.totalExperience = totalExperienceIn;
        this.level = levelIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.experienceBar = buf.readFloat();
        this.level = buf.readVarInt();
        this.totalExperience = buf.readVarInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeFloat(this.experienceBar);
        buf.writeVarInt(this.level);
        buf.writeVarInt(this.totalExperience);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetExperience(this);
    }
    
    public float getExperienceBar() {
        return this.experienceBar;
    }
    
    public int getTotalExperience() {
        return this.totalExperience;
    }
    
    public int getLevel() {
        return this.level;
    }
}
