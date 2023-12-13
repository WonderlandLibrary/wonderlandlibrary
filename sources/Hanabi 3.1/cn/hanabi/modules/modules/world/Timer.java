package cn.hanabi.modules.modules.world;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class Timer extends Mod {
   public Value speed = new Value("Timer", "TimerSpeed", 1.0, 0.1, 2.0, 0.01);

   public Timer() {
      super("Timer", Category.WORLD);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      this.setDisplayName(" " + this.speed.getValue());
      Wrapper.getTimer().timerSpeed = 1.0F + ((Double)this.speed.getValue()).floatValue() - 1.0F;
   }

   public void onDisable() {
      Wrapper.getTimer().timerSpeed = 1.0F;
   }
}
