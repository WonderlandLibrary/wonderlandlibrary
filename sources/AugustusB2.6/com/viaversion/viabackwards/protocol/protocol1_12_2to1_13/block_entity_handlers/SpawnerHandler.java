// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.EntityNameRewrites;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class SpawnerHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        final Tag dataTag = tag.get("SpawnData");
        if (dataTag instanceof CompoundTag) {
            final CompoundTag data = (CompoundTag)dataTag;
            final Tag idTag = data.get("id");
            if (idTag instanceof StringTag) {
                final StringTag s = (StringTag)idTag;
                s.setValue(EntityNameRewrites.rewrite(s.getValue()));
            }
        }
        return tag;
    }
}
