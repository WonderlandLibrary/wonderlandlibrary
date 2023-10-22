package cn.hanabi.modules.modules.world;

import cn.hanabi.events.BBSetEvent;
import cn.hanabi.events.EventJump;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.BlockUtils;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Jesus extends Mod {
   private static final Value mode = new Value("Jesus", "Mode", 0);
   public static Value motionY = new Value("Jesus", "Motion Y", 0.27, 0.01, 0.4, 0.01);
   private final TimeHelper timer = new TimeHelper();
   int stage;

   public Jesus() {
      super("Jesus", Category.WORLD);
      mode.LoadValue(new String[]{"Solid", "Motion"});
   }

   public static boolean isOnLiquid() {
      if (mc.thePlayer == null) {
         return false;
      } else {
         boolean onLiquid = false;
         int y = (int)mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;

         for(int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
               Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && block.getMaterial() != Material.air) {
                  if (!(block instanceof BlockLiquid)) {
                     return false;
                  }

                  if (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL) instanceof Integer && (Integer)((Comparable)mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL)) > 1) {
                     return false;
                  }

                  onLiquid = true;
               }
            }
         }

         return onLiquid;
      }
   }

   @EventTarget
   public void onBounding(BBSetEvent event) {
      if (mode.isCurrentMode("Solid") && event.getBlock() instanceof BlockLiquid && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && this.isPossible(event.pos)) {
         event.setBoundingBox((new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)).contract(0.0, 2.0E-12, 0.0).offset((double)event.getPos().getX(), (double)event.getPos().getY(), (double)event.getPos().getZ()));
      }

   }

   @EventTarget
   public void onPacket(EventPreMotion event) {
      if (mode.isCurrentMode("Solid") && mc.thePlayer.onGround && !mc.thePlayer.isInWater() && BlockUtils.isOnLiquid() && !mc.thePlayer.isSneaking()) {
         event.y += mc.thePlayer.ticksExisted % 2 == 0 ? 2.0E-12 : 0.0;
      }

   }

   @EventTarget
   public void onJump(EventJump event) {
      if (BlockUtils.isOnLiquid()) {
         event.setCancelled(true);
      }

   }

   @EventTarget
   public void onMove(EventMove event) {
      if (mode.isCurrentMode("Motion") && isOnLiquid() && !this.isInLiquid()) {
         MoveUtils.setMotion(event, 0.2163);
      }

   }

   @EventTarget
   public void onUpdatePre(EventPreMotion event) {
      if (BlockUtils.isInLiquid() && mc.thePlayer.movementInput.jump) {
         mc.thePlayer.movementInput.jump = false;
         mc.thePlayer.setJumping(false);
      }

      if (mode.isCurrentMode("Motion")) {
         if (!ModManager.getModule("Speed").isEnabled() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            if (isOnLiquid() && !this.isInLiquid() && !this.isInLiquiddol()) {
               this.Motion(event);
               return;
            }

            if (this.isInLiquid() && this.isInLiquiddol()) {
               this.flat();
            }
         }

         this.stage = 0;
         if (mc.thePlayer.stepHeight < 0.02F) {
            mc.thePlayer.stepHeight = 0.6F;
         }
      }

      if (mode.isCurrentMode("BHop")) {
         if (BlockUtils.isInLiquid() && this.timer.isDelayComplete(200L) && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.isInWater()) {
            float forward = mc.thePlayer.movementInput.moveForward;
            float strafe = mc.thePlayer.movementInput.moveStrafe;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
            if (forward == 0.0F && strafe == 0.0F) {
               mc.thePlayer.motionY = 0.085;
            } else {
               mc.thePlayer.motionY = 0.4;
               mc.thePlayer.isAirBorne = true;
               float var4 = strafe * strafe + forward * forward;
               var4 = MathHelper.sqrt_float(var4);
               if (var4 < 1.0F) {
                  var4 = 1.0F;
               }

               var4 = 0.26F / var4;
               strafe *= var4;
               forward *= var4;
               float var5 = MathHelper.sin(mc.thePlayer.rotationYaw * 3.1415927F / 180.0F);
               float var6 = MathHelper.cos(mc.thePlayer.rotationYaw * 3.1415927F / 180.0F);
               EntityPlayerSP var10000 = mc.thePlayer;
               var10000.motionX += (double)(strafe * var6 - forward * var5);
               var10000 = mc.thePlayer;
               var10000.motionZ += (double)(forward * var6 + strafe * var5);
               this.timer.reset();
            }
         } else if (mc.thePlayer.isInWater() && mc.thePlayer.movementInput.jump) {
            mc.thePlayer.motionY = 0.08;
         }
      }

   }

   private boolean isPossible(BlockPos pos) {
      int i = mc.theWorld.getBlockState(pos).getBlock().getMetaFromState(mc.theWorld.getBlockState(pos));
      return i < 5 && mc.thePlayer.fallDistance <= 3.0F && !mc.thePlayer.isSneaking();
   }

   public boolean shouldGround(double n) {
      return n % 1.0 == 0.015625 || n % 1.0 == 0.0625 || n % 0.125 == 0.0;
   }

   @EventTarget
   public void onBlockCollide(BBSetEvent event) {
      int x = event.getPos().getX();
      int y = event.getPos().getY();
      int z = event.getPos().getZ();
      if (mode.isCurrentMode("Motion") && !ModManager.getModule("Speed").isEnabled() && !this.isInLiquid() && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
         if (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL) instanceof Integer && (Integer)((Comparable)mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL)) > 2) {
            return;
         }

         Object v2 = mc.theWorld.getBlockState(event.getPos()).getProperties().get(BlockLiquid.LEVEL);
         if ((!(v2 instanceof Integer) || (Integer)v2 <= 3) && mc.theWorld.getBlockState(event.getPos()).getBlock() instanceof BlockLiquid && !mc.thePlayer.isSneaking()) {
            event.setCancelled(true);
            event.setBoundingBox(new AxisAlignedBB((double)event.getPos().getX(), (double)event.getPos().getY(), (double)event.getPos().getZ(), (double)event.getPos().getX() + 0.98, (double)event.getPos().getY() + 1.0, (double)event.getPos().getZ() + 0.98));
         }
      }

   }

   private boolean isInLiquid() {
      if (mc.thePlayer == null) {
         return false;
      } else {
         for(int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
               BlockPos pos = new BlockPos(x, (int)mc.thePlayer.getEntityBoundingBox().minY, z);
               Block block = mc.theWorld.getBlockState(pos).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  if (block instanceof BlockLiquid && mc.theWorld.getBlockState(pos).getProperties().get(BlockLiquid.LEVEL) instanceof Integer && (Integer)((Comparable)mc.theWorld.getBlockState(pos).getProperties().get(BlockLiquid.LEVEL)) > 1) {
                     return false;
                  }

                  return block instanceof BlockLiquid;
               }
            }
         }

         return false;
      }
   }

   public void Motion(EventPreMotion em) {
      if (mc.thePlayer.fallDistance == 0.0F) {
         mc.thePlayer.stepHeight = 0.015625F;
         ++this.stage;
         if (this.stage == 1) {
            em.setY(em.y - ThreadLocalRandom.current().nextDouble(0.0155249999999, 0.015625));
         }

         if (this.stage == 2) {
            em.setY(em.y + ThreadLocalRandom.current().nextDouble(0.0148999999999, 0.015));
         }

         if (this.stage == 3) {
            em.setY(em.y + ThreadLocalRandom.current().nextDouble(0.0198999999999, 0.02));
         }

         if (this.stage >= 4) {
            em.setY(em.y + 0.015625);
            this.stage = 0;
         }

         if (this.stage % 2 == 0) {
            em.setY(em.y - 1.0E-13);
         }

         em.setY(em.y + 1.0E-13);
         em.setOnGround(this.shouldGround(em.y));
      }
   }

   public void flat() {
      mc.thePlayer.motionY = 0.11999998688698;
   }

   public boolean isInLiquiddol() {
      if (mc.thePlayer == null) {
         return false;
      } else {
         boolean inLiquid = false;
         int y = (int)(mc.thePlayer.getEntityBoundingBox().minY + 0.02);

         for(int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
               Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
               if (block != null && !(block instanceof BlockAir)) {
                  if (!(block instanceof BlockLiquid)) {
                     return false;
                  }

                  if (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL) instanceof Integer && (Integer)((Comparable)mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL)) > 1) {
                     return false;
                  }

                  inLiquid = true;
               }
            }
         }

         return inLiquid;
      }
   }
}
