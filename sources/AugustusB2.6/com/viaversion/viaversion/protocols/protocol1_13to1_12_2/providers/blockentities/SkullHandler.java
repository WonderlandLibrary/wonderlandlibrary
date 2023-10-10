// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.blockentities;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class SkullHandler implements BlockEntityProvider.BlockEntityHandler
{
    private static final int SKULL_WALL_START = 5447;
    private static final int SKULL_END = 5566;
    
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        final BlockStorage storage = user.get(BlockStorage.class);
        final Position position = new Position((int)this.getLong(tag.get("x")), (short)this.getLong(tag.get("y")), (int)this.getLong(tag.get("z")));
        if (!storage.contains(position)) {
            Via.getPlatform().getLogger().warning("Received an head update packet, but there is no head! O_o " + tag);
            return -1;
        }
        int id = storage.get(position).getOriginal();
        if (id >= 5447 && id <= 5566) {
            final Tag skullType = tag.get("SkullType");
            if (skullType != null) {
                id += tag.get("SkullType").asInt() * 20;
            }
            if (tag.contains("Rot")) {
                id += tag.get("Rot").asInt();
            }
            return id;
        }
        Via.getPlatform().getLogger().warning("Why does this block have the skull block entity? " + tag);
        return -1;
    }
    
    private long getLong(final NumberTag tag) {
        return tag.asLong();
    }
}
