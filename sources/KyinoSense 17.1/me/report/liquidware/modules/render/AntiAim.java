/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiAim", category=ModuleCategory.RENDER, description="AntiAim CSGO")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lme/report/liquidware/modules/render/AntiAim;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "pitch", "", "pitchModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "rotateValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "yaw", "yawModeValue", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AntiAim
extends Module {
    private final ListValue yawModeValue = new ListValue("YawMove", new String[]{"Jitter", "Spin", "Back"}, "Spin");
    private final ListValue pitchModeValue = new ListValue("PitchMode", new String[]{"Down", "Up", "Jitter"}, "Down");
    private final BoolValue rotateValue = new BoolValue("SilentRotate", true);
    private float yaw;
    private float pitch;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.yawModeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "spin": {
                this.yaw += 20.0f;
                if (this.yaw > 180.0f) {
                    this.yaw = -180.0f;
                    break;
                }
                if (!(this.yaw < -180.0f)) break;
                this.yaw = 180.0f;
                break;
            }
            case "jitter": {
                this.yaw = AntiAim.access$getMc$p$s1046033730().field_71439_g.field_70177_z + (AntiAim.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 2 == 0 ? 90.0f : -90.0f);
                break;
            }
            case "back": {
                this.yaw = AntiAim.access$getMc$p$s1046033730().field_71439_g.field_70177_z + 180.0f;
            }
        }
        string = (String)this.pitchModeValue.get();
        bl = false;
        String string4 = string;
        if (string4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string5 = string4.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string5, "(this as java.lang.String).toLowerCase()");
        switch (string5) {
            case "up": {
                this.pitch = -90.0f;
                break;
            }
            case "down": {
                this.pitch = 90.0f;
                break;
            }
            case "jitter": {
                this.pitch += 30.0f;
                if (this.pitch > 90.0f) {
                    this.pitch = -90.0f;
                    break;
                }
                if (!(this.pitch < -90.0f)) break;
                this.pitch = 90.0f;
            }
        }
        if (((Boolean)this.rotateValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(new Rotation(this.yaw, this.pitch));
        } else {
            AntiAim.access$getMc$p$s1046033730().field_71439_g.field_70177_z = this.yaw;
            AntiAim.access$getMc$p$s1046033730().field_71439_g.field_70125_A = this.pitch;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

