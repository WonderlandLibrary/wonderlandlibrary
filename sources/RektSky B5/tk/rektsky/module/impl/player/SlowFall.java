/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class SlowFall
extends Module {
    private double fall = 0.0;

    public SlowFall() {
        super("SlowFall", "So you won't take fall damage and fall slowly like a p2w player", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.fall = 0.0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PacketSentEvent && ((PacketSentEvent)event).getPacket() instanceof C03PacketPlayer) {
            if (this.mc.thePlayer.fallDistance == 0.0f) {
                this.fall = 0.0;
            }
            if ((double)this.mc.thePlayer.fallDistance - this.fall > 2.6969) {
                this.fall = this.mc.thePlayer.fallDistance;
                ((C03PacketPlayer)((PacketSentEvent)event).getPacket()).onGround = true;
                this.mc.thePlayer.motionY = 0.0;
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        }
    }
}

