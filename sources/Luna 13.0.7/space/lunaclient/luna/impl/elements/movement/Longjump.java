package space.lunaclient.luna.impl.elements.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.Event.Type;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventMove;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.scaffold.BlockUtils;

@ElementInfo(name="LongJump", category=Category.MOVEMENT)
public class Longjump
  extends Element
{
  private int airTicks;
  private int groundTicks;
  public double yOffset;
  
  public Longjump() {}
  
  @EventRegister
  public void onMove(EventMove event)
  {
    if (mc.gameSettings.keyBindSneak.pressed) {
      return;
    }
    Minecraft.thePlayer.setSprinting(false);
    mc.gameSettings.keyBindLeft.pressed = false;
    mc.gameSettings.keyBindRight.pressed = false;
    mc.gameSettings.keyBindBack.pressed = false;
    if (Minecraft.thePlayer.isMoving())
    {
      mc.gameSettings.keyBindJump.pressed = true;
      float direction3 = Minecraft.thePlayer.rotationYaw + (Minecraft.thePlayer.moveForward < 0.0F ? 180 : 0) + (Minecraft.thePlayer.moveStrafing > 0.0F ? -90.0F * (Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : Minecraft.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F) - (Minecraft.thePlayer.moveStrafing < 0.0F ? -90.0F * (Minecraft.thePlayer.moveForward > 0.0F ? 0.5F : Minecraft.thePlayer.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F);
      float xDir = (float)Math.cos((direction3 + 90.0F) * 3.141592653589793D / 180.0D);
      float zDir = (float)Math.sin((direction3 + 90.0F) * 3.141592653589793D / 180.0D);
      if (!Minecraft.thePlayer.isCollidedVertically)
      {
        this.airTicks += 1;
        if (mc.gameSettings.keyBindSneak.isPressed()) {
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.0D, 2.147483647E9D, 0.0D, false));
        }
        this.groundTicks = 0;
        if (!Minecraft.thePlayer.isCollidedVertically)
        {
          if (Minecraft.thePlayer.motionY == -0.07190068807140403D) {
            Minecraft.thePlayer.motionY *= 0.3499999940395355D;
          }
          if (Minecraft.thePlayer.motionY == -0.10306193759436909D) {
            Minecraft.thePlayer.motionY *= 0.550000011920929D;
          }
          if (Minecraft.thePlayer.motionY == -0.13395038817442878D) {
            Minecraft.thePlayer.motionY *= 0.6700000166893005D;
          }
          if (Minecraft.thePlayer.motionY == -0.16635183030382D) {
            Minecraft.thePlayer.motionY *= 0.6899999976158142D;
          }
          if (Minecraft.thePlayer.motionY == -0.19088711097794803D) {
            Minecraft.thePlayer.motionY *= 0.7099999785423279D;
          }
          if (Minecraft.thePlayer.motionY == -0.21121925191528862D) {
            Minecraft.thePlayer.motionY *= 0.20000000298023224D;
          }
          if (Minecraft.thePlayer.motionY == -0.11979897632390576D) {
            Minecraft.thePlayer.motionY *= 0.9300000071525574D;
          }
          if (Minecraft.thePlayer.motionY == -0.18758479151225355D) {
            Minecraft.thePlayer.motionY *= 0.7200000286102295D;
          }
          if (Minecraft.thePlayer.motionY == -0.21075983825251726D) {
            Minecraft.thePlayer.motionY *= 0.7599999904632568D;
          }
          if ((getDistance(Minecraft.thePlayer, 69.0D) < 0.5D) && (!BlockUtils.getBlock(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.32D, Minecraft.thePlayer.posZ)).isFullCube()))
          {
            if (Minecraft.thePlayer.motionY == -0.23537393014173347D) {
              Minecraft.thePlayer.motionY *= 0.029999999329447746D;
            }
            if (Minecraft.thePlayer.motionY == -0.08531999505205401D) {
              Minecraft.thePlayer.motionY *= -0.5D;
            }
            if (Minecraft.thePlayer.motionY == -0.03659320313669756D) {
              Minecraft.thePlayer.motionY *= -0.10000000149011612D;
            }
            if (Minecraft.thePlayer.motionY == -0.07481386749524899D) {
              Minecraft.thePlayer.motionY *= -0.07000000029802322D;
            }
            if (Minecraft.thePlayer.motionY == -0.0732677700939672D) {
              Minecraft.thePlayer.motionY *= -0.05000000074505806D;
            }
            if (Minecraft.thePlayer.motionY == -0.07480988066790395D) {
              Minecraft.thePlayer.motionY *= -0.03999999910593033D;
            }
            if (Minecraft.thePlayer.motionY == -0.0784000015258789D) {
              Minecraft.thePlayer.motionY *= 0.10000000149011612D;
            }
            if (Minecraft.thePlayer.motionY == -0.08608320193943977D) {
              Minecraft.thePlayer.motionY *= 0.10000000149011612D;
            }
            if (Minecraft.thePlayer.motionY == -0.08683615560584318D) {
              Minecraft.thePlayer.motionY *= 0.05000000074505806D;
            }
            if (Minecraft.thePlayer.motionY == -0.08265497329678266D) {
              Minecraft.thePlayer.motionY *= 0.05000000074505806D;
            }
            if (Minecraft.thePlayer.motionY == -0.08245009535659828D) {
              Minecraft.thePlayer.motionY *= 0.05000000074505806D;
            }
            if (Minecraft.thePlayer.motionY == -0.08244005633718426D) {
              Minecraft.thePlayer.motionY = -0.08243956442521608D;
            }
            if (Minecraft.thePlayer.motionY == -0.08243956442521608D) {
              Minecraft.thePlayer.motionY = -0.08244005590677261D;
            }
            if ((Minecraft.thePlayer.motionY > -0.1D) && (Minecraft.thePlayer.motionY < -0.08D) && (!Minecraft.thePlayer.onGround) && (mc.gameSettings.keyBindForward.pressed)) {
              Minecraft.thePlayer.motionY = -9.999999747378752E-5D;
            }
          }
          else
          {
            if ((Minecraft.thePlayer.motionY < -0.2D) && (Minecraft.thePlayer.motionY > -0.24D)) {
              Minecraft.thePlayer.motionY *= 0.7D;
            }
            if ((Minecraft.thePlayer.motionY < -0.25D) && (Minecraft.thePlayer.motionY > -0.32D)) {
              Minecraft.thePlayer.motionY *= 0.8D;
            }
            if ((Minecraft.thePlayer.motionY < -0.35D) && (Minecraft.thePlayer.motionY > -0.8D)) {
              Minecraft.thePlayer.motionY *= 0.98D;
            }
            if ((Minecraft.thePlayer.motionY < -0.8D) && (Minecraft.thePlayer.motionY > -1.6D)) {
              Minecraft.thePlayer.motionY *= 0.99D;
            }
          }
        }
        net.minecraft.util.Timer.timerSpeed = 0.85F;
        double[] speedVals = { 0.420606D, 0.417924D, 0.415258D, 0.412609D, 0.409977D, 0.407361D, 0.404761D, 0.402178D, 0.399611D, 0.39706D, 0.394525D, 0.392D, 0.3894D, 0.38644D, 0.383655D, 0.381105D, 0.37867D, 0.37625D, 0.37384D, 0.37145D, 0.369D, 0.3666D, 0.3642D, 0.3618D, 0.35945D, 0.357D, 0.354D, 0.351D, 0.348D, 0.345D, 0.342D, 0.339D, 0.336D, 0.333D, 0.33D, 0.327D, 0.324D, 0.321D, 0.318D, 0.315D, 0.312D, 0.309D, 0.307D, 0.305D, 0.303D, 0.3D, 0.297D, 0.295D, 0.293D, 0.291D, 0.289D, 0.287D, 0.285D, 0.283D, 0.281D, 0.279D, 0.277D, 0.275D, 0.273D, 0.271D, 0.269D, 0.267D, 0.265D, 0.263D, 0.261D, 0.259D, 0.257D, 0.255D, 0.253D, 0.251D, 0.249D, 0.247D, 0.245D, 0.243D, 0.241D, 0.239D, 0.237D };
        if (mc.gameSettings.keyBindForward.pressed)
        {
          try
          {
            Minecraft.thePlayer.motionX = (xDir * speedVals[(this.airTicks - 1)] * 2.5D * addSpeedForSpeedEffect());
            Minecraft.thePlayer.motionZ = (zDir * speedVals[(this.airTicks - 1)] * 2.5D * addSpeedForSpeedEffect());
          }
          catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {}
        }
        else
        {
          Minecraft.thePlayer.motionX = 0.0D;
          Minecraft.thePlayer.motionZ = 0.0D;
        }
      }
      else
      {
        net.minecraft.util.Timer.timerSpeed = 1.0F;
        this.airTicks = 0;
        this.groundTicks += 1;
        Minecraft.thePlayer.motionX /= 13.0D;
        Minecraft.thePlayer.motionZ /= 13.0D;
        if (this.groundTicks == 1)
        {
          updatePosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
          updatePosition(Minecraft.thePlayer.posX + 0.0624D, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
          updatePosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.419D, Minecraft.thePlayer.posZ);
          updatePosition(Minecraft.thePlayer.posX + 0.0624D, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
          updatePosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.419D, Minecraft.thePlayer.posZ);
        }
        if (this.groundTicks > 2)
        {
          this.groundTicks = 0;
          Minecraft.thePlayer.motionX = (xDir * 0.3D);
          Minecraft.thePlayer.motionZ = (zDir * 0.3D);
          Minecraft.thePlayer.motionY = 0.42399999499320984D;
        }
      }
    }
    else
    {
      mc.gameSettings.keyBindJump.pressed = false;
    }
  }
  
  private double addSpeedForSpeedEffect()
  {
    double baseSpeed = 1.0D;
    if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed))
    {
      Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
      baseSpeed = 0.4D;
    }
    return baseSpeed;
  }
  
  private void updatePosition(double x, double y, double z)
  {
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, Minecraft.thePlayer.onGround));
  }
  
  private double getDistance(EntityPlayer player, double distance)
  {
    List boundingBoxes = player.worldObj.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().addCoord(0.0D, -distance, 0.0D));
    if (boundingBoxes.isEmpty()) {
      return 0.0D;
    }
    double y = 0.0D;
    return player.posY - y;
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
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if (event.getType() == Event.Type.PRE)
    {
      boolean speedy = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed);
      double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
      double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
      double d1 = Math.sqrt(xDist * xDist + zDist * zDist);
    }
  }
}
