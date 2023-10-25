package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BannerHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {
  private static final int WALL_BANNER_START = 7110;
  
  private static final int WALL_BANNER_STOP = 7173;
  
  private static final int BANNER_START = 6854;
  
  private static final int BANNER_STOP = 7109;
  
  public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
    if (blockId >= 6854 && blockId <= 7109) {
      int color = blockId - 6854 >> 4;
      tag.put((Tag)new IntTag("Base", 15 - color));
    } else if (blockId >= 7110 && blockId <= 7173) {
      int color = blockId - 7110 >> 2;
      tag.put((Tag)new IntTag("Base", 15 - color));
    } else {
      ViaBackwards.getPlatform().getLogger().warning("Why does this block have the banner block entity? :(" + tag);
    } 
    Tag patternsTag = tag.get("Patterns");
    if (patternsTag instanceof us.myles.viaversion.libs.opennbt.tag.builtin.ListTag)
      for (Tag pattern : patternsTag) {
        if (!(pattern instanceof CompoundTag))
          continue; 
        IntTag c = (IntTag)((CompoundTag)pattern).get("Color");
        c.setValue(15 - c.getValue().intValue());
      }  
    return tag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\block_entity_handlers\BannerHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */