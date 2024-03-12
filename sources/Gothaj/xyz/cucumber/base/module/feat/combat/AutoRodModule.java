package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityLivingBase;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.events.ext.EventRenderRotation;
import xyz.cucumber.base.events.ext.EventRotation;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.COMBAT, description = "Automatically throw projectiles into player", name = "Auto Rod", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)

public class AutoRodModule extends Mod {
	
	public EntityLivingBase target;
    public boolean cancel;
    public KillAuraModule ka;
    public Timer timer = new Timer();
    
    public void onEnable()
    {
        ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
    }
    
    public void onDisable()
    {
        RotationUtils.customRots = false;
    }
    
    @EventListener
    public void onRotation(EventRotation event) {
    	int newSlot = InventoryUtils.getProjectileSlot();
        if (newSlot == -1) {
        	cancel = false;
        	return;
        }

        target = EntityUtils.getTarget(10, "Players", "Single", (int) 400, Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled(), ka.TroughWalls.isEnabled(), ka.attackInvisible.isEnabled(), ka.attackDead.isEnabled());

        if (target == null) {
        	cancel = false;
        	return;
        }

        if (!ka.isEnabled() ||  EntityUtils.getDistanceToEntityBox(target) < Math.max(ka.Range.value, 4) || target.hurtTime >= 3) {
        	cancel = false;
        	return;
        }
        
        double delay = 350;
        
        if(mc.thePlayer.getDistanceToEntity(target) <= 8) {
        	delay = 300;
        }else if(mc.thePlayer.getDistanceToEntity(target) <= 8) {
        	delay = 250;
        }else if(mc.thePlayer.getDistanceToEntity(target) <= 7) {
        	delay = 200;
        }else if(mc.thePlayer.getDistanceToEntity(target) <= 6) {
        	delay = 150;
        }else if(mc.thePlayer.getDistanceToEntity(target) <= 5) {
        	delay = 100;
        }
        
        if (!timer.hasTimeElapsed(delay, false)) {
        	if(cancel) {
        		cancel = false;
        		RotationUtils.customRots = false;
        	}
        	return;
        }

        double multiplier = mc.thePlayer.getDistanceToEntity(target) / 1.25;
        
        double deltaX = (target.posX - target.lastTickPosX) * multiplier;
        double deltaZ = (target.posZ - target.lastTickPosZ) * multiplier;
        double targetPosX = target.posX + deltaX;
        double targetPosZ = target.posZ + deltaZ;
        
        double targetPosY = (target.posY + target.getEyeHeight()) - 0.4;
        	
        float rots[] = RotationUtils.getRotations(targetPosX, targetPosY, targetPosZ);
        
        RotationUtils.customRots = true;
        RotationUtils.serverYaw = rots[0];
        RotationUtils.serverPitch = rots[1];
        
        mc.thePlayer.inventory.currentItem = newSlot;
        
        cancel = true;
    }
    
    @EventListener
    public void onMotion(EventMotion event) {
    	if(cancel) {
            if(event.getType() == EventType.PRE) {
        		event.setYaw(RotationUtils.serverYaw);
        		event.setPitch(RotationUtils.serverPitch);
        	}else {
        		if(mc.thePlayer.inventory.currentItem == InventoryUtils.getProjectileSlot())mc.rightClickMouse();
        		timer.reset();
        	}
        }
    }
    
    @EventListener
    public void onMove(EventMoveFlying event) {
    	if(cancel) {
    		event.setYaw(RotationUtils.serverYaw);
    	}
    }
    
    @EventListener
    public void onJump(EventJump event) {
    	if(cancel) {
    		event.setYaw(RotationUtils.serverYaw);
    	}
    }
    
    @EventListener
    public void onLook(EventLook event) {
    	if(cancel) {
    		event.setYaw(RotationUtils.serverYaw);
    		event.setPitch(RotationUtils.serverPitch);
    	}
    }
    @EventListener
    public void onRotationRender(EventRenderRotation event) {
    	if(cancel) {
    		event.setYaw(RotationUtils.serverYaw);
    		event.setPitch(RotationUtils.serverPitch);
    	}
    }
}
