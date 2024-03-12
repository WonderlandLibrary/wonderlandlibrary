package xyz.cucumber.base.module.feat.visuals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.math.Convertors;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.StencilUtils;

@ModuleInfo(category = Category.VISUALS, description = "Allows you see players behind obsticles", name = "ESP")
public class ESPModule extends Mod{
	
	public BooleanSettings filled = new BooleanSettings("Filled", true);
	
	public ColorSettings filledColor = new ColorSettings("Filled Color", "Static", 0xff000000, -1, 40);
	
	public ModeSettings outline = new ModeSettings("Outline", new String[] {
			"None", "Full", "Brackets", "Arrows"
	});
	
	public ColorSettings outlineColor = new ColorSettings("Outline Color", "Static", -1, -1, 100);
	
	public BooleanSettings healthBar = new BooleanSettings("Health Bar", true);
	
	public HashMap<EntityPlayer, PositionUtils> entities = new HashMap();
	
	private double start;
	
	public ESPModule() {
		this.addSettings(
				filled,
				filledColor,
				outline,
				outlineColor,
				healthBar
				);
	}
	public void onEnable() {
		start = System.nanoTime() /1000000;
	}

	@EventListener
	public void onRender3d(EventRender3D e) {
		entities.clear();
		for(Entity entity : mc.theWorld.loadedEntityList) {
			if(entity == mc.thePlayer || (mc.thePlayer.getDistanceToEntity(entity) < 1 && mc.gameSettings.thirdPersonView == 0) ||
					!(entity instanceof EntityPlayer) || !RenderUtils.isInViewFrustrum(entity)) continue;
			
			double x = (entity.prevPosX + (entity.posX - entity.prevPosX) * e.getPartialTicks()) - mc.getRenderManager().viewerPosX;
			double y = (entity.prevPosY + (entity.posY - entity.prevPosY) * e.getPartialTicks()) - mc.getRenderManager().viewerPosY;
			double z = (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * e.getPartialTicks()) - mc.getRenderManager().viewerPosZ;
			double width = entity.width/2.5;
            AxisAlignedBB bb = new AxisAlignedBB(x - width, y, z - width,x + width, y + entity.height, z + width).expand(0.2, 0.1, 0.2);
            
            List<double[]> vectors = Arrays.asList(
        		   new double[] {bb.minX, bb.minY, bb.minZ},
        		   new double[] {bb.minX, bb.maxY, bb.minZ},
        		   new double[] {bb.minX, bb.maxY, bb.maxZ},
        		   new double[] {bb.minX, bb.minY, bb.maxZ},
        		   new double[] {bb.maxX, bb.minY, bb.minZ},
        		   new double[] {bb.maxX, bb.maxY, bb.minZ},
        		   new double[] {bb.maxX, bb.maxY, bb.maxZ},
        		   new double[] {bb.maxX, bb.minY, bb.maxZ}
        		   );

            double[] position = new double[]{Float.MAX_VALUE, Float.MAX_VALUE, -1.0F, -1.0F};

            for (double[] vec : vectors) {
            	float[] points = Convertors.convert2D((float) vec[0], (float) vec[1], (float) vec[2], new ScaledResolution(mc).getScaleFactor());
                if (points != null && points[2] >= 0.0F && points[2] < 1.0F) {
                    final float pX = points[0];
                    final float pY = points[1];
                    position[0] = Math.min(position[0], pX);
                    position[1] = Math.min(position[1], pY);
                    position[2] = Math.max(position[2], pX);
                    position[3] = Math.max(position[3], pY);
                }
            }
            
            entities.put((EntityPlayer)entity, new PositionUtils(position[0], position[1], position[2]-position[0], position[3]-position[1], 1));
		}
	}
	
	@EventListener
	public void onRenderGui(EventRenderGui e) {
		GlStateManager.pushMatrix();
		for(Entry<EntityPlayer, PositionUtils> entry : entities.entrySet()) {
			EntityPlayer player = entry.getKey();
			PositionUtils pos = entry.getValue();
			
			GlStateManager.pushMatrix();
			if(filled.isEnabled()) {
				RenderUtils.drawRect(pos.getX(), pos.getY(), pos.getX2(), pos.getY2(), ColorUtils.getColor(filledColor,System.nanoTime()/1000000, 1, 5));
			}
			
			renderOutline(pos);
			
			
			if(healthBar.isEnabled()) {
				renderHealth(pos.getX()-2, pos.getY(), pos.getHeight(), 0x90000000, 3);
				renderHealth(pos.getX()-2, pos.getY(), (pos.getHeight()) / mc.thePlayer.getMaxHealth() * mc.thePlayer.getHealth(), ColorUtils.mix(0xffff0000, 0xff00ff00, mc.thePlayer.getMaxHealth(), mc.thePlayer.getHealth() < 0 ? mc.thePlayer.getMaxHealth() : mc.thePlayer.getHealth()), 2.5f);
			}

			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
	public void renderOutline(PositionUtils pos) {
		switch(outline.getMode().toLowerCase()) {
		case "full":
			RenderUtils.drawOutlinedRect(pos.getX(), pos.getY(), pos.getX2(), pos.getY2(), 0x90000000, 1.6f);
			break;
		case "brackets":
			double bw = pos.getWidth()/4;
			renderBracket(new double [] {pos.getX(), pos.getY()}, new double [] {pos.getX(), pos.getY2()}, bw+0.3, 0x90000000, 1.6);

			
			renderBracket(new double [] {pos.getX2(), pos.getY()}, new double [] {pos.getX2(), pos.getY2()}, -bw-0.3, 0x90000000, 1.6);

			break;
		case "arrows":
			double aw = pos.getWidth()/4;
			renderBracket(pos.getX()+aw-0.3, pos.getY(),pos.getX(), pos.getY(),pos.getX(), pos.getY()+aw+0.3, 0x90000000, 1.6);

			renderBracket(pos.getX2()-aw-0.3, pos.getY(),pos.getX2(), pos.getY(),pos.getX2(), pos.getY()+aw+0.3, 0x90000000, 1.6);

			
			renderBracket(pos.getX(), pos.getY2()-aw-0.3, pos.getX(), pos.getY2(),pos.getX()+aw+0.3, pos.getY2(), 0x90000000, 1.6);

			
			renderBracket(pos.getX2(), pos.getY2()-aw-0.3, pos.getX2(), pos.getY2(),pos.getX2()-aw-0.3, pos.getY2(), 0x90000000, 1.6);

			
			break;
		}
		if(!outline.getMode().toLowerCase().equals("none")) {
			StencilUtils.initStencil();
            GL11.glEnable(GL11.GL_STENCIL_TEST);
            StencilUtils.bindWriteStencilBuffer();
            
            switch(outline.getMode().toLowerCase()) {
			case "full":
				 RenderUtils.drawOutlinedRect(pos.getX(), pos.getY(), pos.getX2(), pos.getY2(), -1, 1);
				break;
			case "brackets":
				double bw = pos.getWidth()/4;

				renderBracket(new double [] {pos.getX(), pos.getY()}, new double [] {pos.getX(), pos.getY2()}, bw, -1, 1);
				
				renderBracket(new double [] {pos.getX2(), pos.getY()}, new double [] {pos.getX2(), pos.getY2()}, -bw, -1, 1);
				break;
			case "arrows":
				double aw = pos.getWidth()/4;

				renderBracket(pos.getX()+aw, pos.getY(),pos.getX(), pos.getY(),pos.getX(), pos.getY()+aw, -1, 1);
				

				renderBracket(pos.getX2()-aw, pos.getY(),pos.getX2(), pos.getY(),pos.getX2(), pos.getY()+aw, -1, 1);
				

				renderBracket(pos.getX(), pos.getY2()-aw, pos.getX(), pos.getY2(),pos.getX()+aw, pos.getY2(), -1, 1);

				renderBracket(pos.getX2(), pos.getY2()-aw, pos.getX2(), pos.getY2(),pos.getX2()-aw, pos.getY2(), -1, 1);
				
				break;
			}
    		
    		StencilUtils.bindReadStencilBuffer(1);
    		
    		GL11.glPushMatrix();
    		
    		RenderUtils.start2D();
    		
    		GL11.glShadeModel(GL11.GL_SMOOTH);
    		
    		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
    		GL11.glVertex2d(pos.getX()+pos.getWidth()/2, pos.getY()+pos.getHeight()/2);
    		
    		for(double z = 0; z <=360; z+=5) {
    			RenderUtils.color(ColorUtils.getColor(outlineColor, System.nanoTime()/1000000, z, 5));
    			GL11.glVertex2d(pos.getX()+pos.getWidth()/2+Math.sin(z * Math.PI /180)* Math.sqrt(pos.getWidth()*pos.getWidth()+ pos.getHeight()* pos.getHeight())/2, pos.getY()+pos.getHeight()/2-Math.cos(z * Math.PI /180)*Math.sqrt(pos.getWidth()*pos.getWidth()+ pos.getHeight()* pos.getHeight())/2);
    		}
    		GL11.glEnd();
    		RenderUtils.stop2D();
    		GlStateManager.resetColor();
    		
    		GL11.glPopMatrix();
    		
    		StencilUtils.uninitStencilBuffer();
		}
	}
	
	public void renderHealth(double x, double y, double width,int color, float player) {
		GlStateManager.pushMatrix();
		RenderUtils.start2D();
		RenderUtils.color(color);
		GL11.glLineWidth(player);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y+width);
		
		GL11.glEnd();
		GL11.glLineWidth(1);
		RenderUtils.color(0xffffffff);
		RenderUtils.stop2D();
		GlStateManager.popMatrix();
	}
	public void renderBracket(double[] pos1, double[] pos2, double width, int color, double size) {
		GlStateManager.pushMatrix();
		RenderUtils.start2D();
		RenderUtils.color(color);
		GL11.glLineWidth((float) size);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		
		GL11.glVertex2d(pos1[0]+width, pos1[1]);
		GL11.glVertex2d(pos1[0], pos1[1]);
		GL11.glVertex2d(pos2[0], pos2[1]);
		GL11.glVertex2d(pos2[0]+width,  pos2[1]);
		
		GL11.glEnd();
		RenderUtils.color(0xffffffff);
		RenderUtils.stop2D();
		GlStateManager.popMatrix();
	}
	
	public void renderBracket(double x, double y, double x1, double y1, double x2, double y2, int color, double size) {
		GlStateManager.pushMatrix();
		RenderUtils.start2D();
		RenderUtils.color(color);
		GL11.glLineWidth((float) size);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x2, y2);
		
		GL11.glEnd();
		RenderUtils.color(0xffffffff);
		RenderUtils.stop2D();
		GlStateManager.popMatrix();
	}
}
