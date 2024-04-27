/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.minecraft.nbt;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH&J\u0018\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H&J\u0018\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0001H&\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTBase;", "getShort", "", "name", "", "hasKey", "", "setInteger", "", "key", "value", "", "setString", "setTag", "tag", "AtField"})
public interface INBTTagCompound
extends INBTBase {
    public boolean hasKey(@NotNull String var1);

    public void setTag(@NotNull String var1, @NotNull INBTBase var2);

    public void setInteger(@NotNull String var1, int var2);

    public void setString(@NotNull String var1, @NotNull String var2);

    public short getShort(@NotNull String var1);
}

