package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.LongJump;

public class LongJumpBWPracticeImpl implements ModeImpl<LongJump> {
    private double speed;
    private int ticks;
    private boolean hasJumped = false;

    @Override
    public LongJump getParent() {
        return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
    }

    @Override
    public String getName() {
        return "BW Practice";
    }

    @Override
    public void onEnable() {
        speed = ticks = 0;
        hasJumped = false;
    }

    @Override
    public void onDisable() {
        speed = ticks = 0;
        hasJumped = false;
    }

    @EventHandler
    public final Listener<EventMotion> moveListener = e -> {
        switch (ticks) {
            case 0:
                mc.thePlayer.jump();
                hasJumped = true;
                ticks++;
                break;
            case 1:
                if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(getParent().speedValue.getObject());
                ticks++;
                break;
            case 2:
                if (mc.thePlayer.isMoving()) mc.thePlayer.setSpeed(getParent().speedValue.getObject());
                if (mc.thePlayer.onGround && hasJumped)
                    getParent().setEnabled(false);
                break;
        }
    };
}