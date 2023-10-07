// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import java.util.List;
import net.minecraft.network.Packet;

public class S26PacketMapChunkBulk implements Packet
{
    private int[] field_149266_a;
    private int[] field_149264_b;
    private S21PacketChunkData.Extracted[] field_179755_c;
    private boolean field_149267_h;
    private static final String __OBFID = "CL_00001306";
    
    public S26PacketMapChunkBulk() {
    }
    
    public S26PacketMapChunkBulk(final List p_i45197_1_) {
        final int var2 = p_i45197_1_.size();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_179755_c = new S21PacketChunkData.Extracted[var2];
        this.field_149267_h = !p_i45197_1_.get(0).getWorld().provider.getHasNoSky();
        for (int var3 = 0; var3 < var2; ++var3) {
            final Chunk var4 = p_i45197_1_.get(var3);
            final S21PacketChunkData.Extracted var5 = S21PacketChunkData.func_179756_a(var4, true, this.field_149267_h, 65535);
            this.field_149266_a[var3] = var4.xPosition;
            this.field_149264_b[var3] = var4.zPosition;
            this.field_179755_c[var3] = var5;
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149267_h = data.readBoolean();
        final int var2 = data.readVarIntFromBuffer();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_179755_c = new S21PacketChunkData.Extracted[var2];
        for (int var3 = 0; var3 < var2; ++var3) {
            this.field_149266_a[var3] = data.readInt();
            this.field_149264_b[var3] = data.readInt();
            this.field_179755_c[var3] = new S21PacketChunkData.Extracted();
            this.field_179755_c[var3].field_150280_b = (data.readShort() & 0xFFFF);
            this.field_179755_c[var3].field_150282_a = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(this.field_179755_c[var3].field_150280_b), this.field_149267_h, true)];
        }
        for (int var3 = 0; var3 < var2; ++var3) {
            data.readBytes(this.field_179755_c[var3].field_150282_a);
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBoolean(this.field_149267_h);
        data.writeVarIntToBuffer(this.field_179755_c.length);
        for (int var2 = 0; var2 < this.field_149266_a.length; ++var2) {
            data.writeInt(this.field_149266_a[var2]);
            data.writeInt(this.field_149264_b[var2]);
            data.writeShort((short)(this.field_179755_c[var2].field_150280_b & 0xFFFF));
        }
        for (int var2 = 0; var2 < this.field_149266_a.length; ++var2) {
            data.writeBytes(this.field_179755_c[var2].field_150282_a);
        }
    }
    
    public void func_180738_a(final INetHandlerPlayClient p_180738_1_) {
        p_180738_1_.handleMapChunkBulk(this);
    }
    
    public int func_149255_a(final int p_149255_1_) {
        return this.field_149266_a[p_149255_1_];
    }
    
    public int func_149253_b(final int p_149253_1_) {
        return this.field_149264_b[p_149253_1_];
    }
    
    public int func_149254_d() {
        return this.field_149266_a.length;
    }
    
    public byte[] func_149256_c(final int p_149256_1_) {
        return this.field_179755_c[p_149256_1_].field_150282_a;
    }
    
    public int func_179754_d(final int p_179754_1_) {
        return this.field_179755_c[p_179754_1_].field_150280_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180738_a((INetHandlerPlayClient)handler);
    }
}
