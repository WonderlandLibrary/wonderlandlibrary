package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
   private final BlockPressurePlate.Sensitivity sensitivity;
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockPressurePlate$Sensitivity;

   protected int computeRedstoneStrength(World var1, BlockPos var2) {
      AxisAlignedBB var3 = this.getSensitiveAABB(var2);
      List var4;
      switch($SWITCH_TABLE$net$minecraft$block$BlockPressurePlate$Sensitivity()[this.sensitivity.ordinal()]) {
      case 1:
         var4 = var1.getEntitiesWithinAABBExcludingEntity((Entity)null, var3);
         break;
      case 2:
         var4 = var1.getEntitiesWithinAABB(EntityLivingBase.class, var3);
         break;
      default:
         return 0;
      }

      if (!var4.isEmpty()) {
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            Entity var5 = (Entity)var6.next();
            if (!var5.doesEntityNotTriggerPressurePlate()) {
               return 15;
            }
         }
      }

      return 0;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{POWERED});
   }

   public int getMetaFromState(IBlockState var1) {
      return (Boolean)var1.getValue(POWERED) ? 1 : 0;
   }

   protected int getRedstoneStrength(IBlockState var1) {
      return (Boolean)var1.getValue(POWERED) ? 15 : 0;
   }

   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWERED, var1 == 1);
   }

   static int[] $SWITCH_TABLE$net$minecraft$block$BlockPressurePlate$Sensitivity() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockPressurePlate$Sensitivity;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[BlockPressurePlate.Sensitivity.values().length];

         try {
            var0[BlockPressurePlate.Sensitivity.EVERYTHING.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[BlockPressurePlate.Sensitivity.MOBS.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$block$BlockPressurePlate$Sensitivity = var0;
         return var0;
      }
   }

   protected IBlockState setRedstoneStrength(IBlockState var1, int var2) {
      return var1.withProperty(POWERED, var2 > 0);
   }

   protected BlockPressurePlate(Material var1, BlockPressurePlate.Sensitivity var2) {
      super(var1);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
      this.sensitivity = var2;
   }

   public static enum Sensitivity {
      EVERYTHING;

      private static final BlockPressurePlate.Sensitivity[] ENUM$VALUES = new BlockPressurePlate.Sensitivity[]{EVERYTHING, MOBS};
      MOBS;
   }
}
