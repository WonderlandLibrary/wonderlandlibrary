package cn.hanabi.utils;

import cn.hanabi.Wrapper;
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
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtils {
   private EntityOtherPlayerMP player;

   public static String getBlockName(Block block) {
      if (block == Blocks.air) {
         return null;
      } else {
         Item item = Item.getItemFromBlock(block);
         ItemStack itemStack = item != null ? new ItemStack(Item.getByNameOrId(block.getUnlocalizedName()), 1, 0) : null;
         String name = itemStack == null ? block.getLocalizedName() : item.getItemStackDisplayName(itemStack);
         return name.length() > 5 && name.startsWith("tile.") ? block.getUnlocalizedName() : name;
      }
   }

   public static boolean stairCollision() {
      return getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || getBlockatPosSpeed(Wrapper.mc.thePlayer, 1.05F, 1.05F) instanceof BlockStairs;
   }

   public static boolean isReallyOnGround() {
      Entity entity = Minecraft.getMinecraft().thePlayer;
      double y = entity.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
      Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(entity.posX, y, entity.posZ)).getBlock();
      return block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid) ? entity.onGround : false;
   }

   public static Block getBlockatPosSpeed(EntityPlayer inPlayer, float x, float z) {
      double posX = inPlayer.posX + inPlayer.motionX * (double)x;
      double posZ = inPlayer.posZ + inPlayer.motionZ * (double)z;
      return getBlockAtPos(new BlockPos(posX, inPlayer.posY, posZ));
   }

   public static Block getBlockAtPos(BlockPos inBlockPos) {
      IBlockState s = Wrapper.mc.theWorld.getBlockState(inBlockPos);
      return s.getBlock();
   }

   public static double getDistanceToFall() {
      double distance = 0.0;

      for(double i = Wrapper.mc.thePlayer.posY; i > 0.0; --i) {
         Block block = getBlock(new BlockPos(Wrapper.mc.thePlayer.posX, i, Wrapper.mc.thePlayer.posZ));
         if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
            distance = i;
            break;
         }

         if (i < 0.0) {
            break;
         }
      }

      return Wrapper.mc.thePlayer.posY - distance - 1.0;
   }

   public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
      return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
   }

   public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
      return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
   }

   public static boolean isOnLiquidFull() {
      boolean onLiquid = false;
      if (getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial().isLiquid() && getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial().isLiquid() && (Integer)Wrapper.mc.theWorld.getBlockState(new BlockPos(Wrapper.mc.thePlayer.posX - 0.3, Wrapper.mc.thePlayer.posY - 0.1, Wrapper.mc.thePlayer.posZ - 0.3)).getValue(BlockLiquid.LEVEL) == 0 && (Integer)Wrapper.mc.theWorld.getBlockState(new BlockPos(Wrapper.mc.thePlayer.posX + 0.3, Wrapper.mc.thePlayer.posY - 0.1, Wrapper.mc.thePlayer.posZ + 0.3)).getValue(BlockLiquid.LEVEL) == 0) {
         onLiquid = true;
      }

      return onLiquid;
   }

   public static Block getBlock(double x, double y, double z) {
      return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
   }

   public static Block getBlock(BlockPos block) {
      return Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock();
   }

   public static Block getBlock(Entity entity, double offsetY) {
      if (entity == null) {
         return null;
      } else {
         int y = (int)entity.getEntityBoundingBox().offset(0.0, offsetY, 0.0).minY;

         for(int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ);
            if (z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1) {
               return Wrapper.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            }
         }

         return null;
      }
   }

   public static boolean isOnLiquid() {
      AxisAlignedBB axisAlignedBB = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0, -0.1, 0.0).contract(0.001, 0.0, 0.001);
      int xMin = MathHelper.floor_double(axisAlignedBB.minX);
      int xMax = MathHelper.floor_double(axisAlignedBB.maxX + 1.0);
      int yMin = MathHelper.floor_double(axisAlignedBB.minY);
      int yMax = MathHelper.floor_double(axisAlignedBB.maxY + 1.0);
      int zMin = MathHelper.floor_double(axisAlignedBB.minZ);
      int zMax = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0);
      boolean gotcha = false;

      for(int y = yMin; y < yMax; ++y) {
         for(int x = xMin; x < xMax; ++x) {
            for(int z = zMin; z < zMax; ++z) {
               Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block instanceof BlockLiquid) {
                  gotcha = true;
               }

               if (!(block instanceof BlockLiquid) && block.getCollisionBoundingBox(Wrapper.mc.theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z))) != null) {
                  return false;
               }
            }
         }
      }

      return gotcha;
   }

   public static boolean isOnLiquidFixed() {
      return Wrapper.mc.theWorld.handleMaterialAcceleration(Wrapper.mc.thePlayer.getEntityBoundingBox().expand(0.0, -0.4000000059604645, 0.0).contract(0.001, 0.001, 0.001), Material.water, Wrapper.mc.thePlayer);
   }

   public static boolean isInLiquid() {
      AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().contract(0.001, 0.001, 0.001);
      int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
      int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
      int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
      int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);

      for(int var11 = var4; var11 < var5; ++var11) {
         for(int var12 = var6; var12 < var7; ++var12) {
            for(int var13 = var8; var13 < var9; ++var13) {
               BlockPos pos = new BlockPos(var11, var12, var13);
               Block var14 = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
               if (var14 instanceof BlockLiquid) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean CanStep() {
      AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().contract(0.0, 0.001, 0.0);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);

      for(int y = var6; y < var7; ++y) {
         BlockPos pos = new BlockPos(Wrapper.mc.thePlayer.posX, (double)y, Wrapper.mc.thePlayer.posZ);
         Block var14 = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
         if (var14.isFullBlock()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isOnLadder() {
      boolean onLadder = false;
      int y = (int)Wrapper.mc.thePlayer.getEntityBoundingBox().offset(0.0, 1.0, 0.0).minY;

      for(int x = MathHelper.floor_double(Wrapper.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Wrapper.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
         for(int z = MathHelper.floor_double(Wrapper.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Wrapper.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
            Block block = Wrapper.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (Objects.nonNull(block) && !(block instanceof BlockAir)) {
               if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                  return false;
               }

               onLadder = true;
            }
         }
      }

      return onLadder || Wrapper.mc.thePlayer.isOnLadder();
   }

   public static boolean isInsideBlock() {
      for(int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
         for(int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
            for(int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
               Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               AxisAlignedBB boundingBox;
               if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)))) != null && Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static float[] getBlockRotations(double x, double y, double z) {
      double var4 = x - Wrapper.mc.thePlayer.posX + 0.5;
      double var6 = z - Wrapper.mc.thePlayer.posZ + 0.5;
      double var8 = y - (Wrapper.mc.thePlayer.posY + (double)Wrapper.mc.thePlayer.getEyeHeight() - 1.0);
      double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var6 * var6);
      float var12 = (float)(Math.atan2(var6, var4) * 180.0 / Math.PI) - 90.0F;
      return new float[]{var12, (float)(-Math.atan2(var8, var14) * 180.0 / Math.PI)};
   }

   private static float[] getRotationsNeeded(Entity entity) {
      double posX = entity.posX - Wrapper.mc.thePlayer.posX;
      double posY = entity.posY - (Wrapper.mc.thePlayer.posY + (double)Wrapper.mc.thePlayer.getEyeHeight());
      double posZ = entity.posZ - Wrapper.mc.thePlayer.posZ;
      double var14 = (double)MathHelper.sqrt_double(posX * posX + posZ * posZ);
      float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0F;
      float pitch = (float)(-(Math.atan2(posY, var14) * 180.0 / Math.PI));
      return new float[]{yaw, pitch};
   }
}
