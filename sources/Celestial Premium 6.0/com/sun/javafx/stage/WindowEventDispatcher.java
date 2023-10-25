/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.event.BasicEventDispatcher
 *  com.sun.javafx.event.CompositeEventDispatcher
 *  com.sun.javafx.event.EventHandlerManager
 *  com.sun.javafx.event.EventRedirector
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.event.EventRedirector;
import com.sun.javafx.stage.WindowCloseRequestHandler;
import javafx.stage.Window;

public class WindowEventDispatcher
extends CompositeEventDispatcher {
    private final EventRedirector eventRedirector;
    private final WindowCloseRequestHandler windowCloseRequestHandler;
    private final EventHandlerManager eventHandlerManager;

    public WindowEventDispatcher(Window window) {
        this(new EventRedirector((Object)window), new WindowCloseRequestHandler(window), new EventHandlerManager((Object)window));
    }

    public WindowEventDispatcher(EventRedirector eventRedirector, WindowCloseRequestHandler windowCloseRequestHandler, EventHandlerManager eventHandlerManager) {
        this.eventRedirector = eventRedirector;
        this.windowCloseRequestHandler = windowCloseRequestHandler;
        this.eventHandlerManager = eventHandlerManager;
        eventRedirector.insertNextDispatcher((BasicEventDispatcher)windowCloseRequestHandler);
        windowCloseRequestHandler.insertNextDispatcher((BasicEventDispatcher)eventHandlerManager);
    }

    public final EventRedirector getEventRedirector() {
        return this.eventRedirector;
    }

    public final WindowCloseRequestHandler getWindowCloseRequestHandler() {
        return this.windowCloseRequestHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    public BasicEventDispatcher getFirstDispatcher() {
        return this.eventRedirector;
    }

    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}

