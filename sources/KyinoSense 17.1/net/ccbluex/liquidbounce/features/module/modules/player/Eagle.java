/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Eagle", description="Makes you eagle (aka. FastBridge).", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Eagle;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Eagle
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        KeyBinding keyBinding = Eagle.access$getMc$p$s1046033730().field_71474_y.field_74311_E;
        IBlockState iBlockState = Eagle.access$getMc$p$s1046033730().field_71441_e.func_180495_p(new BlockPos(Eagle.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Eagle.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Eagle.access$getMc$p$s1046033730().field_71439_g.field_70161_v));
        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockStat\u2026 1.0, mc.thePlayer.posZ))");
        keyBinding.field_74513_e = Intrinsics.areEqual(iBlockState.func_177230_c(), Blocks.field_150350_a);
    }

    @Override
    public void onDisable() {
        if (Eagle.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (!GameSettings.func_100015_a((KeyBinding)Eagle.access$getMc$p$s1046033730().field_71474_y.field_74311_E)) {
            Eagle.access$getMc$p$s1046033730().field_71474_y.field_74311_E.field_74513_e = false;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

