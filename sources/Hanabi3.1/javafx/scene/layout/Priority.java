package javafx.scene.layout;

public enum Priority {
   ALWAYS,
   SOMETIMES,
   NEVER;

   public static Priority max(Priority var0, Priority var1) {
      if (var0 != ALWAYS && var1 != ALWAYS) {
         return var0 != SOMETIMES && var1 != SOMETIMES ? NEVER : SOMETIMES;
      } else {
         return ALWAYS;
      }
   }

   public static Priority min(Priority var0, Priority var1) {
      if (var0 != NEVER && var1 != NEVER) {
         return var0 != SOMETIMES && var1 != SOMETIMES ? ALWAYS : SOMETIMES;
      } else {
         return NEVER;
      }
   }
}
