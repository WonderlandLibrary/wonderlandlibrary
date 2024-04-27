package net.ccbluex.liquidbounce.api.minecraft.entity.player;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\bR0X¦¢\f\b\"\b\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/entity/player/IPlayerCapabilities;", "", "allowFlying", "", "getAllowFlying", "()Z", "isCreativeMode", "isFlying", "setFlying", "(Z)V", "Pride"})
public interface IPlayerCapabilities {
    public boolean getAllowFlying();

    public boolean isFlying();

    public void setFlying(boolean var1);

    public boolean isCreativeMode();
}
