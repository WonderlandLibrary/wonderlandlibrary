package best.azura.client.impl.module.impl.movement.longjump;

import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventLivingUpdate;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;

public class LongJumpRedeSkyImpl implements ModeImpl<LongJump> {

    @Override
    public LongJump getParent() {
        return (LongJump) Client.INSTANCE.getModuleManager().getModule(LongJump.class);
    }

    @Override
    public String getName() {
        return "Rede Sky";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventLivingUpdate) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            } else {
                mc.thePlayer.motionY += 0.07;
                mc.timer.timerSpeed = 5.0f;
                mc.thePlayer.speedInAir = 0.05F;
            }
        }
    }
}