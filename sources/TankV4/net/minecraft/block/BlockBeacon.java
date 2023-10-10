package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BlockBeacon extends BlockContainer {
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityBeacon();
   }

   public int getRenderType() {
      return 3;
   }

   public static void updateColorAsync(World var0, BlockPos var1) {
      HttpUtil.field_180193_a.submit(new Runnable(var0, var1) {
         private final World val$worldIn;
         private final BlockPos val$glassPos;

         public void run() {
            Chunk var1 = this.val$worldIn.getChunkFromBlockCoords(this.val$glassPos);

            for(int var2 = this.val$glassPos.getY() - 1; var2 >= 0; --var2) {
               BlockPos var3 = new BlockPos(this.val$glassPos.getX(), var2, this.val$glassPos.getZ());
               if (!var1.canSeeSky(var3)) {
                  break;
               }

               IBlockState var4 = this.val$worldIn.getBlockState(var3);
               if (var4.getBlock() == Blocks.beacon) {
                  ((WorldServer)this.val$worldIn).addScheduledTask(new Runnable(this, this.val$worldIn, var3) {
                     private final World val$worldIn;
                     private final BlockPos val$blockpos;
                     final <undefinedtype> this$1;

                     {
                        this.this$1 = var1;
                        this.val$worldIn = var2;
                        this.val$blockpos = var3;
                     }

                     public void run() {
                        TileEntity var1 = this.val$worldIn.getTileEntity(this.val$blockpos);
                        if (var1 instanceof TileEntityBeacon) {
                           ((TileEntityBeacon)var1).updateBeacon();
                           this.val$worldIn.addBlockEvent(this.val$blockpos, Blocks.beacon, 1, 0);
                        }

                     }
                  });
               }
            }

         }

         {
            this.val$worldIn = var1;
            this.val$glassPos = var2;
         }
      });
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public BlockBeacon() {
      super(Material.glass, MapColor.diamondColor);
      this.setHardness(3.0F);
      this.setCreativeTab(CreativeTabs.tabMisc);
   }

   public boolean isFullCube() {
      return false;
   }

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      TileEntity var5 = var1.getTileEntity(var2);
      if (var5 instanceof TileEntityBeacon) {
         ((TileEntityBeacon)var5).updateBeacon();
         var1.addBlockEvent(var2, this, 1, 0);
      }

   }

   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if (var9 instanceof TileEntityBeacon) {
            var4.displayGUIChest((TileEntityBeacon)var9);
            var4.triggerAchievement(StatList.field_181730_N);
         }

         return true;
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      super.onBlockPlacedBy(var1, var2, var3, var4, var5);
      if (var5.hasDisplayName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if (var6 instanceof TileEntityBeacon) {
            ((TileEntityBeacon)var6).setName(var5.getDisplayName());
         }
      }

   }
}
