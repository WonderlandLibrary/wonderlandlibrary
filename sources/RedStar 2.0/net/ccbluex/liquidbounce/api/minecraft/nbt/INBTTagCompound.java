package net.ccbluex.liquidbounce.api.minecraft.nbt;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\n\n\u0000\n\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\b\n\b\bf\u000020J020H&J020H&J\b0\t2\n020\fH&J\r0\t2\n020H&J0\t2\n020H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTBase;", "getShort", "", "name", "", "hasKey", "", "setInteger", "", "key", "value", "", "setString", "setTag", "tag", "Pride"})
public interface INBTTagCompound
extends INBTBase {
    public boolean hasKey(@NotNull String var1);

    public short getShort(@NotNull String var1);

    public void setString(@NotNull String var1, @NotNull String var2);

    public void setTag(@NotNull String var1, @NotNull INBTBase var2);

    public void setInteger(@NotNull String var1, int var2);
}
