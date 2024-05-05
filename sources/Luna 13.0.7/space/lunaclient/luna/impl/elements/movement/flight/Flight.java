package space.lunaclient.luna.impl.elements.movement.flight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.movement.flight.structure.CubeCraft;
import space.lunaclient.luna.impl.elements.movement.flight.structure.Hypixel;
import space.lunaclient.luna.impl.elements.movement.flight.structure.SkyGiants;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="Flight", category=Category.MOVEMENT, description="Allows you to fly.")
public class Flight
  extends Element
{
  public static boolean lock;
  @ModeSetting(name="Mode", currentOption="Hypixel", options={"Hypixel", "CubeCraft", "SkyGiants"}, locked=false)
  public static Setting mode;
  private Hypixel hypixel = new Hypixel();
  private CubeCraft cc = new CubeCraft();
  private SkyGiants c = new SkyGiants();
  
  public Flight() {}
  
  public void onEnable()
  {
    if (mode.getValString().equalsIgnoreCase("Hypixel"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.hypixel);
    }
    else if (mode.getValString().equalsIgnoreCase("CubeCraft"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.cc);
    }
    else if (mode.getValString().equalsIgnoreCase("SkyGiants"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.c);
      SkyGiants.stage = 0;
      this.c.counter = 1;
      Minecraft.thePlayer.motionX = 0.0D;
      Minecraft.thePlayer.motionZ = 0.0D;
    }
    this.hypixel.counter = 1;
    super.onEnable();
  }
  
  public void onDisable()
  {
    Minecraft.thePlayer.capabilities.isFlying = false;
    net.minecraft.util.Timer.timerSpeed = 1.0F;
    Minecraft.thePlayer.motionX *= 0.0D;
    Minecraft.thePlayer.motionZ *= 0.0D;
    Minecraft.thePlayer.speedInAir = 0.02F;
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.hypixel);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.cc);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.c);
    super.onDisable();
  }
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if (isToggled())
    {
      if (!getMode().contains(mode.getValString()))
      {
        toggle();
        toggle();
      }
    }
    else {
      setMode(mode.getValString());
    }
  }
}
