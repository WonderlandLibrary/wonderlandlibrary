package epsilon.modules.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import epsilon.Epsilon;
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

public class TargetLines2 extends Module {

	private int offset = 0;
	private Entity t = null;
	private static final ShapeUtils render = new ShapeUtils();
	private List<Vec3> posUpdates = new ArrayList<Vec3>();
	
	public BooleanSetting d = new BooleanSetting ("InWorld", false);
	public BooleanSetting r = new BooleanSetting ("Round", false);

	public NumberSetting w = new NumberSetting ("Width", 3, 0.5, 25, 0.5);
	
	public TargetLines2() {
		super("TargetLines", Keyboard.KEY_NONE, Category.RENDER, "Lines on target, hence the name");
		this.addSettings(w, d, r);
	}
	
	public void onEnable() {
		posUpdates.clear();	
	}
	
	public void onEvent(Event e) {
		
		if(e instanceof EventUpdate) {
			this.displayInfo = (d.isEnabled() ? "InWorld" : "Overlay") + (t!=null ? " : "+ t.getName() : "") + (r.isEnabled() ? " : BlockAligned" : "");
			if(KillAura.target!=null) {
				
				if(t!=KillAura.target) {
					posUpdates.clear();
				}
				
				t = KillAura.target;
			}
			if(t!=null) {
				posUpdates.add(new Vec3(t.posX, t.posY, t.posZ));
				if(t.isDead || mc.thePlayer.getDistanceToEntity(t) > 64) {
					t = null;

					posUpdates.clear();
				}
			}
		}
		
		if(e instanceof EventRender3d) {
			if(t!=null) {
				int i = 0;
				int off = 2;
				for(Vec3 p : posUpdates) {

					if(i>0) {
						double x, y, z, lx, ly, lz;
						x = p.xCoord; y = p.yCoord; z = p.zCoord;
						lx = posUpdates.get(i-1).xCoord; lz = posUpdates.get(i-1).zCoord; ly = posUpdates.get(i-1).yCoord;
						
						if(r.isEnabled()) {
							if(y>ly)  { 
								Math.ceil(y); 
								Math.floor(ly);
							}else { 
								Math.ceil(ly);
								Math.floor(y);
							}
						}
						
						if(lx!=x || lz!=z || ly!=y) off++;
						
						drawLine((int) w.getValue(), d.isEnabled(), x, lx, z, lz, y, ly, off/50);
					}
					i++;
				}
			}
		}
	}
	
	public static void drawLine(final int i, boolean d, final double x1, final double x2, final double z1, final double z2, final double y, final double y2, final int offset) {
		

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
        render.color(EpsilonColorUtils.getColor(offset, Theme.getTheme(), 0.5f));
        GL11.glBegin(2);
    	 


        
        	double yy = (y + (y - y) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
        	double yy2 = (y2 + (y2 - y2) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
        

        	double xf1 = (x1 + (x1 - x1) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
        	double xf2 = (x2 + (x2 - x2) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;

        	double zf1 = (z1 + (z1 - z1) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        	double zf2 = (z2 + (z2 - z2) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
        	
        	
        	
            GL11.glVertex3d(xf1, yy, zf1);
            GL11.glVertex3d(xf2, yy2, zf2);


        		
        		
        		
        	
            
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if(!d) GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
		
	}
	
}
