/*    */ package org.neverhook.client.helpers.world;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockHelper
/*    */   implements Helper
/*    */ {
/*    */   public static Block getBlock(BlockPos pos) {
/* 18 */     return mc.world.getBlockState(pos).getBlock();
/*    */   }
/*    */   
/*    */   public static BlockPos getPlayerPos() {
/* 22 */     return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
/*    */   }
/*    */   
/*    */   public static boolean IsValidBlockPos(BlockPos pos) {
/* 26 */     IBlockState state = mc.world.getBlockState(pos);
/* 27 */     if (state.getBlock() instanceof net.minecraft.block.BlockDirt || (state.getBlock() instanceof net.minecraft.block.BlockGrass && !(state.getBlock() instanceof net.minecraft.block.BlockFarmland)))
/* 28 */       return (mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR); 
/* 29 */     return false;
/*    */   }
/*    */   
/*    */   public static List<BlockPos> getSphere(BlockPos blockPos, float offset, int range, boolean hollow, boolean sphere) {
/* 33 */     ArrayList<BlockPos> blockPosList = new ArrayList<>();
/* 34 */     int blockPosX = blockPos.getX();
/* 35 */     int blockPosY = blockPos.getY();
/* 36 */     int blockPosZ = blockPos.getZ();
/* 37 */     float x = blockPosX - offset;
/* 38 */     while (x <= blockPosX + offset) {
/* 39 */       float z = blockPosZ - offset;
/* 40 */       while (z <= blockPosZ + offset) {
/* 41 */         float y = sphere ? (blockPosY - offset) : blockPosY;
/*    */         while (true) {
/* 43 */           float f = sphere ? (blockPosY + offset) : (blockPosY + range);
/* 44 */           if (y >= f) {
/*    */             break;
/*    */           }
/* 47 */           float dist = (blockPosX - x) * (blockPosX - x) + (blockPosZ - z) * (blockPosZ - z) + (sphere ? ((blockPosY - y) * (blockPosY - y)) : 0.0F);
/* 48 */           if (dist < offset * offset && (!hollow || dist >= offset - 1.0F * (offset - 1.0F))) {
/* 49 */             BlockPos pos = new BlockPos(x, y, z);
/* 50 */             blockPosList.add(pos);
/*    */           } 
/* 52 */           y++;
/*    */         } 
/* 54 */         z++;
/*    */       } 
/* 56 */       x++;
/*    */     } 
/* 58 */     return blockPosList;
/*    */   }
/*    */   
/*    */   public static ArrayList<BlockPos> getBlocks(int x, int y, int z) {
/* 62 */     BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
/* 63 */     BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
/* 64 */     return getAllInBox(min, max);
/*    */   }
/*    */   
/*    */   public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
/* 68 */     ArrayList<BlockPos> blocks = new ArrayList<>();
/*    */     
/* 70 */     BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
/* 71 */     BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
/*    */     
/* 73 */     for (int x = min.getX(); x <= max.getX(); x++) {
/* 74 */       for (int y = min.getY(); y <= max.getY(); y++) {
/* 75 */         for (int z = min.getZ(); z <= max.getZ(); z++) {
/* 76 */           blocks.add(new BlockPos(x, y, z));
/*    */         }
/*    */       } 
/*    */     } 
/* 80 */     return blocks;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\world\BlockHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */