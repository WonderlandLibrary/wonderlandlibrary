package me.nyan.flush.module.impl.player;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.movement.Fly;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet", "Edit", "Verus");

    public NoFall() {
        super("NoFall", Category.PLAYER);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        switch (mode.getValue().toUpperCase()) {
            case "PACKET":
                if (mc.thePlayer.fallDistance > 2) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
                break;
            case "EDIT":
                if (mc.thePlayer.fallDistance > 2) {
                    e.setGround(true);
                }
                break;
            case "VERUS":
                /*
                if (mc.thePlayer.fallDistance > 0) {
                    Flush.enableVerusDisabler();
                } else {
                    Flush.disableVerusDisabler();
                }
                 */
                if (mc.thePlayer.fallDistance >= 2.5) {
                    Flush.enableVerusDisabler();
                    e.setGround(true);
                    mc.thePlayer.fallDistance = -1;
                    break;
                }
                if (MovementUtils.isOnGround(0.001) && !getModule(Fly.class).isUsingVerusDisabler()) {
                    Flush.disableVerusDisabler();
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}

