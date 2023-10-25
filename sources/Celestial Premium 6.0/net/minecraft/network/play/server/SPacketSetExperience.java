/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetExperience
implements Packet<INetHandlerPlayClient> {
    private float experienceBar;
    private int totalExperience;
    private int level;

    public SPacketSetExperience() {
    }

    public SPacketSetExperience(float experienceBarIn, int totalExperienceIn, int levelIn) {
        this.experienceBar = experienceBarIn;
        this.totalExperience = totalExperienceIn;
        this.level = levelIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.experienceBar = buf.readFloat();
        this.level = buf.readVarIntFromBuffer();
        this.totalExperience = buf.readVarIntFromBuffer();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeFloat(this.experienceBar);
        buf.writeVarIntToBuffer(this.level);
        buf.writeVarIntToBuffer(this.totalExperience);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
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

