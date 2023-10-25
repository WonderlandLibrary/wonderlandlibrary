package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import net.minecraft.scoreboard.Score;

class GuiIngame.1
implements Predicate<Score> {
    GuiIngame.1() {
    }

    public boolean apply(Score p_apply_1_) {
        return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
    }
}
