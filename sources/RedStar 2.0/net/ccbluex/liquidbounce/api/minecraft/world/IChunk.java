package net.ccbluex.liquidbounce.api.minecraft.world;

import java.util.List;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\n\u0000\n\u0000\n\b\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n!\n\n\u0000\n\n\b\bf\u000020J\b0\t2\n0H&J0\f0\r20202\f\b002\b0H&J02020H&R0X¦¢\bR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/world/IChunk;", "", "x", "", "getX", "()I", "z", "getZ", "getBlockState", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getEntitiesWithinAABBForEntity", "", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "arrowBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "collidedEntities", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "nothing", "", "getHeightValue", "Pride"})
public interface IChunk {
    public int getX();

    public int getZ();

    public void getEntitiesWithinAABBForEntity(@NotNull IEntityPlayerSP var1, @NotNull IAxisAlignedBB var2, @NotNull List<IEntity> var3, @Nullable Void var4);

    public int getHeightValue(int var1, int var2);

    @NotNull
    public IIBlockState getBlockState(@NotNull WBlockPos var1);
}
