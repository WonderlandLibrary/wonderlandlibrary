package client.module.impl.movement.speed;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.UpdateEvent;
import client.module.impl.movement.Speed;
import client.util.MoveUtil;
import client.value.Mode;

public class TestSpeed extends Mode<Speed> {

    private int ticks;

    public TestSpeed(final String name, final Speed parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ticks = 0;
        mc.timer.timerSpeed = 1f;
    }
    @Override
    public void onDisable() {
        ticks = 0;
        mc.timer.timerSpeed = 1f;
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {



            ticks++;
            switch (ticks) {
                case 1:
                    mc.timer.timerSpeed = 3.1f;
                    MoveUtil.strafe(0.1F);
                    break;
                case 4:
                    mc.thePlayer.motionY = -0.0284000015258789;
                    break;
                case 5:
                case 6:
                    mc.timer.timerSpeed = 3.7f;
                    MoveUtil.strafe(0.35F);
                    break;
                case 10:
                case 11:
                    mc.timer.timerSpeed = 0.2f;
                    mc.thePlayer.motionY = -35281.91495F;
//                    MoveUtil.strafe(0.25F);
                    ticks = 0;
                    break;
            }
            MoveUtil.strafe();

    };
}
