package cn.hanabi.modules.modules.player;

import cn.hanabi.Hanabi;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.DebugUtil;
import com.darkmagician6.eventapi.EventTarget;

public class Debug extends Mod {
   public Debug() {
      super("Debug", Category.RENDER);
   }

   @EventTarget
   public void onRender(EventRender2D eventRender2D) {
      int index = 0;

      for(int i = 0; i < Hanabi.INSTANCE.debugUtils.size() - 1; ++i) {
         DebugUtil debug = (DebugUtil)Hanabi.INSTANCE.debugUtils.get(i);
         debug.index = index;
         debug.translate.translate(0.0F, (float)index);
         debug.y = -5.0F + 10.0F * debug.translate.getY() + 11.0F;
         debug.onRender();
         ++index;
      }

   }
}
