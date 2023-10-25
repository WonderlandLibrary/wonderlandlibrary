package nl.matsv.viabackwards.api.data;

import com.google.common.base.Preconditions;
import java.util.Map;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.Mappings;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.viaversion.libs.fastutil.ints.Int2ObjectMap;
import us.myles.viaversion.libs.gson.JsonObject;

public class BackwardsMappings extends MappingData {
  private final Class<? extends Protocol> vvProtocolClass;
  
  private Int2ObjectMap<MappedItem> backwardsItemMappings;
  
  private Map<String, String> backwardsSoundMappings;
  
  public BackwardsMappings(String oldVersion, String newVersion, @Nullable Class<? extends Protocol> vvProtocolClass) {
    this(oldVersion, newVersion, vvProtocolClass, false);
  }
  
  public BackwardsMappings(String oldVersion, String newVersion, @Nullable Class<? extends Protocol> vvProtocolClass, boolean hasDiffFile) {
    super(oldVersion, newVersion, hasDiffFile);
    Preconditions.checkArgument(!vvProtocolClass.isAssignableFrom(BackwardsProtocol.class));
    this.vvProtocolClass = vvProtocolClass;
    this.loadItems = false;
  }
  
  protected void loadExtras(JsonObject oldMappings, JsonObject newMappings, @Nullable JsonObject diffMappings) {
    if (diffMappings != null) {
      JsonObject diffItems = diffMappings.getAsJsonObject("items");
      if (diffItems != null)
        this.backwardsItemMappings = VBMappingDataLoader.loadItemMappings(oldMappings.getAsJsonObject("items"), newMappings.getAsJsonObject("items"), diffItems); 
      JsonObject diffSounds = diffMappings.getAsJsonObject("sounds");
      if (diffSounds != null)
        this.backwardsSoundMappings = VBMappingDataLoader.objectToMap(diffSounds); 
    } 
    if (this.vvProtocolClass != null)
      this.itemMappings = ProtocolRegistry.getProtocol(this.vvProtocolClass).getMappingData().getItemMappings().inverse(); 
    loadVBExtras(oldMappings, newMappings);
  }
  
  @Nullable
  protected Mappings loadFromArray(JsonObject oldMappings, JsonObject newMappings, @Nullable JsonObject diffMappings, String key) {
    if (!oldMappings.has(key) || !newMappings.has(key))
      return null; 
    JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
    return new VBMappings(oldMappings.getAsJsonArray(key), newMappings.getAsJsonArray(key), diff, shouldWarnOnMissing(key));
  }
  
  @Nullable
  protected Mappings loadFromObject(JsonObject oldMappings, JsonObject newMappings, @Nullable JsonObject diffMappings, String key) {
    if (!oldMappings.has(key) || !newMappings.has(key))
      return null; 
    JsonObject diff = (diffMappings != null) ? diffMappings.getAsJsonObject(key) : null;
    return new VBMappings(oldMappings.getAsJsonObject(key), newMappings.getAsJsonObject(key), diff, shouldWarnOnMissing(key));
  }
  
  protected JsonObject loadDiffFile() {
    return VBMappingDataLoader.loadFromDataDir("mapping-" + this.newVersion + "to" + this.oldVersion + ".json");
  }
  
  protected void loadVBExtras(JsonObject oldMappings, JsonObject newMappings) {}
  
  protected boolean shouldWarnOnMissing(String key) {
    return (!key.equals("blocks") && !key.equals("statistics"));
  }
  
  public int getNewItemId(int id) {
    return this.itemMappings.get(id);
  }
  
  public int getNewBlockId(int id) {
    return this.blockMappings.getNewId(id);
  }
  
  public int getOldItemId(int id) {
    return checkValidity(id, this.itemMappings.inverse().get(id), "item");
  }
  
  @Nullable
  public MappedItem getMappedItem(int id) {
    return (this.backwardsItemMappings != null) ? (MappedItem)this.backwardsItemMappings.get(id) : null;
  }
  
  @Nullable
  public String getMappedNamedSound(String id) {
    return (this.backwardsSoundMappings != null) ? this.backwardsSoundMappings.get(id) : null;
  }
  
  @Nullable
  public Int2ObjectMap<MappedItem> getBackwardsItemMappings() {
    return this.backwardsItemMappings;
  }
  
  @Nullable
  public Map<String, String> getBackwardsSoundMappings() {
    return this.backwardsSoundMappings;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\data\BackwardsMappings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */