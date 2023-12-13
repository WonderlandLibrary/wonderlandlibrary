package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import net.minecraft.network.play.client.*;

public class FlightHycraftImpl implements ModeImpl<Flight> {

    private int ticks;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Hycraft";
    }

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    private void onEvent(final Event event) {
        /*if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isUpdate()) {
                mc.timer.timerSpeed = 0.5f;
                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, -0.42, 0)).isEmpty())
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                mc.thePlayer.motionY = 0;
                mc.thePlayer.onGround = true;
            }
        }*/
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre() && ticks++ % 13 == 0) {
                MovementUtil.spoof(0, mc.thePlayer.onGround);
                MovementUtil.spoof(10.0E+6, mc.thePlayer.onGround);
                MovementUtil.spoof(0, mc.thePlayer.onGround);
            }
        }
        if (event instanceof EventMove) {
            final EventMove e = (EventMove) event;
            mc.timer.timerSpeed = 0.4f;
            e.setY(mc.thePlayer.motionY = 0);
            if (mc.gameSettings.keyBindSneak.pressed)
                e.setY(mc.thePlayer.motionY -= Flight.speedValue.getObject() * 0.75);
            if (mc.gameSettings.keyBindJump.pressed)
                e.setY(mc.thePlayer.motionY += Flight.speedValue.getObject() * 0.75);
            mc.thePlayer.motionY = 0;
            MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Flight.speedValue.getObject() : 0, e);
        }
    }

}