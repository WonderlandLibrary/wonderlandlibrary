/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.Validate;

public class SPacketCustomSound
implements Packet<INetHandlerPlayClient> {
    private String soundName;
    private SoundCategory category;
    private int x;
    private int y = Integer.MAX_VALUE;
    private int z;
    private float volume;
    private float pitch;

    public SPacketCustomSound() {
    }

    public SPacketCustomSound(String soundNameIn, SoundCategory categoryIn, double xIn, double yIn, double zIn, float volumeIn, float pitchIn) {
        Validate.notNull(soundNameIn, "name", new Object[0]);
        this.soundName = soundNameIn;
        this.category = categoryIn;
        this.x = (int)(xIn * 8.0);
        this.y = (int)(yIn * 8.0);
        this.z = (int)(zIn * 8.0);
        this.volume = volumeIn;
        this.pitch = pitchIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.soundName = buf.readStringFromBuffer(256);
        this.category = buf.readEnumValue(SoundCategory.class);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(this.soundName);
        buf.writeEnumValue(this.category);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    public String getSoundName() {
        return this.soundName;
    }

    public SoundCategory getCategory() {
        return this.category;
    }

    public double getX() {
        return (float)this.x / 8.0f;
    }

    public double getY() {
        return (float)this.y / 8.0f;
    }

    public double getZ() {
        return (float)this.z / 8.0f;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleCustomSound(this);
    }
}

