package optifine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils {
   private static Field fieldHasEntities = null;
   private static boolean fieldHasEntitiesMissing = false;

   public static boolean hasEntities(Chunk var0) {
      if (fieldHasEntities == null) {
         if (fieldHasEntitiesMissing) {
            return true;
         }

         fieldHasEntities = findFieldHasEntities(var0);
         if (fieldHasEntities == null) {
            fieldHasEntitiesMissing = true;
            return true;
         }
      }

      try {
         return fieldHasEntities.getBoolean(var0);
      } catch (Exception var2) {
         Config.warn("Error calling Chunk.hasEntities");
         Config.warn(var2.getClass().getName() + " " + var2.getMessage());
         fieldHasEntitiesMissing = true;
         return true;
      }
   }

   private static Field findFieldHasEntities(Chunk var0) {
      try {
         ArrayList var1 = new ArrayList();
         ArrayList var2 = new ArrayList();
         Field[] var3 = Chunk.class.getDeclaredFields();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            Field var5 = var3[var4];
            if (var5.getType() == Boolean.TYPE) {
               var5.setAccessible(true);
               var1.add(var5);
               var2.add(var5.get(var0));
            }
         }

         var0.setHasEntities(false);
         ArrayList var13 = new ArrayList();
         Iterator var6 = var1.iterator();

         while(var6.hasNext()) {
            Object var14 = var6.next();
            var13.add(((Field)var14).get(var0));
         }

         var0.setHasEntities(true);
         ArrayList var15 = new ArrayList();
         Iterator var7 = var1.iterator();

         while(var7.hasNext()) {
            Object var16 = var7.next();
            var15.add(((Field)var16).get(var0));
         }

         ArrayList var17 = new ArrayList();

         for(int var18 = 0; var18 < var1.size(); ++var18) {
            Field var8 = (Field)var1.get(var18);
            Boolean var9 = (Boolean)var13.get(var18);
            Boolean var10 = (Boolean)var15.get(var18);
            if (!var9 && var10) {
               var17.add(var8);
               Boolean var11 = (Boolean)var2.get(var18);
               var8.set(var0, var11);
            }
         }

         if (var17.size() == 1) {
            Field var19 = (Field)var17.get(0);
            return var19;
         }
      } catch (Exception var12) {
         Config.warn(var12.getClass().getName() + " " + var12.getMessage());
      }

      Config.warn("Error finding Chunk.hasEntities");
      return null;
   }
}
