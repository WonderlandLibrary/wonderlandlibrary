package space.lunaclient.luna.impl.elements.movement.flight.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import space.lunaclient.luna.api.event.Event.Type;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventMotion;
import space.lunaclient.luna.impl.events.EventMove;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;

public class SkyGiants
{
  public static double moveSpeed;
  private static double lastDist;
  public static int stage = 0;
  private double[] speedVal;
  public int counter;
  
  public SkyGiants() {}
  
  @EventRegister
  public void onMotion(EventMotion event)
  {
    if (event.getType() == Event.Type.PRE)
    {
      double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
      double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
      lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }
  }
  
  @EventRegister
  public void onMove(EventMove e)
  {
    this.counter = (Minecraft.thePlayer.ticksExisted % 13 == 0 ? 1 : 0);
    
    float direction3 = Minecraft.thePlayer.rotationYaw + (Minecraft.thePlayer.moveForward < 0.0F ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0F ? -90.0F * (Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : Minecraft.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F) - (Minecraft.thePlayer.moveStrafing < 0.0F ? -90.0F * (Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : Minecraft.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F);
    float xDir = (float)Math.cos((direction3 + 90.0F) * 3.141592653589793D / 180.0D);
    float zDir = (float)Math.sin((direction3 + 90.0F) * 3.141592653589793D / 180.0D);
    net.minecraft.util.Timer.timerSpeed = 1.0F;
    if ((Minecraft.thePlayer.moveForward == 0.0F) && (Minecraft.thePlayer.moveStrafing == 0.0F)) {
      moveSpeed = PlayerUtils.getBaseMoveSpeed() - 0.05D;
    }
    if ((stage == 1) && (Minecraft.thePlayer.isCollidedVertically) && ((Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F))) {
      moveSpeed = 1.37D + PlayerUtils.getBaseMoveSpeed() - 0.01D;
    }
    if ((stage == 2) && (Minecraft.thePlayer.isCollidedVertically) && ((Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F)))
    {
      e.setY(Minecraft.thePlayer.motionY = Minecraft.thePlayer.ticksExisted % 2 == 0 ? 0.4D : 0.3995D);
      Random random = new Random();
      double[] speedVal = { 2.11D };
      int index = random.nextInt(speedVal.length);
      double var = speedVal[index];
      moveSpeed *= var;
      Minecraft.thePlayer.motionY += 0.005D;
    }
    else if (stage == 3)
    {
      double difference = 0.56D * (lastDist - PlayerUtils.getBaseMoveSpeed() - 0.05D);
      moveSpeed = lastDist - difference;
    }
    else
    {
      List collidingList = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox
        .offset(0.0D, Minecraft.thePlayer.motionY, 0.0D));
      if (((collidingList.size() > 0) || (Minecraft.thePlayer.isCollidedVertically)) && (stage > 0)) {
        stage = (Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
      }
      moveSpeed = lastDist - lastDist / 159.0D;
    }
    moveSpeed = Math.max(moveSpeed, PlayerUtils.getBaseMoveSpeed());
    if (stage > 0)
    {
      double forward = MovementInput.moveForward;
      double strafe = MovementInput.moveStrafe;
      float yaw = Minecraft.thePlayer.rotationYaw;
      if ((forward == 0.0D) && (strafe == 0.0D))
      {
        e.setX(0.0D);
        e.setZ(0.0D);
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
          } else {
            forward = -1.0D;
          }
        }
        e.setX(forward * moveSpeed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * moveSpeed * 
          Math.sin(Math.toRadians(yaw + 90.0F)));
        e.setZ(forward * moveSpeed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * moveSpeed * 
          Math.cos(Math.toRadians(yaw + 90.0F)));
      }
    }
    if ((Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F)) {
      stage += 1;
    }
  }
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if (this.counter < 1)
    {
      if (Minecraft.thePlayer.isMoving()) {
        Minecraft.thePlayer.setSpeed(PlayerUtils.getBaseMoveSpeed() - 0.1D);
      } else {
        Minecraft.thePlayer.setSpeed(0.0D);
      }
      Minecraft.thePlayer.onGround = true;
      if (Minecraft.thePlayer.isMoving())
      {
        net.minecraft.util.Timer.timerSpeed = Minecraft.thePlayer.ticksExisted % 2 == 0 ? 1.0F : 1.0535F;
      }
      else
      {
        Minecraft.thePlayer.motionX *= 0.0D;
        Minecraft.thePlayer.motionZ *= 0.0D;
        net.minecraft.util.Timer.timerSpeed = 1.0F;
      }
      PlayerUtils.updatePosition(Minecraft.thePlayer);
      Minecraft.thePlayer.motionY = 0.0D;
    }
  }
}
