package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MapGenStructureData extends WorldSavedData {
   private NBTTagCompound tagCompound = new NBTTagCompound();

   public static String formatChunkCoords(int var0, int var1) {
      return "[" + var0 + "," + var1 + "]";
   }

   public MapGenStructureData(String var1) {
      super(var1);
   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.setTag("Features", this.tagCompound);
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.tagCompound = var1.getCompoundTag("Features");
   }

   public NBTTagCompound getTagCompound() {
      return this.tagCompound;
   }

   public void writeInstance(NBTTagCompound var1, int var2, int var3) {
      this.tagCompound.setTag(formatChunkCoords(var2, var3), var1);
   }
}
