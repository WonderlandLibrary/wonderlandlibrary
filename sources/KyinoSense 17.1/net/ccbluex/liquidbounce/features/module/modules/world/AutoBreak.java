/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MovingObjectPosition
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

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
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoBreak", description="Automatically breaks the block you are looking at.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0007\u00a8\u0006\b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/AutoBreak;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoBreak
extends Module {
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block3: {
            block2: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (AutoBreak.access$getMc$p$s1046033730().field_71476_x == null) break block2;
                MovingObjectPosition movingObjectPosition = AutoBreak.access$getMc$p$s1046033730().field_71476_x;
                Intrinsics.checkExpressionValueIsNotNull(movingObjectPosition, "mc.objectMouseOver");
                if (movingObjectPosition.func_178782_a() != null) break block3;
            }
            return;
        }
        KeyBinding keyBinding = AutoBreak.access$getMc$p$s1046033730().field_71474_y.field_74312_F;
        WorldClient worldClient = AutoBreak.access$getMc$p$s1046033730().field_71441_e;
        MovingObjectPosition movingObjectPosition = AutoBreak.access$getMc$p$s1046033730().field_71476_x;
        Intrinsics.checkExpressionValueIsNotNull(movingObjectPosition, "mc.objectMouseOver");
        IBlockState iBlockState = worldClient.func_180495_p(movingObjectPosition.func_178782_a());
        Intrinsics.checkExpressionValueIsNotNull(iBlockState, "mc.theWorld.getBlockStat\u2026objectMouseOver.blockPos)");
        keyBinding.field_74513_e = Intrinsics.areEqual(iBlockState.func_177230_c(), Blocks.field_150350_a) ^ true;
    }

    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a((KeyBinding)AutoBreak.access$getMc$p$s1046033730().field_71474_y.field_74312_F)) {
            AutoBreak.access$getMc$p$s1046033730().field_71474_y.field_74312_F.field_74513_e = false;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

