package cn.hanabi.modules.modules.movement;

import aLph4anTi1eaK_cN.Annotation.ObfuscationMethod;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventStep;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.modules.modules.movement.Speed.Speed;
import cn.hanabi.utils.BlockUtils;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;

@ObfuscationMethod
public class Step extends Mod {
   public Value mode = new Value("Step", "Mode", 0);
   public Value height = new Value("Step", "Height", 1.0, 1.0, 1.5, 0.5);
   public Value delay = new Value("Step", "Delay", 0.0, 0.0, 1000.0, 50.0);
   TimeHelper timer = new TimeHelper();
   boolean resetTimer;

   public Step() {
      super("Step", Category.MOVEMENT);
      this.mode.LoadValue(new String[]{"Vanilla", "NCP", "Hyt", "Test"});
   }

   public void onEnable() {
      this.resetTimer = false;
      super.onEnable();
   }

   public void onDisable() {
      if (mc.thePlayer != null) {
         mc.thePlayer.stepHeight = 0.625F;
      }

      ((IMinecraft)mc).getTimer().timerSpeed = 1.0F;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
      if (((IMinecraft)mc).getTimer().timerSpeed < 1.0F && mc.thePlayer.onGround) {
         ((IMinecraft)mc).getTimer().timerSpeed = 1.0F;
      }

   }

   @EventTarget
   public void onStep(EventStep event) {
      if (!BlockUtils.isInLiquid() && !((Speed)ModManager.getModule(Speed.class)).isEnabled()) {
         if (this.mode.isCurrentMode("Vanilla")) {
            event.setHeight(mc.thePlayer.stepHeight = ((Double)this.height.getValueState()).floatValue());
         }

         if (this.mode.isCurrentMode("NCP")) {
            if (event.getEventType() == EventType.PRE) {
               if (this.resetTimer) {
                  this.resetTimer = false;
                  Wrapper.getTimer().timerSpeed = 1.0F;
               }

               if (mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.isKeyDown() && this.timer.isDelayComplete((Double)this.delay.getValue())) {
                  event.setHeight(mc.thePlayer.stepHeight = ((Double)this.height.getValue()).floatValue());
               }
            }

            if (event.getEventType() == EventType.POST) {
               double realHeight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
               if (realHeight >= 0.625) {
                  this.timer.reset();
                  Wrapper.getTimer().timerSpeed = 0.4F;
                  this.resetTimer = true;
                  this.doNCPStep(realHeight);
               }
            }
         }

      } else {
         mc.thePlayer.stepHeight = 0.5F;
      }
   }

   @ObfuscationMethod
   private void doNCPStep(double height) {
      double posX = mc.thePlayer.posX;
      double posY = mc.thePlayer.posY;
      double posZ = mc.thePlayer.posZ;
      Wrapper.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING));
      mc.thePlayer.setSprinting(false);
      float[] values;
      if (height <= 1.0) {
         values = new float[]{0.42F, 0.75F};
         if (height != 1.0) {
            values[0] = (float)((double)values[0] * height);
            values[1] = (float)((double)values[1] * height);
            if ((double)values[0] > 0.425) {
               values[0] = 0.425F;
            }

            if ((double)values[1] > 0.78) {
               values[1] = 0.78F;
            }

            if ((double)values[1] < 0.49) {
               values[1] = 0.49F;
            }
         }

         if ((double)values[0] == 0.42) {
            values[0] = 0.42F;
         }

         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + (double)values[0], posZ, false));
         if (posY + (double)values[1] < posY + height) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + (double)values[1], posZ, false));
         }
      } else if (height <= 1.5) {
         values = new float[]{0.42F, 0.7532F, 1.001336F, 1.060836F, 0.982436F};
         float[] var10 = values;
         int var11 = values.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            double val = (double)var10[var12];
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + val, posZ, false));
         }
      }

      Wrapper.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING));
      mc.thePlayer.stepHeight = 0.625F;
   }
}
