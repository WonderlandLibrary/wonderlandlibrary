package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\bf\u000020R0X¦¢\f\b\"\bR\b0\tX¦¢\f\b\n\"\b\f\rR0X¦¢\f\b\"\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IChatStyle;", "", "chatClickEvent", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "getChatClickEvent", "()Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;", "setChatClickEvent", "(Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent;)V", "color", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "getColor", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "setColor", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;)V", "underlined", "", "getUnderlined", "()Z", "setUnderlined", "(Z)V", "Pride"})
public interface IChatStyle {
    @Nullable
    public IClickEvent getChatClickEvent();

    public void setChatClickEvent(@Nullable IClickEvent var1);

    public boolean getUnderlined();

    public void setUnderlined(boolean var1);

    @Nullable
    public WEnumChatFormatting getColor();

    public void setColor(@Nullable WEnumChatFormatting var1);
}
