package net.ccbluex.liquidbounce.api.network;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\u0000\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\bf\u000020J020H&J020\bH&J\t0\u00002\n0H&¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "", "writeBytes", "", "payload", "", "writeItemStackToBuffer", "itemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "writeString", "vanilla", "", "Pride"})
public interface IPacketBuffer {
    public void writeBytes(@NotNull byte[] var1);

    public void writeItemStackToBuffer(@NotNull IItemStack var1);

    @NotNull
    public IPacketBuffer writeString(@NotNull String var1);
}
