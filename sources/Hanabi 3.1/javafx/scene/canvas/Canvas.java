package javafx.scene.canvas;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.sg.prism.NGCanvas;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;

public class Canvas extends Node {
   static final int DEFAULT_VAL_BUF_SIZE = 1024;
   static final int DEFAULT_OBJ_BUF_SIZE = 32;
   private static final int SIZE_HISTORY = 5;
   private GrowableDataBuffer current;
   private boolean rendererBehind;
   private int[] recentvalsizes;
   private int[] recentobjsizes;
   private int lastsizeindex;
   private GraphicsContext theContext;
   private DoubleProperty width;
   private DoubleProperty height;

   public Canvas() {
      this(0.0, 0.0);
   }

   public Canvas(double var1, double var3) {
      this.recentvalsizes = new int[5];
      this.recentobjsizes = new int[5];
      this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
      this.setWidth(var1);
      this.setHeight(var3);
   }

   private static int max(int[] var0, int var1) {
      int[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         if (var1 < var5) {
            var1 = var5;
         }
      }

      return var1;
   }

   GrowableDataBuffer getBuffer() {
      this.impl_markDirty(DirtyBits.NODE_CONTENTS);
      this.impl_markDirty(DirtyBits.NODE_FORCE_SYNC);
      if (this.current == null) {
         int var1 = max(this.recentvalsizes, 1024);
         int var2 = max(this.recentobjsizes, 32);
         this.current = GrowableDataBuffer.getBuffer(var1, var2);
         this.theContext.updateDimensions();
      }

      return this.current;
   }

   boolean isRendererFallingBehind() {
      return this.rendererBehind;
   }

   public GraphicsContext getGraphicsContext2D() {
      if (this.theContext == null) {
         this.theContext = new GraphicsContext(this);
      }

      return this.theContext;
   }

   public final void setWidth(double var1) {
      this.widthProperty().set(var1);
   }

   public final double getWidth() {
      return this.width == null ? 0.0 : this.width.get();
   }

   public final DoubleProperty widthProperty() {
      if (this.width == null) {
         this.width = new DoublePropertyBase() {
            public void invalidated() {
               Canvas.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Canvas.this.impl_geomChanged();
               if (Canvas.this.theContext != null) {
                  Canvas.this.theContext.updateDimensions();
               }

            }

            public Object getBean() {
               return Canvas.this;
            }

            public String getName() {
               return "width";
            }
         };
      }

      return this.width;
   }

   public final void setHeight(double var1) {
      this.heightProperty().set(var1);
   }

   public final double getHeight() {
      return this.height == null ? 0.0 : this.height.get();
   }

   public final DoubleProperty heightProperty() {
      if (this.height == null) {
         this.height = new DoublePropertyBase() {
            public void invalidated() {
               Canvas.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Canvas.this.impl_geomChanged();
               if (Canvas.this.theContext != null) {
                  Canvas.this.theContext.updateDimensions();
               }

            }

            public Object getBean() {
               return Canvas.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGCanvas();
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGCanvas var1;
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         var1 = (NGCanvas)this.impl_getPeer();
         var1.updateBounds((float)this.getWidth(), (float)this.getHeight());
      }

      if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
         var1 = (NGCanvas)this.impl_getPeer();
         if (this.current != null && !this.current.isEmpty()) {
            if (--this.lastsizeindex < 0) {
               this.lastsizeindex = 4;
            }

            this.recentvalsizes[this.lastsizeindex] = this.current.writeValuePosition();
            this.recentobjsizes[this.lastsizeindex] = this.current.writeObjectPosition();
            this.rendererBehind = var1.updateRendering(this.current);
            this.current = null;
         }
      }

   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      double var5 = this.getWidth();
      double var7 = this.getHeight();
      return var5 > 0.0 && var7 > 0.0 && var1 >= 0.0 && var3 >= 0.0 && var1 < var5 && var3 < var7;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      RectBounds var3 = new RectBounds(0.0F, 0.0F, (float)this.getWidth(), (float)this.getHeight());
      var1 = var2.transform((BaseBounds)var3, (BaseBounds)var3);
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      return var1.processLeafNode(this, var2);
   }
}
