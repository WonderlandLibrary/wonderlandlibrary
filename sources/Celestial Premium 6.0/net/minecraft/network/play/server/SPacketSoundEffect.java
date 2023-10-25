/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.apache.commons.lang3.Validate;

public class SPacketSoundEffect
implements Packet<INetHandlerPlayClient> {
    private SoundEvent sound;
    private SoundCategory category;
    private int posX;
    private int posY;
    private int posZ;
    private float soundVolume;
    private float soundPitch;

    public SPacketSoundEffect() {
    }

    public SPacketSoundEffect(SoundEvent soundIn, SoundCategory categoryIn, double xIn, double yIn, double zIn, float volumeIn, float pitchIn) {
        Validate.notNull(soundIn, "sound", new Object[0]);
        this.sound = soundIn;
        this.category = categoryIn;
        this.posX = (int)(xIn * 8.0);
        this.posY = (int)(yIn * 8.0);
        this.posZ = (int)(zIn * 8.0);
        this.soundVolume = volumeIn;
        this.soundPitch = pitchIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.sound = SoundEvent.REGISTRY.getObjectById(buf.readVarIntFromBuffer());
        this.category = buf.readEnumValue(SoundCategory.class);
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.soundVolume = buf.readFloat();
        this.soundPitch = buf.readFloat();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(SoundEvent.REGISTRY.getIDForObject(this.sound));
        buf.writeEnumValue(this.category);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeFloat(this.soundVolume);
        buf.writeFloat(this.soundPitch);
    }

    public SoundEvent getSound() {
        return this.sound;
    }

    public SoundCategory getCategory() {
        return this.category;
    }

    public double getX() {
        return (float)this.posX / 8.0f;
    }

    public double getY() {
        return (float)this.posY / 8.0f;
    }

    public double getZ() {
        return (float)this.posZ / 8.0f;
    }

    public float getVolume() {
        return this.soundVolume;
    }

    public float getPitch() {
        return this.soundPitch;
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSoundEffect(this);
    }
}

