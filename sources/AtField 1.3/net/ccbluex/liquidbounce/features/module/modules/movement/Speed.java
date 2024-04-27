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
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4Hop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4SlowHop;
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
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.LegitHop;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Speed", description="Allows you to move faster.", category=ModuleCategory.MOVEMENT)
public final class Speed
extends Module {
    private final FloatValue portMax;
    private final FloatValue aacGroundTimerValue;
    private final BoolValue customStrafeValue;
    private final FloatValue customTimerValue;
    private final ListValue modeValue;
    private final BoolValue resetYValue;
    private final FloatValue cubecraftPortLengthValue;
    private final FloatValue mineplexGroundSpeedValue;
    private final FloatValue customSpeedValue;
    private final SpeedMode[] speedModes = new SpeedMode[]{new AAC4Hop(), new AAC4SlowHop(), new AACBHop(), new AAC2BHop(), new AAC3BHop(), new AAC4BHop(), new AAC5BHop(), new AAC6BHop(), new AAC7BHop(), new AACHop3313(), new AACHop350(), new AACLowHop(), new AACLowHop2(), new AACLowHop3(), new AACGround(), new AACGround2(), new AACYPort(), new AACYPort2(), new AACPort(), new OldAACBHop(), new LegitHop()};
    private final BoolValue resetXZValue;
    private final FloatValue customYValue;

    @EventTarget
    public final void onMove(@Nullable MoveEvent moveEvent) {
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
            MoveEvent moveEvent2 = moveEvent;
            if (moveEvent2 == null) {
                Intrinsics.throwNpe();
            }
            speedMode.onMove(moveEvent2);
        }
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        block3: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
            if (iEntityPlayerSP2.isSneaking()) {
                return;
            }
            if (MovementUtils.isMoving()) {
                iEntityPlayerSP2.setSprinting(true);
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block3;
            speedMode.onUpdate();
        }
    }

    private final String[] getModes() {
        List list = new ArrayList();
        for (SpeedMode object2 : this.speedModes) {
            list.add(object2.getModeName());
        }
        Collection collection = list;
        int n = 0;
        Collection object3 = collection;
        String[] stringArray = object3.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return stringArray;
    }

    public final BoolValue getResetXZValue() {
        return this.resetXZValue;
    }

    public final FloatValue getMineplexGroundSpeedValue() {
        return this.mineplexGroundSpeedValue;
    }

    public final FloatValue getPortMax() {
        return this.portMax;
    }

    private final SpeedMode getMode() {
        String string = (String)this.modeValue.get();
        for (SpeedMode speedMode : this.speedModes) {
            if (!StringsKt.equals((String)speedMode.getModeName(), (String)string, (boolean)true)) continue;
            return speedMode;
        }
        return null;
    }

    public final BoolValue getCustomStrafeValue() {
        return this.customStrafeValue;
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

    public final FloatValue getCustomYValue() {
        return this.customYValue;
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final FloatValue getCustomSpeedValue() {
        return this.customSpeedValue;
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
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public final FloatValue getCustomTimerValue() {
        return this.customTimerValue;
    }

    public final BoolValue getResetYValue() {
        return this.resetYValue;
    }

    public Speed() {
        this.modeValue = new ListValue(this, "Mode", this.getModes(), "NCPBHop"){
            final Speed this$0;

            public void onChanged(Object object, Object object2) {
                this.onChanged((String)object, (String)object2);
            }

            protected void onChanged(String string, String string2) {
                if (this.this$0.getState()) {
                    this.this$0.onEnable();
                }
            }

            static {
            }

            public void onChange(Object object, Object object2) {
                this.onChange((String)object, (String)object2);
            }

            protected void onChange(String string, String string2) {
                if (this.this$0.getState()) {
                    this.this$0.onDisable();
                }
            }
            {
                this.this$0 = speed;
                super(string, stringArray, string2);
            }
        };
        this.customSpeedValue = new FloatValue("CustomSpeed", 1.6f, 0.2f, 2.0f);
        this.customYValue = new FloatValue("CustomY", 0.0f, 0.0f, 4.0f);
        this.customTimerValue = new FloatValue("CustomTimer", 1.0f, 0.1f, 2.0f);
        this.customStrafeValue = new BoolValue("CustomStrafe", true);
        this.resetXZValue = new BoolValue("CustomResetXZ", false);
        this.resetYValue = new BoolValue("CustomResetY", false);
        this.portMax = new FloatValue("AAC-PortLength", 1.0f, 1.0f, 20.0f);
        this.aacGroundTimerValue = new FloatValue("AACGround-Timer", 3.0f, 1.1f, 10.0f);
        this.cubecraftPortLengthValue = new FloatValue("CubeCraft-PortLength", 1.0f, 0.1f, 2.0f);
        this.mineplexGroundSpeedValue = new FloatValue("MineplexGround-Speed", 0.5f, 0.1f, 1.0f);
    }

    public final FloatValue getAacGroundTimerValue() {
        return this.aacGroundTimerValue;
    }

    @EventTarget
    public final void onTick(@Nullable TickEvent tickEvent) {
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

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        block3: {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
            if (iEntityPlayerSP2.isSneaking() || motionEvent.getEventState() != EventState.PRE) {
                return;
            }
            if (MovementUtils.isMoving()) {
                iEntityPlayerSP2.setSprinting(true);
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block3;
            speedMode.onMotion();
        }
    }

    public final FloatValue getCubecraftPortLengthValue() {
        return this.cubecraftPortLengthValue;
    }
}

