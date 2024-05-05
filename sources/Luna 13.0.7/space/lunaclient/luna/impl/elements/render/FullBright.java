package space.lunaclient.luna.impl.elements.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="FullBright", category=Category.RENDER, description="Makes your brightness very bright.")
public class FullBright
  extends Element
{
  private float gamaSetting;
  
  public FullBright() {}
  
  public void onEnable()
  {
    super.onEnable();
    this.gamaSetting = mc.gameSettings.gammaSetting;
  }
  
  public void onDisable()
  {
    super.onDisable();
    mc.gameSettings.gammaSetting = this.gamaSetting;
  }
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    mc.gameSettings.gammaSetting += 1.0F;
  }
}
