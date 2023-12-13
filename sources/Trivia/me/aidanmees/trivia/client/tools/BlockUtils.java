package me.aidanmees.trivia.client.tools;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtils
{
  public static int getBestTool(BlockPos pos)
  {
    Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
    int slot = 0;
    float dmg = 0.1F;
    for (int index = 36; index < 45; index++)
    {
      ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
      if ((itemStack != null) && (block != null) && (itemStack.getItem().getStrVsBlock(itemStack, block) > dmg))
      {
        slot = index - 36;
        dmg = itemStack.getItem().getStrVsBlock(itemStack, block);
      }
    }
    if (dmg > 0.1F) {
      return slot;
    }
    return Minecraft.thePlayer.inventory.currentItem;
  }
  
  public static boolean isInLiquid(Entity entity)
  {
    if (entity == null) {
      return false;
    }
    boolean inLiquid = false;
    int y = (int)entity.getEntityBoundingBox().minY;
    for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
      for (int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1; z++)
      {
        Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
        if ((block != null) && (!(block instanceof BlockAir)))
        {
          if (!(block instanceof BlockLiquid)) {
            return false;
          }
          inLiquid = true;
        }
      }
    }
    return (inLiquid) || (Minecraft.thePlayer.isInWater());
  }
  
  public static boolean isOnLiquid(Entity entity)
  {
    if (entity == null) {
      return false;
    }
    boolean onLiquid = false;
    int y = (int)entity.boundingBox.offset(0.0D, -0.01D, 0.0D).minY;
    for (int x = MathHelper.floor_double(entity.boundingBox.minX); x < 
          MathHelper.floor_double(entity.boundingBox.maxX) + 1; x++) {
      for (int z = MathHelper.floor_double(entity.boundingBox.minZ); z < 
            MathHelper.floor_double(entity.boundingBox.maxZ) + 1; z++)
      {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z))
          .getBlock();
        if ((block != null) && (!(block instanceof BlockAir)))
        {
          if (!(block instanceof BlockLiquid)) {
            return false;
          }
          onLiquid = true;
        }
      }
    }
    return onLiquid;
  }
  
  public static boolean isOnIce(Entity entity)
  {
    if (entity == null) {
      return false;
    }
    boolean onIce = false;
    int y = (int)entity.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
    for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; x++) {
      for (int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1; z++)
      {
        Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
        if ((block != null) && (!(block instanceof BlockAir)))
        {
          if ((!(block instanceof BlockIce)) && (!(block instanceof BlockPackedIce))) {
            return false;
          }
          onIce = true;
        }
      }
    }
    return onIce;
  }
  
  public static boolean isOnLadder(Entity entity)
  {
    if (entity == null) {
      return false;
    }
    boolean onLadder = false;
    int y = (int)entity.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D).minY;
    for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; x++) {
      for (int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1; z++)
      {
        Minecraft.getMinecraft();Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
        if ((Objects.nonNull(block)) && (!(block instanceof BlockAir)))
        {
          if ((!(block instanceof BlockLadder)) && (!(block instanceof BlockVine))) {
            return false;
          }
          onLadder = true;
        }
      }
    }
    return (onLadder) || (Minecraft.thePlayer.isOnLadder());
  }
  
  public static boolean isInsideBlock(Entity entity)
  {
    for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; x++) {
      for (int y = MathHelper.floor_double(entity.getEntityBoundingBox().minY); y < MathHelper.floor_double(entity.getEntityBoundingBox().maxY) + 1; y++) {
        for (int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ); z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1; z++)
        {
          Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
          if (block != null)
          {
            AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(x, y, z), Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)));
            if ((block instanceof BlockHopper)) {
              boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
            }
            if ((boundingBox != null) && (entity.getEntityBoundingBox().intersectsWith(boundingBox))) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  public static Block getBlock(Entity entity, double offset)
  {
    if (entity == null) {
      return null;
    }
    int y = (int)entity.getEntityBoundingBox().offset(0.0D, offset, 0.0D).minY;
    for (int x = MathHelper.floor_double(entity.getEntityBoundingBox().minX); x < MathHelper.floor_double(entity.getEntityBoundingBox().maxX) + 1; x++)
    {
      int z = MathHelper.floor_double(entity.getEntityBoundingBox().minZ);
      if (z < MathHelper.floor_double(entity.getEntityBoundingBox().maxZ) + 1) {
        return Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
      }
    }
    return null;
  }
  
  
  public static Block getBlock(int x, int y, int z)
  {
    return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
  }
  
  public static Block getBlock(BlockPos pos)
  {
    return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
  }
  
  public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height)
  {
    return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
  }
  
  public static Block getBlock(Entity entity, float x, float y, float z)
  {
    Minecraft.getMinecraft();return getBlock(Minecraft.thePlayer.getEntityBoundingBox().offset(x, y, z));
  }
  
  public static Block getBlock(AxisAlignedBB boundingBox)
  {
    double y = boundingBox.minY;
    for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX) + 1; x++) {
      for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ) + 1; z++)
      {
        Minecraft.getMinecraft();Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
        if (block != null) {
          return block;
        }
      }
    }
    return null;
  }
}
