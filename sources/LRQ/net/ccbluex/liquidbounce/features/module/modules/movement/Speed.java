/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC2BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC3BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC5BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC6BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC7BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACGround2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACHop3313;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACHop350;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACLowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACLowHop2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACLowHop3;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACYPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AACYPort2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.OldAACBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit.AAC4Hop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aquavit.AAC4SlowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.Boost;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.Frame;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.MiJump;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPFHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.NCPYPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.OnGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.SNCPBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.YPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.YPort2;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.CustomSpeed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.HiveHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.HypixelHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.MineplexGround;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.SlowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.TeleportCubeCraft;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spartan.SpartanYPort;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreBHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreLowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.spectre.SpectreOnGround;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Speed", description="Allows you to move faster.", category=ModuleCategory.MOVEMENT)
public final class Speed
extends Module {
    private final SpeedMode[] speedModes = new SpeedMode[]{new AAC4Hop(), new AAC4SlowHop(), new NCPBHop(), new NCPFHop(), new SNCPBHop(), new NCPHop(), new YPort(), new YPort2(), new NCPYPort(), new Boost(), new Frame(), new MiJump(), new OnGround(), new AACBHop(), new AAC2BHop(), new AAC3BHop(), new AAC4BHop(), new AAC5BHop(), new AAC6BHop(), new AAC7BHop(), new AACHop3313(), new AACHop350(), new AACLowHop(), new AACLowHop2(), new AACLowHop3(), new AACGround(), new AACGround2(), new AACYPort(), new AACYPort2(), new AACPort(), new OldAACBHop(), new SpartanYPort(), new SpectreLowHop(), new SpectreBHop(), new SpectreOnGround(), new TeleportCubeCraft(), new HiveHop(), new HypixelHop(), new MineplexGround(), new SlowHop(), new CustomSpeed()};
    private final ListValue modeValue = new ListValue(this, "Mode", this.getModes(), "NCPBHop"){
        final /* synthetic */ Speed this$0;

        protected void onChange(String oldValue, String newValue) {
            if (this.this$0.getState()) {
                this.this$0.onDisable();
            }
        }

        protected void onChanged(String oldValue, String newValue) {
            if (this.this$0.getState()) {
                this.this$0.onEnable();
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3);
        }
    };
    private final FloatValue customSpeedValue = new FloatValue("CustomSpeed", 1.6f, 0.2f, 2.0f);
    private final FloatValue customYValue = new FloatValue("CustomY", 0.0f, 0.0f, 4.0f);
    private final FloatValue customTimerValue = new FloatValue("CustomTimer", 1.0f, 0.1f, 2.0f);
    private final BoolValue customStrafeValue = new BoolValue("CustomStrafe", true);
    private final BoolValue resetXZValue = new BoolValue("CustomResetXZ", false);
    private final BoolValue resetYValue = new BoolValue("CustomResetY", false);
    private final FloatValue portMax = new FloatValue("AAC-PortLength", 1.0f, 1.0f, 20.0f);
    private final FloatValue aacGroundTimerValue = new FloatValue("AACGround-Timer", 3.0f, 1.1f, 10.0f);
    private final FloatValue cubecraftPortLengthValue = new FloatValue("CubeCraft-PortLength", 1.0f, 0.1f, 2.0f);
    private final FloatValue mineplexGroundSpeedValue = new FloatValue("MineplexGround-Speed", 0.5f, 0.1f, 1.0f);

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final FloatValue getCustomSpeedValue() {
        return this.customSpeedValue;
    }

    public final FloatValue getCustomYValue() {
        return this.customYValue;
    }

    public final FloatValue getCustomTimerValue() {
        return this.customTimerValue;
    }

    public final BoolValue getCustomStrafeValue() {
        return this.customStrafeValue;
    }

    public final BoolValue getResetXZValue() {
        return this.resetXZValue;
    }

    public final BoolValue getResetYValue() {
        return this.resetYValue;
    }

    public final FloatValue getPortMax() {
        return this.portMax;
    }

    public final FloatValue getAacGroundTimerValue() {
        return this.aacGroundTimerValue;
    }

    public final FloatValue getCubecraftPortLengthValue() {
        return this.cubecraftPortLengthValue;
    }

    public final FloatValue getMineplexGroundSpeedValue() {
        return this.mineplexGroundSpeedValue;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        block3: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP thePlayer = iEntityPlayerSP;
            if (thePlayer.isSneaking()) {
                return;
            }
            if (MovementUtils.isMoving()) {
                thePlayer.setSprinting(true);
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block3;
            speedMode.onUpdate();
        }
    }

    @EventTarget
    public final void onMotion(MotionEvent event) {
        block3: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP thePlayer = iEntityPlayerSP;
            if (thePlayer.isSneaking() || event.getEventState() != EventState.PRE) {
                return;
            }
            if (MovementUtils.isMoving()) {
                thePlayer.setSprinting(true);
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block3;
            speedMode.onMotion();
        }
    }

    @EventTarget
    public final void onMove(@Nullable MoveEvent event) {
        block3: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.isSneaking()) {
                return;
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block3;
            MoveEvent moveEvent = event;
            if (moveEvent == null) {
                Intrinsics.throwNpe();
            }
            speedMode.onMove(moveEvent);
        }
    }

    @EventTarget
    public final void onTick(@Nullable TickEvent event) {
        block2: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.isSneaking()) {
                return;
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block2;
            speedMode.onTick();
        }
    }

    @Override
    public void onEnable() {
        block1: {
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block1;
            speedMode.onEnable();
        }
    }

    @Override
    public void onDisable() {
        block1: {
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block1;
            speedMode.onDisable();
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private final SpeedMode getMode() {
        String mode = (String)this.modeValue.get();
        for (SpeedMode speedMode : this.speedModes) {
            if (!StringsKt.equals((String)speedMode.getModeName(), (String)mode, (boolean)true)) continue;
            return speedMode;
        }
        return null;
    }

    private final String[] getModes() {
        List list = new ArrayList();
        for (SpeedMode speedMode : this.speedModes) {
            list.add(speedMode.getModeName());
        }
        Collection $this$toTypedArray$iv = list;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return stringArray;
    }
}

