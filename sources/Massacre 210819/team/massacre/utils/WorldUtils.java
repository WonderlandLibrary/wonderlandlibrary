package team.massacre.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

public final class WorldUtils {
   private WorldUtils() {
   }

   public static WorldClient getWorld() {
      return Minecraft.getMinecraft().theWorld;
   }

   public static Block getBlock(BlockPos pos) {
      return getWorld().getBlockState(pos).getBlock();
   }
}
