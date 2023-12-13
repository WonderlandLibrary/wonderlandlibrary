package javafx.scene;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.SubSceneHelper;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.SubSceneTraversalEngine;
import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGSubScene;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point3D;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Paint;
import sun.util.logging.PlatformLogger;

public class SubScene extends Node {
   private static boolean is3DSupported;
   private final SceneAntialiasing antiAliasing;
   private final boolean depthBuffer;
   private ObjectProperty root;
   private ObjectProperty camera;
   private Camera defaultCamera;
   private DoubleProperty width;
   private DoubleProperty height;
   private ObjectProperty fill;
   private ObjectProperty userAgentStylesheet;
   boolean dirtyLayout;
   private boolean dirtyNodes;
   private TopMostTraversalEngine traversalEngine;
   private int dirtyBits;
   private List lights;

   public SubScene(@NamedArg("root") Parent var1, @NamedArg("width") double var2, @NamedArg("height") double var4) {
      this(var1, var2, var4, false, SceneAntialiasing.DISABLED);
   }

   public SubScene(@NamedArg("root") Parent var1, @NamedArg("width") double var2, @NamedArg("height") double var4, @NamedArg("depthBuffer") boolean var6, @NamedArg("antiAliasing") SceneAntialiasing var7) {
      this.userAgentStylesheet = null;
      this.dirtyLayout = false;
      this.dirtyNodes = false;
      this.traversalEngine = new SubSceneTraversalEngine(this);
      this.dirtyBits = -1;
      this.lights = new ArrayList();
      this.depthBuffer = var6;
      this.antiAliasing = var7;
      boolean var8 = var7 != null && var7 != SceneAntialiasing.DISABLED;
      this.setRoot(var1);
      this.setWidth(var2);
      this.setHeight(var4);
      String var9;
      if ((var6 || var8) && !is3DSupported) {
         var9 = SubScene.class.getName();
         PlatformLogger.getLogger(var9).warning("System can't support ConditionalFeature.SCENE3D");
      }

      if (var8 && !Toolkit.getToolkit().isMSAASupported()) {
         var9 = SubScene.class.getName();
         PlatformLogger.getLogger(var9).warning("System can't support antiAliasing");
      }

   }

   public final SceneAntialiasing getAntiAliasing() {
      return this.antiAliasing;
   }

   public final boolean isDepthBuffer() {
      return this.depthBuffer;
   }

   private boolean isDepthBufferInternal() {
      return is3DSupported ? this.depthBuffer : false;
   }

   public final void setRoot(Parent var1) {
      this.rootProperty().set(var1);
   }

   public final Parent getRoot() {
      return this.root == null ? null : (Parent)this.root.get();
   }

   public final ObjectProperty rootProperty() {
      if (this.root == null) {
         this.root = new ObjectPropertyBase() {
            private Parent oldRoot;

            private void forceUnbind() {
               System.err.println("Unbinding illegal root.");
               this.unbind();
            }

            protected void invalidated() {
               Parent var1 = (Parent)this.get();
               if (var1 == null) {
                  if (this.isBound()) {
                     this.forceUnbind();
                  }

                  throw new NullPointerException("Scene's root cannot be null");
               } else if (var1.getParent() != null) {
                  if (this.isBound()) {
                     this.forceUnbind();
                  }

                  throw new IllegalArgumentException(var1 + "is already inside a scene-graph and cannot be set as root");
               } else if (var1.getClipParent() != null) {
                  if (this.isBound()) {
                     this.forceUnbind();
                  }

                  throw new IllegalArgumentException(var1 + "is set as a clip on another node, so cannot be set as root");
               } else if ((var1.getScene() == null || var1.getScene().getRoot() != var1) && (var1.getSubScene() == null || var1.getSubScene().getRoot() != var1 || var1.getSubScene() == SubScene.this)) {
                  var1.setTreeVisible(SubScene.this.impl_isTreeVisible());
                  var1.setDisabled(SubScene.this.isDisabled());
                  if (this.oldRoot != null) {
                     StyleManager.getInstance().forget(SubScene.this);
                     this.oldRoot.setScenes((Scene)null, (SubScene)null);
                  }

                  this.oldRoot = var1;
                  var1.getStyleClass().add(0, "root");
                  var1.setScenes(SubScene.this.getScene(), SubScene.this);
                  SubScene.this.markDirty(SubScene.SubSceneDirtyBits.ROOT_SG_DIRTY);
                  var1.resize(SubScene.this.getWidth(), SubScene.this.getHeight());
                  var1.requestLayout();
               } else {
                  if (this.isBound()) {
                     this.forceUnbind();
                  }

                  throw new IllegalArgumentException(var1 + "is already set as root of another scene or subScene");
               }
            }

            public Object getBean() {
               return SubScene.this;
            }

            public String getName() {
               return "root";
            }
         };
      }

      return this.root;
   }

   public final void setCamera(Camera var1) {
      this.cameraProperty().set(var1);
   }

   public final Camera getCamera() {
      return this.camera == null ? null : (Camera)this.camera.get();
   }

   public final ObjectProperty cameraProperty() {
      if (this.camera == null) {
         this.camera = new ObjectPropertyBase() {
            Camera oldCamera = null;

            protected void invalidated() {
               Camera var1 = (Camera)this.get();
               if (var1 != null) {
                  if (var1 instanceof PerspectiveCamera && !SubScene.is3DSupported) {
                     String var2 = SubScene.class.getName();
                     PlatformLogger.getLogger(var2).warning("System can't support ConditionalFeature.SCENE3D");
                  }

                  if ((var1.getScene() != null || var1.getSubScene() != null) && (var1.getScene() != SubScene.this.getScene() || var1.getSubScene() != SubScene.this)) {
                     throw new IllegalArgumentException(var1 + "is already part of other scene or subscene");
                  }

                  var1.setOwnerSubScene(SubScene.this);
                  var1.setViewWidth(SubScene.this.getWidth());
                  var1.setViewHeight(SubScene.this.getHeight());
               }

               SubScene.this.markDirty(SubScene.SubSceneDirtyBits.CAMERA_DIRTY);
               if (this.oldCamera != null && this.oldCamera != var1) {
                  this.oldCamera.setOwnerSubScene((SubScene)null);
               }

               this.oldCamera = var1;
            }

            public Object getBean() {
               return SubScene.this;
            }

            public String getName() {
               return "camera";
            }
         };
      }

      return this.camera;
   }

   Camera getEffectiveCamera() {
      Camera var1 = this.getCamera();
      if (var1 == null || var1 instanceof PerspectiveCamera && !is3DSupported) {
         if (this.defaultCamera == null) {
            this.defaultCamera = new ParallelCamera();
            this.defaultCamera.setOwnerSubScene(this);
            this.defaultCamera.setViewWidth(this.getWidth());
            this.defaultCamera.setViewHeight(this.getHeight());
         }

         return this.defaultCamera;
      } else {
         return var1;
      }
   }

   final void markContentDirty() {
      this.markDirty(SubScene.SubSceneDirtyBits.CONTENT_DIRTY);
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
               Parent var1 = SubScene.this.getRoot();
               if (var1.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                  var1.impl_transformsChanged();
               }

               if (var1.isResizable()) {
                  var1.resize(this.get() - var1.getLayoutX() - var1.getTranslateX(), var1.getLayoutBounds().getHeight());
               }

               SubScene.this.markDirty(SubScene.SubSceneDirtyBits.SIZE_DIRTY);
               SubScene.this.impl_geomChanged();
               SubScene.this.getEffectiveCamera().setViewWidth(this.get());
            }

            public Object getBean() {
               return SubScene.this;
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
               Parent var1 = SubScene.this.getRoot();
               if (var1.isResizable()) {
                  var1.resize(var1.getLayoutBounds().getWidth(), this.get() - var1.getLayoutY() - var1.getTranslateY());
               }

               SubScene.this.markDirty(SubScene.SubSceneDirtyBits.SIZE_DIRTY);
               SubScene.this.impl_geomChanged();
               SubScene.this.getEffectiveCamera().setViewHeight(this.get());
            }

            public Object getBean() {
               return SubScene.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   public final void setFill(Paint var1) {
      this.fillProperty().set(var1);
   }

   public final Paint getFill() {
      return this.fill == null ? null : (Paint)this.fill.get();
   }

   public final ObjectProperty fillProperty() {
      if (this.fill == null) {
         this.fill = new ObjectPropertyBase((Paint)null) {
            protected void invalidated() {
               SubScene.this.markDirty(SubScene.SubSceneDirtyBits.FILL_DIRTY);
            }

            public Object getBean() {
               return SubScene.this;
            }

            public String getName() {
               return "fill";
            }
         };
      }

      return this.fill;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      this.dirtyNodes = false;
      if (this.isDirty()) {
         NGSubScene var1 = (NGSubScene)this.impl_getPeer();
         Camera var2 = this.getEffectiveCamera();
         boolean var3 = false;
         if (var2.getSubScene() == null && this.isDirty(SubScene.SubSceneDirtyBits.CONTENT_DIRTY)) {
            var2.impl_syncPeer();
         }

         if (this.isDirty(SubScene.SubSceneDirtyBits.FILL_DIRTY)) {
            Object var4 = this.getFill() == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(this.getFill());
            var1.setFillPaint(var4);
            var3 = true;
         }

         if (this.isDirty(SubScene.SubSceneDirtyBits.SIZE_DIRTY)) {
            var1.setWidth((float)this.getWidth());
            var1.setHeight((float)this.getHeight());
         }

         if (this.isDirty(SubScene.SubSceneDirtyBits.CAMERA_DIRTY)) {
            var1.setCamera((NGCamera)var2.impl_getPeer());
            var3 = true;
         }

         if (this.isDirty(SubScene.SubSceneDirtyBits.ROOT_SG_DIRTY)) {
            var1.setRoot(this.getRoot().impl_getPeer());
            var3 = true;
         }

         var3 |= this.syncLights();
         if (var3 || this.isDirty(SubScene.SubSceneDirtyBits.CONTENT_DIRTY)) {
            var1.markContentDirty();
         }

         this.clearDirtyBits();
      }

   }

   void nodeResolvedOrientationChanged() {
      this.getRoot().parentResolvedOrientationInvalidated();
   }

   /** @deprecated */
   @Deprecated
   protected void impl_processCSS(WritableValue var1) {
      if (this.cssFlag != CssFlags.CLEAN) {
         if (this.getRoot().cssFlag == CssFlags.CLEAN) {
            this.getRoot().cssFlag = this.cssFlag;
         }

         super.impl_processCSS(var1);
         this.getRoot().processCSS();
      }
   }

   void processCSS() {
      Parent var1 = this.getRoot();
      if (var1.impl_isDirty(DirtyBits.NODE_CSS)) {
         var1.impl_clearDirty(DirtyBits.NODE_CSS);
         if (this.cssFlag == CssFlags.CLEAN) {
            this.cssFlag = CssFlags.UPDATE;
         }
      }

      super.processCSS();
   }

   public final ObjectProperty userAgentStylesheetProperty() {
      if (this.userAgentStylesheet == null) {
         this.userAgentStylesheet = new SimpleObjectProperty(this, "userAgentStylesheet", (String)null) {
            protected void invalidated() {
               StyleManager.getInstance().forget(SubScene.this);
               SubScene.this.impl_reapplyCSS();
            }
         };
      }

      return this.userAgentStylesheet;
   }

   public final String getUserAgentStylesheet() {
      return this.userAgentStylesheet == null ? null : (String)this.userAgentStylesheet.get();
   }

   public final void setUserAgentStylesheet(String var1) {
      this.userAgentStylesheetProperty().set(var1);
   }

   void updateBounds() {
      super.updateBounds();
      this.getRoot().updateBounds();
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      if (!is3DSupported) {
         return new NGSubScene(false, false);
      } else {
         boolean var1 = this.antiAliasing != null && this.antiAliasing != SceneAntialiasing.DISABLED;
         return new NGSubScene(this.depthBuffer, var1 && Toolkit.getToolkit().isMSAASupported());
      }
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      int var3 = (int)Math.ceil(this.width.get());
      int var4 = (int)Math.ceil(this.height.get());
      var1 = var1.deriveWithNewBounds(0.0F, 0.0F, 0.0F, (float)var3, (float)var4, 0.0F);
      var1 = var2.transform(var1, var1);
      return var1;
   }

   void setDirtyLayout(Parent var1) {
      if (!this.dirtyLayout && var1 != null && var1.getSubScene() == this && this.getScene() != null) {
         this.dirtyLayout = true;
         this.markDirtyLayoutBranch();
         this.markDirty(SubScene.SubSceneDirtyBits.CONTENT_DIRTY);
      }

   }

   void setDirty(Node var1) {
      if (!this.dirtyNodes && var1 != null && var1.getSubScene() == this && this.getScene() != null) {
         this.dirtyNodes = true;
         this.markDirty(SubScene.SubSceneDirtyBits.CONTENT_DIRTY);
      }

   }

   void layoutPass() {
      if (this.dirtyLayout) {
         Parent var1 = this.getRoot();
         if (var1 != null) {
            var1.layout();
         }

         this.dirtyLayout = false;
      }

   }

   boolean traverse(Node var1, Direction var2) {
      return this.traversalEngine.trav(var1, var2) != null;
   }

   private void clearDirtyBits() {
      this.dirtyBits = 0;
   }

   private boolean isDirty() {
      return this.dirtyBits != 0;
   }

   private void setDirty(SubSceneDirtyBits var1) {
      this.dirtyBits |= var1.getMask();
   }

   private boolean isDirty(SubSceneDirtyBits var1) {
      return (this.dirtyBits & var1.getMask()) != 0;
   }

   private void markDirty(SubSceneDirtyBits var1) {
      if (!this.isDirty()) {
         this.impl_markDirty(DirtyBits.NODE_CONTENTS);
      }

      this.setDirty(var1);
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      return this.subSceneComputeContains(var1, var3) ? true : this.getRoot().impl_computeContains(var1, var3);
   }

   private boolean subSceneComputeContains(double var1, double var3) {
      if (!(var1 < 0.0) && !(var3 < 0.0) && !(var1 > this.getWidth()) && !(var3 > this.getHeight())) {
         return this.getFill() != null;
      } else {
         return false;
      }
   }

   private PickResult pickRootSG(double var1, double var3) {
      double var5 = this.getWidth();
      double var7 = this.getHeight();
      if (!(var1 < 0.0) && !(var3 < 0.0) && !(var1 > var5) && !(var3 > var7)) {
         PickResultChooser var9 = new PickResultChooser();
         PickRay var10 = this.getEffectiveCamera().computePickRay(var1, var3, new PickRay());
         var10.getDirectionNoClone().normalize();
         this.getRoot().impl_pickNode(var10, var9);
         return var9.toPickResult();
      } else {
         return null;
      }
   }

   /** @deprecated */
   @Deprecated
   protected void impl_pickNodeLocal(PickRay var1, PickResultChooser var2) {
      double var3 = this.impl_intersectsBounds(var1);
      if (!Double.isNaN(var3) && var2.isCloser(var3)) {
         Point3D var5 = PickResultChooser.computePoint(var1, var3);
         PickResult var6 = this.pickRootSG(var5.getX(), var5.getY());
         if (var6 != null) {
            var2.offerSubScenePickResult(this, var6, var3);
         } else if (this.isPickOnBounds() || this.subSceneComputeContains(var5.getX(), var5.getY())) {
            var2.offer(this, var3, var5);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   final void addLight(LightBase var1) {
      if (!this.lights.contains(var1)) {
         this.markDirty(SubScene.SubSceneDirtyBits.LIGHTS_DIRTY);
         this.lights.add(var1);
      }

   }

   final void removeLight(LightBase var1) {
      if (this.lights.remove(var1)) {
         this.markDirty(SubScene.SubSceneDirtyBits.LIGHTS_DIRTY);
      }

   }

   private boolean syncLights() {
      boolean var1 = false;
      if (!this.isDirty(SubScene.SubSceneDirtyBits.LIGHTS_DIRTY)) {
         return var1;
      } else {
         NGSubScene var2 = (NGSubScene)this.impl_getPeer();
         NGLightBase[] var3 = var2.getLights();
         if (!this.lights.isEmpty() || var3 != null) {
            if (this.lights.isEmpty()) {
               var2.setLights((NGLightBase[])null);
            } else {
               if (var3 == null || var3.length < this.lights.size()) {
                  var3 = new NGLightBase[this.lights.size()];
               }

               int var4;
               for(var4 = 0; var4 < this.lights.size(); ++var4) {
                  var3[var4] = (NGLightBase)((LightBase)this.lights.get(var4)).impl_getPeer();
               }

               while(var4 < var3.length && var3[var4] != null) {
                  var3[var4++] = null;
               }

               var2.setLights(var3);
            }

            var1 = true;
         }

         return var1;
      }
   }

   static {
      is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
      SubSceneHelper.setSubSceneAccessor(new SubSceneHelper.SubSceneAccessor() {
         public boolean isDepthBuffer(SubScene var1) {
            return var1.isDepthBufferInternal();
         }

         public Camera getEffectiveCamera(SubScene var1) {
            return var1.getEffectiveCamera();
         }
      });
   }

   private static enum SubSceneDirtyBits {
      SIZE_DIRTY,
      FILL_DIRTY,
      ROOT_SG_DIRTY,
      CAMERA_DIRTY,
      LIGHTS_DIRTY,
      CONTENT_DIRTY;

      private int mask = 1 << this.ordinal();

      public final int getMask() {
         return this.mask;
      }
   }
}
