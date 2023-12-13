package joptsimple.util;

public final class KeyValuePair {
   public final String key;
   public final String value;

   private KeyValuePair(String var1, String var2) {
      this.key = var1;
      this.value = var2;
   }

   public static KeyValuePair valueOf(String var0) {
      int var1 = var0.indexOf(61);
      if (var1 == -1) {
         return new KeyValuePair(var0, "");
      } else {
         String var2 = var0.substring(0, var1);
         String var3 = var1 == var0.length() - 1 ? "" : var0.substring(var1 + 1);
         return new KeyValuePair(var2, var3);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof KeyValuePair)) {
         return false;
      } else {
         KeyValuePair var2 = (KeyValuePair)var1;
         return this.key.equals(var2.key) && this.value.equals(var2.value);
      }
   }

   public int hashCode() {
      return this.key.hashCode() ^ this.value.hashCode();
   }

   public String toString() {
      return this.key + '=' + this.value;
   }
}
