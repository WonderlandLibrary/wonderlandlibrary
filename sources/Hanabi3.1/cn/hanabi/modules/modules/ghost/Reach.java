package cn.hanabi.modules.modules.ghost;

import cn.hanabi.events.EventUpdate;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;

public class Reach extends Mod {
   public static Value minreach = new Value("Reach", "Min-Range", 3.5, 3.0, 6.0, 0.01);
   public static Value maxreach = new Value("Reach", "Max-Range", 4.2, 3.0, 6.0, 0.01);
   public static Value throughWall = new Value("Reach", "Through Wall", true);

   public Reach() {
      super("Reach", Category.GHOST);
   }

   public static double getReach() {
      double min = Math.min((Double)minreach.getValue(), (Double)maxreach.getValue());
      double max = Math.max((Double)minreach.getValue(), (Double)maxreach.getValue());
      return Math.random() * (max - min) + min;
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      this.setDisplayName("Range: " + maxreach.getValue());
   }
}
