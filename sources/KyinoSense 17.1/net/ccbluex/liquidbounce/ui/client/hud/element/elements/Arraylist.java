/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import me.report.liquidware.modules.render.ColorMixer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Arraylist$WhenMappings;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.AnimationUtilsArrayList;
import net.ccbluex.liquidbounce.utils.ColorManager;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.VisualUtils;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUti;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.Palette;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.ShadowUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="Arraylist", single=true)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b#\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\n\u0010Z\u001a\u0004\u0018\u00010[H\u0016J\u0010\u0010\\\u001a\u00020]2\u0006\u0010^\u001a\u000204H\u0002J\u0010\u0010_\u001a\u00020]2\u0006\u0010^\u001a\u000204H\u0002J\u0010\u0010`\u001a\u00020]2\u0006\u0010^\u001a\u000204H\u0002J\b\u0010a\u001a\u00020bH\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010 \u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u000e\u0010#\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u00102\u001a\b\u0012\u0004\u0012\u00020403X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020XX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006c"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Arraylist;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "x", "", "y", "scale", "", "side", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Side;", "(DDFLnet/ccbluex/liquidbounce/ui/client/hud/element/Side;)V", "Rianbowb", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "Rianbowb2", "RianbowbValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "Rianbowg", "Rianbowg2", "Rianbowr", "Rianbowr2", "RianbowspeedValue", "animationSpeed", "backgroundColorAlphaValue", "backgroundColorBlueValue", "backgroundColorGreenValue", "backgroundColorModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "backgroundColorRedValue", "backgroundExpand", "blurStrength", "blurValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "brightnessValue", "colorAlphaValue", "getColorAlphaValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorBlueValue", "colorBlueValue2", "colorGreenValue", "colorGreenValue2", "colorModeValue", "colorRedValue", "colorRedValue2", "fadeDistanceValue", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "gidentspeed", "hAnimation", "liquidSlowlyDistanceValue", "mixerDistValue", "mixerSecValue", "modules", "", "Lnet/ccbluex/liquidbounce/features/module/Module;", "nameBreakValue", "noOtherModules", "noPlayerModules", "noRenderModules", "rainbowSaturation", "rainbowSpeeds", "rainbowX", "rainbowY", "rectColorAlphaValue", "rectColorBlueAlpha", "rectColorBlueValue", "rectColorGreenValue", "rectColorModeValue", "rectColorRedValue", "rectValue", "rleftall", "rleftright", "rlefttop", "saturationValue", "shadow", "shadowColorBlueValue", "shadowColorGreenValue", "shadowColorMode", "shadowColorRedValue", "shadowNoCutValue", "shadowShaderValue", "shadowStrength", "spaceValue", "tags", "tagsArrayColor", "textHeightValue", "textYValue", "upperCaseValue", "vAnimation", "x2", "", "y2", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "shouldExpect", "", "module", "shouldExpect2", "shouldExpect3", "updateElement", "", "KyinoClient"})
public final class Arraylist
extends Element {
    private final ListValue rectValue;
    private final ListValue colorModeValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    @NotNull
    private final IntegerValue colorAlphaValue;
    private final IntegerValue colorRedValue2;
    private final IntegerValue colorGreenValue2;
    private final IntegerValue colorBlueValue2;
    private final ListValue rectColorModeValue;
    private final IntegerValue rectColorRedValue;
    private final IntegerValue rectColorGreenValue;
    private final IntegerValue rectColorBlueValue;
    private final IntegerValue rectColorBlueAlpha;
    private final IntegerValue rectColorAlphaValue;
    private final ListValue backgroundColorModeValue;
    private final FloatValue rainbowX;
    private final FloatValue rainbowY;
    private final IntegerValue Rianbowr;
    private final IntegerValue Rianbowb;
    private final IntegerValue Rianbowg;
    private final IntegerValue Rianbowr2;
    private final IntegerValue Rianbowb2;
    private final IntegerValue Rianbowg2;
    private final IntegerValue backgroundColorRedValue;
    private final IntegerValue backgroundColorGreenValue;
    private final IntegerValue backgroundColorBlueValue;
    private final IntegerValue backgroundColorAlphaValue;
    private final IntegerValue backgroundExpand;
    private final BoolValue blurValue;
    private final FloatValue blurStrength;
    private final BoolValue shadowShaderValue;
    private final BoolValue shadowNoCutValue;
    private final IntegerValue shadowStrength;
    private final ListValue shadowColorMode;
    private final IntegerValue shadowColorRedValue;
    private final IntegerValue shadowColorGreenValue;
    private final IntegerValue shadowColorBlueValue;
    private final IntegerValue fadeDistanceValue;
    private final FloatValue rainbowSaturation;
    private final IntegerValue mixerSecValue;
    private final IntegerValue mixerDistValue;
    private final FloatValue textYValue;
    private final BoolValue tagsArrayColor;
    private final IntegerValue gidentspeed;
    private final BoolValue noRenderModules;
    private final BoolValue noOtherModules;
    private final BoolValue noPlayerModules;
    private final FloatValue animationSpeed;
    private final ListValue hAnimation;
    private final ListValue vAnimation;
    private final IntegerValue RianbowspeedValue;
    private final FloatValue RianbowbValue;
    private final BoolValue rlefttop;
    private final BoolValue rleftright;
    private final BoolValue rleftall;
    private final FloatValue saturationValue;
    private final FloatValue brightnessValue;
    private final BoolValue upperCaseValue;
    private final FloatValue spaceValue;
    private final FloatValue textHeightValue;
    private final BoolValue tags;
    private final BoolValue shadow;
    private final BoolValue nameBreakValue;
    private final IntegerValue rainbowSpeeds;
    private final IntegerValue liquidSlowlyDistanceValue;
    private final FontValue fontValue;
    private int x2;
    private float y2;
    private List<? extends Module> modules;

    @NotNull
    public final IntegerValue getColorAlphaValue() {
        return this.colorAlphaValue;
    }

    private final boolean shouldExpect(Module module) {
        return (Boolean)this.noRenderModules.get() != false && module.getCategory() == ModuleCategory.RENDER;
    }

    private final boolean shouldExpect2(Module module) {
        return (Boolean)this.noOtherModules.get() != false && module.getCategory() == ModuleCategory.FUN;
    }

    private final boolean shouldExpect3(Module module) {
        return (Boolean)this.noPlayerModules.get() != false && module.getCategory() == ModuleCategory.PLAYER;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        int[] counter = new int[]{0};
        boolean cou = false;
        String colorMode = (String)this.colorModeValue.get();
        String rectColorMode = (String)this.rectColorModeValue.get();
        String backgroundColorMode = (String)this.backgroundColorModeValue.get();
        int customColor = new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1).getRGB();
        int rectCustomColor = new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorBlueAlpha.get()).intValue()).getRGB();
        float space = ((Number)this.spaceValue.get()).floatValue();
        float textHeight = ((Number)this.textHeightValue.get()).floatValue();
        float textY = ((Number)this.textYValue.get()).floatValue();
        String rectMode = (String)this.rectValue.get();
        int backgroundCustomColor = new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()).getRGB();
        int alpha = new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorRedValue.get()).intValue(), 150).getRGB();
        boolean textShadow = (Boolean)this.shadow.get();
        float textSpacer = textHeight + space;
        float Rsaturation = ((Number)this.RianbowbValue.get()).floatValue();
        int liquidSlowlyDistanceValue = ((Number)this.liquidSlowlyDistanceValue.get()).intValue();
        float Rbrightness = 1.0f;
        float saturation = ((Number)this.saturationValue.get()).floatValue();
        float brightness = ((Number)this.brightnessValue.get()).floatValue();
        int rainbowSpeed = 5;
        FontRenderer fontRenderer = (FontRenderer)this.fontValue.get();
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        int delta = RenderUtils.deltaTime;
        int inx = 0;
        for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
            if (module.getArray() && (module.getState() || module.getSlide() != 0.0f)) {
                String displayString;
                String string = (Boolean)this.tags.get() == false ? module.getBreakName((Boolean)this.nameBreakValue.get()) : (displayString = (Boolean)this.tagsArrayColor.get() != false ? module.colorlessTagName((Boolean)this.nameBreakValue.get()) : module.tagName((Boolean)this.nameBreakValue.get()));
                if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                    String string2 = displayString;
                    boolean bl = false;
                    String string3 = string2;
                    if (string3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string4 = string3.toUpperCase();
                    Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toUpperCase()");
                    displayString = string4;
                }
                int width = fontRenderer.func_78256_a(displayString);
                switch ((String)this.hAnimation.get()) {
                    case "Astolfo": {
                        if (module.getState()) {
                            if (!(module.getSlide() < (float)width)) break;
                            Module module2 = module;
                            module2.setSlide(module2.getSlide() + ((Number)this.animationSpeed.get()).floatValue() * (float)delta);
                            module.setSlideStep((float)delta / 0.6f);
                            break;
                        }
                        if (!(module.getSlide() > 0.0f)) break;
                        Module module3 = module;
                        module3.setSlide(module3.getSlide() - ((Number)this.animationSpeed.get()).floatValue() * (float)delta);
                        module.setSlideStep((float)delta / 0.6f);
                        break;
                    }
                    case "Slide": {
                        if (module.getState()) {
                            if (!(module.getSlide() < (float)width)) break;
                            module.setSlide((float)AnimationUtilsArrayList.animate((double)width, (double)module.getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                            module.setSlideStep((float)delta / 1.0f);
                            break;
                        }
                        if (!(module.getSlide() > 0.0f)) break;
                        module.setSlide((float)AnimationUtilsArrayList.animate(-((double)width), (double)module.getSlide(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                        module.setSlideStep(0.0f);
                        break;
                    }
                    case "Default": {
                        if (module.getState()) {
                            if (!(module.getSlide() < (float)width)) break;
                            module.setSlide(AnimationUtils.easeOut(module.getSlideStep(), width) * (float)width);
                            Module module4 = module;
                            module4.setSlideStep(module4.getSlideStep() + (float)delta / 4.0f);
                            break;
                        }
                        if (!(module.getSlide() > 0.0f)) break;
                        module.setSlide(AnimationUtils.easeOut(module.getSlideStep(), width) * (float)width);
                        Module module5 = module;
                        module5.setSlideStep(module5.getSlideStep() - (float)delta / 4.0f);
                        break;
                    }
                    default: {
                        module.setSlide(module.getState() ? (float)width : 0.0f);
                        Module module6 = module;
                        module6.setSlideStep(module6.getSlideStep() + (float)(module.getState() ? delta : -delta));
                    }
                }
                module.setSlide(RangesKt.coerceIn(module.getSlide(), 0.0f, (float)width));
                module.setSlideStep(RangesKt.coerceIn(module.getSlideStep(), 0.0f, (float)width));
            }
            float yPos = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? inx + 1 : inx);
            if (module.getArray() && module.getSlide() > 0.0f) {
                if (StringsKt.equals((String)this.vAnimation.get(), "Rise", true) && !module.getState()) {
                    yPos = (float)(-fontRenderer.field_78288_b) - textY;
                }
                float size = (float)this.modules.size() * 0.02f;
                switch ((String)this.vAnimation.get()) {
                    case "LiquidSense": {
                        if (!module.getState()) break;
                        if (module.getArrayY() < yPos) {
                            Module module7 = module;
                            module7.setArrayY(module7.getArrayY() + (size - Math.min(module.getArrayY() * 0.002f, size - module.getArrayY() * 1.0E-4f)) * (float)delta);
                            module.setArrayY(Math.min(yPos, module.getArrayY()));
                            break;
                        }
                        Module module8 = module;
                        module8.setArrayY(module8.getArrayY() - (size - Math.min(module.getArrayY() * 0.002f, size - module.getArrayY() * 1.0E-4f)) * (float)delta);
                        module.setArrayY(Math.max(module.getArrayY(), yPos));
                        break;
                    }
                    case "Slide": 
                    case "Rise": {
                        module.setArrayY((float)AnimationUtilsArrayList.animate((double)yPos, (double)module.getArrayY(), (double)((Number)this.animationSpeed.get()).floatValue() * 0.025 * (double)delta));
                        break;
                    }
                    case "Astolfo": {
                        if (module.getArrayY() < yPos) {
                            Module module9 = module;
                            module9.setArrayY(module9.getArrayY() + ((Number)this.animationSpeed.get()).floatValue() / 2.2f * (float)delta);
                            module.setArrayY(Math.min(yPos, module.getArrayY()));
                            break;
                        }
                        Module module10 = module;
                        module10.setArrayY(module10.getArrayY() - ((Number)this.animationSpeed.get()).floatValue() / 2.2f * (float)delta);
                        module.setArrayY(Math.max(module.getArrayY(), yPos));
                        break;
                    }
                    default: {
                        module.setArrayY(yPos);
                    }
                }
                ++inx;
                continue;
            }
            if (StringsKt.equals((String)this.vAnimation.get(), "rise", true)) continue;
            module.setArrayY(yPos);
        }
        switch (Arraylist$WhenMappings.$EnumSwitchMapping$0[this.getSide().getHorizontal().ordinal()]) {
            case 1: 
            case 2: {
                int braibow;
                int braibow2;
                Color col;
                int c;
                void module;
                boolean bl;
                int index;
                boolean bl2;
                int n;
                if (((Boolean)this.shadowShaderValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    ShadowUtils.INSTANCE.shadow(((Number)this.shadowStrength.get()).intValue(), new Function0<Unit>(this, fontRenderer, textSpacer, counter, Rsaturation, Rbrightness, textHeight, saturation, brightness, liquidSlowlyDistanceValue, colorMode, rainbowSpeed, customColor){
                        final /* synthetic */ Arraylist this$0;
                        final /* synthetic */ FontRenderer $fontRenderer;
                        final /* synthetic */ float $textSpacer;
                        final /* synthetic */ int[] $counter;
                        final /* synthetic */ float $Rsaturation;
                        final /* synthetic */ float $Rbrightness;
                        final /* synthetic */ float $textHeight;
                        final /* synthetic */ float $saturation;
                        final /* synthetic */ float $brightness;
                        final /* synthetic */ int $liquidSlowlyDistanceValue;
                        final /* synthetic */ String $colorMode;
                        final /* synthetic */ int $rainbowSpeed;
                        final /* synthetic */ int $customColor;

                        /*
                         * WARNING - void declaration
                         */
                        public final void invoke() {
                            GL11.glPushMatrix();
                            GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                            Iterable $this$forEachIndexed$iv = Arraylist.access$getModules$p(this.this$0);
                            boolean $i$f$forEachIndexed = false;
                            int index$iv = 0;
                            for (T item$iv : $this$forEachIndexed$iv) {
                                int n;
                                String string;
                                int LiquidSlowly;
                                void module;
                                int n2 = index$iv++;
                                boolean bl = false;
                                if (n2 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n3 = n2;
                                Module module2 = (Module)item$iv;
                                int index = n3;
                                boolean bl2 = false;
                                float xPos = -module.getSlide() - (float)2;
                                int color3 = 0;
                                Color color = VisualUtils.getGradientOffset(new Color(((Number)Arraylist.access$getColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue$p(this.this$0).get()).intValue(), 1), new Color(((Number)Arraylist.access$getColorRedValue2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue2$p(this.this$0).get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)Arraylist.access$getGidentspeed$p(this.this$0).get()).intValue() + (double)(module.getTranslate().getY() / (float)this.$fontRenderer.field_78288_b)) / ((Number)Arraylist.access$getRianbowspeedValue$p(this.this$0).get()).doubleValue());
                                Intrinsics.checkExpressionValueIsNotNull(color, "VisualUtils.getGradientO\u2026RianbowspeedValue.get()))");
                                color3 = color.getRGB();
                                float yPos = (this.this$0.getSide().getVertical() == Side.Vertical.DOWN ? -this.$textSpacer : this.$textSpacer) * (float)(this.this$0.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                                int Astolfo = 0;
                                Astolfo = RenderUtils.Astolfo(this.$counter[0] * 100);
                                int FadeColor = ColorUtils.fade(new Color(((Number)Arraylist.access$getColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue$p(this.this$0).get()).intValue(), ((Number)this.this$0.getColorAlphaValue().get()).intValue()), index * ((Number)Arraylist.access$getFadeDistanceValue$p(this.this$0).get()).intValue(), 100).getRGB();
                                int Sky = 0;
                                Sky = RenderUtils.SkyRainbow(this.$counter[0] * 100, ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)Arraylist.access$getRianbowspeedValue$p(this.this$0).get()).intValue(), this.$Rsaturation, this.$Rbrightness);
                                int c = LiquidSlowly = (color2 != null ? Integer.valueOf(color2.getRGB()) : null).intValue();
                                Color col = new Color(c);
                                int braibow2 = new Color(((Number)Arraylist.access$getRianbowr$p(this.this$0).get()).intValue(), col.getGreen() / 2 + ((Number)Arraylist.access$getRianbowb$p(this.this$0).get()).intValue(), col.getGreen() / 2 + ((Number)Arraylist.access$getRianbowb$p(this.this$0).get()).intValue() + ((Number)Arraylist.access$getRianbowg$p(this.this$0).get()).intValue()).getRGB();
                                int braibow = new Color(col.getRed() / 2 + ((Number)Arraylist.access$getRianbowr2$p(this.this$0).get()).intValue(), col.getGreen() / 2 + ((Number)Arraylist.access$getRianbowg2$p(this.this$0).get()).intValue(), col.getBlue() / 2 + ((Number)Arraylist.access$getRianbowb2$p(this.this$0).get()).intValue()).getRGB();
                                String string2 = (String)Arraylist.access$getShadowColorMode$p(this.this$0).get();
                                float f = module.getArrayY() + this.$textHeight;
                                float f2 = StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? -1.0f : 0.0f;
                                float f3 = module.getArrayY();
                                float f4 = xPos - (float)(StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? 3 : 2);
                                boolean bl3 = false;
                                String string3 = string2;
                                if (string3 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                Intrinsics.checkExpressionValueIsNotNull(string3.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                                switch (string) {
                                    case "background": {
                                        n = new Color(((Number)Arraylist.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                        break;
                                    }
                                    case "text": {
                                        int test;
                                        Color color4 = Color.getHSBColor(module.getHue(), this.$saturation, this.$brightness);
                                        Intrinsics.checkExpressionValueIsNotNull(color4, "Color.getHSBColor(module\u2026, saturation, brightness)");
                                        int moduleColor = color4.getRGB();
                                        this.$counter[0] = this.$counter[0] - 1;
                                        Color color5 = ColorUtils.LiquidSlowly(System.nanoTime(), index * this.$liquidSlowlyDistanceValue, this.$saturation, this.$brightness);
                                        int LiquidSlowlys = test = (color5 != null ? Integer.valueOf(color5.getRGB()) : null).intValue();
                                        Color color6 = ColorMixer.getMixedColor(-index * ((Number)Arraylist.access$getMixerDistValue$p(this.this$0).get()).intValue() * 10, ((Number)Arraylist.access$getMixerSecValue$p(this.this$0).get()).intValue());
                                        Intrinsics.checkExpressionValueIsNotNull(color6, "ColorMixer.getMixedColor\u2026 10, mixerSecValue.get())");
                                        int mixerColor = color6.getRGB();
                                        if (StringsKt.equals(this.$colorMode, "Rainbow2", true)) {
                                            n = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Random", true)) {
                                            n = moduleColor;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "ARGB", true)) {
                                            n = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "ARGB2", true)) {
                                            n = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)Arraylist.access$getRainbowSaturation$p(this.this$0).get()).floatValue()).getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Bainbow", true)) {
                                            n = braibow;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Bainbow2", true)) {
                                            n = braibow2;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Sky", true)) {
                                            n = Sky;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Rise", true)) {
                                            n = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 100 * this.$rainbowSpeed, 0, 22, null).getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "SkyRainbows", true)) {
                                            n = ColorManager.astolfoRainbow(this.$counter[0] * 100, 5, 109);
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Flux", true)) {
                                            n = ColorManager.fluxRainbow(-100, (long)(this.$counter[0] * -50) * (long)((Number)Arraylist.access$getRainbowSpeeds$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getRainbowSaturation$p(this.this$0).get()).floatValue());
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "LiquidBounce", true)) {
                                            n = LiquidSlowlys;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "CustonRainbow", true)) {
                                            Color color7 = Palette.fade(new Color(((Number)Arraylist.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                                            Intrinsics.checkExpressionValueIsNotNull(color7, "Palette.fade(Color(backg\u2026indexOf(module) * 2 + 10)");
                                            n = color7.getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Astolfo", true)) {
                                            n = Astolfo;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Fade", true)) {
                                            n = mixerColor;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Gident", true)) {
                                            n = color3;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Fade2", true)) {
                                            n = FadeColor;
                                            break;
                                        }
                                        n = this.$customColor;
                                        break;
                                    }
                                    default: {
                                        n = new Color(((Number)Arraylist.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                    }
                                }
                                RenderUtils.newDrawRect(f4, f3, f2, f, n);
                            }
                            GL11.glPopMatrix();
                            this.$counter[0] = 0;
                        }
                        {
                            this.this$0 = arraylist;
                            this.$fontRenderer = fontRenderer;
                            this.$textSpacer = f;
                            this.$counter = nArray;
                            this.$Rsaturation = f2;
                            this.$Rbrightness = f3;
                            this.$textHeight = f4;
                            this.$saturation = f5;
                            this.$brightness = f6;
                            this.$liquidSlowlyDistanceValue = n;
                            this.$colorMode = string;
                            this.$rainbowSpeed = n2;
                            this.$customColor = n3;
                            super(0);
                        }
                    }, new Function0<Unit>(this, textHeight){
                        final /* synthetic */ Arraylist this$0;
                        final /* synthetic */ float $textHeight;

                        /*
                         * WARNING - void declaration
                         */
                        public final void invoke() {
                            if (!((Boolean)Arraylist.access$getShadowNoCutValue$p(this.this$0).get()).booleanValue()) {
                                GL11.glPushMatrix();
                                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                                Iterable $this$forEachIndexed$iv = Arraylist.access$getModules$p(this.this$0);
                                boolean $i$f$forEachIndexed = false;
                                int index$iv = 0;
                                for (T item$iv : $this$forEachIndexed$iv) {
                                    void module;
                                    int n = index$iv++;
                                    boolean bl = false;
                                    if (n < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    int n2 = n;
                                    Module module2 = (Module)item$iv;
                                    int index = n2;
                                    boolean bl2 = false;
                                    float xPos = -module.getSlide() - (float)2;
                                    RenderUtils.quickDrawRect(xPos - (float)(StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? 3 : 2), module.getArrayY(), StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? -1.0f : 0.0f, module.getArrayY() + this.$textHeight);
                                }
                                GL11.glPopMatrix();
                            }
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
                Iterable iterable = this.modules;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : iterable) {
                    int test;
                    int LiquidSlowly22;
                    int LiquidSlowly;
                    String displayString;
                    n = index$iv++;
                    bl2 = false;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n2 = n;
                    Module module11 = (Module)item$iv;
                    index = n2;
                    bl = false;
                    String string = (Boolean)this.tags.get() == false ? module.getBreakName((Boolean)this.nameBreakValue.get()) : (displayString = (Boolean)this.tagsArrayColor.get() != false ? module.colorlessTagName((Boolean)this.nameBreakValue.get()) : module.tagName((Boolean)this.nameBreakValue.get()));
                    if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                        String string5;
                        String string6 = displayString;
                        boolean bl3 = false;
                        String string7 = string6;
                        if (string7 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        Intrinsics.checkExpressionValueIsNotNull(string7.toUpperCase(), "(this as java.lang.String).toUpperCase()");
                        displayString = string5;
                    }
                    float xPos = -module.getSlide() - (float)2;
                    float yPos = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                    int[] counter2 = new int[]{0};
                    Color color = Color.getHSBColor(module.getHue(), saturation, brightness);
                    Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(module\u2026, saturation, brightness)");
                    int moduleColor = color.getRGB();
                    Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.RianbowspeedValue.get()).intValue(), Rsaturation, Rbrightness);
                    c = LiquidSlowly = (color2 != null ? Integer.valueOf(color2.getRGB()) : null).intValue();
                    col = new Color(c);
                    braibow2 = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                    braibow = new Color(col.getRed() / 2 + ((Number)this.Rianbowr2.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowg2.get()).intValue(), col.getBlue() / 2 + ((Number)this.Rianbowb2.get()).intValue()).getRGB();
                    int FadeColor = ColorUtils.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()), index * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    Color color3 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.RianbowspeedValue.get()).intValue(), Rsaturation, Rbrightness);
                    int c22 = LiquidSlowly22 = (color3 != null ? Integer.valueOf(color3.getRGB()) : null).intValue();
                    Color col22 = new Color(c);
                    int color32 = 0;
                    Color color4 = VisualUtils.getGradientOffset(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(module.getTranslate().getY() / (float)fontRenderer.field_78288_b)) / ((Number)this.RianbowspeedValue.get()).doubleValue());
                    Intrinsics.checkExpressionValueIsNotNull(color4, "VisualUtils.getGradientO\u2026RianbowspeedValue.get()))");
                    color32 = color4.getRGB();
                    float size = (float)this.modules.size() * 0.02f;
                    if (module.getState()) {
                        if (module.getHigt() < yPos) {
                            void v18 = module;
                            v18.setHigt(v18.getHigt() + (size - Math.min(module.getHigt() * 0.002f, size - module.getHigt() * 1.0E-4f)) * (float)delta);
                            module.setHigt(Math.min(yPos, module.getHigt()));
                        } else {
                            void v19 = module;
                            v19.setHigt(v19.getHigt() - (size - Math.min(module.getHigt() * 0.002f, size - module.getHigt() * 1.0E-4f)) * (float)delta);
                            module.setHigt(Math.max(module.getHigt(), yPos));
                        }
                    }
                    Ref.IntRef Sky = new Ref.IntRef();
                    Sky.element = RenderUtils.SkyRainbow(counter2[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    counter2[0] = counter2[0] + 1;
                    Color color5 = ColorUtils.LiquidSlowly(System.nanoTime(), index * liquidSlowlyDistanceValue, saturation, brightness);
                    int LiquidSlowlys2 = test = (color5 != null ? Integer.valueOf(color5.getRGB()) : null).intValue();
                    boolean backgroundRectRainbow2 = StringsKt.equals(backgroundColorMode, "Rainbow", true);
                    Ref.IntRef Astolfo2 = new Ref.IntRef();
                    Astolfo2.element = RenderUtils.Astolfo(counter2[0] * 100);
                    Color color6 = ColorMixer.getMixedColor(-index * ((Number)this.mixerDistValue.get()).intValue() * 10, ((Number)this.mixerSecValue.get()).intValue());
                    Intrinsics.checkExpressionValueIsNotNull(color6, "ColorMixer.getMixedColor\u2026 10, mixerSecValue.get())");
                    int mixerColor = color6.getRGB();
                    RainbowShader.Companion companion = RainbowShader.Companion;
                    float f = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float f2 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin = false;
                    RainbowShader instance$iv = RainbowShader.INSTANCE;
                    if (backgroundRectRainbow2) {
                        void y$iv232;
                        void x$iv232;
                        instance$iv.setStrengthX((float)x$iv232);
                        instance$iv.setStrengthY((float)y$iv232);
                        instance$iv.setOffset(offset$iv);
                        instance$iv.startShader();
                    }
                    Closeable this_$iv = instance$iv;
                    boolean x$iv232 = false;
                    Throwable y$iv232 = null;
                    try {
                        int n3;
                        Object it = (RainbowShader)this_$iv;
                        boolean bl4 = false;
                        float rectX = xPos - (float)(StringsKt.equals(rectMode, "right", true) || StringsKt.equals(rectMode, "rleft", true) ? 5 : 3);
                        float f3 = rectX - ((Number)this.backgroundExpand.get()).floatValue();
                        float f4 = module.getHigt() - (float)(index == 0 ? 1 : 0);
                        float f5 = StringsKt.equals(rectMode, "right", true) || StringsKt.equals(rectMode, "rleft", true) ? -1.5f : 0.0f;
                        float f6 = module.getHigt() + textHeight;
                        if (StringsKt.equals(backgroundColorMode, "Rainbow2", true)) {
                            n3 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                        } else if (StringsKt.equals(backgroundColorMode, "Random", true)) {
                            n3 = moduleColor;
                        } else if (StringsKt.equals(backgroundColorMode, "ARGB", true)) {
                            n3 = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                        } else if (StringsKt.equals(backgroundColorMode, "ARGB2", true)) {
                            n3 = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)this.rainbowSaturation.get()).floatValue()).getRGB();
                        } else if (StringsKt.equals(backgroundColorMode, "Bainbow", true)) {
                            n3 = braibow;
                        } else if (StringsKt.equals(backgroundColorMode, "Bainbow2", true)) {
                            n3 = braibow2;
                        } else if (StringsKt.equals(backgroundColorMode, "Sky", true)) {
                            n3 = Sky.element;
                        } else if (StringsKt.equals(backgroundColorMode, "Rise", true)) {
                            n3 = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 100 * rainbowSpeed, 0, 22, null).getRGB();
                        } else if (StringsKt.equals(backgroundColorMode, "SkyRainbows", true)) {
                            n3 = ColorManager.astolfoRainbow(counter2[0] * 100, 5, 109);
                        } else if (StringsKt.equals(backgroundColorMode, "Flux", true)) {
                            n3 = ColorManager.fluxRainbow(-100, (long)(counter2[0] * -50) * (long)((Number)this.rainbowSpeeds.get()).intValue(), ((Number)this.rainbowSaturation.get()).floatValue());
                        } else if (StringsKt.equals(backgroundColorMode, "LiquidBounce", true)) {
                            n3 = LiquidSlowlys2;
                        } else if (StringsKt.equals(backgroundColorMode, "CustonRainbow", true)) {
                            Color color7 = Palette.fade(new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                            Intrinsics.checkExpressionValueIsNotNull(color7, "Palette.fade(Color(backg\u2026indexOf(module) * 2 + 10)");
                            n3 = color7.getRGB();
                        } else {
                            n3 = StringsKt.equals(backgroundColorMode, "Astolfo", true) ? Astolfo2.element : (StringsKt.equals(backgroundColorMode, "Fade", true) ? mixerColor : (StringsKt.equals(backgroundColorMode, "Gident", true) ? color32 : (StringsKt.equals(backgroundColorMode, "Fade2", true) ? FadeColor : backgroundCustomColor)));
                        }
                        RenderUtils.drawRect(f3, f4, f5, f6, n3);
                        it = Unit.INSTANCE;
                    }
                    catch (Throwable it) {
                        y$iv232 = it;
                        throw it;
                    }
                    finally {
                        CloseableKt.closeFinally(this_$iv, y$iv232);
                    }
                    boolean rainbow = StringsKt.equals(colorMode, "Rainbow", true);
                    GlStateManager.func_179117_G();
                    RainbowFontShader.Companion x$iv232 = RainbowFontShader.Companion;
                    float y$iv232 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float it = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv2 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin2 = false;
                    RainbowFontShader instance$iv2 = RainbowFontShader.INSTANCE;
                    if (rainbow) {
                        void y$iv332;
                        void x$iv332;
                        instance$iv2.setStrengthX((float)x$iv332);
                        instance$iv2.setStrengthY((float)y$iv332);
                        instance$iv2.setOffset(offset$iv2);
                        instance$iv2.startShader();
                    }
                    Closeable this_$iv2 = instance$iv2;
                    boolean x$iv332 = false;
                    Throwable y$iv332 = null;
                    try {
                        int n4;
                        RainbowFontShader it232 = (RainbowFontShader)this_$iv2;
                        boolean bl5 = false;
                        float f7 = xPos - (float)(StringsKt.equals(rectMode, "right", true) || StringsKt.equals(rectMode, "rleft", true) ? 3 : 0);
                        float f8 = module.getHigt() + textY;
                        if (StringsKt.equals(colorMode, "Rainbow2", true)) {
                            n4 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                        } else if (StringsKt.equals(colorMode, "Random", true)) {
                            n4 = moduleColor;
                        } else if (StringsKt.equals(colorMode, "ARGB", true)) {
                            n4 = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                        } else if (StringsKt.equals(colorMode, "ARGB2", true)) {
                            n4 = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)this.rainbowSaturation.get()).floatValue()).getRGB();
                        } else if (StringsKt.equals(colorMode, "Bainbow", true)) {
                            n4 = braibow;
                        } else if (StringsKt.equals(colorMode, "Bainbow2", true)) {
                            n4 = braibow2;
                        } else if (StringsKt.equals(colorMode, "Sky", true)) {
                            n4 = Sky.element;
                        } else if (StringsKt.equals(colorMode, "Rise", true)) {
                            n4 = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 500, 0, 22, null).getRGB();
                        } else if (StringsKt.equals(colorMode, "SkyRainbows", true)) {
                            n4 = ColorManager.astolfoRainbow(counter2[0] * 100, 5, 109);
                        } else if (StringsKt.equals(colorMode, "Flux", true)) {
                            n4 = ColorManager.fluxRainbow(-100, (long)(counter2[0] * -50) * (long)((Number)this.rainbowSpeeds.get()).intValue(), ((Number)this.rainbowSaturation.get()).floatValue());
                        } else if (StringsKt.equals(colorMode, "LiquidBounce", true)) {
                            n4 = LiquidSlowlys2;
                        } else if (StringsKt.equals(colorMode, "CustonRainbow", true)) {
                            Color color8 = Palette.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                            Intrinsics.checkExpressionValueIsNotNull(color8, "Palette.fade(Color(color\u2026indexOf(module) * 2 + 10)");
                            n4 = color8.getRGB();
                        } else {
                            n4 = StringsKt.equals(colorMode, "Astolfo", true) ? Astolfo2.element : (StringsKt.equals(colorMode, "Fade", true) ? mixerColor : (StringsKt.equals(colorMode, "Gident", true) ? color32 : (StringsKt.equals(colorMode, "Fade2", true) ? FadeColor : customColor)));
                        }
                        int it232 = fontRenderer.func_175065_a(displayString, f7, f8, n4, textShadow);
                    }
                    catch (Throwable it232) {
                        y$iv332 = it232;
                        throw it232;
                    }
                    finally {
                        CloseableKt.closeFinally(this_$iv2, y$iv332);
                    }
                    if (StringsKt.equals(rectMode, "none", true)) continue;
                    boolean rectRainbow = StringsKt.equals(rectColorMode, "Rainbow", true);
                    RainbowShader.Companion x$iv332 = RainbowShader.Companion;
                    float y$iv332 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float it232 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv3 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin3 = false;
                    RainbowShader instance$iv3 = RainbowShader.INSTANCE;
                    if (rectRainbow) {
                        void y$iv;
                        void x$iv;
                        instance$iv3.setStrengthX((float)x$iv);
                        instance$iv3.setStrengthY((float)y$iv);
                        instance$iv3.setOffset(offset$iv3);
                        instance$iv3.startShader();
                    }
                    Closeable this_$iv3 = instance$iv3;
                    boolean x$iv = false;
                    Throwable throwable = null;
                    try {
                        int n5;
                        RainbowShader it2 = (RainbowShader)this_$iv3;
                        boolean bl6 = false;
                        if (StringsKt.equals(rectColorMode, "Rainbow2", true)) {
                            n5 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                        } else if (StringsKt.equals(rectColorMode, "Random", true)) {
                            n5 = moduleColor;
                        } else if (StringsKt.equals(rectColorMode, "ARGB", true)) {
                            n5 = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                        } else if (StringsKt.equals(rectColorMode, "ARGB2", true)) {
                            n5 = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)this.rainbowSaturation.get()).floatValue()).getRGB();
                        } else if (StringsKt.equals(rectColorMode, "Bainbow", true)) {
                            n5 = braibow;
                        } else if (StringsKt.equals(rectColorMode, "Bainbow2", true)) {
                            n5 = braibow2;
                        } else if (StringsKt.equals(rectColorMode, "Sky", true)) {
                            n5 = Sky.element;
                        } else if (StringsKt.equals(rectColorMode, "Rise", true)) {
                            n5 = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 500, 0, 22, null).getRGB();
                        } else if (StringsKt.equals(rectColorMode, "Rainbow", true)) {
                            n5 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                        } else if (StringsKt.equals(rectColorMode, "SkyRainbows", true)) {
                            n5 = ColorManager.astolfoRainbow(counter2[0] * 100, 5, 109);
                        } else if (StringsKt.equals(rectColorMode, "Flux", true)) {
                            n5 = ColorManager.fluxRainbow(-100, (long)(counter2[0] * -50) * (long)((Number)this.rainbowSpeeds.get()).intValue(), ((Number)this.rainbowSaturation.get()).floatValue());
                        } else if (StringsKt.equals(rectColorMode, "LiquidBounce", true)) {
                            n5 = LiquidSlowlys2;
                        } else if (StringsKt.equals(rectColorMode, "CustonRainbow", true)) {
                            Color color9 = Palette.fade(new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorAlphaValue.get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                            Intrinsics.checkExpressionValueIsNotNull(color9, "Palette.fade(Color(rectC\u2026indexOf(module) * 2 + 10)");
                            n5 = color9.getRGB();
                        } else {
                            n5 = StringsKt.equals(rectColorMode, "Astolfo", true) ? Astolfo2.element : (StringsKt.equals(rectColorMode, "Fade", true) ? mixerColor : (StringsKt.equals(rectColorMode, "Gident", true) ? color32 : (StringsKt.equals(rectColorMode, "Fade2", true) ? FadeColor : rectCustomColor)));
                        }
                        int rectColor = n5;
                        if (StringsKt.equals(rectMode, "left", true)) {
                            RenderUtils.drawRect(xPos - 3.2f, module.getHigt() - 1.0f, xPos - (float)2, module.getHigt() + textHeight, rectColor);
                        } else if (StringsKt.equals(rectMode, "right", true)) {
                            RenderUtils.drawRect(-1.5f, module.getHigt() - 1.0f, 0.0f, module.getHigt() + textHeight, rectColor);
                        } else if (StringsKt.equals(rectMode, "rleft", true)) {
                            if (((Boolean)this.rleftright.get()).booleanValue()) {
                                RenderUtils.drawRect(-2.0f, module.getHigt() - 2.0f, 0.0f, module.getHigt() + textHeight - 1.0f, rectColor);
                            }
                            RenderUtils.drawRect(xPos - (float)8, module.getHigt() - (float)3, xPos - (float)7, module.getHigt() + textHeight - (float)2, rectColor);
                            if (Intrinsics.areEqual(module, this.modules.get(0)) ^ true) {
                                String displayStrings;
                                String string8 = (Boolean)this.tags.get() == false ? this.modules.get(index - 1).getBreakName((Boolean)this.nameBreakValue.get()) : (displayStrings = (Boolean)this.tagsArrayColor.get() != false ? this.modules.get(index - 1).colorlessTagName((Boolean)this.nameBreakValue.get()) : this.modules.get(index - 1).tagName((Boolean)this.nameBreakValue.get()));
                                if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                                    String string9;
                                    if (displayStrings != null) {
                                        String string10;
                                        boolean bl4 = false;
                                        String string11 = string10;
                                        if (string11 == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                        }
                                        String string12 = string11.toUpperCase();
                                        string9 = string12;
                                        Intrinsics.checkExpressionValueIsNotNull(string12, "(this as java.lang.String).toUpperCase()");
                                    } else {
                                        string9 = null;
                                    }
                                    displayStrings = string9;
                                }
                                RenderUtils.drawRect(xPos - (float)8 - (float)(fontRenderer.func_78256_a(displayStrings) - fontRenderer.func_78256_a(displayString)), yPos - (float)3, xPos - (float)8, yPos - (float)2, rectColor);
                            }
                            if (Intrinsics.areEqual(module, this.modules.get(this.modules.size() - 1))) {
                                RenderUtils.drawRect(xPos - (float)8, module.getHigt() + textHeight - (float)2, 0.0f, module.getHigt() + textHeight - 1.0f, rectColor);
                            }
                            if (((Boolean)this.rleftall.get()).booleanValue()) {
                                RenderUtils.drawRect(xPos - (float)8, module.getHigt() + textHeight - (float)3, 0.0f, module.getHigt() + textHeight - (float)2, rectColor);
                            }
                            if (((Boolean)this.rlefttop.get()).booleanValue() && Intrinsics.areEqual(module, this.modules.get(this.modules.size() - this.modules.size()))) {
                                RenderUtils.drawRect(xPos - (float)7, module.getHigt() - (float)3, 0.0f, module.getHigt() - (float)2, rectColor);
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    finally {
                        CloseableKt.closeFinally(this_$iv3, throwable);
                    }
                }
                break;
            }
            case 3: {
                int braibow;
                int braibow2;
                Color col;
                int c;
                void module;
                boolean bl;
                int index;
                boolean bl2;
                int n;
                if (((Boolean)this.shadowShaderValue.get()).booleanValue()) {
                    GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                    GL11.glPushMatrix();
                    ShadowUtils.INSTANCE.shadow(((Number)this.shadowStrength.get()).intValue(), new Function0<Unit>(this, fontRenderer, textSpacer, counter, Rsaturation, Rbrightness, textHeight, saturation, brightness, liquidSlowlyDistanceValue, colorMode, rainbowSpeed, customColor){
                        final /* synthetic */ Arraylist this$0;
                        final /* synthetic */ FontRenderer $fontRenderer;
                        final /* synthetic */ float $textSpacer;
                        final /* synthetic */ int[] $counter;
                        final /* synthetic */ float $Rsaturation;
                        final /* synthetic */ float $Rbrightness;
                        final /* synthetic */ float $textHeight;
                        final /* synthetic */ float $saturation;
                        final /* synthetic */ float $brightness;
                        final /* synthetic */ int $liquidSlowlyDistanceValue;
                        final /* synthetic */ String $colorMode;
                        final /* synthetic */ int $rainbowSpeed;
                        final /* synthetic */ int $customColor;

                        /*
                         * WARNING - void declaration
                         */
                        public final void invoke() {
                            GL11.glPushMatrix();
                            GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                            Iterable $this$forEachIndexed$iv = Arraylist.access$getModules$p(this.this$0);
                            boolean $i$f$forEachIndexed = false;
                            int index$iv = 0;
                            for (T item$iv : $this$forEachIndexed$iv) {
                                int n;
                                String string;
                                int LiquidSlowly;
                                void module;
                                int n2 = index$iv++;
                                boolean bl = false;
                                if (n2 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n3 = n2;
                                Module module2 = (Module)item$iv;
                                int index = n3;
                                boolean bl2 = false;
                                float xPos = -module.getSlide() - (float)2;
                                int color3 = 0;
                                Color color = VisualUtils.getGradientOffset(new Color(((Number)Arraylist.access$getColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue$p(this.this$0).get()).intValue(), 1), new Color(((Number)Arraylist.access$getColorRedValue2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue2$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue2$p(this.this$0).get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)Arraylist.access$getGidentspeed$p(this.this$0).get()).intValue() + (double)(module.getTranslate().getY() / (float)this.$fontRenderer.field_78288_b)) / ((Number)Arraylist.access$getRianbowspeedValue$p(this.this$0).get()).doubleValue());
                                Intrinsics.checkExpressionValueIsNotNull(color, "VisualUtils.getGradientO\u2026RianbowspeedValue.get()))");
                                color3 = color.getRGB();
                                float yPos = (this.this$0.getSide().getVertical() == Side.Vertical.DOWN ? -this.$textSpacer : this.$textSpacer) * (float)(this.this$0.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                                int Astolfo = 0;
                                Astolfo = RenderUtils.Astolfo(this.$counter[0] * 100);
                                int FadeColor = ColorUtils.fade(new Color(((Number)Arraylist.access$getColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getColorBlueValue$p(this.this$0).get()).intValue(), ((Number)this.this$0.getColorAlphaValue().get()).intValue()), index * ((Number)Arraylist.access$getFadeDistanceValue$p(this.this$0).get()).intValue(), 100).getRGB();
                                int Sky = 0;
                                Sky = RenderUtils.SkyRainbow(this.$counter[0] * 100, ((Number)Arraylist.access$getSaturationValue$p(this.this$0).get()).floatValue(), ((Number)Arraylist.access$getBrightnessValue$p(this.this$0).get()).floatValue());
                                Color color2 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)Arraylist.access$getRianbowspeedValue$p(this.this$0).get()).intValue(), this.$Rsaturation, this.$Rbrightness);
                                int c = LiquidSlowly = (color2 != null ? Integer.valueOf(color2.getRGB()) : null).intValue();
                                Color col = new Color(c);
                                int braibow2 = new Color(((Number)Arraylist.access$getRianbowr$p(this.this$0).get()).intValue(), col.getGreen() / 2 + ((Number)Arraylist.access$getRianbowb$p(this.this$0).get()).intValue(), col.getGreen() / 2 + ((Number)Arraylist.access$getRianbowb$p(this.this$0).get()).intValue() + ((Number)Arraylist.access$getRianbowg$p(this.this$0).get()).intValue()).getRGB();
                                int braibow = new Color(col.getRed() / 2 + ((Number)Arraylist.access$getRianbowr2$p(this.this$0).get()).intValue(), col.getGreen() / 2 + ((Number)Arraylist.access$getRianbowg2$p(this.this$0).get()).intValue(), col.getBlue() / 2 + ((Number)Arraylist.access$getRianbowb2$p(this.this$0).get()).intValue()).getRGB();
                                String string2 = (String)Arraylist.access$getShadowColorMode$p(this.this$0).get();
                                float f = module.getArrayY() + this.$textHeight;
                                float f2 = StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? -1.0f : 0.0f;
                                float f3 = module.getArrayY();
                                float f4 = xPos - (float)(StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? 3 : 2);
                                boolean bl3 = false;
                                String string3 = string2;
                                if (string3 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                Intrinsics.checkExpressionValueIsNotNull(string3.toLowerCase(), "(this as java.lang.String).toLowerCase()");
                                switch (string) {
                                    case "background": {
                                        n = new Color(((Number)Arraylist.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                        break;
                                    }
                                    case "text": {
                                        int test;
                                        Color color4 = Color.getHSBColor(module.getHue(), this.$saturation, this.$brightness);
                                        Intrinsics.checkExpressionValueIsNotNull(color4, "Color.getHSBColor(module\u2026, saturation, brightness)");
                                        int moduleColor = color4.getRGB();
                                        this.$counter[0] = this.$counter[0] - 1;
                                        Color color5 = ColorUtils.LiquidSlowly(System.nanoTime(), index * this.$liquidSlowlyDistanceValue, this.$saturation, this.$brightness);
                                        int LiquidSlowlys = test = (color5 != null ? Integer.valueOf(color5.getRGB()) : null).intValue();
                                        Color color6 = ColorMixer.getMixedColor(-index * ((Number)Arraylist.access$getMixerDistValue$p(this.this$0).get()).intValue() * 10, ((Number)Arraylist.access$getMixerSecValue$p(this.this$0).get()).intValue());
                                        Intrinsics.checkExpressionValueIsNotNull(color6, "ColorMixer.getMixedColor\u2026 10, mixerSecValue.get())");
                                        int mixerColor = color6.getRGB();
                                        if (StringsKt.equals(this.$colorMode, "Rainbow2", true)) {
                                            n = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Random", true)) {
                                            n = moduleColor;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "ARGB", true)) {
                                            n = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "ARGB2", true)) {
                                            n = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)Arraylist.access$getRainbowSaturation$p(this.this$0).get()).floatValue()).getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Bainbow", true)) {
                                            n = braibow;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Bainbow2", true)) {
                                            n = braibow2;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Sky", true)) {
                                            n = Sky;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Rise", true)) {
                                            n = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 100 * this.$rainbowSpeed, 0, 22, null).getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "SkyRainbows", true)) {
                                            n = ColorManager.astolfoRainbow(this.$counter[0] * 100, 5, 109);
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Flux", true)) {
                                            n = ColorManager.fluxRainbow(-100, (long)(this.$counter[0] * -50) * (long)((Number)Arraylist.access$getRainbowSpeeds$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getRainbowSaturation$p(this.this$0).get()).floatValue());
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "LiquidBounce", true)) {
                                            n = LiquidSlowlys;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "CustonRainbow", true)) {
                                            Color color7 = Palette.fade(new Color(((Number)Arraylist.access$getBackgroundColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorBlueValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getBackgroundColorAlphaValue$p(this.this$0).get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                                            Intrinsics.checkExpressionValueIsNotNull(color7, "Palette.fade(Color(backg\u2026indexOf(module) * 2 + 10)");
                                            n = color7.getRGB();
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Astolfo", true)) {
                                            n = Astolfo;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Fade", true)) {
                                            n = mixerColor;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Gident", true)) {
                                            n = color3;
                                            break;
                                        }
                                        if (StringsKt.equals(this.$colorMode, "Fade2", true)) {
                                            n = FadeColor;
                                            break;
                                        }
                                        n = this.$customColor;
                                        break;
                                    }
                                    default: {
                                        n = new Color(((Number)Arraylist.access$getShadowColorRedValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorGreenValue$p(this.this$0).get()).intValue(), ((Number)Arraylist.access$getShadowColorBlueValue$p(this.this$0).get()).intValue()).getRGB();
                                    }
                                }
                                RenderUtils.newDrawRect(f4, f3, f2, f, n);
                            }
                            GL11.glPopMatrix();
                            this.$counter[0] = 0;
                        }
                        {
                            this.this$0 = arraylist;
                            this.$fontRenderer = fontRenderer;
                            this.$textSpacer = f;
                            this.$counter = nArray;
                            this.$Rsaturation = f2;
                            this.$Rbrightness = f3;
                            this.$textHeight = f4;
                            this.$saturation = f5;
                            this.$brightness = f6;
                            this.$liquidSlowlyDistanceValue = n;
                            this.$colorMode = string;
                            this.$rainbowSpeed = n2;
                            this.$customColor = n3;
                            super(0);
                        }
                    }, new Function0<Unit>(this, textHeight){
                        final /* synthetic */ Arraylist this$0;
                        final /* synthetic */ float $textHeight;

                        /*
                         * WARNING - void declaration
                         */
                        public final void invoke() {
                            if (!((Boolean)Arraylist.access$getShadowNoCutValue$p(this.this$0).get()).booleanValue()) {
                                GL11.glPushMatrix();
                                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                                Iterable $this$forEachIndexed$iv = Arraylist.access$getModules$p(this.this$0);
                                boolean $i$f$forEachIndexed = false;
                                int index$iv = 0;
                                for (T item$iv : $this$forEachIndexed$iv) {
                                    void module;
                                    int n = index$iv++;
                                    boolean bl = false;
                                    if (n < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    int n2 = n;
                                    Module module2 = (Module)item$iv;
                                    int index = n2;
                                    boolean bl2 = false;
                                    float xPos = -module.getSlide() - (float)2;
                                    RenderUtils.quickDrawRect(xPos - (float)(StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? 3 : 2), module.getArrayY(), StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? -1.0f : 0.0f, module.getArrayY() + this.$textHeight);
                                }
                                GL11.glPopMatrix();
                            }
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
                    float f = (float)this.getRenderX();
                    float floatY = (float)this.getRenderY();
                    float yP = 0.0f;
                    float xP = 0.0f;
                    Iterable $this$forEachIndexed$iv = this.modules;
                    boolean $i$f$forEachIndexed = false;
                    int index$iv = 0;
                    for (Module item$iv2 : $this$forEachIndexed$iv) {
                        void module12;
                        index = index$iv++;
                        bl = false;
                        if (index < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        int displayString = index;
                        Module xPos = item$iv2;
                        int index2 = displayString;
                        boolean bl8 = false;
                        boolean displayString2 = (Boolean)this.tags.get() == false;
                        module12.getBreakName((Boolean)this.nameBreakValue.get());
                        String dString = String.valueOf(displayString2);
                        float wid = (float)fontRenderer.func_78256_a(dString) + 2.0f;
                        float yPos = this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index2 + 1 : index2);
                        yP += yPos;
                        xP = Math.max(xP, wid);
                    }
                    BlurUtils.blur(f, floatY, f + xP, floatY + yP, ((Number)this.blurStrength.get()).floatValue(), false, new Function0<Unit>(this, fontRenderer, f, floatY, textHeight){
                        final /* synthetic */ Arraylist this$0;
                        final /* synthetic */ FontRenderer $fontRenderer;
                        final /* synthetic */ float $floatX;
                        final /* synthetic */ float $floatY;
                        final /* synthetic */ float $textHeight;

                        /*
                         * WARNING - void declaration
                         */
                        public final void invoke() {
                            Iterable $this$forEachIndexed$iv = Arraylist.access$getModules$p(this.this$0);
                            boolean $i$f$forEachIndexed = false;
                            int index$iv = 0;
                            for (T item$iv : $this$forEachIndexed$iv) {
                                void module;
                                int n = index$iv++;
                                boolean bl = false;
                                if (n < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                int n2 = n;
                                Module module2 = (Module)item$iv;
                                int index = n2;
                                boolean bl2 = false;
                                boolean displayString = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false;
                                module.getBreakName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get());
                                int width = this.$fontRenderer.func_78256_a(String.valueOf(displayString));
                                float xPos = -((float)width - module.getSlide()) + (float)(StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "left", true) ? 3 : 2);
                                RenderUtils.quickDrawRect(this.$floatX, this.$floatY + module.getArrayY(), this.$floatX + xPos + (float)width + (float)(StringsKt.equals((String)Arraylist.access$getRectValue$p(this.this$0).get(), "right", true) ? 3 : 2), this.$floatY + module.getArrayY() + this.$textHeight);
                            }
                        }
                        {
                            this.this$0 = arraylist;
                            this.$fontRenderer = fontRenderer;
                            this.$floatX = f;
                            this.$floatY = f2;
                            this.$textHeight = f3;
                            super(0);
                        }
                    });
                    GL11.glPopMatrix();
                    GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
                }
                Iterable iterable = this.modules;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (Object item$iv : iterable) {
                    int test;
                    int LiquidSlowly;
                    String displayString;
                    Module item$iv2;
                    n = index$iv++;
                    bl2 = false;
                    if (n < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    int n6 = n;
                    item$iv2 = (Module)item$iv;
                    index = n6;
                    boolean bl9 = false;
                    String string = (Boolean)this.tags.get() == false ? module.getBreakName((Boolean)this.nameBreakValue.get()) : (displayString = (Boolean)this.tagsArrayColor.get() != false ? module.colorlessTagName((Boolean)this.nameBreakValue.get()) : module.tagName((Boolean)this.nameBreakValue.get()));
                    if (((Boolean)this.upperCaseValue.get()).booleanValue()) {
                        String string13;
                        String module12 = displayString;
                        boolean bl8 = false;
                        String string14 = module12;
                        if (string14 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        Intrinsics.checkExpressionValueIsNotNull(string14.toUpperCase(), "(this as java.lang.String).toUpperCase()");
                        displayString = string13;
                    }
                    int width = fontRenderer.func_78256_a(displayString);
                    float xPos = -((float)width - module.getSlide()) + (float)(StringsKt.equals(rectMode, "left", true) ? 5 : 2);
                    float yPos = (this.getSide().getVertical() == Side.Vertical.DOWN ? -textSpacer : textSpacer) * (float)(this.getSide().getVertical() == Side.Vertical.DOWN ? index + 1 : index);
                    Color color = Color.getHSBColor(module.getHue(), saturation, brightness);
                    Intrinsics.checkExpressionValueIsNotNull(color, "Color.getHSBColor(module\u2026, saturation, brightness)");
                    int moduleColor = color.getRGB();
                    Color color10 = ColorUtils.LiquidSlowly(System.nanoTime(), index * ((Number)this.RianbowspeedValue.get()).intValue(), Rsaturation, Rbrightness);
                    c = LiquidSlowly = (color10 != null ? Integer.valueOf(color10.getRGB()) : null).intValue();
                    col = new Color(c);
                    braibow2 = new Color(((Number)this.Rianbowr.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb.get()).intValue() + ((Number)this.Rianbowg.get()).intValue()).getRGB();
                    braibow = new Color(col.getRed() / 2 + ((Number)this.Rianbowr2.get()).intValue(), col.getGreen() / 2 + ((Number)this.Rianbowb2.get()).intValue(), col.getBlue() / 2 + ((Number)this.Rianbowb2.get()).intValue()).getRGB();
                    Ref.IntRef Sky = new Ref.IntRef();
                    Sky.element = RenderUtils.SkyRainbow(counter[0] * 100, ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                    counter[0] = counter[0] + 1;
                    Color color11 = ColorUtils.LiquidSlowly(System.nanoTime(), index * liquidSlowlyDistanceValue, saturation, brightness);
                    int LiquidSlowlys = test = (color11 != null ? Integer.valueOf(color11.getRGB()) : null).intValue();
                    Ref.IntRef Astolfo = new Ref.IntRef();
                    Astolfo.element = RenderUtils.Astolfo(counter[0] * 100);
                    boolean backgroundRectRainbow = StringsKt.equals(backgroundColorMode, "Rainbow", true);
                    int FadeColor = ColorUtils.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), ((Number)this.colorAlphaValue.get()).intValue()), index * ((Number)this.fadeDistanceValue.get()).intValue(), 100).getRGB();
                    int color3 = 0;
                    Color color12 = VisualUtils.getGradientOffset(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 1), new Color(((Number)this.colorRedValue2.get()).intValue(), ((Number)this.colorGreenValue2.get()).intValue(), ((Number)this.colorBlueValue2.get()).intValue(), 1), Math.abs((double)System.currentTimeMillis() / (double)((Number)this.gidentspeed.get()).intValue() + (double)(module.getTranslate().getY() / (float)fontRenderer.field_78288_b)) / (double)10);
                    Intrinsics.checkExpressionValueIsNotNull(color12, "VisualUtils.getGradientO\u2026                 ) / 10))");
                    color3 = color12.getRGB();
                    Color color13 = ColorMixer.getMixedColor(-index * ((Number)this.mixerDistValue.get()).intValue() * 10, ((Number)this.mixerSecValue.get()).intValue());
                    Intrinsics.checkExpressionValueIsNotNull(color13, "ColorMixer.getMixedColor\u2026 10, mixerSecValue.get())");
                    int mixerColor = color13.getRGB();
                    RainbowShader.Companion LiquidSlowlys2 = RainbowShader.Companion;
                    float backgroundRectRainbow2 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float Astolfo2 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin = false;
                    RainbowShader instance$iv = RainbowShader.INSTANCE;
                    if (backgroundRectRainbow) {
                        void y$iv432;
                        void x$iv432;
                        instance$iv.setStrengthX((float)x$iv432);
                        instance$iv.setStrengthY((float)y$iv432);
                        instance$iv.setOffset(offset$iv);
                        instance$iv.startShader();
                    }
                    Closeable this_$iv = instance$iv;
                    boolean x$iv432 = false;
                    Throwable y$iv432 = null;
                    try {
                        int n7;
                        Object it = (RainbowShader)this_$iv;
                        boolean bl10 = false;
                        float f = module.getHigt();
                        float f9 = xPos + (float)module.getWidth() + (float)(StringsKt.equals(rectMode, "right", true) ? 5 : 2 + ((Number)this.backgroundExpand.get()).intValue());
                        float f10 = module.getHigt() + textHeight;
                        if (StringsKt.equals(backgroundColorMode, "Rainbow2", true)) {
                            n7 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                        } else if (StringsKt.equals(backgroundColorMode, "Random", true)) {
                            n7 = moduleColor;
                        } else if (StringsKt.equals(backgroundColorMode, "ARGB", true)) {
                            n7 = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                        } else if (StringsKt.equals(backgroundColorMode, "ARGB2", true)) {
                            n7 = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)this.rainbowSaturation.get()).floatValue()).getRGB();
                        } else if (StringsKt.equals(backgroundColorMode, "Bainbow", true)) {
                            n7 = braibow;
                        } else if (StringsKt.equals(backgroundColorMode, "Bainbow2", true)) {
                            n7 = braibow2;
                        } else if (StringsKt.equals(backgroundColorMode, "Sky", true)) {
                            n7 = Sky.element;
                        } else if (StringsKt.equals(backgroundColorMode, "Rise", true)) {
                            n7 = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 100 * rainbowSpeed, 0, 22, null).getRGB();
                        } else if (StringsKt.equals(backgroundColorMode, "SkyRainbows", true)) {
                            n7 = ColorManager.astolfoRainbow(counter[0] * 100, 5, 109);
                        } else if (StringsKt.equals(backgroundColorMode, "Flux", true)) {
                            n7 = ColorManager.fluxRainbow(-100, (long)(counter[0] * -50) * (long)((Number)this.rainbowSpeeds.get()).intValue(), ((Number)this.rainbowSaturation.get()).floatValue());
                        } else if (StringsKt.equals(backgroundColorMode, "LiquidBounce", true)) {
                            n7 = LiquidSlowlys;
                        } else if (StringsKt.equals(backgroundColorMode, "CustonRainbow", true)) {
                            Color color14 = Palette.fade(new Color(((Number)this.backgroundColorRedValue.get()).intValue(), ((Number)this.backgroundColorGreenValue.get()).intValue(), ((Number)this.backgroundColorBlueValue.get()).intValue(), ((Number)this.backgroundColorAlphaValue.get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                            Intrinsics.checkExpressionValueIsNotNull(color14, "Palette.fade(Color(backg\u2026indexOf(module) * 2 + 10)");
                            n7 = color14.getRGB();
                        } else {
                            n7 = StringsKt.equals(backgroundColorMode, "Astolfo", true) ? Astolfo.element : (StringsKt.equals(backgroundColorMode, "Fade", true) ? mixerColor : (StringsKt.equals(backgroundColorMode, "Gident", true) ? color3 : (StringsKt.equals(backgroundColorMode, "Fade2", true) ? FadeColor : backgroundCustomColor)));
                        }
                        RenderUtils.drawRect(0.0f, f, f9, f10, n7);
                        it = Unit.INSTANCE;
                    }
                    catch (Throwable it) {
                        y$iv432 = it;
                        throw it;
                    }
                    finally {
                        CloseableKt.closeFinally(this_$iv, y$iv432);
                    }
                    boolean rainbow = StringsKt.equals(colorMode, "Rainbow", true);
                    GlStateManager.func_179117_G();
                    RainbowFontShader.Companion x$iv432 = RainbowFontShader.Companion;
                    float y$iv432 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float it = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv4 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin4 = false;
                    RainbowFontShader instance$iv4 = RainbowFontShader.INSTANCE;
                    if (rainbow) {
                        void y$iv532;
                        void x$iv532;
                        instance$iv4.setStrengthX((float)x$iv532);
                        instance$iv4.setStrengthY((float)y$iv532);
                        instance$iv4.setOffset(offset$iv4);
                        instance$iv4.startShader();
                    }
                    Closeable this_$iv4 = instance$iv4;
                    boolean x$iv532 = false;
                    Throwable y$iv532 = null;
                    try {
                        int n8;
                        RainbowFontShader it332 = (RainbowFontShader)this_$iv4;
                        boolean bl11 = false;
                        float f = module.getHigt() + textY;
                        if (StringsKt.equals(colorMode, "Rainbow2", true)) {
                            n8 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                        } else if (StringsKt.equals(colorMode, "Random", true)) {
                            n8 = moduleColor;
                        } else if (StringsKt.equals(colorMode, "ARGB", true)) {
                            n8 = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                        } else if (StringsKt.equals(colorMode, "ARGB2", true)) {
                            n8 = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)this.rainbowSaturation.get()).floatValue()).getRGB();
                        } else if (StringsKt.equals(colorMode, "Bainbow", true)) {
                            n8 = braibow;
                        } else if (StringsKt.equals(colorMode, "Bainbow2", true)) {
                            n8 = braibow2;
                        } else if (StringsKt.equals(colorMode, "Sky", true)) {
                            n8 = Sky.element;
                        } else if (StringsKt.equals(colorMode, "Rise", true)) {
                            n8 = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 100 * rainbowSpeed, 0, 22, null).getRGB();
                        } else if (StringsKt.equals(colorMode, "SkyRainbows", true)) {
                            n8 = ColorManager.astolfoRainbow(counter[0] * 100, 5, 109);
                        } else if (StringsKt.equals(colorMode, "Flux", true)) {
                            n8 = ColorManager.fluxRainbow(-100, (long)(counter[0] * -50) * (long)((Number)this.rainbowSpeeds.get()).intValue(), ((Number)this.rainbowSaturation.get()).floatValue());
                        } else if (StringsKt.equals(colorMode, "LiquidBounce", true)) {
                            n8 = LiquidSlowlys;
                        } else if (StringsKt.equals(colorMode, "CustonRainbow", true)) {
                            Color color15 = Palette.fade(new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                            Intrinsics.checkExpressionValueIsNotNull(color15, "Palette.fade(Color(color\u2026indexOf(module) * 2 + 10)");
                            n8 = color15.getRGB();
                        } else {
                            n8 = StringsKt.equals(colorMode, "Astolfo", true) ? Astolfo.element : (StringsKt.equals(colorMode, "Fade", true) ? mixerColor : (StringsKt.equals(colorMode, "Gident", true) ? color3 : (StringsKt.equals(colorMode, "Fade2", true) ? FadeColor : customColor)));
                        }
                        int it332 = fontRenderer.func_175065_a(displayString, xPos, f, n8, textShadow);
                    }
                    catch (Throwable it332) {
                        y$iv532 = it332;
                        throw it332;
                    }
                    finally {
                        CloseableKt.closeFinally(this_$iv4, y$iv532);
                    }
                    boolean rectColorRainbow = StringsKt.equals(rectColorMode, "Rainbow", true);
                    RainbowShader.Companion x$iv532 = RainbowShader.Companion;
                    float y$iv532 = ((Number)this.rainbowX.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowX.get()).floatValue();
                    float it332 = ((Number)this.rainbowY.get()).floatValue() == 0.0f ? 0.0f : 1.0f / ((Number)this.rainbowY.get()).floatValue();
                    float offset$iv5 = (float)(System.currentTimeMillis() % (long)10000) / 10000.0f;
                    boolean $i$f$begin5 = false;
                    RainbowShader instance$iv5 = RainbowShader.INSTANCE;
                    if (rectColorRainbow) {
                        void y$iv;
                        void x$iv;
                        instance$iv5.setStrengthX((float)x$iv);
                        instance$iv5.setStrengthY((float)y$iv);
                        instance$iv5.setOffset(offset$iv5);
                        instance$iv5.startShader();
                    }
                    Closeable closeable = instance$iv5;
                    boolean bl5 = false;
                    Throwable throwable = null;
                    try {
                        RainbowShader it3 = (RainbowShader)closeable;
                        boolean bl13 = false;
                        if (!StringsKt.equals(rectMode, "none", true)) {
                            int n9;
                            if (StringsKt.equals(rectColorMode, "Rainbow2", true)) {
                                n9 = ColorUtils.rainbow(400000000L * (long)index).getRGB();
                            } else if (StringsKt.equals(rectColorMode, "Random", true)) {
                                n9 = moduleColor;
                            } else if (StringsKt.equals(rectColorMode, "ARGB", true)) {
                                n9 = Colors.getRainbow(-2000, (int)(yPos * (float)8));
                            } else if (StringsKt.equals(rectColorMode, "ARGB2", true)) {
                                n9 = ColorUti.rainbow(400000000L * (long)index, 255, ((Number)this.rainbowSaturation.get()).floatValue()).getRGB();
                            } else if (StringsKt.equals(rectColorMode, "Bainbow", true)) {
                                n9 = braibow;
                            } else if (StringsKt.equals(rectColorMode, "Bainbow2", true)) {
                                n9 = braibow2;
                            } else if (StringsKt.equals(rectColorMode, "Sky", true)) {
                                n9 = Sky.element;
                            } else if (StringsKt.equals(rectColorMode, "Rise", true)) {
                                n9 = ColorUtils.hslRainbow$default(ColorUtils.INSTANCE, index + 1, 0.0f, 0.0f, 100 * rainbowSpeed, 0, 22, null).getRGB();
                            } else if (StringsKt.equals(rectColorMode, "SkyRainbows", true)) {
                                n9 = ColorManager.astolfoRainbow(counter[0] * 100, 5, 109);
                            } else if (StringsKt.equals(rectColorMode, "Flux", true)) {
                                n9 = ColorManager.fluxRainbow(-100, (long)(counter[0] * -50) * (long)((Number)this.rainbowSpeeds.get()).intValue(), ((Number)this.rainbowSaturation.get()).floatValue());
                            } else if (StringsKt.equals(rectColorMode, "LiquidBounce", true)) {
                                n9 = LiquidSlowlys;
                            } else if (StringsKt.equals(rectColorMode, "CustonRainbow", true)) {
                                Color color16 = Palette.fade(new Color(((Number)this.rectColorRedValue.get()).intValue(), ((Number)this.rectColorGreenValue.get()).intValue(), ((Number)this.rectColorBlueValue.get()).intValue(), ((Number)this.rectColorAlphaValue.get()).intValue()), 100, CollectionsKt.indexOf((Iterable)LiquidBounce.INSTANCE.getModuleManager().getModules(), module) * 2 + 10);
                                Intrinsics.checkExpressionValueIsNotNull(color16, "Palette.fade(Color(rectC\u2026indexOf(module) * 2 + 10)");
                                n9 = color16.getRGB();
                            } else {
                                n9 = StringsKt.equals(rectColorMode, "Astolfo", true) ? Astolfo.element : (StringsKt.equals(rectColorMode, "Fade", true) ? mixerColor : (StringsKt.equals(rectColorMode, "Gident", true) ? color3 : (StringsKt.equals(rectColorMode, "Fade2", true) ? FadeColor : rectCustomColor)));
                            }
                            int rectColor = n9;
                            if (StringsKt.equals(rectMode, "left", true)) {
                                RenderUtils.drawRect(0.0f, yPos - 1.0f, 3.0f, yPos + textHeight, rectColor);
                            } else if (StringsKt.equals(rectMode, "rights", true)) {
                                RenderUtils.drawRect(xPos + (float)width + (float)2, yPos, xPos + (float)width + (float)2 + (float)3, yPos + textHeight, rectColor);
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                    catch (Throwable throwable3) {
                        throwable = throwable3;
                        throw throwable3;
                    }
                    finally {
                        CloseableKt.closeFinally(closeable, throwable);
                    }
                }
                break;
            }
        }
        if (Arraylist.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner) {
            this.x2 = Integer.MIN_VALUE;
            if (this.modules.isEmpty()) {
                return this.getSide().getHorizontal() == Side.Horizontal.LEFT ? new Border(0.0f, -1.0f, 20.0f, 20.0f) : new Border(0.0f, -1.0f, -20.0f, 20.0f);
            }
            for (Module module : this.modules) {
                switch (Arraylist$WhenMappings.$EnumSwitchMapping$1[this.getSide().getHorizontal().ordinal()]) {
                    case 1: 
                    case 2: {
                        int xPos = -((int)module.getSlide()) - 2;
                        if (this.x2 != Integer.MIN_VALUE && xPos >= this.x2) break;
                        this.x2 = xPos;
                        break;
                    }
                    case 3: {
                        int xPos = (int)module.getSlide() + 14;
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
        void $this$sortedBy$iv;
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv;
        Iterable iterable = LiquidBounce.INSTANCE.getModuleManager().getModules();
        Arraylist arraylist = this;
        boolean $i$f$filter = false;
        void var3_4 = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!it.getArray() || (StringsKt.equals((String)this.hAnimation.get(), "none", true) ? it.getState() : it.getSlide() > 0.0f)) {
                // empty if block
            }
            if (!(!this.shouldExpect(it) && !this.shouldExpect2(it) && !this.shouldExpect3(it) && it.getSlide() > 0.0f)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List list = (List)destination$iv$iv;
        $this$filter$iv = list;
        boolean $i$f$sortedBy = false;
        var3_4 = $this$sortedBy$iv;
        boolean bl = false;
        Comparator comparator = new Comparator<T>(this){
            final /* synthetic */ Arraylist this$0;
            {
                this.this$0 = arraylist;
            }

            public final int compare(T a, T b) {
                String string;
                String string2;
                String string3;
                boolean bl;
                FontRenderer fontRenderer;
                String string4;
                boolean bl2 = false;
                Module it = (Module)a;
                boolean bl3 = false;
                FontRenderer fontRenderer2 = (FontRenderer)Arraylist.access$getFontValue$p(this.this$0).get();
                if (((Boolean)Arraylist.access$getUpperCaseValue$p(this.this$0).get()).booleanValue()) {
                    string4 = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getBreakName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.colorlessTagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : it.tagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()));
                    fontRenderer = fontRenderer2;
                    bl = false;
                    String string5 = string4;
                    if (string5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string6 = string5.toUpperCase();
                    Intrinsics.checkExpressionValueIsNotNull(string6, "(this as java.lang.String).toUpperCase()");
                    string3 = string6;
                    fontRenderer2 = fontRenderer;
                    string2 = string3;
                } else {
                    string2 = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getBreakName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.colorlessTagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : it.tagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()));
                }
                it = (Module)b;
                Comparable comparable = Integer.valueOf(-fontRenderer2.func_78256_a(string2));
                bl3 = false;
                FontRenderer fontRenderer3 = (FontRenderer)Arraylist.access$getFontValue$p(this.this$0).get();
                if (((Boolean)Arraylist.access$getUpperCaseValue$p(this.this$0).get()).booleanValue()) {
                    string4 = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getBreakName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.colorlessTagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : it.tagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()));
                    fontRenderer = fontRenderer3;
                    bl = false;
                    String string7 = string4;
                    if (string7 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    String string8 = string7.toUpperCase();
                    Intrinsics.checkExpressionValueIsNotNull(string8, "(this as java.lang.String).toUpperCase()");
                    string3 = string8;
                    fontRenderer3 = fontRenderer;
                    string = string3;
                } else {
                    string = (Boolean)Arraylist.access$getTags$p(this.this$0).get() == false ? it.getBreakName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : ((Boolean)Arraylist.access$getTagsArrayColor$p(this.this$0).get() != false ? it.colorlessTagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()) : it.tagName((Boolean)Arraylist.access$getNameBreakValue$p(this.this$0).get()));
                }
                Integer n = -fontRenderer3.func_78256_a(string);
                return ComparisonsKt.compareValues(comparable, (Comparable)n);
            }
        };
        list = CollectionsKt.sortedWith(var3_4, comparator);
        arraylist.modules = list;
    }

    public Arraylist(double x, double y, float scale, @NotNull Side side) {
        Intrinsics.checkParameterIsNotNull(side, "side");
        super(x, y, scale, side);
        this.rectValue = new ListValue("Rect", new String[]{"None", "Left", "rleft", "Right"}, "Right");
        this.colorModeValue = new ListValue("Text-Color", new String[]{"Custom", "Rainbow", "Rainbow2", "Random", "ARGB", "ARGB2", "Bainbow", "Bainbow2", "Rise", "LiquidBounce", "Fade", "Fade2"}, "Rainbow2");
        this.colorRedValue = new IntegerValue("Text Red", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("Text Green", 111, 0, 255);
        this.colorBlueValue = new IntegerValue("Text Blue", 255, 0, 255);
        this.colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
        this.colorRedValue2 = new IntegerValue("Text Red2", 0, 0, 255);
        this.colorGreenValue2 = new IntegerValue("Text Green2", 111, 0, 255);
        this.colorBlueValue2 = new IntegerValue("Text Blue2", 255, 0, 255);
        this.rectColorModeValue = new ListValue("Rect Color", new String[]{"Custom", "Rainbow", "Rainbow2", "Random", "ARGB", "ARGB2", "Bainbow", "Bainbow2", "Fade", "Rise", "LiquidBounce", "Fade", "Fade2"}, "Rainbow2");
        this.rectColorRedValue = new IntegerValue("Rect Red", 255, 0, 255);
        this.rectColorGreenValue = new IntegerValue("Rect Green", 255, 0, 255);
        this.rectColorBlueValue = new IntegerValue("Rect Blue", 255, 0, 255);
        this.rectColorBlueAlpha = new IntegerValue("Rect Alpha", 255, 0, 255);
        this.rectColorAlphaValue = new IntegerValue("Rect Alphad", 255, 0, 255);
        this.backgroundColorModeValue = new ListValue("Background-Color", new String[]{"Custom"}, "Custom");
        this.rainbowX = new FloatValue("Rainbow X", -1000.0f, -2000.0f, 2000.0f);
        this.rainbowY = new FloatValue("Rainbow Y", -1000.0f, -2000.0f, 2000.0f);
        this.Rianbowr = new IntegerValue("Bainbow Red", 0, 0, 255);
        this.Rianbowb = new IntegerValue("Bainbow Blue", 50, 0, 64);
        this.Rianbowg = new IntegerValue("Bainbow Green", 50, 0, 64);
        this.Rianbowr2 = new IntegerValue("Bainbow2 Red", 50, 0, 255);
        this.Rianbowb2 = new IntegerValue("Bainbow2 Blue", 50, 0, 255);
        this.Rianbowg2 = new IntegerValue("Bainbow2 Green", 50, 0, 255);
        this.backgroundColorRedValue = new IntegerValue("Background Red", 0, 0, 255);
        this.backgroundColorGreenValue = new IntegerValue("Background Green", 0, 0, 255);
        this.backgroundColorBlueValue = new IntegerValue("Background Blue", 0, 0, 255);
        this.backgroundColorAlphaValue = new IntegerValue("Background Alpha", 0, 0, 255);
        this.backgroundExpand = new IntegerValue("Background Expand", 2, 0, 10);
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 5.0f, 0.0f, 30.0f);
        this.shadowShaderValue = new BoolValue("Shadow", false);
        this.shadowNoCutValue = new BoolValue("Shadow-NoCut", false);
        this.shadowStrength = new IntegerValue("Shadow-Strength", 1, 1, 30);
        this.shadowColorMode = new ListValue("Shadow-Color", new String[]{"Background", "Text", "Custom"}, "Background");
        this.shadowColorRedValue = new IntegerValue("Shadow-Red", 0, 0, 255);
        this.shadowColorGreenValue = new IntegerValue("Shadow-Green", 111, 0, 255);
        this.shadowColorBlueValue = new IntegerValue("Shadow-Blue", 255, 0, 255);
        this.fadeDistanceValue = new IntegerValue("Fade2 Distance", 95, 1, 100);
        this.rainbowSaturation = new FloatValue("Rainbow Saturation", 0.5f, 0.01f, 1.0f);
        this.mixerSecValue = new IntegerValue("Fade Seconds", 2, 1, 10);
        this.mixerDistValue = new IntegerValue("Fade Distance", 2, 0, 10);
        this.textYValue = new FloatValue("TextY", 1.0f, 0.0f, 20.0f);
        this.tagsArrayColor = new BoolValue("Tags Array Color", false);
        this.gidentspeed = new IntegerValue("Gident Speed", 1, 1, 2000);
        this.noRenderModules = new BoolValue("No Render", false);
        this.noOtherModules = new BoolValue("No Other", false);
        this.noPlayerModules = new BoolValue("No Player", false);
        this.animationSpeed = new FloatValue("Animation Speed", 0.25f, 0.01f, 1.0f);
        this.hAnimation = new ListValue("HorizontalAnimation", new String[]{"Default", "None", "Slide", "Astolfo"}, "Astolfo");
        this.vAnimation = new ListValue("VerticalAnimation", new String[]{"None", "LiquidSense", "Slide", "Rise", "Astolfo"}, "Astolfo");
        this.RianbowspeedValue = new IntegerValue("Bainbow Speed", 90, 1, 140);
        this.RianbowbValue = new FloatValue("Bainbow Saturation", 1.0f, 0.0f, 1.0f);
        this.rlefttop = new BoolValue("RLeft Top", true);
        this.rleftright = new BoolValue("RLeft Right", true);
        this.rleftall = new BoolValue("RLeft ALL", false);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.upperCaseValue = new BoolValue("Upper Case", false);
        this.spaceValue = new FloatValue("Space", 0.0f, 0.0f, 5.0f);
        this.textHeightValue = new FloatValue("Text Height", 11.0f, 1.0f, 20.0f);
        this.tags = new BoolValue("Tags", true);
        this.shadow = new BoolValue("Shadow Text", true);
        this.nameBreakValue = new BoolValue("Name Break", true);
        this.rainbowSpeeds = new IntegerValue("Rainbow Speed", 1, 1, 10);
        this.liquidSlowlyDistanceValue = new IntegerValue("liquidBounce Distance", 70, 1, 100);
        FontRenderer fontRenderer = Fonts.minecraftFont;
        Intrinsics.checkExpressionValueIsNotNull(fontRenderer, "Fonts.minecraftFont");
        this.fontValue = new FontValue("Font", fontRenderer);
        this.modules = CollectionsKt.emptyList();
    }

    public /* synthetic */ Arraylist(double d, double d2, float f, Side side, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 0.0;
        }
        if ((n & 2) != 0) {
            d2 = 1.0;
        }
        if ((n & 4) != 0) {
            f = 1.0f;
        }
        if ((n & 8) != 0) {
            side = new Side(Side.Horizontal.RIGHT, Side.Vertical.UP);
        }
        this(d, d2, f, side);
    }

    public Arraylist() {
        this(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ List access$getModules$p(Arraylist $this) {
        return $this.modules;
    }

    public static final /* synthetic */ void access$setModules$p(Arraylist $this, List list) {
        $this.modules = list;
    }

    public static final /* synthetic */ IntegerValue access$getColorRedValue$p(Arraylist $this) {
        return $this.colorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getColorGreenValue$p(Arraylist $this) {
        return $this.colorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getColorBlueValue$p(Arraylist $this) {
        return $this.colorBlueValue;
    }

    public static final /* synthetic */ IntegerValue access$getColorRedValue2$p(Arraylist $this) {
        return $this.colorRedValue2;
    }

    public static final /* synthetic */ IntegerValue access$getColorGreenValue2$p(Arraylist $this) {
        return $this.colorGreenValue2;
    }

    public static final /* synthetic */ IntegerValue access$getColorBlueValue2$p(Arraylist $this) {
        return $this.colorBlueValue2;
    }

    public static final /* synthetic */ IntegerValue access$getGidentspeed$p(Arraylist $this) {
        return $this.gidentspeed;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowspeedValue$p(Arraylist $this) {
        return $this.RianbowspeedValue;
    }

    public static final /* synthetic */ IntegerValue access$getFadeDistanceValue$p(Arraylist $this) {
        return $this.fadeDistanceValue;
    }

    public static final /* synthetic */ FloatValue access$getSaturationValue$p(Arraylist $this) {
        return $this.saturationValue;
    }

    public static final /* synthetic */ FloatValue access$getBrightnessValue$p(Arraylist $this) {
        return $this.brightnessValue;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowr$p(Arraylist $this) {
        return $this.Rianbowr;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowb$p(Arraylist $this) {
        return $this.Rianbowb;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowg$p(Arraylist $this) {
        return $this.Rianbowg;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowr2$p(Arraylist $this) {
        return $this.Rianbowr2;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowg2$p(Arraylist $this) {
        return $this.Rianbowg2;
    }

    public static final /* synthetic */ IntegerValue access$getRianbowb2$p(Arraylist $this) {
        return $this.Rianbowb2;
    }

    public static final /* synthetic */ ListValue access$getRectValue$p(Arraylist $this) {
        return $this.rectValue;
    }

    public static final /* synthetic */ ListValue access$getShadowColorMode$p(Arraylist $this) {
        return $this.shadowColorMode;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorRedValue$p(Arraylist $this) {
        return $this.backgroundColorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorGreenValue$p(Arraylist $this) {
        return $this.backgroundColorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorBlueValue$p(Arraylist $this) {
        return $this.backgroundColorBlueValue;
    }

    public static final /* synthetic */ IntegerValue access$getMixerDistValue$p(Arraylist $this) {
        return $this.mixerDistValue;
    }

    public static final /* synthetic */ IntegerValue access$getMixerSecValue$p(Arraylist $this) {
        return $this.mixerSecValue;
    }

    public static final /* synthetic */ FloatValue access$getRainbowSaturation$p(Arraylist $this) {
        return $this.rainbowSaturation;
    }

    public static final /* synthetic */ IntegerValue access$getRainbowSpeeds$p(Arraylist $this) {
        return $this.rainbowSpeeds;
    }

    public static final /* synthetic */ IntegerValue access$getBackgroundColorAlphaValue$p(Arraylist $this) {
        return $this.backgroundColorAlphaValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorRedValue$p(Arraylist $this) {
        return $this.shadowColorRedValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorGreenValue$p(Arraylist $this) {
        return $this.shadowColorGreenValue;
    }

    public static final /* synthetic */ IntegerValue access$getShadowColorBlueValue$p(Arraylist $this) {
        return $this.shadowColorBlueValue;
    }

    public static final /* synthetic */ BoolValue access$getShadowNoCutValue$p(Arraylist $this) {
        return $this.shadowNoCutValue;
    }

    public static final /* synthetic */ BoolValue access$getTags$p(Arraylist $this) {
        return $this.tags;
    }

    public static final /* synthetic */ BoolValue access$getNameBreakValue$p(Arraylist $this) {
        return $this.nameBreakValue;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ FontValue access$getFontValue$p(Arraylist $this) {
        return $this.fontValue;
    }

    public static final /* synthetic */ BoolValue access$getUpperCaseValue$p(Arraylist $this) {
        return $this.upperCaseValue;
    }

    public static final /* synthetic */ BoolValue access$getTagsArrayColor$p(Arraylist $this) {
        return $this.tagsArrayColor;
    }
}

