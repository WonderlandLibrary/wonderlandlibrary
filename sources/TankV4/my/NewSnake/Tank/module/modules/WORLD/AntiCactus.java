package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.BoundingBoxEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

@Module.Mod
public class AntiCactus extends Module {
   @EventTarget
   private void onBoundingBox(BoundingBoxEvent var1) {
      if (var1.getBlock() instanceof BlockCactus) {
         var1.setBoundingBox(new AxisAlignedBB((double)var1.getBlockPos().getX(), (double)var1.getBlockPos().getY(), (double)var1.getBlockPos().getZ(), (double)(var1.getBlockPos().getX() + 1), var1.getBoundingBox().maxY, (double)(var1.getBlockPos().getZ() + 1)));
      }

      if (var1.getBlock() instanceof BlockLiquid) {
         var1.setBoundingBox(new AxisAlignedBB((double)var1.getBlockPos().getX(), (double)var1.getBlockPos().getY(), (double)var1.getBlockPos().getZ(), (double)(var1.getBlockPos().getX() + 1), var1.getBoundingBox().maxY, (double)(var1.getBlockPos().getZ() + 1)));
      }

      if (var1.getBlock() instanceof BlockHopper) {
         var1.setBoundingBox(new AxisAlignedBB((double)var1.getBlockPos().getX(), (double)var1.getBlockPos().getY(), (double)var1.getBlockPos().getZ(), (double)(var1.getBlockPos().getX() + 1), var1.getBoundingBox().maxY, (double)(var1.getBlockPos().getZ() + 1)));
      }

      if (var1.getBlock() instanceof Block) {
         var1.setBoundingBox(new AxisAlignedBB((double)var1.getBlockPos().getX(), (double)var1.getBlockPos().getY(), (double)var1.getBlockPos().getZ(), (double)(var1.getBlockPos().getX() + 1), var1.getBoundingBox().maxY, (double)(var1.getBlockPos().getZ() + 1)));
      }

   }
}
