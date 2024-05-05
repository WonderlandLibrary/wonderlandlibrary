/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoClip", description="Allows you to freely move through walls (A sandblock has to fall on your head).", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/NoClip;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class NoClip
extends Module {
    @Override
    public void onDisable() {
        block0: {
            if (NoClip.access$getMc$p$s1046033730().field_71439_g == null) break block0;
            NoClip.access$getMc$p$s1046033730().field_71439_g.field_70145_X = false;
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        float speed;
        Intrinsics.checkParameterIsNotNull(event, "event");
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70145_X = true;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75100_b = false;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
        NoClip.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = speed = 0.32f;
        KeyBinding keyBinding = NoClip.access$getMc$p$s1046033730().field_71474_y.field_74314_A;
        Intrinsics.checkExpressionValueIsNotNull(keyBinding, "mc.gameSettings.keyBindJump");
        if (keyBinding.func_151470_d()) {
            NoClip.access$getMc$p$s1046033730().field_71439_g.field_70181_x += (double)speed;
        }
        KeyBinding keyBinding2 = NoClip.access$getMc$p$s1046033730().field_71474_y.field_74311_E;
        Intrinsics.checkExpressionValueIsNotNull(keyBinding2, "mc.gameSettings.keyBindSneak");
        if (keyBinding2.func_151470_d()) {
            NoClip.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= (double)speed;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

