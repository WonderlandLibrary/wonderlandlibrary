package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.util.Timer;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MathUtils;
import slavikcodd3r.rainbow.utils.MoveUtils;
import slavikcodd3r.rainbow.utils.SpeedUtils;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;

@Module.Mod(displayName = "LongJump")
public class LongJump extends Module
{
    @Op(name = "Bobbing")
    private boolean bobbing;
    @Op(name = "Speed", min = 0.0, max = 4.5, increment = 0.01)
    private double speed;
    Minecraft mc = Minecraft.getMinecraft();
    double lastDist;
    double stage;
    double moveSpeed;
    
    public LongJump() {
    	this.speed = 4.5;
    }
    
    public void enable() {
        this.stage = 1.0;
        super.enable();
    }
    
    public void disable() {
    	MoveUtils.setMotion(0);
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent e) {
    	this.setSuffix("Redesky");
    	if (mc.thePlayer.onGround) {
    		MoveUtils.setMotion(speed);
    		mc.thePlayer.jump();
    	}
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent e) {
    	if (this.bobbing) {
    		mc.thePlayer.cameraYaw = 0.11f;
    	}
    }
    
    @EventTarget
    private void onMove(final MoveEvent e) {
    }
}
