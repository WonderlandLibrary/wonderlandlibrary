package club.async.module.impl.combat;

import club.async.Async;
import club.async.event.impl.EventMovePlayer;
import club.async.event.impl.EventPreUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.impl.movement.Speed;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.NumberSetting;
import club.async.util.*;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import rip.hippo.lwjeb.annotation.Handler;

import java.util.Objects;

@ModuleInfo(name = "TargetStrafe", description = "Circumcision", category = Category.COMBAT)
public class TargetStrafe extends Module {

    public NumberSetting range = new NumberSetting("Range", this, 0.1, 5, 2, 0.1);
    public BooleanSetting keepRange = new BooleanSetting("KeepRange", this, false);

    public int direction = -1;

    public TimeUtil timer = new TimeUtil();

    public EntityLivingBase target;

    @Handler
    public void update(EventPreUpdate event) {
        if((mc.thePlayer.isCollidedHorizontally || PlayerUtil.checkVoid()) && timer.hasTimePassed(100)) {
            direction = -direction;
            timer.reset();
        }

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) direction = -1;
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) direction = 1;

//        if(Async.INSTANCE.getModuleManager().moduleBy(KillAura.class).target != null) {
        target = Async.INSTANCE.getModuleManager().moduleBy(KillAura.class).target;
//        }
    }

    public boolean canStrafe() {
        return target != null && Async.INSTANCE.getModuleManager().isToggled(Speed.class) && Async.INSTANCE.getModuleManager().isToggled(KillAura.class) && isEnabled() && MovementUtil.isMoving();
    }

    public void strafe(EventMovePlayer event, double speed) {

        float forward = 0;

        if(PlayerUtil.getHorizontalDistanceToEntity(target) > range.getDouble()) forward = 1;

        if(keepRange.get()) {
            if(PlayerUtil.getHorizontalDistanceToEntity(target) < range.getDouble()) {
                forward = -1;
            }
        }

        MovementUtil.setSpeed(event, speed, forward, direction, Objects.requireNonNull(RotationUtil.getRotations(target, false, false))[0]);
    }

}
