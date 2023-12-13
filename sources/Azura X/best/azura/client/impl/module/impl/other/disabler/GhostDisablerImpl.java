package best.azura.client.impl.module.impl.other.disabler;

import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.util.math.MathUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.*;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.other.Disabler;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;

public class GhostDisablerImpl implements ModeImpl<Disabler> {

    private double lastY;

    @Override
    public Disabler getParent() {
        return (Disabler) Client.INSTANCE.getModuleManager().getModule(Disabler.class);
    }

    @Override
    public String getName() {
        return "Ghost";
    }

    @Override
    public void onEnable() {
        lastY = Integer.MIN_VALUE;
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    private void handle(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(Float.MAX_VALUE, Float.MAX_VALUE, true, true));
                if (Client.INSTANCE.getModuleManager().getModule(Flight.class).isEnabled() && !mc.thePlayer.onGround) {
                    final double originalY = e.y;
                    e.y = (int) Math.ceil(e.y);
                    final AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -mc.thePlayer.posY, 0).offset(0, e.y, 0);
                    if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                        e.y = (int) Math.floor(originalY);
                    e.onGround = true;
                }
            }
        }
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C0BPacketEntityAction || e.getPacket() instanceof C00PacketKeepAlive ||
                    e.getPacket() instanceof C0FPacketConfirmTransaction) e.setCancelled(true);
            if (e.getPacket() instanceof C03PacketPlayer) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(Float.MAX_VALUE, Float.MAX_VALUE, true, true));
                if (Client.INSTANCE.getModuleManager().getModule(Flight.class).isEnabled() && !mc.thePlayer.onGround) {
                    final C03PacketPlayer c03 = e.getPacket();
                    if (MathUtil.getDifference(c03.y, lastY) > 3.0 && c03.moving)
                        lastY = c03.y;
                    c03.y = lastY;
                }
            }
        }
        if (event instanceof EventReceivedPacket) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook)
                lastY = Integer.MIN_VALUE;
        }
    }
}