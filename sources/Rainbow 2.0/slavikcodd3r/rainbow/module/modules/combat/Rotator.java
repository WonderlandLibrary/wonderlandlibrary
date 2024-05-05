package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.RotatorMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Rotator")
public class Rotator extends Module
{
    @Option.Op(name = "Yaw", min = -360, max = 360, increment = 1)
    public float yaw;
    @Option.Op(name = "Pitch", min = -360, max = 360, increment = 1)
    public float pitch;
	private RotatorMode normal;
	private RotatorMode event;
	private RotatorMode position;
	private RotatorMode packet;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Rotator() {
        this.normal = new RotatorMode("Normal", true, this);
        this.event = new RotatorMode("Event", false, this);
        this.position = new RotatorMode("Position", false, this);
        this.packet = new RotatorMode("Packet", false, this);
        this.yaw = 90;
        this.pitch = 90;
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.normal);
        OptionManager.getOptionList().add(this.event);
        OptionManager.getOptionList().add(this.position);
        OptionManager.getOptionList().add(this.packet);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.normal.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Normal")).toString());
        }
        else if (this.event.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Event")).toString());
        }
        else if (this.position.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Position")).toString());
        }
        else if (this.packet.getValue()) {
        	this.setSuffix(new StringBuilder(String.valueOf("Packet")).toString());
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.normal.getValue()) {
    		mc.thePlayer.rotationYaw = yaw;
    		mc.thePlayer.rotationPitch = pitch;
    		mc.thePlayer.rotationYawHead = yaw;
    		mc.thePlayer.renderYawOffset = yaw;
    	}
    	else if (this.event.getValue()) {
    		event.setYaw(yaw);
    		event.setPitch(pitch);
    		mc.thePlayer.rotationYawHead = yaw;
    		mc.thePlayer.renderYawOffset = yaw;
    	}
    	else if (this.position.getValue()) {
    		mc.thePlayer.setRotationYawHead(yaw);
    		mc.thePlayer.setRotationPitchHead(pitch);
    		mc.thePlayer.rotationYawHead = yaw;
    		mc.thePlayer.renderYawOffset = yaw;
    	}
    	else if (this.packet.getValue()) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround ? true : false));
    		mc.thePlayer.rotationYawHead = yaw;
    		mc.thePlayer.renderYawOffset = yaw;
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
