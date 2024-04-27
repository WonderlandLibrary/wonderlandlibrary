package net.ccbluex.liquidbounce.api.minecraft.scoreboard;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\b\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\t¨\n"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScore;", "", "playerName", "", "getPlayerName", "()Ljava/lang/String;", "scorePoints", "", "getScorePoints", "()I", "Pride"})
public interface IScore {
    public int getScorePoints();

    @NotNull
    public String getPlayerName();
}
