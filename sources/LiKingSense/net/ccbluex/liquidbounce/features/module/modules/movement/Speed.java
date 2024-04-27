/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
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
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4BHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4Hop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac.AAC4SlowHop;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.LegitHop;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Speed", description="Allows you to move faster.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010-\u001a\u00020.H\u0016J\b\u0010/\u001a\u00020.H\u0016J\u0010\u00100\u001a\u00020.2\u0006\u00101\u001a\u000202H\u0007J\u0012\u00103\u001a\u00020.2\b\u00101\u001a\u0004\u0018\u000104H\u0007J\u0012\u00105\u001a\u00020.2\b\u00101\u001a\u0004\u0018\u000106H\u0007J\u0012\u00107\u001a\u00020.2\b\u00101\u001a\u0004\u0018\u000108H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0016\u0010\u0015\u001a\u0004\u0018\u00010\u00168BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0006R\u0011\u0010$\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u000eR\u0011\u0010&\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b'\u0010\u000eR\u0016\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00160\u001eX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010)R\u0014\u0010*\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b+\u0010,\u00a8\u00069"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/Speed;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aacGroundTimerValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getAacGroundTimerValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "cubecraftPortLengthValue", "getCubecraftPortLengthValue", "customSpeedValue", "getCustomSpeedValue", "customStrafeValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getCustomStrafeValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "customTimerValue", "getCustomTimerValue", "customYValue", "getCustomYValue", "mineplexGroundSpeedValue", "getMineplexGroundSpeedValue", "mode", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "getMode", "()Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "modes", "", "", "getModes", "()[Ljava/lang/String;", "portMax", "getPortMax", "resetXZValue", "getResetXZValue", "resetYValue", "getResetYValue", "speedModes", "[Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "tag", "getTag", "()Ljava/lang/String;", "onDisable", "", "onEnable", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onTick", "Lnet/ccbluex/liquidbounce/event/TickEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class Speed
extends Module {
    public final SpeedMode[] speedModes = new SpeedMode[]{new AAC4Hop(), new AAC4SlowHop(), new AAC4BHop(), new LegitHop()};
    @NotNull
    public final ListValue modeValue = new ListValue(this, "Mode", this.getModes(), "NCPBHop"){
        public final /* synthetic */ Speed this$0;

        public protected void onChange(@NotNull String oldValue, @NotNull String newValue) {
            Intrinsics.checkParameterIsNotNull((Object)oldValue, (String)"oldValue");
            Intrinsics.checkParameterIsNotNull((Object)newValue, (String)"newValue");
            if (this.this$0.getState()) {
                this.this$0.onDisable();
            }
        }

        public protected void onChanged(@NotNull String oldValue, @NotNull String newValue) {
            Intrinsics.checkParameterIsNotNull((Object)oldValue, (String)"oldValue");
            Intrinsics.checkParameterIsNotNull((Object)newValue, (String)"newValue");
            if (this.this$0.getState()) {
                this.this$0.onEnable();
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3);
        }
    };
    @NotNull
    public final FloatValue customSpeedValue = new FloatValue("CustomSpeed", 1.6f, 0.2f, 2.0f);
    @NotNull
    public final FloatValue customYValue = new FloatValue("CustomY", 0.0f, 0.0f, 4.0f);
    @NotNull
    public final FloatValue customTimerValue = new FloatValue("CustomTimer", 1.0f, 0.1f, 2.0f);
    @NotNull
    public final BoolValue customStrafeValue = new BoolValue("CustomStrafe", true);
    @NotNull
    public final BoolValue resetXZValue = new BoolValue("CustomResetXZ", false);
    @NotNull
    public final BoolValue resetYValue = new BoolValue("CustomResetY", false);
    @NotNull
    public final FloatValue portMax = new FloatValue("AAC-PortLength", 1.0f, 1.0f, 20.0f);
    @NotNull
    public final FloatValue aacGroundTimerValue = new FloatValue("AACGround-Timer", 3.0f, 1.1f, 10.0f);
    @NotNull
    public final FloatValue cubecraftPortLengthValue = new FloatValue("CubeCraft-PortLength", 1.0f, 0.1f, 2.0f);
    @NotNull
    public final FloatValue mineplexGroundSpeedValue = new FloatValue("MineplexGround-Speed", 0.5f, 0.1f, 1.0f);

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @NotNull
    public final FloatValue getCustomSpeedValue() {
        return this.customSpeedValue;
    }

    @NotNull
    public final FloatValue getCustomYValue() {
        return this.customYValue;
    }

    @NotNull
    public final FloatValue getCustomTimerValue() {
        return this.customTimerValue;
    }

    @NotNull
    public final BoolValue getCustomStrafeValue() {
        return this.customStrafeValue;
    }

    @NotNull
    public final BoolValue getResetXZValue() {
        return this.resetXZValue;
    }

    @NotNull
    public final BoolValue getResetYValue() {
        return this.resetYValue;
    }

    @NotNull
    public final FloatValue getPortMax() {
        return this.portMax;
    }

    @NotNull
    public final FloatValue getAacGroundTimerValue() {
        return this.aacGroundTimerValue;
    }

    @NotNull
    public final FloatValue getCubecraftPortLengthValue() {
        return this.cubecraftPortLengthValue;
    }

    @NotNull
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
    public final void onMotion(@NotNull MotionEvent event) {
        block3: {
            Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
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
        block1: {
            if (MinecraftInstance.mc.getThePlayer().isSneaking()) {
                return;
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block1;
            speedMode.onMove(event);
        }
    }

    @EventTarget
    public final void onTick(@Nullable TickEvent event) {
        block1: {
            if (MinecraftInstance.mc.getThePlayer().isSneaking()) {
                return;
            }
            SpeedMode speedMode = this.getMode();
            if (speedMode == null) break block1;
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
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    /*
     * WARNING - void declaration
     */
    public final SpeedMode getMode() {
        void var3_5;
        String mode = (String)this.modeValue.get();
        SpeedMode[] speedModeArray = this.speedModes;
        int n = speedModeArray.length;
        while (var3_5 < n) {
            SpeedMode speedMode = speedModeArray[var3_5];
            if (StringsKt.equals((String)speedMode.getModeName(), (String)mode, (boolean)true)) {
                return speedMode;
            }
            ++var3_5;
        }
        return null;
    }

    /*
     * WARNING - void declaration
     */
    public final String[] getModes() {
        Collection $this$toTypedArray$iv;
        void var3_5;
        List list = new ArrayList();
        SpeedMode[] speedModeArray = this.speedModes;
        int n = speedModeArray.length;
        while (var3_5 < n) {
            SpeedMode speedMode = speedModeArray[var3_5];
            list.add(speedMode.getModeName());
            ++var3_5;
        }
        Collection thisCollection$iv = $this$toTypedArray$iv = (Collection)list;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return stringArray;
    }
}

