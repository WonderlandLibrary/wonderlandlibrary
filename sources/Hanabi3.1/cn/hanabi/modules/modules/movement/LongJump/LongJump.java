package cn.hanabi.modules.modules.movement.LongJump;

import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventPostMotion;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventPullback;
import cn.hanabi.gui.notifications.Notification;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;

public class LongJump extends Mod {
   private final Value lagback = new Value("LongJump", "Lag Back Checks", false);
   private static final Value visual = new Value("LongJump", "Visual Fly", false);
   Value mode = (new Value("LongJump", "Mode", 0)).LoadValue(new String[]{"Bow", "Damage", "NonDMG"});
   LongJump_Bow bow = new LongJump_Bow();
   LongJump_DMG dmg = new LongJump_DMG();
   LongJump_NonDMG nonDMG = new LongJump_NonDMG();

   public LongJump() {
      super("LongJump", Category.MOVEMENT);
   }

   @EventTarget
   private void onLagBack(EventPullback e) {
      if ((Boolean)this.lagback.getValueState()) {
         ClientUtil.sendClientMessage("(LagBackCheck) LongJump Disabled", Notification.Type.WARNING);
         this.set(false);
      }

   }

   @EventTarget
   private void onMove(EventMove e) {
      if (this.mode.isCurrentMode("Bow")) {
         this.bow.onMove(e);
      }

      if (this.mode.isCurrentMode("Damage")) {
         this.dmg.onMove(e);
      }

      if (this.mode.isCurrentMode("NonDMG")) {
         this.nonDMG.onMove(e);
      }

   }

   @EventTarget
   private void onPre(EventPreMotion e) {
      if ((Boolean)visual.getValue()) {
         EntityPlayerSP var10000 = mc.thePlayer;
         var10000.posY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
         var10000 = mc.thePlayer;
         var10000.lastTickPosY -= mc.thePlayer.posY - mc.thePlayer.lastTickPosY;
      }

      if (this.mode.isCurrentMode("Bow")) {
         this.bow.onPre(e);
      }

      if (this.mode.isCurrentMode("Damage")) {
         this.dmg.onPre(e);
      }

   }

   @EventTarget
   private void onPost(EventPostMotion e) {
      if (this.mode.isCurrentMode("Bow")) {
         this.bow.onPost(e);
      }

   }

   public void onEnable() {
      if (this.mode.isCurrentMode("Bow")) {
         this.bow.onEnable();
      }

      if (this.mode.isCurrentMode("Damage")) {
         this.dmg.onEnable();
      }

      if (this.mode.isCurrentMode("NonDMG")) {
         this.nonDMG.onEnable();
      }

   }

   public void onDisable() {
      if (this.mode.isCurrentMode("Bow")) {
         this.bow.onDisable();
      }

      if (this.mode.isCurrentMode("Damage")) {
         this.dmg.onDisable();
      }

      if (this.mode.isCurrentMode("NonDMG")) {
         this.nonDMG.onDisable();
      }

   }
}
