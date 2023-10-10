package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
   public static SliderSetting a;
   public static SliderSetting b;
   public static SliderSetting c;
   public static TickSetting d;
   public static TickSetting e;

   public Velocity() {
      super("Velocity", ModuleCategory.combat);
      this.registerSetting(a = new SliderSetting("Horizontal", 90.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(b = new SliderSetting("Vertical", 100.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(c = new SliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(d = new TickSetting("Only while targeting", false));
      // Inside your Velocity class constructor
      this.registerSetting(e = new TickSetting("Disable while holding S", false));
   }

   public void onLivingUpdate(LivingUpdateEvent ev) {
      if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
         if (d.isToggled() && (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null)) {
            return;
         }

         if (e.isToggled() && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
            return;
         }

         double chance = c.getInput() / 100.0D;

         if (c.getInput() != 100.0D) {
            double ch = Math.random();
            if (ch >= c.getInput() / 100.0D) {
               return;
            }
         }

         // Check if player is on the ground
         if (mc.thePlayer.onGround) {
            // Reset motion to avoid unintended behavior
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionY = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
         } else {
            // Set the player Y based on player X input
            if (mc.thePlayer.motionX != 0.0D) {
               mc.thePlayer.motionY = 0.0D;
               mc.thePlayer.motionY -= (b.getInput() / 100.0D);
            }
         }

         // Adjust the player's motion in the X and Z directions
         if (a.getInput() != 100.0D) {
            double horizontalMultiplier = a.getInput() / 100.0D;
            mc.thePlayer.motionX *= horizontalMultiplier;
            mc.thePlayer.motionZ *= horizontalMultiplier;
         }
      }
   }
}
