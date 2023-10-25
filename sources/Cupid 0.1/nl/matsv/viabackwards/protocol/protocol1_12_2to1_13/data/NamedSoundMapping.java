package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.NamedSoundRewriter;

public class NamedSoundMapping {
  private static final Map<String, String> SOUNDS = new HashMap<>();
  
  static {
    try {
      Field field = NamedSoundRewriter.class.getDeclaredField("oldToNew");
      field.setAccessible(true);
      Map<String, String> sounds = (Map<String, String>)field.get(null);
      sounds.forEach((sound1_12, sound1_13) -> (String)SOUNDS.put(sound1_13, sound1_12));
    } catch (NoSuchFieldException|IllegalAccessException ex) {
      ex.printStackTrace();
    } 
  }
  
  public static String getOldId(String sound1_13) {
    return SOUNDS.get(sound1_13);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\data\NamedSoundMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */