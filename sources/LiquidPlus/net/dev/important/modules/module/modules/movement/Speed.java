/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.modules.module.modules.movement;

import java.util.ArrayList;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.movement.speeds.SpeedMode;
import net.dev.important.modules.module.modules.movement.speeds.aac.AACBHop;
import net.dev.important.modules.module.modules.movement.speeds.ncp.NCPBHop;
import net.dev.important.modules.module.modules.movement.speeds.ncp.Watchdog;
import net.dev.important.modules.module.modules.movement.speeds.other.CustomSpeed;
import net.dev.important.modules.module.modules.movement.speeds.other.RedeskyTimerHop;
import net.dev.important.modules.module.modules.movement.speeds.other.VulcanLowHop;
import net.dev.important.utils.MovementUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import oh.yalan.NativeClass;

@NativeClass
@Info(name="Speed", description="Allows you to move faster.", category=Category.MOVEMENT, cnName="\u52a0\u901f")
public class Speed
extends Module {
    private final SpeedMode[] speedModes = new SpeedMode[]{new NCPBHop(), new Watchdog(), new AACBHop(), new VulcanLowHop(), new RedeskyTimerHop(), new CustomSpeed()};
    public final ListValue modeValue = new ListValue("Mode", this.getModes(), "NCPBHop"){

        @Override
        protected void onChange(String oldValue, String newValue) {
            if (Speed.this.getState()) {
                Speed.this.onDisable();
            }
        }

        @Override
        protected void onChanged(String oldValue, String newValue) {
            if (Speed.this.getState()) {
                Speed.this.onEnable();
            }
        }
    };
    public final FloatValue customSpeedValue = new FloatValue("CustomSpeed", 1.6f, 0.2f, 2.0f);
    public final FloatValue customYValue = new FloatValue("CustomY", 0.0f, 0.0f, 4.0f);
    public final FloatValue customTimerValue = new FloatValue("CustomTimer", 1.0f, 0.1f, 2.0f);
    public final BoolValue customStrafeValue = new BoolValue("CustomStrafe", true);
    public final BoolValue resetXZValue = new BoolValue("CustomResetXZ", false);
    public final BoolValue resetYValue = new BoolValue("CustomResetY", false);

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        SpeedMode speedMode;
        if (Speed.mc.field_71439_g.func_70093_af()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            Speed.mc.field_71439_g.func_70031_b(true);
        }
        if ((speedMode = this.getMode()) != null) {
            speedMode.onUpdate();
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        SpeedMode speedMode;
        if (Speed.mc.field_71439_g.func_70093_af() || event.getEventState() != EventState.PRE) {
            return;
        }
        if (MovementUtils.isMoving()) {
            Speed.mc.field_71439_g.func_70031_b(true);
        }
        if ((speedMode = this.getMode()) != null) {
            speedMode.onMotion();
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (Speed.mc.field_71439_g.func_70093_af()) {
            return;
        }
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onMove(event);
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (Speed.mc.field_71439_g.func_70093_af()) {
            return;
        }
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onTick();
        }
    }

    @Override
    public void onEnable() {
        if (Speed.mc.field_71439_g == null) {
            return;
        }
        Speed.mc.field_71428_T.field_74278_d = 1.0f;
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onEnable();
        }
    }

    @Override
    public void onDisable() {
        if (Speed.mc.field_71439_g == null) {
            return;
        }
        Speed.mc.field_71428_T.field_74278_d = 1.0f;
        SpeedMode speedMode = this.getMode();
        if (speedMode != null) {
            speedMode.onDisable();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private SpeedMode getMode() {
        String mode = (String)this.modeValue.get();
        for (SpeedMode speedMode : this.speedModes) {
            if (!speedMode.modeName.equalsIgnoreCase(mode)) continue;
            return speedMode;
        }
        return null;
    }

    private String[] getModes() {
        ArrayList<String> list = new ArrayList<String>();
        for (SpeedMode speedMode : this.speedModes) {
            list.add(speedMode.modeName);
        }
        return list.toArray(new String[0]);
    }
}

