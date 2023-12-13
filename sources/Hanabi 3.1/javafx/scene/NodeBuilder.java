package javafx.scene;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.transform.Transform;

/** @deprecated */
@Deprecated
public abstract class NodeBuilder {
   BitSet __set = new BitSet();
   private BlendMode blendMode;
   private boolean cache;
   private CacheHint cacheHint;
   private Node clip;
   private Cursor cursor;
   private DepthTest depthTest;
   private boolean disable;
   private Effect effect;
   private EventDispatcher eventDispatcher;
   private boolean focusTraversable;
   private String id;
   private InputMethodRequests inputMethodRequests;
   private double layoutX;
   private double layoutY;
   private boolean managed;
   private boolean mouseTransparent;
   private EventHandler onContextMenuRequested;
   private EventHandler onDragDetected;
   private EventHandler onDragDone;
   private EventHandler onDragDropped;
   private EventHandler onDragEntered;
   private EventHandler onDragExited;
   private EventHandler onDragOver;
   private EventHandler onInputMethodTextChanged;
   private EventHandler onKeyPressed;
   private EventHandler onKeyReleased;
   private EventHandler onKeyTyped;
   private EventHandler onMouseClicked;
   private EventHandler onMouseDragEntered;
   private EventHandler onMouseDragExited;
   private EventHandler onMouseDragged;
   private EventHandler onMouseDragOver;
   private EventHandler onMouseDragReleased;
   private EventHandler onMouseEntered;
   private EventHandler onMouseExited;
   private EventHandler onMouseMoved;
   private EventHandler onMousePressed;
   private EventHandler onMouseReleased;
   private EventHandler onRotate;
   private EventHandler onRotationFinished;
   private EventHandler onRotationStarted;
   private EventHandler onScroll;
   private EventHandler onScrollFinished;
   private EventHandler onScrollStarted;
   private EventHandler onSwipeDown;
   private EventHandler onSwipeLeft;
   private EventHandler onSwipeRight;
   private EventHandler onSwipeUp;
   private EventHandler onTouchMoved;
   private EventHandler onTouchPressed;
   private EventHandler onTouchReleased;
   private EventHandler onTouchStationary;
   private EventHandler onZoom;
   private EventHandler onZoomFinished;
   private EventHandler onZoomStarted;
   private double opacity;
   private boolean pickOnBounds;
   private double rotate;
   private Point3D rotationAxis;
   private double scaleX;
   private double scaleY;
   private double scaleZ;
   private String style;
   private Collection styleClass;
   private Collection transforms;
   private double translateX;
   private double translateY;
   private double translateZ;
   private Object userData;
   private boolean visible;

   protected NodeBuilder() {
   }

   private void __set(int var1) {
      this.__set.set(var1);
   }

   public void applyTo(Node var1) {
      BitSet var2 = this.__set;
      int var3 = -1;

      while((var3 = var2.nextSetBit(var3 + 1)) >= 0) {
         switch (var3) {
            case 0:
               var1.setBlendMode(this.blendMode);
               break;
            case 1:
               var1.setCache(this.cache);
               break;
            case 2:
               var1.setCacheHint(this.cacheHint);
               break;
            case 3:
               var1.setClip(this.clip);
               break;
            case 4:
               var1.setCursor(this.cursor);
               break;
            case 5:
               var1.setDepthTest(this.depthTest);
               break;
            case 6:
               var1.setDisable(this.disable);
               break;
            case 7:
               var1.setEffect(this.effect);
               break;
            case 8:
               var1.setEventDispatcher(this.eventDispatcher);
               break;
            case 9:
               var1.setFocusTraversable(this.focusTraversable);
               break;
            case 10:
               var1.setId(this.id);
               break;
            case 11:
               var1.setInputMethodRequests(this.inputMethodRequests);
               break;
            case 12:
               var1.setLayoutX(this.layoutX);
               break;
            case 13:
               var1.setLayoutY(this.layoutY);
               break;
            case 14:
               var1.setManaged(this.managed);
               break;
            case 15:
               var1.setMouseTransparent(this.mouseTransparent);
               break;
            case 16:
               var1.setOnContextMenuRequested(this.onContextMenuRequested);
               break;
            case 17:
               var1.setOnDragDetected(this.onDragDetected);
               break;
            case 18:
               var1.setOnDragDone(this.onDragDone);
               break;
            case 19:
               var1.setOnDragDropped(this.onDragDropped);
               break;
            case 20:
               var1.setOnDragEntered(this.onDragEntered);
               break;
            case 21:
               var1.setOnDragExited(this.onDragExited);
               break;
            case 22:
               var1.setOnDragOver(this.onDragOver);
               break;
            case 23:
               var1.setOnInputMethodTextChanged(this.onInputMethodTextChanged);
               break;
            case 24:
               var1.setOnKeyPressed(this.onKeyPressed);
               break;
            case 25:
               var1.setOnKeyReleased(this.onKeyReleased);
               break;
            case 26:
               var1.setOnKeyTyped(this.onKeyTyped);
               break;
            case 27:
               var1.setOnMouseClicked(this.onMouseClicked);
               break;
            case 28:
               var1.setOnMouseDragEntered(this.onMouseDragEntered);
               break;
            case 29:
               var1.setOnMouseDragExited(this.onMouseDragExited);
               break;
            case 30:
               var1.setOnMouseDragged(this.onMouseDragged);
               break;
            case 31:
               var1.setOnMouseDragOver(this.onMouseDragOver);
               break;
            case 32:
               var1.setOnMouseDragReleased(this.onMouseDragReleased);
               break;
            case 33:
               var1.setOnMouseEntered(this.onMouseEntered);
               break;
            case 34:
               var1.setOnMouseExited(this.onMouseExited);
               break;
            case 35:
               var1.setOnMouseMoved(this.onMouseMoved);
               break;
            case 36:
               var1.setOnMousePressed(this.onMousePressed);
               break;
            case 37:
               var1.setOnMouseReleased(this.onMouseReleased);
               break;
            case 38:
               var1.setOnRotate(this.onRotate);
               break;
            case 39:
               var1.setOnRotationFinished(this.onRotationFinished);
               break;
            case 40:
               var1.setOnRotationStarted(this.onRotationStarted);
               break;
            case 41:
               var1.setOnScroll(this.onScroll);
               break;
            case 42:
               var1.setOnScrollFinished(this.onScrollFinished);
               break;
            case 43:
               var1.setOnScrollStarted(this.onScrollStarted);
               break;
            case 44:
               var1.setOnSwipeDown(this.onSwipeDown);
               break;
            case 45:
               var1.setOnSwipeLeft(this.onSwipeLeft);
               break;
            case 46:
               var1.setOnSwipeRight(this.onSwipeRight);
               break;
            case 47:
               var1.setOnSwipeUp(this.onSwipeUp);
               break;
            case 48:
               var1.setOnTouchMoved(this.onTouchMoved);
               break;
            case 49:
               var1.setOnTouchPressed(this.onTouchPressed);
               break;
            case 50:
               var1.setOnTouchReleased(this.onTouchReleased);
               break;
            case 51:
               var1.setOnTouchStationary(this.onTouchStationary);
               break;
            case 52:
               var1.setOnZoom(this.onZoom);
               break;
            case 53:
               var1.setOnZoomFinished(this.onZoomFinished);
               break;
            case 54:
               var1.setOnZoomStarted(this.onZoomStarted);
               break;
            case 55:
               var1.setOpacity(this.opacity);
               break;
            case 56:
               var1.setPickOnBounds(this.pickOnBounds);
               break;
            case 57:
               var1.setRotate(this.rotate);
               break;
            case 58:
               var1.setRotationAxis(this.rotationAxis);
               break;
            case 59:
               var1.setScaleX(this.scaleX);
               break;
            case 60:
               var1.setScaleY(this.scaleY);
               break;
            case 61:
               var1.setScaleZ(this.scaleZ);
               break;
            case 62:
               var1.setStyle(this.style);
               break;
            case 63:
               var1.getStyleClass().addAll(this.styleClass);
               break;
            case 64:
               var1.getTransforms().addAll(this.transforms);
               break;
            case 65:
               var1.setTranslateX(this.translateX);
               break;
            case 66:
               var1.setTranslateY(this.translateY);
               break;
            case 67:
               var1.setTranslateZ(this.translateZ);
               break;
            case 68:
               var1.setUserData(this.userData);
               break;
            case 69:
               var1.setVisible(this.visible);
         }
      }

   }

   public NodeBuilder blendMode(BlendMode var1) {
      this.blendMode = var1;
      this.__set(0);
      return this;
   }

   public NodeBuilder cache(boolean var1) {
      this.cache = var1;
      this.__set(1);
      return this;
   }

   public NodeBuilder cacheHint(CacheHint var1) {
      this.cacheHint = var1;
      this.__set(2);
      return this;
   }

   public NodeBuilder clip(Node var1) {
      this.clip = var1;
      this.__set(3);
      return this;
   }

   public NodeBuilder cursor(Cursor var1) {
      this.cursor = var1;
      this.__set(4);
      return this;
   }

   public NodeBuilder depthTest(DepthTest var1) {
      this.depthTest = var1;
      this.__set(5);
      return this;
   }

   public NodeBuilder disable(boolean var1) {
      this.disable = var1;
      this.__set(6);
      return this;
   }

   public NodeBuilder effect(Effect var1) {
      this.effect = var1;
      this.__set(7);
      return this;
   }

   public NodeBuilder eventDispatcher(EventDispatcher var1) {
      this.eventDispatcher = var1;
      this.__set(8);
      return this;
   }

   public NodeBuilder focusTraversable(boolean var1) {
      this.focusTraversable = var1;
      this.__set(9);
      return this;
   }

   public NodeBuilder id(String var1) {
      this.id = var1;
      this.__set(10);
      return this;
   }

   public NodeBuilder inputMethodRequests(InputMethodRequests var1) {
      this.inputMethodRequests = var1;
      this.__set(11);
      return this;
   }

   public NodeBuilder layoutX(double var1) {
      this.layoutX = var1;
      this.__set(12);
      return this;
   }

   public NodeBuilder layoutY(double var1) {
      this.layoutY = var1;
      this.__set(13);
      return this;
   }

   public NodeBuilder managed(boolean var1) {
      this.managed = var1;
      this.__set(14);
      return this;
   }

   public NodeBuilder mouseTransparent(boolean var1) {
      this.mouseTransparent = var1;
      this.__set(15);
      return this;
   }

   public NodeBuilder onContextMenuRequested(EventHandler var1) {
      this.onContextMenuRequested = var1;
      this.__set(16);
      return this;
   }

   public NodeBuilder onDragDetected(EventHandler var1) {
      this.onDragDetected = var1;
      this.__set(17);
      return this;
   }

   public NodeBuilder onDragDone(EventHandler var1) {
      this.onDragDone = var1;
      this.__set(18);
      return this;
   }

   public NodeBuilder onDragDropped(EventHandler var1) {
      this.onDragDropped = var1;
      this.__set(19);
      return this;
   }

   public NodeBuilder onDragEntered(EventHandler var1) {
      this.onDragEntered = var1;
      this.__set(20);
      return this;
   }

   public NodeBuilder onDragExited(EventHandler var1) {
      this.onDragExited = var1;
      this.__set(21);
      return this;
   }

   public NodeBuilder onDragOver(EventHandler var1) {
      this.onDragOver = var1;
      this.__set(22);
      return this;
   }

   public NodeBuilder onInputMethodTextChanged(EventHandler var1) {
      this.onInputMethodTextChanged = var1;
      this.__set(23);
      return this;
   }

   public NodeBuilder onKeyPressed(EventHandler var1) {
      this.onKeyPressed = var1;
      this.__set(24);
      return this;
   }

   public NodeBuilder onKeyReleased(EventHandler var1) {
      this.onKeyReleased = var1;
      this.__set(25);
      return this;
   }

   public NodeBuilder onKeyTyped(EventHandler var1) {
      this.onKeyTyped = var1;
      this.__set(26);
      return this;
   }

   public NodeBuilder onMouseClicked(EventHandler var1) {
      this.onMouseClicked = var1;
      this.__set(27);
      return this;
   }

   public NodeBuilder onMouseDragEntered(EventHandler var1) {
      this.onMouseDragEntered = var1;
      this.__set(28);
      return this;
   }

   public NodeBuilder onMouseDragExited(EventHandler var1) {
      this.onMouseDragExited = var1;
      this.__set(29);
      return this;
   }

   public NodeBuilder onMouseDragged(EventHandler var1) {
      this.onMouseDragged = var1;
      this.__set(30);
      return this;
   }

   public NodeBuilder onMouseDragOver(EventHandler var1) {
      this.onMouseDragOver = var1;
      this.__set(31);
      return this;
   }

   public NodeBuilder onMouseDragReleased(EventHandler var1) {
      this.onMouseDragReleased = var1;
      this.__set(32);
      return this;
   }

   public NodeBuilder onMouseEntered(EventHandler var1) {
      this.onMouseEntered = var1;
      this.__set(33);
      return this;
   }

   public NodeBuilder onMouseExited(EventHandler var1) {
      this.onMouseExited = var1;
      this.__set(34);
      return this;
   }

   public NodeBuilder onMouseMoved(EventHandler var1) {
      this.onMouseMoved = var1;
      this.__set(35);
      return this;
   }

   public NodeBuilder onMousePressed(EventHandler var1) {
      this.onMousePressed = var1;
      this.__set(36);
      return this;
   }

   public NodeBuilder onMouseReleased(EventHandler var1) {
      this.onMouseReleased = var1;
      this.__set(37);
      return this;
   }

   public NodeBuilder onRotate(EventHandler var1) {
      this.onRotate = var1;
      this.__set(38);
      return this;
   }

   public NodeBuilder onRotationFinished(EventHandler var1) {
      this.onRotationFinished = var1;
      this.__set(39);
      return this;
   }

   public NodeBuilder onRotationStarted(EventHandler var1) {
      this.onRotationStarted = var1;
      this.__set(40);
      return this;
   }

   public NodeBuilder onScroll(EventHandler var1) {
      this.onScroll = var1;
      this.__set(41);
      return this;
   }

   public NodeBuilder onScrollFinished(EventHandler var1) {
      this.onScrollFinished = var1;
      this.__set(42);
      return this;
   }

   public NodeBuilder onScrollStarted(EventHandler var1) {
      this.onScrollStarted = var1;
      this.__set(43);
      return this;
   }

   public NodeBuilder onSwipeDown(EventHandler var1) {
      this.onSwipeDown = var1;
      this.__set(44);
      return this;
   }

   public NodeBuilder onSwipeLeft(EventHandler var1) {
      this.onSwipeLeft = var1;
      this.__set(45);
      return this;
   }

   public NodeBuilder onSwipeRight(EventHandler var1) {
      this.onSwipeRight = var1;
      this.__set(46);
      return this;
   }

   public NodeBuilder onSwipeUp(EventHandler var1) {
      this.onSwipeUp = var1;
      this.__set(47);
      return this;
   }

   public NodeBuilder onTouchMoved(EventHandler var1) {
      this.onTouchMoved = var1;
      this.__set(48);
      return this;
   }

   public NodeBuilder onTouchPressed(EventHandler var1) {
      this.onTouchPressed = var1;
      this.__set(49);
      return this;
   }

   public NodeBuilder onTouchReleased(EventHandler var1) {
      this.onTouchReleased = var1;
      this.__set(50);
      return this;
   }

   public NodeBuilder onTouchStationary(EventHandler var1) {
      this.onTouchStationary = var1;
      this.__set(51);
      return this;
   }

   public NodeBuilder onZoom(EventHandler var1) {
      this.onZoom = var1;
      this.__set(52);
      return this;
   }

   public NodeBuilder onZoomFinished(EventHandler var1) {
      this.onZoomFinished = var1;
      this.__set(53);
      return this;
   }

   public NodeBuilder onZoomStarted(EventHandler var1) {
      this.onZoomStarted = var1;
      this.__set(54);
      return this;
   }

   public NodeBuilder opacity(double var1) {
      this.opacity = var1;
      this.__set(55);
      return this;
   }

   public NodeBuilder pickOnBounds(boolean var1) {
      this.pickOnBounds = var1;
      this.__set(56);
      return this;
   }

   public NodeBuilder rotate(double var1) {
      this.rotate = var1;
      this.__set(57);
      return this;
   }

   public NodeBuilder rotationAxis(Point3D var1) {
      this.rotationAxis = var1;
      this.__set(58);
      return this;
   }

   public NodeBuilder scaleX(double var1) {
      this.scaleX = var1;
      this.__set(59);
      return this;
   }

   public NodeBuilder scaleY(double var1) {
      this.scaleY = var1;
      this.__set(60);
      return this;
   }

   public NodeBuilder scaleZ(double var1) {
      this.scaleZ = var1;
      this.__set(61);
      return this;
   }

   public NodeBuilder style(String var1) {
      this.style = var1;
      this.__set(62);
      return this;
   }

   public NodeBuilder styleClass(Collection var1) {
      this.styleClass = var1;
      this.__set(63);
      return this;
   }

   public NodeBuilder styleClass(String... var1) {
      return this.styleClass((Collection)Arrays.asList(var1));
   }

   public NodeBuilder transforms(Collection var1) {
      this.transforms = var1;
      this.__set(64);
      return this;
   }

   public NodeBuilder transforms(Transform... var1) {
      return this.transforms((Collection)Arrays.asList(var1));
   }

   public NodeBuilder translateX(double var1) {
      this.translateX = var1;
      this.__set(65);
      return this;
   }

   public NodeBuilder translateY(double var1) {
      this.translateY = var1;
      this.__set(66);
      return this;
   }

   public NodeBuilder translateZ(double var1) {
      this.translateZ = var1;
      this.__set(67);
      return this;
   }

   public NodeBuilder userData(Object var1) {
      this.userData = var1;
      this.__set(68);
      return this;
   }

   public NodeBuilder visible(boolean var1) {
      this.visible = var1;
      this.__set(69);
      return this;
   }
}
