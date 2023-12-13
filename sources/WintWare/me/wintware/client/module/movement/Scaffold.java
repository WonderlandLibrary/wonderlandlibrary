package me.wintware.client.module.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.event.impl.EventRender2D;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.event.impl.EventSafeWalk;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.ui.notification.NotificationPublisher;
import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.combat.RotationSpoofer;
import me.wintware.client.utils.combat.RotationUtil;
import me.wintware.client.utils.inventory.InventoryUtil;
import me.wintware.client.utils.other.RandomUtils;
import me.wintware.client.utils.other.TimerHelper;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Scaffold extends Module {
   public static final List<Block> invalidBlocks;
   private final List<Block> validBlocks;
   private final BlockPos[] blockPositions;
   private final EnumFacing[] facings;
   private final float[] angles;
   public static Scaffold.BlockData data;
   private int slot;
   public static boolean isSneaking;
   private static final double dif;
   private final TimerHelper time;
   private final TimerHelper timer;
   private double speed;
   private int item;
   private boolean canPlace;
   private double pitch;
   Object localObject;
   private int itspoof;
   private final Setting jump;
   private final Setting tower;
   private final Setting packetSneak;
   private final Setting swing;
   private final Setting delay1;
   private final Setting delay2;
   private final Setting chance3;
   public static Setting cast;

   public Scaffold() {
      super("Scaffold", Category.Movement);
      this.validBlocks = Arrays.asList(Blocks.AIR, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA);
      this.blockPositions = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 0)};
      this.facings = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH};
      this.angles = new float[2];
      this.time = new TimerHelper();
      this.timer = new TimerHelper();
      ArrayList<String> rots = new ArrayList();
      rots.add("Matrix");
      rots.add("None");
      Main.instance.setmgr.rSetting(new Setting("BlockRotation Mode", this, "Matrix", rots));
      ArrayList<String> motion = new ArrayList();
      motion.add("Zitter");
      motion.add("MoonWalk");
      Main.instance.setmgr.rSetting(new Setting("Jitter Mode", this, "Zitter", motion));
      Main.instance.setmgr.rSetting(this.chance3 = new Setting("PlaceChance", this, 100.0D, 0.0D, 100.0D, false));
      Main.instance.setmgr.rSetting(this.delay1 = new Setting("PlaceDelay", this, 55.0D, 0.0D, 300.0D, false));
      Main.instance.setmgr.rSetting(this.delay2 = new Setting("PlaceDelayRandom", this, 0.0D, 0.0D, 1000.0D, false));
      Main.instance.setmgr.rSetting(new Setting("BlockRayCast", this, false));
      Main.instance.setmgr.rSetting(cast = new Setting("BlockCastReduce", this, 0.9D, 0.4D, 1.5D, false));
      Main.instance.setmgr.rSetting(new Setting("ScaffoldSpeed", this, 0.6D, 0.05D, 1.2D, false));
      Main.instance.setmgr.rSetting(new Setting("VaildCheck", this, true));
      Main.instance.setmgr.rSetting(this.tower = new Setting("Tower", this, true));
      Main.instance.setmgr.rSetting(this.jump = new Setting("Jump", this, false));
      Main.instance.setmgr.rSetting(this.packetSneak = new Setting("PacketSneak", this, true));
      Main.instance.setmgr.rSetting(this.swing = new Setting("SwingHand", this, false));
      Main.instance.setmgr.rSetting(new Setting("AutoMotionStop", this, true));
      Main.instance.setmgr.rSetting(new Setting("SprintOFF", this, true));
      Main.instance.setmgr.rSetting(new Setting("BlockSafe", this, true));
      Main.instance.setmgr.rSetting(new Setting("MotionJitter", this, false));
      Main.instance.setmgr.rSetting(new Setting("YawRandom", this, 1.5D, 0.1D, 5.0D, false));
      Main.instance.setmgr.rSetting(new Setting("PitchRandom", this, 1.5D, 0.1D, 5.0D, false));
   }

   public void onEnable() {
      NotificationPublisher.queue("Module", "Scaffold was Enabled!", NotificationType.INFO);
      if (!InventoryUtil.doesHotbarHaveBlock()) {
         NotificationPublisher.queue("Scaffold", "You have no blocks in your hotbar!", NotificationType.INFO);
      }

      this.angles[0] = 999.0F;
      this.angles[1] = 999.0F;
      if (Main.instance.setmgr.getSettingByName("AutoMotionStop").getValue()) {
         Minecraft var10000 = mc;
         EntityPlayerSP var1 = Minecraft.player;
         var1.motionX *= -1.15D;
         var10000 = mc;
         var1 = Minecraft.player;
         var1.motionZ *= -1.15D;
      }

      Minecraft var10001 = mc;
      this.slot = Minecraft.player.inventory.currentItem;
      data = null;
      super.onEnable();
   }

   @EventTarget
   public void onUpdate(EventPreMotionUpdate eventUpdate) {
      Minecraft var10002 = mc;
      this.item = this.findBlock(Minecraft.player.inventoryContainer);
      Minecraft var10000;
      EntityPlayerSP var21;
      if (InventoryUtil.doesHotbarHaveBlock()) {
         if (this.jump.getValue() && (!this.jump.getValue() || !mc.gameSettings.keyBindJump.isKeyDown())) {
            var10000 = mc;
            if (Minecraft.player.onGround) {
               mc.gameSettings.keyBindJump.pressed = false;
               var10000 = mc;
               Minecraft.player.jump();
            }
         }

         if (Main.instance.setmgr.getSettingByName("AutoMotionStop").getValue()) {
            var10000 = mc;
            if (Minecraft.player.isSprinting() && mc.gameSettings.keyBindForward.isKeyDown() && Main.instance.setmgr.getSettingByName("SprintOFF").getValue() && this.jump.getValue()) {
               var10000 = mc;
               var21 = Minecraft.player;
               var21.motionX *= -1.15D;
               var10000 = mc;
               var21 = Minecraft.player;
               var21.motionZ *= -1.15D;
            }
         }

         var10000 = mc;
         var21 = Minecraft.player;
         var21.motionX /= 1.0499999523162842D;
         var10000 = mc;
         var21 = Minecraft.player;
         var21.motionZ /= 1.0499999523162842D;
      }

      String mod = Main.instance.setmgr.getSettingByName("BlockRotation Mode").getValString();
      this.setSuffix(mod);
      double yDif = 1.0D;
      var10000 = mc;

      for(double posY = Minecraft.player.posY - 1.0D; posY > 0.0D; --posY) {
         BlockPos var26;
         if (isSneaking) {
            var10002 = mc;
            var26 = (new BlockPos(Minecraft.player)).add(0.0D, -1.0D, 0.0D).down();
         } else {
            var10002 = mc;
            var26 = (new BlockPos(Minecraft.player)).add(0.0D, -1.0D, 0.0D);
         }

         BlockPos blockPos = var26;
         Scaffold.BlockData newData = this.getBlockData2(blockPos);
         if (newData != null) {
            var10000 = mc;
            yDif = Minecraft.player.posY - posY;
            if (yDif <= 7.0D) {
               data = newData;
            }
         }
      }

      double var10001;
      double var24;
      if (!this.jump.getValue()) {
         label137: {
            var10000 = mc;
            var21 = Minecraft.player;
            var10001 = var21.motionX;
            var10002 = mc;
            if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
               var10002 = mc;
               if (Minecraft.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                  var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble() / 1.37D;
                  break label137;
               }
            }

            var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble();
         }

         label131: {
            var21.motionX = var10001 * var24;
            var10000 = mc;
            var21 = Minecraft.player;
            var10001 = var21.motionZ;
            var10002 = mc;
            if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
               var10002 = mc;
               if (Minecraft.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                  var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble() / 1.37D;
                  break label131;
               }
            }

            var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble();
         }

         var21.motionZ = var10001 * var24;
      }

      if (Main.instance.setmgr.getSettingByName("MotionJitter").getValue()) {
         String mo = Main.instance.setmgr.getSettingByName("Jitter Mode").getValString();
         double dir;
         if (mo.equalsIgnoreCase("Zitter")) {
            dir = RandomUtils.getRandomDouble(0.008D, -0.008D);
            var10000 = mc;
            var21 = Minecraft.player;
            var21.motionX += dir;
            var10000 = mc;
            var21 = Minecraft.player;
            var21.motionZ -= dir;
         }

         if (mo.equalsIgnoreCase("SlowForward") && mc.gameSettings.keyBindForward.isKeyDown()) {
            var10000 = mc;
            dir = Math.toRadians(Minecraft.player.rotationYaw);
            var10000 = mc;
            var21 = Minecraft.player;
            var21.motionX += -Math.sin(dir) * -0.05D;
            var10000 = mc;
            var21 = Minecraft.player;
            var21.motionZ += Math.cos(dir) * -0.05D;
         }

         if (mo.equalsIgnoreCase("MoonWalk")) {
            label119: {
               var10000 = mc;
               var21 = Minecraft.player;
               var10001 = var21.motionX;
               var10002 = mc;
               if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                  var10002 = mc;
                  if (Minecraft.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                     var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble() / 1.37D;
                     break label119;
                  }
               }

               var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble();
            }

            label113: {
               var21.motionX = var10001 * var24;
               var10000 = mc;
               var21 = Minecraft.player;
               var10001 = var21.motionZ;
               var10002 = mc;
               if (Minecraft.player.isPotionActive(MobEffects.SPEED)) {
                  var10002 = mc;
                  if (Minecraft.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                     var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble() / 1.37D;
                     break label113;
                  }
               }

               var24 = Main.instance.setmgr.getSettingByName("ScaffoldSpeed").getValDouble();
            }

            var21.motionZ = var10001 * var24;
            var10000 = mc;
            if (Minecraft.player.ticksExisted % 7 == 0) {
               var10000 = mc;
               dir = Math.toRadians(Minecraft.player.rotationYaw - 90.0F);
               var10000 = mc;
               var21 = Minecraft.player;
               var21.motionX += -Math.sin(dir) * 0.03D;
               var10000 = mc;
               var21 = Minecraft.player;
               var21.motionZ += Math.cos(dir) * 0.03D;
            }

            var10000 = mc;
            if (Minecraft.player.ticksExisted % 11 == 0) {
               var10000 = mc;
               dir = Math.toRadians(Minecraft.player.rotationYaw + 90.0F);
               var10000 = mc;
               var21 = Minecraft.player;
               var21.motionX += -Math.sin(dir) * 0.003D;
               var10000 = mc;
               var21 = Minecraft.player;
               var21.motionZ += Math.cos(dir) * 0.003D;
               var10000 = mc;
               double dir1 = Math.toRadians(Minecraft.player.rotationYaw + 90.0F);
               var10000 = mc;
               var21 = Minecraft.player;
               Minecraft var22 = mc;
               EntityPlayerSP var23 = Minecraft.player;
               var10001 = var23.posX += -Math.sin(dir1) * 0.072D;
               var10002 = mc;
               Minecraft var10003 = mc;
               var21.setPosition(var10001, Minecraft.player.posY, Minecraft.player.posZ);
               var10000 = mc;
               var22 = mc;
               var10002 = mc;
               var10003 = mc;
               EntityPlayerSP var25 = Minecraft.player;
               Minecraft.player.setPosition(Minecraft.player.posX, Minecraft.player.posY, var25.posZ += Math.cos(dir1) * 0.072D);
            }
         }
      }

      if (Main.instance.setmgr.getSettingByName("SprintOFF").getValue() && this.getState()) {
         var10000 = mc;
         Minecraft.player.setSprinting(false);
      }

      if (data != null && this.slot != -1) {
         BlockPos pos = data.pos;
         EnumFacing facing = data.face;
         Block block = mc.world.getBlockState(pos.offset(data.face)).getBlock();
         Vec3d hitVec = this.getVec3(data);
         if (mod.equalsIgnoreCase("Matrix")) {
            float yawRand = (float)Main.instance.setmgr.getSettingByName("YawRandom").getValDouble();
            float pitchRand = (float)Main.instance.setmgr.getSettingByName("PitchRandom").getValDouble();
            float[] rots = RotationUtil.getNeededFacing(hitVec);
            float yawGCD = rots[0] + (float)RandomUtils.getRandomDouble(-yawRand, yawRand);
            float pitchGCD = rots[1] + (float)RandomUtils.getRandomDouble(-pitchRand, pitchRand);
            eventUpdate.setYaw(yawGCD);
            eventUpdate.setPitch(pitchGCD);
            var10000 = mc;
            Minecraft.player.renderYawOffset = yawGCD;
            var10000 = mc;
            Minecraft.player.rotationYawHead = yawGCD;
            var10000 = mc;
            Minecraft.player.rotationPitchHead = pitchGCD;
         }

         if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
            return;
         }

         if (mc.gameSettings.keyBindJump.isKeyDown() && this.tower.getValue()) {
            var10000 = mc;
            if (Minecraft.player.onGround) {
               var10000 = mc;
               Minecraft.player.jump();
            }
         }
      }

   }

   @EventTarget
   public void onSafeWalk(EventSafeWalk event) {
      Minecraft var10001;
      if (Main.instance.setmgr.getSettingByName("BlockSafe").getValue() && !isSneaking) {
         var10001 = mc;
         event.setCancelled(Minecraft.player.onGround);
      }

      Minecraft var10003 = mc;
      Minecraft var10004 = mc;
      double var12 = Minecraft.player.posY - 0.5D;
      Minecraft var10005 = mc;
      this.localObject = new BlockPos(Minecraft.player.posX, var12, Minecraft.player.posZ);
      if (InventoryUtil.doesHotbarHaveBlock()) {
         int slot = -1;
         boolean blockCount = false;

         Minecraft var10000;
         for(int i = 0; i < 9; ++i) {
            var10000 = mc;
            ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
            if (itemStack != null) {
               int stackSize = itemStack.stackSize;
               if (this.isValidItem(itemStack.getItem())) {
                  slot = i;
               }
            }
         }

         if (data != null && slot != -1) {
            BlockPos pos = data.pos;
            EnumFacing facing = data.face;
            mc.world.getBlockState(pos.offset(data.face)).getBlock();
            Vec3d hitVec = this.getVec3(data);
            if (this.itspoof != -1) {
               this.ItemSpoof();
            }

            if (this.time.check(this.delay1.getValFloat() + (float)RandomUtils.getRandomDouble(0.0D, this.delay2.getValFloat()))) {
               boolean look = RotationSpoofer.isLookingAt1(hitVec);
               if (!look && Main.instance.setmgr.getSettingByName("BlockRayCast").getValue()) {
                  return;
               }

               if (!this.jump.getValue() && this.packetSneak.getValue() && this.allowPlacing() && this.getState()) {
                  var10000 = mc;
                  var10003 = mc;
                  Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_SNEAKING));
               }

               if (Main.instance.setmgr.getSettingByName("VaildCheck").getValue() && (!this.validateDataRange(data) || !this.validateReplaceable(data))) {
                  return;
               }

               if (RandomUtils.getRandomDouble(0.0D, 100.0D) <= this.chance3.getValDouble()) {
                  var10001 = mc;
                  mc.playerController.processRightClickBlock(Minecraft.player, mc.world, pos, data.face, hitVec, EnumHand.MAIN_HAND);
                  if (this.swing.getValue()) {
                     var10000 = mc;
                     Minecraft.player.swingArm(EnumHand.MAIN_HAND);
                  } else {
                     mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                  }

                  this.time.reset();
               }
            }

            if (!this.jump.getValue() && this.packetSneak.getValue() && this.allowPlacing() && this.getState()) {
               var10000 = mc;
               var10003 = mc;
               Minecraft.player.connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
         }
      }

   }

   private boolean validateDataRange(Scaffold.BlockData data) {
      Vec3d hitVec = this.getVec3(data);
      Minecraft var10000 = mc;
      EntityPlayerSP entity = Minecraft.player;
      double x = hitVec.xCoord - entity.posX;
      double y = hitVec.yCoord - (entity.posY + (double)entity.getEyeHeight());
      double z = hitVec.zCoord - entity.posZ;
      return Math.sqrt(x * x + y * y + z * z) <= 4.0D;
   }

   private boolean validateReplaceable(Scaffold.BlockData data) {
      BlockPos pos = data.pos.offset(data.face);
      World world = mc.world;
      return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
   }

   private boolean allowPlacing() {
      Minecraft var10002 = mc;
      double var5 = Minecraft.player.posX - 0.024D;
      Minecraft var10003 = mc;
      double var6 = Minecraft.player.posY - 0.5D;
      Minecraft var10004 = mc;
      BlockPos localBlockPos1 = new BlockPos(var5, var6, Minecraft.player.posZ - 0.024D);
      var10002 = mc;
      var5 = Minecraft.player.posX - 0.024D;
      var10003 = mc;
      var6 = Minecraft.player.posY - 0.5D;
      var10004 = mc;
      BlockPos localBlockPos2 = new BlockPos(var5, var6, Minecraft.player.posZ + 0.024D);
      var10002 = mc;
      var5 = Minecraft.player.posX + 0.024D;
      var10003 = mc;
      var6 = Minecraft.player.posY - 0.5D;
      var10004 = mc;
      BlockPos localBlockPos3 = new BlockPos(var5, var6, Minecraft.player.posZ + 0.024D);
      var10002 = mc;
      var5 = Minecraft.player.posX + 0.024D;
      var10003 = mc;
      var6 = Minecraft.player.posY - 0.5D;
      var10004 = mc;
      BlockPos localBlockPos4 = new BlockPos(var5, var6, Minecraft.player.posZ - 0.024D);
      return mc.world.getBlockState(localBlockPos1).getBlock() == Blocks.AIR && mc.world.getBlockState(localBlockPos2).getBlock() == Blocks.AIR && mc.world.getBlockState(localBlockPos3).getBlock() == Blocks.AIR && mc.world.getBlockState(localBlockPos4).getBlock() == Blocks.AIR;
   }

   private boolean allowRotation() {
      double d = 0.1D;
      Minecraft var10002 = mc;
      double var7 = Minecraft.player.posX - 0.1D;
      Minecraft var10003 = mc;
      double var8 = Minecraft.player.posY - 0.5D;
      Minecraft var10004 = mc;
      BlockPos localBlockPos1 = new BlockPos(var7, var8, Minecraft.player.posZ - 0.1D);
      var10002 = mc;
      var7 = Minecraft.player.posX - 0.1D;
      var10003 = mc;
      var8 = Minecraft.player.posY - 0.5D;
      var10004 = mc;
      BlockPos localBlockPos2 = new BlockPos(var7, var8, Minecraft.player.posZ + 0.1D);
      var10002 = mc;
      var7 = Minecraft.player.posX + 0.1D;
      var10003 = mc;
      var8 = Minecraft.player.posY - 0.5D;
      var10004 = mc;
      BlockPos localBlockPos3 = new BlockPos(var7, var8, Minecraft.player.posZ + 0.1D);
      var10002 = mc;
      var7 = Minecraft.player.posX + 0.1D;
      var10003 = mc;
      var8 = Minecraft.player.posY - 0.5D;
      var10004 = mc;
      BlockPos localBlockPos4 = new BlockPos(var7, var8, Minecraft.player.posZ - 0.1D);
      return mc.world.getBlockState(localBlockPos1).getBlock() == Blocks.AIR && mc.world.getBlockState(localBlockPos2).getBlock() == Blocks.AIR && mc.world.getBlockState(localBlockPos3).getBlock() == Blocks.AIR && mc.world.getBlockState(localBlockPos4).getBlock() == Blocks.AIR;
   }

   static float getSensitivityMultiplier2() {
      float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
      return f * f * f * 8.0F * 0.15F;
   }

   private void ItemSpoof() {
      Minecraft var10001;
      if (this.itspoof != -1) {
         var10001 = mc;
         if (this.itspoof != Minecraft.player.inventory.currentItem) {
         }
      }

      ItemStack is = new ItemStack(Item.getItemById(261));

      try {
         for(int i = 36; i < 45; ++i) {
            int slot1 = i - 36;
            Minecraft var10000 = mc;
            Container var6 = Minecraft.player.inventoryContainer;
            var10000 = mc;
            if (!Container.canAddItemToSlot(Minecraft.player.inventoryContainer.getSlot(i), is, true)) {
               var10000 = mc;
               if (Minecraft.player.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock) {
                  var10000 = mc;
                  if (Minecraft.player.inventoryContainer.getSlot(i).getStack() != null) {
                     var10001 = mc;
                     if (this.isValidItem(Minecraft.player.inventoryContainer.getSlot(i).getStack().getItem())) {
                        var10000 = mc;
                        if (Minecraft.player.inventoryContainer.getSlot(i).getStack().stackSize != 0) {
                           var10000 = mc;
                           if (Minecraft.player.inventory.currentItem != slot1) {
                              if (this.itspoof != slot1) {
                                 CPacketHeldItemChange p = new CPacketHeldItemChange(slot1);
                                 this.itspoof = slot1;
                                 mc.getConnection().sendPacket(p);
                                 var10000 = mc;
                                 Minecraft.player.inventory.currentItem = slot1;
                                 mc.playerController.updateController();
                              } else {
                                 var10000 = mc;
                                 Minecraft.player.inventory.currentItem = slot1;
                                 mc.playerController.updateController();
                              }
                           } else {
                              var10000 = mc;
                              Minecraft.player.inventory.currentItem = slot1;
                              mc.playerController.updateController();
                           }
                           break;
                        }
                     }
                  }
               }
            }
         }
      } catch (Exception var5) {
      }

   }

   float to1801(float ang) {
      float value = ang % 360.0F;
      if (value >= 180.0F) {
         value -= 360.0F;
      }

      if (value < -180.0F) {
         value += 360.0F;
      }

      return value;
   }

   @EventTarget
   public void onRender(EventRender3D event) {
      if (data != null && this.slot != -1) {
         Minecraft var10000 = mc;
         double x = Minecraft.player.posX;
         var10000 = mc;
         double y = Minecraft.player.posY;
         var10000 = mc;
         double z = Minecraft.player.posZ;
         var10000 = mc;
         double yaw = (double)Minecraft.player.rotationYaw * 0.017453292D;
         BlockPos below = new BlockPos(x - Math.sin(yaw), y - 1.0D, z + Math.cos(yaw));
         Color color = new Color(255, 73, 73, 255);
         if (this.getBlockCount() >= 64 && 128 > this.getBlockCount()) {
            color = new Color(255, 231, 77, 255);
         } else if (this.getBlockCount() >= 128) {
            color = new Color(79, 255, 79, 255);
         }

         RenderUtil.blockEsp(below, color, 1.0D, 1.0D);
      }

   }

   @EventTarget
   public void onRender2D(EventRender2D render) {
      float width = (float)render.getResolution().getScaledWidth();
      float height = (float)render.getResolution().getScaledHeight();
      int color = (new Color(255, 73, 73, 255)).getRGB();
      if (this.getBlockCount() >= 64 && 128 > this.getBlockCount()) {
         color = (new Color(255, 231, 77, 255)).getRGB();
      } else if (this.getBlockCount() >= 128) {
         color = (new Color(79, 255, 79, 255)).getRGB();
      }

      GlStateManager.enableBlend();
      RenderUtil.drawOutlinedString(this.getBlockCount() + " ", mc.fontRenderer, width / 2.0F + 10.0F, height / 2.0F - (float)mc.fontRenderer.getStringHeight(this.getBlockCount() + " §fBlocks") / 2.0F, color);
   }

   private int getBlockCount() {
      int blockCount = 0;

      for(int i = 0; i < 45; ++i) {
         Minecraft var10000 = mc;
         if (Minecraft.player.inventoryContainer.getSlot(i).getHasStack()) {
            var10000 = mc;
            ItemStack is = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (this.isValidItem(item)) {
               blockCount += is.stackSize;
            }
         }
      }

      return blockCount;
   }

   private boolean isBlockUnder(double yOffset) {
      Minecraft var10004 = mc;
      Minecraft var10005 = mc;
      double var3 = Minecraft.player.posY - yOffset;
      Minecraft var10006 = mc;
      return !this.validBlocks.contains(mc.world.getBlockState(new BlockPos(Minecraft.player.posX, var3, Minecraft.player.posZ)).getBlock());
   }

   private boolean isValidItem(Item item) {
      if (item instanceof ItemBlock) {
         ItemBlock iBlock = (ItemBlock)item;
         Block block = iBlock.getBlock();
         return !invalidBlocks.contains(block);
      } else {
         return false;
      }
   }

   public Scaffold.BlockData getBlockData(BlockPos pos, int i) {
      return mc.world.getBlockState(pos.add(0, 0, i)).getBlock() != Blocks.AIR ? new Scaffold.BlockData(pos.add(0, 0, i), EnumFacing.NORTH) : (mc.world.getBlockState(pos.add(0, 0, -i)).getBlock() != Blocks.AIR ? new Scaffold.BlockData(pos.add(0, 0, -i), EnumFacing.SOUTH) : (mc.world.getBlockState(pos.add(i, 0, 0)).getBlock() != Blocks.AIR ? new Scaffold.BlockData(pos.add(i, 0, 0), EnumFacing.WEST) : (mc.world.getBlockState(pos.add(-i, 0, 0)).getBlock() != Blocks.AIR ? new Scaffold.BlockData(pos.add(-i, 0, 0), EnumFacing.EAST) : (mc.world.getBlockState(pos.add(0, -i, 0)).getBlock() != Blocks.AIR ? new Scaffold.BlockData(pos.add(0, -i, 0), EnumFacing.UP) : null))));
   }

   public final int findBlock(Container paramContainer) {
      for(int i = 0; i < 9; ++i) {
         ItemStack localItemStack = paramContainer.getSlot(i | 36).getStack();
         if (localItemStack != null && localItemStack.getItem() instanceof ItemBlock) {
            return i;
         }
      }

      return -1;
   }

   private int findBlockValue(Container paramContainer) {
      int i = 0;

      for(int j = 0; j < 9; ++j) {
         ItemStack localItemStack = paramContainer.getSlot(j | 36).getStack();
         if (localItemStack != null && localItemStack.getItem() instanceof ItemBlock) {
            i |= localItemStack.stackSize;
         }
      }

      return i;
   }

   public Scaffold.BlockData getBlockData2(BlockPos pos) {
      Scaffold.BlockData blockData = null;

      for(int i = 0; blockData == null && i < 2; ++i) {
         if (!this.isBlockPosAir(pos.add(0, 0, 1))) {
            blockData = new Scaffold.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
            break;
         }

         if (!this.isBlockPosAir(pos.add(0, 0, -1))) {
            blockData = new Scaffold.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
            break;
         }

         if (!this.isBlockPosAir(pos.add(1, 0, 0))) {
            blockData = new Scaffold.BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
            break;
         }

         if (!this.isBlockPosAir(pos.add(-1, 0, 0))) {
            blockData = new Scaffold.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
            break;
         }

         if (this.tower.getValue() && mc.gameSettings.keyBindJump.isKeyDown() && !this.isBlockPosAir(pos.add(0, -1, 0))) {
            blockData = new Scaffold.BlockData(pos.add(0, -1, 0), EnumFacing.UP);
            break;
         }

         if (!this.isBlockPosAir(pos.add(0, 1, 0)) && isSneaking) {
            blockData = new Scaffold.BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
            break;
         }

         if (!this.isBlockPosAir(pos.add(0, 1, 1)) && isSneaking) {
            blockData = new Scaffold.BlockData(pos.add(0, 1, 1), EnumFacing.DOWN);
            break;
         }

         if (!this.isBlockPosAir(pos.add(0, 1, -1)) && isSneaking) {
            blockData = new Scaffold.BlockData(pos.add(0, 1, -1), EnumFacing.DOWN);
            break;
         }

         if (!this.isBlockPosAir(pos.add(1, 1, 0)) && isSneaking) {
            blockData = new Scaffold.BlockData(pos.add(1, 1, 0), EnumFacing.DOWN);
            break;
         }

         if (!this.isBlockPosAir(pos.add(-1, 1, 0)) && isSneaking) {
            blockData = new Scaffold.BlockData(pos.add(-1, 1, 0), EnumFacing.DOWN);
            break;
         }

         if (!this.isBlockPosAir(pos.add(1, 0, 1))) {
            blockData = new Scaffold.BlockData(pos.add(1, 0, 1), EnumFacing.NORTH);
            break;
         }

         if (!this.isBlockPosAir(pos.add(-1, 0, -1))) {
            blockData = new Scaffold.BlockData(pos.add(-1, 0, -1), EnumFacing.SOUTH);
            break;
         }

         if (!this.isBlockPosAir(pos.add(1, 0, 1))) {
            blockData = new Scaffold.BlockData(pos.add(1, 0, 1), EnumFacing.WEST);
            break;
         }

         if (!this.isBlockPosAir(pos.add(-1, 0, -1))) {
            blockData = new Scaffold.BlockData(pos.add(-1, 0, -1), EnumFacing.EAST);
            break;
         }

         if (!this.isBlockPosAir(pos.add(-1, 0, 1))) {
            blockData = new Scaffold.BlockData(pos.add(-1, 0, 1), EnumFacing.NORTH);
            break;
         }

         if (!this.isBlockPosAir(pos.add(1, 0, -1))) {
            blockData = new Scaffold.BlockData(pos.add(1, 0, -1), EnumFacing.SOUTH);
            break;
         }

         if (!this.isBlockPosAir(pos.add(1, 0, -1))) {
            blockData = new Scaffold.BlockData(pos.add(1, 0, -1), EnumFacing.WEST);
            break;
         }

         if (!this.isBlockPosAir(pos.add(-1, 0, 1))) {
            blockData = new Scaffold.BlockData(pos.add(-1, 0, 1), EnumFacing.EAST);
            break;
         }

         pos = pos.down();
      }

      return blockData;
   }

   private Vec3d getVec3(Scaffold.BlockData data) {
      BlockPos pos = data.pos;
      EnumFacing face = data.face;
      double x = (double)pos.getX() + 0.5D;
      double y = (double)pos.getY() + 0.5D;
      double z = (double)pos.getZ() + 0.5D;
      if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
         y += 0.5D;
      } else {
         x += 0.3D;
         z += 0.3D;
      }

      if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
         z += 0.15D;
      }

      if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
         x += 0.15D;
      }

      return new Vec3d(x, y, z);
   }

   public Vec3d getPositionByFace(BlockPos position, EnumFacing facing) {
      Vec3d offset = new Vec3d((double)facing.getDirectionVec().getX() / 2.0D, (double)facing.getDirectionVec().getY() / 2.0D, (double)facing.getDirectionVec().getZ() / 2.0D);
      Vec3d point = new Vec3d((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D);
      return point.add(offset);
   }

   public boolean isBlockPosAir(BlockPos blockPos) {
      return this.getBlockByPos(blockPos) == Blocks.AIR || this.getBlockByPos(blockPos) instanceof BlockLiquid;
   }

   public Block getBlockByPos(BlockPos blockPos) {
      return mc.world.getBlockState(blockPos).getBlock();
   }

   private double randomNumber(double max, double min) {
      return Math.random() * (max - min) + min;
   }

   static {
      invalidBlocks = Arrays.asList(Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER, Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA, Blocks.SAND, Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_SLAB, Blocks.WOODEN_SLAB, Blocks.STONE_SLAB2, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.CACTUS, Blocks.LADDER, Blocks.WEB, Blocks.PUMPKIN);
      dif = 0.5D;
   }

   private static class BlockData {
      public final BlockPos pos;
      public final EnumFacing face;

      private BlockData(BlockPos pos, EnumFacing face) {
         this.pos = pos;
         this.face = face;
      }

      // $FF: synthetic method
      BlockData(BlockPos x0, EnumFacing x1, Object x2) {
         this(x0, x1);
      }
   }
}
