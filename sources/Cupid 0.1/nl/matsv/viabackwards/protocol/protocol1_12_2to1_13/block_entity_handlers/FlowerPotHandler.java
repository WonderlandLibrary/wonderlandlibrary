package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;
import us.myles.ViaVersion.api.Pair;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class FlowerPotHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler {
  private static final Int2ObjectMap<Pair<String, Byte>> FLOWERS = (Int2ObjectMap<Pair<String, Byte>>)new Int2ObjectOpenHashMap(22, 1.0F);
  
  private static final Pair<String, Byte> AIR = new Pair("minecraft:air", Byte.valueOf((byte)0));
  
  static {
    FLOWERS.put(5265, AIR);
    register(5266, "minecraft:sapling", (byte)0);
    register(5267, "minecraft:sapling", (byte)1);
    register(5268, "minecraft:sapling", (byte)2);
    register(5269, "minecraft:sapling", (byte)3);
    register(5270, "minecraft:sapling", (byte)4);
    register(5271, "minecraft:sapling", (byte)5);
    register(5272, "minecraft:tallgrass", (byte)2);
    register(5273, "minecraft:yellow_flower", (byte)0);
    register(5274, "minecraft:red_flower", (byte)0);
    register(5275, "minecraft:red_flower", (byte)1);
    register(5276, "minecraft:red_flower", (byte)2);
    register(5277, "minecraft:red_flower", (byte)3);
    register(5278, "minecraft:red_flower", (byte)4);
    register(5279, "minecraft:red_flower", (byte)5);
    register(5280, "minecraft:red_flower", (byte)6);
    register(5281, "minecraft:red_flower", (byte)7);
    register(5282, "minecraft:red_flower", (byte)8);
    register(5283, "minecraft:red_mushroom", (byte)0);
    register(5284, "minecraft:brown_mushroom", (byte)0);
    register(5285, "minecraft:deadbush", (byte)0);
    register(5286, "minecraft:cactus", (byte)0);
  }
  
  private static void register(int id, String identifier, byte data) {
    FLOWERS.put(id, new Pair(identifier, Byte.valueOf(data)));
  }
  
  public static boolean isFlowah(int id) {
    return (id >= 5265 && id <= 5286);
  }
  
  public Pair<String, Byte> getOrDefault(int blockId) {
    Pair<String, Byte> pair = (Pair<String, Byte>)FLOWERS.get(blockId);
    return (pair != null) ? pair : AIR;
  }
  
  public CompoundTag transform(UserConnection user, int blockId, CompoundTag tag) {
    Pair<String, Byte> item = getOrDefault(blockId);
    tag.put((Tag)new StringTag("Item", (String)item.getKey()));
    tag.put((Tag)new IntTag("Data", ((Byte)item.getValue()).byteValue()));
    return tag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\block_entity_handlers\FlowerPotHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */