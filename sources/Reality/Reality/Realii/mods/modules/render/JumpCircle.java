
package Reality.Realii.mods.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.entity.EntityValidator;
import Reality.Realii.utils.entity.impl.VoidCheck;
import Reality.Realii.utils.entity.impl.WallCheck;
import Reality.Realii.utils.render.RenderUtil;

import java.awt.*;

public final class JumpCircle
        extends Module {
	private TimerUtil timer = new TimerUtil();
	
	
    public JumpCircle() {
        super("JumpCircle", ModuleType.Render);
       
      
    }
    @Override
    public void onEnable() {
    	timer.reset();
    }

   
    

    @EventHandler
    public void onRender2(EventRender3D eventRender) {
    	
    	  if(mc.thePlayer.onGround) {
    	  GL11.glPushMatrix();
          GL11.glDisable((int) 3553);
          RenderUtil.startSmooth();
          GL11.glDisable((int) 2929);
          GL11.glDepthMask((boolean) false);
          GL11.glLineWidth((float) 2);
          GL11.glBegin((int) 3);
          float x = (float) ((float)(mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * eventRender.getPartialTicks()) - mc.getRenderManager().renderPosX);
  		  float y = (float) (((float)(mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * eventRender.getPartialTicks())) - mc.getRenderManager().renderPosY);
          float z = (float) ((float)(mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * eventRender.getPartialTicks()) - mc.getRenderManager().renderPosZ);
          double pix2 = Math.PI * 2;
          int tick = 0;

          for (int i = 0; i <= 45; ++i) {
        	  int i2 = 0;
        	//  GL11.glVertex3d(x + Math.sin(i * Math.PI / 180.0D) * 0.6, y - 0.3D , z + Math.cos(i * Math.PI / 180.0D) * 0.6);
             
	      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
	      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
	        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
	        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i2 * 20), colors.getFirst(), colors.getSecond(), false);
              GL11.glColor3f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f);
              GL11.glVertex3d((double) (x + 0.65 * Math.cos((double) i * (Math.PI * 2) / 45.0)), (double) y, (double) (z + 0.65 * Math.sin((double) i * (Math.PI * 2) / 45.0)));
              GL11.glVertex3d((double) (x + 0.65 * Math.cos((double) i * (Math.PI * 2) / 45.0)), (double) y, (double) (z + 0.65 * Math.sin((double) i * (Math.PI * 2) / 45.0)));
              tick += 1;
          }
          GL11.glEnd();
          GL11.glDepthMask((boolean) true);
          GL11.glEnable((int) 2929);
          RenderUtil.endSmooth();
          GL11.glEnable((int) 3553);
          GL11.glPopMatrix();
          timer.reset();
    	  }
    }
    }



