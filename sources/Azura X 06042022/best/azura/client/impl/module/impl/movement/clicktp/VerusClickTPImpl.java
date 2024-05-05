package best.azura.client.impl.module.impl.movement.clicktp;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Teleport;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.math.CustomVec3;
import best.azura.client.util.pathing.Path;
import best.azura.client.util.pathing.PathUtil;
import best.azura.client.util.player.RaytraceUtil;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MovingObjectPosition;

public class VerusClickTPImpl implements ModeImpl<Teleport> {

    private Path path;
    private int ticks;

    @Override
    public Teleport getParent() {
        return (Teleport) Client.INSTANCE.getModuleManager().getModule(Teleport.class);
    }

    @Override
    public String getName() {
        return "Verus";
    }

    @Override
    public void onEnable() {
        ticks = 0;
        path = null;
    }

    @Override
    public void onDisable() {
        ticks = 0;
        path = null;
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                if (ticks > 0 && mc.thePlayer.isMoving()) {
                    getParent().setEnabled(false);
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Click TP", "Disabled due to movement", 4000, Type.INFO));
                    return;
                }
                if (ticks == 0 && !e.onGround && path != null && !path.getAdditionalVectors().isEmpty()) {
                    getParent().setEnabled(false);
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Click TP", "Please stand on ground when selecting...", 4000, Type.WARNING));
                    return;
                }
                if (path == null) {
                    final MovingObjectPosition position = RaytraceUtil.rayTrace(60, e.yaw, e.pitch);
                    if (mc.gameSettings.keyBindAttack.pressed && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !mc.theWorld.isAirBlock(position.getBlockPos())) {
                        e.y = -100;
                        e.onGround = true;
                        this.path = PathUtil.findPath(new CustomVec3(mc.thePlayer), new CustomVec3(position.getBlockPos()).offset(0, 1, 0), 10);
                        double time = Math.max(path.getAdditionalVectors().size() * 10, 20) / 20.0;
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Click TP", "Teleporting in "
                                + time + (time == 1 ? " second " : " seconds"), (long) (time * 1000), Type.INFO));
                    }
                } else {
                    mc.thePlayer.setSprinting(true);
                    e.sprinting = true;
                    if (ticks < Math.max(path.getAdditionalVectors().size() * 10, 20)) ticks++;
                }
                if (ticks == Math.max(path.getAdditionalVectors().size() * 10, 20) && path != null) {
                    for (CustomVec3 v : path.getAdditionalVectors())
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(e.x = v.getX(), e.y = v.getY(), e.z = v.getZ(), false));
                    getParent().setEnabled(false);
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Click TP",
                            "It is advised to now not use the click tp for 25 seconds.", 25000, Type.INFO));
                }
            }
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if ((e.getPacket() instanceof C0FPacketConfirmTransaction || e.getPacket() instanceof C00PacketKeepAlive) && ticks > 0) e.setCancelled(true);
            if (e.getPacket() instanceof C03PacketPlayer && ticks < Math.max(path.getAdditionalVectors().size() * 2, 20)) e.setCancelled(true);
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                if (ticks > 2) {
                    getParent().setEnabled(false);
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Click TP", "Disabled due to teleport/lagback", 4000, Type.INFO));
                } else {
                    final S08PacketPlayerPosLook s = e.getPacket();
                    e.setCancelled(true);
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(s.x, s.y, s.z, s.yaw, s.pitch, false));
                }
            }
        }
    }
}