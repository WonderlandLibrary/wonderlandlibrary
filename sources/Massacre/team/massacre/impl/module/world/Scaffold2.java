package team.massacre.impl.module.world;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3d;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventCollide;
import team.massacre.impl.event.EventPacket;
import team.massacre.impl.event.EventRender2D;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.MovementUtils;
import team.massacre.utils.PacketUtil;
import team.massacre.utils.RapeUtils;
import team.massacre.utils.RotationUtil;
import team.massacre.utils.TimerUtil;

public class Scaffold2 extends Module {
   private static List<Block> invalidBlocks;
   public static boolean enabled;
   public TimerUtil timer = new TimerUtil();
   public TimerUtil collisiontimer = new TimerUtil();
   public TimerUtil towerTimer = new TimerUtil();
   public float nigger;
   public boolean isSprinting;
   int heldItem;
   int usedTicks;
   int placeCounter;
   public static float lastYaw;
   public static float lastPitch;
   public DecimalFormat format = new DecimalFormat("0.0");
   private static transient double keepPosY;
   public EnumProperty<Scaffold2.SprintMode> sprintMode;
   public EnumProperty<Scaffold2.BypassMode> bypassMode;
   public Property<Boolean> safeWalk;
   public Property<Boolean> tower;
   public Property<Boolean> swing;
   public Property<Boolean> spoof;
   public Property<Boolean> keepYpos;
   public Property<Boolean> timerBoost;
   public DoubleProperty timerSpeed;
   public static Property<Boolean> overrideYpos;
   public static Property<Boolean> collision;
   public static Property<Boolean> slowSpeed;

   public Scaffold2() {
      super("Scaffold", 0, Category.WORLD);
      this.sprintMode = new EnumProperty("Sprint Mode", Scaffold2.SprintMode.Cancel);
      this.bypassMode = new EnumProperty("Sprint Bypass Mode", Scaffold2.BypassMode.Normal, () -> {
         return this.sprintMode.getValue() == Scaffold2.SprintMode.Bypass;
      });
      this.safeWalk = new Property("Safe Walk", true);
      this.tower = new Property("Tower", true);
      this.swing = new Property("Swing", false);
      this.spoof = new Property("Spoof Sword", true);
      this.keepYpos = new Property("Keep Y", true);
      this.timerBoost = new Property("Timer Boost", true);
      this.timerSpeed = new DoubleProperty("Timer Speed", 1.65D, () -> {
         return (Boolean)this.timerBoost.getValue();
      }, 1.0D, 10.0D, 0.01D);
      this.addValues(new Property[]{this.sprintMode, this.bypassMode, this.timerSpeed, this.safeWalk, this.tower, this.swing, this.spoof, this.keepYpos, overrideYpos, collision, slowSpeed, this.timerBoost});
   }

   @Handler
   public void e(EventPacket event) {
      if (event.getPacket() instanceof C0BPacketEntityAction) {
         C0BPacketEntityAction c0b = (C0BPacketEntityAction)event.getPacket();
         if (c0b.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING && this.isSprinting) {
            this.isSprinting = false;
         } else if (c0b.getAction() == C0BPacketEntityAction.Action.START_SPRINTING && !this.isSprinting) {
            this.isSprinting = true;
         }
      }

   }

   public void onEnable() {
      super.onEnable();
      this.usedTicks = 0;
      this.placeCounter = 0;
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (this.sprintMode.getValue() == Scaffold2.SprintMode.Cancel && this.mc.thePlayer != null && this.mc.theWorld != null) {
            this.mc.thePlayer.setSprinting(false);
         }

         enabled = true;
         if (this.mc.thePlayer != null) {
            this.heldItem = this.mc.thePlayer.inventory.currentItem;
         }

      }
   }

   public void onDisable() {
      super.onDisable();
      enabled = false;
      this.mc.timer.timerSpeed = 1.0F;
      this.usedTicks = 0;
      this.mc.gameSettings.keyBindSneak.pressed = false;
      this.mc.thePlayer.movementInput.sneak = false;
      this.mc.thePlayer.inventory.currentItem = this.heldItem;
   }

   @Handler
   public void a(EventRender2D event) {
      ScaledResolution sr = new ScaledResolution(this.mc);
      int blockCount = getBlockCount();
      Color color = new Color(0, 255, 0);
      if (getBlockCount() > 128) {
         color = new Color(0, 255, 0);
      }

      if (getBlockCount() < 128) {
         color = new Color(255, 255, 0);
      }

      if (getBlockCount() < 64) {
         color = new Color(255, 0, 0);
      }

      this.mc.fontRendererObj.drawStringWithShadow(blockCount + "", (float)(sr.getScaledWidth() / 2 - -10 - this.mc.fontRendererObj.getStringWidth(blockCount + "") / 2), (float)(sr.getScaledHeight() / 2 + 30 + -21), color.getRGB());
   }

   @Handler
   public void ee(EventCollide e) {
      if ((Boolean)collision.getValue() && e.getBlock() instanceof BlockAir && (double)e.getY() < this.mc.thePlayer.posY) {
         e.setAxisAlignedBB(new AxisAlignedBB((double)e.getX(), (double)e.getY(), (double)e.getZ(), (double)e.getX() + 1.0D, this.mc.thePlayer.posY, (double)e.getZ() + 1.0D));
      }

   }

   @Handler
   public void a(EventUpdate e) {
      if (this.getState()) {
         if (!this.mc.thePlayer.movementInput.jump && this.sprintMode.getValue() == Scaffold2.SprintMode.Bypass) {
            switch((Scaffold2.BypassMode)this.bypassMode.getValue()) {
            case Normal:
               if (this.mc.thePlayer.ticksExisted % 5 == 0 && !this.isSprinting) {
                  if (e.isPre()) {
                     PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                  }
               } else if (this.mc.thePlayer.ticksExisted % 2 == 0 && this.isSprinting && e.isPost()) {
                  PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
               }
            }
         }

         if (RapeUtils.grabBlockSlot() == -1) {
            return;
         }

         if (this.sprintMode.getValue() != Scaffold2.SprintMode.Full && this.mc.thePlayer.isSprinting()) {
            this.mc.thePlayer.setSprinting(false);
         }

         if ((Boolean)slowSpeed.getValue() && this.mc.thePlayer.isPotionActive(Potion.moveSpeed) && !Massacre.INSTANCE.getModuleManager().getModule("Speed").getState()) {
            EntityPlayerSP var10000 = this.mc.thePlayer;
            var10000.motionX *= 0.8180000185966492D;
            var10000 = this.mc.thePlayer;
            var10000.motionZ *= 0.8180000185966492D;
         }

         if (MovementUtils.isOnGround(1.0E-5D)) {
            keepPosY = (double)((int)this.mc.thePlayer.posY - 1);
         }

         BlockPos underPos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
         Scaffold2.BlockData data = this.getBlockData(underPos);
         if ((Boolean)this.keepYpos.getValue() && Massacre.INSTANCE.getModuleManager().getModule("Speed").getState() && !(this.mc.thePlayer.posY - 1.0D < keepPosY) && (!(Boolean)overrideYpos.getValue() || !this.mc.gameSettings.keyBindJump.isPressed())) {
            underPos.y = (int)keepPosY;
         }

         if (this.getBlockSlot() != -1 && (Boolean)this.tower.getValue() && !(Boolean)this.keepYpos.getValue() && !(Boolean)overrideYpos.getValue() && this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isPotionActive(Potion.jump)) {
            if (!this.mc.thePlayer.isMoving()) {
               if (MovementUtils.isOnGround(0.35D)) {
                  if (this.usedTicks <= 5) {
                     this.mc.thePlayer.motionY = MovementUtils.getJumpHeight() - 1.6641999536659569E-4D;
                  } else if (this.timer.hasHit(1800.0F)) {
                     this.usedTicks = 0;
                     this.timer.reset();
                  }
               }
            } else {
               if (MovementUtils.isOnGround(0.35D) && this.timer.hasHit(150.0F)) {
                  this.mc.thePlayer.motionY = MovementUtils.getJumpHeight() - 1.6641999536659569E-4D;
               }

               if (this.mc.thePlayer.ticksExisted > 4 && (Boolean)this.timerBoost.getValue() && !this.mc.gameSettings.keyBindJump.isKeyDown()) {
                  this.mc.timer.timerSpeed = 2.145F;
               }
            }
         }

         if (e.isPre() && data.position != null && data.face != null) {
            if (this.bypassMode.getValue() == Scaffold2.BypassMode.Test && this.isSprinting) {
               PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            float[] rotations = this.getBlockRotations(data.position, data.face);
            float Sensitivity = MovementUtils.getSensitivityMultiplier();
            float yaw = (float)((double)rotations[0] + ThreadLocalRandom.current().nextDouble(-0.25D, 0.25D));
            float pitch = (float)((double)rotations[1] - ThreadLocalRandom.current().nextDouble(-0.25D, 0.25D));
            float yawSENS = (float)Math.round(yaw / Sensitivity) * Sensitivity;
            float pitchSENS = (float)Math.round(pitch / Sensitivity) * Sensitivity;
            e.setYaw(yawSENS);
            e.setPitch(pitchSENS);
            lastYaw = e.getYaw();
            lastPitch = e.getPitch();
         }

         if (this.mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
            if (e.isPre()) {
               if (!this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
                  e.setOnGround(false);
               }
            } else if (this.getBlockSlot() != -1) {
               int slot = this.mc.thePlayer.inventory.currentItem;
               if (!this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
                  e.setOnGround(false);
               }

               this.mc.thePlayer.inventory.currentItem = this.getBlockSlot();
               if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getCurrentEquippedItem(), data.position, data.face, RotationUtil.getVectorForRotation(e.getPitch(), e.getYaw()))) {
                  if (this.bypassMode.getValue() == Scaffold2.BypassMode.Test && !this.isSprinting) {
                     PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                  }

                  ++this.placeCounter;
                  ++this.usedTicks;
                  if (this.mc.thePlayer.isMovingOnGround() && (Boolean)this.timerBoost.getValue()) {
                     this.mc.timer.timerSpeed = (float)((Double)this.timerSpeed.getValue() + Math.random() / 10.0D);
                  }

                  if ((Boolean)this.swing.getValue()) {
                     this.mc.thePlayer.swingItem();
                  } else {
                     this.mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                  }
               }

               if ((Boolean)this.spoof.getValue()) {
                  this.mc.thePlayer.inventory.currentItem = slot;
               }
            }
         }
      }

   }

   private float[] getBlockRotations(BlockPos blockPos, EnumFacing enumFacing) {
      if (blockPos == null && enumFacing == null) {
         return null;
      } else {
         Vec3d positionEyes = this.mc.thePlayer.getPositionEyes1(2.0F);
         Vec3d add = (new Vec3d((double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.5D, (double)blockPos.getZ() + 0.5D)).add((new Vec3d(enumFacing.getDirectionVec())).scale(0.49000000953674316D));
         double n = add.xCoord - positionEyes.xCoord;
         double n2 = add.yCoord - positionEyes.yCoord;
         double n3 = add.zCoord - positionEyes.zCoord;
         return new float[]{(float)(Math.atan2(n3, n) * 180.0D / 3.141592653589793D - 90.0D), -((float)(Math.atan2(n2, (double)((float)Math.hypot(n, n3))) * 180.0D / 3.141592653589793D))};
      }
   }

   public int getBlockSlot() {
      for(int i = 36; i < 45; ++i) {
         if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack() != null && Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock)Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock())) {
            return i - 36;
         }
      }

      return -1;
   }

   public static boolean isPosSolid(BlockPos pos) {
      Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
      return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isFullBlock() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
   }

   public static boolean isOnGround(double height) {
      return !Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
   }

   private Scaffold2.BlockData getBlockData(BlockPos pos) {
      EnumFacing[] var2 = EnumFacing.VALUES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing face = var2[var4];
         if (this.shouldCancelCheck(face) && (Boolean)this.keepYpos.getValue() && this.mc.thePlayer.posY - 1.0D >= keepPosY && Massacre.INSTANCE.getModuleManager().getModule("Speed").getState()) {
            pos.y = (int)keepPosY;
         }
      }

      if (isPosSolid(pos.add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, -1, 0).add(0, -1, 0))) {
         return new Scaffold2.BlockData(pos.add(0, -1, 0).add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, -1, 0).add(-1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, -1, 0).add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, -1, 0).add(1, 0, 0))) {
         return new Scaffold2.BlockData(pos.add(0, -1, 0).add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, -1, 0).add(0, 0, 1))) {
         return new Scaffold2.BlockData(pos.add(0, -1, 0).add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
      } else if (isPosSolid(pos.add(0, -1, 0).add(0, 0, -1))) {
         return new Scaffold2.BlockData(pos.add(0, -1, 0).add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
      } else {
         BlockPos pos2 = pos.add(0, -1, 0).add(1, 0, 0);
         BlockPos pos3 = pos.add(0, -1, 0).add(0, 0, 1);
         BlockPos pos4 = pos.add(0, -1, 0).add(-1, 0, 0);
         BlockPos pos5 = pos.add(0, -1, 0).add(0, 0, -1);
         if (isPosSolid(pos2.add(0, -1, 0))) {
            return new Scaffold2.BlockData(pos2.add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new Scaffold2.BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos2.add(1, 0, 0))) {
            return new Scaffold2.BlockData(pos2.add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos2.add(0, 0, 1))) {
            return new Scaffold2.BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos2.add(0, 0, -1))) {
            return new Scaffold2.BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos4.add(0, -1, 0))) {
            return new Scaffold2.BlockData(pos4.add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new Scaffold2.BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos4.add(1, 0, 0))) {
            return new Scaffold2.BlockData(pos4.add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos4.add(0, 0, 1))) {
            return new Scaffold2.BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos4.add(0, 0, -1))) {
            return new Scaffold2.BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos3.add(0, -1, 0))) {
            return new Scaffold2.BlockData(pos3.add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new Scaffold2.BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos3.add(1, 0, 0))) {
            return new Scaffold2.BlockData(pos3.add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos3.add(0, 0, 1))) {
            return new Scaffold2.BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos3.add(0, 0, -1))) {
            return new Scaffold2.BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos5.add(0, -1, 0))) {
            return new Scaffold2.BlockData(pos5.add(0, -1, 0), EnumFacing.UP, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new Scaffold2.BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos5.add(1, 0, 0))) {
            return new Scaffold2.BlockData(pos5.add(1, 0, 0), EnumFacing.WEST, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos5.add(0, 0, 1))) {
            return new Scaffold2.BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH, (Scaffold2.BlockData)null);
         } else if (isPosSolid(pos5.add(0, 0, -1))) {
            return new Scaffold2.BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH, (Scaffold2.BlockData)null);
         } else {
            return null;
         }
      }
   }

   public boolean shouldCancelCheck(EnumFacing face) {
      if ((Boolean)this.keepYpos.getValue() && (Boolean)overrideYpos.getValue() && this.mc.gameSettings.keyBindJump.isPressed() && Massacre.INSTANCE.getModuleManager().getModule("Speed").getState()) {
         keepPosY = this.mc.thePlayer.posY;
         return true;
      } else if ((Boolean)this.keepYpos.getValue() && this.mc.thePlayer.posY - 1.0D >= keepPosY && Massacre.INSTANCE.getModuleManager().getModule("Speed").getState()) {
         return face != EnumFacing.UP;
      } else {
         return true;
      }
   }

   public static int getBlockCount() {
      int blockCount = 0;

      for(int i = 0; i < 45; ++i) {
         if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
            ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (is.getItem() instanceof ItemBlock && RapeUtils.canIItemBePlaced(item)) {
               blockCount += is.stackSize;
            }
         }
      }

      return blockCount;
   }

   static {
      invalidBlocks = Arrays.asList(Blocks.sand, Blocks.ladder, Blocks.flower_pot, Blocks.red_flower, Blocks.yellow_flower, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.beacon, Blocks.web, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.flower_pot, Blocks.double_plant);
      lastYaw = 0.0F;
      lastPitch = 0.0F;
      keepPosY = 0.0D;
      overrideYpos = new Property("Override Keep Y", true);
      collision = new Property("Collision", true);
      slowSpeed = new Property("Slowdown w/Speed", true);
   }

   private static class BlockData {
      public BlockPos position;
      public EnumFacing face;

      private BlockData(BlockPos position, EnumFacing face, Scaffold2.BlockData blockData) {
         this.position = position;
         this.face = face;
      }

      private EnumFacing getFacing() {
         return this.face;
      }

      private BlockPos getPosition() {
         return this.position;
      }

      // $FF: synthetic method
      BlockData(BlockPos x0, EnumFacing x1, Scaffold2.BlockData x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   public static enum BypassMode {
      Normal,
      Test;
   }

   public static enum SprintMode {
      Cancel,
      Bypass,
      Full;
   }
}
