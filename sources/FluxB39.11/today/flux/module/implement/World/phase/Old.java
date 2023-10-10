package today.flux.module.implement.World.phase;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.PostUpdateEvent;
import today.flux.module.SubModule;
import today.flux.utility.BlockUtils;

/**
 * Created by John on 2017/06/23.
 */
public class Old extends SubModule {
    public Old() {
        super("Old", "Phase");
    }

    @EventTarget
    public void onPostUpdate(PostUpdateEvent event){
        double multiplier = 0.3;
        final double mx = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));

        final double x = this.mc.thePlayer.movementInput.moveForward * multiplier * mx + this.mc.thePlayer.movementInput.moveStrafe * multiplier * mz;
        final double z = this.mc.thePlayer.movementInput.moveForward * multiplier * mz - this.mc.thePlayer.movementInput.moveStrafe * multiplier * mx;

        if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && this.mc.thePlayer.isSneaking()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, false));
            final double posX2 = this.mc.thePlayer.posX;
            final double posY2 = this.mc.thePlayer.posY;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX2, posY2 - (BlockUtils.isOnLiquid() ? 9000.0 : 0.09), this.mc.thePlayer.posZ, false));
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
        }
    }
}
