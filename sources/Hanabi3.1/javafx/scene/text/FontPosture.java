package javafx.scene.text;

public enum FontPosture {
   REGULAR(new String[]{"", "regular"}),
   ITALIC(new String[]{"italic"});

   private final String[] names;

   private FontPosture(String... var3) {
      this.names = var3;
   }

   public static FontPosture findByName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         FontPosture[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            FontPosture var4 = var1[var3];
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
}
