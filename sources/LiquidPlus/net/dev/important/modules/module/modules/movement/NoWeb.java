/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.JumpEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="NoWeb", spacedName="No Web", description="Prevents you from getting slowed down in webs.", category=Category.MOVEMENT, cnName="\u65e0\u51cf\u901f")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016J\u000e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/dev/important/modules/module/modules/movement/NoWeb;", "Lnet/dev/important/modules/module/Module;", "()V", "horizonSpeed", "Lnet/dev/important/value/FloatValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "usedTimer", "", "onDisable", "", "onJump", "event", "Lnet/dev/important/event/JumpEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class NoWeb
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final FloatValue horizonSpeed;
    private boolean usedTimer;

    public NoWeb() {
        String[] stringArray = new String[]{"None", "AAC", "LAAC", "Rewi", "AACv4", "Cardinal", "Horizon", "Spartan", "Negativity"};
        this.modeValue = new ListValue("Mode", stringArray, "None");
        this.horizonSpeed = new FloatValue("HorizonSpeed", 0.1f, 0.01f, 0.8f);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.usedTimer) {
            MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        if (!MinecraftInstance.mc.field_71439_g.field_70134_J) {
            return;
        }
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "none": {
                MinecraftInstance.mc.field_71439_g.field_70134_J = false;
                break;
            }
            case "aac": {
                MinecraftInstance.mc.field_71439_g.field_70747_aH = 0.59f;
                if (MinecraftInstance.mc.field_71474_y.field_74311_E.func_151470_d()) break;
                MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "laac": {
                float f = MinecraftInstance.mc.field_71439_g.field_70747_aH = !(MinecraftInstance.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) ? 1.0f : 1.21f;
                if (!MinecraftInstance.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
                }
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) break;
                MinecraftInstance.mc.field_71439_g.func_70664_aZ();
                break;
            }
            case "rewi": {
                MinecraftInstance.mc.field_71439_g.field_70747_aH = 0.42f;
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) break;
                MinecraftInstance.mc.field_71439_g.func_70664_aZ();
                break;
            }
            case "aacv4": {
                MinecraftInstance.mc.field_71474_y.field_74366_z.field_74513_e = false;
                MinecraftInstance.mc.field_71474_y.field_74368_y.field_74513_e = false;
                MinecraftInstance.mc.field_71474_y.field_74370_x.field_74513_e = false;
                if (MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    MovementUtils.strafe(0.25f);
                    break;
                }
                MovementUtils.strafe(0.12f);
                MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "cardinal": {
                if (MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    MovementUtils.strafe(0.262f);
                    break;
                }
                MovementUtils.strafe(0.366f);
                break;
            }
            case "horizon": {
                if (!MinecraftInstance.mc.field_71439_g.field_70122_E) break;
                MovementUtils.strafe(((Number)this.horizonSpeed.get()).floatValue());
                break;
            }
            case "spartan": {
                MovementUtils.strafe(0.27f);
                MinecraftInstance.mc.field_71428_T.field_74278_d = 3.7f;
                if (!MinecraftInstance.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 2 == 0) {
                    MinecraftInstance.mc.field_71428_T.field_74278_d = 1.7f;
                }
                if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 40 == 0) {
                    MinecraftInstance.mc.field_71428_T.field_74278_d = 3.0f;
                }
                this.usedTimer = true;
                break;
            }
            case "negativity": {
                MinecraftInstance.mc.field_71439_g.field_70747_aH = 0.4f;
                if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 2 == 0) {
                    MinecraftInstance.mc.field_71439_g.field_70747_aH = 0.53f;
                }
                if (MinecraftInstance.mc.field_71474_y.field_74311_E.func_151470_d()) break;
                MinecraftInstance.mc.field_71439_g.field_70181_x = 0.0;
            }
        }
    }

    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (StringsKt.equals((String)this.modeValue.get(), "AACv4", true) || StringsKt.equals((String)this.modeValue.get(), "Negativity", true) || StringsKt.equals((String)this.modeValue.get(), "Intave", true)) {
            event.cancelEvent();
        }
    }

    @Override
    public void onDisable() {
        MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

