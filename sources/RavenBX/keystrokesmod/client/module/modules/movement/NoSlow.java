package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.DescriptionSetting;
import keystrokesmod.client.module.setting.impl.SliderSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;

public class NoSlow extends Module {
   public static DescriptionSetting a;
   public static DescriptionSetting c;
   public static SliderSetting b;

   public NoSlow() {
      super("NoSlow", ModuleCategory.movement);
      this.registerSetting(a = new DescriptionSetting("Default is 80% motion reduction."));
      this.registerSetting(c = new DescriptionSetting("Hypixel max: 22%"));
      this.registerSetting(b = new SliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
   }

   public static void sl() {
      float val = (100.0F - (float) b.getInput()) / 100.0F;
      EntityPlayerSP player = mc.thePlayer;
      if (player != null) {
         player.movementInput.moveStrafe *= val;
         player.movementInput.moveForward *= val;

         // Check if the player has a slowness effect and remove it
         if (player.isPotionActive(2)) { // ID 2 for "moveSlowdown" (slowness) potion effect
            player.removePotionEffect(2);
         }
      }
   }
}
