package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.events.ext.EventRenderRotation;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.math.RotationUtils;
import xyz.cucumber.base.module.Mod;

@ModuleInfo(category = Category.COMBAT, description = "Automatically aims at player with bow", name = "Bow Aim Bot", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)
public class BowAimBotModule extends Mod {
	
	public KillAuraModule ka;
    public Timer timer = new Timer();
    public EntityLivingBase target;
    public boolean cancel;
    
    public float velocity;
    
    public void onEnable()
    {
        ka = (KillAuraModule)Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
    }
    public void onDisable()
    {
        RotationUtils.customRots = false;
    }
    
    @EventListener
    public void onUpdate(EventUpdate e) {
    	target = EntityUtils.getTarget(80, ka.Targets.getMode(), "Single", (int) 400, Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(), ka.TroughWalls.isEnabled(), ka.attackInvisible.isEnabled(), ka.attackDead.isEnabled());
        if(mc.thePlayer.getHeldItem() == null) {
        	target = null;
        	if(cancel) {
        		RotationUtils.customRots = false;
        		cancel = false;
        	}
        	return;
        }
        if(mc.thePlayer.getHeldItem().getItem() == null) {
        	target = null;
        	if(cancel) {
        		RotationUtils.customRots = false;
        		cancel = false;
        	}
        	return;
        }
        if(!(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
        	target = null;
        	if(cancel) {
        		RotationUtils.customRots = false;
        		cancel = false;
        	}
        	return;
        }
        
        if(!mc.thePlayer.isUsingItem()) {
        	target = null;
        	if(cancel) {
        		RotationUtils.customRots = false;
        		cancel = false;
        	}
        	return;
        }
        
        if(mc.thePlayer.getItemInUseDuration() <= 1) {
        	target = null;
        	if(cancel) {
        		RotationUtils.customRots = false;
        		cancel = false;
        	}
        	return;
        }
        
        if(target == null)return;
        cancel = true;
        
        float rots[] = getBowRotations(target);
        
        RotationUtils.customRots = true;
        RotationUtils.serverYaw = rots[0];
        RotationUtils.serverPitch = rots[1];
    }
    
    @EventListener
    public void onMotion(EventMotion e) {
    	if(target != null) {
        	e.setYaw(RotationUtils.serverYaw);
    		e.setPitch(RotationUtils.serverPitch);
        }
    }
    
    @EventListener
    public void onMove(EventMoveFlying e) {
    	if(target != null) {
        	e.setYaw(RotationUtils.serverYaw);
        }
    }
    
    @EventListener
    public void onJump(EventJump e) {
    	if(target != null) {
        	e.setYaw(RotationUtils.serverYaw);
        }
    }
    
    @EventListener
    public void onLook(EventLook e) {
    	if(target != null) {
        	e.setYaw(RotationUtils.serverYaw);
    		e.setPitch(RotationUtils.serverPitch);
        }
    }
    
    @EventListener
    public void onRotation(EventRenderRotation e) {
    	if(target != null) {
        	e.setYaw(RotationUtils.serverYaw);
    		e.setPitch(RotationUtils.serverPitch);
        }
    }
    
public float[] getBowRotations(EntityLivingBase entity) {
    	
    	// set velocity
		velocity = (72000 - mc.thePlayer.getItemInUseCount()) / 20F;
		velocity = (velocity * velocity + velocity * 2) / 3;
		if(velocity > 1)
			velocity = 1;
		
		// set position to aim at
		double d = mc.thePlayer.getDistanceToEntity(entity) / 2.5;
		double posX = target.posX + (target.posX - target.prevPosX) * d
			- mc.thePlayer.posX;
		double posY = target.posY + (target.posY - target.prevPosY) * 1
			+ target.height * 0.5 - mc.thePlayer.posY
			- mc.thePlayer.getEyeHeight();
		double posZ = target.posZ + (target.posZ - target.prevPosZ) * d
			- mc.thePlayer.posZ;
		
		float yaw = 0f;
		float pitch = 0f;
		
		// set yaw
		yaw =
			(float)Math.toDegrees(Math.atan2(posZ, posX)) - 90;
		
		// calculate needed pitch
		double hDistance = Math.sqrt(posX * posX + posZ * posZ);
		double hDistanceSq = hDistance * hDistance;
		float g = 0.006F;
		float velocitySq = velocity * velocity;
		float velocityPow4 = velocitySq * velocitySq;
		float neededPitch = (float)-Math.toDegrees(Math.atan((velocitySq - Math
			.sqrt(velocityPow4 - g * (g * hDistanceSq + 2 * posY * velocitySq)))
			/ (g * hDistance)));
		
		// set pitch
		if(Float.isNaN(neededPitch))
			return RotationUtils.getRotations(entity, 0, 0, 0);
		else
			pitch = neededPitch;
    	
        return new float[] {yaw, pitch};
    }
}
