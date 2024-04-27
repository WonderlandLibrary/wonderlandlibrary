package net.ccbluex.liquidbounce.api.minecraft.potion;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\b\n\b\n\n\u0000\bf\u000020J\b\n0H&R0X¦¢\bR0X¦¢\bR\b0X¦¢\b\t¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "", "amplifier", "", "getAmplifier", "()I", "duration", "getDuration", "potionID", "getPotionID", "getDurationString", "", "Pride"})
public interface IPotionEffect {
    @NotNull
    public String getDurationString();

    public int getAmplifier();

    public int getDuration();

    public int getPotionID();
}
