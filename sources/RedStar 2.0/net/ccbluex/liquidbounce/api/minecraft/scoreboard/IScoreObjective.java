package net.ccbluex.liquidbounce.api.minecraft.scoreboard;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreObjective;", "", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "scoreboard", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "getScoreboard", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "Pride"})
public interface IScoreObjective {
    @NotNull
    public String getDisplayName();

    @NotNull
    public IScoreboard getScoreboard();
}
