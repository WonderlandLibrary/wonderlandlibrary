package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.lang.reflect.Field;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.data.EntityTypeRewriter;
import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import us.myles.viaversion.libs.fastutil.objects.ObjectIterator;

public class EntityTypeMapping {
  private static final Int2IntMap TYPES = (Int2IntMap)new Int2IntOpenHashMap();
  
  static {
    TYPES.defaultReturnValue(-1);
    try {
      Field field = EntityTypeRewriter.class.getDeclaredField("ENTITY_TYPES");
      field.setAccessible(true);
      Int2IntMap entityTypes = (Int2IntMap)field.get(null);
      for (ObjectIterator<Int2IntMap.Entry> objectIterator = entityTypes.int2IntEntrySet().iterator(); objectIterator.hasNext(); ) {
        Int2IntMap.Entry entry = objectIterator.next();
        TYPES.put(entry.getIntValue(), entry.getIntKey());
      } 
    } catch (NoSuchFieldException|IllegalAccessException ex) {
      ex.printStackTrace();
    } 
  }
  
  public static int getOldId(int type1_13) {
    return TYPES.get(type1_13);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\data\EntityTypeMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */