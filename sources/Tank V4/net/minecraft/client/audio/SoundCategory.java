package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;

public enum SoundCategory {
   private static final SoundCategory[] ENUM$VALUES = new SoundCategory[]{MASTER, MUSIC, RECORDS, WEATHER, BLOCKS, MOBS, ANIMALS, PLAYERS, AMBIENT};
   private static final Map ID_CATEGORY_MAP = Maps.newHashMap();
   MASTER("master", 0);

   private final int categoryId;
   WEATHER("weather", 3),
   PLAYERS("player", 7),
   RECORDS("record", 2),
   MUSIC("music", 1),
   BLOCKS("block", 4),
   AMBIENT("ambient", 8),
   ANIMALS("neutral", 6);

   private static final Map NAME_CATEGORY_MAP = Maps.newHashMap();
   MOBS("hostile", 5);

   private final String categoryName;

   static {
      SoundCategory[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         SoundCategory var0 = var3[var1];
         if (NAME_CATEGORY_MAP.containsKey(var0.getCategoryName()) || ID_CATEGORY_MAP.containsKey(var0.getCategoryId())) {
            throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + var0);
         }

         NAME_CATEGORY_MAP.put(var0.getCategoryName(), var0);
         ID_CATEGORY_MAP.put(var0.getCategoryId(), var0);
      }

   }

   public static SoundCategory getCategory(String var0) {
      return (SoundCategory)NAME_CATEGORY_MAP.get(var0);
   }

   private SoundCategory(String var3, int var4) {
      this.categoryName = var3;
      this.categoryId = var4;
   }

   public int getCategoryId() {
      return this.categoryId;
   }

   public String getCategoryName() {
      return this.categoryName;
   }
}
