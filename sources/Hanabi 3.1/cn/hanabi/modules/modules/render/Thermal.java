package cn.hanabi.modules.modules.render;

import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import me.yarukon.palette.ColorValue;

public class Thermal extends Mod {
   public static ColorValue renderColor = new ColorValue("Thermal Color", 0.5F, 1.0F, 1.0F, 1.0F, true, false, 10.0F);

   public Thermal() {
      super("Thermal", Category.RENDER);
   }
}
