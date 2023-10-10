package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.PacketReceiveEvent;
import client.module.impl.movement.Flight;
import client.util.MoveUtil;
import client.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Mush2Flight extends Mode<Flight> {

    private boolean aBoolean, down;

    public Mush2Flight(final String name, final Flight parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {
        if (mc.thePlayer.ticksExisted % 24 == 0) {
            event.setY(event.getY() - 255);
            aBoolean = true;
        }
        MoveUtil.stop();
        MoveUtil.strafe(0.5F);
        mc.thePlayer.motionY = (mc.gameSettings.keyBindJump.isKeyDown() ? 0.42F : 0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.42F : 0);
        if (mc.thePlayer.motionY == 0) {
            mc.thePlayer.motionY = 0.0626 * (down ? -1 : 1);
            down = !down;
        }
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook wrapper = (S08PacketPlayerPosLook) packet;
            if (aBoolean) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
                aBoolean = false;
            }
        }
    };
}
