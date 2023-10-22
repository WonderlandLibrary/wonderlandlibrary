package cn.hanabi.modules.modules.movement.Fly;

import cn.hanabi.events.BBSetEvent;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventPullback;
import cn.hanabi.events.EventStep;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fly extends Mod {
   public static TimeHelper lagbacktimer = new TimeHelper();
   public static TimeHelper disablertimer = new TimeHelper();
   public static Value timer = new Value("Fly", "Motion Speed", 1.0, 1.0, 10.0, 1.0);
   Value mode = new Value("Fly", "Mode", 0);
   Value lagback = new Value("Fly", "Lag Back Checks", true);
   Fly_Motion MotionFly = new Fly_Motion();
   Fly_Hypixel hypixelfly = new Fly_Hypixel();
   Fly_AACv5 aacv5Fly = new Fly_AACv5();
   Fly_NiggaBaipai nigga = new Fly_NiggaBaipai();

   public Fly() {
      super("Fly", Category.MOVEMENT);
      this.mode.addValue("Motion");
      this.mode.addValue("Hypixel");
      this.mode.addValue("AACv5");
   }

   public static double getRandomInRange(double minDouble, double maxDouble) {
      return minDouble >= maxDouble ? minDouble : (new Random()).nextDouble() * (maxDouble - minDouble) + minDouble;
   }

   public static void damagePlayer(int damage) {
      Minecraft mc = Minecraft.getMinecraft();
      double i1 = getRandomInRange(0.059, 0.0615);
      double i2 = getRandomInRange(0.049, 0.0625);
      double i3 = getRandomInRange(5.0E-7, 7.0E-7);
      PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
      if (potioneffect != null) {
         int var10000 = potioneffect.getAmplifier() + 1;
      } else {
         boolean var10 = false;
      }

   }

   @EventTarget
   public void onPacket(EventPacket e) {
      Packet packet = e.getPacket();
      if (this.mode.isCurrentMode("AACv5")) {
         this.aacv5Fly.onPacket(e);
      }

      if (this.mode.isCurrentMode("Hypixel")) {
         this.nigga.onPacket(e);
      }

   }

   @EventTarget
   public void onPre(EventPreMotion event) {
      if (!this.mode.isCurrentMode("Disabler")) {
         if (this.mode.isCurrentMode("Motion")) {
            this.setDisplayName("FMotion");
            this.MotionFly.onPre();
         }

      }
   }

   @EventTarget
   public void onPost(EventPostMotion event) {
   }

   @EventTarget
   public void onBB(BBSetEvent event) {
      if (this.mode.isCurrentMode("Hypixel")) {
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (this.mode.isCurrentMode("AACv5")) {
         this.aacv5Fly.onUpdate();
      }

   }

   @EventTarget
   public void onPullback(EventPullback e) {
      if ((Boolean)this.lagback.getValueState() && !this.mode.isCurrentMode("Disabler")) {
         lagbacktimer.reset();
         ClientUtil.sendClientMessage("(LagBackCheck) Fly Disabled", Notification.Type.WARNING);
         this.set(false);
      }

   }

   @EventTarget
   public void onMove(EventMove event) {
      if (this.mode.isCurrentMode("Hypixel")) {
         this.nigga.onMove(event);
      }
   }

   public void onEnable() {
      if (this.mode.isCurrentMode("AACv5")) {
         this.aacv5Fly.onEnable();
      }

      super.onEnable();
   }

   public void onDisable() {
      if (this.mode.isCurrentMode("Hypixel")) {
         this.nigga.onDisable();
      } else {
         if (this.mode.isCurrentMode("AACv5")) {
            this.aacv5Fly.onDisable();
         }

         super.onDisable();
      }
   }

   @EventTarget
   public void onStep(EventStep e) {
      if (this.mode.isCurrentMode("Hypixel")) {
      }

   }
}
