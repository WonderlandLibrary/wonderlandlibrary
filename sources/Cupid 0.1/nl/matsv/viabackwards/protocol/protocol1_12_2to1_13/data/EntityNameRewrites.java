package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.util.HashMap;
import java.util.Map;

public class EntityNameRewrites {
  private static final Map<String, String> ENTITY_NAMES = new HashMap<>();
  
  static {
    reg("commandblock_minecart", "command_block_minecart");
    reg("ender_crystal", "end_crystal");
    reg("evocation_fangs", "evoker_fangs");
    reg("evocation_illager", "evoker");
    reg("eye_of_ender_signal", "eye_of_ender");
    reg("fireworks_rocket", "firework_rocket");
    reg("illusion_illager", "illusioner");
    reg("snowman", "snow_golem");
    reg("villager_golem", "iron_golem");
    reg("vindication_illager", "vindicator");
    reg("xp_bottle", "experience_bottle");
    reg("xp_orb", "experience_orb");
  }
  
  private static void reg(String past, String future) {
    ENTITY_NAMES.put("minecraft:" + future, "minecraft:" + past);
  }
  
  public static String rewrite(String entName) {
    String entityName = ENTITY_NAMES.get(entName);
    if (entityName != null)
      return entityName; 
    entityName = ENTITY_NAMES.get("minecraft:" + entName);
    if (entityName != null)
      return entityName; 
    return entName;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\data\EntityNameRewrites.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */