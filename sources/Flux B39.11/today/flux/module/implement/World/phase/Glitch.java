package today.flux.module.implement.World.phase;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.BBSetEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;
import today.flux.utility.DelayTimer;

/**
 * Created by John on 2017/06/23.
 */
public class Glitch extends SubModule {
    private DelayTimer timer = new DelayTimer();
    
    public Glitch(){
        super("Glitch", "Phase");
    }

    @Override
    public void onEnable(){
        super.onEnable();
        
        this.timer.reset();
    }

    @EventTarget
    public void onSetBoundingbox(final BBSetEvent event) {
        if (BlockUtils.isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > this.mc.thePlayer.getEntityBoundingBox().minY) {
            event.setBoundingBox(null);
        }
    }

    @EventTarget
    public void onUpdate(PostUpdateEvent event) {
        if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder()) {
            final double multiplier = 0.3;
            final double mx = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
            double xOff = this.mc.thePlayer.movementInput.moveForward * multiplier * mx + this.mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
            double zOff = this.mc.thePlayer.movementInput.moveForward * multiplier * mz - this.mc.thePlayer.movementInput.moveStrafe * multiplier * mx;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + xOff, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + zOff, false));
            for (int i = 1; i < 10; ++i) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, 8.988465674311579E307, this.mc.thePlayer.posZ, false));
            }
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + xOff, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + zOff);
        }
    }
}
