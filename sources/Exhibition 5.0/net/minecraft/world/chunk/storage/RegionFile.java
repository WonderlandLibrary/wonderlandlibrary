// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk.storage;

import java.io.ByteArrayOutputStream;
import net.minecraft.server.MinecraftServer;
import java.util.zip.DeflaterOutputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.util.zip.InflaterInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.RandomAccessFile;
import java.io.File;

public class RegionFile
{
    private static final byte[] emptySector;
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int[] offsets;
    private final int[] chunkTimestamps;
    private List sectorFree;
    private int sizeDelta;
    private long lastModified;
    private static final String __OBFID = "CL_00000381";
    
    public RegionFile(final File p_i2001_1_) {
        this.offsets = new int[1024];
        this.chunkTimestamps = new int[1024];
        this.fileName = p_i2001_1_;
        this.sizeDelta = 0;
        try {
            if (p_i2001_1_.exists()) {
                this.lastModified = p_i2001_1_.lastModified();
            }
            this.dataFile = new RandomAccessFile(p_i2001_1_, "rw");
            if (this.dataFile.length() < 4096L) {
                for (int var2 = 0; var2 < 1024; ++var2) {
                    this.dataFile.writeInt(0);
                }
                for (int var2 = 0; var2 < 1024; ++var2) {
                    this.dataFile.writeInt(0);
                }
                this.sizeDelta += 8192;
            }
            if ((this.dataFile.length() & 0xFFFL) != 0x0L) {
                for (int var2 = 0; var2 < (this.dataFile.length() & 0xFFFL); ++var2) {
                    this.dataFile.write(0);
                }
            }
            int var2 = (int)this.dataFile.length() / 4096;
            this.sectorFree = Lists.newArrayListWithCapacity(var2);
            for (int var3 = 0; var3 < var2; ++var3) {
                this.sectorFree.add(true);
            }
            this.sectorFree.set(0, false);
            this.sectorFree.set(1, false);
            this.dataFile.seek(0L);
            for (int var3 = 0; var3 < 1024; ++var3) {
                final int var4 = this.dataFile.readInt();
                this.offsets[var3] = var4;
                if (var4 != 0 && (var4 >> 8) + (var4 & 0xFF) <= this.sectorFree.size()) {
                    for (int var5 = 0; var5 < (var4 & 0xFF); ++var5) {
                        this.sectorFree.set((var4 >> 8) + var5, false);
                    }
                }
            }
            for (int var3 = 0; var3 < 1024; ++var3) {
                final int var4 = this.dataFile.readInt();
                this.chunkTimestamps[var3] = var4;
            }
        }
        catch (IOException var6) {
            var6.printStackTrace();
        }
    }
    
    public synchronized DataInputStream getChunkDataInputStream(final int p_76704_1_, final int p_76704_2_) {
        if (this.outOfBounds(p_76704_1_, p_76704_2_)) {
            return null;
        }
        try {
            final int var3 = this.getOffset(p_76704_1_, p_76704_2_);
            if (var3 == 0) {
                return null;
            }
            final int var4 = var3 >> 8;
            final int var5 = var3 & 0xFF;
            if (var4 + var5 > this.sectorFree.size()) {
                return null;
            }
            this.dataFile.seek(var4 * 4096);
            final int var6 = this.dataFile.readInt();
            if (var6 > 4096 * var5) {
                return null;
            }
            if (var6 <= 0) {
                return null;
            }
            final byte var7 = this.dataFile.readByte();
            if (var7 == 1) {
                final byte[] var8 = new byte[var6 - 1];
                this.dataFile.read(var8);
                return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(var8))));
            }
            if (var7 == 2) {
                final byte[] var8 = new byte[var6 - 1];
                this.dataFile.read(var8);
                return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(var8))));
            }
            return null;
        }
        catch (IOException var9) {
            return null;
        }
    }
    
    public DataOutputStream getChunkDataOutputStream(final int p_76710_1_, final int p_76710_2_) {
        DataOutputStream dataOutputStream;
        if (this.outOfBounds(p_76710_1_, p_76710_2_)) {
            dataOutputStream = null;
        }
        else {
            final DeflaterOutputStream deflaterOutputStream;
            dataOutputStream = new DataOutputStream(deflaterOutputStream);
            deflaterOutputStream = new DeflaterOutputStream(new ChunkBuffer(p_76710_1_, p_76710_2_));
        }
        return dataOutputStream;
    }
    
    protected synchronized void write(final int p_76706_1_, final int p_76706_2_, final byte[] p_76706_3_, final int p_76706_4_) {
        try {
            final int var5 = this.getOffset(p_76706_1_, p_76706_2_);
            int var6 = var5 >> 8;
            final int var7 = var5 & 0xFF;
            final int var8 = (p_76706_4_ + 5) / 4096 + 1;
            if (var8 >= 256) {
                return;
            }
            if (var6 != 0 && var7 == var8) {
                this.write(var6, p_76706_3_, p_76706_4_);
            }
            else {
                for (int var9 = 0; var9 < var7; ++var9) {
                    this.sectorFree.set(var6 + var9, true);
                }
                int var9 = this.sectorFree.indexOf(true);
                int var10 = 0;
                if (var9 != -1) {
                    for (int var11 = var9; var11 < this.sectorFree.size(); ++var11) {
                        if (var10 != 0) {
                            if (this.sectorFree.get(var11)) {
                                ++var10;
                            }
                            else {
                                var10 = 0;
                            }
                        }
                        else if (this.sectorFree.get(var11)) {
                            var9 = var11;
                            var10 = 1;
                        }
                        if (var10 >= var8) {
                            break;
                        }
                    }
                }
                if (var10 >= var8) {
                    var6 = var9;
                    this.setOffset(p_76706_1_, p_76706_2_, var9 << 8 | var8);
                    for (int var11 = 0; var11 < var8; ++var11) {
                        this.sectorFree.set(var6 + var11, false);
                    }
                    this.write(var6, p_76706_3_, p_76706_4_);
                }
                else {
                    this.dataFile.seek(this.dataFile.length());
                    var6 = this.sectorFree.size();
                    for (int var11 = 0; var11 < var8; ++var11) {
                        this.dataFile.write(RegionFile.emptySector);
                        this.sectorFree.add(false);
                    }
                    this.sizeDelta += 4096 * var8;
                    this.write(var6, p_76706_3_, p_76706_4_);
                    this.setOffset(p_76706_1_, p_76706_2_, var6 << 8 | var8);
                }
            }
            this.setChunkTimestamp(p_76706_1_, p_76706_2_, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
        }
        catch (IOException var12) {
            var12.printStackTrace();
        }
    }
    
    private void write(final int p_76712_1_, final byte[] p_76712_2_, final int p_76712_3_) throws IOException {
        this.dataFile.seek(p_76712_1_ * 4096);
        this.dataFile.writeInt(p_76712_3_ + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(p_76712_2_, 0, p_76712_3_);
    }
    
    private boolean outOfBounds(final int p_76705_1_, final int p_76705_2_) {
        return p_76705_1_ < 0 || p_76705_1_ >= 32 || p_76705_2_ < 0 || p_76705_2_ >= 32;
    }
    
    private int getOffset(final int p_76707_1_, final int p_76707_2_) {
        return this.offsets[p_76707_1_ + p_76707_2_ * 32];
    }
    
    public boolean isChunkSaved(final int p_76709_1_, final int p_76709_2_) {
        return this.getOffset(p_76709_1_, p_76709_2_) != 0;
    }
    
    private void setOffset(final int p_76711_1_, final int p_76711_2_, final int p_76711_3_) throws IOException {
        this.offsets[p_76711_1_ + p_76711_2_ * 32] = p_76711_3_;
        this.dataFile.seek((p_76711_1_ + p_76711_2_ * 32) * 4);
        this.dataFile.writeInt(p_76711_3_);
    }
    
    private void setChunkTimestamp(final int p_76713_1_, final int p_76713_2_, final int p_76713_3_) throws IOException {
        this.chunkTimestamps[p_76713_1_ + p_76713_2_ * 32] = p_76713_3_;
        this.dataFile.seek(4096 + (p_76713_1_ + p_76713_2_ * 32) * 4);
        this.dataFile.writeInt(p_76713_3_);
    }
    
    public void close() throws IOException {
        if (this.dataFile != null) {
            this.dataFile.close();
        }
    }
    
    static {
        emptySector = new byte[4096];
    }
    
    class ChunkBuffer extends ByteArrayOutputStream
    {
        private int chunkX;
        private int chunkZ;
        private static final String __OBFID = "CL_00000382";
        
        public ChunkBuffer(final int p_i2000_2_, final int p_i2000_3_) {
            super(8096);
            this.chunkX = p_i2000_2_;
            this.chunkZ = p_i2000_3_;
        }
        
        @Override
        public void close() throws IOException {
            RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
        }
    }
}
