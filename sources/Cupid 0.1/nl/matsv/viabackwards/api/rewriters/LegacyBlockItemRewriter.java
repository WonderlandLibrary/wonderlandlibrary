package nl.matsv.viabackwards.api.rewriters;

import java.util.HashMap;
import java.util.Map;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.MappedLegacyBlockItem;
import nl.matsv.viabackwards.api.data.VBMappingDataLoader;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data.BlockColors;
import nl.matsv.viabackwards.utils.Block;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.gson.JsonPrimitive;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public abstract class LegacyBlockItemRewriter<T extends BackwardsProtocol> extends ItemRewriterBase<T> {
  private static final Map<String, Int2ObjectMap<MappedLegacyBlockItem>> LEGACY_MAPPINGS = new HashMap<>();
  
  protected final Int2ObjectMap<MappedLegacyBlockItem> replacementData;
  
  static {
    JsonObject jsonObject = VBMappingDataLoader.loadFromDataDir("legacy-mappings.json");
    for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)jsonObject.entrySet()) {
      Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap(8);
      LEGACY_MAPPINGS.put(entry.getKey(), int2ObjectOpenHashMap);
      for (Map.Entry<String, JsonElement> dataEntry : (Iterable<Map.Entry<String, JsonElement>>)((JsonElement)entry.getValue()).getAsJsonObject().entrySet()) {
        JsonObject object = ((JsonElement)dataEntry.getValue()).getAsJsonObject();
        int id = object.getAsJsonPrimitive("id").getAsInt();
        JsonPrimitive jsonData = object.getAsJsonPrimitive("data");
        short data = (jsonData != null) ? jsonData.getAsShort() : 0;
        String name = object.getAsJsonPrimitive("name").getAsString();
        JsonPrimitive blockField = object.getAsJsonPrimitive("block");
        boolean block = (blockField != null && blockField.getAsBoolean());
        if (((String)dataEntry.getKey()).indexOf('-') != -1) {
          String[] split = ((String)dataEntry.getKey()).split("-", 2);
          int from = Integer.parseInt(split[0]);
          int to = Integer.parseInt(split[1]);
          if (name.contains("%color%")) {
            for (int j = from; j <= to; j++)
              int2ObjectOpenHashMap.put(j, new MappedLegacyBlockItem(id, data, name.replace("%color%", BlockColors.get(j - from)), block)); 
            continue;
          } 
          MappedLegacyBlockItem mappedBlockItem = new MappedLegacyBlockItem(id, data, name, block);
          for (int i = from; i <= to; i++)
            int2ObjectOpenHashMap.put(i, mappedBlockItem); 
          continue;
        } 
        int2ObjectOpenHashMap.put(Integer.parseInt(dataEntry.getKey()), new MappedLegacyBlockItem(id, data, name, block));
      } 
    } 
  }
  
  protected LegacyBlockItemRewriter(T protocol) {
    super(protocol, false);
    this.replacementData = LEGACY_MAPPINGS.get(protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
  }
  
  @Nullable
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    MappedLegacyBlockItem data = (MappedLegacyBlockItem)this.replacementData.get(item.getIdentifier());
    if (data == null)
      return super.handleItemToClient(item); 
    short originalData = item.getData();
    item.setIdentifier(data.getId());
    if (data.getData() != -1)
      item.setData(data.getData()); 
    if (data.getName() != null) {
      if (item.getTag() == null)
        item.setTag(new CompoundTag("")); 
      CompoundTag display = (CompoundTag)item.getTag().get("display");
      if (display == null)
        item.getTag().put((Tag)(display = new CompoundTag("display"))); 
      StringTag nameTag = (StringTag)display.get("Name");
      if (nameTag == null) {
        display.put((Tag)(nameTag = new StringTag("Name", data.getName())));
        display.put((Tag)new ByteTag(this.nbtTagName + "|customName"));
      } 
      String value = nameTag.getValue();
      if (value.contains("%vb_color%"))
        display.put((Tag)new StringTag("Name", value.replace("%vb_color%", BlockColors.get(originalData)))); 
    } 
    return item;
  }
  
  public int handleBlockID(int idx) {
    int type = idx >> 4;
    int meta = idx & 0xF;
    Block b = handleBlock(type, meta);
    if (b == null)
      return idx; 
    return b.getId() << 4 | b.getData() & 0xF;
  }
  
  @Nullable
  public Block handleBlock(int blockId, int data) {
    MappedLegacyBlockItem settings = (MappedLegacyBlockItem)this.replacementData.get(blockId);
    if (settings == null || !settings.isBlock())
      return null; 
    Block block = settings.getBlock();
    if (block.getData() == -1)
      return block.withData(data); 
    return block;
  }
  
  protected void handleChunk(Chunk chunk) {
    Map<Pos, CompoundTag> tags = new HashMap<>();
    for (CompoundTag tag : chunk.getBlockEntities()) {
      Tag xTag, yTag, zTag;
      if ((xTag = tag.get("x")) == null || (yTag = tag.get("y")) == null || (zTag = tag.get("z")) == null)
        continue; 
      Pos pos = new Pos(((Integer)xTag.getValue()).intValue() & 0xF, ((Integer)yTag.getValue()).intValue(), ((Integer)zTag.getValue()).intValue() & 0xF);
      tags.put(pos, tag);
      ChunkSection section = chunk.getSections()[pos.getY() >> 4];
      if (section == null)
        continue; 
      int block = section.getFlatBlock(pos.getX(), pos.getY() & 0xF, pos.getZ());
      int btype = block >> 4;
      MappedLegacyBlockItem settings = (MappedLegacyBlockItem)this.replacementData.get(btype);
      if (settings != null && settings.hasBlockEntityHandler())
        settings.getBlockEntityHandler().handleOrNewCompoundTag(block, tag); 
    } 
    for (int i = 0; i < (chunk.getSections()).length; i++) {
      ChunkSection section = chunk.getSections()[i];
      if (section != null) {
        boolean hasBlockEntityHandler = false;
        for (int j = 0; j < section.getPaletteSize(); j++) {
          int block = section.getPaletteEntry(j);
          int btype = block >> 4;
          int meta = block & 0xF;
          Block b = handleBlock(btype, meta);
          if (b != null)
            section.setPaletteEntry(j, b.getId() << 4 | b.getData() & 0xF); 
          if (!hasBlockEntityHandler) {
            MappedLegacyBlockItem settings = (MappedLegacyBlockItem)this.replacementData.get(btype);
            if (settings != null && settings.hasBlockEntityHandler())
              hasBlockEntityHandler = true; 
          } 
        } 
        if (hasBlockEntityHandler)
          for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
              for (int z = 0; z < 16; z++) {
                int block = section.getFlatBlock(x, y, z);
                int btype = block >> 4;
                int meta = block & 0xF;
                MappedLegacyBlockItem settings = (MappedLegacyBlockItem)this.replacementData.get(btype);
                if (settings != null && settings.hasBlockEntityHandler()) {
                  Pos pos = new Pos(x, y + (i << 4), z);
                  if (!tags.containsKey(pos)) {
                    CompoundTag tag = new CompoundTag("");
                    tag.put((Tag)new IntTag("x", x + (chunk.getX() << 4)));
                    tag.put((Tag)new IntTag("y", y + (i << 4)));
                    tag.put((Tag)new IntTag("z", z + (chunk.getZ() << 4)));
                    settings.getBlockEntityHandler().handleOrNewCompoundTag(block, tag);
                    chunk.getBlockEntities().add(tag);
                  } 
                } 
              } 
            } 
          }  
      } 
    } 
  }
  
  protected CompoundTag getNamedTag(String text) {
    CompoundTag tag = new CompoundTag("");
    tag.put((Tag)new CompoundTag("display"));
    text = ChatColor.RESET + text;
    ((CompoundTag)tag.get("display")).put((Tag)new StringTag("Name", this.jsonNameFormat ? ChatRewriter.legacyTextToJson(text).toString() : text));
    return tag;
  }
  
  private static final class Pos {
    private final int x;
    
    private final short y;
    
    private final int z;
    
    private Pos(int x, int y, int z) {
      this.x = x;
      this.y = (short)y;
      this.z = z;
    }
    
    public int getX() {
      return this.x;
    }
    
    public int getY() {
      return this.y;
    }
    
    public int getZ() {
      return this.z;
    }
    
    public boolean equals(Object o) {
      if (this == o)
        return true; 
      if (o == null || getClass() != o.getClass())
        return false; 
      Pos pos = (Pos)o;
      if (this.x != pos.x)
        return false; 
      if (this.y != pos.y)
        return false; 
      return (this.z == pos.z);
    }
    
    public int hashCode() {
      int result = this.x;
      result = 31 * result + this.y;
      result = 31 * result + this.z;
      return result;
    }
    
    public String toString() {
      return "Pos{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\LegacyBlockItemRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */