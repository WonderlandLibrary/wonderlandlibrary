package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {
   public static final PropertyEnum LOG_AXIS = PropertyEnum.create("axis", BlockLog.EnumAxis.class);

   public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return super.onBlockPlaced(var1, var2, var3, var4, var5, var6, var7, var8).withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(var3.getAxis()));
   }

   public BlockLog() {
      super(Material.wood);
      this.setCreativeTab(CreativeTabs.tabBlock);
      this.setHardness(2.0F);
      this.setStepSound(soundTypeWood);
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      byte var4 = 4;
      int var5 = var4 + 1;
      if (var1.isAreaLoaded(var2.add(-var5, -var5, -var5), var2.add(var5, var5, var5))) {
         Iterator var7 = BlockPos.getAllInBox(var2.add(-var4, -var4, -var4), var2.add(var4, var4, var4)).iterator();

         while(var7.hasNext()) {
            BlockPos var6 = (BlockPos)var7.next();
            IBlockState var8 = var1.getBlockState(var6);
            if (var8.getBlock().getMaterial() == Material.leaves && !(Boolean)var8.getValue(BlockLeaves.CHECK_DECAY)) {
               var1.setBlockState(var6, var8.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
            }
         }
      }

   }

   public static enum EnumAxis implements IStringSerializable {
      NONE("none");

      private final String name;
      private static final BlockLog.EnumAxis[] ENUM$VALUES = new BlockLog.EnumAxis[]{X, Y, Z, NONE};
      X("x"),
      Y("y");

      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis;
      Z("z");

      private EnumAxis(String var3) {
         this.name = var3;
      }

      public String toString() {
         return this.name;
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

      public String getName() {
         return this.name;
      }

      public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis var0) {
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing$Axis()[var0.ordinal()]) {
         case 1:
            return X;
         case 2:
            return Y;
         case 3:
            return Z;
         default:
            return NONE;
         }
      }
   }
}
