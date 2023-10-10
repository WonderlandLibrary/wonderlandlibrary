// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class BedHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        final int offset = blockId - 748;
        final int color = offset >> 4;
        tag.put("color", new IntTag(color));
        return tag;
    }
}
