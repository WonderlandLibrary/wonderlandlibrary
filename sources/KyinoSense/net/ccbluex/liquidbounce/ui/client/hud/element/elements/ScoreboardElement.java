/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.scoreboard.Score
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.EnumChatFormatting
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.report.liquidware.modules.render.Camera;
import me.report.liquidware.modules.render.ColorMixer;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ScoreboardElement;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.ShadowUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Scoreboard")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010;\u001a\u00020<H\u0002J\n\u0010=\u001a\u0004\u0018\u00010>H\u0016J\b\u0010?\u001a\u00020@H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0016\u001a\u0012\u0012\u0004\u0012\u00020\u00180\u0017j\b\u0012\u0004\u0012\u00020\u0018`\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00180 X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010!R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006A"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/ScoreboardElement;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "antiSnipeMatch", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "backgroundColorAlphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorRedValue", "bgRoundedValue", "blurStrength", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "blurValue", "cRainbowSecValue", "cachedDomains", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "changeDomain", "delayValue", "domainFontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "domainFontYValue", "domainList", "", "[Ljava/lang/String;", "domainShadowValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "fontValue", "garbageTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "hypickleRegex", "Lkotlin/text/Regex;", "outlineWidthValue", "rectColorBlueAlpha", "rectColorBlueValue", "rectColorGreenValue", "rectColorModeValue", "rectColorRedValue", "rectHeight", "rectValue", "roundStrength", "shadowColorBlueValue", "shadowColorGreenValue", "shadowColorMode", "shadowColorRedValue", "shadowShaderValue", "shadowStrength", "shadowValue", "showRedNumbersValue", "useVanillaBackground", "backgroundColor", "Ljava/awt/Color;", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "updateElement", "", "KyinoClient"})
public final class ScoreboardElement
extends Element {
    private final BoolValue useVanillaBackground;
    private final IntegerValue backgroundColorRedValue;
    private final IntegerValue backgroundColorGreenValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue backgroundColorAlphaValue;
    private final BoolValue rectValue;
    private final IntegerValue rectHeight;
    private final BoolValue blurValue;
    private final FloatValue blurStrength;
    private final BoolValue shadowShaderValue;
    private final FloatValue shadowStrength;
    private final ListValue shadowColorMode;
    private final IntegerValue shadowColorRedValue;
    private final IntegerValue shadowColorGreenValue;
    private final IntegerValue shadowColorBlueValue;
    private final BoolValue bgRoundedValue;
    private final FloatValue roundStrength;
    private final ListValue rectColorModeValue;
    private final IntegerValue rectColorRedValue;
    private final IntegerValue rectColorGreenValue;
    private final IntegerValue rectColorBlueValue;
    private final IntegerValue rectColorBlueAlpha;
    private final IntegerValue cRainbowSecValue;
    private final IntegerValue delayValue;
    private final BoolValue shadowValue;
    private final BoolValue antiSnipeMatch;
    private final BoolValue changeDomain;
    private final BoolValue showRedNumbersValue;
    private final FontValue fontValue;
    private final FontValue domainFontValue;
    private final FloatValue domainFontYValue;
    private final ListValue domainShadowValue;
    private final FloatValue outlineWidthValue;
    private final String[] domainList;
    private final ArrayList<String> cachedDomains;
    private final MSTimer garbageTimer;
    private final Regex hypickleRegex;

    @Override
    public void updateElement() {
        if (this.garbageTimer.hasTimePassed(30000L) || this.cachedDomains.size() > 50) {
            this.cachedDomains.clear();
            this.garbageTimer.reset();
        }
    }

    /*
     * Unable to fully structure code
     */
    @Override
    @Nullable
    public Border drawElement() {
        v0 = LiquidBounce.INSTANCE.getModuleManager().getModule(Camera.class);
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type me.report.liquidware.modules.render.Camera");
        }
        antiBlind = (Camera)v0;
        if (antiBlind.getState() && ((Boolean)antiBlind.getScoreBoard().get()).booleanValue()) {
            return null;
        }
        fontRenderer = (FontRenderer)this.fontValue.get();
        backColor = this.backgroundColor().getRGB();
        rectColorMode = (String)this.rectColorModeValue.get();
        rectCustomColor = new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()).getRGB();
        v1 = ScoreboardElement.access$getMc$p$s1046033730().field_71441_e;
        Intrinsics.checkExpressionValueIsNotNull(v1, "mc.theWorld");
        v2 = v1.func_96441_U();
        Intrinsics.checkExpressionValueIsNotNull(v2, "mc.theWorld.scoreboard");
        worldScoreboard = v2;
        currObjective = null;
        v3 = ScoreboardElement.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(v3, "mc.thePlayer");
        playerTeam = worldScoreboard.func_96509_i(v3.func_70005_c_());
        if (playerTeam != null) {
            v4 = playerTeam.func_178775_l();
            Intrinsics.checkExpressionValueIsNotNull(v4, "playerTeam.chatFormat");
            colorIndex = v4.func_175746_b();
            if (colorIndex >= 0) {
                currObjective = worldScoreboard.func_96539_a(3 + colorIndex);
            }
        }
        if ((v5 = currObjective) == null) {
            v5 = worldScoreboard.func_96539_a(1);
        }
        if (v5 == null) {
            return null;
        }
        objective = v5;
        v6 = objective.func_96682_a();
        Intrinsics.checkExpressionValueIsNotNull(v6, "objective.scoreboard");
        scoreboard = v6;
        scoreCollection = scoreboard.func_96534_i(objective);
        scores = Lists.newArrayList((Iterable)Iterables.filter((Iterable)scoreCollection, (Predicate)drawElement.scores.1.INSTANCE));
        scoreCollection = scores.size() > 15 ? (Collection)Lists.newArrayList((Iterable)Iterables.skip((Iterable)scores, (int)(scoreCollection.size() - 15))) : (Collection)scores;
        maxWidth = fontRenderer.func_78256_a(objective.func_96678_d());
        v7 = LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class);
        if (v7 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        hud = (HUD)v7;
        var16_16 = scoreCollection.iterator();
        while (var16_16.hasNext()) {
            v8 = score = (Score)var16_16.next();
            Intrinsics.checkExpressionValueIsNotNull(v8, "score");
            scorePlayerTeam = scoreboard.func_96509_i(v8.func_96653_e());
            name = ScorePlayerTeam.func_96667_a((Team)((Team)scorePlayerTeam), (String)score.func_96653_e());
            v9 = ColorUtils.stripColor(name);
            if (v9 == null) {
                Intrinsics.throwNpe();
            }
            stripped = StringUtils.fixString(v9);
            if (((Boolean)this.changeDomain.get()).booleanValue()) {
                if (this.cachedDomains.contains(stripped)) {
                    name = "Report1337";
                } else if (ServerUtils.isHypixelDomain(stripped)) {
                    name = "Report1337";
                    this.cachedDomains.add(stripped);
                } else {
                    for (String domain : this.domainList) {
                        v10 = stripped;
                        Intrinsics.checkExpressionValueIsNotNull(v10, "stripped");
                        if (!StringsKt.contains((CharSequence)v10, domain, true)) continue;
                        name = "Report1337";
                        this.cachedDomains.add(stripped);
                        break;
                    }
                }
            }
            width = name + ": " + EnumChatFormatting.RED + score.func_96652_c();
            maxWidth = RangesKt.coerceAtLeast(maxWidth, fontRenderer.func_78256_a((String)width));
        }
        maxHeight = scoreCollection.size() * fontRenderer.field_78288_b;
        l1 = this.getSide().getHorizontal() == Side.Horizontal.LEFT ? maxWidth + 3 : -maxWidth - 3;
        hud2 = (HudColors)LiquidBounce.INSTANCE.getModuleManager().getModule(HudColors.class);
        FadeColor = ColorUtils.fade(new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()), 0, 100).getRGB();
        v11 = hud2;
        if (v11 != null && (v11 = v11.getSaturationValue()) != null) {
            width = v11;
            var21_26 = 0;
            var22_28 = false;
            it = width;
            $i$a$-let-ScoreboardElement$drawElement$LiquidSlowly$1 = false;
            v12 = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)it.get()).floatValue(), ((Number)hud2.getBrightnessValue().get()).floatValue());
            v13 = (int)(v12 != null ? Integer.valueOf(v12.getRGB()) : null);
        } else {
            v13 = null;
        }
        v14 = LiquidSlowly = v13;
        if (v14 == null) {
            Intrinsics.throwNpe();
        }
        liquidSlowli = v14;
        v15 = ColorMixer.getMixedColor(0, ((Number)this.cRainbowSecValue.get()).intValue());
        Intrinsics.checkExpressionValueIsNotNull(v15, "ColorMixer.getMixedColor\u2026, cRainbowSecValue.get())");
        mixerColor = v15.getRGB();
        if (scoreCollection.size() > 0) {
            if (((Boolean)this.shadowShaderValue.get()).booleanValue()) {
                GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glPushMatrix();
                ShadowUtils.INSTANCE.shadow(((Number)this.shadowStrength.get()).floatValue(), new Function0<Unit>(this, l1, maxHeight, fontRenderer){
                    final /* synthetic */ ScoreboardElement this$0;
                    final /* synthetic */ int $l1;
                    final /* synthetic */ int $maxHeight;
                    final /* synthetic */ FontRenderer $fontRenderer;

                    public final void invoke() {
                        GL11.glPushMatrix();
                        GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                        GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                        if (((Boolean)ScoreboardElement.access$getBgRoundedValue$p(this.this$0).get()).booleanValue()) {
                            RenderUtils.originalRoundedRect((float)this.$l1 + (this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), (Boolean)ScoreboardElement.access$getRectValue$p(this.this$0).get() != false ? -2.0f - (float)((Number)ScoreboardElement.access$getRectHeight$p(this.this$0).get()).intValue() : -2.0f, this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, this.$maxHeight + this.$fontRenderer.field_78288_b, ((Number)ScoreboardElement.access$getRoundStrength$p(this.this$0).get()).floatValue(), StringsKt.equals((String)ScoreboardElement.access$getShadowColorMode$p(this.this$0).get(), "background", true) ? new Color(((Number)ScoreboardElement.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB() : new Color(((Number)ScoreboardElement.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB());
                        } else {
                            RenderUtils.newDrawRect((float)this.$l1 + (this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), (Boolean)ScoreboardElement.access$getRectValue$p(this.this$0).get() != false ? -2.0f - (float)((Number)ScoreboardElement.access$getRectHeight$p(this.this$0).get()).intValue() : -2.0f, this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, this.$maxHeight + this.$fontRenderer.field_78288_b, StringsKt.equals((String)ScoreboardElement.access$getShadowColorMode$p(this.this$0).get(), "background", true) ? new Color(((Number)ScoreboardElement.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB() : new Color(((Number)ScoreboardElement.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB());
                        }
                        GL11.glPopMatrix();
                    }
                    {
                        this.this$0 = scoreboardElement;
                        this.$l1 = n;
                        this.$maxHeight = n2;
                        this.$fontRenderer = fontRenderer;
                        super(0);
                    }
                }, new Function0<Unit>(this, l1, maxHeight, fontRenderer){
                    final /* synthetic */ ScoreboardElement this$0;
                    final /* synthetic */ int $l1;
                    final /* synthetic */ int $maxHeight;
                    final /* synthetic */ FontRenderer $fontRenderer;

                    public final void invoke() {
                        GL11.glPushMatrix();
                        GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                        GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                        GlStateManager.func_179147_l();
                        GlStateManager.func_179090_x();
                        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                        if (((Boolean)ScoreboardElement.access$getBgRoundedValue$p(this.this$0).get()).booleanValue()) {
                            RenderUtils.fastRoundedRect((float)this.$l1 + (this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), (Boolean)ScoreboardElement.access$getRectValue$p(this.this$0).get() != false ? -2.0f - (float)((Number)ScoreboardElement.access$getRectHeight$p(this.this$0).get()).intValue() : -2.0f, this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, this.$maxHeight + this.$fontRenderer.field_78288_b, ((Number)ScoreboardElement.access$getRoundStrength$p(this.this$0).get()).floatValue());
                        } else {
                            RenderUtils.quickDrawRect((float)this.$l1 + (this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), (Boolean)ScoreboardElement.access$getRectValue$p(this.this$0).get() != false ? -2.0f - (float)((Number)ScoreboardElement.access$getRectHeight$p(this.this$0).get()).intValue() : -2.0f, this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, this.$maxHeight + this.$fontRenderer.field_78288_b);
                        }
                        GlStateManager.func_179098_w();
                        GlStateManager.func_179084_k();
                        GL11.glPopMatrix();
                    }
                    {
                        this.this$0 = scoreboardElement;
                        this.$l1 = n;
                        this.$maxHeight = n2;
                        this.$fontRenderer = fontRenderer;
                        super(0);
                    }
                });
                GL11.glPopMatrix();
                GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
                GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
            }
            if (((Boolean)this.blurValue.get()).booleanValue()) {
                GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glPushMatrix();
                if (((Boolean)this.bgRoundedValue.get()).booleanValue()) {
                    if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                        BlurUtils.blurAreaRounded((float)this.getRenderX() + ((float)l1 + 2.0f) * this.getScale(), (float)this.getRenderY() + -2.0f * this.getScale(), (float)this.getRenderX() + -5.0f * this.getScale(), (float)this.getRenderY() + (float)(maxHeight + fontRenderer.field_78288_b) * this.getScale(), ((Number)this.roundStrength.get()).floatValue(), ((Number)this.blurStrength.get()).floatValue());
                    } else {
                        BlurUtils.blurAreaRounded((float)this.getRenderX() + ((float)l1 - 2.0f) * this.getScale(), (float)this.getRenderY() + -2.0f * this.getScale(), (float)this.getRenderX() + 5.0f * this.getScale(), (float)this.getRenderY() + (float)(maxHeight + fontRenderer.field_78288_b) * this.getScale(), ((Number)this.roundStrength.get()).floatValue(), ((Number)this.blurStrength.get()).floatValue());
                    }
                } else if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    BlurUtils.blurArea((float)this.getRenderX() + ((float)l1 + 2.0f) * this.getScale(), (float)this.getRenderY() + -2.0f * this.getScale(), (float)this.getRenderX() + -5.0f * this.getScale(), (float)this.getRenderY() + (float)(maxHeight + fontRenderer.field_78288_b) * this.getScale(), ((Number)this.blurStrength.get()).floatValue());
                } else {
                    BlurUtils.blurArea((float)this.getRenderX() + ((float)l1 - 2.0f) * this.getScale(), (float)this.getRenderY() + -2.0f * this.getScale(), (float)this.getRenderX() + 5.0f * this.getScale(), (float)this.getRenderY() + (float)(maxHeight + fontRenderer.field_78288_b) * this.getScale(), ((Number)this.blurStrength.get()).floatValue());
                }
                GL11.glPopMatrix();
                GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
                GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
            }
            if (((Boolean)this.bgRoundedValue.get()).booleanValue()) {
                Stencil.write(false);
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect((float)l1 + (this.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), (Boolean)this.rectValue.get() != false ? -2.0f - (float)((Number)this.rectHeight.get()).intValue() : -2.0f, this.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, maxHeight + fontRenderer.field_78288_b, ((Number)this.roundStrength.get()).floatValue());
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                Stencil.erase(true);
            }
            if (((Boolean)this.useVanillaBackground.get()).booleanValue()) {
                if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    Gui.func_73734_a((int)(l1 + 2), (int)-2, (int)-5, (int)(-2 + fontRenderer.field_78288_b + 1), (int)0x60000000);
                    Gui.func_73734_a((int)(l1 + 2), (int)(-2 + fontRenderer.field_78288_b + 1), (int)-5, (int)(maxHeight + fontRenderer.field_78288_b), (int)0x50000000);
                } else {
                    Gui.func_73734_a((int)(l1 - 2), (int)-2, (int)5, (int)(-2 + fontRenderer.field_78288_b + 1), (int)0x60000000);
                    Gui.func_73734_a((int)(l1 - 2), (int)(-2 + fontRenderer.field_78288_b + 1), (int)5, (int)(maxHeight + fontRenderer.field_78288_b), (int)0x50000000);
                }
            } else if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                Gui.func_73734_a((int)(l1 + 2), (int)-2, (int)-5, (int)(maxHeight + fontRenderer.field_78288_b), (int)backColor);
            } else {
                Gui.func_73734_a((int)(l1 - 2), (int)-2, (int)5, (int)(maxHeight + fontRenderer.field_78288_b), (int)backColor);
            }
            if (((Boolean)this.rectValue.get()).booleanValue()) {
                it = rectColorMode;
                $i$a$-let-ScoreboardElement$drawElement$LiquidSlowly$1 = false;
                v16 = it;
                if (v16 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                v17 = v16.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(v17, "(this as java.lang.String).toLowerCase()");
                it = v17;
                tmp = -1;
                switch (it.hashCode()) {
                    case 113953: {
                        if (!it.equals("sky")) break;
                        tmp = 1;
                        break;
                    }
                    case 973576630: {
                        if (!it.equals("rainbow")) break;
                        tmp = 2;
                        break;
                    }
                    case 3135100: {
                        if (!it.equals("fade")) break;
                        tmp = 3;
                        break;
                    }
                    case -132200566: {
                        if (!it.equals("liquidslowly")) break;
                        tmp = 4;
                        break;
                    }
                    case 103910409: {
                        if (!it.equals("mixer")) break;
                        tmp = 5;
                        break;
                    }
                }
                switch (tmp) {
                    case 1: {
                        v18 = RenderUtils.SkyRainbow(0, ((Number)hud2.getSaturationValue().get()).floatValue(), ((Number)hud2.getBrightnessValue().get()).floatValue());
                        break;
                    }
                    case 2: {
                        v18 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)hud2.getSaturationValue().get()).floatValue(), ((Number)hud2.getBrightnessValue().get()).floatValue(), 0);
                        break;
                    }
                    case 4: {
                        v18 = liquidSlowli;
                        break;
                    }
                    case 3: {
                        v18 = FadeColor;
                        break;
                    }
                    case 5: {
                        v18 = mixerColor;
                        break;
                    }
                    default: {
                        v18 = rectColor = rectCustomColor;
                    }
                }
                if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    Gui.func_73734_a((int)(l1 + 2), (int)-2, (int)-5, (int)(-2 - ((Number)this.rectHeight.get()).intValue()), (int)rectColor);
                } else {
                    Gui.func_73734_a((int)(l1 - 2), (int)-2, (int)5, (int)(-2 - ((Number)this.rectHeight.get()).intValue()), (int)rectColor);
                }
            }
            if (((Boolean)this.bgRoundedValue.get()).booleanValue()) {
                Stencil.dispose();
            }
        }
        v19 = scoreCollection;
        Intrinsics.checkExpressionValueIsNotNull(v19, "scoreCollection");
        $this$forEachIndexed$iv = v19;
        $i$f$forEachIndexed = false;
        index$iv = 0;
        for (T item$iv : $this$forEachIndexed$iv) {
            block89: {
                block90: {
                    var27_38 = index$iv++;
                    var28_39 = false;
                    if (var27_38 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var29_40 = var27_38;
                    var30_41 = (Score)item$iv;
                    index = var29_40;
                    $i$a$-forEachIndexed-ScoreboardElement$drawElement$3 = false;
                    v20 = score;
                    Intrinsics.checkExpressionValueIsNotNull(v20, "score");
                    team = scoreboard.func_96509_i(v20.func_96653_e());
                    name = ScorePlayerTeam.func_96667_a((Team)((Team)team), (String)score.func_96653_e());
                    scorePoints = "" + EnumChatFormatting.RED + score.func_96652_c();
                    width = 5;
                    height = maxHeight - index * fontRenderer.field_78288_b;
                    changed = false;
                    v21 = ColorUtils.stripColor(name);
                    if (v21 == null) {
                        Intrinsics.throwNpe();
                    }
                    stripped = StringUtils.fixString(v21);
                    GlStateManager.func_179117_G();
                    if (((Boolean)this.changeDomain.get()).booleanValue() && this.cachedDomains.contains(stripped)) {
                        name = "Report1337";
                        changed = true;
                    }
                    if (((Boolean)this.antiSnipeMatch.get()).booleanValue()) {
                        v22 = stripped;
                        Intrinsics.checkExpressionValueIsNotNull(v22, "stripped");
                        if (this.hypickleRegex.containsMatchIn(v22)) {
                            name = "";
                        }
                    }
                    if (!changed) break block90;
                    stringZ = "";
                    var41_52 = 0;
                    var42_53 = name.length() - 1;
                    if (var41_52 > var42_53) break block89;
                    while (true) {
                        block93: {
                            block94: {
                                block91: {
                                    block92: {
                                        if (StringsKt.equals(rectColorMode, "Sky", true)) {
                                            v23 = RenderUtils.SkyRainbow((int)(z * ((Number)this.delayValue.get()).intValue()), ((Number)hud2.getSaturationValue().get()).floatValue(), ((Number)hud2.getBrightnessValue().get()).floatValue());
                                        } else if (StringsKt.equals(rectColorMode, "Rainbow", true)) {
                                            v23 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)hud2.getSaturationValue().get()).floatValue(), ((Number)hud2.getBrightnessValue().get()).floatValue(), (int)(z * ((Number)this.delayValue.get()).intValue()));
                                        } else if (StringsKt.equals(rectColorMode, "LiquidSlowly", true)) {
                                            v24 = ColorUtils.LiquidSlowly(System.nanoTime(), (int)(z * ((Number)this.delayValue.get()).intValue()), ((Number)hud2.getSaturationValue().get()).floatValue(), ((Number)hud2.getBrightnessValue().get()).floatValue());
                                            if (v24 == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            v23 = v24.getRGB();
                                        } else if (StringsKt.equals(rectColorMode, "Fade", true)) {
                                            v23 = ColorUtils.fade(new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()), (int)(z * ((Number)this.delayValue.get()).intValue()), 100).getRGB();
                                        } else if (StringsKt.equals(rectColorMode, "Mixer", true)) {
                                            v25 = ColorMixer.getMixedColor((int)(z * ((Number)this.delayValue.get()).intValue()), ((Number)this.cRainbowSecValue.get()).intValue());
                                            Intrinsics.checkExpressionValueIsNotNull(v25, "ColorMixer.getMixedColor\u2026, cRainbowSecValue.get())");
                                            v23 = v25.getRGB();
                                        } else {
                                            v23 = typeColor = rectCustomColor;
                                        }
                                        if (this.getSide().getHorizontal() != Side.Horizontal.LEFT) break block91;
                                        var44_55 = (String)this.domainShadowValue.get();
                                        var45_56 = false;
                                        v26 = var44_55;
                                        if (v26 == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                        }
                                        v27 = v26.toLowerCase();
                                        Intrinsics.checkExpressionValueIsNotNull(v27, "(this as java.lang.String).toLowerCase()");
                                        var44_55 = v27;
                                        switch (var44_55.hashCode()) {
                                            case 1544803905: {
                                                if (!var44_55.equals("default")) ** break;
                                                break;
                                            }
                                            case -1106245566: {
                                                if (!var44_55.equals("outline")) ** break;
                                                break block92;
                                            }
                                            case 3387192: {
                                                if (!var44_55.equals("none")) ** break;
                                                ((FontRenderer)this.domainFontValue.get()).func_175065_a(String.valueOf(name.charAt((int)z)), -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ), (float)height + ((Number)this.domainFontYValue.get()).floatValue(), typeColor, false);
                                                ** break;
                                            }
                                        }
                                        ((FontRenderer)this.domainFontValue.get()).func_175063_a(String.valueOf(name.charAt((int)z)), -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ), (float)height + ((Number)this.domainFontYValue.get()).floatValue(), typeColor);
                                        break block93;
                                    }
                                    v28 = (FontRenderer)this.domainFontValue.get();
                                    v29 = String.valueOf(name.charAt((int)z));
                                    v30 = -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ) - ((Number)this.outlineWidthValue.get()).floatValue();
                                    v31 = (float)height + ((Number)this.domainFontYValue.get()).floatValue();
                                    v32 = Color.black;
                                    Intrinsics.checkExpressionValueIsNotNull(v32, "Color.black");
                                    v28.func_175065_a(v29, v30, v31, v32.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                                    v33 = (FontRenderer)this.domainFontValue.get();
                                    v34 = String.valueOf(name.charAt((int)z));
                                    v35 = -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ) + ((Number)this.outlineWidthValue.get()).floatValue();
                                    v36 = (float)height + ((Number)this.domainFontYValue.get()).floatValue();
                                    v37 = Color.black;
                                    Intrinsics.checkExpressionValueIsNotNull(v37, "Color.black");
                                    v33.func_175065_a(v34, v35, v36, v37.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                                    v38 = (FontRenderer)this.domainFontValue.get();
                                    v39 = String.valueOf(name.charAt((int)z));
                                    v40 = -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ);
                                    v41 = (float)height + ((Number)this.domainFontYValue.get()).floatValue() - ((Number)this.outlineWidthValue.get()).floatValue();
                                    v42 = Color.black;
                                    Intrinsics.checkExpressionValueIsNotNull(v42, "Color.black");
                                    v38.func_175065_a(v39, v40, v41, v42.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                                    v43 = (FontRenderer)this.domainFontValue.get();
                                    v44 = String.valueOf(name.charAt((int)z));
                                    v45 = -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ);
                                    v46 = (float)height + ((Number)this.domainFontYValue.get()).floatValue() + ((Number)this.outlineWidthValue.get()).floatValue();
                                    v47 = Color.black;
                                    Intrinsics.checkExpressionValueIsNotNull(v47, "Color.black");
                                    v43.func_175065_a(v44, v45, v46, v47.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                                    ((FontRenderer)this.domainFontValue.get()).func_175065_a(String.valueOf(name.charAt((int)z)), -3.0f + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ), (float)height + ((Number)this.domainFontYValue.get()).floatValue(), typeColor, ((Boolean)this.shadowValue.get()).booleanValue());
lbl319:
                                    // 6 sources

                                    break block93;
                                }
                                var44_55 = (String)this.domainShadowValue.get();
                                var45_56 = false;
                                v48 = var44_55;
                                if (v48 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                v49 = v48.toLowerCase();
                                Intrinsics.checkExpressionValueIsNotNull(v49, "(this as java.lang.String).toLowerCase()");
                                var44_55 = v49;
                                switch (var44_55.hashCode()) {
                                    case 1544803905: {
                                        if (!var44_55.equals("default")) ** break;
                                        break;
                                    }
                                    case -1106245566: {
                                        if (!var44_55.equals("outline")) ** break;
                                        break block94;
                                    }
                                    case 3387192: {
                                        if (!var44_55.equals("none")) ** break;
                                        ((FontRenderer)this.domainFontValue.get()).func_175065_a(String.valueOf(name.charAt((int)z)), (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ), (float)height + ((Number)this.domainFontYValue.get()).floatValue(), typeColor, false);
                                        ** break;
                                    }
                                }
                                ((FontRenderer)this.domainFontValue.get()).func_175063_a(String.valueOf(name.charAt((int)z)), (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ), (float)height + ((Number)this.domainFontYValue.get()).floatValue(), typeColor);
                                ** break;
                            }
                            v50 = (FontRenderer)this.domainFontValue.get();
                            v51 = String.valueOf(name.charAt((int)z));
                            v52 = (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ) - ((Number)this.outlineWidthValue.get()).floatValue();
                            v53 = (float)height + ((Number)this.domainFontYValue.get()).floatValue();
                            v54 = Color.black;
                            Intrinsics.checkExpressionValueIsNotNull(v54, "Color.black");
                            v50.func_175065_a(v51, v52, v53, v54.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                            v55 = (FontRenderer)this.domainFontValue.get();
                            v56 = String.valueOf(name.charAt((int)z));
                            v57 = (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ) + ((Number)this.outlineWidthValue.get()).floatValue();
                            v58 = (float)height + ((Number)this.domainFontYValue.get()).floatValue();
                            v59 = Color.black;
                            Intrinsics.checkExpressionValueIsNotNull(v59, "Color.black");
                            v55.func_175065_a(v56, v57, v58, v59.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                            v60 = (FontRenderer)this.domainFontValue.get();
                            v61 = String.valueOf(name.charAt((int)z));
                            v62 = (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ);
                            v63 = (float)height + ((Number)this.domainFontYValue.get()).floatValue() - ((Number)this.outlineWidthValue.get()).floatValue();
                            v64 = Color.black;
                            Intrinsics.checkExpressionValueIsNotNull(v64, "Color.black");
                            v60.func_175065_a(v61, v62, v63, v64.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                            v65 = (FontRenderer)this.domainFontValue.get();
                            v66 = String.valueOf(name.charAt((int)z));
                            v67 = (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ);
                            v68 = (float)height + ((Number)this.domainFontYValue.get()).floatValue() + ((Number)this.outlineWidthValue.get()).floatValue();
                            v69 = Color.black;
                            Intrinsics.checkExpressionValueIsNotNull(v69, "Color.black");
                            v65.func_175065_a(v66, v67, v68, v69.getRGB(), ((Boolean)this.shadowValue.get()).booleanValue());
                            ((FontRenderer)this.domainFontValue.get()).func_175065_a(String.valueOf(name.charAt((int)z)), (float)l1 + (float)((FontRenderer)this.domainFontValue.get()).func_78256_a(stringZ), (float)height + ((Number)this.domainFontYValue.get()).floatValue(), typeColor, ((Boolean)this.shadowValue.get()).booleanValue());
                        }
                        stringZ = stringZ + String.valueOf(name.charAt((int)z));
                        if (z != var42_53) {
                            ++z;
                            continue;
                        }
                        break block89;
                        break;
                    }
                }
                if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    fontRenderer.func_175065_a(name, -3.0f, (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
                } else {
                    fontRenderer.func_175065_a(name, (float)l1, (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
                }
            }
            if (((Boolean)this.showRedNumbersValue.get()).booleanValue()) {
                if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    fontRenderer.func_175065_a(scorePoints, (float)(l1 + 1 - fontRenderer.func_78256_a(scorePoints)), (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
                } else {
                    fontRenderer.func_175065_a(scorePoints, (float)(width - fontRenderer.func_78256_a(scorePoints)), (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
                }
            }
            if (index != scoreCollection.size() - 1) continue;
            displayName = objective.func_96678_d();
            GlStateManager.func_179117_G();
            fontRenderer.func_175065_a(displayName, this.getSide().getHorizontal() == Side.Horizontal.LEFT ? (float)(maxWidth / 2 - fontRenderer.func_78256_a(displayName) / 2) : (float)(l1 + maxWidth / 2 - fontRenderer.func_78256_a(displayName) / 2), (float)(height - fontRenderer.field_78288_b), -1, ((Boolean)this.shadowValue.get()).booleanValue());
        }
        return this.getSide().getHorizontal() == Side.Horizontal.LEFT ? new Border((float)maxWidth + (float)5, -2.0f, -5.0f, (float)maxHeight + (float)fontRenderer.field_78288_b) : new Border(-((float)maxWidth) - (float)5, -2.0f, 5.0f, (float)maxHeight + (float)fontRenderer.field_78288_b);
    }

    private final Color backgroundColor() {
        return new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
    }

    public ScoreboardElement(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        this.useVanillaBackground = new BoolValue("UseVanillaBackground", false);
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getUseVanillaBackground$p(this.this$0).get() == false;
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getUseVanillaBackground$p(this.this$0).get() == false;
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getUseVanillaBackground$p(this.this$0).get() == false;
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 95, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getUseVanillaBackground$p(this.this$0).get() == false;
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.rectValue = new BoolValue("Rect", false);
        this.rectHeight = new IntegerValue("Rect-Height", 1, 1, 10, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getRectValue$p(this.this$0).get();
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getBlurValue$p(this.this$0).get();
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.shadowShaderValue = new BoolValue("Shadow", false);
        this.shadowStrength = new FloatValue("Shadow-Strength", 0.0f, 0.0f, 30.0f, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getShadowShaderValue$p(this.this$0).get();
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.shadowColorMode = new ListValue("Shadow-Color", new String[]{"Background", "Custom"}, "Background", new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getShadowShaderValue$p(this.this$0).get();
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.shadowColorRedValue = new IntegerValue("Shadow-Red", 0, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getShadowShaderValue$p(this.this$0).get() != false && StringsKt.equals((String)ScoreboardElement.access$getShadowColorMode$p(this.this$0).get(), "custom", true);
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.shadowColorGreenValue = new IntegerValue("Shadow-Green", 111, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getShadowShaderValue$p(this.this$0).get() != false && StringsKt.equals((String)ScoreboardElement.access$getShadowColorMode$p(this.this$0).get(), "custom", true);
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.shadowColorBlueValue = new IntegerValue("Shadow-Blue", 255, 0, 255, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getShadowShaderValue$p(this.this$0).get() != false && StringsKt.equals((String)ScoreboardElement.access$getShadowColorMode$p(this.this$0).get(), "custom", true);
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.bgRoundedValue = new BoolValue("Rounded", false);
        this.roundStrength = new FloatValue("Rounded-Strength", 5.0f, 0.0f, 30.0f, new Function0<Boolean>(this){
            final /* synthetic */ ScoreboardElement this$0;

            public final boolean invoke() {
                return (Boolean)ScoreboardElement.access$getBgRoundedValue$p(this.this$0).get();
            }
            {
                this.this$0 = scoreboardElement;
                super(0);
            }
        });
        this.rectColorModeValue = new ListValue("Color", new String[]{"Custom", "Rainbow", "LiquidSlowly", "Fade", "Sky", "Mixer"}, "Custom");
        this.rectColorRedValue = new IntegerValue("Red", 0, 0, 255);
        this.rectColorGreenValue = new IntegerValue("Green", 111, 0, 255);
        this.rectColorBlueValue = new IntegerValue("Blue", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Alpha", 255, 0, 255);
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.delayValue = new IntegerValue("Delay", 50, 0, 200);
        this.shadowValue = new BoolValue("Shadow", false);
        this.antiSnipeMatch = new BoolValue("AntiSnipeMatch", true);
        this.changeDomain = new BoolValue("ChangeDomain", false);
        this.showRedNumbersValue = new BoolValue("ShowRedNumbers", false);
        FontRenderer fontRenderer = Fonts.minecraftFont;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "Fonts.minecraftFont");
        this.fontValue = new FontValue("Font", fontRenderer);
        FontRenderer fontRenderer2 = Fonts.minecraftFont;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer2, "Fonts.minecraftFont");
        this.domainFontValue = new FontValue("DomainFont", fontRenderer2);
        this.domainFontYValue = new FloatValue("Domain-TextY", 0.0f, 0.0f, 3.0f);
        this.domainShadowValue = new ListValue("Domain-Shadow", new String[]{"None", "Outline", "Default"}, "None");
        this.outlineWidthValue = new FloatValue("OutlineWidth", 0.5f, 0.5f, 2.0f);
        this.domainList = new String[]{".ac", ".academy", ".accountant", ".accountants", ".actor", ".adult", ".ag", ".agency", ".ai", ".airforce", ".am", ".amsterdam", ".apartments", ".app", ".archi", ".army", ".art", ".asia", ".associates", ".at", ".attorney", ".au", ".auction", ".auto", ".autos", ".baby", ".band", ".bar", ".barcelona", ".bargains", ".bayern", ".be", ".beauty", ".beer", ".berlin", ".best", ".bet", ".bid", ".bike", ".bingo", ".bio", ".biz", ".biz.pl", ".black", ".blog", ".blue", ".boats", ".boston", ".boutique", ".build", ".builders", ".business", ".buzz", ".bz", ".ca", ".cab", ".cafe", ".camera", ".camp", ".capital", ".car", ".cards", ".care", ".careers", ".cars", ".casa", ".cash", ".casino", ".catering", ".cc", ".center", ".ceo", ".ch", ".charity", ".chat", ".cheap", ".church", ".city", ".cl", ".claims", ".cleaning", ".clinic", ".clothing", ".cloud", ".club", ".cn", ".co", ".co.in", ".co.jp", ".co.kr", ".co.nz", ".co.uk", ".co.za", ".coach", ".codes", ".coffee", ".college", ".com", ".com.ag", ".com.au", ".com.br", ".com.bz", ".com.cn", ".com.co", ".com.es", ".com.mx", ".com.pe", ".com.ph", ".com.pl", ".com.ru", ".com.tw", ".community", ".company", ".computer", ".condos", ".construction", ".consulting", ".contact", ".contractors", ".cooking", ".cool", ".country", ".coupons", ".courses", ".credit", ".creditcard", ".cricket", ".cruises", ".cymru", ".cz", ".dance", ".date", ".dating", ".de", ".deals", ".degree", ".delivery", ".democrat", ".dental", ".dentist", ".design", ".dev", ".diamonds", ".digital", ".direct", ".directory", ".discount", ".dk", ".doctor", ".dog", ".domains", ".download", ".earth", ".education", ".email", ".energy", ".engineer", ".engineering", ".enterprises", ".equipment", ".es", ".estate", ".eu", ".events", ".exchange", ".expert", ".exposed", ".express", ".fail", ".faith", ".family", ".fan", ".fans", ".farm", ".fashion", ".film", ".finance", ".financial", ".firm.in", ".fish", ".fishing", ".fit", ".fitness", ".flights", ".florist", ".fm", ".football", ".forsale", ".foundation", ".fr", ".fun", ".fund", ".furniture", ".futbol", ".fyi", ".gallery", ".games", ".garden", ".gay", ".gen.in", ".gg", ".gifts", ".gives", ".glass", ".global", ".gmbh", ".gold", ".golf", ".graphics", ".gratis", ".green", ".gripe", ".group", ".gs", ".guide", ".guru", ".hair", ".haus", ".health", ".healthcare", ".hockey", ".holdings", ".holiday", ".homes", ".horse", ".hospital", ".host", ".house", ".idv.tw", ".immo", ".immobilien", ".in", ".inc", ".ind.in", ".industries", ".info", ".info.pl", ".ink", ".institute", ".insure", ".international", ".investments", ".io", ".irish", ".ist", ".istanbul", ".it", ".jetzt", ".jewelry", ".jobs", ".jp", ".kaufen", ".kim", ".kitchen", ".kiwi", ".kr", ".la", ".land", ".law", ".lawyer", ".lease", ".legal", ".lgbt", ".life", ".lighting", ".limited", ".limo", ".live", ".llc", ".loan", ".loans", ".london", ".love", ".ltd", ".ltda", ".luxury", ".maison", ".makeup", ".management", ".market", ".marketing", ".mba", ".me", ".me.uk", ".media", ".melbourne", ".memorial", ".men", ".menu", ".miami", ".mobi", ".moda", ".moe", ".money", ".monster", ".mortgage", ".motorcycles", ".movie", ".ms", ".mx", ".nagoya", ".name", ".navy", ".ne", ".ne.kr", ".net", ".net.ag", ".net.au", ".net.br", ".net.bz", ".net.cn", ".net.co", ".net.in", ".net.nz", ".net.pe", ".net.ph", ".net.pl", ".net.ru", ".network", ".news", ".ninja", ".nl", ".no", ".nom.co", ".nom.es", ".nom.pe", ".nrw", ".nyc", ".okinawa", ".one", ".onl", ".online", ".org", ".org.ag", ".org.au", ".org.cn", ".org.es", ".org.in", ".org.nz", ".org.pe", ".org.ph", ".org.pl", ".org.ru", ".org.uk", ".page", ".paris", ".partners", ".parts", ".party", ".pe", ".pet", ".ph", ".photography", ".photos", ".pictures", ".pink", ".pizza", ".pl", ".place", ".plumbing", ".plus", ".poker", ".porn", ".press", ".pro", ".productions", ".promo", ".properties", ".protection", ".pub", ".pw", ".quebec", ".quest", ".racing", ".re.kr", ".realestate", ".recipes", ".red", ".rehab", ".reise", ".reisen", ".rent", ".rentals", ".repair", ".report", ".republican", ".rest", ".restaurant", ".review", ".reviews", ".rich", ".rip", ".rocks", ".rodeo", ".ru", ".run", ".ryukyu", ".sale", ".salon", ".sarl", ".school", ".schule", ".science", ".se", ".security", ".services", ".sex", ".sg", ".sh", ".shiksha", ".shoes", ".shop", ".shopping", ".show", ".singles", ".site", ".ski", ".skin", ".soccer", ".social", ".software", ".solar", ".solutions", ".space", ".storage", ".store", ".stream", ".studio", ".study", ".style", ".supplies", ".supply", ".support", ".surf", ".surgery", ".sydney", ".systems", ".tax", ".taxi", ".team", ".tech", ".technology", ".tel", ".tennis", ".theater", ".theatre", ".tienda", ".tips", ".tires", ".today", ".tokyo", ".tools", ".tours", ".town", ".toys", ".top", ".trade", ".training", ".travel", ".tube", ".tv", ".tw", ".uk", ".university", ".uno", ".us", ".vacations", ".vegas", ".ventures", ".vet", ".viajes", ".video", ".villas", ".vin", ".vip", ".vision", ".vodka", ".vote", ".voto", ".voyage", ".wales", ".watch", ".webcam", ".website", ".wedding", ".wiki", ".win", ".wine", ".work", ".works", ".world", ".ws", ".wtf", ".xxx", ".xyz", ".yachts", ".yoga", ".yokohama", ".zone", ".vn"};
        ScoreboardElement scoreboardElement = this;
        boolean bl = false;
        ArrayList arrayList = new ArrayList();
        scoreboardElement.cachedDomains = arrayList;
        this.garbageTimer = new MSTimer();
        this.hypickleRegex = new Regex("[0-9][0-9]/[0-9][0-9]/[0-9][0-9]");
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

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ BoolValue access$getBgRoundedValue$p(ScoreboardElement $this) {
        return $this.bgRoundedValue;
    }

    public static final /* synthetic */ BoolValue access$getRectValue$p(ScoreboardElement $this) {
        return $this.rectValue;
    }

    public static final /* synthetic */ IntegerValue access$getRectHeight$p(ScoreboardElement $this) {
        return $this.rectHeight;
    }

    public static final /* synthetic */ FloatValue access$getRoundStrength$p(ScoreboardElement $this) {
        return $this.roundStrength;
    }

    public static final /* synthetic */ ListValue access$getShadowColorMode$p(ScoreboardElement $this) {
        return $this.shadowColorMode;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorRedValue$p(ScoreboardElement $this) {
        return $this.backgroundColorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorGreenValue$p(ScoreboardElement $this) {
        return $this.backgroundColorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorBlueValue$p(ScoreboardElement $this) {
        return $this.backgroundColorBlueValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorRedValue$p(ScoreboardElement $this) {
        return $this.shadowColorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorGreenValue$p(ScoreboardElement $this) {
        return $this.shadowColorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorBlueValue$p(ScoreboardElement $this) {
        return $this.shadowColorBlueValue;
    }

    public static final /* synthetic */ BoolValue access$getUseVanillaBackground$p(ScoreboardElement $this) {
        return $this.useVanillaBackground;
    }

    public static final /* synthetic */ BoolValue access$getBlurValue$p(ScoreboardElement $this) {
        return $this.blurValue;
    }

    public static final /* synthetic */ BoolValue access$getShadowShaderValue$p(ScoreboardElement $this) {
        return $this.shadowShaderValue;
    }
}

