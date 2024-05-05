package club.async.module.impl.movement;

import club.async.event.impl.EventMovePlayer;
import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.MovementUtil;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Speed", description = "Travel faster than Normal", category = Category.MOVEMENT)
public class Speed extends Module {

    private ModeSetting mode = new ModeSetting("Mode", this, new String[]{"Vanilla", "NCP"}, "Vanilla");
    private NumberSetting speed = new NumberSetting("Speed", this,0.1,6,1, 0.1);

    private int tick;
    private double moveSpeed;

    @Handler
    public void movePlayer(EventMovePlayer eventMovePlayer) {;
        switch (mode.getCurrMode())
        {
            case "Vanilla":
                MovementUtil.setSpeed(eventMovePlayer, speed.getDouble());
                break;
            case "NCP":
                if(MovementUtil.isMoving()) {
                    if(mc.thePlayer.onGround) tick = 0;
                    switch (tick) {
                        case 0:
                            moveSpeed = MovementUtil.getBaseMoveSpeed() * 2.1;
                            break;
                        case 1:
                            moveSpeed *= 0.61;
                            break;
                        default:
                            moveSpeed -= moveSpeed / 159.9985;
                            break;
                    }
                    tick++;
                    MovementUtil.setSpeed(eventMovePlayer, Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed()));
                }
                break;
        }
    }

    @Handler
    public void update(EventUpdate eventUpdate) {
        setExtraTag(mode.getCurrMode());
        MovementUtil.jump();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
