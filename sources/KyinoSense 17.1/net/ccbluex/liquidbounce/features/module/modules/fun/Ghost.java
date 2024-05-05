/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiGameOver
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Ghost", description="Allows you to walk around and interact with your environment after dying.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/Ghost;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "isGhost", "", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Ghost
extends Module {
    private boolean isGhost;

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (Ghost.access$getMc$p$s1046033730().field_71462_r instanceof GuiGameOver) {
            Ghost.access$getMc$p$s1046033730().func_147108_a(null);
            Ghost.access$getMc$p$s1046033730().field_71439_g.field_70128_L = false;
            EntityPlayerSP entityPlayerSP = Ghost.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            entityPlayerSP.func_70606_j(20.0f);
            Ghost.access$getMc$p$s1046033730().field_71439_g.func_70634_a(Ghost.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Ghost.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Ghost.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            this.isGhost = true;
            ClientUtils.displayChatMessage("\u00a7cYou are now a ghost.");
        }
    }

    @Override
    public void onDisable() {
        if (this.isGhost) {
            Ghost.access$getMc$p$s1046033730().field_71439_g.func_71004_bE();
            this.isGhost = false;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

