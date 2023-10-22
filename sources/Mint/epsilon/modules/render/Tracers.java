package epsilon.modules.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import epsilon.events.Event;
import epsilon.events.listeners.render.EventRender3d;
import epsilon.modules.Module;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.ESP.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module{
	
	
	public Tracers(){
		super("Tracers", Keyboard.KEY_NONE, Category.RENDER, "Doesnt do anything yet");
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventRender3d) {
			if(mc.theWorld!=null) {
				for (Object ento: mc.theWorld.loadedEntityList) {
					
					if(ento instanceof EntityPlayer) {
						EntityPlayer t = (EntityPlayer) ento;
						if(!t.isDead && t!=mc.thePlayer) {
							final double x = (t.lastTickPosX + (t.posX - t.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
					        final double y = (t.lastTickPosY + (t.posY - t.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
					        final double z = (t.lastTickPosZ + (t.posZ - t.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
					        GL11.glColor4d(255, 255, 255, 255);
							drawLine(x, y, z);
					        
					        GL11.glColor4d(255, 255, 255, 255);
						}
					}
				}
			}	
		}
		
		
		
	}
	
	private void drawLine(final double x, final double y, final double z) {


    	final Color color = EpsilonColorUtils.getColor(1, Theme.getTheme(), 0.5f);
    	
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glLineWidth(2);
        GL11.glColor4d(255, 255, 255, 255);
        GL11.glBegin(2);
        
        
            GL11.glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
            GL11.glVertex3d(x, y, z);
    	
            
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
		
	}
}