/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
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
import net.minecraft.client.entity.EntityPlayerSP;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0017J\b\u0010\u0005\u001a\u00020\u0004H\u0017J\u0012\u0010\u0006\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0017J\b\u0010\t\u001a\u00020\u0004H\u0017J(\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/Report/AAC5Bhop;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "setMotion", "baseMoveSpeed", "", "d", "b", "", "KyinoClient"})
public final class AAC5Bhop
extends SpeedMode {
    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
        EntityPlayerSP entityPlayerSP = AAC5Bhop.access$getMc$p$s361255530().field_71439_g;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (entityPlayerSP.func_70090_H()) {
            return;
        }
        if (!MovementUtils.isMoving()) {
            return;
        }
        if (AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_70122_E) {
            AAC5Bhop.access$getMc$p$s361255530().field_71474_y.field_74314_A.field_74513_e = false;
            AAC5Bhop.access$getMc$p$s361255530().field_71439_g.func_70664_aZ();
        }
        if (!AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_70122_E && (double)AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_70143_R <= 0.1) {
            AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
            AAC5Bhop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.5f;
        }
        if ((double)AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_70143_R > 0.1 && (double)AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_70143_R < 1.3) {
            AAC5Bhop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 0.7f;
        }
        if ((double)AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_70143_R >= 1.3) {
            AAC5Bhop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
            AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(@Nullable MoveEvent event) {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onDisable() {
        if (AAC5Bhop.access$getMc$p$s361255530().field_71439_g == null) {
            Intrinsics.throwNpe();
        }
        AAC5Bhop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
        AAC5Bhop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
    }

    @Override
    public void setMotion(@NotNull MoveEvent event, double baseMoveSpeed, double d, boolean b) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    public AAC5Bhop() {
        super("AAC5.2.0Bhop");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s361255530() {
        return SpeedMode.mc;
    }
}

