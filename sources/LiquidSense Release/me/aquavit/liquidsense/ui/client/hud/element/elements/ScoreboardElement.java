package me.aquavit.liquidsense.ui.client.hud.element.elements;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FontValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ElementInfo(name = "ScoreboardElement")
public class ScoreboardElement extends Element {

    public ScoreboardElement(){
        super(5,0,1f,new Side(Side.Horizontal.RIGHT,Side.Vertical.MIDDLE));
    }

    private IntegerValue textRedValue = new IntegerValue("Text-R", 255, 0, 255);
    private IntegerValue textGreenValue = new IntegerValue("Text-G", 255, 0, 255);
    private IntegerValue textBlueValue = new IntegerValue("Text-B", 255, 0, 255);

    private ListValue modeValue = new ListValue("Mode",new String[]{"Normal", "Blur"}, "Blur");
    private IntegerValue backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
    private IntegerValue backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
    private IntegerValue backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
    private IntegerValue backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 95, 0, 255);

    private BoolValue shadowValue = new BoolValue("Shadow", false);
    private final FontValue fontValue = new FontValue("Font", Fonts.minecraftFont);


    @Override
    public Border drawElement() {

        FontRenderer fontRenderer = fontValue.get();
        Color textColor = textColor();
        Color backColor = backgroundColor();

        Scoreboard worldScoreboard = mc.theWorld.getScoreboard();
        ScoreObjective currObjective = null;
        ScorePlayerTeam playerTeam = worldScoreboard.getPlayersTeam(mc.thePlayer.getName());

        if (playerTeam != null) {
            int colorIndex = playerTeam.getChatFormat().getColorIndex();

            if (colorIndex >= 0)
                currObjective = worldScoreboard.getObjectiveInDisplaySlot(3 + colorIndex);
        }

        ScoreObjective objective = currObjective != null ? currObjective : worldScoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return null;

        Scoreboard scoreboard = objective.getScoreboard();

        List<Score> scoreCollection = new ArrayList<>(scoreboard.getSortedScores(objective));

        List<Score> scores = scoreCollection.stream().filter(score -> score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());

        scoreCollection = scores.size() > 15 ? Lists.newArrayList(Iterables.skip(scores, scoreCollection.size() - 15)) : scores;

        int maxWidth = fontRenderer.getStringWidth(objective.getDisplayName());

        for (Score score : scoreCollection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            String width = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            maxWidth = Math.max(maxWidth,fontRenderer.getStringWidth(width));
        }

        int maxHeight = scoreCollection.size() * fontRenderer.FONT_HEIGHT;
        int l1 = -maxWidth - 3;

        if(modeValue.get().equalsIgnoreCase("Blur")) {
            BlurBuffer.blurArea((int)((getRenderX() + l1 - 2) * getScale()),(int)((getRenderY() - 2 - fontRenderer.FONT_HEIGHT /2) * getScale()), (maxWidth + 10) *getScale(),(maxHeight + fontRenderer.FONT_HEIGHT +  (float)fontRenderer.FONT_HEIGHT /2)*getScale(),true);
            if (!this.getInfo().disableScale())
                GL11.glScalef(this.getScale(), this.getScale(), this.getScale());

            GL11.glTranslated(this.getRenderX(), this.getRenderY(), 0.0);
        }else {
            Gui.drawRect(l1 - 2, -5, 5, maxHeight + fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT /2, backColor.getRGB());
        }

        RenderUtils.drawRoundedRect(l1 - 1.7F, -6.5F - Fonts.csgo40.FONT_HEIGHT - (float)fontRenderer.FONT_HEIGHT /2, (maxWidth + 10), 1.5F + (float)fontRenderer.FONT_HEIGHT /2 + Fonts.csgo40.FONT_HEIGHT ,1.5F,
                new Color(16, 25, 32, 200).getRGB(), 1F,new Color(16, 25, 32, 200).getRGB());
        Fonts.csgo40.drawString("N", l1 + 2F, -(Fonts.csgo40.FONT_HEIGHT + 2.5F + (float)fontRenderer.FONT_HEIGHT /2), new Color(0, 131, 193).getRGB(), false);
        Fonts.font20.drawString("ScoreBoard", l1 + Fonts.csgo40.getStringWidth("N") + 7.5F, -(Fonts.csgo40.FONT_HEIGHT + 2.1F + (float)fontRenderer.FONT_HEIGHT /2), Color.WHITE.getRGB(), false);

        int index = 0;
        for (Score score : scoreCollection){
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String name = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            String scorePoints = "" + EnumChatFormatting.RED + score.getScorePoints();
            int width = 5;
            int height = maxHeight - index * fontRenderer.FONT_HEIGHT;
            GlStateManager.resetColor();
            fontRenderer.drawString(name, l1, height, textColor.getRGB(), shadowValue.get());
            fontRenderer.drawString(scorePoints, (width - fontRenderer.getStringWidth(scorePoints)), height, textColor.getRGB(), shadowValue.get());

            if (index == scoreCollection.size() - 1) {
                String displayName = objective.getDisplayName();

                GlStateManager.resetColor();

                fontRenderer.drawString(displayName, (float)(l1 + maxWidth / 2 - fontRenderer.getStringWidth(displayName) / 2), (float)(height -fontRenderer.FONT_HEIGHT), textColor.getRGB(), shadowValue.get());
            }
            index++;
        }

        return new Border(-maxWidth - 5, -2F, 5F, maxHeight + fontRenderer.FONT_HEIGHT);
    }

    private Color backgroundColor() {
        return new Color(backgroundColorRedValue.get(), backgroundColorGreenValue.get(),
                backgroundColorBlueValue.get(), backgroundColorAlphaValue.get());
    }

    private Color textColor() {
        return new Color(textRedValue.get(), textGreenValue.get(),
                textBlueValue.get());
    }

}
