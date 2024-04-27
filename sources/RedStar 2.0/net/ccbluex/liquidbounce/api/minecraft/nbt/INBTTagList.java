package net.ccbluex.liquidbounce.api.minecraft.nbt;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\b\n\n\u0000\n\b\n\u0000\n\n\b\bf\u000020J020H&J020\bH&J\b\t0\nH&J\b0\bH&¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTBase;", "appendTag", "", "createNBTTagString", "getCompoundTagAt", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "index", "", "hasNoTags", "", "tagCount", "Pride"})
public interface INBTTagList
extends INBTBase {
    public boolean hasNoTags();

    public int tagCount();

    @NotNull
    public INBTTagCompound getCompoundTagAt(int var1);

    public void appendTag(@NotNull INBTBase var1);
}
