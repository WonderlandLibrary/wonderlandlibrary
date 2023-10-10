package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityPiston extends TileEntity implements ITickable {
   private float progress;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;
   private float lastProgress;
   private List field_174933_k = Lists.newArrayList();
   private boolean shouldHeadBeRendered;
   private EnumFacing pistonFacing;
   private IBlockState pistonState;
   private boolean extending;

   public float getOffsetX(float var1) {
      return this.extending ? (this.getProgress(var1) - 1.0F) * (float)this.pistonFacing.getFrontOffsetX() : (1.0F - this.getProgress(var1)) * (float)this.pistonFacing.getFrontOffsetX();
   }

   private void launchWithSlimeBlock(float var1, float var2) {
      if (this.extending) {
         var1 = 1.0F - var1;
      } else {
         --var1;
      }

      AxisAlignedBB var3 = Blocks.piston_extension.getBoundingBox(this.worldObj, this.pos, this.pistonState, var1, this.pistonFacing);
      if (var3 != null) {
         List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, var3);
         if (!var4.isEmpty()) {
            this.field_174933_k.addAll(var4);
            Iterator var6 = this.field_174933_k.iterator();

            while(true) {
               while(var6.hasNext()) {
                  Entity var5 = (Entity)var6.next();
                  if (this.pistonState.getBlock() == Blocks.slime_block && this.extending) {
                     switch($SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis()[this.pistonFacing.getAxis().ordinal()]) {
                     case 1:
                        var5.motionX = (double)this.pistonFacing.getFrontOffsetX();
                        break;
                     case 2:
                        var5.motionY = (double)this.pistonFacing.getFrontOffsetY();
                        break;
                     case 3:
                        var5.motionZ = (double)this.pistonFacing.getFrontOffsetZ();
                     }
                  } else {
                     var5.moveEntity((double)(var2 * (float)this.pistonFacing.getFrontOffsetX()), (double)(var2 * (float)this.pistonFacing.getFrontOffsetY()), (double)(var2 * (float)this.pistonFacing.getFrontOffsetZ()));
                  }
               }

               this.field_174933_k.clear();
               break;
            }
         }
      }

   }

   public float getOffsetY(float var1) {
      return this.extending ? (this.getProgress(var1) - 1.0F) * (float)this.pistonFacing.getFrontOffsetY() : (1.0F - this.getProgress(var1)) * (float)this.pistonFacing.getFrontOffsetY();
   }

   public float getProgress(float var1) {
      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      return this.lastProgress + (this.progress - this.lastProgress) * var1;
   }

   public TileEntityPiston() {
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.Axis.values().length];

         try {
            var0[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis = var0;
         return var0;
      }
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.pistonState = Block.getBlockById(var1.getInteger("blockId")).getStateFromMeta(var1.getInteger("blockData"));
      this.pistonFacing = EnumFacing.getFront(var1.getInteger("facing"));
      this.lastProgress = this.progress = var1.getFloat("progress");
      this.extending = var1.getBoolean("extending");
   }

   public TileEntityPiston(IBlockState var1, EnumFacing var2, boolean var3, boolean var4) {
      this.pistonState = var1;
      this.pistonFacing = var2;
      this.extending = var3;
      this.shouldHeadBeRendered = var4;
   }

   public void update() {
      this.lastProgress = this.progress;
      if (this.lastProgress >= 1.0F) {
         this.launchWithSlimeBlock(1.0F, 0.25F);
         this.worldObj.removeTileEntity(this.pos);
         this.invalidate();
         if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
            this.worldObj.setBlockState(this.pos, this.pistonState, 3);
            this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
         }
      } else {
         this.progress += 0.5F;
         if (this.progress >= 1.0F) {
            this.progress = 1.0F;
         }

         if (this.extending) {
            this.launchWithSlimeBlock(this.progress, this.progress - this.lastProgress + 0.0625F);
         }
      }

   }

   public IBlockState getPistonState() {
      return this.pistonState;
   }

   public boolean isExtending() {
      return this.extending;
   }

   public EnumFacing getFacing() {
      return this.pistonFacing;
   }

   public boolean shouldPistonHeadBeRendered() {
      return this.shouldHeadBeRendered;
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      var1.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
      var1.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
      var1.setInteger("facing", this.pistonFacing.getIndex());
      var1.setFloat("progress", this.lastProgress);
      var1.setBoolean("extending", this.extending);
   }

   public void clearPistonTileEntity() {
      if (this.lastProgress < 1.0F && this.worldObj != null) {
         this.lastProgress = this.progress = 1.0F;
         this.worldObj.removeTileEntity(this.pos);
         this.invalidate();
         if (this.worldObj.getBlockState(this.pos).getBlock() == Blocks.piston_extension) {
            this.worldObj.setBlockState(this.pos, this.pistonState, 3);
            this.worldObj.notifyBlockOfStateChange(this.pos, this.pistonState.getBlock());
         }
      }

   }

   public int getBlockMetadata() {
      return 0;
   }

   public float getOffsetZ(float var1) {
      return this.extending ? (this.getProgress(var1) - 1.0F) * (float)this.pistonFacing.getFrontOffsetZ() : (1.0F - this.getProgress(var1)) * (float)this.pistonFacing.getFrontOffsetZ();
   }
}
