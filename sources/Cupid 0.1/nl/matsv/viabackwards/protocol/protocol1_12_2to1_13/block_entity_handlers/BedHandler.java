package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BedHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {
  public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
    int offset = blockId - 748;
    int color = offset >> 4;
    tag.put((Tag)new IntTag("color", color));
    return tag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\block_entity_handlers\BedHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */