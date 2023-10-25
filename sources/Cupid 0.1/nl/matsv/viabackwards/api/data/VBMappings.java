package nl.matsv.viabackwards.api.data;

import java.util.Arrays;
import us.myles.ViaVersion.api.data.Mappings;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.viaversion.libs.gson.JsonObject;

public class VBMappings extends Mappings {
  public VBMappings(int size, JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
    super(create(size, oldMapping, newMapping, diffMapping, warnOnMissing));
  }
  
  public VBMappings(JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
    super(create(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping, warnOnMissing));
  }
  
  public VBMappings(JsonObject oldMapping, JsonObject newMapping, boolean warnOnMissing) {
    this(oldMapping, newMapping, (JsonObject)null, warnOnMissing);
  }
  
  public VBMappings(JsonArray oldMapping, JsonArray newMapping, JsonObject diffMapping, boolean warnOnMissing) {
    super(oldMapping.size(), oldMapping, newMapping, diffMapping, warnOnMissing);
  }
  
  private static short[] create(int size, JsonObject oldMapping, JsonObject newMapping, JsonObject diffMapping, boolean warnOnMissing) {
    short[] oldToNew = new short[size];
    Arrays.fill(oldToNew, (short)-1);
    VBMappingDataLoader.mapIdentifiers(oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
    return oldToNew;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\data\VBMappings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */