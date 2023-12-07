package Velo.modules.impl.movement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Ninja extends Module {
	
	public NumberSetting range = new NumberSetting("Range", 3, 0, 500, 0.01);
	public ModeSetting mode = new ModeSetting("Range", "TeleportAura", "TeleportAura", "PacketAura", "SilentAura");
	
	public Ninja() {
		super("Ninja", "Ninja", Keyboard.KEY_NONE, Category.MOVEMENT);	
		this.loadSettings(mode, range);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1F;
	}
	
	public void onUpdate(EventUpdate event) {
		this.setDisplayName("Ninja " + mode.modes.get(mode.index));
		if(mc.theWorld != null && mc.thePlayer != null) {
			List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());
			
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
			
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
			
			mc.playerController.extendedReach();
			
			if(!targets.isEmpty() && !mc.thePlayer.isDead && mc.thePlayer != null && mc.theWorld != null) {
				
				mc.thePlayer.motionY = 0;
				
				EntityLivingBase target = (EntityLivingBase) targets.get(0);
				
				if(mode.equalsIgnorecase("TeleportAura")) {
					mc.timer.timerSpeed = 1F;
					mc.thePlayer.setPosition(target.posX, target.posY, target.posZ);
				}
				if(mode.equalsIgnorecase("SilentAura")) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(target.posX, target.posY, target.posZ, mc.thePlayer.onGround));
				}
			}
		}
	}
	
	public void onEventSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C03PacketPlayer) {
			if(mc.theWorld != null && mc.thePlayer != null) {
				List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
				targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());
				
				targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
				
				targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
				
				mc.playerController.extendedReach();
				
				C03PacketPlayer c03 = (C03PacketPlayer) event.getPacket();
				
				if(!targets.isEmpty() && !mc.thePlayer.isDead && mc.thePlayer != null && mc.theWorld != null) {
					
					EntityLivingBase target = (EntityLivingBase) targets.get(0);
					
					if(mode.equalsIgnorecase("SilentAura")) {
						//mc.thePlayer.onGround = true;
						if(mc.thePlayer.posX < target.posX) {
							for(double i = mc.thePlayer.posX; i < target.posX; i++) {
								c03.x = i;
								mc.thePlayer.setPosition(i, mc.thePlayer.posY, mc.thePlayer.posZ);
							}
						}
						if(mc.thePlayer.posX > target.posX) {
							for(double i = mc.thePlayer.posX; i > target.posX; i--) {
								c03.x = i;
								mc.thePlayer.setPosition(i, mc.thePlayer.posY, mc.thePlayer.posZ);
							}
						}
						if(mc.thePlayer.posZ < target.posZ) {
							for(double i = mc.thePlayer.posZ; i < target.posZ; i++) {
								c03.z = i;
								mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, i);
							}
						}
						if(mc.thePlayer.posZ > target.posZ) {
							for(double i = mc.thePlayer.posZ; i > target.posZ; i--) {
								c03.z = i;
								mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, i);
							}
						}
						if(mc.thePlayer.posY < target.posY) {
							for(double i = mc.thePlayer.posY; i < target.posY; i++) {
								c03.z = i;
								mc.thePlayer.setPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ);
							}
						}
						if(mc.thePlayer.posY > target.posY) {
							for(int i = (int) mc.thePlayer.posY; i > target.posY; i--) {
								c03.y = i;
								mc.thePlayer.setPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ);
							}
						}
						//c03.x = target.posX;
						//c03.y = target.posY;
						//c03.z = target.posZ;
						//c03.x = mc.thePlayer.posX;
						//c03.y = mc.thePlayer.posY;
						//c03.z = mc.thePlayer.posZ;
					}
				}
			}
		}
	}
}
