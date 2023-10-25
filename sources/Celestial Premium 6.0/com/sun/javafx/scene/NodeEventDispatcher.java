/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.event.BasicEventDispatcher
 *  com.sun.javafx.event.CompositeEventDispatcher
 *  com.sun.javafx.event.EventHandlerManager
 */
package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.EnteredExitedHandler;

public class NodeEventDispatcher
extends CompositeEventDispatcher {
    private final EnteredExitedHandler enteredExitedHandler;
    private final EventHandlerManager eventHandlerManager;

    public NodeEventDispatcher(Object object) {
        this(new EnteredExitedHandler(object), new EventHandlerManager(object));
    }

    public NodeEventDispatcher(EnteredExitedHandler enteredExitedHandler, EventHandlerManager eventHandlerManager) {
        this.enteredExitedHandler = enteredExitedHandler;
        this.eventHandlerManager = eventHandlerManager;
        enteredExitedHandler.insertNextDispatcher((BasicEventDispatcher)eventHandlerManager);
    }

    public final EnteredExitedHandler getEnteredExitedHandler() {
        return this.enteredExitedHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    public BasicEventDispatcher getFirstDispatcher() {
        return this.enteredExitedHandler;
    }

    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}

