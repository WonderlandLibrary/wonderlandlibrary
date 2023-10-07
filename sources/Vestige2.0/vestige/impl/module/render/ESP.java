package vestige.impl.module.render;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.Config;
import net.minecraft.util.AxisAlignedBB;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.Render3DEvent;
import vestige.api.event.impl.RenderEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.util.render.RenderUtils;

@ModuleInfo(name = "ESP", category = Category.RENDER)
public class ESP extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", this, "2D", "2D", "Box", "Minecraft Outline");
	private final BooleanSetting renderInvisibles = new BooleanSetting("Render invisibles", this, false);
	
	public ESP() {
		this.registerSettings(mode);
	}
	
	public void onEnable() {
		if (mode.is("Minecraft Outline")) {
			if (Config.isFastRender()) {
				Vestige.getInstance().addChatMessage("You need to disable fast render to use minecraft outline ESP !");
				this.setEnabled(false);
				return;
			}
		}
	}
	
	@Listener
	public void onRender(RenderEvent event) {
		switch (mode.getMode()) {
			case "2D":
				HUD hud = (HUD) Vestige.getInstance().getModuleManager().getModule(HUD.class);
                int color = hud.getCurrentColor().getRGB();
                
                float partialTicks = event.getPartialTicks();
				
				mc.theWorld.getLoadedEntityList().forEach(entity -> {
		            if (RenderUtils.isInViewFrustrum(entity) && entity != mc.thePlayer && entity instanceof EntityPlayer && (!entity.isInvisible() || renderInvisibles.isEnabled())) {
		                EntityPlayer player = (EntityPlayer) entity;
		                
		                double x = interpolate(entity.posX, entity.prevPosX, partialTicks);
		                double y = interpolate(entity.posY, entity.prevPosY, partialTicks);
		                double z = interpolate(entity.posZ, entity.prevPosZ, partialTicks);
		                
		                double width = entity.width / 1.5;
		                double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
		                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
		                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
		                mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
		                Vector4d position = null;
		                for (Vector3d vector : vectors) {
		                    vector = RenderUtils.project(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
		                    if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
		                        if (position == null) {
		                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
		                        }
		                        position.x = Math.min(vector.x, position.x);
		                        position.y = Math.min(vector.y, position.y);
		                        position.z = Math.max(vector.x, position.z);
		                        position.w = Math.max(vector.y, position.w);
		                    }
		                }
		                
		                mc.entityRenderer.setupOverlayRendering();
		                
		                if (position != null && player.getDistanceToEntity(mc.thePlayer) > 2) {
		                    GL11.glPushMatrix();

		                    RenderUtils.drawBorderedRectOther(position.x - 1.5, position.y - 0.5, position.z - position.x + 4.0, position.w - position.y + 1.0, 1.5f, color, 0);
		                    RenderUtils.drawBorderedRectOther(position.x - 1, position.y, position.z - position.x + 3, position.w - position.y, 0.5f, color, 0);
		                    GL11.glPopMatrix();
		                    
		                    /*
		                    if (player.getHealth() > 0.0F && player.getHealth() <= 40F) {
		                        GL11.glPushMatrix();
		                        GlStateManager.enableBlend();
		                        double hpPercentage = player.getHealth() / player.getMaxHealth();
		                        if (hpPercentage > 1)
		                            hpPercentage = 1;
		                        else if (hpPercentage < 0)
		                            hpPercentage = 0;

		                        double offset = position.w - position.y;
		                        double percentoffset = offset / player.getMaxHealth();
		                        double finalnumber = percentoffset * player.getHealth();
		                        
		                        GlStateManager.disableBlend();
		                        GL11.glPopMatrix();

		                    }
		                    */
		                }

		            }
		        });
				break;
		}
	}
	
	@Listener
	public void onRender3D(Render3DEvent event) {
		switch (mode.getMode()) {
			case "Box":
				HUD hud = (HUD) Vestige.getInstance().getModuleManager().getModule(HUD.class);
				Color color = hud.getCurrentColor();
				
				RenderManager rm = mc.getRenderManager();
				float partialTicks = event.getPartialTicks();
				
				mc.theWorld.getLoadedEntityList().stream().filter(entity ->
				entity != mc.thePlayer &&
				(!(entity.isInvisible() || entity.isInvisibleToPlayer(mc.thePlayer)) || renderInvisibles.isEnabled()) &&
				entity instanceof EntityPlayer
					).forEach(entity -> {
						GL11.glBlendFunc(770, 771);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glLineWidth(3.25F);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glDepthMask(false);
						
						GL11.glColor4d(color.getRed() / 255.0D, color.getGreen() / 255.0D, color.getBlue() / 255.0D, 0.8F);
						
						AxisAlignedBB bb = entity.boundingBox;
	
						double posX = interpolate(entity.posX, entity.lastTickPosX, partialTicks);
						double posY = interpolate(entity.posY, entity.lastTickPosY, partialTicks);
						double posZ = interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
	
						RenderGlobal.func_181561_a(
							new AxisAlignedBB(
								bb.minX - 0.05 - entity.posX + (posX - rm.renderPosX),
								bb.minY - 0.05 - entity.posY + (posY - rm.renderPosY),
								bb.minZ - 0.05 - entity.posZ + (posZ - rm.renderPosZ),
								bb.maxX + 0.05 - entity.posX + (posX - rm.renderPosX),
								bb.maxY + 0.1 - entity.posY + (posY - rm.renderPosY),
								bb.maxZ + 0.05 - entity.posZ + (posZ - rm.renderPosZ)
							)
						);
						
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDepthMask(true);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glColor4d(1, 1, 1, 1);
					});
				break;
		}
	}
	
	private double interpolate(double current, double old, double scale) {
		return (old + (current - old) * scale);
	}
	
}