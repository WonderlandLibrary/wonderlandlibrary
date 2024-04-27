package net.ccbluex.liquidbounce.api.minecraft.potion;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\b\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\tR\f0\rX¦¢\bR0X¦¢\b\t¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotion;", "", "hasStatusIcon", "", "getHasStatusIcon", "()Z", "id", "", "getId", "()I", "liquidColor", "getLiquidColor", "name", "", "getName", "()Ljava/lang/String;", "statusIconIndex", "getStatusIconIndex", "Pride"})
public interface IPotion {
    public int getLiquidColor();

    public int getId();

    @NotNull
    public String getName();

    public boolean getHasStatusIcon();

    public int getStatusIconIndex();
}
