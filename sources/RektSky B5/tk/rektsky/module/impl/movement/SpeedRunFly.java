/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class SpeedRunFly
extends Module {
    public SpeedRunFly() {
        super("SFly", "A module for speedrun record", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 4.0, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
    }

    @Override
    public void onDisable() {
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        this.mc.thePlayer.motionX = -Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw)) * 6.0;
        this.mc.thePlayer.motionZ = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw)) * 6.0;
    }
}

