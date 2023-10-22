package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class SceneBuilder implements Builder {
   private long __set;
   private Camera camera;
   private Cursor cursor;
   private boolean depthBuffer;
   private EventDispatcher eventDispatcher;
   private Paint fill;
   private double height = -1.0;
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
   private Parent root;
   private Collection stylesheets;
   private double width = -1.0;

   protected SceneBuilder() {
   }

   public static SceneBuilder create() {
      return new SceneBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1L << var1;
   }

   public void applyTo(Scene var1) {
      long var2 = this.__set;

      while(var2 != 0L) {
         int var4 = Long.numberOfTrailingZeros(var2);
         var2 &= ~(1L << var4);
         switch (var4) {
            case 0:
               var1.setCamera(this.camera);
               break;
            case 1:
               var1.setCursor(this.cursor);
               break;
            case 2:
               var1.setEventDispatcher(this.eventDispatcher);
               break;
            case 3:
               var1.setFill(this.fill);
               break;
            case 4:
               var1.setOnContextMenuRequested(this.onContextMenuRequested);
               break;
            case 5:
               var1.setOnDragDetected(this.onDragDetected);
               break;
            case 6:
               var1.setOnDragDone(this.onDragDone);
               break;
            case 7:
               var1.setOnDragDropped(this.onDragDropped);
               break;
            case 8:
               var1.setOnDragEntered(this.onDragEntered);
               break;
            case 9:
               var1.setOnDragExited(this.onDragExited);
               break;
            case 10:
               var1.setOnDragOver(this.onDragOver);
               break;
            case 11:
               var1.setOnInputMethodTextChanged(this.onInputMethodTextChanged);
               break;
            case 12:
               var1.setOnKeyPressed(this.onKeyPressed);
               break;
            case 13:
               var1.setOnKeyReleased(this.onKeyReleased);
               break;
            case 14:
               var1.setOnKeyTyped(this.onKeyTyped);
               break;
            case 15:
               var1.setOnMouseClicked(this.onMouseClicked);
               break;
            case 16:
               var1.setOnMouseDragEntered(this.onMouseDragEntered);
               break;
            case 17:
               var1.setOnMouseDragExited(this.onMouseDragExited);
               break;
            case 18:
               var1.setOnMouseDragged(this.onMouseDragged);
               break;
            case 19:
               var1.setOnMouseDragOver(this.onMouseDragOver);
               break;
            case 20:
               var1.setOnMouseDragReleased(this.onMouseDragReleased);
               break;
            case 21:
               var1.setOnMouseEntered(this.onMouseEntered);
               break;
            case 22:
               var1.setOnMouseExited(this.onMouseExited);
               break;
            case 23:
               var1.setOnMouseMoved(this.onMouseMoved);
               break;
            case 24:
               var1.setOnMousePressed(this.onMousePressed);
               break;
            case 25:
               var1.setOnMouseReleased(this.onMouseReleased);
               break;
            case 26:
               var1.setOnRotate(this.onRotate);
               break;
            case 27:
               var1.setOnRotationFinished(this.onRotationFinished);
               break;
            case 28:
               var1.setOnRotationStarted(this.onRotationStarted);
               break;
            case 29:
               var1.setOnScroll(this.onScroll);
               break;
            case 30:
               var1.setOnScrollFinished(this.onScrollFinished);
               break;
            case 31:
               var1.setOnScrollStarted(this.onScrollStarted);
               break;
            case 32:
               var1.setOnSwipeDown(this.onSwipeDown);
               break;
            case 33:
               var1.setOnSwipeLeft(this.onSwipeLeft);
               break;
            case 34:
               var1.setOnSwipeRight(this.onSwipeRight);
               break;
            case 35:
               var1.setOnSwipeUp(this.onSwipeUp);
               break;
            case 36:
               var1.setOnTouchMoved(this.onTouchMoved);
               break;
            case 37:
               var1.setOnTouchPressed(this.onTouchPressed);
               break;
            case 38:
               var1.setOnTouchReleased(this.onTouchReleased);
               break;
            case 39:
               var1.setOnTouchStationary(this.onTouchStationary);
               break;
            case 40:
               var1.setOnZoom(this.onZoom);
               break;
            case 41:
               var1.setOnZoomFinished(this.onZoomFinished);
               break;
            case 42:
               var1.setOnZoomStarted(this.onZoomStarted);
               break;
            case 43:
               var1.getStylesheets().addAll(this.stylesheets);
         }
      }

   }

   public SceneBuilder camera(Camera var1) {
      this.camera = var1;
      this.__set(0);
      return this;
   }

   public SceneBuilder cursor(Cursor var1) {
      this.cursor = var1;
      this.__set(1);
      return this;
   }

   public SceneBuilder depthBuffer(boolean var1) {
      this.depthBuffer = var1;
      return this;
   }

   public SceneBuilder eventDispatcher(EventDispatcher var1) {
      this.eventDispatcher = var1;
      this.__set(2);
      return this;
   }

   public SceneBuilder fill(Paint var1) {
      this.fill = var1;
      this.__set(3);
      return this;
   }

   public SceneBuilder height(double var1) {
      this.height = var1;
      return this;
   }

   public SceneBuilder onContextMenuRequested(EventHandler var1) {
      this.onContextMenuRequested = var1;
      this.__set(4);
      return this;
   }

   public SceneBuilder onDragDetected(EventHandler var1) {
      this.onDragDetected = var1;
      this.__set(5);
      return this;
   }

   public SceneBuilder onDragDone(EventHandler var1) {
      this.onDragDone = var1;
      this.__set(6);
      return this;
   }

   public SceneBuilder onDragDropped(EventHandler var1) {
      this.onDragDropped = var1;
      this.__set(7);
      return this;
   }

   public SceneBuilder onDragEntered(EventHandler var1) {
      this.onDragEntered = var1;
      this.__set(8);
      return this;
   }

   public SceneBuilder onDragExited(EventHandler var1) {
      this.onDragExited = var1;
      this.__set(9);
      return this;
   }

   public SceneBuilder onDragOver(EventHandler var1) {
      this.onDragOver = var1;
      this.__set(10);
      return this;
   }

   public SceneBuilder onInputMethodTextChanged(EventHandler var1) {
      this.onInputMethodTextChanged = var1;
      this.__set(11);
      return this;
   }

   public SceneBuilder onKeyPressed(EventHandler var1) {
      this.onKeyPressed = var1;
      this.__set(12);
      return this;
   }

   public SceneBuilder onKeyReleased(EventHandler var1) {
      this.onKeyReleased = var1;
      this.__set(13);
      return this;
   }

   public SceneBuilder onKeyTyped(EventHandler var1) {
      this.onKeyTyped = var1;
      this.__set(14);
      return this;
   }

   public SceneBuilder onMouseClicked(EventHandler var1) {
      this.onMouseClicked = var1;
      this.__set(15);
      return this;
   }

   public SceneBuilder onMouseDragEntered(EventHandler var1) {
      this.onMouseDragEntered = var1;
      this.__set(16);
      return this;
   }

   public SceneBuilder onMouseDragExited(EventHandler var1) {
      this.onMouseDragExited = var1;
      this.__set(17);
      return this;
   }

   public SceneBuilder onMouseDragged(EventHandler var1) {
      this.onMouseDragged = var1;
      this.__set(18);
      return this;
   }

   public SceneBuilder onMouseDragOver(EventHandler var1) {
      this.onMouseDragOver = var1;
      this.__set(19);
      return this;
   }

   public SceneBuilder onMouseDragReleased(EventHandler var1) {
      this.onMouseDragReleased = var1;
      this.__set(20);
      return this;
   }

   public SceneBuilder onMouseEntered(EventHandler var1) {
      this.onMouseEntered = var1;
      this.__set(21);
      return this;
   }

   public SceneBuilder onMouseExited(EventHandler var1) {
      this.onMouseExited = var1;
      this.__set(22);
      return this;
   }

   public SceneBuilder onMouseMoved(EventHandler var1) {
      this.onMouseMoved = var1;
      this.__set(23);
      return this;
   }

   public SceneBuilder onMousePressed(EventHandler var1) {
      this.onMousePressed = var1;
      this.__set(24);
      return this;
   }

   public SceneBuilder onMouseReleased(EventHandler var1) {
      this.onMouseReleased = var1;
      this.__set(25);
      return this;
   }

   public SceneBuilder onRotate(EventHandler var1) {
      this.onRotate = var1;
      this.__set(26);
      return this;
   }

   public SceneBuilder onRotationFinished(EventHandler var1) {
      this.onRotationFinished = var1;
      this.__set(27);
      return this;
   }

   public SceneBuilder onRotationStarted(EventHandler var1) {
      this.onRotationStarted = var1;
      this.__set(28);
      return this;
   }

   public SceneBuilder onScroll(EventHandler var1) {
      this.onScroll = var1;
      this.__set(29);
      return this;
   }

   public SceneBuilder onScrollFinished(EventHandler var1) {
      this.onScrollFinished = var1;
      this.__set(30);
      return this;
   }

   public SceneBuilder onScrollStarted(EventHandler var1) {
      this.onScrollStarted = var1;
      this.__set(31);
      return this;
   }

   public SceneBuilder onSwipeDown(EventHandler var1) {
      this.onSwipeDown = var1;
      this.__set(32);
      return this;
   }

   public SceneBuilder onSwipeLeft(EventHandler var1) {
      this.onSwipeLeft = var1;
      this.__set(33);
      return this;
   }

   public SceneBuilder onSwipeRight(EventHandler var1) {
      this.onSwipeRight = var1;
      this.__set(34);
      return this;
   }

   public SceneBuilder onSwipeUp(EventHandler var1) {
      this.onSwipeUp = var1;
      this.__set(35);
      return this;
   }

   public SceneBuilder onTouchMoved(EventHandler var1) {
      this.onTouchMoved = var1;
      this.__set(36);
      return this;
   }

   public SceneBuilder onTouchPressed(EventHandler var1) {
      this.onTouchPressed = var1;
      this.__set(37);
      return this;
   }

   public SceneBuilder onTouchReleased(EventHandler var1) {
      this.onTouchReleased = var1;
      this.__set(38);
      return this;
   }

   public SceneBuilder onTouchStationary(EventHandler var1) {
      this.onTouchStationary = var1;
      this.__set(39);
      return this;
   }

   public SceneBuilder onZoom(EventHandler var1) {
      this.onZoom = var1;
      this.__set(40);
      return this;
   }

   public SceneBuilder onZoomFinished(EventHandler var1) {
      this.onZoomFinished = var1;
      this.__set(41);
      return this;
   }

   public SceneBuilder onZoomStarted(EventHandler var1) {
      this.onZoomStarted = var1;
      this.__set(42);
      return this;
   }

   public SceneBuilder root(Parent var1) {
      this.root = var1;
      return this;
   }

   public SceneBuilder stylesheets(Collection var1) {
      this.stylesheets = var1;
      this.__set(43);
      return this;
   }

   public SceneBuilder stylesheets(String... var1) {
      return this.stylesheets((Collection)Arrays.asList(var1));
   }

   public SceneBuilder width(double var1) {
      this.width = var1;
      return this;
   }

   public Scene build() {
      Scene var1 = new Scene(this.root, this.width, this.height, this.depthBuffer);
      this.applyTo(var1);
      return var1;
   }
}
