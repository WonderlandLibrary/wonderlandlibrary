package cn.hanabi.modules.modules.movement.Speed;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.injection.interfaces.IEntityPlayer;
import cn.hanabi.utils.MoveUtils;
import cn.hanabi.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

@ObfuscationClass
public class Speed_AAC {
   static Value mode = new Value("Speed", "AACMode", 0);
   public Value hytSpeed = new Value("Speed", "Hyt Speed", 1.0, 0.3, 8.0, 0.1);
   public Value hytMotionY = new Value("Speed", "Hyt MotionY", 0.42, 0.01, 2.0, 0.01);
   Minecraft mc = Minecraft.getMinecraft();
   private double[] lastPos;
   private int aac4Delay = 0;
   private boolean redeskyStage = false;
   private long redeskyTimer = 0L;

   public Speed_AAC() {
      mode.addValue("Hyt");
      mode.addValue("AACv4");
      mode.addValue("RedeSkyTimer");
   }

   public void onPre(EventPreMotion e) {
      if (mode.isCurrentMode("Hyt")) {
         if (this.mc.thePlayer.onGround && MoveUtils.isMoving()) {
            MoveUtils.strafe((Double)this.hytSpeed.getValue());
            this.mc.thePlayer.motionY = (Double)this.hytMotionY.getValue();
            this.lastPos = null;
         } else if (this.lastPos == null) {
            this.lastPos = new double[]{this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ};
            e.setCancel(true);
         } else {
            if (this.lastPos.length == 3) {
               this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.lastPos[0], this.lastPos[1], this.lastPos[2], false));
            }

            this.lastPos = new double[0];
         }
      } else if (mode.isCurrentMode("AACv4")) {
         if (MoveUtils.isMoving()) {
            if (this.mc.thePlayer.onGround) {
               this.mc.thePlayer.jump();
               this.aac4Delay = 0;
            } else {
               if (this.aac4Delay >= 3 && this.aac4Delay <= 4) {
                  this.mc.thePlayer.jumpMovementFactor = 0.1F;
               }

               ++this.aac4Delay;
            }
         }
      } else if (mode.isCurrentMode("RedeSkyTimer")) {
         long nowTime = System.currentTimeMillis();
         if (MoveUtils.isMoving()) {
            if (this.redeskyStage) {
               Wrapper.getTimer().timerSpeed = 1.5F;
               if (nowTime - this.redeskyTimer > 700L) {
                  this.redeskyTimer = nowTime;
                  this.redeskyStage = !this.redeskyStage;
               }
            } else {
               Wrapper.getTimer().timerSpeed = 0.8F;
               if (nowTime - this.redeskyTimer > 400L) {
                  this.redeskyTimer = nowTime;
                  this.redeskyStage = !this.redeskyStage;
               }
            }
         }
      }

   }

   public void onEnable() {
      this.aac4Delay = 0;
   }

   public void onDisable() {
      if (this.mc.thePlayer != null) {
         ((IEntityPlayer)this.mc.thePlayer).setSpeedInAir(0.02F);
         Wrapper.getTimer().timerSpeed = 1.0F;
      }
   }
}
