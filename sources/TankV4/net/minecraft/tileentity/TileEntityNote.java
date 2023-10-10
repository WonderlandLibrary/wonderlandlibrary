package net.minecraft.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityNote extends TileEntity {
   public byte note;
   public boolean previousRedstoneState;

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.note = var1.getByte("note");
      this.note = (byte)MathHelper.clamp_int(this.note, 0, 24);
   }

   public void triggerNote(World var1, BlockPos var2) {
      if (var1.getBlockState(var2.up()).getBlock().getMaterial() == Material.air) {
         Material var3 = var1.getBlockState(var2.down()).getBlock().getMaterial();
         byte var4 = 0;
         if (var3 == Material.rock) {
            var4 = 1;
         }

         if (var3 == Material.sand) {
            var4 = 2;
         }

         if (var3 == Material.glass) {
            var4 = 3;
         }

         if (var3 == Material.wood) {
            var4 = 4;
         }

         var1.addBlockEvent(var2, Blocks.noteblock, var4, this.note);
      }

   }

   public void changePitch() {
      this.note = (byte)((this.note + 1) % 25);
      this.markDirty();
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      var1.setByte("note", this.note);
   }
}
