package com.sun.scenario.effect.impl.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.sw.RendererDelegate;
import java.lang.reflect.Constructor;

public class PSWRenderer extends PrRenderer {
   private final Screen screen;
   private final ResourceFactory resourceFactory;
   private final RendererDelegate delegate;
   private Renderer.RendererState state;

   private PSWRenderer(Screen var1, RendererDelegate var2) {
      this.screen = var1;
      this.resourceFactory = null;
      this.delegate = var2;
      synchronized(this) {
         this.state = Renderer.RendererState.OK;
      }
   }

   private PSWRenderer(ResourceFactory var1, RendererDelegate var2) {
      this.screen = null;
      this.resourceFactory = var1;
      this.delegate = var2;
      synchronized(this) {
         this.state = Renderer.RendererState.OK;
      }
   }

   public PrDrawable createDrawable(RTTexture var1) {
      return PSWDrawable.create(var1);
   }

   public static synchronized PSWRenderer createJSWInstance(Screen var0) {
      PSWRenderer var1 = null;

      try {
         Class var2 = Class.forName("com.sun.scenario.effect.impl.sw.java.JSWRendererDelegate");
         RendererDelegate var3 = (RendererDelegate)var2.newInstance();
         var1 = new PSWRenderer(var0, var3);
      } catch (Throwable var4) {
      }

      return var1;
   }

   public static synchronized PSWRenderer createJSWInstance(ResourceFactory var0) {
      PSWRenderer var1 = null;

      try {
         Class var2 = Class.forName("com.sun.scenario.effect.impl.sw.java.JSWRendererDelegate");
         RendererDelegate var3 = (RendererDelegate)var2.newInstance();
         var1 = new PSWRenderer(var0, var3);
      } catch (Throwable var4) {
      }

      return var1;
   }

   public static synchronized PSWRenderer createJSWInstance(FilterContext var0) {
      PSWRenderer var1 = null;

      try {
         ResourceFactory var2 = (ResourceFactory)var0.getReferent();
         var1 = createJSWInstance(var2);
      } catch (Throwable var3) {
      }

      return var1;
   }

   private static synchronized PSWRenderer createSSEInstance(Screen var0) {
      PSWRenderer var1 = null;

      try {
         Class var2 = Class.forName("com.sun.scenario.effect.impl.sw.sse.SSERendererDelegate");
         RendererDelegate var3 = (RendererDelegate)var2.newInstance();
         var1 = new PSWRenderer(var0, var3);
      } catch (Throwable var4) {
      }

      return var1;
   }

   public static Renderer createRenderer(FilterContext var0) {
      Object var1 = var0.getReferent();
      GraphicsPipeline var2 = GraphicsPipeline.getPipeline();
      if (var2 != null && var1 instanceof Screen) {
         Screen var3 = (Screen)var1;
         PSWRenderer var4 = createSSEInstance(var3);
         if (var4 == null) {
            var4 = createJSWInstance(var3);
         }

         return var4;
      } else {
         return null;
      }
   }

   public Effect.AccelType getAccelType() {
      return this.delegate.getAccelType();
   }

   public synchronized Renderer.RendererState getRendererState() {
      return this.state;
   }

   protected Renderer getBackupRenderer() {
      return this;
   }

   protected void dispose() {
      synchronized(this) {
         this.state = Renderer.RendererState.DISPOSED;
      }
   }

   protected final synchronized void markLost() {
      if (this.state == Renderer.RendererState.OK) {
         this.state = Renderer.RendererState.LOST;
      }

   }

   public int getCompatibleWidth(int var1) {
      return this.screen != null ? PSWDrawable.getCompatibleWidth(this.screen, var1) : this.resourceFactory.getRTTWidth(var1, Texture.WrapMode.CLAMP_TO_EDGE);
   }

   public int getCompatibleHeight(int var1) {
      return this.screen != null ? PSWDrawable.getCompatibleHeight(this.screen, var1) : this.resourceFactory.getRTTHeight(var1, Texture.WrapMode.CLAMP_TO_EDGE);
   }

   public final PSWDrawable createCompatibleImage(int var1, int var2) {
      if (this.screen != null) {
         return PSWDrawable.create(this.screen, var1, var2);
      } else {
         RTTexture var3 = this.resourceFactory.createRTTexture(var1, var2, Texture.WrapMode.CLAMP_TO_EDGE);
         return PSWDrawable.create(var3);
      }
   }

   public PSWDrawable getCompatibleImage(int var1, int var2) {
      PSWDrawable var3 = (PSWDrawable)super.getCompatibleImage(var1, var2);
      if (var3 == null) {
         this.markLost();
      }

      return var3;
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
      String var4 = this.delegate.getPlatformPeerName(var2, var3);

      try {
         Class var6 = Class.forName(var4);
         Constructor var7 = var6.getConstructor(FilterContext.class, Renderer.class, String.class);
         EffectPeer var5 = (EffectPeer)var7.newInstance(var1, this, var2);
         return var5;
      } catch (Exception var8) {
         System.err.println("Error: " + this.getAccelType() + " peer not found for: " + var2 + " due to error: " + var8.getMessage());
         return null;
      }
   }

   protected EffectPeer createPeer(FilterContext var1, String var2, int var3) {
      return PrRenderer.isIntrinsicPeer(var2) ? this.createIntrinsicPeer(var1, var2) : this.createPlatformPeer(var1, var2, var3);
   }

   public boolean isImageDataCompatible(ImageData var1) {
      return this.getRendererState() == Renderer.RendererState.OK && var1.getUntransformedImage() instanceof PSWDrawable;
   }

   public void clearImage(Filterable var1) {
      PSWDrawable var2 = (PSWDrawable)var1;
      var2.clear();
   }

   public ImageData createImageData(FilterContext var1, Filterable var2) {
      if (!(var2 instanceof PrImage)) {
         throw new IllegalArgumentException("Identity source must be PrImage");
      } else {
         Image var3 = ((PrImage)var2).getImage();
         int var4 = var3.getWidth();
         int var5 = var3.getHeight();
         PSWDrawable var6 = this.createCompatibleImage(var4, var5);
         if (var6 == null) {
            return null;
         } else {
            Graphics var7 = var6.createGraphics();
            ResourceFactory var8 = var7.getResourceFactory();
            Texture var9 = var8.createTexture(var3, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
            var7.drawTexture(var9, 0.0F, 0.0F, (float)var4, (float)var5);
            var7.sync();
            var9.dispose();
            return new ImageData(var1, var6, new Rectangle(var4, var5));
         }
      }
   }

   public Filterable transform(FilterContext var1, Filterable var2, BaseTransform var3, Rectangle var4, Rectangle var5) {
      PSWDrawable var6 = this.getCompatibleImage(var5.width, var5.height);
      if (var6 != null) {
         Graphics var7 = var6.createGraphics();
         var7.translate((float)(-var5.x), (float)(-var5.y));
         var7.transform(var3);
         var7.drawTexture(((PSWDrawable)var2).getTextureObject(), (float)var4.x, (float)var4.y, (float)var4.width, (float)var4.height);
      }

      return var6;
   }

   public ImageData transform(FilterContext var1, ImageData var2, BaseTransform var3, Rectangle var4, Rectangle var5) {
      PSWDrawable var6 = this.getCompatibleImage(var5.width, var5.height);
      if (var6 != null) {
         PSWDrawable var7 = (PSWDrawable)var2.getUntransformedImage();
         Graphics var8 = var6.createGraphics();
         var8.translate((float)(-var5.x), (float)(-var5.y));
         var8.transform(var3);
         var8.drawTexture(var7.getTextureObject(), (float)var4.x, (float)var4.y, (float)var4.width, (float)var4.height);
      }

      var2.unref();
      return new ImageData(var1, var6, var5);
   }
}
