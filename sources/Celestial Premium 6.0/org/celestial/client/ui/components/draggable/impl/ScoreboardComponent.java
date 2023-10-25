/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.impl.FakeNameCommand;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.feature.impl.visuals.NoRender;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class ScoreboardComponent
extends DraggableModule {
    public ScoreboardComponent() {
        super("ScoreboardComponent", 340, 220);
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getHeight() {
        return 200;
    }

    public FontRenderer getFontRenderer() {
        return this.mc.fontRendererObj;
    }

    @Override
    public void draw() {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.noScoreBoard.getCurrentValue()) {
            return;
        }
        ScoreObjective objective = GuiIngame.objective1;
        if (objective == null) {
            return;
        }
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        ArrayList<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>(){

            @Override
            public boolean apply(@Nullable Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
        }));
        collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + (Object)((Object)TextFormatting.RED) + score.getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }
        int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
        int j1 = this.getY() + i1 / 3;
        int l1 = this.getX();
        int j = 0;
        for (Score score1 : collection) {
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = (Object)((Object)TextFormatting.RED) + "" + score1.getScorePoints();
            int k = j1 - ++j * this.getFontRenderer().FONT_HEIGHT;
            int l = l1 + i - 3;
            RectHelper.drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
            if (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.scoreBoardSpoof.getCurrentValue()) {
                String str = FakeNameCommand.canChange ? FakeNameCommand.currentName : this.mc.player.getName();
                this.getFontRenderer().drawString(s1.replace(str.substring(0, 2), "                                                                                                                                                                                                                                                                                                                                                                        "), l1, k, 0x20FFFFFF);
            } else {
                this.getFontRenderer().drawString(s1, l1, k, 0x20FFFFFF);
            }
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0x20FFFFFF);
            if (j != collection.size()) continue;
            String s3 = objective.getDisplayName();
            RectHelper.drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 0x60000000);
            RectHelper.drawRect(l1 - 2, k - 1, l, k, 0x50000000);
            this.getFontRenderer().drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2, k - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
        }
        super.draw();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.noScoreBoard.getCurrentValue()) {
            return;
        }
        ScoreObjective objective = GuiIngame.objective1;
        if (objective == null) {
            return;
        }
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        ArrayList<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>(){

            @Override
            public boolean apply(@Nullable Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
        }));
        collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + (Object)((Object)TextFormatting.RED) + score.getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }
        int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
        int j1 = this.getY() + i1 / 3;
        int l1 = this.getX();
        int j = 0;
        for (Score score1 : collection) {
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = (Object)((Object)TextFormatting.RED) + "" + score1.getScorePoints();
            int k = j1 - ++j * this.getFontRenderer().FONT_HEIGHT;
            int l = l1 + i - 3;
            RectHelper.drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
            if (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.scoreBoardSpoof.getCurrentValue()) {
                String str = FakeNameCommand.canChange ? FakeNameCommand.currentName : this.mc.player.getName();
                this.getFontRenderer().drawString(s1.replace(str.substring(0, 2), "                                                                                                                                                                                                                                                                                                                                                                        "), l1, k, 0x20FFFFFF);
            } else {
                this.getFontRenderer().drawString(s1, l1, k, 0x20FFFFFF);
            }
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0x20FFFFFF);
            if (j != collection.size()) continue;
            String s3 = objective.getDisplayName();
            RectHelper.drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 0x60000000);
            RectHelper.drawRect(l1 - 2, k - 1, l, k, 0x50000000);
            this.getFontRenderer().drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2, k - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
        }
        super.render(mouseX, mouseY);
    }
}

