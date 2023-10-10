package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {
   private final List toMove = Lists.newArrayList();
   private final BlockPos blockToMove;
   private final List toDestroy = Lists.newArrayList();
   private final World world;
   private final BlockPos pistonPos;
   private final EnumFacing moveDirection;

   public List getBlocksToDestroy() {
      return this.toDestroy;
   }

   public boolean canMove() {
      this.toMove.clear();
      this.toDestroy.clear();
      Block var1 = this.world.getBlockState(this.blockToMove).getBlock();
      if (!BlockPistonBase.canPush(var1, this.world, this.blockToMove, this.moveDirection, false)) {
         if (var1.getMobilityFlag() != 1) {
            return false;
         } else {
            this.toDestroy.add(this.blockToMove);
            return true;
         }
      } else if (this == this.blockToMove) {
         return false;
      } else {
         for(int var2 = 0; var2 < this.toMove.size(); ++var2) {
            BlockPos var3 = (BlockPos)this.toMove.get(var2);
            if (this.world.getBlockState(var3).getBlock() == Blocks.slime_block && var3 > 0) {
               return false;
            }
         }

         return true;
      }
   }

   public List getBlocksToMove() {
      return this.toMove;
   }

   public BlockPistonStructureHelper(World var1, BlockPos var2, EnumFacing var3, boolean var4) {
      this.world = var1;
      this.pistonPos = var2;
      if (var4) {
         this.moveDirection = var3;
         this.blockToMove = var2.offset(var3);
      } else {
         this.moveDirection = var3.getOpposite();
         this.blockToMove = var2.offset(var3, 2);
      }

   }

   private void func_177255_a(int var1, int var2) {
      ArrayList var3 = Lists.newArrayList();
      ArrayList var4 = Lists.newArrayList();
      ArrayList var5 = Lists.newArrayList();
      var3.addAll(this.toMove.subList(0, var2));
      var4.addAll(this.toMove.subList(this.toMove.size() - var1, this.toMove.size()));
      var5.addAll(this.toMove.subList(var2, this.toMove.size() - var1));
      this.toMove.clear();
      this.toMove.addAll(var3);
      this.toMove.addAll(var4);
      this.toMove.addAll(var5);
   }
}
