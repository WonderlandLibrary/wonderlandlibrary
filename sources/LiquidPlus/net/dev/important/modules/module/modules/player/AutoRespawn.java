/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiScreen
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
import net.dev.important.value.BoolValue;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoRespawn", spacedName="Auto Respawn", description="Automatically respawns you after dying.", category=Category.PLAYER, cnName="\u81ea\u52a8\u590d\u6d3b")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/module/modules/player/AutoRespawn;", "Lnet/dev/important/modules/module/Module;", "()V", "instantValue", "Lnet/dev/important/value/BoolValue;", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class AutoRespawn
extends Module {
    @NotNull
    private final BoolValue instantValue = new BoolValue("Instant", true);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        boolean bl;
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.instantValue.get()).booleanValue()) {
            if (!(MinecraftInstance.mc.field_71439_g.func_110143_aJ() == 0.0f)) {
                if (!MinecraftInstance.mc.field_71439_g.field_70128_L) return;
            }
            bl = true;
        } else {
            if (!(MinecraftInstance.mc.field_71462_r instanceof GuiGameOver)) return;
            GuiScreen guiScreen = MinecraftInstance.mc.field_71462_r;
            if (guiScreen == null) {
                throw new NullPointerException("null cannot be cast to non-null type net.minecraft.client.gui.GuiGameOver");
            }
            if (((GuiGameOver)guiScreen).field_146347_a < 20) return;
            bl = true;
        }
        if (!bl) return;
        MinecraftInstance.mc.field_71439_g.func_71004_bE();
        MinecraftInstance.mc.func_147108_a(null);
    }
}

