package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.state.RenderState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NGGroup extends NGNode {
   private Blend.Mode blendMode;
   private List children;
   private List unmod;
   private List removed;
   private static final int REGION_INTERSECTS_MASK = 357913941;

   public NGGroup() {
      this.blendMode = Blend.Mode.SRC_OVER;
      this.children = new ArrayList(1);
      this.unmod = Collections.unmodifiableList(this.children);
   }

   public List getChildren() {
      return this.unmod;
   }

   public void add(int var1, NGNode var2) {
      if (var1 >= -1 && var1 <= this.children.size()) {
         var2.setParent(this);
         this.childDirty = true;
         if (var1 == -1) {
            this.children.add(var2);
         } else {
            this.children.add(var1, var2);
         }

         var2.markDirty();
         this.markTreeDirtyNoIncrement();
         this.geometryChanged();
      } else {
         throw new IndexOutOfBoundsException("invalid index");
      }
   }

   public void clearFrom(int var1) {
      if (var1 < this.children.size()) {
         this.children.subList(var1, this.children.size()).clear();
         this.geometryChanged();
         this.childDirty = true;
         this.markTreeDirtyNoIncrement();
      }

   }

   public List getRemovedChildren() {
      return this.removed;
   }

   public void addToRemoved(NGNode var1) {
      if (this.removed == null) {
         this.removed = new ArrayList();
      }

      if (this.dirtyChildrenAccumulated <= 12) {
         this.removed.add(var1);
         ++this.dirtyChildrenAccumulated;
         if (this.dirtyChildrenAccumulated > 12) {
            this.removed.clear();
         }

      }
   }

   protected void clearDirty() {
      super.clearDirty();
      if (this.removed != null) {
         this.removed.clear();
      }

   }

   public void remove(NGNode var1) {
      this.children.remove(var1);
      this.geometryChanged();
      this.childDirty = true;
      this.markTreeDirtyNoIncrement();
   }

   public void remove(int var1) {
      this.children.remove(var1);
      this.geometryChanged();
      this.childDirty = true;
      this.markTreeDirtyNoIncrement();
   }

   public void clear() {
      this.children.clear();
      this.childDirty = false;
      this.geometryChanged();
      this.markTreeDirtyNoIncrement();
   }

   public void setBlendMode(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Mode must be non-null");
      } else {
         if (this.blendMode != var1) {
            this.blendMode = (Blend.Mode)var1;
            this.visualsChanged();
         }

      }
   }

   public void renderForcedContent(Graphics var1) {
      if (this.children != null) {
         for(int var2 = 0; var2 < this.children.size(); ++var2) {
            ((NGNode)this.children.get(var2)).renderForcedContent(var1);
         }

      }
   }

   protected void renderContent(Graphics var1) {
      if (this.children != null) {
         NodePath var2 = var1.getRenderRoot();
         int var3 = 0;
         int var4;
         if (var2 != null) {
            if (var2.hasNext()) {
               var2.next();
               var3 = this.children.indexOf(var2.getCurrentNode());

               for(var4 = 0; var4 < var3; ++var4) {
                  ((NGNode)this.children.get(var4)).clearDirtyTree();
               }
            } else {
               var1.setRenderRoot((NodePath)null);
            }
         }

         if (this.blendMode != Blend.Mode.SRC_OVER && this.children.size() >= 2) {
            Blend var15 = new Blend(this.blendMode, (Effect)null, (Effect)null);
            FilterContext var16 = getFilterContext(var1);
            ImageData var6 = null;
            boolean var7 = true;

            do {
               BaseTransform var8 = var1.getTransformNoClone().copy();
               if (var6 != null) {
                  var6.unref();
                  var6 = null;
               }

               Rectangle var9 = PrEffectHelper.getGraphicsClipNoClone(var1);

               for(int var10 = var3; var10 < this.children.size(); ++var10) {
                  NGNode var11 = (NGNode)this.children.get(var10);
                  ImageData var12 = NodeEffectInput.getImageDataForNode(var16, var11, false, var8, var9);
                  if (var6 == null) {
                     var6 = var12;
                  } else {
                     ImageData var13 = var15.filterImageDatas(var16, var8, var9, (RenderState)null, new ImageData[]{var6, var12});
                     var6.unref();
                     var12.unref();
                     var6 = var13;
                  }
               }

               if (var6 != null && (var7 = var6.validate(var16))) {
                  Rectangle var17 = var6.getUntransformedBounds();
                  PrDrawable var18 = (PrDrawable)var6.getUntransformedImage();
                  var1.setTransform(var6.getTransform());
                  var1.drawTexture(var18.getTextureObject(), (float)var17.x, (float)var17.y, (float)var17.width, (float)var17.height);
               }
            } while(var6 == null || !var7);

            if (var6 != null) {
               var6.unref();
            }

         } else {
            for(var4 = var3; var4 < this.children.size(); ++var4) {
               NGNode var5;
               try {
                  var5 = (NGNode)this.children.get(var4);
               } catch (Exception var14) {
                  var5 = null;
               }

               if (var5 != null) {
                  var5.render(var1);
               }
            }

         }
      }
   }

   protected boolean hasOverlappingContents() {
      if (this.blendMode != Blend.Mode.SRC_OVER) {
         return false;
      } else {
         int var1 = this.children == null ? 0 : this.children.size();
         if (var1 == 1) {
            return ((NGNode)this.children.get(0)).hasOverlappingContents();
         } else {
            return var1 != 0;
         }
      }
   }

   public boolean isEmpty() {
      return this.children == null || this.children.isEmpty();
   }

   protected boolean hasVisuals() {
      return false;
   }

   protected boolean needsBlending() {
      Blend.Mode var1 = this.getNodeBlendMode();
      return var1 != null;
   }

   protected NGNode.RenderRootResult computeRenderRoot(NodePath var1, RectBounds var2, int var3, BaseTransform var4, GeneralTransform3D var5) {
      if (var3 != -1) {
         int var6 = this.cullingBits >> var3 * 2;
         if ((var6 & 3) == 0) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
         }

         if ((var6 & 2) != 0) {
            var3 = -1;
         }
      }

      if (!this.isVisible()) {
         return NGNode.RenderRootResult.NO_RENDER_ROOT;
      } else if ((double)this.getOpacity() == 1.0 && (this.getEffect() == null || !this.getEffect().reducesOpaquePixels()) && !this.needsBlending()) {
         if (this.getClipNode() != null) {
            NGNode var35 = this.getClipNode();
            RectBounds var7 = var35.getOpaqueRegion();
            if (var7 == null) {
               return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }

            TEMP_TRANSFORM.deriveWithNewTransform(var4).deriveWithConcatenation(this.getTransform()).deriveWithConcatenation(var35.getTransform());
            if (!checkBoundsInQuad(var7, var2, TEMP_TRANSFORM, var5)) {
               return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
         }

         double var36 = var4.getMxx();
         double var8 = var4.getMxy();
         double var10 = var4.getMxz();
         double var12 = var4.getMxt();
         double var14 = var4.getMyx();
         double var16 = var4.getMyy();
         double var18 = var4.getMyz();
         double var20 = var4.getMyt();
         double var22 = var4.getMzx();
         double var24 = var4.getMzy();
         double var26 = var4.getMzz();
         double var28 = var4.getMzt();
         BaseTransform var30 = var4.deriveWithConcatenation(this.getTransform());
         NGNode.RenderRootResult var31 = NGNode.RenderRootResult.NO_RENDER_ROOT;
         boolean var32 = true;

         for(int var33 = this.children.size() - 1; var33 >= 0; --var33) {
            NGNode var34 = (NGNode)this.children.get(var33);
            var31 = var34.computeRenderRoot(var1, var2, var3, var30, var5);
            var32 &= var34.isClean();
            if (var31 == NGNode.RenderRootResult.HAS_RENDER_ROOT) {
               var1.add(this);
               break;
            }

            if (var31 == NGNode.RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) {
               var1.add(this);
               if (!var32) {
                  var31 = NGNode.RenderRootResult.HAS_RENDER_ROOT;
               }
               break;
            }
         }

         var4.restoreTransform(var36, var8, var10, var12, var14, var16, var18, var20, var22, var24, var26, var28);
         return var31;
      } else {
         return NGNode.RenderRootResult.NO_RENDER_ROOT;
      }
   }

   protected void markCullRegions(DirtyRegionContainer var1, int var2, BaseTransform var3, GeneralTransform3D var4) {
      super.markCullRegions(var1, var2, var3, var4);
      if (this.cullingBits == -1 || this.cullingBits != 0 && (this.cullingBits & 357913941) != 0) {
         double var5 = var3.getMxx();
         double var7 = var3.getMxy();
         double var9 = var3.getMxz();
         double var11 = var3.getMxt();
         double var13 = var3.getMyx();
         double var15 = var3.getMyy();
         double var17 = var3.getMyz();
         double var19 = var3.getMyt();
         double var21 = var3.getMzx();
         double var23 = var3.getMzy();
         double var25 = var3.getMzz();
         double var27 = var3.getMzt();
         BaseTransform var29 = var3.deriveWithConcatenation(this.getTransform());

         for(int var31 = 0; var31 < this.children.size(); ++var31) {
            NGNode var30 = (NGNode)this.children.get(var31);
            var30.markCullRegions(var1, this.cullingBits, var29, var4);
         }

         var3.restoreTransform(var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25, var27);
      }

   }

   public void drawDirtyOpts(BaseTransform var1, GeneralTransform3D var2, Rectangle var3, int[] var4, int var5) {
      super.drawDirtyOpts(var1, var2, var3, var4, var5);
      BaseTransform var6 = var1.copy();
      var6 = var6.deriveWithConcatenation(this.getTransform());

      for(int var7 = 0; var7 < this.children.size(); ++var7) {
         NGNode var8 = (NGNode)this.children.get(var7);
         var8.drawDirtyOpts(var6, var2, var3, var4, var5);
      }

   }
}
