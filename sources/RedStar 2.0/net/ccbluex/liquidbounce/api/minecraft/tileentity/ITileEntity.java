package net.ccbluex.liquidbounce.api.minecraft.tileentity;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\bf\u000020R0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "", "pos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Pride"})
public interface ITileEntity {
    @NotNull
    public WBlockPos getPos();
}
