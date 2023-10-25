/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;

public class S29PacketSoundEffect
implements Packet<INetHandlerPlayClient> {
    private float soundVolume;
    private String soundName;
    private int posZ;
    private int posX;
    private int soundPitch;
    private int posY = Integer.MAX_VALUE;

    public float getVolume() {
        return this.soundVolume;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.soundName);
        packetBuffer.writeInt(this.posX);
        packetBuffer.writeInt(this.posY);
        packetBuffer.writeInt(this.posZ);
        packetBuffer.writeFloat(this.soundVolume);
        packetBuffer.writeByte(this.soundPitch);
    }

    public float getPitch() {
        return (float)this.soundPitch / 63.0f;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.soundName = packetBuffer.readStringFromBuffer(256);
        this.posX = packetBuffer.readInt();
        this.posY = packetBuffer.readInt();
        this.posZ = packetBuffer.readInt();
        this.soundVolume = packetBuffer.readFloat();
        this.soundPitch = packetBuffer.readUnsignedByte();
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleSoundEffect(this);
    }

    public double getX() {
        return (float)this.posX / 8.0f;
    }

    public String getSoundName() {
        return this.soundName;
    }

    public S29PacketSoundEffect() {
    }

    public S29PacketSoundEffect(String string, double d, double d2, double d3, float f, float f2) {
        Validate.notNull((Object)string, (String)"name", (Object[])new Object[0]);
        this.soundName = string;
        this.posX = (int)(d * 8.0);
        this.posY = (int)(d2 * 8.0);
        this.posZ = (int)(d3 * 8.0);
        this.soundVolume = f;
        this.soundPitch = (int)(f2 * 63.0f);
        f2 = MathHelper.clamp_float(f2, 0.0f, 255.0f);
    }

    public double getY() {
        return (float)this.posY / 8.0f;
    }

    public double getZ() {
        return (float)this.posZ / 8.0f;
    }
}

