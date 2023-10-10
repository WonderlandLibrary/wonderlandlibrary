package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;

public class DestroyBlockProgress {
   private int partialBlockProgress;
   private final int miningPlayerEntId;
   private final BlockPos position;
   private int createdAtCloudUpdateTick;

   public void setCloudUpdateTick(int var1) {
      this.createdAtCloudUpdateTick = var1;
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void setPartialBlockDamage(int var1) {
      if (var1 > 10) {
         var1 = 10;
      }

      this.partialBlockProgress = var1;
   }

   public int getCreationCloudUpdateTick() {
      return this.createdAtCloudUpdateTick;
   }

   public int getPartialBlockDamage() {
      return this.partialBlockProgress;
   }

   public DestroyBlockProgress(int var1, BlockPos var2) {
      this.miningPlayerEntId = var1;
      this.position = var2;
   }
}
