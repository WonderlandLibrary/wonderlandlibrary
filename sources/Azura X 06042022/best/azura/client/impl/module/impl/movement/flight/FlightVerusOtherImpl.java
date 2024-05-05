package best.azura.client.impl.module.impl.movement.flight;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventUpdate;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.block.BlockAir;

public class FlightVerusOtherImpl implements ModeImpl<Flight> {
    private double speed;
    private int ticks;

    @Override
    public Flight getParent() {
        return (Flight) Client.INSTANCE.getModuleManager().getModule(Flight.class);
    }

    @Override
    public String getName() {
        return "Verus Other";
    }

    @Override
    public void onDeselect() {
        BlockAir.collision = false;
        BlockAir.collisionMaxY = -1;
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        BlockAir.collisionMaxY = (int) mc.thePlayer.posY;
        if (mc.gameSettings.keyBindSneak.pressed && mc.thePlayer.ticksExisted % 3 == 0) BlockAir.collisionMaxY = (int) (mc.thePlayer.posY - 1);
        BlockAir.collision = true;
    };

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        speed = 0;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        mc.timer.timerSpeed = 1F;
        speed = 0;
        ticks = 0;
        BlockAir.collision = false;
        BlockAir.collisionMaxY = -1;
    }

    @EventHandler
    public final Listener<EventJump> eventJumpListener = e -> {
        e.setSpeed(0);
    };


    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (!e.isPre()) return;
        if(!mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(0);
        if (mc.gameSettings.keyBindSneak.pressed) return;
        switch (ticks) {
            case 0:
            case 1:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.4F;
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.18);
                        ticks++;
                    }
                }
            case 2:
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42F;
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.1);
                    } else {
                        mc.thePlayer.setSpeed(MovementUtil.getBaseSpeed() + 0.02 * 0.991);

                    }
                }
                break;
        }
        if (mc.thePlayer.motionY > 0 && mc.thePlayer.motionY < 0.4 && !mc.gameSettings.keyBindJump.pressed) {
            e.onGround = true;
            mc.thePlayer.motionY = 0;
        }
    };
}