package space.lunaclient.luna.impl.elements.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventRender3D;
import space.lunaclient.luna.impl.managers.FriendManager;
import space.lunaclient.luna.util.RenderUtils;

@ElementInfo(name="Lines", category=Category.RENDER)
public class Lines
  extends Element
{
  public Lines() {}
  
  @EventRegister
  public void onRender(EventRender3D e)
  {
    for (Object theObject : Minecraft.theWorld.loadedEntityList) {
      if ((theObject instanceof EntityLivingBase))
      {
        EntityLivingBase entity = (EntityLivingBase)theObject;
        if ((entity instanceof EntityPlayer & entity != Minecraft.thePlayer & !entity.isInvisible())) {
          draw(entity);
        }
      }
    }
  }
  
  public void draw(EntityLivingBase entity)
  {
    Color tracercolor = Color.WHITE;
    int entitycolordistance = (int)Minecraft.thePlayer.getDistanceSqToEntity(entity) / 85;
    if (entitycolordistance >= 200) {
      entitycolordistance = 199;
    }
    if ((!entity.isInvisible() & entity instanceof EntityPlayer))
    {
      int green = entitycolordistance * 10;
      if (green >= 255) {
        green = 255;
      }
      tracercolor = new Color(255 - green, green, 0, 255);
      if (FriendManager.isFriend(entity.getName())) {
        tracercolor = new Color(117, 30, 255, 255);
      }
    }
    mc.getRenderManager();
    double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
    mc.getRenderManager();
    double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
    mc.getRenderManager();
    double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
    if (tracercolor != Color.WHITE) {
      render(tracercolor, xPos, yPos, zPos);
    }
  }
  
  public void render(Color color, double x, double y, double z)
  {
    RenderUtils.drawTracerLine(x, y, z, color, 0.45F, 1.15921F);
  }
}
