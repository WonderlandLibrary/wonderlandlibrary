package xyz.cucumber.base.module.feat.player;

import net.minecraft.util.MathHelper;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventJump;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMoveFlying;
import xyz.cucumber.base.events.ext.EventRenderRotation;
import xyz.cucumber.base.events.ext.EventRotation;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.PLAYER, description = "Allows you to rotate back smoother", name = "Smooth Rotation")
public class SmoothRotationModule extends Mod{
	public KillAuraModule killAura;
	public ScaffoldModule scaffold;
	
	public boolean wasKillAura, wasScaffold;
	
	public BooleanSettings ka = new BooleanSettings("Kill Aura", true);
	public BooleanSettings sc = new BooleanSettings("Scaffold", true);
	public NumberSettings turnSpeed = new NumberSettings("Turn Speed", 3, 17, 60, 1);
	
	public SmoothRotationModule()
    {
        this.addSettings(ka, sc, turnSpeed);
    }
	
	public void onEnable() {
    	wasKillAura = false;
    	wasScaffold = false;
    	
    	killAura = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
    	scaffold = (ScaffoldModule) Client.INSTANCE.getModuleManager().getModule(ScaffoldModule.class);
    }
	
	@EventListener
	public void onRotation(EventTick e) {
		this.setInfo(turnSpeed.getValue() + "");
		if(killAura.isEnabled() && killAura.allowedToWork && ka.isEnabled()) {
    		wasKillAura = true;
    	}
    	
    	if(wasKillAura && (!killAura.isEnabled() || (killAura.isEnabled() && !killAura.allowedToWork))) {
    		if(Math.abs(MathHelper.wrapAngleTo180_float(RotationUtils.serverYaw - mc.thePlayer.rotationYaw)) > 10 || Math.abs(MathHelper.wrapAngleTo180_float(RotationUtils.serverPitch - mc.thePlayer.rotationPitch)) > 10) {
    			RotationUtils.customRots = true;
    			RotationUtils.serverYaw = RotationUtils.updateRotation(RotationUtils.serverYaw, mc.thePlayer.rotationYaw, (float) turnSpeed.getValue());
    			RotationUtils.serverPitch = RotationUtils.updateRotation(RotationUtils.serverPitch, mc.thePlayer.rotationPitch, (float) turnSpeed.getValue());
    		}else {
    			if(wasKillAura) {
    				RotationUtils.customRots = false;
    			}
    			wasKillAura = false;
    		}
    	}
    	
    	
    	if(scaffold.isEnabled() && InventoryUtils.getBlockSlot() != -1 && sc.isEnabled()) {
    		wasScaffold = true;
    	}
    	
    	if(wasScaffold && scaffold.enabledTicks < 10) {
    		if(Math.abs(MathHelper.wrapAngleTo180_float(RotationUtils.serverYaw - scaffold.lastYaw)) > 10 || Math.abs(MathHelper.wrapAngleTo180_float(RotationUtils.serverPitch - scaffold.lastPitch)) > 10) {
    			RotationUtils.customRots = true;
    			RotationUtils.serverYaw = RotationUtils.updateRotation(RotationUtils.serverYaw, scaffold.lastYaw, (float) turnSpeed.getValue());
    			RotationUtils.serverPitch = RotationUtils.updateRotation(RotationUtils.serverPitch, scaffold.lastPitch, (float) turnSpeed.getValue());
    		}else {
    			scaffold.enabledTicks = 11;
    		}
    	}
    	
    	if(wasScaffold && !scaffold.isEnabled()) {
    		if(Math.abs(MathHelper.wrapAngleTo180_float(RotationUtils.serverYaw - mc.thePlayer.rotationYaw)) > 10 || Math.abs(MathHelper.wrapAngleTo180_float(RotationUtils.serverPitch - mc.thePlayer.rotationPitch)) > 10) {
    			RotationUtils.customRots = true;
    			RotationUtils.serverYaw = RotationUtils.updateRotation(RotationUtils.serverYaw, mc.thePlayer.rotationYaw, (float) turnSpeed.getValue());
    			RotationUtils.serverPitch = RotationUtils.updateRotation(RotationUtils.serverPitch, mc.thePlayer.rotationPitch, (float) turnSpeed.getValue());
    		}else {
    			if(wasScaffold) {
    				RotationUtils.customRots = false;
    			}
    			wasScaffold = false;
    		}
    	}
	}
	@EventListener
	public void onMotion(EventMotion e) {
		
		if(e.getType() == EventType.POST) return;
		
		if(RotationUtils.customRots && (wasKillAura || wasScaffold)) {
			EventMotion event = (EventMotion)e;
        	event.setYaw(RotationUtils.serverYaw);
        	event.setPitch(RotationUtils.serverPitch);
		}
		
	}
	
	@EventListener
	public void onLook(EventLook event) {
		
		if(event.getType() == EventType.POST) return;
		
		if(RotationUtils.customRots && (wasKillAura || wasScaffold)) {
			
        	event.setYaw(RotationUtils.serverYaw);
        	event.setPitch(RotationUtils.serverPitch);
		}
		
	}
	@EventListener
	public void onRenderRotation(EventRenderRotation event) {
		
		if(event.getType() == EventType.POST) return;
		
		if(RotationUtils.customRots && (wasKillAura || wasScaffold)) {
			
        	event.setYaw(RotationUtils.serverYaw);
        	event.setPitch(RotationUtils.serverPitch);
		}
		
	}
	@EventListener
	public void onMoveFlying(EventMoveFlying e) {
		if(RotationUtils.customRots && (wasKillAura || wasScaffold)) {
			if(wasScaffold) {
	    		if(!scaffold.isEnabled()) {
	    			e.setCancelled(true);
	    			MovementUtils.silentMoveFix(e);
	    		}
	    	}else {
	    		e.setYaw(RotationUtils.serverYaw);	
	    	}
		}
		
	}
	@EventListener
	public void onJump(EventJump e) {
		if(RotationUtils.customRots && (wasKillAura))
			e.setYaw(RotationUtils.serverYaw);
		
	}
}
