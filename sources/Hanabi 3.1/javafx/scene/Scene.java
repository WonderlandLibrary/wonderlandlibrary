package javafx.scene;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.event.EventQueue;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.runtime.SystemProperties;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.SceneEventDispatcher;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.SceneTraversalEngine;
import com.sun.javafx.scene.traversal.TopMostTraversalEngine;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKScenePaintListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.CssMetaData;
import javafx.css.StyleableObjectProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

@DefaultProperty("root")
public class Scene implements EventTarget {
   private double widthSetByUser;
   private double heightSetByUser;
   private boolean sizeInitialized;
   private final boolean depthBuffer;
   private final SceneAntialiasing antiAliasing;
   private int dirtyBits;
   final AccessControlContext acc;
   private Camera defaultCamera;
   private Node transientFocusContainer;
   private static final int MIN_DIRTY_CAPACITY = 30;
   private static boolean inSynchronizer;
   private static boolean inMousePick;
   private static boolean allowPGAccess;
   private static int pgAccessCount;
   private static boolean paused;
   private static final boolean PLATFORM_DRAG_GESTURE_INITIATION = false;
   private Node[] dirtyNodes;
   private int dirtyNodesSize;
   /** @deprecated */
   @Deprecated
   private TKScene impl_peer;
   ScenePulseListener scenePulseListener;
   private ReadOnlyObjectWrapper window;
   DnDGesture dndGesture;
   DragGestureListener dragGestureListener;
   private ReadOnlyDoubleWrapper x;
   private ReadOnlyDoubleWrapper y;
   private ReadOnlyDoubleWrapper width;
   private ReadOnlyDoubleWrapper height;
   private TargetWrapper tmpTargetWrapper;
   private ObjectProperty camera;
   private ObjectProperty fill;
   private ObjectProperty root;
   Parent oldRoot;
   private static TKPulseListener snapshotPulseListener;
   private static List snapshotRunnableListA;
   private static List snapshotRunnableListB;
   private static List snapshotRunnableList;
   private ObjectProperty cursor;
   private final ObservableList stylesheets;
   private ObjectProperty userAgentStylesheet;
   private PerformanceTracker tracker;
   private static final Object trackerMonitor;
   private MouseHandler mouseHandler;
   private ClickGenerator clickGenerator;
   private Point2D cursorScreenPos;
   private Point2D cursorScenePos;
   private final TouchGesture scrollGesture;
   private final TouchGesture zoomGesture;
   private final TouchGesture rotateGesture;
   private final TouchGesture swipeGesture;
   private TouchMap touchMap;
   private TouchEvent nextTouchEvent;
   private TouchPoint[] touchPoints;
   private int touchEventSetId;
   private int touchPointIndex;
   private Map touchTargets;
   private KeyHandler keyHandler;
   private boolean focusDirty;
   private TopMostTraversalEngine traversalEngine;
   private Node oldFocusOwner;
   private ReadOnlyObjectWrapper focusOwner;
   Runnable testPulseListener;
   private List lights;
   private ObjectProperty eventDispatcher;
   private SceneEventDispatcher internalEventDispatcher;
   private ObjectProperty onContextMenuRequested;
   private ObjectProperty onMouseClicked;
   private ObjectProperty onMouseDragged;
   private ObjectProperty onMouseEntered;
   private ObjectProperty onMouseExited;
   private ObjectProperty onMouseMoved;
   private ObjectProperty onMousePressed;
   private ObjectProperty onMouseReleased;
   private ObjectProperty onDragDetected;
   private ObjectProperty onMouseDragOver;
   private ObjectProperty onMouseDragReleased;
   private ObjectProperty onMouseDragEntered;
   private ObjectProperty onMouseDragExited;
   private ObjectProperty onScrollStarted;
   private ObjectProperty onScroll;
   private ObjectProperty onScrollFinished;
   private ObjectProperty onRotationStarted;
   private ObjectProperty onRotate;
   private ObjectProperty onRotationFinished;
   private ObjectProperty onZoomStarted;
   private ObjectProperty onZoom;
   private ObjectProperty onZoomFinished;
   private ObjectProperty onSwipeUp;
   private ObjectProperty onSwipeDown;
   private ObjectProperty onSwipeLeft;
   private ObjectProperty onSwipeRight;
   private ObjectProperty onTouchPressed;
   private ObjectProperty onTouchMoved;
   private ObjectProperty onTouchReleased;
   private ObjectProperty onTouchStationary;
   private ObjectProperty onDragEntered;
   private ObjectProperty onDragExited;
   private ObjectProperty onDragOver;
   private ObjectProperty onDragDropped;
   private ObjectProperty onDragDone;
   private ObjectProperty onKeyPressed;
   private ObjectProperty onKeyReleased;
   private ObjectProperty onKeyTyped;
   private ObjectProperty onInputMethodTextChanged;
   private static final Object USER_DATA_KEY;
   private ObservableMap properties;
   private static final NodeOrientation defaultNodeOrientation;
   private ObjectProperty nodeOrientation;
   private EffectiveOrientationProperty effectiveNodeOrientationProperty;
   private NodeOrientation effectiveNodeOrientation;
   private Map accMap;
   private Accessible accessible;

   public Scene(@NamedArg("root") Parent var1) {
      this(var1, -1.0, -1.0, Color.WHITE, false, SceneAntialiasing.DISABLED);
   }

   public Scene(@NamedArg("root") Parent var1, @NamedArg("width") double var2, @NamedArg("height") double var4) {
      this(var1, var2, var4, Color.WHITE, false, SceneAntialiasing.DISABLED);
   }

   public Scene(@NamedArg("root") Parent var1, @NamedArg(value = "fill",defaultValue = "WHITE") Paint var2) {
      this(var1, -1.0, -1.0, var2, false, SceneAntialiasing.DISABLED);
   }

   public Scene(@NamedArg("root") Parent var1, @NamedArg("width") double var2, @NamedArg("height") double var4, @NamedArg(value = "fill",defaultValue = "WHITE") Paint var6) {
      this(var1, var2, var4, var6, false, SceneAntialiasing.DISABLED);
   }

   public Scene(@NamedArg("root") Parent var1, @NamedArg(value = "width",defaultValue = "-1") double var2, @NamedArg(value = "height",defaultValue = "-1") double var4, @NamedArg("depthBuffer") boolean var6) {
      this(var1, var2, var4, Color.WHITE, var6, SceneAntialiasing.DISABLED);
   }

   public Scene(@NamedArg("root") Parent var1, @NamedArg(value = "width",defaultValue = "-1") double var2, @NamedArg(value = "height",defaultValue = "-1") double var4, @NamedArg("depthBuffer") boolean var6, @NamedArg(value = "antiAliasing",defaultValue = "DISABLED") SceneAntialiasing var7) {
      this(var1, var2, var4, Color.WHITE, var6, var7);
      if (var7 != null && var7 != SceneAntialiasing.DISABLED && !Toolkit.getToolkit().isMSAASupported()) {
         String var8 = Scene.class.getName();
         PlatformLogger.getLogger(var8).warning("System can't support antiAliasing");
      }

   }

   private Scene(Parent var1, double var2, double var4, Paint var6, boolean var7, SceneAntialiasing var8) {
      this.widthSetByUser = -1.0;
      this.heightSetByUser = -1.0;
      this.sizeInitialized = false;
      this.acc = AccessController.getContext();
      this.scenePulseListener = new ScenePulseListener();
      this.dndGesture = null;
      this.tmpTargetWrapper = new TargetWrapper();
      this.stylesheets = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            StyleManager.getInstance().stylesheetsChanged(Scene.this, var1);
            var1.reset();

            while(var1.next() && !var1.wasRemoved()) {
            }

            Scene.this.getRoot().impl_reapplyCSS();
         }
      };
      this.userAgentStylesheet = null;
      this.scrollGesture = new TouchGesture();
      this.zoomGesture = new TouchGesture();
      this.rotateGesture = new TouchGesture();
      this.swipeGesture = new TouchGesture();
      this.touchMap = new TouchMap();
      this.nextTouchEvent = null;
      this.touchPoints = null;
      this.touchEventSetId = 0;
      this.touchPointIndex = 0;
      this.touchTargets = new HashMap();
      this.keyHandler = null;
      this.focusDirty = true;
      this.traversalEngine = new SceneTraversalEngine(this);
      this.focusOwner = new ReadOnlyObjectWrapper(this, "focusOwner") {
         protected void invalidated() {
            if (Scene.this.oldFocusOwner != null) {
               ((Node.FocusedProperty)Scene.this.oldFocusOwner.focusedProperty()).store(false);
            }

            Node var1 = (Node)this.get();
            if (var1 != null) {
               ((Node.FocusedProperty)var1.focusedProperty()).store(Scene.this.keyHandler.windowFocused);
               if (var1 != Scene.this.oldFocusOwner) {
                  var1.getScene().impl_enableInputMethodEvents(var1.getInputMethodRequests() != null && var1.getOnInputMethodTextChanged() != null);
               }
            }

            Node var2 = Scene.this.oldFocusOwner;
            Scene.this.oldFocusOwner = var1;
            if (var2 != null) {
               ((Node.FocusedProperty)var2.focusedProperty()).notifyListeners();
            }

            if (var1 != null) {
               ((Node.FocusedProperty)var1.focusedProperty()).notifyListeners();
            }

            PlatformLogger var3 = Logging.getFocusLogger();
            if (var3.isLoggable(Level.FINE)) {
               if (var1 == this.get()) {
                  var3.fine("Changed focus from " + var2 + " to " + var1);
               } else {
                  var3.fine("Changing focus from " + var2 + " to " + var1 + " canceled by nested requestFocus");
               }
            }

            if (Scene.this.accessible != null) {
               Scene.this.accessible.sendNotification(AccessibleAttribute.FOCUS_NODE);
            }

         }
      };
      this.testPulseListener = null;
      this.lights = new ArrayList();
      this.depthBuffer = var7;
      this.antiAliasing = var8;
      if (var1 == null) {
         throw new NullPointerException("Root cannot be null");
      } else {
         if ((var7 || var8 != null && var8 != SceneAntialiasing.DISABLED) && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String var9 = Scene.class.getName();
            PlatformLogger.getLogger(var9).warning("System can't support ConditionalFeature.SCENE3D");
         }

         this.init();
         this.setRoot(var1);
         this.init(var2, var4);
         this.setFill(var6);
      }
   }

   static boolean isPGAccessAllowed() {
      return inSynchronizer || inMousePick || allowPGAccess;
   }

   /** @deprecated */
   @Deprecated
   public static void impl_setAllowPGAccess(boolean var0) {
      if (Utils.assertionEnabled()) {
         if (var0) {
            ++pgAccessCount;
            allowPGAccess = true;
         } else {
            if (pgAccessCount <= 0) {
               throw new AssertionError("*** pgAccessCount underflow");
            }

            if (--pgAccessCount == 0) {
               allowPGAccess = false;
            }
         }
      }

   }

   void addToDirtyList(Node var1) {
      if ((this.dirtyNodes == null || this.dirtyNodesSize == 0) && this.impl_peer != null) {
         Toolkit.getToolkit().requestNextPulse();
      }

      if (this.dirtyNodes != null) {
         if (this.dirtyNodesSize == this.dirtyNodes.length) {
            Node[] var2 = new Node[this.dirtyNodesSize + (this.dirtyNodesSize >> 1)];
            System.arraycopy(this.dirtyNodes, 0, var2, 0, this.dirtyNodesSize);
            this.dirtyNodes = var2;
         }

         this.dirtyNodes[this.dirtyNodesSize++] = var1;
      }

   }

   private void doCSSPass() {
      Parent var1 = this.getRoot();
      if (var1.cssFlag != CssFlags.CLEAN) {
         var1.impl_clearDirty(com.sun.javafx.scene.DirtyBits.NODE_CSS);
         var1.processCSS();
      }

   }

   void doLayoutPass() {
      Parent var1 = this.getRoot();
      if (var1 != null) {
         var1.layout();
      }

   }

   /** @deprecated */
   @Deprecated
   public TKScene impl_getPeer() {
      return this.impl_peer;
   }

   /** @deprecated */
   @Deprecated
   public TKPulseListener impl_getScenePulseListener() {
      return SystemProperties.isDebug() ? this.scenePulseListener : null;
   }

   public final SceneAntialiasing getAntiAliasing() {
      return this.antiAliasing;
   }

   private boolean getAntiAliasingInternal() {
      return this.antiAliasing != null && Toolkit.getToolkit().isMSAASupported() && Platform.isSupported(ConditionalFeature.SCENE3D) ? this.antiAliasing != SceneAntialiasing.DISABLED : false;
   }

   private void setWindow(Window var1) {
      this.windowPropertyImpl().set(var1);
   }

   public final Window getWindow() {
      return this.window == null ? null : (Window)this.window.get();
   }

   public final ReadOnlyObjectProperty windowProperty() {
      return this.windowPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper windowPropertyImpl() {
      if (this.window == null) {
         this.window = new ReadOnlyObjectWrapper() {
            private Window oldWindow;

            protected void invalidated() {
               Window var1 = (Window)this.get();
               Scene.this.getKeyHandler().windowForSceneChanged(this.oldWindow, var1);
               if (this.oldWindow != null) {
                  Scene.this.impl_disposePeer();
               }

               if (var1 != null) {
                  Scene.this.impl_initPeer();
               }

               Scene.this.parentEffectiveOrientationInvalidated();
               this.oldWindow = var1;
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "window";
            }
         };
      }

      return this.window;
   }

   /** @deprecated */
   @Deprecated
   public void impl_setWindow(Window var1) {
      this.setWindow(var1);
   }

   /** @deprecated */
   @Deprecated
   public void impl_initPeer() {
      assert this.impl_peer == null;

      Window var1 = this.getWindow();

      assert var1 != null;

      TKStage var2 = var1.impl_getPeer();
      if (var2 != null) {
         boolean var3 = Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW);
         if (!var3) {
            PlatformImpl.addNoTransparencyStylesheetToScene(this);
         }

         PerformanceTracker.logEvent("Scene.initPeer started");
         impl_setAllowPGAccess(true);
         Toolkit var4 = Toolkit.getToolkit();
         this.impl_peer = var2.createTKScene(this.isDepthBufferInternal(), this.getAntiAliasingInternal(), this.acc);
         PerformanceTracker.logEvent("Scene.initPeer TKScene created");
         this.impl_peer.setTKSceneListener(new ScenePeerListener());
         this.impl_peer.setTKScenePaintListener(new ScenePeerPaintListener());
         PerformanceTracker.logEvent("Scene.initPeer TKScene set");
         this.impl_peer.setRoot(this.getRoot().impl_getPeer());
         this.impl_peer.setFillPaint(this.getFill() == null ? null : var4.getPaint(this.getFill()));
         this.getEffectiveCamera().impl_updatePeer();
         this.impl_peer.setCamera((NGCamera)this.getEffectiveCamera().impl_getPeer());
         this.impl_peer.markDirty();
         PerformanceTracker.logEvent("Scene.initPeer TKScene initialized");
         impl_setAllowPGAccess(false);
         var4.addSceneTkPulseListener(this.scenePulseListener);
         var4.enableDrop(this.impl_peer, new DropTargetListener());
         var4.installInputMethodRequests(this.impl_peer, new InputMethodRequestsDelegate());
         PerformanceTracker.logEvent("Scene.initPeer finished");
      }
   }

   /** @deprecated */
   @Deprecated
   public void impl_disposePeer() {
      if (this.impl_peer != null) {
         PerformanceTracker.logEvent("Scene.disposePeer started");
         Toolkit var1 = Toolkit.getToolkit();
         var1.removeSceneTkPulseListener(this.scenePulseListener);
         if (this.accessible != null) {
            this.disposeAccessibles();
            Parent var2 = this.getRoot();
            if (var2 != null) {
               var2.releaseAccessible();
            }

            this.accessible.dispose();
            this.accessible = null;
         }

         this.impl_peer.dispose();
         this.impl_peer = null;
         PerformanceTracker.logEvent("Scene.disposePeer finished");
      }
   }

   private final void setX(double var1) {
      this.xPropertyImpl().set(var1);
   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final ReadOnlyDoubleProperty xProperty() {
      return this.xPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyDoubleWrapper xPropertyImpl() {
      if (this.x == null) {
         this.x = new ReadOnlyDoubleWrapper(this, "x");
      }

      return this.x;
   }

   private final void setY(double var1) {
      this.yPropertyImpl().set(var1);
   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final ReadOnlyDoubleProperty yProperty() {
      return this.yPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyDoubleWrapper yPropertyImpl() {
      if (this.y == null) {
         this.y = new ReadOnlyDoubleWrapper(this, "y");
      }

      return this.y;
   }

   private final void setWidth(double var1) {
      this.widthPropertyImpl().set(var1);
   }

   public final double getWidth() {
      return this.width == null ? 0.0 : this.width.get();
   }

   public final ReadOnlyDoubleProperty widthProperty() {
      return this.widthPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyDoubleWrapper widthPropertyImpl() {
      if (this.width == null) {
         this.width = new ReadOnlyDoubleWrapper() {
            protected void invalidated() {
               Parent var1 = Scene.this.getRoot();
               if (var1.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                  var1.impl_transformsChanged();
               }

               if (var1.isResizable()) {
                  Scene.this.resizeRootOnSceneSizeChange(this.get() - var1.getLayoutX() - var1.getTranslateX(), var1.getLayoutBounds().getHeight());
               }

               Scene.this.getEffectiveCamera().setViewWidth(this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "width";
            }
         };
      }

      return this.width;
   }

   private final void setHeight(double var1) {
      this.heightPropertyImpl().set(var1);
   }

   public final double getHeight() {
      return this.height == null ? 0.0 : this.height.get();
   }

   public final ReadOnlyDoubleProperty heightProperty() {
      return this.heightPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyDoubleWrapper heightPropertyImpl() {
      if (this.height == null) {
         this.height = new ReadOnlyDoubleWrapper() {
            protected void invalidated() {
               Parent var1 = Scene.this.getRoot();
               if (var1.isResizable()) {
                  Scene.this.resizeRootOnSceneSizeChange(var1.getLayoutBounds().getWidth(), this.get() - var1.getLayoutY() - var1.getTranslateY());
               }

               Scene.this.getEffectiveCamera().setViewHeight(this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "height";
            }
         };
      }

      return this.height;
   }

   void resizeRootOnSceneSizeChange(double var1, double var3) {
      this.getRoot().resize(var1, var3);
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
                  if (var1 instanceof PerspectiveCamera && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
                     String var2 = Scene.class.getName();
                     PlatformLogger.getLogger(var2).warning("System can't support ConditionalFeature.SCENE3D");
                  }

                  if (var1.getScene() != null && var1.getScene() != Scene.this || var1.getSubScene() != null) {
                     throw new IllegalArgumentException(var1 + "is already part of other scene or subscene");
                  }

                  var1.setOwnerScene(Scene.this);
                  var1.setViewWidth(Scene.this.getWidth());
                  var1.setViewHeight(Scene.this.getHeight());
               }

               if (this.oldCamera != null && this.oldCamera != var1) {
                  this.oldCamera.setOwnerScene((Scene)null);
               }

               this.oldCamera = var1;
            }

            public Object getBean() {
               return Scene.this;
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
      if (var1 == null || var1 instanceof PerspectiveCamera && !Platform.isSupported(ConditionalFeature.SCENE3D)) {
         if (this.defaultCamera == null) {
            this.defaultCamera = new ParallelCamera();
            this.defaultCamera.setOwnerScene(this);
            this.defaultCamera.setViewWidth(this.getWidth());
            this.defaultCamera.setViewHeight(this.getHeight());
         }

         return this.defaultCamera;
      } else {
         return var1;
      }
   }

   void markCameraDirty() {
      this.markDirty(Scene.DirtyBits.CAMERA_DIRTY);
      this.setNeedsRepaint();
   }

   void markCursorDirty() {
      this.markDirty(Scene.DirtyBits.CURSOR_DIRTY);
   }

   public final void setFill(Paint var1) {
      this.fillProperty().set(var1);
   }

   public final Paint getFill() {
      return (Paint)(this.fill == null ? Color.WHITE : (Paint)this.fill.get());
   }

   public final ObjectProperty fillProperty() {
      if (this.fill == null) {
         this.fill = new ObjectPropertyBase(Color.WHITE) {
            protected void invalidated() {
               Scene.this.markDirty(Scene.DirtyBits.FILL_DIRTY);
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "fill";
            }
         };
      }

      return this.fill;
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
               } else if (var1.getScene() != null && var1.getScene().getRoot() == var1 && var1.getScene() != Scene.this) {
                  if (this.isBound()) {
                     this.forceUnbind();
                  }

                  throw new IllegalArgumentException(var1 + "is already set as root of another scene");
               } else {
                  if (Scene.this.oldRoot != null) {
                     Scene.this.oldRoot.setScenes((Scene)null, (SubScene)null);
                  }

                  Scene.this.oldRoot = var1;
                  var1.getStyleClass().add(0, "root");
                  var1.setScenes(Scene.this, (SubScene)null);
                  Scene.this.markDirty(Scene.DirtyBits.ROOT_DIRTY);
                  var1.resize(Scene.this.getWidth(), Scene.this.getHeight());
                  var1.requestLayout();
               }
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "root";
            }
         };
      }

      return this.root;
   }

   void setNeedsRepaint() {
      if (this.impl_peer != null) {
         this.impl_peer.entireSceneNeedsRepaint();
      }

   }

   void doCSSLayoutSyncForSnapshot(Node var1) {
      if (!this.sizeInitialized) {
         this.preferredSize();
      } else {
         this.doCSSPass();
      }

      this.doLayoutPass();
      if (!paused) {
         this.getRoot().updateBounds();
         if (this.impl_peer != null) {
            this.impl_peer.waitForRenderingToComplete();
            this.impl_peer.waitForSynchronization();

            try {
               this.scenePulseListener.synchronizeSceneNodes();
            } finally {
               this.impl_peer.releaseSynchronization(false);
            }
         } else {
            this.scenePulseListener.synchronizeSceneNodes();
         }
      }

   }

   static WritableImage doSnapshot(Scene var0, double var1, double var3, double var5, double var7, Node var9, BaseTransform var10, boolean var11, Paint var12, Camera var13, WritableImage var14) {
      Toolkit var15 = Toolkit.getToolkit();
      Toolkit.ImageRenderingContext var16 = new Toolkit.ImageRenderingContext();
      int var17 = (int)Math.floor(var1);
      int var18 = (int)Math.floor(var3);
      int var19 = (int)Math.ceil(var1 + var5);
      int var20 = (int)Math.ceil(var3 + var7);
      int var21 = Math.max(var19 - var17, 1);
      int var22 = Math.max(var20 - var18, 1);
      if (var14 == null) {
         var14 = new WritableImage(var21, var22);
      } else {
         var21 = (int)var14.getWidth();
         var22 = (int)var14.getHeight();
      }

      impl_setAllowPGAccess(true);
      var16.x = var17;
      var16.y = var18;
      var16.width = var21;
      var16.height = var22;
      var16.transform = var10;
      var16.depthBuffer = var11;
      var16.root = var9.impl_getPeer();
      var16.platformPaint = var12 == null ? null : var15.getPaint(var12);
      double var23 = 1.0;
      double var25 = 1.0;
      if (var13 != null) {
         var23 = var13.getViewWidth();
         var25 = var13.getViewHeight();
         var13.setViewWidth((double)var21);
         var13.setViewHeight((double)var22);
         var13.impl_updatePeer();
         var16.camera = (NGCamera)var13.impl_getPeer();
      } else {
         var16.camera = null;
      }

      var16.lights = null;
      if (var0 != null && !var0.lights.isEmpty()) {
         var16.lights = new NGLightBase[var0.lights.size()];

         for(int var27 = 0; var27 < var0.lights.size(); ++var27) {
            var16.lights[var27] = (NGLightBase)((LightBase)var0.lights.get(var27)).impl_getPeer();
         }
      }

      Toolkit.WritableImageAccessor var29 = Toolkit.getWritableImageAccessor();
      var16.platformImage = var29.getTkImageLoader(var14);
      impl_setAllowPGAccess(false);
      Object var28 = var15.renderToImage(var16);
      var29.loadTkImage(var14, var28);
      if (var13 != null) {
         impl_setAllowPGAccess(true);
         var13.setViewWidth(var23);
         var13.setViewHeight(var25);
         var13.impl_updatePeer();
         impl_setAllowPGAccess(false);
      }

      if (var0 != null && var0.impl_peer != null) {
         var0.setNeedsRepaint();
      }

      return var14;
   }

   private WritableImage doSnapshot(WritableImage var1) {
      this.doCSSLayoutSyncForSnapshot(this.getRoot());
      double var2 = this.getWidth();
      double var4 = this.getHeight();
      BaseTransform var6 = BaseTransform.IDENTITY_TRANSFORM;
      return doSnapshot(this, 0.0, 0.0, var2, var4, this.getRoot(), var6, this.isDepthBufferInternal(), this.getFill(), this.getEffectiveCamera(), var1);
   }

   static void addSnapshotRunnable(Runnable var0) {
      Toolkit.getToolkit().checkFxUserThread();
      if (snapshotPulseListener == null) {
         snapshotRunnableListA = new ArrayList();
         snapshotRunnableListB = new ArrayList();
         snapshotRunnableList = snapshotRunnableListA;
         snapshotPulseListener = () -> {
            if (snapshotRunnableList.size() > 0) {
               List var0 = snapshotRunnableList;
               if (snapshotRunnableList == snapshotRunnableListA) {
                  snapshotRunnableList = snapshotRunnableListB;
               } else {
                  snapshotRunnableList = snapshotRunnableListA;
               }

               Iterator var1 = var0.iterator();

               while(var1.hasNext()) {
                  Runnable var2 = (Runnable)var1.next();

                  try {
                     var2.run();
                  } catch (Throwable var4) {
                     System.err.println("Exception in snapshot runnable");
                     var4.printStackTrace(System.err);
                  }
               }

               var0.clear();
            }

         };
         Toolkit.getToolkit().addPostSceneTkPulseListener(snapshotPulseListener);
      }

      AccessControlContext var1 = AccessController.getContext();
      snapshotRunnableList.add(() -> {
         AccessController.doPrivileged(() -> {
            var0.run();
            return null;
         }, var1);
      });
      Toolkit.getToolkit().requestNextPulse();
   }

   public WritableImage snapshot(WritableImage var1) {
      if (!paused) {
         Toolkit.getToolkit().checkFxUserThread();
      }

      return this.doSnapshot(var1);
   }

   public void snapshot(Callback var1, WritableImage var2) {
      Toolkit.getToolkit().checkFxUserThread();
      if (var1 == null) {
         throw new NullPointerException("The callback must not be null");
      } else {
         Runnable var5 = () -> {
            WritableImage var3 = this.doSnapshot(var2);
            SnapshotResult var4 = new SnapshotResult(var3, this, (SnapshotParameters)null);

            try {
               Void var5 = (Void)var1.call(var4);
            } catch (Throwable var6) {
               System.err.println("Exception in snapshot callback");
               var6.printStackTrace(System.err);
            }

         };
         addSnapshotRunnable(var5);
      }
   }

   public final void setCursor(Cursor var1) {
      this.cursorProperty().set(var1);
   }

   public final Cursor getCursor() {
      return this.cursor == null ? null : (Cursor)this.cursor.get();
   }

   public final ObjectProperty cursorProperty() {
      if (this.cursor == null) {
         this.cursor = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.markCursorDirty();
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "cursor";
            }
         };
      }

      return this.cursor;
   }

   public Node lookup(String var1) {
      return this.getRoot().lookup(var1);
   }

   public final ObservableList getStylesheets() {
      return this.stylesheets;
   }

   public final ObjectProperty userAgentStylesheetProperty() {
      if (this.userAgentStylesheet == null) {
         this.userAgentStylesheet = new SimpleObjectProperty(this, "userAgentStylesheet", (String)null) {
            protected void invalidated() {
               StyleManager.getInstance().forget(Scene.this);
               Scene.this.getRoot().impl_reapplyCSS();
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

   public final boolean isDepthBuffer() {
      return this.depthBuffer;
   }

   boolean isDepthBufferInternal() {
      return !Platform.isSupported(ConditionalFeature.SCENE3D) ? false : this.depthBuffer;
   }

   private void init(double var1, double var3) {
      if (var1 >= 0.0) {
         this.widthSetByUser = var1;
         this.setWidth((double)((float)var1));
      }

      if (var3 >= 0.0) {
         this.heightSetByUser = var3;
         this.setHeight((double)((float)var3));
      }

      this.sizeInitialized = this.widthSetByUser >= 0.0 && this.heightSetByUser >= 0.0;
   }

   private void init() {
      if (PerformanceTracker.isLoggingEnabled()) {
         PerformanceTracker.logEvent("Scene.init for [" + this + "]");
      }

      this.mouseHandler = new MouseHandler();
      this.clickGenerator = new ClickGenerator();
      if (PerformanceTracker.isLoggingEnabled()) {
         PerformanceTracker.logEvent("Scene.init for [" + this + "] - finished");
      }

   }

   private void preferredSize() {
      Parent var1 = this.getRoot();
      this.doCSSPass();
      this.resizeRootToPreferredSize(var1);
      this.doLayoutPass();
      if (this.widthSetByUser < 0.0) {
         this.setWidth(var1.isResizable() ? var1.getLayoutX() + var1.getTranslateX() + var1.getLayoutBounds().getWidth() : var1.getBoundsInParent().getMaxX());
      } else {
         this.setWidth(this.widthSetByUser);
      }

      if (this.heightSetByUser < 0.0) {
         this.setHeight(var1.isResizable() ? var1.getLayoutY() + var1.getTranslateY() + var1.getLayoutBounds().getHeight() : var1.getBoundsInParent().getMaxY());
      } else {
         this.setHeight(this.heightSetByUser);
      }

      this.sizeInitialized = this.getWidth() > 0.0 && this.getHeight() > 0.0;
      PerformanceTracker.logEvent("Scene preferred bounds computation complete");
   }

   final void resizeRootToPreferredSize(Parent var1) {
      Orientation var6 = var1.getContentBias();
      double var2;
      double var4;
      if (var6 == null) {
         var2 = getPreferredWidth(var1, this.widthSetByUser, -1.0);
         var4 = getPreferredHeight(var1, this.heightSetByUser, -1.0);
      } else if (var6 == Orientation.HORIZONTAL) {
         var2 = getPreferredWidth(var1, this.widthSetByUser, -1.0);
         var4 = getPreferredHeight(var1, this.heightSetByUser, var2);
      } else {
         var4 = getPreferredHeight(var1, this.heightSetByUser, -1.0);
         var2 = getPreferredWidth(var1, this.widthSetByUser, var4);
      }

      var1.resize(var2, var4);
   }

   private static double getPreferredWidth(Parent var0, double var1, double var3) {
      if (var1 >= 0.0) {
         return var1;
      } else {
         double var5 = var3 >= 0.0 ? var3 : -1.0;
         return var0.boundedSize(var0.prefWidth(var5), var0.minWidth(var5), var0.maxWidth(var5));
      }
   }

   private static double getPreferredHeight(Parent var0, double var1, double var3) {
      if (var1 >= 0.0) {
         return var1;
      } else {
         double var5 = var3 >= 0.0 ? var3 : -1.0;
         return var0.boundedSize(var0.prefHeight(var5), var0.minHeight(var5), var0.maxHeight(var5));
      }
   }

   /** @deprecated */
   @Deprecated
   public void impl_preferredSize() {
      this.preferredSize();
   }

   /** @deprecated */
   @Deprecated
   public void impl_processMouseEvent(MouseEvent var1) {
      this.mouseHandler.process(var1, false);
   }

   private void processMenuEvent(double var1, double var3, double var5, double var7, boolean var9) {
      Object var10 = null;
      inMousePick = true;
      if (var9) {
         Node var11 = this.getFocusOwner();
         double var12 = var5 - var1;
         double var14 = var7 - var3;
         if (var11 != null) {
            Bounds var16 = var11.localToScene(var11.getBoundsInLocal());
            var1 = var16.getMinX() + var16.getWidth() / 4.0;
            var3 = var16.getMinY() + var16.getHeight() / 2.0;
            var10 = var11;
         } else {
            var1 = this.getWidth() / 4.0;
            var3 = this.getWidth() / 2.0;
            var10 = this;
         }

         var5 = var1 + var12;
         var7 = var3 + var14;
      }

      PickResult var17 = this.pick(var1, var3);
      if (!var9) {
         var10 = var17.getIntersectedNode();
         if (var10 == null) {
            var10 = this;
         }
      }

      if (var10 != null) {
         ContextMenuEvent var18 = new ContextMenuEvent(ContextMenuEvent.CONTEXT_MENU_REQUESTED, var1, var3, var5, var7, var9, var17);
         Event.fireEvent((EventTarget)var10, var18);
      }

      inMousePick = false;
   }

   private void processGestureEvent(GestureEvent var1, TouchGesture var2) {
      Object var3 = null;
      if (var1.getEventType() == ZoomEvent.ZOOM_STARTED || var1.getEventType() == RotateEvent.ROTATION_STARTED || var1.getEventType() == ScrollEvent.SCROLL_STARTED) {
         var2.target = null;
         var2.finished = false;
      }

      if (var2.target != null && (!var2.finished || var1.isInertia())) {
         var3 = var2.target;
      } else {
         var3 = var1.getPickResult().getIntersectedNode();
         if (var3 == null) {
            var3 = this;
         }
      }

      if (var1.getEventType() == ZoomEvent.ZOOM_STARTED || var1.getEventType() == RotateEvent.ROTATION_STARTED || var1.getEventType() == ScrollEvent.SCROLL_STARTED) {
         var2.target = (EventTarget)var3;
      }

      if (var1.getEventType() != ZoomEvent.ZOOM_FINISHED && var1.getEventType() != RotateEvent.ROTATION_FINISHED && var1.getEventType() != ScrollEvent.SCROLL_FINISHED && !var1.isInertia()) {
         var2.sceneCoords = new Point2D(var1.getSceneX(), var1.getSceneY());
         var2.screenCoords = new Point2D(var1.getScreenX(), var1.getScreenY());
      }

      if (var3 != null) {
         Event.fireEvent((EventTarget)var3, var1);
      }

      if (var1.getEventType() == ZoomEvent.ZOOM_FINISHED || var1.getEventType() == RotateEvent.ROTATION_FINISHED || var1.getEventType() == ScrollEvent.SCROLL_FINISHED) {
         var2.finished = true;
      }

   }

   private void processTouchEvent(TouchEvent var1, TouchPoint[] var2) {
      inMousePick = true;
      ++this.touchEventSetId;
      List var3 = Arrays.asList(var2);
      TouchPoint[] var4 = var2;
      int var5 = var2.length;

      int var6;
      TouchPoint var7;
      for(var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         if (var7.getTarget() != null) {
            EventType var8 = null;
            switch (var7.getState()) {
               case MOVED:
                  var8 = TouchEvent.TOUCH_MOVED;
                  break;
               case PRESSED:
                  var8 = TouchEvent.TOUCH_PRESSED;
                  break;
               case RELEASED:
                  var8 = TouchEvent.TOUCH_RELEASED;
                  break;
               case STATIONARY:
                  var8 = TouchEvent.TOUCH_STATIONARY;
            }

            TouchPoint[] var9 = var2;
            int var10 = var2.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               TouchPoint var12 = var9[var11];
               var12.impl_reset();
            }

            TouchEvent var13 = new TouchEvent(var8, var7, var3, this.touchEventSetId, var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown());
            Event.fireEvent(var7.getTarget(), var13);
         }
      }

      var4 = var2;
      var5 = var2.length;

      for(var6 = 0; var6 < var5; ++var6) {
         var7 = var4[var6];
         EventTarget var14 = var7.getGrabbed();
         if (var14 != null) {
            this.touchTargets.put(var7.getId(), var14);
         }

         if (var14 == null || var7.getState() == TouchPoint.State.RELEASED) {
            this.touchTargets.remove(var7.getId());
         }
      }

      inMousePick = false;
   }

   Node test_pick(double var1, double var3) {
      inMousePick = true;
      PickResult var5 = this.mouseHandler.pickNode(new PickRay(var1, var3, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
      inMousePick = false;
      return var5 != null ? var5.getIntersectedNode() : null;
   }

   private PickResult pick(double var1, double var3) {
      this.pick(this.tmpTargetWrapper, var1, var3);
      return this.tmpTargetWrapper.getResult();
   }

   private boolean isInScene(double var1, double var3) {
      if (!(var1 < 0.0) && !(var3 < 0.0) && !(var1 > this.getWidth()) && !(var3 > this.getHeight())) {
         Window var5 = this.getWindow();
         return !(var5 instanceof Stage) || ((Stage)var5).getStyle() != StageStyle.TRANSPARENT || this.getFill() != null;
      } else {
         return false;
      }
   }

   private void pick(TargetWrapper var1, double var2, double var4) {
      PickRay var6 = this.getEffectiveCamera().computePickRay(var2, var4, (PickRay)null);
      double var7 = var6.getDirectionNoClone().length();
      var6.getDirectionNoClone().normalize();
      PickResult var9 = this.mouseHandler.pickNode(var6);
      if (var9 != null) {
         var1.setNodeResult(var9);
      } else {
         Vec3d var10 = var6.getOriginNoClone();
         Vec3d var11 = var6.getDirectionNoClone();
         var1.setSceneResult(new PickResult((Node)null, new Point3D(var10.x + var7 * var11.x, var10.y + var7 * var11.y, var10.z + var7 * var11.z), var7), this.isInScene(var2, var4) ? this : null);
      }

   }

   private KeyHandler getKeyHandler() {
      if (this.keyHandler == null) {
         this.keyHandler = new KeyHandler();
      }

      return this.keyHandler;
   }

   final void setFocusDirty(boolean var1) {
      if (!this.focusDirty) {
         Toolkit.getToolkit().requestNextPulse();
      }

      this.focusDirty = var1;
   }

   final boolean isFocusDirty() {
      return this.focusDirty;
   }

   boolean traverse(Node var1, Direction var2) {
      if (var1.getSubScene() != null) {
         return var1.getSubScene().traverse(var1, var2);
      } else {
         return this.traversalEngine.trav(var1, var2) != null;
      }
   }

   private void focusInitial() {
      this.traversalEngine.traverseToFirst();
   }

   private void focusIneligible(Node var1) {
      this.traverse(var1, Direction.NEXT);
   }

   /** @deprecated */
   @Deprecated
   public void impl_processKeyEvent(KeyEvent var1) {
      if (this.dndGesture != null && !this.dndGesture.processKey(var1)) {
         this.dndGesture = null;
      }

      this.getKeyHandler().process(var1);
   }

   void requestFocus(Node var1) {
      this.getKeyHandler().requestFocus(var1);
   }

   public final Node getFocusOwner() {
      return (Node)this.focusOwner.get();
   }

   public final ReadOnlyObjectProperty focusOwnerProperty() {
      return this.focusOwner.getReadOnlyProperty();
   }

   void focusCleanup() {
      this.scenePulseListener.focusCleanup();
   }

   private void processInputMethodEvent(InputMethodEvent var1) {
      Node var2 = this.getFocusOwner();
      if (var2 != null) {
         var2.fireEvent(var1);
      }

   }

   /** @deprecated */
   @Deprecated
   public void impl_enableInputMethodEvents(boolean var1) {
      if (this.impl_peer != null) {
         this.impl_peer.enableInputMethodEvents(var1);
      }

   }

   boolean isQuiescent() {
      Parent var1 = this.getRoot();
      return !this.isFocusDirty() && (var1 == null || var1.cssFlag == CssFlags.CLEAN && var1.layoutFlag == LayoutFlags.CLEAN);
   }

   private void markDirty(DirtyBits var1) {
      this.setDirty(var1);
      if (this.impl_peer != null) {
         Toolkit.getToolkit().requestNextPulse();
      }

   }

   private void setDirty(DirtyBits var1) {
      this.dirtyBits |= var1.getMask();
   }

   private boolean isDirty(DirtyBits var1) {
      return (this.dirtyBits & var1.getMask()) != 0;
   }

   private boolean isDirtyEmpty() {
      return this.dirtyBits == 0;
   }

   private void clearDirty() {
      this.dirtyBits = 0;
   }

   final void addLight(LightBase var1) {
      if (!this.lights.contains(var1)) {
         this.lights.add(var1);
         this.markDirty(Scene.DirtyBits.LIGHTS_DIRTY);
      }

   }

   final void removeLight(LightBase var1) {
      if (this.lights.remove(var1)) {
         this.markDirty(Scene.DirtyBits.LIGHTS_DIRTY);
      }

   }

   private void syncLights() {
      if (this.isDirty(Scene.DirtyBits.LIGHTS_DIRTY)) {
         inSynchronizer = true;
         NGLightBase[] var1 = this.impl_peer.getLights();
         if (!this.lights.isEmpty() || var1 != null) {
            if (this.lights.isEmpty()) {
               this.impl_peer.setLights((NGLightBase[])null);
            } else {
               if (var1 == null || var1.length < this.lights.size()) {
                  var1 = new NGLightBase[this.lights.size()];
               }

               int var2;
               for(var2 = 0; var2 < this.lights.size(); ++var2) {
                  var1[var2] = (NGLightBase)((LightBase)this.lights.get(var2)).impl_getPeer();
               }

               while(var2 < var1.length && var1[var2] != null) {
                  var1[var2++] = null;
               }

               this.impl_peer.setLights(var1);
            }
         }

         inSynchronizer = false;
      }
   }

   void generateMouseExited(Node var1) {
      this.mouseHandler.handleNodeRemoval(var1);
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

   private SceneEventDispatcher getInternalEventDispatcher() {
      this.initializeInternalEventDispatcher();
      return this.internalEventDispatcher;
   }

   final void initializeInternalEventDispatcher() {
      if (this.internalEventDispatcher == null) {
         this.internalEventDispatcher = this.createInternalEventDispatcher();
         this.eventDispatcher = new SimpleObjectProperty(this, "eventDispatcher", this.internalEventDispatcher);
      }

   }

   private SceneEventDispatcher createInternalEventDispatcher() {
      return new SceneEventDispatcher(this);
   }

   public void addMnemonic(Mnemonic var1) {
      this.getInternalEventDispatcher().getKeyboardShortcutsHandler().addMnemonic(var1);
   }

   public void removeMnemonic(Mnemonic var1) {
      this.getInternalEventDispatcher().getKeyboardShortcutsHandler().removeMnemonic(var1);
   }

   final void clearNodeMnemonics(Node var1) {
      this.getInternalEventDispatcher().getKeyboardShortcutsHandler().clearNodeMnemonics(var1);
   }

   public ObservableMap getMnemonics() {
      return this.getInternalEventDispatcher().getKeyboardShortcutsHandler().getMnemonics();
   }

   public ObservableMap getAccelerators() {
      return this.getInternalEventDispatcher().getKeyboardShortcutsHandler().getAccelerators();
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      if (this.eventDispatcher != null) {
         EventDispatcher var2 = (EventDispatcher)this.eventDispatcher.get();
         if (var2 != null) {
            var1 = var1.prepend(var2);
         }
      }

      if (this.getWindow() != null) {
         var1 = this.getWindow().buildEventDispatchChain(var1);
      }

      return var1;
   }

   public final void setOnContextMenuRequested(EventHandler var1) {
      this.onContextMenuRequestedProperty().set(var1);
   }

   public final EventHandler getOnContextMenuRequested() {
      return this.onContextMenuRequested == null ? null : (EventHandler)this.onContextMenuRequested.get();
   }

   public final ObjectProperty onContextMenuRequestedProperty() {
      if (this.onContextMenuRequested == null) {
         this.onContextMenuRequested = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onContextMenuRequested";
            }
         };
      }

      return this.onContextMenuRequested;
   }

   public final void setOnMouseClicked(EventHandler var1) {
      this.onMouseClickedProperty().set(var1);
   }

   public final EventHandler getOnMouseClicked() {
      return this.onMouseClicked == null ? null : (EventHandler)this.onMouseClicked.get();
   }

   public final ObjectProperty onMouseClickedProperty() {
      if (this.onMouseClicked == null) {
         this.onMouseClicked = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_CLICKED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseClicked";
            }
         };
      }

      return this.onMouseClicked;
   }

   public final void setOnMouseDragged(EventHandler var1) {
      this.onMouseDraggedProperty().set(var1);
   }

   public final EventHandler getOnMouseDragged() {
      return this.onMouseDragged == null ? null : (EventHandler)this.onMouseDragged.get();
   }

   public final ObjectProperty onMouseDraggedProperty() {
      if (this.onMouseDragged == null) {
         this.onMouseDragged = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_DRAGGED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseDragged";
            }
         };
      }

      return this.onMouseDragged;
   }

   public final void setOnMouseEntered(EventHandler var1) {
      this.onMouseEnteredProperty().set(var1);
   }

   public final EventHandler getOnMouseEntered() {
      return this.onMouseEntered == null ? null : (EventHandler)this.onMouseEntered.get();
   }

   public final ObjectProperty onMouseEnteredProperty() {
      if (this.onMouseEntered == null) {
         this.onMouseEntered = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_ENTERED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseEntered";
            }
         };
      }

      return this.onMouseEntered;
   }

   public final void setOnMouseExited(EventHandler var1) {
      this.onMouseExitedProperty().set(var1);
   }

   public final EventHandler getOnMouseExited() {
      return this.onMouseExited == null ? null : (EventHandler)this.onMouseExited.get();
   }

   public final ObjectProperty onMouseExitedProperty() {
      if (this.onMouseExited == null) {
         this.onMouseExited = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_EXITED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseExited";
            }
         };
      }

      return this.onMouseExited;
   }

   public final void setOnMouseMoved(EventHandler var1) {
      this.onMouseMovedProperty().set(var1);
   }

   public final EventHandler getOnMouseMoved() {
      return this.onMouseMoved == null ? null : (EventHandler)this.onMouseMoved.get();
   }

   public final ObjectProperty onMouseMovedProperty() {
      if (this.onMouseMoved == null) {
         this.onMouseMoved = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_MOVED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseMoved";
            }
         };
      }

      return this.onMouseMoved;
   }

   public final void setOnMousePressed(EventHandler var1) {
      this.onMousePressedProperty().set(var1);
   }

   public final EventHandler getOnMousePressed() {
      return this.onMousePressed == null ? null : (EventHandler)this.onMousePressed.get();
   }

   public final ObjectProperty onMousePressedProperty() {
      if (this.onMousePressed == null) {
         this.onMousePressed = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_PRESSED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMousePressed";
            }
         };
      }

      return this.onMousePressed;
   }

   public final void setOnMouseReleased(EventHandler var1) {
      this.onMouseReleasedProperty().set(var1);
   }

   public final EventHandler getOnMouseReleased() {
      return this.onMouseReleased == null ? null : (EventHandler)this.onMouseReleased.get();
   }

   public final ObjectProperty onMouseReleasedProperty() {
      if (this.onMouseReleased == null) {
         this.onMouseReleased = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.MOUSE_RELEASED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseReleased";
            }
         };
      }

      return this.onMouseReleased;
   }

   public final void setOnDragDetected(EventHandler var1) {
      this.onDragDetectedProperty().set(var1);
   }

   public final EventHandler getOnDragDetected() {
      return this.onDragDetected == null ? null : (EventHandler)this.onDragDetected.get();
   }

   public final ObjectProperty onDragDetectedProperty() {
      if (this.onDragDetected == null) {
         this.onDragDetected = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseEvent.DRAG_DETECTED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onDragDetected";
            }
         };
      }

      return this.onDragDetected;
   }

   public final void setOnMouseDragOver(EventHandler var1) {
      this.onMouseDragOverProperty().set(var1);
   }

   public final EventHandler getOnMouseDragOver() {
      return this.onMouseDragOver == null ? null : (EventHandler)this.onMouseDragOver.get();
   }

   public final ObjectProperty onMouseDragOverProperty() {
      if (this.onMouseDragOver == null) {
         this.onMouseDragOver = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_OVER, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseDragOver";
            }
         };
      }

      return this.onMouseDragOver;
   }

   public final void setOnMouseDragReleased(EventHandler var1) {
      this.onMouseDragReleasedProperty().set(var1);
   }

   public final EventHandler getOnMouseDragReleased() {
      return this.onMouseDragReleased == null ? null : (EventHandler)this.onMouseDragReleased.get();
   }

   public final ObjectProperty onMouseDragReleasedProperty() {
      if (this.onMouseDragReleased == null) {
         this.onMouseDragReleased = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_RELEASED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseDragReleased";
            }
         };
      }

      return this.onMouseDragReleased;
   }

   public final void setOnMouseDragEntered(EventHandler var1) {
      this.onMouseDragEnteredProperty().set(var1);
   }

   public final EventHandler getOnMouseDragEntered() {
      return this.onMouseDragEntered == null ? null : (EventHandler)this.onMouseDragEntered.get();
   }

   public final ObjectProperty onMouseDragEnteredProperty() {
      if (this.onMouseDragEntered == null) {
         this.onMouseDragEntered = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseDragEntered";
            }
         };
      }

      return this.onMouseDragEntered;
   }

   public final void setOnMouseDragExited(EventHandler var1) {
      this.onMouseDragExitedProperty().set(var1);
   }

   public final EventHandler getOnMouseDragExited() {
      return this.onMouseDragExited == null ? null : (EventHandler)this.onMouseDragExited.get();
   }

   public final ObjectProperty onMouseDragExitedProperty() {
      if (this.onMouseDragExited == null) {
         this.onMouseDragExited = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(MouseDragEvent.MOUSE_DRAG_EXITED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onMouseDragExited";
            }
         };
      }

      return this.onMouseDragExited;
   }

   public final void setOnScrollStarted(EventHandler var1) {
      this.onScrollStartedProperty().set(var1);
   }

   public final EventHandler getOnScrollStarted() {
      return this.onScrollStarted == null ? null : (EventHandler)this.onScrollStarted.get();
   }

   public final ObjectProperty onScrollStartedProperty() {
      if (this.onScrollStarted == null) {
         this.onScrollStarted = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ScrollEvent.SCROLL_STARTED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onScrollStarted";
            }
         };
      }

      return this.onScrollStarted;
   }

   public final void setOnScroll(EventHandler var1) {
      this.onScrollProperty().set(var1);
   }

   public final EventHandler getOnScroll() {
      return this.onScroll == null ? null : (EventHandler)this.onScroll.get();
   }

   public final ObjectProperty onScrollProperty() {
      if (this.onScroll == null) {
         this.onScroll = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ScrollEvent.SCROLL, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onScroll";
            }
         };
      }

      return this.onScroll;
   }

   public final void setOnScrollFinished(EventHandler var1) {
      this.onScrollFinishedProperty().set(var1);
   }

   public final EventHandler getOnScrollFinished() {
      return this.onScrollFinished == null ? null : (EventHandler)this.onScrollFinished.get();
   }

   public final ObjectProperty onScrollFinishedProperty() {
      if (this.onScrollFinished == null) {
         this.onScrollFinished = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ScrollEvent.SCROLL_FINISHED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onScrollFinished";
            }
         };
      }

      return this.onScrollFinished;
   }

   public final void setOnRotationStarted(EventHandler var1) {
      this.onRotationStartedProperty().set(var1);
   }

   public final EventHandler getOnRotationStarted() {
      return this.onRotationStarted == null ? null : (EventHandler)this.onRotationStarted.get();
   }

   public final ObjectProperty onRotationStartedProperty() {
      if (this.onRotationStarted == null) {
         this.onRotationStarted = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(RotateEvent.ROTATION_STARTED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onRotationStarted";
            }
         };
      }

      return this.onRotationStarted;
   }

   public final void setOnRotate(EventHandler var1) {
      this.onRotateProperty().set(var1);
   }

   public final EventHandler getOnRotate() {
      return this.onRotate == null ? null : (EventHandler)this.onRotate.get();
   }

   public final ObjectProperty onRotateProperty() {
      if (this.onRotate == null) {
         this.onRotate = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(RotateEvent.ROTATE, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onRotate";
            }
         };
      }

      return this.onRotate;
   }

   public final void setOnRotationFinished(EventHandler var1) {
      this.onRotationFinishedProperty().set(var1);
   }

   public final EventHandler getOnRotationFinished() {
      return this.onRotationFinished == null ? null : (EventHandler)this.onRotationFinished.get();
   }

   public final ObjectProperty onRotationFinishedProperty() {
      if (this.onRotationFinished == null) {
         this.onRotationFinished = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(RotateEvent.ROTATION_FINISHED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onRotationFinished";
            }
         };
      }

      return this.onRotationFinished;
   }

   public final void setOnZoomStarted(EventHandler var1) {
      this.onZoomStartedProperty().set(var1);
   }

   public final EventHandler getOnZoomStarted() {
      return this.onZoomStarted == null ? null : (EventHandler)this.onZoomStarted.get();
   }

   public final ObjectProperty onZoomStartedProperty() {
      if (this.onZoomStarted == null) {
         this.onZoomStarted = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ZoomEvent.ZOOM_STARTED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onZoomStarted";
            }
         };
      }

      return this.onZoomStarted;
   }

   public final void setOnZoom(EventHandler var1) {
      this.onZoomProperty().set(var1);
   }

   public final EventHandler getOnZoom() {
      return this.onZoom == null ? null : (EventHandler)this.onZoom.get();
   }

   public final ObjectProperty onZoomProperty() {
      if (this.onZoom == null) {
         this.onZoom = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ZoomEvent.ZOOM, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onZoom";
            }
         };
      }

      return this.onZoom;
   }

   public final void setOnZoomFinished(EventHandler var1) {
      this.onZoomFinishedProperty().set(var1);
   }

   public final EventHandler getOnZoomFinished() {
      return this.onZoomFinished == null ? null : (EventHandler)this.onZoomFinished.get();
   }

   public final ObjectProperty onZoomFinishedProperty() {
      if (this.onZoomFinished == null) {
         this.onZoomFinished = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(ZoomEvent.ZOOM_FINISHED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onZoomFinished";
            }
         };
      }

      return this.onZoomFinished;
   }

   public final void setOnSwipeUp(EventHandler var1) {
      this.onSwipeUpProperty().set(var1);
   }

   public final EventHandler getOnSwipeUp() {
      return this.onSwipeUp == null ? null : (EventHandler)this.onSwipeUp.get();
   }

   public final ObjectProperty onSwipeUpProperty() {
      if (this.onSwipeUp == null) {
         this.onSwipeUp = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(SwipeEvent.SWIPE_UP, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onSwipeUp";
            }
         };
      }

      return this.onSwipeUp;
   }

   public final void setOnSwipeDown(EventHandler var1) {
      this.onSwipeDownProperty().set(var1);
   }

   public final EventHandler getOnSwipeDown() {
      return this.onSwipeDown == null ? null : (EventHandler)this.onSwipeDown.get();
   }

   public final ObjectProperty onSwipeDownProperty() {
      if (this.onSwipeDown == null) {
         this.onSwipeDown = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(SwipeEvent.SWIPE_DOWN, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onSwipeDown";
            }
         };
      }

      return this.onSwipeDown;
   }

   public final void setOnSwipeLeft(EventHandler var1) {
      this.onSwipeLeftProperty().set(var1);
   }

   public final EventHandler getOnSwipeLeft() {
      return this.onSwipeLeft == null ? null : (EventHandler)this.onSwipeLeft.get();
   }

   public final ObjectProperty onSwipeLeftProperty() {
      if (this.onSwipeLeft == null) {
         this.onSwipeLeft = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(SwipeEvent.SWIPE_LEFT, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onSwipeLeft";
            }
         };
      }

      return this.onSwipeLeft;
   }

   public final void setOnSwipeRight(EventHandler var1) {
      this.onSwipeRightProperty().set(var1);
   }

   public final EventHandler getOnSwipeRight() {
      return this.onSwipeRight == null ? null : (EventHandler)this.onSwipeRight.get();
   }

   public final ObjectProperty onSwipeRightProperty() {
      if (this.onSwipeRight == null) {
         this.onSwipeRight = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(SwipeEvent.SWIPE_RIGHT, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onSwipeRight";
            }
         };
      }

      return this.onSwipeRight;
   }

   public final void setOnTouchPressed(EventHandler var1) {
      this.onTouchPressedProperty().set(var1);
   }

   public final EventHandler getOnTouchPressed() {
      return this.onTouchPressed == null ? null : (EventHandler)this.onTouchPressed.get();
   }

   public final ObjectProperty onTouchPressedProperty() {
      if (this.onTouchPressed == null) {
         this.onTouchPressed = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(TouchEvent.TOUCH_PRESSED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onTouchPressed";
            }
         };
      }

      return this.onTouchPressed;
   }

   public final void setOnTouchMoved(EventHandler var1) {
      this.onTouchMovedProperty().set(var1);
   }

   public final EventHandler getOnTouchMoved() {
      return this.onTouchMoved == null ? null : (EventHandler)this.onTouchMoved.get();
   }

   public final ObjectProperty onTouchMovedProperty() {
      if (this.onTouchMoved == null) {
         this.onTouchMoved = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(TouchEvent.TOUCH_MOVED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onTouchMoved";
            }
         };
      }

      return this.onTouchMoved;
   }

   public final void setOnTouchReleased(EventHandler var1) {
      this.onTouchReleasedProperty().set(var1);
   }

   public final EventHandler getOnTouchReleased() {
      return this.onTouchReleased == null ? null : (EventHandler)this.onTouchReleased.get();
   }

   public final ObjectProperty onTouchReleasedProperty() {
      if (this.onTouchReleased == null) {
         this.onTouchReleased = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(TouchEvent.TOUCH_RELEASED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onTouchReleased";
            }
         };
      }

      return this.onTouchReleased;
   }

   public final void setOnTouchStationary(EventHandler var1) {
      this.onTouchStationaryProperty().set(var1);
   }

   public final EventHandler getOnTouchStationary() {
      return this.onTouchStationary == null ? null : (EventHandler)this.onTouchStationary.get();
   }

   public final ObjectProperty onTouchStationaryProperty() {
      if (this.onTouchStationary == null) {
         this.onTouchStationary = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(TouchEvent.TOUCH_STATIONARY, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onTouchStationary";
            }
         };
      }

      return this.onTouchStationary;
   }

   public final void setOnDragEntered(EventHandler var1) {
      this.onDragEnteredProperty().set(var1);
   }

   public final EventHandler getOnDragEntered() {
      return this.onDragEntered == null ? null : (EventHandler)this.onDragEntered.get();
   }

   public final ObjectProperty onDragEnteredProperty() {
      if (this.onDragEntered == null) {
         this.onDragEntered = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(DragEvent.DRAG_ENTERED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onDragEntered";
            }
         };
      }

      return this.onDragEntered;
   }

   public final void setOnDragExited(EventHandler var1) {
      this.onDragExitedProperty().set(var1);
   }

   public final EventHandler getOnDragExited() {
      return this.onDragExited == null ? null : (EventHandler)this.onDragExited.get();
   }

   public final ObjectProperty onDragExitedProperty() {
      if (this.onDragExited == null) {
         this.onDragExited = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(DragEvent.DRAG_EXITED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onDragExited";
            }
         };
      }

      return this.onDragExited;
   }

   public final void setOnDragOver(EventHandler var1) {
      this.onDragOverProperty().set(var1);
   }

   public final EventHandler getOnDragOver() {
      return this.onDragOver == null ? null : (EventHandler)this.onDragOver.get();
   }

   public final ObjectProperty onDragOverProperty() {
      if (this.onDragOver == null) {
         this.onDragOver = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(DragEvent.DRAG_OVER, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onDragOver";
            }
         };
      }

      return this.onDragOver;
   }

   public final void setOnDragDropped(EventHandler var1) {
      this.onDragDroppedProperty().set(var1);
   }

   public final EventHandler getOnDragDropped() {
      return this.onDragDropped == null ? null : (EventHandler)this.onDragDropped.get();
   }

   public final ObjectProperty onDragDroppedProperty() {
      if (this.onDragDropped == null) {
         this.onDragDropped = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(DragEvent.DRAG_DROPPED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onDragDropped";
            }
         };
      }

      return this.onDragDropped;
   }

   public final void setOnDragDone(EventHandler var1) {
      this.onDragDoneProperty().set(var1);
   }

   public final EventHandler getOnDragDone() {
      return this.onDragDone == null ? null : (EventHandler)this.onDragDone.get();
   }

   public final ObjectProperty onDragDoneProperty() {
      if (this.onDragDone == null) {
         this.onDragDone = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(DragEvent.DRAG_DONE, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onDragDone";
            }
         };
      }

      return this.onDragDone;
   }

   public Dragboard startDragAndDrop(TransferMode... var1) {
      return this.startDragAndDrop(this, var1);
   }

   public void startFullDrag() {
      this.startFullDrag(this);
   }

   Dragboard startDragAndDrop(EventTarget var1, TransferMode... var2) {
      Toolkit.getToolkit().checkFxUserThread();
      if (this.dndGesture != null && this.dndGesture.dragDetected == Scene.DragDetectedState.PROCESSING) {
         EnumSet var3 = EnumSet.noneOf(TransferMode.class);
         Iterator var4 = InputEventUtils.safeTransferModes(var2).iterator();

         while(var4.hasNext()) {
            TransferMode var5 = (TransferMode)var4.next();
            var3.add(var5);
         }

         return this.dndGesture.startDrag(var1, var3);
      } else {
         throw new IllegalStateException("Cannot start drag and drop outside of DRAG_DETECTED event handler");
      }
   }

   void startFullDrag(EventTarget var1) {
      Toolkit.getToolkit().checkFxUserThread();
      if (this.dndGesture.dragDetected != Scene.DragDetectedState.PROCESSING) {
         throw new IllegalStateException("Cannot start full drag outside of DRAG_DETECTED event handler");
      } else if (this.dndGesture != null) {
         this.dndGesture.startFullPDR(var1);
      } else {
         throw new IllegalStateException("Cannot start full drag when mouse button is not pressed");
      }
   }

   public final void setOnKeyPressed(EventHandler var1) {
      this.onKeyPressedProperty().set(var1);
   }

   public final EventHandler getOnKeyPressed() {
      return this.onKeyPressed == null ? null : (EventHandler)this.onKeyPressed.get();
   }

   public final ObjectProperty onKeyPressedProperty() {
      if (this.onKeyPressed == null) {
         this.onKeyPressed = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(KeyEvent.KEY_PRESSED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onKeyPressed";
            }
         };
      }

      return this.onKeyPressed;
   }

   public final void setOnKeyReleased(EventHandler var1) {
      this.onKeyReleasedProperty().set(var1);
   }

   public final EventHandler getOnKeyReleased() {
      return this.onKeyReleased == null ? null : (EventHandler)this.onKeyReleased.get();
   }

   public final ObjectProperty onKeyReleasedProperty() {
      if (this.onKeyReleased == null) {
         this.onKeyReleased = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(KeyEvent.KEY_RELEASED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onKeyReleased";
            }
         };
      }

      return this.onKeyReleased;
   }

   public final void setOnKeyTyped(EventHandler var1) {
      this.onKeyTypedProperty().set(var1);
   }

   public final EventHandler getOnKeyTyped() {
      return this.onKeyTyped == null ? null : (EventHandler)this.onKeyTyped.get();
   }

   public final ObjectProperty onKeyTypedProperty() {
      if (this.onKeyTyped == null) {
         this.onKeyTyped = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(KeyEvent.KEY_TYPED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onKeyTyped";
            }
         };
      }

      return this.onKeyTyped;
   }

   public final void setOnInputMethodTextChanged(EventHandler var1) {
      this.onInputMethodTextChangedProperty().set(var1);
   }

   public final EventHandler getOnInputMethodTextChanged() {
      return this.onInputMethodTextChanged == null ? null : (EventHandler)this.onInputMethodTextChanged.get();
   }

   public final ObjectProperty onInputMethodTextChangedProperty() {
      if (this.onInputMethodTextChanged == null) {
         this.onInputMethodTextChanged = new ObjectPropertyBase() {
            protected void invalidated() {
               Scene.this.setEventHandler(InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, (EventHandler)this.get());
            }

            public Object getBean() {
               return Scene.this;
            }

            public String getName() {
               return "onInputMethodTextChanged";
            }
         };
      }

      return this.onInputMethodTextChanged;
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

   public final void setNodeOrientation(NodeOrientation var1) {
      this.nodeOrientationProperty().set(var1);
   }

   public final NodeOrientation getNodeOrientation() {
      return this.nodeOrientation == null ? defaultNodeOrientation : (NodeOrientation)this.nodeOrientation.get();
   }

   public final ObjectProperty nodeOrientationProperty() {
      if (this.nodeOrientation == null) {
         this.nodeOrientation = new StyleableObjectProperty(defaultNodeOrientation) {
            protected void invalidated() {
               Scene.this.sceneEffectiveOrientationInvalidated();
               Scene.this.getRoot().applyCss();
            }

            public Object getBean() {
               return Scene.this;
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
      if (this.effectiveNodeOrientation == null) {
         this.effectiveNodeOrientation = this.calcEffectiveNodeOrientation();
      }

      return this.effectiveNodeOrientation;
   }

   public final ReadOnlyObjectProperty effectiveNodeOrientationProperty() {
      if (this.effectiveNodeOrientationProperty == null) {
         this.effectiveNodeOrientationProperty = new EffectiveOrientationProperty();
      }

      return this.effectiveNodeOrientationProperty;
   }

   private void parentEffectiveOrientationInvalidated() {
      if (this.getNodeOrientation() == NodeOrientation.INHERIT) {
         this.sceneEffectiveOrientationInvalidated();
      }

   }

   private void sceneEffectiveOrientationInvalidated() {
      this.effectiveNodeOrientation = null;
      if (this.effectiveNodeOrientationProperty != null) {
         this.effectiveNodeOrientationProperty.invalidate();
      }

      this.getRoot().parentResolvedOrientationInvalidated();
   }

   private NodeOrientation calcEffectiveNodeOrientation() {
      NodeOrientation var1 = this.getNodeOrientation();
      if (var1 == NodeOrientation.INHERIT) {
         Window var2 = this.getWindow();
         if (var2 != null) {
            Window var3 = null;
            if (var2 instanceof Stage) {
               var3 = ((Stage)var2).getOwner();
            } else if (var2 instanceof PopupWindow) {
               var3 = ((PopupWindow)var2).getOwnerWindow();
            }

            if (var3 != null) {
               Scene var4 = var3.getScene();
               if (var4 != null) {
                  return var4.getEffectiveNodeOrientation();
               }
            }
         }

         return NodeOrientation.LEFT_TO_RIGHT;
      } else {
         return var1;
      }
   }

   Accessible removeAccessible(Node var1) {
      return this.accMap == null ? null : (Accessible)this.accMap.remove(var1);
   }

   void addAccessible(Node var1, Accessible var2) {
      if (this.accMap == null) {
         this.accMap = new HashMap();
      }

      this.accMap.put(var1, var2);
   }

   private void disposeAccessibles() {
      if (this.accMap != null) {
         Iterator var1 = this.accMap.entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry var2 = (Map.Entry)var1.next();
            Node var3 = (Node)var2.getKey();
            Accessible var4 = (Accessible)var2.getValue();
            if (var3.accessible != null) {
               if (var3.accessible == var4) {
                  System.err.println("[A11y] 'node.accessible == acc' should never happen.");
               }

               if (var3.getScene() == this) {
                  System.err.println("[A11y] 'node.getScene() == this' should never happen.");
               }

               var4.dispose();
            } else if (var3.getScene() == this) {
               var3.accessible = var4;
            } else {
               var4.dispose();
            }
         }

         this.accMap.clear();
      }

   }

   Accessible getAccessible() {
      if (this.impl_peer == null) {
         return null;
      } else {
         if (this.accessible == null) {
            this.accessible = Application.GetApplication().createAccessible();
            this.accessible.setEventHandler(new Accessible.EventHandler() {
               public AccessControlContext getAccessControlContext() {
                  return Scene.this.impl_getPeer().getAccessControlContext();
               }

               public Object getAttribute(AccessibleAttribute var1, Object... var2) {
                  Window var3;
                  switch (var1) {
                     case CHILDREN:
                        Parent var7 = Scene.this.getRoot();
                        if (var7 != null) {
                           return FXCollections.observableArrayList((Object[])(var7));
                        }
                        break;
                     case TEXT:
                        var3 = Scene.this.getWindow();
                        if (var3 instanceof Stage) {
                           return ((Stage)var3).getTitle();
                        }
                        break;
                     case NODE_AT_POINT:
                        var3 = Scene.this.getWindow();
                        Point2D var4 = (Point2D)var2[0];
                        PickResult var5 = Scene.this.pick(var4.getX() - Scene.this.getX() - var3.getX(), var4.getY() - Scene.this.getY() - var3.getY());
                        if (var5 != null) {
                           Node var6 = var5.getIntersectedNode();
                           if (var6 != null) {
                              return var6;
                           }
                        }

                        return Scene.this.getRoot();
                     case ROLE:
                        return AccessibleRole.PARENT;
                     case SCENE:
                        return Scene.this;
                     case FOCUS_NODE:
                        if (Scene.this.transientFocusContainer != null) {
                           return Scene.this.transientFocusContainer.queryAccessibleAttribute(AccessibleAttribute.FOCUS_NODE);
                        }

                        return Scene.this.getFocusOwner();
                  }

                  return super.getAttribute(var1, var2);
               }
            });
            PlatformImpl.accessibilityActiveProperty().set(true);
         }

         return this.accessible;
      }
   }

   static {
      PerformanceTracker.setSceneAccessor(new PerformanceTracker.SceneAccessor() {
         public void setPerfTracker(Scene var1, PerformanceTracker var2) {
            synchronized(Scene.trackerMonitor) {
               var1.tracker = var2;
            }
         }

         public PerformanceTracker getPerfTracker(Scene var1) {
            synchronized(Scene.trackerMonitor) {
               return var1.tracker;
            }
         }
      });
      FXRobotHelper.setSceneAccessor(new FXRobotHelper.FXRobotSceneAccessor() {
         public void processKeyEvent(Scene var1, KeyEvent var2) {
            var1.impl_processKeyEvent(var2);
         }

         public void processMouseEvent(Scene var1, MouseEvent var2) {
            var1.impl_processMouseEvent(var2);
         }

         public void processScrollEvent(Scene var1, ScrollEvent var2) {
            var1.processGestureEvent(var2, var1.scrollGesture);
         }

         public ObservableList getChildren(Parent var1) {
            return var1.getChildren();
         }

         public Object renderToImage(Scene var1, Object var2) {
            return var1.snapshot((WritableImage)null).impl_getPlatformImage();
         }
      });
      SceneHelper.setSceneAccessor(new SceneHelper.SceneAccessor() {
         public void setPaused(boolean var1) {
            Scene.paused = var1;
         }

         public void parentEffectiveOrientationInvalidated(Scene var1) {
            var1.parentEffectiveOrientationInvalidated();
         }

         public Camera getEffectiveCamera(Scene var1) {
            return var1.getEffectiveCamera();
         }

         public Scene createPopupScene(Parent var1) {
            return new Scene(var1) {
               void doLayoutPass() {
                  this.resizeRootToPreferredSize(this.getRoot());
                  super.doLayoutPass();
               }

               void resizeRootOnSceneSizeChange(double var1, double var3) {
               }
            };
         }

         public void setTransientFocusContainer(Scene var1, Node var2) {
            if (var1 != null) {
               var1.transientFocusContainer = var2;
            }

         }

         public Accessible getAccessible(Scene var1) {
            return var1.getAccessible();
         }
      });
      inSynchronizer = false;
      inMousePick = false;
      allowPGAccess = false;
      pgAccessCount = 0;
      paused = false;
      snapshotPulseListener = null;
      trackerMonitor = new Object();
      USER_DATA_KEY = new Object();
      defaultNodeOrientation = (Boolean)AccessController.doPrivileged(() -> {
         return Boolean.getBoolean("javafx.scene.nodeOrientation.RTL");
      }) ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.INHERIT;
   }

   private final class EffectiveOrientationProperty extends ReadOnlyObjectPropertyBase {
      private EffectiveOrientationProperty() {
      }

      public NodeOrientation get() {
         return Scene.this.getEffectiveNodeOrientation();
      }

      public Object getBean() {
         return Scene.this;
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

   private static class TargetWrapper {
      private Scene scene;
      private Node node;
      private PickResult result;

      private TargetWrapper() {
      }

      public void fillHierarchy(List var1) {
         var1.clear();

         Parent var3;
         for(Object var2 = this.node; var2 != null; var2 = var3 != null ? var3 : ((Node)var2).getSubScene()) {
            var1.add(var2);
            var3 = ((Node)var2).getParent();
         }

         if (this.scene != null) {
            var1.add(this.scene);
         }

      }

      public EventTarget getEventTarget() {
         return (EventTarget)(this.node != null ? this.node : this.scene);
      }

      public Cursor getCursor() {
         Cursor var1 = null;
         if (this.node != null) {
            var1 = this.node.getCursor();

            Parent var3;
            for(Object var2 = this.node.getParent(); var1 == null && var2 != null; var2 = var3 != null ? var3 : ((Node)var2).getSubScene()) {
               var1 = ((Node)var2).getCursor();
               var3 = ((Node)var2).getParent();
            }
         }

         return var1;
      }

      public void clear() {
         this.set((Node)null, (Scene)null);
         this.result = null;
      }

      public void setNodeResult(PickResult var1) {
         if (var1 != null) {
            this.result = var1;
            Node var2 = var1.getIntersectedNode();
            this.set(var2, var2.getScene());
         }

      }

      public void setSceneResult(PickResult var1, Scene var2) {
         if (var1 != null) {
            this.result = var1;
            this.set((Node)null, var2);
         }

      }

      public PickResult getResult() {
         return this.result;
      }

      public void copy(TargetWrapper var1) {
         this.node = var1.node;
         this.scene = var1.scene;
         this.result = var1.result;
      }

      private void set(Node var1, Scene var2) {
         this.node = var1;
         this.scene = var2;
      }

      // $FF: synthetic method
      TargetWrapper(Object var1) {
         this();
      }
   }

   private static class TouchMap {
      private static final int FAST_THRESHOLD = 10;
      int[] fastMap;
      Map slowMap;
      List order;
      List removed;
      int counter;
      int active;

      private TouchMap() {
         this.fastMap = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
         this.slowMap = new HashMap();
         this.order = new LinkedList();
         this.removed = new ArrayList(10);
         this.counter = 0;
         this.active = 0;
      }

      public int add(long var1) {
         ++this.counter;
         ++this.active;
         if (var1 < 10L) {
            this.fastMap[(int)var1] = this.counter;
         } else {
            this.slowMap.put(var1, this.counter);
         }

         this.order.add(this.counter);
         return this.counter;
      }

      public void remove(long var1) {
         this.removed.add(var1);
      }

      public int get(long var1) {
         if (var1 < 10L) {
            int var3 = this.fastMap[(int)var1];
            if (var3 == 0) {
               throw new RuntimeException("Platform reported wrong touch point ID");
            } else {
               return var3;
            }
         } else {
            try {
               return (Integer)this.slowMap.get(var1);
            } catch (NullPointerException var4) {
               throw new RuntimeException("Platform reported wrong touch point ID");
            }
         }
      }

      public int getOrder(int var1) {
         return this.order.indexOf(var1);
      }

      public boolean cleanup() {
         Iterator var1 = this.removed.iterator();

         while(var1.hasNext()) {
            long var2 = (Long)var1.next();
            --this.active;
            this.order.remove(this.get(var2));
            if (var2 < 10L) {
               this.fastMap[(int)var2] = 0;
            } else {
               this.slowMap.remove(var2);
            }

            if (this.active == 0) {
               this.counter = 0;
            }
         }

         this.removed.clear();
         return this.active == 0;
      }

      // $FF: synthetic method
      TouchMap(Object var1) {
         this();
      }
   }

   class InputMethodRequestsDelegate implements ExtendedInputMethodRequests {
      public Point2D getTextLocation(int var1) {
         InputMethodRequests var2 = this.getClientRequests();
         return var2 != null ? var2.getTextLocation(var1) : new Point2D(0.0, 0.0);
      }

      public int getLocationOffset(int var1, int var2) {
         InputMethodRequests var3 = this.getClientRequests();
         return var3 != null ? var3.getLocationOffset(var1, var2) : 0;
      }

      public void cancelLatestCommittedText() {
         InputMethodRequests var1 = this.getClientRequests();
         if (var1 != null) {
            var1.cancelLatestCommittedText();
         }

      }

      public String getSelectedText() {
         InputMethodRequests var1 = this.getClientRequests();
         return var1 != null ? var1.getSelectedText() : null;
      }

      public int getInsertPositionOffset() {
         InputMethodRequests var1 = this.getClientRequests();
         return var1 != null && var1 instanceof ExtendedInputMethodRequests ? ((ExtendedInputMethodRequests)var1).getInsertPositionOffset() : 0;
      }

      public String getCommittedText(int var1, int var2) {
         InputMethodRequests var3 = this.getClientRequests();
         return var3 != null && var3 instanceof ExtendedInputMethodRequests ? ((ExtendedInputMethodRequests)var3).getCommittedText(var1, var2) : null;
      }

      public int getCommittedTextLength() {
         InputMethodRequests var1 = this.getClientRequests();
         return var1 != null && var1 instanceof ExtendedInputMethodRequests ? ((ExtendedInputMethodRequests)var1).getCommittedTextLength() : 0;
      }

      private InputMethodRequests getClientRequests() {
         Node var1 = Scene.this.getFocusOwner();
         return var1 != null ? var1.getInputMethodRequests() : null;
      }
   }

   class KeyHandler {
      private boolean windowFocused;
      private final InvalidationListener sceneWindowFocusedListener = (var1x) -> {
         this.setWindowFocused(((ReadOnlyBooleanProperty)var1x).get());
      };

      private void setFocusOwner(Node var1) {
         if (Scene.this.oldFocusOwner != null) {
            Scene var2 = Scene.this.oldFocusOwner.getScene();
            if (var2 != null) {
               TKScene var3 = var2.impl_getPeer();
               if (var3 != null) {
                  var3.finishInputMethodComposition();
               }
            }
         }

         Scene.this.focusOwner.set(var1);
      }

      protected boolean isWindowFocused() {
         return this.windowFocused;
      }

      protected void setWindowFocused(boolean var1) {
         this.windowFocused = var1;
         if (Scene.this.getFocusOwner() != null) {
            Scene.this.getFocusOwner().setFocused(this.windowFocused);
         }

         if (this.windowFocused && Scene.this.accessible != null) {
            Scene.this.accessible.sendNotification(AccessibleAttribute.FOCUS_NODE);
         }

      }

      private void windowForSceneChanged(Window var1, Window var2) {
         if (var1 != null) {
            var1.focusedProperty().removeListener(this.sceneWindowFocusedListener);
         }

         if (var2 != null) {
            var2.focusedProperty().addListener(this.sceneWindowFocusedListener);
            this.setWindowFocused(var2.isFocused());
         } else {
            this.setWindowFocused(false);
         }

      }

      private void process(KeyEvent var1) {
         Node var2 = Scene.this.getFocusOwner();
         Object var3 = var2 != null ? var2 : Scene.this;
         Event.fireEvent((EventTarget)var3, var1);
      }

      private void requestFocus(Node var1) {
         if (Scene.this.getFocusOwner() != var1 && (var1 == null || var1.isCanReceiveFocus())) {
            this.setFocusOwner(var1);
         }
      }
   }

   class MouseHandler {
      private TargetWrapper pdrEventTarget = new TargetWrapper();
      private boolean pdrInProgress = false;
      private boolean fullPDREntered = false;
      private EventTarget currentEventTarget = null;
      private MouseEvent lastEvent;
      private boolean hover = false;
      private boolean primaryButtonDown = false;
      private boolean secondaryButtonDown = false;
      private boolean middleButtonDown = false;
      private EventTarget fullPDRSource = null;
      private TargetWrapper fullPDRTmpTargetWrapper = new TargetWrapper();
      private final List pdrEventTargets = new ArrayList();
      private final List currentEventTargets = new ArrayList();
      private final List newEventTargets = new ArrayList();
      private final List fullPDRCurrentEventTargets = new ArrayList();
      private final List fullPDRNewEventTargets = new ArrayList();
      private EventTarget fullPDRCurrentTarget = null;
      private Cursor currCursor;
      private CursorFrame currCursorFrame;
      private EventQueue queue = new EventQueue();
      private Runnable pickProcess = new Runnable() {
         public void run() {
            if (Scene.this.impl_peer != null && MouseHandler.this.lastEvent != null) {
               MouseHandler.this.process(MouseHandler.this.lastEvent, true);
            }

         }
      };

      private void pulse() {
         if (this.hover && this.lastEvent != null) {
            Platform.runLater(this.pickProcess);
         }

      }

      private void clearPDREventTargets() {
         this.pdrInProgress = false;
         this.currentEventTarget = this.currentEventTargets.size() > 0 ? (EventTarget)this.currentEventTargets.get(0) : null;
         this.pdrEventTarget.clear();
      }

      public void enterFullPDR(EventTarget var1) {
         this.fullPDREntered = true;
         this.fullPDRSource = var1;
         this.fullPDRCurrentTarget = null;
         this.fullPDRCurrentEventTargets.clear();
      }

      public void exitFullPDR(MouseEvent var1) {
         if (this.fullPDREntered) {
            this.fullPDREntered = false;

            for(int var2 = this.fullPDRCurrentEventTargets.size() - 1; var2 >= 0; --var2) {
               EventTarget var3 = (EventTarget)this.fullPDRCurrentEventTargets.get(var2);
               Event.fireEvent(var3, MouseEvent.copyForMouseDragEvent(var1, var3, var3, MouseDragEvent.MOUSE_DRAG_EXITED_TARGET, this.fullPDRSource, var1.getPickResult()));
            }

            this.fullPDRSource = null;
            this.fullPDRCurrentEventTargets.clear();
            this.fullPDRCurrentTarget = null;
         }
      }

      private void handleNodeRemoval(Node var1) {
         if (this.lastEvent != null) {
            int var2;
            EventTarget var3;
            if (this.currentEventTargets.contains(var1)) {
               var2 = 0;
               var3 = null;

               while(var3 != var1) {
                  var3 = (EventTarget)this.currentEventTargets.get(var2++);
                  this.queue.postEvent(this.lastEvent.copyFor(var3, var3, MouseEvent.MOUSE_EXITED_TARGET));
               }

               this.currentEventTargets.subList(0, var2).clear();
            }

            if (this.fullPDREntered && this.fullPDRCurrentEventTargets.contains(var1)) {
               var2 = 0;
               var3 = null;

               while(var3 != var1) {
                  var3 = (EventTarget)this.fullPDRCurrentEventTargets.get(var2++);
                  this.queue.postEvent(MouseEvent.copyForMouseDragEvent(this.lastEvent, var3, var3, MouseDragEvent.MOUSE_DRAG_EXITED_TARGET, this.fullPDRSource, this.lastEvent.getPickResult()));
               }

               this.fullPDRCurrentEventTargets.subList(0, var2).clear();
            }

            this.queue.fire();
            if (this.pdrInProgress && this.pdrEventTargets.contains(var1)) {
               var2 = 0;
               var3 = null;

               while(var3 != var1) {
                  var3 = (EventTarget)this.pdrEventTargets.get(var2++);
                  ((Node)var3).setPressed(false);
               }

               this.pdrEventTargets.subList(0, var2).clear();
               var3 = (EventTarget)this.pdrEventTargets.get(0);
               PickResult var4 = this.pdrEventTarget.getResult();
               if (var3 instanceof Node) {
                  this.pdrEventTarget.setNodeResult(new PickResult((Node)var3, var4.getIntersectedPoint(), var4.getIntersectedDistance()));
               } else {
                  this.pdrEventTarget.setSceneResult(new PickResult((Node)null, var4.getIntersectedPoint(), var4.getIntersectedDistance()), (Scene)var3);
               }
            }

         }
      }

      private void handleEnterExit(MouseEvent var1, TargetWrapper var2) {
         if (var2.getEventTarget() != this.currentEventTarget || var1.getEventType() == MouseEvent.MOUSE_EXITED) {
            if (var1.getEventType() == MouseEvent.MOUSE_EXITED) {
               this.newEventTargets.clear();
            } else {
               var2.fillHierarchy(this.newEventTargets);
            }

            int var3 = this.newEventTargets.size();
            int var4 = this.currentEventTargets.size() - 1;
            int var5 = var3 - 1;

            int var6;
            for(var6 = this.pdrEventTargets.size() - 1; var4 >= 0 && var5 >= 0 && this.currentEventTargets.get(var4) == this.newEventTargets.get(var5); --var6) {
               --var4;
               --var5;
            }

            EventTarget var8;
            while(var4 >= 0) {
               var8 = (EventTarget)this.currentEventTargets.get(var4);
               if (this.pdrInProgress && (var6 < 0 || var8 != this.pdrEventTargets.get(var6))) {
                  break;
               }

               this.queue.postEvent(var1.copyFor(var8, var8, MouseEvent.MOUSE_EXITED_TARGET));
               --var4;
               --var6;
            }

            for(var6 = var6; var5 >= 0; --var6) {
               var8 = (EventTarget)this.newEventTargets.get(var5);
               if (this.pdrInProgress && (var6 < 0 || var8 != this.pdrEventTargets.get(var6))) {
                  break;
               }

               this.queue.postEvent(var1.copyFor(var8, var8, MouseEvent.MOUSE_ENTERED_TARGET));
               --var5;
            }

            this.currentEventTarget = var2.getEventTarget();
            this.currentEventTargets.clear();
            ++var5;

            while(var5 < var3) {
               this.currentEventTargets.add(this.newEventTargets.get(var5));
               ++var5;
            }
         }

         this.queue.fire();
      }

      private void process(MouseEvent var1, boolean var2) {
         Toolkit.getToolkit().checkFxUserThread();
         Scene.inMousePick = true;
         Scene.this.cursorScreenPos = new Point2D(var1.getScreenX(), var1.getScreenY());
         Scene.this.cursorScenePos = new Point2D(var1.getSceneX(), var1.getSceneY());
         boolean var3 = false;
         if (!var2) {
            if (var1.getEventType() == MouseEvent.MOUSE_PRESSED) {
               if (!this.primaryButtonDown && !this.secondaryButtonDown && !this.middleButtonDown) {
                  var3 = true;
                  Scene.this.dndGesture = Scene.this.new DnDGesture();
                  this.clearPDREventTargets();
               }
            } else if (var1.getEventType() == MouseEvent.MOUSE_MOVED) {
               this.clearPDREventTargets();
            } else if (var1.getEventType() == MouseEvent.MOUSE_ENTERED) {
               this.hover = true;
            } else if (var1.getEventType() == MouseEvent.MOUSE_EXITED) {
               this.hover = false;
            }

            this.primaryButtonDown = var1.isPrimaryButtonDown();
            this.secondaryButtonDown = var1.isSecondaryButtonDown();
            this.middleButtonDown = var1.isMiddleButtonDown();
         }

         Scene.this.pick(Scene.this.tmpTargetWrapper, var1.getSceneX(), var1.getSceneY());
         PickResult var4 = Scene.this.tmpTargetWrapper.getResult();
         if (var4 != null) {
            var1 = new MouseEvent(var1.getEventType(), var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), var1.getButton(), var1.getClickCount(), var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), var1.isPrimaryButtonDown(), var1.isMiddleButtonDown(), var1.isSecondaryButtonDown(), var1.isSynthesized(), var1.isPopupTrigger(), var1.isStillSincePress(), var4);
         }

         if (var1.getEventType() == MouseEvent.MOUSE_EXITED) {
            Scene.this.tmpTargetWrapper.clear();
         }

         TargetWrapper var5;
         if (this.pdrInProgress) {
            var5 = this.pdrEventTarget;
         } else {
            var5 = Scene.this.tmpTargetWrapper;
         }

         if (var3) {
            this.pdrEventTarget.copy(var5);
            this.pdrEventTarget.fillHierarchy(this.pdrEventTargets);
         }

         if (!var2) {
            var1 = Scene.this.clickGenerator.preProcess(var1);
         }

         this.handleEnterExit(var1, Scene.this.tmpTargetWrapper);
         if (Scene.this.dndGesture != null) {
            Scene.this.dndGesture.processDragDetection(var1);
         }

         if (this.fullPDREntered && var1.getEventType() == MouseEvent.MOUSE_RELEASED) {
            this.processFullPDR(var1, var2);
         }

         if (var5.getEventTarget() != null && var1.getEventType() != MouseEvent.MOUSE_ENTERED && var1.getEventType() != MouseEvent.MOUSE_EXITED && !var2) {
            Event.fireEvent(var5.getEventTarget(), var1);
         }

         if (this.fullPDREntered && var1.getEventType() != MouseEvent.MOUSE_RELEASED) {
            this.processFullPDR(var1, var2);
         }

         if (!var2) {
            Scene.this.clickGenerator.postProcess(var1, var5, Scene.this.tmpTargetWrapper);
         }

         if (!var2 && Scene.this.dndGesture != null && !Scene.this.dndGesture.process(var1, var5.getEventTarget())) {
            Scene.this.dndGesture = null;
         }

         Cursor var6 = var5.getCursor();
         if (var1.getEventType() != MouseEvent.MOUSE_EXITED) {
            if (var6 == null && this.hover) {
               var6 = Scene.this.getCursor();
            }

            this.updateCursor(var6);
            this.updateCursorFrame();
         }

         if (var3) {
            this.pdrInProgress = true;
         }

         if (this.pdrInProgress && !this.primaryButtonDown && !this.secondaryButtonDown && !this.middleButtonDown) {
            this.clearPDREventTargets();
            this.exitFullPDR(var1);
            Scene.this.pick(Scene.this.tmpTargetWrapper, var1.getSceneX(), var1.getSceneY());
            this.handleEnterExit(var1, Scene.this.tmpTargetWrapper);
         }

         this.lastEvent = var1.getEventType() == MouseEvent.MOUSE_EXITED ? null : var1;
         Scene.inMousePick = false;
      }

      private void processFullPDR(MouseEvent var1, boolean var2) {
         Scene.this.pick(this.fullPDRTmpTargetWrapper, var1.getSceneX(), var1.getSceneY());
         PickResult var3 = this.fullPDRTmpTargetWrapper.getResult();
         EventTarget var4 = this.fullPDRTmpTargetWrapper.getEventTarget();
         if (var4 != this.fullPDRCurrentTarget) {
            this.fullPDRTmpTargetWrapper.fillHierarchy(this.fullPDRNewEventTargets);
            int var5 = this.fullPDRNewEventTargets.size();
            int var6 = this.fullPDRCurrentEventTargets.size() - 1;

            int var7;
            for(var7 = var5 - 1; var6 >= 0 && var7 >= 0 && this.fullPDRCurrentEventTargets.get(var6) == this.fullPDRNewEventTargets.get(var7); --var7) {
               --var6;
            }

            EventTarget var8;
            while(var6 >= 0) {
               var8 = (EventTarget)this.fullPDRCurrentEventTargets.get(var6);
               Event.fireEvent(var8, MouseEvent.copyForMouseDragEvent(var1, var8, var8, MouseDragEvent.MOUSE_DRAG_EXITED_TARGET, this.fullPDRSource, var3));
               --var6;
            }

            while(var7 >= 0) {
               var8 = (EventTarget)this.fullPDRNewEventTargets.get(var7);
               Event.fireEvent(var8, MouseEvent.copyForMouseDragEvent(var1, var8, var8, MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET, this.fullPDRSource, var3));
               --var7;
            }

            this.fullPDRCurrentTarget = var4;
            this.fullPDRCurrentEventTargets.clear();
            this.fullPDRCurrentEventTargets.addAll(this.fullPDRNewEventTargets);
            this.fullPDRNewEventTargets.clear();
         }

         if (var4 != null && !var2) {
            if (var1.getEventType() == MouseEvent.MOUSE_DRAGGED) {
               Event.fireEvent(var4, MouseEvent.copyForMouseDragEvent(var1, var4, var4, MouseDragEvent.MOUSE_DRAG_OVER, this.fullPDRSource, var3));
            }

            if (var1.getEventType() == MouseEvent.MOUSE_RELEASED) {
               Event.fireEvent(var4, MouseEvent.copyForMouseDragEvent(var1, var4, var4, MouseDragEvent.MOUSE_DRAG_RELEASED, this.fullPDRSource, var3));
            }
         }

      }

      private void updateCursor(Cursor var1) {
         if (this.currCursor != var1) {
            if (this.currCursor != null) {
               this.currCursor.deactivate();
            }

            if (var1 != null) {
               var1.activate();
            }

            this.currCursor = var1;
         }

      }

      public void updateCursorFrame() {
         CursorFrame var1 = this.currCursor != null ? this.currCursor.getCurrentFrame() : Cursor.DEFAULT.getCurrentFrame();
         if (this.currCursorFrame != var1) {
            if (Scene.this.impl_peer != null) {
               Scene.this.impl_peer.setCursor(var1);
            }

            this.currCursorFrame = var1;
         }

      }

      private PickResult pickNode(PickRay var1) {
         PickResultChooser var2 = new PickResultChooser();
         Scene.this.getRoot().impl_pickNode(var1, var2);
         return var2.toPickResult();
      }
   }

   static class ClickGenerator {
      private ClickCounter lastPress = null;
      private Map counters = new EnumMap(MouseButton.class);
      private List pressedTargets = new ArrayList();
      private List releasedTargets = new ArrayList();

      public ClickGenerator() {
         MouseButton[] var1 = MouseButton.values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MouseButton var4 = var1[var3];
            if (var4 != MouseButton.NONE) {
               this.counters.put(var4, new ClickCounter());
            }
         }

      }

      private MouseEvent preProcess(MouseEvent var1) {
         Iterator var2 = this.counters.values().iterator();

         while(var2.hasNext()) {
            ClickCounter var3 = (ClickCounter)var2.next();
            var3.moved(var1.getSceneX(), var1.getSceneY());
         }

         ClickCounter var4 = (ClickCounter)this.counters.get(var1.getButton());
         boolean var5 = this.lastPress != null ? this.lastPress.isStill() : false;
         if (var1.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (!var1.isPrimaryButtonDown()) {
               ((ClickCounter)this.counters.get(MouseButton.PRIMARY)).clear();
            }

            if (!var1.isSecondaryButtonDown()) {
               ((ClickCounter)this.counters.get(MouseButton.SECONDARY)).clear();
            }

            if (!var1.isMiddleButtonDown()) {
               ((ClickCounter)this.counters.get(MouseButton.MIDDLE)).clear();
            }

            var4.applyOut();
            var4.inc();
            var4.start(var1.getSceneX(), var1.getSceneY());
            this.lastPress = var4;
         }

         return new MouseEvent(var1.getEventType(), var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), var1.getButton(), var4 != null && var1.getEventType() != MouseEvent.MOUSE_MOVED ? var4.get() : 0, var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), var1.isPrimaryButtonDown(), var1.isMiddleButtonDown(), var1.isSecondaryButtonDown(), var1.isSynthesized(), var1.isPopupTrigger(), var5, var1.getPickResult());
      }

      private void postProcess(MouseEvent var1, TargetWrapper var2, TargetWrapper var3) {
         if (var1.getEventType() == MouseEvent.MOUSE_RELEASED) {
            ClickCounter var4 = (ClickCounter)this.counters.get(var1.getButton());
            var2.fillHierarchy(this.pressedTargets);
            var3.fillHierarchy(this.releasedTargets);
            int var5 = this.pressedTargets.size() - 1;
            int var6 = this.releasedTargets.size() - 1;

            EventTarget var7;
            for(var7 = null; var5 >= 0 && var6 >= 0 && this.pressedTargets.get(var5) == this.releasedTargets.get(var6); --var6) {
               var7 = (EventTarget)this.pressedTargets.get(var5);
               --var5;
            }

            this.pressedTargets.clear();
            this.releasedTargets.clear();
            if (var7 != null && this.lastPress != null) {
               MouseEvent var8 = new MouseEvent((Object)null, var7, MouseEvent.MOUSE_CLICKED, var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), var1.getButton(), var4.get(), var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), var1.isPrimaryButtonDown(), var1.isMiddleButtonDown(), var1.isSecondaryButtonDown(), var1.isSynthesized(), var1.isPopupTrigger(), this.lastPress.isStill(), var1.getPickResult());
               Event.fireEvent(var7, var8);
            }
         }

      }
   }

   static class ClickCounter {
      Toolkit toolkit = Toolkit.getToolkit();
      private int count;
      private boolean out;
      private boolean still;
      private Timeline timeout;
      private double pressedX;
      private double pressedY;

      private void inc() {
         ++this.count;
      }

      private int get() {
         return this.count;
      }

      private boolean isStill() {
         return this.still;
      }

      private void clear() {
         this.count = 0;
         this.stopTimeout();
      }

      private void out() {
         this.out = true;
         this.stopTimeout();
      }

      private void applyOut() {
         if (this.out) {
            this.clear();
         }

         this.out = false;
      }

      private void moved(double var1, double var3) {
         if (Math.abs(var1 - this.pressedX) > (double)this.toolkit.getMultiClickMaxX() || Math.abs(var3 - this.pressedY) > (double)this.toolkit.getMultiClickMaxY()) {
            this.out();
            this.still = false;
         }

      }

      private void start(double var1, double var3) {
         this.pressedX = var1;
         this.pressedY = var3;
         this.out = false;
         if (this.timeout != null) {
            this.timeout.stop();
         }

         this.timeout = new Timeline();
         this.timeout.getKeyFrames().add(new KeyFrame(new Duration((double)this.toolkit.getMultiClickTime()), (var1x) -> {
            this.out = true;
            this.timeout = null;
         }, new KeyValue[0]));
         this.timeout.play();
         this.still = true;
      }

      private void stopTimeout() {
         if (this.timeout != null) {
            this.timeout.stop();
            this.timeout = null;
         }

      }
   }

   class DragSourceListener implements TKDragSourceListener {
      public void dragDropEnd(double var1, double var3, double var5, double var7, TransferMode var9) {
         if (Scene.this.dndGesture != null) {
            if (Scene.this.dndGesture.dragboard == null) {
               throw new RuntimeException("dndGesture.dragboard is null in dragDropEnd");
            }

            DragEvent var10 = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, var1, var3, var5, var7, var9, (Object)null, (Object)null, (PickResult)null);
            DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, false);

            try {
               Scene.this.dndGesture.processDropEnd(var10);
            } finally {
               DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, true);
            }

            Scene.this.dndGesture = null;
         }

      }
   }

   private static enum DragDetectedState {
      NOT_YET,
      PROCESSING,
      DONE;
   }

   class DnDGesture {
      private final double hysteresisSizeX = (double)Toolkit.getToolkit().getMultiClickMaxX();
      private final double hysteresisSizeY = (double)Toolkit.getToolkit().getMultiClickMaxY();
      private EventTarget source = null;
      private Set sourceTransferModes = null;
      private TransferMode acceptedTransferMode = null;
      private Dragboard dragboard = null;
      private EventTarget potentialTarget = null;
      private EventTarget target = null;
      private DragDetectedState dragDetected;
      private double pressedX;
      private double pressedY;
      private List currentTargets;
      private List newTargets;
      private EventTarget fullPDRSource;

      DnDGesture() {
         this.dragDetected = Scene.DragDetectedState.NOT_YET;
         this.currentTargets = new ArrayList();
         this.newTargets = new ArrayList();
         this.fullPDRSource = null;
      }

      private void fireEvent(EventTarget var1, Event var2) {
         if (var1 != null) {
            Event.fireEvent(var1, var2);
         }

      }

      private void processingDragDetected() {
         this.dragDetected = Scene.DragDetectedState.PROCESSING;
      }

      private void dragDetectedProcessed() {
         this.dragDetected = Scene.DragDetectedState.DONE;
         boolean var1 = this.dragboard != null && this.dragboard.impl_contentPut();
         if (var1) {
            Toolkit.getToolkit().startDrag(Scene.this.impl_peer, this.sourceTransferModes, Scene.this.new DragSourceListener(), this.dragboard);
         } else if (this.fullPDRSource != null) {
            Scene.this.mouseHandler.enterFullPDR(this.fullPDRSource);
         }

         this.fullPDRSource = null;
      }

      private void processDragDetection(MouseEvent var1) {
         if (this.dragDetected != Scene.DragDetectedState.NOT_YET) {
            var1.setDragDetect(false);
         } else {
            if (var1.getEventType() == MouseEvent.MOUSE_PRESSED) {
               this.pressedX = var1.getSceneX();
               this.pressedY = var1.getSceneY();
               var1.setDragDetect(false);
            } else if (var1.getEventType() == MouseEvent.MOUSE_DRAGGED) {
               double var2 = Math.abs(var1.getSceneX() - this.pressedX);
               double var4 = Math.abs(var1.getSceneY() - this.pressedY);
               var1.setDragDetect(var2 > this.hysteresisSizeX || var4 > this.hysteresisSizeY);
            }

         }
      }

      private boolean process(MouseEvent var1, EventTarget var2) {
         boolean var3 = true;
         if (this.dragDetected != Scene.DragDetectedState.DONE && (var1.getEventType() == MouseEvent.MOUSE_PRESSED || var1.getEventType() == MouseEvent.MOUSE_DRAGGED) && var1.isDragDetect()) {
            this.processingDragDetected();
            if (var2 != null) {
               MouseEvent var4 = var1.copyFor(var1.getSource(), var2, MouseEvent.DRAG_DETECTED);

               try {
                  this.fireEvent(var2, var4);
               } finally {
                  if (this.dragboard != null) {
                     DragboardHelper.setDataAccessRestriction(this.dragboard, true);
                  }

               }
            }

            this.dragDetectedProcessed();
         }

         if (var1.getEventType() == MouseEvent.MOUSE_RELEASED) {
            var3 = false;
         }

         return var3;
      }

      private boolean processRecognized(DragEvent var1) {
         MouseEvent var2 = new MouseEvent(MouseEvent.DRAG_DETECTED, var1.getX(), var1.getY(), var1.getSceneX(), var1.getScreenY(), MouseButton.PRIMARY, 1, false, false, false, false, false, true, false, false, false, false, var1.getPickResult());
         this.processingDragDetected();
         Node var3 = var1.getPickResult().getIntersectedNode();

         try {
            this.fireEvent(var3 != null ? var3 : Scene.this, var2);
         } finally {
            if (this.dragboard != null) {
               DragboardHelper.setDataAccessRestriction(this.dragboard, true);
            }

         }

         this.dragDetectedProcessed();
         boolean var4 = this.dragboard != null && !this.dragboard.getContentTypes().isEmpty();
         return var4;
      }

      private void processDropEnd(DragEvent var1) {
         if (this.source == null) {
            System.out.println("Scene.DnDGesture.processDropEnd() - UNEXPECTD - source is NULL");
         } else {
            var1 = new DragEvent(var1.getSource(), this.source, DragEvent.DRAG_DONE, var1.getDragboard(), var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), var1.getTransferMode(), this.source, this.target, var1.getPickResult());
            Event.fireEvent(this.source, var1);
            Scene.this.tmpTargetWrapper.clear();
            this.handleExitEnter(var1, Scene.this.tmpTargetWrapper);
            Toolkit.getToolkit().stopDrag(this.dragboard);
         }
      }

      private TransferMode processTargetEnterOver(DragEvent var1) {
         Scene.this.pick(Scene.this.tmpTargetWrapper, var1.getSceneX(), var1.getSceneY());
         EventTarget var2 = Scene.this.tmpTargetWrapper.getEventTarget();
         if (this.dragboard == null) {
            this.dragboard = this.createDragboard(var1, false);
         }

         var1 = new DragEvent(var1.getSource(), var2, var1.getEventType(), this.dragboard, var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), var1.getTransferMode(), this.source, this.potentialTarget, var1.getPickResult());
         this.handleExitEnter(var1, Scene.this.tmpTargetWrapper);
         var1 = new DragEvent(var1.getSource(), var2, DragEvent.DRAG_OVER, var1.getDragboard(), var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), var1.getTransferMode(), this.source, this.potentialTarget, var1.getPickResult());
         this.fireEvent(var2, var1);
         Object var3 = var1.getAcceptingObject();
         this.potentialTarget = var3 instanceof EventTarget ? (EventTarget)var3 : null;
         this.acceptedTransferMode = var1.getAcceptedTransferMode();
         return this.acceptedTransferMode;
      }

      private void processTargetActionChanged(DragEvent var1) {
      }

      private void processTargetExit(DragEvent var1) {
         if (this.dragboard == null) {
            throw new NullPointerException("dragboard is null in processTargetExit()");
         } else {
            if (this.currentTargets.size() > 0) {
               this.potentialTarget = null;
               Scene.this.tmpTargetWrapper.clear();
               this.handleExitEnter(var1, Scene.this.tmpTargetWrapper);
            }

         }
      }

      private TransferMode processTargetDrop(DragEvent var1) {
         Scene.this.pick(Scene.this.tmpTargetWrapper, var1.getSceneX(), var1.getSceneY());
         EventTarget var2 = Scene.this.tmpTargetWrapper.getEventTarget();
         var1 = new DragEvent(var1.getSource(), var2, DragEvent.DRAG_DROPPED, var1.getDragboard(), var1.getSceneX(), var1.getSceneY(), var1.getScreenX(), var1.getScreenY(), this.acceptedTransferMode, this.source, this.potentialTarget, var1.getPickResult());
         if (this.dragboard == null) {
            throw new NullPointerException("dragboard is null in processTargetDrop()");
         } else {
            this.handleExitEnter(var1, Scene.this.tmpTargetWrapper);
            this.fireEvent(var2, var1);
            Object var3 = var1.getAcceptingObject();
            this.potentialTarget = var3 instanceof EventTarget ? (EventTarget)var3 : null;
            this.target = this.potentialTarget;
            TransferMode var4 = var1.isDropCompleted() ? var1.getAcceptedTransferMode() : null;
            Scene.this.tmpTargetWrapper.clear();
            this.handleExitEnter(var1, Scene.this.tmpTargetWrapper);
            return var4;
         }
      }

      private void handleExitEnter(DragEvent var1, TargetWrapper var2) {
         EventTarget var3 = this.currentTargets.size() > 0 ? (EventTarget)this.currentTargets.get(0) : null;
         if (var2.getEventTarget() != var3) {
            var2.fillHierarchy(this.newTargets);
            int var4 = this.currentTargets.size() - 1;

            int var5;
            for(var5 = this.newTargets.size() - 1; var4 >= 0 && var5 >= 0 && this.currentTargets.get(var4) == this.newTargets.get(var5); --var5) {
               --var4;
            }

            EventTarget var6;
            while(var4 >= 0) {
               var6 = (EventTarget)this.currentTargets.get(var4);
               if (this.potentialTarget == var6) {
                  this.potentialTarget = null;
               }

               var1 = var1.copyFor(var1.getSource(), var6, this.source, this.potentialTarget, DragEvent.DRAG_EXITED_TARGET);
               Event.fireEvent(var6, var1);
               --var4;
            }

            for(this.potentialTarget = null; var5 >= 0; --var5) {
               var6 = (EventTarget)this.newTargets.get(var5);
               var1 = var1.copyFor(var1.getSource(), var6, this.source, this.potentialTarget, DragEvent.DRAG_ENTERED_TARGET);
               Object var7 = var1.getAcceptingObject();
               if (var7 instanceof EventTarget) {
                  this.potentialTarget = (EventTarget)var7;
               }

               Event.fireEvent(var6, var1);
            }

            this.currentTargets.clear();
            this.currentTargets.addAll(this.newTargets);
            this.newTargets.clear();
         }

      }

      private boolean processKey(KeyEvent var1) {
         if (var1.getEventType() == KeyEvent.KEY_PRESSED && var1.getCode() == KeyCode.ESCAPE) {
            DragEvent var2 = new DragEvent(this.source, this.source, DragEvent.DRAG_DONE, this.dragboard, 0.0, 0.0, 0.0, 0.0, (TransferMode)null, this.source, (Object)null, (PickResult)null);
            if (this.source != null) {
               Event.fireEvent(this.source, var2);
            }

            Scene.this.tmpTargetWrapper.clear();
            this.handleExitEnter(var2, Scene.this.tmpTargetWrapper);
            return false;
         } else {
            return true;
         }
      }

      private Dragboard startDrag(EventTarget var1, Set var2) {
         if (this.dragDetected != Scene.DragDetectedState.PROCESSING) {
            throw new IllegalStateException("Cannot start drag and drop outside of DRAG_DETECTED event handler");
         } else {
            if (var2.isEmpty()) {
               this.dragboard = null;
            } else if (this.dragboard == null) {
               this.dragboard = this.createDragboard((DragEvent)null, true);
            }

            DragboardHelper.setDataAccessRestriction(this.dragboard, false);
            this.source = var1;
            this.potentialTarget = var1;
            this.sourceTransferModes = var2;
            return this.dragboard;
         }
      }

      private void startFullPDR(EventTarget var1) {
         this.fullPDRSource = var1;
      }

      private Dragboard createDragboard(DragEvent var1, boolean var2) {
         Dragboard var3 = null;
         if (var1 != null) {
            var3 = var1.getDragboard();
            if (var3 != null) {
               return var3;
            }
         }

         TKClipboard var4 = Scene.this.impl_peer.createDragboard(var2);
         return Dragboard.impl_createDragboard(var4);
      }
   }

   class DragGestureListener implements TKDragGestureListener {
      public void dragGestureRecognized(double var1, double var3, double var5, double var7, int var9, TKClipboard var10) {
         Dragboard var11 = Dragboard.impl_createDragboard(var10);
         Scene.this.dndGesture = Scene.this.new DnDGesture();
         Scene.this.dndGesture.dragboard = var11;
         DragEvent var12 = new DragEvent(DragEvent.ANY, var11, var1, var3, var5, var7, (TransferMode)null, (Object)null, (Object)null, Scene.this.pick(var1, var3));
         Scene.this.dndGesture.processRecognized(var12);
         Scene.this.dndGesture = null;
      }
   }

   class DropTargetListener implements TKDropTargetListener {
      public TransferMode dragEnter(double var1, double var3, double var5, double var7, TransferMode var9, TKClipboard var10) {
         if (Scene.this.dndGesture == null) {
            Scene.this.dndGesture = Scene.this.new DnDGesture();
         }

         Dragboard var11 = Dragboard.impl_createDragboard(var10);
         Scene.this.dndGesture.dragboard = var11;
         DragEvent var12 = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, var1, var3, var5, var7, var9, (Object)null, (Object)null, Scene.this.pick(var1, var3));
         return Scene.this.dndGesture.processTargetEnterOver(var12);
      }

      public TransferMode dragOver(double var1, double var3, double var5, double var7, TransferMode var9) {
         if (Scene.this.dndGesture == null) {
            System.err.println("GOT A dragOver when dndGesture is null!");
            return null;
         } else if (Scene.this.dndGesture.dragboard == null) {
            throw new RuntimeException("dndGesture.dragboard is null in dragOver");
         } else {
            DragEvent var10 = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, var1, var3, var5, var7, var9, (Object)null, (Object)null, Scene.this.pick(var1, var3));
            return Scene.this.dndGesture.processTargetEnterOver(var10);
         }
      }

      public void dragExit(double var1, double var3, double var5, double var7) {
         if (Scene.this.dndGesture == null) {
            System.err.println("GOT A dragExit when dndGesture is null!");
         } else {
            if (Scene.this.dndGesture.dragboard == null) {
               throw new RuntimeException("dndGesture.dragboard is null in dragExit");
            }

            DragEvent var9 = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, var1, var3, var5, var7, (TransferMode)null, (Object)null, (Object)null, Scene.this.pick(var1, var3));
            Scene.this.dndGesture.processTargetExit(var9);
            if (Scene.this.dndGesture.source == null) {
               Scene.this.dndGesture.dragboard = null;
               Scene.this.dndGesture = null;
            }
         }

      }

      public TransferMode drop(double var1, double var3, double var5, double var7, TransferMode var9) {
         if (Scene.this.dndGesture == null) {
            System.err.println("GOT A drop when dndGesture is null!");
            return null;
         } else if (Scene.this.dndGesture.dragboard == null) {
            throw new RuntimeException("dndGesture.dragboard is null in dragDrop");
         } else {
            DragEvent var10 = new DragEvent(DragEvent.ANY, Scene.this.dndGesture.dragboard, var1, var3, var5, var7, var9, (Object)null, (Object)null, Scene.this.pick(var1, var3));
            DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, false);

            TransferMode var11;
            try {
               var11 = Scene.this.dndGesture.processTargetDrop(var10);
            } finally {
               DragboardHelper.setDataAccessRestriction(Scene.this.dndGesture.dragboard, true);
            }

            if (Scene.this.dndGesture.source == null) {
               Scene.this.dndGesture.dragboard = null;
               Scene.this.dndGesture = null;
            }

            return var11;
         }
      }
   }

   private class ScenePeerPaintListener implements TKScenePaintListener {
      private ScenePeerPaintListener() {
      }

      public void frameRendered() {
         synchronized(Scene.trackerMonitor) {
            if (Scene.this.tracker != null) {
               Scene.this.tracker.frameRendered();
            }

         }
      }

      // $FF: synthetic method
      ScenePeerPaintListener(Object var2) {
         this();
      }
   }

   class ScenePeerListener implements TKSceneListener {
      public void changedLocation(float var1, float var2) {
         if ((double)var1 != Scene.this.getX()) {
            Scene.this.setX((double)var1);
         }

         if ((double)var2 != Scene.this.getY()) {
            Scene.this.setY((double)var2);
         }

      }

      public void changedSize(float var1, float var2) {
         if ((double)var1 != Scene.this.getWidth()) {
            Scene.this.setWidth((double)var1);
         }

         if ((double)var2 != Scene.this.getHeight()) {
            Scene.this.setHeight((double)var2);
         }

      }

      public void mouseEvent(EventType var1, double var2, double var4, double var6, double var8, MouseButton var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19) {
         MouseEvent var20 = new MouseEvent(var1, var2, var4, var6, var8, var10, 0, var13, var14, var15, var16, var17, var18, var19, var12, var11, false, (PickResult)null);
         Scene.this.impl_processMouseEvent(var20);
      }

      public void keyEvent(KeyEvent var1) {
         Scene.this.impl_processKeyEvent(var1);
      }

      public void inputMethodEvent(EventType var1, ObservableList var2, String var3, int var4) {
         InputMethodEvent var5 = new InputMethodEvent(var1, var2, var3, var4);
         Scene.this.processInputMethodEvent(var5);
      }

      public void menuEvent(double var1, double var3, double var5, double var7, boolean var9) {
         Scene.this.processMenuEvent(var1, var3, var5, var7, var9);
      }

      public void scrollEvent(EventType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14, int var15, int var16, int var17, int var18, double var19, double var21, double var23, double var25, boolean var27, boolean var28, boolean var29, boolean var30, boolean var31, boolean var32) {
         ScrollEvent.HorizontalTextScrollUnits var33 = var15 > 0 ? ScrollEvent.HorizontalTextScrollUnits.CHARACTERS : ScrollEvent.HorizontalTextScrollUnits.NONE;
         double var34 = var15 < 0 ? 0.0 : (double)var15 * var2;
         ScrollEvent.VerticalTextScrollUnits var36 = var16 > 0 ? ScrollEvent.VerticalTextScrollUnits.LINES : (var16 < 0 ? ScrollEvent.VerticalTextScrollUnits.PAGES : ScrollEvent.VerticalTextScrollUnits.NONE);
         double var37 = var16 < 0 ? var4 : (double)var16 * var4;
         var10 = var17 > 0 && var15 >= 0 ? (double)Math.round(var10 * (double)var15 / (double)var17) : var10;
         var12 = var18 > 0 && var16 >= 0 ? (double)Math.round(var12 * (double)var16 / (double)var18) : var12;
         if (var1 == ScrollEvent.SCROLL_FINISHED) {
            var19 = Scene.this.scrollGesture.sceneCoords.getX();
            var21 = Scene.this.scrollGesture.sceneCoords.getY();
            var23 = Scene.this.scrollGesture.screenCoords.getX();
            var25 = Scene.this.scrollGesture.screenCoords.getY();
         } else if (Double.isNaN(var19) || Double.isNaN(var21) || Double.isNaN(var23) || Double.isNaN(var25)) {
            if (Scene.this.cursorScenePos == null || Scene.this.cursorScreenPos == null) {
               return;
            }

            var19 = Scene.this.cursorScenePos.getX();
            var21 = Scene.this.cursorScenePos.getY();
            var23 = Scene.this.cursorScreenPos.getX();
            var25 = Scene.this.cursorScreenPos.getY();
         }

         Scene.inMousePick = true;
         Scene.this.processGestureEvent(new ScrollEvent(var1, var19, var21, var23, var25, var27, var28, var29, var30, var31, var32, var2 * var10, var4 * var12, var6 * var10, var8 * var12, var10, var12, var33, var34, var36, var37, var14, Scene.this.pick(var19, var21)), Scene.this.scrollGesture);
         Scene.inMousePick = false;
      }

      public void zoomEvent(EventType var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19) {
         if (var1 == ZoomEvent.ZOOM_FINISHED) {
            var6 = Scene.this.zoomGesture.sceneCoords.getX();
            var8 = Scene.this.zoomGesture.sceneCoords.getY();
            var10 = Scene.this.zoomGesture.screenCoords.getX();
            var12 = Scene.this.zoomGesture.screenCoords.getY();
         } else if (Double.isNaN(var6) || Double.isNaN(var8) || Double.isNaN(var10) || Double.isNaN(var12)) {
            if (Scene.this.cursorScenePos == null || Scene.this.cursorScreenPos == null) {
               return;
            }

            var6 = Scene.this.cursorScenePos.getX();
            var8 = Scene.this.cursorScenePos.getY();
            var10 = Scene.this.cursorScreenPos.getX();
            var12 = Scene.this.cursorScreenPos.getY();
         }

         Scene.inMousePick = true;
         Scene.this.processGestureEvent(new ZoomEvent(var1, var6, var8, var10, var12, var14, var15, var16, var17, var18, var19, var2, var4, Scene.this.pick(var6, var8)), Scene.this.zoomGesture);
         Scene.inMousePick = false;
      }

      public void rotateEvent(EventType var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14, boolean var15, boolean var16, boolean var17, boolean var18, boolean var19) {
         if (var1 == RotateEvent.ROTATION_FINISHED) {
            var6 = Scene.this.rotateGesture.sceneCoords.getX();
            var8 = Scene.this.rotateGesture.sceneCoords.getY();
            var10 = Scene.this.rotateGesture.screenCoords.getX();
            var12 = Scene.this.rotateGesture.screenCoords.getY();
         } else if (Double.isNaN(var6) || Double.isNaN(var8) || Double.isNaN(var10) || Double.isNaN(var12)) {
            if (Scene.this.cursorScenePos == null || Scene.this.cursorScreenPos == null) {
               return;
            }

            var6 = Scene.this.cursorScenePos.getX();
            var8 = Scene.this.cursorScenePos.getY();
            var10 = Scene.this.cursorScreenPos.getX();
            var12 = Scene.this.cursorScreenPos.getY();
         }

         Scene.inMousePick = true;
         Scene.this.processGestureEvent(new RotateEvent(var1, var6, var8, var10, var12, var14, var15, var16, var17, var18, var19, var2, var4, Scene.this.pick(var6, var8)), Scene.this.rotateGesture);
         Scene.inMousePick = false;
      }

      public void swipeEvent(EventType var1, int var2, double var3, double var5, double var7, double var9, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15) {
         if (Double.isNaN(var3) || Double.isNaN(var5) || Double.isNaN(var7) || Double.isNaN(var9)) {
            if (Scene.this.cursorScenePos == null || Scene.this.cursorScreenPos == null) {
               return;
            }

            var3 = Scene.this.cursorScenePos.getX();
            var5 = Scene.this.cursorScenePos.getY();
            var7 = Scene.this.cursorScreenPos.getX();
            var9 = Scene.this.cursorScreenPos.getY();
         }

         Scene.inMousePick = true;
         Scene.this.processGestureEvent(new SwipeEvent(var1, var3, var5, var7, var9, var11, var12, var13, var14, var15, var2, Scene.this.pick(var3, var5)), Scene.this.swipeGesture);
         Scene.inMousePick = false;
      }

      public void touchEventBegin(long var1, int var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8) {
         if (!var4) {
            Scene.this.nextTouchEvent = null;
         } else {
            Scene.this.nextTouchEvent = new TouchEvent(TouchEvent.ANY, (TouchPoint)null, (List)null, 0, var5, var6, var7, var8);
            if (Scene.this.touchPoints == null || Scene.this.touchPoints.length != var3) {
               Scene.this.touchPoints = new TouchPoint[var3];
            }

            Scene.this.touchPointIndex = 0;
         }
      }

      public void touchEventNext(TouchPoint.State var1, long var2, double var4, double var6, double var8, double var10) {
         Scene.inMousePick = true;
         if (Scene.this.nextTouchEvent != null) {
            Scene.this.touchPointIndex++;
            int var12 = var1 == TouchPoint.State.PRESSED ? Scene.this.touchMap.add(var2) : Scene.this.touchMap.get(var2);
            if (var1 == TouchPoint.State.RELEASED) {
               Scene.this.touchMap.remove(var2);
            }

            int var13 = Scene.this.touchMap.getOrder(var12);
            if (var13 >= Scene.this.touchPoints.length) {
               throw new RuntimeException("Too many touch points reported");
            } else {
               boolean var14 = false;
               PickResult var15 = Scene.this.pick(var4, var6);
               Object var16 = (EventTarget)Scene.this.touchTargets.get(var12);
               if (var16 == null) {
                  var16 = var15.getIntersectedNode();
                  if (var16 == null) {
                     var16 = Scene.this;
                  }
               } else {
                  var14 = true;
               }

               TouchPoint var17 = new TouchPoint(var12, var1, var4, var6, var8, var10, (EventTarget)var16, var15);
               Scene.this.touchPoints[var13] = var17;
               if (var14) {
                  var17.grab((EventTarget)var16);
               }

               if (var17.getState() == TouchPoint.State.PRESSED) {
                  var17.grab((EventTarget)var16);
                  Scene.this.touchTargets.put(var17.getId(), var16);
               } else if (var17.getState() == TouchPoint.State.RELEASED) {
                  Scene.this.touchTargets.remove(var17.getId());
               }

               Scene.inMousePick = false;
            }
         }
      }

      public void touchEventEnd() {
         if (Scene.this.nextTouchEvent != null) {
            if (Scene.this.touchPointIndex != Scene.this.touchPoints.length) {
               throw new RuntimeException("Wrong number of touch points reported");
            } else {
               Scene.this.processTouchEvent(Scene.this.nextTouchEvent, Scene.this.touchPoints);
               if (Scene.this.touchMap.cleanup()) {
                  Scene.this.touchEventSetId = 0;
               }

            }
         }
      }

      public Accessible getSceneAccessible() {
         return Scene.this.getAccessible();
      }
   }

   class ScenePulseListener implements TKPulseListener {
      private boolean firstPulse = true;

      private void synchronizeSceneNodes() {
         Toolkit.getToolkit().checkFxUserThread();
         Scene.inSynchronizer = true;
         if (Scene.this.dirtyNodes == null) {
            this.syncAll(Scene.this.getRoot());
            Scene.this.dirtyNodes = new Node[30];
         } else {
            for(int var1 = 0; var1 < Scene.this.dirtyNodesSize; ++var1) {
               Node var2 = Scene.this.dirtyNodes[var1];
               Scene.this.dirtyNodes[var1] = null;
               if (var2.getScene() == Scene.this) {
                  var2.impl_syncPeer();
               }
            }

            Scene.this.dirtyNodesSize = 0;
         }

         Scene.inSynchronizer = false;
      }

      private int syncAll(Node var1) {
         var1.impl_syncPeer();
         int var2 = 1;
         if (var1 instanceof Parent) {
            Parent var3 = (Parent)var1;
            int var4 = var3.getChildren().size();

            for(int var5 = 0; var5 < var4; ++var5) {
               Node var6 = (Node)var3.getChildren().get(var5);
               if (var6 != null) {
                  var2 += this.syncAll(var6);
               }
            }
         } else if (var1 instanceof SubScene) {
            SubScene var7 = (SubScene)var1;
            var2 += this.syncAll(var7.getRoot());
         }

         if (var1.getClip() != null) {
            var2 += this.syncAll(var1.getClip());
         }

         return var2;
      }

      private void synchronizeSceneProperties() {
         Scene.inSynchronizer = true;
         if (Scene.this.isDirty(Scene.DirtyBits.ROOT_DIRTY)) {
            Scene.this.impl_peer.setRoot(Scene.this.getRoot().impl_getPeer());
         }

         if (Scene.this.isDirty(Scene.DirtyBits.FILL_DIRTY)) {
            Toolkit var1 = Toolkit.getToolkit();
            Scene.this.impl_peer.setFillPaint(Scene.this.getFill() == null ? null : var1.getPaint(Scene.this.getFill()));
         }

         Camera var2 = Scene.this.getEffectiveCamera();
         if (Scene.this.isDirty(Scene.DirtyBits.CAMERA_DIRTY)) {
            var2.impl_updatePeer();
            Scene.this.impl_peer.setCamera((NGCamera)var2.impl_getPeer());
         }

         if (Scene.this.isDirty(Scene.DirtyBits.CURSOR_DIRTY)) {
            Scene.this.mouseHandler.updateCursor(Scene.this.getCursor());
         }

         Scene.this.clearDirty();
         Scene.inSynchronizer = false;
      }

      private void focusCleanup() {
         if (Scene.this.isFocusDirty()) {
            Node var1 = Scene.this.getFocusOwner();
            if (var1 == null) {
               Scene.this.focusInitial();
            } else if (var1.getScene() != Scene.this) {
               Scene.this.requestFocus((Node)null);
               Scene.this.focusInitial();
            } else if (!var1.isCanReceiveFocus()) {
               Scene.this.requestFocus((Node)null);
               Scene.this.focusIneligible(var1);
            }

            Scene.this.setFocusDirty(false);
         }

      }

      public void pulse() {
         if (Scene.this.tracker != null) {
            Scene.this.tracker.pulse();
         }

         if (this.firstPulse) {
            PerformanceTracker.logEvent("Scene - first repaint");
         }

         this.focusCleanup();
         Scene.this.disposeAccessibles();
         if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newPhase("CSS Pass");
         }

         Scene.this.doCSSPass();
         if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newPhase("Layout Pass");
         }

         Scene.this.doLayoutPass();
         boolean var1 = Scene.this.dirtyNodes == null || Scene.this.dirtyNodesSize != 0 || !Scene.this.isDirtyEmpty();
         if (var1) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
               PulseLogger.newPhase("Update bounds");
            }

            Scene.this.getRoot().updateBounds();
            if (Scene.this.impl_peer != null) {
               try {
                  if (PulseLogger.PULSE_LOGGING_ENABLED) {
                     PulseLogger.newPhase("Waiting for previous rendering");
                  }

                  Scene.this.impl_peer.waitForRenderingToComplete();
                  Scene.this.impl_peer.waitForSynchronization();
                  if (PulseLogger.PULSE_LOGGING_ENABLED) {
                     PulseLogger.newPhase("Copy state to render graph");
                  }

                  Scene.this.syncLights();
                  this.synchronizeSceneProperties();
                  this.synchronizeSceneNodes();
                  Scene.this.mouseHandler.pulse();
                  Scene.this.impl_peer.markDirty();
               } finally {
                  Scene.this.impl_peer.releaseSynchronization(true);
               }
            } else {
               if (PulseLogger.PULSE_LOGGING_ENABLED) {
                  PulseLogger.newPhase("Synchronize with null peer");
               }

               this.synchronizeSceneNodes();
               Scene.this.mouseHandler.pulse();
            }

            if (Scene.this.getRoot().cssFlag != CssFlags.CLEAN) {
               Scene.this.getRoot().impl_markDirty(com.sun.javafx.scene.DirtyBits.NODE_CSS);
            }
         }

         Scene.this.mouseHandler.updateCursorFrame();
         if (this.firstPulse) {
            if (PerformanceTracker.isLoggingEnabled()) {
               PerformanceTracker.logEvent("Scene - first repaint - layout complete");
               if (PrismSettings.perfLogFirstPaintFlush) {
                  PerformanceTracker.outputLog();
               }

               if (PrismSettings.perfLogFirstPaintExit) {
                  System.exit(0);
               }
            }

            this.firstPulse = false;
         }

         if (Scene.this.testPulseListener != null) {
            Scene.this.testPulseListener.run();
         }

      }
   }

   private static enum DirtyBits {
      FILL_DIRTY,
      ROOT_DIRTY,
      CAMERA_DIRTY,
      LIGHTS_DIRTY,
      CURSOR_DIRTY;

      private int mask = 1 << this.ordinal();

      public final int getMask() {
         return this.mask;
      }
   }

   private static class TouchGesture {
      EventTarget target;
      Point2D sceneCoords;
      Point2D screenCoords;
      boolean finished;

      private TouchGesture() {
      }

      // $FF: synthetic method
      TouchGesture(Object var1) {
         this();
      }
   }
}
