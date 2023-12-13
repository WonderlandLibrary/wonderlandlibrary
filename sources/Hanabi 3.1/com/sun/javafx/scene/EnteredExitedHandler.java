package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import javafx.event.Event;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class EnteredExitedHandler extends BasicEventDispatcher {
   private final Object eventSource;
   private boolean eventTypeModified;

   public EnteredExitedHandler(Object var1) {
      this.eventSource = var1;
   }

   public final Event dispatchCapturingEvent(Event var1) {
      if (this.eventSource == var1.getTarget()) {
         if (var1.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET) {
            this.eventTypeModified = true;
            return ((MouseEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseEvent.MOUSE_ENTERED);
         }

         if (var1.getEventType() == MouseEvent.MOUSE_EXITED_TARGET) {
            this.eventTypeModified = true;
            return ((MouseEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseEvent.MOUSE_EXITED);
         }

         if (var1.getEventType() == MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET) {
            this.eventTypeModified = true;
            return ((MouseDragEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseDragEvent.MOUSE_DRAG_ENTERED);
         }

         if (var1.getEventType() == MouseDragEvent.MOUSE_DRAG_EXITED_TARGET) {
            this.eventTypeModified = true;
            return ((MouseDragEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseDragEvent.MOUSE_DRAG_EXITED);
         }

         if (var1.getEventType() == DragEvent.DRAG_ENTERED_TARGET) {
            this.eventTypeModified = true;
            return ((DragEvent)var1).copyFor(this.eventSource, var1.getTarget(), DragEvent.DRAG_ENTERED);
         }

         if (var1.getEventType() == DragEvent.DRAG_EXITED_TARGET) {
            this.eventTypeModified = true;
            return ((DragEvent)var1).copyFor(this.eventSource, var1.getTarget(), DragEvent.DRAG_EXITED);
         }
      }

      this.eventTypeModified = false;
      return var1;
   }

   public final Event dispatchBubblingEvent(Event var1) {
      if (this.eventTypeModified && this.eventSource == var1.getTarget()) {
         if (var1.getEventType() == MouseEvent.MOUSE_ENTERED) {
            return ((MouseEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseEvent.MOUSE_ENTERED_TARGET);
         }

         if (var1.getEventType() == MouseEvent.MOUSE_EXITED) {
            return ((MouseEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseEvent.MOUSE_EXITED_TARGET);
         }

         if (var1.getEventType() == MouseDragEvent.MOUSE_DRAG_ENTERED) {
            this.eventTypeModified = true;
            return ((MouseDragEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET);
         }

         if (var1.getEventType() == MouseDragEvent.MOUSE_DRAG_EXITED) {
            this.eventTypeModified = true;
            return ((MouseDragEvent)var1).copyFor(this.eventSource, var1.getTarget(), MouseDragEvent.MOUSE_DRAG_EXITED_TARGET);
         }

         if (var1.getEventType() == DragEvent.DRAG_ENTERED) {
            return ((DragEvent)var1).copyFor(this.eventSource, var1.getTarget(), DragEvent.DRAG_ENTERED_TARGET);
         }

         if (var1.getEventType() == DragEvent.DRAG_EXITED) {
            return ((DragEvent)var1).copyFor(this.eventSource, var1.getTarget(), DragEvent.DRAG_EXITED_TARGET);
         }
      }

      return var1;
   }
}
