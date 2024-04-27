/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.dev.important.event.EventTarget;
import net.dev.important.event.StrafeEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.Rotation;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.extensions.PlayerExtensionKt;
import net.dev.important.utils.misc.RandomUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

@Info(name="Aimbot", description="Automatically faces selected entities around you.", category=Category.COMBAT, cnName="\u81ea\u52a8\u7784\u51c6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/modules/module/modules/combat/Aimbot;", "Lnet/dev/important/modules/module/Module;", "()V", "centerValue", "Lnet/dev/important/value/BoolValue;", "clickTimer", "Lnet/dev/important/utils/timer/MSTimer;", "fovValue", "Lnet/dev/important/value/FloatValue;", "jitterValue", "lockValue", "onClickValue", "rangeValue", "turnSpeedValue", "onStrafe", "", "event", "Lnet/dev/important/event/StrafeEvent;", "LiquidBounce"})
public final class Aimbot
extends Module {
    @NotNull
    private final FloatValue rangeValue = new FloatValue("Range", 4.4f, 1.0f, 8.0f, "m");
    @NotNull
    private final FloatValue turnSpeedValue = new FloatValue("TurnSpeed", 2.0f, 1.0f, 180.0f, "\u00b0");
    @NotNull
    private final FloatValue fovValue = new FloatValue("FOV", 180.0f, 1.0f, 180.0f, "\u00b0");
    @NotNull
    private final BoolValue centerValue = new BoolValue("Center", false);
    @NotNull
    private final BoolValue lockValue = new BoolValue("Lock", true);
    @NotNull
    private final BoolValue onClickValue = new BoolValue("OnClick", false);
    @NotNull
    private final BoolValue jitterValue = new BoolValue("Jitter", false);
    @NotNull
    private final MSTimer clickTimer = new MSTimer();

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @EventTarget
    public final void onStrafe(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71474_y.field_74312_F.func_151470_d()) {
            this.clickTimer.reset();
        }
        if (((Boolean)this.onClickValue.get()).booleanValue() && this.clickTimer.hasTimePassed(500L)) {
            return;
        }
        range = ((Number)this.rangeValue.get()).floatValue();
        var5_3 /* !! */  = MinecraftInstance.mc.field_71441_e.field_72996_f;
        Intrinsics.checkNotNullExpressionValue(var5_3 /* !! */ , "mc.theWorld.loadedEntityList");
        var5_3 /* !! */  = var5_3 /* !! */ ;
        $i$f$filter = false;
        var7_6 = $this$filter$iv;
        destination$iv$iv = new ArrayList<E>();
        $i$f$filterTo = false;
        for (T element$iv$iv : $this$filterTo$iv$iv) {
            it = (Entity)element$iv$iv;
            $i$a$-filter-Aimbot$onStrafe$entity$1 = false;
            if (!EntityUtils.isSelected(it, true) || !MinecraftInstance.mc.field_71439_g.func_70685_l(it)) ** GOTO lbl-1000
            var16_24 = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNullExpressionValue(var16_24, "mc.thePlayer");
            v0 = (Entity)var16_24;
            Intrinsics.checkNotNullExpressionValue(it, "it");
            if (PlayerExtensionKt.getDistanceToEntityBox(v0, it) <= (double)range && RotationUtils.getRotationDifference(it) <= (double)((Number)this.fovValue.get()).floatValue()) {
                v1 = true;
            } else lbl-1000:
            // 2 sources

            {
                v1 = false;
            }
            if (!v1) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        $this$filter$iv = (List)destination$iv$iv;
        $i$f$minByOrNull = false;
        iterator$iv = $this$minByOrNull$iv.iterator();
        if (!iterator$iv.hasNext()) {
            v2 = null;
        } else {
            minElem$iv = iterator$iv.next();
            if (!iterator$iv.hasNext()) {
                v2 = minElem$iv;
            } else {
                it = (Entity)minElem$iv;
                $i$a$-minByOrNull-Aimbot$onStrafe$entity$2 = false;
                minValue$iv = RotationUtils.getRotationDifference(it);
                do {
                    e$iv = iterator$iv.next();
                    it = (Entity)e$iv;
                    $i$a$-minByOrNull-Aimbot$onStrafe$entity$2 = false;
                    v$iv = RotationUtils.getRotationDifference(it);
                    if (Double.compare(minValue$iv, v$iv) <= 0) continue;
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                } while (iterator$iv.hasNext());
                v2 = minElem$iv;
            }
        }
        var4_25 = v2;
        if (var4_25 == null) {
            return;
        }
        entity = var4_25;
        if (!((Boolean)this.lockValue.get()).booleanValue() && RotationUtils.isFaced(entity, range)) {
            return;
        }
        $this$minByOrNull$iv = RotationUtils.limitAngleChange(new Rotation(MinecraftInstance.mc.field_71439_g.field_70177_z, MinecraftInstance.mc.field_71439_g.field_70125_A), (Boolean)this.centerValue.get() != false ? RotationUtils.toRotation(RotationUtils.getCenter(entity.func_174813_aQ()), true) : RotationUtils.searchCenter(entity.func_174813_aQ(), false, false, true, false, range).getRotation(), (float)(((Number)this.turnSpeedValue.get()).doubleValue() + Math.random()));
        Intrinsics.checkNotNullExpressionValue($this$minByOrNull$iv, "limitAngleChange(\n      \u2026om()).toFloat()\n        )");
        rotation = $this$minByOrNull$iv;
        $this$minByOrNull$iv = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNullExpressionValue($this$minByOrNull$iv, "mc.thePlayer");
        rotation.toPlayer((EntityPlayer)$this$minByOrNull$iv);
        if (((Boolean)this.jitterValue.get()).booleanValue()) {
            yaw = Random.Default.nextBoolean();
            pitch = Random.Default.nextBoolean();
            yawNegative = Random.Default.nextBoolean();
            pitchNegative = Random.Default.nextBoolean();
            if (yaw) {
                var9_13 = MinecraftInstance.mc.field_71439_g;
                var9_13.field_70177_z = var9_13.field_70177_z + (yawNegative != false ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
            }
            if (pitch) {
                var9_14 = MinecraftInstance.mc.field_71439_g;
                var9_14.field_70125_A = var9_14.field_70125_A + (pitchNegative != false ? -RandomUtils.nextFloat(0.0f, 1.0f) : RandomUtils.nextFloat(0.0f, 1.0f));
                if (MinecraftInstance.mc.field_71439_g.field_70125_A > 90.0f) {
                    MinecraftInstance.mc.field_71439_g.field_70125_A = 90.0f;
                } else if (MinecraftInstance.mc.field_71439_g.field_70125_A < -90.0f) {
                    MinecraftInstance.mc.field_71439_g.field_70125_A = -90.0f;
                }
            }
        }
    }
}

