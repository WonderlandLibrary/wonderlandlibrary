/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerMapDataPacket
extends MinecraftPacket {
    private int mapId;
    private byte scale;
    private boolean trackingPosition;
    private MapIcon[] icons;
    private MapData data;

    private ServerMapDataPacket() {
    }

    public ServerMapDataPacket(int mapId, byte scale, boolean trackingPosition, MapIcon[] icons) {
        this(mapId, scale, trackingPosition, icons, null);
    }

    public ServerMapDataPacket(int mapId, byte scale, boolean trackingPosition, MapIcon[] icons, MapData data) {
        this.mapId = mapId;
        this.scale = scale;
        this.trackingPosition = trackingPosition;
        this.icons = icons;
        this.data = data;
    }

    public int getMapId() {
        return this.mapId;
    }

    public byte getScale() {
        return this.scale;
    }

    public boolean getTrackingPosition() {
        return this.trackingPosition;
    }

    public MapIcon[] getIcons() {
        return this.icons;
    }

    public MapData getData() {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.mapId = in.readVarInt();
        this.scale = in.readByte();
        this.trackingPosition = in.readBoolean();
        this.icons = new MapIcon[in.readVarInt()];
        int index = 0;
        while (index < this.icons.length) {
            int data = in.readUnsignedByte();
            int type = data >> 4 & 0xF;
            int rotation = data & 0xF;
            int x = in.readUnsignedByte();
            int z = in.readUnsignedByte();
            this.icons[index] = new MapIcon(x, z, MagicValues.key(MapIconType.class, type), rotation);
            ++index;
        }
        int columns = in.readUnsignedByte();
        if (columns > 0) {
            int rows = in.readUnsignedByte();
            int x = in.readUnsignedByte();
            int y = in.readUnsignedByte();
            byte[] data = in.readBytes(in.readVarInt());
            this.data = new MapData(columns, rows, x, y, data);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.mapId);
        out.writeByte(this.scale);
        out.writeBoolean(this.trackingPosition);
        out.writeVarInt(this.icons.length);
        int index = 0;
        while (index < this.icons.length) {
            MapIcon icon = this.icons[index];
            int type = MagicValues.value(Integer.class, (Object)icon.getIconType());
            out.writeByte((type & 0xF) << 4 | icon.getIconRotation() & 0xF);
            out.writeByte(icon.getCenterX());
            out.writeByte(icon.getCenterZ());
            ++index;
        }
        if (this.data != null && this.data.getColumns() != 0) {
            out.writeByte(this.data.getColumns());
            out.writeByte(this.data.getRows());
            out.writeByte(this.data.getX());
            out.writeByte(this.data.getY());
            out.writeVarInt(this.data.getData().length);
            out.writeBytes(this.data.getData());
        } else {
            out.writeByte(0);
        }
    }
}

