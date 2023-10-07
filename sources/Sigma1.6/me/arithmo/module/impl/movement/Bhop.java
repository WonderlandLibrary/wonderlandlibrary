package me.arithmo.module.impl.movement;

import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventMove;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;

public class Bhop
  extends Module
{
  private final String MODE = "MODE";
  private double moveSpeed;
  private double lastDist;
  public static int stage;
  
  public Bhop(ModuleData data)
  {
    super(data);
    this.settings.put("MODE", new Setting("MODE", new Options("Speed Mode", "Hypixel", new String[] { "Bhop", "Hypixel", "OnGround", "YPort" }), "Speed bypass method."));
  }
  
  public static double getBaseMoveSpeed()
  {
    double baseSpeed = 0.2873D;
    if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
    {
      int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
      baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
    }
    return baseSpeed;
  }
  
  public void onEnable()
  {
    if (mc.thePlayer != null) {
      this.moveSpeed = getBaseMoveSpeed();
    }
    this.lastDist = 0.0D;
    stage = 2;
    mc.timer.timerSpeed = 1.0F;
  }
  
  public void onDisable()
  {
    mc.timer.timerSpeed = 1.0F;
  }
  
  @RegisterEvent(events={EventMove.class, EventMotion.class})
  public void onEvent(Event event)
  {
    String currentMode = ((Options)((Setting)this.settings.get("MODE")).getValue()).getSelected();
    setSuffix(currentMode);
    switch (currentMode)
    {
    case "Hypixel": 
      if ((event instanceof EventMove))
      {
        EventMove em = (EventMove)event;
        mc.timer.timerSpeed = 1.09F;
        if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
          this.moveSpeed = getBaseMoveSpeed();
        }
        if (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.4D, 3)) {
          em.setY(mc.thePlayer.motionY = 0.31D);
        } else if (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.71D, 3)) {
          em.setY(mc.thePlayer.motionY = 0.04D);
        } else if (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.75D, 3)) {
          em.setY(mc.thePlayer.motionY = -0.2D);
        }
        List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, -0.56D, 0.0D));
        if ((collidingList.size() > 0) && (MathUtils.roundToPlace(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.roundToPlace(0.55D, 3))) {
          em.setY(-0.14D);
        }
        if ((stage == 1) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
          this.moveSpeed = (2.14D * getBaseMoveSpeed() - 0.01D);
        }
        if ((stage == 2) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
        {
          em.setY(mc.thePlayer.motionY = 0.4D);
          this.moveSpeed *= 1.5563D;
        }
        else if (stage == 3)
        {
          double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
          this.moveSpeed = (this.lastDist - difference);
        }
        else
        {
          List collidingList2 = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
          if (((collidingList2.size() > 0) || (mc.thePlayer.isCollidedVertically)) && (stage > 0)) {
            stage = (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
          }
          this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        if (stage > 0)
        {
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
            em.setX(forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
          }
        }
        if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
          stage += 1;
        }
      }
      if ((event instanceof EventMotion))
      {
        EventMotion em = (EventMotion)event;
        if (em.isPre())
        {
          double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
          double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
          this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
      }
      break;
    case "Bhop": 
      if ((event instanceof EventMove))
      {
        EventMove em = (EventMove)event;
        mc.timer.timerSpeed = 1.085F;
        if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
          this.moveSpeed = getBaseMoveSpeed();
        }
        if ((stage == 1) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
          this.moveSpeed = (1.35D + getBaseMoveSpeed() - 0.01D);
        }
        if ((stage == 2) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
        {
          em.setY(mc.thePlayer.motionY = 0.4D);
          this.moveSpeed *= 1.533D;
        }
        else if (stage == 3)
        {
          double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
          this.moveSpeed = (this.lastDist - difference);
        }
        else
        {
          List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
          if (((collidingList.size() > 0) || (mc.thePlayer.isCollidedVertically)) && (stage > 0)) {
            stage = (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
          }
          this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        if (stage > 0)
        {
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
            em.setX(forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
          }
        }
        if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
          stage += 1;
        }
      }
      if ((event instanceof EventMotion))
      {
        EventMotion em = (EventMotion)event;
        if (em.isPre())
        {
          double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
          double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
          this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
      }
      break;
    case "OnGround": 
      if ((event instanceof EventMotion))
      {
        EventMotion em = (EventMotion)event;
        if (em.isPre())
        {
          mc.timer.timerSpeed = 1.085F;
          double forward = mc.thePlayer.movementInput.moveForward;
          double strafe = mc.thePlayer.movementInput.moveStrafe;
          if (((forward != 0.0D) || (strafe != 0.0D)) && (!mc.thePlayer.isJumping) && (!mc.thePlayer.isInWater()) && (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isCollidedHorizontally)) {
            em.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.4D : 0.0D));
          }
          this.moveSpeed = Math.max(mc.thePlayer.ticksExisted % 2 == 0 ? 2.1D : 1.3D, getBaseMoveSpeed());
          float yaw = mc.thePlayer.rotationYaw;
          if ((forward == 0.0D) && (strafe == 0.0D))
          {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
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
                forward = 0.15D;
              } else if (forward < 0.0D) {
                forward = -0.15D;
              }
            }
            if (strafe > 0.0D) {
              strafe = 0.15D;
            } else if (strafe < 0.0D) {
              strafe = -0.15D;
            }
            mc.thePlayer.motionX = (forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
            mc.thePlayer.motionZ = (forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
          }
        }
      }
      break;
    case "YPort": 
      if ((event instanceof EventMove))
      {
        EventMove em = (EventMove)event;
        if ((mc.thePlayer.onGround) || (stage == 3))
        {
          if (((!mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.moveForward != 0.0F)) || (mc.thePlayer.moveStrafing != 0.0F))
          {
            if (stage == 2)
            {
              this.moveSpeed *= 2.149D;
              stage = 3;
            }
            else if (stage == 3)
            {
              stage = 2;
              double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
              this.moveSpeed = (this.lastDist - difference);
            }
            else
            {
              List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
              if ((collidingList.size() > 0) || (mc.thePlayer.isCollidedVertically)) {
                stage = 1;
              }
            }
          }
          else {
            mc.timer.timerSpeed = 1.0F;
          }
          this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
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
            em.setX(forward * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * this.moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)));
          }
        }
      }
      if ((event instanceof EventMotion))
      {
        EventMotion em = (EventMotion)event;
        if (em.isPre())
        {
          if (stage == 3)
          {
            double gay = 0.4D;
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
              gay = (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
            }
            em.setY(em.getY() + gay);
          }
          double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
          double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
          this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
      }
      break;
    }
  }
}
