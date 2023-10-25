package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class SkullHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {
  private static final int SKULL_START = 5447;
  
  public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
    int diff = blockId - 5447;
    int pos = diff % 20;
    byte type = (byte)(int)Math.floor((diff / 20.0F));
    tag.put((Tag)new ByteTag("SkullType", type));
    if (pos < 4)
      return tag; 
    tag.put((Tag)new ByteTag("Rot", (byte)(pos - 4 & 0xFF)));
    return tag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\block_entity_handlers\SkullHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */