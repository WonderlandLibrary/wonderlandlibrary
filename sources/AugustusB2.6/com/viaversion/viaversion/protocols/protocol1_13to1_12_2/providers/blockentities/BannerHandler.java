// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.blockentities;

import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.BlockStorage;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.providers.BlockEntityProvider;

public class BannerHandler implements BlockEntityProvider.BlockEntityHandler
{
    private static final int WALL_BANNER_START = 7110;
    private static final int WALL_BANNER_STOP = 7173;
    private static final int BANNER_START = 6854;
    private static final int BANNER_STOP = 7109;
    
    @Override
    public int transform(final UserConnection user, final CompoundTag tag) {
        final BlockStorage storage = user.get(BlockStorage.class);
        final Position position = new Position((int)this.getLong(tag.get("x")), (short)this.getLong(tag.get("y")), (int)this.getLong(tag.get("z")));
        if (!storage.contains(position)) {
            Via.getPlatform().getLogger().warning("Received an banner color update packet, but there is no banner! O_o " + tag);
            return -1;
        }
        int blockId = storage.get(position).getOriginal();
        final Tag base = tag.get("Base");
        int color = 0;
        if (base != null) {
            color = tag.get("Base").asInt();
        }
        if (blockId >= 6854 && blockId <= 7109) {
            blockId += (15 - color) * 16;
        }
        else if (blockId >= 7110 && blockId <= 7173) {
            blockId += (15 - color) * 4;
        }
        else {
            Via.getPlatform().getLogger().warning("Why does this block have the banner block entity? :(" + tag);
        }
        if (tag.get("Patterns") instanceof ListTag) {
            for (final Tag pattern : tag.get("Patterns")) {
                if (pattern instanceof CompoundTag) {
                    final Tag c = ((CompoundTag)pattern).get("Color");
                    if (!(c instanceof IntTag)) {
                        continue;
                    }
                    ((IntTag)c).setValue(15 - (int)c.getValue());
                }
            }
        }
        final Tag name = tag.get("CustomName");
        if (name instanceof StringTag) {
            ((StringTag)name).setValue(ChatRewriter.legacyTextToJsonString(((StringTag)name).getValue()));
        }
        return blockId;
    }
    
    private long getLong(final NumberTag tag) {
        return tag.asLong();
    }
}
