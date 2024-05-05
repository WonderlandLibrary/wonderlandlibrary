package client.module.impl.movement.flight;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.MoveEvent;
import client.event.impl.other.PacketSendEvent;
import client.module.impl.movement.Flight;
import client.util.MoveUtil;
import client.value.Mode;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.List;

public class AGCFlight extends Mode<Flight> {

    private boolean damaged;
    private final List<Packet<?>> packets = new ArrayList<>();

    public AGCFlight(String name, Flight parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.0F;
        damaged = false;
        if (!packets.isEmpty()) {
            packets.clear();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        if (!packets.isEmpty()) {
            packets.forEach(mc.getNetHandler()::addToSendQueueUnregistered);
            packets.clear();
        }
    }

    @EventLink()
    public final Listener<MotionEvent> onMotion = event -> {
        if (damaged) {
            mc.timer.timerSpeed = 0.6F;
            MoveUtil.strafe(9.5F);
            mc.thePlayer.motionY = 0;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.6F;
            }
            if (mc.thePlayer.offGroundTicks >= 5) {
                mc.thePlayer.motionY = -0.0625;
            }
        } else {
            if (mc.thePlayer.hurtTime == 0) event.setY(event.getY() - 2.5 + Math.random() / 16);
            if (mc.thePlayer.hurtTime > 0) damaged = true;
        }
        /*final float speed = 2.5F;
        MoveUtil.stop();
        MoveUtil.strafe(speed);
        if (mc.thePlayer.ticksExisted % 2 == 0) {
            mc.thePlayer.motionY = 0.0626D
                    + (mc.gameSettings.keyBindJump.isKeyDown() ? speed * 0.5F : 0.0D)
                    - (mc.gameSettings.keyBindSneak.isKeyDown() ? speed * 0.5F : 0.0D);
        } else {
            mc.thePlayer.motionY = -0.0626D
                    + (mc.gameSettings.keyBindJump.isKeyDown() ? speed * 0.5F : 0.0D)
                    - (mc.gameSettings.keyBindSneak.isKeyDown() ? speed * 0.5F : 0.0D);
        }*/
    };

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        if (damaged) {
            packets.add(event.getPacket());
            event.setCancelled(true);
        }
    };

    @EventLink()
    public final Listener<MoveEvent> onMove = event -> {
        if (!damaged) event.setCancelled(true);
    };
}