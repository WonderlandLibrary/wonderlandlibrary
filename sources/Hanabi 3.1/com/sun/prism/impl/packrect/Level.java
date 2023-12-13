package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;

class Level {
   int length;
   int size;
   private int sizeOffset;
   private int lengthOffset;

   Level(int var1, int var2, int var3) {
      this.length = var1;
      this.size = var2;
      this.sizeOffset = var3;
   }

   boolean add(Rectangle var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.lengthOffset + var4 <= this.length && var5 <= this.size) {
         if (var6) {
            var1.x = this.sizeOffset;
            var1.y = this.lengthOffset;
         } else {
            var1.x = this.lengthOffset;
            var1.y = this.sizeOffset;
         }

         this.lengthOffset += var4;
         var1.x += var2;
         var1.y += var3;
         return true;
      } else {
         return false;
      }
   }
}
