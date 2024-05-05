package space.lunaclient.luna.impl.elements.movement;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="Step", category=Category.MOVEMENT)
public class Step
  extends Element
{
  public Step() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (isToggled())
    {
      double xOffset = MovementInput.moveForward * 0.1D * Math.cos(Math.toRadians(Minecraft.thePlayer.rotationYaw + 90.0F)) + MovementInput.moveStrafe * 0.1D * Math.sin(Math.toRadians(Minecraft.thePlayer.rotationYaw + 90.0F));
      double zOffset = MovementInput.moveForward * 0.1D * Math.sin(Math.toRadians(Minecraft.thePlayer.rotationYaw + 90.0F)) - MovementInput.moveStrafe * 0.1D * Math.cos(Math.toRadians(Minecraft.thePlayer.rotationYaw + 90.0F));
      boolean clearAbove = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(xOffset, 1.5D, zOffset)).isEmpty();
      if ((Minecraft.thePlayer.isOnLadder() | !clearAbove | !Minecraft.thePlayer.isCollidedHorizontally))
      {
        net.minecraft.util.Timer.timerSpeed = 1.0F;
        return;
      }
      if (Minecraft.thePlayer.onGround)
      {
        Minecraft.thePlayer.motionY = 0.3915D;
        net.minecraft.util.Timer.timerSpeed = 0.5F;
      }
      else if (Minecraft.thePlayer.motionY <= -0.1701D)
      {
        Minecraft.thePlayer.motionY = 0.2599D;
        net.minecraft.util.Timer.timerSpeed = 0.5F;
      }
      else
      {
        net.minecraft.util.Timer.timerSpeed = 1.0F;
      }
    }
  }
}
