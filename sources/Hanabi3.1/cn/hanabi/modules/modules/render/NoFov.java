package cn.hanabi.modules.modules.render;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.value.Value;

public class NoFov extends Mod {
   public static Value fovspoof = new Value("NoFov", "Fov", 1.0, 0.1, 1.5, 0.01);

   public NoFov() {
      super("NoFov", Category.RENDER);
   }
}
