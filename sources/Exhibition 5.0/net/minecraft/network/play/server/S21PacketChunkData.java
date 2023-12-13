// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import com.google.common.collect.Lists;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.Packet;

public class S21PacketChunkData implements Packet
{
    private int field_149284_a;
    private int field_149282_b;
    private Extracted field_179758_c;
    private boolean field_149279_g;
    private static final String __OBFID = "CL_00001304";
    
    public S21PacketChunkData() {
    }
    
    public S21PacketChunkData(final Chunk p_i45196_1_, final boolean p_i45196_2_, final int p_i45196_3_) {
        this.field_149284_a = p_i45196_1_.xPosition;
        this.field_149282_b = p_i45196_1_.zPosition;
        this.field_149279_g = p_i45196_2_;
        this.field_179758_c = func_179756_a(p_i45196_1_, p_i45196_2_, !p_i45196_1_.getWorld().provider.getHasNoSky(), p_i45196_3_);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149284_a = data.readInt();
        this.field_149282_b = data.readInt();
        this.field_149279_g = data.readBoolean();
        this.field_179758_c = new Extracted();
        this.field_179758_c.field_150280_b = data.readShort();
        this.field_179758_c.field_150282_a = data.readByteArray();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeInt(this.field_149284_a);
        data.writeInt(this.field_149282_b);
        data.writeBoolean(this.field_149279_g);
        data.writeShort((short)(this.field_179758_c.field_150280_b & 0xFFFF));
        data.writeByteArray(this.field_179758_c.field_150282_a);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChunkData(this);
    }
    
    public byte[] func_149272_d() {
        return this.field_179758_c.field_150282_a;
    }
    
    protected static int func_180737_a(final int p_180737_0_, final boolean p_180737_1_, final boolean p_180737_2_) {
        final int var3 = p_180737_0_ * 2 * 16 * 16 * 16;
        final int var4 = p_180737_0_ * 16 * 16 * 16 / 2;
        final int var5 = p_180737_1_ ? (p_180737_0_ * 16 * 16 * 16 / 2) : 0;
        final int var6 = p_180737_2_ ? 256 : 0;
        return var3 + var4 + var5 + var6;
    }
    
    public static Extracted func_179756_a(final Chunk p_179756_0_, final boolean p_179756_1_, final boolean p_179756_2_, final int p_179756_3_) {
        final ExtendedBlockStorage[] var4 = p_179756_0_.getBlockStorageArray();
        final Extracted var5 = new Extracted();
        final ArrayList var6 = Lists.newArrayList();
        for (int var7 = 0; var7 < var4.length; ++var7) {
            final ExtendedBlockStorage var8 = var4[var7];
            if (var8 != null && (!p_179756_1_ || !var8.isEmpty()) && (p_179756_3_ & 1 << var7) != 0x0) {
                final Extracted extracted = var5;
                extracted.field_150280_b |= 1 << var7;
                var6.add(var8);
            }
        }
        var5.field_150282_a = new byte[func_180737_a(Integer.bitCount(var5.field_150280_b), p_179756_2_, p_179756_1_)];
        int var7 = 0;
        for (final ExtendedBlockStorage var10 : var6) {
            final char[] var12;
            final char[] var11 = var12 = var10.getData();
            for (int var13 = var11.length, var14 = 0; var14 < var13; ++var14) {
                final char var15 = var12[var14];
                var5.field_150282_a[var7++] = (byte)(var15 & '\u00ff');
                var5.field_150282_a[var7++] = (byte)(var15 >> 8 & '\u00ff');
            }
        }
        for (final ExtendedBlockStorage var10 : var6) {
            var7 = func_179757_a(var10.getBlocklightArray().getData(), var5.field_150282_a, var7);
        }
        if (p_179756_2_) {
            for (final ExtendedBlockStorage var10 : var6) {
                var7 = func_179757_a(var10.getSkylightArray().getData(), var5.field_150282_a, var7);
            }
        }
        if (p_179756_1_) {
            func_179757_a(p_179756_0_.getBiomeArray(), var5.field_150282_a, var7);
        }
        return var5;
    }
    
    private static int func_179757_a(final byte[] p_179757_0_, final byte[] p_179757_1_, final int p_179757_2_) {
        System.arraycopy(p_179757_0_, 0, p_179757_1_, p_179757_2_, p_179757_0_.length);
        return p_179757_2_ + p_179757_0_.length;
    }
    
    public int func_149273_e() {
        return this.field_149284_a;
    }
    
    public int func_149271_f() {
        return this.field_149282_b;
    }
    
    public int func_149276_g() {
        return this.field_179758_c.field_150280_b;
    }
    
    public boolean func_149274_i() {
        return this.field_149279_g;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
    
    public static class Extracted
    {
        public byte[] field_150282_a;
        public int field_150280_b;
        private static final String __OBFID = "CL_00001305";
    }
}
