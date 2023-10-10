package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
   public final String mapName;
   private boolean dirty;

   public abstract void writeToNBT(NBTTagCompound var1);

   public abstract void readFromNBT(NBTTagCompound var1);

   public void setDirty(boolean var1) {
      this.dirty = var1;
   }

   public WorldSavedData(String var1) {
      this.mapName = var1;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void markDirty() {
      this.setDirty(true);
   }
}
