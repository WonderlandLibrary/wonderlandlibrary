package space.lunaclient.luna.impl.elements.examples.modes;

import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.examples.modes.structure.TestMode;
import space.lunaclient.luna.impl.elements.examples.modes.structure.TestMode2;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="Basic Modes", category=Category.PLAYER, description="A semi-basic element which holds all the modes.")
public class BasicModes
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Test", options={"Test", "Test 2"}, locked=false)
  public static Setting mode;
  private TestMode test1 = new TestMode();
  private TestMode2 test2 = new TestMode2();
  
  public BasicModes() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if ((isToggled()) && 
      (!getMode().contains(mode.getValString())))
    {
      toggle();
      toggle();
    }
  }
  
  public void onEnable()
  {
    if (mode.getValString().equalsIgnoreCase("Test"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.test1);
    }
    else if (mode.getValString().equalsIgnoreCase("Test 2"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.test2);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.test1);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.test2);
    super.onDisable();
  }
}
