/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public interface Graphics {
    public BaseTransform getTransformNoClone();

    public void setTransform(BaseTransform var1);

    public void setTransform(double var1, double var3, double var5, double var7, double var9, double var11);

    public void setTransform3D(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

    public void transform(BaseTransform var1);

    public void translate(float var1, float var2);

    public void translate(float var1, float var2, float var3);

    public void scale(float var1, float var2);

    public void scale(float var1, float var2, float var3);

    public void setPerspectiveTransform(GeneralTransform3D var1);

    public void setCamera(NGCamera var1);

    public NGCamera getCameraNoClone();

    public void setDepthTest(boolean var1);

    public boolean isDepthTest();

    public void setDepthBuffer(boolean var1);

    public boolean isDepthBuffer();

    public boolean isAlphaTestShader();

    public void setAntialiasedShape(boolean var1);

    public boolean isAntialiasedShape();

    public RectBounds getFinalClipNoClone();

    public Rectangle getClipRect();

    public Rectangle getClipRectNoClone();

    public void setHasPreCullingBits(boolean var1);

    public boolean hasPreCullingBits();

    public void setClipRect(Rectangle var1);

    public void setClipRectIndex(int var1);

    public int getClipRectIndex();

    public float getExtraAlpha();

    public void setExtraAlpha(float var1);

    public void setLights(NGLightBase[] var1);

    public NGLightBase[] getLights();

    public Paint getPaint();

    public void setPaint(Paint var1);

    public BasicStroke getStroke();

    public void setStroke(BasicStroke var1);

    public void setCompositeMode(CompositeMode var1);

    public CompositeMode getCompositeMode();

    public void clear();

    public void clear(Color var1);

    public void clearQuad(float var1, float var2, float var3, float var4);

    public void fill(Shape var1);

    public void fillQuad(float var1, float var2, float var3, float var4);

    public void fillRect(float var1, float var2, float var3, float var4);

    public void fillRoundRect(float var1, float var2, float var3, float var4, float var5, float var6);

    public void fillEllipse(float var1, float var2, float var3, float var4);

    public void draw(Shape var1);

    public void drawLine(float var1, float var2, float var3, float var4);

    public void drawRect(float var1, float var2, float var3, float var4);

    public void drawRoundRect(float var1, float var2, float var3, float var4, float var5, float var6);

    public void drawEllipse(float var1, float var2, float var3, float var4);

    public void setNodeBounds(RectBounds var1);

    public void drawString(GlyphList var1, FontStrike var2, float var3, float var4, Color var5, int var6, int var7);

    public void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public void drawTexture(Texture var1, float var2, float var3, float var4, float var5);

    public void drawTexture(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

    public void drawTexture3SliceH(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13);

    public void drawTexture3SliceV(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13);

    public void drawTexture9Slice(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17);

    public void drawTextureVO(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11);

    public void drawTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

    public void drawMappedTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13);

    public void sync();

    public Screen getAssociatedScreen();

    public ResourceFactory getResourceFactory();

    public RenderTarget getRenderTarget();

    public void setRenderRoot(NodePath var1);

    public NodePath getRenderRoot();

    public void setState3D(boolean var1);

    public boolean isState3D();

    public void setup3DRendering();

    public void setPixelScaleFactors(float var1, float var2);

    public float getPixelScaleFactorX();

    public float getPixelScaleFactorY();
}

