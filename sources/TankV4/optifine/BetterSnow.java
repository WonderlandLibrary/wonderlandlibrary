package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BetterSnow {
   private static IBakedModel modelSnowLayer = null;

   public static void update() {
      modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.snow_layer.getDefaultState());
   }

   public static IBakedModel getModelSnowLayer() {
      return modelSnowLayer;
   }

   public static IBlockState getStateSnowLayer() {
      return Blocks.snow_layer.getDefaultState();
   }

   public static boolean shouldRender(IBlockAccess var0, Block var1, IBlockState var2, BlockPos var3) {
      return var2 != false ? false : hasSnowNeighbours(var0, var3);
   }

   private static boolean hasSnowNeighbours(IBlockAccess var0, BlockPos var1) {
      Block var2 = Blocks.snow_layer;
      return var0.getBlockState(var1.north()).getBlock() != var2 && var0.getBlockState(var1.south()).getBlock() != var2 && var0.getBlockState(var1.west()).getBlock() != var2 && var0.getBlockState(var1.east()).getBlock() != var2 ? false : var0.getBlockState(var1.down()).getBlock().isOpaqueCube();
   }
}
