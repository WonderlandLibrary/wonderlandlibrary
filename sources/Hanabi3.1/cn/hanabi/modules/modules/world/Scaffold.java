package cn.hanabi.modules.modules.world;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventSafeWalk;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;

public class Scaffold extends Mod {
   private final Value towerMode = (new Value("Scaffold", "TowerMode", 0)).LoadValue(new String[]{"None", "NCP", "AACv4"});
   private final Value delay = new Value("Scaffold", "Place Delay", 0.0, 0.0, 500.0, 10.0);
   private final Value diagonal = new Value("Scaffold", "Diagonal", true);
   private final Value silent = new Value("Scaffold", "Slient", true);
   private final Value noSwing = new Value("Scaffold", "No Swing", true);
   private final Value safeWalk = new Value("Scaffold", "Safe Walk", true);
   private final Value onlyGround = new Value("Scaffold", "Only Ground", true);
   private final Value sprint = new Value("Scaffold", "Sprint", true);
   private final Value sneak = new Value("Scaffold", "Sneak", true);
   private final Value speedlimit = new Value("Scaffold", "Move Motify", 1.0, 0.6, 1.2, 0.1);
   private final Value rayCast = new Value("Scaffold", "Ray Cast", true);
   private final Value render = new Value("Scaffold", "ESP", true);
   private final Value sneakAfter = new Value("Scaffold", "Sneak Tick", 1.0, 1.0, 10.0, 1.0);
   private final Value moveTower = new Value("Scaffold", "Move Tower", true);
   private final Value hypixel = new Value("Scaffold", "Hypixel", true);
   private final Value timer = new Value("Scaffold", "Timer Speed", 1.0, 0.1, 5.0, 0.01);
   private final Value turnspeed = new Value("Scaffold", "Rotation Speed", 6.0, 1.0, 6.0, 0.5);
   float curYaw;
   float curPitch;
   Vec3i rotate = null;
   int sneakCount;
   int slot;
   EnumFacing enumFacing;
   TimeHelper timeHelper = new TimeHelper();
   boolean istower;
   double jumpGround = 0.0;
   public static ItemStack items;
   List blackList;

   public Scaffold() {
      super("Scaffold", Category.WORLD);
      this.blackList = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.gravel, Blocks.ender_chest, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.redstone_torch);
   }

   public float[] faceBlock(BlockPos pos, float yTranslation, float currentYaw, float currentPitch, float speed) {
      double x = (double)((float)pos.getX() + 0.5F) - mc.thePlayer.posX - mc.thePlayer.motionX;
      double y = (double)((float)pos.getY() - yTranslation) - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
      double z = (double)((float)pos.getZ() + 0.5F) - mc.thePlayer.posZ - mc.thePlayer.motionZ;
      double calculate = (double)MathHelper.sqrt_double(x * x + z * z);
      float calcYaw = (float)(MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
      float calcPitch = (float)(-(MathHelper.atan2(y, calculate) * 180.0 / Math.PI));
      float yaw = this.updateRotation(currentYaw, calcYaw, speed);
      float pitch = this.updateRotation(currentPitch, calcPitch, speed);
      float sense = mc.gameSettings.mouseSensitivity * 0.8F + 0.2F;
      float fix = (float)(Math.pow((double)sense, 3.0) * 1.5);
      yaw -= yaw % fix;
      pitch -= pitch % fix;
      return new float[]{yaw, pitch >= 90.0F ? 90.0F : (pitch <= -90.0F ? -90.0F : pitch)};
   }

   private float[] getRotation(Vec3i vec3, float currentYaw, float currentPitch, float speed) {
      double xdiff = (double)vec3.getX() - mc.thePlayer.posX;
      double zdiff = (double)vec3.getZ() - mc.thePlayer.posZ;
      double y = (double)vec3.getY();
      double posy = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - y;
      double lastdis = (double)MathHelper.sqrt_double(xdiff * xdiff + zdiff * zdiff);
      float calcYaw = (float)(Math.atan2(zdiff, xdiff) * 180.0 / Math.PI) - 90.0F;
      float calcPitch = (float)(Math.atan2(posy, lastdis) * 180.0 / Math.PI);
      if (Float.compare(calcYaw, 0.0F) < 0) {
         calcPitch += 360.0F;
      }

      float yaw = this.updateRotation(currentYaw, calcYaw, speed);
      float pitch = this.updateRotation(currentPitch, calcPitch, speed);
      return new float[]{yaw, pitch >= 90.0F ? 90.0F : (pitch <= -90.0F ? -90.0F : pitch)};
   }

   public static Vec3i translate(BlockPos blockPos, EnumFacing enumFacing) {
      double x = (double)blockPos.getX();
      double y = (double)blockPos.getY();
      double z = (double)blockPos.getZ();
      double r1 = ThreadLocalRandom.current().nextDouble(0.3, 0.5);
      double r2 = ThreadLocalRandom.current().nextDouble(0.9, 1.0);
      if (enumFacing.equals(EnumFacing.UP)) {
         x += r1;
         z += r1;
         ++y;
      } else if (enumFacing.equals(EnumFacing.DOWN)) {
         x += r1;
         z += r1;
      } else if (enumFacing.equals(EnumFacing.WEST)) {
         y += r2;
         z += r1;
      } else if (enumFacing.equals(EnumFacing.EAST)) {
         y += r2;
         z += r1;
         ++x;
      } else if (enumFacing.equals(EnumFacing.SOUTH)) {
         y += r2;
         x += r1;
         ++z;
      } else if (enumFacing.equals(EnumFacing.NORTH)) {
         y += r2;
         x += r1;
      }

      return new Vec3i(x, y, z);
   }

   float updateRotation(float curRot, float destination, float speed) {
      float f = MathHelper.wrapAngleTo180_float(destination - curRot);
      if (f > speed) {
         f = speed;
      }

      if (f < -speed) {
         f = -speed;
      }

      return curRot + f;
   }

   @EventTarget
   private void onSafe(EventSafeWalk e) {
      if ((Boolean)this.safeWalk.getValue()) {
         e.setSafe(mc.thePlayer.onGround || !(Boolean)this.onlyGround.getValue());
      }

   }

   @EventTarget
   private void onPre(EventPreMotion e) {
      if (this.rotate != null) {
         e.setYaw(mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = this.curYaw);
         e.setPitch(this.curPitch);
      }

   }

   @EventTarget
   private void onPacket(EventPacket e) {
      if (e.getPacket() instanceof C09PacketHeldItemChange) {
         C09PacketHeldItemChange C09 = (C09PacketHeldItemChange)e.getPacket();
         if (this.slot != C09.getSlotId()) {
            this.slot = C09.getSlotId();
         }
      }

   }

   @EventTarget
   public void onMove(EventMove event) {
      if (mc.thePlayer.onGround) {
         EntityPlayerSP var10001 = mc.thePlayer;
         event.setX(var10001.motionX *= (Double)this.speedlimit.getValue());
         var10001 = mc.thePlayer;
         event.setZ(var10001.motionZ *= (Double)this.speedlimit.getValue());
      }

   }

   @EventTarget
   private void onUpdate(EventPostMotion e) {
      BlockPos blockPos = this.getBlockPosToPlaceOn(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ));
      if (blockPos != null) {
         this.rotate = translate(blockPos, this.enumFacing);
      }

      ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(this.slot);
      Wrapper.getTimer().timerSpeed = (float)((double)((Double)this.timer.getValue()).floatValue() + Math.random() / 100.0);
      if ((Boolean)this.silent.getValue() && (itemStack == null || !(itemStack.getItem() instanceof ItemBlock)) && this.slot != this.getBlockSlot()) {
         if (this.getBlockSlot() == -1) {
            return;
         }

         mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getBlockSlot()));
      }

      if (blockPos != null && itemStack != null && itemStack.getItem() instanceof ItemBlock) {
         items = itemStack;
         mc.thePlayer.setSprinting((Boolean)this.sprint.getValue());
         ((IKeyBinding)mc.gameSettings.keyBindSprint).setPress((Boolean)this.sprint.getValue());
         if ((Boolean)this.sneak.getValue() && (double)this.sneakCount >= (Double)this.sneakAfter.getValue()) {
            ((IKeyBinding)mc.gameSettings.keyBindSneak).setPress(true);
         } else if ((double)this.sneakCount < (Double)this.sneakAfter.getValue()) {
            ((IKeyBinding)mc.gameSettings.keyBindSneak).setPress(false);
         }

         float[] rotation = (Boolean)this.hypixel.getValue() ? this.getRotation(this.rotate, this.curYaw, this.curPitch, ((Double)this.turnspeed.getValue()).floatValue() * 30.0F) : this.faceBlock(blockPos, (float)(mc.theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMaxY() - mc.theWorld.getBlockState(blockPos).getBlock().getBlockBoundsMinY()) + 0.5F, this.curYaw, this.curPitch, ((Double)this.turnspeed.getValue()).floatValue() * 30.0F);
         this.curYaw = rotation[0];
         this.curPitch = rotation[1];
         MovingObjectPosition ray = PlayerUtil.rayCastedBlock(this.curYaw, this.curPitch);
         if (!this.timeHelper.isDelayComplete(((Double)this.delay.getValue()).longValue()) || (ray == null || !ray.getBlockPos().equals(blockPos)) && (Boolean)this.rayCast.getValue()) {
            if ((Boolean)this.sneak.getValue()) {
               ((IKeyBinding)mc.gameSettings.keyBindSneak).setPress(false);
            }
         } else {
            Vec3 hitVec = (Boolean)this.hypixel.getValue() ? new Vec3((double)this.rotate.getX(), (double)this.rotate.getY(), (double)this.rotate.getZ()) : (ray != null ? ray.hitVec : new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()));
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, blockPos, this.enumFacing, hitVec)) {
               ++this.sneakCount;
               if ((double)this.sneakCount > (Double)this.sneakAfter.getValue()) {
                  this.sneakCount = 0;
               }

               if (!(Boolean)this.noSwing.getValue()) {
                  mc.thePlayer.swingItem();
               } else {
                  mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
               }

               this.timeHelper.reset();
            }
         }

         if (MoveUtils.getJumpEffect() == 0 && mc.thePlayer.movementInput.jump && MoveUtils.isOnGround(0.15) && ((Boolean)this.moveTower.getValue() || !PlayerUtil.MovementInput()) && mc.gameSettings.keyBindJump.isKeyDown()) {
            Wrapper.getTimer().timerSpeed = 1.0F;
            this.istower = true;
            switch (this.towerMode.getModeAt(this.towerMode.getCurrentMode())) {
               case "NCP":
                  EntityPlayerSP var10000 = mc.thePlayer;
                  var10000.motionX *= 0.8;
                  var10000 = mc.thePlayer;
                  var10000.motionZ *= 0.8;
                  mc.thePlayer.motionY = 0.41999976;
                  break;
               case "AACv4":
                  if (mc.thePlayer.onGround) {
                     this.fakeJump();
                     this.jumpGround = mc.thePlayer.posY;
                     mc.thePlayer.motionY = 0.42;
                  }

                  if (mc.thePlayer.posY > this.jumpGround + 0.76) {
                     this.fakeJump();
                     mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                     mc.thePlayer.motionY = 0.42;
                     this.jumpGround = mc.thePlayer.posY;
                  }

                  Wrapper.getTimer().timerSpeed = 0.7F;
            }
         }
      }

   }

   public void onEnable() {
      this.sneakCount = 0;
      this.curYaw = mc.thePlayer.rotationYaw;
      this.curPitch = mc.thePlayer.rotationPitch;
      this.slot = mc.thePlayer.inventory.currentItem;
   }

   public void onDisable() {
      if ((Boolean)this.silent.getValue() && this.slot != mc.thePlayer.inventory.currentItem) {
         mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.slot = mc.thePlayer.inventory.currentItem));
      }

      if (Wrapper.getTimer().timerSpeed != 1.0F) {
         Wrapper.getTimer().timerSpeed = 1.0F;
      }

   }

   private BlockPos getBlockPosToPlaceOn(BlockPos pos) {
      BlockPos blockPos1 = pos.add(-1, 0, 0);
      BlockPos blockPos2 = pos.add(1, 0, 0);
      BlockPos blockPos3 = pos.add(0, 0, -1);
      BlockPos blockPos4 = pos.add(0, 0, 1);
      float down = 0.0F;
      if (mc.theWorld.getBlockState(pos.add(0.0, (double)(-1.0F - down), 0.0)).getBlock() != Blocks.air) {
         this.enumFacing = EnumFacing.UP;
         return pos.add(0, -1, 0);
      } else if (mc.theWorld.getBlockState(pos.add(-1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air) {
         this.enumFacing = EnumFacing.EAST;
         return pos.add(-1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(pos.add(1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air) {
         this.enumFacing = EnumFacing.WEST;
         return pos.add(1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(pos.add(0.0, (double)(0.0F - down), -1.0)).getBlock() != Blocks.air) {
         this.enumFacing = EnumFacing.SOUTH;
         return pos.add(0.0, (double)(0.0F - down), -1.0);
      } else if (mc.theWorld.getBlockState(pos.add(0.0, (double)(0.0F - down), 1.0)).getBlock() != Blocks.air) {
         this.enumFacing = EnumFacing.NORTH;
         return pos.add(0.0, (double)(0.0F - down), 1.0);
      } else if (mc.theWorld.getBlockState(blockPos1.add(0.0, (double)(-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.UP;
         return blockPos1.add(0.0, (double)(-1.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos1.add(-1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.EAST;
         return blockPos1.add(-1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos1.add(1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.WEST;
         return blockPos1.add(1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos1.add(0.0, (double)(0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.SOUTH;
         return blockPos1.add(0.0, (double)(0.0F - down), -1.0);
      } else if (mc.theWorld.getBlockState(blockPos1.add(0.0, (double)(0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.NORTH;
         return blockPos1.add(0.0, (double)(0.0F - down), 1.0);
      } else if (mc.theWorld.getBlockState(blockPos2.add(0.0, (double)(-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.UP;
         return blockPos2.add(0.0, (double)(-1.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos2.add(-1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.EAST;
         return blockPos2.add(-1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos2.add(1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.WEST;
         return blockPos2.add(1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos2.add(0.0, (double)(0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.SOUTH;
         return blockPos2.add(0.0, (double)(0.0F - down), -1.0);
      } else if (mc.theWorld.getBlockState(blockPos2.add(0.0, (double)(0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.NORTH;
         return blockPos2.add(0.0, (double)(0.0F - down), 1.0);
      } else if (mc.theWorld.getBlockState(blockPos3.add(0.0, (double)(-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.UP;
         return blockPos3.add(0.0, (double)(-1.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos3.add(-1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.EAST;
         return blockPos3.add(-1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos3.add(1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.WEST;
         return blockPos3.add(1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos3.add(0.0, (double)(0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.SOUTH;
         return blockPos3.add(0.0, (double)(0.0F - down), -1.0);
      } else if (mc.theWorld.getBlockState(blockPos3.add(0.0, (double)(0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.NORTH;
         return blockPos3.add(0.0, (double)(0.0F - down), 1.0);
      } else if (mc.theWorld.getBlockState(blockPos4.add(0.0, (double)(-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.UP;
         return blockPos4.add(0.0, (double)(-1.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos4.add(-1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.EAST;
         return blockPos4.add(-1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos4.add(1.0, (double)(0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.WEST;
         return blockPos4.add(1.0, (double)(0.0F - down), 0.0);
      } else if (mc.theWorld.getBlockState(blockPos4.add(0.0, (double)(0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.SOUTH;
         return blockPos4.add(0.0, (double)(0.0F - down), -1.0);
      } else if (mc.theWorld.getBlockState(blockPos4.add(0.0, (double)(0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean)this.diagonal.getValue()) {
         this.enumFacing = EnumFacing.NORTH;
         return blockPos4.add(0.0, (double)(0.0F - down), 1.0);
      } else {
         return null;
      }
   }

   @EventTarget
   public void on3D(EventRender event) {
      if ((Boolean)this.render.getValueState()) {
         this.esp(mc.thePlayer, event.getPartialTicks(), 0.5);
         this.esp(mc.thePlayer, event.getPartialTicks(), 0.4);
      }

   }

   public void esp(Entity entity, float partialTicks, double rad) {
      float points = 90.0F;
      GlStateManager.enableDepth();

      for(double il = 0.0; il < Double.MIN_VALUE; il += Double.MIN_VALUE) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glEnable(2848);
         GL11.glEnable(2881);
         GL11.glEnable(2832);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glHint(3154, 4354);
         GL11.glHint(3155, 4354);
         GL11.glHint(3153, 4354);
         GL11.glDisable(2929);
         GL11.glLineWidth(3.5F);
         GL11.glBegin(3);
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
         float speed = 5000.0F;
         float baseHue = (float)(System.currentTimeMillis() % (long)((int)speed));
         baseHue /= speed;

         for(int i = 0; i <= 90; ++i) {
            float max = ((float)i + (float)(il * 8.0)) / points;

            for(float hue = max + baseHue; hue > 1.0F; --hue) {
            }

            double pix2 = 6.283185307179586;

            int i2;
            for(i2 = 0; i2 <= 6; ++i2) {
               if (this.istower) {
                  GlStateManager.color(255.0F, 255.0F, 255.0F, 1.0F);
               } else {
                  GlStateManager.color(255.0F, 255.0F, 255.0F, 0.4F);
               }

               GL11.glVertex3d(x + rad * Math.cos((double)i2 * 6.283185307179586 / 6.0), y, z + rad * Math.sin((double)i2 * 6.283185307179586 / 6.0));
            }

            for(i2 = 0; i2 <= 6; ++i2) {
               if (this.istower) {
                  GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
               } else {
                  GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
               }

               GL11.glVertex3d(x + rad * Math.cos((double)i2 * 6.283185307179586 / 6.0) * 1.01, y, z + rad * Math.sin((double)i2 * 6.283185307179586 / 6.0) * 1.01);
            }
         }

         GL11.glEnd();
         GL11.glDepthMask(true);
         GL11.glEnable(2929);
         GL11.glDisable(2848);
         GL11.glDisable(2881);
         GL11.glEnable(2832);
         GL11.glEnable(3553);
         GL11.glPopMatrix();
         GlStateManager.color(255.0F, 255.0F, 255.0F);
      }

   }

   private void fakeJump() {
      mc.thePlayer.isAirBorne = true;
      mc.thePlayer.triggerAchievement(StatList.jumpStat);
   }

   private int getBlockSlot() {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
         if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize >= 1) {
            ItemBlock block = (ItemBlock)itemStack.getItem();
            if (!this.blackList.contains(block.getBlock())) {
               slot = i;
               break;
            }
         }
      }

      return slot;
   }
}
