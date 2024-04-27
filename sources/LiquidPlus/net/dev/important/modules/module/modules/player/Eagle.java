/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Info(name="Eagle", description="Makes you eagle (aka. FastBridge).", category=Category.PLAYER, cnName="\u5feb\u901f\u642d\u6865")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\b"}, d2={"Lnet/dev/important/modules/module/modules/player/Eagle;", "Lnet/dev/important/modules/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Eagle
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        boolean shouldEagle;
        Intrinsics.checkNotNullParameter(event, "event");
        MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e = shouldEagle = MinecraftInstance.mc.field_71441_e.func_180495_p(new BlockPos(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u - 1.0, MinecraftInstance.mc.field_71439_g.field_70161_v)).func_177230_c() == Blocks.field_150350_a;
    }

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        if (!GameSettings.func_100015_a((KeyBinding)MinecraftInstance.mc.field_71474_y.field_74311_E)) {
            MinecraftInstance.mc.field_71474_y.field_74311_E.field_74513_e = false;
        }
    }
}

