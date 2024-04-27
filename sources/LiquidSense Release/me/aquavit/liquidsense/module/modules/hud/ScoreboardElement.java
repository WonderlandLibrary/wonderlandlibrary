package me.aquavit.liquidsense.module.modules.hud;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.BlurBuffer;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FontValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ModuleInfo(name = "ScoreboardElement", description = "Drawing 2D Scoreboard", category = ModuleCategory.HUD, array = false)
public class ScoreboardElement extends Module {

    private final ListValue horizontal = new ListValue("Horizontal", new String[] {"Left", "Middle", "Right"}, "Right") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "left":
                    setRenderx(x);
                    break;
                case "middle":
                    setRenderx((float) new ScaledResolution(mc).getScaledWidth() / 2 - x);
                    break;
                case "right":
                    setRenderx(new ScaledResolution(mc).getScaledWidth() - x);
                    break;
            }
        }
    };
    private final ListValue vertical = new ListValue("Vertical", new String[] {"Up", "Middle", "Down"}, "Middle") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "up":
                    setRendery(y);
                    break;
                case "middle":
                    setRendery((float) new ScaledResolution(mc).getScaledHeight() / 2 - y);
                    break;
                case "down":
                    setRendery(new ScaledResolution(mc).getScaledHeight() - y);
                    break;
            }
        }
    };
    int x,y;

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

    public ScoreboardElement(double x, double y){
        this.setDefaultx(x);
        this.setDefaulty(y);
    }

    public ScoreboardElement(){
        this(5,0);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        isLeft = horizontal.get().equalsIgnoreCase("Left");
        isUp = vertical.get().equalsIgnoreCase("Up");
        x = (int) getHUDX(horizontal.get().equalsIgnoreCase("Left"),
                horizontal.get().equalsIgnoreCase("Middle"),
                horizontal.get().equalsIgnoreCase("Right"));
        y = (int) getHUDY(vertical.get().equalsIgnoreCase("Up"),
                vertical.get().equalsIgnoreCase("Middle"),
                vertical.get().equalsIgnoreCase("Down"));

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
        if (objective == null) {
            return;
        }

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
            BlurBuffer.blurArea(x + l1 - 2, y - fontRenderer.FONT_HEIGHT /2 - 1, maxWidth + 10,maxHeight + fontRenderer.FONT_HEIGHT +  (float)fontRenderer.FONT_HEIGHT /2,true);

        }else {
            Gui.drawRect(x+l1 - 2, y - 5,  x + 5, y+maxHeight + fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT /2, backColor.getRGB());
        }

        RenderUtils.drawRoundedRect(x+l1 - 1.7F, (y - 6.5F) - Fonts.csgo40.FONT_HEIGHT - (float)fontRenderer.FONT_HEIGHT /2, (maxWidth + 10), 1.5F + (float)fontRenderer.FONT_HEIGHT /2 + Fonts.csgo40.FONT_HEIGHT ,1.5F,
                new Color(16, 25, 32, 200).getRGB(), 1F,new Color(16, 25, 32, 200).getRGB());
        Fonts.csgo40.drawString("N", x+l1 + 2F, y - (Fonts.csgo40.FONT_HEIGHT + 2.5F + (float) fontRenderer.FONT_HEIGHT / 2), new Color(0, 131, 193).getRGB(), false);
        Fonts.font20.drawString("ScoreBoard", x+l1 + Fonts.csgo40.getStringWidth("N") + 7.5F, y - (Fonts.csgo40.FONT_HEIGHT + 2.1F + (float) fontRenderer.FONT_HEIGHT / 2), Color.WHITE.getRGB(), false);

        int index = 0;
        for (Score score : scoreCollection){
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String name = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
            String scorePoints = "" + EnumChatFormatting.RED + score.getScorePoints();
            int width = 5;
            int height = maxHeight - index * fontRenderer.FONT_HEIGHT;
            GlStateManager.resetColor();
            fontRenderer.drawString(name, x+l1, y+height, textColor.getRGB(), shadowValue.get());
            fontRenderer.drawString(scorePoints, x+(width - fontRenderer.getStringWidth(scorePoints)), y+height, textColor.getRGB(), shadowValue.get());

            if (index == scoreCollection.size() - 1) {
                String displayName = objective.getDisplayName();

                GlStateManager.resetColor();

                fontRenderer.drawString(displayName, x+(float)(l1 + maxWidth / 2 - fontRenderer.getStringWidth(displayName) / 2), y+(float)(height -fontRenderer.FONT_HEIGHT), textColor.getRGB(), shadowValue.get());
            }
            index++;
        }

        drawBorder( -maxWidth - 5 + x, y - 6F, maxWidth + 10, maxHeight + fontRenderer.FONT_HEIGHT + 6 );
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
