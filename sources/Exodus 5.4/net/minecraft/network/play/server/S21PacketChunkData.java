/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class S21PacketChunkData
implements Packet<INetHandlerPlayClient> {
    private int chunkZ;
    private Extracted extractedData;
    private boolean field_149279_g;
    private int chunkX;

    public int getChunkX() {
        return this.chunkX;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.chunkX);
        packetBuffer.writeInt(this.chunkZ);
        packetBuffer.writeBoolean(this.field_149279_g);
        packetBuffer.writeShort((short)(this.extractedData.dataSize & 0xFFFF));
        packetBuffer.writeByteArray(this.extractedData.data);
    }

    public S21PacketChunkData() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleChunkData(this);
    }

    public boolean func_149274_i() {
        return this.field_149279_g;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    private static int func_179757_a(byte[] byArray, byte[] byArray2, int n) {
        System.arraycopy(byArray, 0, byArray2, n, byArray.length);
        return n + byArray.length;
    }

    public byte[] func_149272_d() {
        return this.extractedData.data;
    }

    public S21PacketChunkData(Chunk chunk, boolean bl, int n) {
        this.chunkX = chunk.xPosition;
        this.chunkZ = chunk.zPosition;
        this.field_149279_g = bl;
        this.extractedData = S21PacketChunkData.func_179756_a(chunk, bl, !chunk.getWorld().provider.getHasNoSky(), n);
    }

    protected static int func_180737_a(int n, boolean bl, boolean bl2) {
        int n2 = n * 2 * 16 * 16 * 16;
        int n3 = n * 16 * 16 * 16 / 2;
        int n4 = bl ? n * 16 * 16 * 16 / 2 : 0;
        int n5 = bl2 ? 256 : 0;
        return n2 + n3 + n4 + n5;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.chunkX = packetBuffer.readInt();
        this.chunkZ = packetBuffer.readInt();
        this.field_149279_g = packetBuffer.readBoolean();
        this.extractedData = new Extracted();
        this.extractedData.dataSize = packetBuffer.readShort();
        this.extractedData.data = packetBuffer.readByteArray();
    }

    public int getExtractedSize() {
        return this.extractedData.dataSize;
    }

    public static Extracted func_179756_a(Chunk chunk, boolean bl, boolean bl2, int n) {
        ExtendedBlockStorage[] extendedBlockStorageArray = chunk.getBlockStorageArray();
        Extracted extracted = new Extracted();
        ArrayList arrayList = Lists.newArrayList();
        int n2 = 0;
        while (n2 < extendedBlockStorageArray.length) {
            ExtendedBlockStorage extendedBlockStorage = extendedBlockStorageArray[n2];
            if (!(extendedBlockStorage == null || bl && extendedBlockStorage.isEmpty() || (n & 1 << n2) == 0)) {
                extracted.dataSize |= 1 << n2;
                arrayList.add(extendedBlockStorage);
            }
            ++n2;
        }
        extracted.data = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(extracted.dataSize), bl2, bl)];
        n2 = 0;
        for (ExtendedBlockStorage extendedBlockStorage : arrayList) {
            char[] cArray;
            char[] cArray2 = cArray = extendedBlockStorage.getData();
            int n3 = cArray.length;
            int n4 = 0;
            while (n4 < n3) {
                char c = cArray2[n4];
                extracted.data[n2++] = (byte)(c & 0xFF);
                extracted.data[n2++] = (byte)(c >> 8 & 0xFF);
                ++n4;
            }
        }
        for (ExtendedBlockStorage extendedBlockStorage : arrayList) {
            n2 = S21PacketChunkData.func_179757_a(extendedBlockStorage.getBlocklightArray().getData(), extracted.data, n2);
        }
        if (bl2) {
            for (ExtendedBlockStorage extendedBlockStorage : arrayList) {
                n2 = S21PacketChunkData.func_179757_a(extendedBlockStorage.getSkylightArray().getData(), extracted.data, n2);
            }
        }
        if (bl) {
            S21PacketChunkData.func_179757_a(chunk.getBiomeArray(), extracted.data, n2);
        }
        return extracted;
    }

    public static class Extracted {
        public int dataSize;
        public byte[] data;
    }
}

