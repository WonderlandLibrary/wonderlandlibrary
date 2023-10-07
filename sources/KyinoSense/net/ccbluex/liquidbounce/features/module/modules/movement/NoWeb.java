/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoWeb", description="Prevents you from getting slowed down in webs.", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/NoWeb;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "usedTimer", "", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class NoWeb
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"None", "AAC", "LAAC", "Rewi", "AAC4.4.0", "AAC5.2.0", "Matrix"}, "None");
    private boolean usedTimer;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.usedTimer) {
            NoWeb.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        if (!NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70134_J) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "none": {
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70134_J = false;
                break;
            }
            case "aac": {
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.59f;
                KeyBinding keyBinding = NoWeb.access$getMc$p$s1046033730().field_71474_y.field_74311_E;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindSneak");
                if (keyBinding.func_151470_d()) break;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                break;
            }
            case "laac": {
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = NoWeb.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78902_a != 0.0f ? 1.0f : 1.21f;
                KeyBinding keyBinding = NoWeb.access$getMc$p$s1046033730().field_71474_y.field_74311_E;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindSneak");
                if (!keyBinding.func_151470_d()) {
                    NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                }
                if (!NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                break;
            }
            case "aac4.4.0": {
                NoWeb.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.99f;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.02958f;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= 0.00775;
                if (!NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.405;
                NoWeb.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.35f;
                break;
            }
            case "aac5.2.0": {
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.42f;
                if (!NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                break;
            }
            case "matrix": {
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.12425f;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.0125;
                KeyBinding keyBinding = NoWeb.access$getMc$p$s1046033730().field_71474_y.field_74311_E;
                Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindSneak");
                if (keyBinding.func_151470_d()) {
                    NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.1625;
                }
                if (NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    NoWeb.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
                    NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.2425;
                }
                if (NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 40 != 0) break;
                NoWeb.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 3.0f;
                this.usedTimer = true;
                break;
            }
            case "rewi": {
                NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.42f;
                if (!NoWeb.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                NoWeb.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
            }
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

