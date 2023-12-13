/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.render;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPostRenderEntity;
import me.arithmo.event.impl.EventRender3D;
import me.arithmo.event.impl.EventRenderEntity;
import me.arithmo.event.impl.EventRenderGui;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.util.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers
extends Module {

    public Tracers(ModuleData data) {
        super(data);
        this.settings.put(this.MOBS, new Setting(this.MOBS, Boolean.valueOf(true), "Draws lines to mobs."));
        this.settings.put(this.PLAYERS, new Setting(this.PLAYERS, Boolean.valueOf(false), "Draws lines to players."));

    }

    private String PLAYERS = "PLAYERS";
    private String MOBS = "MOBS";
    
    @RegisterEvent(events={EventRenderEntity.class})
    public void onEvent(Event event) {
    	GlStateManager.pushMatrix();
		mc.gameSettings.viewBobbing = false;
		for(Object theObject : mc.theWorld.loadedEntityList) {
			if(!(theObject instanceof EntityLivingBase))
				continue;
			
			EntityLivingBase entity = (EntityLivingBase) theObject;
			
			if(entity instanceof EntityMob) {
				mob(entity);
				continue;
			}
			
			
			if(entity instanceof EntityPlayer) {
				player(entity);
				continue;
			}
			
			if(entity instanceof EntityAnimal) {
				animal(entity);
				continue;
			}
			
		}
    	GlStateManager.popMatrix();

		
	}
	
	public void mob(EntityLivingBase entity) {
		if(((Boolean)((Setting)this.settings.get("MOBS")).getValue()).booleanValue()){
		float red = 0.6F;
		float green = 0.5F;
		float blue = 1.0F;
		
		double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
		double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
		double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
		
		render(red, green, blue, xPos, yPos, zPos);
		}
	}
	
	public void player(EntityLivingBase entity) {
		if((entity != mc.thePlayer) && ((Boolean)((Setting)this.settings.get("PLAYERS")).getValue()).booleanValue()){
		float red = 1F;
		float green = 0.5F;
		float blue = 0.5F;
		
		double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
		double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
		double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
		
		render(red, green, blue, xPos, yPos, zPos);
		}
	}

	public void animal(EntityLivingBase entity) {
		if(((Boolean)((Setting)this.settings.get("MOBS")).getValue()).booleanValue()){
		float red = 0.5F;
		float green = 1F;
		float blue = 0.5F;
		
		double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX;
		double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY;
		double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ;
		
		render(red, green, blue, xPos, yPos, zPos);
		}
	}
	
	
	public void render(float red, float green, float blue, double x, double y, double z) {
		RenderUtils.drawTracerLine(x, y, z, red, green, blue, 0.45F, 1.1F);
	}
}