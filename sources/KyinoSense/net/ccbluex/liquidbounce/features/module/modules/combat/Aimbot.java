/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AimBot", description="Automatically faces selected entities around you.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Aimbot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "centerValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "clickTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "fovValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "jitterValue", "lockValue", "onClickValue", "rangeValue", "turnSpeedValue", "onStrafe", "", "event", "Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "KyinoClient"})
public final class Aimbot
extends Module {
    private final FloatValue rangeValue = new FloatValue("Range", 4.4f, 1.0f, 8.0f);
    private final FloatValue turnSpeedValue = new FloatValue("TurnSpeed", 2.0f, 1.0f, 180.0f);
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 1.0f, 180.0f);
    private final BoolValue centerValue = new BoolValue("Center", false);
    private final BoolValue lockValue = new BoolValue("Lock", true);
    private final BoolValue onClickValue = new BoolValue("OnClick", false);
    private final BoolValue jitterValue = new BoolValue("Jitter", false);
    private final MSTimer clickTimer = new MSTimer();

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        v0 = Aimbot.access$getMc$p$s1046033730().field_71474_y.field_74312_F;
        Intrinsics.checkExpressionValueIsNotNull(v0, "mc.gameSettings.keyBindAttack");
        if (v0.func_151470_d()) {
            this.clickTimer.reset();
        }
        if (((Boolean)this.onClickValue.get()).booleanValue() && this.clickTimer.hasTimePassed(500L)) {
            return;
        }
        range = ((Number)this.rangeValue.get()).floatValue();
        v1 = Aimbot.access$getMc$p$s1046033730().field_71441_e.field_72996_f;
        Intrinsics.checkExpressionValueIsNotNull(v1, "mc.theWorld.loadedEntityList");
        $this$filter$iv = v1;
        $i$f$filter = false;
        var6_5 = $this$filter$iv;
        destination$iv$iv = new ArrayList<E>();
        $i$f$filterTo = false;
        for (T element$iv$iv : $this$filterTo$iv$iv) {
            it = (Entity)element$iv$iv;
            $i$a$-filter-Aimbot$onStrafe$entity$1 = false;
            if (!EntityUtils.isSelected(it, true) || !Aimbot.access$getMc$p$s1046033730().field_71439_g.func_70685_l(it)) ** GOTO lbl-1000
            v2 = Aimbot.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(v2, "mc.thePlayer");
            v3 = (Entity)v2;
            v4 = it;
            Intrinsics.checkExpressionValueIsNotNull(v4, "it");
            if (PlayerExtensionKt.getDistanceToEntityBox(v3, v4) <= (double)range && RotationUtils.getRotationDifference(it) <= ((Number)this.fovValue.get()).doubleValue()) {
                v5 = true;
            } else lbl-1000:
            // 2 sources

            {
                v5 = false;
            }
            if (!v5) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        $this$minBy$iv = (List)destination$iv$iv;
        $i$f$minBy = false;
        iterator$iv = $this$minBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v6 = null;
        } else {
            minElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v6 = minElem$iv;
            } else {
                it = (Entity)minElem$iv;
                $i$a$-minBy-Aimbot$onStrafe$entity$2 = false;
                minValue$iv = RotationUtils.getRotationDifference(it);
                do {
                    e$iv = iterator$iv.next();
                    it = (Entity)e$iv;
                    $i$a$-minBy-Aimbot$onStrafe$entity$2 = false;
                    v$iv = RotationUtils.getRotationDifference(it);
                    if (Double.compare(minValue$iv, v$iv) <= 0) continue;
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v6 = minElem$iv;
            }
        }
        v7 = v6;
        if (v7 == null) {
            return;
        }
        entity = v7;
        if (!((Boolean)this.lockValue.get()).booleanValue() && RotationUtils.isFaced(entity, range)) {
            return;
        }
        v8 = RotationUtils.limitAngleChange(new Rotation(Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70177_z, Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A), (Boolean)this.centerValue.get() != false ? RotationUtils.toRotation(RotationUtils.getCenter(entity.func_174813_aQ()), true) : RotationUtils.searchCenter(entity.func_174813_aQ(), false, false, true, false).getRotation(), (float)(((Number)this.turnSpeedValue.get()).doubleValue() + Math.random()));
        Intrinsics.checkExpressionValueIsNotNull(v8, "RotationUtils.limitAngle\u2026om()).toFloat()\n        )");
        rotation = v8;
        v9 = Aimbot.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(v9, "mc.thePlayer");
        rotation.toPlayer((EntityPlayer)v9);
        if (((Boolean)this.jitterValue.get()).booleanValue()) {
            yaw = Random.Default.nextBoolean();
            pitch = Random.Default.nextBoolean();
            yawNegative = Random.Default.nextBoolean();
            pitchNegative = Random.Default.nextBoolean();
            if (yaw) {
                Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70177_z = Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70177_z + (yawNegative != false ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
            }
            if (pitch) {
                Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A = Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A + (pitchNegative != false ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
                if (Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A > (float)90) {
                    Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A = 90.0f;
                } else if (Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A < (float)-90) {
                    Aimbot.access$getMc$p$s1046033730().field_71439_g.field_70125_A = -90.0f;
                }
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

