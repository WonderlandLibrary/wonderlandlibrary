// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.storage.MapData;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.Collection;
import net.minecraft.util.Vec4b;
import net.minecraft.network.Packet;

public class S34PacketMaps implements Packet
{
    private int mapId;
    private byte field_179739_b;
    private Vec4b[] field_179740_c;
    private int field_179737_d;
    private int field_179738_e;
    private int field_179735_f;
    private int field_179736_g;
    private byte[] field_179741_h;
    private static final String __OBFID = "CL_00001311";
    
    public S34PacketMaps() {
    }
    
    public S34PacketMaps(final int p_i45975_1_, final byte p_i45975_2_, final Collection p_i45975_3_, final byte[] p_i45975_4_, final int p_i45975_5_, final int p_i45975_6_, final int p_i45975_7_, final int p_i45975_8_) {
        this.mapId = p_i45975_1_;
        this.field_179739_b = p_i45975_2_;
        this.field_179740_c = p_i45975_3_.toArray(new Vec4b[p_i45975_3_.size()]);
        this.field_179737_d = p_i45975_5_;
        this.field_179738_e = p_i45975_6_;
        this.field_179735_f = p_i45975_7_;
        this.field_179736_g = p_i45975_8_;
        this.field_179741_h = new byte[p_i45975_7_ * p_i45975_8_];
        for (int var9 = 0; var9 < p_i45975_7_; ++var9) {
            for (int var10 = 0; var10 < p_i45975_8_; ++var10) {
                this.field_179741_h[var9 + var10 * p_i45975_7_] = p_i45975_4_[p_i45975_5_ + var9 + (p_i45975_6_ + var10) * 128];
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.mapId = data.readVarIntFromBuffer();
        this.field_179739_b = data.readByte();
        this.field_179740_c = new Vec4b[data.readVarIntFromBuffer()];
        for (int var2 = 0; var2 < this.field_179740_c.length; ++var2) {
            final short var3 = data.readByte();
            this.field_179740_c[var2] = new Vec4b((byte)(var3 >> 4 & 0xF), data.readByte(), data.readByte(), (byte)(var3 & 0xF));
        }
        this.field_179735_f = data.readUnsignedByte();
        if (this.field_179735_f > 0) {
            this.field_179736_g = data.readUnsignedByte();
            this.field_179737_d = data.readUnsignedByte();
            this.field_179738_e = data.readUnsignedByte();
            this.field_179741_h = data.readByteArray();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.mapId);
        data.writeByte(this.field_179739_b);
        data.writeVarIntToBuffer(this.field_179740_c.length);
        for (final Vec4b var5 : this.field_179740_c) {
            data.writeByte((var5.func_176110_a() & 0xF) << 4 | (var5.func_176111_d() & 0xF));
            data.writeByte(var5.func_176112_b());
            data.writeByte(var5.func_176113_c());
        }
        data.writeByte(this.field_179735_f);
        if (this.field_179735_f > 0) {
            data.writeByte(this.field_179736_g);
            data.writeByte(this.field_179737_d);
            data.writeByte(this.field_179738_e);
            data.writeByteArray(this.field_179741_h);
        }
    }
    
    public void func_180741_a(final INetHandlerPlayClient p_180741_1_) {
        p_180741_1_.handleMaps(this);
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public void func_179734_a(final MapData p_179734_1_) {
        p_179734_1_.scale = this.field_179739_b;
        p_179734_1_.playersVisibleOnMap.clear();
        for (int var2 = 0; var2 < this.field_179740_c.length; ++var2) {
            final Vec4b var3 = this.field_179740_c[var2];
            p_179734_1_.playersVisibleOnMap.put("icon-" + var2, var3);
        }
        for (int var2 = 0; var2 < this.field_179735_f; ++var2) {
            for (int var4 = 0; var4 < this.field_179736_g; ++var4) {
                p_179734_1_.colors[this.field_179737_d + var2 + (this.field_179738_e + var4) * 128] = this.field_179741_h[var2 + var4 * this.field_179735_f];
            }
        }
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180741_a((INetHandlerPlayClient)handler);
    }
}
