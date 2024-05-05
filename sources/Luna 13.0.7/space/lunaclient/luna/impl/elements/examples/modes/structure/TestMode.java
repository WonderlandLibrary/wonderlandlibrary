package space.lunaclient.luna.impl.elements.examples.modes.structure;

import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;

public class TestMode
{
  public TestMode() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    PlayerUtils.tellPlayer("Hello, this is mode 1", false);
  }
}
