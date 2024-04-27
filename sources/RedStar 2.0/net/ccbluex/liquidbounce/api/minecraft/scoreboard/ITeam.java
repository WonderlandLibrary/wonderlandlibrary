package net.ccbluex.liquidbounce.api.minecraft.scoreboard;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\bf\u000020J02\b0H&J\t0\n20\u0000H&R0X¦¢\b¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "", "chatFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "getChatFormat", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "formatString", "", "name", "isSameTeam", "", "team", "Pride"})
public interface ITeam {
    @NotNull
    public WEnumChatFormatting getChatFormat();

    @NotNull
    public String formatString(@NotNull String var1);

    public boolean isSameTeam(@NotNull ITeam var1);
}
