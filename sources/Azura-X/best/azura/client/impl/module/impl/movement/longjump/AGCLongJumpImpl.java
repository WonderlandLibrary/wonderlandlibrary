package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.ArrayList;

public class AGCLongJumpImpl implements ModeImpl<LongJump> {

    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private int stage;

    @Override
    public LongJump getParent() {
        return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
    }

    @Override
    public String getName() {
        return "AGC";
    }

    @Override
    public void onEnable() {
        stage = 0;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setSpeed(0);
        mc.timer.timerSpeed = 1.0f;
        for (Packet<?> packet : packets) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        }
        packets.clear();
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (!e.isUpdate()) return;
        switch (stage) {
            case 0:
                mc.timer.timerSpeed = 0.1f;
                stage++;
                break;
            case 1:
                MovementUtil.spoof(-3, true);
                MovementUtil.spoof(-4, true);
                MovementUtil.spoof(-1, true);
                stage++;
                break;
            case 2:
                if (mc.thePlayer.hurtTime == 0) return;
                mc.timer.timerSpeed = 0.5f;
                stage++;
                break;
            case 3:
                mc.thePlayer.jump();
                stage++;
                break;
            case 4:
                if (mc.thePlayer.motionY < 0 && mc.thePlayer.onGround) {
                    getParent().setEnabled(false);
                    return;
                }
                if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(getParent().speedValue.getObject());
                else mc.thePlayer.setSpeed(0);
                break;
        }
    };

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if ((e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) && stage >= 4) {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof C0APacketAnimation || e.getPacket() instanceof C08PacketPlayerBlockPlacement || e.getPacket() instanceof C02PacketUseEntity) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction)) {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
    };
}