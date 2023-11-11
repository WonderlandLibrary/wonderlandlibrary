package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "VClip", "VClip", "Redesky");

    public Phase() {
        super("Phase", Category.PLAYER);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (mode.is("redesky")) {
            if (mc.thePlayer.isCollidedHorizontally) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 0.00000001,
                        mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 1,
                        mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            }
        }

        if (mode.is("vclip")) {
            MovementUtils.vClip(-3);
        }
        toggle();
    }
}
