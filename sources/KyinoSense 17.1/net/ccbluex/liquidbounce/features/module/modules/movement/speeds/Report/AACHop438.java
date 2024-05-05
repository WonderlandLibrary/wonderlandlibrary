/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.Report;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\u0004H\u0016J(\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/Report/AACHop438;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "setMotion", "baseMoveSpeed", "", "d", "b", "", "KyinoClient"})
public final class AACHop438
extends SpeedMode {
    @Override
    public void onMotion() {
    }

    @Override
    public void onUpdate() {
        EntityPlayerSP entityPlayerSP = AACHop438.access$getMc$p$s361255530().field_71439_g;
        if (entityPlayerSP == null) {
            return;
        }
        EntityPlayerSP thePlayer = entityPlayerSP;
        AACHop438.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
        if (!MovementUtils.isMoving() || thePlayer.func_70090_H() || thePlayer.func_180799_ab() || thePlayer.func_70617_f_() || thePlayer.func_70115_ae()) {
            return;
        }
        if (thePlayer.field_70122_E) {
            thePlayer.func_70664_aZ();
        } else {
            AACHop438.access$getMc$p$s361255530().field_71428_T.field_74278_d = (double)thePlayer.field_70143_R <= 0.1 ? 1.5f : ((double)thePlayer.field_70143_R < 1.3 ? 0.7f : 1.0f);
        }
    }

    @Override
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void setMotion(@NotNull MoveEvent event, double baseMoveSpeed, double d, boolean b) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    public AACHop438() {
        super("AACHop4.3.8");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s361255530() {
        return SpeedMode.mc;
    }
}

