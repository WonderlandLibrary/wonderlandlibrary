package dev.monsoon.module.implementation.combat;

import java.util.Comparator;
import java.util.stream.Collectors;

import java.util.List;

import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.module.enums.Category;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.module.base.Module;
import dev.monsoon.util.misc.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Aimbot extends Module {
	
	public float oldSens;
	
	public Timer timer = new Timer();
	
	public Aimbot() {
		super("Aimbot", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	public void onEnable() {
		oldSens = mc.gameSettings.mouseSensitivity;
	}
	
	public void onDisable() {
		mc.gameSettings.mouseSensitivity = oldSens;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			if(e.isPre()) {
		EventMotion event = (EventMotion)e;
		
		List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		
		
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < 5 && entity != mc.thePlayer && !entity.isDead && !entity.isInvisible()).collect(Collectors.toList());
		
		targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
		
		targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
		
		if(!targets.isEmpty()) {
			EntityLivingBase target = targets.get(0);
			
			
			//event.setYaw(getRotations(target)[0]);
			//event.setPitch(getRotations(target)[1]);
			
			mc.thePlayer.rotationYaw = (getRotations(target)[0]);
			mc.thePlayer.rotationPitch = (getRotations(target)[1]);
			
			//mc.thePlayer.setSprinting(false);
			
			if(timer.hasTimeElapsed(1000 / 10, true)) {
			//	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
			//	mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 1);
				}		
				}
				}
				}
		
		//if(mc.objectMouseOver.entityHit != null) {
			//mc.gameSettings.mouseSensitivity = 0.00f;
		//}
		//else
		//{
			//mc.gameSettings.mouseSensitivity = oldSens;
		//}
	}
	
	public float[] getRotations(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
		deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
		deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) -mc.thePlayer.posZ,
		distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
		
		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
		pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));
		
		if(deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}else if(deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}
		
		return new float[] {yaw, pitch};
		
	}
	
	
}
