package space.lunaclient.luna.util.scaffold;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;

public class BlockUtils
{
  public BlockUtils() {}
  
  public static boolean isOnLiquid()
  {
    boolean onLiquid = false;
    if ((getBlockAtPosC(Minecraft.thePlayer, 0.30000001192092896D, 0.10000000149011612D, 0.30000001192092896D).getMaterial().isLiquid()) && (getBlockAtPosC(Minecraft.thePlayer, -0.30000001192092896D, 0.10000000149011612D, -0.30000001192092896D).getMaterial().isLiquid())) {
      onLiquid = true;
    }
    return onLiquid;
  }
  
  public static float getPlayerBlockDistance(BlockPos blockPos)
  {
    return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }
  
  private static float getPlayerBlockDistance(double posX, double posY, double posZ)
  {
    float xDiff = (float)(Minecraft.thePlayer.posX - posX);
    float yDiff = (float)(Minecraft.thePlayer.posY - posY);
    float zDiff = (float)(Minecraft.thePlayer.posZ - posZ);
    return getBlockDistance(xDiff, yDiff, zDiff);
  }
  
  public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing)
  {
    EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
    temp.posX = (x + 0.5D);
    temp.posY = (y + 0.5D);
    temp.posZ = (z + 0.5D);
    temp.posX += facing.getDirectionVec().getX() * 0.25D;
    temp.posY += facing.getDirectionVec().getY() * 0.25D;
    temp.posZ += facing.getDirectionVec().getZ() * 0.25D;
    return getAngles(temp);
  }
  
  private static PlayerControllerMP getPlayerController()
  {
    return Minecraft.playerController;
  }
  
  private static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec)
  {
    getPlayerController().processRightClickBlock(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer
    
      .getCurrentEquippedItem(), pos, side, hitVec);
  }
  
  public static boolean placeBlockScaffold(BlockPos pos)
  {
    Minecraft mc = Minecraft.getMinecraft();
    Vec3d eyesPos = new Vec3d(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
    EnumFacing[] var5;
    int var4 = (var5 = EnumFacing.values()).length;
    for (int var3 = 0; var3 < var4; var3++)
    {
      EnumFacing side = var5[var3];
      BlockPos neighbor = pos.offset(side);
      EnumFacing side2 = side.getOpposite();
      if ((eyesPos.squareDistanceTo(new Vec3d(pos).addVector(0.5D, 0.5D, 0.5D)) < eyesPos.squareDistanceTo(new Vec3d(neighbor).addVector(1.5D, 0.5D, 1.5D))) && (canBeClicked(neighbor)))
      {
        Vec3d hitVec = new Vec3d(neighbor).addVector(0.5D, 0.5D, 0.5D).add(new Vec3d(side2.getDirectionVec()).scale(0.5D));
        if (eyesPos.squareDistanceTo(hitVec) <= 18.0625D)
        {
          if (Minecraft.thePlayer.ticksExisted % 2 == 0)
          {
            if (!mc.gameSettings.keyBindJump.pressed) {
              RotationUtils.faceVectorPacketInstant(hitVec);
            } else {
              Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.thePlayer.rotationYaw, 90.0F, Minecraft.thePlayer.onGround));
            }
            processRightClickBlock(neighbor, side2, hitVec);
          }
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
          mc.rightClickDelayTimer = 4;
          return true;
        }
      }
    }
    return false;
  }
  
  public static void swingArmClient()
  {
    Minecraft.thePlayer.swingItem();
  }
  
  public static void swingArmPacket()
  {
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
  }
  
  public static boolean isNull(ItemStack stack)
  {
    return stack == null;
  }
  
  public static int getArmorType(ItemArmor armor)
  {
    return 3 - armor.armorType;
  }
  
  public static boolean isThrowable(ItemStack stack)
  {
    Item item = stack.getItem();
    if ((!(item instanceof ItemBow)) && (!(item instanceof ItemSnowball)) && (!(item instanceof ItemEgg)) && (!(item instanceof ItemEnderPearl))) {
      if (!(item instanceof ItemPotion)) {
        break label54;
      }
    }
    label54:
    return 
    
      ItemPotion.isSplash(stack.getItemDamage());
  }
  
  private static boolean canBeClicked(BlockPos pos)
  {
    return getBlock(pos).canCollideCheck(getState(pos), false);
  }
  
  private static IBlockState getState(BlockPos pos)
  {
    return Minecraft.theWorld.getBlockState(pos);
  }
  
  public static boolean isPotion(ItemStack stack)
  {
    return (stack != null) && ((stack.getItem() instanceof ItemPotion));
  }
  
  public static Item getFromRegistry(ResourceLocation location)
  {
    return (Item)Item.itemRegistry.getObject(location);
  }
  
  public static int getStackSize(ItemStack stack)
  {
    return stack.stackSize;
  }
  
  public static Material getMaterial(BlockPos pos)
  {
    return getBlock(pos).getMaterial();
  }
  
  private static float[] getAngles(Entity e)
  {
    return new float[] { getYawChangeToEntity(e) + Minecraft.thePlayer.rotationYaw, getPitchChangeToEntity(e) + Minecraft.thePlayer.rotationPitch };
  }
  
  private static float getYawChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double yawToEntity = (deltaZ < 0.0D) && (deltaX > 0.0D) ? -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0D) && (deltaX < 0.0D) ? 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ));
    return MathHelper.wrapAngleTo180_float(-Minecraft.thePlayer.rotationYaw - (float)yawToEntity);
  }
  
  private static float getPitchChangeToEntity(Entity entity)
  {
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - Minecraft.thePlayer.posY;
    double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
    return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
  }
  
  public static boolean canSeeBlock(int x, int y, int z)
  {
    return getFacing(new BlockPos(x, y, z)) != null;
  }
  
  public static EnumFacing getFacing(BlockPos pos)
  {
    EnumFacing[] orderedValues;
    EnumFacing[] arrenumFacing = orderedValues = new EnumFacing[] { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
    int n = arrenumFacing.length;
    int n2 = 0;
    while (n2 < n)
    {
      EnumFacing facing = arrenumFacing[n2];
      EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
      temp.posX = (pos.getX() + 0.5D);
      temp.posY = (pos.getY() + 0.5D);
      temp.posZ = (pos.getZ() + 0.5D);
      temp.posX += facing.getDirectionVec().getX() * 0.5D;
      temp.posY += facing.getDirectionVec().getY() * 0.5D;
      temp.posZ += facing.getDirectionVec().getZ() * 0.5D;
      if (Minecraft.thePlayer.canEntityBeSeen(temp)) {
        return facing;
      }
      n2++;
    }
    return null;
  }
  
  private static float getBlockDistance(float xDiff, float yDiff, float zDiff)
  {
    return MathHelper.sqrt_float((xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
  }
  
  public static boolean isOnLadder()
  {
    if (Minecraft.thePlayer == null) {
      return false;
    }
    boolean onLadder = false;
    int y = (int)Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D).minY;
    int x = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX);
    while (x < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1)
    {
      int z = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ);
      while (z < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1)
      {
        Block block = getBlock(x, y, z);
        if ((block != null) && (!(block instanceof BlockAir)))
        {
          if ((!(block instanceof BlockLadder)) && (!(block instanceof BlockVine))) {
            return false;
          }
          onLadder = true;
        }
        z++;
      }
      x++;
    }
    return (onLadder) || (Minecraft.thePlayer.isOnLadder());
  }
  
  public static boolean isOnIce()
  {
    if (Minecraft.thePlayer == null) {
      return false;
    }
    boolean onIce = false;
    int y = (int)Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -0.01D, 0.0D).minY;
    int x = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX);
    while (x < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1)
    {
      int z = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ);
      while (z < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1)
      {
        Block block = getBlock(x, y, z);
        if ((block != null) && (!(block instanceof BlockAir)))
        {
          if ((!(block instanceof BlockIce)) && (!(block instanceof BlockPackedIce))) {
            return false;
          }
          onIce = true;
        }
        z++;
      }
      x++;
    }
    return onIce;
  }
  
  public boolean isInsideBlock()
  {
    int x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX);
    while (x < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1)
    {
      int y = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minY);
      while (y < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxY) + 1)
      {
        int z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ);
        while (z < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1)
        {
          Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
          AxisAlignedBB boundingBox;
          if ((block != null) && (!(block instanceof BlockAir)) && ((boundingBox = block.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(x, y, z), Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)))) != null) && (Minecraft.thePlayer.boundingBox.intersectsWith(boundingBox))) {
            return true;
          }
          z++;
        }
        y++;
      }
      x++;
    }
    return false;
  }
  
  public static boolean isBlockUnderPlayer(Material material, float height)
  {
    return (getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, height, 0.3100000023841858D).getMaterial() == material) && (getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, height, -0.3100000023841858D).getMaterial() == material) && (getBlockAtPosC(Minecraft.thePlayer, -0.3100000023841858D, height, 0.3100000023841858D).getMaterial() == material) && (getBlockAtPosC(Minecraft.thePlayer, 0.3100000023841858D, height, -0.3100000023841858D).getMaterial() == material);
  }
  
  private static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z)
  {
    return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
  }
  
  public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height)
  {
    return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
  }
  
  public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double height)
  {
    return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY + inPlayer.height + height, inPlayer.posZ));
  }
  
  public static Block getBlock(int x, int y, int z)
  {
    return Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
  }
  
  public static Block getBlock(BlockPos pos)
  {
    return Minecraft.theWorld.getBlockState(pos).getBlock();
  }
}
