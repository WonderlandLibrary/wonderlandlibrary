package javafx.scene.text;

public enum FontWeight {
   THIN(100, new String[]{"Thin"}),
   EXTRA_LIGHT(200, new String[]{"Extra Light", "Ultra Light"}),
   LIGHT(300, new String[]{"Light"}),
   NORMAL(400, new String[]{"Normal", "Regular"}),
   MEDIUM(500, new String[]{"Medium"}),
   SEMI_BOLD(600, new String[]{"Semi Bold", "Demi Bold"}),
   BOLD(700, new String[]{"Bold"}),
   EXTRA_BOLD(800, new String[]{"Extra Bold", "Ultra Bold"}),
   BLACK(900, new String[]{"Black", "Heavy"});

   private final int weight;
   private final String[] names;

   private FontWeight(int var3, String... var4) {
      this.weight = var3;
      this.names = var4;
   }

   public int getWeight() {
      return this.weight;
   }

   public static FontWeight findByName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         FontWeight[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            FontWeight var4 = var1[var3];
            String[] var5 = var4.names;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               if (var8.equalsIgnoreCase(var0)) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   public static FontWeight findByWeight(int var0) {
      if (var0 <= 150) {
         return THIN;
      } else if (var0 <= 250) {
         return EXTRA_LIGHT;
      } else if (var0 < 350) {
         return LIGHT;
      } else if (var0 <= 450) {
         return NORMAL;
      } else if (var0 <= 550) {
         return MEDIUM;
      } else if (var0 < 650) {
         return SEMI_BOLD;
      } else if (var0 <= 750) {
         return BOLD;
      } else {
         return var0 <= 850 ? EXTRA_BOLD : BLACK;
      }
   }
}
