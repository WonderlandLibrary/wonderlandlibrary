package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.AntiVoidMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;

@Module.Mod(displayName = "AntiVoid")
public class AntiVoid extends Module
{
	private AntiVoidMode vanilla;
	private AntiVoidMode ncp;
	private AntiVoidMode aac;
	private AntiVoidMode packet;
	private AntiVoidMode cubecraft;
	private AntiVoidMode mineplex;
	private AntiVoidMode spartan;
	private AntiVoidMode aerox;
	private AntiVoidMode agc;
	private AntiVoidMode horizon;
	private AntiVoidMode hawk;
	private AntiVoidMode security;
	private AntiVoidMode twilight;
	private AntiVoidMode matrix;
	private AntiVoidMode rewinside;
    public static Minecraft mc = Minecraft.getMinecraft();
	double lastX = 0;
	double lastY = 0;
	double lastZ = 0;
    
    public AntiVoid() {
        this.vanilla = new AntiVoidMode("Vanilla", true, this);
        this.ncp = new AntiVoidMode("NCP", false, this);
        this.aac = new AntiVoidMode("AAC", false, this);
        this.packet = new AntiVoidMode("Packet", false, this);
        this.cubecraft = new AntiVoidMode("CubeCraft", false, this);
        this.mineplex = new AntiVoidMode("Mineplex", false, this);
        this.spartan = new AntiVoidMode("Spartan", false, this);
        this.aerox = new AntiVoidMode("Aerox", false, this);
        this.agc = new AntiVoidMode("AGC", false, this);
        this.horizon = new AntiVoidMode("Horizon", false, this);
        this.hawk = new AntiVoidMode("Hawk", false, this);
        this.security = new AntiVoidMode("Security", false, this);
        this.twilight = new AntiVoidMode("Twilight", false, this);
        this.matrix = new AntiVoidMode("Matrix", false, this);
        this.rewinside = new AntiVoidMode("Rewinside", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.ncp);
        OptionManager.getOptionList().add(this.aac);
        OptionManager.getOptionList().add(this.packet);
        OptionManager.getOptionList().add(this.cubecraft);
        OptionManager.getOptionList().add(this.mineplex);
        OptionManager.getOptionList().add(this.spartan);
        OptionManager.getOptionList().add(this.aerox);
        OptionManager.getOptionList().add(this.agc);
        OptionManager.getOptionList().add(this.horizon);
        OptionManager.getOptionList().add(this.hawk);
        OptionManager.getOptionList().add(this.security);
        OptionManager.getOptionList().add(this.twilight);
        OptionManager.getOptionList().add(this.matrix);
        OptionManager.getOptionList().add(this.rewinside);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.ncp.getValue()) {
            this.setSuffix("NCP");
        }
        else if (this.aac.getValue()) {
        	this.setSuffix("AAC");
        }
        else if (this.packet.getValue()) {
        	this.setSuffix("Packet");
        }
        else if (this.cubecraft.getValue()) {
        	this.setSuffix("CubeCraft");
        }
        else if (this.mineplex.getValue()) {
        	this.setSuffix("Mineplex");
        }
        else if (this.spartan.getValue()) {
        	this.setSuffix("Spartan");
        }
        else if (this.aerox.getValue()) {
        	this.setSuffix("Aerox");
        }
        else if (this.agc.getValue()) {
        	this.setSuffix("AGC");
        }
        else if (this.horizon.getValue()) {
        	this.setSuffix("Horizon");
        }
        else if (this.hawk.getValue()) {
        	this.setSuffix("Hawk");
        }
        else if (this.security.getValue()) {
        	this.setSuffix("Security");
        }
        else if (this.twilight.getValue()) {
        	this.setSuffix("Twilight");
        }
        else if (this.matrix.getValue()) {
        	this.setSuffix("Matrix");
        }
        else if (this.rewinside.getValue()) {
        	this.setSuffix("Rewinside");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
		Timer.timerSpeed = 1.0f;
    	mc.thePlayer.isDead = false;
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.vanilla.getValue()) {
    		if (mc.thePlayer.fallDistance == 0 && mc.thePlayer.onGround) {
    			lastX = mc.thePlayer.posX;
    			lastY = mc.thePlayer.posY;
    			lastZ = mc.thePlayer.posZ;
    		}
    		if (mc.thePlayer.fallDistance >= 3.5f) {
    			mc.thePlayer.setPosition(lastX, lastY, lastZ);
    			mc.thePlayer.fallDistance = 0;
    		}
    	}
    	else if (this.ncp.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.motionY = -0.01;
    		mc.thePlayer.fallDistance = 0;
    	}
    	else if (this.aac.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.motionY = 2;
    		mc.thePlayer.fallDistance = 0;
    	}
    	else if (this.packet.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 12.0, mc.thePlayer.posZ, false));
    		mc.thePlayer.fallDistance = 0;
    	}
    	else if (this.cubecraft.getValue()) {
    		if (mc.thePlayer.fallDistance >= 3.5f) {
    		Timer.timerSpeed = 0.2f;
    		mc.thePlayer.motionY = 2.4f;
    		mc.thePlayer.fallDistance = 0;
    		} else {
    		Timer.timerSpeed = 1.0f;
    		}
    	}
    	else if (this.mineplex.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
    	}
        else if (this.spartan.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.aerox.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.agc.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.horizon.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.hawk.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.security.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.twilight.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.isDead = true;
        }
        else if (this.matrix.getValue() && mc.thePlayer.fallDistance > 3.0f) {
        	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1337, mc.thePlayer.posZ, true));
        }
    	else if (this.rewinside.getValue() && mc.thePlayer.fallDistance >= 3.5f) {
    		mc.thePlayer.motionY = -0.01;
    		mc.thePlayer.fallDistance = 0;
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
