package cn.hanabi.modules.modules.ghost;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;

public class Wtap extends Mod {
   public Value range = new Value("WTap", "Range", 3.0, 0.0, 6.0, 0.1);
   public Value delay = new Value("WTap", "Delay", 500.0, 100.0, 2000.0, 50.0);
   public Value hold = new Value("WTap", "Held", 100.0, 50.0, 250.0, 50.0);

   public Wtap() {
      super("WTap", Category.GHOST);
   }
}
