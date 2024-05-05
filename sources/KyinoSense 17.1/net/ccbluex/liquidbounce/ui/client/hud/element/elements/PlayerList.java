/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.AntiBot;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.PlayerList;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

@ElementInfo(name="PlayerList")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/PlayerList;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "alphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "bgalphaValue", "bgblueValue", "bggreenValue", "bgredValue", "blueValue", "cRainbowSecValue", "decimalFormat3", "Ljava/text/DecimalFormat;", "distanceValue", "fontOffsetValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "gradientAmountValue", "greenValue", "lineValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "rainbowList", "Lnet/ccbluex/liquidbounce/value/ListValue;", "redValue", "reverseValue", "shadowValue", "sortValue", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "KyinoClient"})
public final class PlayerList
extends Element {
    private final DecimalFormat decimalFormat3 = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
    private final ListValue sortValue = new ListValue("Sort", new String[]{"Alphabet", "Distance", "Health"}, "Alphabet");
    private final FloatValue fontOffsetValue = new FloatValue("Font-Offset", 0.0f, 3.0f, -3.0f);
    private final BoolValue reverseValue = new BoolValue("Reverse", false);
    private final FontValue fontValue;
    private final BoolValue shadowValue;
    private final BoolValue lineValue;
    private final IntegerValue redValue;
    private final IntegerValue greenValue;
    private final IntegerValue blueValue;
    private final IntegerValue alphaValue;
    private final IntegerValue bgredValue;
    private final IntegerValue bggreenValue;
    private final IntegerValue bgblueValue;
    private final IntegerValue bgalphaValue;
    private final ListValue rainbowList;
    private final IntegerValue cRainbowSecValue;
    private final IntegerValue distanceValue;
    private final IntegerValue gradientAmountValue;

    /*
     * WARNING - void declaration
     */
    @Override
    @NotNull
    public Border drawElement() {
        Iterator $this$filterTo$iv$iv;
        boolean reverse = (Boolean)this.reverseValue.get();
        FontRenderer font = (FontRenderer)this.fontValue.get();
        float fontOffset = ((Number)this.fontOffsetValue.get()).floatValue();
        String rainbowType = (String)this.rainbowList.get();
        float nameLength = font.func_78256_a("Name (0)");
        float hpLength = font.func_78256_a("Health");
        float distLength = font.func_78256_a("Distance");
        float height = 4.0f + (float)font.field_78288_b;
        int color = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()).getRGB();
        Color bgColor = new Color(((Number)this.bgredValue.get()).intValue(), ((Number)this.bggreenValue.get()).intValue(), ((Number)this.bgblueValue.get()).intValue(), ((Number)this.bgalphaValue.get()).intValue());
        List list = PlayerList.access$getMc$p$s1046033730().field_71441_e.field_73010_i;
        Intrinsics.checkExpressionValueIsNotNull(list, "mc.theWorld.playerEntities");
        Object $this$filter$iv = list;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        Iterator iterator2 = $this$filterTo$iv$iv.iterator();
        while (iterator2.hasNext()) {
            Object element$iv$iv = iterator2.next();
            EntityPlayer it = (EntityPlayer)element$iv$iv;
            boolean bl2 = false;
            if (!(!AntiBot.isBot((EntityLivingBase)it) && Intrinsics.areEqual(it, PlayerList.access$getMc$p$s1046033730().field_71439_g) ^ true)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List playerList = CollectionsKt.toMutableList((List)destination$iv$iv);
        nameLength = font.func_78256_a("Name (" + playerList.size() + ')');
        switch ((String)this.sortValue.get()) {
            case "Alphabet": {
                List list2 = playerList;
                $i$f$filter = false;
                Comparator comparator = new Comparator<T>(){

                    public final int compare(T a, T b) {
                        boolean bl = false;
                        EntityPlayer it = (EntityPlayer)a;
                        boolean bl2 = false;
                        String string = it.func_70005_c_();
                        Intrinsics.checkExpressionValueIsNotNull(string, "it.name");
                        String string2 = string;
                        boolean bl3 = false;
                        String string3 = string2;
                        if (string3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string4 = string3.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase()");
                        it = (EntityPlayer)b;
                        Comparable comparable = (Comparable)((Object)string4);
                        bl2 = false;
                        String string5 = it.func_70005_c_();
                        Intrinsics.checkExpressionValueIsNotNull(string5, "it.name");
                        string2 = string5;
                        bl3 = false;
                        String string6 = string2;
                        if (string6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string7 = string6.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string7, "(this as java.lang.String).toLowerCase()");
                        String string8 = string7;
                        return ComparisonsKt.compareValues(comparable, (Comparable)((Object)string8));
                    }
                };
                CollectionsKt.sortWith(list2, comparator);
                break;
            }
            case "Distance": {
                CollectionsKt.sortWith(playerList, drawElement.2.INSTANCE);
                break;
            }
            default: {
                CollectionsKt.sortWith(playerList, drawElement.3.INSTANCE);
            }
        }
        if (reverse) {
            playerList = CollectionsKt.toMutableList(CollectionsKt.reversed(playerList));
        }
        Iterable $this$forEach$iv = playerList;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            EntityPlayer it = (EntityPlayer)element$iv;
            boolean bl = false;
            if ((float)font.func_78256_a(it.func_70005_c_()) > nameLength) {
                nameLength = font.func_78256_a(it.func_70005_c_());
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((float)font.func_78256_a(stringBuilder.append(this.decimalFormat3.format(Float.valueOf(it.func_110143_aJ()))).append(" HP").toString()) > hpLength) {
                hpLength = font.func_78256_a(this.decimalFormat3.format(Float.valueOf(it.func_110143_aJ())) + " HP");
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            EntityPlayerSP entityPlayerSP = PlayerList.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!((float)font.func_78256_a(stringBuilder2.append(this.decimalFormat3.format(PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)it))).append('m').toString()) > distLength)) continue;
            StringBuilder stringBuilder3 = new StringBuilder();
            EntityPlayerSP entityPlayerSP2 = PlayerList.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            distLength = font.func_78256_a(stringBuilder3.append(this.decimalFormat3.format(PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP2, (Entity)it))).append('m').toString());
        }
        if (((Boolean)this.lineValue.get()).booleanValue()) {
            double barLength = nameLength + hpLength + distLength + 50.0f;
            HudColors hud = (HudColors)LiquidBounce.INSTANCE.getModuleManager().getModule(HudColors.class);
            int element$iv = 0;
            int it = ((Number)this.gradientAmountValue.get()).intValue() - 1;
            if (element$iv <= it) {
                while (true) {
                    void i;
                    double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                    double barEnd = (double)(i + true) / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                    if (hud != null) {
                        int n;
                        int n2;
                        switch (rainbowType) {
                            case "Rainbow": {
                                n2 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                                break;
                            }
                            case "Sky": {
                                n2 = RenderUtils.SkyRainbow((int)(i * ((Number)this.distanceValue.get()).intValue()), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                                break;
                            }
                            case "Fade": {
                                n2 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)(i * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                                break;
                            }
                            default: {
                                n2 = color;
                            }
                        }
                        switch (rainbowType) {
                            case "Rainbow": {
                                n = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue(), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()));
                                break;
                            }
                            case "Sky": {
                                n = RenderUtils.SkyRainbow((int)((i + true) * ((Number)this.distanceValue.get()).intValue()), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                                break;
                            }
                            case "Fade": {
                                n = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                                break;
                            }
                            default: {
                                n = color;
                            }
                        }
                        RenderUtils.drawGradientSideways(barStart, -1.0, barEnd, 0.0, n2, n);
                    }
                    if (i == it) break;
                    ++i;
                }
            }
        }
        RenderUtils.drawRect(0.0f, 0.0f, nameLength + hpLength + distLength + 50.0f, 4.0f + (float)font.field_78288_b, bgColor.getRGB());
        font.func_175065_a("Name (" + playerList.size() + ')', 5.0f, 3.0f, -1, ((Boolean)this.shadowValue.get()).booleanValue());
        font.func_175065_a("Distance", 5.0f + nameLength + 10.0f, 3.0f, -1, ((Boolean)this.shadowValue.get()).booleanValue());
        font.func_175065_a("Health", 5.0f + nameLength + distLength + 20.0f, 3.0f, -1, ((Boolean)this.shadowValue.get()).booleanValue());
        Iterable $this$forEachIndexed$iv = playerList;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            void player;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n3 = n;
            EntityPlayer bl2 = (EntityPlayer)item$iv;
            int index = n3;
            boolean bl3 = false;
            RenderUtils.drawRect(0.0f, height, nameLength + hpLength + distLength + 50.0f, height + 2.0f + (float)font.field_78288_b, bgColor.getRGB());
            font.func_175065_a(player.func_70005_c_(), 5.0f, height + 1.0f + fontOffset, -1, ((Boolean)this.shadowValue.get()).booleanValue());
            StringBuilder stringBuilder = new StringBuilder();
            EntityPlayerSP entityPlayerSP = PlayerList.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            font.func_175065_a(stringBuilder.append(this.decimalFormat3.format(PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP, (Entity)player))).append('m').toString(), 5.0f + nameLength + 10.0f, height + 1.0f + fontOffset, -1, ((Boolean)this.shadowValue.get()).booleanValue());
            font.func_175065_a(this.decimalFormat3.format(Float.valueOf(player.func_110143_aJ())) + " HP", 5.0f + nameLength + distLength + 20.0f, height + 1.0f + fontOffset, -1, ((Boolean)this.shadowValue.get()).booleanValue());
            height += 2.0f + (float)font.field_78288_b;
        }
        return new Border(0.0f, 0.0f, nameLength + hpLength + distLength + 50.0f, 4.0f + height + (float)font.field_78288_b);
    }

    public PlayerList() {
        super(0.0, 0.0, 0.0f, null, 15, null);
        FontRenderer fontRenderer = Fonts.minecraftFont;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "Fonts.minecraftFont");
        this.fontValue = new FontValue("Font", fontRenderer);
        this.shadowValue = new BoolValue("Shadow", false);
        this.lineValue = new BoolValue("Line", true);
        this.redValue = new IntegerValue("Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.bgredValue = new IntegerValue("Background-Red", 0, 0, 255);
        this.bggreenValue = new IntegerValue("Background-Green", 0, 0, 255);
        this.bgblueValue = new IntegerValue("Background-Blue", 0, 0, 255);
        this.bgalphaValue = new IntegerValue("Background-Alpha", 120, 0, 255);
        this.rainbowList = new ListValue("Rainbow", new String[]{"Off", "Rainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"}, "Off");
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.distanceValue = new IntegerValue("Line-Distance", 0, 0, 400);
        this.gradientAmountValue = new IntegerValue("Gradient-Amount", 25, 1, 50);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

