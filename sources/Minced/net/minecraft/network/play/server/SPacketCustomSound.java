// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient>
{
    private String soundName;
    private SoundCategory category;
    private int x;
    private int y;
    private int z;
    private float volume;
    private float pitch;
    
    public SPacketCustomSound() {
        this.y = Integer.MAX_VALUE;
    }
    
    public SPacketCustomSound(final String soundNameIn, final SoundCategory categoryIn, final double xIn, final double yIn, final double zIn, final float volumeIn, final float pitchIn) {
        this.y = Integer.MAX_VALUE;
        Validate.notNull((Object)soundNameIn, "name", new Object[0]);
        this.soundName = soundNameIn;
        this.category = categoryIn;
        this.x = (int)(xIn * 8.0);
        this.y = (int)(yIn * 8.0);
        this.z = (int)(zIn * 8.0);
        this.volume = volumeIn;
        this.pitch = pitchIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.soundName = buf.readString(256);
        this.category = buf.readEnumValue(SoundCategory.class);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
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
        return this.x / 8.0f;
    }
    
    public double getY() {
        return this.y / 8.0f;
    }
    
    public double getZ() {
        return this.z / 8.0f;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCustomSound(this);
    }
}
