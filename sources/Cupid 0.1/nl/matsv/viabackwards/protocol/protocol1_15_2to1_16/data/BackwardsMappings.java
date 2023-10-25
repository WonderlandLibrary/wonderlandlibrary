package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data;

import java.util.HashMap;
import java.util.Map;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import us.myles.viaversion.libs.gson.JsonObject;

public class BackwardsMappings extends BackwardsMappings {
  private final Map<String, String> attributeMappings = new HashMap<>();
  
  public BackwardsMappings() {
    super("1.16", "1.15", Protocol1_16To1_15_2.class, true);
  }
  
  protected void loadVBExtras(JsonObject oldMappings, JsonObject newMappings) {
    for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)Protocol1_16To1_15_2.MAPPINGS.getAttributeMappings().entrySet())
      this.attributeMappings.put(entry.getValue(), entry.getKey()); 
  }
  
  public Map<String, String> getAttributeMappings() {
    return this.attributeMappings;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\data\BackwardsMappings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */