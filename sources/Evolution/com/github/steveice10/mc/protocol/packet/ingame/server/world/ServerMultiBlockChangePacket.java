/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerMultiBlockChangePacket
extends MinecraftPacket {
    private BlockChangeRecord[] records;

    private ServerMultiBlockChangePacket() {
    }

    public ServerMultiBlockChangePacket(BlockChangeRecord ... records) {
        if (records == null || records.length == 0) {
            throw new IllegalArgumentException("Records must contain at least 1 value.");
        }
        this.records = records;
    }

    public BlockChangeRecord[] getRecords() {
        return this.records;
    }

    @Override
    public void read(NetInput in) throws IOException {
        int chunkX = in.readInt();
        int chunkZ = in.readInt();
        this.records = new BlockChangeRecord[in.readVarInt()];
        int index = 0;
        while (index < this.records.length) {
            short pos = in.readShort();
            BlockState block = NetUtil.readBlockState(in);
            int x = (chunkX << 4) + (pos >> 12 & 0xF);
            int y = pos & 0xFF;
            int z = (chunkZ << 4) + (pos >> 8 & 0xF);
            this.records[index] = new BlockChangeRecord(new Position(x, y, z), block);
            ++index;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        int chunkX = this.records[0].getPosition().getX() >> 4;
        int chunkZ = this.records[0].getPosition().getZ() >> 4;
        out.writeInt(chunkX);
        out.writeInt(chunkZ);
        out.writeVarInt(this.records.length);
        BlockChangeRecord[] blockChangeRecordArray = this.records;
        int n = this.records.length;
        int n2 = 0;
        while (n2 < n) {
            BlockChangeRecord record = blockChangeRecordArray[n2];
            out.writeShort(record.getPosition().getX() - (chunkX << 4) << 12 | record.getPosition().getZ() - (chunkZ << 4) << 8 | record.getPosition().getY());
            NetUtil.writeBlockState(out, record.getBlock());
            ++n2;
        }
    }
}

