// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.chunks;

import java.util.HashMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9_1_2to1_9_3_4.Protocol1_9_1_2To1_9_3_4;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import java.util.Iterator;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.List;
import java.util.Map;

public class BlockEntity
{
    private static final Map<String, Integer> types;
    
    public static void handle(final List<CompoundTag> tags, final UserConnection connection) {
        for (final CompoundTag tag : tags) {
            try {
                if (!tag.contains("id")) {
                    throw new Exception("NBT tag not handled because the id key is missing");
                }
                final String id = (String)tag.get("id").getValue();
                if (!BlockEntity.types.containsKey(id)) {
                    throw new Exception("Not handled id: " + id);
                }
                final int newId = BlockEntity.types.get(id);
                if (newId == -1) {
                    continue;
                }
                final int x = tag.get("x").asInt();
                final int y = tag.get("y").asInt();
                final int z = tag.get("z").asInt();
                final Position pos = new Position(x, (short)y, z);
                updateBlockEntity(pos, (short)newId, tag, connection);
            }
            catch (Exception e) {
                if (!Via.getManager().isDebug()) {
                    continue;
                }
                Via.getPlatform().getLogger().warning("Block Entity: " + e.getMessage() + ": " + tag);
            }
        }
    }
    
    private static void updateBlockEntity(final Position pos, final short id, final CompoundTag tag, final UserConnection connection) throws Exception {
        final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9_3.BLOCK_ENTITY_DATA, null, connection);
        wrapper.write(Type.POSITION, pos);
        wrapper.write(Type.UNSIGNED_BYTE, id);
        wrapper.write(Type.NBT, tag);
        wrapper.scheduleSend(Protocol1_9_1_2To1_9_3_4.class, false);
    }
    
    static {
        (types = new HashMap<String, Integer>()).put("MobSpawner", 1);
        BlockEntity.types.put("Control", 2);
        BlockEntity.types.put("Beacon", 3);
        BlockEntity.types.put("Skull", 4);
        BlockEntity.types.put("FlowerPot", 5);
        BlockEntity.types.put("Banner", 6);
        BlockEntity.types.put("UNKNOWN", 7);
        BlockEntity.types.put("EndGateway", 8);
        BlockEntity.types.put("Sign", 9);
    }
}
