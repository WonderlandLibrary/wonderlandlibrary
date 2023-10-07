/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiScreen
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.fun.Ghost;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoRespawn", description="Automatically respawns you after dying.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AutoRespawn;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "instantValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoRespawn
extends Module {
    private final BoolValue instantValue = new BoolValue("Instant", true);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        boolean bl;
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Ghost.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) {
            return;
        }
        if (((Boolean)this.instantValue.get()).booleanValue()) {
            EntityPlayerSP entityPlayerSP = AutoRespawn.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_110143_aJ() != 0.0f) {
                if (!AutoRespawn.access$getMc$p$s1046033730().field_71439_g.field_70128_L) return;
            }
            bl = true;
        } else {
            if (!(AutoRespawn.access$getMc$p$s1046033730().field_71462_r instanceof GuiGameOver)) return;
            GuiScreen guiScreen = AutoRespawn.access$getMc$p$s1046033730().field_71462_r;
            if (guiScreen == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.GuiGameOver");
            }
            if (((GuiGameOver)guiScreen).field_146347_a < 20) return;
            bl = true;
        }
        if (!bl) return;
        AutoRespawn.access$getMc$p$s1046033730().field_71439_g.func_71004_bE();
        AutoRespawn.access$getMc$p$s1046033730().func_147108_a(null);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

