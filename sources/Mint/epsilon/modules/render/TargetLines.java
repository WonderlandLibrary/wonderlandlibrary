package epsilon.modules.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import epsilon.events.Event;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.render.EventRender3d;
import epsilon.modules.Module;
import epsilon.modules.combat.KillAura;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.ShapeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TargetLines extends Module {

	private int offset = 0;
	private Entity t = null;
	private static final ShapeUtils render = new ShapeUtils();
	private List<Vec3[]> posUpdates = new ArrayList<Vec3[]>();
	
	public BooleanSetting full = new BooleanSetting ("FullBox", false);
	public BooleanSetting d = new BooleanSetting ("InWorld", false);

	public NumberSetting w = new NumberSetting ("Width", 3, 0.5, 25, 0.5);
	
	public TargetLines() {
		super("TargetBox", Keyboard.KEY_NONE, Category.RENDER, "Box on target, hence the name");
		this.addSettings(full, w, d);
	}
	
	public void onEnable() {
		posUpdates.clear();	
	}
	
	public void onEvent(Event e) {
		
		if(e instanceof EventUpdate) {
			this.displayInfo = (d.isEnabled() ? "InWorld" : "Overlay") + (t!=null ? " : "+ t.getName() : "");
			if(KillAura.target!=null) {
				t = KillAura.target;
			}
			if(t!=null) {
				if(t.isDead || mc.thePlayer.getDistanceToEntity(t) > 12) {
					t = null;
				}
			}
		}
		
		if(e instanceof EventRender3d) {
			if(t!=null) {

				drawTargetBox(t, (int) w.getValue(), full.isEnabled(), d.isEnabled());
				
			}
		}
	}
	
	public static void drawTargetBox(final Entity t, final int i, boolean filled, boolean d) {
		

    	final Color color = EpsilonColorUtils.getColor(1, Theme.getTheme(), 1.5f);
    	
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if(!d) GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glLineWidth(i);
        render.color(EpsilonColorUtils.getColor(1, Theme.getTheme(), 0.5f));
        GL11.glBegin(2);
    	 
        if(filled) {
		        double y = (t.lastTickPosY + (t.posY - t.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
		        boolean b = false;
		        double bX;
		        double bZ;
		        double mX;
		        double mZ;
		        double bY, bYY;
		        y+=0.1;
		        
		        AxisAlignedBB axis = t.getEntityBoundingBox();
		        
	        	
	        	bX = axis.maxX+0.5;
	        	bX = (bX + (bX- bX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
	        	bZ = axis.maxZ+0.5;
	        	bZ = (bZ + (bZ- bZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
	        	mX = axis.minX-0.5;
	        	mX = (mX + (mX- mX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
	        	mZ = axis.minZ-0.5;
	        	mZ = (mZ + (mZ- mZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
	        	bY = axis.maxY;
	        	bY = (bY + (bY- bY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
	        	bYY = axis.maxY - (axis.maxY-axis.minY);
	        	bYY = (bYY + (bYY- bYY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
	        	
	        

	        	GL11.glVertex3d(bX, y, bZ);
                GL11.glVertex3d(mX, y, bZ);
                GL11.glVertex3d(mX, y, mZ);
                GL11.glVertex3d(bX, y, mZ);

	        	GL11.glVertex3d(bX, y, bZ);
	        	GL11.glVertex3d(bX, bY, bZ);
	        	GL11.glVertex3d(bX, y, bZ);
                

	        	GL11.glVertex3d(mX, y, bZ);
	        	GL11.glVertex3d(mX, bY, bZ);
	        	GL11.glVertex3d(mX, y, bZ);
                GL11.glVertex3d(mX, y, mZ);
                

	        	GL11.glVertex3d(mX, y, mZ);
	        	GL11.glVertex3d(mX, bY, mZ);
                GL11.glVertex3d(mX, y, mZ);

	        	GL11.glVertex3d(bX, y, mZ);
	        	GL11.glVertex3d(bX, bY, mZ);
                GL11.glVertex3d(bX, y, mZ);

                GL11.glVertex3d(bX, y, bZ);
                GL11.glVertex3d(bX, bY, bZ);
                GL11.glVertex3d(mX, bY, bZ);
                GL11.glVertex3d(mX, bY, mZ);
                GL11.glVertex3d(bX, bY, mZ);
                GL11.glVertex3d(bX, bY, bZ);

                
                
                // bX bZ, mX bZ, mX mZ, bX, mZ
        }else {

	        double y = (t.lastTickPosY + (t.posY - t.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
	        
	        double bX;
	        double bZ;
	        double mX;
	        double mZ;
	        
	        AxisAlignedBB axis = t.getEntityBoundingBox();
	        
        	
        	bX = axis.maxX+0.5;
        	bX = (bX + (bX- bX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        	bZ = axis.maxZ+0.5;
        	bZ = (bZ + (bZ- bZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        	mX = axis.minX-0.5;
        	mX = (mX + (mX- mX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        	mZ = axis.minZ-0.5;
        	mZ = (mZ + (mZ- mZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        	y+=0.2;

        	GL11.glVertex3d(bX, y, bZ);
            GL11.glVertex3d(mX, y, bZ);
            GL11.glVertex3d(mX, y, mZ);
            GL11.glVertex3d(bX, y, mZ);
        	
        }


        		
        		
        		
        	
            
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if(!d) GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
		
	}
	
	/*
	public void onEvent(Event e) {
		if(e instanceof EventRender3d && KillAura.target!=null ) {
			t = KillAura.target;
	
			int i = 0;
			
			for(Vec3[] v : posUpdates) {

				drawVecPath(v);
				i++;
			}
	        
	        GL11.glColor4d(255, 255, 255, 255);
			
		}
		
		if(e instanceof EventUpdate && KillAura.target!=null) {

			t = KillAura.target;
			
			if(posUpdates.size()>55) {
				posUpdates.remove(0);
			}
			
			
				double pX, pY, pZ, pX2, pY2, pZ2;
				
				
		        pX = (t.lastTickPosX + (t.posX - t.lastTickPosX) * (double)mc.timer.renderPartialTicks) - t.posX - (mc.thePlayer.posX-t.posX);
		        pY = (t.lastTickPosY + (t.posY - t.lastTickPosY) * (double)mc.timer.renderPartialTicks) - mc.getRenderManager().viewerPosY;
		        pZ = (t.lastTickPosZ + (t.posZ - t.lastTickPosZ) * (double)mc.timer.renderPartialTicks) - t.posZ - (mc.thePlayer.posZ-t.posZ);
	
		        pX2 = (t.posX + (t.lastTickPosX - t.posX) * (double)mc.timer.renderPartialTicks) - t.posX - (mc.thePlayer.posX-t.posX);
		        pZ2 = (t.posZ + (t.lastTickPosZ - t.posZ) * (double)mc.timer.renderPartialTicks) - t.posZ - (mc.thePlayer.posZ-t.posZ);
		        
		        Math.floor(pX2); Math.floor(pX);
		        Math.round(pY);
		        Math.floor(pZ2); Math.floor(pZ);


		        Vec3 a = new Vec3(Math.ceil(pX), Math.floor(pY), Math.ceil(pZ));
		        
		        
		        
		        Vec3 b = new Vec3(Math.floor(pX2), Math.floor(pY), Math.floor(pZ2));
		        
		        if(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) > 90) {
		        	a.xCoord+=1;
		        	a.zCoord-=0.5;
		        	b.zCoord+=0.5;
		        }else if (MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) > 0) {
		        	a.xCoord-=1;
		        	a.zCoord-=1;
		        }else if (MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) > -90) {
		        	a.xCoord+=2;
		        	a.zCoord-=0.75f;
		        }else {
		        	a.xCoord-=1;
		        	a.zCoord+=1;
		        }
		        
		        Vec3[] c = {a,b};
		        	        
				posUpdates.add(c);
			
			
		}
		if(KillAura.target==null && !posUpdates.isEmpty()) posUpdates.clear();
	}
    
    public static void drawVecPath(final Vec3[] v) {

    	final Color color = EpsilonColorUtils.getColor(1, Theme.getTheme(), 0.5f);
    	
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glLineWidth(2);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 255);
        GL11.glBegin(2);
        
        
        	GL11.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
    	
            GL11.glVertex3d(v[0].xCoord, v[0].yCoord, v[0].zCoord);
            GL11.glVertex3d(v[1].xCoord, v[1].yCoord, v[1].zCoord);
    	
            
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }*/

}
