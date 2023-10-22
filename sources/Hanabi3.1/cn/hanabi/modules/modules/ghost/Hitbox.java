package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class Hitbox extends Mod {
   public static Value minsize = new Value("Hitbox", "Min-Size", 0.1, 0.1, 0.8, 0.01);
   public static Value maxsize = new Value("Hitbox", "Max-Size", 0.25, 0.1, 1.0, 0.01);

   public Hitbox() {
      super("Hitbox", Category.GHOST);
   }

   public static float getSize() {
      double min = Math.min((Double)minsize.getValue(), (Double)maxsize.getValue());
      double max = Math.max((Double)minsize.getValue(), (Double)maxsize.getValue());
      return (float)(ModManager.getModule("Hitbox").isEnabled() ? Math.random() * (max - min) + min : 0.10000000149011612);
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName("Size: " + maxsize.getValueState());
   }
}
