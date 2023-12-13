package com.sun.scenario.effect.impl.prism.ps;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Map;

public class PPSRenderer extends PrRenderer {
   private final ResourceFactory rf;
   private final ShaderSource shaderSource;
   private Renderer.RendererState state;
   private boolean needsSWDispMap;
   private final ResourceFactoryListener listener = new ResourceFactoryListener() {
      public void factoryReset() {
         PPSRenderer.this.dispose();
      }

      public void factoryReleased() {
         PPSRenderer.this.dispose();
      }
   };

   private PPSRenderer(Screen var1, ShaderSource var2) {
      this.shaderSource = var2;
      synchronized(this) {
         this.state = Renderer.RendererState.OK;
      }

      this.rf = GraphicsPipeline.getPipeline().getResourceFactory(var1);
      this.rf.addFactoryListener(this.listener);
      this.needsSWDispMap = !this.rf.isFormatSupported(PixelFormat.FLOAT_XYZW);
   }

   public PrDrawable createDrawable(RTTexture var1) {
      return PPSDrawable.create(var1);
   }

   public Effect.AccelType getAccelType() {
      return this.shaderSource.getAccelType();
   }

   public synchronized Renderer.RendererState getRendererState() {
      return this.state;
   }

   protected Renderer getBackupRenderer() {
      return this;
   }

   protected void dispose() {
      Iterator var1 = this.getPeers().iterator();

      while(var1.hasNext()) {
         EffectPeer var2 = (EffectPeer)var1.next();
         var2.dispose();
      }

      synchronized(this) {
         this.state = Renderer.RendererState.DISPOSED;
      }

      this.rf.removeFactoryListener(this.listener);
   }

   protected final synchronized void markLost() {
      if (this.state == Renderer.RendererState.OK) {
         this.state = Renderer.RendererState.LOST;
      }

   }

   public int getCompatibleWidth(int var1) {
      return PPSDrawable.getCompatibleWidth(this.rf, var1);
   }

   public int getCompatibleHeight(int var1) {
      return PPSDrawable.getCompatibleHeight(this.rf, var1);
   }

   public final PPSDrawable createCompatibleImage(int var1, int var2) {
      return PPSDrawable.create(this.rf, var1, var2);
   }

   public PPSDrawable getCompatibleImage(int var1, int var2) {
      PPSDrawable var3 = (PPSDrawable)super.getCompatibleImage(var1, var2);
      if (var3 == null) {
         this.markLost();
      }

      return var3;
   }

   public LockableResource createFloatTexture(int var1, int var2) {
      Texture var3 = this.rf.createFloatTexture(var1, var2);
      return new PrTexture(var3);
   }

   public void updateFloatTexture(LockableResource var1, FloatMap var2) {
      FloatBuffer var3 = var2.getBuffer();
      int var4 = var2.getWidth();
      int var5 = var2.getHeight();
      Image var6 = Image.fromFloatMapData(var3, var4, var5);
      Texture var7 = ((PrTexture)var1).getTextureObject();
      var7.update(var6);
   }

   public Shader createShader(String var1, Map var2, Map var3, boolean var4) {
      if (PrismSettings.verbose) {
         System.out.println("PPSRenderer: scenario.effect - createShader: " + var1);
      }

      InputStream var5 = this.shaderSource.loadSource(var1);
      int var6 = var2.keySet().size() - 1;
      ShaderFactory var7 = (ShaderFactory)this.rf;
      return var7.createShader(var5, var2, var3, var6, var4, false);
   }

   private EffectPeer createIntrinsicPeer(FilterContext var1, String var2) {
      Class var3 = null;

      try {
         var3 = Class.forName("com.sun.scenario.effect.impl.prism.Pr" + var2 + "Peer");
         Constructor var5 = var3.getConstructor(FilterContext.class, Renderer.class, String.class);
         EffectPeer var4 = (EffectPeer)var5.newInstance(var1, this, var2);
         return var4;
      } catch (Exception var6) {
         return null;
      }
   }

   private EffectPeer createPlatformPeer(FilterContext var1, String var2, int var3) {
      String var5 = var2;
      if (var3 > 0) {
         var5 = var2 + "_" + var3;
      }

      try {
         Class var6 = Class.forName("com.sun.scenario.effect.impl.prism.ps.PPS" + var2 + "Peer");
         Constructor var7 = var6.getConstructor(FilterContext.class, Renderer.class, String.class);
         EffectPeer var4 = (EffectPeer)var7.newInstance(var1, this, var5);
         return var4;
      } catch (Exception var8) {
         System.err.println("Error: Prism peer not found for: " + var2 + " due to error: " + var8.getMessage());
         return null;
      }
   }

   protected EffectPeer createPeer(FilterContext var1, String var2, int var3) {
      if (PrRenderer.isIntrinsicPeer(var2)) {
         return this.createIntrinsicPeer(var1, var2);
      } else if (this.needsSWDispMap && var2.equals("DisplacementMap")) {
         PrFilterContext var4 = ((PrFilterContext)var1).getSoftwareInstance();
         return new PPStoPSWDisplacementMapPeer(var4, this, var2);
      } else {
         return this.createPlatformPeer(var1, var2, var3);
      }
   }

   public boolean isImageDataCompatible(ImageData var1) {
      if (this.getRendererState() != Renderer.RendererState.OK) {
         return false;
      } else {
         Filterable var2 = var1.getUntransformedImage();
         return var2 instanceof PrDrawable && !var2.isLost();
      }
   }

   public void clearImage(Filterable var1) {
      PPSDrawable var2 = (PPSDrawable)var1;
      var2.clear();
   }

   public ImageData createImageData(FilterContext var1, Filterable var2) {
      if (!(var2 instanceof PrImage)) {
         throw new IllegalArgumentException("Identity source must be PrImage");
      } else {
         Image var3 = ((PrImage)var2).getImage();
         int var4 = var3.getWidth();
         int var5 = var3.getHeight();
         PPSDrawable var6 = this.createCompatibleImage(var4, var5);
         if (var6 == null) {
            return null;
         } else {
            ShaderGraphics var7 = var6.createGraphics();
            ResourceFactory var8 = var7.getResourceFactory();
            Texture var9 = var8.createTexture(var3, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
            var7.drawTexture(var9, 0.0F, 0.0F, (float)var4, (float)var5);
            var7.sync();
            var9.dispose();
            float var11 = var3.getPixelScale();
            BaseTransform var10;
            if (var11 != 1.0F) {
               var11 = 1.0F / var11;
               var10 = BaseTransform.getScaleInstance((double)var11, (double)var11);
            } else {
               var10 = BaseTransform.IDENTITY_TRANSFORM;
            }

            ImageData var12 = new ImageData(var1, var6, new Rectangle(var4, var5), var10);
            return var12;
         }
      }
   }

   public Filterable transform(FilterContext var1, Filterable var2, BaseTransform var3, Rectangle var4, Rectangle var5) {
      PPSDrawable var6 = this.getCompatibleImage(var5.width, var5.height);
      if (var6 != null) {
         ShaderGraphics var7 = var6.createGraphics();
         var7.translate((float)(-var5.x), (float)(-var5.y));
         var7.transform(var3);
         var7.drawTexture(((PPSDrawable)var2).getTextureObject(), (float)var4.x, (float)var4.y, (float)var4.width, (float)var4.height);
      }

      return var6;
   }

   public ImageData transform(FilterContext var1, ImageData var2, BaseTransform var3, Rectangle var4, Rectangle var5) {
      PPSDrawable var6 = this.getCompatibleImage(var5.width, var5.height);
      if (var6 != null) {
         PPSDrawable var7 = (PPSDrawable)var2.getUntransformedImage();
         ShaderGraphics var8 = var6.createGraphics();
         var8.translate((float)(-var5.x), (float)(-var5.y));
         var8.transform(var3);
         var8.drawTexture(var7.getTextureObject(), (float)var4.x, (float)var4.y, (float)var4.width, (float)var4.height);
      }

      var2.unref();
      return new ImageData(var1, var6, var5);
   }

   private static ShaderSource createShaderSource(String var0) {
      Class var1 = null;

      try {
         var1 = Class.forName(var0);
         return (ShaderSource)var1.newInstance();
      } catch (ClassNotFoundException var3) {
         System.err.println(var0 + " class not found");
         return null;
      } catch (Throwable var4) {
         return null;
      }
   }

   public static Renderer createRenderer(FilterContext var0) {
      Object var1 = var0.getReferent();
      GraphicsPipeline var2 = GraphicsPipeline.getPipeline();
      if (var2 != null && var1 instanceof Screen) {
         Screen var3 = (Screen)var1;
         ShaderSource var4 = null;
         if (var2.supportsShader(GraphicsPipeline.ShaderType.HLSL, GraphicsPipeline.ShaderModel.SM3)) {
            var4 = createShaderSource("com.sun.scenario.effect.impl.hw.d3d.D3DShaderSource");
         } else {
            if (!var2.supportsShader(GraphicsPipeline.ShaderType.GLSL, GraphicsPipeline.ShaderModel.SM3)) {
               throw new InternalError("Unknown GraphicsPipeline");
            }

            var4 = createShaderSource("com.sun.scenario.effect.impl.es2.ES2ShaderSource");
         }

         return var4 == null ? null : new PPSRenderer(var3, var4);
      } else {
         return null;
      }
   }
}
