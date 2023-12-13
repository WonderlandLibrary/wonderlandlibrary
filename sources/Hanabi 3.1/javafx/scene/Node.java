package javafx.scene;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.UnmodifiableListSet;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.CursorConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.geometry.BoundsUtils;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.scene.CameraHelper;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.EventHandlerProperties;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.NodeEventDispatcher;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.SceneUtils;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.transform.TransformUtils;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.TempState;
import com.sun.javafx.util.Utils;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.PseudoClass;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Window;
import javafx.util.Callback;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

@IDProperty("id")
public abstract class Node implements EventTarget, Styleable {
   private int dirtyBits;
   private BaseBounds _geomBounds = new RectBounds(0.0F, 0.0F, -1.0F, -1.0F);
   private BaseBounds _txBounds = new RectBounds(0.0F, 0.0F, -1.0F, -1.0F);
   private boolean pendingUpdateBounds = false;
   private static final Object USER_DATA_KEY;
   private ObservableMap properties;
   private ReadOnlyObjectWrapper parent;
   private final InvalidationListener parentDisabledChangedListener = (var1) -> {
      this.updateDisabled();
   };
   private final InvalidationListener parentTreeVisibleChangedListener = (var1) -> {
      this.updateTreeVisible(true);
   };
   private SubScene subScene = null;
   private ReadOnlyObjectWrapperManualFire scene = new ReadOnlyObjectWrapperManualFire();
   private StringProperty id;
   private ObservableList styleClass = new TrackableObservableList() {
      protected void onChanged(ListChangeListener.Change var1) {
         Node.this.impl_reapplyCSS();
      }

      public String toString() {
         if (this.size() == 0) {
            return "";
         } else if (this.size() == 1) {
            return (String)this.get(0);
         } else {
            StringBuilder var1 = new StringBuilder();

            for(int var2 = 0; var2 < this.size(); ++var2) {
               var1.append((String)this.get(var2));
               if (var2 + 1 < this.size()) {
                  var1.append(' ');
               }
            }

            return var1.toString();
         }
      }
   };
   private StringProperty style;
   private BooleanProperty visible;
   private DoubleProperty opacity;
   private ObjectProperty blendMode;
   private boolean derivedDepthTest = true;
   private BooleanProperty pickOnBounds;
   private ReadOnlyBooleanWrapper disabled;
   private Node clipParent;
   private NGNode peer;
   private BooleanProperty managed;
   private DoubleProperty layoutX;
   private DoubleProperty layoutY;
   public static final double BASELINE_OFFSET_SAME_AS_HEIGHT = Double.NEGATIVE_INFINITY;
   private LazyBoundsProperty layoutBounds = new LazyBoundsProperty() {
      protected Bounds computeBounds() {
         return Node.this.impl_computeLayoutBounds();
      }

      public Object getBean() {
         return Node.this;
      }

      public String getName() {
         return "layoutBounds";
      }
   };
   private BaseTransform localToParentTx;
   private boolean transformDirty;
   private BaseBounds txBounds;
   private BaseBounds geomBounds;
   private BaseBounds localBounds;
   boolean boundsChanged;
   private boolean geomBoundsInvalid;
   private boolean localBoundsInvalid;
   private boolean txBoundsInvalid;
   private static final double EPSILON_ABSOLUTE = 1.0E-5;
   private NodeTransformation nodeTransformation;
   private static final double DEFAULT_TRANSLATE_X = 0.0;
   private static final double DEFAULT_TRANSLATE_Y = 0.0;
   private static final double DEFAULT_TRANSLATE_Z = 0.0;
   private static final double DEFAULT_SCALE_X = 1.0;
   private static final double DEFAULT_SCALE_Y = 1.0;
   private static final double DEFAULT_SCALE_Z = 1.0;
   private static final double DEFAULT_ROTATE = 0.0;
   private static final Point3D DEFAULT_ROTATION_AXIS;
   private EventHandlerProperties eventHandlerProperties;
   private ObjectProperty nodeOrientation;
   private EffectiveOrientationProperty effectiveNodeOrientationProperty;
   private static final byte EFFECTIVE_ORIENTATION_LTR = 0;
   private static final byte EFFECTIVE_ORIENTATION_RTL = 1;
   private static final byte EFFECTIVE_ORIENTATION_MASK = 1;
   private static final byte AUTOMATIC_ORIENTATION_LTR = 0;
   private static final byte AUTOMATIC_ORIENTATION_RTL = 2;
   private static final byte AUTOMATIC_ORIENTATION_MASK = 2;
   private byte resolvedNodeOrientation;
   private MiscProperties miscProperties;
   private static final boolean DEFAULT_CACHE = false;
   private static final CacheHint DEFAULT_CACHE_HINT;
   private static final Node DEFAULT_CLIP;
   private static final Cursor DEFAULT_CURSOR;
   private static final DepthTest DEFAULT_DEPTH_TEST;
   private static final boolean DEFAULT_DISABLE = false;
   private static final Effect DEFAULT_EFFECT;
   private static final InputMethodRequests DEFAULT_INPUT_METHOD_REQUESTS;
   private static final boolean DEFAULT_MOUSE_TRANSPARENT = false;
   private ReadOnlyBooleanWrapper hover;
   private ReadOnlyBooleanWrapper pressed;
   private FocusedProperty focused;
   private BooleanProperty focusTraversable;
   private boolean treeVisible;
   private TreeVisiblePropertyReadOnly treeVisibleRO;
   private boolean canReceiveFocus;
   /** @deprecated */
   @Deprecated
   private BooleanProperty impl_showMnemonics;
   private Node labeledBy;
   private ObjectProperty eventDispatcher;
   private NodeEventDispatcher internalEventDispatcher;
   private EventDispatcher preprocessMouseEventDispatcher;
   CssFlags cssFlag;
   final ObservableSet pseudoClassStates;
   CssStyleHelper styleHelper;
   private static final PseudoClass HOVER_PSEUDOCLASS_STATE;
   private static final PseudoClass PRESSED_PSEUDOCLASS_STATE;
   private static final PseudoClass DISABLED_PSEUDOCLASS_STATE;
   private static final PseudoClass FOCUSED_PSEUDOCLASS_STATE;
   private static final PseudoClass SHOW_MNEMONICS_PSEUDOCLASS_STATE;
   private static final BoundsAccessor boundsAccessor;
   private ObjectProperty accessibleRole;
   AccessibilityProperties accessibilityProperties;
   Accessible accessible;

   /** @deprecated */
   @Deprecated
   protected void impl_markDirty(DirtyBits var1) {
      if (this.impl_isDirtyEmpty()) {
         this.addToSceneDirtyList();
      }

      this.dirtyBits = (int)((long)this.dirtyBits | var1.getMask());
   }

   private void addToSceneDirtyList() {
      Scene var1 = this.getScene();
      if (var1 != null) {
         var1.addToDirtyList(this);
         if (this.getSubScene() != null) {
            this.getSubScene().setDirty(this);
         }
      }

   }

   /** @deprecated */
   @Deprecated
   protected final boolean impl_isDirty(DirtyBits var1) {
      return ((long)this.dirtyBits & var1.getMask()) != 0L;
   }

   /** @deprecated */
   @Deprecated
   protected final void impl_clearDirty(DirtyBits var1) {
      this.dirtyBits = (int)((long)this.dirtyBits & ~var1.getMask());
   }

   private void setDirty() {
      this.dirtyBits = -1;
   }

   private void clearDirty() {
      this.dirtyBits = 0;
   }

   /** @deprecated */
   @Deprecated
   protected final boolean impl_isDirtyEmpty() {
      return this.dirtyBits == 0;
   }

   /** @deprecated */
   @Deprecated
   public final void impl_syncPeer() {
      if (!this.impl_isDirtyEmpty() && (this.treeVisible || this.impl_isDirty(DirtyBits.NODE_VISIBLE) || this.impl_isDirty(DirtyBits.NODE_FORCE_SYNC))) {
         this.impl_updatePeer();
         this.clearDirty();
      }

   }

   void updateBounds() {
      Node var1 = this.getClip();
      if (var1 != null) {
         var1.updateBounds();
      }

      if (!this.treeVisible && !this.impl_isDirty(DirtyBits.NODE_VISIBLE)) {
         if (this.impl_isDirty(DirtyBits.NODE_TRANSFORM) || this.impl_isDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS) || this.impl_isDirty(DirtyBits.NODE_BOUNDS)) {
            this.pendingUpdateBounds = true;
         }

      } else {
         if (this.pendingUpdateBounds) {
            this.impl_markDirty(DirtyBits.NODE_TRANSFORM);
            this.impl_markDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS);
            this.impl_markDirty(DirtyBits.NODE_BOUNDS);
            this.pendingUpdateBounds = false;
         }

         if (this.impl_isDirty(DirtyBits.NODE_TRANSFORM) || this.impl_isDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS)) {
            if (this.impl_isDirty(DirtyBits.NODE_TRANSFORM)) {
               this.updateLocalToParentTransform();
            }

            this._txBounds = this.getTransformedBounds(this._txBounds, BaseTransform.IDENTITY_TRANSFORM);
         }

         if (this.impl_isDirty(DirtyBits.NODE_BOUNDS)) {
            this._geomBounds = this.getGeomBounds(this._geomBounds, BaseTransform.IDENTITY_TRANSFORM);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      NGNode var1 = this.impl_getPeer();
      if (PrismSettings.printRenderGraph && this.impl_isDirty(DirtyBits.DEBUG)) {
         String var2 = this.getId();
         String var3 = this.getClass().getSimpleName();
         if (var3.isEmpty()) {
            var3 = this.getClass().getName();
         }

         var1.setName(var2 == null ? var3 : var2 + "(" + var3 + ")");
      }

      if (this.impl_isDirty(DirtyBits.NODE_TRANSFORM)) {
         var1.setTransformMatrix(this.localToParentTx);
      }

      if (this.impl_isDirty(DirtyBits.NODE_BOUNDS)) {
         var1.setContentBounds(this._geomBounds);
      }

      if (this.impl_isDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS)) {
         var1.setTransformedBounds(this._txBounds, !this.impl_isDirty(DirtyBits.NODE_BOUNDS));
      }

      if (this.impl_isDirty(DirtyBits.NODE_OPACITY)) {
         var1.setOpacity((float)Utils.clamp(0.0, this.getOpacity(), 1.0));
      }

      if (this.impl_isDirty(DirtyBits.NODE_CACHE)) {
         var1.setCachedAsBitmap(this.isCache(), this.getCacheHint());
      }

      if (this.impl_isDirty(DirtyBits.NODE_CLIP)) {
         var1.setClipNode(this.getClip() != null ? this.getClip().impl_getPeer() : null);
      }

      if (this.impl_isDirty(DirtyBits.EFFECT_EFFECT) && this.getEffect() != null) {
         this.getEffect().impl_sync();
         var1.effectChanged();
      }

      if (this.impl_isDirty(DirtyBits.NODE_EFFECT)) {
         var1.setEffect(this.getEffect() != null ? this.getEffect().impl_getImpl() : null);
      }

      if (this.impl_isDirty(DirtyBits.NODE_VISIBLE)) {
         var1.setVisible(this.isVisible());
      }

      if (this.impl_isDirty(DirtyBits.NODE_DEPTH_TEST)) {
         var1.setDepthTest(this.isDerivedDepthTest());
      }

      if (this.impl_isDirty(DirtyBits.NODE_BLENDMODE)) {
         BlendMode var4 = this.getBlendMode();
         var1.setNodeBlendMode(var4 == null ? null : Blend.impl_getToolkitMode(var4));
      }

   }

   public final ObservableMap getProperties() {
      if (this.properties == null) {
         this.properties = FXCollections.observableMap(new HashMap());
      }

      return this.properties;
   }

   public boolean hasProperties() {
      return this.properties != null && !this.properties.isEmpty();
   }

   public void setUserData(Object var1) {
      this.getProperties().put(USER_DATA_KEY, var1);
   }

   public Object getUserData() {
      return this.getProperties().get(USER_DATA_KEY);
   }

   final void setParent(Parent var1) {
      this.parentPropertyImpl().set(var1);
   }

   public final Parent getParent() {
      return this.parent == null ? null : (Parent)this.parent.get();
   }

   public final ReadOnlyObjectProperty parentProperty() {
      return this.parentPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper parentPropertyImpl() {
      if (this.parent == null) {
         this.parent = new ReadOnlyObjectWrapper() {
            private Parent oldParent;

            protected void invalidated() {
               if (this.oldParent != null) {
                  this.oldParent.disabledProperty().removeListener(Node.this.parentDisabledChangedListener);
                  this.oldParent.impl_treeVisibleProperty().removeListener(Node.this.parentTreeVisibleChangedListener);
                  if (Node.this.nodeTransformation != null && Node.this.nodeTransformation.listenerReasons > 0) {
                     this.oldParent.localToSceneTransformProperty().removeListener(Node.this.nodeTransformation.getLocalToSceneInvalidationListener());
                  }
               }

               Node.this.updateDisabled();
               Node.this.computeDerivedDepthTest();
               Parent var1 = (Parent)this.get();
               if (var1 != null) {
                  var1.disabledProperty().addListener(Node.this.parentDisabledChangedListener);
                  var1.impl_treeVisibleProperty().addListener(Node.this.parentTreeVisibleChangedListener);
                  if (Node.this.nodeTransformation != null && Node.this.nodeTransformation.listenerReasons > 0) {
                     var1.localToSceneTransformProperty().addListener(Node.this.nodeTransformation.getLocalToSceneInvalidationListener());
                  }

                  Node.this.impl_reapplyCSS();
               } else {
                  Node.this.cssFlag = CssFlags.CLEAN;
               }

               Node.this.updateTreeVisible(true);
               this.oldParent = var1;
               Node.this.invalidateLocalToSceneTransform();
               Node.this.parentResolvedOrientationInvalidated();
               Node.this.notifyAccessibleAttributeChanged(AccessibleAttribute.PARENT);
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "parent";
            }
         };
      }

      return this.parent;
   }

   private void invalidatedScenes(Scene var1, SubScene var2) {
      Scene var3 = (Scene)this.sceneProperty().get();
      boolean var4 = var1 != var3;
      SubScene var5 = this.subScene;
      if (this.getClip() != null) {
         this.getClip().setScenes(var3, var5);
      }

      if (var4) {
         this.updateCanReceiveFocus();
         if (this.isFocusTraversable() && var3 != null) {
            var3.initializeInternalEventDispatcher();
         }

         this.focusSetDirty(var1);
         this.focusSetDirty(var3);
      }

      this.scenesChanged(var3, var5, var1, var2);
      if (var4) {
         this.impl_reapplyCSS();
      }

      if (var4 && !this.impl_isDirtyEmpty()) {
         this.addToSceneDirtyList();
      }

      if (var3 == null && this.peer != null) {
         this.peer.release();
      }

      if (var1 != null) {
         var1.clearNodeMnemonics(this);
      }

      if (this.getParent() == null) {
         this.parentResolvedOrientationInvalidated();
      }

      if (var4) {
         this.scene.fireSuperValueChangedEvent();
      }

      if (this.accessible != null) {
         if (var1 != null && var1 != var3 && var3 == null) {
            var1.addAccessible(this, this.accessible);
         } else {
            this.accessible.dispose();
         }

         this.accessible = null;
      }

   }

   final void setScenes(Scene var1, SubScene var2) {
      Scene var3 = (Scene)this.sceneProperty().get();
      if (var1 != var3 || var2 != this.subScene) {
         this.scene.set(var1);
         SubScene var4 = this.subScene;
         this.subScene = var2;
         this.invalidatedScenes(var3, var4);
         if (this instanceof SubScene) {
            SubScene var5 = (SubScene)this;
            var5.getRoot().setScenes(var1, var5);
         }
      }

   }

   final SubScene getSubScene() {
      return this.subScene;
   }

   public final Scene getScene() {
      return (Scene)this.scene.get();
   }

   public final ReadOnlyObjectProperty sceneProperty() {
      return this.scene.getReadOnlyProperty();
   }

   void scenesChanged(Scene var1, SubScene var2, Scene var3, SubScene var4) {
   }

   public final void setId(String var1) {
      this.idProperty().set(var1);
   }

   public final String getId() {
      return this.id == null ? null : (String)this.id.get();
   }

   public final StringProperty idProperty() {
      if (this.id == null) {
         this.id = new StringPropertyBase() {
            protected void invalidated() {
               Node.this.impl_reapplyCSS();
               if (PrismSettings.printRenderGraph) {
                  Node.this.impl_markDirty(DirtyBits.DEBUG);
               }

            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "id";
            }
         };
      }

      return this.id;
   }

   public final ObservableList getStyleClass() {
      return this.styleClass;
   }

   public final void setStyle(String var1) {
      this.styleProperty().set(var1);
   }

   public final String getStyle() {
      return this.style == null ? "" : (String)this.style.get();
   }

   public final StringProperty styleProperty() {
      if (this.style == null) {
         this.style = new StringPropertyBase("") {
            public void set(String var1) {
               super.set(var1 != null ? var1 : "");
            }

            protected void invalidated() {
               Node.this.impl_reapplyCSS();
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "style";
            }
         };
      }

      return this.style;
   }

   public final void setVisible(boolean var1) {
      this.visibleProperty().set(var1);
   }

   public final boolean isVisible() {
      return this.visible == null ? true : this.visible.get();
   }

   public final BooleanProperty visibleProperty() {
      if (this.visible == null) {
         this.visible = new StyleableBooleanProperty(true) {
            boolean oldValue = true;

            protected void invalidated() {
               if (this.oldValue != this.get()) {
                  Node.this.impl_markDirty(DirtyBits.NODE_VISIBLE);
                  Node.this.impl_geomChanged();
                  Node.this.updateTreeVisible(false);
                  if (Node.this.getParent() != null) {
                     Node.this.getParent().childVisibilityChanged(Node.this);
                  }

                  this.oldValue = this.get();
               }

            }

            public CssMetaData getCssMetaData() {
               return Node.StyleableProperties.VISIBILITY;
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "visible";
            }
         };
      }

      return this.visible;
   }

   public final void setCursor(Cursor var1) {
      this.cursorProperty().set(var1);
   }

   public final Cursor getCursor() {
      return this.miscProperties == null ? DEFAULT_CURSOR : this.miscProperties.getCursor();
   }

   public final ObjectProperty cursorProperty() {
      return this.getMiscProperties().cursorProperty();
   }

   public final void setOpacity(double var1) {
      this.opacityProperty().set(var1);
   }

   public final double getOpacity() {
      return this.opacity == null ? 1.0 : this.opacity.get();
   }

   public final DoubleProperty opacityProperty() {
      if (this.opacity == null) {
         this.opacity = new StyleableDoubleProperty(1.0) {
            public void invalidated() {
               Node.this.impl_markDirty(DirtyBits.NODE_OPACITY);
            }

            public CssMetaData getCssMetaData() {
               return Node.StyleableProperties.OPACITY;
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "opacity";
            }
         };
      }

      return this.opacity;
   }

   public final void setBlendMode(BlendMode var1) {
      this.blendModeProperty().set(var1);
   }

   public final BlendMode getBlendMode() {
      return this.blendMode == null ? null : (BlendMode)this.blendMode.get();
   }

   public final ObjectProperty blendModeProperty() {
      if (this.blendMode == null) {
         this.blendMode = new StyleableObjectProperty((BlendMode)null) {
            public void invalidated() {
               Node.this.impl_markDirty(DirtyBits.NODE_BLENDMODE);
            }

            public CssMetaData getCssMetaData() {
               return Node.StyleableProperties.BLEND_MODE;
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "blendMode";
            }
         };
      }

      return this.blendMode;
   }

   public final void setClip(Node var1) {
      this.clipProperty().set(var1);
   }

   public final Node getClip() {
      return this.miscProperties == null ? DEFAULT_CLIP : this.miscProperties.getClip();
   }

   public final ObjectProperty clipProperty() {
      return this.getMiscProperties().clipProperty();
   }

   public final void setCache(boolean var1) {
      this.cacheProperty().set(var1);
   }

   public final boolean isCache() {
      return this.miscProperties == null ? false : this.miscProperties.isCache();
   }

   public final BooleanProperty cacheProperty() {
      return this.getMiscProperties().cacheProperty();
   }

   public final void setCacheHint(CacheHint var1) {
      this.cacheHintProperty().set(var1);
   }

   public final CacheHint getCacheHint() {
      return this.miscProperties == null ? DEFAULT_CACHE_HINT : this.miscProperties.getCacheHint();
   }

   public final ObjectProperty cacheHintProperty() {
      return this.getMiscProperties().cacheHintProperty();
   }

   public final void setEffect(Effect var1) {
      this.effectProperty().set(var1);
   }

   public final Effect getEffect() {
      return this.miscProperties == null ? DEFAULT_EFFECT : this.miscProperties.getEffect();
   }

   public final ObjectProperty effectProperty() {
      return this.getMiscProperties().effectProperty();
   }

   public final void setDepthTest(DepthTest var1) {
      this.depthTestProperty().set(var1);
   }

   public final DepthTest getDepthTest() {
      return this.miscProperties == null ? DEFAULT_DEPTH_TEST : this.miscProperties.getDepthTest();
   }

   public final ObjectProperty depthTestProperty() {
      return this.getMiscProperties().depthTestProperty();
   }

   void computeDerivedDepthTest() {
      boolean var1;
      if (this.getDepthTest() == DepthTest.INHERIT) {
         if (this.getParent() != null) {
            var1 = this.getParent().isDerivedDepthTest();
         } else {
            var1 = true;
         }
      } else if (this.getDepthTest() == DepthTest.ENABLE) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (this.isDerivedDepthTest() != var1) {
         this.impl_markDirty(DirtyBits.NODE_DEPTH_TEST);
         this.setDerivedDepthTest(var1);
      }

   }

   void setDerivedDepthTest(boolean var1) {
      this.derivedDepthTest = var1;
   }

   boolean isDerivedDepthTest() {
      return this.derivedDepthTest;
   }

   public final void setDisable(boolean var1) {
      this.disableProperty().set(var1);
   }

   public final boolean isDisable() {
      return this.miscProperties == null ? false : this.miscProperties.isDisable();
   }

   public final BooleanProperty disableProperty() {
      return this.getMiscProperties().disableProperty();
   }

   public final void setPickOnBounds(boolean var1) {
      this.pickOnBoundsProperty().set(var1);
   }

   public final boolean isPickOnBounds() {
      return this.pickOnBounds == null ? false : this.pickOnBounds.get();
   }

   public final BooleanProperty pickOnBoundsProperty() {
      if (this.pickOnBounds == null) {
         this.pickOnBounds = new SimpleBooleanProperty(this, "pickOnBounds");
      }

      return this.pickOnBounds;
   }

   protected final void setDisabled(boolean var1) {
      this.disabledPropertyImpl().set(var1);
   }

   public final boolean isDisabled() {
      return this.disabled == null ? false : this.disabled.get();
   }

   public final ReadOnlyBooleanProperty disabledProperty() {
      return this.disabledPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper disabledPropertyImpl() {
      if (this.disabled == null) {
         this.disabled = new ReadOnlyBooleanWrapper() {
            protected void invalidated() {
               Node.this.pseudoClassStateChanged(Node.DISABLED_PSEUDOCLASS_STATE, this.get());
               Node.this.updateCanReceiveFocus();
               Node.this.focusSetDirty(Node.this.getScene());
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "disabled";
            }
         };
      }

      return this.disabled;
   }

   private void updateDisabled() {
      boolean var1 = this.isDisable();
      if (!var1) {
         var1 = this.getParent() != null ? this.getParent().isDisabled() : this.getSubScene() != null && this.getSubScene().isDisabled();
      }

      this.setDisabled(var1);
      if (this instanceof SubScene) {
         ((SubScene)this).getRoot().setDisabled(var1);
      }

   }

   public Node lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         Selector var2 = Selector.createSelector(var1);
         return var2 != null && var2.applies(this) ? this : null;
      }
   }

   public Set lookupAll(String var1) {
      Selector var2 = Selector.createSelector(var1);
      Set var3 = Collections.emptySet();
      if (var2 == null) {
         return var3;
      } else {
         List var4 = this.lookupAll(var2, (List)null);
         return (Set)(var4 == null ? var3 : new UnmodifiableListSet(var4));
      }
   }

   List lookupAll(Selector var1, List var2) {
      if (var1.applies(this)) {
         if (var2 == null) {
            var2 = new LinkedList();
         }

         ((List)var2).add(this);
      }

      return (List)var2;
   }

   public void toBack() {
      if (this.getParent() != null) {
         this.getParent().impl_toBack(this);
      }

   }

   public void toFront() {
      if (this.getParent() != null) {
         this.getParent().impl_toFront(this);
      }

   }

   private void doCSSPass() {
      if (this.cssFlag != CssFlags.CLEAN) {
         this.processCSS();
      }

   }

   private static void syncAll(Node var0) {
      var0.impl_syncPeer();
      if (var0 instanceof Parent) {
         Parent var1 = (Parent)var0;
         int var2 = var1.getChildren().size();

         for(int var3 = 0; var3 < var2; ++var3) {
            Node var4 = (Node)var1.getChildren().get(var3);
            if (var4 != null) {
               syncAll(var4);
            }
         }
      }

      if (var0.getClip() != null) {
         syncAll(var0.getClip());
      }

   }

   private void doLayoutPass() {
      if (this instanceof Parent) {
         Parent var1 = (Parent)this;

         for(int var2 = 0; var2 < 3; ++var2) {
            var1.layout();
         }
      }

   }

   private void doCSSLayoutSyncForSnapshot() {
      this.doCSSPass();
      this.doLayoutPass();
      this.updateBounds();
      Scene.impl_setAllowPGAccess(true);
      syncAll(this);
      Scene.impl_setAllowPGAccess(false);
   }

   private WritableImage doSnapshot(SnapshotParameters var1, WritableImage var2) {
      if (this.getScene() != null) {
         this.getScene().doCSSLayoutSyncForSnapshot(this);
      } else {
         this.doCSSLayoutSyncForSnapshot();
      }

      Object var3 = BaseTransform.IDENTITY_TRANSFORM;
      if (var1.getTransform() != null) {
         Affine3D var4 = new Affine3D();
         var1.getTransform().impl_apply(var4);
         var3 = var4;
      }

      Rectangle2D var12 = var1.getViewport();
      double var6;
      double var8;
      double var10;
      double var15;
      if (var12 != null) {
         var15 = var12.getMinX();
         var6 = var12.getMinY();
         var8 = var12.getWidth();
         var10 = var12.getHeight();
      } else {
         BaseBounds var13 = TempState.getInstance().bounds;
         var13 = this.getTransformedBounds(var13, (BaseTransform)var3);
         var15 = (double)var13.getMinX();
         var6 = (double)var13.getMinY();
         var8 = (double)var13.getWidth();
         var10 = (double)var13.getHeight();
      }

      WritableImage var14 = Scene.doSnapshot(this.getScene(), var15, var6, var8, var10, this, (BaseTransform)var3, var1.isDepthBufferInternal(), var1.getFill(), var1.getEffectiveCamera(), var2);
      return var14;
   }

   public WritableImage snapshot(SnapshotParameters var1, WritableImage var2) {
      Toolkit.getToolkit().checkFxUserThread();
      if (var1 == null) {
         var1 = new SnapshotParameters();
         Scene var3 = this.getScene();
         if (var3 != null) {
            var1.setCamera(var3.getEffectiveCamera());
            var1.setDepthBuffer(var3.isDepthBufferInternal());
            var1.setFill(var3.getFill());
         }
      }

      return this.doSnapshot(var1, var2);
   }

   public void snapshot(Callback var1, SnapshotParameters var2, WritableImage var3) {
      Toolkit.getToolkit().checkFxUserThread();
      if (var1 == null) {
         throw new NullPointerException("The callback must not be null");
      } else {
         if (var2 == null) {
            var2 = new SnapshotParameters();
            Scene var4 = this.getScene();
            if (var4 != null) {
               var2.setCamera(var4.getEffectiveCamera());
               var2.setDepthBuffer(var4.isDepthBufferInternal());
               var2.setFill(var4.getFill());
            }
         } else {
            var2 = var2.copy();
         }

         Runnable var7 = () -> {
            WritableImage var4 = this.doSnapshot(var2, var3);
            SnapshotResult var5 = new SnapshotResult(var4, this, var2);

            try {
               Void var6 = (Void)var1.call(var5);
            } catch (Throwable var7) {
               System.err.println("Exception in snapshot callback");
               var7.printStackTrace(System.err);
            }

         };
         Scene.addSnapshotRunnable(var7);
      }
   }

   public final void setOnDragEntered(EventHandler var1) {
      this.onDragEnteredProperty().set(var1);
   }

   public final EventHandler getOnDragEntered() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnDragEntered();
   }

   public final ObjectProperty onDragEnteredProperty() {
      return this.getEventHandlerProperties().onDragEnteredProperty();
   }

   public final void setOnDragExited(EventHandler var1) {
      this.onDragExitedProperty().set(var1);
   }

   public final EventHandler getOnDragExited() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnDragExited();
   }

   public final ObjectProperty onDragExitedProperty() {
      return this.getEventHandlerProperties().onDragExitedProperty();
   }

   public final void setOnDragOver(EventHandler var1) {
      this.onDragOverProperty().set(var1);
   }

   public final EventHandler getOnDragOver() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnDragOver();
   }

   public final ObjectProperty onDragOverProperty() {
      return this.getEventHandlerProperties().onDragOverProperty();
   }

   public final void setOnDragDropped(EventHandler var1) {
      this.onDragDroppedProperty().set(var1);
   }

   public final EventHandler getOnDragDropped() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnDragDropped();
   }

   public final ObjectProperty onDragDroppedProperty() {
      return this.getEventHandlerProperties().onDragDroppedProperty();
   }

   public final void setOnDragDone(EventHandler var1) {
      this.onDragDoneProperty().set(var1);
   }

   public final EventHandler getOnDragDone() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnDragDone();
   }

   public final ObjectProperty onDragDoneProperty() {
      return this.getEventHandlerProperties().onDragDoneProperty();
   }

   public Dragboard startDragAndDrop(TransferMode... var1) {
      if (this.getScene() != null) {
         return this.getScene().startDragAndDrop(this, var1);
      } else {
         throw new IllegalStateException("Cannot start drag and drop on node that is not in scene");
      }
   }

   public void startFullDrag() {
      if (this.getScene() != null) {
         this.getScene().startFullDrag(this);
      } else {
         throw new IllegalStateException("Cannot start full drag on node that is not in scene");
      }
   }

   final Node getClipParent() {
      return this.clipParent;
   }

   boolean isConnected() {
      return this.getParent() != null || this.clipParent != null;
   }

   boolean wouldCreateCycle(Node var1, Node var2) {
      if (var2 != null && var2.getClip() == null && !(var2 instanceof Parent)) {
         return false;
      } else {
         Object var3 = var1;

         while(var3 != var2) {
            if (((Node)var3).getParent() != null) {
               var3 = ((Node)var3).getParent();
            } else if (((Node)var3).getSubScene() != null) {
               var3 = ((Node)var3).getSubScene();
            } else {
               if (((Node)var3).clipParent == null) {
                  return false;
               }

               var3 = ((Node)var3).clipParent;
            }
         }

         return true;
      }
   }

   /** @deprecated */
   @Deprecated
   public NGNode impl_getPeer() {
      if (Utils.assertionEnabled() && this.getScene() != null && !Scene.isPGAccessAllowed()) {
         System.err.println();
         System.err.println("*** unexpected PG access");
         Thread.dumpStack();
      }

      if (this.peer == null) {
         this.peer = this.impl_createPeer();
      }

      return this.peer;
   }

   /** @deprecated */
   @Deprecated
   protected abstract NGNode impl_createPeer();

   protected Node() {
      this.localToParentTx = BaseTransform.IDENTITY_TRANSFORM;
      this.transformDirty = true;
      this.txBounds = new RectBounds();
      this.geomBounds = new RectBounds();
      this.localBounds = null;
      this.geomBoundsInvalid = true;
      this.localBoundsInvalid = true;
      this.txBoundsInvalid = true;
      this.resolvedNodeOrientation = 0;
      this.canReceiveFocus = false;
      this.labeledBy = null;
      this.cssFlag = CssFlags.CLEAN;
      this.pseudoClassStates = new PseudoClassState();
      this.setDirty();
      this.updateTreeVisible(false);
   }

   public final void setManaged(boolean var1) {
      this.managedProperty().set(var1);
   }

   public final boolean isManaged() {
      return this.managed == null ? true : this.managed.get();
   }

   public final BooleanProperty managedProperty() {
      if (this.managed == null) {
         this.managed = new BooleanPropertyBase(true) {
            protected void invalidated() {
               Parent var1 = Node.this.getParent();
               if (var1 != null) {
                  var1.managedChildChanged();
               }

               Node.this.notifyManagedChanged();
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "managed";
            }
         };
      }

      return this.managed;
   }

   void notifyManagedChanged() {
   }

   public final void setLayoutX(double var1) {
      this.layoutXProperty().set(var1);
   }

   public final double getLayoutX() {
      return this.layoutX == null ? 0.0 : this.layoutX.get();
   }

   public final DoubleProperty layoutXProperty() {
      if (this.layoutX == null) {
         this.layoutX = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               Node.this.impl_transformsChanged();
               Parent var1 = Node.this.getParent();
               if (var1 != null && !var1.performingLayout) {
                  if (Node.this.isManaged()) {
                     var1.requestLayout();
                  } else {
                     var1.clearSizeCache();
                     var1.requestParentLayout();
                  }
               }

            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "layoutX";
            }
         };
      }

      return this.layoutX;
   }

   public final void setLayoutY(double var1) {
      this.layoutYProperty().set(var1);
   }

   public final double getLayoutY() {
      return this.layoutY == null ? 0.0 : this.layoutY.get();
   }

   public final DoubleProperty layoutYProperty() {
      if (this.layoutY == null) {
         this.layoutY = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               Node.this.impl_transformsChanged();
               Parent var1 = Node.this.getParent();
               if (var1 != null && !var1.performingLayout) {
                  if (Node.this.isManaged()) {
                     var1.requestLayout();
                  } else {
                     var1.clearSizeCache();
                     var1.requestParentLayout();
                  }
               }

            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "layoutY";
            }
         };
      }

      return this.layoutY;
   }

   public void relocate(double var1, double var3) {
      this.setLayoutX(var1 - this.getLayoutBounds().getMinX());
      this.setLayoutY(var3 - this.getLayoutBounds().getMinY());
      PlatformLogger var5 = Logging.getLayoutLogger();
      if (var5.isLoggable(Level.FINER)) {
         var5.finer(this.toString() + " moved to (" + var1 + "," + var3 + ")");
      }

   }

   public boolean isResizable() {
      return false;
   }

   public Orientation getContentBias() {
      return null;
   }

   public double minWidth(double var1) {
      return this.prefWidth(var1);
   }

   public double minHeight(double var1) {
      return this.prefHeight(var1);
   }

   public double prefWidth(double var1) {
      double var3 = this.getLayoutBounds().getWidth();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public double prefHeight(double var1) {
      double var3 = this.getLayoutBounds().getHeight();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public double maxWidth(double var1) {
      return this.prefWidth(var1);
   }

   public double maxHeight(double var1) {
      return this.prefHeight(var1);
   }

   public void resize(double var1, double var3) {
   }

   public final void autosize() {
      if (this.isResizable()) {
         Orientation var1 = this.getContentBias();
         double var2;
         double var4;
         if (var1 == null) {
            var2 = this.boundedSize(this.prefWidth(-1.0), this.minWidth(-1.0), this.maxWidth(-1.0));
            var4 = this.boundedSize(this.prefHeight(-1.0), this.minHeight(-1.0), this.maxHeight(-1.0));
         } else if (var1 == Orientation.HORIZONTAL) {
            var2 = this.boundedSize(this.prefWidth(-1.0), this.minWidth(-1.0), this.maxWidth(-1.0));
            var4 = this.boundedSize(this.prefHeight(var2), this.minHeight(var2), this.maxHeight(var2));
         } else {
            var4 = this.boundedSize(this.prefHeight(-1.0), this.minHeight(-1.0), this.maxHeight(-1.0));
            var2 = this.boundedSize(this.prefWidth(var4), this.minWidth(var4), this.maxWidth(var4));
         }

         this.resize(var2, var4);
      }

   }

   double boundedSize(double var1, double var3, double var5) {
      return Math.min(Math.max(var1, var3), Math.max(var3, var5));
   }

   public void resizeRelocate(double var1, double var3, double var5, double var7) {
      this.resize(var5, var7);
      this.relocate(var1, var3);
   }

   public double getBaselineOffset() {
      return this.isResizable() ? Double.NEGATIVE_INFINITY : this.getLayoutBounds().getHeight();
   }

   public double computeAreaInScreen() {
      return this.impl_computeAreaInScreen();
   }

   private double impl_computeAreaInScreen() {
      Scene var1 = this.getScene();
      if (var1 != null) {
         Bounds var2 = this.getBoundsInLocal();
         Camera var3 = var1.getEffectiveCamera();
         boolean var4 = var3 instanceof PerspectiveCamera;
         Transform var5 = this.getLocalToSceneTransform();
         Affine3D var6 = TempState.getInstance().tempTx;
         BoxBounds var7 = new BoxBounds((float)var2.getMinX(), (float)var2.getMinY(), (float)var2.getMinZ(), (float)var2.getMaxX(), (float)var2.getMaxY(), (float)var2.getMaxZ());
         if (var4) {
            Transform var8 = var3.getLocalToSceneTransform();
            if (var8.getMxx() == 1.0 && var8.getMxy() == 0.0 && var8.getMxz() == 0.0 && var8.getMyx() == 0.0 && var8.getMyy() == 1.0 && var8.getMyz() == 0.0 && var8.getMzx() == 0.0 && var8.getMzy() == 0.0 && var8.getMzz() == 1.0) {
               double var11;
               double var16;
               if (var5.getMxx() == 1.0 && var5.getMxy() == 0.0 && var5.getMxz() == 0.0 && var5.getMyx() == 0.0 && var5.getMyy() == 1.0 && var5.getMyz() == 0.0 && var5.getMzx() == 0.0 && var5.getMzy() == 0.0 && var5.getMzz() == 1.0) {
                  Vec3d var18 = TempState.getInstance().vec3d;
                  var18.set(0.0, 0.0, var2.getMinZ());
                  this.localToScene(var18);
                  var16 = var18.z;
                  var18.set(0.0, 0.0, var2.getMaxZ());
                  this.localToScene(var18);
                  var11 = var18.z;
               } else {
                  Bounds var13 = this.localToScene(var2);
                  var16 = var13.getMinZ();
                  var11 = var13.getMaxZ();
               }

               if (var16 > var3.getFarClipInScene() || var11 < var3.getNearClipInScene()) {
                  return 0.0;
               }
            } else {
               BoxBounds var9 = new BoxBounds();
               var6.setToIdentity();
               var5.impl_apply(var6);
               var6.preConcatenate(var3.getSceneToLocalTransform());
               var6.transform((BaseBounds)var7, (BaseBounds)var9);
               if ((double)var9.getMinZ() > var3.getFarClip() || (double)var9.getMaxZ() < var3.getNearClip()) {
                  return 0.0;
               }
            }
         }

         GeneralTransform3D var15 = TempState.getInstance().projViewTx;
         var15.set(var3.getProjViewTransform());
         var6.setToIdentity();
         var5.impl_apply(var6);
         GeneralTransform3D var17 = var15.mul((BaseTransform)var6);
         BaseBounds var14 = var17.transform((BaseBounds)var7, (BaseBounds)var7);
         double var10 = (double)(var14.getWidth() * var14.getHeight());
         if (var4) {
            var14.intersectWith(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            var10 = !(var14.getWidth() < 0.0F) && !(var14.getHeight() < 0.0F) ? var10 : 0.0;
         }

         return var10 * (var3.getViewWidth() / 2.0 * var3.getViewHeight() / 2.0);
      } else {
         return 0.0;
      }
   }

   public final Bounds getBoundsInParent() {
      return (Bounds)this.boundsInParentProperty().get();
   }

   public final ReadOnlyObjectProperty boundsInParentProperty() {
      return this.getMiscProperties().boundsInParentProperty();
   }

   private void invalidateBoundsInParent() {
      if (this.miscProperties != null) {
         this.miscProperties.invalidateBoundsInParent();
      }

   }

   public final Bounds getBoundsInLocal() {
      return (Bounds)this.boundsInLocalProperty().get();
   }

   public final ReadOnlyObjectProperty boundsInLocalProperty() {
      return this.getMiscProperties().boundsInLocalProperty();
   }

   private void invalidateBoundsInLocal() {
      if (this.miscProperties != null) {
         this.miscProperties.invalidateBoundsInLocal();
      }

   }

   public final Bounds getLayoutBounds() {
      return (Bounds)this.layoutBoundsProperty().get();
   }

   public final ReadOnlyObjectProperty layoutBoundsProperty() {
      return this.layoutBounds;
   }

   /** @deprecated */
   @Deprecated
   protected Bounds impl_computeLayoutBounds() {
      BaseBounds var1 = TempState.getInstance().bounds;
      var1 = this.getGeomBounds(var1, BaseTransform.IDENTITY_TRANSFORM);
      return new BoundingBox((double)var1.getMinX(), (double)var1.getMinY(), (double)var1.getMinZ(), (double)var1.getWidth(), (double)var1.getHeight(), (double)var1.getDepth());
   }

   /** @deprecated */
   @Deprecated
   protected final void impl_layoutBoundsChanged() {
      if (this.layoutBounds.valid) {
         this.layoutBounds.invalidate();
         if (this.nodeTransformation != null && this.nodeTransformation.hasScaleOrRotate() || this.hasMirroring()) {
            this.impl_transformsChanged();
         }

      }
   }

   BaseBounds getTransformedBounds(BaseBounds var1, BaseTransform var2) {
      this.updateLocalToParentTransform();
      double var3;
      double var5;
      double var7;
      if (var2.isTranslateOrIdentity()) {
         this.updateTxBounds();
         var1 = var1.deriveWithNewBounds(this.txBounds);
         if (!var2.isIdentity()) {
            var3 = var2.getMxt();
            var5 = var2.getMyt();
            var7 = var2.getMzt();
            var1 = var1.deriveWithNewBounds((float)((double)var1.getMinX() + var3), (float)((double)var1.getMinY() + var5), (float)((double)var1.getMinZ() + var7), (float)((double)var1.getMaxX() + var3), (float)((double)var1.getMaxY() + var5), (float)((double)var1.getMaxZ() + var7));
         }

         return var1;
      } else if (this.localToParentTx.isIdentity()) {
         return this.getLocalBounds(var1, var2);
      } else {
         var3 = var2.getMxx();
         var5 = var2.getMxy();
         var7 = var2.getMxz();
         double var9 = var2.getMxt();
         double var11 = var2.getMyx();
         double var13 = var2.getMyy();
         double var15 = var2.getMyz();
         double var17 = var2.getMyt();
         double var19 = var2.getMzx();
         double var21 = var2.getMzy();
         double var23 = var2.getMzz();
         double var25 = var2.getMzt();
         BaseTransform var27 = var2.deriveWithConcatenation(this.localToParentTx);
         var1 = this.getLocalBounds(var1, var27);
         if (var27 == var2) {
            var2.restoreTransform(var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25);
         }

         return var1;
      }
   }

   BaseBounds getLocalBounds(BaseBounds var1, BaseTransform var2) {
      if (this.getEffect() == null && this.getClip() == null) {
         return this.getGeomBounds(var1, var2);
      } else if (var2.isTranslateOrIdentity()) {
         this.updateLocalBounds();
         var1 = var1.deriveWithNewBounds(this.localBounds);
         if (!var2.isIdentity()) {
            double var3 = var2.getMxt();
            double var5 = var2.getMyt();
            double var7 = var2.getMzt();
            var1 = var1.deriveWithNewBounds((float)((double)var1.getMinX() + var3), (float)((double)var1.getMinY() + var5), (float)((double)var1.getMinZ() + var7), (float)((double)var1.getMaxX() + var3), (float)((double)var1.getMaxY() + var5), (float)((double)var1.getMaxZ() + var7));
         }

         return var1;
      } else if (var2.is2D() && (var2.getType() & -76) != 0) {
         return this.computeLocalBounds(var1, var2);
      } else {
         this.updateLocalBounds();
         return var2.transform(this.localBounds, var1);
      }
   }

   BaseBounds getGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (var2.isTranslateOrIdentity()) {
         this.updateGeomBounds();
         var1 = var1.deriveWithNewBounds(this.geomBounds);
         if (!var2.isIdentity()) {
            double var3 = var2.getMxt();
            double var5 = var2.getMyt();
            double var7 = var2.getMzt();
            var1 = var1.deriveWithNewBounds((float)((double)var1.getMinX() + var3), (float)((double)var1.getMinY() + var5), (float)((double)var1.getMinZ() + var7), (float)((double)var1.getMaxX() + var3), (float)((double)var1.getMaxY() + var5), (float)((double)var1.getMaxZ() + var7));
         }

         return var1;
      } else if (var2.is2D() && (var2.getType() & -76) != 0) {
         return this.impl_computeGeomBounds(var1, var2);
      } else {
         this.updateGeomBounds();
         return var2.transform(this.geomBounds, var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public abstract BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2);

   void updateGeomBounds() {
      if (this.geomBoundsInvalid) {
         this.geomBounds = this.impl_computeGeomBounds(this.geomBounds, BaseTransform.IDENTITY_TRANSFORM);
         this.geomBoundsInvalid = false;
      }

   }

   private BaseBounds computeLocalBounds(BaseBounds var1, BaseTransform var2) {
      if (this.getEffect() != null) {
         BaseBounds var3 = this.getEffect().impl_getBounds(var1, var2, this, boundsAccessor);
         var1 = var1.deriveWithNewBounds(var3);
      } else {
         var1 = this.getGeomBounds(var1, var2);
      }

      if (this.getClip() != null && !(this instanceof Shape3D) && !(this.getClip() instanceof Shape3D)) {
         double var15 = (double)var1.getMinX();
         double var5 = (double)var1.getMinY();
         double var7 = (double)var1.getMaxX();
         double var9 = (double)var1.getMaxY();
         double var11 = (double)var1.getMinZ();
         double var13 = (double)var1.getMaxZ();
         var1 = this.getClip().getTransformedBounds(var1, var2);
         var1.intersectWith((float)var15, (float)var5, (float)var11, (float)var7, (float)var9, (float)var13);
      }

      return var1;
   }

   private void updateLocalBounds() {
      if (this.localBoundsInvalid) {
         if (this.getClip() == null && this.getEffect() == null) {
            this.localBounds = null;
         } else {
            this.localBounds = this.computeLocalBounds((BaseBounds)(this.localBounds == null ? new RectBounds() : this.localBounds), BaseTransform.IDENTITY_TRANSFORM);
         }

         this.localBoundsInvalid = false;
      }

   }

   void updateTxBounds() {
      if (this.txBoundsInvalid) {
         this.updateLocalToParentTransform();
         this.txBounds = this.getLocalBounds(this.txBounds, this.localToParentTx);
         this.txBoundsInvalid = false;
      }

   }

   /** @deprecated */
   @Deprecated
   protected abstract boolean impl_computeContains(double var1, double var3);

   /** @deprecated */
   @Deprecated
   protected void impl_geomChanged() {
      if (this.geomBoundsInvalid) {
         this.impl_notifyLayoutBoundsChanged();
         this.transformedBoundsChanged();
      } else {
         this.geomBounds.makeEmpty();
         this.geomBoundsInvalid = true;
         this.impl_markDirty(DirtyBits.NODE_BOUNDS);
         this.impl_notifyLayoutBoundsChanged();
         this.localBoundsChanged();
      }
   }

   void localBoundsChanged() {
      this.localBoundsInvalid = true;
      this.invalidateBoundsInLocal();
      this.transformedBoundsChanged();
   }

   void transformedBoundsChanged() {
      if (!this.txBoundsInvalid) {
         this.txBounds.makeEmpty();
         this.txBoundsInvalid = true;
         this.invalidateBoundsInParent();
         this.impl_markDirty(DirtyBits.NODE_TRANSFORMED_BOUNDS);
      }

      if (this.isVisible()) {
         this.notifyParentOfBoundsChange();
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_notifyLayoutBoundsChanged() {
      this.impl_layoutBoundsChanged();
      Parent var1 = this.getParent();
      if (this.isManaged() && var1 != null && (!(var1 instanceof Group) || this.isResizable()) && !var1.performingLayout) {
         var1.requestLayout();
      }

   }

   void notifyParentOfBoundsChange() {
      Parent var1 = this.getParent();
      if (var1 != null) {
         var1.childBoundsChanged(this);
      }

      if (this.clipParent != null) {
         this.clipParent.localBoundsChanged();
      }

   }

   public boolean contains(double var1, double var3) {
      if (!this.containsBounds(var1, var3)) {
         return false;
      } else {
         return this.isPickOnBounds() || this.impl_computeContains(var1, var3);
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean containsBounds(double var1, double var3) {
      TempState var5 = TempState.getInstance();
      BaseBounds var6 = var5.bounds;
      var6 = this.getLocalBounds(var6, BaseTransform.IDENTITY_TRANSFORM);
      if (var6.contains((float)var1, (float)var3)) {
         if (this.getClip() != null) {
            var5.point.x = (float)var1;
            var5.point.y = (float)var3;

            try {
               this.getClip().parentToLocal(var5.point);
            } catch (NoninvertibleTransformException var8) {
               return false;
            }

            if (!this.getClip().contains((double)var5.point.x, (double)var5.point.y)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean contains(Point2D var1) {
      return this.contains(var1.getX(), var1.getY());
   }

   public boolean intersects(double var1, double var3, double var5, double var7) {
      BaseBounds var9 = TempState.getInstance().bounds;
      var9 = this.getLocalBounds(var9, BaseTransform.IDENTITY_TRANSFORM);
      return var9.intersects((float)var1, (float)var3, (float)var5, (float)var7);
   }

   public boolean intersects(Bounds var1) {
      return this.intersects(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight());
   }

   public Point2D screenToLocal(double var1, double var3) {
      Scene var5 = this.getScene();
      if (var5 == null) {
         return null;
      } else {
         Window var6 = var5.getWindow();
         if (var6 == null) {
            return null;
         } else {
            com.sun.javafx.geom.Point2D var7 = TempState.getInstance().point;
            var7.setLocation((float)(var1 - var5.getX() - var6.getX()), (float)(var3 - var5.getY() - var6.getY()));
            SubScene var8 = this.getSubScene();
            if (var8 != null) {
               Point2D var9 = SceneUtils.sceneToSubScenePlane(var8, new Point2D((double)var7.x, (double)var7.y));
               if (var9 == null) {
                  return null;
               }

               var7.setLocation((float)var9.getX(), (float)var9.getY());
            }

            Point3D var12 = var5.getEffectiveCamera().pickProjectPlane((double)var7.x, (double)var7.y);
            var7.setLocation((float)var12.getX(), (float)var12.getY());

            try {
               this.sceneToLocal(var7);
            } catch (NoninvertibleTransformException var11) {
               return null;
            }

            return new Point2D((double)var7.x, (double)var7.y);
         }
      }
   }

   public Point2D screenToLocal(Point2D var1) {
      return this.screenToLocal(var1.getX(), var1.getY());
   }

   public Bounds screenToLocal(Bounds var1) {
      Point2D var2 = this.screenToLocal(var1.getMinX(), var1.getMinY());
      Point2D var3 = this.screenToLocal(var1.getMinX(), var1.getMaxY());
      Point2D var4 = this.screenToLocal(var1.getMaxX(), var1.getMinY());
      Point2D var5 = this.screenToLocal(var1.getMaxX(), var1.getMaxY());
      return BoundsUtils.createBoundingBox(var2, var3, var4, var5);
   }

   public Point2D sceneToLocal(double var1, double var3, boolean var5) {
      if (!var5) {
         return this.sceneToLocal(var1, var3);
      } else {
         com.sun.javafx.geom.Point2D var6 = TempState.getInstance().point;
         var6.setLocation((float)var1, (float)var3);
         SubScene var7 = this.getSubScene();
         if (var7 != null) {
            Point2D var8 = SceneUtils.sceneToSubScenePlane(var7, new Point2D((double)var6.x, (double)var6.y));
            if (var8 == null) {
               return null;
            }

            var6.setLocation((float)var8.getX(), (float)var8.getY());
         }

         try {
            this.sceneToLocal(var6);
            return new Point2D((double)var6.x, (double)var6.y);
         } catch (NoninvertibleTransformException var9) {
            return null;
         }
      }
   }

   public Point2D sceneToLocal(Point2D var1, boolean var2) {
      return this.sceneToLocal(var1.getX(), var1.getY(), var2);
   }

   public Bounds sceneToLocal(Bounds var1, boolean var2) {
      if (!var2) {
         return this.sceneToLocal(var1);
      } else if (var1.getMinZ() == 0.0 && var1.getMaxZ() == 0.0) {
         Point2D var3 = this.sceneToLocal(var1.getMinX(), var1.getMinY(), true);
         Point2D var4 = this.sceneToLocal(var1.getMinX(), var1.getMaxY(), true);
         Point2D var5 = this.sceneToLocal(var1.getMaxX(), var1.getMinY(), true);
         Point2D var6 = this.sceneToLocal(var1.getMaxX(), var1.getMaxY(), true);
         return BoundsUtils.createBoundingBox(var3, var4, var5, var6);
      } else {
         return null;
      }
   }

   public Point2D sceneToLocal(double var1, double var3) {
      com.sun.javafx.geom.Point2D var5 = TempState.getInstance().point;
      var5.setLocation((float)var1, (float)var3);

      try {
         this.sceneToLocal(var5);
      } catch (NoninvertibleTransformException var7) {
         return null;
      }

      return new Point2D((double)var5.x, (double)var5.y);
   }

   public Point2D sceneToLocal(Point2D var1) {
      return this.sceneToLocal(var1.getX(), var1.getY());
   }

   public Point3D sceneToLocal(Point3D var1) {
      return this.sceneToLocal(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D sceneToLocal(double var1, double var3, double var5) {
      try {
         return this.sceneToLocal0(var1, var3, var5);
      } catch (NoninvertibleTransformException var8) {
         return null;
      }
   }

   private Point3D sceneToLocal0(double var1, double var3, double var5) throws NoninvertibleTransformException {
      Vec3d var7 = TempState.getInstance().vec3d;
      var7.set(var1, var3, var5);
      this.sceneToLocal(var7);
      return new Point3D(var7.x, var7.y, var7.z);
   }

   public Bounds sceneToLocal(Bounds var1) {
      this.updateLocalToParentTransform();
      if (this.localToParentTx.is2D() && var1.getMinZ() == 0.0 && var1.getMaxZ() == 0.0) {
         Point2D var11 = this.sceneToLocal(var1.getMinX(), var1.getMinY());
         Point2D var12 = this.sceneToLocal(var1.getMaxX(), var1.getMinY());
         Point2D var13 = this.sceneToLocal(var1.getMaxX(), var1.getMaxY());
         Point2D var14 = this.sceneToLocal(var1.getMinX(), var1.getMaxY());
         return BoundsUtils.createBoundingBox(var11, var12, var13, var14);
      } else {
         try {
            Point3D var2 = this.sceneToLocal0(var1.getMinX(), var1.getMinY(), var1.getMinZ());
            Point3D var3 = this.sceneToLocal0(var1.getMinX(), var1.getMinY(), var1.getMaxZ());
            Point3D var4 = this.sceneToLocal0(var1.getMinX(), var1.getMaxY(), var1.getMinZ());
            Point3D var5 = this.sceneToLocal0(var1.getMinX(), var1.getMaxY(), var1.getMaxZ());
            Point3D var6 = this.sceneToLocal0(var1.getMaxX(), var1.getMaxY(), var1.getMinZ());
            Point3D var7 = this.sceneToLocal0(var1.getMaxX(), var1.getMaxY(), var1.getMaxZ());
            Point3D var8 = this.sceneToLocal0(var1.getMaxX(), var1.getMinY(), var1.getMinZ());
            Point3D var9 = this.sceneToLocal0(var1.getMaxX(), var1.getMinY(), var1.getMaxZ());
            return BoundsUtils.createBoundingBox(var2, var3, var4, var5, var6, var7, var8, var9);
         } catch (NoninvertibleTransformException var10) {
            return null;
         }
      }
   }

   public Point2D localToScreen(double var1, double var3) {
      return this.localToScreen(var1, var3, 0.0);
   }

   public Point2D localToScreen(Point2D var1) {
      return this.localToScreen(var1.getX(), var1.getY());
   }

   public Point2D localToScreen(double var1, double var3, double var5) {
      Scene var7 = this.getScene();
      if (var7 == null) {
         return null;
      } else {
         Window var8 = var7.getWindow();
         if (var8 == null) {
            return null;
         } else {
            Point3D var9 = this.localToScene(var1, var3, var5);
            SubScene var10 = this.getSubScene();
            if (var10 != null) {
               var9 = SceneUtils.subSceneToScene(var10, var9);
            }

            Point2D var11 = CameraHelper.project(SceneHelper.getEffectiveCamera(this.getScene()), var9);
            return new Point2D(var11.getX() + var7.getX() + var8.getX(), var11.getY() + var7.getY() + var8.getY());
         }
      }
   }

   public Point2D localToScreen(Point3D var1) {
      return this.localToScreen(var1.getX(), var1.getY(), var1.getZ());
   }

   public Bounds localToScreen(Bounds var1) {
      Point2D var2 = this.localToScreen(var1.getMinX(), var1.getMinY(), var1.getMinZ());
      Point2D var3 = this.localToScreen(var1.getMinX(), var1.getMinY(), var1.getMaxZ());
      Point2D var4 = this.localToScreen(var1.getMinX(), var1.getMaxY(), var1.getMinZ());
      Point2D var5 = this.localToScreen(var1.getMinX(), var1.getMaxY(), var1.getMaxZ());
      Point2D var6 = this.localToScreen(var1.getMaxX(), var1.getMaxY(), var1.getMinZ());
      Point2D var7 = this.localToScreen(var1.getMaxX(), var1.getMaxY(), var1.getMaxZ());
      Point2D var8 = this.localToScreen(var1.getMaxX(), var1.getMinY(), var1.getMinZ());
      Point2D var9 = this.localToScreen(var1.getMaxX(), var1.getMinY(), var1.getMaxZ());
      return BoundsUtils.createBoundingBox(var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public Point2D localToScene(double var1, double var3) {
      com.sun.javafx.geom.Point2D var5 = TempState.getInstance().point;
      var5.setLocation((float)var1, (float)var3);
      this.localToScene(var5);
      return new Point2D((double)var5.x, (double)var5.y);
   }

   public Point2D localToScene(Point2D var1) {
      return this.localToScene(var1.getX(), var1.getY());
   }

   public Point3D localToScene(Point3D var1) {
      return this.localToScene(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D localToScene(double var1, double var3, double var5) {
      Vec3d var7 = TempState.getInstance().vec3d;
      var7.set(var1, var3, var5);
      this.localToScene(var7);
      return new Point3D(var7.x, var7.y, var7.z);
   }

   public Point3D localToScene(Point3D var1, boolean var2) {
      Point3D var3 = this.localToScene(var1);
      if (var2) {
         SubScene var4 = this.getSubScene();
         if (var4 != null) {
            var3 = SceneUtils.subSceneToScene(var4, var3);
         }
      }

      return var3;
   }

   public Point3D localToScene(double var1, double var3, double var5, boolean var7) {
      return this.localToScene(new Point3D(var1, var3, var5), var7);
   }

   public Point2D localToScene(Point2D var1, boolean var2) {
      if (!var2) {
         return this.localToScene(var1);
      } else {
         Point3D var3 = this.localToScene(var1.getX(), var1.getY(), 0.0, var2);
         return new Point2D(var3.getX(), var3.getY());
      }
   }

   public Point2D localToScene(double var1, double var3, boolean var5) {
      return this.localToScene(new Point2D(var1, var3), var5);
   }

   public Bounds localToScene(Bounds var1, boolean var2) {
      if (!var2) {
         return this.localToScene(var1);
      } else {
         Point3D var3 = this.localToScene(var1.getMinX(), var1.getMinY(), var1.getMinZ(), true);
         Point3D var4 = this.localToScene(var1.getMinX(), var1.getMinY(), var1.getMaxZ(), true);
         Point3D var5 = this.localToScene(var1.getMinX(), var1.getMaxY(), var1.getMinZ(), true);
         Point3D var6 = this.localToScene(var1.getMinX(), var1.getMaxY(), var1.getMaxZ(), true);
         Point3D var7 = this.localToScene(var1.getMaxX(), var1.getMaxY(), var1.getMinZ(), true);
         Point3D var8 = this.localToScene(var1.getMaxX(), var1.getMaxY(), var1.getMaxZ(), true);
         Point3D var9 = this.localToScene(var1.getMaxX(), var1.getMinY(), var1.getMinZ(), true);
         Point3D var10 = this.localToScene(var1.getMaxX(), var1.getMinY(), var1.getMaxZ(), true);
         return BoundsUtils.createBoundingBox(var3, var4, var5, var6, var7, var8, var9, var10);
      }
   }

   public Bounds localToScene(Bounds var1) {
      this.updateLocalToParentTransform();
      if (this.localToParentTx.is2D() && var1.getMinZ() == 0.0 && var1.getMaxZ() == 0.0) {
         Point2D var10 = this.localToScene(var1.getMinX(), var1.getMinY());
         Point2D var11 = this.localToScene(var1.getMaxX(), var1.getMinY());
         Point2D var12 = this.localToScene(var1.getMaxX(), var1.getMaxY());
         Point2D var13 = this.localToScene(var1.getMinX(), var1.getMaxY());
         return BoundsUtils.createBoundingBox(var10, var11, var12, var13);
      } else {
         Point3D var2 = this.localToScene(var1.getMinX(), var1.getMinY(), var1.getMinZ());
         Point3D var3 = this.localToScene(var1.getMinX(), var1.getMinY(), var1.getMaxZ());
         Point3D var4 = this.localToScene(var1.getMinX(), var1.getMaxY(), var1.getMinZ());
         Point3D var5 = this.localToScene(var1.getMinX(), var1.getMaxY(), var1.getMaxZ());
         Point3D var6 = this.localToScene(var1.getMaxX(), var1.getMaxY(), var1.getMinZ());
         Point3D var7 = this.localToScene(var1.getMaxX(), var1.getMaxY(), var1.getMaxZ());
         Point3D var8 = this.localToScene(var1.getMaxX(), var1.getMinY(), var1.getMinZ());
         Point3D var9 = this.localToScene(var1.getMaxX(), var1.getMinY(), var1.getMaxZ());
         return BoundsUtils.createBoundingBox(var2, var3, var4, var5, var6, var7, var8, var9);
      }
   }

   public Point2D parentToLocal(double var1, double var3) {
      com.sun.javafx.geom.Point2D var5 = TempState.getInstance().point;
      var5.setLocation((float)var1, (float)var3);

      try {
         this.parentToLocal(var5);
      } catch (NoninvertibleTransformException var7) {
         return null;
      }

      return new Point2D((double)var5.x, (double)var5.y);
   }

   public Point2D parentToLocal(Point2D var1) {
      return this.parentToLocal(var1.getX(), var1.getY());
   }

   public Point3D parentToLocal(Point3D var1) {
      return this.parentToLocal(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D parentToLocal(double var1, double var3, double var5) {
      Vec3d var7 = TempState.getInstance().vec3d;
      var7.set(var1, var3, var5);

      try {
         this.parentToLocal(var7);
      } catch (NoninvertibleTransformException var9) {
         return null;
      }

      return new Point3D(var7.x, var7.y, var7.z);
   }

   public Bounds parentToLocal(Bounds var1) {
      this.updateLocalToParentTransform();
      if (this.localToParentTx.is2D() && var1.getMinZ() == 0.0 && var1.getMaxZ() == 0.0) {
         Point2D var10 = this.parentToLocal(var1.getMinX(), var1.getMinY());
         Point2D var11 = this.parentToLocal(var1.getMaxX(), var1.getMinY());
         Point2D var12 = this.parentToLocal(var1.getMaxX(), var1.getMaxY());
         Point2D var13 = this.parentToLocal(var1.getMinX(), var1.getMaxY());
         return BoundsUtils.createBoundingBox(var10, var11, var12, var13);
      } else {
         Point3D var2 = this.parentToLocal(var1.getMinX(), var1.getMinY(), var1.getMinZ());
         Point3D var3 = this.parentToLocal(var1.getMinX(), var1.getMinY(), var1.getMaxZ());
         Point3D var4 = this.parentToLocal(var1.getMinX(), var1.getMaxY(), var1.getMinZ());
         Point3D var5 = this.parentToLocal(var1.getMinX(), var1.getMaxY(), var1.getMaxZ());
         Point3D var6 = this.parentToLocal(var1.getMaxX(), var1.getMaxY(), var1.getMinZ());
         Point3D var7 = this.parentToLocal(var1.getMaxX(), var1.getMaxY(), var1.getMaxZ());
         Point3D var8 = this.parentToLocal(var1.getMaxX(), var1.getMinY(), var1.getMinZ());
         Point3D var9 = this.parentToLocal(var1.getMaxX(), var1.getMinY(), var1.getMaxZ());
         return BoundsUtils.createBoundingBox(var2, var3, var4, var5, var6, var7, var8, var9);
      }
   }

   public Point2D localToParent(double var1, double var3) {
      com.sun.javafx.geom.Point2D var5 = TempState.getInstance().point;
      var5.setLocation((float)var1, (float)var3);
      this.localToParent(var5);
      return new Point2D((double)var5.x, (double)var5.y);
   }

   public Point2D localToParent(Point2D var1) {
      return this.localToParent(var1.getX(), var1.getY());
   }

   public Point3D localToParent(Point3D var1) {
      return this.localToParent(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D localToParent(double var1, double var3, double var5) {
      Vec3d var7 = TempState.getInstance().vec3d;
      var7.set(var1, var3, var5);
      this.localToParent(var7);
      return new Point3D(var7.x, var7.y, var7.z);
   }

   public Bounds localToParent(Bounds var1) {
      this.updateLocalToParentTransform();
      if (this.localToParentTx.is2D() && var1.getMinZ() == 0.0 && var1.getMaxZ() == 0.0) {
         Point2D var10 = this.localToParent(var1.getMinX(), var1.getMinY());
         Point2D var11 = this.localToParent(var1.getMaxX(), var1.getMinY());
         Point2D var12 = this.localToParent(var1.getMaxX(), var1.getMaxY());
         Point2D var13 = this.localToParent(var1.getMinX(), var1.getMaxY());
         return BoundsUtils.createBoundingBox(var10, var11, var12, var13);
      } else {
         Point3D var2 = this.localToParent(var1.getMinX(), var1.getMinY(), var1.getMinZ());
         Point3D var3 = this.localToParent(var1.getMinX(), var1.getMinY(), var1.getMaxZ());
         Point3D var4 = this.localToParent(var1.getMinX(), var1.getMaxY(), var1.getMinZ());
         Point3D var5 = this.localToParent(var1.getMinX(), var1.getMaxY(), var1.getMaxZ());
         Point3D var6 = this.localToParent(var1.getMaxX(), var1.getMaxY(), var1.getMinZ());
         Point3D var7 = this.localToParent(var1.getMaxX(), var1.getMaxY(), var1.getMaxZ());
         Point3D var8 = this.localToParent(var1.getMaxX(), var1.getMinY(), var1.getMinZ());
         Point3D var9 = this.localToParent(var1.getMaxX(), var1.getMinY(), var1.getMaxZ());
         return BoundsUtils.createBoundingBox(var2, var3, var4, var5, var6, var7, var8, var9);
      }
   }

   BaseTransform getLocalToParentTransform(BaseTransform var1) {
      this.updateLocalToParentTransform();
      var1.setTransform(this.localToParentTx);
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public final BaseTransform impl_getLeafTransform() {
      return this.getLocalToParentTransform(TempState.getInstance().leafTx);
   }

   /** @deprecated */
   @Deprecated
   public void impl_transformsChanged() {
      if (!this.transformDirty) {
         this.impl_markDirty(DirtyBits.NODE_TRANSFORM);
         this.transformDirty = true;
         this.transformedBoundsChanged();
      }

      this.invalidateLocalToParentTransform();
      this.invalidateLocalToSceneTransform();
   }

   /** @deprecated */
   @Deprecated
   public final double impl_getPivotX() {
      Bounds var1 = this.getLayoutBounds();
      return var1.getMinX() + var1.getWidth() / 2.0;
   }

   /** @deprecated */
   @Deprecated
   public final double impl_getPivotY() {
      Bounds var1 = this.getLayoutBounds();
      return var1.getMinY() + var1.getHeight() / 2.0;
   }

   /** @deprecated */
   @Deprecated
   public final double impl_getPivotZ() {
      Bounds var1 = this.getLayoutBounds();
      return var1.getMinZ() + var1.getDepth() / 2.0;
   }

   void updateLocalToParentTransform() {
      if (this.transformDirty) {
         this.localToParentTx.setToIdentity();
         boolean var1 = false;
         double var2 = 0.0;
         if (this.hasMirroring()) {
            Scene var4 = this.getScene();
            if (var4 != null && var4.getRoot() == this) {
               var2 = var4.getWidth() / 2.0;
               if (var2 == 0.0) {
                  var2 = this.impl_getPivotX();
               }

               this.localToParentTx = this.localToParentTx.deriveWithTranslation(var2, 0.0);
               this.localToParentTx = this.localToParentTx.deriveWithScale(-1.0, 1.0, 1.0);
               this.localToParentTx = this.localToParentTx.deriveWithTranslation(-var2, 0.0);
            } else {
               var1 = true;
               var2 = this.impl_getPivotX();
            }
         }

         if (this.getScaleX() == 1.0 && this.getScaleY() == 1.0 && this.getScaleZ() == 1.0 && this.getRotate() == 0.0) {
            this.localToParentTx = this.localToParentTx.deriveWithTranslation(this.getTranslateX() + this.getLayoutX(), this.getTranslateY() + this.getLayoutY(), this.getTranslateZ());
         } else {
            double var10 = this.impl_getPivotX();
            double var6 = this.impl_getPivotY();
            double var8 = this.impl_getPivotZ();
            this.localToParentTx = this.localToParentTx.deriveWithTranslation(this.getTranslateX() + this.getLayoutX() + var10, this.getTranslateY() + this.getLayoutY() + var6, this.getTranslateZ() + var8);
            this.localToParentTx = this.localToParentTx.deriveWithRotation(Math.toRadians(this.getRotate()), this.getRotationAxis().getX(), this.getRotationAxis().getY(), this.getRotationAxis().getZ());
            this.localToParentTx = this.localToParentTx.deriveWithScale(this.getScaleX(), this.getScaleY(), this.getScaleZ());
            this.localToParentTx = this.localToParentTx.deriveWithTranslation(-var10, -var6, -var8);
         }

         Transform var5;
         if (this.impl_hasTransforms()) {
            for(Iterator var11 = this.getTransforms().iterator(); var11.hasNext(); this.localToParentTx = var5.impl_derive(this.localToParentTx)) {
               var5 = (Transform)var11.next();
            }
         }

         if (var1) {
            this.localToParentTx = this.localToParentTx.deriveWithTranslation(var2, 0.0);
            this.localToParentTx = this.localToParentTx.deriveWithScale(-1.0, 1.0, 1.0);
            this.localToParentTx = this.localToParentTx.deriveWithTranslation(-var2, 0.0);
         }

         this.transformDirty = false;
      }

   }

   void parentToLocal(com.sun.javafx.geom.Point2D var1) throws NoninvertibleTransformException {
      this.updateLocalToParentTransform();
      this.localToParentTx.inverseTransform(var1, var1);
   }

   void parentToLocal(Vec3d var1) throws NoninvertibleTransformException {
      this.updateLocalToParentTransform();
      this.localToParentTx.inverseTransform(var1, var1);
   }

   void sceneToLocal(com.sun.javafx.geom.Point2D var1) throws NoninvertibleTransformException {
      if (this.getParent() != null) {
         this.getParent().sceneToLocal(var1);
      }

      this.parentToLocal(var1);
   }

   void sceneToLocal(Vec3d var1) throws NoninvertibleTransformException {
      if (this.getParent() != null) {
         this.getParent().sceneToLocal(var1);
      }

      this.parentToLocal(var1);
   }

   void localToScene(com.sun.javafx.geom.Point2D var1) {
      this.localToParent(var1);
      if (this.getParent() != null) {
         this.getParent().localToScene(var1);
      }

   }

   void localToScene(Vec3d var1) {
      this.localToParent(var1);
      if (this.getParent() != null) {
         this.getParent().localToScene(var1);
      }

   }

   void localToParent(com.sun.javafx.geom.Point2D var1) {
      this.updateLocalToParentTransform();
      this.localToParentTx.transform(var1, var1);
   }

   void localToParent(Vec3d var1) {
      this.updateLocalToParentTransform();
      this.localToParentTx.transform(var1, var1);
   }

   /** @deprecated */
   @Deprecated
   protected void impl_pickNodeLocal(PickRay var1, PickResultChooser var2) {
      this.impl_intersects(var1, var2);
   }

   /** @deprecated */
   @Deprecated
   public final void impl_pickNode(PickRay var1, PickResultChooser var2) {
      if (this.isVisible() && !this.isDisable() && !this.isMouseTransparent()) {
         Vec3d var3 = var1.getOriginNoClone();
         double var4 = var3.x;
         double var6 = var3.y;
         double var8 = var3.z;
         Vec3d var10 = var1.getDirectionNoClone();
         double var11 = var10.x;
         double var13 = var10.y;
         double var15 = var10.z;
         this.updateLocalToParentTransform();

         try {
            this.localToParentTx.inverseTransform(var3, var3);
            this.localToParentTx.inverseDeltaTransform(var10, var10);
            this.impl_pickNodeLocal(var1, var2);
         } catch (NoninvertibleTransformException var18) {
         }

         var1.setOrigin(var4, var6, var8);
         var1.setDirection(var11, var13, var15);
      }
   }

   /** @deprecated */
   @Deprecated
   protected final boolean impl_intersects(PickRay var1, PickResultChooser var2) {
      double var3 = this.impl_intersectsBounds(var1);
      if (!Double.isNaN(var3)) {
         if (this.isPickOnBounds()) {
            if (var2 != null) {
               var2.offer(this, var3, PickResultChooser.computePoint(var1, var3));
            }

            return true;
         } else {
            return this.impl_computeIntersects(var1, var2);
         }
      } else {
         return false;
      }
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeIntersects(PickRay var1, PickResultChooser var2) {
      double var3 = var1.getOriginNoClone().z;
      double var5 = var1.getDirectionNoClone().z;
      if (almostZero(var5)) {
         return false;
      } else {
         double var7 = -var3 / var5;
         if (!(var7 < var1.getNearClip()) && !(var7 > var1.getFarClip())) {
            double var9 = var1.getOriginNoClone().x + var1.getDirectionNoClone().x * var7;
            double var11 = var1.getOriginNoClone().y + var1.getDirectionNoClone().y * var7;
            if (this.contains((double)((float)var9), (double)((float)var11))) {
               if (var2 != null) {
                  var2.offer(this, var7, PickResultChooser.computePoint(var1, var7));
               }

               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   protected final double impl_intersectsBounds(PickRay var1) {
      Vec3d var2 = var1.getDirectionNoClone();
      Vec3d var7 = var1.getOriginNoClone();
      double var8 = var7.x;
      double var10 = var7.y;
      double var12 = var7.z;
      TempState var14 = TempState.getInstance();
      BaseBounds var15 = var14.bounds;
      var15 = this.getLocalBounds(var15, BaseTransform.IDENTITY_TRANSFORM);
      double var3;
      double var5;
      double var16;
      double var19;
      double var21;
      boolean var23;
      if (var2.x == 0.0 && var2.y == 0.0) {
         if (var2.z == 0.0) {
            return Double.NaN;
         }

         if (var8 < (double)var15.getMinX() || var8 > (double)var15.getMaxX() || var10 < (double)var15.getMinY() || var10 > (double)var15.getMaxY()) {
            return Double.NaN;
         }

         var16 = 1.0 / var2.z;
         boolean var43 = var16 < 0.0;
         var19 = (double)var15.getMinZ();
         var21 = (double)var15.getMaxZ();
         var3 = ((var43 ? var21 : var19) - var12) * var16;
         var5 = ((var43 ? var19 : var21) - var12) * var16;
      } else {
         double var18;
         double var20;
         if ((double)var15.getDepth() == 0.0) {
            if (almostZero(var2.z)) {
               return Double.NaN;
            }

            var16 = ((double)var15.getMinZ() - var12) / var2.z;
            var18 = var8 + var2.x * var16;
            var20 = var10 + var2.y * var16;
            if (var18 < (double)var15.getMinX() || var18 > (double)var15.getMaxX() || var20 < (double)var15.getMinY() || var20 > (double)var15.getMaxY()) {
               return Double.NaN;
            }

            var5 = var16;
            var3 = var16;
         } else {
            var16 = var2.x == 0.0 ? Double.POSITIVE_INFINITY : 1.0 / var2.x;
            var18 = var2.y == 0.0 ? Double.POSITIVE_INFINITY : 1.0 / var2.y;
            var20 = var2.z == 0.0 ? Double.POSITIVE_INFINITY : 1.0 / var2.z;
            boolean var22 = var16 < 0.0;
            var23 = var18 < 0.0;
            boolean var24 = var20 < 0.0;
            double var25 = (double)var15.getMinX();
            double var27 = (double)var15.getMinY();
            double var29 = (double)var15.getMaxX();
            double var31 = (double)var15.getMaxY();
            var3 = Double.NEGATIVE_INFINITY;
            var5 = Double.POSITIVE_INFINITY;
            if (Double.isInfinite(var16)) {
               if (!(var25 <= var8) || !(var29 >= var8)) {
                  return Double.NaN;
               }
            } else {
               var3 = ((var22 ? var29 : var25) - var8) * var16;
               var5 = ((var22 ? var25 : var29) - var8) * var16;
            }

            double var33;
            double var35;
            if (Double.isInfinite(var18)) {
               if (!(var27 <= var10) || !(var31 >= var10)) {
                  return Double.NaN;
               }
            } else {
               var33 = ((var23 ? var31 : var27) - var10) * var18;
               var35 = ((var23 ? var27 : var31) - var10) * var18;
               if (var3 > var35 || var33 > var5) {
                  return Double.NaN;
               }

               if (var33 > var3) {
                  var3 = var33;
               }

               if (var35 < var5) {
                  var5 = var35;
               }
            }

            var33 = (double)var15.getMinZ();
            var35 = (double)var15.getMaxZ();
            if (Double.isInfinite(var20)) {
               if (!(var33 <= var12) || !(var35 >= var12)) {
                  return Double.NaN;
               }
            } else {
               double var37 = ((var24 ? var35 : var33) - var12) * var20;
               double var39 = ((var24 ? var33 : var35) - var12) * var20;
               if (var3 > var39 || var37 > var5) {
                  return Double.NaN;
               }

               if (var37 > var3) {
                  var3 = var37;
               }

               if (var39 < var5) {
                  var5 = var39;
               }
            }
         }
      }

      Node var42 = this.getClip();
      double var17;
      if (var42 != null && !(this instanceof Shape3D) && !(var42 instanceof Shape3D)) {
         var17 = var2.x;
         var19 = var2.y;
         var21 = var2.z;
         var42.updateLocalToParentTransform();
         var23 = true;

         try {
            var42.localToParentTx.inverseTransform(var7, var7);
            var42.localToParentTx.inverseDeltaTransform(var2, var2);
         } catch (NoninvertibleTransformException var41) {
            var23 = false;
         }

         var23 = var23 && var42.impl_intersects(var1, (PickResultChooser)null);
         var1.setOrigin(var8, var10, var12);
         var1.setDirection(var17, var19, var21);
         if (!var23) {
            return Double.NaN;
         }
      }

      if (!Double.isInfinite(var3) && !Double.isNaN(var3)) {
         var17 = var1.getNearClip();
         var19 = var1.getFarClip();
         if (var3 < var17) {
            if (var5 >= var17) {
               return 0.0;
            } else {
               return Double.NaN;
            }
         } else if (var3 > var19) {
            return Double.NaN;
         } else {
            return var3;
         }
      } else {
         return Double.NaN;
      }
   }

   static boolean almostZero(double var0) {
      return var0 < 1.0E-5 && var0 > -1.0E-5;
   }

   public final ObservableList getTransforms() {
      return this.transformsProperty();
   }

   private ObservableList transformsProperty() {
      return this.getNodeTransformation().getTransforms();
   }

   public final void setTranslateX(double var1) {
      this.translateXProperty().set(var1);
   }

   public final double getTranslateX() {
      return this.nodeTransformation == null ? 0.0 : this.nodeTransformation.getTranslateX();
   }

   public final DoubleProperty translateXProperty() {
      return this.getNodeTransformation().translateXProperty();
   }

   public final void setTranslateY(double var1) {
      this.translateYProperty().set(var1);
   }

   public final double getTranslateY() {
      return this.nodeTransformation == null ? 0.0 : this.nodeTransformation.getTranslateY();
   }

   public final DoubleProperty translateYProperty() {
      return this.getNodeTransformation().translateYProperty();
   }

   public final void setTranslateZ(double var1) {
      this.translateZProperty().set(var1);
   }

   public final double getTranslateZ() {
      return this.nodeTransformation == null ? 0.0 : this.nodeTransformation.getTranslateZ();
   }

   public final DoubleProperty translateZProperty() {
      return this.getNodeTransformation().translateZProperty();
   }

   public final void setScaleX(double var1) {
      this.scaleXProperty().set(var1);
   }

   public final double getScaleX() {
      return this.nodeTransformation == null ? 1.0 : this.nodeTransformation.getScaleX();
   }

   public final DoubleProperty scaleXProperty() {
      return this.getNodeTransformation().scaleXProperty();
   }

   public final void setScaleY(double var1) {
      this.scaleYProperty().set(var1);
   }

   public final double getScaleY() {
      return this.nodeTransformation == null ? 1.0 : this.nodeTransformation.getScaleY();
   }

   public final DoubleProperty scaleYProperty() {
      return this.getNodeTransformation().scaleYProperty();
   }

   public final void setScaleZ(double var1) {
      this.scaleZProperty().set(var1);
   }

   public final double getScaleZ() {
      return this.nodeTransformation == null ? 1.0 : this.nodeTransformation.getScaleZ();
   }

   public final DoubleProperty scaleZProperty() {
      return this.getNodeTransformation().scaleZProperty();
   }

   public final void setRotate(double var1) {
      this.rotateProperty().set(var1);
   }

   public final double getRotate() {
      return this.nodeTransformation == null ? 0.0 : this.nodeTransformation.getRotate();
   }

   public final DoubleProperty rotateProperty() {
      return this.getNodeTransformation().rotateProperty();
   }

   public final void setRotationAxis(Point3D var1) {
      this.rotationAxisProperty().set(var1);
   }

   public final Point3D getRotationAxis() {
      return this.nodeTransformation == null ? DEFAULT_ROTATION_AXIS : this.nodeTransformation.getRotationAxis();
   }

   public final ObjectProperty rotationAxisProperty() {
      return this.getNodeTransformation().rotationAxisProperty();
   }

   public final ReadOnlyObjectProperty localToParentTransformProperty() {
      return this.getNodeTransformation().localToParentTransformProperty();
   }

   private void invalidateLocalToParentTransform() {
      if (this.nodeTransformation != null) {
         this.nodeTransformation.invalidateLocalToParentTransform();
      }

   }

   public final Transform getLocalToParentTransform() {
      return (Transform)this.localToParentTransformProperty().get();
   }

   public final ReadOnlyObjectProperty localToSceneTransformProperty() {
      return this.getNodeTransformation().localToSceneTransformProperty();
   }

   private void invalidateLocalToSceneTransform() {
      if (this.nodeTransformation != null) {
         this.nodeTransformation.invalidateLocalToSceneTransform();
      }

   }

   public final Transform getLocalToSceneTransform() {
      return (Transform)this.localToSceneTransformProperty().get();
   }

   private NodeTransformation getNodeTransformation() {
      if (this.nodeTransformation == null) {
         this.nodeTransformation = new NodeTransformation();
      }

      return this.nodeTransformation;
   }

   /** @deprecated */
   @Deprecated
   public boolean impl_hasTransforms() {
      return this.nodeTransformation != null && this.nodeTransformation.hasTransforms();
   }

   Transform getCurrentLocalToSceneTransformState() {
      return this.nodeTransformation != null && this.nodeTransformation.localToSceneTransform != null ? this.nodeTransformation.localToSceneTransform.transform : null;
   }

   private EventHandlerProperties getEventHandlerProperties() {
      if (this.eventHandlerProperties == null) {
         this.eventHandlerProperties = new EventHandlerProperties(this.getInternalEventDispatcher().getEventHandlerManager(), this);
      }

      return this.eventHandlerProperties;
   }

   public final void setNodeOrientation(NodeOrientation var1) {
      this.nodeOrientationProperty().set(var1);
   }

   public final NodeOrientation getNodeOrientation() {
      return this.nodeOrientation == null ? NodeOrientation.INHERIT : (NodeOrientation)this.nodeOrientation.get();
   }

   public final ObjectProperty nodeOrientationProperty() {
      if (this.nodeOrientation == null) {
         this.nodeOrientation = new StyleableObjectProperty(NodeOrientation.INHERIT) {
            protected void invalidated() {
               Node.this.nodeResolvedOrientationInvalidated();
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "nodeOrientation";
            }

            public CssMetaData getCssMetaData() {
               throw new UnsupportedOperationException("Not supported yet.");
            }
         };
      }

      return this.nodeOrientation;
   }

   public final NodeOrientation getEffectiveNodeOrientation() {
      return getEffectiveOrientation(this.resolvedNodeOrientation) == 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT;
   }

   public final ReadOnlyObjectProperty effectiveNodeOrientationProperty() {
      if (this.effectiveNodeOrientationProperty == null) {
         this.effectiveNodeOrientationProperty = new EffectiveOrientationProperty();
      }

      return this.effectiveNodeOrientationProperty;
   }

   public boolean usesMirroring() {
      return true;
   }

   final void parentResolvedOrientationInvalidated() {
      if (this.getNodeOrientation() == NodeOrientation.INHERIT) {
         this.nodeResolvedOrientationInvalidated();
      } else {
         this.impl_transformsChanged();
      }

   }

   final void nodeResolvedOrientationInvalidated() {
      byte var1 = this.resolvedNodeOrientation;
      this.resolvedNodeOrientation = (byte)(this.calcEffectiveNodeOrientation() | this.calcAutomaticNodeOrientation());
      if (this.effectiveNodeOrientationProperty != null && getEffectiveOrientation(this.resolvedNodeOrientation) != getEffectiveOrientation(var1)) {
         this.effectiveNodeOrientationProperty.invalidate();
      }

      this.impl_transformsChanged();
      if (this.resolvedNodeOrientation != var1) {
         this.nodeResolvedOrientationChanged();
      }

   }

   void nodeResolvedOrientationChanged() {
   }

   private Node getMirroringOrientationParent() {
      for(Parent var1 = this.getParent(); var1 != null; var1 = var1.getParent()) {
         if (var1.usesMirroring()) {
            return var1;
         }
      }

      SubScene var2 = this.getSubScene();
      if (var2 != null) {
         return var2;
      } else {
         return null;
      }
   }

   private Node getOrientationParent() {
      Parent var1 = this.getParent();
      if (var1 != null) {
         return var1;
      } else {
         SubScene var2 = this.getSubScene();
         return var2 != null ? var2 : null;
      }
   }

   private byte calcEffectiveNodeOrientation() {
      NodeOrientation var1 = this.getNodeOrientation();
      if (var1 != NodeOrientation.INHERIT) {
         return (byte)(var1 == NodeOrientation.LEFT_TO_RIGHT ? 0 : 1);
      } else {
         Node var2 = this.getOrientationParent();
         if (var2 != null) {
            return getEffectiveOrientation(var2.resolvedNodeOrientation);
         } else {
            Scene var3 = this.getScene();
            if (var3 != null) {
               return (byte)(var3.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT ? 0 : 1);
            } else {
               return 0;
            }
         }
      }
   }

   private byte calcAutomaticNodeOrientation() {
      if (!this.usesMirroring()) {
         return 0;
      } else {
         NodeOrientation var1 = this.getNodeOrientation();
         if (var1 != NodeOrientation.INHERIT) {
            return (byte)(var1 == NodeOrientation.LEFT_TO_RIGHT ? 0 : 2);
         } else {
            Node var2 = this.getMirroringOrientationParent();
            if (var2 != null) {
               return getAutomaticOrientation(var2.resolvedNodeOrientation);
            } else {
               Scene var3 = this.getScene();
               if (var3 != null) {
                  return (byte)(var3.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT ? 0 : 2);
               } else {
                  return 0;
               }
            }
         }
      }
   }

   final boolean hasMirroring() {
      Node var1 = this.getOrientationParent();
      byte var2 = getAutomaticOrientation(this.resolvedNodeOrientation);
      byte var3 = var1 != null ? getAutomaticOrientation(var1.resolvedNodeOrientation) : 0;
      return var2 != var3;
   }

   private static byte getEffectiveOrientation(byte var0) {
      return (byte)(var0 & 1);
   }

   private static byte getAutomaticOrientation(byte var0) {
      return (byte)(var0 & 2);
   }

   private MiscProperties getMiscProperties() {
      if (this.miscProperties == null) {
         this.miscProperties = new MiscProperties();
      }

      return this.miscProperties;
   }

   public final void setMouseTransparent(boolean var1) {
      this.mouseTransparentProperty().set(var1);
   }

   public final boolean isMouseTransparent() {
      return this.miscProperties == null ? false : this.miscProperties.isMouseTransparent();
   }

   public final BooleanProperty mouseTransparentProperty() {
      return this.getMiscProperties().mouseTransparentProperty();
   }

   protected final void setHover(boolean var1) {
      this.hoverPropertyImpl().set(var1);
   }

   public final boolean isHover() {
      return this.hover == null ? false : this.hover.get();
   }

   public final ReadOnlyBooleanProperty hoverProperty() {
      return this.hoverPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper hoverPropertyImpl() {
      if (this.hover == null) {
         this.hover = new ReadOnlyBooleanWrapper() {
            protected void invalidated() {
               PlatformLogger var1 = Logging.getInputLogger();
               if (var1.isLoggable(Level.FINER)) {
                  var1.finer(this + " hover=" + this.get());
               }

               Node.this.pseudoClassStateChanged(Node.HOVER_PSEUDOCLASS_STATE, this.get());
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "hover";
            }
         };
      }

      return this.hover;
   }

   protected final void setPressed(boolean var1) {
      this.pressedPropertyImpl().set(var1);
   }

   public final boolean isPressed() {
      return this.pressed == null ? false : this.pressed.get();
   }

   public final ReadOnlyBooleanProperty pressedProperty() {
      return this.pressedPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper pressedPropertyImpl() {
      if (this.pressed == null) {
         this.pressed = new ReadOnlyBooleanWrapper() {
            protected void invalidated() {
               PlatformLogger var1 = Logging.getInputLogger();
               if (var1.isLoggable(Level.FINER)) {
                  var1.finer(this + " pressed=" + this.get());
               }

               Node.this.pseudoClassStateChanged(Node.PRESSED_PSEUDOCLASS_STATE, this.get());
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "pressed";
            }
         };
      }

      return this.pressed;
   }

   public final void setOnContextMenuRequested(EventHandler var1) {
      this.onContextMenuRequestedProperty().set(var1);
   }

   public final EventHandler getOnContextMenuRequested() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.onContextMenuRequested();
   }

   public final ObjectProperty onContextMenuRequestedProperty() {
      return this.getEventHandlerProperties().onContextMenuRequestedProperty();
   }

   public final void setOnMouseClicked(EventHandler var1) {
      this.onMouseClickedProperty().set(var1);
   }

   public final EventHandler getOnMouseClicked() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseClicked();
   }

   public final ObjectProperty onMouseClickedProperty() {
      return this.getEventHandlerProperties().onMouseClickedProperty();
   }

   public final void setOnMouseDragged(EventHandler var1) {
      this.onMouseDraggedProperty().set(var1);
   }

   public final EventHandler getOnMouseDragged() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseDragged();
   }

   public final ObjectProperty onMouseDraggedProperty() {
      return this.getEventHandlerProperties().onMouseDraggedProperty();
   }

   public final void setOnMouseEntered(EventHandler var1) {
      this.onMouseEnteredProperty().set(var1);
   }

   public final EventHandler getOnMouseEntered() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseEntered();
   }

   public final ObjectProperty onMouseEnteredProperty() {
      return this.getEventHandlerProperties().onMouseEnteredProperty();
   }

   public final void setOnMouseExited(EventHandler var1) {
      this.onMouseExitedProperty().set(var1);
   }

   public final EventHandler getOnMouseExited() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseExited();
   }

   public final ObjectProperty onMouseExitedProperty() {
      return this.getEventHandlerProperties().onMouseExitedProperty();
   }

   public final void setOnMouseMoved(EventHandler var1) {
      this.onMouseMovedProperty().set(var1);
   }

   public final EventHandler getOnMouseMoved() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseMoved();
   }

   public final ObjectProperty onMouseMovedProperty() {
      return this.getEventHandlerProperties().onMouseMovedProperty();
   }

   public final void setOnMousePressed(EventHandler var1) {
      this.onMousePressedProperty().set(var1);
   }

   public final EventHandler getOnMousePressed() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMousePressed();
   }

   public final ObjectProperty onMousePressedProperty() {
      return this.getEventHandlerProperties().onMousePressedProperty();
   }

   public final void setOnMouseReleased(EventHandler var1) {
      this.onMouseReleasedProperty().set(var1);
   }

   public final EventHandler getOnMouseReleased() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseReleased();
   }

   public final ObjectProperty onMouseReleasedProperty() {
      return this.getEventHandlerProperties().onMouseReleasedProperty();
   }

   public final void setOnDragDetected(EventHandler var1) {
      this.onDragDetectedProperty().set(var1);
   }

   public final EventHandler getOnDragDetected() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnDragDetected();
   }

   public final ObjectProperty onDragDetectedProperty() {
      return this.getEventHandlerProperties().onDragDetectedProperty();
   }

   public final void setOnMouseDragOver(EventHandler var1) {
      this.onMouseDragOverProperty().set(var1);
   }

   public final EventHandler getOnMouseDragOver() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseDragOver();
   }

   public final ObjectProperty onMouseDragOverProperty() {
      return this.getEventHandlerProperties().onMouseDragOverProperty();
   }

   public final void setOnMouseDragReleased(EventHandler var1) {
      this.onMouseDragReleasedProperty().set(var1);
   }

   public final EventHandler getOnMouseDragReleased() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseDragReleased();
   }

   public final ObjectProperty onMouseDragReleasedProperty() {
      return this.getEventHandlerProperties().onMouseDragReleasedProperty();
   }

   public final void setOnMouseDragEntered(EventHandler var1) {
      this.onMouseDragEnteredProperty().set(var1);
   }

   public final EventHandler getOnMouseDragEntered() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseDragEntered();
   }

   public final ObjectProperty onMouseDragEnteredProperty() {
      return this.getEventHandlerProperties().onMouseDragEnteredProperty();
   }

   public final void setOnMouseDragExited(EventHandler var1) {
      this.onMouseDragExitedProperty().set(var1);
   }

   public final EventHandler getOnMouseDragExited() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnMouseDragExited();
   }

   public final ObjectProperty onMouseDragExitedProperty() {
      return this.getEventHandlerProperties().onMouseDragExitedProperty();
   }

   public final void setOnScrollStarted(EventHandler var1) {
      this.onScrollStartedProperty().set(var1);
   }

   public final EventHandler getOnScrollStarted() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnScrollStarted();
   }

   public final ObjectProperty onScrollStartedProperty() {
      return this.getEventHandlerProperties().onScrollStartedProperty();
   }

   public final void setOnScroll(EventHandler var1) {
      this.onScrollProperty().set(var1);
   }

   public final EventHandler getOnScroll() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnScroll();
   }

   public final ObjectProperty onScrollProperty() {
      return this.getEventHandlerProperties().onScrollProperty();
   }

   public final void setOnScrollFinished(EventHandler var1) {
      this.onScrollFinishedProperty().set(var1);
   }

   public final EventHandler getOnScrollFinished() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnScrollFinished();
   }

   public final ObjectProperty onScrollFinishedProperty() {
      return this.getEventHandlerProperties().onScrollFinishedProperty();
   }

   public final void setOnRotationStarted(EventHandler var1) {
      this.onRotationStartedProperty().set(var1);
   }

   public final EventHandler getOnRotationStarted() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnRotationStarted();
   }

   public final ObjectProperty onRotationStartedProperty() {
      return this.getEventHandlerProperties().onRotationStartedProperty();
   }

   public final void setOnRotate(EventHandler var1) {
      this.onRotateProperty().set(var1);
   }

   public final EventHandler getOnRotate() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnRotate();
   }

   public final ObjectProperty onRotateProperty() {
      return this.getEventHandlerProperties().onRotateProperty();
   }

   public final void setOnRotationFinished(EventHandler var1) {
      this.onRotationFinishedProperty().set(var1);
   }

   public final EventHandler getOnRotationFinished() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnRotationFinished();
   }

   public final ObjectProperty onRotationFinishedProperty() {
      return this.getEventHandlerProperties().onRotationFinishedProperty();
   }

   public final void setOnZoomStarted(EventHandler var1) {
      this.onZoomStartedProperty().set(var1);
   }

   public final EventHandler getOnZoomStarted() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnZoomStarted();
   }

   public final ObjectProperty onZoomStartedProperty() {
      return this.getEventHandlerProperties().onZoomStartedProperty();
   }

   public final void setOnZoom(EventHandler var1) {
      this.onZoomProperty().set(var1);
   }

   public final EventHandler getOnZoom() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnZoom();
   }

   public final ObjectProperty onZoomProperty() {
      return this.getEventHandlerProperties().onZoomProperty();
   }

   public final void setOnZoomFinished(EventHandler var1) {
      this.onZoomFinishedProperty().set(var1);
   }

   public final EventHandler getOnZoomFinished() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnZoomFinished();
   }

   public final ObjectProperty onZoomFinishedProperty() {
      return this.getEventHandlerProperties().onZoomFinishedProperty();
   }

   public final void setOnSwipeUp(EventHandler var1) {
      this.onSwipeUpProperty().set(var1);
   }

   public final EventHandler getOnSwipeUp() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnSwipeUp();
   }

   public final ObjectProperty onSwipeUpProperty() {
      return this.getEventHandlerProperties().onSwipeUpProperty();
   }

   public final void setOnSwipeDown(EventHandler var1) {
      this.onSwipeDownProperty().set(var1);
   }

   public final EventHandler getOnSwipeDown() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnSwipeDown();
   }

   public final ObjectProperty onSwipeDownProperty() {
      return this.getEventHandlerProperties().onSwipeDownProperty();
   }

   public final void setOnSwipeLeft(EventHandler var1) {
      this.onSwipeLeftProperty().set(var1);
   }

   public final EventHandler getOnSwipeLeft() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnSwipeLeft();
   }

   public final ObjectProperty onSwipeLeftProperty() {
      return this.getEventHandlerProperties().onSwipeLeftProperty();
   }

   public final void setOnSwipeRight(EventHandler var1) {
      this.onSwipeRightProperty().set(var1);
   }

   public final EventHandler getOnSwipeRight() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnSwipeRight();
   }

   public final ObjectProperty onSwipeRightProperty() {
      return this.getEventHandlerProperties().onSwipeRightProperty();
   }

   public final void setOnTouchPressed(EventHandler var1) {
      this.onTouchPressedProperty().set(var1);
   }

   public final EventHandler getOnTouchPressed() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnTouchPressed();
   }

   public final ObjectProperty onTouchPressedProperty() {
      return this.getEventHandlerProperties().onTouchPressedProperty();
   }

   public final void setOnTouchMoved(EventHandler var1) {
      this.onTouchMovedProperty().set(var1);
   }

   public final EventHandler getOnTouchMoved() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnTouchMoved();
   }

   public final ObjectProperty onTouchMovedProperty() {
      return this.getEventHandlerProperties().onTouchMovedProperty();
   }

   public final void setOnTouchReleased(EventHandler var1) {
      this.onTouchReleasedProperty().set(var1);
   }

   public final EventHandler getOnTouchReleased() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnTouchReleased();
   }

   public final ObjectProperty onTouchReleasedProperty() {
      return this.getEventHandlerProperties().onTouchReleasedProperty();
   }

   public final void setOnTouchStationary(EventHandler var1) {
      this.onTouchStationaryProperty().set(var1);
   }

   public final EventHandler getOnTouchStationary() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnTouchStationary();
   }

   public final ObjectProperty onTouchStationaryProperty() {
      return this.getEventHandlerProperties().onTouchStationaryProperty();
   }

   public final void setOnKeyPressed(EventHandler var1) {
      this.onKeyPressedProperty().set(var1);
   }

   public final EventHandler getOnKeyPressed() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnKeyPressed();
   }

   public final ObjectProperty onKeyPressedProperty() {
      return this.getEventHandlerProperties().onKeyPressedProperty();
   }

   public final void setOnKeyReleased(EventHandler var1) {
      this.onKeyReleasedProperty().set(var1);
   }

   public final EventHandler getOnKeyReleased() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnKeyReleased();
   }

   public final ObjectProperty onKeyReleasedProperty() {
      return this.getEventHandlerProperties().onKeyReleasedProperty();
   }

   public final void setOnKeyTyped(EventHandler var1) {
      this.onKeyTypedProperty().set(var1);
   }

   public final EventHandler getOnKeyTyped() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnKeyTyped();
   }

   public final ObjectProperty onKeyTypedProperty() {
      return this.getEventHandlerProperties().onKeyTypedProperty();
   }

   public final void setOnInputMethodTextChanged(EventHandler var1) {
      this.onInputMethodTextChangedProperty().set(var1);
   }

   public final EventHandler getOnInputMethodTextChanged() {
      return this.eventHandlerProperties == null ? null : this.eventHandlerProperties.getOnInputMethodTextChanged();
   }

   public final ObjectProperty onInputMethodTextChangedProperty() {
      return this.getEventHandlerProperties().onInputMethodTextChangedProperty();
   }

   public final void setInputMethodRequests(InputMethodRequests var1) {
      this.inputMethodRequestsProperty().set(var1);
   }

   public final InputMethodRequests getInputMethodRequests() {
      return this.miscProperties == null ? DEFAULT_INPUT_METHOD_REQUESTS : this.miscProperties.getInputMethodRequests();
   }

   public final ObjectProperty inputMethodRequestsProperty() {
      return this.getMiscProperties().inputMethodRequestsProperty();
   }

   protected final void setFocused(boolean var1) {
      FocusedProperty var2 = this.focusedPropertyImpl();
      if (var2.value != var1) {
         var2.store(var1);
         var2.notifyListeners();
      }

   }

   public final boolean isFocused() {
      return this.focused == null ? false : this.focused.get();
   }

   public final ReadOnlyBooleanProperty focusedProperty() {
      return this.focusedPropertyImpl();
   }

   private FocusedProperty focusedPropertyImpl() {
      if (this.focused == null) {
         this.focused = new FocusedProperty();
      }

      return this.focused;
   }

   public final void setFocusTraversable(boolean var1) {
      this.focusTraversableProperty().set(var1);
   }

   public final boolean isFocusTraversable() {
      return this.focusTraversable == null ? false : this.focusTraversable.get();
   }

   public final BooleanProperty focusTraversableProperty() {
      if (this.focusTraversable == null) {
         this.focusTraversable = new StyleableBooleanProperty(false) {
            public void invalidated() {
               Scene var1 = Node.this.getScene();
               if (var1 != null) {
                  if (this.get()) {
                     var1.initializeInternalEventDispatcher();
                  }

                  Node.this.focusSetDirty(var1);
               }

            }

            public CssMetaData getCssMetaData() {
               return Node.StyleableProperties.FOCUS_TRAVERSABLE;
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "focusTraversable";
            }
         };
      }

      return this.focusTraversable;
   }

   private void focusSetDirty(Scene var1) {
      if (var1 != null && (this == var1.getFocusOwner() || this.isFocusTraversable())) {
         var1.setFocusDirty(true);
      }

   }

   public void requestFocus() {
      if (this.getScene() != null) {
         this.getScene().requestFocus(this);
      }

   }

   /** @deprecated */
   @Deprecated
   public final boolean impl_traverse(Direction var1) {
      return this.getScene() == null ? false : this.getScene().traverse(this, var1);
   }

   public String toString() {
      String var1 = this.getClass().getName();
      String var2 = var1.substring(var1.lastIndexOf(46) + 1);
      StringBuilder var3 = new StringBuilder(var2);
      boolean var4 = this.id != null && !"".equals(this.getId());
      boolean var5 = !this.getStyleClass().isEmpty();
      if (!var4) {
         var3.append('@');
         var3.append(Integer.toHexString(this.hashCode()));
      } else {
         var3.append("[id=");
         var3.append(this.getId());
         if (!var5) {
            var3.append("]");
         }
      }

      if (var5) {
         if (!var4) {
            var3.append('[');
         } else {
            var3.append(", ");
         }

         var3.append("styleClass=");
         var3.append(this.getStyleClass());
         var3.append("]");
      }

      return var3.toString();
   }

   private void preprocessMouseEvent(MouseEvent var1) {
      EventType var2 = var1.getEventType();
      Object var3;
      if (var2 == MouseEvent.MOUSE_PRESSED) {
         for(var3 = this; var3 != null; var3 = ((Node)var3).getParent()) {
            ((Node)var3).setPressed(var1.isPrimaryButtonDown());
         }

      } else if (var2 == MouseEvent.MOUSE_RELEASED) {
         for(var3 = this; var3 != null; var3 = ((Node)var3).getParent()) {
            ((Node)var3).setPressed(var1.isPrimaryButtonDown());
         }

      } else {
         if (var1.getTarget() == this) {
            if (var2 == MouseEvent.MOUSE_ENTERED || var2 == MouseEvent.MOUSE_ENTERED_TARGET) {
               this.setHover(true);
               return;
            }

            if (var2 == MouseEvent.MOUSE_EXITED || var2 == MouseEvent.MOUSE_EXITED_TARGET) {
               this.setHover(false);
               return;
            }
         }

      }
   }

   void markDirtyLayoutBranch() {
      for(Parent var1 = this.getParent(); var1 != null && var1.layoutFlag == LayoutFlags.CLEAN; var1 = var1.getParent()) {
         var1.setLayoutFlag(LayoutFlags.DIRTY_BRANCH);
         if (var1.isSceneRoot()) {
            Toolkit.getToolkit().requestNextPulse();
            if (this.getSubScene() != null) {
               this.getSubScene().setDirtyLayout(var1);
            }
         }
      }

   }

   private void updateTreeVisible(boolean var1) {
      boolean var2 = this.isVisible();
      Object var3 = this.getParent() != null ? this.getParent() : (this.clipParent != null ? this.clipParent : (this.getSubScene() != null ? this.getSubScene() : null));
      if (var2) {
         var2 = var3 == null || ((Node)var3).impl_isTreeVisible();
      }

      if (var1 && var3 != null && ((Node)var3).impl_isTreeVisible() && this.impl_isDirty(DirtyBits.NODE_VISIBLE)) {
         this.addToSceneDirtyList();
      }

      this.setTreeVisible(var2);
   }

   final void setTreeVisible(boolean var1) {
      if (this.treeVisible != var1) {
         this.treeVisible = var1;
         this.updateCanReceiveFocus();
         this.focusSetDirty(this.getScene());
         if (this.getClip() != null) {
            this.getClip().updateTreeVisible(true);
         }

         if (this.treeVisible && !this.impl_isDirtyEmpty()) {
            this.addToSceneDirtyList();
         }

         ((TreeVisiblePropertyReadOnly)this.impl_treeVisibleProperty()).invalidate();
         if (this instanceof SubScene) {
            Parent var2 = ((SubScene)this).getRoot();
            if (var2 != null) {
               var2.setTreeVisible(var1 && var2.isVisible());
            }
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public final boolean impl_isTreeVisible() {
      return this.impl_treeVisibleProperty().get();
   }

   /** @deprecated */
   @Deprecated
   protected final BooleanExpression impl_treeVisibleProperty() {
      if (this.treeVisibleRO == null) {
         this.treeVisibleRO = new TreeVisiblePropertyReadOnly();
      }

      return this.treeVisibleRO;
   }

   private void setCanReceiveFocus(boolean var1) {
      this.canReceiveFocus = var1;
   }

   final boolean isCanReceiveFocus() {
      return this.canReceiveFocus;
   }

   private void updateCanReceiveFocus() {
      this.setCanReceiveFocus(this.getScene() != null && !this.isDisabled() && this.impl_isTreeVisible());
   }

   String indent() {
      String var1 = "";

      for(Parent var2 = this.getParent(); var2 != null; var2 = var2.getParent()) {
         var1 = var1 + "  ";
      }

      return var1;
   }

   /** @deprecated */
   @Deprecated
   public final void impl_setShowMnemonics(boolean var1) {
      this.impl_showMnemonicsProperty().set(var1);
   }

   /** @deprecated */
   @Deprecated
   public final boolean impl_isShowMnemonics() {
      return this.impl_showMnemonics == null ? false : this.impl_showMnemonics.get();
   }

   /** @deprecated */
   @Deprecated
   public final BooleanProperty impl_showMnemonicsProperty() {
      if (this.impl_showMnemonics == null) {
         this.impl_showMnemonics = new BooleanPropertyBase(false) {
            protected void invalidated() {
               Node.this.pseudoClassStateChanged(Node.SHOW_MNEMONICS_PSEUDOCLASS_STATE, this.get());
            }

            public Object getBean() {
               return Node.this;
            }

            public String getName() {
               return "showMnemonics";
            }
         };
      }

      return this.impl_showMnemonics;
   }

   public final void setEventDispatcher(EventDispatcher var1) {
      this.eventDispatcherProperty().set(var1);
   }

   public final EventDispatcher getEventDispatcher() {
      return (EventDispatcher)this.eventDispatcherProperty().get();
   }

   public final ObjectProperty eventDispatcherProperty() {
      this.initializeInternalEventDispatcher();
      return this.eventDispatcher;
   }

   public final void addEventHandler(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().getEventHandlerManager().addEventHandler(var1, var2);
   }

   public final void removeEventHandler(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().getEventHandlerManager().removeEventHandler(var1, var2);
   }

   public final void addEventFilter(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().getEventHandlerManager().addEventFilter(var1, var2);
   }

   public final void removeEventFilter(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().getEventHandlerManager().removeEventFilter(var1, var2);
   }

   protected final void setEventHandler(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().getEventHandlerManager().setEventHandler(var1, var2);
   }

   private NodeEventDispatcher getInternalEventDispatcher() {
      this.initializeInternalEventDispatcher();
      return this.internalEventDispatcher;
   }

   private void initializeInternalEventDispatcher() {
      if (this.internalEventDispatcher == null) {
         this.internalEventDispatcher = this.createInternalEventDispatcher();
         this.eventDispatcher = new SimpleObjectProperty(this, "eventDispatcher", this.internalEventDispatcher);
      }

   }

   private NodeEventDispatcher createInternalEventDispatcher() {
      return new NodeEventDispatcher(this);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      if (this.preprocessMouseEventDispatcher == null) {
         this.preprocessMouseEventDispatcher = (var1x, var2x) -> {
            var1x = var2x.dispatchEvent(var1x);
            if (var1x instanceof MouseEvent) {
               this.preprocessMouseEvent((MouseEvent)var1x);
            }

            return var1x;
         };
      }

      var1 = var1.prepend(this.preprocessMouseEventDispatcher);
      Object var2 = this;

      do {
         if (((Node)var2).eventDispatcher != null) {
            EventDispatcher var3 = (EventDispatcher)((Node)var2).eventDispatcher.get();
            if (var3 != null) {
               var1 = var1.prepend(var3);
            }
         }

         Parent var4 = ((Node)var2).getParent();
         var2 = var4 != null ? var4 : ((Node)var2).getSubScene();
      } while(var2 != null);

      if (this.getScene() != null) {
         var1 = this.getScene().buildEventDispatchChain(var1);
      }

      return var1;
   }

   public final void fireEvent(Event var1) {
      if (var1 instanceof InputEvent) {
         PlatformLogger var2 = Logging.getInputLogger();
         if (var2.isLoggable(Level.FINE)) {
            EventType var3 = var1.getEventType();
            if (var3 != MouseEvent.MOUSE_ENTERED && var3 != MouseEvent.MOUSE_EXITED) {
               if (var3 != MouseEvent.MOUSE_MOVED && var3 != MouseEvent.MOUSE_DRAGGED) {
                  var2.fine(var1.toString());
               } else {
                  var2.finest(var1.toString());
               }
            } else {
               var2.finer(var1.toString());
            }
         }
      }

      Event.fireEvent(this, var1);
   }

   public String getTypeSelector() {
      Class var1 = this.getClass();
      Package var2 = var1.getPackage();
      int var3 = 0;
      if (var2 != null) {
         var3 = var2.getName().length();
      }

      int var4 = var1.getName().length();
      int var5 = 0 < var3 && var3 < var4 ? var3 + 1 : 0;
      return var1.getName().substring(var5);
   }

   public Styleable getStyleableParent() {
      return this.getParent();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   /** @deprecated */
   @Deprecated
   protected Cursor impl_cssGetCursorInitialValue() {
      return null;
   }

   public static List getClassCssMetaData() {
      return Node.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   public static List impl_getMatchingStyles(CssMetaData var0, Styleable var1) {
      return CssStyleHelper.getMatchingStyles(var1, var0);
   }

   /** @deprecated */
   @Deprecated
   public final ObservableMap impl_getStyleMap() {
      ObservableMap var1 = (ObservableMap)this.getProperties().get("STYLEMAP");
      Map var2 = CssStyleHelper.getMatchingStyles((Map)var1, (Node)this);
      if (var2 != null) {
         return var2 instanceof ObservableMap ? (ObservableMap)var2 : FXCollections.observableMap(var2);
      } else {
         return FXCollections.emptyObservableMap();
      }
   }

   /** @deprecated */
   @Deprecated
   public final void impl_setStyleMap(ObservableMap var1) {
      if (var1 != null) {
         this.getProperties().put("STYLEMAP", var1);
      } else {
         this.getProperties().remove("STYLEMAP");
      }

   }

   /** @deprecated */
   @Deprecated
   public Map impl_findStyles(Map var1) {
      Map var2 = CssStyleHelper.getMatchingStyles(var1, this);
      return var2 != null ? var2 : Collections.emptyMap();
   }

   final CssFlags getCSSFlags() {
      return this.cssFlag;
   }

   private void requestCssStateTransition() {
      if (this.getScene() != null) {
         if (this.cssFlag == CssFlags.CLEAN || this.cssFlag == CssFlags.DIRTY_BRANCH) {
            this.cssFlag = CssFlags.UPDATE;
            this.notifyParentsOfInvalidatedCSS();
         }

      }
   }

   public final void pseudoClassStateChanged(PseudoClass var1, boolean var2) {
      boolean var3 = var2 ? this.pseudoClassStates.add(var1) : this.pseudoClassStates.remove(var1);
      if (var3 && this.styleHelper != null) {
         boolean var4 = this.styleHelper.pseudoClassStateChanged(var1);
         if (var4) {
            this.requestCssStateTransition();
         }
      }

   }

   public final ObservableSet getPseudoClassStates() {
      return FXCollections.unmodifiableObservableSet(this.pseudoClassStates);
   }

   final void notifyParentsOfInvalidatedCSS() {
      SubScene var1 = this.getSubScene();
      Parent var2 = var1 != null ? var1.getRoot() : this.getScene().getRoot();
      if (!var2.impl_isDirty(DirtyBits.NODE_CSS)) {
         var2.impl_markDirty(DirtyBits.NODE_CSS);
         if (var1 != null) {
            var1.cssFlag = CssFlags.UPDATE;
            var1.notifyParentsOfInvalidatedCSS();
         }
      }

      Parent var3 = this.getParent();

      while(var3 != null) {
         if (var3.cssFlag == CssFlags.CLEAN) {
            var3.cssFlag = CssFlags.DIRTY_BRANCH;
            var3 = var3.getParent();
         } else {
            var3 = null;
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public final void impl_reapplyCSS() {
      if (this.getScene() != null) {
         if (this.cssFlag != CssFlags.REAPPLY) {
            if (this.cssFlag == CssFlags.DIRTY_BRANCH) {
               this.cssFlag = CssFlags.REAPPLY;
            } else if (this.cssFlag == CssFlags.UPDATE) {
               this.cssFlag = CssFlags.REAPPLY;
               this.notifyParentsOfInvalidatedCSS();
            } else {
               this.reapplyCss();
               if (this.getParent() != null && this.getParent().performingLayout) {
                  this.impl_processCSS((WritableValue)null);
               } else {
                  this.notifyParentsOfInvalidatedCSS();
               }

            }
         }
      }
   }

   private void reapplyCss() {
      CssStyleHelper var1 = this.styleHelper;
      this.cssFlag = CssFlags.REAPPLY;
      this.styleHelper = CssStyleHelper.createStyleHelper(this);
      if (this instanceof Parent) {
         boolean var2 = this.styleHelper == null || var1 != this.styleHelper || this.getParent() == null || this.getParent().cssFlag != CssFlags.CLEAN;
         if (var2) {
            ObservableList var3 = ((Parent)this).getChildren();
            int var4 = 0;

            for(int var5 = var3.size(); var4 < var5; ++var4) {
               Node var6 = (Node)var3.get(var4);
               var6.reapplyCss();
            }
         }
      } else if (this instanceof SubScene) {
         Parent var7 = ((SubScene)this).getRoot();
         if (var7 != null) {
            var7.reapplyCss();
         }
      } else if (this.styleHelper == null) {
         this.cssFlag = CssFlags.CLEAN;
         return;
      }

      this.cssFlag = CssFlags.UPDATE;
   }

   void processCSS() {
      switch (this.cssFlag) {
         case CLEAN:
            break;
         case DIRTY_BRANCH:
            Parent var1 = (Parent)this;
            var1.cssFlag = CssFlags.CLEAN;
            ObservableList var2 = var1.getChildren();
            int var3 = 0;

            for(int var4 = var2.size(); var3 < var4; ++var3) {
               ((Node)var2.get(var3)).processCSS();
            }

            return;
         case REAPPLY:
         case UPDATE:
         default:
            this.impl_processCSS((WritableValue)null);
      }

   }

   /** @deprecated */
   @Deprecated
   public final void impl_processCSS(boolean var1) {
      this.applyCss();
   }

   public final void applyCss() {
      if (this.getScene() != null) {
         if (this.cssFlag != CssFlags.REAPPLY) {
            this.cssFlag = CssFlags.UPDATE;
         }

         Object var1 = this;
         boolean var2 = this.getScene().getRoot().impl_isDirty(DirtyBits.NODE_CSS);
         if (var2) {
            for(Parent var3 = this.getParent(); var3 != null; var3 = var3.getParent()) {
               if (var3.cssFlag == CssFlags.UPDATE || var3.cssFlag == CssFlags.REAPPLY) {
                  var1 = var3;
               }
            }

            if (var1 == this.getScene().getRoot()) {
               this.getScene().getRoot().impl_clearDirty(DirtyBits.NODE_CSS);
            }
         }

         ((Node)var1).processCSS();
      }
   }

   /** @deprecated */
   @Deprecated
   protected void impl_processCSS(WritableValue var1) {
      if (this.cssFlag != CssFlags.CLEAN) {
         if (this.cssFlag == CssFlags.REAPPLY) {
            this.reapplyCss();
         }

         this.cssFlag = CssFlags.CLEAN;
         if (this.styleHelper != null && this.getScene() != null) {
            this.styleHelper.transitionToState(this);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public abstract Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2);

   public final void setAccessibleRole(AccessibleRole var1) {
      if (var1 == null) {
         var1 = AccessibleRole.NODE;
      }

      this.accessibleRoleProperty().set(var1);
   }

   public final AccessibleRole getAccessibleRole() {
      return this.accessibleRole == null ? AccessibleRole.NODE : (AccessibleRole)this.accessibleRoleProperty().get();
   }

   public final ObjectProperty accessibleRoleProperty() {
      if (this.accessibleRole == null) {
         this.accessibleRole = new SimpleObjectProperty(this, "accessibleRole", AccessibleRole.NODE);
      }

      return this.accessibleRole;
   }

   public final void setAccessibleRoleDescription(String var1) {
      this.accessibleRoleDescriptionProperty().set(var1);
   }

   public final String getAccessibleRoleDescription() {
      if (this.accessibilityProperties == null) {
         return null;
      } else {
         return this.accessibilityProperties.accessibleRoleDescription == null ? null : (String)this.accessibleRoleDescriptionProperty().get();
      }
   }

   public final ObjectProperty accessibleRoleDescriptionProperty() {
      return this.getAccessibilityProperties().getAccessibleRoleDescription();
   }

   public final void setAccessibleText(String var1) {
      this.accessibleTextProperty().set(var1);
   }

   public final String getAccessibleText() {
      if (this.accessibilityProperties == null) {
         return null;
      } else {
         return this.accessibilityProperties.accessibleText == null ? null : (String)this.accessibleTextProperty().get();
      }
   }

   public final ObjectProperty accessibleTextProperty() {
      return this.getAccessibilityProperties().getAccessibleText();
   }

   public final void setAccessibleHelp(String var1) {
      this.accessibleHelpProperty().set(var1);
   }

   public final String getAccessibleHelp() {
      if (this.accessibilityProperties == null) {
         return null;
      } else {
         return this.accessibilityProperties.accessibleHelp == null ? null : (String)this.accessibleHelpProperty().get();
      }
   }

   public final ObjectProperty accessibleHelpProperty() {
      return this.getAccessibilityProperties().getAccessibleHelp();
   }

   private AccessibilityProperties getAccessibilityProperties() {
      if (this.accessibilityProperties == null) {
         this.accessibilityProperties = new AccessibilityProperties();
      }

      return this.accessibilityProperties;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case ROLE:
            return this.getAccessibleRole();
         case ROLE_DESCRIPTION:
            return this.getAccessibleRoleDescription();
         case TEXT:
            return this.getAccessibleText();
         case HELP:
            return this.getAccessibleHelp();
         case PARENT:
            return this.getParent();
         case SCENE:
            return this.getScene();
         case BOUNDS:
            return this.localToScreen(this.getBoundsInLocal());
         case DISABLED:
            return this.isDisabled();
         case FOCUSED:
            return this.isFocused();
         case VISIBLE:
            return this.isVisible();
         case LABELED_BY:
            return this.labeledBy;
         default:
            return null;
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case REQUEST_FOCUS:
            if (this.isFocusTraversable()) {
               this.requestFocus();
            }
            break;
         case SHOW_MENU:
            Bounds var3 = this.getBoundsInLocal();
            Point2D var4 = this.localToScreen(var3.getMaxX(), var3.getMaxY());
            ContextMenuEvent var5 = new ContextMenuEvent(ContextMenuEvent.CONTEXT_MENU_REQUESTED, var3.getMaxX(), var3.getMaxY(), var4.getX(), var4.getY(), false, new PickResult(this, var3.getMaxX(), var3.getMaxY()));
            Event.fireEvent(this, var5);
      }

   }

   public final void notifyAccessibleAttributeChanged(AccessibleAttribute var1) {
      if (this.accessible == null) {
         Scene var2 = this.getScene();
         if (var2 != null) {
            this.accessible = var2.removeAccessible(this);
         }
      }

      if (this.accessible != null) {
         this.accessible.sendNotification(var1);
      }

   }

   Accessible getAccessible() {
      if (this.accessible == null) {
         Scene var1 = this.getScene();
         if (var1 != null) {
            this.accessible = var1.removeAccessible(this);
         }
      }

      if (this.accessible == null) {
         this.accessible = Application.GetApplication().createAccessible();
         this.accessible.setEventHandler(new Accessible.EventHandler() {
            public AccessControlContext getAccessControlContext() {
               Scene var1 = Node.this.getScene();
               if (var1 == null) {
                  throw new RuntimeException("Accessbility requested for node not on a scene");
               } else {
                  return var1.impl_getPeer() != null ? var1.impl_getPeer().getAccessControlContext() : var1.acc;
               }
            }

            public Object getAttribute(AccessibleAttribute var1, Object... var2) {
               return Node.this.queryAccessibleAttribute(var1, var2);
            }

            public void executeAction(AccessibleAction var1, Object... var2) {
               Node.this.executeAccessibleAction(var1, var2);
            }

            public String toString() {
               String var1 = Node.this.getClass().getName();
               return var1.substring(var1.lastIndexOf(46) + 1);
            }
         });
      }

      return this.accessible;
   }

   void releaseAccessible() {
      Accessible var1 = this.accessible;
      if (var1 != null) {
         this.accessible = null;
         var1.dispose();
      }

   }

   static {
      PerformanceTracker.logEvent("Node class loaded");
      USER_DATA_KEY = new Object();
      DEFAULT_ROTATION_AXIS = Rotate.Z_AXIS;
      DEFAULT_CACHE_HINT = CacheHint.DEFAULT;
      DEFAULT_CLIP = null;
      DEFAULT_CURSOR = null;
      DEFAULT_DEPTH_TEST = DepthTest.INHERIT;
      DEFAULT_EFFECT = null;
      DEFAULT_INPUT_METHOD_REQUESTS = null;
      HOVER_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("hover");
      PRESSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("pressed");
      DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");
      FOCUSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("focused");
      SHOW_MNEMONICS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("show-mnemonics");
      boundsAccessor = (var0, var1, var2) -> {
         return var2.getGeomBounds(var0, var1);
      };
      NodeHelper.setNodeAccessor(new NodeHelper.NodeAccessor() {
         public void layoutNodeForPrinting(Node var1) {
            var1.doCSSLayoutSyncForSnapshot();
         }

         public boolean isDerivedDepthTest(Node var1) {
            return var1.isDerivedDepthTest();
         }

         public SubScene getSubScene(Node var1) {
            return var1.getSubScene();
         }

         public void setLabeledBy(Node var1, Node var2) {
            var1.labeledBy = var2;
         }

         public Accessible getAccessible(Node var1) {
            return var1.getAccessible();
         }
      });
   }

   private class AccessibilityProperties {
      ObjectProperty accessibleRoleDescription;
      ObjectProperty accessibleText;
      ObjectProperty accessibleHelp;

      private AccessibilityProperties() {
      }

      ObjectProperty getAccessibleRoleDescription() {
         if (this.accessibleRoleDescription == null) {
            this.accessibleRoleDescription = new SimpleObjectProperty(Node.this, "accessibleRoleDescription", (Object)null);
         }

         return this.accessibleRoleDescription;
      }

      ObjectProperty getAccessibleText() {
         if (this.accessibleText == null) {
            this.accessibleText = new SimpleObjectProperty(Node.this, "accessibleText", (Object)null);
         }

         return this.accessibleText;
      }

      ObjectProperty getAccessibleHelp() {
         if (this.accessibleHelp == null) {
            this.accessibleHelp = new SimpleObjectProperty(Node.this, "accessibleHelp", (Object)null);
         }

         return this.accessibleHelp;
      }

      // $FF: synthetic method
      AccessibilityProperties(Object var2) {
         this();
      }
   }

   private abstract static class LazyBoundsProperty extends ReadOnlyObjectProperty {
      private ExpressionHelper helper;
      private boolean valid;
      private Bounds bounds;

      private LazyBoundsProperty() {
      }

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public Bounds get() {
         if (!this.valid) {
            this.bounds = this.computeBounds();
            this.valid = true;
         }

         return this.bounds;
      }

      public void invalidate() {
         if (this.valid) {
            this.valid = false;
            ExpressionHelper.fireValueChangedEvent(this.helper);
         }

      }

      protected abstract Bounds computeBounds();

      // $FF: synthetic method
      LazyBoundsProperty(Object var1) {
         this();
      }
   }

   private abstract static class LazyTransformProperty extends ReadOnlyObjectProperty {
      protected static final int VALID = 0;
      protected static final int INVALID = 1;
      protected static final int VALIDITY_UNKNOWN = 2;
      protected int valid;
      private ExpressionHelper helper;
      private Transform transform;
      private boolean canReuse;

      private LazyTransformProperty() {
         this.valid = 1;
         this.canReuse = false;
      }

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      protected Transform getInternalValue() {
         if (this.valid == 1 || this.valid == 2 && this.computeValidity() == 1) {
            this.transform = this.computeTransform(this.canReuse ? this.transform : null);
            this.canReuse = true;
            this.valid = this.validityKnown() ? 0 : 2;
         }

         return this.transform;
      }

      public Transform get() {
         this.transform = this.getInternalValue();
         this.canReuse = false;
         return this.transform;
      }

      public void validityUnknown() {
         if (this.valid == 0) {
            this.valid = 2;
         }

      }

      public void invalidate() {
         if (this.valid != 1) {
            this.valid = 1;
            ExpressionHelper.fireValueChangedEvent(this.helper);
         }

      }

      protected abstract boolean validityKnown();

      protected abstract int computeValidity();

      protected abstract Transform computeTransform(Transform var1);

      // $FF: synthetic method
      LazyTransformProperty(Object var1) {
         this();
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData CURSOR = new CssMetaData("-fx-cursor", CursorConverter.getInstance()) {
         public boolean isSettable(Node var1) {
            return var1.miscProperties == null || var1.miscProperties.canSetCursor();
         }

         public StyleableProperty getStyleableProperty(Node var1) {
            return (StyleableProperty)var1.cursorProperty();
         }

         public Cursor getInitialValue(Node var1) {
            return var1.impl_cssGetCursorInitialValue();
         }
      };
      private static final CssMetaData EFFECT = new CssMetaData("-fx-effect", EffectConverter.getInstance()) {
         public boolean isSettable(Node var1) {
            return var1.miscProperties == null || var1.miscProperties.canSetEffect();
         }

         public StyleableProperty getStyleableProperty(Node var1) {
            return (StyleableProperty)var1.effectProperty();
         }
      };
      private static final CssMetaData FOCUS_TRAVERSABLE;
      private static final CssMetaData OPACITY;
      private static final CssMetaData BLEND_MODE;
      private static final CssMetaData ROTATE;
      private static final CssMetaData SCALE_X;
      private static final CssMetaData SCALE_Y;
      private static final CssMetaData SCALE_Z;
      private static final CssMetaData TRANSLATE_X;
      private static final CssMetaData TRANSLATE_Y;
      private static final CssMetaData TRANSLATE_Z;
      private static final CssMetaData VISIBILITY;
      private static final List STYLEABLES;

      static {
         FOCUS_TRAVERSABLE = new CssMetaData("-fx-focus-traversable", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Node var1) {
               return var1.focusTraversable == null || !var1.focusTraversable.isBound();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.focusTraversableProperty();
            }

            public Boolean getInitialValue(Node var1) {
               return var1.impl_cssGetFocusTraversableInitialValue();
            }
         };
         OPACITY = new CssMetaData("-fx-opacity", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(Node var1) {
               return var1.opacity == null || !var1.opacity.isBound();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.opacityProperty();
            }
         };
         BLEND_MODE = new CssMetaData("-fx-blend-mode", new EnumConverter(BlendMode.class)) {
            public boolean isSettable(Node var1) {
               return var1.blendMode == null || !var1.blendMode.isBound();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.blendModeProperty();
            }
         };
         ROTATE = new CssMetaData("-fx-rotate", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.rotate == null || var1.nodeTransformation.canSetRotate();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.rotateProperty();
            }
         };
         SCALE_X = new CssMetaData("-fx-scale-x", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.scaleX == null || var1.nodeTransformation.canSetScaleX();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.scaleXProperty();
            }
         };
         SCALE_Y = new CssMetaData("-fx-scale-y", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.scaleY == null || var1.nodeTransformation.canSetScaleY();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.scaleYProperty();
            }
         };
         SCALE_Z = new CssMetaData("-fx-scale-z", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.scaleZ == null || var1.nodeTransformation.canSetScaleZ();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.scaleZProperty();
            }
         };
         TRANSLATE_X = new CssMetaData("-fx-translate-x", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.translateX == null || var1.nodeTransformation.canSetTranslateX();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.translateXProperty();
            }
         };
         TRANSLATE_Y = new CssMetaData("-fx-translate-y", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.translateY == null || var1.nodeTransformation.canSetTranslateY();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.translateYProperty();
            }
         };
         TRANSLATE_Z = new CssMetaData("-fx-translate-z", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(Node var1) {
               return var1.nodeTransformation == null || var1.nodeTransformation.translateZ == null || var1.nodeTransformation.canSetTranslateZ();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.translateZProperty();
            }
         };
         VISIBILITY = new CssMetaData("visibility", new StyleConverter() {
            public Boolean convert(ParsedValue var1, Font var2) {
               String var3 = var1 != null ? (String)var1.getValue() : null;
               return "visible".equalsIgnoreCase(var3);
            }
         }, Boolean.TRUE) {
            public boolean isSettable(Node var1) {
               return var1.visible == null || !var1.visible.isBound();
            }

            public StyleableProperty getStyleableProperty(Node var1) {
               return (StyleableProperty)var1.visibleProperty();
            }
         };
         ArrayList var0 = new ArrayList();
         var0.add(CURSOR);
         var0.add(EFFECT);
         var0.add(FOCUS_TRAVERSABLE);
         var0.add(OPACITY);
         var0.add(BLEND_MODE);
         var0.add(ROTATE);
         var0.add(SCALE_X);
         var0.add(SCALE_Y);
         var0.add(SCALE_Z);
         var0.add(TRANSLATE_X);
         var0.add(TRANSLATE_Y);
         var0.add(TRANSLATE_Z);
         var0.add(VISIBILITY);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   class TreeVisiblePropertyReadOnly extends BooleanExpression {
      private ExpressionHelper helper;
      private boolean valid;

      public void addListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      public void addListener(ChangeListener var1) {
         this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
      }

      public void removeListener(ChangeListener var1) {
         this.helper = ExpressionHelper.removeListener(this.helper, var1);
      }

      protected void invalidate() {
         if (this.valid) {
            this.valid = false;
            ExpressionHelper.fireValueChangedEvent(this.helper);
         }

      }

      public boolean get() {
         this.valid = true;
         return Node.this.treeVisible;
      }
   }

   final class FocusedProperty extends ReadOnlyBooleanPropertyBase {
      private boolean value;
      private boolean valid = true;
      private boolean needsChangeEvent = false;

      public void store(boolean var1) {
         if (var1 != this.value) {
            this.value = var1;
            this.markInvalid();
         }

      }

      public void notifyListeners() {
         if (this.needsChangeEvent) {
            this.fireValueChangedEvent();
            this.needsChangeEvent = false;
         }

      }

      private void markInvalid() {
         if (this.valid) {
            this.valid = false;
            Node.this.pseudoClassStateChanged(Node.FOCUSED_PSEUDOCLASS_STATE, this.get());
            PlatformLogger var1 = Logging.getFocusLogger();
            if (var1.isLoggable(Level.FINE)) {
               var1.fine(this + " focused=" + this.get());
            }

            this.needsChangeEvent = true;
            Node.this.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUSED);
         }

      }

      public boolean get() {
         this.valid = true;
         return this.value;
      }

      public Object getBean() {
         return Node.this;
      }

      public String getName() {
         return "focused";
      }
   }

   private final class MiscProperties {
      private LazyBoundsProperty boundsInParent;
      private LazyBoundsProperty boundsInLocal;
      private BooleanProperty cache;
      private ObjectProperty cacheHint;
      private ObjectProperty clip;
      private ObjectProperty cursor;
      private ObjectProperty depthTest;
      private BooleanProperty disable;
      private ObjectProperty effect;
      private ObjectProperty inputMethodRequests;
      private BooleanProperty mouseTransparent;

      private MiscProperties() {
      }

      public final Bounds getBoundsInParent() {
         return (Bounds)this.boundsInParentProperty().get();
      }

      public final ReadOnlyObjectProperty boundsInParentProperty() {
         if (this.boundsInParent == null) {
            this.boundsInParent = new LazyBoundsProperty() {
               protected Bounds computeBounds() {
                  BaseBounds var1 = TempState.getInstance().bounds;
                  var1 = Node.this.getTransformedBounds(var1, BaseTransform.IDENTITY_TRANSFORM);
                  return new BoundingBox((double)var1.getMinX(), (double)var1.getMinY(), (double)var1.getMinZ(), (double)var1.getWidth(), (double)var1.getHeight(), (double)var1.getDepth());
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "boundsInParent";
               }
            };
         }

         return this.boundsInParent;
      }

      public void invalidateBoundsInParent() {
         if (this.boundsInParent != null) {
            this.boundsInParent.invalidate();
         }

      }

      public final Bounds getBoundsInLocal() {
         return (Bounds)this.boundsInLocalProperty().get();
      }

      public final ReadOnlyObjectProperty boundsInLocalProperty() {
         if (this.boundsInLocal == null) {
            this.boundsInLocal = new LazyBoundsProperty() {
               protected Bounds computeBounds() {
                  BaseBounds var1 = TempState.getInstance().bounds;
                  var1 = Node.this.getLocalBounds(var1, BaseTransform.IDENTITY_TRANSFORM);
                  return new BoundingBox((double)var1.getMinX(), (double)var1.getMinY(), (double)var1.getMinZ(), (double)var1.getWidth(), (double)var1.getHeight(), (double)var1.getDepth());
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "boundsInLocal";
               }
            };
         }

         return this.boundsInLocal;
      }

      public void invalidateBoundsInLocal() {
         if (this.boundsInLocal != null) {
            this.boundsInLocal.invalidate();
         }

      }

      public final boolean isCache() {
         return this.cache == null ? false : this.cache.get();
      }

      public final BooleanProperty cacheProperty() {
         if (this.cache == null) {
            this.cache = new BooleanPropertyBase(false) {
               protected void invalidated() {
                  Node.this.impl_markDirty(DirtyBits.NODE_CACHE);
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "cache";
               }
            };
         }

         return this.cache;
      }

      public final CacheHint getCacheHint() {
         return this.cacheHint == null ? Node.DEFAULT_CACHE_HINT : (CacheHint)this.cacheHint.get();
      }

      public final ObjectProperty cacheHintProperty() {
         if (this.cacheHint == null) {
            this.cacheHint = new ObjectPropertyBase(Node.DEFAULT_CACHE_HINT) {
               protected void invalidated() {
                  Node.this.impl_markDirty(DirtyBits.NODE_CACHE);
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "cacheHint";
               }
            };
         }

         return this.cacheHint;
      }

      public final Node getClip() {
         return this.clip == null ? Node.DEFAULT_CLIP : (Node)this.clip.get();
      }

      public final ObjectProperty clipProperty() {
         if (this.clip == null) {
            this.clip = new ObjectPropertyBase(Node.DEFAULT_CLIP) {
               private Node oldClip;

               protected void invalidated() {
                  Node var1 = (Node)this.get();
                  if (var1 != null && (var1.isConnected() && var1.clipParent != Node.this || Node.this.wouldCreateCycle(Node.this, var1))) {
                     String var2 = var1.isConnected() && var1.clipParent != Node.this ? "node already connected" : "cycle detected";
                     if (this.isBound()) {
                        this.unbind();
                        this.set(this.oldClip);
                        throw new IllegalArgumentException("Node's clip set to incorrect value  through binding (" + var2 + ", node  = " + Node.this + ", clip = " + MiscProperties.this.clip + "). Binding has been removed.");
                     } else {
                        this.set(this.oldClip);
                        throw new IllegalArgumentException("Node's clip set to incorrect value (" + var2 + ", node  = " + Node.this + ", clip = " + MiscProperties.this.clip + ").");
                     }
                  } else {
                     if (this.oldClip != null) {
                        this.oldClip.clipParent = null;
                        this.oldClip.setScenes((Scene)null, (SubScene)null);
                        this.oldClip.updateTreeVisible(false);
                     }

                     if (var1 != null) {
                        var1.clipParent = Node.this;
                        var1.setScenes(Node.this.getScene(), Node.this.getSubScene());
                        var1.updateTreeVisible(true);
                     }

                     Node.this.impl_markDirty(DirtyBits.NODE_CLIP);
                     Node.this.localBoundsChanged();
                     this.oldClip = var1;
                  }
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "clip";
               }
            };
         }

         return this.clip;
      }

      public final Cursor getCursor() {
         return this.cursor == null ? Node.DEFAULT_CURSOR : (Cursor)this.cursor.get();
      }

      public final ObjectProperty cursorProperty() {
         if (this.cursor == null) {
            this.cursor = new StyleableObjectProperty(Node.DEFAULT_CURSOR) {
               protected void invalidated() {
                  Scene var1 = Node.this.getScene();
                  if (var1 != null) {
                     var1.markCursorDirty();
                  }

               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.CURSOR;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "cursor";
               }
            };
         }

         return this.cursor;
      }

      public final DepthTest getDepthTest() {
         return this.depthTest == null ? Node.DEFAULT_DEPTH_TEST : (DepthTest)this.depthTest.get();
      }

      public final ObjectProperty depthTestProperty() {
         if (this.depthTest == null) {
            this.depthTest = new ObjectPropertyBase(Node.DEFAULT_DEPTH_TEST) {
               protected void invalidated() {
                  Node.this.computeDerivedDepthTest();
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "depthTest";
               }
            };
         }

         return this.depthTest;
      }

      public final boolean isDisable() {
         return this.disable == null ? false : this.disable.get();
      }

      public final BooleanProperty disableProperty() {
         if (this.disable == null) {
            this.disable = new BooleanPropertyBase(false) {
               protected void invalidated() {
                  Node.this.updateDisabled();
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "disable";
               }
            };
         }

         return this.disable;
      }

      public final Effect getEffect() {
         return this.effect == null ? Node.DEFAULT_EFFECT : (Effect)this.effect.get();
      }

      public final ObjectProperty effectProperty() {
         if (this.effect == null) {
            this.effect = new StyleableObjectProperty(Node.DEFAULT_EFFECT) {
               private Effect oldEffect = null;
               private int oldBits;
               private final AbstractNotifyListener effectChangeListener = new AbstractNotifyListener() {
                  public void invalidated(Observable var1) {
                     int var2 = ((IntegerProperty)var1).get();
                     int var3 = var2 ^ oldBits;
                     oldBits = var2;
                     if (EffectDirtyBits.isSet(var3, EffectDirtyBits.EFFECT_DIRTY) && EffectDirtyBits.isSet(var2, EffectDirtyBits.EFFECT_DIRTY)) {
                        Node.this.impl_markDirty(DirtyBits.EFFECT_EFFECT);
                     }

                     if (EffectDirtyBits.isSet(var3, EffectDirtyBits.BOUNDS_CHANGED)) {
                        Node.this.localBoundsChanged();
                     }

                  }
               };

               protected void invalidated() {
                  Effect var1 = (Effect)this.get();
                  if (this.oldEffect != null) {
                     this.oldEffect.impl_effectDirtyProperty().removeListener(this.effectChangeListener.getWeakListener());
                  }

                  this.oldEffect = var1;
                  if (var1 != null) {
                     var1.impl_effectDirtyProperty().addListener(this.effectChangeListener.getWeakListener());
                     if (var1.impl_isEffectDirty()) {
                        Node.this.impl_markDirty(DirtyBits.EFFECT_EFFECT);
                     }

                     this.oldBits = var1.impl_effectDirtyProperty().get();
                  }

                  Node.this.impl_markDirty(DirtyBits.NODE_EFFECT);
                  Node.this.localBoundsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.EFFECT;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "effect";
               }
            };
         }

         return this.effect;
      }

      public final InputMethodRequests getInputMethodRequests() {
         return this.inputMethodRequests == null ? Node.DEFAULT_INPUT_METHOD_REQUESTS : (InputMethodRequests)this.inputMethodRequests.get();
      }

      public ObjectProperty inputMethodRequestsProperty() {
         if (this.inputMethodRequests == null) {
            this.inputMethodRequests = new SimpleObjectProperty(Node.this, "inputMethodRequests", Node.DEFAULT_INPUT_METHOD_REQUESTS);
         }

         return this.inputMethodRequests;
      }

      public final boolean isMouseTransparent() {
         return this.mouseTransparent == null ? false : this.mouseTransparent.get();
      }

      public final BooleanProperty mouseTransparentProperty() {
         if (this.mouseTransparent == null) {
            this.mouseTransparent = new SimpleBooleanProperty(Node.this, "mouseTransparent", false);
         }

         return this.mouseTransparent;
      }

      public boolean canSetCursor() {
         return this.cursor == null || !this.cursor.isBound();
      }

      public boolean canSetEffect() {
         return this.effect == null || !this.effect.isBound();
      }

      // $FF: synthetic method
      MiscProperties(Object var2) {
         this();
      }
   }

   private final class EffectiveOrientationProperty extends ReadOnlyObjectPropertyBase {
      private EffectiveOrientationProperty() {
      }

      public NodeOrientation get() {
         return Node.this.getEffectiveNodeOrientation();
      }

      public Object getBean() {
         return Node.this;
      }

      public String getName() {
         return "effectiveNodeOrientation";
      }

      public void invalidate() {
         this.fireValueChangedEvent();
      }

      // $FF: synthetic method
      EffectiveOrientationProperty(Object var2) {
         this();
      }
   }

   private final class NodeTransformation {
      private DoubleProperty translateX;
      private DoubleProperty translateY;
      private DoubleProperty translateZ;
      private DoubleProperty scaleX;
      private DoubleProperty scaleY;
      private DoubleProperty scaleZ;
      private DoubleProperty rotate;
      private ObjectProperty rotationAxis;
      private ObservableList transforms;
      private LazyTransformProperty localToParentTransform;
      private LazyTransformProperty localToSceneTransform;
      private int listenerReasons;
      private InvalidationListener localToSceneInvLstnr;

      private NodeTransformation() {
         this.listenerReasons = 0;
      }

      private InvalidationListener getLocalToSceneInvalidationListener() {
         if (this.localToSceneInvLstnr == null) {
            this.localToSceneInvLstnr = (var1) -> {
               this.invalidateLocalToSceneTransform();
            };
         }

         return this.localToSceneInvLstnr;
      }

      public void incListenerReasons() {
         if (this.listenerReasons == 0) {
            Parent var1 = Node.this.getParent();
            if (var1 != null) {
               var1.localToSceneTransformProperty().addListener(this.getLocalToSceneInvalidationListener());
            }
         }

         ++this.listenerReasons;
      }

      public void decListenerReasons() {
         --this.listenerReasons;
         if (this.listenerReasons == 0) {
            Parent var1 = Node.this.getParent();
            if (var1 != null) {
               var1.localToSceneTransformProperty().removeListener(this.getLocalToSceneInvalidationListener());
            }

            if (this.localToSceneTransform != null) {
               this.localToSceneTransform.validityUnknown();
            }
         }

      }

      public final Transform getLocalToParentTransform() {
         return (Transform)this.localToParentTransformProperty().get();
      }

      public final ReadOnlyObjectProperty localToParentTransformProperty() {
         if (this.localToParentTransform == null) {
            this.localToParentTransform = new LazyTransformProperty() {
               protected Transform computeTransform(Transform var1) {
                  Node.this.updateLocalToParentTransform();
                  return TransformUtils.immutableTransform(var1, Node.this.localToParentTx.getMxx(), Node.this.localToParentTx.getMxy(), Node.this.localToParentTx.getMxz(), Node.this.localToParentTx.getMxt(), Node.this.localToParentTx.getMyx(), Node.this.localToParentTx.getMyy(), Node.this.localToParentTx.getMyz(), Node.this.localToParentTx.getMyt(), Node.this.localToParentTx.getMzx(), Node.this.localToParentTx.getMzy(), Node.this.localToParentTx.getMzz(), Node.this.localToParentTx.getMzt());
               }

               protected boolean validityKnown() {
                  return true;
               }

               protected int computeValidity() {
                  return this.valid;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "localToParentTransform";
               }
            };
         }

         return this.localToParentTransform;
      }

      public void invalidateLocalToParentTransform() {
         if (this.localToParentTransform != null) {
            this.localToParentTransform.invalidate();
         }

      }

      public final Transform getLocalToSceneTransform() {
         return (Transform)this.localToSceneTransformProperty().get();
      }

      public final ReadOnlyObjectProperty localToSceneTransformProperty() {
         if (this.localToSceneTransform == null) {
            this.localToSceneTransform = new LocalToSceneTransformProperty();
         }

         return this.localToSceneTransform;
      }

      public void invalidateLocalToSceneTransform() {
         if (this.localToSceneTransform != null) {
            this.localToSceneTransform.invalidate();
         }

      }

      public double getTranslateX() {
         return this.translateX == null ? 0.0 : this.translateX.get();
      }

      public final DoubleProperty translateXProperty() {
         if (this.translateX == null) {
            this.translateX = new StyleableDoubleProperty(0.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.TRANSLATE_X;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "translateX";
               }
            };
         }

         return this.translateX;
      }

      public double getTranslateY() {
         return this.translateY == null ? 0.0 : this.translateY.get();
      }

      public final DoubleProperty translateYProperty() {
         if (this.translateY == null) {
            this.translateY = new StyleableDoubleProperty(0.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.TRANSLATE_Y;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "translateY";
               }
            };
         }

         return this.translateY;
      }

      public double getTranslateZ() {
         return this.translateZ == null ? 0.0 : this.translateZ.get();
      }

      public final DoubleProperty translateZProperty() {
         if (this.translateZ == null) {
            this.translateZ = new StyleableDoubleProperty(0.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.TRANSLATE_Z;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "translateZ";
               }
            };
         }

         return this.translateZ;
      }

      public double getScaleX() {
         return this.scaleX == null ? 1.0 : this.scaleX.get();
      }

      public final DoubleProperty scaleXProperty() {
         if (this.scaleX == null) {
            this.scaleX = new StyleableDoubleProperty(1.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.SCALE_X;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "scaleX";
               }
            };
         }

         return this.scaleX;
      }

      public double getScaleY() {
         return this.scaleY == null ? 1.0 : this.scaleY.get();
      }

      public final DoubleProperty scaleYProperty() {
         if (this.scaleY == null) {
            this.scaleY = new StyleableDoubleProperty(1.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.SCALE_Y;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "scaleY";
               }
            };
         }

         return this.scaleY;
      }

      public double getScaleZ() {
         return this.scaleZ == null ? 1.0 : this.scaleZ.get();
      }

      public final DoubleProperty scaleZProperty() {
         if (this.scaleZ == null) {
            this.scaleZ = new StyleableDoubleProperty(1.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.SCALE_Z;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "scaleZ";
               }
            };
         }

         return this.scaleZ;
      }

      public double getRotate() {
         return this.rotate == null ? 0.0 : this.rotate.get();
      }

      public final DoubleProperty rotateProperty() {
         if (this.rotate == null) {
            this.rotate = new StyleableDoubleProperty(0.0) {
               public void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public CssMetaData getCssMetaData() {
                  return Node.StyleableProperties.ROTATE;
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "rotate";
               }
            };
         }

         return this.rotate;
      }

      public Point3D getRotationAxis() {
         return this.rotationAxis == null ? Node.DEFAULT_ROTATION_AXIS : (Point3D)this.rotationAxis.get();
      }

      public final ObjectProperty rotationAxisProperty() {
         if (this.rotationAxis == null) {
            this.rotationAxis = new ObjectPropertyBase(Node.DEFAULT_ROTATION_AXIS) {
               protected void invalidated() {
                  Node.this.impl_transformsChanged();
               }

               public Object getBean() {
                  return Node.this;
               }

               public String getName() {
                  return "rotationAxis";
               }
            };
         }

         return this.rotationAxis;
      }

      public ObservableList getTransforms() {
         if (this.transforms == null) {
            this.transforms = new TrackableObservableList() {
               protected void onChanged(ListChangeListener.Change var1) {
                  while(var1.next()) {
                     Iterator var2 = var1.getRemoved().iterator();

                     Transform var3;
                     while(var2.hasNext()) {
                        var3 = (Transform)var2.next();
                        var3.impl_remove(Node.this);
                     }

                     var2 = var1.getAddedSubList().iterator();

                     while(var2.hasNext()) {
                        var3 = (Transform)var2.next();
                        var3.impl_add(Node.this);
                     }
                  }

                  Node.this.impl_transformsChanged();
               }
            };
         }

         return this.transforms;
      }

      public boolean canSetTranslateX() {
         return this.translateX == null || !this.translateX.isBound();
      }

      public boolean canSetTranslateY() {
         return this.translateY == null || !this.translateY.isBound();
      }

      public boolean canSetTranslateZ() {
         return this.translateZ == null || !this.translateZ.isBound();
      }

      public boolean canSetScaleX() {
         return this.scaleX == null || !this.scaleX.isBound();
      }

      public boolean canSetScaleY() {
         return this.scaleY == null || !this.scaleY.isBound();
      }

      public boolean canSetScaleZ() {
         return this.scaleZ == null || !this.scaleZ.isBound();
      }

      public boolean canSetRotate() {
         return this.rotate == null || !this.rotate.isBound();
      }

      public boolean hasTransforms() {
         return this.transforms != null && !this.transforms.isEmpty();
      }

      public boolean hasScaleOrRotate() {
         if (this.scaleX != null && this.scaleX.get() != 1.0) {
            return true;
         } else if (this.scaleY != null && this.scaleY.get() != 1.0) {
            return true;
         } else if (this.scaleZ != null && this.scaleZ.get() != 1.0) {
            return true;
         } else {
            return this.rotate != null && this.rotate.get() != 0.0;
         }
      }

      // $FF: synthetic method
      NodeTransformation(Object var2) {
         this();
      }

      class LocalToSceneTransformProperty extends LazyTransformProperty {
         private List localToSceneListeners;
         private long stamp;
         private long parentStamp;

         LocalToSceneTransformProperty() {
            super(null);
         }

         protected Transform computeTransform(Transform var1) {
            ++this.stamp;
            Node.this.updateLocalToParentTransform();
            Parent var2 = Node.this.getParent();
            if (var2 != null) {
               LocalToSceneTransformProperty var3 = (LocalToSceneTransformProperty)var2.localToSceneTransformProperty();
               Transform var4 = var3.getInternalValue();
               this.parentStamp = var3.stamp;
               return TransformUtils.immutableTransform(var1, var4, ((LazyTransformProperty)NodeTransformation.this.localToParentTransformProperty()).getInternalValue());
            } else {
               return TransformUtils.immutableTransform(var1, ((LazyTransformProperty)NodeTransformation.this.localToParentTransformProperty()).getInternalValue());
            }
         }

         public Object getBean() {
            return Node.this;
         }

         public String getName() {
            return "localToSceneTransform";
         }

         protected boolean validityKnown() {
            return NodeTransformation.this.listenerReasons > 0;
         }

         protected int computeValidity() {
            if (this.valid != 2) {
               return this.valid;
            } else {
               Node var1 = (Node)this.getBean();
               Parent var2 = var1.getParent();
               if (var2 != null) {
                  LocalToSceneTransformProperty var3 = (LocalToSceneTransformProperty)var2.localToSceneTransformProperty();
                  if (this.parentStamp != var3.stamp) {
                     this.valid = 1;
                     return 1;
                  } else {
                     int var4 = var3.computeValidity();
                     if (var4 == 1) {
                        this.valid = 1;
                     }

                     return var4;
                  }
               } else {
                  return 0;
               }
            }
         }

         public void addListener(InvalidationListener var1) {
            NodeTransformation.this.incListenerReasons();
            if (this.localToSceneListeners == null) {
               this.localToSceneListeners = new LinkedList();
            }

            this.localToSceneListeners.add(var1);
            super.addListener(var1);
         }

         public void addListener(ChangeListener var1) {
            NodeTransformation.this.incListenerReasons();
            if (this.localToSceneListeners == null) {
               this.localToSceneListeners = new LinkedList();
            }

            this.localToSceneListeners.add(var1);
            super.addListener(var1);
         }

         public void removeListener(InvalidationListener var1) {
            if (this.localToSceneListeners != null && this.localToSceneListeners.remove(var1)) {
               NodeTransformation.this.decListenerReasons();
            }

            super.removeListener(var1);
         }

         public void removeListener(ChangeListener var1) {
            if (this.localToSceneListeners != null && this.localToSceneListeners.remove(var1)) {
               NodeTransformation.this.decListenerReasons();
            }

            super.removeListener(var1);
         }
      }
   }

   private class ReadOnlyObjectWrapperManualFire extends ReadOnlyObjectWrapper {
      private ReadOnlyObjectWrapperManualFire() {
      }

      public Object getBean() {
         return Node.this;
      }

      public String getName() {
         return "scene";
      }

      protected void fireValueChangedEvent() {
      }

      public void fireSuperValueChangedEvent() {
         super.fireValueChangedEvent();
      }

      // $FF: synthetic method
      ReadOnlyObjectWrapperManualFire(Object var2) {
         this();
      }
   }
}
