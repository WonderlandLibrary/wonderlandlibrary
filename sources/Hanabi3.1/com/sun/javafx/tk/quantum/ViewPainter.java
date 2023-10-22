package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.Presentable;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

abstract class ViewPainter implements Runnable {
   private static NodePath[] ROOT_PATHS;
   protected static final ReentrantLock renderLock;
   protected int penWidth = -1;
   protected int penHeight = -1;
   protected int viewWidth;
   protected int viewHeight;
   protected final SceneState sceneState;
   protected Presentable presentable;
   protected ResourceFactory factory;
   protected boolean freshBackBuffer;
   private int width;
   private int height;
   private NGNode root;
   private NGNode overlayRoot;
   private Rectangle dirtyRect;
   private RectBounds clip;
   private RectBounds dirtyRegionTemp;
   private DirtyRegionPool dirtyRegionPool;
   private DirtyRegionContainer dirtyRegionContainer;
   private Affine3D tx;
   private Affine3D scaleTx;
   private GeneralTransform3D viewProjTx;
   private GeneralTransform3D projTx;
   private RTTexture sceneBuffer;

   protected ViewPainter(GlassScene var1) {
      this.sceneState = var1.getSceneState();
      if (this.sceneState == null) {
         throw new NullPointerException("Scene state is null");
      } else {
         if (PrismSettings.dirtyOptsEnabled) {
            this.tx = new Affine3D();
            this.viewProjTx = new GeneralTransform3D();
            this.projTx = new GeneralTransform3D();
            this.scaleTx = new Affine3D();
            this.clip = new RectBounds();
            this.dirtyRect = new Rectangle();
            this.dirtyRegionTemp = new RectBounds();
            this.dirtyRegionPool = new DirtyRegionPool(PrismSettings.dirtyRegionCount);
            this.dirtyRegionContainer = this.dirtyRegionPool.checkOut();
         }

      }
   }

   protected final void setRoot(NGNode var1) {
      this.root = var1;
   }

   protected final void setOverlayRoot(NGNode var1) {
      this.overlayRoot = var1;
   }

   private void adjustPerspective(NGCamera var1) {
      assert PrismSettings.dirtyOptsEnabled;

      if (var1 instanceof NGPerspectiveCamera) {
         this.scaleTx.setToScale((double)this.width / 2.0, (double)(-this.height) / 2.0, 1.0);
         this.scaleTx.translate(1.0, -1.0);
         this.projTx.mul((BaseTransform)this.scaleTx);
         this.viewProjTx = var1.getProjViewTx(this.viewProjTx);
         this.projTx.mul(this.viewProjTx);
      }

   }

   protected void paintImpl(Graphics var1) {
      if (this.width > 0 && this.height > 0 && var1 != null) {
         Graphics var2 = var1;
         float var3 = this.getPixelScaleFactorX();
         float var4 = this.getPixelScaleFactorY();
         var1.setPixelScaleFactors(var3, var4);
         boolean var5 = this.overlayRoot != null || this.freshBackBuffer || this.sceneState.getScene().isEntireSceneDirty() || this.sceneState.getScene().getDepthBuffer() || !PrismSettings.dirtyOptsEnabled;
         boolean var6 = PrismSettings.showDirtyRegions || PrismSettings.showOverdraw;
         int var7;
         int var8;
         if (var6 && !this.sceneState.getScene().getDepthBuffer()) {
            var7 = (int)Math.ceil((double)((float)this.width * var3));
            var8 = (int)Math.ceil((double)((float)this.height * var4));
            if (this.sceneBuffer != null) {
               this.sceneBuffer.lock();
               if (this.sceneBuffer.isSurfaceLost() || var7 != this.sceneBuffer.getContentWidth() || var8 != this.sceneBuffer.getContentHeight()) {
                  this.sceneBuffer.unlock();
                  this.sceneBuffer.dispose();
                  this.sceneBuffer = null;
               }
            }

            if (this.sceneBuffer == null) {
               this.sceneBuffer = var1.getResourceFactory().createRTTexture(var7, var8, Texture.WrapMode.CLAMP_TO_ZERO, false);
               var5 = true;
            }

            this.sceneBuffer.contentsUseful();
            var2 = this.sceneBuffer.createGraphics();
            var2.setPixelScaleFactors(var3, var4);
            var2.scale(var3, var4);
         } else if (this.sceneBuffer != null) {
            this.sceneBuffer.dispose();
            this.sceneBuffer = null;
         }

         var7 = -1;
         if (!var5) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
               PulseLogger.newPhase("Dirty Opts Computed");
            }

            this.clip.setBounds(0.0F, 0.0F, (float)this.width, (float)this.height);
            this.dirtyRegionTemp.makeEmpty();
            this.dirtyRegionContainer.reset();
            this.tx.setToIdentity();
            this.projTx.setIdentity();
            this.adjustPerspective(this.sceneState.getCamera());
            var7 = this.root.accumulateDirtyRegions(this.clip, this.dirtyRegionTemp, this.dirtyRegionPool, this.dirtyRegionContainer, this.tx, this.projTx);
            this.dirtyRegionContainer.roundOut();
            if (var7 == 1) {
               this.root.doPreCulling(this.dirtyRegionContainer, this.tx, this.projTx);
            }
         }

         var8 = var7 == 1 ? this.dirtyRegionContainer.size() : 0;
         int var9;
         RectBounds var16;
         if (var8 > 0) {
            var2.setHasPreCullingBits(true);
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
               PulseLogger.newPhase("Render Roots Discovered");
            }

            for(var9 = 0; var9 < var8; ++var9) {
               NodePath var10 = getRootPath(var9);
               var10.clear();
               this.root.getRenderRoot(getRootPath(var9), this.dirtyRegionContainer.getDirtyRegion(var9), var9, this.tx, this.projTx);
            }

            if (PulseLogger.PULSE_LOGGING_ENABLED) {
               PulseLogger.addMessage(var8 + " different dirty regions to render");

               for(var9 = 0; var9 < var8; ++var9) {
                  PulseLogger.addMessage("Dirty Region " + var9 + ": " + this.dirtyRegionContainer.getDirtyRegion(var9));
                  PulseLogger.addMessage("Render Root Path " + var9 + ": " + getRootPath(var9));
               }
            }

            int var11;
            if (PulseLogger.PULSE_LOGGING_ENABLED && PrismSettings.printRenderGraph) {
               StringBuilder var14 = new StringBuilder();
               ArrayList var15 = new ArrayList();

               for(var11 = 0; var11 < var8; ++var11) {
                  RectBounds var12 = this.dirtyRegionContainer.getDirtyRegion(var11);
                  if (var12.getWidth() > 0.0F && var12.getHeight() > 0.0F) {
                     NodePath var13 = getRootPath(var11);
                     if (!var13.isEmpty()) {
                        var15.add(var13.last());
                     }
                  }
               }

               this.root.printDirtyOpts(var14, var15);
               PulseLogger.addMessage(var14.toString());
            }

            for(var9 = 0; var9 < var8; ++var9) {
               var16 = this.dirtyRegionContainer.getDirtyRegion(var9);
               if (var16.getWidth() > 0.0F && var16.getHeight() > 0.0F) {
                  this.dirtyRect.x = var11 = (int)Math.floor((double)(var16.getMinX() * var3));
                  int var19;
                  this.dirtyRect.y = var19 = (int)Math.floor((double)(var16.getMinY() * var4));
                  this.dirtyRect.width = (int)Math.ceil((double)(var16.getMaxX() * var3)) - var11;
                  this.dirtyRect.height = (int)Math.ceil((double)(var16.getMaxY() * var4)) - var19;
                  var2.setClipRect(this.dirtyRect);
                  var2.setClipRectIndex(var9);
                  this.doPaint(var2, getRootPath(var9));
               }
            }
         } else {
            var2.setHasPreCullingBits(false);
            var2.setClipRect((Rectangle)null);
            this.doPaint(var2, (NodePath)null);
         }

         this.root.renderForcedContent(var2);
         if (this.overlayRoot != null) {
            this.overlayRoot.render(var2);
         }

         if (var6) {
            if (this.sceneBuffer != null) {
               var2.sync();
               var1.clear();
               var1.drawTexture(this.sceneBuffer, 0.0F, 0.0F, (float)this.width, (float)this.height, (float)this.sceneBuffer.getContentX(), (float)this.sceneBuffer.getContentY(), (float)(this.sceneBuffer.getContentX() + this.sceneBuffer.getContentWidth()), (float)(this.sceneBuffer.getContentY() + this.sceneBuffer.getContentHeight()));
               this.sceneBuffer.unlock();
            }

            if (PrismSettings.showOverdraw) {
               if (var8 > 0) {
                  for(var9 = 0; var9 < var8; ++var9) {
                     Rectangle var17 = new Rectangle(this.dirtyRegionContainer.getDirtyRegion(var9));
                     var1.setClipRectIndex(var9);
                     this.paintOverdraw(var1, var17);
                     var1.setPaint(new Color(1.0F, 0.0F, 0.0F, 0.3F));
                     var1.drawRect((float)var17.x, (float)var17.y, (float)var17.width, (float)var17.height);
                  }
               } else {
                  Rectangle var18 = new Rectangle(0, 0, this.width, this.height);

                  assert var1.getClipRectIndex() == 0;

                  this.paintOverdraw(var1, var18);
                  var1.setPaint(new Color(1.0F, 0.0F, 0.0F, 0.3F));
                  var1.drawRect((float)var18.x, (float)var18.y, (float)var18.width, (float)var18.height);
               }
            } else if (var8 > 0) {
               var1.setPaint(new Color(1.0F, 0.0F, 0.0F, 0.3F));

               for(var9 = 0; var9 < var8; ++var9) {
                  var16 = this.dirtyRegionContainer.getDirtyRegion(var9);
                  var1.fillRect(var16.getMinX(), var16.getMinY(), var16.getWidth(), var16.getHeight());
               }
            } else {
               var1.setPaint(new Color(1.0F, 0.0F, 0.0F, 0.3F));
               var1.fillRect(0.0F, 0.0F, (float)this.width, (float)this.height);
            }

            this.root.clearPainted();
         }

      } else {
         this.root.renderForcedContent(var1);
      }
   }

   private void paintOverdraw(Graphics var1, Rectangle var2) {
      int[] var3 = new int[var2.width * var2.height];
      this.root.drawDirtyOpts(BaseTransform.IDENTITY_TRANSFORM, this.projTx, var2, var3, var1.getClipRectIndex());
      Image var4 = Image.fromIntArgbPreData(var3, var2.width, var2.height);
      Texture var5 = this.factory.getCachedTexture(var4, Texture.WrapMode.CLAMP_TO_EDGE);
      var1.drawTexture(var5, (float)var2.x, (float)var2.y, (float)(var2.x + var2.width), (float)(var2.y + var2.height), 0.0F, 0.0F, (float)var2.width, (float)var2.height);
      var5.unlock();
   }

   private static NodePath getRootPath(int var0) {
      if (ROOT_PATHS[var0] == null) {
         ROOT_PATHS[var0] = new NodePath();
      }

      return ROOT_PATHS[var0];
   }

   protected void disposePresentable() {
      if (this.presentable instanceof GraphicsResource) {
         ((GraphicsResource)this.presentable).dispose();
      }

      this.presentable = null;
   }

   protected boolean validateStageGraphics() {
      if (!this.sceneState.isValid()) {
         return false;
      } else {
         this.width = this.viewWidth = this.sceneState.getWidth();
         this.height = this.viewHeight = this.sceneState.getHeight();
         return this.sceneState.isWindowVisible() && !this.sceneState.isWindowMinimized();
      }
   }

   protected float getPixelScaleFactorX() {
      return this.presentable == null ? 1.0F : this.presentable.getPixelScaleFactorX();
   }

   protected float getPixelScaleFactorY() {
      return this.presentable == null ? 1.0F : this.presentable.getPixelScaleFactorY();
   }

   private void doPaint(Graphics var1, NodePath var2) {
      if (var2 != null) {
         if (var2.isEmpty()) {
            this.root.clearDirtyTree();
            return;
         }

         assert var2.getCurrentNode() == this.root;
      }

      if (PulseLogger.PULSE_LOGGING_ENABLED) {
         PulseLogger.newPhase("Painting");
      }

      GlassScene var3 = this.sceneState.getScene();
      var3.clearEntireSceneDirty();
      var1.setLights(var3.getLights());
      var1.setDepthBuffer(var3.getDepthBuffer());
      Color var4 = this.sceneState.getClearColor();
      if (var4 != null) {
         var1.clear(var4);
      }

      Paint var5 = this.sceneState.getCurrentPaint();
      if (var5 != null) {
         if (var5.getType() != Paint.Type.COLOR) {
            var1.getRenderTarget().setOpaque(var5.isOpaque());
         }

         var1.setPaint(var5);
         var1.fillQuad(0.0F, 0.0F, (float)this.width, (float)this.height);
      }

      var1.setCamera(this.sceneState.getCamera());
      var1.setRenderRoot(var2);
      this.root.render(var1);
   }

   static {
      ROOT_PATHS = new NodePath[PrismSettings.dirtyRegionCount];
      renderLock = new ReentrantLock();
   }
}
