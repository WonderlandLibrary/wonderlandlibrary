package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
   private static final int COUNT_FACES = EnumFacing.values().length;
   private final BitSet bitSet;

   public boolean isVisible(EnumFacing var1, EnumFacing var2) {
      return this.bitSet.get(var1.ordinal() + var2.ordinal() * COUNT_FACES);
   }

   public void setManyVisible(Set var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EnumFacing var2 = (EnumFacing)var3.next();
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            EnumFacing var4 = (EnumFacing)var5.next();
            this.setVisible(var2, var4, true);
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(' ');
      EnumFacing[] var5;
      int var4 = (var5 = EnumFacing.values()).length;

      EnumFacing var2;
      int var3;
      for(var3 = 0; var3 < var4; ++var3) {
         var2 = var5[var3];
         var1.append(' ').append(var2.toString().toUpperCase().charAt(0));
      }

      var1.append('\n');
      var4 = (var5 = EnumFacing.values()).length;

      for(var3 = 0; var3 < var4; ++var3) {
         var2 = var5[var3];
         var1.append(var2.toString().toUpperCase().charAt(0));
         EnumFacing[] var9;
         int var8 = (var9 = EnumFacing.values()).length;

         for(int var7 = 0; var7 < var8; ++var7) {
            EnumFacing var6 = var9[var7];
            if (var2 == var6) {
               var1.append("  ");
            } else {
               boolean var10 = this.isVisible(var2, var6);
               var1.append(' ').append((char)(var10 ? 'Y' : 'n'));
            }
         }

         var1.append('\n');
      }

      return String.valueOf(var1);
   }

   public void setVisible(EnumFacing var1, EnumFacing var2, boolean var3) {
      this.bitSet.set(var1.ordinal() + var2.ordinal() * COUNT_FACES, var3);
      this.bitSet.set(var2.ordinal() + var1.ordinal() * COUNT_FACES, var3);
   }

   public void setAllVisible(boolean var1) {
      this.bitSet.set(0, this.bitSet.size(), var1);
   }

   public SetVisibility() {
      this.bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
   }
}
