package space.lunaclient.luna.api.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.util.StringUtils;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.render.hud.HUD;
import space.lunaclient.luna.impl.gui.notifications.ClientNotification.Type;
import space.lunaclient.luna.impl.gui.notifications.NotificationUtil;
import space.lunaclient.luna.impl.managers.EventManager;

public abstract class Element
{
  public static Luna INSTANCE = Luna.INSTANCE;
  public static Minecraft mc = Minecraft.getMinecraft();
  public static FontRenderer fr = Minecraft.fontRendererObj;
  public final space.lunaclient.luna.util.Timer timer = new space.lunaclient.luna.util.Timer();
  private int transition;
  private Category category = ((ElementInfo)getClass().getAnnotation(ElementInfo.class)).category();
  private String name = ((ElementInfo)getClass().getAnnotation(ElementInfo.class)).name();
  private int keyCode = ((ElementInfo)getClass().getAnnotation(ElementInfo.class)).keyCode();
  private String description = ((ElementInfo)getClass().getAnnotation(ElementInfo.class)).description();
  private boolean toggled = ((ElementInfo)getClass().getAnnotation(ElementInfo.class)).toggled();
  private String mode = "";
  
  public Element() {}
  
  public int getTransition()
  {
    return this.transition;
  }
  
  public void setTransition(int transition)
  {
    this.transition = transition;
  }
  
  public void onEnable()
  {
    if (HUD.noti.getValBoolean()) {
      NotificationUtil.sendClientMessage("Enabled " + getName(), 4000, ClientNotification.Type.SUCCESS);
    }
    this.timer.reset();
    INSTANCE.EVENT_MANAGER.register(this);
    mc.timer.resetTimer();
    this.transition = (Minecraft.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(getName())) - 5);
  }
  
  public void onDisable()
  {
    if (getName().contains("LongJump")) {
      mc.gameSettings.keyBindJump.pressed = false;
    }
    if (HUD.noti.getValBoolean()) {
      NotificationUtil.sendClientMessage("Disabled " + getName(), 4000, ClientNotification.Type.ERROR);
    }
    this.timer.reset();
    INSTANCE.EVENT_MANAGER.unregister(this);
    mc.timer.resetTimer();
    this.transition = (Minecraft.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(getName())) + 10);
  }
  
  public void sendPacket(Packet<INetHandler> packet)
  {
    Minecraft.thePlayer.sendQueue.addToSendQueue(packet);
  }
  
  public void toggle()
  {
    if (this.toggled) {
      onDisable();
    } else {
      onEnable();
    }
    this.toggled = (!this.toggled);
  }
  
  public String getDisplayName()
  {
    if (this.mode.equalsIgnoreCase("")) {
      return this.name;
    }
    return this.name + " " + this.mode;
  }
  
  public void setMode(String mode)
  {
    this.mode = ("§7" + mode);
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public Category getCategory()
  {
    return this.category;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public int getKeyCode()
  {
    return this.keyCode;
  }
  
  public void setKeyCode(int keBind)
  {
    this.keyCode = keBind;
  }
  
  public boolean isToggled()
  {
    return this.toggled;
  }
  
  public String getMode()
  {
    return this.mode;
  }
}
