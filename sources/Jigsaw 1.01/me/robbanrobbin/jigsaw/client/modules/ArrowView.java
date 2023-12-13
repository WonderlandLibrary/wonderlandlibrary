package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PreFrameBufferEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.util.math.Vec3d;

public class ArrowView extends Module {
	
	public static boolean enabled = true;
	
	private boolean render = false;
	
	public ArrowView() {
		super("ArrowView", Keyboard.KEY_NONE, Category.RENDER, "Shows a window of what the arrow sees");
	}
	
	@Override
	public void onEnable() {
		Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Turn on Projectiles along with ArrowView for better functionality!"));
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Jigsaw.getFramebufferHandler().getToggleableFramebufferByName(this.getName()).setEnabled(false);
		super.onDisable();
	}
	
	@Override
	public void onClientLoad() {
		Jigsaw.getFramebufferHandler().newFramebuffer(this, this.getName(), mc.displayWidth, mc.displayHeight, true);
		Jigsaw.getFramebufferHandler().getToggleableFramebufferByName(this.getName()).setEnabled(false);
		super.onClientLoad();
	}
	
	@Override
	public void onResize(int width, int height) {
		Jigsaw.getFramebufferHandler().updateFramebuffer(this.getName(), mc.displayWidth, mc.displayHeight, true);
		super.onResize(width, height);
	}
	
	@Override
	public void onLoadWorld() {
		super.onLoadWorld();
		Jigsaw.getFramebufferHandler().getToggleableFramebufferByName(this.getName()).setEnabled(true);
	}
	
	@Override
	public void onUpdate() {
		render = Utils.isPlayerHoldingItem(Items.BOW);
		super.onUpdate();
	}
	
	@Override
	public void preFrameBuffer(PreFrameBufferEvent event) {
		
		if(mc.currentScreen != null) {
			event.cancel();
			return;
		}
		
		EntityArrow arrow = null;
		for(Entity ent : mc.world.loadedEntityList) {
			if(ent instanceof EntityArrow && ((EntityArrow)ent).shootingEntity != null && ((EntityArrow)ent).shootingEntity instanceof EntityPlayerSP && !((EntityArrow)ent).inGround) {
				arrow = (EntityArrow) ent;
			}
		}
		
		if(arrow != null) {
			mc.setRenderViewEntity(arrow);
			return;
		}
		
		if(!render) {
			event.cancel();
			return;
		}
		
		if(arrow == null) {
			if(!Jigsaw.getModuleByName("Projectiles").isToggled()) {
				event.cancel();
				return;
			}
			Vec3d position = Projectiles.lastArrowLandingPosition.addVector(0, 0, 0);
			Jigsaw.getFramebufferHandler().setOverride(position.x, position.y, position.z, -Projectiles.lastArrowYaw, -Projectiles.lastArrowPitch);
		}
		
		super.preFrameBuffer(event);
	}

	@Override
	public void postFrameBuffer() {
		
		mc.setRenderViewEntity(mc.player);
		
		super.postFrameBuffer();
	}
	
}
