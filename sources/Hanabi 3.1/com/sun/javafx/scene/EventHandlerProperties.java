package com.sun.javafx.scene;

import com.sun.javafx.event.EventHandlerManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;

public final class EventHandlerProperties {
   private final EventHandlerManager eventDispatcher;
   private final Object bean;
   private EventHandlerProperty onMenuContextRequested;
   private EventHandlerProperty onMouseClicked;
   private EventHandlerProperty onMouseDragged;
   private EventHandlerProperty onMouseEntered;
   private EventHandlerProperty onMouseExited;
   private EventHandlerProperty onMouseMoved;
   private EventHandlerProperty onMousePressed;
   private EventHandlerProperty onMouseReleased;
   private EventHandlerProperty onDragDetected;
   private EventHandlerProperty onScroll;
   private EventHandlerProperty onScrollStarted;
   private EventHandlerProperty onScrollFinished;
   private EventHandlerProperty onRotationStarted;
   private EventHandlerProperty onRotate;
   private EventHandlerProperty onRotationFinished;
   private EventHandlerProperty onZoomStarted;
   private EventHandlerProperty onZoom;
   private EventHandlerProperty onZoomFinished;
   private EventHandlerProperty onSwipeUp;
   private EventHandlerProperty onSwipeDown;
   private EventHandlerProperty onSwipeLeft;
   private EventHandlerProperty onSwipeRight;
   private EventHandlerProperty onMouseDragOver;
   private EventHandlerProperty onMouseDragReleased;
   private EventHandlerProperty onMouseDragEntered;
   private EventHandlerProperty onMouseDragExited;
   private EventHandlerProperty onKeyPressed;
   private EventHandlerProperty onKeyReleased;
   private EventHandlerProperty onKeyTyped;
   private EventHandlerProperty onInputMethodTextChanged;
   private EventHandlerProperty onDragEntered;
   private EventHandlerProperty onDragExited;
   private EventHandlerProperty onDragOver;
   private EventHandlerProperty onDragDropped;
   private EventHandlerProperty onDragDone;
   private EventHandlerProperty onTouchPressed;
   private EventHandlerProperty onTouchMoved;
   private EventHandlerProperty onTouchReleased;
   private EventHandlerProperty onTouchStationary;

   public EventHandlerProperties(EventHandlerManager var1, Object var2) {
      this.eventDispatcher = var1;
      this.bean = var2;
   }

   public final EventHandler onContextMenuRequested() {
      return this.onMenuContextRequested == null ? null : (EventHandler)this.onMenuContextRequested.get();
   }

   public ObjectProperty onContextMenuRequestedProperty() {
      if (this.onMenuContextRequested == null) {
         this.onMenuContextRequested = new EventHandlerProperty(this.bean, "onMenuContextRequested", ContextMenuEvent.CONTEXT_MENU_REQUESTED);
      }

      return this.onMenuContextRequested;
   }

   public final EventHandler getOnMouseClicked() {
      return this.onMouseClicked == null ? null : (EventHandler)this.onMouseClicked.get();
   }

   public ObjectProperty onMouseClickedProperty() {
      if (this.onMouseClicked == null) {
         this.onMouseClicked = new EventHandlerProperty(this.bean, "onMouseClicked", MouseEvent.MOUSE_CLICKED);
      }

      return this.onMouseClicked;
   }

   public final EventHandler getOnMouseDragged() {
      return this.onMouseDragged == null ? null : (EventHandler)this.onMouseDragged.get();
   }

   public ObjectProperty onMouseDraggedProperty() {
      if (this.onMouseDragged == null) {
         this.onMouseDragged = new EventHandlerProperty(this.bean, "onMouseDragged", MouseEvent.MOUSE_DRAGGED);
      }

      return this.onMouseDragged;
   }

   public final EventHandler getOnMouseEntered() {
      return this.onMouseEntered == null ? null : (EventHandler)this.onMouseEntered.get();
   }

   public ObjectProperty onMouseEnteredProperty() {
      if (this.onMouseEntered == null) {
         this.onMouseEntered = new EventHandlerProperty(this.bean, "onMouseEntered", MouseEvent.MOUSE_ENTERED);
      }

      return this.onMouseEntered;
   }

   public final EventHandler getOnMouseExited() {
      return this.onMouseExited == null ? null : (EventHandler)this.onMouseExited.get();
   }

   public ObjectProperty onMouseExitedProperty() {
      if (this.onMouseExited == null) {
         this.onMouseExited = new EventHandlerProperty(this.bean, "onMouseExited", MouseEvent.MOUSE_EXITED);
      }

      return this.onMouseExited;
   }

   public final EventHandler getOnMouseMoved() {
      return this.onMouseMoved == null ? null : (EventHandler)this.onMouseMoved.get();
   }

   public ObjectProperty onMouseMovedProperty() {
      if (this.onMouseMoved == null) {
         this.onMouseMoved = new EventHandlerProperty(this.bean, "onMouseMoved", MouseEvent.MOUSE_MOVED);
      }

      return this.onMouseMoved;
   }

   public final EventHandler getOnMousePressed() {
      return this.onMousePressed == null ? null : (EventHandler)this.onMousePressed.get();
   }

   public ObjectProperty onMousePressedProperty() {
      if (this.onMousePressed == null) {
         this.onMousePressed = new EventHandlerProperty(this.bean, "onMousePressed", MouseEvent.MOUSE_PRESSED);
      }

      return this.onMousePressed;
   }

   public final EventHandler getOnMouseReleased() {
      return this.onMouseReleased == null ? null : (EventHandler)this.onMouseReleased.get();
   }

   public ObjectProperty onMouseReleasedProperty() {
      if (this.onMouseReleased == null) {
         this.onMouseReleased = new EventHandlerProperty(this.bean, "onMouseReleased", MouseEvent.MOUSE_RELEASED);
      }

      return this.onMouseReleased;
   }

   public final EventHandler getOnDragDetected() {
      return this.onDragDetected == null ? null : (EventHandler)this.onDragDetected.get();
   }

   public ObjectProperty onDragDetectedProperty() {
      if (this.onDragDetected == null) {
         this.onDragDetected = new EventHandlerProperty(this.bean, "onDragDetected", MouseEvent.DRAG_DETECTED);
      }

      return this.onDragDetected;
   }

   public final EventHandler getOnScroll() {
      return this.onScroll == null ? null : (EventHandler)this.onScroll.get();
   }

   public ObjectProperty onScrollProperty() {
      if (this.onScroll == null) {
         this.onScroll = new EventHandlerProperty(this.bean, "onScroll", ScrollEvent.SCROLL);
      }

      return this.onScroll;
   }

   public final EventHandler getOnScrollStarted() {
      return this.onScrollStarted == null ? null : (EventHandler)this.onScrollStarted.get();
   }

   public ObjectProperty onScrollStartedProperty() {
      if (this.onScrollStarted == null) {
         this.onScrollStarted = new EventHandlerProperty(this.bean, "onScrollStarted", ScrollEvent.SCROLL_STARTED);
      }

      return this.onScrollStarted;
   }

   public final EventHandler getOnScrollFinished() {
      return this.onScrollFinished == null ? null : (EventHandler)this.onScrollFinished.get();
   }

   public ObjectProperty onScrollFinishedProperty() {
      if (this.onScrollFinished == null) {
         this.onScrollFinished = new EventHandlerProperty(this.bean, "onScrollFinished", ScrollEvent.SCROLL_FINISHED);
      }

      return this.onScrollFinished;
   }

   public final EventHandler getOnRotationStarted() {
      return this.onRotationStarted == null ? null : (EventHandler)this.onRotationStarted.get();
   }

   public ObjectProperty onRotationStartedProperty() {
      if (this.onRotationStarted == null) {
         this.onRotationStarted = new EventHandlerProperty(this.bean, "onRotationStarted", RotateEvent.ROTATION_STARTED);
      }

      return this.onRotationStarted;
   }

   public final EventHandler getOnRotate() {
      return this.onRotate == null ? null : (EventHandler)this.onRotate.get();
   }

   public ObjectProperty onRotateProperty() {
      if (this.onRotate == null) {
         this.onRotate = new EventHandlerProperty(this.bean, "onRotate", RotateEvent.ROTATE);
      }

      return this.onRotate;
   }

   public final EventHandler getOnRotationFinished() {
      return this.onRotationFinished == null ? null : (EventHandler)this.onRotationFinished.get();
   }

   public ObjectProperty onRotationFinishedProperty() {
      if (this.onRotationFinished == null) {
         this.onRotationFinished = new EventHandlerProperty(this.bean, "onRotationFinished", RotateEvent.ROTATION_FINISHED);
      }

      return this.onRotationFinished;
   }

   public final EventHandler getOnZoomStarted() {
      return this.onZoomStarted == null ? null : (EventHandler)this.onZoomStarted.get();
   }

   public ObjectProperty onZoomStartedProperty() {
      if (this.onZoomStarted == null) {
         this.onZoomStarted = new EventHandlerProperty(this.bean, "onZoomStarted", ZoomEvent.ZOOM_STARTED);
      }

      return this.onZoomStarted;
   }

   public final EventHandler getOnZoom() {
      return this.onZoom == null ? null : (EventHandler)this.onZoom.get();
   }

   public ObjectProperty onZoomProperty() {
      if (this.onZoom == null) {
         this.onZoom = new EventHandlerProperty(this.bean, "onZoom", ZoomEvent.ZOOM);
      }

      return this.onZoom;
   }

   public final EventHandler getOnZoomFinished() {
      return this.onZoomFinished == null ? null : (EventHandler)this.onZoomFinished.get();
   }

   public ObjectProperty onZoomFinishedProperty() {
      if (this.onZoomFinished == null) {
         this.onZoomFinished = new EventHandlerProperty(this.bean, "onZoomFinished", ZoomEvent.ZOOM_FINISHED);
      }

      return this.onZoomFinished;
   }

   public final EventHandler getOnSwipeUp() {
      return this.onSwipeUp == null ? null : (EventHandler)this.onSwipeUp.get();
   }

   public ObjectProperty onSwipeUpProperty() {
      if (this.onSwipeUp == null) {
         this.onSwipeUp = new EventHandlerProperty(this.bean, "onSwipeUp", SwipeEvent.SWIPE_UP);
      }

      return this.onSwipeUp;
   }

   public final EventHandler getOnSwipeDown() {
      return this.onSwipeDown == null ? null : (EventHandler)this.onSwipeDown.get();
   }

   public ObjectProperty onSwipeDownProperty() {
      if (this.onSwipeDown == null) {
         this.onSwipeDown = new EventHandlerProperty(this.bean, "onSwipeDown", SwipeEvent.SWIPE_DOWN);
      }

      return this.onSwipeDown;
   }

   public final EventHandler getOnSwipeLeft() {
      return this.onSwipeLeft == null ? null : (EventHandler)this.onSwipeLeft.get();
   }

   public ObjectProperty onSwipeLeftProperty() {
      if (this.onSwipeLeft == null) {
         this.onSwipeLeft = new EventHandlerProperty(this.bean, "onSwipeLeft", SwipeEvent.SWIPE_LEFT);
      }

      return this.onSwipeLeft;
   }

   public final EventHandler getOnSwipeRight() {
      return this.onSwipeRight == null ? null : (EventHandler)this.onSwipeRight.get();
   }

   public ObjectProperty onSwipeRightProperty() {
      if (this.onSwipeRight == null) {
         this.onSwipeRight = new EventHandlerProperty(this.bean, "onSwipeRight", SwipeEvent.SWIPE_RIGHT);
      }

      return this.onSwipeRight;
   }

   public final EventHandler getOnMouseDragOver() {
      return this.onMouseDragOver == null ? null : (EventHandler)this.onMouseDragOver.get();
   }

   public ObjectProperty onMouseDragOverProperty() {
      if (this.onMouseDragOver == null) {
         this.onMouseDragOver = new EventHandlerProperty(this.bean, "onMouseDragOver", MouseDragEvent.MOUSE_DRAG_OVER);
      }

      return this.onMouseDragOver;
   }

   public final EventHandler getOnMouseDragReleased() {
      return this.onMouseDragReleased == null ? null : (EventHandler)this.onMouseDragReleased.get();
   }

   public ObjectProperty onMouseDragReleasedProperty() {
      if (this.onMouseDragReleased == null) {
         this.onMouseDragReleased = new EventHandlerProperty(this.bean, "onMouseDragReleased", MouseDragEvent.MOUSE_DRAG_RELEASED);
      }

      return this.onMouseDragReleased;
   }

   public final EventHandler getOnMouseDragEntered() {
      return this.onMouseDragEntered == null ? null : (EventHandler)this.onMouseDragEntered.get();
   }

   public ObjectProperty onMouseDragEnteredProperty() {
      if (this.onMouseDragEntered == null) {
         this.onMouseDragEntered = new EventHandlerProperty(this.bean, "onMouseDragEntered", MouseDragEvent.MOUSE_DRAG_ENTERED);
      }

      return this.onMouseDragEntered;
   }

   public final EventHandler getOnMouseDragExited() {
      return this.onMouseDragExited == null ? null : (EventHandler)this.onMouseDragExited.get();
   }

   public ObjectProperty onMouseDragExitedProperty() {
      if (this.onMouseDragExited == null) {
         this.onMouseDragExited = new EventHandlerProperty(this.bean, "onMouseDragExited", MouseDragEvent.MOUSE_DRAG_EXITED);
      }

      return this.onMouseDragExited;
   }

   public final EventHandler getOnKeyPressed() {
      return this.onKeyPressed == null ? null : (EventHandler)this.onKeyPressed.get();
   }

   public ObjectProperty onKeyPressedProperty() {
      if (this.onKeyPressed == null) {
         this.onKeyPressed = new EventHandlerProperty(this.bean, "onKeyPressed", KeyEvent.KEY_PRESSED);
      }

      return this.onKeyPressed;
   }

   public final EventHandler getOnKeyReleased() {
      return this.onKeyReleased == null ? null : (EventHandler)this.onKeyReleased.get();
   }

   public ObjectProperty onKeyReleasedProperty() {
      if (this.onKeyReleased == null) {
         this.onKeyReleased = new EventHandlerProperty(this.bean, "onKeyReleased", KeyEvent.KEY_RELEASED);
      }

      return this.onKeyReleased;
   }

   public final EventHandler getOnKeyTyped() {
      return this.onKeyTyped == null ? null : (EventHandler)this.onKeyTyped.get();
   }

   public ObjectProperty onKeyTypedProperty() {
      if (this.onKeyTyped == null) {
         this.onKeyTyped = new EventHandlerProperty(this.bean, "onKeyTyped", KeyEvent.KEY_TYPED);
      }

      return this.onKeyTyped;
   }

   public final EventHandler getOnInputMethodTextChanged() {
      return this.onInputMethodTextChanged == null ? null : (EventHandler)this.onInputMethodTextChanged.get();
   }

   public ObjectProperty onInputMethodTextChangedProperty() {
      if (this.onInputMethodTextChanged == null) {
         this.onInputMethodTextChanged = new EventHandlerProperty(this.bean, "onInputMethodTextChanged", InputMethodEvent.INPUT_METHOD_TEXT_CHANGED);
      }

      return this.onInputMethodTextChanged;
   }

   public final EventHandler getOnDragEntered() {
      return this.onDragEntered == null ? null : (EventHandler)this.onDragEntered.get();
   }

   public ObjectProperty onDragEnteredProperty() {
      if (this.onDragEntered == null) {
         this.onDragEntered = new EventHandlerProperty(this.bean, "onDragEntered", DragEvent.DRAG_ENTERED);
      }

      return this.onDragEntered;
   }

   public final EventHandler getOnDragExited() {
      return this.onDragExited == null ? null : (EventHandler)this.onDragExited.get();
   }

   public ObjectProperty onDragExitedProperty() {
      if (this.onDragExited == null) {
         this.onDragExited = new EventHandlerProperty(this.bean, "onDragExited", DragEvent.DRAG_EXITED);
      }

      return this.onDragExited;
   }

   public final EventHandler getOnDragOver() {
      return this.onDragOver == null ? null : (EventHandler)this.onDragOver.get();
   }

   public ObjectProperty onDragOverProperty() {
      if (this.onDragOver == null) {
         this.onDragOver = new EventHandlerProperty(this.bean, "onDragOver", DragEvent.DRAG_OVER);
      }

      return this.onDragOver;
   }

   public final EventHandler getOnDragDropped() {
      return this.onDragDropped == null ? null : (EventHandler)this.onDragDropped.get();
   }

   public ObjectProperty onDragDroppedProperty() {
      if (this.onDragDropped == null) {
         this.onDragDropped = new EventHandlerProperty(this.bean, "onDragDropped", DragEvent.DRAG_DROPPED);
      }

      return this.onDragDropped;
   }

   public final EventHandler getOnDragDone() {
      return this.onDragDone == null ? null : (EventHandler)this.onDragDone.get();
   }

   public ObjectProperty onDragDoneProperty() {
      if (this.onDragDone == null) {
         this.onDragDone = new EventHandlerProperty(this.bean, "onDragDone", DragEvent.DRAG_DONE);
      }

      return this.onDragDone;
   }

   public final EventHandler getOnTouchPressed() {
      return this.onTouchPressed == null ? null : (EventHandler)this.onTouchPressed.get();
   }

   public ObjectProperty onTouchPressedProperty() {
      if (this.onTouchPressed == null) {
         this.onTouchPressed = new EventHandlerProperty(this.bean, "onTouchPressed", TouchEvent.TOUCH_PRESSED);
      }

      return this.onTouchPressed;
   }

   public final EventHandler getOnTouchMoved() {
      return this.onTouchMoved == null ? null : (EventHandler)this.onTouchMoved.get();
   }

   public ObjectProperty onTouchMovedProperty() {
      if (this.onTouchMoved == null) {
         this.onTouchMoved = new EventHandlerProperty(this.bean, "onTouchMoved", TouchEvent.TOUCH_MOVED);
      }

      return this.onTouchMoved;
   }

   public final EventHandler getOnTouchReleased() {
      return this.onTouchReleased == null ? null : (EventHandler)this.onTouchReleased.get();
   }

   public ObjectProperty onTouchReleasedProperty() {
      if (this.onTouchReleased == null) {
         this.onTouchReleased = new EventHandlerProperty(this.bean, "onTouchReleased", TouchEvent.TOUCH_RELEASED);
      }

      return this.onTouchReleased;
   }

   public final EventHandler getOnTouchStationary() {
      return this.onTouchStationary == null ? null : (EventHandler)this.onTouchStationary.get();
   }

   public ObjectProperty onTouchStationaryProperty() {
      if (this.onTouchStationary == null) {
         this.onTouchStationary = new EventHandlerProperty(this.bean, "onTouchStationary", TouchEvent.TOUCH_STATIONARY);
      }

      return this.onTouchStationary;
   }

   private final class EventHandlerProperty extends SimpleObjectProperty {
      private final EventType eventType;

      public EventHandlerProperty(Object var2, String var3, EventType var4) {
         super(var2, var3);
         this.eventType = var4;
      }

      protected void invalidated() {
         EventHandlerProperties.this.eventDispatcher.setEventHandler(this.eventType, (EventHandler)this.get());
      }
   }
}
