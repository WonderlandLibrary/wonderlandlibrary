/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import liying.utils.ShadowUtils;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ScoreboardElement;
import net.ccbluex.liquidbounce.ui.font.FontLoaders;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Scoreboard")
public final class ScoreboardElement
extends Element {
    private final BoolValue rectValue;
    private final IntegerValue shadowColorGreenValue;
    private final ListValue rectColorModeValue;
    private final IntegerValue rectColorBlueValue;
    private final BoolValue noPointValue;
    private final IntegerValue textBlueValue;
    private final ListValue serverValue;
    private final IntegerValue backgroundColorGreenValue;
    private final FontValue fontValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue rectColorGreenValue;
    private final IntegerValue rectColorBlueAlpha;
    private final IntegerValue shadowColorRedValue;
    private final IntegerValue backgroundColorAlphaValue;
    private final String[] allowedDomains;
    private final ListValue shadowColorMode;
    private final IntegerValue textGreenValue;
    private final BoolValue shadowValue;
    private final BoolValue shadowShaderValue;
    private final IntegerValue backgroundColorRedValue;
    private final IntegerValue shadowColorBlueValue;
    private final IntegerValue rectColorRedValue;
    private final IntegerValue textRedValue;

    public ScoreboardElement() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final IntegerValue access$getShadowColorBlueValue$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.shadowColorBlueValue;
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public ScoreboardElement(double d, double d2, float f, Side side) {
        super(d, d2, f, side);
        this.textRedValue = new IntegerValue("Text-R", 255, 0, 255);
        this.textGreenValue = new IntegerValue("Text-G", 255, 0, 255);
        this.textBlueValue = new IntegerValue("Text-B", 255, 0, 255);
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 95, 0, 255);
        this.shadowShaderValue = new BoolValue("Shadow", false);
        this.shadowColorMode = new ListValue("Shadow-Color", new String[]{"Background", "Custom"}, "Background");
        this.shadowColorRedValue = new IntegerValue("Shadow-Red", 0, 0, 255);
        this.shadowColorGreenValue = new IntegerValue("Shadow-Green", 111, 0, 255);
        this.shadowColorBlueValue = new IntegerValue("Shadow-Blue", 255, 0, 255);
        this.rectValue = new BoolValue("Rect", false);
        this.rectColorModeValue = new ListValue("Rect-Color", new String[]{"Custom", "Rainbow"}, "Custom");
        this.rectColorRedValue = new IntegerValue("Rect-R", 0, 0, 255);
        this.rectColorGreenValue = new IntegerValue("Rect-G", 111, 0, 255);
        this.rectColorBlueValue = new IntegerValue("Rect-B", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Rect-Alpha", 255, 0, 255);
        this.shadowValue = new BoolValue("Shadow", false);
        this.serverValue = new ListValue("ServerIp", new String[]{"None", "ClientName"}, "ClientName");
        this.noPointValue = new BoolValue("NoPoints", false);
        this.fontValue = new FontValue("Font", Fonts.minecraftFont);
        this.allowedDomains = new String[]{".ac", ".academy", ".accountant", ".accountants", ".actor", ".adult", ".ag", ".agency", ".ai", ".airforce", ".am", ".amsterdam", ".apartments", ".app", ".archi", ".army", ".art", ".asia", ".associates", ".at", ".attorney", ".au", ".auction", ".auto", ".autos", ".baby", ".band", ".bar", ".barcelona", ".bargains", ".bayern", ".be", ".beauty", ".beer", ".berlin", ".best", ".bet", ".bid", ".bike", ".bingo", ".bio", ".biz", ".biz.pl", ".black", ".blog", ".blue", ".boats", ".boston", ".boutique", ".build", ".builders", ".business", ".buzz", ".bz", ".ca", ".cab", ".cafe", ".camera", ".camp", ".capital", ".car", ".cards", ".care", ".careers", ".cars", ".casa", ".cash", ".casino", ".catering", ".cc", ".center", ".ceo", ".ch", ".charity", ".chat", ".cheap", ".church", ".city", ".cl", ".claims", ".cleaning", ".clinic", ".clothing", ".cloud", ".club", ".cn", ".co", ".co.in", ".co.jp", ".co.kr", ".co.nz", ".co.uk", ".co.za", ".coach", ".codes", ".coffee", ".college", ".com", ".com.ag", ".com.au", ".com.br", ".com.bz", ".com.cn", ".com.co", ".com.es", ".com.mx", ".com.pe", ".com.ph", ".com.pl", ".com.ru", ".com.tw", ".community", ".company", ".computer", ".condos", ".construction", ".consulting", ".contact", ".contractors", ".cooking", ".cool", ".country", ".coupons", ".courses", ".credit", ".creditcard", ".cricket", ".cruises", ".cymru", ".cz", ".dance", ".date", ".dating", ".de", ".deals", ".degree", ".delivery", ".democrat", ".dental", ".dentist", ".design", ".dev", ".diamonds", ".digital", ".direct", ".directory", ".discount", ".dk", ".doctor", ".dog", ".domains", ".download", ".earth", ".education", ".email", ".energy", ".engineer", ".engineering", ".enterprises", ".equipment", ".es", ".estate", ".eu", ".events", ".exchange", ".expert", ".exposed", ".express", ".fail", ".faith", ".family", ".fan", ".fans", ".farm", ".fashion", ".film", ".finance", ".financial", ".firm.in", ".fish", ".fishing", ".fit", ".fitness", ".flights", ".florist", ".fm", ".football", ".forsale", ".foundation", ".fr", ".fun", ".fund", ".furniture", ".futbol", ".fyi", ".gallery", ".games", ".garden", ".gay", ".gen.in", ".gg", ".gifts", ".gives", ".glass", ".global", ".gmbh", ".gold", ".golf", ".graphics", ".gratis", ".green", ".gripe", ".group", ".gs", ".guide", ".guru", ".hair", ".haus", ".health", ".healthcare", ".hockey", ".holdings", ".holiday", ".homes", ".horse", ".hospital", ".host", ".house", ".idv.tw", ".immo", ".immobilien", ".in", ".inc", ".ind.in", ".industries", ".info", ".info.pl", ".ink", ".institute", ".insure", ".international", ".investments", ".io", ".irish", ".ist", ".istanbul", ".it", ".jetzt", ".jewelry", ".jobs", ".jp", ".kaufen", ".kim", ".kitchen", ".kiwi", ".kr", ".la", ".land", ".law", ".lawyer", ".lease", ".legal", ".lgbt", ".life", ".lighting", ".limited", ".limo", ".live", ".llc", ".loan", ".loans", ".london", ".love", ".ltd", ".ltda", ".luxury", ".maison", ".makeup", ".management", ".market", ".marketing", ".mba", ".me", ".me.uk", ".media", ".melbourne", ".memorial", ".men", ".menu", ".miami", ".mobi", ".moda", ".moe", ".money", ".monster", ".mortgage", ".motorcycles", ".movie", ".ms", ".mx", ".nagoya", ".name", ".navy", ".ne.kr", ".net", ".net.ag", ".net.au", ".net.br", ".net.bz", ".net.cn", ".net.co", ".net.in", ".net.nz", ".net.pe", ".net.ph", ".net.pl", ".net.ru", ".network", ".news", ".ninja", ".nl", ".no", ".nom.co", ".nom.es", ".nom.pe", ".nrw", ".nyc", ".okinawa", ".one", ".onl", ".online", ".org", ".org.ag", ".org.au", ".org.cn", ".org.es", ".org.in", ".org.nz", ".org.pe", ".org.ph", ".org.pl", ".org.ru", ".org.uk", ".page", ".paris", ".partners", ".parts", ".party", ".pe", ".pet", ".ph", ".photography", ".photos", ".pictures", ".pink", ".pizza", ".pl", ".place", ".plumbing", ".plus", ".poker", ".porn", ".press", ".pro", ".productions", ".promo", ".properties", ".protection", ".pub", ".pw", ".quebec", ".quest", ".racing", ".re.kr", ".realestate", ".recipes", ".red", ".rehab", ".reise", ".reisen", ".rent", ".rentals", ".repair", ".report", ".republican", ".rest", ".restaurant", ".review", ".reviews", ".rich", ".rip", ".rocks", ".rodeo", ".ru", ".run", ".ryukyu", ".sale", ".salon", ".sarl", ".school", ".schule", ".science", ".se", ".security", ".services", ".sex", ".sg", ".sh", ".shiksha", ".shoes", ".shop", ".shopping", ".show", ".singles", ".site", ".ski", ".skin", ".soccer", ".social", ".software", ".solar", ".solutions", ".space", ".storage", ".store", ".stream", ".studio", ".study", ".style", ".supplies", ".supply", ".support", ".surf", ".surgery", ".sydney", ".systems", ".tax", ".taxi", ".team", ".tech", ".technology", ".tel", ".tennis", ".theater", ".theatre", ".tienda", ".tips", ".tires", ".today", ".tokyo", ".tools", ".tours", ".town", ".toys", ".top", ".trade", ".training", ".travel", ".tube", ".tv", ".tw", ".uk", ".university", ".uno", ".us", ".vacations", ".vegas", ".ventures", ".vet", ".viajes", ".video", ".villas", ".vin", ".vip", ".vision", ".vodka", ".vote", ".voto", ".voyage", ".wales", ".watch", ".webcam", ".website", ".wedding", ".wiki", ".win", ".wine", ".work", ".works", ".world", ".ws", ".wtf", ".xxx", ".xyz", ".yachts", ".yoga", ".yokohama", ".zone", "\u82b1\u96e8\u5ead", "855712180"};
    }

    private final Color textColor() {
        return new Color(((Number)this.textRedValue.get()).intValue(), ((Number)this.textGreenValue.get()).intValue(), ((Number)this.textBlueValue.get()).intValue());
    }

    @Override
    public Border drawElement() {
        Object object;
        IScoreObjective iScoreObjective;
        int n;
        ITeam iTeam;
        IFontRenderer iFontRenderer = (IFontRenderer)this.fontValue.get();
        int n2 = this.textColor().getRGB();
        int n3 = this.backgroundColor().getRGB();
        String string = (String)this.rectColorModeValue.get();
        int n4 = new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()).getRGB();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IScoreboard iScoreboard = iWorldClient.getScoreboard();
        IScoreObjective iScoreObjective2 = null;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if ((iTeam = iScoreboard.getPlayersTeam(iEntityPlayerSP.getName())) != null && (n = iTeam.getChatFormat().getColorIndex()) >= 0) {
            iScoreObjective2 = iScoreboard.getObjectiveInDisplaySlot(3 + n);
        }
        if ((iScoreObjective = iScoreObjective2) == null) {
            iScoreObjective = iScoreboard.getObjectiveInDisplaySlot(1);
        }
        if (iScoreObjective == null) {
            return null;
        }
        IScoreObjective iScoreObjective3 = iScoreObjective;
        IScoreboard iScoreboard2 = iScoreObjective3.getScoreboard();
        Collection collection = iScoreboard2.getSortedScores(iScoreObjective3);
        ArrayList arrayList = Lists.newArrayList((Iterable)Iterables.filter((Iterable)collection, (Predicate)drawElement.scores.1.INSTANCE));
        collection = arrayList.size() > 15 ? (Collection)Lists.newArrayList((Iterable)Iterables.skip((Iterable)arrayList, (int)(collection.size() - 15))) : (Collection)arrayList;
        int n5 = iFontRenderer.getStringWidth(iScoreObjective3.getDisplayName());
        for (IScore iScore : (ArrayList)collection) {
            object = iScoreboard2.getPlayersTeam(iScore.getPlayerName());
            String string2 = ScoreboardElement.access$getFunctions$p$s1046033730().scoreboardFormatPlayerName((ITeam)object, iScore.getPlayerName()) + ": " + (Object)((Object)WEnumChatFormatting.RED) + iScore.getScorePoints();
            n5 = RangesKt.coerceAtLeast((int)n5, (int)iFontRenderer.getStringWidth(string2));
        }
        int n6 = collection.size() * iFontRenderer.getFontHeight();
        int n7 = -n5 - 3 - ((Boolean)this.rectValue.get() != false ? 3 : 0);
        if (((Boolean)this.shadowShaderValue.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPushMatrix();
            ShadowUtils.INSTANCE.shadow(20.0f, new Function0(this, n7, n6, iFontRenderer){
                final ScoreboardElement this$0;
                final IFontRenderer $fontRenderer;
                final int $l1;
                final int $maxHeight;

                public final void invoke() {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                    GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                    RenderUtils.newDrawRect((float)this.$l1 + (this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), -2.0f, this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, this.$maxHeight + this.$fontRenderer.getFontHeight(), StringsKt.equals((String)((String)ScoreboardElement.access$getShadowColorMode$p(this.this$0).get()), (String)"background", (boolean)true) ? new Color(((Number)ScoreboardElement.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB() : new Color(((Number)ScoreboardElement.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)ScoreboardElement.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB());
                    GL11.glPopMatrix();
                }

                public Object invoke() {
                    this.invoke();
                    return Unit.INSTANCE;
                }

                static {
                }
                {
                    this.this$0 = scoreboardElement;
                    this.$l1 = n;
                    this.$maxHeight = n2;
                    this.$fontRenderer = iFontRenderer;
                    super(0);
                }
            }, new Function0(this, n7, n6, iFontRenderer){
                final ScoreboardElement this$0;
                final int $l1;
                final IFontRenderer $fontRenderer;
                final int $maxHeight;

                public Object invoke() {
                    this.invoke();
                    return Unit.INSTANCE;
                }

                public final void invoke() {
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                    GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                    RenderUtils.quickDrawRect((float)this.$l1 + (this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? 2.0f : -2.0f), -2.0f, this.this$0.getSide().getHorizontal() == Side.Horizontal.LEFT ? -5.0f : 5.0f, this.$maxHeight + this.$fontRenderer.getFontHeight());
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179084_k();
                    GL11.glPopMatrix();
                }
                {
                    this.this$0 = scoreboardElement;
                    this.$l1 = n;
                    this.$maxHeight = n2;
                    this.$fontRenderer = iFontRenderer;
                    super(0);
                }

                static {
                }
            });
            GL11.glPopMatrix();
            GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        Gui.func_73734_a((int)(n7 - 7), (int)-5, (int)9, (int)(n6 + iFontRenderer.getFontHeight() + 5), (int)n3);
        object = collection;
        boolean bl = false;
        int n8 = 0;
        Iterator iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            Object t = iterator2.next();
            int n9 = n8++;
            boolean bl2 = false;
            if (n9 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n10 = n9;
            IScore iScore = (IScore)t;
            int n11 = n10;
            boolean bl3 = false;
            ITeam iTeam2 = iScoreboard2.getPlayersTeam(iScore.getPlayerName());
            String string3 = ScoreboardElement.access$getFunctions$p$s1046033730().scoreboardFormatPlayerName(iTeam2, iScore.getPlayerName());
            String string4 = "" + (Object)((Object)WEnumChatFormatting.RED) + iScore.getScorePoints();
            int n12 = 5 - ((Boolean)this.rectValue.get() != false ? 4 : 0);
            int n13 = n6 - n11 * iFontRenderer.getFontHeight();
            GlStateManager.func_179117_G();
            int n14 = n2;
            if (!this.serverValue.equals("none")) {
                for (String string5 : this.allowedDomains) {
                    String string6;
                    if (!StringsKt.contains((CharSequence)string3, (CharSequence)string5, (boolean)true)) continue;
                    String string7 = (String)this.serverValue.get();
                    boolean bl4 = false;
                    String string8 = string7;
                    if (string8 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string7 = string8.toLowerCase();
                    switch (string7.hashCode()) {
                        case 1103204566: {
                            if (string7.equals("clientname")) {
                                string6 = "AtField";
                                break;
                            }
                        }
                        default: {
                            string6 = "null";
                        }
                    }
                    string3 = string6;
                    n14 = new Color(148, 87, 235).getRGB();
                    break;
                }
            }
            FontLoaders.F16.drawString(string3, (float)n7, (float)n13, n14, (boolean)((Boolean)this.shadowValue.get()));
            if (!((Boolean)this.noPointValue.get()).booleanValue()) {
                FontLoaders.F16.drawString(string4, (float)(n12 - iFontRenderer.getStringWidth(string4)), (float)n13, n2, (boolean)((Boolean)this.shadowValue.get()));
            }
            if (n11 == collection.size() - 1) {
                String string5;
                string5 = iScoreObjective3.getDisplayName();
                GlStateManager.func_179117_G();
                FontLoaders.F18.drawString(string5, (float)(n7 + n5 / 2 - iFontRenderer.getStringWidth(string5) / 2), (float)(n13 - iFontRenderer.getFontHeight()), n2, (boolean)((Boolean)this.shadowValue.get()));
            }
            if (!((Boolean)this.rectValue.get()).booleanValue()) continue;
            int n15 = StringsKt.equals((String)string, (String)"Rainbow", (boolean)true) ? ColorUtils.rainbow(n11).getRGB() : n4;
            RenderUtils.drawRect(2.0f, n11 == collection.size() - 1 ? -2.0f : (float)n13, 5.0f, n11 == 0 ? (float)iFontRenderer.getFontHeight() : (float)n13 + (float)iFontRenderer.getFontHeight() * 2.0f, n15);
        }
        return new Border(-((float)n5) - 10.0f - (float)((Boolean)this.rectValue.get() != false ? 3 : 0), -5.0f, 9.0f, (float)n6 + (float)iFontRenderer.getFontHeight() + (float)5);
    }

    public static final IntegerValue access$getBackgroundColorRedValue$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.backgroundColorRedValue;
    }

    public ScoreboardElement(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    public static final IntegerValue access$getBackgroundColorBlueValue$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.backgroundColorBlueValue;
    }

    public static final IntegerValue access$getShadowColorRedValue$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.shadowColorRedValue;
    }

    private final Color backgroundColor() {
        return new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
    }

    public static final ListValue access$getShadowColorMode$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.shadowColorMode;
    }

    public static final IntegerValue access$getBackgroundColorGreenValue$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.backgroundColorGreenValue;
    }

    public static final IntegerValue access$getShadowColorGreenValue$p(ScoreboardElement scoreboardElement) {
        return scoreboardElement.shadowColorGreenValue;
    }
}

