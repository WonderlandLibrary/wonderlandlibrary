package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers;

import java.util.HashMap;
import java.util.Map;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.BannerHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.BedHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.FlowerPotHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.PistonHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.SkullHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers.SpawnerHandler;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.BackwardsBlockStorage;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.platform.providers.Provider;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BackwardsBlockEntityProvider implements Provider {
  private final Map<String, BackwardsBlockEntityHandler> handlers = new HashMap<>();
  
  public BackwardsBlockEntityProvider() {
    this.handlers.put("minecraft:flower_pot", new FlowerPotHandler());
    this.handlers.put("minecraft:bed", new BedHandler());
    this.handlers.put("minecraft:banner", new BannerHandler());
    this.handlers.put("minecraft:skull", new SkullHandler());
    this.handlers.put("minecraft:mob_spawner", new SpawnerHandler());
    this.handlers.put("minecraft:piston", new PistonHandler());
  }
  
  public boolean isHandled(String key) {
    return this.handlers.containsKey(key);
  }
  
  public CompoundTag transform(UserConnection user, Position position, CompoundTag tag) throws Exception {
    String id = (String)tag.get("id").getValue();
    BackwardsBlockEntityHandler handler = this.handlers.get(id);
    if (handler == null) {
      if (Via.getManager().isDebug())
        ViaBackwards.getPlatform().getLogger().warning("Unhandled BlockEntity " + id + " full tag: " + tag); 
      return tag;
    } 
    BackwardsBlockStorage storage = (BackwardsBlockStorage)user.get(BackwardsBlockStorage.class);
    Integer blockId = storage.get(position);
    if (blockId == null) {
      if (Via.getManager().isDebug())
        ViaBackwards.getPlatform().getLogger().warning("Handled BlockEntity does not have a stored block :( " + id + " full tag: " + tag); 
      return tag;
    } 
    return handler.transform(user, blockId.intValue(), tag);
  }
  
  public CompoundTag transform(UserConnection user, Position position, String id) throws Exception {
    CompoundTag tag = new CompoundTag("");
    tag.put((Tag)new StringTag("id", id));
    tag.put((Tag)new IntTag("x", Math.toIntExact(position.getX())));
    tag.put((Tag)new IntTag("y", Math.toIntExact(position.getY())));
    tag.put((Tag)new IntTag("z", Math.toIntExact(position.getZ())));
    return transform(user, position, tag);
  }
  
  public static interface BackwardsBlockEntityHandler {
    CompoundTag transform(UserConnection param1UserConnection, int param1Int, CompoundTag param1CompoundTag);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\providers\BackwardsBlockEntityProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */