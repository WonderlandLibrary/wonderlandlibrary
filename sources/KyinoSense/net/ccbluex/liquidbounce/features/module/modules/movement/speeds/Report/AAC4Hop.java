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
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0017J\b\u0010\u0005\u001a\u00020\u0004H\u0017J\b\u0010\u0006\u001a\u00020\u0004H\u0017J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0017J\b\u0010\n\u001a\u00020\u0004H\u0017J\b\u0010\u000b\u001a\u00020\u0004H\u0017J(\u0010\f\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/Report/AAC4Hop;", "Lnet/ccbluex/liquidbounce/features/module/modules/movement/speeds/SpeedMode;", "()V", "onDisable", "", "onEnable", "onMotion", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onTick", "onUpdate", "setMotion", "baseMoveSpeed", "", "d", "b", "", "KyinoClient"})
public final class AAC4Hop
extends SpeedMode {
    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onDisable() {
        AAC4Hop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
        if (AAC4Hop.access$getMc$p$s361255530().field_71439_g == null) {
            Intrinsics.throwNpe();
        }
        AAC4Hop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
    }

    @Override
    public void setMotion(@NotNull MoveEvent event, double baseMoveSpeed, double d, boolean b) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onTick() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMotion() {
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate() {
        EntityPlayerSP entityPlayerSP = AAC4Hop.access$getMc$p$s361255530().field_71439_g;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (entityPlayerSP.func_70090_H()) {
            return;
        }
        if (MovementUtils.isMoving()) {
            EntityPlayerSP entityPlayerSP2 = AAC4Hop.access$getMc$p$s361255530().field_71439_g;
            if (entityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (entityPlayerSP2.field_70122_E) {
                EntityPlayerSP entityPlayerSP3 = AAC4Hop.access$getMc$p$s361255530().field_71439_g;
                if (entityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                entityPlayerSP3.func_70664_aZ();
                if (AAC4Hop.access$getMc$p$s361255530().field_71439_g == null) {
                    Intrinsics.throwNpe();
                }
                AAC4Hop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.0201f;
                AAC4Hop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 0.94f;
            }
            EntityPlayerSP entityPlayerSP4 = AAC4Hop.access$getMc$p$s361255530().field_71439_g;
            if (entityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)entityPlayerSP4.field_70143_R > 0.7) {
                EntityPlayerSP entityPlayerSP5 = AAC4Hop.access$getMc$p$s361255530().field_71439_g;
                if (entityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                if ((double)entityPlayerSP5.field_70143_R < 1.3) {
                    if (AAC4Hop.access$getMc$p$s361255530().field_71439_g == null) {
                        Intrinsics.throwNpe();
                    }
                    AAC4Hop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
                    AAC4Hop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.8f;
                }
            }
            EntityPlayerSP entityPlayerSP6 = AAC4Hop.access$getMc$p$s361255530().field_71439_g;
            if (entityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            if ((double)entityPlayerSP6.field_70143_R >= 1.3) {
                AAC4Hop.access$getMc$p$s361255530().field_71428_T.field_74278_d = 1.0f;
                if (AAC4Hop.access$getMc$p$s361255530().field_71439_g == null) {
                    Intrinsics.throwNpe();
                }
                AAC4Hop.access$getMc$p$s361255530().field_71439_g.field_71102_ce = 0.02f;
            }
        } else {
            if (AAC4Hop.access$getMc$p$s361255530().field_71439_g == null) {
                Intrinsics.throwNpe();
            }
            AAC4Hop.access$getMc$p$s361255530().field_71439_g.field_70159_w = 0.0;
            if (AAC4Hop.access$getMc$p$s361255530().field_71439_g == null) {
                Intrinsics.throwNpe();
            }
            AAC4Hop.access$getMc$p$s361255530().field_71439_g.field_70179_y = 0.0;
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onEnable() {
    }

    public AAC4Hop() {
        super("AAC4.4.0Hop");
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s361255530() {
        return SpeedMode.mc;
    }
}

