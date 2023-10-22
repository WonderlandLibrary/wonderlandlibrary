package cn.hanabi.utils;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.events.EventMove;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.ModManager;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class PlayerUtil {
   private static final Minecraft MC = Minecraft.getMinecraft();
   private static Minecraft mc = Minecraft.getMinecraft();

   public static boolean isAirUnder(Entity ent) {
      return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1.0, ent.posZ)).getBlock() == Blocks.air;
   }

   public static boolean isUnderBlock(Entity ent) {
      return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY + 2.0, ent.posZ)).getBlock() != Blocks.air;
   }

   public static boolean isHoldingSword() {
      return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
   }

   public static double getBaseJumpHeight() {
      return isInLiquid() ? 0.13500000163912773 : 0.41999998688697815 + (double)((float)getJumpEffect() * 0.1F);
   }

   public static int getJumpEffect() {
      return mc.thePlayer.isPotionActive(Potion.jump) ? mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0;
   }

   public static int getSpeedEffect() {
      return mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
   }

   public static int getJumpEffect(EntityPlayer player) {
      return player.isPotionActive(Potion.jump) ? player.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0;
   }

   public static int getSpeedEffect(EntityPlayer player) {
      return player.isPotionActive(Potion.moveSpeed) ? player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
   }

   public static double getBaseMoveSpeed() {
      double baseSpeed = 0.2873;
      if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
         int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
         baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static float getDirection() {
      float yaw = mc.thePlayer.rotationYaw;
      if (mc.thePlayer.moveForward < 0.0F) {
         yaw += 180.0F;
      }

      float forward = 1.0F;
      if (mc.thePlayer.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (mc.thePlayer.moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (mc.thePlayer.moveStrafing > 0.0F) {
         yaw -= 90.0F * forward;
      }

      if (mc.thePlayer.moveStrafing < 0.0F) {
         yaw += 90.0F * forward;
      }

      yaw *= 0.017453292F;
      return yaw;
   }

   public static boolean isInWater() {
      return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).getBlock().getMaterial() == Material.water;
   }

   public static boolean isBlockUnder() {
      if (mc.thePlayer.posY < 0.0) {
         return true;
      } else {
         for(int i = (int)(mc.thePlayer.posY - 1.0); i > 0; --i) {
            if (!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, (double)i, mc.thePlayer.posZ)).getBlock() instanceof BlockAir)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isInLiquid() {
      if (mc.thePlayer.isInWater()) {
         return true;
      } else {
         boolean inLiquid = false;
         int y = (int)mc.thePlayer.getEntityBoundingBox().minY;

         for(int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
               Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && block.getMaterial() != Material.air) {
                  if (!(block instanceof BlockLiquid)) {
                     return false;
                  }

                  inLiquid = true;
               }
            }
         }

         return inLiquid;
      }
   }

   public static void toFwd(double speed) {
      float yaw = mc.thePlayer.rotationYaw * 0.017453292F;
      EntityPlayerSP thePlayer = mc.thePlayer;
      thePlayer.motionX -= (double)MathHelper.sin(yaw) * speed;
      EntityPlayerSP thePlayer2 = mc.thePlayer;
      thePlayer2.motionZ += (double)MathHelper.cos(yaw) * speed;
   }

   public static double getSpeed() {
      return Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
   }

   public static void setSpeed(double speed) {
      mc.thePlayer.motionX = -(Math.sin((double)getDirection()) * speed);
      mc.thePlayer.motionZ = Math.cos((double)getDirection()) * speed;
   }

   public static boolean canEntityBeSeenFixed(Entity entityIn) {
      return mc.thePlayer.worldObj.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ)) == null || mc.thePlayer.worldObj.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(entityIn.posX, entityIn.posY, entityIn.posZ)) == null;
   }

   public static Block getBlock(BlockPos pos) {
      return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
   }

   public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
      double d0 = x1 - x2;
      double d2 = y1 - y2;
      double d3 = z1 - z2;
      return (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
   }

   public static boolean MovementInput() {
      return ((IKeyBinding)mc.gameSettings.keyBindForward).getPress() || ((IKeyBinding)mc.gameSettings.keyBindLeft).getPress() || ((IKeyBinding)mc.gameSettings.keyBindRight).getPress() || ((IKeyBinding)mc.gameSettings.keyBindBack).getPress();
   }

   public static void blockHit(Entity en, boolean value) {
      Minecraft mc = Minecraft.getMinecraft();
      ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
      if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword && (double)mc.thePlayer.swingProgress > 0.2) {
         mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
      }

   }

   public static float getItemAtkDamage(ItemStack itemStack) {
      Multimap multimap = itemStack.getAttributeModifiers();
      if (!multimap.isEmpty()) {
         Iterator iterator = multimap.entries().iterator();
         if (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            if (attributeModifier.getAmount() > 1.0) {
               return 1.0F + (float)damage;
            }

            return 1.0F;
         }
      }

      return 1.0F;
   }

   public static int bestWeapon(Entity target) {
      Minecraft mc = Minecraft.getMinecraft();
      int firstSlot = mc.thePlayer.inventory.currentItem = 0;
      int bestWeapon = -1;
      int j = 1;

      for(byte i = 0; i < 9; ++i) {
         mc.thePlayer.inventory.currentItem = i;
         ItemStack itemStack = mc.thePlayer.getHeldItem();
         if (itemStack != null) {
            int itemAtkDamage = (int)getItemAtkDamage(itemStack);
            itemAtkDamage = (int)((float)itemAtkDamage + EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED));
            if (itemAtkDamage > j) {
               j = itemAtkDamage;
               bestWeapon = i;
            }
         }
      }

      if (bestWeapon != -1) {
         return bestWeapon;
      } else {
         return firstSlot;
      }
   }

   public static List getLivingEntities() {
      return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter((entity) -> {
         return entity instanceof EntityLivingBase;
      }).filter((entity) -> {
         return entity != Minecraft.getMinecraft().thePlayer;
      }).map((entity) -> {
         return (EntityLivingBase)entity;
      }).toArray((x$0) -> {
         return new EntityLivingBase[x$0];
      }));
   }

   public static void tellPlayer(String string) {
      if (string != null && mc.thePlayer != null) {
         Hanabi.INSTANCE.debugUtils.add(new DebugUtil(string, 1000, DebugUtil.Type.NONE));
      }

   }

   public static void debugChat(Object string) {
      if (string != null && mc.thePlayer != null) {
         mc.thePlayer.addChatMessage(new ChatComponentText("§b[Hanabi] §r " + string));
      }

   }

   public static void debug(Object string) {
      if (string != null && mc.thePlayer != null && ModManager.getModule("Debug").isEnabled() && Client.rank.toLowerCase().contains("beta")) {
         Hanabi.INSTANCE.debugUtils.add(new DebugUtil((String)string, 1000, DebugUtil.Type.ERROR));
      }

   }

   public static boolean isMoving() {
      if (!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking()) {
         return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
      } else {
         return false;
      }
   }

   public static boolean isMoving2() {
      return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
   }

   public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
      double curX = startPos[0];
      double curY = startPos[1];
      double curZ = startPos[2];
      double endX = (double)endPos.getX() + 0.5;
      double endY = (double)endPos.getY() + 1.0;
      double endZ = (double)endPos.getZ() + 0.5;
      double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

      for(int count = 0; distance > slack; ++count) {
         distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
         if (count > 120) {
            break;
         }

         boolean next = false;
         double diffX = curX - endX;
         double diffY = curY - endY;
         double diffZ = curZ - endZ;
         double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
         if (diffX < 0.0) {
            curX += Math.min(Math.abs(diffX), offset);
         }

         if (diffX > 0.0) {
            curX -= Math.min(Math.abs(diffX), offset);
         }

         if (diffY < 0.0) {
            curY += Math.min(Math.abs(diffY), 0.25);
         }

         if (diffY > 0.0) {
            curY -= Math.min(Math.abs(diffY), 0.25);
         }

         if (diffZ < 0.0) {
            curZ += Math.min(Math.abs(diffZ), offset);
         }

         if (diffZ > 0.0) {
            curZ -= Math.min(Math.abs(diffZ), offset);
         }

         Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
      }

   }

   public static void damage(int damage) {
      for(int index = 0; index <= 67 + 23 * (damage - 1); ++index) {
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.535E-9, mc.thePlayer.posZ, false));
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.05E-10, mc.thePlayer.posZ, false));
         mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0448865, mc.thePlayer.posZ, false));
      }

   }

   public static List getCollidingBoundingList(EntityPlayerSP thePlayer, float f) {
      return mc.theWorld.getCollidingBoundingBoxes(thePlayer, thePlayer.getEntityBoundingBox().offset(0.0, (double)(-f), 0.0));
   }

   public static Block getBlockBelowEntity(Entity entity, double offset) {
      Vec3 below = entity.getPositionVector();
      return MC.theWorld.getBlockState((new BlockPos(below)).add(0.0, -offset, 0.0)).getBlock();
   }

   public static void setSpeed(EventMove event, double speed) {
      float yaw = mc.thePlayer.rotationYaw;
      double forward = (double)mc.thePlayer.movementInput.moveForward;
      double strafe = (double)mc.thePlayer.movementInput.moveStrafe;
      if (forward == 0.0 && strafe == 0.0) {
         event.setX(0.0);
         event.setZ(0.0);
      } else {
         if (forward != 0.0) {
            if (strafe > 0.0) {
               yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
               yaw += (float)(forward > 0.0 ? 45 : -45);
            }

            strafe = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else {
               forward = -1.0;
            }
         }

         event.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
         event.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
      }

   }

   public static double getLastDist() {
      double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
      double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
      return Math.sqrt(xDist * xDist + zDist * zDist);
   }

   public void portMove(float yaw, float multiplyer, float up) {
      double moveX = -Math.sin(Math.toRadians((double)yaw)) * (double)multiplyer;
      double moveZ = Math.cos(Math.toRadians((double)yaw)) * (double)multiplyer;
      mc.thePlayer.setPosition(moveX + mc.thePlayer.posX, (double)up + mc.thePlayer.posY, moveZ + mc.thePlayer.posZ);
   }

   public final Block getBlockBelowPlayer(double offset) {
      return getBlockBelowEntity(MC.thePlayer, offset);
   }

   public static MovingObjectPosition rayCastedBlock(float yaw, float pitch) {
      float range = mc.playerController.getBlockReachDistance();
      Vec3 vec31 = getVectorForRotation(pitch, yaw);
      Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
      Vec3 vec32 = vec3.addVector(vec31.xCoord * (double)range, vec31.yCoord * (double)range, vec31.zCoord * (double)range);
      MovingObjectPosition ray = mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, false);
      return ray != null && ray.typeOfHit == MovingObjectType.BLOCK ? ray : null;
   }

   protected static final Vec3 getVectorForRotation(float pitch, float yaw) {
      float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
      float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
      float f2 = -MathHelper.cos(-pitch * 0.017453292F);
      float f3 = MathHelper.sin(-pitch * 0.017453292F);
      return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
   }
}
