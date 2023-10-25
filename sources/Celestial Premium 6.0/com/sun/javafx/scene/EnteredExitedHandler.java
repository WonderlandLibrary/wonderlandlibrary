/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.event.BasicEventDispatcher
 *  javafx.event.Event
 *  javafx.scene.input.DragEvent
 *  javafx.scene.input.MouseDragEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import javafx.event.Event;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class EnteredExitedHandler
extends BasicEventDispatcher {
    private final Object eventSource;
    private boolean eventTypeModified;

    public EnteredExitedHandler(Object object) {
        this.eventSource = object;
    }

    public final Event dispatchCapturingEvent(Event event) {
        if (this.eventSource == event.getTarget()) {
            if (event.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET) {
                this.eventTypeModified = true;
                return ((MouseEvent)event).copyFor(this.eventSource, event.getTarget(), MouseEvent.MOUSE_ENTERED);
            }
            if (event.getEventType() == MouseEvent.MOUSE_EXITED_TARGET) {
                this.eventTypeModified = true;
                return ((MouseEvent)event).copyFor(this.eventSource, event.getTarget(), MouseEvent.MOUSE_EXITED);
            }
            if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET) {
                this.eventTypeModified = true;
                return ((MouseDragEvent)event).copyFor(this.eventSource, event.getTarget(), MouseDragEvent.MOUSE_DRAG_ENTERED);
            }
            if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_EXITED_TARGET) {
                this.eventTypeModified = true;
                return ((MouseDragEvent)event).copyFor(this.eventSource, event.getTarget(), MouseDragEvent.MOUSE_DRAG_EXITED);
            }
            if (event.getEventType() == DragEvent.DRAG_ENTERED_TARGET) {
                this.eventTypeModified = true;
                return ((DragEvent)event).copyFor(this.eventSource, event.getTarget(), DragEvent.DRAG_ENTERED);
            }
            if (event.getEventType() == DragEvent.DRAG_EXITED_TARGET) {
                this.eventTypeModified = true;
                return ((DragEvent)event).copyFor(this.eventSource, event.getTarget(), DragEvent.DRAG_EXITED);
            }
        }
        this.eventTypeModified = false;
        return event;
    }

    public final Event dispatchBubblingEvent(Event event) {
        if (this.eventTypeModified && this.eventSource == event.getTarget()) {
            if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
                return ((MouseEvent)event).copyFor(this.eventSource, event.getTarget(), MouseEvent.MOUSE_ENTERED_TARGET);
            }
            if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
                return ((MouseEvent)event).copyFor(this.eventSource, event.getTarget(), MouseEvent.MOUSE_EXITED_TARGET);
            }
            if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_ENTERED) {
                this.eventTypeModified = true;
                return ((MouseDragEvent)event).copyFor(this.eventSource, event.getTarget(), MouseDragEvent.MOUSE_DRAG_ENTERED_TARGET);
            }
            if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_EXITED) {
                this.eventTypeModified = true;
                return ((MouseDragEvent)event).copyFor(this.eventSource, event.getTarget(), MouseDragEvent.MOUSE_DRAG_EXITED_TARGET);
            }
            if (event.getEventType() == DragEvent.DRAG_ENTERED) {
                return ((DragEvent)event).copyFor(this.eventSource, event.getTarget(), DragEvent.DRAG_ENTERED_TARGET);
            }
            if (event.getEventType() == DragEvent.DRAG_EXITED) {
                return ((DragEvent)event).copyFor(this.eventSource, event.getTarget(), DragEvent.DRAG_EXITED_TARGET);
            }
        }
        return event;
    }
}

