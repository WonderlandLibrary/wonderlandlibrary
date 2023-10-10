package today.flux.gui.hud.window.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.hud.window.HudWindow;
import today.flux.utility.ServerUtils;

import java.util.ArrayList;
import java.util.Collection;

public class WindowScoreBoard extends HudWindow {
    public WindowScoreBoard() {
        super("Scoreboard", 5, 200, 200, 300, "Scoreboard", "", 12, 1, .5f);
    }

    @Override
    public void draw() {
        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());

        if (scoreplayerteam != null) {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective
                : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null) {
            super.draw();
            Collection collection = scoreboard.getSortedScores(scoreobjective1);
            ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate() {

                public boolean apply(Score p_apply_1_) {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }

                public boolean apply(Object p_apply_1_) {
                    return this.apply((Score) p_apply_1_);
                }
            }));

            ArrayList arraylist1;

            if (arraylist.size() > 15) {
                arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
            } else {
                arraylist1 = arraylist;
            }

            int height = 0;
            title = "Scoreboard";
            float width = iconOffX + FontManager.wqy18.getStringWidth(title) + 10;

            String s3 = scoreobjective1.getDisplayName();
            FontManager.wqy18.drawCenteredString(s3, x + (this.width / 2), y + draggableHeight + 1, 0xffffffff);
            height += 15;

            for (int i = 0; i < arraylist1.size(); i++) {
                Score score1 = (Score) arraylist1.get(arraylist1.size() - i - 1);
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());

                if (ServerUtils.INSTANCE.isOnHypixel()) {
                    int chars = 0;
                    String str = "www.hypixel.net";

                    for (char c : str.toCharArray()) {
                        if (s1.contains(String.valueOf(c))) chars++;
                    }

                    if (chars == str.length()) {
                        s1 = EnumChatFormatting.YELLOW + "flux.today";
                    }
                }

                width = Math.max(FontManager.wqy18.getStringWidth(s1), width);
                FontManager.wqy18.drawString(s1, x + 4, y + (height += 10), 0xffffffff);
            }

            width = Math.max(FontManager.wqy18.getStringWidth(s3), width);

            this.width = width + 8;
            this.height = height + 2;
        }
    }

}
