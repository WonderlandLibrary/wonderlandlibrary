package net.minecraft.client.resources;

public class Language implements Comparable {
   private final String region;
   private final String languageCode;
   private final boolean bidirectional;
   private final String name;

   public int compareTo(Language var1) {
      return this.languageCode.compareTo(var1.languageCode);
   }

   public Language(String var1, String var2, String var3, boolean var4) {
      this.languageCode = var1;
      this.region = var2;
      this.name = var3;
      this.bidirectional = var4;
   }

   public boolean isBidirectional() {
      return this.bidirectional;
   }

   public int hashCode() {
      return this.languageCode.hashCode();
   }

   public String toString() {
      return String.format("%s (%s)", this.name, this.region);
   }

   public boolean equals(Object var1) {
      return this == var1 ? true : (!(var1 instanceof Language) ? false : this.languageCode.equals(((Language)var1).languageCode));
   }

   public int compareTo(Object var1) {
      return this.compareTo((Language)var1);
   }

   public String getLanguageCode() {
      return this.languageCode;
   }
}
