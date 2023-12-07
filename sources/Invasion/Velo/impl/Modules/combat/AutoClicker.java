package Velo.impl.Modules.combat;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.MathUtils;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Other.other.RotationUtil;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventStrafe;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.MathHelper;

public class AutoClicker extends Module {
	
	public Timer timer = new Timer();
	
	public static NumberSetting maxcps = new NumberSetting("Max Cps", 11, 1, 100, 1);
	public static NumberSetting mincps = new NumberSetting("Min Cps", 11, 1, 100, 1);
	
	public AutoClicker() {
		super("AutoClicker", "AutoClicker", Keyboard.KEY_NONE, Category.COMBAT);
		this.loadSettings(mincps, maxcps);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onUpdate(EventUpdate event) {
		
		MathUtils random = new MathUtils();
		
		if(mc.gameSettings.keyBindAttack.pressed) {
			
			if(timer.hasTimedElapsed((long) (1000 / random.randomInt((int) mincps.getValue(), (int) maxcps.getValue())), true) && mc.thePlayer != null) {
				if(!mc.thePlayer.isUsingItem()) {
					mc.thePlayer.swingItem();
				}
				
				Entity target = mc.objectMouseOver.entityHit;
				
				if(mc.thePlayer != null && target != null && !mc.thePlayer.isUsingItem()) {
					mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
				}
			}
		}
	}
	
	public void onPreMotionUpdate(EventPreMotion event) {
		List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < 7 && entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());
		targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
		
		if(!targets.isEmpty()) {
			EntityLivingBase target = (EntityLivingBase) targets.get(0);
		}
		
		
	}
	
	public float[] getKaRotations(Entity e) {
    	double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
    		   deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
    		   deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
    		   distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ,2));
    	
    	float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
    		  pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));
    				   
    	
    	
    	if (pitch > 90) {
    	    pitch = 90F;
    	} else if (pitch < -90) {
    	    pitch = -90F;
    	}
    	
    	
    	if(deltaX < 0 && deltaZ < 0 ) {
    		yaw = (float) (90 +Math.toDegrees(Math.atan(deltaZ / deltaX)));
    	}else if(deltaX > 0 && deltaZ < 0) {
    		yaw = (float) (-90 +Math.toDegrees(Math.atan(deltaZ / deltaX)));
    		
    	}
    			
    	return new float[] { yaw, pitch };
    }
	
	public void onStrafe(EventStrafe event) {
		
	}
	
	
	
	
}
