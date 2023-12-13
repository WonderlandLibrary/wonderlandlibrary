package optifine;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.client.Minecraft;

public class FieldLocatorActionKeyF3 implements IFieldLocator {
   public Field getField() {
      Class var1 = Minecraft.class;
      Field var2 = this.getFieldRenderChunksMany();
      if (var2 == null) {
         Config.log("(Reflector) Field not present: " + var1.getName() + ".actionKeyF3 (field renderChunksMany not found)");
         return null;
      } else {
         Field var3 = ReflectorRaw.getFieldAfter(Minecraft.class, var2, Boolean.TYPE, 0);
         if (var3 == null) {
            Config.log("(Reflector) Field not present: " + var1.getName() + ".actionKeyF3");
            return null;
         } else {
            return var3;
         }
      }
   }

   private Field getFieldRenderChunksMany() {
      Minecraft var1 = Minecraft.getMinecraft();
      boolean var2 = var1.renderChunksMany;
      Field[] var3 = Minecraft.class.getDeclaredFields();
      var1.renderChunksMany = true;
      Field[] var4 = ReflectorRaw.getFields(var1, var3, Boolean.TYPE, Boolean.TRUE);
      var1.renderChunksMany = false;
      Field[] var5 = ReflectorRaw.getFields(var1, var3, Boolean.TYPE, Boolean.FALSE);
      var1.renderChunksMany = var2;
      HashSet var6 = new HashSet(Arrays.asList(var4));
      HashSet var7 = new HashSet(Arrays.asList(var5));
      HashSet var8 = new HashSet(var6);
      var8.retainAll(var7);
      Field[] var9 = (Field[])var8.toArray(new Field[var8.size()]);
      return var9.length != 1 ? null : var9[0];
   }
}
