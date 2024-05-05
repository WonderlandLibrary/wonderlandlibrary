package space.lunaclient.luna.impl.elements.movement.speed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.DoubleSetting;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.movement.speed.structure.Bhop;
import space.lunaclient.luna.impl.elements.movement.speed.structure.Bunny;
import space.lunaclient.luna.impl.elements.movement.speed.structure.Hypixel;
import space.lunaclient.luna.impl.elements.movement.speed.structure.Strafe;
import space.lunaclient.luna.impl.elements.movement.speed.structure.yPort;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="Speed", category=Category.MOVEMENT, description="Allows you to run faster.")
public class Speed
  extends Element
{
  public static int yOffset;
  @ModeSetting(name="Mode", currentOption="Vanilla", options={"yPort", "Bhop", "Hypixel", "Bunny", "Strafe"}, locked=false)
  public static Setting mode;
  @DoubleSetting(name="yPort Speed", currentValue=1.2D, minValue=0.6D, maxValue=5.0D, onlyInt=false, locked=false)
  public static Setting vSpeed;
  private yPort van = new yPort();
  private Bhop lh = new Bhop();
  private Hypixel hypixel = new Hypixel();
  private Bunny bunny = new Bunny();
  private Strafe strafe = new Strafe();
  
  public Speed() {}
  
  public void onEnable()
  {
    if (mode.getValString().equalsIgnoreCase("yPort"))
    {
      setMode(mode.getValString());
      yPort.state.set(0);
      if ((Minecraft.thePlayer.onGround & !Luna.INSTANCE.isLoading & Minecraft.theWorld != null)) {
        yOffset = (int)Minecraft.thePlayer.posY;
      }
      Luna.INSTANCE.EVENT_MANAGER.register(this.van);
    }
    else if (mode.getValString().equalsIgnoreCase("Bhop"))
    {
      setMode(mode.getValString());
      Bhop.stage = 0;
      Minecraft.thePlayer.motionX = 0.0D;
      Minecraft.thePlayer.motionZ = 0.0D;
      Luna.INSTANCE.EVENT_MANAGER.register(this.lh);
    }
    else if (mode.getValString().equalsIgnoreCase("Hypixel"))
    {
      setMode(mode.getValString());
      Hypixel.stage = 0;
      Minecraft.thePlayer.motionX = 0.0D;
      Minecraft.thePlayer.motionZ = 0.0D;
      Luna.INSTANCE.EVENT_MANAGER.register(this.hypixel);
    }
    else if (mode.getValString().equalsIgnoreCase("Bunny"))
    {
      setMode(mode.getValString());
      Minecraft.thePlayer.motionX = 0.0D;
      Minecraft.thePlayer.motionZ = 0.0D;
      Luna.INSTANCE.EVENT_MANAGER.register(this.bunny);
    }
    else if (mode.getValString().equalsIgnoreCase("Strafe"))
    {
      setMode(mode.getValString());
      Hypixel.stage = 0;
      Minecraft.thePlayer.motionX = 0.0D;
      Minecraft.thePlayer.motionZ = 0.0D;
      Luna.INSTANCE.EVENT_MANAGER.register(this.strafe);
    }
    net.minecraft.util.Timer.timerSpeed = 1.0F;
    super.onEnable();
  }
  
  public void onDisable()
  {
    if (mode.getValString().equalsIgnoreCase("Vanilla")) {
      Minecraft.thePlayer.motionY = -0.4D;
    }
    net.minecraft.util.Timer.timerSpeed = 1.0F;
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.lh);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.van);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.hypixel);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.bunny);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.strafe);
    Minecraft.thePlayer.distanceWalkedModified = 0.0F;
    Minecraft.thePlayer.motionX *= 0.0D;
    Minecraft.thePlayer.motionZ *= 0.0D;
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
  
  public double round(double value, int places)
  {
    if (places < 0) {
      throw new IllegalArgumentException();
    }
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
