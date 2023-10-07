// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.Packet;

public class S29PacketSoundEffect implements Packet
{
    private String name;
    private int x;
    private int y;
    private int z;
    private float field_149216_e;
    private int field_149214_f;
    private static final String __OBFID = "CL_00001309";
    
    public S29PacketSoundEffect() {
        this.y = Integer.MAX_VALUE;
    }
    
    public S29PacketSoundEffect(final String name, final double x, final double y, final double z, final float p_i45200_8_, float p_i45200_9_) {
        this.y = Integer.MAX_VALUE;
        Validate.notNull((Object)name, "name", new Object[0]);
        this.name = name;
        this.x = (int)(x * 8.0);
        this.y = (int)(y * 8.0);
        this.z = (int)(z * 8.0);
        this.field_149216_e = p_i45200_8_;
        this.field_149214_f = (int)(p_i45200_9_ * 63.0f);
        p_i45200_9_ = MathHelper.clamp_float(p_i45200_9_, 0.0f, 255.0f);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.name = data.readStringFromBuffer(256);
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.field_149216_e = data.readFloat();
        this.field_149214_f = data.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.name);
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.z);
        data.writeFloat(this.field_149216_e);
        data.writeByte(this.field_149214_f);
    }
    
    public String getName() {
        return this.name;
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
    
    public float func_149208_g() {
        return this.field_149216_e;
    }
    
    public float func_149209_h() {
        return this.field_149214_f / 63.0f;
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSoundEffect(this);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
