/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Screen {
    private static volatile List<Screen> screens = null;
    private static final int dpiOverride = AccessController.doPrivileged(() -> Integer.getInteger("com.sun.javafx.screenDPI", 0));
    private static EventHandler eventHandler;
    private volatile long ptr;
    private volatile int adapter;
    private final int depth;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int platformX;
    private final int platformY;
    private final int platformWidth;
    private final int platformHeight;
    private final int visibleX;
    private final int visibleY;
    private final int visibleWidth;
    private final int visibleHeight;
    private final int resolutionX;
    private final int resolutionY;
    private final float platformScaleX;
    private final float platformScaleY;
    private final float outputScaleX;
    private final float outputScaleY;

    public static double getVideoRefreshPeriod() {
        Application.checkEventThread();
        return Application.GetApplication().staticScreen_getVideoRefreshPeriod();
    }

    public static Screen getMainScreen() {
        return Screen.getScreens().get(0);
    }

    public static List<Screen> getScreens() {
        if (screens == null) {
            throw new RuntimeException("Internal graphics not initialized yet");
        }
        return screens;
    }

    public Screen(long l, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13, int n14, int n15, float f, float f2, float f3, float f4) {
        this.ptr = l;
        this.depth = n;
        this.x = n2;
        this.y = n3;
        this.width = n4;
        this.height = n5;
        this.platformX = n6;
        this.platformY = n7;
        this.platformWidth = n8;
        this.platformHeight = n9;
        this.visibleX = n10;
        this.visibleY = n11;
        this.visibleWidth = n12;
        this.visibleHeight = n13;
        if (dpiOverride > 0) {
            this.resolutionX = this.resolutionY = dpiOverride;
        } else {
            this.resolutionX = n14;
            this.resolutionY = n15;
        }
        this.platformScaleX = f;
        this.platformScaleY = f2;
        this.outputScaleX = f3;
        this.outputScaleY = f4;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getPlatformX() {
        return this.platformX;
    }

    public int getPlatformY() {
        return this.platformY;
    }

    public int getPlatformWidth() {
        return this.platformWidth;
    }

    public int getPlatformHeight() {
        return this.platformHeight;
    }

    public float fromPlatformX(int n) {
        return (float)this.x + (float)(n - this.platformX) / this.platformScaleX;
    }

    public float fromPlatformY(int n) {
        return (float)this.y + (float)(n - this.platformY) / this.platformScaleY;
    }

    public int toPlatformX(float f) {
        return this.platformX + Math.round((f - (float)this.x) * this.platformScaleX);
    }

    public int toPlatformY(float f) {
        return this.platformY + Math.round((f - (float)this.y) * this.platformScaleY);
    }

    public float portionIntersectsPlatformRect(int n, int n2, int n3, int n4) {
        int n5 = Math.max(n, this.platformX);
        int n6 = Math.max(n2, this.platformY);
        int n7 = Math.min(n + n3, this.platformX + this.platformWidth);
        int n8 = Math.min(n2 + n4, this.platformY + this.platformHeight);
        if ((n7 -= n5) <= 0) {
            return 0.0f;
        }
        if ((n8 -= n6) <= 0) {
            return 0.0f;
        }
        float f = n7 * n8;
        return f / (float)n3 / (float)n4;
    }

    public boolean containsPlatformRect(int n, int n2, int n3, int n4) {
        if (!this.containsPlatformCoords(n, n2)) {
            return false;
        }
        if (n3 <= 0 || n4 <= 0) {
            return true;
        }
        return n + n3 <= this.platformX + this.platformWidth && n2 + n4 <= this.platformY + this.platformHeight;
    }

    public boolean containsPlatformCoords(int n, int n2) {
        return (n -= this.platformX) >= 0 && n < this.platformWidth && (n2 -= this.platformY) >= 0 && n2 < this.platformHeight;
    }

    public float getPlatformScaleX() {
        return this.platformScaleX;
    }

    public float getPlatformScaleY() {
        return this.platformScaleY;
    }

    public float getRecommendedOutputScaleX() {
        return this.outputScaleX;
    }

    public float getRecommendedOutputScaleY() {
        return this.outputScaleY;
    }

    public int getVisibleX() {
        return this.visibleX;
    }

    public int getVisibleY() {
        return this.visibleY;
    }

    public int getVisibleWidth() {
        return this.visibleWidth;
    }

    public int getVisibleHeight() {
        return this.visibleHeight;
    }

    public int getResolutionX() {
        return this.resolutionX;
    }

    public int getResolutionY() {
        return this.resolutionY;
    }

    public long getNativeScreen() {
        return this.ptr;
    }

    private void dispose() {
        this.ptr = 0L;
    }

    public int getAdapterOrdinal() {
        return this.adapter;
    }

    public void setAdapterOrdinal(int n) {
        this.adapter = n;
    }

    public static void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        Screen.eventHandler = eventHandler;
    }

    public static void notifySettingsChanged() {
        List<Screen> list = screens;
        Screen.initScreens();
        if (eventHandler != null) {
            eventHandler.handleSettingsChanged();
        }
        List<Window> list2 = Window.getWindows();
        block0: for (Window object : list2) {
            Screen screen = object.getScreen();
            for (Screen screen2 : screens) {
                if (screen.getNativeScreen() != screen2.getNativeScreen()) continue;
                object.setScreen(screen2);
                continue block0;
            }
        }
        if (list != null) {
            for (Screen screen : list) {
                screen.dispose();
            }
        }
    }

    static void initScreens() {
        Application.checkEventThread();
        Screen[] arrscreen = Application.GetApplication().staticScreen_getScreens();
        if (arrscreen == null) {
            throw new RuntimeException("Internal graphics failed to initialize");
        }
        screens = Collections.unmodifiableList(Arrays.asList(arrscreen));
    }

    public String toString() {
        return "Screen:\n    ptr:" + this.getNativeScreen() + "\n    adapter:" + this.getAdapterOrdinal() + "\n    depth:" + this.getDepth() + "\n    x:" + this.getX() + "\n    y:" + this.getY() + "\n    width:" + this.getWidth() + "\n    height:" + this.getHeight() + "\n    platformX:" + this.getPlatformX() + "\n    platformY:" + this.getPlatformY() + "\n    platformWidth:" + this.getPlatformWidth() + "\n    platformHeight:" + this.getPlatformHeight() + "\n    visibleX:" + this.getVisibleX() + "\n    visibleY:" + this.getVisibleY() + "\n    visibleWidth:" + this.getVisibleWidth() + "\n    visibleHeight:" + this.getVisibleHeight() + "\n    platformScaleX:" + this.getPlatformScaleX() + "\n    platformScaleY:" + this.getPlatformScaleY() + "\n    outputScaleX:" + this.getRecommendedOutputScaleX() + "\n    outputScaleY:" + this.getRecommendedOutputScaleY() + "\n    resolutionX:" + this.getResolutionX() + "\n    resolutionY:" + this.getResolutionY() + "\n";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Screen screen = (Screen)object;
        return this.ptr == screen.ptr && this.adapter == screen.adapter && this.depth == screen.depth && this.x == screen.x && this.y == screen.y && this.width == screen.width && this.height == screen.height && this.visibleX == screen.visibleX && this.visibleY == screen.visibleY && this.visibleWidth == screen.visibleWidth && this.visibleHeight == screen.visibleHeight && this.resolutionX == screen.resolutionX && this.resolutionY == screen.resolutionY && Float.compare(screen.platformScaleX, this.platformScaleX) == 0 && Float.compare(screen.platformScaleY, this.platformScaleY) == 0 && Float.compare(screen.outputScaleX, this.outputScaleX) == 0 && Float.compare(screen.outputScaleY, this.outputScaleY) == 0;
    }

    public int hashCode() {
        int n = 17;
        n = 31 * n + (int)(this.ptr ^ this.ptr >>> 32);
        n = 31 * n + this.adapter;
        n = 31 * n + this.depth;
        n = 31 * n + this.x;
        n = 31 * n + this.y;
        n = 31 * n + this.width;
        n = 31 * n + this.height;
        n = 31 * n + this.visibleX;
        n = 31 * n + this.visibleY;
        n = 31 * n + this.visibleWidth;
        n = 31 * n + this.visibleHeight;
        n = 31 * n + this.resolutionX;
        n = 31 * n + this.resolutionY;
        n = 31 * n + (this.platformScaleX != 0.0f ? Float.floatToIntBits(this.platformScaleX) : 0);
        n = 31 * n + (this.platformScaleY != 0.0f ? Float.floatToIntBits(this.platformScaleY) : 0);
        n = 31 * n + (this.outputScaleX != 0.0f ? Float.floatToIntBits(this.outputScaleX) : 0);
        n = 31 * n + (this.outputScaleY != 0.0f ? Float.floatToIntBits(this.outputScaleY) : 0);
        return n;
    }

    public static class EventHandler {
        public void handleSettingsChanged() {
        }
    }
}

