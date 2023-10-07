package me.arithmo.module.impl.movement;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventMove;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class Fly
  extends Module
{
  public static String SPEED = "SPEED";
  public static final String MODE = "FLYMODE";
  Timer kickTimer = new Timer();
  private double flyHeight;
  private double startY;
  
  public Fly(ModuleData data)
  {
    super(data);
    this.settings.put(SPEED, new Setting(SPEED, Float.valueOf(2.0F), "Movement speed.", 0.25D, 0.25D, 5.0D));
    this.settings.put("FLYMODE", new Setting("FLYMODE", new Options("Fly Mode", "Vanilla", new String[] { "Vanilla", "AntiKick", "Glide" }), "Fly method."));
  }
  
  public static double getBaseMoveSpeed()
  {
    double baseSpeed = 0.2873D;
    if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
    {
      int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
      baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
    }
    return baseSpeed;
  }
  
  public void updateFlyHeight()
  {
    double h = 1.0D;
    AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
    for (this.flyHeight = 0.0D; this.flyHeight < mc.thePlayer.posY; this.flyHeight += h)
    {
      AxisAlignedBB nextBox = box.offset(0.0D, -this.flyHeight, 0.0D);
      if (mc.theWorld.checkBlockCollision(nextBox))
      {
        if (h < 0.0625D) {
          break;
        }
        this.flyHeight -= h;
        h /= 2.0D;
      }
    }
  }
  
  public void goToGround()
  {
    if (this.flyHeight > 300.0D) {
      return;
    }
    double minY = mc.thePlayer.posY - this.flyHeight;
    if (minY <= 0.0D) {
      return;
    }
    for (double y = mc.thePlayer.posY; y > minY;)
    {
      y -= 8.0D;
      if (y < minY) {
        y = minY;
      }
      C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
      
      mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
    for (double y = minY; y < mc.thePlayer.posY;)
    {
      y += 8.0D;
      if (y > mc.thePlayer.posY) {
        y = mc.thePlayer.posY;
      }
      C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
      
      mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
  }
  
  public void onEnable()
  {
    mc.timer.timerSpeed = 1.0F;
    this.startY = mc.thePlayer.posY;
  }
  
  @RegisterEvent(events={EventMove.class, EventMotion.class})
  public void onEvent(Event event)
  {
    if ((event instanceof EventMotion))
    {
      EventMotion em = (EventMotion)event;
      double speed = Math.max(((Number)((Setting)this.settings.get(SPEED)).getValue()).floatValue() / 10.0F, getBaseMoveSpeed());
      if (em.isPre())
      {
        setSuffix(((Options)((Setting)this.settings.get("FLYMODE")).getValue()).getSelected());
        switch (((Options)((Setting)this.settings.get("FLYMODE")).getValue()).getSelected())
        {
        case "Glide": 
          boolean shouldBlock = (mc.thePlayer.posY + 0.1D >= this.startY) && (mc.gameSettings.keyBindJump.getIsKeyPressed());
          if (mc.thePlayer.isSneaking()) {
            mc.thePlayer.motionY = -0.4000000059604645D;
          } else if ((mc.gameSettings.keyBindJump.getIsKeyPressed()) && (!shouldBlock)) {
            mc.thePlayer.motionY = 0.4000000059604645D;
          } else {
            mc.thePlayer.motionY = -0.1D;
          }
          break;
        case "Vanilla": 
          if (mc.thePlayer.movementInput.jump) {
            mc.thePlayer.motionY = (speed * 0.6D);
          } else if (mc.thePlayer.movementInput.sneak) {
            mc.thePlayer.motionY = (-speed * 0.6D);
          } else {
            mc.thePlayer.motionY = 0.0D;
          }
        case "AntiKick": /** Damage fly, stand on the edge of a block, 8+ fall*/
          if (mc.thePlayer.movementInput.jump) {
            mc.thePlayer.motionY = (speed * 0.6D);
          } else if (mc.thePlayer.movementInput.sneak) {
            mc.thePlayer.motionY = (-speed * 0.6D);
          } else {
            mc.thePlayer.motionY = 0.0D;
          }
          updateFlyHeight();
          mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
          if (((this.flyHeight <= 290.0D) && (this.kickTimer.delay(500.0F))) || ((this.flyHeight > 290.0D) && 
            (this.kickTimer.delay(100.0F))))
          {
            goToGround();
            this.kickTimer.reset();
          }
          break;
        }
      }
    }
    if ((event instanceof EventMove))
    {
      EventMove em = (EventMove)event;
      double speed = Math.max(((Number)((Setting)this.settings.get(SPEED)).getValue()).floatValue() / 10.0F, getBaseMoveSpeed());
      double forward = mc.thePlayer.movementInput.moveForward;
      double strafe = mc.thePlayer.movementInput.moveStrafe;
      float yaw = mc.thePlayer.rotationYaw;
      if ((forward == 0.0D) && (strafe == 0.0D))
      {
        em.setX(0.0D);
        em.setZ(0.0D);
      }
      else
      {
        if (forward != 0.0D)
        {
          if (strafe > 0.0D) {
            yaw += (forward > 0.0D ? -45 : 45);
          } else if (strafe < 0.0D) {
            yaw += (forward > 0.0D ? 45 : -45);
          }
          strafe = 0.0D;
          if (forward > 0.0D) {
            forward = 1.0D;
          } else if (forward < 0.0D) {
            forward = -1.0D;
          }
        }
        em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * 
          Math.sin(Math.toRadians(yaw + 90.0F)));
        em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * 
          Math.cos(Math.toRadians(yaw + 90.0F)));
      }
    }
  }
}
