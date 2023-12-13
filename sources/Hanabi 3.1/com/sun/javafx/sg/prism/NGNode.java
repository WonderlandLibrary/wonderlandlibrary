package com.sun.javafx.sg.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.impl.PrismSettings;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.CacheHint;

public abstract class NGNode {
   private static final GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
   private static final Boolean effectsSupported;
   private String name;
   private static final BoxBounds TEMP_BOUNDS;
   private static final RectBounds TEMP_RECT_BOUNDS;
   protected static final Affine3D TEMP_TRANSFORM;
   static final int DIRTY_REGION_INTERSECTS_NODE_BOUNDS = 1;
   static final int DIRTY_REGION_CONTAINS_NODE_BOUNDS = 2;
   static final int DIRTY_REGION_CONTAINS_OR_INTERSECTS_NODE_BOUNDS = 3;
   private BaseTransform transform;
   protected BaseBounds transformedBounds;
   protected BaseBounds contentBounds;
   BaseBounds dirtyBounds;
   private boolean visible;
   protected DirtyFlag dirty;
   private NGNode parent;
   private boolean isClip;
   private NGNode clipNode;
   private float opacity;
   private Blend.Mode nodeBlendMode;
   private boolean depthTest;
   private CacheFilter cacheFilter;
   private EffectFilter effectFilter;
   protected boolean childDirty;
   protected int dirtyChildrenAccumulated;
   protected static final int DIRTY_CHILDREN_ACCUMULATED_THRESHOLD = 12;
   protected int cullingBits;
   private DirtyHint hint;
   private RectBounds opaqueRegion;
   private boolean opaqueRegionInvalid;
   private int painted;
   private static Point2D[] TEMP_POINTS2D_4;

   protected NGNode() {
      this.transform = BaseTransform.IDENTITY_TRANSFORM;
      this.transformedBounds = new RectBounds();
      this.contentBounds = new RectBounds();
      this.dirtyBounds = new RectBounds();
      this.visible = true;
      this.dirty = NGNode.DirtyFlag.DIRTY;
      this.opacity = 1.0F;
      this.depthTest = true;
      this.childDirty = false;
      this.dirtyChildrenAccumulated = 0;
      this.cullingBits = 0;
      this.opaqueRegion = null;
      this.opaqueRegionInvalid = true;
      this.painted = 0;
   }

   public void setVisible(boolean var1) {
      if (this.visible != var1) {
         this.visible = var1;
         this.markDirty();
      }

   }

   public void setContentBounds(BaseBounds var1) {
      this.contentBounds = this.contentBounds.deriveWithNewBounds(var1);
   }

   public void setTransformedBounds(BaseBounds var1, boolean var2) {
      if (!this.transformedBounds.equals(var1)) {
         if (this.dirtyBounds.isEmpty()) {
            this.dirtyBounds = this.dirtyBounds.deriveWithNewBounds(this.transformedBounds);
            this.dirtyBounds = this.dirtyBounds.deriveWithUnion(var1);
         } else {
            this.dirtyBounds = this.dirtyBounds.deriveWithUnion(this.transformedBounds);
         }

         this.transformedBounds = this.transformedBounds.deriveWithNewBounds(var1);
         if (this.hasVisuals() && !var2) {
            this.markDirty();
         }

      }
   }

   public void setTransformMatrix(BaseTransform var1) {
      if (!this.transform.equals(var1)) {
         boolean var2 = false;
         if (this.parent != null && this.parent.cacheFilter != null && PrismSettings.scrollCacheOpt) {
            if (this.hint == null) {
               this.hint = new DirtyHint();
            } else if (this.transform.getMxx() == var1.getMxx() && this.transform.getMxy() == var1.getMxy() && this.transform.getMyy() == var1.getMyy() && this.transform.getMyx() == var1.getMyx() && this.transform.getMxz() == var1.getMxz() && this.transform.getMyz() == var1.getMyz() && this.transform.getMzx() == var1.getMzx() && this.transform.getMzy() == var1.getMzy() && this.transform.getMzz() == var1.getMzz() && this.transform.getMzt() == var1.getMzt()) {
               var2 = true;
               this.hint.translateXDelta = var1.getMxt() - this.transform.getMxt();
               this.hint.translateYDelta = var1.getMyt() - this.transform.getMyt();
            }
         }

         this.transform = this.transform.deriveWithNewTransform(var1);
         if (var2) {
            this.markDirtyByTranslation();
         } else {
            this.markDirty();
         }

         this.invalidateOpaqueRegion();
      }
   }

   public void setClipNode(NGNode var1) {
      if (var1 != this.clipNode) {
         if (this.clipNode != null) {
            this.clipNode.setParent((NGNode)null);
         }

         if (var1 != null) {
            var1.setParent(this, true);
         }

         this.clipNode = var1;
         this.visualsChanged();
         this.invalidateOpaqueRegion();
      }

   }

   public void setOpacity(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         if (var1 != this.opacity) {
            float var2 = this.opacity;
            this.opacity = var1;
            this.markDirty();
            if (var2 < 1.0F && (var1 == 1.0F || var1 == 0.0F) || var1 < 1.0F && (var2 == 1.0F || var2 == 0.0F)) {
               this.invalidateOpaqueRegion();
            }
         }

      } else {
         throw new IllegalArgumentException("Internal Error: The opacity must be between 0 and 1");
      }
   }

   public void setNodeBlendMode(Blend.Mode var1) {
      if (this.nodeBlendMode != var1) {
         this.nodeBlendMode = var1;
         this.markDirty();
         this.invalidateOpaqueRegion();
      }

   }

   public void setDepthTest(boolean var1) {
      if (var1 != this.depthTest) {
         this.depthTest = var1;
         this.visualsChanged();
      }

   }

   public void setCachedAsBitmap(boolean var1, CacheHint var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("Internal Error: cacheHint must not be null");
      } else {
         if (var1) {
            if (this.cacheFilter == null) {
               this.cacheFilter = new CacheFilter(this, var2);
               this.markDirty();
            } else if (!this.cacheFilter.matchesHint(var2)) {
               this.cacheFilter.setHint(var2);
               this.markDirty();
            }
         } else if (this.cacheFilter != null) {
            this.cacheFilter.dispose();
            this.cacheFilter = null;
            this.markDirty();
         }

      }
   }

   public void setEffect(Effect var1) {
      Effect var2 = this.getEffect();
      if (PrismSettings.disableEffects) {
         var1 = null;
      }

      if (this.effectFilter == null && var1 != null) {
         this.effectFilter = new EffectFilter(var1, this);
         this.visualsChanged();
      } else if (this.effectFilter != null && this.effectFilter.getEffect() != var1) {
         this.effectFilter.dispose();
         this.effectFilter = null;
         if (var1 != null) {
            this.effectFilter = new EffectFilter(var1, this);
         }

         this.visualsChanged();
      }

      if (var2 != var1 && (var2 == null || var1 == null)) {
         this.invalidateOpaqueRegion();
      }

   }

   public void effectChanged() {
      this.visualsChanged();
   }

   public boolean isContentBounds2D() {
      return this.contentBounds.is2D() || Affine3D.almostZero((double)this.contentBounds.getMaxZ()) && Affine3D.almostZero((double)this.contentBounds.getMinZ());
   }

   public NGNode getParent() {
      return this.parent;
   }

   public void setParent(NGNode var1) {
      this.setParent(var1, false);
   }

   private void setParent(NGNode var1, boolean var2) {
      this.parent = var1;
      this.isClip = var2;
   }

   public final void setName(String var1) {
      this.name = var1;
   }

   public final String getName() {
      return this.name;
   }

   protected final Effect getEffect() {
      return this.effectFilter == null ? null : this.effectFilter.getEffect();
   }

   public boolean isVisible() {
      return this.visible;
   }

   public final BaseTransform getTransform() {
      return this.transform;
   }

   public final float getOpacity() {
      return this.opacity;
   }

   public final Blend.Mode getNodeBlendMode() {
      return this.nodeBlendMode;
   }

   public final boolean isDepthTest() {
      return this.depthTest;
   }

   public final CacheFilter getCacheFilter() {
      return this.cacheFilter;
   }

   public final EffectFilter getEffectFilter() {
      return this.effectFilter;
   }

   public final NGNode getClipNode() {
      return this.clipNode;
   }

   public BaseBounds getContentBounds(BaseBounds var1, BaseTransform var2) {
      if (var2.isTranslateOrIdentity()) {
         var1 = var1.deriveWithNewBounds(this.contentBounds);
         if (!var2.isIdentity()) {
            float var3 = (float)var2.getMxt();
            float var4 = (float)var2.getMyt();
            float var5 = (float)var2.getMzt();
            var1 = var1.deriveWithNewBounds(var1.getMinX() + var3, var1.getMinY() + var4, var1.getMinZ() + var5, var1.getMaxX() + var3, var1.getMaxY() + var4, var1.getMaxZ() + var5);
         }

         return var1;
      } else {
         return this.computeBounds(var1, var2);
      }
   }

   private BaseBounds computeBounds(BaseBounds var1, BaseTransform var2) {
      var1 = var1.deriveWithNewBounds(this.contentBounds);
      return var2.transform(this.contentBounds, var1);
   }

   public final BaseBounds getClippedBounds(BaseBounds var1, BaseTransform var2) {
      BaseBounds var3 = this.getEffectBounds(var1, var2);
      if (this.clipNode != null) {
         float var4 = var3.getMinX();
         float var5 = var3.getMinY();
         float var6 = var3.getMinZ();
         float var7 = var3.getMaxX();
         float var8 = var3.getMaxY();
         float var9 = var3.getMaxZ();
         var3 = this.clipNode.getCompleteBounds(var3, var2);
         var3.intersectWith(var4, var5, var6, var7, var8, var9);
      }

      return var3;
   }

   public final BaseBounds getEffectBounds(BaseBounds var1, BaseTransform var2) {
      return this.effectFilter != null ? this.effectFilter.getBounds(var1, var2) : this.getContentBounds(var1, var2);
   }

   public final BaseBounds getCompleteBounds(BaseBounds var1, BaseTransform var2) {
      if (var2.isIdentity()) {
         var1 = var1.deriveWithNewBounds(this.transformedBounds);
         return var1;
      } else if (this.transform.isIdentity()) {
         return this.getClippedBounds(var1, var2);
      } else {
         double var3 = var2.getMxx();
         double var5 = var2.getMxy();
         double var7 = var2.getMxz();
         double var9 = var2.getMxt();
         double var11 = var2.getMyx();
         double var13 = var2.getMyy();
         double var15 = var2.getMyz();
         double var17 = var2.getMyt();
         double var19 = var2.getMzx();
         double var21 = var2.getMzy();
         double var23 = var2.getMzz();
         double var25 = var2.getMzt();
         BaseTransform var27 = var2.deriveWithConcatenation(this.transform);
         var1 = this.getClippedBounds(var1, var2);
         if (var27 == var2) {
            var2.restoreTransform(var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25);
         }

         return var1;
      }
   }

   protected void visualsChanged() {
      this.invalidateCache();
      this.markDirty();
   }

   protected void geometryChanged() {
      this.invalidateCache();
      this.invalidateOpaqueRegion();
      if (this.hasVisuals()) {
         this.markDirty();
      }

   }

   public final void markDirty() {
      if (this.dirty != NGNode.DirtyFlag.DIRTY) {
         this.dirty = NGNode.DirtyFlag.DIRTY;
         this.markTreeDirty();
      }

   }

   private void markDirtyByTranslation() {
      if (this.dirty == NGNode.DirtyFlag.CLEAN) {
         if (this.parent != null && this.parent.dirty == NGNode.DirtyFlag.CLEAN && !this.parent.childDirty) {
            this.dirty = NGNode.DirtyFlag.DIRTY_BY_TRANSLATION;
            this.parent.childDirty = true;
            ++this.parent.dirtyChildrenAccumulated;
            this.parent.invalidateCacheByTranslation(this.hint);
            this.parent.markTreeDirty();
         } else {
            this.markDirty();
         }
      }

   }

   protected final void markTreeDirtyNoIncrement() {
      if (this.parent != null && (!this.parent.childDirty || this.dirty == NGNode.DirtyFlag.DIRTY_BY_TRANSLATION)) {
         this.markTreeDirty();
      }

   }

   protected final void markTreeDirty() {
      NGNode var1 = this.parent;
      boolean var2 = this.isClip;

      boolean var3;
      for(var3 = this.dirty == NGNode.DirtyFlag.DIRTY_BY_TRANSLATION; var1 != null && var1.dirty != NGNode.DirtyFlag.DIRTY && (!var1.childDirty || var2 || var3); var1 = var1.parent) {
         if (var2) {
            var1.dirty = NGNode.DirtyFlag.DIRTY;
         } else if (!var3) {
            var1.childDirty = true;
            ++var1.dirtyChildrenAccumulated;
         }

         var1.invalidateCache();
         var2 = var1.isClip;
         var3 = var1.dirty == NGNode.DirtyFlag.DIRTY_BY_TRANSLATION;
      }

      if (var1 != null && var1.dirty == NGNode.DirtyFlag.CLEAN && !var2 && !var3) {
         ++var1.dirtyChildrenAccumulated;
      }

      if (var1 != null) {
         var1.invalidateCache();
      }

   }

   public final boolean isClean() {
      return this.dirty == NGNode.DirtyFlag.CLEAN && !this.childDirty;
   }

   protected void clearDirty() {
      this.dirty = NGNode.DirtyFlag.CLEAN;
      this.childDirty = false;
      this.dirtyBounds.makeEmpty();
      this.dirtyChildrenAccumulated = 0;
   }

   public void clearPainted() {
      this.painted = 0;
      if (this instanceof NGGroup) {
         List var1 = ((NGGroup)this).getChildren();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            ((NGNode)var1.get(var2)).clearPainted();
         }
      }

   }

   public void clearDirtyTree() {
      this.clearDirty();
      if (this.getClipNode() != null) {
         this.getClipNode().clearDirtyTree();
      }

      if (this instanceof NGGroup) {
         List var1 = ((NGGroup)this).getChildren();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            NGNode var3 = (NGNode)var1.get(var2);
            if (var3.dirty != NGNode.DirtyFlag.CLEAN || var3.childDirty) {
               var3.clearDirtyTree();
            }
         }
      }

   }

   protected final void invalidateCache() {
      if (this.cacheFilter != null) {
         this.cacheFilter.invalidate();
      }

   }

   protected final void invalidateCacheByTranslation(DirtyHint var1) {
      if (this.cacheFilter != null) {
         this.cacheFilter.invalidateByTranslation(var1.translateXDelta, var1.translateYDelta);
      }

   }

   public int accumulateDirtyRegions(RectBounds var1, RectBounds var2, DirtyRegionPool var3, DirtyRegionContainer var4, BaseTransform var5, GeneralTransform3D var6) {
      if (var1 != null && var2 != null && var3 != null && var4 != null && var5 != null && var6 != null) {
         if (this.dirty == NGNode.DirtyFlag.CLEAN && !this.childDirty) {
            return 1;
         } else if (this.dirty != NGNode.DirtyFlag.CLEAN) {
            return this.accumulateNodeDirtyRegion(var1, var2, var4, var5, var6);
         } else {
            assert this.childDirty;

            return this.accumulateGroupDirtyRegion(var1, var2, var3, var4, var5, var6);
         }
      } else {
         throw new NullPointerException();
      }
   }

   int accumulateNodeDirtyRegion(RectBounds var1, RectBounds var2, DirtyRegionContainer var3, BaseTransform var4, GeneralTransform3D var5) {
      BaseBounds var6 = this.computeDirtyRegion(var2, var4, var5);
      if (var6 != var2) {
         var6.flattenInto(var2);
      }

      if (!var2.isEmpty() && !var1.disjoint(var2)) {
         if (var2.contains(var1)) {
            return 0;
         } else {
            var2.intersectWith((BaseBounds)var1);
            var3.addDirtyRegion(var2);
            return 1;
         }
      } else {
         return 1;
      }
   }

   int accumulateGroupDirtyRegion(RectBounds var1, RectBounds var2, DirtyRegionPool var3, DirtyRegionContainer var4, BaseTransform var5, GeneralTransform3D var6) {
      assert this.childDirty;

      assert this.dirty == NGNode.DirtyFlag.CLEAN;

      int var7 = 1;
      if (this.dirtyChildrenAccumulated > 12) {
         var7 = this.accumulateNodeDirtyRegion(var1, var2, var4, var5, var6);
         return var7;
      } else {
         double var8 = var5.getMxx();
         double var10 = var5.getMxy();
         double var12 = var5.getMxz();
         double var14 = var5.getMxt();
         double var16 = var5.getMyx();
         double var18 = var5.getMyy();
         double var20 = var5.getMyz();
         double var22 = var5.getMyt();
         double var24 = var5.getMzx();
         double var26 = var5.getMzy();
         double var28 = var5.getMzz();
         double var30 = var5.getMzt();
         BaseTransform var32 = var5;
         if (this.transform != null) {
            var32 = var5.deriveWithConcatenation(this.transform);
         }

         RectBounds var33 = var1;
         DirtyRegionContainer var34 = null;
         BaseTransform var35 = null;
         BaseBounds var36;
         if (this.effectFilter != null) {
            try {
               var33 = new RectBounds();
               var36 = var32.inverseTransform((BaseBounds)var1, (BaseBounds)TEMP_BOUNDS);
               var36.flattenInto(var33);
            } catch (NoninvertibleTransformException var41) {
               return 1;
            }

            var35 = var32;
            var32 = BaseTransform.IDENTITY_TRANSFORM;
            var34 = var4;
            var4 = var3.checkOut();
         } else if (this.clipNode != null) {
            var34 = var4;
            var33 = new RectBounds();
            var36 = this.clipNode.getCompleteBounds(var33, var32);
            var6.transform(var36, var36);
            var36.flattenInto(var33);
            var33.intersectWith((BaseBounds)var1);
            var4 = var3.checkOut();
         }

         List var42 = ((NGGroup)this).getRemovedChildren();
         int var38;
         if (var42 != null) {
            for(var38 = var42.size() - 1; var38 >= 0; --var38) {
               NGNode var37 = (NGNode)var42.get(var38);
               var37.dirty = NGNode.DirtyFlag.DIRTY;
               var7 = var37.accumulateDirtyRegions(var33, var2, var3, var4, var32, var6);
               if (var7 == 0) {
                  break;
               }
            }
         }

         List var43 = ((NGGroup)this).getChildren();
         var38 = var43.size();

         for(int var39 = 0; var39 < var38 && var7 == 1; ++var39) {
            NGNode var40 = (NGNode)var43.get(var39);
            var7 = var40.accumulateDirtyRegions(var33, var2, var3, var4, var32, var6);
            if (var7 == 0) {
               break;
            }
         }

         if (this.effectFilter != null && var7 == 1) {
            this.applyEffect(this.effectFilter, var4, var3);
            if (this.clipNode != null) {
               var33 = new RectBounds();
               BaseBounds var44 = this.clipNode.getCompleteBounds(var33, var32);
               this.applyClip(var44, var4);
            }

            this.applyTransform(var35, var4);
            var32 = var35;
            var34.merge(var4);
            var3.checkIn(var4);
         }

         if (var32 == var5) {
            var5.restoreTransform(var8, var10, var12, var14, var16, var18, var20, var22, var24, var26, var28, var30);
         }

         if (this.clipNode != null && this.effectFilter == null) {
            if (var7 == 0) {
               var7 = this.accumulateNodeDirtyRegion(var1, var2, var34, var5, var6);
            } else {
               var34.merge(var4);
            }

            var3.checkIn(var4);
         }

         return var7;
      }
   }

   private BaseBounds computeDirtyRegion(RectBounds var1, BaseTransform var2, GeneralTransform3D var3) {
      if (this.cacheFilter != null) {
         return this.cacheFilter.computeDirtyBounds(var1, var2, var3);
      } else {
         BaseBounds var4;
         if (!this.dirtyBounds.isEmpty()) {
            var4 = var1.deriveWithNewBounds(this.dirtyBounds);
         } else {
            var4 = var1.deriveWithNewBounds(this.transformedBounds);
         }

         if (!var4.isEmpty()) {
            var4 = this.computePadding(var4);
            var4 = var2.transform(var4, var4);
            var4 = var3.transform(var4, var4);
         }

         return var4;
      }
   }

   protected BaseBounds computePadding(BaseBounds var1) {
      return var1;
   }

   protected boolean hasVisuals() {
      return true;
   }

   public final void doPreCulling(DirtyRegionContainer var1, BaseTransform var2, GeneralTransform3D var3) {
      if (var1 != null && var2 != null && var3 != null) {
         this.markCullRegions(var1, -1, var2, var3);
      } else {
         throw new NullPointerException();
      }
   }

   void markCullRegions(DirtyRegionContainer var1, int var2, BaseTransform var3, GeneralTransform3D var4) {
      if (var3.isIdentity()) {
         TEMP_BOUNDS.deriveWithNewBounds(this.transformedBounds);
      } else {
         var3.transform((BaseBounds)this.transformedBounds, (BaseBounds)TEMP_BOUNDS);
      }

      if (!var4.isIdentity()) {
         var4.transform((BaseBounds)TEMP_BOUNDS, (BaseBounds)TEMP_BOUNDS);
      }

      TEMP_BOUNDS.flattenInto(TEMP_RECT_BOUNDS);
      this.cullingBits = 0;
      int var6 = 1;

      for(int var7 = 0; var7 < var1.size(); ++var7) {
         RectBounds var5 = var1.getDirtyRegion(var7);
         if (var5 == null || var5.isEmpty()) {
            break;
         }

         if ((var2 == -1 || (var2 & var6) != 0) && var5.intersects(TEMP_RECT_BOUNDS)) {
            byte var8 = 1;
            if (var5.contains(TEMP_RECT_BOUNDS)) {
               var8 = 2;
            }

            this.cullingBits |= var8 << 2 * var7;
         }

         var6 <<= 2;
      }

      if (this.cullingBits == 0 && (this.dirty != NGNode.DirtyFlag.CLEAN || this.childDirty)) {
         this.clearDirtyTree();
      }

   }

   public final void printDirtyOpts(StringBuilder var1, List var2) {
      var1.append("\n*=Render Root\n");
      var1.append("d=Dirty\n");
      var1.append("dt=Dirty By Translation\n");
      var1.append("i=Dirty Region Intersects the NGNode\n");
      var1.append("c=Dirty Region Contains the NGNode\n");
      var1.append("ef=Effect Filter\n");
      var1.append("cf=Cache Filter\n");
      var1.append("cl=This node is a clip node\n");
      var1.append("b=Blend mode is set\n");
      var1.append("or=Opaque Region\n");
      this.printDirtyOpts(var1, this, BaseTransform.IDENTITY_TRANSFORM, "", var2);
   }

   private final void printDirtyOpts(StringBuilder var1, NGNode var2, BaseTransform var3, String var4, List var5) {
      if (var2.isVisible() && var2.getOpacity() != 0.0F) {
         BaseTransform var6 = var3.copy();
         var6 = var6.deriveWithConcatenation(var2.getTransform());
         ArrayList var7 = new ArrayList();

         int var8;
         for(var8 = 0; var8 < var5.size(); ++var8) {
            NGNode var9 = (NGNode)var5.get(var8);
            if (var2 == var9) {
               var7.add("*" + var8);
            }
         }

         if (var2.dirty != NGNode.DirtyFlag.CLEAN) {
            var7.add(var2.dirty == NGNode.DirtyFlag.DIRTY ? "d" : "dt");
         }

         int var10;
         if (var2.cullingBits != 0) {
            var8 = 17;

            for(int var11 = 0; var11 < 15; ++var11) {
               var10 = var2.cullingBits & var8;
               if (var10 != 0) {
                  var7.add(var10 == 1 ? "i" + var11 : (var10 == 0 ? "c" + var11 : "ci" + var11));
               }

               var8 <<= 2;
            }
         }

         if (var2.effectFilter != null) {
            var7.add("ef");
         }

         if (var2.cacheFilter != null) {
            var7.add("cf");
         }

         if (var2.nodeBlendMode != null) {
            var7.add("b");
         }

         RectBounds var12 = var2.getOpaqueRegion();
         if (var12 != null) {
            RectBounds var13 = new RectBounds();
            var6.transform((BaseBounds)var12, (BaseBounds)var13);
            var7.add("or=" + var13.getMinX() + ", " + var13.getMinY() + ", " + var13.getWidth() + ", " + var13.getHeight());
         }

         if (var7.isEmpty()) {
            var1.append(var4 + var2.name + "\n");
         } else {
            String var14 = " [";

            for(var10 = 0; var10 < var7.size(); ++var10) {
               var14 = var14 + (String)var7.get(var10);
               if (var10 < var7.size() - 1) {
                  var14 = var14 + " ";
               }
            }

            var1.append(var4 + var2.name + var14 + "]\n");
         }

         if (var2.getClipNode() != null) {
            this.printDirtyOpts(var1, var2.getClipNode(), var6, var4 + "  cl ", var5);
         }

         if (var2 instanceof NGGroup) {
            NGGroup var15 = (NGGroup)var2;

            for(var10 = 0; var10 < var15.getChildren().size(); ++var10) {
               this.printDirtyOpts(var1, (NGNode)var15.getChildren().get(var10), var6, var4 + "  ", var5);
            }
         }

      }
   }

   public void drawDirtyOpts(BaseTransform var1, GeneralTransform3D var2, Rectangle var3, int[] var4, int var5) {
      if ((this.painted & 1 << var5 * 2) != 0) {
         var1.copy().deriveWithConcatenation(this.getTransform()).transform((BaseBounds)this.contentBounds, (BaseBounds)TEMP_BOUNDS);
         if (var2 != null) {
            var2.transform((BaseBounds)TEMP_BOUNDS, (BaseBounds)TEMP_BOUNDS);
         }

         RectBounds var6 = new RectBounds();
         TEMP_BOUNDS.flattenInto(var6);

         assert var3.width * var3.height == var4.length;

         var6.intersectWith(var3);
         int var7 = (int)var6.getMinX() - var3.x;
         int var8 = (int)var6.getMinY() - var3.y;
         int var9 = (int)((double)var6.getWidth() + 0.5);
         int var10 = (int)((double)var6.getHeight() + 0.5);
         if (var9 == 0 || var10 == 0) {
            return;
         }

         for(int var11 = var8; var11 < var8 + var10; ++var11) {
            for(int var12 = var7; var12 < var7 + var9; ++var12) {
               int var13 = var11 * var3.width + var12;
               int var14 = var4[var13];
               if (var14 == 0) {
                  var14 = 134250240;
               } else if ((this.painted & 3 << var5 * 2) == 3) {
                  switch (var14) {
                     case -2147451136:
                        var14 = -2147450880;
                        break;
                     case -2147450880:
                        var14 = -2139128064;
                        break;
                     case -2139128064:
                        var14 = -2139062272;
                        break;
                     case -2139062272:
                        var14 = -2139160576;
                        break;
                     default:
                        var14 = -2139095040;
                  }
               }

               var4[var13] = var14;
            }
         }
      }

   }

   public final void getRenderRoot(NodePath var1, RectBounds var2, int var3, BaseTransform var4, GeneralTransform3D var5) {
      if (var1 != null && var2 != null && var4 != null && var5 != null) {
         if (var3 >= -1 && var3 <= 15) {
            RenderRootResult var6 = this.computeRenderRoot(var1, var2, var3, var4, var5);
            if (var6 == NGNode.RenderRootResult.NO_RENDER_ROOT) {
               var1.add(this);
            } else if (var6 == NGNode.RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN) {
               var1.clear();
            }

         } else {
            throw new IllegalArgumentException("cullingIndex cannot be < -1 or > 15");
         }
      } else {
         throw new NullPointerException();
      }
   }

   RenderRootResult computeRenderRoot(NodePath var1, RectBounds var2, int var3, BaseTransform var4, GeneralTransform3D var5) {
      return this.computeNodeRenderRoot(var1, var2, var3, var4, var5);
   }

   private static int ccw(double var0, double var2, Point2D var4, Point2D var5) {
      return (int)Math.signum((double)(var5.x - var4.x) * (var2 - (double)var4.y) - (double)(var5.y - var4.y) * (var0 - (double)var4.x));
   }

   private static boolean pointInConvexQuad(double var0, double var2, Point2D[] var4) {
      int var5 = ccw(var0, var2, var4[0], var4[1]);
      int var6 = ccw(var0, var2, var4[1], var4[2]);
      int var7 = ccw(var0, var2, var4[2], var4[3]);
      int var8 = ccw(var0, var2, var4[3], var4[0]);
      var5 ^= var5 >>> 1;
      var6 ^= var6 >>> 1;
      var7 ^= var7 >>> 1;
      var8 ^= var8 >>> 1;
      int var9 = var5 | var6 | var7 | var8;
      return var9 == Integer.MIN_VALUE || var9 == 1;
   }

   final RenderRootResult computeNodeRenderRoot(NodePath var1, RectBounds var2, int var3, BaseTransform var4, GeneralTransform3D var5) {
      if (var3 != -1) {
         int var6 = this.cullingBits >> var3 * 2;
         if ((var6 & 3) == 0) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
         }
      }

      if (!this.isVisible()) {
         return NGNode.RenderRootResult.NO_RENDER_ROOT;
      } else {
         RectBounds var9 = this.getOpaqueRegion();
         if (var9 == null) {
            return NGNode.RenderRootResult.NO_RENDER_ROOT;
         } else {
            BaseTransform var7 = this.getTransform();
            Affine3D var8 = TEMP_TRANSFORM.deriveWithNewTransform(var4).deriveWithConcatenation(var7);
            if (checkBoundsInQuad(var9, var2, var8, var5)) {
               var1.add(this);
               return this.isClean() ? NGNode.RenderRootResult.HAS_RENDER_ROOT_AND_IS_CLEAN : NGNode.RenderRootResult.HAS_RENDER_ROOT;
            } else {
               return NGNode.RenderRootResult.NO_RENDER_ROOT;
            }
         }
      }
   }

   static boolean checkBoundsInQuad(RectBounds var0, RectBounds var1, BaseTransform var2, GeneralTransform3D var3) {
      if (var3.isIdentity() && (var2.getType() & -16) == 0) {
         if (var2.isIdentity()) {
            TEMP_BOUNDS.deriveWithNewBounds((BaseBounds)var0);
         } else {
            var2.transform((BaseBounds)var0, (BaseBounds)TEMP_BOUNDS);
         }

         TEMP_BOUNDS.flattenInto(TEMP_RECT_BOUNDS);
         return TEMP_RECT_BOUNDS.contains(var1);
      } else {
         TEMP_POINTS2D_4[0].setLocation(var0.getMinX(), var0.getMinY());
         TEMP_POINTS2D_4[1].setLocation(var0.getMaxX(), var0.getMinY());
         TEMP_POINTS2D_4[2].setLocation(var0.getMaxX(), var0.getMaxY());
         TEMP_POINTS2D_4[3].setLocation(var0.getMinX(), var0.getMaxY());
         Point2D[] var4 = TEMP_POINTS2D_4;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Point2D var7 = var4[var6];
            var2.transform(var7, var7);
            if (!var3.isIdentity()) {
               var3.transform(var7, var7);
            }
         }

         return pointInConvexQuad((double)var1.getMinX(), (double)var1.getMinY(), TEMP_POINTS2D_4) && pointInConvexQuad((double)var1.getMaxX(), (double)var1.getMinY(), TEMP_POINTS2D_4) && pointInConvexQuad((double)var1.getMaxX(), (double)var1.getMaxY(), TEMP_POINTS2D_4) && pointInConvexQuad((double)var1.getMinX(), (double)var1.getMaxY(), TEMP_POINTS2D_4);
      }
   }

   protected final void invalidateOpaqueRegion() {
      this.opaqueRegionInvalid = true;
      if (this.isClip) {
         this.parent.invalidateOpaqueRegion();
      }

   }

   final boolean isOpaqueRegionInvalid() {
      return this.opaqueRegionInvalid;
   }

   public final RectBounds getOpaqueRegion() {
      if (this.opaqueRegionInvalid || this.getEffect() != null) {
         this.opaqueRegionInvalid = false;
         if (this.supportsOpaqueRegions() && this.hasOpaqueRegion()) {
            this.opaqueRegion = this.computeOpaqueRegion(this.opaqueRegion == null ? new RectBounds() : this.opaqueRegion);

            assert this.opaqueRegion != null;

            if (this.opaqueRegion == null) {
               return null;
            }

            NGNode var1 = this.getClipNode();
            if (var1 != null) {
               RectBounds var2 = var1.getOpaqueRegion();
               if (var2 == null || (var1.getTransform().getType() & -8) != 0) {
                  return this.opaqueRegion = null;
               }

               BaseBounds var3 = var1.getTransform().transform((BaseBounds)var2, (BaseBounds)TEMP_BOUNDS);
               var3.flattenInto(TEMP_RECT_BOUNDS);
               this.opaqueRegion.intersectWith((BaseBounds)TEMP_RECT_BOUNDS);
            }
         } else {
            this.opaqueRegion = null;
         }
      }

      return this.opaqueRegion;
   }

   protected boolean supportsOpaqueRegions() {
      return false;
   }

   protected boolean hasOpaqueRegion() {
      NGNode var1 = this.getClipNode();
      Effect var2 = this.getEffect();
      return (var2 == null || !var2.reducesOpaquePixels()) && this.getOpacity() == 1.0F && (this.nodeBlendMode == null || this.nodeBlendMode == Blend.Mode.SRC_OVER) && (var1 == null || var1.supportsOpaqueRegions() && var1.hasOpaqueRegion());
   }

   protected RectBounds computeOpaqueRegion(RectBounds var1) {
      return null;
   }

   protected boolean isRectClip(BaseTransform var1, boolean var2) {
      return false;
   }

   public final void render(Graphics var1) {
      if (PulseLogger.PULSE_LOGGING_ENABLED) {
         PulseLogger.incrementCounter("Nodes visited during render");
      }

      this.clearDirty();
      if (this.visible && this.opacity != 0.0F) {
         this.doRender(var1);
      }
   }

   public void renderForcedContent(Graphics var1) {
   }

   boolean isShape3D() {
      return false;
   }

   protected void doRender(Graphics var1) {
      var1.setState3D(this.isShape3D());
      boolean var2 = false;
      if (PrismSettings.dirtyOptsEnabled && var1.hasPreCullingBits()) {
         int var3 = this.cullingBits >> var1.getClipRectIndex() * 2;
         if ((var3 & 3) == 0) {
            return;
         }

         if ((var3 & 2) != 0) {
            var1.setHasPreCullingBits(false);
            var2 = true;
         }
      }

      boolean var30 = var1.isDepthTest();
      var1.setDepthTest(this.isDepthTest());
      BaseTransform var4 = var1.getTransformNoClone();
      double var5 = var4.getMxx();
      double var7 = var4.getMxy();
      double var9 = var4.getMxz();
      double var11 = var4.getMxt();
      double var13 = var4.getMyx();
      double var15 = var4.getMyy();
      double var17 = var4.getMyz();
      double var19 = var4.getMyt();
      double var21 = var4.getMzx();
      double var23 = var4.getMzy();
      double var25 = var4.getMzz();
      double var27 = var4.getMzt();
      var1.transform(this.getTransform());
      boolean var29 = false;
      if (!this.isShape3D() && var1 instanceof ReadbackGraphics && this.needsBlending()) {
         this.renderNodeBlendMode(var1);
         var29 = true;
      } else if (!this.isShape3D() && this.getOpacity() < 1.0F) {
         this.renderOpacity(var1);
         var29 = true;
      } else if (!this.isShape3D() && this.getCacheFilter() != null) {
         this.renderCached(var1);
         var29 = true;
      } else if (!this.isShape3D() && this.getClipNode() != null) {
         this.renderClip(var1);
         var29 = true;
      } else if (!this.isShape3D() && this.getEffectFilter() != null && effectsSupported) {
         this.renderEffect(var1);
         var29 = true;
      } else {
         this.renderContent(var1);
         if (PrismSettings.showOverdraw) {
            var29 = this instanceof NGRegion || !(this instanceof NGGroup);
         }
      }

      if (var2) {
         var1.setHasPreCullingBits(true);
      }

      var1.setTransform3D(var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25, var27);
      var1.setDepthTest(var30);
      if (PulseLogger.PULSE_LOGGING_ENABLED) {
         PulseLogger.incrementCounter("Nodes rendered");
      }

      if (PrismSettings.showOverdraw) {
         if (var29) {
            this.painted |= 3 << var1.getClipRectIndex() * 2;
         } else {
            this.painted |= 1 << var1.getClipRectIndex() * 2;
         }
      }

   }

   protected boolean needsBlending() {
      Blend.Mode var1 = this.getNodeBlendMode();
      return var1 != null && var1 != Blend.Mode.SRC_OVER;
   }

   private void renderNodeBlendMode(Graphics var1) {
      BaseTransform var2 = var1.getTransformNoClone();
      BaseBounds var3 = this.getClippedBounds(new RectBounds(), var2);
      if (var3.isEmpty()) {
         this.clearDirtyTree();
      } else if (!this.isReadbackSupported(var1)) {
         if (this.getOpacity() < 1.0F) {
            this.renderOpacity(var1);
         } else if (this.getClipNode() != null) {
            this.renderClip(var1);
         } else {
            this.renderContent(var1);
         }

      } else {
         Rectangle var4 = new Rectangle(var3);
         var4.intersectWith(PrEffectHelper.getGraphicsClipNoClone(var1));
         FilterContext var5 = getFilterContext(var1);
         PrDrawable var6 = (PrDrawable)Effect.getCompatibleImage(var5, var4.width, var4.height);
         if (var6 == null) {
            this.clearDirtyTree();
         } else {
            Graphics var7 = var6.createGraphics();
            var7.setHasPreCullingBits(var1.hasPreCullingBits());
            var7.setClipRectIndex(var1.getClipRectIndex());
            var7.translate((float)(-var4.x), (float)(-var4.y));
            var7.transform(var2);
            if (this.getOpacity() < 1.0F) {
               this.renderOpacity(var7);
            } else if (this.getCacheFilter() != null) {
               this.renderCached(var7);
            } else if (this.getClipNode() != null) {
               this.renderClip(var1);
            } else if (this.getEffectFilter() != null) {
               this.renderEffect(var7);
            } else {
               this.renderContent(var7);
            }

            RTTexture var8 = ((ReadbackGraphics)var1).readBack(var4);
            PrDrawable var9 = PrDrawable.create(var5, var8);
            Blend var10 = new Blend(this.getNodeBlendMode(), new PassThrough(var9, var4), new PassThrough(var6, var4));
            CompositeMode var11 = var1.getCompositeMode();
            var1.setTransform((BaseTransform)null);
            var1.setCompositeMode(CompositeMode.SRC);
            PrEffectHelper.render(var10, var1, 0.0F, 0.0F, (Effect)null);
            var1.setCompositeMode(var11);
            Effect.releaseCompatibleImage(var5, var6);
            ((ReadbackGraphics)var1).releaseReadBackBuffer(var8);
         }
      }
   }

   private void renderRectClip(Graphics var1, NGRectangle var2) {
      Object var3 = var2.getShape().getBounds();
      if (!var2.getTransform().isIdentity()) {
         var3 = var2.getTransform().transform((BaseBounds)var3, (BaseBounds)var3);
      }

      BaseTransform var4 = var1.getTransformNoClone();
      Rectangle var5 = var1.getClipRectNoClone();
      BaseBounds var6 = var4.transform((BaseBounds)var3, (BaseBounds)var3);
      var6.intersectWith(PrEffectHelper.getGraphicsClipNoClone(var1));
      if (!var6.isEmpty() && var6.getWidth() != 0.0F && var6.getHeight() != 0.0F) {
         var1.setClipRect(new Rectangle(var6));
         this.renderForClip(var1);
         var1.setClipRect(var5);
         var2.clearDirty();
      } else {
         this.clearDirtyTree();
      }
   }

   void renderClip(Graphics var1) {
      if ((double)this.getClipNode().getOpacity() == 0.0) {
         this.clearDirtyTree();
      } else {
         BaseTransform var2 = var1.getTransformNoClone();
         BaseBounds var3 = this.getClippedBounds(new RectBounds(), var2);
         if (var3.isEmpty()) {
            this.clearDirtyTree();
         } else {
            if (this.getClipNode() instanceof NGRectangle) {
               NGRectangle var4 = (NGRectangle)this.getClipNode();
               if (var4.isRectClip(var2, false)) {
                  this.renderRectClip(var1, var4);
                  return;
               }
            }

            Rectangle var11 = new Rectangle(var3);
            var11.intersectWith(PrEffectHelper.getGraphicsClipNoClone(var1));
            if (!var2.is2D()) {
               Rectangle var12 = var1.getClipRect();
               var1.setClipRect(var11);
               NodeEffectInput var13 = new NodeEffectInput(this.getClipNode(), NodeEffectInput.RenderType.FULL_CONTENT);
               NodeEffectInput var14 = new NodeEffectInput(this, NodeEffectInput.RenderType.CLIPPED_CONTENT);
               Blend var15 = new Blend(Blend.Mode.SRC_IN, var13, var14);
               PrEffectHelper.render(var15, var1, 0.0F, 0.0F, (Effect)null);
               var13.flush();
               var14.flush();
               var1.setClipRect(var12);
               this.clearDirtyTree();
            } else {
               FilterContext var5 = getFilterContext(var1);
               PrDrawable var6 = (PrDrawable)Effect.getCompatibleImage(var5, var11.width, var11.height);
               if (var6 == null) {
                  this.clearDirtyTree();
               } else {
                  Graphics var7 = var6.createGraphics();
                  var7.setExtraAlpha(var1.getExtraAlpha());
                  var7.setHasPreCullingBits(var1.hasPreCullingBits());
                  var7.setClipRectIndex(var1.getClipRectIndex());
                  var7.translate((float)(-var11.x), (float)(-var11.y));
                  var7.transform(var2);
                  this.renderForClip(var7);
                  PrDrawable var8 = (PrDrawable)Effect.getCompatibleImage(var5, var11.width, var11.height);
                  if (var8 == null) {
                     this.getClipNode().clearDirtyTree();
                     Effect.releaseCompatibleImage(var5, var6);
                  } else {
                     Graphics var9 = var8.createGraphics();
                     var9.translate((float)(-var11.x), (float)(-var11.y));
                     var9.transform(var2);
                     this.getClipNode().render(var9);
                     var1.setTransform((BaseTransform)null);
                     Blend var10 = new Blend(Blend.Mode.SRC_IN, new PassThrough(var8, var11), new PassThrough(var6, var11));
                     PrEffectHelper.render(var10, var1, 0.0F, 0.0F, (Effect)null);
                     Effect.releaseCompatibleImage(var5, var6);
                     Effect.releaseCompatibleImage(var5, var8);
                  }
               }
            }
         }
      }
   }

   void renderForClip(Graphics var1) {
      if (this.getEffectFilter() != null) {
         this.renderEffect(var1);
      } else {
         this.renderContent(var1);
      }

   }

   private void renderOpacity(Graphics var1) {
      if (this.getEffectFilter() == null && this.getCacheFilter() == null && this.getClipNode() == null && this.hasOverlappingContents()) {
         FilterContext var9 = getFilterContext(var1);
         BaseTransform var3 = var1.getTransformNoClone();
         BaseBounds var4 = this.getContentBounds(new RectBounds(), var3);
         Rectangle var5 = new Rectangle(var4);
         var5.intersectWith(PrEffectHelper.getGraphicsClipNoClone(var1));
         PrDrawable var6 = (PrDrawable)Effect.getCompatibleImage(var9, var5.width, var5.height);
         if (var6 != null) {
            Graphics var7 = var6.createGraphics();
            var7.setHasPreCullingBits(var1.hasPreCullingBits());
            var7.setClipRectIndex(var1.getClipRectIndex());
            var7.translate((float)(-var5.x), (float)(-var5.y));
            var7.transform(var3);
            this.renderContent(var7);
            var1.setTransform((BaseTransform)null);
            float var8 = var1.getExtraAlpha();
            var1.setExtraAlpha(this.getOpacity() * var8);
            var1.drawTexture(var6.getTextureObject(), (float)var5.x, (float)var5.y, (float)var5.width, (float)var5.height);
            var1.setExtraAlpha(var8);
            Effect.releaseCompatibleImage(var9, var6);
         }
      } else {
         float var2 = var1.getExtraAlpha();
         var1.setExtraAlpha(var2 * this.getOpacity());
         if (this.getCacheFilter() != null) {
            this.renderCached(var1);
         } else if (this.getClipNode() != null) {
            this.renderClip(var1);
         } else if (this.getEffectFilter() != null) {
            this.renderEffect(var1);
         } else {
            this.renderContent(var1);
         }

         var1.setExtraAlpha(var2);
      }
   }

   private void renderCached(Graphics var1) {
      if (this.isContentBounds2D() && var1.getTransformNoClone().is2D() && !(var1 instanceof PrinterGraphics)) {
         this.getCacheFilter().render(var1);
      } else {
         this.renderContent(var1);
      }

   }

   protected void renderEffect(Graphics var1) {
      this.getEffectFilter().render(var1);
   }

   protected abstract void renderContent(Graphics var1);

   protected abstract boolean hasOverlappingContents();

   boolean isReadbackSupported(Graphics var1) {
      return var1 instanceof ReadbackGraphics && ((ReadbackGraphics)var1).canReadBack();
   }

   static FilterContext getFilterContext(Graphics var0) {
      Screen var1 = var0.getAssociatedScreen();
      return var1 == null ? PrFilterContext.getPrinterContext(var0.getResourceFactory()) : PrFilterContext.getInstance(var1);
   }

   public void release() {
   }

   public String toString() {
      return this.name == null ? super.toString() : this.name;
   }

   public void applyTransform(BaseTransform var1, DirtyRegionContainer var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         var2.setDirtyRegion(var3, (RectBounds)var1.transform((BaseBounds)var2.getDirtyRegion(var3), (BaseBounds)var2.getDirtyRegion(var3)));
         if (var2.checkAndClearRegion(var3)) {
            --var3;
         }
      }

   }

   public void applyClip(BaseBounds var1, DirtyRegionContainer var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         var2.getDirtyRegion(var3).intersectWith(var1);
         if (var2.checkAndClearRegion(var3)) {
            --var3;
         }
      }

   }

   public void applyEffect(EffectFilter var1, DirtyRegionContainer var2, DirtyRegionPool var3) {
      Effect var4 = var1.getEffect();
      EffectDirtyBoundsHelper var5 = NGNode.EffectDirtyBoundsHelper.getInstance();
      var5.setInputBounds(this.contentBounds);
      var5.setDirtyRegions(var2);
      DirtyRegionContainer var6 = var4.getDirtyRegions(var5, var3);
      var2.deriveWithNewContainer(var6);
      var3.checkIn(var6);
   }

   static {
      effectsSupported = pipeline == null ? false : pipeline.isEffectSupported();
      TEMP_BOUNDS = new BoxBounds();
      TEMP_RECT_BOUNDS = new RectBounds();
      TEMP_TRANSFORM = new Affine3D();
      TEMP_POINTS2D_4 = new Point2D[]{new Point2D(), new Point2D(), new Point2D(), new Point2D()};
   }

   private static class EffectDirtyBoundsHelper extends Effect {
      private BaseBounds bounds;
      private static EffectDirtyBoundsHelper instance = null;
      private DirtyRegionContainer drc;

      public void setInputBounds(BaseBounds var1) {
         this.bounds = var1;
      }

      public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
         throw new UnsupportedOperationException();
      }

      public BaseBounds getBounds(BaseTransform var1, Effect var2) {
         return (BaseBounds)(this.bounds.getBoundsType() == BaseBounds.BoundsType.RECTANGLE ? this.bounds : new RectBounds(this.bounds.getMinX(), this.bounds.getMinY(), this.bounds.getMaxX(), this.bounds.getMaxY()));
      }

      public Effect.AccelType getAccelType(FilterContext var1) {
         return null;
      }

      public static EffectDirtyBoundsHelper getInstance() {
         if (instance == null) {
            instance = new EffectDirtyBoundsHelper();
         }

         return instance;
      }

      public boolean reducesOpaquePixels() {
         return true;
      }

      private void setDirtyRegions(DirtyRegionContainer var1) {
         this.drc = var1;
      }

      public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
         DirtyRegionContainer var3 = var2.checkOut();
         var3.deriveWithNewContainer(this.drc);
         return var3;
      }
   }

   private static class PassThrough extends Effect {
      private PrDrawable img;
      private Rectangle bounds;

      PassThrough(PrDrawable var1, Rectangle var2) {
         this.img = var1;
         this.bounds = var2;
      }

      public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
         this.img.lock();
         ImageData var6 = new ImageData(var1, this.img, new Rectangle(this.bounds));
         var6.setReusable(true);
         return var6;
      }

      public RectBounds getBounds(BaseTransform var1, Effect var2) {
         return new RectBounds(this.bounds);
      }

      public Effect.AccelType getAccelType(FilterContext var1) {
         return Effect.AccelType.INTRINSIC;
      }

      public boolean reducesOpaquePixels() {
         return false;
      }

      public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
         return null;
      }
   }

   protected static enum RenderRootResult {
      NO_RENDER_ROOT,
      HAS_RENDER_ROOT,
      HAS_RENDER_ROOT_AND_IS_CLEAN;
   }

   public static enum DirtyFlag {
      CLEAN,
      DIRTY_BY_TRANSLATION,
      DIRTY;
   }
}
