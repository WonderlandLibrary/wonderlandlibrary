package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.modules.combat.RotationCombat;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Sprint extends Module {
   public static TickSetting a;
   private final TickSetting combatRotation; // Step 1: Declare the CombatRotation TickSetting
   private final RotationCombat combatRotate; // Step 2: Declare the CombatRotate instance

   public Sprint() {
      super("Sprint", ModuleCategory.movement);
      a = new TickSetting("OmniSprint", false);
      combatRotation = new TickSetting("CombatRotation", true); // Step 3: Initialize the CombatRotation TickSetting
      combatRotate = new RotationCombat(); // Step 4: Initialize the CombatRotate instance
      this.registerSetting(a);
      this.registerSetting(combatRotation); // Step 5: Register the CombatRotation TickSetting
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (Utils.Player.isPlayerInGame() && mc.inGameHasFocus) {
         EntityPlayerSP p = mc.thePlayer;
         if (a.isToggled()) {
            if (Utils.Player.isMoving() && p.getFoodStats().getFoodLevel() > 6) {
               p.setSprinting(true);

               // Step 6: Check the CombatRotation setting before calling the CombatRotate event handler
               if (combatRotation.isToggled()) {
                  combatRotate.onPlayerTick(e);
               }
            }
         } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
         }
      }
   }
}
