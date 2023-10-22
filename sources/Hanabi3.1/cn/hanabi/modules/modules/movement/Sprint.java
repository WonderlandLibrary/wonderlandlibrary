package cn.hanabi.modules.modules.movement;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class Sprint extends Mod {
   public static boolean isSprinting;
   public Value ommi = new Value("Sprint", "Omni", false);

   public Sprint() {
      super("Sprint", Category.MOVEMENT);
      this.setState(true);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (!ModManager.getModule("Scaffold").getState()) {
         label43: {
            boolean canSprint = (float)mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying;
            if ((Boolean)this.ommi.getValue()) {
               if (PlayerUtil.MovementInput()) {
                  break label43;
               }
            } else if (((IKeyBinding)mc.gameSettings.keyBindForward).getPress() && canSprint) {
               break label43;
            }

            isSprinting = false;
            return;
         }

         isSprinting = true;
         mc.thePlayer.setSprinting(true);
      }
   }

   public void onDisable() {
      isSprinting = false;
      mc.thePlayer.setSprinting(false);
      super.onDisable();
   }
}
