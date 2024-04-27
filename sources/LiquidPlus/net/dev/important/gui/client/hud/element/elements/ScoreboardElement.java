/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.scoreboard.Score
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.EnumChatFormatting
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.render.AntiBlind;
import net.dev.important.modules.module.modules.render.HUD;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.ServerUtils;
import net.dev.important.utils.misc.StringUtils;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.ColorManager;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@ElementInfo(name="Scoreboard")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000|\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u00102\u001a\u000203H\u0002J\n\u00104\u001a\u0004\u0018\u000105H\u0016J\b\u00106\u001a\u000207H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u0012\u0012\u0004\u0012\u00020\u001b0\u001aj\b\u0012\u0004\u0012\u00020\u001b`\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001b0 X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010!R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00068"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/ScoreboardElement;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/dev/important/gui/client/hud/element/Side;", "(DDFLnet/dev/important/gui/client/hud/element/Side;)V", "antiSnipeMatch", "Lnet/dev/important/value/BoolValue;", "astolfoRainbowIndex", "Lnet/dev/important/value/IntegerValue;", "astolfoRainbowOffset", "backgroundColorAlphaValue", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorRedValue", "backgroundShadow", "blurStrength", "Lnet/dev/important/value/FloatValue;", "blurValue", "brightnessValue", "cRainbowSecValue", "cachedDomains", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "changeDomain", "delayValue", "domainList", "", "[Ljava/lang/String;", "fontValue", "Lnet/dev/important/value/FontValue;", "garbageTimer", "Lnet/dev/important/utils/timer/MSTimer;", "hypickleRegex", "Lkotlin/text/Regex;", "rectColorBlueAlpha", "rectColorBlueValue", "rectColorGreenValue", "rectColorModeValue", "Lnet/dev/important/value/ListValue;", "rectColorRedValue", "rectValue", "saturationValue", "shadowValue", "showRedNumbersValue", "backgroundColor", "Ljava/awt/Color;", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "updateElement", "", "LiquidBounce"})
public final class ScoreboardElement
extends Element {
    @NotNull
    private final IntegerValue backgroundColorRedValue;
    @NotNull
    private final IntegerValue backgroundColorGreenValue;
    @NotNull
    private final IntegerValue backgroundColorBlueValue;
    @NotNull
    private final IntegerValue backgroundColorAlphaValue;
    @NotNull
    private final IntegerValue astolfoRainbowOffset;
    @NotNull
    private final IntegerValue astolfoRainbowIndex;
    @NotNull
    private final BoolValue rectValue;
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final ListValue rectColorModeValue;
    @NotNull
    private final IntegerValue rectColorRedValue;
    @NotNull
    private final IntegerValue rectColorGreenValue;
    @NotNull
    private final IntegerValue rectColorBlueValue;
    @NotNull
    private final IntegerValue rectColorBlueAlpha;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue cRainbowSecValue;
    @NotNull
    private final IntegerValue delayValue;
    @NotNull
    private final BoolValue shadowValue;
    @NotNull
    private final BoolValue antiSnipeMatch;
    @NotNull
    private final BoolValue changeDomain;
    @NotNull
    private final BoolValue showRedNumbersValue;
    @NotNull
    private final FontValue fontValue;
    @NotNull
    private final BoolValue backgroundShadow;
    @NotNull
    private final String[] domainList;
    @NotNull
    private final ArrayList<String> cachedDomains;
    @NotNull
    private final MSTimer garbageTimer;
    @NotNull
    private final Regex hypickleRegex;

    public ScoreboardElement(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "side");
        super(x, y, scale, side);
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 95, 0, 255);
        this.astolfoRainbowOffset = new IntegerValue("AstolfoOffset", 5, 1, 20);
        this.astolfoRainbowIndex = new IntegerValue("AstolfoIndex", 109, 1, 300);
        this.rectValue = new BoolValue("Rect", false);
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        String[] stringArray = new String[]{"Custom", "Rainbow", "LiquidSlowly", "Fade", "Sky", "Mixer", "Astolfo"};
        this.rectColorModeValue = new ListValue("Color", stringArray, "Custom");
        this.rectColorRedValue = new IntegerValue("Red", 0, 0, 255);
        this.rectColorGreenValue = new IntegerValue("Green", 111, 0, 255);
        this.rectColorBlueValue = new IntegerValue("Blue", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Alpha", 255, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.delayValue = new IntegerValue("Delay", 50, 0, 200);
        this.shadowValue = new BoolValue("Shadow", false);
        this.antiSnipeMatch = new BoolValue("AntiSnipeMatch", true);
        this.changeDomain = new BoolValue("ChangeDomain", false);
        this.showRedNumbersValue = new BoolValue("ShowRedNumbers", false);
        stringArray = Fonts.minecraftFont;
        Intrinsics.checkNotNullExpressionValue(stringArray, "minecraftFont");
        this.fontValue = new FontValue("Font", (FontRenderer)stringArray);
        this.backgroundShadow = new BoolValue("BackgroundShadow", true);
        stringArray = new String[]{".ac", ".academy", ".accountant", ".accountants", ".actor", ".adult", ".ag", ".agency", ".ai", ".airforce", ".am", ".amsterdam", ".apartments", ".app", ".archi", ".army", ".art", ".asia", ".associates", ".at", ".attorney", ".au", ".auction", ".auto", ".autos", ".baby", ".band", ".bar", ".barcelona", ".bargains", ".bayern", ".be", ".beauty", ".beer", ".berlin", ".best", ".bet", ".bid", ".bike", ".bingo", ".bio", ".biz", ".biz.pl", ".black", ".blog", ".blue", ".boats", ".boston", ".boutique", ".build", ".builders", ".business", ".buzz", ".bz", ".ca", ".cab", ".cafe", ".camera", ".camp", ".capital", ".car", ".cards", ".care", ".careers", ".cars", ".casa", ".cash", ".casino", ".catering", ".cc", ".center", ".ceo", ".ch", ".charity", ".chat", ".cheap", ".church", ".city", ".cl", ".claims", ".cleaning", ".clinic", ".clothing", ".cloud", ".club", ".cn", ".co", ".co.in", ".co.jp", ".co.kr", ".co.nz", ".co.uk", ".co.za", ".coach", ".codes", ".coffee", ".college", ".com", ".com.ag", ".com.au", ".com.br", ".com.bz", ".com.cn", ".com.co", ".com.es", ".com.mx", ".com.pe", ".com.ph", ".com.pl", ".com.ru", ".com.tw", ".community", ".company", ".computer", ".condos", ".construction", ".consulting", ".contact", ".contractors", ".cooking", ".cool", ".country", ".coupons", ".courses", ".credit", ".creditcard", ".cricket", ".cruises", ".cymru", ".cz", ".dance", ".date", ".dating", ".de", ".deals", ".degree", ".delivery", ".democrat", ".dental", ".dentist", ".design", ".dev", ".diamonds", ".digital", ".direct", ".directory", ".discount", ".dk", ".doctor", ".dog", ".domains", ".download", ".earth", ".education", ".email", ".energy", ".engineer", ".engineering", ".enterprises", ".equipment", ".es", ".estate", ".eu", ".events", ".exchange", ".expert", ".exposed", ".express", ".fail", ".faith", ".family", ".fan", ".fans", ".farm", ".fashion", ".film", ".finance", ".financial", ".firm.in", ".fish", ".fishing", ".fit", ".fitness", ".flights", ".florist", ".fm", ".football", ".forsale", ".foundation", ".fr", ".fun", ".fund", ".furniture", ".futbol", ".fyi", ".gallery", ".games", ".garden", ".gay", ".gen.in", ".gg", ".gifts", ".gives", ".glass", ".global", ".gmbh", ".gold", ".golf", ".graphics", ".gratis", ".green", ".gripe", ".group", ".gs", ".guide", ".guru", ".hair", ".haus", ".health", ".healthcare", ".hockey", ".holdings", ".holiday", ".homes", ".horse", ".hospital", ".host", ".house", ".idv.tw", ".immo", ".immobilien", ".in", ".inc", ".ind.in", ".industries", ".info", ".info.pl", ".ink", ".institute", ".insure", ".international", ".investments", ".io", ".irish", ".ist", ".istanbul", ".it", ".jetzt", ".jewelry", ".jobs", ".jp", ".kaufen", ".kim", ".kitchen", ".kiwi", ".kr", ".la", ".land", ".law", ".lawyer", ".lease", ".legal", ".lgbt", ".life", ".lighting", ".limited", ".limo", ".live", ".llc", ".loan", ".loans", ".london", ".love", ".ltd", ".ltda", ".luxury", ".maison", ".makeup", ".management", ".market", ".marketing", ".mba", ".me", ".me.uk", ".media", ".melbourne", ".memorial", ".men", ".menu", ".miami", ".mobi", ".moda", ".moe", ".money", ".monster", ".mortgage", ".motorcycles", ".movie", ".ms", ".mx", ".nagoya", ".name", ".navy", ".ne", ".ne.kr", ".net", ".net.ag", ".net.au", ".net.br", ".net.bz", ".net.cn", ".net.co", ".net.in", ".net.nz", ".net.pe", ".net.ph", ".net.pl", ".net.ru", ".network", ".news", ".ninja", ".nl", ".no", ".nom.co", ".nom.es", ".nom.pe", ".nrw", ".nyc", ".okinawa", ".one", ".onl", ".online", ".org", ".org.ag", ".org.au", ".org.cn", ".org.es", ".org.in", ".org.nz", ".org.pe", ".org.ph", ".org.pl", ".org.ru", ".org.uk", ".page", ".paris", ".partners", ".parts", ".party", ".pe", ".pet", ".ph", ".photography", ".photos", ".pictures", ".pink", ".pizza", ".pl", ".place", ".plumbing", ".plus", ".poker", ".porn", ".press", ".pro", ".productions", ".promo", ".properties", ".protection", ".pub", ".pw", ".quebec", ".quest", ".racing", ".re.kr", ".realestate", ".recipes", ".red", ".rehab", ".reise", ".reisen", ".rent", ".rentals", ".repair", ".report", ".republican", ".rest", ".restaurant", ".review", ".reviews", ".rich", ".rip", ".rocks", ".rodeo", ".ru", ".run", ".ryukyu", ".sale", ".salon", ".sarl", ".school", ".schule", ".science", ".se", ".security", ".services", ".sex", ".sg", ".sh", ".shiksha", ".shoes", ".shop", ".shopping", ".show", ".singles", ".site", ".ski", ".skin", ".soccer", ".social", ".software", ".solar", ".solutions", ".space", ".storage", ".store", ".stream", ".studio", ".study", ".style", ".supplies", ".supply", ".support", ".surf", ".surgery", ".sydney", ".systems", ".tax", ".taxi", ".team", ".tech", ".technology", ".tel", ".tennis", ".theater", ".theatre", ".tienda", ".tips", ".tires", ".today", ".tokyo", ".tools", ".tours", ".town", ".toys", ".top", ".trade", ".training", ".travel", ".tube", ".tv", ".tw", ".uk", ".university", ".uno", ".us", ".vacations", ".vegas", ".ventures", ".vet", ".viajes", ".video", ".villas", ".vin", ".vip", ".vision", ".vodka", ".vote", ".voto", ".voyage", ".wales", ".watch", ".webcam", ".website", ".wedding", ".wiki", ".win", ".wine", ".work", ".works", ".world", ".ws", ".wtf", ".xxx", ".xyz", ".yachts", ".yoga", ".yokohama", ".zone", ".vn"};
        this.domainList = stringArray;
        this.cachedDomains = new ArrayList();
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

    @Override
    public void updateElement() {
        if (this.garbageTimer.hasTimePassed(30000L)) {
            this.cachedDomains.clear();
            this.garbageTimer.reset();
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        Integer LiquidSlowly;
        ScoreObjective scoreObjective;
        int colorIndex;
        Module module2 = Client.INSTANCE.getModuleManager().getModule(AntiBlind.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.render.AntiBlind");
        }
        AntiBlind antiBlind = (AntiBlind)module2;
        if (antiBlind.getState() && ((Boolean)antiBlind.getScoreBoard().get()).booleanValue()) {
            return null;
        }
        FontRenderer fontRenderer = (FontRenderer)this.fontValue.get();
        int backColor = this.backgroundColor().getRGB();
        String rectColorMode = (String)this.rectColorModeValue.get();
        int rectCustomColor = new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()).getRGB();
        Scoreboard scoreboard = MinecraftInstance.mc.field_71441_e.func_96441_U();
        Intrinsics.checkNotNullExpressionValue(scoreboard, "mc.theWorld.scoreboard");
        Scoreboard worldScoreboard = scoreboard;
        ScoreObjective currObjective = null;
        ScorePlayerTeam playerTeam = worldScoreboard.func_96509_i(MinecraftInstance.mc.field_71439_g.func_70005_c_());
        if (playerTeam != null && (colorIndex = playerTeam.func_178775_l().func_175746_b()) >= 0) {
            currObjective = worldScoreboard.func_96539_a(3 + colorIndex);
        }
        if ((scoreObjective = currObjective) == null) {
            scoreObjective = worldScoreboard.func_96539_a(1);
        }
        if (scoreObjective == null) {
            return null;
        }
        ScoreObjective objective = scoreObjective;
        Scoreboard scoreboard2 = objective.func_96682_a();
        Intrinsics.checkNotNullExpressionValue(scoreboard2, "objective.scoreboard");
        Scoreboard scoreboard3 = scoreboard2;
        Collection scoreCollection = null;
        scoreCollection = scoreboard3.func_96534_i(objective);
        ArrayList scores = Lists.newArrayList((Iterable)Iterables.filter((Iterable)scoreCollection, ScoreboardElement::drawElement$lambda-0));
        scoreCollection = scores.size() > 15 ? (Collection)Lists.newArrayList((Iterable)Iterables.skip((Iterable)scores, (int)(scoreCollection.size() - 15))) : (Collection)scores;
        int maxWidth = 0;
        maxWidth = fontRenderer.func_78256_a(objective.func_96678_d());
        Module module3 = Client.INSTANCE.getModuleManager().getModule(HUD.class);
        if (module3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.render.HUD");
        }
        HUD hud = (HUD)module3;
        for (Score score : scoreCollection) {
            ScorePlayerTeam scorePlayerTeam = scoreboard3.func_96509_i(score.func_96653_e());
            String name = ScorePlayerTeam.func_96667_a((Team)((Team)scorePlayerTeam), (String)score.func_96653_e());
            String string = ColorUtils.stripColor(name);
            Intrinsics.checkNotNull(string);
            String stripped = StringUtils.fixString(string);
            if (((Boolean)this.changeDomain.get()).booleanValue()) {
                if (this.cachedDomains.contains(stripped)) {
                    name = "LiquidPlus.web";
                } else if (ServerUtils.isHypixelDomain(stripped)) {
                    name = "LiquidPlus.web";
                    this.cachedDomains.add(stripped);
                } else {
                    for (String string2 : this.domainList) {
                        String string3 = stripped;
                        Intrinsics.checkNotNullExpressionValue(string3, "stripped");
                        if (!StringsKt.contains((CharSequence)string3, string2, true)) continue;
                        name = "LiquidPlus.web";
                        this.cachedDomains.add(stripped);
                        break;
                    }
                }
            }
            String width = name + ": " + EnumChatFormatting.RED + score.func_96652_c();
            maxWidth = RangesKt.coerceAtLeast(maxWidth, fontRenderer.func_78256_a(width));
        }
        int maxHeight = scoreCollection.size() * fontRenderer.field_78288_b;
        int l1 = this.getSide().getHorizontal() == Side.Horizontal.LEFT ? maxWidth + 3 : -maxWidth - 3;
        int FadeColor = ColorUtils.fade(new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()), 0, 100).getRGB();
        Color color = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
        Integer n = LiquidSlowly = color == null ? null : Integer.valueOf(color.getRGB());
        Intrinsics.checkNotNull(n);
        int liquidSlowli = n;
        int[] nArray = new int[]{0};
        int[] counter = nArray;
        int Astolfo = 0;
        Astolfo = ColorManager.astolfoRainbow(counter[0] * 100, ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue());
        int mixerColor = ColorMixer.getMixedColor(0, ((Number)this.cRainbowSecValue.get()).intValue()).getRGB();
        if (((Boolean)this.blurValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                BlurUtils.blurArea((float)this.getRenderX() + ((float)l1 + 2.0f) * this.getScale(), (float)this.getRenderY() + -2.0f * this.getScale(), (float)this.getRenderX() + -5.0f * this.getScale(), (float)this.getRenderY() + (float)(maxHeight + fontRenderer.field_78288_b) * this.getScale(), ((Number)this.blurStrength.get()).floatValue());
            } else {
                BlurUtils.blurArea((float)this.getRenderX() + ((float)l1 - 2.0f) * this.getScale(), (float)this.getRenderY() + -2.0f * this.getScale(), (float)this.getRenderX() + 5.0f * this.getScale(), (float)this.getRenderY() + (float)(maxHeight + fontRenderer.field_78288_b) * this.getScale(), ((Number)this.blurStrength.get()).floatValue());
            }
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
            GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
            GL11.glPopMatrix();
        }
        if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
            if (((Boolean)this.backgroundShadow.get()).booleanValue()) {
                RenderUtils.drawShadow(l1 + 2, -2, 5 - l1 - 2, maxHeight + fontRenderer.field_78288_b + 2);
                RenderUtils.drawShadow(l1 + 2, -2, 5 - l1 + 2, maxHeight + fontRenderer.field_78288_b + 2);
            }
            Gui.func_73734_a((int)(l1 + 2), (int)-2, (int)-5, (int)(maxHeight + fontRenderer.field_78288_b), (int)backColor);
        } else {
            if (((Boolean)this.backgroundShadow.get()).booleanValue()) {
                RenderUtils.drawShadow(l1 - 2, -2, 5 - l1 + 2, maxHeight + fontRenderer.field_78288_b + 2);
                RenderUtils.drawShadow(l1 - 2, -2, 5 - l1 + 2, maxHeight + fontRenderer.field_78288_b + 2);
            }
            Gui.func_73734_a((int)(l1 - 2), (int)-2, (int)5, (int)(maxHeight + fontRenderer.field_78288_b), (int)backColor);
        }
        if (((Boolean)this.rectValue.get()).booleanValue()) {
            Collection collection = scoreCollection;
            Intrinsics.checkNotNullExpressionValue(collection, "scoreCollection");
            if (!collection.isEmpty()) {
                int n2;
                int n3 = StringsKt.equals(rectColorMode, "Sky", true) ? RenderUtils.SkyRainbow(0, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue()) : (StringsKt.equals(rectColorMode, "Rainbow", true) ? RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), 0) : (StringsKt.equals(rectColorMode, "LiquidSlowly", true) ? liquidSlowli : (StringsKt.equals(rectColorMode, "Fade", true) ? FadeColor : (StringsKt.equals(rectColorMode, "Mixer", true) ? mixerColor : (n2 = StringsKt.equals(rectColorMode, "Astolfo", true) ? Astolfo : rectCustomColor)))));
                if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    Gui.func_73734_a((int)(l1 + 2), (int)-2, (int)-5, (int)-3, (int)n2);
                } else {
                    Gui.func_73734_a((int)(l1 - 2), (int)-2, (int)5, (int)-3, (int)n2);
                }
            }
        }
        Collection collection = scoreCollection;
        Intrinsics.checkNotNullExpressionValue(collection, "scoreCollection");
        Iterable iterable = collection;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : iterable) {
            void score;
            int n4 = index$iv;
            index$iv = n4 + 1;
            if (n4 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Score score2 = (Score)item$iv;
            int index = n4;
            boolean bl = false;
            ScorePlayerTeam team = scoreboard3.func_96509_i(score.func_96653_e());
            String name = ScorePlayerTeam.func_96667_a((Team)((Team)team), (String)score.func_96653_e());
            String scorePoints = "" + EnumChatFormatting.RED + score.func_96652_c();
            int width = 5;
            int height = maxHeight - index * fontRenderer.field_78288_b;
            boolean changed = false;
            String string = ColorUtils.stripColor(name);
            Intrinsics.checkNotNull(string);
            String stripped = StringUtils.fixString(string);
            GlStateManager.func_179117_G();
            if (((Boolean)this.changeDomain.get()).booleanValue() && this.cachedDomains.contains(stripped)) {
                name = "LiquidPlus.web";
                changed = true;
            }
            if (((Boolean)this.antiSnipeMatch.get()).booleanValue()) {
                String string4 = stripped;
                Intrinsics.checkNotNullExpressionValue(string4, "stripped");
                if (this.hypickleRegex.containsMatchIn(string4)) {
                    name = "";
                }
            }
            if (changed) {
                String stringZ = "";
                int n5 = 0;
                int n6 = name.length() - 1;
                if (n5 <= n6) {
                    int z;
                    do {
                        int typeColor;
                        int n7;
                        z = n5++;
                        if (StringsKt.equals(rectColorMode, "Sky", true)) {
                            n7 = RenderUtils.SkyRainbow(z * ((Number)this.delayValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        } else if (StringsKt.equals(rectColorMode, "Astolfo", true)) {
                            n7 = Astolfo;
                        } else if (StringsKt.equals(rectColorMode, "Rainbow", true)) {
                            n7 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), z * ((Number)this.delayValue.get()).intValue());
                        } else if (StringsKt.equals(rectColorMode, "LiquidSlowly", true)) {
                            Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), z * ((Number)this.delayValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                            Intrinsics.checkNotNull(color2);
                            n7 = color2.getRGB();
                        } else {
                            n7 = StringsKt.equals(rectColorMode, "Fade", true) ? ColorUtils.fade(new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()), z * ((Number)this.delayValue.get()).intValue(), 100).getRGB() : (typeColor = StringsKt.equals(rectColorMode, "Mixer", true) ? ColorMixer.getMixedColor(z * ((Number)this.delayValue.get()).intValue(), ((Number)this.cRainbowSecValue.get()).intValue()).getRGB() : rectCustomColor);
                        }
                        if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                            fontRenderer.func_175065_a(String.valueOf(name.charAt(z)), -3.0f + (float)fontRenderer.func_78256_a(stringZ), (float)height, typeColor, ((Boolean)this.shadowValue.get()).booleanValue());
                        } else {
                            fontRenderer.func_175065_a(String.valueOf(name.charAt(z)), (float)l1 + (float)fontRenderer.func_78256_a(stringZ), (float)height, typeColor, ((Boolean)this.shadowValue.get()).booleanValue());
                        }
                        stringZ = Intrinsics.stringPlus(stringZ, Character.valueOf(name.charAt(z)));
                    } while (z != n6);
                }
            } else if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                fontRenderer.func_175065_a(name, -3.0f, (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
            } else {
                fontRenderer.func_175065_a(name, (float)l1, (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
            }
            if (((Boolean)this.showRedNumbersValue.get()).booleanValue()) {
                if (this.getSide().getHorizontal() == Side.Horizontal.LEFT) {
                    fontRenderer.func_175065_a(scorePoints, (float)(l1 + 1 - fontRenderer.func_78256_a(scorePoints)), (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
                } else {
                    fontRenderer.func_175065_a(scorePoints, (float)(width - fontRenderer.func_78256_a(scorePoints)), (float)height, -1, ((Boolean)this.shadowValue.get()).booleanValue());
                }
            }
            if (index != scoreCollection.size() - 1) continue;
            String displayName = objective.func_96678_d();
            GlStateManager.func_179117_G();
            fontRenderer.func_175065_a(displayName, this.getSide().getHorizontal() == Side.Horizontal.LEFT ? (float)(maxWidth / 2 - fontRenderer.func_78256_a(displayName) / 2) : (float)(l1 + maxWidth / 2 - fontRenderer.func_78256_a(displayName) / 2), (float)(height - fontRenderer.field_78288_b), -1, ((Boolean)this.shadowValue.get()).booleanValue());
        }
        return this.getSide().getHorizontal() == Side.Horizontal.LEFT ? new Border((float)maxWidth + (float)5, -2.0f, -5.0f, (float)maxHeight + (float)fontRenderer.field_78288_b) : new Border(-((float)maxWidth) - (float)5, -2.0f, 5.0f, (float)maxHeight + (float)fontRenderer.field_78288_b);
    }

    private final Color backgroundColor() {
        return new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static final boolean drawElement$lambda-0(Score input) {
        Score score = input;
        if (score == null) {
            return false;
        }
        String string = score.func_96653_e();
        if (string == null) return false;
        String string2 = input.func_96653_e();
        Intrinsics.checkNotNullExpressionValue(string2, "input.playerName");
        if (StringsKt.startsWith$default(string2, "#", false, 2, null)) return false;
        return true;
    }

    public ScoreboardElement() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }
}

