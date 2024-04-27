/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.OpenGlHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import dev.sakura_starring.util.safe.QQUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import liying.utils.blur.BlurBuffer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.PictureColor;
import net.ccbluex.liquidbounce.features.module.modules.render.PictureColor2;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="QQLogo")
public final class QQLogo
extends Element {
    private FloatValue x5;
    private IntegerValue x4;
    private final BufferedImage bufferedImage2;
    private IntegerValue x3;
    private IntegerValue x2;
    private boolean got;
    private float easingHealth;
    private IResourceLocation resourcelocation3;
    private IntegerValue x1 = new IntegerValue("x1", 2, 1, 50);
    private HUD blurmodule;
    private IResourceLocation resourcelocation4;
    private final PictureColor2 color2;
    private final PictureColor color;

    public final BufferedImage getBufferedImage2() {
        return this.bufferedImage2;
    }

    public final void setX5(FloatValue floatValue) {
        this.x5 = floatValue;
    }

    public final IntegerValue getX2() {
        return this.x2;
    }

    public final FloatValue getX5() {
        return this.x5;
    }

    public final HUD getBlurmodule() {
        return this.blurmodule;
    }

    public final IntegerValue getX1() {
        return this.x1;
    }

    public final void setResourcelocation3(IResourceLocation iResourceLocation) {
        this.resourcelocation3 = iResourceLocation;
    }

    public final PictureColor2 getColor2() {
        return this.color2;
    }

    public final void setBlurmodule(HUD hUD) {
        this.blurmodule = hUD;
    }

    public final void setX1(IntegerValue integerValue) {
        this.x1 = integerValue;
    }

    public final void setX3(IntegerValue integerValue) {
        this.x3 = integerValue;
    }

    public final IResourceLocation getResourcelocation4() {
        return this.resourcelocation4;
    }

    public final void setX2(IntegerValue integerValue) {
        this.x2 = integerValue;
    }

    @Override
    public Border drawElement() {
        this.updateAnim();
        Color color = Color.WHITE;
        Color color2 = Color.WHITE;
        Color color3 = Color.WHITE;
        Color color4 = Color.WHITE;
        ColorUtils colorUtils = ColorUtils.INSTANCE;
        color = colorUtils.interpolateColorsBackAndForth(10, 20, new Color(((Number)this.color.getColorRedValue().get()).intValue(), ((Number)this.color.getColorGreenValue().get()).intValue(), ((Number)this.color.getColorBlueValue().get()).intValue(), ((Number)this.color.getColoralpha().get()).intValue()), new Color(((Number)this.color2.getColorRedValue().get()).intValue(), ((Number)this.color2.getColorGreenValue().get()).intValue(), ((Number)this.color2.getColorBlueValue().get()).intValue(), ((Number)this.color2.getColoralpha().get()).intValue()), false);
        color2 = colorUtils.interpolateColorsBackAndForth(20, 90, new Color(((Number)this.color2.getColorRedValue().get()).intValue(), ((Number)this.color2.getColorGreenValue().get()).intValue(), ((Number)this.color2.getColorBlueValue().get()).intValue(), ((Number)this.color.getColoralpha().get()).intValue()), new Color(((Number)this.color2.getColorRedValue().get()).intValue(), ((Number)this.color2.getColorGreenValue().get()).intValue(), ((Number)this.color2.getColorBlueValue().get()).intValue(), ((Number)this.color2.getColoralpha().get()).intValue()), false);
        color3 = colorUtils.interpolateColorsBackAndForth(20, 270, new Color(((Number)this.color.getColorRedValue().get()).intValue(), ((Number)this.color.getColorGreenValue().get()).intValue(), ((Number)this.color.getColorBlueValue().get()).intValue(), ((Number)this.color.getColoralpha().get()).intValue()), new Color(((Number)this.color.getColorRedValue().get()).intValue(), ((Number)this.color.getColorGreenValue().get()).intValue(), ((Number)this.color.getColorBlueValue().get()).intValue(), ((Number)this.color2.getColoralpha().get()).intValue()), false);
        color4 = colorUtils.interpolateColorsBackAndForth(20, 270, new Color(((Number)this.color2.getColorRedValue().get()).intValue(), ((Number)this.color2.getColorGreenValue().get()).intValue(), ((Number)this.color2.getColorBlueValue().get()).intValue(), ((Number)this.color.getColoralpha().get()).intValue()), new Color(((Number)this.color2.getColorRedValue().get()).intValue(), ((Number)this.color2.getColorGreenValue().get()).intValue(), ((Number)this.color2.getColorBlueValue().get()).intValue(), ((Number)this.color2.getColoralpha().get()).intValue()), false);
        if (((Boolean)this.blurmodule.getBlurValue().get()).booleanValue()) {
            GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
            BlurBuffer.blurRoundArea((float)this.getRenderX() + 9.0f, (float)this.getRenderY() - 16.0f, 100.0f, 36.0f, 4);
            GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        }
        RoundedUtil.drawGradientRound(9.0f, -16.0f, 100.0f, 36.0f, 4.0f, color, color3, color2, color);
        Fonts.font40.drawString("AtField", 50.0f, -0.0f, new Color(255, 255, 255, 255).getRGB());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_153190_b((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (!this.got) {
            ITextureManager iTextureManager = MinecraftInstance.mc.getTextureManager();
            BufferedImage bufferedImage = this.bufferedImage2;
            iTextureManager.loadTexture(this.resourcelocation4, MinecraftInstance.classProvider.createDynamicTexture(bufferedImage));
            this.got = true;
        }
        Stencil.write(false);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        RoundedUtil.drawGradientRound(16.0f, -11.0f, 27.0f, 27.0f, 4.0f, color, color3, color2, color);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        Stencil.erase(true);
        MinecraftInstance.mc.getTextureManager().bindTexture(this.resourcelocation3);
        GL11.glPushMatrix();
        GL11.glScaled((double)0.7, (double)0.7, (double)0.7);
        Gui.func_146110_a((int)20, (int)-20, (float)0.0f, (float)0.0f, (int)45, (int)60, (float)45.0f, (float)45.0f);
        GL11.glPopMatrix();
        Stencil.dispose();
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        return new Border(-13.0f, -16.0f, 102.0f, 52.0f);
    }

    public final void setX4(IntegerValue integerValue) {
        this.x4 = integerValue;
    }

    public final PictureColor getColor() {
        return this.color;
    }

    public QQLogo() {
        this.x2 = new IntegerValue("x2", 2, 1, 50);
        this.x3 = new IntegerValue("x3", 2, 1, 150);
        this.x4 = new IntegerValue("x4", 2, 1, 100);
        this.x5 = new FloatValue("x5", 2.0f, 1.0f, 20.0f);
        this.resourcelocation4 = MinecraftInstance.classProvider.createResourceLocation("\u4ec0\u4e48\u73a9\u610f\u5988\u5988\u66b4\u6bd9\u662f\u4e0d\u662f\u5f53\u5e74\u6211\u628a\u4f60\u5a4a\u5b50\u5988\u7684\u81ed\u903c\u7528\u65e5\u672c\u6b66\u58eb\u5200\u5272\u4e0b\u6765\u7096\u70c2\u653e\u5230\u79bb\u5fc3\u673a\u91cc\u628a\u4f60\u5988\u7684\u81ed\u903c\u52a0\u4e0a\u5b50\u5bab\u7528\u4e00\u767e\u4e07\u8f6c\u6bcf\u79d2\u7684\u8f6c\u901f\u7ed9\u4f60\u79bb\u5fc3\u51fa\u6765\u4f60\u8fd9\u4e2a\u5e9f\u7269\u4e1c\u897f\u4e00\u6837\u6211\u770b\u4f60\u4e0d\u4e09\u4e0d\u56db\u4e0d\u75db\u4e0d\u75d2\u7684\u4e00\u76f4\u865a\u7a7a\u5bf9\u7ebf\u662f\u4e0d\u662f\u60f3\u5ff5\u725b\u97ad\u8ddf\u4f60\u5988\u7684\u81ed\u903c\u7096\u5728\u4e00\u8d77\u7684B\u5473\u9053\u4e86\u554a");
        this.resourcelocation3 = MinecraftInstance.classProvider.createResourceLocation("\u4ec0\u4e48\u73a9\u610f\u5988\u5988\u66b4\u6bd9\u662f\u4e0d\u662f\u5f53\u5e74\u6211\u628a\u4f60\u5a4a\u5b50\u5988\u7684\u81ed\u903c\u7528\u65e5\u672c\u6b66\u58eb\u5200\u5272\u4e0b\u6765\u7096\u70c2\u653e\u5230\u79bb\u5fc3\u673a\u91cc\u628a\u4f60\u5988\u7684\u81ed\u903c\u52a0\u4e0a\u5b50\u5bab\u7528\u4e00\u767e\u4e07\u8f6c\u6bcf\u79d2\u7684\u8f6c\u901f\u7ed9\u4f60\u79bb\u5fc3\u51fa\u6765\u4f60\u8fd9\u4e2a\u5e9f\u7269\u4e1c\u897f\u4e00\u6837\u6211\u770b\u4f60\u4e0d\u4e09\u4e0d\u56db\u4e0d\u75db\u4e0d\u75d2\u7684\u4e00\u76f4\u865a\u7a7a\u5bf9\u7ebf\u662f\u4e0d\u662f\u60f3\u5ff5\u725b\u97ad\u8ddf\u4f60\u5988\u7684\u81ed\u903c\u7096\u5728\u4e00\u8d77\u7684B\u5473\u9053\u4e86\u554a");
        try {
            this.bufferedImage2 = ImageIO.read(new URL("https://q.qlogo.cn/headimg_dl?dst_uin=" + QQUtils.qqNumber + "&spec=640&img_type=png"));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(PictureColor.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.PictureColor");
        }
        this.color = (PictureColor)module;
        Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(PictureColor2.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.PictureColor2");
        }
        this.color2 = (PictureColor2)module2;
        Module module3 = LiquidBounce.INSTANCE.getModuleManager().getModule(HUD.class);
        if (module3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.HUD");
        }
        this.blurmodule = (HUD)module3;
    }

    public final IntegerValue getX3() {
        return this.x3;
    }

    public final IResourceLocation getResourcelocation3() {
        return this.resourcelocation3;
    }

    public final void updateAnim() {
        QQLogo qQLogo = this;
        float f = qQLogo.easingHealth;
        IEntityPlayerSP iEntityPlayerSP = Objects.requireNonNull(MinecraftInstance.mc.getThePlayer());
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        float f2 = 2.0f;
        float f3 = 8.5f;
        float f4 = iEntityPlayerSP.getHealth() - this.easingHealth;
        float f5 = f;
        QQLogo qQLogo2 = qQLogo;
        boolean bl = false;
        float f6 = (float)Math.pow(f2, f3);
        qQLogo2.easingHealth = f5 + f4 / f6 * (float)RenderUtils.deltaTime;
    }

    public final IntegerValue getX4() {
        return this.x4;
    }

    public final void setResourcelocation4(IResourceLocation iResourceLocation) {
        this.resourcelocation4 = iResourceLocation;
    }
}

