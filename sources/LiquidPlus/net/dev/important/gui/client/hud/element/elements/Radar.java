/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Vector2f
 */
package net.dev.important.gui.client.hud.element.elements;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.render.ESP;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.BlurUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

@ElementInfo(name="Radar", disableScale=true, priority=1)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\n\u0010'\u001a\u0004\u0018\u00010(H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020 X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Radar;", "Lnet/dev/important/gui/client/hud/element/Element;", "x", "", "y", "(DD)V", "alphaValue", "Lnet/dev/important/value/IntegerValue;", "backgroundAlphaValue", "backgroundBlueValue", "backgroundGreenValue", "backgroundRedValue", "blueValue", "blurStrength", "Lnet/dev/important/value/FloatValue;", "blurValue", "Lnet/dev/important/value/BoolValue;", "borderAlphaValue", "borderBlueValue", "borderGreenValue", "borderRedValue", "borderStrengthValue", "borderValue", "brightnessValue", "cRainbowSecValue", "distanceValue", "exhiValue", "fovSizeValue", "gradientAmountValue", "greenValue", "lineValue", "playerShapeValue", "Lnet/dev/important/value/ListValue;", "playerSizeValue", "rainbowList", "redValue", "saturationValue", "sizeValue", "viewDistanceValue", "drawElement", "Lnet/dev/important/gui/client/hud/element/Border;", "Companion", "LiquidBounce"})
public final class Radar
extends Element {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final BoolValue blurValue;
    @NotNull
    private final FloatValue blurStrength;
    @NotNull
    private final FloatValue sizeValue;
    @NotNull
    private final FloatValue viewDistanceValue;
    @NotNull
    private final ListValue playerShapeValue;
    @NotNull
    private final FloatValue playerSizeValue;
    @NotNull
    private final FloatValue fovSizeValue;
    @NotNull
    private final BoolValue exhiValue;
    @NotNull
    private final BoolValue lineValue;
    @NotNull
    private final ListValue rainbowList;
    @NotNull
    private final IntegerValue redValue;
    @NotNull
    private final IntegerValue greenValue;
    @NotNull
    private final IntegerValue blueValue;
    @NotNull
    private final IntegerValue alphaValue;
    @NotNull
    private final FloatValue saturationValue;
    @NotNull
    private final FloatValue brightnessValue;
    @NotNull
    private final IntegerValue cRainbowSecValue;
    @NotNull
    private final IntegerValue distanceValue;
    @NotNull
    private final IntegerValue gradientAmountValue;
    @NotNull
    private final IntegerValue backgroundRedValue;
    @NotNull
    private final IntegerValue backgroundGreenValue;
    @NotNull
    private final IntegerValue backgroundBlueValue;
    @NotNull
    private final IntegerValue backgroundAlphaValue;
    @NotNull
    private final BoolValue borderValue;
    @NotNull
    private final FloatValue borderStrengthValue;
    @NotNull
    private final IntegerValue borderRedValue;
    @NotNull
    private final IntegerValue borderGreenValue;
    @NotNull
    private final IntegerValue borderBlueValue;
    @NotNull
    private final IntegerValue borderAlphaValue;
    private static final float SQRT_OF_TWO = (float)Math.sqrt(2.0f);

    public Radar(double x, double y) {
        super(x, y, 0.0f, null, 12, null);
        this.blurValue = new BoolValue("Blur", false);
        this.blurStrength = new FloatValue("Blur-Strength", 0.0f, 0.0f, 30.0f);
        this.sizeValue = new FloatValue("Size", 90.0f, 30.0f, 500.0f);
        this.viewDistanceValue = new FloatValue("View Distance", 4.0f, 0.5f, 32.0f);
        String[] stringArray = new String[]{"Rectangle", "Circle"};
        this.playerShapeValue = new ListValue("Player Shape", stringArray, "Triangle");
        this.playerSizeValue = new FloatValue("Player Size", 2.0f, 0.5f, 20.0f);
        this.fovSizeValue = new FloatValue("FOV Size", 10.0f, 0.0f, 50.0f);
        this.exhiValue = new BoolValue("Use Exhi Rect", true);
        this.lineValue = new BoolValue("Line", false);
        stringArray = new String[]{"Off", "CRainbow", "Sky", "LiquidSlowly", "Fade", "Mixer"};
        this.rainbowList = new ListValue("Line-Rainbow", stringArray, "Off");
        this.redValue = new IntegerValue("Line-Red", 255, 0, 255);
        this.greenValue = new IntegerValue("Line-Green", 255, 0, 255);
        this.blueValue = new IntegerValue("Line-Blue", 255, 0, 255);
        this.alphaValue = new IntegerValue("Line-Alpha", 255, 0, 255);
        this.saturationValue = new FloatValue("Saturation", 0.9f, 0.0f, 1.0f);
        this.brightnessValue = new FloatValue("Brightness", 1.0f, 0.0f, 1.0f);
        this.cRainbowSecValue = new IntegerValue("Seconds", 2, 1, 10);
        this.distanceValue = new IntegerValue("Line-Distance", 0, 0, 400);
        this.gradientAmountValue = new IntegerValue("Gradient-Amount", 25, 1, 50);
        this.backgroundRedValue = new IntegerValue("Background Red", 0, 0, 255);
        this.backgroundGreenValue = new IntegerValue("Background Green", 0, 0, 255);
        this.backgroundBlueValue = new IntegerValue("Background Blue", 0, 0, 255);
        this.backgroundAlphaValue = new IntegerValue("Background Alpha", 50, 0, 255);
        this.borderValue = new BoolValue("Border", false);
        this.borderStrengthValue = new FloatValue("Border Strength", 2.0f, 1.0f, 5.0f);
        this.borderRedValue = new IntegerValue("Border Red", 0, 0, 255);
        this.borderGreenValue = new IntegerValue("Border Green", 0, 0, 255);
        this.borderBlueValue = new IntegerValue("Border Blue", 0, 0, 255);
        this.borderAlphaValue = new IntegerValue("Border Alpha", 150, 0, 255);
    }

    public /* synthetic */ Radar(double d, double d2, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 5.0;
        }
        if ((n & 2) != 0) {
            d2 = 130.0;
        }
        this(d, d2);
    }

    @Override
    @Nullable
    public Border drawElement() {
        float halfSize;
        double maxDisplayableDistanceSquare;
        float viewDistance;
        float size;
        Entity renderViewEntity;
        block48: {
            int i;
            renderViewEntity = MinecraftInstance.mc.func_175606_aa();
            size = ((Number)this.sizeValue.get()).floatValue();
            viewDistance = ((Number)this.viewDistanceValue.get()).floatValue() * 16.0f;
            maxDisplayableDistanceSquare = ((double)viewDistance + (double)((Number)this.fovSizeValue.get()).floatValue()) * ((double)viewDistance + (double)((Number)this.fovSizeValue.get()).floatValue());
            halfSize = size / 2.0f;
            String rainbowType = (String)this.rainbowList.get();
            int cColor = new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue(), ((Number)this.alphaValue.get()).intValue()).getRGB();
            if (((Boolean)this.blurValue.get()).booleanValue()) {
                float floatX = (float)this.getRenderX();
                float floatY = (float)this.getRenderY();
                GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
                GL11.glPushMatrix();
                BlurUtils.blurArea(floatX, floatY, floatX + size, floatY + size, ((Number)this.blurStrength.get()).floatValue());
                GL11.glPopMatrix();
                GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
            }
            if (((Boolean)this.exhiValue.get()).booleanValue()) {
                RenderUtils.drawExhiRect(0.0f, (Boolean)this.lineValue.get() != false ? -1.0f : 0.0f, size, size);
            } else {
                RenderUtils.drawRect(0.0f, 0.0f, size, size, new Color(((Number)this.backgroundRedValue.get()).intValue(), ((Number)this.backgroundGreenValue.get()).intValue(), ((Number)this.backgroundBlueValue.get()).intValue(), ((Number)this.backgroundAlphaValue.get()).intValue()).getRGB());
            }
            if (!((Boolean)this.lineValue.get()).booleanValue()) break block48;
            double barLength = size;
            int n = 0;
            int n2 = ((Number)this.gradientAmountValue.get()).intValue() - 1;
            if (n > n2) break block48;
            do {
                int n3;
                int n4;
                i = n++;
                double barStart = (double)i / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                double barEnd = (double)(i + 1) / (double)((Number)this.gradientAmountValue.get()).intValue() * barLength;
                switch (rainbowType) {
                    case "CRainbow": {
                        n4 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), i * ((Number)this.distanceValue.get()).intValue());
                        break;
                    }
                    case "Sky": {
                        n4 = RenderUtils.SkyRainbow(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "LiquidSlowly": {
                        Color color = ColorUtils.LiquidSlowly(System.nanoTime(), i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        Intrinsics.checkNotNull(color);
                        n4 = color.getRGB();
                        break;
                    }
                    case "Mixer": {
                        n4 = ColorMixer.getMixedColor(i * ((Number)this.distanceValue.get()).intValue(), ((Number)this.cRainbowSecValue.get()).intValue()).getRGB();
                        break;
                    }
                    case "Fade": {
                        n4 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), i * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                        break;
                    }
                    default: {
                        n4 = cColor;
                    }
                }
                switch (rainbowType) {
                    case "CRainbow": {
                        n3 = RenderUtils.getRainbowOpaque(((Number)this.cRainbowSecValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue(), (i + 1) * ((Number)this.distanceValue.get()).intValue());
                        break;
                    }
                    case "Sky": {
                        n3 = RenderUtils.SkyRainbow((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        break;
                    }
                    case "LiquidSlowly": {
                        Color color = ColorUtils.LiquidSlowly(System.nanoTime(), (i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.saturationValue.get()).floatValue(), ((Number)this.brightnessValue.get()).floatValue());
                        Intrinsics.checkNotNull(color);
                        n3 = color.getRGB();
                        break;
                    }
                    case "Mixer": {
                        n3 = ColorMixer.getMixedColor((i + 1) * ((Number)this.distanceValue.get()).intValue(), ((Number)this.cRainbowSecValue.get()).intValue()).getRGB();
                        break;
                    }
                    case "Fade": {
                        n3 = ColorUtils.fade(new Color(((Number)this.redValue.get()).intValue(), ((Number)this.greenValue.get()).intValue(), ((Number)this.blueValue.get()).intValue()), (i + 1) * ((Number)this.distanceValue.get()).intValue(), 100).getRGB();
                        break;
                    }
                    default: {
                        n3 = cColor;
                    }
                }
                RenderUtils.drawGradientSideways(barStart, -1.0, barEnd, 0.0, n4, n3);
            } while (i != n2);
        }
        if (((Boolean)this.borderValue.get()).booleanValue()) {
            float strength = ((Number)this.borderStrengthValue.get()).floatValue() / 2.0f;
            int borderColor = new Color(((Number)this.borderRedValue.get()).intValue(), ((Number)this.borderGreenValue.get()).intValue(), ((Number)this.borderBlueValue.get()).intValue(), ((Number)this.borderAlphaValue.get()).intValue()).getRGB();
            RenderUtils.drawRect(halfSize - strength, 0.0f, halfSize + strength, size, borderColor);
            RenderUtils.drawRect(0.0f, halfSize - strength, halfSize - strength, halfSize + strength, borderColor);
            RenderUtils.drawRect(halfSize + strength, halfSize - strength, size, halfSize + strength, borderColor);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        RenderUtils.makeScissorBox((float)this.getX(), (float)this.getY(), (float)this.getX() + (float)Math.ceil(size), (float)this.getY() + (float)Math.ceil(size));
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
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        if (circleMode) {
            GL11.glEnable((int)2832);
        }
        float playerSize = ((Number)this.playerSizeValue.get()).floatValue();
        GL11.glEnable((int)2881);
        worldRenderer.func_181668_a(0, DefaultVertexFormats.field_181706_f);
        GL11.glPointSize((float)playerSize);
        for (Entity entity : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            boolean transform;
            Vector2f positionRelativeToPlayer;
            if (entity == null || entity == MinecraftInstance.mc.field_71439_g || !EntityUtils.isSelected(entity, false) || maxDisplayableDistanceSquare < (double)(positionRelativeToPlayer = new Vector2f((float)(renderViewEntity.field_70165_t - entity.field_70165_t), (float)(renderViewEntity.field_70161_v - entity.field_70161_v))).lengthSquared()) continue;
            boolean bl = transform = ((Number)this.fovSizeValue.get()).floatValue() > 0.0f;
            if (transform) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(positionRelativeToPlayer.x / viewDistance * size), (float)(positionRelativeToPlayer.y / viewDistance * size), (float)0.0f);
                GL11.glRotatef((float)entity.field_70177_z, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            Module module2 = Client.INSTANCE.getModuleManager().get(ESP.class);
            if (module2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.render.ESP");
            }
            Color color = ((ESP)module2).getColor(entity);
            worldRenderer.func_181662_b((double)(positionRelativeToPlayer.x / viewDistance * size), (double)(positionRelativeToPlayer.y / viewDistance * size), 0.0).func_181666_a((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f).func_181675_d();
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
        this(0.0, 0.0, 3, (DefaultConstructorMarker)null);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lnet/dev/important/gui/client/hud/element/elements/Radar$Companion;", "", "()V", "SQRT_OF_TWO", "", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

