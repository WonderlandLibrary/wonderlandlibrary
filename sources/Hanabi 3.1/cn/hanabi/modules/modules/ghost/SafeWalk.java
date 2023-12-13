package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventSafeWalk;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;

public class SafeWalk extends Mod {
   public SafeWalk() {
      super("SafeWalk", Category.GHOST);
   }

   @EventTarget
   public void onSafe(EventSafeWalk event) {
      event.setSafe(true);
   }
}
