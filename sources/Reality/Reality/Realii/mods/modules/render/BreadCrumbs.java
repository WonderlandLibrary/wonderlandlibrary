
package Reality.Realii.mods.modules.render;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Reality.Realii.mods.modules.ClientSettings;

import Reality.Realii.utils.cheats.RenderUtills.Render2;






public class BreadCrumbs
        extends Module {
	  
	
	 private Numbers<Number> ParticlesAmount = new Numbers<>("ParticlesAmount", 0, 0, 22999, 1);
    public BreadCrumbs() {
        super("BreadCrumbs", ModuleType.Render);
       addValues(ParticlesAmount);
    }
    private final List<Vec3> path = new ArrayList<>();
    @Override
    public void onEnable() {
        path.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        path.clear();
        super.onDisable();
    }

    @EventHandler
    public void onPre(EventPreUpdate e) {
    	   
    	 
             if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
                 path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
             }
    	   
             while (path.size() > 15) {
           //  while (path.size() > ParticlesAmount.getValue().intValue()) {
                 path.remove(0);
             }
         
    }
    
    @EventHandler
    public void onRender(EventRender3D event) {
    	   int i = 0;
    	
    	   Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
    	   Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
    	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
    	  
    	   
    	   GL11.glEnable(GL11.GL_BLEND);
           GL11.glDisable(GL11.GL_TEXTURE_2D);
           GL11.glEnable(GL11.GL_LINE_SMOOTH);
           GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

           for (final Vec3 v : path) {


               i++;

               boolean draw = true;

               final double x = v.xCoord - mc.getRenderManager().renderPosX;
               final double y = v.yCoord - mc.getRenderManager().renderPosY;
               final double z = v.zCoord - mc.getRenderManager().renderPosZ;

               final double distanceFromPlayer = mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
               int quality = (int) (distanceFromPlayer * 5.3 + 10);

               if (quality > 350)
                   quality = 350;

               if (i % 10 != 0 && distanceFromPlayer > 25) {
                   draw = false;
               }

               if (i % 3 == 0 && distanceFromPlayer > 15) {
                   draw = false;
               }

               if (draw) {

                   GL11.glPushMatrix();
                   GL11.glTranslated(x, y, z);

                   
                   GL11.glScalef(-0.07f, -0.07f, -0.07f);

                   GL11.glRotated(-(mc.getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
                   GL11.glRotated((mc.getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);


                   Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);

                 


            
                   Render2.drawFilledCircleNoGL(0, -2, 1.7, Render2.applyOpacity(c.getRGB(), .6f), quality);
                  

                   if (distanceFromPlayer < 4)
                	   Render2.drawFilledCircleNoGL(0, -2, 1.4, Render2.applyOpacity(c.getRGB(), .25f), quality);

              
                   


                   GL11.glScalef(0.8f, 0.8f, 0.8f);

                   GL11.glPopMatrix();

               }

           }

           GL11.glDisable(GL11.GL_LINE_SMOOTH);
           GL11.glEnable(GL11.GL_TEXTURE_2D);
           GL11.glDisable(GL11.GL_BLEND);
           GL11.glColor3d(255, 255, 255);
    }
    
    
}



