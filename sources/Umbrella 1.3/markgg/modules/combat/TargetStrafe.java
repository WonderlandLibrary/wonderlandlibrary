/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.combat;

import markgg.events.Event;
import markgg.events.listeners.EventPacket;
import markgg.modules.Module;
import markgg.modules.combat.KillAura;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import markgg.utilities.movement.MovementUtil;
import markgg.utilities.rotation.RotationUtil;

public class TargetStrafe
extends Module {
    public static NumberSetting radius = new NumberSetting("Radius", 1.0, 1.0, 5.0, 1.0);
    public static BooleanSetting space = new BooleanSetting("Space", false);
    public static NumberSetting timer = new NumberSetting("Timer", 1.0, 1.0, 5.0, 1.0);
    boolean isStrafing;
    private double Direction;
    boolean changedDirection;

    public TargetStrafe() {
        super("TargetStrafe", 0, Module.Category.COMBAT);
        this.addSettings(radius, space, timer);
    }

    @Override
    public void onEnable() {
        this.Direction = 1.0;
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
    }

    public void strafe(Event e, double moveSpeed) {
        int dist = (int)this.mc.thePlayer.getDistanceToEntity(KillAura.targets.get(0));
        if (this.mc.thePlayer.isCollidedHorizontally && !this.changedDirection) {
            if (this.Direction == 1.0) {
                this.Direction = -1.0;
            } else if (this.Direction == -1.0) {
                this.Direction = 1.0;
            }
            this.changedDirection = true;
        } else {
            this.changedDirection = false;
        }
        if ((double)dist > radius.getValue()) {
            this.mc.timer.timerSpeed = (float)timer.getValue();
            MovementUtil.setMotionWithValues(e, MovementUtil.getBaseMoveSpeed() - 0.0, RotationUtil.getPredictedRotations(KillAura.targets.get(0))[0], 1.0, this.Direction);
        } else {
            MovementUtil.setMotionWithValues(e, MovementUtil.getBaseMoveSpeed(), RotationUtil.getPredictedRotations(KillAura.targets.get(0))[0], 0.0, this.Direction);
        }
    }

    @Override
    public void onEvent(Event e) {
        boolean cfr_ignored_0 = e instanceof EventPacket;
    }
}

