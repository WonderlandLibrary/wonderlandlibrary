package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.alphacentauri.management.events.EventSlowdown;
import org.alphacentauri.management.events.EventSlowdown$Cause;

public class BlockSoulSand extends Block {
   public BlockSoulSand() {
      super(Material.sand, MapColor.brownColor);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if(entityIn instanceof EntityPlayerSP) {
         float noSlowDown = ((EventSlowdown)(new EventSlowdown(EventSlowdown$Cause.SoulSand)).fire()).getNoSlowDown();
         entityIn.motionX *= 0.4D + 0.6D * (double)noSlowDown;
         entityIn.motionZ *= 0.4D + 0.6D * (double)noSlowDown;
      } else {
         entityIn.motionX *= 0.4D;
         entityIn.motionZ *= 0.4D;
      }

   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      float f = 0.125F;
      return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)((float)(pos.getY() + 1) - f), (double)(pos.getZ() + 1));
   }
}
