package xyz.cucumber.base.module.feat.visuals;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

@ModuleInfo(category = Category.VISUALS, description = "Displays circle where you jump", name = "Jump Circles")
public class JumpCirclesModule extends Mod {
	
	private HashMap<Integer, Vec3> circles = new HashMap();
	
	private ColorSettings color = new ColorSettings("Color", "Static", -1, -1, 90);
	
	public JumpCirclesModule() {
		this.addSettings(color);
	}
	
	@EventListener
	public void onRender3D(EventRender3D e) {
		
		Iterator<Entry<Integer, Vec3>> itr = circles.entrySet().iterator();
        
        while(itr.hasNext()){
        	Entry<Integer, Vec3> circle = itr.next();
        	Vec3 position = circle.getValue();
 			int time = circle.getKey();
 			if(time < System.nanoTime()/1000000) {
 				itr.remove();
 				continue;
 			}
 			
 			double timeDiff = time-System.nanoTime()/1000000;
 			
 			double r = 0.8F;
 			
 			float a = (float)(color.getAlpha()) /100F;
 			
 			if(timeDiff >=1000) {
 				double f = Math.abs(timeDiff - 2000);
 				
 				r = Math.pow(0.8F /1000F * f, 0.9);
 			}else {
 				double f = Math.abs(timeDiff - 1000);
 				
 				a = color.getAlpha() /100F - (float) (color.getAlpha() /100F / 1000f * f);
 			}
 			
 			double x = position.xCoord - mc.getRenderManager().viewerPosX;
			double y = position.yCoord - mc.getRenderManager().viewerPosY;
			double z = position.zCoord - mc.getRenderManager().viewerPosZ;
			
			GL11.glPushMatrix();
			
			RenderUtils.start3D();
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			GL11.glTranslated(x, y+0.01, z);
			GL11.glRotatef((mc.thePlayer.ticksExisted+e.getPartialTicks()) *8, 0f, 1f, 0f);
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			
			RenderUtils.color(0x00ffffff);
			GL11.glVertex3d(0,0,0);
			
			for(double i = 0; i <=360 ; i+=5) {
				double posX = Math.sin(Math.toRadians(i))* r;
				double posZ = Math.cos(Math.toRadians(i))* r;
				int c = ColorUtils.getColor(color, 0, i, 10);
				RenderUtils.color(c, a);
				
				GL11.glVertex3d(posX, 0+0.01, posZ);
			}
			GL11.glEnd();
			GL11.glShadeModel(GL11.GL_FLAT);
			RenderUtils.stop3D();
			
			GL11.glPopMatrix();
        }
	}
	@EventListener
	public void onJump(EventJump e) {
		if(mc.thePlayer.onGround)
			circles.put((int) (System.nanoTime()/1000000+2000), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
	}

}
