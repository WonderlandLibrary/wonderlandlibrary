package net.ccbluex.liquidbounce.api.minecraft.scoreboard;

import java.util.Collection;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\n\u0000\n\u0000\n\n\u0000\n\b\n\u0000\n\n\u0000\n\n\u0000\n\n\n\b\bf\u000020J020H&J02\b\b0\tH&J\n\b0\f02\r0H&¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "", "getObjectiveInDisplaySlot", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreObjective;", "index", "", "getPlayersTeam", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "name", "", "getSortedScores", "", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScore;", "objective", "Pride"})
public interface IScoreboard {
    @Nullable
    public ITeam getPlayersTeam(@Nullable String var1);

    @Nullable
    public IScoreObjective getObjectiveInDisplaySlot(int var1);

    @NotNull
    public Collection<IScore> getSortedScores(@NotNull IScoreObjective var1);
}
