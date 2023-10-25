/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  com.sun.javafx.collections.TrackableObservableList
 *  com.sun.javafx.logging.PulseLogger
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.event.EventType
 *  javafx.geometry.Point2D
 *  javafx.scene.input.InputMethodEvent
 *  javafx.scene.input.InputMethodHighlight
 *  javafx.scene.input.InputMethodTextRun
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.RotateEvent
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.input.SwipeEvent
 *  javafx.scene.input.TouchPoint$State
 *  javafx.scene.input.TransferMode
 *  javafx.scene.input.ZoomEvent
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.events.ViewEvent;
import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.tk.quantum.GestureRecognizers;
import com.sun.javafx.tk.quantum.GlassSceneDnDEventHandler;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.RotateGestureRecognizer;
import com.sun.javafx.tk.quantum.ScrollGestureRecognizer;
import com.sun.javafx.tk.quantum.SwipeGestureRecognizer;
import com.sun.javafx.tk.quantum.ViewScene;
import com.sun.javafx.tk.quantum.WindowStage;
import com.sun.javafx.tk.quantum.ZoomGestureRecognizer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;

class GlassViewEventHandler
extends View.EventHandler {
    static boolean zoomGestureEnabled;
    static boolean rotateGestureEnabled;
    static boolean scrollGestureEnabled;
    private ViewScene scene;
    private final GlassSceneDnDEventHandler dndHandler;
    private final GestureRecognizers gestures;
    private final PaintCollector collector = PaintCollector.getInstance();
    private final KeyEventNotification keyNotification = new KeyEventNotification();
    private int mouseButtonPressedMask = 0;
    private final MouseEventNotification mouseNotification = new MouseEventNotification();
    private ClipboardAssistance dropSourceAssistant;
    private final ViewEventNotification viewNotification = new ViewEventNotification();

    public GlassViewEventHandler(ViewScene viewScene) {
        this.scene = viewScene;
        this.dndHandler = new GlassSceneDnDEventHandler(viewScene);
        this.gestures = new GestureRecognizers();
        if (PlatformUtil.isWindows() || PlatformUtil.isIOS() || PlatformUtil.isEmbedded()) {
            this.gestures.add(new SwipeGestureRecognizer(viewScene));
        }
        if (zoomGestureEnabled) {
            this.gestures.add(new ZoomGestureRecognizer(viewScene));
        }
        if (rotateGestureEnabled) {
            this.gestures.add(new RotateGestureRecognizer(viewScene));
        }
        if (scrollGestureEnabled) {
            this.gestures.add(new ScrollGestureRecognizer(viewScene));
        }
    }

    private static boolean allowableFullScreenKeys(int n) {
        switch (n) {
            case 9: 
            case 10: 
            case 32: 
            case 33: 
            case 34: 
            case 35: 
            case 36: 
            case 37: 
            case 38: 
            case 39: 
            case 40: {
                return true;
            }
        }
        return false;
    }

    private boolean checkFullScreenKeyEvent(int n, int n2, char[] arrc, int n3) {
        return this.scene.getWindowStage().isTrustedFullScreen() || GlassViewEventHandler.allowableFullScreenKeys(n2);
    }

    private static EventType<KeyEvent> keyEventType(int n) {
        switch (n) {
            case 111: {
                return KeyEvent.KEY_PRESSED;
            }
            case 112: {
                return KeyEvent.KEY_RELEASED;
            }
            case 113: {
                return KeyEvent.KEY_TYPED;
            }
        }
        if (QuantumToolkit.verbose) {
            System.err.println("Unknown Glass key event type: " + n);
        }
        return null;
    }

    @Override
    public void handleKeyEvent(View view, long l, int n, int n2, char[] arrc, int n3) {
        this.keyNotification.view = view;
        this.keyNotification.time = l;
        this.keyNotification.type = n;
        this.keyNotification.key = n2;
        this.keyNotification.chars = arrc;
        this.keyNotification.modifiers = n3;
        QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(this.keyNotification, this.scene.getAccessControlContext()));
    }

    private static EventType<MouseEvent> mouseEventType(int n) {
        switch (n) {
            case 221: {
                return MouseEvent.MOUSE_PRESSED;
            }
            case 222: {
                return MouseEvent.MOUSE_RELEASED;
            }
            case 225: {
                return MouseEvent.MOUSE_ENTERED;
            }
            case 226: {
                return MouseEvent.MOUSE_EXITED;
            }
            case 224: {
                return MouseEvent.MOUSE_MOVED;
            }
            case 223: {
                return MouseEvent.MOUSE_DRAGGED;
            }
            case 228: {
                throw new IllegalArgumentException("WHEEL event cannot be translated to MouseEvent, must be translated to ScrollEvent");
            }
        }
        if (QuantumToolkit.verbose) {
            System.err.println("Unknown Glass mouse event type: " + n);
        }
        return null;
    }

    private static MouseButton mouseEventButton(int n) {
        switch (n) {
            case 212: {
                return MouseButton.PRIMARY;
            }
            case 213: {
                return MouseButton.SECONDARY;
            }
            case 214: {
                return MouseButton.MIDDLE;
            }
            case 215: {
                return MouseButton.BACK;
            }
            case 216: {
                return MouseButton.FORWARD;
            }
        }
        return MouseButton.NONE;
    }

    @Override
    public void handleMouseEvent(View view, long l, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl, boolean bl2) {
        this.mouseNotification.view = view;
        this.mouseNotification.time = l;
        this.mouseNotification.type = n;
        this.mouseNotification.button = n2;
        this.mouseNotification.x = n3;
        this.mouseNotification.y = n4;
        this.mouseNotification.xAbs = n5;
        this.mouseNotification.yAbs = n6;
        this.mouseNotification.modifiers = n7;
        this.mouseNotification.isPopupTrigger = bl;
        this.mouseNotification.isSynthesized = bl2;
        QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(this.mouseNotification, this.scene.getAccessControlContext()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleMenuEvent(View view, int n, int n2, int n3, int n4, boolean bl) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"MENU_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d;
                    double d2;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    Window window = view.getWindow();
                    if (window != null) {
                        d6 = window.getPlatformScaleX();
                        d5 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d4 = screen.getPlatformX();
                            d3 = screen.getPlatformY();
                            d2 = screen.getX();
                            d = screen.getY();
                        } else {
                            d = 0.0;
                            d2 = 0.0;
                            d3 = 0.0;
                            d4 = 0.0;
                        }
                    } else {
                        d5 = 1.0;
                        d6 = 1.0;
                        d = 0.0;
                        d2 = 0.0;
                        d3 = 0.0;
                        d4 = 0.0;
                    }
                    this.scene.sceneListener.menuEvent((double)n / d6, (double)n2 / d5, d2 + ((double)n3 - d4) / d6, d + ((double)n4 - d3) / d5, bl);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleScrollEvent(View view, long l, int n, int n2, int n3, int n4, double d, double d2, int n5, int n6, int n7, int n8, int n9, double d3, double d4) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"SCROLL_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d5;
                    double d6;
                    double d7;
                    double d8;
                    double d9;
                    double d10;
                    Window window = view.getWindow();
                    if (window != null) {
                        d10 = window.getPlatformScaleX();
                        d9 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d8 = screen.getPlatformX();
                            d7 = screen.getPlatformY();
                            d6 = screen.getX();
                            d5 = screen.getY();
                        } else {
                            d5 = 0.0;
                            d6 = 0.0;
                            d7 = 0.0;
                            d8 = 0.0;
                        }
                    } else {
                        d9 = 1.0;
                        d10 = 1.0;
                        d5 = 0.0;
                        d6 = 0.0;
                        d7 = 0.0;
                        d8 = 0.0;
                    }
                    this.scene.sceneListener.scrollEvent((EventType<ScrollEvent>)ScrollEvent.SCROLL, d / d10, d2 / d9, 0.0, 0.0, d3, d4, 0, n7, n6, n9, n8, (double)n / d10, (double)n2 / d9, d6 + ((double)n3 - d8) / d10, d5 + ((double)n4 - d7) / d9, (n5 & 1) != 0, (n5 & 4) != 0, (n5 & 8) != 0, (n5 & 0x10) != 0, false, false);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    private static byte inputMethodEventAttrValue(int n, int[] arrn, byte[] arrby) {
        if (arrn != null) {
            for (int i = 0; i < arrn.length - 1; ++i) {
                if (n < arrn[i] || n >= arrn[i + 1]) continue;
                return arrby[i];
            }
        }
        return 4;
    }

    private static ObservableList<InputMethodTextRun> inputMethodEventComposed(String string, int n, int[] arrn, int[] arrn2, byte[] arrby) {
        TrackableObservableList<InputMethodTextRun> trackableObservableList = new TrackableObservableList<InputMethodTextRun>(){

            protected void onChanged(ListChangeListener.Change<InputMethodTextRun> change) {
            }
        };
        if (n < string.length()) {
            if (arrn == null) {
                trackableObservableList.add((Object)new InputMethodTextRun(string.substring(n), InputMethodHighlight.UNSELECTED_RAW));
            } else {
                for (int i = 0; i < arrn.length - 1; ++i) {
                    InputMethodHighlight inputMethodHighlight;
                    if (arrn[i] < n) continue;
                    switch (GlassViewEventHandler.inputMethodEventAttrValue(arrn[i], arrn2, arrby)) {
                        case 1: {
                            inputMethodHighlight = InputMethodHighlight.SELECTED_CONVERTED;
                            break;
                        }
                        case 2: {
                            inputMethodHighlight = InputMethodHighlight.UNSELECTED_CONVERTED;
                            break;
                        }
                        case 3: {
                            inputMethodHighlight = InputMethodHighlight.SELECTED_RAW;
                            break;
                        }
                        default: {
                            inputMethodHighlight = InputMethodHighlight.UNSELECTED_RAW;
                        }
                    }
                    trackableObservableList.add((Object)new InputMethodTextRun(string.substring(arrn[i], arrn[i + 1]), inputMethodHighlight));
                }
            }
        }
        return trackableObservableList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleInputMethodEvent(long l, String string, int[] arrn, int[] arrn2, byte[] arrby, int n, int n2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"INPUT_METHOD_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    String string2 = string != null ? string : "";
                    EventType eventType = InputMethodEvent.INPUT_METHOD_TEXT_CHANGED;
                    ObservableList<InputMethodTextRun> observableList = GlassViewEventHandler.inputMethodEventComposed(string2, n, arrn, arrn2, arrby);
                    String string3 = string2.substring(0, n);
                    this.scene.sceneListener.inputMethodEvent((EventType<InputMethodEvent>)eventType, observableList, string3, n2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    @Override
    public double[] getInputMethodCandidatePos(int n) {
        Point2D point2D = this.scene.inputMethodRequests.getTextLocation(n);
        double[] arrd = new double[]{point2D.getX(), point2D.getY()};
        return arrd;
    }

    private static TransferMode actionToTransferMode(int n) {
        if (n == 0) {
            return null;
        }
        if (n == 1 || n == 0x40000001) {
            return TransferMode.COPY;
        }
        if (n == 2 || n == 0x40000002) {
            return TransferMode.MOVE;
        }
        if (n == 0x40000000) {
            return TransferMode.LINK;
        }
        if (n == 3) {
            if (QuantumToolkit.verbose) {
                System.err.println("Ambiguous drop action: " + Integer.toHexString(n));
            }
        } else if (QuantumToolkit.verbose) {
            System.err.println("Unknown drop action: " + Integer.toHexString(n));
        }
        return null;
    }

    private static int transferModeToAction(TransferMode transferMode) {
        if (transferMode == null) {
            return 0;
        }
        switch (transferMode) {
            case COPY: {
                return 1;
            }
            case MOVE: {
                return 2;
            }
            case LINK: {
                return 0x40000000;
            }
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int handleDragEnter(View view, int n, int n2, int n3, int n4, int n5, ClipboardAssistance clipboardAssistance) {
        TransferMode transferMode;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"DRAG_ENTER");
        }
        try {
            transferMode = QuantumToolkit.runWithoutRenderLock(() -> this.dndHandler.handleDragEnter(n, n2, n3, n4, GlassViewEventHandler.actionToTransferMode(n5), clipboardAssistance));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        return GlassViewEventHandler.transferModeToAction(transferMode);
    }

    @Override
    public void handleDragLeave(View view, ClipboardAssistance clipboardAssistance) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"DRAG_LEAVE");
        }
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragLeave(clipboardAssistance);
                return null;
            });
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int handleDragDrop(View view, int n, int n2, int n3, int n4, int n5, ClipboardAssistance clipboardAssistance) {
        TransferMode transferMode;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"DRAG_DROP");
        }
        try {
            transferMode = QuantumToolkit.runWithoutRenderLock(() -> this.dndHandler.handleDragDrop(n, n2, n3, n4, GlassViewEventHandler.actionToTransferMode(n5), clipboardAssistance));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        return GlassViewEventHandler.transferModeToAction(transferMode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int handleDragOver(View view, int n, int n2, int n3, int n4, int n5, ClipboardAssistance clipboardAssistance) {
        TransferMode transferMode;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"DRAG_OVER");
        }
        try {
            transferMode = QuantumToolkit.runWithoutRenderLock(() -> this.dndHandler.handleDragOver(n, n2, n3, n4, GlassViewEventHandler.actionToTransferMode(n5), clipboardAssistance));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        return GlassViewEventHandler.transferModeToAction(transferMode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleDragStart(View view, int n, int n2, int n3, int n4, int n5, ClipboardAssistance clipboardAssistance) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"DRAG_START");
        }
        this.dropSourceAssistant = clipboardAssistance;
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragStart(n, n2, n3, n4, n5, clipboardAssistance);
                return null;
            });
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    @Override
    public void handleDragEnd(View view, int n) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"DRAG_END");
        }
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragEnd(GlassViewEventHandler.actionToTransferMode(n), this.dropSourceAssistant);
                return null;
            });
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleViewEvent(View view, long l, int n) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)("VIEW_EVENT: " + ViewEvent.getTypeString(n)));
        }
        this.viewNotification.view = view;
        this.viewNotification.time = l;
        this.viewNotification.type = n;
        try {
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(this.viewNotification, this.scene.getAccessControlContext()));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleScrollGestureEvent(View view, long l, int n, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, double d, double d2, double d3, double d4, double d5, double d6) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"SCROLL_GESTURE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d7;
                    double d8;
                    double d9;
                    double d10;
                    double d11;
                    double d12;
                    EventType eventType;
                    switch (n) {
                        case 1: {
                            eventType = ScrollEvent.SCROLL_STARTED;
                            break;
                        }
                        case 2: {
                            eventType = ScrollEvent.SCROLL;
                            break;
                        }
                        case 3: {
                            eventType = ScrollEvent.SCROLL_FINISHED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown scroll event type: " + n);
                        }
                    }
                    Window window = view.getWindow();
                    if (window != null) {
                        d12 = window.getPlatformScaleX();
                        d11 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d10 = screen.getPlatformX();
                            d9 = screen.getPlatformY();
                            d8 = screen.getX();
                            d7 = screen.getY();
                        } else {
                            d7 = 0.0;
                            d8 = 0.0;
                            d9 = 0.0;
                            d10 = 0.0;
                        }
                    } else {
                        d11 = 1.0;
                        d12 = 1.0;
                        d7 = 0.0;
                        d8 = 0.0;
                        d9 = 0.0;
                        d10 = 0.0;
                    }
                    this.scene.sceneListener.scrollEvent((EventType<ScrollEvent>)eventType, d / d12, d2 / d11, d3 / d12, d4 / d11, d5, d6, n3, 0, 0, 0, 0, n4 == Integer.MAX_VALUE ? Double.NaN : (double)n4 / d12, n5 == Integer.MAX_VALUE ? Double.NaN : (double)n5 / d11, n6 == Integer.MAX_VALUE ? Double.NaN : d8 + ((double)n6 - d10) / d12, n7 == Integer.MAX_VALUE ? Double.NaN : d7 + ((double)n7 - d9) / d11, (n2 & 1) != 0, (n2 & 4) != 0, (n2 & 8) != 0, (n2 & 0x10) != 0, bl, bl2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleZoomGestureEvent(View view, long l, int n, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, double d, double d2, double d3, double d4) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"ZOOM_GESTURE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    double d7;
                    double d8;
                    EventType eventType;
                    switch (n) {
                        case 1: {
                            eventType = ZoomEvent.ZOOM_STARTED;
                            break;
                        }
                        case 2: {
                            eventType = ZoomEvent.ZOOM;
                            break;
                        }
                        case 3: {
                            eventType = ZoomEvent.ZOOM_FINISHED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown scroll event type: " + n);
                        }
                    }
                    Window window = view.getWindow();
                    if (window != null) {
                        d8 = window.getPlatformScaleX();
                        d7 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d6 = screen.getPlatformX();
                            d5 = screen.getPlatformY();
                            d4 = screen.getX();
                            d3 = screen.getY();
                        } else {
                            d3 = 0.0;
                            d4 = 0.0;
                            d5 = 0.0;
                            d6 = 0.0;
                        }
                    } else {
                        d7 = 1.0;
                        d8 = 1.0;
                        d3 = 0.0;
                        d4 = 0.0;
                        d5 = 0.0;
                        d6 = 0.0;
                    }
                    this.scene.sceneListener.zoomEvent((EventType<ZoomEvent>)eventType, d, d3, n3 == Integer.MAX_VALUE ? Double.NaN : (double)n3 / d8, n4 == Integer.MAX_VALUE ? Double.NaN : (double)n4 / d7, n5 == Integer.MAX_VALUE ? Double.NaN : d4 + ((double)n5 - d6) / d8, n6 == Integer.MAX_VALUE ? Double.NaN : d3 + ((double)n6 - d5) / d7, (n2 & 1) != 0, (n2 & 4) != 0, (n2 & 8) != 0, (n2 & 0x10) != 0, bl, bl2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleRotateGestureEvent(View view, long l, int n, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, double d, double d2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"ROTATE_GESTURE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    double d7;
                    double d8;
                    EventType eventType;
                    switch (n) {
                        case 1: {
                            eventType = RotateEvent.ROTATION_STARTED;
                            break;
                        }
                        case 2: {
                            eventType = RotateEvent.ROTATE;
                            break;
                        }
                        case 3: {
                            eventType = RotateEvent.ROTATION_FINISHED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown scroll event type: " + n);
                        }
                    }
                    Window window = view.getWindow();
                    if (window != null) {
                        d8 = window.getPlatformScaleX();
                        d7 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d6 = screen.getPlatformX();
                            d5 = screen.getPlatformY();
                            d4 = screen.getX();
                            d3 = screen.getY();
                        } else {
                            d3 = 0.0;
                            d4 = 0.0;
                            d5 = 0.0;
                            d6 = 0.0;
                        }
                    } else {
                        d7 = 1.0;
                        d8 = 1.0;
                        d3 = 0.0;
                        d4 = 0.0;
                        d5 = 0.0;
                        d6 = 0.0;
                    }
                    this.scene.sceneListener.rotateEvent((EventType<RotateEvent>)eventType, d, d2, n3 == Integer.MAX_VALUE ? Double.NaN : (double)n3 / d8, n4 == Integer.MAX_VALUE ? Double.NaN : (double)n4 / d7, n5 == Integer.MAX_VALUE ? Double.NaN : d4 + ((double)n5 - d6) / d8, n6 == Integer.MAX_VALUE ? Double.NaN : d3 + ((double)n6 - d5) / d7, (n2 & 1) != 0, (n2 & 4) != 0, (n2 & 8) != 0, (n2 & 0x10) != 0, bl, bl2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleSwipeGestureEvent(View view, long l, int n, int n2, boolean bl, boolean bl2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"SWIPE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d;
                    double d2;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    EventType eventType;
                    switch (n4) {
                        case 1: {
                            eventType = SwipeEvent.SWIPE_UP;
                            break;
                        }
                        case 2: {
                            eventType = SwipeEvent.SWIPE_DOWN;
                            break;
                        }
                        case 3: {
                            eventType = SwipeEvent.SWIPE_LEFT;
                            break;
                        }
                        case 4: {
                            eventType = SwipeEvent.SWIPE_RIGHT;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown swipe event direction: " + n4);
                        }
                    }
                    Window window = view.getWindow();
                    if (window != null) {
                        d6 = window.getPlatformScaleX();
                        d5 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d4 = screen.getPlatformX();
                            d3 = screen.getPlatformY();
                            d2 = screen.getX();
                            d = screen.getY();
                        } else {
                            d = 0.0;
                            d2 = 0.0;
                            d3 = 0.0;
                            d4 = 0.0;
                        }
                    } else {
                        d5 = 1.0;
                        d6 = 1.0;
                        d = 0.0;
                        d2 = 0.0;
                        d3 = 0.0;
                        d4 = 0.0;
                    }
                    this.scene.sceneListener.swipeEvent((EventType<SwipeEvent>)eventType, n3, n5 == Integer.MAX_VALUE ? Double.NaN : (double)n5 / d6, n6 == Integer.MAX_VALUE ? Double.NaN : (double)n6 / d5, n7 == Integer.MAX_VALUE ? Double.NaN : d2 + ((double)n7 - d4) / d6, n8 == Integer.MAX_VALUE ? Double.NaN : d + ((double)n8 - d3) / d5, (n2 & 1) != 0, (n2 & 4) != 0, (n2 & 8) != 0, (n2 & 0x10) != 0, bl);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleBeginTouchEvent(View view, long l, int n, boolean bl, int n2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"BEGIN_TOUCH_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    this.scene.sceneListener.touchEventBegin(l, n2, bl, (n & 1) != 0, (n & 4) != 0, (n & 8) != 0, (n & 0x10) != 0);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        this.gestures.notifyBeginTouchEvent(l, n, bl, n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleNextTouchEvent(View view, long l, int n, long l2, int n2, int n3, int n4, int n5) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"NEXT_TOUCH_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d;
                    double d2;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    TouchPoint.State state;
                    switch (n) {
                        case 811: {
                            state = TouchPoint.State.PRESSED;
                            break;
                        }
                        case 812: {
                            state = TouchPoint.State.MOVED;
                            break;
                        }
                        case 814: {
                            state = TouchPoint.State.STATIONARY;
                            break;
                        }
                        case 813: {
                            state = TouchPoint.State.RELEASED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown touch state: " + n);
                        }
                    }
                    Window window = view.getWindow();
                    if (window != null) {
                        d6 = window.getPlatformScaleX();
                        d5 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d4 = screen.getPlatformX();
                            d3 = screen.getPlatformY();
                            d2 = screen.getX();
                            d = screen.getY();
                        } else {
                            d = 0.0;
                            d2 = 0.0;
                            d3 = 0.0;
                            d4 = 0.0;
                        }
                    } else {
                        d5 = 1.0;
                        d6 = 1.0;
                        d = 0.0;
                        d2 = 0.0;
                        d3 = 0.0;
                        d4 = 0.0;
                    }
                    this.scene.sceneListener.touchEventNext(state, l2, (double)n2 / d6, (double)n3 / d5, d2 + ((double)n4 - d4) / d6, d + ((double)n5 - d3) / d5);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        this.gestures.notifyNextTouchEvent(l, n, l2, n2, n3, n4, n5);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleEndTouchEvent(View view, long l) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput((String)"END_TOUCH_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    this.scene.sceneListener.touchEventEnd();
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        this.gestures.notifyEndTouchEvent(l);
    }

    @Override
    public Accessible getSceneAccessible() {
        if (this.scene != null && this.scene.sceneListener != null) {
            return this.scene.sceneListener.getSceneAccessible();
        }
        return null;
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            zoomGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.zoom", "false"));
            rotateGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.rotate", "false"));
            scrollGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.scroll", "false"));
            return null;
        });
    }

    private class KeyEventNotification
    implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;
        int key;
        char[] chars;
        int modifiers;
        private KeyCode lastKeyCode;

        private KeyEventNotification() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public Void run() {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput((String)GlassViewEventHandler.keyEventType(this.type).toString());
            }
            WindowStage windowStage = GlassViewEventHandler.this.scene.getWindowStage();
            try {
                String string;
                boolean bl = (this.modifiers & 1) != 0;
                boolean bl2 = (this.modifiers & 4) != 0;
                boolean bl3 = (this.modifiers & 8) != 0;
                boolean bl4 = (this.modifiers & 0x10) != 0;
                String string2 = string = new String(this.chars);
                KeyEvent keyEvent = new KeyEvent(GlassViewEventHandler.keyEventType(this.type), string, string2, KeyCodeMap.valueOf(this.key), bl, bl2, bl3, bl4);
                KeyCode keyCode = KeyCodeMap.valueOf(this.key);
                switch (this.type) {
                    case 111: 
                    case 112: {
                        this.lastKeyCode = keyCode;
                        break;
                    }
                    case 113: {
                        keyCode = this.lastKeyCode;
                    }
                }
                if (windowStage != null) {
                    if (keyCode == KeyCode.ESCAPE) {
                        windowStage.setInAllowedEventHandler(false);
                    } else {
                        windowStage.setInAllowedEventHandler(true);
                    }
                }
                switch (this.type) {
                    case 111: {
                        if (this.view.isInFullscreen() && windowStage != null && windowStage.getSavedFullScreenExitKey() != null && windowStage.getSavedFullScreenExitKey().match(keyEvent)) {
                            windowStage.exitFullScreen();
                        }
                    }
                    case 112: 
                    case 113: {
                        if (this.view.isInFullscreen() && !GlassViewEventHandler.this.checkFullScreenKeyEvent(this.type, this.key, this.chars, this.modifiers)) {
                            return null;
                        } else {
                            if (GlassViewEventHandler.this.scene.sceneListener == null) return null;
                            GlassViewEventHandler.this.scene.sceneListener.keyEvent(keyEvent);
                            return null;
                        }
                    }
                    default: {
                        if (!QuantumToolkit.verbose) return null;
                        System.out.println("handleKeyEvent: unhandled type: " + this.type);
                        return null;
                    }
                }
            }
            finally {
                if (windowStage != null) {
                    windowStage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
            }
        }
    }

    private class MouseEventNotification
    implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;
        int button;
        int x;
        int y;
        int xAbs;
        int yAbs;
        int modifiers;
        boolean isPopupTrigger;
        boolean isSynthesized;

        private MouseEventNotification() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Void run() {
            int n;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput((String)GlassViewEventHandler.mouseEventType(this.type).toString());
            }
            switch (this.button) {
                case 212: {
                    n = 32;
                    break;
                }
                case 214: {
                    n = 128;
                    break;
                }
                case 213: {
                    n = 64;
                    break;
                }
                case 215: {
                    n = 256;
                    break;
                }
                case 216: {
                    n = 512;
                    break;
                }
                default: {
                    n = 0;
                }
            }
            switch (this.type) {
                case 224: {
                    if (this.button == 211) break;
                    return null;
                }
                case 222: {
                    if ((GlassViewEventHandler.this.mouseButtonPressedMask & n) == 0) {
                        return null;
                    }
                    GlassViewEventHandler.this.mouseButtonPressedMask &= ~n;
                    break;
                }
                case 221: {
                    GlassViewEventHandler.this.mouseButtonPressedMask |= n;
                    break;
                }
                case 225: 
                case 226: {
                    break;
                }
                case 227: {
                    return null;
                }
                default: {
                    if (!QuantumToolkit.verbose) break;
                    System.out.println("handleMouseEvent: unhandled type: " + this.type);
                }
            }
            WindowStage windowStage = GlassViewEventHandler.this.scene.getWindowStage();
            try {
                if (windowStage != null) {
                    switch (this.type) {
                        case 221: 
                        case 222: {
                            windowStage.setInAllowedEventHandler(true);
                            break;
                        }
                        default: {
                            windowStage.setInAllowedEventHandler(false);
                        }
                    }
                }
                if (GlassViewEventHandler.this.scene.sceneListener != null) {
                    double d;
                    double d2;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    boolean bl = (this.modifiers & 1) != 0;
                    boolean bl2 = (this.modifiers & 4) != 0;
                    boolean bl3 = (this.modifiers & 8) != 0;
                    boolean bl4 = (this.modifiers & 0x10) != 0;
                    boolean bl5 = (this.modifiers & 0x20) != 0;
                    boolean bl6 = (this.modifiers & 0x80) != 0;
                    boolean bl7 = (this.modifiers & 0x40) != 0;
                    boolean bl8 = (this.modifiers & 0x100) != 0;
                    boolean bl9 = (this.modifiers & 0x200) != 0;
                    Window window = this.view.getWindow();
                    if (window != null) {
                        d6 = window.getPlatformScaleX();
                        d5 = window.getPlatformScaleY();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d4 = screen.getPlatformX();
                            d3 = screen.getPlatformY();
                            d2 = screen.getX();
                            d = screen.getY();
                        } else {
                            d = 0.0;
                            d2 = 0.0;
                            d3 = 0.0;
                            d4 = 0.0;
                        }
                    } else {
                        d5 = 1.0;
                        d6 = 1.0;
                        d = 0.0;
                        d2 = 0.0;
                        d3 = 0.0;
                        d4 = 0.0;
                    }
                    GlassViewEventHandler.this.scene.sceneListener.mouseEvent(GlassViewEventHandler.mouseEventType(this.type), (double)this.x / d6, (double)this.y / d5, d2 + ((double)this.xAbs - d4) / d6, d + ((double)this.yAbs - d3) / d5, GlassViewEventHandler.mouseEventButton(this.button), this.isPopupTrigger, this.isSynthesized, bl, bl2, bl3, bl4, bl5, bl6, bl7, bl8, bl9);
                }
            }
            finally {
                if (windowStage != null) {
                    windowStage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
            }
            return null;
        }
    }

    private class ViewEventNotification
    implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;

        private ViewEventNotification() {
        }

        @Override
        public Void run() {
            if (GlassViewEventHandler.this.scene.sceneListener == null) {
                return null;
            }
            switch (this.type) {
                case 421: {
                    WindowStage windowStage;
                    Window window = this.view.getWindow();
                    if (window != null && window.getMinimumWidth() == this.view.getWidth() && !window.isVisible()) break;
                    if (QuantumToolkit.drawInPaint && window != null && window.isVisible() && (windowStage = GlassViewEventHandler.this.scene.getWindowStage()) != null && !windowStage.isApplet()) {
                        GlassViewEventHandler.this.collector.liveRepaintRenderJob(GlassViewEventHandler.this.scene);
                    }
                    GlassViewEventHandler.this.scene.entireSceneNeedsRepaint();
                    break;
                }
                case 422: {
                    WindowStage windowStage;
                    Window window = this.view.getWindow();
                    float f = window == null ? 1.0f : window.getPlatformScaleX();
                    float f2 = window == null ? 1.0f : window.getPlatformScaleY();
                    GlassViewEventHandler.this.scene.sceneListener.changedSize((float)this.view.getWidth() / f, (float)this.view.getHeight() / f2);
                    GlassViewEventHandler.this.scene.entireSceneNeedsRepaint();
                    QuantumToolkit.runWithRenderLock(() -> {
                        GlassViewEventHandler.this.scene.updateSceneState();
                        return null;
                    });
                    if (!QuantumToolkit.liveResize || window == null || !window.isVisible() || (windowStage = GlassViewEventHandler.this.scene.getWindowStage()) == null || windowStage.isApplet()) break;
                    GlassViewEventHandler.this.collector.liveRepaintRenderJob(GlassViewEventHandler.this.scene);
                    break;
                }
                case 423: {
                    Window window = this.view.getWindow();
                    float f = window == null ? 1.0f : window.getPlatformScaleX();
                    float f3 = window == null ? 1.0f : window.getPlatformScaleY();
                    GlassViewEventHandler.this.scene.sceneListener.changedLocation((float)this.view.getX() / f, (float)this.view.getY() / f3);
                    break;
                }
                case 431: 
                case 432: {
                    if (GlassViewEventHandler.this.scene.getWindowStage() == null) break;
                    GlassViewEventHandler.this.scene.getWindowStage().fullscreenChanged(this.type == 431);
                    break;
                }
                case 411: 
                case 412: {
                    break;
                }
                default: {
                    throw new RuntimeException("handleViewEvent: unhandled type: " + this.type);
                }
            }
            return null;
        }
    }
}

