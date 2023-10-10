package my.NewSnake.Tank.module.modules.PLAYER;

import java.awt.Color;
import java.util.Iterator;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render3DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

@Module.Mod(
   displayName = "ESP 2D"
)
public class ESP2D extends Module {
   @EventTarget(0)
   private void onRender3D(Render3DEvent var1) {
      this.setSuffix("Old");
      double var2 = 0.5D;
      ClientUtils.mc();
      Iterator var5 = Minecraft.theWorld.loadedEntityList.iterator();

      while(var5.hasNext()) {
         Object var4 = var5.next();
         if (var4 instanceof EntityPlayer) {
            ClientUtils.mc();
            if (var4 != Minecraft.thePlayer) {
               EntityPlayer var6 = (EntityPlayer)var4;
               if (!var6.isInvisible()) {
                  GL11.glPushMatrix();
                  double var7 = var6.lastTickPosX + (var6.posX - var6.lastTickPosX) * (double)ClientUtils.mc().timer.renderPartialTicks - RenderManager.renderPosX;
                  double var9 = var6.lastTickPosY + (var6.posY - var6.lastTickPosY) * (double)ClientUtils.mc().timer.renderPartialTicks - RenderManager.renderPosY;
                  double var11 = var6.lastTickPosZ + (var6.posZ - var6.lastTickPosZ) * (double)ClientUtils.mc().timer.renderPartialTicks - RenderManager.renderPosZ;
                  int var13 = var6.hurtTime == 0 ? Color.green.hashCode() : Color.RED.hashCode();
                  GL11.glTranslated(var7, var9, var11);
                  GlStateManager.disableDepth();
                  RenderManager var14 = ClientUtils.mc().renderManager;
                  GL11.glRotated((double)(-RenderManager.playerViewY), 0.0D, 1.0D, 0.0D);
                  boolean var15 = false;
                  Gui.drawRect(0.83D, 2.03D, 0.67D, 1.47D, 0);
                  Gui.drawRect(0.83D, 0.63D, 0.67D, -0.03D, 0);
                  Gui.drawRect(-0.47D, 2.03D, -0.63D, 1.47D, 0);
                  Gui.drawRect(-0.47D, 0.63D, -0.63D, -0.03D, 0);
                  Gui.drawRect(0.8D, 2.03D, 0.27D, 1.87D, 0);
                  Gui.drawRect(0.03D, 2.03D, -0.5D, 1.87D, 0);
                  Gui.drawRect(0.03D, 0.13D, -0.53D, -0.03D, 0);
                  Gui.drawRect(0.8D, 0.13D, 0.27D, -0.03D, 0);
                  Gui.drawRect(0.8D, 2.0D, 0.7D, 1.5D, var13);
                  Gui.drawRect(0.8D, 0.6D, 0.7D, 0.09999999999999998D, var13);
                  Gui.drawRect(-0.5D, 2.0D, -0.6D, 1.5D, var13);
                  Gui.drawRect(-0.5D, 0.6D, -0.6D, -2.7755575615628914E-17D, var13);
                  Gui.drawRect(0.8D, 2.0D, 0.30000000000000004D, 1.9D, var13);
                  Gui.drawRect(0.0D, 2.0D, -0.5D, 1.9D, var13);
                  Gui.drawRect(0.0D, 0.1D, -0.5D, 0.0D, var13);
                  Gui.drawRect(0.8D, 0.1D, 0.30000000000000004D, 0.0D, var13);
                  GlStateManager.enableDepth();
                  GL11.glPopMatrix();
               }
            }
         }
      }

   }
}
