package cn.hanabi.modules.modules.player;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.injection.interfaces.IPlayerControllerMP;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.CombatUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public class Nuker extends Mod {
   private final TimeHelper timer2 = new TimeHelper();
   private final TimeHelper timer = new TimeHelper();
   private final Value mode = new Value("Nuker", "Mode", 0);
   private final Value reach = new Value("Nuker", "Reach", 6.0, 1.0, 6.0, 0.1);
   private final Value delay = new Value("Nuker", "Delay", 120.0, 0.0, 1000.0, 10.0);
   private final Value instant = new Value("Nuker", "Instant", false);
   ArrayList positions = null;

   public Nuker() {
      super("Nuker", Category.PLAYER);
      this.mode.LoadValue(new String[]{"Bed", "Egg", "Cake"});
   }

   @EventTarget
   public void onPre(EventPreMotion event) {
      Iterator positions = BlockPos.getAllInBox(mc.thePlayer.getPosition().subtract(new Vec3i((Double)this.reach.getValueState(), (Double)this.reach.getValueState(), (Double)this.reach.getValueState())), mc.thePlayer.getPosition().add(new Vec3i((Double)this.reach.getValueState(), (Double)this.reach.getValueState(), (Double)this.reach.getValueState()))).iterator();
      BlockPos bedPos = null;

      while((bedPos = (BlockPos)positions.next()) != null && (!(mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockBed) || !this.mode.isCurrentMode("Bed")) && (!(mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockDragonEgg) || !this.mode.isCurrentMode("Egg"))) {
         if (mc.theWorld.getBlockState(bedPos).getBlock() instanceof BlockCake && this.mode.isCurrentMode("Cake")) {
            break;
         }
      }

      if (bedPos != null) {
         float[] rot = CombatUtil.getRotationsNeededBlock((double)bedPos.getX(), (double)bedPos.getY(), (double)bedPos.getZ());
         event.yaw = rot[0];
         event.pitch = rot[1];
         if (this.timer.isDelayComplete((Double)this.delay.getValueState())) {
            if ((Boolean)this.instant.getValueState()) {
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
               mc.thePlayer.swingItem();
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            } else {
               if (((IPlayerControllerMP)mc.playerController).getBlockDELAY() > 1) {
                  ((IPlayerControllerMP)mc.playerController).setBlockHitDelay(1);
               }

               mc.thePlayer.swingItem();
               EnumFacing direction = this.getClosestEnum(bedPos);
               if (direction != null) {
                  mc.playerController.onPlayerDamageBlock(bedPos, direction);
               }
            }

            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, bedPos, EnumFacing.DOWN));
            mc.thePlayer.swingItem();
            this.timer.reset();
         }
      }

   }

   private EnumFacing getClosestEnum(BlockPos pos) {
      EnumFacing closestEnum = EnumFacing.UP;
      float rotations = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
      if (rotations >= 45.0F && rotations <= 135.0F) {
         closestEnum = EnumFacing.EAST;
      } else if ((!(rotations >= 135.0F) || !(rotations <= 180.0F)) && (!(rotations <= -135.0F) || !(rotations >= -180.0F))) {
         if (rotations <= -45.0F && rotations >= -135.0F) {
            closestEnum = EnumFacing.WEST;
         } else if (rotations >= -45.0F && rotations <= 0.0F || rotations <= 45.0F && rotations >= 0.0F) {
            closestEnum = EnumFacing.NORTH;
         }
      } else {
         closestEnum = EnumFacing.SOUTH;
      }

      if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0F || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0F) {
         closestEnum = EnumFacing.UP;
      }

      return closestEnum;
   }

   public float[] getRotations(BlockPos block, EnumFacing face) {
      double x = (double)block.getX() + 0.5 - mc.thePlayer.posX;
      double z = (double)block.getZ() + 0.5 - mc.thePlayer.posZ;
      double d1 = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
      double d3 = (double)MathHelper.sqrt_double(x * x + z * z);
      float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
      float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
      if (yaw < 0.0F) {
         yaw += 360.0F;
      }

      return new float[]{yaw, pitch};
   }
}
