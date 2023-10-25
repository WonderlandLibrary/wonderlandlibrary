/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

public class S34PacketMaps
implements Packet<INetHandlerPlayClient> {
    private int mapMinX;
    private byte[] mapDataBytes;
    private int mapMaxX;
    private byte mapScale;
    private Vec4b[] mapVisiblePlayersVec4b;
    private int mapId;
    private int mapMaxY;
    private int mapMinY;

    public int getMapId() {
        return this.mapId;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleMaps(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.mapId = packetBuffer.readVarIntFromBuffer();
        this.mapScale = packetBuffer.readByte();
        this.mapVisiblePlayersVec4b = new Vec4b[packetBuffer.readVarIntFromBuffer()];
        int n = 0;
        while (n < this.mapVisiblePlayersVec4b.length) {
            short s = packetBuffer.readByte();
            this.mapVisiblePlayersVec4b[n] = new Vec4b((byte)(s >> 4 & 0xF), packetBuffer.readByte(), packetBuffer.readByte(), (byte)(s & 0xF));
            ++n;
        }
        this.mapMaxX = packetBuffer.readUnsignedByte();
        if (this.mapMaxX > 0) {
            this.mapMaxY = packetBuffer.readUnsignedByte();
            this.mapMinX = packetBuffer.readUnsignedByte();
            this.mapMinY = packetBuffer.readUnsignedByte();
            this.mapDataBytes = packetBuffer.readByteArray();
        }
    }

    public S34PacketMaps(int n, byte by, Collection<Vec4b> collection, byte[] byArray, int n2, int n3, int n4, int n5) {
        this.mapId = n;
        this.mapScale = by;
        this.mapVisiblePlayersVec4b = collection.toArray(new Vec4b[collection.size()]);
        this.mapMinX = n2;
        this.mapMinY = n3;
        this.mapMaxX = n4;
        this.mapMaxY = n5;
        this.mapDataBytes = new byte[n4 * n5];
        int n6 = 0;
        while (n6 < n4) {
            int n7 = 0;
            while (n7 < n5) {
                this.mapDataBytes[n6 + n7 * n4] = byArray[n2 + n6 + (n3 + n7) * 128];
                ++n7;
            }
            ++n6;
        }
    }

    public void setMapdataTo(MapData mapData) {
        mapData.scale = this.mapScale;
        mapData.mapDecorations.clear();
        int n = 0;
        while (n < this.mapVisiblePlayersVec4b.length) {
            Vec4b vec4b = this.mapVisiblePlayersVec4b[n];
            mapData.mapDecorations.put("icon-" + n, vec4b);
            ++n;
        }
        n = 0;
        while (n < this.mapMaxX) {
            int n2 = 0;
            while (n2 < this.mapMaxY) {
                mapData.colors[this.mapMinX + n + (this.mapMinY + n2) * 128] = this.mapDataBytes[n + n2 * this.mapMaxX];
                ++n2;
            }
            ++n;
        }
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.mapId);
        packetBuffer.writeByte(this.mapScale);
        packetBuffer.writeVarIntToBuffer(this.mapVisiblePlayersVec4b.length);
        Vec4b[] vec4bArray = this.mapVisiblePlayersVec4b;
        int n = this.mapVisiblePlayersVec4b.length;
        int n2 = 0;
        while (n2 < n) {
            Vec4b vec4b = vec4bArray[n2];
            packetBuffer.writeByte((vec4b.func_176110_a() & 0xF) << 4 | vec4b.func_176111_d() & 0xF);
            packetBuffer.writeByte(vec4b.func_176112_b());
            packetBuffer.writeByte(vec4b.func_176113_c());
            ++n2;
        }
        packetBuffer.writeByte(this.mapMaxX);
        if (this.mapMaxX > 0) {
            packetBuffer.writeByte(this.mapMaxY);
            packetBuffer.writeByte(this.mapMinX);
            packetBuffer.writeByte(this.mapMinY);
            packetBuffer.writeByteArray(this.mapDataBytes);
        }
    }

    public S34PacketMaps() {
    }
}

