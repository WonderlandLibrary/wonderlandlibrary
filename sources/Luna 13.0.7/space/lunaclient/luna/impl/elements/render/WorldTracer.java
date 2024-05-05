package space.lunaclient.luna.impl.elements.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.BooleanSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventPacketSend;
import space.lunaclient.luna.impl.events.EventRender3D;
import space.lunaclient.luna.util.Box;
import space.lunaclient.luna.util.RenderUtils;
import space.lunaclient.luna.util.scaffold.Vec3d;

@ElementInfo(name="WorldTracer", category=Category.RENDER, description="Draws a rainbow line after your ass, credits to Direkt (idfk who actually made this, all ik is its from Direkt)")
public class WorldTracer
  extends Element
{
  @BooleanSetting(name="Dots", booleanValue=true)
  private Setting dots;
  private List<Vec3d> cords = new ArrayList();
  private float color = 0.0F;
  private float offset = 0.0F;
  
  public WorldTracer() {}
  
  @EventRegister
  public void onRender(EventRender3D event)
  {
    if (!isToggled()) {
      this.cords.remove(0);
    }
    GL11.glPushMatrix();
    GL11.glDisable(3553);
    GL11.glEnable(3042);
    GL11.glDisable(2929);
    GL11.glEnable(2848);
    
    GL11.glBegin(3);
    for (Vec3d vec : this.cords)
    {
      double x = vec.xCoord - RenderManager.renderPosX;
      double y = vec.yCoord - RenderManager.renderPosY;
      double z = vec.zCoord - RenderManager.renderPosZ;
      RenderUtils.glColor(Color.getHSBColor(this.color += 0.05F, 1.0F, 0.7F).getRGB());
      GL11.glVertex3d(x, y, z);
    }
    GL11.glEnd();
    this.color -= this.color + this.offset * 0.1F;
    
    GL11.glDisable(2848);
    GL11.glEnable(2929);
    GL11.glDisable(3042);
    GL11.glEnable(3553);
    if (this.dots.getValBoolean())
    {
      RenderUtils.enableGL3D(1.5F);
      GL11.glEnable(3042);
      GL11.glDisable(2929);
      for (Vec3d vec : this.cords)
      {
        double x = vec.xCoord - RenderManager.renderPosX;
        double y = vec.yCoord - RenderManager.renderPosY;
        double z = vec.zCoord - RenderManager.renderPosZ;
        GL11.glTranslated(x, y, z);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
        RenderUtils.drawBox(new Box(-0.01D, -0.01D, -0.01D, 0.01D, 0.01D, 0.01D));
        GL11.glTranslated(-x, -y, -z);
      }
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      RenderUtils.disableGL3D();
    }
    GL11.glPopMatrix();
    if (this.offset < 10.0F) {
      this.offset += 0.1F;
    } else {
      this.offset = 0.0F;
    }
  }
  
  @EventRegister
  public void onPacket(EventPacketSend event)
  {
    if ((event.getPacket() instanceof C03PacketPlayer & Minecraft.thePlayer.isMoving()))
    {
      C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
      event.setCancelled(true);
      this.cords.add(new Vec3d(packet.getX(Minecraft.thePlayer.posX), packet.getY(Minecraft.thePlayer.posY) < 0.0D ? Minecraft.thePlayer.posY : packet.getY(Minecraft.thePlayer.posY), packet.getZ(Minecraft.thePlayer.posZ)));
    }
  }
}
