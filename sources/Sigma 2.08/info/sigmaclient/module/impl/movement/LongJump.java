package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.module.Module;

public class LongJump extends Module {

    private String OFF = "TOGGLE";

    private boolean wasOnGround;

    public LongJump(ModuleData data) {
        super(data);
        settings.put(BOOST, new Setting<>(BOOST, 3, "Boost speed.", 0.1, 3, 5));
        speed = 0.27999999999999997;
        onGroundLastTick = false;
        distance = 0.0;
        settings.put(OFF, new Setting<>(OFF, true, "Toggles off on landing."));
    }

    @Override
    public void onEnable() {
        speed = 0.27999999999999997;
        onGroundLastTick = false;
        distance = 0.0;
        wasOnGround = false;
    }

    private double speed;
    private boolean onGroundLastTick;
    private double distance;

    private final String BOOST = "BOOST";

    @Override
    @RegisterEvent(events = {EventMove.class, EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventMove) {
            EventMove em = (EventMove) event;
            double boost = ((Number) settings.get(BOOST).getValue()).doubleValue();
            if ((mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) || mc.theWorld == null || PlayerUtil.isOnLiquid() || PlayerUtil.isInLiquid()) {
                speed = 0.27999999999999997;
                return;
            }
            if (mc.thePlayer.onGround) {
                if (onGroundLastTick || mc.thePlayer.motionY < -0.3) {
                    speed *= 2.15 - 1.0 / Math.pow(10.0, 5.0);
                    em.setY(mc.thePlayer.motionY = 0.4);
                    mc.thePlayer.onGround = true;
                } else {
                    speed = boost * 0.27999999999999997;
                }
            } else if (onGroundLastTick) {
                if (distance < 2.147)
                    distance = 2.147;
                speed = distance - 0.66 * (distance - 0.27999999999999997);
            } else {
                speed = distance - distance / 159.0;
            }
            onGroundLastTick = mc.thePlayer.onGround;
            speed = Math.max(speed, 0.27999999999999997);
            em.setX(-(Math.sin(mc.thePlayer.getDirection()) * speed));
            em.setZ(Math.cos(mc.thePlayer.getDirection()) * speed);
        }
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre())
                distance = Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
        }
        if ((Boolean) settings.get(OFF).getValue()) {
            if (!onGroundLastTick && mc.thePlayer.isCollidedVertically && wasOnGround)
                toggle();
            if (!wasOnGround && PlayerUtil.isMoving())
                wasOnGround = true;
        }
    }

}
