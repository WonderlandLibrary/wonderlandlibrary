package me.nyan.flush.customhud.component.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.nyan.flush.customhud.component.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Scoreboard extends Component {
    private int width;
    private int height;

    @Override
    public void onAdded() {

    }

    @Override
    public void draw(float x, float y) {
        width = height = 0;
        net.minecraft.scoreboard.Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective objective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());

        if (scoreplayerteam != null) {
            int j1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (j1 >= 0) {
                objective = scoreboard.getObjectiveInDisplaySlot(3 + j1);
            }
        }

        ScoreObjective objective1 = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);

        if (objective1 != null) {
            this.renderScoreboard(x, y, objective1);
        }
    }

    private void renderScoreboard(float x, float y, ScoreObjective objective) {
        net.minecraft.scoreboard.Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> sortedScores = scoreboard.getSortedScores(objective);
        java.util.ArrayList<Score> filteredScores = sortedScores
                .stream()
                .filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                .collect(Collectors.toCollection(Lists::newArrayList));

        ArrayList<Score> scores;

        if (filteredScores.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(filteredScores, sortedScores.size() - 15));
        } else {
            scores = filteredScores;
        }

        int i = mc.fontRendererObj.getStringWidth(objective.getDisplayName());

        for (Score score : scores) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, mc.fontRendererObj.getStringWidth(s));
        }

        width = 2 + i;
        height = (scores.size() + 1) * mc.fontRendererObj.FONT_HEIGHT;

        int k = 0;
        for (Score score : scores) {
            ++k;
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String playerName = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            String scorePoints = EnumChatFormatting.RED + "" + score.getScorePoints();
            float l = y + height - k * mc.fontRendererObj.FONT_HEIGHT;
            float x2 = x + 2 + i;
            Gui.drawRect(x, l, x2, l + mc.fontRendererObj.FONT_HEIGHT, 1342177280);
            mc.fontRendererObj.drawString(playerName, (int) (x + 2), (int) l, 553648127);
            mc.fontRendererObj.drawString(scorePoints, (int) (x2 - mc.fontRendererObj.getStringWidth(scorePoints)), (int) l, 553648127);

            if (k == scores.size()) {
                String s3 = objective.getDisplayName();
                Gui.drawRect(x, l - mc.fontRendererObj.FONT_HEIGHT - 1, x2, l - 1, 1610612736);
                Gui.drawRect(x, l - 1, x2, l, 1342177280);
                mc.fontRendererObj.drawString(
                        s3,
                        (int) (x + 2 + i / 2 - mc.fontRendererObj.getStringWidth(s3) / 2),
                        (int) (l - mc.fontRendererObj.FONT_HEIGHT),
                        553648127
                );
            }
        }
    }

    @Override
    public int width() {
        return width == 0 ? 90 : width;
    }

    @Override
    public int height() {
        return height == 0 ? 120 : height;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
