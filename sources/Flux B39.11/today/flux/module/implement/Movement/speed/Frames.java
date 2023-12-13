package today.flux.module.implement.Movement.speed;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.entity.EntityPlayerSP;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.DelayTimer;

public class Frames extends SubModule{

    private boolean move;
    private boolean canChangeMotion;
    private DelayTimer timer;

    public Frames(){
        super("Frames", "Speed");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.timer = new DelayTimer();
        this.move = true;
        this.canChangeMotion = false;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final PostUpdateEvent event) { //i stole this from alerithe :D
        if (!this.mc.thePlayer.onGround) {
            if (this.canChangeMotion) {
                if (!this.move) {
                    final EntityPlayerSP thePlayer = this.mc.thePlayer;
                    thePlayer.motionX *= 4.6805;
                    final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                    thePlayer2.motionZ *= 4.6805;
                    this.move = true;
                    this.canChangeMotion = false;
                } else {
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                    this.move = false;
                    this.canChangeMotion = false;
                }
            }
        } else if ((this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && !this.mc.thePlayer.isCollidedHorizontally) {
            this.mc.thePlayer.jump();
            this.canChangeMotion = true;
        }

    }
}

