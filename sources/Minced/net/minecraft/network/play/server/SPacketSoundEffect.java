// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSoundEffect implements Packet<INetHandlerPlayClient>
{
    private SoundEvent sound;
    private SoundCategory category;
    private int posX;
    private int posY;
    private int posZ;
    private float soundVolume;
    private float soundPitch;
    
    public SPacketSoundEffect() {
    }
    
    public SPacketSoundEffect(final SoundEvent soundIn, final SoundCategory categoryIn, final double xIn, final double yIn, final double zIn, final float volumeIn, final float pitchIn) {
        Validate.notNull((Object)soundIn, "sound", new Object[0]);
        this.sound = soundIn;
        this.category = categoryIn;
        this.posX = (int)(xIn * 8.0);
        this.posY = (int)(yIn * 8.0);
        this.posZ = (int)(zIn * 8.0);
        this.soundVolume = volumeIn;
        this.soundPitch = pitchIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.sound = SoundEvent.REGISTRY.getObjectById(buf.readVarInt());
        this.category = buf.readEnumValue(SoundCategory.class);
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.soundVolume = buf.readFloat();
        this.soundPitch = buf.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(SoundEvent.REGISTRY.getIDForObject(this.sound));
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
        return this.posX / 8.0f;
    }
    
    public double getY() {
        return this.posY / 8.0f;
    }
    
    public double getZ() {
        return this.posZ / 8.0f;
    }
    
    public float getVolume() {
        return this.soundVolume;
    }
    
    public float getPitch() {
        return this.soundPitch;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSoundEffect(this);
    }
}
