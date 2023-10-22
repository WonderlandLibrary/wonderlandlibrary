package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventPreMotion;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ReflectionUtils;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class FastPlace extends Mod {
   public Value delay = new Value("FastPlace", "Delay", 50.0, 1.0, 500.0, 10.0);
   private final TimeHelper timer = new TimeHelper();

   public FastPlace() {
      super("FastPlace", Category.GHOST);
   }

   @EventTarget
   public void onUpdate(EventPreMotion e) {
      if (mc.gameSettings.keyBindUseItem.isKeyDown() && this.timer.isDelayComplete(((Double)this.delay.getValue()).longValue())) {
         ReflectionUtils.setRightClickDelayTimer(0);
         this.timer.reset();
      } else {
         ReflectionUtils.setRightClickDelayTimer(1);
      }

   }
}
