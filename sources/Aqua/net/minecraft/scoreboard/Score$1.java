package net.minecraft.scoreboard;

import java.util.Comparator;
import net.minecraft.scoreboard.Score;

static final class Score.1
implements Comparator<Score> {
    Score.1() {
    }

    public int compare(Score p_compare_1_, Score p_compare_2_) {
        return p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints() ? 1 : (p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints() ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName()));
    }
}
