package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.StrafeEvent;
import client.module.impl.movement.Flight;
import client.util.MoveUtil;
import client.value.Mode;
import net.minecraft.network.play.client.C03PacketPlayer;

public class VulcanFlight extends Mode<Flight> {

    private int ticks;

    public VulcanFlight(String name, Flight parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ticks = 0;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                mc.thePlayer.posX,
                mc.thePlayer.posY - 2,
                mc.thePlayer.posZ,
                mc.thePlayer.rotationYaw,
                mc.thePlayer.rotationPitch,
                false
        ));
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }

    @EventLink()
    public final Listener<MotionEvent> onMotion = event -> {
        final float speed = 1;

        mc.thePlayer.motionY = -1E-10D
                + (mc.gameSettings.keyBindJump.isKeyDown() ? speed : 0.0D)
                - (mc.gameSettings.keyBindSneak.isKeyDown() ? speed : 0.0D);

        if (mc.thePlayer.getDistance(mc.thePlayer.lastReportedPosX, mc.thePlayer.lastReportedPosY, mc.thePlayer.lastReportedPosZ) <= 10 - speed - 0.15) event.setCancelled(true);
        else {
            ticks++;
            if (ticks >= 8) {
                MoveUtil.stop();
                getParent().toggle();
            }
        }
    };

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {
        event.setFriction(1);
        MoveUtil.stop();
    };
}