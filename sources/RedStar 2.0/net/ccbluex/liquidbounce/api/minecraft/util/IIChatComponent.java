package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.IChatStyle;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\bf\u000020J\f0\r20\u0000H&J0\r20H&R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\t¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IIChatComponent;", "", "chatStyle", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IChatStyle;", "getChatStyle", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IChatStyle;", "formattedText", "", "getFormattedText", "()Ljava/lang/String;", "unformattedText", "getUnformattedText", "appendSibling", "", "component", "appendText", "text", "Pride"})
public interface IIChatComponent {
    @NotNull
    public String getUnformattedText();

    @NotNull
    public IChatStyle getChatStyle();

    @NotNull
    public String getFormattedText();

    public void appendText(@NotNull String var1);

    public void appendSibling(@NotNull IIChatComponent var1);
}
