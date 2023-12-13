package shadersmod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.MatchBlock;
import optifine.PropertiesOrdered;
import optifine.StrUtils;

public class BlockAliases {
   private static BlockAlias[][] blockAliases = null;

   public static int getMappedBlockId(int var0, int var1) {
      if (blockAliases == null) {
         return var0;
      } else if (var0 >= 0 && var0 < blockAliases.length) {
         BlockAlias[] var2 = blockAliases[var0];
         if (var2 == null) {
            return var0;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               BlockAlias var4 = var2[var3];
               if (var4.matches(var0, var1)) {
                  return var4.getBlockId();
               }
            }

            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void update(IShaderPack var0) {
      reset();
      String var1 = "/shaders/block.properties";

      try {
         InputStream var2 = var0.getResourceAsStream(var1);
         if (var2 == null) {
            return;
         }

         PropertiesOrdered var3 = new PropertiesOrdered();
         var3.load(var2);
         var2.close();
         Config.dbg("[Shaders] Parsing block mappings: " + var1);
         ArrayList var4 = new ArrayList();
         ConnectedParser var5 = new ConnectedParser("Shaders");
         Iterator var7 = var3.keySet().iterator();

         while(true) {
            while(var7.hasNext()) {
               Object var6 = var7.next();
               String var8 = (String)var6;
               String var9 = var3.getProperty(var8);
               String var10 = "block.";
               if (!var8.startsWith(var10)) {
                  Config.warn("[Shaders] Invalid block ID: " + var8);
               } else {
                  String var11 = StrUtils.removePrefix(var8, var10);
                  int var12 = Config.parseInt(var11, -1);
                  if (var12 < 0) {
                     Config.warn("[Shaders] Invalid block ID: " + var8);
                  } else {
                     MatchBlock[] var13 = var5.parseMatchBlocks(var9);
                     if (var13 != null && var13.length >= 1) {
                        BlockAlias var14 = new BlockAlias(var12, var13);
                        addToList(var4, var14);
                     } else {
                        Config.warn("[Shaders] Invalid block ID mapping: " + var8 + "=" + var9);
                     }
                  }
               }
            }

            if (var4.size() <= 0) {
               return;
            }

            blockAliases = toArrays(var4);
            break;
         }
      } catch (IOException var15) {
         Config.warn("[Shaders] Error reading: " + var1);
      }

   }

   private static void addToList(List var0, BlockAlias var1) {
      int[] var2 = var1.getMatchBlockIds();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         int var4 = var2[var3];

         while(var4 >= var0.size()) {
            var0.add((Object)null);
         }

         Object var5 = (List)var0.get(var4);
         if (var5 == null) {
            var5 = new ArrayList();
            var0.set(var4, var5);
         }

         ((List)var5).add(var1);
      }

   }

   private static BlockAlias[][] toArrays(List var0) {
      BlockAlias[][] var1 = new BlockAlias[var0.size()][];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         List var3 = (List)var0.get(var2);
         if (var3 != null) {
            var1[var2] = (BlockAlias[])var3.toArray(new BlockAlias[var3.size()]);
         }
      }

      return var1;
   }

   public static void reset() {
      blockAliases = null;
   }
}
