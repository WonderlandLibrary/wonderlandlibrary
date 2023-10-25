/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ReadOnlyObjectProperty
 *  javafx.stage.Screen
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.util.Utils;
import java.security.AccessControlContext;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.stage.Screen;
import javafx.stage.Window;

public class WindowHelper {
    private static final WindowHelper theInstance = new WindowHelper();
    private static WindowAccessor windowAccessor;

    protected WindowHelper() {
    }

    private static WindowHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(Window window) {
        WindowHelper.setHelper(window, WindowHelper.getInstance());
    }

    private static WindowHelper getHelper(Window window) {
        return windowAccessor.getHelper(window);
    }

    protected static void setHelper(Window window, WindowHelper windowHelper) {
        windowAccessor.setHelper(window, windowHelper);
    }

    public static void visibleChanging(Window window, boolean bl) {
        WindowHelper.getHelper(window).visibleChangingImpl(window, bl);
    }

    public static void visibleChanged(Window window, boolean bl) {
        WindowHelper.getHelper(window).visibleChangedImpl(window, bl);
    }

    protected void visibleChangingImpl(Window window, boolean bl) {
        windowAccessor.doVisibleChanging(window, bl);
    }

    protected void visibleChangedImpl(Window window, boolean bl) {
        windowAccessor.doVisibleChanged(window, bl);
    }

    public static TKStage getPeer(Window window) {
        return windowAccessor.getPeer(window);
    }

    public static void setPeer(Window window, TKStage tKStage) {
        windowAccessor.setPeer(window, tKStage);
    }

    public static WindowPeerListener getPeerListener(Window window) {
        return windowAccessor.getPeerListener(window);
    }

    public static void setPeerListener(Window window, WindowPeerListener windowPeerListener) {
        windowAccessor.setPeerListener(window, windowPeerListener);
    }

    public static void setFocused(Window window, boolean bl) {
        windowAccessor.setFocused(window, bl);
    }

    public static void notifyLocationChanged(Window window, double d, double d2) {
        windowAccessor.notifyLocationChanged(window, d, d2);
    }

    public static void notifySizeChanged(Window window, double d, double d2) {
        windowAccessor.notifySizeChanged(window, d, d2);
    }

    public static void notifyScaleChanged(Window window, double d, double d2) {
        windowAccessor.notifyScaleChanged(window, d, d2);
    }

    static AccessControlContext getAccessControlContext(Window window) {
        return windowAccessor.getAccessControlContext(window);
    }

    public static void setWindowAccessor(WindowAccessor windowAccessor) {
        if (WindowHelper.windowAccessor != null) {
            throw new IllegalStateException();
        }
        WindowHelper.windowAccessor = windowAccessor;
    }

    public static WindowAccessor getWindowAccessor() {
        return windowAccessor;
    }

    static {
        Utils.forceInit(Window.class);
    }

    public static interface WindowAccessor {
        public WindowHelper getHelper(Window var1);

        public void setHelper(Window var1, WindowHelper var2);

        public void doVisibleChanging(Window var1, boolean var2);

        public void doVisibleChanged(Window var1, boolean var2);

        public TKStage getPeer(Window var1);

        public void setPeer(Window var1, TKStage var2);

        public WindowPeerListener getPeerListener(Window var1);

        public void setPeerListener(Window var1, WindowPeerListener var2);

        public void setFocused(Window var1, boolean var2);

        public void notifyLocationChanged(Window var1, double var2, double var4);

        public void notifySizeChanged(Window var1, double var2, double var4);

        public void notifyScreenChanged(Window var1, Object var2, Object var3);

        public float getPlatformScaleX(Window var1);

        public float getPlatformScaleY(Window var1);

        public void notifyScaleChanged(Window var1, double var2, double var4);

        public ReadOnlyObjectProperty<Screen> screenProperty(Window var1);

        public AccessControlContext getAccessControlContext(Window var1);
    }
}

