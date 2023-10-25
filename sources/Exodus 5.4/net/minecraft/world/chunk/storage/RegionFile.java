/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import net.minecraft.server.MinecraftServer;

public class RegionFile {
    private long lastModified;
    private int sizeDelta;
    private final int[] chunkTimestamps;
    private static final byte[] emptySector = new byte[4096];
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int[] offsets = new int[1024];
    private List<Boolean> sectorFree;

    private void setOffset(int n, int n2, int n3) throws IOException {
        this.offsets[n + n2 * 32] = n3;
        this.dataFile.seek((n + n2 * 32) * 4);
        this.dataFile.writeInt(n3);
    }

    private void setChunkTimestamp(int n, int n2, int n3) throws IOException {
        this.chunkTimestamps[n + n2 * 32] = n3;
        this.dataFile.seek(4096 + (n + n2 * 32) * 4);
        this.dataFile.writeInt(n3);
    }

    protected synchronized void write(int n, int n2, byte[] byArray, int n3) {
        try {
            int n4 = this.getOffset(n, n2);
            int n5 = n4 >> 8;
            int n6 = n4 & 0xFF;
            int n7 = (n3 + 5) / 4096 + 1;
            if (n7 >= 256) {
                return;
            }
            if (n5 != 0 && n6 == n7) {
                this.write(n5, byArray, n3);
            } else {
                int n8;
                int n9 = 0;
                while (n9 < n6) {
                    this.sectorFree.set(n5 + n9, true);
                    ++n9;
                }
                n9 = this.sectorFree.indexOf(true);
                int n10 = 0;
                if (n9 != -1) {
                    n8 = n9;
                    while (n8 < this.sectorFree.size()) {
                        if (n10 != 0) {
                            n10 = this.sectorFree.get(n8).booleanValue() ? ++n10 : 0;
                        } else if (this.sectorFree.get(n8).booleanValue()) {
                            n9 = n8;
                            n10 = 1;
                        }
                        if (n10 >= n7) break;
                        ++n8;
                    }
                }
                if (n10 >= n7) {
                    n5 = n9;
                    this.setOffset(n, n2, n9 << 8 | n7);
                    n8 = 0;
                    while (n8 < n7) {
                        this.sectorFree.set(n5 + n8, false);
                        ++n8;
                    }
                    this.write(n5, byArray, n3);
                } else {
                    this.dataFile.seek(this.dataFile.length());
                    n5 = this.sectorFree.size();
                    n8 = 0;
                    while (n8 < n7) {
                        this.dataFile.write(emptySector);
                        this.sectorFree.add(false);
                        ++n8;
                    }
                    this.sizeDelta += 4096 * n7;
                    this.write(n5, byArray, n3);
                    this.setOffset(n, n2, n5 << 8 | n7);
                }
            }
            this.setChunkTimestamp(n, n2, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private int getOffset(int n, int n2) {
        return this.offsets[n + n2 * 32];
    }

    public RegionFile(File file) {
        this.chunkTimestamps = new int[1024];
        this.fileName = file;
        this.sizeDelta = 0;
        try {
            int n;
            int n2;
            if (file.exists()) {
                this.lastModified = file.lastModified();
            }
            this.dataFile = new RandomAccessFile(file, "rw");
            if (this.dataFile.length() < 4096L) {
                n2 = 0;
                while (n2 < 1024) {
                    this.dataFile.writeInt(0);
                    ++n2;
                }
                n2 = 0;
                while (n2 < 1024) {
                    this.dataFile.writeInt(0);
                    ++n2;
                }
                this.sizeDelta += 8192;
            }
            if ((this.dataFile.length() & 0xFFFL) != 0L) {
                n2 = 0;
                while ((long)n2 < (this.dataFile.length() & 0xFFFL)) {
                    this.dataFile.write(0);
                    ++n2;
                }
            }
            n2 = (int)this.dataFile.length() / 4096;
            this.sectorFree = Lists.newArrayListWithCapacity((int)n2);
            int n3 = 0;
            while (n3 < n2) {
                this.sectorFree.add(true);
                ++n3;
            }
            this.sectorFree.set(0, false);
            this.sectorFree.set(1, false);
            this.dataFile.seek(0L);
            n3 = 0;
            while (n3 < 1024) {
                this.offsets[n3] = n = this.dataFile.readInt();
                if (n != 0 && (n >> 8) + (n & 0xFF) <= this.sectorFree.size()) {
                    int n4 = 0;
                    while (n4 < (n & 0xFF)) {
                        this.sectorFree.set((n >> 8) + n4, false);
                        ++n4;
                    }
                }
                ++n3;
            }
            n3 = 0;
            while (n3 < 1024) {
                this.chunkTimestamps[n3] = n = this.dataFile.readInt();
                ++n3;
            }
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private boolean outOfBounds(int n, int n2) {
        return n < 0 || n >= 32 || n2 < 0 || n2 >= 32;
    }

    public synchronized DataInputStream getChunkDataInputStream(int n, int n2) {
        int n3;
        block12: {
            block11: {
                int n4;
                int n5;
                block10: {
                    int n6;
                    block9: {
                        if (this.outOfBounds(n, n2)) {
                            return null;
                        }
                        try {
                            n6 = this.getOffset(n, n2);
                            if (n6 != 0) break block9;
                            return null;
                        }
                        catch (IOException iOException) {
                            return null;
                        }
                    }
                    n5 = n6 >> 8;
                    n4 = n6 & 0xFF;
                    if (n5 + n4 <= this.sectorFree.size()) break block10;
                    return null;
                }
                this.dataFile.seek(n5 * 4096);
                n3 = this.dataFile.readInt();
                if (n3 <= 4096 * n4) break block11;
                return null;
            }
            if (n3 > 0) break block12;
            return null;
        }
        byte by = this.dataFile.readByte();
        if (by == 1) {
            byte[] byArray = new byte[n3 - 1];
            this.dataFile.read(byArray);
            return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(byArray))));
        }
        if (by == 2) {
            byte[] byArray = new byte[n3 - 1];
            this.dataFile.read(byArray);
            return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(byArray))));
        }
        return null;
    }

    public DataOutputStream getChunkDataOutputStream(int n, int n2) {
        return this.outOfBounds(n, n2) ? null : new DataOutputStream(new DeflaterOutputStream(new ChunkBuffer(n, n2)));
    }

    private void write(int n, byte[] byArray, int n2) throws IOException {
        this.dataFile.seek(n * 4096);
        this.dataFile.writeInt(n2 + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(byArray, 0, n2);
    }

    public void close() throws IOException {
        if (this.dataFile != null) {
            this.dataFile.close();
        }
    }

    public boolean isChunkSaved(int n, int n2) {
        return this.getOffset(n, n2) != 0;
    }

    class ChunkBuffer
    extends ByteArrayOutputStream {
        private int chunkZ;
        private int chunkX;

        @Override
        public void close() throws IOException {
            RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
        }

        public ChunkBuffer(int n, int n2) {
            super(8096);
            this.chunkX = n;
            this.chunkZ = n2;
        }
    }
}

