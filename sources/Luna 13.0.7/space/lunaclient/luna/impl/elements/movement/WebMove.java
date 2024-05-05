package space.lunaclient.luna.impl.elements.movement;

import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.setting.DoubleSetting;
import space.lunaclient.luna.api.setting.Setting;

@ElementInfo(name="WebMove", category=Category.MOVEMENT)
public class WebMove
  extends Element
{
  @DoubleSetting(name="Speed", currentValue=0.25D, minValue=0.1D, maxValue=2.25D, onlyInt=false, locked=false)
  public static Setting speed;
  
  public WebMove() {}
}
