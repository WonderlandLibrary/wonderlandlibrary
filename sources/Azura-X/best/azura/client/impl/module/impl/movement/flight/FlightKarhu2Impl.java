package best.azura.client.impl.module.impl.movement.flight;


import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;

public class FlightKarhu2Impl implements ModeImpl<Flight> {
    boolean verusdmg = false;

    public int startY;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Jump";
    }

    @Override
    public void onEnable() {
        MovementUtil.spoof(3.1, false);
        MovementUtil.spoof(0, false);
        startY = (int) mc.thePlayer.posY;
        if(mc.thePlayer.onGround) mc.thePlayer.jump();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.setSpeed(0);
    }

    public  final Listener<EventMotion> motionListener = e -> {
        if (mc.thePlayer.posY < startY) {
            e.onGround = true;
        }
    };

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (mc.thePlayer.posY < startY) {
            mc.timer.timerSpeed = 0.2F;
            mc.thePlayer.setSpeed(4);
            mc.thePlayer.motionY = 0.99;
        } else {
            mc.timer.timerSpeed = 1.0F;
        }
    };
}