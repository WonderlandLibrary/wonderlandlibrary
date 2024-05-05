/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Rotate", category=ModuleCategory.RENDER, description=":/")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lme/report/liquidware/modules/render/Rotate;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "lastSpin", "", "pitch", "getPitch", "()F", "setPitch", "(F)V", "pitchJitterTimer", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "pitchMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getPitchMode", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "pitchTimer", "", "static_offsetPitch", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "static_offsetYaw", "yawJitterTimer", "yawMode", "yawSpinSpeed", "yawTimer", "onDisable", "", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "KyinoClient"})
public final class Rotate
extends Module {
    private final ListValue yawMode = new ListValue("Yaw", new String[]{"Static", "Offset", "Random", "Jitter", "Spin", "None"}, "None");
    @NotNull
    private final ListValue pitchMode = new ListValue("Pitch", new String[]{"Static", "Offset", "Random", "Jitter", "None"}, "Static");
    private final FloatValue static_offsetYaw = new FloatValue("Static/Offset-Yaw", 0.0f, -180.0f, 180.0f);
    private final FloatValue static_offsetPitch = new FloatValue("Static/Offset-Pitch", 0.0f, -90.0f, 90.0f);
    private final IntegerValue yawJitterTimer = new IntegerValue("YawJitterTimer", 40, 1, 40);
    private final IntegerValue pitchJitterTimer = new IntegerValue("PitchJitterTimer", 1, 1, 40);
    private final FloatValue yawSpinSpeed = new FloatValue("YawSpinSpeed", 5.0f, -90.0f, 90.0f);
    private float pitch;
    private float lastSpin;
    private int yawTimer;
    private int pitchTimer;

    @NotNull
    public final ListValue getPitchMode() {
        return this.pitchMode;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float f) {
        this.pitch = f;
    }

    @Override
    public void onDisable() {
        this.pitch = -4.9531336E7f;
        this.lastSpin = 0.0f;
        this.yawTimer = 0;
        this.pitchTimer = 0;
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Object object;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Rotate.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (!StringsKt.equals((String)this.yawMode.get(), "none", true)) {
            float yaw = 0.0f;
            object = (String)this.yawMode.get();
            Locale locale = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.getDefault()");
            Locale locale2 = locale;
            boolean bl = false;
            Object object2 = object;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string = ((String)object2).toLowerCase(locale2);
            Intrinsics.checkExpressionValueIsNotNull(string, "(this as java.lang.String).toLowerCase(locale)");
            switch (string) {
                case "static": {
                    yaw = ((Number)this.static_offsetYaw.get()).floatValue();
                    break;
                }
                case "offset": {
                    yaw = Rotate.access$getMc$p$s1046033730().field_71439_g.field_70177_z + ((Number)this.static_offsetYaw.get()).floatValue();
                    break;
                }
                case "random": {
                    yaw = (float)Math.floor(Math.random() * 360.0 - 180.0);
                    break;
                }
                case "jitter": {
                    int n = this.yawTimer;
                    this.yawTimer = n + 1;
                    if (n % (((Number)this.yawJitterTimer.get()).intValue() * 2) >= ((Number)this.yawJitterTimer.get()).intValue()) {
                        yaw = Rotate.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
                        break;
                    }
                    yaw = Rotate.access$getMc$p$s1046033730().field_71439_g.field_70177_z - 180.0f;
                    break;
                }
                case "spin": {
                    this.lastSpin += ((Number)this.yawSpinSpeed.get()).floatValue();
                    yaw = this.lastSpin;
                }
            }
            Rotate.access$getMc$p$s1046033730().field_71439_g.field_70761_aq = yaw;
            Rotate.access$getMc$p$s1046033730().field_71439_g.field_70759_as = yaw;
            this.lastSpin = yaw;
        }
        String string = (String)this.pitchMode.get();
        Locale locale = Locale.getDefault();
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.getDefault()");
        object = locale;
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase((Locale)object);
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase(locale)");
        switch (string3) {
            case "static": {
                this.pitch = ((Number)this.static_offsetPitch.get()).floatValue();
                break;
            }
            case "offset": {
                this.pitch = Rotate.access$getMc$p$s1046033730().field_71439_g.field_70125_A + ((Number)this.static_offsetPitch.get()).floatValue();
                break;
            }
            case "random": {
                this.pitch = (float)Math.floor(Math.random() * 180.0 - 90.0);
                break;
            }
            case "jitter": {
                int n = this.pitchTimer;
                this.pitchTimer = n + 1;
                this.pitch = n % (((Number)this.pitchJitterTimer.get()).intValue() * 2) >= ((Number)this.pitchJitterTimer.get()).intValue() ? 90.0f : -90.0f;
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

