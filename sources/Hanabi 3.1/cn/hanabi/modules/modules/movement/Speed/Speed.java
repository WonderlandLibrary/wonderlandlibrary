package cn.hanabi.modules.modules.movement.Speed;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.Wrapper;
import cn.hanabi.events.EventJump;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPacket;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventPullback;
import cn.hanabi.events.EventStep;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import io.netty.util.internal.ThreadLocalRandom;
import org.lwjgl.input.Keyboard;

@ObfuscationClass
public class Speed extends Mod {
   public static Value mode = (new Value("Speed", "Mode", 1)).LoadValue(new String[]{"GudHop", "Hypixel", "AAC", "Mineplex", "Verus"});
   public static Value fall = new Value("Speed", "Falling Timer", 1.0, 0.9, 4.0, 0.01);
   private final Speed_Hypixel modeGlobalHypixel = new Speed_Hypixel();
   private final Speed_GudHop modeGudHop = new Speed_GudHop();
   private final Speed_AAC modeAAC = new Speed_AAC();
   private final Speed_Mineplex modeMineplex = new Speed_Mineplex();
   private final Speed_Verus modeVerus = new Speed_Verus();
   public Value lagback = new Value("Speed", "Lag Back Checks", true);
   public Value autodisable = new Value("Speed", "Auto Disable", true);
   public Value toggle = new Value("Speed", "Key Toggle Timer", true);

   public Speed() {
      super("Speed", Category.MOVEMENT);
   }

   @EventTarget
   public void onReload(EventWorldChange e) {
      if ((Boolean)this.autodisable.getValueState()) {
         this.set(false);
      }

   }

   @EventTarget
   public void onPacket(EventPacket e) {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onPacket(e);
      }

   }

   @EventTarget
   public void onStep(EventStep e) {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onStep(e);
      }

   }

   @EventTarget
   public void onUpdate(EventPreMotion e) {
      this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
      Wrapper.getTimer().timerSpeed = mc.thePlayer.fallDistance < 2.0F ? (float)((Boolean)this.toggle.getValue() ? (double)((float)(Keyboard.isKeyDown(56) ? 1.0 + ThreadLocalRandom.current().nextDouble((Double)fall.getValue() - 1.0, (Double)fall.getValue() - 0.8899999856948853) : 1.0)) : 1.0 + ThreadLocalRandom.current().nextDouble((Double)fall.getValue() - 1.0, (Double)fall.getValue() - 0.8899999856948853)) : 1.0F;
      if (mode.isCurrentMode("GudHop")) {
         this.modeGudHop.onPre(e);
      } else if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onPre(e);
      } else if (mode.isCurrentMode("AAC")) {
         this.modeAAC.onPre(e);
      } else if (mode.isCurrentMode("Mineplex")) {
         this.modeMineplex.onUpdate();
      }

   }

   @EventTarget
   public void onPost(EventPostMotion e) {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onPost(e);
      }

   }

   @EventTarget
   public void onJump(EventJump e) {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onJump(e);
      }

   }

   @EventTarget
   public void onPullback(EventPullback e) {
      if ((Boolean)this.lagback.getValueState()) {
         ClientUtil.sendClientMessage("(LagBackCheck) Speed Disabled", Notification.Type.WARNING);
         this.set(false);
      }

      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onPullback(e);
      }

   }

   @EventTarget
   public void onLoop(EventLoop e) {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onLoop(e);
      }

   }

   @EventTarget
   public void onMove(EventMove em) {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onMove(em);
      } else if (mode.isCurrentMode("Verus")) {
         this.modeVerus.onMove(em);
      }

   }

   public void onDisable() {
      Wrapper.getTimer().timerSpeed = 1.0F;
      if (!mode.isCurrentMode("Test")) {
         if (mode.isCurrentMode("Hypixel")) {
            this.modeGlobalHypixel.onDisable();
         } else if (mode.isCurrentMode("AAC")) {
            this.modeAAC.onDisable();
         }
      }

   }

   public void onEnable() {
      if (mode.isCurrentMode("Hypixel")) {
         this.modeGlobalHypixel.onEnable();
      } else if (mode.isCurrentMode("AAC")) {
         this.modeAAC.onEnable();
      }

   }
}
