/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockCactus
 *  net.minecraft.util.AxisAlignedBB
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.BlockBBEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

@Info(name="AntiCactus", spacedName="Anti Cactus", description="Prevents cactuses from damaging you.", category=Category.PLAYER, cnName="\u53cd\u4ed9\u4eba\u638c")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/player/AntiCactus;", "Lnet/dev/important/modules/module/Module;", "()V", "onBlockBB", "", "event", "Lnet/dev/important/event/BlockBBEvent;", "LiquidBounce"})
public final class AntiCactus
extends Module {
    @EventTarget
    public final void onBlockBB(@NotNull BlockBBEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getBlock() instanceof BlockCactus) {
            event.setBoundingBox(new AxisAlignedBB((double)event.getX(), (double)event.getY(), (double)event.getZ(), (double)event.getX() + 1.0, (double)event.getY() + 1.0, (double)event.getZ() + 1.0));
        }
    }
}

