package space.lunaclient.luna.impl.elements.render;

import net.minecraft.client.Minecraft;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="ClickGUI", category=Category.RENDER, description="Shows a custom ClickGUI made by Jacobtread.", keyCode=54)
public class ClickGui
  extends Element
{
  public ClickGui() {}
  
  public void onEnable()
  {
    mc.displayGuiScreen(new space.lunaclient.luna.impl.gui.clickgui.ClickGui());
    super.onEnable();
  }
  
  public void onDisable()
  {
    super.onDisable();
  }
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (isToggled()) {
      toggle();
    }
  }
}
