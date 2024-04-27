/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.gui.font.AWTFontRenderer;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.AnimationUtils;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.ColorManager;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.FontValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Arraylist", single=true)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010A\u001a\u0004\u0018\u00010BH\u0016J\u0010\u0010C\u001a\u00020D2\u0006\u0010E\u001a\u00020/H\u0002J\b\u0010F\u001a\u00020GH\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u001a\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u001d\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u001f\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001cR\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010#\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001cR\u000e\u0010%\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u00106\u001a\b\u0012\u0004\u0012\u00020/0.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020?X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006H"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Arraylist;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/dev/important/gui/client/hud/element/Side;", "(DDFLnet/dev/important/gui/client/hud/element/Side;)V", "abcOrder", "Lnet/dev/important/value/BoolValue;", "animationSpeed", "Lnet/dev/important/value/FloatValue;", "astolfoRainbowIndex", "Lnet/dev/important/value/IntegerValue;", "astolfoRainbowOffset", "backgroundColorAlphaValue", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorRedValue", "blurStrength", "blurValue", "brightnessValue", "cRainbowDistValue", "cRainbowSecValue", "colorAlphaValue", "getColorAlphaValue", "()Lnet/dev/important/value/IntegerValue;", "colorBlueValue", "getColorBlueValue", "colorGreenValue", "getColorGreenValue", "colorModeValue", "Lnet/dev/important/value/ListValue;", "colorRedValue", "getColorRedValue", "fadeDistanceValue", "fontValue", "Lnet/dev/important/value/FontValue;", "hAnimation", "liquidSlowlyDistanceValue", "lowerCaseValue", "mixerDistValue", "mixerSecValue", "modules", "", "Lnet/dev/important/modules/module/Module;", "nameBreak", "rectLeftValue", "rectRightValue", "saturationValue", "shadow", "skyDistanceValue", "sortedModules", "spaceValue", "tags", "tagsArrayColor", "tagsStyleValue", "textHeightValue", "textYValue", "vAnimation", "x2", "", "y2", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "getModName", "", "mod", "updateElement", "", "LiquidBounce"})
public final class Arraylist
extends Element {
    @NotNull
    private final ListValue colorModeValue;
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final IntegerValue colorRedValue;
    @NotNull
    private final IntegerValue colorGreenValue;
    @NotNull
    private final IntegerValue colorBlueValue;
    @NotNull
    private final IntegerValue colorAlphaValue;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue skyDistanceValue;
    @NotNull
    private final IntegerValue cRainbowSecValue;
    @NotNull
    private final IntegerValue cRainbowDistValue;
    @NotNull
    private final IntegerValue mixerSecValue;
    @NotNull
    private final IntegerValue mixerDistValue;
    @NotNull
    private final IntegerValue liquidSlowlyDistanceValue;
    @NotNull
    private final IntegerValue astolfoRainbowOffset;
    @NotNull
    private final IntegerValue astolfoRainbowIndex;
    @NotNull
    private final IntegerValue fadeDistanceValue;
    @NotNull
    private final ListValue hAnimation;
    @NotNull
    private final ListValue vAnimation;
    @NotNull
    private final FloatValue animationSpeed;
    @NotNull
    private final BoolValue nameBreak;
    @NotNull
    private final BoolValue abcOrder;
    @NotNull
    private final BoolValue tags;
    @NotNull
    private final ListValue tagsStyleValue;
    @NotNull
    private final BoolValue shadow;
    @NotNull
    private final IntegerValue backgroundColorRedValue;
    @NotNull
    private final IntegerValue backgroundColorGreenValue;
    @NotNull
    private final IntegerValue backgroundColorBlueValue;
    @NotNull
    private final IntegerValue backgroundColorAlphaValue;
    @NotNull
    private final ListValue rectRightValue;
    @NotNull
    private final ListValue rectLeftValue;
    @NotNull
    private final BoolValue lowerCaseValue;
    @NotNull
    private final FloatValue spaceValue;
    @NotNull
    private final FloatValue textHeightValue;
    @NotNull
    private final FloatValue textYValue;
    @NotNull
    private final BoolValue tagsArrayColor;
    @NotNull
    private final FontValue fontValue;
    private int x2;
    private float y2;
    @NotNull
    private List<? extends Module> modules;
    @NotNull
    private List<? extends Module> sortedModules;

    public Arraylist(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkNotNullParameter(side, "side");
        super(x, y, scale, side);
        Object object = new String[]{"Custom", "Random", "Sky", "CRainbow", "LiquidSlowly", "Fade", "Mixer", "Astolfo"};
        this.colorModeValue = new ListValue("Color", (String[])object, "Astolfo");
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        this.colorRedValue = new IntegerValue("Red", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("Green", 111, 0, 255);
        this.colorBlueValue = new IntegerValue("Blue", 255, 0, 255);
        this.colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.skyDistanceValue = new IntegerValue("Sky-Distance", 2, 0, 4);
        this.cRainbowSecValue = new IntegerValue("CRainbow-Seconds", 2, 1, 10);
        this.cRainbowDistValue = new IntegerValue("CRainbow-Distance", 2, 1, 6);
        this.mixerSecValue = new IntegerValue("Mixer-Seconds", 2, 1, 10);
        this.mixerDistValue = new IntegerValue("Mixer-Distance", 2, 0, 10);
        this.liquidSlowlyDistanceValue = new IntegerValue("LiquidSlowly-Distance", 90, 1, 90);
        this.astolfoRainbowOffset = new IntegerValue("AstolfoOffset", 5, 1, 20);
        this.astolfoRainbowIndex = new IntegerValue("AstolfoIndex", 109, 1, 300);
        this.fadeDistanceValue = new IntegerValue("Fade-Distance", 50, 1, 100);
        object = new String[]{"Default", "None", "Slide", "Astolfo"};
        this.hAnimation = new ListValue("HorizontalAnimation", (String[])object, "Astolfo");
        object = new String[]{"None", "LiquidSense", "Slide", "Rise", "Astolfo"};
        this.vAnimation = new ListValue("VerticalAnimation", (String[])object, "Rise");
        this.animationSpeed = new FloatValue("Animation-Speed", 0.25f, 0.01f, 1.0f);
        this.nameBreak = new BoolValue("EnglishArraylist", true);
        this.abcOrder = new BoolValue("Alphabetical-Order", false);
        this.tags = new BoolValue("Tags", true);
        object = new String[]{"-", "|", "()", "[]", "<>", "Default"};
        this.tagsStyleValue = new ListValue("TagsStyle", (String[])object, "Default");
        this.shadow = new BoolValue("ShadowText", true);
        this.backgroundColorRedValue = new IntegerValue("Background-R", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background-G", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background-B", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background-Alpha", 0, 0, 255);
        object = new String[]{"None", "Left", "Right", "Outline", "Special", "Top"};
        this.rectRightValue = new ListValue("Rect-Right", (String[])object, "None");
        object = new String[]{"None", "Left", "Right"};
        this.rectLeftValue = new ListValue("Rect-Left", (String[])object, "None");
        this.lowerCaseValue = new BoolValue("LowerCase", false);
        this.spaceValue = new FloatValue("Space", 0.0f, 0.0f, 5.0f);
        this.textHeightValue = new FloatValue("TextHeight", 11.0f, 1.0f, 20.0f);
        this.textYValue = new FloatValue("TextY", 1.0f, 0.0f, 20.0f);
        this.tagsArrayColor = new BoolValue("TagsArrayColor", false);
        object = Fonts.fontaa;
        Intrinsics.checkNotNullExpressionValue(object, "fontaa");
        this.fontValue = new FontValue("Font", (FontRenderer)object);
        this.modules = CollectionsKt.emptyList();
        this.sortedModules = CollectionsKt.emptyList();
    }

    public /* synthetic */ Arraylist(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
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

    @NotNull
    public final IntegerValue getColorRedValue() {
        return this.colorRedValue;
    }

    @NotNull
    public final IntegerValue getColorGreenValue() {
        return this.colorGreenValue;
    }

    @NotNull
    public final IntegerValue getColorBlueValue() {
        return this.colorBlueValue;
    }

    @NotNull
    public final IntegerValue getColorAlphaValue() {
        return this.colorAlphaValue;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        FontRenderer fontRenderer = (FontRenderer)this.fontValue.get();
        int[] nArray = new int[]{0};
        int[] counter = nArray;
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        int delta = RenderUtils.deltaTime;
        String colorMode = (String)this.colorModeValue.get();
        String rectColorMode = (String)this.colorModeValue.get();
        int customColor = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()).getRGB();
        int rectCustomColor = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()).getRGB();
        float space = ((Number)this.spaceValue.get()).floatValue();
        float textHeight = ((Number)this.textHeightValue.get()).floatValue();
        float textY = ((Number)this.textYValue.get()).floatValue();
        int backgroundCustomColor = new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()).getRGB();
        boolean textShadow = (Boolean)this.shadow.get();
        float textSpacer = textHeight + space;
        float saturation = ((Number)this.saturationValue.get()).floatValue();
        float brightness = ((Number)this.brightnessValue.get()).floatValue();
        int inx = 0;
        for (Module module2 : this.sortedModules) {
            float yPos;
            block81: {
                boolean shouldAdd = module2.getArray() && module2.getSlide() > 0.0f;
                yPos = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? inx + 1 : inx);
                if (!shouldAdd) break block81;
                if (StringsKt.equals((String)this.vAnimation.get(), "Rise", true) && !module2.getState()) {
                    yPos = (float)(-fontRenderer.field_78288_b) - textY;
                }
                float size = (float)this.modules.size() * 0.02f;
                switch ((String)this.vAnimation.get()) {
                    case "LiquidSense": {
                        if (module2.getState()) {
                            if (module2.getArrayY() < yPos) {
                                module2.setArrayY(module2.getArrayY() + (size - Math.min(module2.getArrayY() * 0.002f, size - module2.getArrayY() * 1.0E-4f)) * (float)delta);
                                module2.setArrayY(Math.min(yPos, module2.getArrayY()));
                            } else {
                                module2.setArrayY(module2.getArrayY() - (size - Math.min(module2.getArrayY() * 0.002f, size - module2.getArrayY() * 1.0E-4f)) * (float)delta);
                                module2.setArrayY(Math.max(module2.getArrayY(), yPos));
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                        break;
                    }
                    case "Slide": 
                    case "Rise": {
                        module2.setArrayY((float)net.dev.important.utils.AnimationUtils.animate((double)yPos, (double)module2.getArrayY(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                        Unit unit = Unit.INSTANCE;
                        break;
                    }
                    case "Astolfo": {
                        if (module2.getArrayY() < yPos) {
                            module2.setArrayY(module2.getArrayY() + ((Number)this.animationSpeed.get()).floatValue() / 2.0f * (float)delta);
                            module2.setArrayY(Math.min(yPos, module2.getArrayY()));
                        } else {
                            module2.setArrayY(module2.getArrayY() - ((Number)this.animationSpeed.get()).floatValue() / 2.0f * (float)delta);
                            module2.setArrayY(Math.max(module2.getArrayY(), yPos));
                        }
                        Unit unit = Unit.INSTANCE;
                        break;
                    }
                    default: {
                        module2.setArrayY(yPos);
                        Unit unit = Unit.INSTANCE;
                    }
                }
                int n = inx;
                inx = n + 1;
                continue;
            }
            if (StringsKt.equals((String)this.vAnimation.get(), "rise", true)) continue;
            module2.setArrayY(yPos);
        }
        for (Module module3 : Client.INSTANCE.getModuleManager().getModules()) {
            if (!module3.getArray() || !module3.getState() && module3.getSlide() == 0.0f) continue;
            Intrinsics.checkNotNullExpressionValue(module3, "module");
            String displayString = this.getModName(module3);
            int width = fontRenderer.func_78256_a(displayString);
            switch ((String)this.hAnimation.get()) {
                case "Astolfo": {
                    if (module3.getState()) {
                        if (module3.getSlide() < (float)width) {
                            module3.setSlide(module3.getSlide() + ((Number)this.animationSpeed.get()).floatValue() * (float)delta);
                            module3.setSlideStep((float)delta / 1.0f);
                        }
                    } else if (module3.getSlide() > 0.0f) {
                        module3.setSlide(module3.getSlide() - ((Number)this.animationSpeed.get()).floatValue() * (float)delta);
                        module3.setSlideStep(0.0f);
                    }
                    if (module3.getSlide() > (float)width) {
                        module3.setSlide(width);
                    }
                    Unit unit = Unit.INSTANCE;
                    break;
                }
                case "Slide": {
                    if (module3.getState()) {
                        if (module3.getSlide() < (float)width) {
                            module3.setSlide((float)net.dev.important.utils.AnimationUtils.animate((double)width, (double)module3.getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                            module3.setSlideStep((float)delta / 1.0f);
                        }
                    } else if (module3.getSlide() > 0.0f) {
                        module3.setSlide((float)net.dev.important.utils.AnimationUtils.animate(-((double)width), (double)module3.getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                        module3.setSlideStep(0.0f);
                    }
                    Unit unit = Unit.INSTANCE;
                    break;
                }
                case "Default": {
                    if (module3.getState()) {
                        if (module3.getSlide() < (float)width) {
                            module3.setSlide(AnimationUtils.easeOut(module3.getSlideStep(), width) * (float)width);
                            module3.setSlideStep(module3.getSlideStep() + (float)delta / 4.0f);
                        }
                    } else if (module3.getSlide() > 0.0f) {
                        module3.setSlide(AnimationUtils.easeOut(module3.getSlideStep(), width) * (float)width);
                        module3.setSlideStep(module3.getSlideStep() - (float)delta / 4.0f);
                    }
                    Unit unit = Unit.INSTANCE;
                    break;
                }
                default: {
                    module3.setSlide(module3.getState() ? (float)width : 0.0f);
                    module3.setSlideStep(module3.getSlideStep() + (float)(module3.getState() ? delta : -delta));
                    Unit unit = Unit.INSTANCE;
                }
            }
            module3.setSlide(RangesKt.coerceIn(module3.getSlide(), 0.0f, (float)width));
            module3.setSlideStep(RangesKt.coerceIn(module3.getSlideStep(), 0.0f, (float)width));
        }
        switch (WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
            case 1: 
            case 2: {
                int LiquidSlowly;
                Integer test;
                int moduleColor;
                boolean bl;
                int $i$f$forEachIndexed;
                Iterable $this$forEachIndexed$iv;
                if (((Boolean)this.blurValue.get()).booleanValue()) {
                    Module module3;
                    int index;
                    int n;
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    float floatX = (float)this.getRenderX();
                    float floatY = (float)this.getRenderY();
                    float yP = 0.0f;
                    float xP = 0.0f;
                    $this$forEachIndexed$iv = this.modules;
                    $i$f$forEachIndexed = 0;
                    int index$iv = 0;
                    for (Object item$iv : $this$forEachIndexed$iv) {
                        n = index$iv;
                        index$iv = n + 1;
                        if (n < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        Module module4 = (Module)item$iv;
                        index = n;
                        bl = false;
                        String dString = this.getModName(module3);
                        float wid = (float)fontRenderer.func_78256_a(dString) + 2.0f;
                        float yPos = this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                        yP += yPos;
                        xP = Math.min(xP, -wid);
                    }
                    BlurUtils.preCustomBlur(((Number)this.blurStrength.get()).floatValue(), floatX, floatY, floatX + xP, floatY + yP, false);
                    $this$forEachIndexed$iv = this.modules;
                    $i$f$forEachIndexed = 0;
                    int index$iv2 = 0;
                    for (Object item$iv : $this$forEachIndexed$iv) {
                        void module5;
                        n = index$iv2;
                        index$iv2 = n + 1;
                        if (n < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        module3 = (Module)item$iv;
                        index = n;
                        bl = false;
                        float xPos = -module5.getSlide() - (float)2;
                        RenderUtils.quickDrawRect(floatX + xPos - (float)(StringsKt.equals((String)this.rectRightValue.get(), "right", true) ? 3 : 2), floatY + module5.getArrayY(), floatX + (StringsKt.equals((String)this.rectRightValue.get(), "right", true) ? -1.0f : 0.0f), floatY + module5.getArrayY() + textHeight);
                    }
                    BlurUtils.postCustomBlur();
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                Iterable $this$forEachIndexed$iv2 = this.modules;
                boolean $i$f$forEachIndexed2 = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv2) {
                    void module5;
                    $i$f$forEachIndexed = index$iv;
                    index$iv = $i$f$forEachIndexed + 1;
                    if ($i$f$forEachIndexed < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    Module index$iv2 = (Module)item$iv;
                    int index = $i$f$forEachIndexed;
                    boolean bl2 = false;
                    String displayString = this.getModName((Module)module5);
                    int width = fontRenderer.func_78256_a(displayString);
                    float xPos = -module5.getSlide() - (float)2;
                    moduleColor = Color.getHSBColor(module5.getHue(), saturation, brightness).getRGB();
                    int Sky = 0;
                    Sky = RenderUtils.SkyRainbow(counter[0] * (((Number)this.skyDistanceValue.get()).intValue() * 50), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    int CRainbow = 0;
                    CRainbow = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), counter[0] * (50 * ((Number)this.cRainbowDistValue.get()).intValue()));
                    int FadeColor = ColorUtils.fade(new Color(((Number)this.getColorRedValue().get()).intValue(), ((Number)this.getColorGreenValue().get()).intValue(), ((Number)this.getColorBlueValue().get()).intValue(), ((Number)this.getColorAlphaValue().get()).intValue()), index * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    counter[0] = counter[0] - 1;
                    Color color = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.liquidSlowlyDistanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    Integer n = test = color == null ? null : Integer.valueOf(color.getRGB());
                    Intrinsics.checkNotNull(n);
                    LiquidSlowly = n;
                    int Astolfo = ColorManager.astolfoRainbow(counter[0] * 100, ((Number)this.astolfoRainbowOffset.get()).intValue(), ((Number)this.astolfoRainbowIndex.get()).intValue());
                    int mixerColor = ColorMixer.getMixedColor(-index * ((Number)this.mixerDistValue.get()).intValue() * 10, ((Number)this.mixerSecValue.get()).intValue()).getRGB();
                    RenderUtils.drawRect(xPos - (float)(StringsKt.equals((String)this.rectRightValue.get(), "right", true) ? 3 : 2), module5.getArrayY(), StringsKt.equals((String)this.rectRightValue.get(), "right", true) ? -1.0f : 0.0f, module5.getArrayY() + textHeight, backgroundCustomColor);
                    fontRenderer.func_175065_a(displayString, xPos - (float)(StringsKt.equals((String)this.rectRightValue.get(), "right", true) ? 1 : 0), module5.getArrayY() + textY, StringsKt.equals(colorMode, "Random", true) ? moduleColor : (StringsKt.equals(colorMode, "Sky", true) ? Sky : (StringsKt.equals(colorMode, "CRainbow", true) ? CRainbow : (StringsKt.equals(colorMode, "LiquidSlowly", true) ? LiquidSlowly : (StringsKt.equals(colorMode, "Fade", true) ? FadeColor : (StringsKt.equals(colorMode, "Mixer", true) ? mixerColor : (StringsKt.equals(colorMode, "Astolfo", true) ? Astolfo : customColor)))))), textShadow);
                    if (StringsKt.equals((String)this.rectRightValue.get(), "none", true)) continue;
                    int rectColor = StringsKt.equals(rectColorMode, "Random", true) ? moduleColor : (StringsKt.equals(rectColorMode, "Sky", true) ? Sky : (StringsKt.equals(rectColorMode, "CRainbow", true) ? CRainbow : (StringsKt.equals(rectColorMode, "LiquidSlowly", true) ? LiquidSlowly : (StringsKt.equals(rectColorMode, "Fade", true) ? FadeColor : (StringsKt.equals(rectColorMode, "Mixer", true) ? mixerColor : (StringsKt.equals(rectColorMode, "Astolfo", true) ? Astolfo : rectCustomColor))))));
                    if (StringsKt.equals((String)this.rectRightValue.get(), "left", true)) {
                        RenderUtils.drawRect(xPos - (float)3, module5.getArrayY(), xPos - (float)2, module5.getArrayY() + textHeight, rectColor);
                        continue;
                    }
                    if (StringsKt.equals((String)this.rectRightValue.get(), "right", true)) {
                        RenderUtils.drawRect(-1.0f, module5.getArrayY(), 0.0f, module5.getArrayY() + textHeight, rectColor);
                        continue;
                    }
                    if (StringsKt.equals((String)this.rectRightValue.get(), "outline", true)) {
                        RenderUtils.drawRect(-1.0f, module5.getArrayY() - 1.0f, 0.0f, module5.getArrayY() + textHeight, rectColor);
                        RenderUtils.drawRect(xPos - (float)3, module5.getArrayY(), xPos - (float)2, module5.getArrayY() + textHeight, rectColor);
                        if (!Intrinsics.areEqual(module5, this.modules.get(0))) {
                            String displayStrings = this.getModName(this.modules.get(index - 1));
                            RenderUtils.drawRect(xPos - (float)3 - (float)(fontRenderer.func_78256_a(displayStrings) - fontRenderer.func_78256_a(displayString)), module5.getArrayY(), xPos - (float)2, module5.getArrayY() + 1.0f, rectColor);
                            if (!Intrinsics.areEqual(module5, this.modules.get(this.modules.size() - 1))) continue;
                            RenderUtils.drawRect(xPos - (float)3, module5.getArrayY() + textHeight, 0.0f, module5.getArrayY() + textHeight + 1.0f, rectColor);
                            continue;
                        }
                        RenderUtils.drawRect(xPos - (float)3, module5.getArrayY(), 0.0f, module5.getArrayY() - 1.0f, rectColor);
                        continue;
                    }
                    if (StringsKt.equals((String)this.rectRightValue.get(), "special", true)) {
                        if (Intrinsics.areEqual(module5, this.modules.get(0))) {
                            RenderUtils.drawRect(xPos - (float)2, module5.getArrayY(), 0.0f, module5.getArrayY() - 1.0f, rectColor);
                        }
                        if (!Intrinsics.areEqual(module5, this.modules.get(this.modules.size() - 1))) continue;
                        RenderUtils.drawRect(xPos - (float)2, module5.getArrayY() + textHeight, 0.0f, module5.getArrayY() + textHeight + 1.0f, rectColor);
                        continue;
                    }
                    if (!StringsKt.equals((String)this.rectRightValue.get(), "top", true) || !Intrinsics.areEqual(module5, this.modules.get(0))) continue;
                    RenderUtils.drawRect(xPos - (float)2, module5.getArrayY(), 0.0f, module5.getArrayY() - 1.0f, rectColor);
                }
                break;
            }
            case 3: {
                int LiquidSlowly;
                Integer test;
                int moduleColor;
                boolean bl;
                int $i$f$forEachIndexed;
                Iterable $this$forEachIndexed$iv;
                if (((Boolean)this.blurValue.get()).booleanValue()) {
                    Module module6;
                    int index;
                    int displayString;
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    float floatX = (float)this.getRenderX();
                    float floatY = (float)this.getRenderY();
                    float yP = 0.0f;
                    float xP = 0.0f;
                    $this$forEachIndexed$iv = this.modules;
                    $i$f$forEachIndexed = 0;
                    int index$iv = 0;
                    for (Object item$iv : $this$forEachIndexed$iv) {
                        displayString = index$iv;
                        index$iv = displayString + 1;
                        if (displayString < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        Module width = (Module)item$iv;
                        index = displayString;
                        bl = false;
                        String dString = this.getModName(module6);
                        float wid = (float)fontRenderer.func_78256_a(dString) + 2.0f;
                        float yPos = this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                        yP += yPos;
                        xP = Math.max(xP, wid);
                    }
                    BlurUtils.preCustomBlur(((Number)this.blurStrength.get()).floatValue(), floatX, floatY, floatX + xP, floatY + yP, false);
                    $this$forEachIndexed$iv = this.modules;
                    $i$f$forEachIndexed = 0;
                    int index$iv3 = 0;
                    for (Object item$iv : $this$forEachIndexed$iv) {
                        void module7;
                        displayString = index$iv3;
                        index$iv3 = displayString + 1;
                        if (displayString < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        module6 = (Module)item$iv;
                        index = displayString;
                        bl = false;
                        String displayString2 = this.getModName((Module)module7);
                        int width = fontRenderer.func_78256_a(displayString2);
                        float xPos = -((float)width - module7.getSlide()) + (float)(StringsKt.equals((String)this.rectLeftValue.get(), "left", true) ? 3 : 2);
                        RenderUtils.quickDrawRect(floatX, floatY + module7.getArrayY(), floatX + xPos + (float)width + (float)(StringsKt.equals((String)this.rectLeftValue.get(), "right", true) ? 3 : 2), floatY + module7.getArrayY() + textHeight);
                    }
                    BlurUtils.postCustomBlur();
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                Iterable $this$forEachIndexed$iv3 = this.modules;
                boolean $i$f$forEachIndexed3 = false;
                int index$iv = 0;
                for (Object item$iv : $this$forEachIndexed$iv3) {
                    void module7;
                    int n = index$iv;
                    index$iv = n + 1;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    Module index$iv3 = (Module)item$iv;
                    int index = n;
                    boolean bl3 = false;
                    String displayString = this.getModName((Module)module7);
                    int width = fontRenderer.func_78256_a(displayString);
                    float xPos = -((float)width - module7.getSlide()) + (float)(StringsKt.equals((String)this.rectLeftValue.get(), "left", true) ? 3 : 2);
                    moduleColor = Color.getHSBColor(module7.getHue(), saturation, brightness).getRGB();
                    int Sky = 0;
                    Sky = RenderUtils.SkyRainbow(counter[0] * (((Number)this.skyDistanceValue.get()).intValue() * 50), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    int CRainbow = 0;
                    CRainbow = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), counter[0] * (50 * ((Number)this.cRainbowDistValue.get()).intValue()));
                    int FadeColor = ColorUtils.fade(new Color(((Number)this.getColorRedValue().get()).intValue(), ((Number)this.getColorGreenValue().get()).intValue(), ((Number)this.getColorBlueValue().get()).intValue(), ((Number)this.getColorAlphaValue().get()).intValue()), index * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    counter[0] = counter[0] - 1;
                    Color color = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.liquidSlowlyDistanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    Integer n2 = test = color == null ? null : Integer.valueOf(color.getRGB());
                    Intrinsics.checkNotNull(n2);
                    LiquidSlowly = n2;
                    int mixerColor = ColorMixer.getMixedColor(-index * ((Number)this.mixerDistValue.get()).intValue() * 10, ((Number)this.mixerSecValue.get()).intValue()).getRGB();
                    RenderUtils.drawRect(0.0f, module7.getArrayY(), xPos + (float)width + (float)(StringsKt.equals((String)this.rectLeftValue.get(), "right", true) ? 3 : 2), module7.getArrayY() + textHeight, backgroundCustomColor);
                    fontRenderer.func_175065_a(displayString, xPos, module7.getArrayY() + textY, StringsKt.equals(colorMode, "Random", true) ? moduleColor : (StringsKt.equals(colorMode, "Sky", true) ? Sky : (StringsKt.equals(colorMode, "CRainbow", true) ? CRainbow : (StringsKt.equals(colorMode, "LiquidSlowly", true) ? LiquidSlowly : (StringsKt.equals(colorMode, "Fade", true) ? FadeColor : (StringsKt.equals(colorMode, "Mixer", true) ? mixerColor : customColor))))), textShadow);
                    if (StringsKt.equals((String)this.rectLeftValue.get(), "none", true)) continue;
                    int rectColor = StringsKt.equals(rectColorMode, "Random", true) ? moduleColor : (StringsKt.equals(rectColorMode, "Sky", true) ? Sky : (StringsKt.equals(rectColorMode, "CRainbow", true) ? CRainbow : (StringsKt.equals(rectColorMode, "LiquidSlowly", true) ? LiquidSlowly : (StringsKt.equals(rectColorMode, "Fade", true) ? FadeColor : (StringsKt.equals(rectColorMode, "Mixer", true) ? mixerColor : rectCustomColor)))));
                    if (StringsKt.equals((String)this.rectLeftValue.get(), "left", true)) {
                        RenderUtils.drawRect(0.0f, module7.getArrayY() - 1.0f, 1.0f, module7.getArrayY() + textHeight, rectColor);
                        continue;
                    }
                    if (!StringsKt.equals((String)this.rectLeftValue.get(), "right", true)) continue;
                    RenderUtils.drawRect(xPos + (float)width + (float)2, module7.getArrayY(), xPos + (float)width + (float)2 + 1.0f, module7.getArrayY() + textHeight, rectColor);
                }
                break;
            }
        }
        if (MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner) {
            this.x2 = Integer.MIN_VALUE;
            if (this.modules.isEmpty()) {
                return this.getSide().getHorizontal() == Side.Horizontal.LEFT ? new Border(0.0f, -1.0f, 20.0f, 20.0f) : new Border(0.0f, -1.0f, -20.0f, 20.0f);
            }
            for (Module module8 : this.modules) {
                switch (WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
                    case 1: 
                    case 2: {
                        int xPos = -((int)module8.getSlide()) - 2;
                        if (this.x2 != Integer.MIN_VALUE && xPos >= this.x2) break;
                        this.x2 = xPos;
                        break;
                    }
                    case 3: {
                        int xPos = (int)module8.getSlide() + 14;
                        if (this.x2 != Integer.MIN_VALUE && xPos <= this.x2) break;
                        this.x2 = xPos;
                    }
                }
            }
            this.y2 = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)this.modules.size();
            return new Border(0.0f, 0.0f, (float)this.x2 - 7.0f, this.y2 - (this.getSide().getVertical() == Side.Vertical.DOWN ? 1.0f : 0.0f));
        }
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        GlStateManager.func_179117_G();
        return null;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void updateElement() {
        List list;
        boolean $i$f$sortedBy;
        Iterable $this$sortedBy$iv;
        Arraylist arraylist;
        List list2;
        Iterable $this$filter$iv;
        boolean $i$f$filter;
        Arraylist arraylist2 = this;
        if (((Boolean)this.abcOrder.get()).booleanValue()) {
            void $this$filterTo$iv$iv;
            Iterable iterable = Client.INSTANCE.getModuleManager().getModules();
            Arraylist arraylist3 = arraylist2;
            $i$f$filter = false;
            Iterable iterable2 = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                Module it = (Module)element$iv$iv;
                boolean bl = false;
                if (!(it.getArray() && (StringsKt.equals((String)this.hAnimation.get(), "none", true) ? it.getState() : it.getSlide() > 0.0f))) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            list2 = (List)destination$iv$iv;
            arraylist = arraylist3;
        } else {
            $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
            Arraylist arraylist4 = arraylist2;
            $i$f$filter = false;
            Iterable $this$filterTo$iv$iv = $this$filter$iv;
            Collection destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                Module it = (Module)element$iv$iv;
                boolean bl = false;
                if (!(it.getArray() && (StringsKt.equals((String)this.hAnimation.get(), "none", true) ? it.getState() : it.getSlide() > 0.0f))) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            arraylist = arraylist4;
            $this$sortedBy$iv = (List)destination$iv$iv;
            $i$f$sortedBy = false;
            list2 = arraylist.modules = CollectionsKt.sortedWith($this$sortedBy$iv, new Comparator(this){
                final /* synthetic */ Arraylist this$0;
                {
                    this.this$0 = arraylist;
                }

                public final int compare(T a, T b) {
                    Module it = (Module)a;
                    boolean bl = false;
                    Comparable comparable = Integer.valueOf(-((FontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).func_78256_a(Arraylist.access$getModName(this.this$0, it)));
                    it = (Module)b;
                    Comparable comparable2 = comparable;
                    bl = false;
                    return ComparisonsKt.compareValues(comparable2, -((FontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).func_78256_a(Arraylist.access$getModName(this.this$0, it)));
                }
            });
        }
        if (((Boolean)this.abcOrder.get()).booleanValue()) {
            list = CollectionsKt.toList((Iterable)Client.INSTANCE.getModuleManager().getModules());
        } else {
            $this$sortedBy$iv = Client.INSTANCE.getModuleManager().getModules();
            $i$f$sortedBy = false;
            list = CollectionsKt.toList(CollectionsKt.sortedWith($this$sortedBy$iv, new Comparator(this){
                final /* synthetic */ Arraylist this$0;
                {
                    this.this$0 = arraylist;
                }

                public final int compare(T a, T b) {
                    Module it = (Module)a;
                    boolean bl = false;
                    Comparable comparable = Integer.valueOf(-((FontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).func_78256_a(Arraylist.access$getModName(this.this$0, it)));
                    it = (Module)b;
                    Comparable comparable2 = comparable;
                    bl = false;
                    return ComparisonsKt.compareValues(comparable2, -((FontRenderer)Arraylist.access$getFontValue$p(this.this$0).get()).func_78256_a(Arraylist.access$getModName(this.this$0, it)));
                }
            }));
        }
        this.sortedModules = list;
    }

    private final String getModName(Module mod) {
        String modTag = "";
        if (((Boolean)this.tags.get()).booleanValue() && mod.getTag() != null) {
            modTag = Intrinsics.stringPlus(modTag, " ");
            if (!((Boolean)this.tagsArrayColor.get()).booleanValue()) {
                modTag = Intrinsics.stringPlus(modTag, "\u00a77");
            }
            if (!StringsKt.equals((String)this.tagsStyleValue.get(), "default", true)) {
                modTag = modTag + ((String)this.tagsStyleValue.get()).charAt(0) + (StringsKt.equals((String)this.tagsStyleValue.get(), "-", true) || StringsKt.equals((String)this.tagsStyleValue.get(), "|", true) ? " " : "");
            }
            modTag = Intrinsics.stringPlus(modTag, mod.getTag());
            if (!(StringsKt.equals((String)this.tagsStyleValue.get(), "default", true) || StringsKt.equals((String)this.tagsStyleValue.get(), "-", true) || StringsKt.equals((String)this.tagsStyleValue.get(), "|", true))) {
                modTag = Intrinsics.stringPlus(modTag, Character.valueOf(((String)this.tagsStyleValue.get()).charAt(1)));
            }
        }
        String displayName = Intrinsics.stringPlus((Boolean)this.nameBreak.get() != false ? mod.getSpacedName() : mod.getCnName(), modTag);
        if (((Boolean)this.lowerCaseValue.get()).booleanValue()) {
            String string = displayName.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            displayName = string;
        }
        return displayName;
    }

    public Arraylist() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ FontValue access$getFontValue$p(Arraylist $this) {
        return $this.fontValue;
    }

    public static final /* synthetic */ String access$getModName(Arraylist $this, Module mod) {
        return $this.getModName(mod);
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[Side.Horizontal.values().length];
            nArray[Side.Horizontal.RIGHT.ordinal()] = 1;
            nArray[Side.Horizontal.MIDDLE.ordinal()] = 2;
            nArray[Side.Horizontal.LEFT.ordinal()] = 3;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

