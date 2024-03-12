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
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.events.ext.EventBlur;
import xyz.cucumber.base.events.ext.EventNametagRenderer;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.math.Convertors;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BloomUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

@ModuleInfo(category = Category.VISUALS, description = "Displays names above players", name = "Name Tags")
public class NameTagsModule extends Mod {
	
	public BooleanSettings health = new BooleanSettings("Health", true);
	
	public BooleanSettings bloom = new BooleanSettings("Shadow", true);
	
	public HashMap<EntityPlayer, PositionUtils> entities = new HashMap();
	
	
	public NameTagsModule() {
		this.addSettings(
				health,
				bloom
				);
	}
	@EventListener
	public void onRender3D(EventRender3D e) {
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
	public void onRenderNameTag(EventNametagRenderer e) {
		e.setCancelled(true);
	}
	@EventListener
	public void onBlur(EventBlur e) {
		
	}
	@EventListener
	public void onBloom(EventBloom e) {
		if(bloom.isEnabled()) {
			if(e.getType() == EventType.PRE) {
				e.setCancelled(true);
				return;
			}
			for(Entry<EntityPlayer, PositionUtils> entry : entities.entrySet()) {
				EntityPlayer player = entry.getKey();
				PositionUtils pos = entry.getValue();
				if(player.getName().equals("")) continue;
				double w = Fonts.getFont("rb-r").getWidth(player.getName())+(health.isEnabled()? Fonts.getFont("rb-r").getWidth((int)player.getHealth()+" ") :0);
				
				RenderUtils.drawRoundedRectWithCorners(pos.getX()+pos.getWidth()/2-w/2-1,  pos.getY()-10, pos.getX()+pos.getWidth()/2+w/2+1,  pos.getY()-10+8, 0xff000000,2, true, true,true,true);
			}
		}
	}
	@EventListener
	public void onRender2D(EventRenderGui e) {
		for(Entry<EntityPlayer, PositionUtils> entry : entities.entrySet()) {
			EntityPlayer player = entry.getKey();
			PositionUtils pos = entry.getValue();
			if(player.getName().equals("")) continue;
			
			GlStateManager.pushMatrix();
			
			GL11.glTranslated(pos.getX()+pos.getWidth()/2, pos.getY()-10, 0);
			
			double w = Fonts.getFont("rb-r").getWidth(player.getName())+(health.isEnabled()? Fonts.getFont("rb-r").getWidth((int)player.getHealth()+" ") :0);
			
			RenderUtils.drawRoundedRectWithCorners(-w/2-1, 0, w/2+1, 8, 0x90000000,2, true, true,true,true);
			if(health.isEnabled())Fonts.getFont("rb-r").drawString((int)player.getHealth()+"", w/2-Fonts.getFont("rb-r").getWidth((int)player.getHealth()+""), 3, ColorUtils.mix(0xffff0000, 0xff00ff00, (player.getHealth() < 0 ? 0 : player.getHealth()), player.getMaxHealth()));
			Fonts.getFont("rb-r").drawString(player.getName(), -w/2, 3, -1);
			
			GlStateManager.popMatrix();
		}
	}
	
}
