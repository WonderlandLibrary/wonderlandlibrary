package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.EntityNameRewrites;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class SpawnerHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {
  public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
    Tag dataTag = tag.get("SpawnData");
    if (dataTag instanceof CompoundTag) {
      CompoundTag data = (CompoundTag)dataTag;
      Tag idTag = data.get("id");
      if (idTag instanceof StringTag) {
        StringTag s = (StringTag)idTag;
        s.setValue(EntityNameRewrites.rewrite(s.getValue()));
      } 
    } 
    return tag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\block_entity_handlers\SpawnerHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */