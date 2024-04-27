package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\u000002\b0H\n¢\b"}, d2={"<anonymous>", "", "it", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "invoke"})
final class WallClimb$onUpdate$isInsideBlock$1
extends Lambda
implements Function1<IBlock, Boolean> {
    public static final WallClimb$onUpdate$isInsideBlock$1 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    public final boolean invoke(@Nullable IBlock it) {
        return !MinecraftInstance.classProvider.isBlockAir(it);
    }

    WallClimb$onUpdate$isInsideBlock$1() {
    }
}
