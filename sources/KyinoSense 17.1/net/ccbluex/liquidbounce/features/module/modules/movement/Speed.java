/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.AAC4BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.AAC4Hop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.AAC5Bhop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.AAC5Fast;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.AAC5Long;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.AACHop438;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.Blocksmc;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.BlocksmcHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.BlocksmcHopTest;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.BlocksmcLowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.BlocksmcNew;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.BlocksmcTest2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.BlocksmcTimer;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.CNHypixel;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.DeadInside;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.HuaYuTing;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.Hypixel;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.LongJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.Matrix;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report.NewHypixelHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Speed", description="Allows you to move faster.", category=ModuleCategory.MOVEMENT, keyBind=46)
public class Speed
extends Module {
    private final SpeedMode[] speedModes = new SpeedMode[]{new BlocksmcTest2(), new BlocksmcHopTest(), new BlocksmcLowHop(), new BlocksmcHop(), new BlocksmcTimer(){

        @Override
        public void setMotion(MoveEvent event, double baseMoveSpeed, double d, boolean b) {
        }
    }, new BlocksmcNew(), new Blocksmc(), new AAC5Bhop(), new AAC5Fast(), new AAC5Long(), new AAC4BHop(), new AAC4Hop(), new DeadInside(), new HuaYuTing(), new LongJump(), new CNHypixel(), new NewHypixelHop(), new Hypixel(), new Matrix(), new AACHop438()};
    public final ListValue modeValue = new ListValue("Mode", this.getModes(), "BlocksmcTimer"){

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
    public final BoolValue DamageBoostValue = new BoolValue("BlocksmcTest-DamageBoost", true);
    public final BoolValue TimerValue = new BoolValue("BlocksmcTest-Timer", true);
    public final FloatValue HypixelTimerValue = new FloatValue("Hypixel-MaxTimer", 2.3f, 0.2f, 5.0f);
    public final FloatValue HypixelDealyTimerValue = new FloatValue("Hypixel-MinTimer", 0.7f, 0.2f, 5.0f);
    public final FloatValue speedtimerValue = new FloatValue("Speed-Timer", 1.0f, 0.1f, 3.0f);
    public final FloatValue BlocksmcTestSpeed = new FloatValue("BlocksmcTest2-Speed-Speed", 0.2f, 0.1f, 3.0f);
    public final FloatValue BlocksmcTestStrafe = new FloatValue("BlocksmcTest2-Speed-Strafe", 0.48f, 0.1f, 3.0f);
    public final FloatValue speedYValue = new FloatValue("Speed-Y", 0.42f, 0.2f, 2.0f);

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

    @EventTarget
    public void onPacket(PacketEvent event) {
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

