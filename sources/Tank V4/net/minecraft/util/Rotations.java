package net.minecraft.util;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations {
   protected final float x;
   protected final float y;
   protected final float z;

   public float getX() {
      return this.x;
   }

   public float getZ() {
      return this.z;
   }

   public Rotations(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Rotations)) {
         return false;
      } else {
         Rotations var2 = (Rotations)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }

   public Rotations(NBTTagList var1) {
      this.x = var1.getFloatAt(0);
      this.y = var1.getFloatAt(1);
      this.z = var1.getFloatAt(2);
   }

   public float getY() {
      return this.y;
   }

   public NBTTagList writeToNBT() {
      NBTTagList var1 = new NBTTagList();
      var1.appendTag(new NBTTagFloat(this.x));
      var1.appendTag(new NBTTagFloat(this.y));
      var1.appendTag(new NBTTagFloat(this.z));
      return var1;
   }
}
