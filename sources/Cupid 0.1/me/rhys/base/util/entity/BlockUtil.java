package me.rhys.base.util.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtil {
  private static Minecraft mc = Minecraft.getMinecraft();
  
  public static List<Block> blacklistedBlocks = Arrays.asList(new Block[] { 
        Blocks.air, (Block)Blocks.water, (Block)Blocks.flowing_water, (Block)Blocks.lava, (Block)Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, (Block)Blocks.stained_glass_pane, Blocks.iron_bars, 
        Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, (Block)Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, 
        Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, 
        Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever });
  
  public static Block getBlockAtPos(BlockPos pos) {
    IBlockState blockState = getBlockStateAtPos(pos);
    if (blockState == null)
      return null; 
    return blockState.getBlock();
  }
  
  public static float[] getBlockRotations(double x, double y, double z) {
    double var4 = x - mc.thePlayer.posX + 0.5D;
    double var6 = z - mc.thePlayer.posZ + 0.5D;
    double var8 = y - mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - 1.0D;
    double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
    float var12 = (float)(Math.atan2(var6, var4) * 180.0D / Math.PI) - 90.0F;
    return new float[] { var12, (float)(-Math.atan2(var8, var14) * 180.0D / Math.PI) };
  }
  
  public static IBlockState getBlockStateAtPos(BlockPos pos) {
    if (Minecraft.getMinecraft() == null || (Minecraft.getMinecraft()).theWorld == null)
      return null; 
    return (Minecraft.getMinecraft()).theWorld.getBlockState(pos);
  }
  
  public static boolean isOnGround() {
    for (double d = 0.0D; d <= 1.0D; d += 0.05D) {
      if (!((Block)Objects.<Block>requireNonNull(getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - d, mc.thePlayer.posZ)))).getUnlocalizedName().toLowerCase().contains("air"))
        return true; 
    } 
    return false;
  }
  
  public static boolean isOnGround(double height) {
    if (!(Minecraft.getMinecraft()).theWorld.getCollidingBoundingBoxes((Entity)(Minecraft.getMinecraft()).thePlayer, (Minecraft.getMinecraft()).thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty())
      return true; 
    return false;
  }
  
  public static boolean isBlockUnderEntity(Entity entity) {
    for (int i = 0; i < entity.posY + entity.getEyeHeight(); i += 2) {
      AxisAlignedBB boundingBox = entity.getEntityBoundingBox().offset(0.0D, -i, 0.0D);
      if (!mc.theWorld.getCollidingBoundingBoxes(entity, boundingBox).isEmpty())
        return true; 
    } 
    return false;
  }
  
  public static boolean onSlab(double amount) {
    boolean found = false;
    for (double i = 0.0D; i < amount; i += 0.10000000149011612D) {
      if (getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ)).getUnlocalizedName().toLowerCase().contains("slab") || 
        getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ)).getUnlocalizedName().toLowerCase().contains("stair"))
        found = true; 
    } 
    return found;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\entity\BlockUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */