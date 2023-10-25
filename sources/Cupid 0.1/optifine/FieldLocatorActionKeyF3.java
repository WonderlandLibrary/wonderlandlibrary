package optifine;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class FieldLocatorActionKeyF3 implements IFieldLocator {
  public Field getField() {
    Class<Minecraft> oclass = Minecraft.class;
    Field field = getFieldRenderChunksMany();
    if (field == null) {
      Config.log("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3 (field renderChunksMany not found)");
      return null;
    } 
    Field field1 = ReflectorRaw.getFieldAfter(Minecraft.class, field, boolean.class, 0);
    if (field1 == null) {
      Config.log("(Reflector) Field not present: " + oclass.getName() + ".actionKeyF3");
      return null;
    } 
    return field1;
  }
  
  private Field getFieldRenderChunksMany() {
    Minecraft minecraft = Minecraft.getMinecraft();
    boolean flag = minecraft.renderChunksMany;
    Field[] afield = Minecraft.class.getDeclaredFields();
    minecraft.renderChunksMany = true;
    Field[] afield1 = ReflectorRaw.getFields(minecraft, afield, boolean.class, Boolean.TRUE);
    minecraft.renderChunksMany = false;
    Field[] afield2 = ReflectorRaw.getFields(minecraft, afield, boolean.class, Boolean.FALSE);
    minecraft.renderChunksMany = flag;
    Set<Field> set = new HashSet<>(Arrays.asList(afield1));
    Set<Field> set1 = new HashSet<>(Arrays.asList(afield2));
    Set<Field> set2 = new HashSet<>(set);
    set2.retainAll(set1);
    Field[] afield3 = set2.<Field>toArray(new Field[set2.size()]);
    return (afield3.length != 1) ? null : afield3[0];
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\FieldLocatorActionKeyF3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */