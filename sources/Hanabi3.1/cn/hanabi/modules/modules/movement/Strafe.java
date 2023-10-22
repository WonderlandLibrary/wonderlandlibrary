package cn.hanabi.modules.modules.movement;

import cn.hanabi.events.EventMove;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.MoveUtils;
import com.darkmagician6.eventapi.EventTarget;

public class Strafe extends Mod {
   public boolean air = true;

   public Strafe() {
      super("Strafe", Category.MOVEMENT);
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (!mc.thePlayer.onGround && ((IKeyBinding)mc.gameSettings.keyBindJump).getPress()) {
         MoveUtils.strafe();
      }

   }
}
