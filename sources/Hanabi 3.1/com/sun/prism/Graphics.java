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
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public interface Graphics {
   BaseTransform getTransformNoClone();

   void setTransform(BaseTransform var1);

   void setTransform(double var1, double var3, double var5, double var7, double var9, double var11);

   void setTransform3D(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

   void transform(BaseTransform var1);

   void translate(float var1, float var2);

   void translate(float var1, float var2, float var3);

   void scale(float var1, float var2);

   void scale(float var1, float var2, float var3);

   void setPerspectiveTransform(GeneralTransform3D var1);

   void setCamera(NGCamera var1);

   NGCamera getCameraNoClone();

   void setDepthTest(boolean var1);

   boolean isDepthTest();

   void setDepthBuffer(boolean var1);

   boolean isDepthBuffer();

   boolean isAlphaTestShader();

   void setAntialiasedShape(boolean var1);

   boolean isAntialiasedShape();

   RectBounds getFinalClipNoClone();

   Rectangle getClipRect();

   Rectangle getClipRectNoClone();

   void setHasPreCullingBits(boolean var1);

   boolean hasPreCullingBits();

   void setClipRect(Rectangle var1);

   void setClipRectIndex(int var1);

   int getClipRectIndex();

   float getExtraAlpha();

   void setExtraAlpha(float var1);

   void setLights(NGLightBase[] var1);

   NGLightBase[] getLights();

   Paint getPaint();

   void setPaint(Paint var1);

   BasicStroke getStroke();

   void setStroke(BasicStroke var1);

   void setCompositeMode(CompositeMode var1);

   CompositeMode getCompositeMode();

   void clear();

   void clear(Color var1);

   void clearQuad(float var1, float var2, float var3, float var4);

   void fill(Shape var1);

   void fillQuad(float var1, float var2, float var3, float var4);

   void fillRect(float var1, float var2, float var3, float var4);

   void fillRoundRect(float var1, float var2, float var3, float var4, float var5, float var6);

   void fillEllipse(float var1, float var2, float var3, float var4);

   void draw(Shape var1);

   void drawLine(float var1, float var2, float var3, float var4);

   void drawRect(float var1, float var2, float var3, float var4);

   void drawRoundRect(float var1, float var2, float var3, float var4, float var5, float var6);

   void drawEllipse(float var1, float var2, float var3, float var4);

   void setNodeBounds(RectBounds var1);

   void drawString(GlyphList var1, FontStrike var2, float var3, float var4, Color var5, int var6, int var7);

   void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

   void drawTexture(Texture var1, float var2, float var3, float var4, float var5);

   void drawTexture(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

   void drawTexture3SliceH(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13);

   void drawTexture3SliceV(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13);

   void drawTexture9Slice(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17);

   void drawTextureVO(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11);

   void drawTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

   void drawMappedTextureRaw(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13);

   void sync();

   Screen getAssociatedScreen();

   ResourceFactory getResourceFactory();

   RenderTarget getRenderTarget();

   void setRenderRoot(NodePath var1);

   NodePath getRenderRoot();

   void setState3D(boolean var1);

   boolean isState3D();

   void setup3DRendering();

   void setPixelScaleFactors(float var1, float var2);

   float getPixelScaleFactorX();

   float getPixelScaleFactorY();
}
