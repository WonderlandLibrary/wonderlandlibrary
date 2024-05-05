/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.modules.render.AntiAim;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="SilentView", category=ModuleCategory.RENDER, description="CSGO View", array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0018\u001a\u00020\u00192\u000e\u0010\u001a\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u001bH\u0002J\u0006\u0010\u001c\u001a\u00020\u0019R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017\u00a8\u0006\u001d"}, d2={"Lme/report/liquidware/modules/render/SilentView;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Alpha", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getAlpha", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "setAlpha", "(Lnet/ccbluex/liquidbounce/value/FloatValue;)V", "B", "getB", "setB", "G", "getG", "setG", "R", "getR", "setR", "mode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getMode", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "setMode", "(Lnet/ccbluex/liquidbounce/value/ListValue;)V", "getState", "", "module", "Ljava/lang/Class;", "shouldRotate", "KyinoClient"})
public final class SilentView
extends Module {
    @NotNull
    private ListValue mode = new ListValue("Mode", new String[]{"Normal", "CSGO"}, "Normal");
    @NotNull
    private FloatValue R = new FloatValue("R", 154.0f, 0.0f, 255.0f);
    @NotNull
    private FloatValue G = new FloatValue("G", 114.0f, 0.0f, 255.0f);
    @NotNull
    private FloatValue B = new FloatValue("B", 175.0f, 0.0f, 255.0f);
    @NotNull
    private FloatValue Alpha = new FloatValue("Alpha", 50.0f, 0.0f, 255.0f);

    @NotNull
    public final ListValue getMode() {
        return this.mode;
    }

    public final void setMode(@NotNull ListValue listValue) {
        Intrinsics.checkParameterIsNotNull(listValue, "<set-?>");
        this.mode = listValue;
    }

    @NotNull
    public final FloatValue getR() {
        return this.R;
    }

    public final void setR(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.R = floatValue;
    }

    @NotNull
    public final FloatValue getG() {
        return this.G;
    }

    public final void setG(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.G = floatValue;
    }

    @NotNull
    public final FloatValue getB() {
        return this.B;
    }

    public final void setB(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.B = floatValue;
    }

    @NotNull
    public final FloatValue getAlpha() {
        return this.Alpha;
    }

    public final void setAlpha(@NotNull FloatValue floatValue) {
        Intrinsics.checkParameterIsNotNull(floatValue, "<set-?>");
        this.Alpha = floatValue;
    }

    private final boolean getState(Class<? extends Module> module) {
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().get(module);
        if (module2 == null) {
            Intrinsics.throwNpe();
        }
        return module2.getState();
    }

    public final boolean shouldRotate() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
        return this.getState(Scaffold.class) || this.getState(KillAura.class) && killAura.getTarget() != null || this.getState(Scaffold.class) || this.getState(AntiAim.class);
    }

    public SilentView() {
        this.setState(true);
    }
}

