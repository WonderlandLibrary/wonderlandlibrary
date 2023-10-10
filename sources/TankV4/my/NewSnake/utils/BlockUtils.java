package my.NewSnake.utils;

import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BlockUtils {
   public static Minecraft mc = Minecraft.getMinecraft();

   public static Block getBlock(double var0, double var2, double var4) {
      return Minecraft.theWorld.getBlockState(new BlockPos(var0, var2, var4)).getBlock();
   }

   public static boolean isOnLiquidFixed() {
      return Minecraft.theWorld.handleMaterialAcceleration(Minecraft.thePlayer.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, Minecraft.thePlayer);
   }

   public static boolean isOnLiquid() {
      AxisAlignedBB var0 = Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -0.1D, 0.0D).contract(0.001D, 0.0D, 0.001D);
      int var1 = MathHelper.floor_double(var0.minX);
      int var2 = MathHelper.floor_double(var0.maxX + 1.0D);
      int var3 = MathHelper.floor_double(var0.minY);
      int var4 = MathHelper.floor_double(var0.maxY + 1.0D);
      int var5 = MathHelper.floor_double(var0.minZ);
      int var6 = MathHelper.floor_double(var0.maxZ + 1.0D);
      boolean var7 = false;

      for(int var8 = var3; var8 < var4; ++var8) {
         for(int var9 = var1; var9 < var2; ++var9) {
            for(int var10 = var5; var10 < var6; ++var10) {
               Block var11 = Minecraft.theWorld.getBlockState(new BlockPos(var9, var8, var10)).getBlock();
               if (var11 instanceof BlockLiquid) {
                  var7 = true;
               }

               if (!(var11 instanceof BlockLiquid) && var11.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(var9, var8, var10), Minecraft.theWorld.getBlockState(new BlockPos(var9, var8, var10))) != null) {
                  return false;
               }
            }
         }
      }

      return var7;
   }

   public static boolean isInLiquid() {
      AxisAlignedBB var0 = Minecraft.thePlayer.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D);
      int var1 = MathHelper.floor_double(var0.minX);
      int var2 = MathHelper.floor_double(var0.maxX + 1.0D);
      int var3 = MathHelper.floor_double(var0.minY);
      int var4 = MathHelper.floor_double(var0.maxY + 1.0D);
      int var5 = MathHelper.floor_double(var0.minZ);
      int var6 = MathHelper.floor_double(var0.maxZ + 1.0D);

      for(int var7 = var1; var7 < var2; ++var7) {
         for(int var8 = var3; var8 < var4; ++var8) {
            for(int var9 = var5; var9 < var6; ++var9) {
               BlockPos var10 = new BlockPos(var7, var8, var9);
               Block var11 = Minecraft.theWorld.getBlockState(var10).getBlock();
               if (var11 instanceof BlockLiquid) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean isOnLadder() {
      boolean var0 = false;
      int var1 = (int)Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D).minY;

      for(int var2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX); var2 < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++var2) {
         for(int var3 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ); var3 < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++var3) {
            Block var4 = Minecraft.theWorld.getBlockState(new BlockPos(var2, var1, var3)).getBlock();
            if (Objects.nonNull(var4) && !(var4 instanceof BlockAir)) {
               if (!(var4 instanceof BlockLadder) && !(var4 instanceof BlockVine)) {
                  return false;
               }

               var0 = true;
            }
         }
      }

      if (!var0 && !Minecraft.thePlayer.isOnLadder()) {
         return false;
      } else {
         return true;
      }
   }

   public static Block getBlockAtPosC(EntityPlayer var0, double var1, double var3, double var5) {
      return getBlock(new BlockPos(var0.posX - var1, var0.posY - var3, var0.posZ - var5));
   }

   public static boolean isInsideBlock() {
      for(int var0 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX); var0 < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++var0) {
         for(int var1 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minY); var1 < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxY) + 1; ++var1) {
            for(int var2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ); var2 < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++var2) {
               Block var3 = Minecraft.theWorld.getBlockState(new BlockPos(var0, var1, var2)).getBlock();
               AxisAlignedBB var4;
               if (var3 != null && !(var3 instanceof BlockAir) || !(var3 instanceof BlockLiquid) || !(var3 instanceof Block) && (var4 = var3.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(var0, var1, var2), Minecraft.theWorld.getBlockState(new BlockPos(var0, var1, var2)))) != null && Minecraft.thePlayer.getEntityBoundingBox().intersectsWith(var4)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static double getDistanceToFall() {
      double var0 = 0.0D;

      double var2;
      for(var2 = Minecraft.thePlayer.posY; var2 > 0.0D; --var2) {
         Block var4 = getBlock(new BlockPos(Minecraft.thePlayer.posX, var2, Minecraft.thePlayer.posZ));
         if (var4.getMaterial() != Material.air && var4.isBlockNormalCube() && var4.isCollidable()) {
            var0 = var2;
            break;
         }

         if (var2 < 0.0D) {
            break;
         }
      }

      var2 = Minecraft.thePlayer.posY - var0 - 1.0D;
      return var2;
   }

   public static boolean stairCollision() {
      return getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, 0.0D, 0.3100000023841858D) instanceof BlockStairs || getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, 0.0D, -0.3100000023841858D) instanceof BlockStairs || getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, 0.0D, -0.3100000023841858D) instanceof BlockStairs || getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, 0.0D, 0.3100000023841858D) instanceof BlockStairs || getBlockatPosSpeed(Minecraft.thePlayer, 1.05F, 1.05F) instanceof BlockStairs;
   }

   public static void placeBlock(BlockPos var0, EnumFacing var1) {
      ItemStack var2 = Minecraft.thePlayer.inventory.getCurrentItem();
      if (var2 != null) {
         Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, var2, var0, var1, new Vec3((double)var0.getX(), (double)var0.getY(), (double)var0.getZ()));
      }
   }

   public static boolean canBeClicked(BlockPos var0) {
      return getBlock(var0).canCollideCheck(Minecraft.theWorld.getBlockState(var0), false);
   }

   public static boolean CanStep() {
      AxisAlignedBB var0 = Minecraft.thePlayer.getEntityBoundingBox().contract(0.0D, 0.001D, 0.0D);
      int var1 = MathHelper.floor_double(var0.minY);
      int var2 = MathHelper.floor_double(var0.maxY + 1.0D);

      for(int var3 = var1; var3 < var2; ++var3) {
         BlockPos var4 = new BlockPos(Minecraft.thePlayer.posX, (double)var3, Minecraft.thePlayer.posZ);
         Block var5 = Minecraft.theWorld.getBlockState(var4).getBlock();
         if (var5.isFullBlock()) {
            return true;
         }
      }

      return false;
   }

   private static float[] getRotationsNeeded(Entity var0) {
      double var1 = var0.posX - Minecraft.thePlayer.posX;
      double var3 = var0.posY - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
      double var5 = var0.posZ - Minecraft.thePlayer.posZ;
      double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var5 * var5);
      float var9 = (float)(Math.atan2(var5, var1) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(Math.atan2(var3, var7) * 180.0D / 3.141592653589793D));
      return new float[]{var9, var10};
   }

   public static String getBlockName(Block var0) {
      if (var0 == Blocks.air) {
         return null;
      } else {
         Item var1 = Item.getItemFromBlock(var0);
         ItemStack var2 = var1 != null ? new ItemStack(Item.getByNameOrId(var0.getUnlocalizedName()), 1, 0) : null;
         String var3 = var2 == null ? var0.getLocalizedName() : var1.getItemStackDisplayName(var2);
         return var3.length() > 5 && var3.startsWith("tile.") ? var0.getUnlocalizedName() : var3;
      }
   }

   public static Block getBlockAtPos(BlockPos var0) {
      IBlockState var1 = Minecraft.theWorld.getBlockState(var0);
      return var1.getBlock();
   }

   public static boolean canBreak(BlockPos var0) {
      IBlockState var1 = Minecraft.theWorld.getBlockState(var0);
      Block var2 = var1.getBlock();
      return var2.getBlockHardness(Minecraft.theWorld, var0) != -1.0F;
   }

   public static boolean isOnLiquidFull() {
      boolean var0 = false;
      if (getBlockAtPosC(Minecraft.thePlayer, 0.30000001192092896D, 0.10000000149011612D, 0.30000001192092896D).getMaterial().isLiquid() && getBlockAtPosC(Minecraft.thePlayer, -0.30000001192092896D, 0.10000000149011612D, -0.30000001192092896D).getMaterial().isLiquid() && (Integer)Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX - 0.3D, Minecraft.thePlayer.posY - 0.1D, Minecraft.thePlayer.posZ - 0.3D)).getValue(BlockLiquid.LEVEL) == 0 && (Integer)Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX + 0.3D, Minecraft.thePlayer.posY - 0.1D, Minecraft.thePlayer.posZ + 0.3D)).getValue(BlockLiquid.LEVEL) == 0) {
         var0 = true;
      }

      return var0;
   }

   public static Block getBlockUnderPlayer(EntityPlayer var0, double var1) {
      return getBlock(new BlockPos(var0.posX, var0.posY - var1, var0.posZ));
   }

   public static Block getBlock(BlockPos var0) {
      return Minecraft.theWorld.getBlockState(var0).getBlock();
   }

   public static Block getBlockatPosSpeed(EntityPlayer var0, float var1, float var2) {
      double var3 = var0.posX + var0.motionX * (double)var1;
      double var5 = var0.posZ + var0.motionZ * (double)var2;
      return getBlockAtPos(new BlockPos(var3, var0.posY, var5));
   }

   public static Block getBlock(Entity var0, double var1) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = (int)var0.getEntityBoundingBox().offset(0.0D, var1, 0.0D).minY;

         for(int var4 = MathHelper.floor_double(var0.getEntityBoundingBox().minX); var4 < MathHelper.floor_double(var0.getEntityBoundingBox().maxX) + 1; ++var4) {
            int var5 = MathHelper.floor_double(var0.getEntityBoundingBox().minZ);
            if (var5 < MathHelper.floor_double(var0.getEntityBoundingBox().maxZ) + 1) {
               return Minecraft.theWorld.getBlockState(new BlockPos(var4, var3, var5)).getBlock();
            }
         }

         return null;
      }
   }

   public static boolean isReallyOnGround() {
      EntityPlayerSP var0 = Minecraft.thePlayer;
      double var1 = var0.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
      Block var3 = Minecraft.theWorld.getBlockState(new BlockPos(var0.posX, var1, var0.posZ)).getBlock();
      return var3 != null && !(var3 instanceof BlockAir) && !(var3 instanceof BlockLiquid) ? var0.onGround : false;
   }

   public static boolean isReplaceable(BlockPos var0) {
      return getBlock(var0).isReplaceable(Minecraft.theWorld, var0);
   }

   public static float[] getBlockRotations(double var0, double var2, double var4) {
      double var6 = var0 - Minecraft.thePlayer.posX + 0.5D;
      double var8 = var4 - Minecraft.thePlayer.posZ + 0.5D;
      double var10 = var2 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - 1.0D);
      double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var8 * var8);
      float var14 = (float)(Math.atan2(var8, var6) * 180.0D / 3.141592653589793D) - 90.0F;
      return new float[]{var14, (float)(-Math.atan2(var10, var12) * 180.0D / 3.141592653589793D)};
   }
}
