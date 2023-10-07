package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class FlightVerusFreeCamImpl implements ModeImpl<Flight> {


    private Vec3 startPos;
    private int startSlot;
    private final ArrayList<Packet<?>> movePackets = new ArrayList<>();

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Motion";
    }

    @EventHandler
    public void handle(final Event event) {
        if (mc.thePlayer == null || mc.theWorld == null) {
            getParent().setEnabled(false);
            return;
        }
        if (event instanceof EventMove) {
            EventMove e = (EventMove) event;
            e.setNoClip(true);
            e.setX(mc.thePlayer.motionX = 0);
            e.setY(mc.thePlayer.motionY = 0);
            e.setZ(mc.thePlayer.motionZ = 0);
            if (mc.thePlayer.isMoving()) MovementUtil.setSpeed(0.28, e);
            if (mc.gameSettings.keyBindJump.pressed) e.setY(e.getY() + 0.5);
            if (mc.gameSettings.keyBindSneak.pressed) e.setY(e.getY() - 0.5);
            mc.playerController.curBlockDamageMP = 0;
        }
        if (event instanceof EventSentPacket) {
            EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C00PacketKeepAlive) return;
            if (e.getPacket() instanceof C0FPacketConfirmTransaction) movePackets.add(e.getPacket());
            if (e.getPacket() instanceof C03PacketPlayer) movePackets.add(e.getPacket());
            e.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        try {
            startPos = mc.thePlayer.getPositionVector();
            startSlot = mc.thePlayer.inventory.currentItem;
        } catch (Exception ignored) {}
    }

    @Override
    public void onDisable() {
        try {
            if (mc.thePlayer.getDistance(startPos.xCoord, startPos.yCoord, startPos.zCoord) < 10) {
                mc.thePlayer.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Flight", "Please fly further in order to bypass.", 3000, Type.WARNING));
                return;
            }
            mc.thePlayer.setPosition(startPos.xCoord, startPos.yCoord, startPos.zCoord);
            mc.thePlayer.inventory.currentItem = startSlot;
        } catch (Exception ignored) {}
        for (Packet<?> packet : movePackets) mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
        movePackets.clear();
        startPos = null;
    }

}