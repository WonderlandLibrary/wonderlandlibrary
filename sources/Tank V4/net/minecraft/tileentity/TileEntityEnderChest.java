package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

public class TileEntityEnderChest extends TileEntity implements ITickable {
   private int ticksSinceSync;
   public float prevLidAngle;
   public float lidAngle;
   public int numPlayersUsing;

   public void update() {
      if (++this.ticksSinceSync % 20 * 4 == 0) {
         this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
      }

      this.prevLidAngle = this.lidAngle;
      int var1 = this.pos.getX();
      int var2 = this.pos.getY();
      int var3 = this.pos.getZ();
      float var4 = 0.1F;
      double var7;
      if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
         double var5 = (double)var1 + 0.5D;
         var7 = (double)var3 + 0.5D;
         this.worldObj.playSoundEffect(var5, (double)var2 + 0.5D, var7, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }

      if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
         float var11 = this.lidAngle;
         if (this.numPlayersUsing > 0) {
            this.lidAngle += var4;
         } else {
            this.lidAngle -= var4;
         }

         if (this.lidAngle > 1.0F) {
            this.lidAngle = 1.0F;
         }

         float var6 = 0.5F;
         if (this.lidAngle < var6 && var11 >= var6) {
            var7 = (double)var1 + 0.5D;
            double var9 = (double)var3 + 0.5D;
            this.worldObj.playSoundEffect(var7, (double)var2 + 0.5D, var9, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
         }

         if (this.lidAngle < 0.0F) {
            this.lidAngle = 0.0F;
         }
      }

   }

   public void closeChest() {
      --this.numPlayersUsing;
      this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
   }

   public void openChest() {
      ++this.numPlayersUsing;
      this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
   }

   public void invalidate() {
      this.updateContainingBlockInfo();
      super.invalidate();
   }

   public boolean receiveClientEvent(int var1, int var2) {
      if (var1 == 1) {
         this.numPlayersUsing = var2;
         return true;
      } else {
         return super.receiveClientEvent(var1, var2);
      }
   }

   public boolean canBeUsed(EntityPlayer var1) {
      return this.worldObj.getTileEntity(this.pos) != this ? false : var1.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
   }
}
