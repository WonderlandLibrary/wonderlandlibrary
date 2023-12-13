package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventMouse;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ReflectionUtils;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;

public class DoubleClicker extends Mod {
   public Value delay = new Value("DoubleClicker", "Delay", 50.0, 0.0, 500.0, 5.0);
   public Value random = new Value("DoubleClicker", "Random", 50.0, 0.0, 250.0, 1.0);
   ArrayList clickTimes = new ArrayList();
   private boolean skip = false;

   public DoubleClicker() {
      super("DoubleClicker", Category.GHOST);
   }

   @EventTarget
   public void onClick(EventUpdate e) {
      for(int i = 0; i < this.clickTimes.size(); ++i) {
         long time = (Long)this.clickTimes.get(i);
         this.skip = true;
         ReflectionUtils.setLeftClickCounter(0);
         ReflectionUtils.clickMouse();
         this.clickTimes.remove(i);
      }

   }

   @EventTarget
   public void Click(EventMouse event) {
      if (this.skip) {
         this.skip = false;
      } else if (mc.theWorld != null) {
         if (mc.thePlayer != null) {
            if (mc.thePlayer.isEntityAlive()) {
               if (!mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking()) {
                  long MS = this.getCurrentMS();
                  double Delay = (Double)this.delay.getValueState();
                  double RDelay = (double)this.randomInt(((Double)this.random.getValueState()).intValue());
                  this.clickTimes.add((long)((double)MS + Delay + RDelay));
               }
            }
         }
      }
   }

   public void onDisable() {
      this.clickTimes.clear();
   }
}
