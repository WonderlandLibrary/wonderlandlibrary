package net.minecraft.block;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import optifine.Config;

public class BlockLeavesBase extends Block {
   private static Map mapOriginalOpacity = new IdentityHashMap();
   protected boolean fancyGraphics;
   private static final String __OBFID = "CL_00000326";

   public static void restoreLightOpacity(Block var0) {
      if (mapOriginalOpacity.containsKey(var0)) {
         int var1 = (Integer)mapOriginalOpacity.get(var0);
         setLightOpacity(var0, var1);
      }

   }

   public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      return Config.isCullFacesLeaves() && var1.getBlockState(var2).getBlock() == this ? false : super.shouldSideBeRendered(var1, var2, var3);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public static void setLightOpacity(Block var0, int var1) {
      if (!mapOriginalOpacity.containsKey(var0)) {
         mapOriginalOpacity.put(var0, var0.getLightOpacity());
      }

      var0.setLightOpacity(var1);
   }

   protected BlockLeavesBase(Material var1, boolean var2) {
      super(var1);
      this.fancyGraphics = var2;
   }
}
