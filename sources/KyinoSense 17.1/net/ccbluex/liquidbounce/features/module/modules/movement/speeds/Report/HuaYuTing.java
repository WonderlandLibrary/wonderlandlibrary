/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0017J\b\u0010\u0005\u001a\u00020\u0004H\u0017J\u0012\u0010\u0006\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0017J\b\u0010\t\u001a\u00020\u0004H\u0017J(\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/Report/HuaYuTing;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "setMotion", "baseMoveSpeed", "", "d", "b", "", "KyinoClient"})
public final class HuaYuTing
extends SpeedMode {
    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
        if (!MovementUtils.isMoving()) {
            return;
        }
        if (HuaYuTing.access$getMc$p$s361255530().field_71439_g.field_70122_E) {
            HuaYuTing.access$getMc$p$s361255530().field_71439_g.func_70664_aZ();
            HuaYuTing.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.0201f;
            HuaYuTing.access$getMc$p$s361255530().field_71428_T.field_74278_d = 0.94f;
        }
        if ((double)HuaYuTing.access$getMc$p$s361255530().field_71439_g.field_70143_R > 0.7 && (double)HuaYuTing.access$getMc$p$s361255530().field_71439_g.field_70143_R < 1.3) {
            HuaYuTing.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
            HuaYuTing.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.8f;
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(@Nullable MoveEvent event) {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onDisable() {
        if (HuaYuTing.access$getMc$p$s361255530().field_71439_g == null) {
            Intrinsics.throwNpe();
        }
        HuaYuTing.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
        HuaYuTing.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
    }

    @Override
    public void setMotion(@NotNull MoveEvent event, double baseMoveSpeed, double d, boolean b) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    public HuaYuTing() {
        super("HuaYuTing");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s361255530() {
        return SpeedMode.mc;
    }
}

