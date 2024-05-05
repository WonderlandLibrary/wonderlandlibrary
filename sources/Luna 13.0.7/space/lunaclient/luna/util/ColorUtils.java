package space.lunaclient.luna.util;

import java.awt.Color;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.render.hud.HUD;

public class ColorUtils
{
  public ColorUtils() {}
  
  public static int getMainColor()
  {
    return new Color((int)HUD.red.getValDouble(), (int)HUD.green.getValDouble(), (int)HUD.blue.getValDouble()).getRGB();
  }
  
  public static int getSecondaryColor()
  {
    return -14474461;
  }
}
