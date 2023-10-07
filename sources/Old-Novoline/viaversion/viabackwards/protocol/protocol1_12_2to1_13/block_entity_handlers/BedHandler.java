package viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.IntTag;
import viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import viaversion.viaversion.api.data.UserConnection;

public class BedHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {

    @Override
    public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
        int offset = blockId - 748;
        int color = offset >> 4;

        tag.put(new IntTag("color", color));

        return tag;
    }
}
