/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import liying.utils.ShadowUtils;
import liying.utils.blur.ArrayBlurUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.CustomColor;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Arraylist$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Arraylist", single=true)
public final class Arraylist
extends Element {
    private List modules;
    private final IntegerValue textBlue;
    private final IntegerValue colorGreenValue;
    private final ListValue colorModeValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue liquidSlowlyDistanceValue;
    private final FloatValue saturationValue;
    private final FloatValue animationSpeed;
    private final BoolValue shadow;
    private final FloatValue textHeightValue;
    private final IntegerValue colorRedValue2;
    private float y2;
    private final IntegerValue colorAlphaValue;
    private final IntegerValue cRainbowDistValue;
    private final ListValue rectLeftValue;
    private int x2;
    private final FloatValue brightnessValue;
    private final BoolValue abcOrder;
    private final IntegerValue backgroundColorRedValue;
    private final ListValue shadowColorMode;
    private final IntegerValue backgroundColorAlphaValue;
    private List sortedModules;
    private final BoolValue tagsArrayColor;
    private final IntegerValue textBlue2;
    private final FloatValue spaceValue;
    private final BoolValue nameBreak;
    private IFontRenderer fontRenderer;
    private final FloatValue fadeOffset;
    private final IntegerValue backgroundColorGreenValue;
    private final ListValue caseValue;
    private final BoolValue shadowNoCutValue;
    private final IntegerValue cRainbowSecValue;
    private final FloatValue blurStrength;
    private final IntegerValue gidentspeed;
    private final IntegerValue textRed2;
    private final IntegerValue shadowColorBlueValue;
    private final ListValue tagsStyleValue;
    private final ListValue rectRightValue;
    private final IntegerValue colorBlueValue;
    private final BoolValue tags;
    private final BoolValue shadowShaderValue;
    private final FloatValue textYValue;
    private final IntegerValue fadeDistanceValue;
    private final IntegerValue shadowColorGreenValue;
    private final IntegerValue shadowStrength;
    private final BoolValue blurValue;
    private final FontValue fontValue;
    private final IntegerValue textGreen;
    private final IntegerValue skyDistanceValue;
    private final ListValue hAnimation;
    private final IntegerValue textGreen2;
    private final IntegerValue colorGreenValue2;
    private final IntegerValue backgroundWithValue;
    private final IntegerValue textRed;
    private final IntegerValue colorBlueValue2;
    private final IntegerValue colorRedValue;
    private final IntegerValue shadowColorRedValue;
    private final ListValue vAnimation;

    public static final IntegerValue access$getSkyDistanceValue$p(Arraylist arraylist) {
        return arraylist.skyDistanceValue;
    }

    public static final ListValue access$getShadowColorMode$p(Arraylist arraylist) {
        return arraylist.shadowColorMode;
    }

    public Arraylist(double d, double d2, float f, Side side) {
        super(d, d2, f, side);
        this.colorModeValue = new ListValue("Color", new String[]{"Custom", "Random", "Sky", "CRainbow", "LiquidSlowly", "Fade", "DoubleColor", "CustomHUD", "Gradinet"}, "DoubleColor");
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        this.shadow = new BoolValue("ShadowText", true);
        this.shadowShaderValue = new BoolValue("Shadow", false);
        this.shadowNoCutValue = new BoolValue("Shadow-NoCut", false);
        this.shadowStrength = new IntegerValue("Shadow-Strength", 1, 1, 30);
        this.shadowColorMode = new ListValue("Shadow-Color", new String[]{"Background", "Text", "Custom", "Round", "Gradient"}, "Background");
        this.shadowColorRedValue = new IntegerValue("Shadow-Red", 0, 0, 255);
        this.shadowColorGreenValue = new IntegerValue("Shadow-Green", 111, 0, 255);
        this.shadowColorBlueValue = new IntegerValue("Shadow-Blue", 255, 0, 255);
        this.colorRedValue = new IntegerValue("Red", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("Green", 111, 0, 255);
        this.colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
        this.colorRedValue2 = new IntegerValue("R2", 0, 0, 255);
        this.colorGreenValue2 = new IntegerValue("G2", 111, 0, 255);
        this.colorBlueValue2 = new IntegerValue("B2", 255, 0, 255);
        this.gidentspeed = new IntegerValue("GidentSpeed", 100, 1, 1000);
        this.colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.fadeOffset = new FloatValue("Gradinet-Offset", 0.2f, 0.1f, 1.0f);
        this.textRed = new IntegerValue("Gradinet-Red", 0, 0, 255);
        this.textGreen = new IntegerValue("Gradinet-Green", 0, 0, 255);
        this.textBlue = new IntegerValue("Gradinet-Blue", 255, 0, 255);
        this.textRed2 = new IntegerValue("Gradinet-Red-2", 25, 0, 255);
        this.textGreen2 = new IntegerValue("Gradinet-Green-2", 45, 0, 255);
        this.textBlue2 = new IntegerValue("Gradinet-Blue-2", 150, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.skyDistanceValue = new IntegerValue("Sky-Distance", 2, 0, 4);
        this.cRainbowSecValue = new IntegerValue("CRainbow-Seconds", 2, 1, 10);
        this.cRainbowDistValue = new IntegerValue("CRainbow-Distance", 2, 1, 6);
        this.liquidSlowlyDistanceValue = new IntegerValue("LiquidSlowly-Distance", 90, 1, 90);
        this.fadeDistanceValue = new IntegerValue("Fade-Distance", 50, 1, 100);
        this.hAnimation = new ListValue("HorizontalAnimation", new String[]{"Default", "None", "Slide", "Astolfo"}, "None");
        this.vAnimation = new ListValue("VerticalAnimation", new String[]{"None", "LiquidSense", "Slide", "Rise", "Astolfo"}, "None");
        this.animationSpeed = new FloatValue("Animation-Speed", 0.25f, 0.01f, 1.0f);
        this.nameBreak = new BoolValue("NameBreak", true);
        this.abcOrder = new BoolValue("Alphabetical-Order", false);
        this.tags = new BoolValue("Tags", true);
        this.tagsStyleValue = new ListValue("TagsStyle", new String[]{"-", "|", "()", "[]", "<>", "Default"}, "-");
        this.backgroundWithValue = new IntegerValue("Background-Width", 2, 0, 12);
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 0, 0, 255);
        this.rectRightValue = new ListValue("Rect-Right", new String[]{"None", "Left", "Right", "Outline", "Special", "Top"}, "None");
        this.rectLeftValue = new ListValue("Rect-Left", new String[]{"None", "Left", "Right"}, "None");
        this.caseValue = new ListValue("Case", new String[]{"None", "Lower", "Upper"}, "None");
        this.spaceValue = new FloatValue("Space", 0.0f, 0.0f, 5.0f);
        this.textHeightValue = new FloatValue("TextHeight", 11.0f, 1.0f, 20.0f);
        this.textYValue = new FloatValue("TextY", 1.0f, 0.0f, 20.0f);
        this.tagsArrayColor = new BoolValue("TagsArrayColor", false);
        this.fontValue = new FontValue("Font", Fonts.font40);
        this.modules = CollectionsKt.emptyList();
        this.sortedModules = CollectionsKt.emptyList();
        this.fontRenderer = Fonts.tenacitybold40;
    }

    public static final IntegerValue access$getShadowColorGreenValue$p(Arraylist arraylist) {
        return arraylist.shadowColorGreenValue;
    }

    public static final IntegerValue access$getColorRedValue2$p(Arraylist arraylist) {
        return arraylist.colorRedValue2;
    }

    public static final IntegerValue access$getBackgroundColorAlphaValue$p(Arraylist arraylist) {
        return arraylist.backgroundColorAlphaValue;
    }

    public static final IntegerValue access$getBackgroundColorGreenValue$p(Arraylist arraylist) {
        return arraylist.backgroundColorGreenValue;
    }

    public static final IntegerValue access$getTextBlue2$p(Arraylist arraylist) {
        return arraylist.textBlue2;
    }

    public static final IntegerValue access$getTextBlue$p(Arraylist arraylist) {
        return arraylist.textBlue;
    }

    public static final IntegerValue access$getCRainbowSecValue$p(Arraylist arraylist) {
        return arraylist.cRainbowSecValue;
    }

    public static final IntegerValue access$getTextGreen2$p(Arraylist arraylist) {
        return arraylist.textGreen2;
    }

    public static final IntegerValue access$getGidentspeed$p(Arraylist arraylist) {
        return arraylist.gidentspeed;
    }

    public static final IntegerValue access$getFadeDistanceValue$p(Arraylist arraylist) {
        return arraylist.fadeDistanceValue;
    }

    public final IntegerValue getColorAlphaValue() {
        return this.colorAlphaValue;
    }

    public static final IntegerValue access$getTextRed$p(Arraylist arraylist) {
        return arraylist.textRed;
    }

    public static final IntegerValue access$getTextGreen$p(Arraylist arraylist) {
        return arraylist.textGreen;
    }

    public static final IntegerValue access$getBackgroundColorRedValue$p(Arraylist arraylist) {
        return arraylist.backgroundColorRedValue;
    }

    @Override
    public void updateElement() {
        List list;
        List list2;
        Arraylist arraylist;
        List list3;
        Iterable iterable;
        boolean bl;
        Arraylist arraylist2;
        Iterable iterable2;
        Arraylist arraylist3 = this;
        if (((Boolean)this.abcOrder.get()).booleanValue()) {
            iterable2 = LiquidBounce.INSTANCE.getModuleManager().getModules();
            arraylist2 = arraylist3;
            bl = false;
            iterable = iterable2;
            Collection collection = new ArrayList();
            boolean bl2 = false;
            for (Object t : iterable) {
                Module module = (Module)t;
                boolean bl3 = false;
                if (!(module.getArray() && (StringsKt.equals((String)((String)this.hAnimation.get()), (String)"none", (boolean)true) ? module.getState() : module.getSlide() > 0.0f))) continue;
                collection.add(t);
            }
            list3 = (List)collection;
            arraylist = arraylist2;
            list2 = list3;
        } else {
            iterable2 = LiquidBounce.INSTANCE.getModuleManager().getModules();
            arraylist2 = arraylist3;
            bl = false;
            iterable = iterable2;
            Collection collection = new ArrayList();
            boolean bl4 = false;
            for (Object t : iterable) {
                Module module = (Module)t;
                boolean bl5 = false;
                if (!(module.getArray() && (StringsKt.equals((String)((String)this.hAnimation.get()), (String)"none", (boolean)true) ? module.getState() : module.getSlide() > 0.0f))) continue;
                collection.add(t);
            }
            list3 = (List)collection;
            iterable2 = list3;
            bl = false;
            iterable = iterable2;
            boolean bl6 = false;
            Comparator comparator = new Comparator(this){
                final Arraylist this$0;

                public final int compare(Object object, Object object2) {
                    boolean bl = false;
                    Module module = (Module)object;
                    boolean bl2 = false;
                    Comparable comparable = Integer.valueOf(-((IFontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).getStringWidth(this.this$0.getModName(module)));
                    module = (Module)object2;
                    Comparable comparable2 = comparable;
                    bl2 = false;
                    Integer n = -((IFontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).getStringWidth(this.this$0.getModName(module));
                    return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
                }
                {
                    this.this$0 = arraylist;
                }

                static {
                }
            };
            list3 = CollectionsKt.sortedWith((Iterable)iterable, (Comparator)comparator);
            arraylist = arraylist2;
            list2 = list3;
        }
        arraylist.modules = list2;
        Arraylist arraylist4 = this;
        if (((Boolean)this.abcOrder.get()).booleanValue()) {
            list = CollectionsKt.toList((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules());
        } else {
            iterable2 = LiquidBounce.INSTANCE.getModuleManager().getModules();
            arraylist2 = arraylist4;
            bl = false;
            iterable = iterable2;
            boolean bl7 = false;
            Comparator comparator = new Comparator(this){
                final Arraylist this$0;

                public final int compare(Object object, Object object2) {
                    boolean bl = false;
                    Module module = (Module)object;
                    boolean bl2 = false;
                    Comparable comparable = Integer.valueOf(-((IFontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).getStringWidth(this.this$0.getModName(module)));
                    module = (Module)object2;
                    Comparable comparable2 = comparable;
                    bl2 = false;
                    Integer n = -((IFontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).getStringWidth(this.this$0.getModName(module));
                    return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
                }

                static {
                }
                {
                    this.this$0 = arraylist;
                }
            };
            list3 = CollectionsKt.sortedWith((Iterable)iterable, (Comparator)comparator);
            arraylist4 = arraylist2;
            list = CollectionsKt.toList((Iterable)list3);
        }
        arraylist4.sortedModules = list;
    }

    public static final IntegerValue access$getColorGreenValue2$p(Arraylist arraylist) {
        return arraylist.colorGreenValue2;
    }

    public static final IntegerValue access$getLiquidSlowlyDistanceValue$p(Arraylist arraylist) {
        return arraylist.liquidSlowlyDistanceValue;
    }

    public static final FloatValue access$getFadeOffset$p(Arraylist arraylist) {
        return arraylist.fadeOffset;
    }

    public final void setFontRenderer(IFontRenderer iFontRenderer) {
        this.fontRenderer = iFontRenderer;
    }

    public static final IntegerValue access$getBackgroundWithValue$p(Arraylist arraylist) {
        return arraylist.backgroundWithValue;
    }

    public static final IntegerValue access$getColorBlueValue2$p(Arraylist arraylist) {
        return arraylist.colorBlueValue2;
    }

    public static final IntegerValue access$getShadowColorBlueValue$p(Arraylist arraylist) {
        return arraylist.shadowColorBlueValue;
    }

    public final IFontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public static final BoolValue access$getShadowNoCutValue$p(Arraylist arraylist) {
        return arraylist.shadowNoCutValue;
    }

    public static final ListValue access$getRectRightValue$p(Arraylist arraylist) {
        return arraylist.rectRightValue;
    }

    public static final FontValue access$getFontValue$p(Arraylist arraylist) {
        return arraylist.fontValue;
    }

    public static final IntegerValue access$getShadowColorRedValue$p(Arraylist arraylist) {
        return arraylist.shadowColorRedValue;
    }

    public static final IntegerValue access$getTextRed2$p(Arraylist arraylist) {
        return arraylist.textRed2;
    }

    public static final FloatValue access$getSaturationValue$p(Arraylist arraylist) {
        return arraylist.saturationValue;
    }

    public static final void access$setModules$p(Arraylist arraylist, List list) {
        arraylist.modules = list;
    }

    public static final ListValue access$getRectLeftValue$p(Arraylist arraylist) {
        return arraylist.rectLeftValue;
    }

    public final IntegerValue getColorBlueValue() {
        return this.colorBlueValue;
    }

    public static final List access$getModules$p(Arraylist arraylist) {
        return arraylist.modules;
    }

    private final String getModTag(Module module) {
        if (!((Boolean)this.tags.get()).booleanValue() || module.getTag() == null) {
            return "";
        }
        String string = ' ' + ((Boolean)this.tagsArrayColor.get() != false ? "" : "\u00a77");
        if (!StringsKt.equals((String)((String)this.tagsStyleValue.get()), (String)"default", (boolean)true)) {
            string = string + String.valueOf(((String)this.tagsStyleValue.get()).charAt(0)) + (StringsKt.equals((String)((String)this.tagsStyleValue.get()), (String)"-", (boolean)true) || StringsKt.equals((String)((String)this.tagsStyleValue.get()), (String)"|", (boolean)true) ? " " : "");
        }
        string = string + module.getTag();
        if (!(StringsKt.equals((String)((String)this.tagsStyleValue.get()), (String)"default", (boolean)true) || StringsKt.equals((String)((String)this.tagsStyleValue.get()), (String)"-", (boolean)true) || StringsKt.equals((String)((String)this.tagsStyleValue.get()), (String)"|", (boolean)true))) {
            string = string + String.valueOf(((String)this.tagsStyleValue.get()).charAt(1));
        }
        return string;
    }

    public Arraylist() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public final IntegerValue getColorGreenValue() {
        return this.colorGreenValue;
    }

    public final IntegerValue getColorRedValue() {
        return this.colorRedValue;
    }

    public Arraylist(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 1.0;
        }
        if ((n & 2) != 0) {
            d2 = 2.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.UP);
        }
        this(d, d2, f, side);
    }

    public static final IntegerValue access$getCRainbowDistValue$p(Arraylist arraylist) {
        return arraylist.cRainbowDistValue;
    }

    public final String getModName(Module module) {
        String string = ((Boolean)this.nameBreak.get() != false ? module.getName() : module.getName()) + this.getModTag(module);
        String string2 = (String)this.caseValue.get();
        boolean bl = false;
        String string3 = string2;
        if (string3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string3.toLowerCase()) {
            case "lower": {
                String string4 = string;
                boolean bl2 = false;
                String string5 = string4;
                if (string5 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string = string5.toLowerCase();
                break;
            }
            case "upper": {
                String string6 = string;
                boolean bl3 = false;
                String string7 = string6;
                if (string7 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string = string7.toUpperCase();
                break;
            }
        }
        return string;
    }

    public static final FloatValue access$getBrightnessValue$p(Arraylist arraylist) {
        return arraylist.brightnessValue;
    }

    @Override
    public Border drawElement() {
        int n;
        float f;
        Object object2;
        IFontRenderer iFontRenderer = (IFontRenderer)this.fontValue.get();
        int[] nArray = new int[]{0};
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        int n2 = RenderUtils.deltaTime;
        String string = (String)this.colorModeValue.get();
        String string2 = (String)this.colorModeValue.get();
        int n3 = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()).getRGB();
        int n4 = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()).getRGB();
        float f2 = ((Number)this.spaceValue.get()).floatValue();
        float f3 = ((Number)this.textHeightValue.get()).floatValue();
        float f4 = ((Number)this.textYValue.get()).floatValue();
        int n5 = new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()).getRGB();
        int n6 = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue()).getRGB();
        boolean bl = (Boolean)this.shadow.get();
        float f5 = f3 + f2;
        float f6 = ((Number)this.saturationValue.get()).floatValue();
        float f7 = ((Number)this.brightnessValue.get()).floatValue();
        int n7 = 0;
        for (Object object2 : this.sortedModules) {
            if (((Module)object2).getArray() && (((Module)object2).getState() || ((Module)object2).getSlide() != 0.0f)) {
                String string3 = this.getModName((Module)object2);
                int n8 = iFontRenderer.getStringWidth(string3);
                switch ((String)this.hAnimation.get()) {
                    case "Astolfo": {
                        if (((Module)object2).getState()) {
                            if (((Module)object2).getSlide() < (float)n8) {
                                Object object3 = object2;
                                ((Module)object3).setSlide(((Module)object3).getSlide() + ((Number)this.animationSpeed.get()).floatValue() * (float)n2);
                                ((Module)object2).setSlideStep((float)n2 / 1.0f);
                            }
                        } else if (((Module)object2).getSlide() > 0.0f) {
                            Object object4 = object2;
                            ((Module)object4).setSlide(((Module)object4).getSlide() - ((Number)this.animationSpeed.get()).floatValue() * (float)n2);
                            ((Module)object2).setSlideStep(0.0f);
                        }
                        if (!(((Module)object2).getSlide() > (float)n8)) break;
                        ((Module)object2).setSlide(n8);
                        break;
                    }
                    case "Slide": {
                        if (((Module)object2).getState()) {
                            if (!(((Module)object2).getSlide() < (float)n8)) break;
                            ((Module)object2).setSlide((float)AnimationUtils.animate((double)n8, (double)((Module)object2).getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)n2));
                            ((Module)object2).setSlideStep((float)n2 / 1.0f);
                            break;
                        }
                        if (!(((Module)object2).getSlide() > 0.0f)) break;
                        ((Module)object2).setSlide((float)AnimationUtils.animate(-((double)n8), (double)((Module)object2).getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)n2));
                        ((Module)object2).setSlideStep(0.0f);
                        break;
                    }
                    case "Default": {
                        if (((Module)object2).getState()) {
                            if (!(((Module)object2).getSlide() < (float)n8)) break;
                            ((Module)object2).setSlide(AnimationUtils.easeOut(((Module)object2).getSlideStep(), n8) * (float)n8);
                            Object object5 = object2;
                            ((Module)object5).setSlideStep(((Module)object5).getSlideStep() + (float)n2 / 4.0f);
                            break;
                        }
                        if (!(((Module)object2).getSlide() > 0.0f)) break;
                        ((Module)object2).setSlide(AnimationUtils.easeOut(((Module)object2).getSlideStep(), n8) * (float)n8);
                        Object object6 = object2;
                        ((Module)object6).setSlideStep(((Module)object6).getSlideStep() - (float)n2 / 4.0f);
                        break;
                    }
                    default: {
                        ((Module)object2).setSlide(((Module)object2).getState() ? (float)n8 : 0.0f);
                        Object object7 = object2;
                        ((Module)object7).setSlideStep(((Module)object7).getSlideStep() + (float)(((Module)object2).getState() ? n2 : -n2));
                    }
                }
                ((Module)object2).setSlide(RangesKt.coerceIn((float)((Module)object2).getSlide(), (float)0.0f, (float)n8));
                ((Module)object2).setSlideStep(RangesKt.coerceIn((float)((Module)object2).getSlideStep(), (float)0.0f, (float)n8));
            }
            f = (this.getSide().getVertical() == Side.Vertical.DOWN ? -f5 : f5) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? n7 + 1 : n7);
            if (((Module)object2).getArray() && ((Module)object2).getSlide() > 0.0f) {
                if (StringsKt.equals((String)((String)this.vAnimation.get()), (String)"Rise", (boolean)true) && !((Module)object2).getState()) {
                    f = (float)(-iFontRenderer.getFontHeight()) - f4;
                }
                float f8 = (float)this.modules.size() * 0.02f;
                switch ((String)this.vAnimation.get()) {
                    case "LiquidSense": {
                        if (((Module)object2).getState()) {
                            if (((Module)object2).getHigt() < f) {
                                Object object8 = object2;
                                ((Module)object8).setHigt(((Module)object8).getHigt() + (f8 - Math.min(((Module)object2).getHigt() * 0.002f, f8 - ((Module)object2).getHigt() * 1.0E-4f)) * (float)n2);
                                ((Module)object2).setHigt(Math.min(f, ((Module)object2).getHigt()));
                            } else {
                                Object object9 = object2;
                                ((Module)object9).setHigt(((Module)object9).getHigt() - (f8 - Math.min(((Module)object2).getHigt() * 0.002f, f8 - ((Module)object2).getHigt() * 1.0E-4f)) * (float)n2);
                                ((Module)object2).setHigt(Math.max(((Module)object2).getHigt(), f));
                            }
                        }
                        double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(((Module)object2).getHigt() / (float)iFontRenderer.getFontHeight());
                        Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                        boolean bl2 = false;
                        double d2 = Math.abs(d);
                        n = RenderUtils.getGradientOffset(color2, color, d2 / (double)10).getRGB();
                        break;
                    }
                    case "Slide": 
                    case "Rise": {
                        ((Module)object2).setHigt((float)AnimationUtils.animate((double)f, (double)((Module)object2).getHigt(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)n2));
                        break;
                    }
                    case "Astolfo": {
                        if (((Module)object2).getHigt() < f) {
                            Object object10 = object2;
                            ((Module)object10).setHigt(((Module)object10).getHigt() + ((Number)this.animationSpeed.get()).floatValue() / 2.0f * (float)n2);
                            ((Module)object2).setHigt(Math.min(f, ((Module)object2).getHigt()));
                            break;
                        }
                        Object object11 = object2;
                        ((Module)object11).setHigt(((Module)object11).getHigt() - ((Number)this.animationSpeed.get()).floatValue() / 2.0f * (float)n2);
                        ((Module)object2).setHigt(Math.max(((Module)object2).getHigt(), f));
                        break;
                    }
                    default: {
                        ((Module)object2).setHigt(f);
                    }
                }
                ++n7;
                continue;
            }
            if (StringsKt.equals((String)((String)this.vAnimation.get()), (String)"rise", (boolean)true)) continue;
            ((Module)object2).setHigt(f);
        }
        switch (Arraylist$WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
            case 1: 
            case 2: {
                int n9;
                int n10;
                int n11;
                boolean bl3;
                int n12;
                Object object12;
                if (((Boolean)this.shadowShaderValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    ShadowUtils.INSTANCE.shadow(((Number)this.shadowStrength.get()).intValue(), new Function0(this, f6, f7, nArray, iFontRenderer, f3, string, n3, n6){
                        final float $saturation;
                        final IFontRenderer $fontRenderer;
                        final int[] $counter;
                        final float $brightness;
                        final int $alpha;
                        final float $textHeight;
                        final Arraylist this$0;
                        final int $customColor;
                        final String $colorMode;

                        public final void invoke() {
                            GL11.glPushMatrix();
                            GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                            Iterable iterable = Arraylist.access$getModules$p(this.this$0);
                            boolean bl = false;
                            int n = 0;
                            for (T t : iterable) {
                                int n2 = n++;
                                boolean bl2 = false;
                                if (n2 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n3 = n2;
                                Module module = (Module)t;
                                int n4 = n3;
                                boolean bl3 = false;
                                float f = -module.getSlide() - (float)2;
                                String string = (String)Arraylist.access$getShadowColorMode$p(this.this$0).get();
                                int n5 = 0;
                                String string2 = string;
                                if (string2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                switch (string2.toLowerCase()) {
                                    case "background": {
                                        new Color(((Number)Arraylist.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                        break;
                                    }
                                    case "gradient": {
                                        Integer n6;
                                        n5 = Color.getHSBColor(module.getHue(), this.$saturation, this.$brightness).getRGB();
                                        int n7 = RenderUtils.SkyRainbow(this.$counter[0] * (((Number)Arraylist.access$getSkyDistanceValue$p(this.this$0).get()).intValue() * 50), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        int n8 = RenderUtils.getRainbowOpaque(((Number)Arraylist.access$getCRainbowSecValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue(), this.$counter[0] * (50 * ((Number)Arraylist.access$getCRainbowDistValue$p(this.this$0).get()).intValue()));
                                        int n9 = ColorUtils.fade(new Color(((Number)this.this$0.getColorRedValue().get()).intValue(), ((Number)this.this$0.getColorGreenValue().get()).intValue(), ((Number)this.this$0.getColorBlueValue().get()).intValue(), ((Number)this.this$0.getColorAlphaValue().get()).intValue()), n4 * ((Number)Arraylist.access$getFadeDistanceValue$p(this.this$0).get()).intValue(), 100).getRGB();
                                        this.$counter[0] = this.$counter[0] - 1;
                                        double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(module.getHigt() / (float)this.$fontRenderer.getFontHeight());
                                        Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        int n10 = 0;
                                        double d2 = Math.abs(d);
                                        int n11 = RenderUtils.getGradientOffset(color2, color, d2 / (double)10).getRGB();
                                        Color color3 = ColorUtils.LiquidSlowly(System.nanoTime(), n4 * ((Number)Arraylist.access$getLiquidSlowlyDistanceValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        Integer n12 = n6 = color3 != null ? Integer.valueOf(color3.getRGB()) : null;
                                        if (n12 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        int n13 = n12;
                                        RenderUtils.ArrayListBGGradient(f - (float)(StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 2 + ((Number)Arraylist.access$getBackgroundWithValue$p(this.this$0).get()).intValue() : 0 + ((Number)Arraylist.access$getBackgroundWithValue$p(this.this$0).get()).intValue()), module.getHigt() - (float)(n4 == 0 ? 1 : 0), StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? (double)-2.0f : 1.0, module.getHigt() + this.$textHeight, StringsKt.equals((String)this.$colorMode, (String)"Random", (boolean)true) ? n5 : (StringsKt.equals((String)this.$colorMode, (String)"Sky", (boolean)true) ? n7 : (StringsKt.equals((String)this.$colorMode, (String)"CRainbow", (boolean)true) ? n8 : (StringsKt.equals((String)this.$colorMode, (String)"LiquidSlowly", (boolean)true) ? n13 : (StringsKt.equals((String)this.$colorMode, (String)"Fade", (boolean)true) ? n9 : (StringsKt.equals((String)this.$colorMode, (String)"DoubleColor", (boolean)true) ? n11 : (StringsKt.equals((String)this.$colorMode, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)Arraylist.access$getTextRed$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextGreen$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextBlue$p(this.this$0).get()).intValue()), new Color(((Number)Arraylist.access$getTextRed2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextGreen2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextBlue2$p(this.this$0).get()).intValue()), (double)n4 * (double)((Number)Arraylist.access$getFadeOffset$p(this.this$0).get()).floatValue()).getRGB() : this.$customColor)))))), this.$alpha);
                                        break;
                                    }
                                    case "round": {
                                        Integer n14;
                                        n5 = Color.getHSBColor(module.getHue(), this.$saturation, this.$brightness).getRGB();
                                        int n7 = RenderUtils.SkyRainbow(this.$counter[0] * (((Number)Arraylist.access$getSkyDistanceValue$p(this.this$0).get()).intValue() * 50), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        int n8 = RenderUtils.getRainbowOpaque(((Number)Arraylist.access$getCRainbowSecValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue(), this.$counter[0] * (50 * ((Number)Arraylist.access$getCRainbowDistValue$p(this.this$0).get()).intValue()));
                                        int n9 = ColorUtils.fade(new Color(((Number)this.this$0.getColorRedValue().get()).intValue(), ((Number)this.this$0.getColorGreenValue().get()).intValue(), ((Number)this.this$0.getColorBlueValue().get()).intValue(), ((Number)this.this$0.getColorAlphaValue().get()).intValue()), n4 * ((Number)Arraylist.access$getFadeDistanceValue$p(this.this$0).get()).intValue(), 100).getRGB();
                                        this.$counter[0] = this.$counter[0] - 1;
                                        int n11 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue()).getRGB();
                                        double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(module.getHigt() / (float)this.$fontRenderer.getFontHeight());
                                        Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        boolean bl4 = false;
                                        double d2 = Math.abs(d);
                                        int n15 = RenderUtils.getGradientOffset(color2, color, d2 / (double)10).getRGB();
                                        Color color4 = ColorUtils.LiquidSlowly(System.nanoTime(), n4 * ((Number)Arraylist.access$getLiquidSlowlyDistanceValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        Integer n16 = n14 = color4 != null ? Integer.valueOf(color4.getRGB()) : null;
                                        if (n16 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        int n10 = n16;
                                        RenderUtils.ArrayListBGGradient(f - (float)(StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 2 + ((Number)Arraylist.access$getBackgroundWithValue$p(this.this$0).get()).intValue() : 0 + ((Number)Arraylist.access$getBackgroundWithValue$p(this.this$0).get()).intValue()), module.getHigt() - (float)(n4 == 0 ? 1 : 0), StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? (double)-2.0f : 1.0, module.getHigt() + this.$textHeight, StringsKt.equals((String)this.$colorMode, (String)"Random", (boolean)true) ? n5 : (StringsKt.equals((String)this.$colorMode, (String)"Sky", (boolean)true) ? n7 : (StringsKt.equals((String)this.$colorMode, (String)"CRainbow", (boolean)true) ? n8 : (StringsKt.equals((String)this.$colorMode, (String)"LiquidSlowly", (boolean)true) ? n10 : (StringsKt.equals((String)this.$colorMode, (String)"Fade", (boolean)true) ? n9 : (StringsKt.equals((String)this.$colorMode, (String)"DoubleColor", (boolean)true) ? n15 : (StringsKt.equals((String)this.$colorMode, (String)"CustomHUD", (boolean)true) ? n11 : (StringsKt.equals((String)this.$colorMode, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)Arraylist.access$getTextRed$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextGreen$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextBlue$p(this.this$0).get()).intValue()), new Color(((Number)Arraylist.access$getTextRed2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextGreen2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextBlue2$p(this.this$0).get()).intValue()), (double)n4 * (double)((Number)Arraylist.access$getFadeOffset$p(this.this$0).get()).floatValue()).getRGB() : this.$customColor))))))), this.$alpha);
                                        break;
                                    }
                                    case "text": {
                                        Integer n6;
                                        n5 = Color.getHSBColor(module.getHue(), this.$saturation, this.$brightness).getRGB();
                                        int n7 = RenderUtils.SkyRainbow(this.$counter[0] * (((Number)Arraylist.access$getSkyDistanceValue$p(this.this$0).get()).intValue() * 50), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        int n8 = RenderUtils.getRainbowOpaque(((Number)Arraylist.access$getCRainbowSecValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue(), this.$counter[0] * (50 * ((Number)Arraylist.access$getCRainbowDistValue$p(this.this$0).get()).intValue()));
                                        int n9 = ColorUtils.fade(new Color(((Number)this.this$0.getColorRedValue().get()).intValue(), ((Number)this.this$0.getColorGreenValue().get()).intValue(), ((Number)this.this$0.getColorBlueValue().get()).intValue(), ((Number)this.this$0.getColorAlphaValue().get()).intValue()), n4 * ((Number)Arraylist.access$getFadeDistanceValue$p(this.this$0).get()).intValue(), 100).getRGB();
                                        this.$counter[0] = this.$counter[0] - 1;
                                        double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(module.getHigt() / (float)this.$fontRenderer.getFontHeight());
                                        Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        Color color2 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        int n10 = 0;
                                        double d2 = Math.abs(d);
                                        int n11 = RenderUtils.getGradientOffset(color2, color, d2 / (double)10).getRGB();
                                        Color color5 = ColorUtils.LiquidSlowly(System.nanoTime(), n4 * ((Number)Arraylist.access$getLiquidSlowlyDistanceValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        Integer n17 = n6 = color5 != null ? Integer.valueOf(color5.getRGB()) : null;
                                        if (n17 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        int n13 = n17;
                                        RenderUtils.drawRoundRect(f - (float)(StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3 : 2), module.getHigt(), StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? -1.0f : 0.0f, module.getHigt() + this.$textHeight, StringsKt.equals((String)this.$colorMode, (String)"Random", (boolean)true) ? n5 : (StringsKt.equals((String)this.$colorMode, (String)"Sky", (boolean)true) ? n7 : (StringsKt.equals((String)this.$colorMode, (String)"CRainbow", (boolean)true) ? n8 : (StringsKt.equals((String)this.$colorMode, (String)"LiquidSlowly", (boolean)true) ? n13 : (StringsKt.equals((String)this.$colorMode, (String)"Fade", (boolean)true) ? n9 : (StringsKt.equals((String)this.$colorMode, (String)"DoubleColor", (boolean)true) ? n11 : (StringsKt.equals((String)this.$colorMode, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)Arraylist.access$getTextRed$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextGreen$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextBlue$p(this.this$0).get()).intValue()), new Color(((Number)Arraylist.access$getTextRed2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextGreen2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getTextBlue2$p(this.this$0).get()).intValue()), (double)n4 * (double)((Number)Arraylist.access$getFadeOffset$p(this.this$0).get()).floatValue()).getRGB() : this.$customColor)))))));
                                        break;
                                    }
                                    default: {
                                        new Color(((Number)Arraylist.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                    }
                                }
                            }
                            GL11.glPopMatrix();
                            this.$counter[0] = 0;
                        }

                        public Object invoke() {
                            this.invoke();
                            return Unit.INSTANCE;
                        }
                        {
                            this.this$0 = arraylist;
                            this.$saturation = f;
                            this.$brightness = f2;
                            this.$counter = nArray;
                            this.$fontRenderer = iFontRenderer;
                            this.$textHeight = f3;
                            this.$colorMode = string;
                            this.$customColor = n;
                            this.$alpha = n2;
                            super(0);
                        }

                        static {
                        }
                    }, new Function0(this, f3){
                        final Arraylist this$0;
                        final float $textHeight;

                        public final void invoke() {
                            if (!((Boolean)Arraylist.access$getShadowNoCutValue$p(this.this$0).get()).booleanValue()) {
                                GL11.glPushMatrix();
                                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                                Iterable iterable = Arraylist.access$getModules$p(this.this$0);
                                boolean bl = false;
                                int n = 0;
                                for (T t : iterable) {
                                    int n2 = n++;
                                    boolean bl2 = false;
                                    if (n2 < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    int n3 = n2;
                                    Module module = (Module)t;
                                    int n4 = n3;
                                    boolean bl3 = false;
                                    float f = -module.getSlide() - (float)2;
                                    RenderUtils.quickDrawRect(f - (float)(StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3 : 2), module.getHigt(), StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? -1.0f : 0.0f, module.getHigt() + this.$textHeight);
                                }
                                GL11.glPopMatrix();
                            }
                        }

                        public Object invoke() {
                            this.invoke();
                            return Unit.INSTANCE;
                        }

                        static {
                        }
                        {
                            this.this$0 = arraylist;
                            this.$textHeight = f;
                            super(0);
                        }
                    });
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                if (((Boolean)this.blurValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    float f9 = (float)this.getRenderX();
                    float f10 = (float)this.getRenderY();
                    f = 0.0f;
                    float f11 = 0.0f;
                    object12 = this.modules;
                    n = 0;
                    int n13 = 0;
                    Iterator iterator2 = object12.iterator();
                    while (iterator2.hasNext()) {
                        Object t = iterator2.next();
                        n12 = n13++;
                        bl3 = false;
                        if (n12 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        int n14 = n12;
                        Module module = (Module)t;
                        int n15 = n14;
                        n11 = 0;
                        String string4 = this.getModName(module);
                        float f12 = (float)iFontRenderer.getStringWidth(string4) + 2.0f;
                        float f13 = this.getSide().getVertical() == Side.Vertical.DOWN ? -f5 : f5 * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? n15 + 1 : n15);
                        f += f13;
                        f11 = Math.min(f11, -f12);
                    }
                    ArrayBlurUtils.blur(f9, f10, f9 + f11, f10 + f, ((Number)this.blurStrength.get()).floatValue(), false, new Function0(this, f9, f10, f3){
                        final float $textHeight;
                        final float $floatX;
                        final float $floatY;
                        final Arraylist this$0;

                        public Object invoke() {
                            this.invoke();
                            return Unit.INSTANCE;
                        }

                        static {
                        }
                        {
                            this.this$0 = arraylist;
                            this.$floatX = f;
                            this.$floatY = f2;
                            this.$textHeight = f3;
                            super(0);
                        }

                        public final void invoke() {
                            Iterable iterable = Arraylist.access$getModules$p(this.this$0);
                            boolean bl = false;
                            int n = 0;
                            for (T t : iterable) {
                                int n2 = n++;
                                boolean bl2 = false;
                                if (n2 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n3 = n2;
                                Module module = (Module)t;
                                int n4 = n3;
                                boolean bl3 = false;
                                float f = -module.getSlide() - (float)2;
                                RenderUtils.quickDrawRect(this.$floatX + f - (float)(StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3 : 2), this.$floatY + module.getHigt(), this.$floatX + (StringsKt.equals((String)((String)Arraylist.access$getRectRightValue$p(this.this$0).get()), (String)"right", (boolean)true) ? -1.0f : 0.0f), this.$floatY + module.getHigt() + this.$textHeight);
                            }
                        }
                    });
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                object2 = this.modules;
                boolean bl4 = false;
                int n16 = 0;
                Iterator iterator3 = object2.iterator();
                while (iterator3.hasNext()) {
                    Integer n17;
                    object12 = iterator3.next();
                    n = n16++;
                    boolean bl5 = false;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n18 = n;
                    Module module = (Module)object12;
                    n12 = n18;
                    bl3 = false;
                    String string5 = this.getModName(module);
                    int n19 = iFontRenderer.getStringWidth(string5);
                    float f14 = -module.getSlide() - (float)2;
                    n11 = Color.getHSBColor(module.getHue(), f6, f7).getRGB();
                    int n20 = 0;
                    n20 = RenderUtils.SkyRainbow(nArray[0] * (((Number)this.skyDistanceValue.get()).intValue() * 50), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    int n21 = 0;
                    n21 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), nArray[0] * (50 * ((Number)this.cRainbowDistValue.get()).intValue()));
                    int n22 = ColorUtils.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()), n12 * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    nArray[0] = nArray[0] - 1;
                    double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(module.getHigt() / (float)iFontRenderer.getFontHeight());
                    Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                    Color color3 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                    n10 = 0;
                    double d3 = Math.abs(d);
                    int n23 = RenderUtils.getGradientOffset(color3, color, d3 / (double)10).getRGB();
                    Color color4 = ColorUtils.LiquidSlowly(System.nanoTime(), n12 * ((Number)this.liquidSlowlyDistanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    Integer n24 = n17 = color4 != null ? Integer.valueOf(color4.getRGB()) : null;
                    if (n24 == null) {
                        Intrinsics.throwNpe();
                    }
                    n9 = n24;
                    RenderUtils.drawRect(f14 - (float)(StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"right", (boolean)true) ? 3 : 2), module.getHigt(), StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"right", (boolean)true) ? -1.0f : 0.0f, module.getHigt() + f3, n5);
                    iFontRenderer.drawString(string5, f14 - (float)(StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"right", (boolean)true) ? 1 : 0), module.getHigt() + f4, StringsKt.equals((String)string, (String)"Random", (boolean)true) ? n11 : (StringsKt.equals((String)string, (String)"Sky", (boolean)true) ? n20 : (StringsKt.equals((String)string, (String)"CRainbow", (boolean)true) ? n21 : (StringsKt.equals((String)string, (String)"LiquidSlowly", (boolean)true) ? n9 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? n22 : (StringsKt.equals((String)string, (String)"DoubleColor", (boolean)true) ? n23 : (StringsKt.equals((String)string, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(module.getHigt() / (float)iFontRenderer.getFontHeight())) / (double)10).getRGB() : n3)))))), bl);
                    if (StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"none", (boolean)true)) continue;
                    n10 = StringsKt.equals((String)string2, (String)"Random", (boolean)true) ? n11 : (StringsKt.equals((String)string2, (String)"Sky", (boolean)true) ? n20 : (StringsKt.equals((String)string2, (String)"CRainbow", (boolean)true) ? n21 : (StringsKt.equals((String)string2, (String)"LiquidSlowly", (boolean)true) ? n9 : (StringsKt.equals((String)string2, (String)"Fade", (boolean)true) ? n22 : (StringsKt.equals((String)string2, (String)"DoubleColor", (boolean)true) ? n23 : (StringsKt.equals((String)string2, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(module.getHigt() / (float)iFontRenderer.getFontHeight())) / (double)10).getRGB() : n4))))));
                    if (StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"left", (boolean)true)) {
                        RenderUtils.drawRect(f14 - (float)3, module.getHigt(), f14 - (float)2, module.getHigt() + f3, n10);
                        continue;
                    }
                    if (StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"right", (boolean)true)) {
                        RenderUtils.drawRect(-1.0f, module.getHigt(), 0.0f, module.getHigt() + f3, n10);
                        continue;
                    }
                    if (StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"outline", (boolean)true)) {
                        RenderUtils.drawRect(-1.0f, module.getHigt() - 1.0f, 0.0f, module.getHigt() + f3, n10);
                        RenderUtils.drawRect(f14 - (float)3, module.getHigt(), f14 - (float)2, module.getHigt() + f3, n10);
                        if (module.equals((Module)this.modules.get(0)) ^ true) {
                            String string6 = this.getModName((Module)this.modules.get(n12 - 1));
                            RenderUtils.drawRect(f14 - (float)3 - (float)(iFontRenderer.getStringWidth(string6) - iFontRenderer.getStringWidth(string5)), module.getHigt(), f14 - (float)2, module.getHigt() + 1.0f, n10);
                            if (!module.equals((Module)this.modules.get(this.modules.size() - 1))) continue;
                            RenderUtils.drawRect(f14 - (float)3, module.getHigt() + f3, 0.0f, module.getHigt() + f3 + 1.0f, n10);
                            continue;
                        }
                        RenderUtils.drawRect(f14 - (float)3, module.getHigt(), 0.0f, module.getHigt() - 1.0f, n10);
                        continue;
                    }
                    if (StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"special", (boolean)true)) {
                        if (module.equals((Module)this.modules.get(0))) {
                            RenderUtils.drawRect(f14 - (float)2, module.getHigt(), 0.0f, module.getHigt() - 1.0f, n10);
                        }
                        if (!module.equals((Module)this.modules.get(this.modules.size() - 1))) continue;
                        RenderUtils.drawRect(f14 - (float)2, module.getHigt() + f3, 0.0f, module.getHigt() + f3 + 1.0f, n10);
                        continue;
                    }
                    if (!StringsKt.equals((String)((String)this.rectRightValue.get()), (String)"top", (boolean)true) || !module.equals((Module)this.modules.get(0))) continue;
                    RenderUtils.drawRect(f14 - (float)2, module.getHigt(), 0.0f, module.getHigt() - 1.0f, n10);
                }
                break;
            }
            case 3: {
                int n9;
                int n10;
                int n11;
                boolean bl3;
                int n12;
                Object object12;
                if (((Boolean)this.shadowShaderValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    ShadowUtils.INSTANCE.shadow(((Number)this.shadowStrength.get()).intValue(), new Function0(this, iFontRenderer, f3, f6, f7, nArray, string, n3){
                        final float $brightness;
                        final float $textHeight;
                        final IFontRenderer $fontRenderer;
                        final Arraylist this$0;
                        final int[] $counter;
                        final float $saturation;
                        final int $customColor;
                        final String $colorMode;
                        {
                            this.this$0 = arraylist;
                            this.$fontRenderer = iFontRenderer;
                            this.$textHeight = f;
                            this.$saturation = f2;
                            this.$brightness = f3;
                            this.$counter = nArray;
                            this.$colorMode = string;
                            this.$customColor = n;
                            super(0);
                        }

                        public Object invoke() {
                            this.invoke();
                            return Unit.INSTANCE;
                        }

                        public final void invoke() {
                            GL11.glPushMatrix();
                            GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                            Iterable iterable = Arraylist.access$getModules$p(this.this$0);
                            boolean bl = false;
                            int n = 0;
                            for (T t : iterable) {
                                int n2;
                                int n3 = n++;
                                boolean bl2 = false;
                                if (n3 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n4 = n3;
                                Module module = (Module)t;
                                int n5 = n4;
                                boolean bl3 = false;
                                String string = this.this$0.getModName(module);
                                int n6 = this.$fontRenderer.getStringWidth(string);
                                float f = -((float)n6 - module.getSlide()) + (float)(StringsKt.equals((String)((String)Arraylist.access$getRectLeftValue$p(this.this$0).get()), (String)"left", (boolean)true) ? 3 : 2);
                                String string2 = (String)Arraylist.access$getShadowColorMode$p(this.this$0).get();
                                float f2 = module.getHigt() + this.$textHeight;
                                float f3 = f + (float)n6 + (StringsKt.equals((String)((String)Arraylist.access$getRectLeftValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3.0f : 2.0f);
                                float f4 = module.getHigt();
                                float f5 = 0.0f;
                                int n7 = 0;
                                String string3 = string2;
                                if (string3 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                Object object = string3.toLowerCase();
                                float f6 = f5;
                                float f7 = f4;
                                float f8 = f3;
                                float f9 = f2;
                                switch (object) {
                                    case "background": {
                                        n2 = new Color(((Number)Arraylist.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                        break;
                                    }
                                    case "text": {
                                        Integer n8;
                                        n7 = Color.getHSBColor(module.getHue(), this.$saturation, this.$brightness).getRGB();
                                        int n9 = RenderUtils.SkyRainbow(this.$counter[0] * (((Number)Arraylist.access$getSkyDistanceValue$p(this.this$0).get()).intValue() * 50), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        int n10 = RenderUtils.getRainbowOpaque(((Number)Arraylist.access$getCRainbowSecValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue(), this.$counter[0] * (50 * ((Number)Arraylist.access$getCRainbowDistValue$p(this.this$0).get()).intValue()));
                                        int n11 = ColorUtils.fade(new Color(((Number)this.this$0.getColorRedValue().get()).intValue(), ((Number)this.this$0.getColorGreenValue().get()).intValue(), ((Number)this.this$0.getColorBlueValue().get()).intValue(), ((Number)this.this$0.getColorAlphaValue().get()).intValue()), n5 * ((Number)Arraylist.access$getFadeDistanceValue$p(this.this$0).get()).intValue(), 100).getRGB();
                                        this.$counter[0] = this.$counter[0] - 1;
                                        double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(module.getHigt() / (float)this.$fontRenderer.getFontHeight());
                                        Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        object = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue());
                                        f2 = f9;
                                        f3 = f8;
                                        f4 = f7;
                                        f5 = f6;
                                        boolean bl4 = false;
                                        double d2 = Math.abs(d);
                                        f6 = f5;
                                        f7 = f4;
                                        f8 = f3;
                                        f9 = f2;
                                        int n12 = RenderUtils.getGradientOffset((Color)object, color, d2 / (double)10).getRGB();
                                        Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), n5 * ((Number)Arraylist.access$getLiquidSlowlyDistanceValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                        Integer n13 = n8 = color2 != null ? Integer.valueOf(color2.getRGB()) : null;
                                        if (n13 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        int n14 = n13;
                                        if (StringsKt.equals((String)this.$colorMode, (String)"Random", (boolean)true)) {
                                            n2 = n7;
                                            break;
                                        }
                                        if (StringsKt.equals((String)this.$colorMode, (String)"Sky", (boolean)true)) {
                                            n2 = n9;
                                            break;
                                        }
                                        if (StringsKt.equals((String)this.$colorMode, (String)"CRainbow", (boolean)true)) {
                                            n2 = n10;
                                            break;
                                        }
                                        if (StringsKt.equals((String)this.$colorMode, (String)"LiquidSlowly", (boolean)true)) {
                                            n2 = n14;
                                            break;
                                        }
                                        if (StringsKt.equals((String)this.$colorMode, (String)"Fade", (boolean)true)) {
                                            n2 = n11;
                                            break;
                                        }
                                        if (StringsKt.equals((String)this.$colorMode, (String)"DoubleColor", (boolean)true)) {
                                            n2 = n12;
                                            break;
                                        }
                                        if (StringsKt.equals((String)this.$colorMode, (String)"Gradinet", (boolean)true)) {
                                            n2 = RenderUtils.getGradientOffset(new Color(((Number)this.this$0.getColorRedValue().get()).intValue(), ((Number)this.this$0.getColorGreenValue().get()).intValue(), ((Number)this.this$0.getColorBlueValue().get()).intValue(), 1), new Color(((Number)Arraylist.access$getColorRedValue2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue2$p(this.this$0).get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)Arraylist.access$getGidentspeed$p(this.this$0).get()).intValue() + (double)(module.getHigt() / (float)this.$fontRenderer.getFontHeight())) / (double)10).getRGB();
                                            break;
                                        }
                                        n2 = this.$customColor;
                                        break;
                                    }
                                    default: {
                                        n2 = new Color(((Number)Arraylist.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                    }
                                }
                                RenderUtils.drawRect(f6, f7, f8, f9, n2);
                            }
                            GL11.glPopMatrix();
                        }

                        static {
                        }
                    }, new Function0(this, iFontRenderer, f3){
                        final IFontRenderer $fontRenderer;
                        final Arraylist this$0;
                        final float $textHeight;

                        static {
                        }

                        public final void invoke() {
                            if (!((Boolean)Arraylist.access$getShadowNoCutValue$p(this.this$0).get()).booleanValue()) {
                                GL11.glPushMatrix();
                                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                                Iterable iterable = Arraylist.access$getModules$p(this.this$0);
                                boolean bl = false;
                                int n = 0;
                                for (T t : iterable) {
                                    int n2 = n++;
                                    boolean bl2 = false;
                                    if (n2 < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    int n3 = n2;
                                    Module module = (Module)t;
                                    int n4 = n3;
                                    boolean bl3 = false;
                                    String string = this.this$0.getModName(module);
                                    int n5 = this.$fontRenderer.getStringWidth(string);
                                    float f = -((float)n5 - module.getSlide()) + (float)(StringsKt.equals((String)((String)Arraylist.access$getRectLeftValue$p(this.this$0).get()), (String)"left", (boolean)true) ? 3 : 2);
                                    RenderUtils.quickDrawRect(0.0f, module.getHigt(), f + (float)n5 + (float)(StringsKt.equals((String)((String)Arraylist.access$getRectLeftValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3 : 2), module.getHigt() + this.$textHeight);
                                }
                                GL11.glPopMatrix();
                            }
                        }
                        {
                            this.this$0 = arraylist;
                            this.$fontRenderer = iFontRenderer;
                            this.$textHeight = f;
                            super(0);
                        }

                        public Object invoke() {
                            this.invoke();
                            return Unit.INSTANCE;
                        }
                    });
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                if (((Boolean)this.blurValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    float f15 = (float)this.getRenderX();
                    float f16 = (float)this.getRenderY();
                    f = 0.0f;
                    float f17 = 0.0f;
                    object12 = this.modules;
                    n = 0;
                    int n25 = 0;
                    Iterator iterator4 = object12.iterator();
                    while (iterator4.hasNext()) {
                        Object t = iterator4.next();
                        n12 = n25++;
                        bl3 = false;
                        if (n12 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        int n26 = n12;
                        Module module = (Module)t;
                        int n27 = n26;
                        n11 = 0;
                        String string7 = this.getModName(module);
                        float f18 = (float)iFontRenderer.getStringWidth(string7) + 2.0f;
                        float f19 = this.getSide().getVertical() == Side.Vertical.DOWN ? -f5 : f5 * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? n27 + 1 : n27);
                        f += f19;
                        f17 = Math.max(f17, f18);
                    }
                    ArrayBlurUtils.blur(f15, f16, f15 + f17, f16 + f, ((Number)this.blurStrength.get()).floatValue(), false, new Function0(this, iFontRenderer, f15, f16, f3){
                        final float $textHeight;
                        final IFontRenderer $fontRenderer;
                        final float $floatX;
                        final float $floatY;
                        final Arraylist this$0;
                        {
                            this.this$0 = arraylist;
                            this.$fontRenderer = iFontRenderer;
                            this.$floatX = f;
                            this.$floatY = f2;
                            this.$textHeight = f3;
                            super(0);
                        }

                        static {
                        }

                        public Object invoke() {
                            this.invoke();
                            return Unit.INSTANCE;
                        }

                        public final void invoke() {
                            Iterable iterable = Arraylist.access$getModules$p(this.this$0);
                            boolean bl = false;
                            int n = 0;
                            for (T t : iterable) {
                                int n2 = n++;
                                boolean bl2 = false;
                                if (n2 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n3 = n2;
                                Module module = (Module)t;
                                int n4 = n3;
                                boolean bl3 = false;
                                String string = this.this$0.getModName(module);
                                int n5 = this.$fontRenderer.getStringWidth(string);
                                float f = -((float)n5 - module.getSlide()) + (float)(StringsKt.equals((String)((String)Arraylist.access$getRectLeftValue$p(this.this$0).get()), (String)"left", (boolean)true) ? 3 : 2);
                                RenderUtils.quickDrawRect(this.$floatX, this.$floatY + module.getHigt(), this.$floatX + f + (float)n5 + (float)(StringsKt.equals((String)((String)Arraylist.access$getRectLeftValue$p(this.this$0).get()), (String)"right", (boolean)true) ? 3 : 2), this.$floatY + module.getHigt() + this.$textHeight);
                            }
                        }
                    });
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                object2 = this.modules;
                boolean bl6 = false;
                int n28 = 0;
                Iterator iterator5 = object2.iterator();
                while (iterator5.hasNext()) {
                    Integer n29;
                    object12 = iterator5.next();
                    n = n28++;
                    boolean bl7 = false;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n30 = n;
                    Module module = (Module)object12;
                    n12 = n30;
                    bl3 = false;
                    String string8 = this.getModName(module);
                    int n31 = iFontRenderer.getStringWidth(string8);
                    float f20 = -((float)n31 - module.getSlide()) + (float)(StringsKt.equals((String)((String)this.rectLeftValue.get()), (String)"left", (boolean)true) ? 3 : 2);
                    n11 = Color.getHSBColor(module.getHue(), f6, f7).getRGB();
                    double d = (double)System.currentTimeMillis() / (double)((Number)CustomColor.gradientSpeed.get()).intValue() + (double)(module.getHigt() / (float)iFontRenderer.getFontHeight());
                    Color color = new Color(((Number)CustomColor.r2.get()).intValue(), ((Number)CustomColor.g2.get()).intValue(), ((Number)CustomColor.b2.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                    Color color5 = new Color(((Number)CustomColor.r.get()).intValue(), ((Number)CustomColor.g.get()).intValue(), ((Number)CustomColor.b.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue());
                    int n32 = 0;
                    double d4 = Math.abs(d);
                    n10 = RenderUtils.getGradientOffset(color5, color, d4 / (double)10).getRGB();
                    int n33 = 0;
                    n33 = RenderUtils.SkyRainbow(nArray[0] * (((Number)this.skyDistanceValue.get()).intValue() * 50), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    int n34 = 0;
                    n34 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), nArray[0] * (50 * ((Number)this.cRainbowDistValue.get()).intValue()));
                    n32 = ColorUtils.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()), n12 * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    nArray[0] = nArray[0] - 1;
                    Color color6 = ColorUtils.LiquidSlowly(System.nanoTime(), n12 * ((Number)this.liquidSlowlyDistanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    Integer n35 = n29 = color6 != null ? Integer.valueOf(color6.getRGB()) : null;
                    if (n35 == null) {
                        Intrinsics.throwNpe();
                    }
                    int n36 = n35;
                    RenderUtils.drawRect(0.0f, module.getHigt(), f20 + (float)n31 + (float)(StringsKt.equals((String)((String)this.rectLeftValue.get()), (String)"right", (boolean)true) ? 3 : 2), module.getHigt() + f3, n5);
                    iFontRenderer.drawString(string8, f20, module.getHigt() + f4, StringsKt.equals((String)string, (String)"Random", (boolean)true) ? n11 : (StringsKt.equals((String)string, (String)"Sky", (boolean)true) ? n33 : (StringsKt.equals((String)string, (String)"CRainbow", (boolean)true) ? n34 : (StringsKt.equals((String)string, (String)"LiquidSlowly", (boolean)true) ? n36 : (StringsKt.equals((String)string, (String)"Fade", (boolean)true) ? n32 : (StringsKt.equals((String)string, (String)"DoubleColor", (boolean)true) ? n10 : (StringsKt.equals((String)string, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(module.getHigt() / (float)iFontRenderer.getFontHeight())) / (double)10).getRGB() : n3)))))), bl);
                    if (StringsKt.equals((String)((String)this.rectLeftValue.get()), (String)"none", (boolean)true)) continue;
                    n9 = StringsKt.equals((String)string2, (String)"Random", (boolean)true) ? n11 : (StringsKt.equals((String)string2, (String)"Sky", (boolean)true) ? n33 : (StringsKt.equals((String)string2, (String)"CRainbow", (boolean)true) ? n34 : (StringsKt.equals((String)string2, (String)"LiquidSlowly", (boolean)true) ? n36 : (StringsKt.equals((String)string2, (String)"Fade", (boolean)true) ? n32 : (StringsKt.equals((String)string2, (String)"DoubleColor", (boolean)true) ? n10 : (StringsKt.equals((String)string2, (String)"Gradinet", (boolean)true) ? RenderUtils.getGradientOffset(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(module.getHigt() / (float)iFontRenderer.getFontHeight())) / (double)10).getRGB() : n4))))));
                    if (StringsKt.equals((String)((String)this.rectLeftValue.get()), (String)"left", (boolean)true)) {
                        RenderUtils.drawRect(0.0f, module.getHigt() - 1.0f, 1.0f, module.getHigt() + f3, n9);
                        continue;
                    }
                    if (!StringsKt.equals((String)((String)this.rectLeftValue.get()), (String)"right", (boolean)true)) continue;
                    RenderUtils.drawRect(f20 + (float)n31 + (float)2, module.getHigt(), f20 + (float)n31 + (float)2 + 1.0f, module.getHigt() + f3, n9);
                }
                break;
            }
        }
        if (MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            this.x2 = Integer.MIN_VALUE;
            if (this.modules.isEmpty()) {
                return this.getSide().getHorizontal() == Side.Horizontal.LEFT ? new Border(0.0f, -1.0f, 20.0f, 20.0f) : new Border(0.0f, -1.0f, -20.0f, 20.0f);
            }
            for (Object object2 : this.modules) {
                switch (Arraylist$WhenMappings.$EnumSwitchMapping$1[this.getSide().getHorizontal().ordinal()]) {
                    case 1: 
                    case 2: {
                        int n37 = -((int)((Module)object2).getSlide()) - 2;
                        if (this.x2 != Integer.MIN_VALUE && n37 >= this.x2) break;
                        this.x2 = n37;
                        break;
                    }
                    case 3: {
                        int n37 = (int)((Module)object2).getSlide() + 14;
                        if (this.x2 != Integer.MIN_VALUE && n37 <= this.x2) break;
                        this.x2 = n37;
                        break;
                    }
                }
            }
            this.y2 = (this.getSide().getVertical() == Side.Vertical.DOWN ? -f5 : f5) * (float)this.modules.size();
            return new Border(0.0f, 0.0f, (float)this.x2 - 7.0f, this.y2 - (this.getSide().getVertical() == Side.Vertical.DOWN ? 1.0f : 0.0f));
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        GlStateManager.func_179117_G();
        return null;
    }

    public static final IntegerValue access$getBackgroundColorBlueValue$p(Arraylist arraylist) {
        return arraylist.backgroundColorBlueValue;
    }
}

