/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.Chunk;

public class S26PacketMapChunkBulk
implements Packet<INetHandlerPlayClient> {
    private int[] xPositions;
    private S21PacketChunkData.Extracted[] chunksData;
    private int[] zPositions;
    private boolean isOverworld;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.isOverworld = packetBuffer.readBoolean();
        int n = packetBuffer.readVarIntFromBuffer();
        this.xPositions = new int[n];
        this.zPositions = new int[n];
        this.chunksData = new S21PacketChunkData.Extracted[n];
        int n2 = 0;
        while (n2 < n) {
            this.xPositions[n2] = packetBuffer.readInt();
            this.zPositions[n2] = packetBuffer.readInt();
            this.chunksData[n2] = new S21PacketChunkData.Extracted();
            this.chunksData[n2].dataSize = packetBuffer.readShort() & 0xFFFF;
            this.chunksData[n2].data = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(this.chunksData[n2].dataSize), this.isOverworld, true)];
            ++n2;
        }
        n2 = 0;
        while (n2 < n) {
            packetBuffer.readBytes(this.chunksData[n2].data);
            ++n2;
        }
    }

    public byte[] getChunkBytes(int n) {
        return this.chunksData[n].data;
    }

    public S26PacketMapChunkBulk() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeBoolean(this.isOverworld);
        packetBuffer.writeVarIntToBuffer(this.chunksData.length);
        int n = 0;
        while (n < this.xPositions.length) {
            packetBuffer.writeInt(this.xPositions[n]);
            packetBuffer.writeInt(this.zPositions[n]);
            packetBuffer.writeShort((short)(this.chunksData[n].dataSize & 0xFFFF));
            ++n;
        }
        n = 0;
        while (n < this.xPositions.length) {
            packetBuffer.writeBytes(this.chunksData[n].data);
            ++n;
        }
    }

    public int getChunkSize(int n) {
        return this.chunksData[n].dataSize;
    }

    public int getChunkX(int n) {
        return this.xPositions[n];
    }

    public int getChunkCount() {
        return this.xPositions.length;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleMapChunkBulk(this);
    }

    public int getChunkZ(int n) {
        return this.zPositions[n];
    }

    public S26PacketMapChunkBulk(List<Chunk> list) {
        int n = list.size();
        this.xPositions = new int[n];
        this.zPositions = new int[n];
        this.chunksData = new S21PacketChunkData.Extracted[n];
        this.isOverworld = !list.get((int)0).getWorld().provider.getHasNoSky();
        int n2 = 0;
        while (n2 < n) {
            Chunk chunk = list.get(n2);
            S21PacketChunkData.Extracted extracted = S21PacketChunkData.func_179756_a(chunk, true, this.isOverworld, 65535);
            this.xPositions[n2] = chunk.xPosition;
            this.zPositions[n2] = chunk.zPosition;
            this.chunksData[n2] = extracted;
            ++n2;
        }
    }
}

