package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.player.MovementUtil;

public class FlightVerusDamageImpl implements ModeImpl<Flight> {


    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Damage";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        MovementUtil.spoof(4, false);
        MovementUtil.spoof(0.1, false);
        MovementUtil.spoof(0, true);
        mc.thePlayer.motionY = 0.5F;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        mc.timer.timerSpeed = 1F;
        mc.thePlayer.setSpeed(0);
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        mc.timer.timerSpeed = 0.3F;
        //e.onGround = true;
        if(mc.thePlayer.isSneaking()) return;
        e.yaw = MathUtil.getRandom_float(-180, 180);
        if(mc.thePlayer.fallDistance > 0.1) {
            e.onGround = true;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.fallDistance = 0;
        }
    };

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if(mc.thePlayer.isMoving() && !mc.thePlayer.isSneaking() && !mc.gameSettings.keyBindJump.pressed) mc.thePlayer.setSpeed(Flight.speedValue.getObject());
        if(!mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0); mc.thePlayer.setSprinting(false);
    };
}