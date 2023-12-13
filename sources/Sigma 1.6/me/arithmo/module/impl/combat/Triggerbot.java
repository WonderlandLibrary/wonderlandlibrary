package me.arithmo.module.impl.combat;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventMouse;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class Triggerbot
  extends Module
{
  public static final String DELAY = "DELAY";
  public static final String RANDOM = "RANDOM";
  public static final String MIN = "MINRAND";
  public static final String MAX = "MAXRAND";
  public static final String MOUSE = "ON-MOUSE";
  public EntityLivingBase targ;
  
  public Triggerbot(ModuleData data)
  {
    super(data);
    this.settings.put("DELAY", new Setting("DELAY", Integer.valueOf(100), "Base click delay.", 25.0D, 50.0D, 500.0D));
    this.settings.put("RANDOM", new Setting("RANDOM", Boolean.valueOf(true), "Randomize click delay."));
    this.settings.put("MINRAND", new Setting("MINRAND", Integer.valueOf(50), "Minimum click randomization.", 25.0D, 25.0D, 200.0D));
    this.settings.put("MAXRAND", new Setting("MAXRAND", Integer.valueOf(100), "Maximum click randomization.", 25.0D, 25.0D, 200.0D));
    this.settings.put("ON-MOUSE", new Setting("ON-MOUSE", Boolean.valueOf(true), "Click when mouse is held down."));
  }
  
  Timer timer = new Timer();
  
  public static int randomNumber(int max, int min)
  {
    int ii = -min + (int)(Math.random() * (max - -min + 1));
    return ii;
  }
  
  @RegisterEvent(events={EventMotion.class, EventMouse.class})
  public void onEvent(Event event)
  {
    if ((event instanceof EventMotion))
    {
      EventMotion em = (EventMotion)event;
      if ((em.isPre()) && (mc.currentScreen == null) && (mc.thePlayer.isEntityAlive()))
      {
        if ((mc.thePlayer.getHeldItem() == null) || (mc.thePlayer.getHeldItem().getItem() == null)) {
          return;
        }
        if ((((Boolean)((Setting)this.settings.get("ON-MOUSE")).getValue()).booleanValue()) && (!Mouse.isButtonDown(0))) {
          return;
        }
        Item heldItem = mc.thePlayer.getHeldItem().getItem();
        if ((mc.objectMouseOver.entityHit != null) && ((heldItem instanceof ItemSword)))
        {
          int delay = ((Number)((Setting)this.settings.get("DELAY")).getValue()).intValue();
          int minran = ((Number)((Setting)this.settings.get("MINRAND")).getValue()).intValue();
          int maxran = ((Number)((Setting)this.settings.get("MAXRAND")).getValue()).intValue();
          boolean random = ((Boolean)((Setting)this.settings.get("RANDOM")).getValue()).booleanValue();
          if (((mc.objectMouseOver.entityHit instanceof EntityPlayer)) && (!mc.objectMouseOver.entityHit.isInvisible()) && (!FriendManager.isFriend(mc.objectMouseOver.entityHit.getName()))) {
            if ((this.timer.delay(delay + (random ? randomNumber(minran, maxran) : 0))) && (!mc.objectMouseOver.entityHit.isDead))
            {
              mc.playerController.onStoppedUsingItem(mc.thePlayer);
              mc.thePlayer.swingItem();
              mc.clickMouse();
              this.timer.reset();
            }
          }
        }
      }
    }
    if ((event instanceof EventMouse))
    {
      EventMouse em = (EventMouse)event;
      if (em.getButtonID() != 1) {}
    }
  }
}
