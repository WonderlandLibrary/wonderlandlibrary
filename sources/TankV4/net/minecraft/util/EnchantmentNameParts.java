package net.minecraft.util;

import java.util.Random;

public class EnchantmentNameParts {
   private Random rand = new Random();
   private static final EnchantmentNameParts instance = new EnchantmentNameParts();
   private String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale ".split(" ");

   public static EnchantmentNameParts getInstance() {
      return instance;
   }

   public void reseedRandomGenerator(long var1) {
      this.rand.setSeed(var1);
   }

   public String generateNewRandomName() {
      int var1 = this.rand.nextInt(2) + 3;
      String var2 = "";

      for(int var3 = 0; var3 < var1; ++var3) {
         if (var3 > 0) {
            var2 = var2 + " ";
         }

         var2 = var2 + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
      }

      return var2;
   }
}
