package xyz.cucumber.base.module.feat.movement;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventHit;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventWorldChange;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows your to teleport", name = "Blink", key = Keyboard.KEY_NONE)
public class BlinkModule extends Mod {
	
	public LinkedList<Packet> inPackets = new LinkedList<>();
    public LinkedList<Packet> outPackets = new LinkedList<>();
    
    public Timer timer = new Timer();
    
    public BooleanSettings releaseOnHit = new BooleanSettings("Release On Hit", true);
    public BooleanSettings releaseWhenHurt = new BooleanSettings("Release When Hurt", true);
    public BooleanSettings pulse = new BooleanSettings("Pulse", true);
    public NumberSettings pulseDelay = new NumberSettings("pulse Delay", 500, 100, 3500, 50);
    
    public BlinkModule() {
    	this.addSettings(releaseOnHit, releaseWhenHurt, pulse, pulseDelay);
    }
    
    public void onDisable() {
    	release();
    }
	
    @EventListener
	public void onWorldChange(EventWorldChange e) {
		//this.toggle();
	}
    
    @EventListener
	public void onMotion(EventMotion e) {
    	if(pulse.isEnabled()) {
    		if(timer.hasTimeElapsed(pulseDelay.getValue(), true)) {
    			release();
    		}
    	}
    	
    	if(releaseWhenHurt.isEnabled()) {
    		if(mc.thePlayer.hurtTime > 0)release();
    	}
	}
    
    @EventListener
    public void onHit(EventHit e) {
    	if(mc.objectMouseOver.entityHit != null) {
    		if(releaseOnHit.isEnabled()) {
    			if(mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
    				EntityLivingBase ent = (EntityLivingBase) mc.objectMouseOver.entityHit;
    				
    				if(ent.hurtTime <= 1) {
    					release();
    				}
    			}
    		}
    	}
    }
    
	@EventListener
	public void onSendPacket(EventSendPacket e) {
		e.setCancelled(true);
		outPackets.add(e.getPacket());
	}
	
	public void release() {
		if(mc.isSingleplayer())return;
		
    	try {
    		while(!inPackets.isEmpty()) {
    			inPackets.poll().processPacket(mc.getNetHandler().getNetworkManager().getNetHandler());
    		}
    	}catch(Exception ex) {
    		
    	}
    	
    	try {
    		while(!outPackets.isEmpty()) {
    			mc.getNetHandler().getNetworkManager().sendPacketNoEvent(outPackets.poll());
    		}
    	}catch(Exception ex) {
    		
    	}
    	
    	inPackets.clear();
    	outPackets.clear();
    }
}
