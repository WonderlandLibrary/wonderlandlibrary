package me.arithmo.module.impl.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventMove;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class LongJump
  extends Module
{
  private double moveSpeed;
  private double lastDist;
  public static int stage;
  
  public LongJump(ModuleData data)
  {
    super(data);
    this.settings.put("BOOST", new Setting("BOOST", Integer.valueOf(3), "Boost speed.", 0.1D, 1.0D, 5.0D));
  }
  
  private final String BOOST = "BOOST";
  
  public void onEnable()
  {
    stage = 0;
    mc.thePlayer.motionX = 0.0D;
    mc.thePlayer.motionZ = 0.0D;
    super.onEnable();
  }
  
  private static double getBaseMoveSpeed()
  {
    double baseSpeed = 0.2873D;
    if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
    {
      int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
      baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
    }
    return baseSpeed;
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
  
  @RegisterEvent(events={EventMove.class, EventMotion.class})
  public void onEvent(Event event)
  {
    if ((event instanceof EventMove))
    {
      EventMove em = (EventMove)event;
      if ((mc.thePlayer.moveStrafing <= 0.0F) && (mc.thePlayer.moveForward <= 0.0F)) {
        stage = 1;
      }
      if (round(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == round(0.943D, 3))
      {
        mc.thePlayer.motionY -= 0.03D;
        em.setY(0.03D);
      }
      if ((stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
      {
        stage = 2;
        this.moveSpeed = (((Number)((Setting)this.settings.get("BOOST")).getValue()).doubleValue() * getBaseMoveSpeed() - 0.01D);
      }
      else if (stage == 2)
      {
        stage = 3;
        mc.thePlayer.motionY = 0.424D;
        em.setY(0.424D);
        this.moveSpeed *= 2.149802D;
      }
      else if (stage == 3)
      {
        stage = 4;
        double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
        this.moveSpeed = (this.lastDist - difference);
      }
      else
      {
        if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) || (mc.thePlayer.isCollidedVertically)) {
          stage = 1;
        }
        this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
      }
      this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
      MovementInput movementInput = mc.thePlayer.movementInput;
      float forward = movementInput.moveForward;
      float strafe = movementInput.moveStrafe;
      float yaw = mc.thePlayer.rotationYaw;
      if ((forward == 0.0F) && (strafe == 0.0F))
      {
        em.setX(0.0D);
        em.setZ(0.0D);
      }
      else if (forward != 0.0F)
      {
        if (strafe >= 1.0F)
        {
          yaw += (forward > 0.0F ? -45 : 45);
          strafe = 0.0F;
        }
        else if (strafe <= -1.0F)
        {
          yaw += (forward > 0.0F ? 45 : -45);
          strafe = 0.0F;
        }
        if (forward > 0.0F) {
          forward = 1.0F;
        } else if (forward < 0.0F) {
          forward = -1.0F;
        }
      }
      double mx = Math.cos(Math.toRadians(yaw + 90.0F));
      double mz = Math.sin(Math.toRadians(yaw + 90.0F));
      em.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
      em.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
    }
    if ((event instanceof EventMotion))
    {
      EventMotion em = (EventMotion)event;
      if (em.isPre()) {
        if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))
        {
          double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
          double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
          this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        else
        {
          event.setCancelled(true);
        }
      }
    }
  }
}
