/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector2f
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.report.liquidware.modules.render.HudColors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

@ElementInfo(name="Radar")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\n\u0010 \u001a\u0004\u0018\u00010!H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Radar;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "()V", "alphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "backgroundAlphaValue", "backgroundBlueValue", "backgroundGreenValue", "backgroundRedValue", "blueValue", "borderAlphaValue", "borderBlueValue", "borderGreenValue", "borderRedValue", "borderStrengthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "borderValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "cRainbowSecValue", "distanceValue", "exhiValue", "fovSizeValue", "gradientAmountValue", "greenValue", "lineValue", "playerShapeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "playerSizeValue", "rainbowList", "redValue", "sizeValue", "viewDistanceValue", "drawElement", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Border;", "Companion", "KyinoClient"})
public final class Radar
extends Element {
    private final FloatValue sizeValue = new FloatValue("Size", 90.0f, 30.0f, 500.0f);
    private final FloatValue viewDistanceValue = new FloatValue("View Distance", 4.0f, 0.5f, 32.0f);
    private final ListValue playerShapeValue = new ListValue("Player Shape", new String[]{"Rectangle", "Circle"}, "Triangle");
    private final FloatValue playerSizeValue = new FloatValue("Player Size", 2.0f, 0.5f, 20.0f);
    private final FloatValue fovSizeValue = new FloatValue("FOV Size", 10.0f, 0.0f, 50.0f);
    private final BoolValue exhiValue = new BoolValue("Use Skeet Rect", true);
    private final BoolValue lineValue = new BoolValue("Line", false);
    private final ListValue rainbowList = new ListValue("Line-Rainbow", new String[]{"Off", "CRainbow", "Sky", "LiquidSlowly", "Fade"}, "Off");
    private final IntegerValue redValue = new IntegerValue("Line Red", 255, 0, 255);
    private final IntegerValue greenValue = new IntegerValue("Line Green", 255, 0, 255);
    private final IntegerValue blueValue = new IntegerValue("Line Blue", 255, 0, 255);
    private final IntegerValue alphaValue = new IntegerValue("Line Alpha", 255, 0, 255);
    private final IntegerValue cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
    private final IntegerValue distanceValue = new IntegerValue("Line Distance", 0, 0, 400);
    private final IntegerValue gradientAmountValue = new IntegerValue("Gradient Amount", 25, 1, 50);
    private final IntegerValue backgroundRedValue = new IntegerValue("Background Red", 0, 0, 255);
    private final IntegerValue backgroundGreenValue = new IntegerValue("Background Green", 0, 0, 255);
    private final IntegerValue backgroundBlueValue = new IntegerValue("Background Blue", 0, 0, 255);
    private final IntegerValue backgroundAlphaValue = new IntegerValue("Background Alpha", 50, 0, 255);
    private final BoolValue borderValue = new BoolValue("Border", false);
    private final FloatValue borderStrengthValue = new FloatValue("Border Strength", 2.0f, 1.0f, 5.0f);
    private final IntegerValue borderRedValue = new IntegerValue("Border Red", 0, 0, 255);
    private final IntegerValue borderGreenValue = new IntegerValue("Border Green", 0, 0, 255);
    private final IntegerValue borderBlueValue = new IntegerValue("Border Blue", 0, 0, 255);
    private final IntegerValue borderAlphaValue = new IntegerValue("Border Alpha", 150, 0, 255);
    private static final float SQRT_OF_TWO;
    public static final Companion Companion;

    /*
     * WARNING - void declaration
     */
    @Override
    @Nullable
    public Border drawElement() {
        Tessellator tessellator;
        Minecraft minecraft = Radar.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        Entity renderViewEntity = minecraft.func_175606_aa();
        float size = ((Number)this.sizeValue.get()).floatValue();
        float viewDistance = ((Number)this.viewDistanceValue.get()).floatValue() * 16.0f;
        double maxDisplayableDistanceSquare = ((double)viewDistance + (double)((Number)this.fovSizeValue.get()).floatValue()) * ((double)viewDistance + (double)((Number)this.fovSizeValue.get()).floatValue());
        float halfSize = size / 2.0f;
        String rainbowType = (String)this.rainbowList.get();
        int cColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()).getRGB();
        if (((Boolean)this.exhiValue.get()).booleanValue()) {
            RenderUtils.drawExhiRect(0.0f, (Boolean)this.lineValue.get() != false ? -1.0f : 0.0f, size, size);
        } else {
            RenderUtils.drawRect(0.0f, 0.0f, size, size, new Color(((Number)this.backgroundRedValue.get()).intValue(), ((Number)this.backgroundGreenValue.get()).intValue(), ((Number)this.backgroundBlueValue.get()).intValue(), ((Number)this.backgroundAlphaValue.get()).intValue()).getRGB());
        }
        if (((Boolean)this.lineValue.get()).booleanValue()) {
            double barLength = size;
            HudColors hud = (HudColors)LiquidBounce.INSTANCE.getModuleManager().getModule(HudColors.class);
            int n = 0;
            int n2 = ((Number)this.gradientAmountValue.get()).intValue() - 1;
            if (n <= n2) {
                while (true) {
                    void i;
                    double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                    double barEnd = (double)(i + true) / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                    if (hud != null) {
                        int n3;
                        int n4;
                        switch (rainbowType) {
                            case "CRainbow": {
                                n4 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue(), (int)(i * ((Number)this.distanceValue.get()).intValue()));
                                break;
                            }
                            case "Sky": {
                                n4 = RenderUtils.SkyRainbow((int)(i * ((Number)this.distanceValue.get()).intValue()), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                                break;
                            }
                            case "LiquidSlowly": {
                                Color color = ColorUtils.LiquidSlowly(System.nanoTime(), (int)(i * ((Number)this.distanceValue.get()).intValue()), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                                if (color == null) {
                                    Intrinsics.throwNpe();
                                }
                                n4 = color.getRGB();
                                break;
                            }
                            case "Fade": {
                                n4 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)(i * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                                break;
                            }
                            default: {
                                n4 = cColor;
                            }
                        }
                        switch (rainbowType) {
                            case "CRainbow": {
                                n3 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue(), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()));
                                break;
                            }
                            case "Sky": {
                                n3 = RenderUtils.SkyRainbow((int)((i + true) * ((Number)this.distanceValue.get()).intValue()), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                                break;
                            }
                            case "LiquidSlowly": {
                                Color color = ColorUtils.LiquidSlowly(System.nanoTime(), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()), ((Number)hud.getSaturationValue().get()).floatValue(), ((Number)hud.getBrightnessValue().get()).floatValue());
                                if (color == null) {
                                    Intrinsics.throwNpe();
                                }
                                n3 = color.getRGB();
                                break;
                            }
                            case "Fade": {
                                n3 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (int)((i + true) * ((Number)this.distanceValue.get()).intValue()), 100).getRGB();
                                break;
                            }
                            default: {
                                n3 = cColor;
                            }
                        }
                        RenderUtils.drawGradientSideways(barStart, -1.0, barEnd, 0.0, n4, n3);
                    }
                    if (i == n2) break;
                    ++i;
                }
            }
        }
        if (((Boolean)this.borderValue.get()).booleanValue()) {
            float strength = ((Number)this.borderStrengthValue.get()).floatValue() / 2.0f;
            int borderColor = new Color(((Number)this.borderRedValue.get()).intValue(), ((Number)this.borderGreenValue.get()).intValue(), ((Number)this.borderBlueValue.get()).intValue(), ((Number)this.borderAlphaValue.get()).intValue()).getRGB();
            RenderUtils.drawRect(halfSize - strength, 0.0f, halfSize + strength, size, borderColor);
            RenderUtils.drawRect(0.0f, halfSize - strength, halfSize - strength, halfSize + strength, borderColor);
            RenderUtils.drawRect(halfSize + strength, halfSize - strength, size, halfSize + strength, borderColor);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        float f = (float)this.getX();
        float f2 = (float)this.getY();
        float f3 = (float)this.getX();
        boolean strength = false;
        float f4 = (float)Math.ceil(size);
        float f5 = f + f4;
        f4 = (float)this.getY();
        f = f5;
        strength = false;
        float f6 = (float)Math.ceil(size);
        RenderUtils.makeScissorBox(f3, f2, f, f4 + f6);
        GL11.glEnable((int)3089);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)halfSize, (float)halfSize, (float)0.0f);
        GL11.glRotatef((float)renderViewEntity.field_70177_z, (float)0.0f, (float)0.0f, (float)-1.0f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        boolean circleMode = StringsKt.equals((String)this.playerShapeValue.get(), "circle", true);
        Tessellator tessellator2 = tessellator = Tessellator.func_178181_a();
        Intrinsics.checkExpressionValueIsNotNull(tessellator2, "tessellator");
        WorldRenderer worldRenderer = tessellator2.func_178180_c();
        if (circleMode) {
            GL11.glEnable((int)2832);
        }
        float playerSize = ((Number)this.playerSizeValue.get()).floatValue();
        GL11.glEnable((int)2881);
        worldRenderer.func_181668_a(0, DefaultVertexFormats.field_181706_f);
        GL11.glPointSize((float)playerSize);
        for (Entity entity : Radar.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            boolean transform;
            Vector2f positionRelativeToPlayer;
            if (entity == null || entity == Radar.access$getMc$p$s1046033730().field_71439_g || !EntityUtils.isSelected(entity, false) || maxDisplayableDistanceSquare < (double)(positionRelativeToPlayer = new Vector2f((float)(renderViewEntity.field_70165_t - entity.field_70165_t), (float)(renderViewEntity.field_70161_v - entity.field_70161_v))).lengthSquared()) continue;
            boolean bl = transform = ((Number)this.fovSizeValue.get()).floatValue() > 0.0f;
            if (transform) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(positionRelativeToPlayer.x / viewDistance * size), (float)(positionRelativeToPlayer.y / viewDistance * size), (float)0.0f);
                GL11.glRotatef((float)entity.field_70177_z, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(ESP.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.ESP");
            }
            Color color = ((ESP)module).getColor(entity);
            WorldRenderer worldRenderer2 = worldRenderer.func_181662_b((double)(positionRelativeToPlayer.x / viewDistance * size), (double)(positionRelativeToPlayer.y / viewDistance * size), 0.0);
            Color color2 = color;
            Intrinsics.checkExpressionValueIsNotNull(color2, "color");
            worldRenderer2.func_181666_a((float)color2.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f).func_181675_d();
            if (!transform) continue;
            GL11.glPopMatrix();
        }
        tessellator.func_78381_a();
        if (circleMode) {
            GL11.glDisable((int)2832);
        }
        GL11.glDisable((int)2881);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        return new Border(0.0f, 0.0f, size, size);
    }

    public Radar() {
        super(0.0, 0.0, 0.0f, null, 15, null);
    }

    static {
        Companion = new Companion(null);
        float f = 2.0f;
        boolean bl = false;
        SQRT_OF_TWO = (float)Math.sqrt(f);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Radar$Companion;", "", "()V", "SQRT_OF_TWO", "", "KyinoClient"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

