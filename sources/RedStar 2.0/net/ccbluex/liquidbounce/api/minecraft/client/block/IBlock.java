package net.ccbluex.liquidbounce.api.minecraft.client.block;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000b\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\bf\u000020J02\b020H&J\"0202020H&J020\u0000H&J 02 02!0\"2#0H&J$0%20H&J &0\r2'0(2!02)0H&J *0202 02)0H&J+020H&J,02 0H&J-0.202)0H&R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\tR\f0\rX¦¢\f\b\"\b¨/"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "", "defaultState", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "getDefaultState", "()Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "localizedName", "", "getLocalizedName", "()Ljava/lang/String;", "registryName", "getRegistryName", "slipperiness", "", "getSlipperiness", "()F", "setSlipperiness", "(F)V", "canCollideCheck", "", "state", "hitIfLiquid", "getCollisionBoundingBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "world", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorld;", "pos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getIdFromBlock", "", "block", "getMapColor", "blockState", "theWorld", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "bp", "getMaterial", "Lnet/ccbluex/liquidbounce/api/minecraft/block/material/IMaterial;", "getPlayerRelativeBlockHardness", "thePlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityPlayerSP;", "blockPos", "getSelectedBoundingBox", "isFullCube", "isTranslucent", "setBlockBoundsBasedOnState", "", "Pride"})
public interface IBlock {
    @NotNull
    public String getRegistryName();

    public float getSlipperiness();

    public void setSlipperiness(float var1);

    @Nullable
    public IIBlockState getDefaultState();

    @NotNull
    public String getLocalizedName();

    @NotNull
    public IAxisAlignedBB getSelectedBoundingBox(@NotNull IWorld var1, @NotNull IIBlockState var2, @NotNull WBlockPos var3);

    @Nullable
    public IAxisAlignedBB getCollisionBoundingBox(@NotNull IWorld var1, @NotNull WBlockPos var2, @NotNull IIBlockState var3);

    public boolean canCollideCheck(@Nullable IIBlockState var1, boolean var2);

    public void setBlockBoundsBasedOnState(@NotNull IWorld var1, @NotNull WBlockPos var2);

    public float getPlayerRelativeBlockHardness(@NotNull IEntityPlayerSP var1, @NotNull IWorld var2, @NotNull WBlockPos var3);

    public int getIdFromBlock(@NotNull IBlock var1);

    public boolean isTranslucent(@NotNull IIBlockState var1);

    public int getMapColor(@NotNull IIBlockState var1, @NotNull IWorldClient var2, @NotNull WBlockPos var3);

    @Nullable
    public IMaterial getMaterial(@NotNull IIBlockState var1);

    public boolean isFullCube(@NotNull IIBlockState var1);
}
