package net.ccbluex.liquidbounce.api.minecraft.nbt;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\u0000\n\n\u0000\bf\u000020J020H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/nbt/IJsonToNBT;", "", "getTagFromJson", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "s", "", "Pride"})
public interface IJsonToNBT {
    @NotNull
    public INBTTagCompound getTagFromJson(@NotNull String var1);
}
