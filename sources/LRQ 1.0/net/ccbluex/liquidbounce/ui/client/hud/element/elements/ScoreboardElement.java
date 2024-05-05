/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.features.module.modules.render.NoScoreboard;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ScoreboardElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Scoreboard", force=true)
public final class ScoreboardElement
extends Element {
    private final IntegerValue textRedValue;
    private final IntegerValue textGreenValue;
    private final IntegerValue textBlueValue;
    private final IntegerValue backgroundColorRedValue;
    private final IntegerValue backgroundColorGreenValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue backgroundColorAlphaValue;
    private final BoolValue rectValue;
    private final ListValue rectColorModeValue;
    private final IntegerValue rectColorRedValue;
    private final IntegerValue rectColorGreenValue;
    private final IntegerValue rectColorBlueValue;
    private final IntegerValue rectColorBlueAlpha;
    private final BoolValue shadowValue;
    private final FontValue fontValue;

    /*
     * WARNING - void declaration
     */
    @Override
    public Border drawElement() {
        IScoreObjective iScoreObjective;
        int colorIndex;
        ITeam playerTeam;
        if (NoScoreboard.INSTANCE.getState()) {
            return null;
        }
        IFontRenderer fontRenderer = (IFontRenderer)this.fontValue.get();
        int textColor = this.textColor().getRGB();
        int backColor = this.backgroundColor().getRGB();
        String rectColorMode = (String)this.rectColorModeValue.get();
        int rectCustomColor = new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()).getRGB();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IScoreboard worldScoreboard = iWorldClient.getScoreboard();
        IScoreObjective currObjective = null;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if ((playerTeam = worldScoreboard.getPlayersTeam(iEntityPlayerSP.getName())) != null && (colorIndex = playerTeam.getChatFormat().getColorIndex()) >= 0) {
            currObjective = worldScoreboard.getObjectiveInDisplaySlot(3 + colorIndex);
        }
        if ((iScoreObjective = currObjective) == null) {
            iScoreObjective = worldScoreboard.getObjectiveInDisplaySlot(1);
        }
        if (iScoreObjective == null) {
            return null;
        }
        IScoreObjective objective = iScoreObjective;
        IScoreboard scoreboard = objective.getScoreboard();
        Collection scoreCollection = scoreboard.getSortedScores(objective);
        ArrayList scores2 = Lists.newArrayList((Iterable)Iterables.filter((Iterable)scoreCollection, (Predicate)drawElement.scores.1.INSTANCE));
        scoreCollection = scores2.size() > 15 ? (Collection)Lists.newArrayList((Iterable)Iterables.skip((Iterable)scores2, (int)(scoreCollection.size() - 15))) : (Collection)scores2;
        int maxWidth = fontRenderer.getStringWidth(objective.getDisplayName());
        for (IScore score : (ArrayList)scoreCollection) {
            ITeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            String width = MinecraftInstance.functions.scoreboardFormatPlayerName(scorePlayerTeam, score.getPlayerName()) + ": " + (Object)((Object)WEnumChatFormatting.RED) + score.getScorePoints();
            maxWidth = RangesKt.coerceAtLeast((int)maxWidth, (int)fontRenderer.getStringWidth(width));
        }
        int maxHeight = scoreCollection.size() * fontRenderer.getFontHeight();
        int l1 = -maxWidth - 3 - ((Boolean)this.rectValue.get() != false ? 3 : 0);
        RenderUtils.drawRect(l1 - 2, -2, 5, maxHeight + fontRenderer.getFontHeight(), backColor);
        Iterable $this$forEachIndexed$iv = scoreCollection;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            void score;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n2 = n;
            IScore iScore = (IScore)item$iv;
            int index = n2;
            boolean bl2 = false;
            ITeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            String name = MinecraftInstance.functions.scoreboardFormatPlayerName(team, score.getPlayerName());
            String scorePoints = "" + (Object)((Object)WEnumChatFormatting.RED) + score.getScorePoints();
            int width = 5 - ((Boolean)this.rectValue.get() != false ? 4 : 0);
            float height = (float)maxHeight - (float)index * (float)fontRenderer.getFontHeight();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            fontRenderer.drawString(name, l1, height, textColor, (Boolean)this.shadowValue.get());
            fontRenderer.drawString(scorePoints, width - fontRenderer.getStringWidth(scorePoints), height, textColor, (Boolean)this.shadowValue.get());
            if (index == scoreCollection.size() - 1) {
                String displayName = objective.getDisplayName();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                fontRenderer.drawString(displayName, l1 + maxWidth / 2 - fontRenderer.getStringWidth(displayName) / 2, height - (float)fontRenderer.getFontHeight(), textColor, (Boolean)this.shadowValue.get());
            }
            if (!((Boolean)this.rectValue.get()).booleanValue()) continue;
            int rectColor = StringsKt.equals((String)rectColorMode, (String)"Rainbow", (boolean)true) ? ColorUtils.rainbow(400000000L * (long)index).getRGB() : rectCustomColor;
            RenderUtils.drawRect(2.0f, index == scoreCollection.size() - 1 ? -2.0f : height, 5.0f, index == 0 ? (float)fontRenderer.getFontHeight() : height + (float)fontRenderer.getFontHeight() * 2.0f, rectColor);
        }
        return new Border(-((float)maxWidth) - (float)5 - (float)((Boolean)this.rectValue.get() != false ? 3 : 0), -2.0f, 5.0f, (float)maxHeight + (float)fontRenderer.getFontHeight());
    }

    private final Color backgroundColor() {
        return new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
    }

    private final Color textColor() {
        return new Color(((Number)this.textRedValue.get()).intValue(), ((Number)this.textGreenValue.get()).intValue(), ((Number)this.textBlueValue.get()).intValue());
    }

    public ScoreboardElement(double x, double y, float scale, Side side) {
        super(x, y, scale, side);
        this.textRedValue = new IntegerValue("Text-R", 255, 0, 255);
        this.textGreenValue = new IntegerValue("Text-G", 255, 0, 255);
        this.textBlueValue = new IntegerValue("Text-B", 255, 0, 255);
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 95, 0, 255);
        this.rectValue = new BoolValue("Rect", false);
        this.rectColorModeValue = new ListValue("Rect-Color", new String[]{"Custom", "Rainbow"}, "Custom");
        this.rectColorRedValue = new IntegerValue("Rect-R", 0, 0, 255);
        this.rectColorGreenValue = new IntegerValue("Rect-G", 111, 0, 255);
        this.rectColorBlueValue = new IntegerValue("Rect-B", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Rect-Alpha", 255, 0, 255);
        this.shadowValue = new BoolValue("Shadow", false);
        this.fontValue = new FontValue("Font", Fonts.minecraftFont);
    }

    public /* synthetic */ ScoreboardElement(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 0.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.MIDDLE);
        }
        this(d, d2, f, side);
    }

    public ScoreboardElement() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

