package wtf.monsoon.api.util.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorClass;
import net.optifine.reflect.ReflectorMethod;

public class BlockUtil {
  private static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
  
  public static Minecraft mc;
  
  private static ReflectorMethod ForgeBlock_setLightOpacity = new ReflectorMethod(ForgeBlock, "setLightOpacity");
  
  private static boolean directAccessValid = true;

  
  public static void setLightOpacity(Block block, int opacity) {
    if (directAccessValid)
      try {
        block.setLightOpacity(opacity);
        return;
      } catch (IllegalAccessError var3) {
        directAccessValid = false;
        if (!ForgeBlock_setLightOpacity.exists())
          throw var3; 
      }  
    Reflector.callVoid(block, ForgeBlock_setLightOpacity, new Object[] { Integer.valueOf(opacity) });
  }
  
  public static float[] faceTarget(Entity target, float p_70625_2_, float p_70625_3_, boolean miss) {
	
    double var6, var4 = target.posX - (mc.thePlayer.posX);
    double var8 = target.posZ - (mc.thePlayer.posZ);
    if (target instanceof EntityLivingBase) {
      EntityLivingBase var10 = (EntityLivingBase)target;
      var6 = var10.posY + var10.getEyeHeight() - 
        (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
    } else {
      var6 = ((target.getEntityBoundingBox()).minY + (target.getEntityBoundingBox()).maxY) / 2.0D - 
        (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
    } 
    Random rnd = new Random();
    double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
    float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
    float var13 = (float)-(Math.atan2(var6 - ((target instanceof EntityPlayer) ? 0.25D : 0.0D), var14) * 
      180.0D / Math.PI);
    //float pitch = changeRotation(mc.thePlayer.rotationPitch, var13, p_70625_3_);
    float pitch = changeRotation(mc.thePlayer.rotationPitch, var13, p_70625_3_);
    //float yaw = changeRotation(mc.thePlayer.rotationYaw, var12, p_70625_2_);
    float yaw = changeRotation(mc.thePlayer.rotationYaw, var12, p_70625_2_);
    return new float[] { yaw, pitch };
  }
  
  public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
    float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
    if (var4 > p_70663_3_)
      var4 = p_70663_3_; 
    if (var4 < -p_70663_3_)
      var4 = -p_70663_3_; 
    return p_70663_1_ + var4;
  }
  
  private List<Block> invalid = Arrays.asList(new Block[] { Blocks.air, (Block)Blocks.water, (Block)Blocks.fire, (Block)Blocks.flowing_water, (Block)Blocks.lava, (Block)Blocks.flowing_lava, (Block)Blocks.chest, Blocks.anvil, Blocks.enchanting_table });
  
  public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
    EntitySnowball temp = new EntitySnowball((World)mc.theWorld);
    temp.posX = x + 0.5D;
    temp.posY = y + 0.5D;
    temp.posZ = z + 0.5D;
    temp.posX += facing.getDirectionVec().getX() * 0.25D;
    temp.posY += facing.getDirectionVec().getY() * 0.25D;
    temp.posZ += facing.getDirectionVec().getZ() * 0.25D;
    return faceTarget((Entity)temp, 100.0F, 100.0F, false);
  }
  
  public static float[] getAngles(EntityPlayerSP player, BlockPos blockPos) {
    double difX = blockPos.getX() + 0.5D - player.posX;
    double difY = blockPos.getY() - player.posY + player.getEyeHeight();
    double difZ = blockPos.getZ() + 0.5D - player.posZ;
    double sqrt = Math.sqrt(difX * difX + difZ * difZ);
    float yaw = (float)(Math.atan2(difZ, difX) * 180.0D / Math.PI) - 90.0F;
    float pitch = (float)-(Math.atan2(difY, sqrt) * 180.0D / Math.PI);
    return new float[] { yaw, pitch };
  }
  
  public static boolean isOnLiquid() {
    boolean onLiquid = false;
    if (getBlockAtPosC((EntityPlayer)mc.thePlayer, 0.30000001192092896D, 0.10000000149011612D, 0.30000001192092896D).getMaterial().isLiquid() && 
      getBlockAtPosC((EntityPlayer)mc.thePlayer, -0.30000001192092896D, 0.10000000149011612D, -0.30000001192092896D).getMaterial().isLiquid())
      onLiquid = true; 
    return onLiquid;
  }
  
  public static boolean isOnLadder() {
    if (mc.thePlayer == null)
      return false; 
    boolean onLadder = false;
    int y = (int)(mc.thePlayer.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D)).minY;
    for (int x = MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).minX); x < MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).minZ); z < MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
          if (!(block instanceof net.minecraft.block.BlockLadder) && !(block instanceof net.minecraft.block.BlockVine))
            return false; 
          onLadder = true;
        } 
      } 
    } 
    return !(!onLadder && !mc.thePlayer.isOnLadder());
  }
  
  public static boolean isOnIce() {
    if (mc.thePlayer == null)
      return false; 
    boolean onIce = false;
    int y = (int)(mc.thePlayer.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D)).minY;
    for (int x = MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).minX); x < MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).maxX) + 1; x++) {
      for (int z = MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).minZ); z < MathHelper.floor_double((mc.thePlayer.getEntityBoundingBox()).maxZ) + 1; z++) {
        Block block = getBlock(x, y, z);
        if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
          if (!(block instanceof net.minecraft.block.BlockIce) && !(block instanceof net.minecraft.block.BlockPackedIce))
            return false; 
          onIce = true;
        } 
      } 
    } 
    return onIce;
  }
  
  public boolean isInsideBlock() {
    //Helper.mc();
    int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX);
    //Helper.mc();
    for (; x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
      //Helper.mc();
      int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY);
      //Helper.mc();
      for (; y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY) + 1; y++) {
        //Helper.mc();
        int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ);
        //Helper.mc();
        for (; z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++) {
          //Helper.mc();
          Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
          //Helper.mc();
          //Helper.mc();
          AxisAlignedBB boundingBox;
          if (block != null && !(block instanceof net.minecraft.block.BlockAir) && (boundingBox = block.getCollisionBoundingBox((World)mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)))) != null) {
            //Helper.mc();
            if (mc.thePlayer.boundingBox.intersectsWith(boundingBox))
              return true; 
          } 
        } 
      } 
    } 
    return false;
  }
  
  public Block getBlockByIDorName(String message) {
    Block tBlock = null;
    try {
      tBlock = Block.getBlockById(Integer.parseInt(message));
    } catch (NumberFormatException e) {
      Block block = null;
      for (Object object : Block.blockRegistry) {
        block = (Block)object;
        String label = block.getLocalizedName().replace(" ", "");
        if (label.toLowerCase().startsWith(message) || label.toLowerCase().contains(message))
          break; 
      } 
      if (block != null)
        tBlock = block; 
    } 
    return tBlock;
  }
  
  public static boolean isBlockUnderPlayer(Material material, float height) {
    if (getBlockAtPosC((EntityPlayer)mc.thePlayer, 0.3100000023841858D, height, 0.3100000023841858D).getMaterial() == material && 
      getBlockAtPosC((EntityPlayer)mc.thePlayer, -0.3100000023841858D, height, -0.3100000023841858D).getMaterial() == material && 
      getBlockAtPosC((EntityPlayer)mc.thePlayer, -0.3100000023841858D, height, 0.3100000023841858D).getMaterial() == material && 
      getBlockAtPosC((EntityPlayer)mc.thePlayer, 0.3100000023841858D, height, -0.3100000023841858D).getMaterial() == material)
      return true; 
    return false;
  }
  
  public static boolean canSeeBlock(float x, float y, float z) {
    return (getFacing(new BlockPos(x, y, z)) != null);
  }
  
  public static EnumFacing getFacing(BlockPos pos) {
    EnumFacing[] orderedValues = { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
    EnumFacing[] var2 = orderedValues;
    int var3 = orderedValues.length;
    for (int var4 = 0; var4 < var3; var4++) {
      EnumFacing facing = var2[var4];
      EntitySnowball temp = new EntitySnowball((World)mc.theWorld);
      temp.posX = pos.getX() + 0.5D;
      temp.posY = pos.getY() + 0.5D;
      temp.posZ = pos.getZ() + 0.5D;
      temp.posX += facing.getDirectionVec().getX() * 0.5D;
      temp.posY += facing.getDirectionVec().getY() * 0.5D;
      temp.posZ += facing.getDirectionVec().getZ() * 0.5D;
      //Helper.mc();
      if (mc.thePlayer.canEntityBeSeen((Entity)temp))
        return facing; 
    } 
    return null;
  }
  
  public BlockData getBlockData1(BlockPos pos) {
    List<Block> invalid = this.invalid;
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock()))
      return new BlockData(pos.add(0, -1, 0), EnumFacing.UP); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock()))
      return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock()))
      return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock()))
      return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()))
      return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH); 
    BlockPos add = pos.add(-1, 0, 0);
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock()))
      return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock()))
      return new BlockData(add.add(1, 0, 0), EnumFacing.WEST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock()))
      return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock()))
      return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH); 
    BlockPos add2 = pos.add(1, 0, 0);
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock()))
      return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock()))
      return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock()))
      return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock()))
      return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH); 
    BlockPos add3 = pos.add(0, 0, -1);
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock()))
      return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock()))
      return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock()))
      return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock()))
      return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH); 
    BlockPos add4 = pos.add(0, 0, 1);
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock()))
      return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock()))
      return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock()))
      return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH); 
    Minecraft.getMinecraft();
    if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock()))
      return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH); 
    BlockData blockData = null;
    return blockData;
  }
  
  public int getBlockSlot() {
    int i = 36;
    while (i < 45) {
      Minecraft.getMinecraft();
      ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
      if (itemStack != null && itemStack.getItem() instanceof net.minecraft.item.ItemBlock)
        return i - 36; 
      i++;
    } 
    return -1;
  }
  
  public int getBestSlot() {
    Minecraft.getMinecraft();
    if (mc.thePlayer.getHeldItem().getItem() instanceof net.minecraft.item.ItemBlock) {
      Minecraft.getMinecraft();
      return mc.thePlayer.inventory.currentItem;
    } 
    for (int i = 0; i < 8; i++) {
      Minecraft.getMinecraft();
      if (mc.thePlayer.inventory.getStackInSlot(i) != null) {
        Minecraft.getMinecraft();
        if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemBlock)
          return i; 
      } 
    } 
    return -1;
  }
  
  public static BlockData getBlockData(BlockPos pos, List list) {
    //Helper.mc();
    //Helper.mc();
    //Helper.mc();
    //Helper.mc();
    //Helper.mc();
    return !list.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock()) ? new BlockData(pos.add(0, -1, 0), EnumFacing.UP) : (!list.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock()) ? new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST) : (!list.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock()) ? new BlockData(pos.add(1, 0, 0), EnumFacing.WEST) : (!list.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock()) ? new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH) : (!list.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()) ? new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null))));
  }
  
  public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
    return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
  }
  
  public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
    return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
  }
  
  public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double height) {
    return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY + inPlayer.height + height, inPlayer.posZ));
  }
  
  public static Block getBlock(int x, int y, int z) {
    return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
  }
  
  public static Block getBlock(BlockPos pos) {
    return mc.theWorld.getBlockState(pos).getBlock();
  }
  
  public static class BlockData {
    public BlockPos pos;
    
    public EnumFacing facing;
    
    public BlockData(BlockPos position, EnumFacing face) {
      this.pos = position;
      this.facing = face;
    }
  }
}
