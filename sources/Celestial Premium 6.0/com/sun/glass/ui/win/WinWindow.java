/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;

class WinWindow
extends Window {
    public static final int RESIZE_DISABLE = 0;
    public static final int RESIZE_AROUND_ANCHOR = 1;
    public static final int RESIZE_TO_FX_ORIGIN = 2;
    public static final long ANCHOR_NO_CAPTURE = Long.MIN_VALUE;
    float fxReqWidth;
    float fxReqHeight;
    int pfReqWidth;
    int pfReqHeight;
    private boolean deferredClosing = false;
    private boolean closingRequested = false;

    private static native void _initIDs();

    protected WinWindow(Window window, Screen screen, int n) {
        super(window, screen, n);
    }

    protected WinWindow(long l) {
        super(l);
    }

    @Override
    public void setBounds(float f, float f2, boolean bl, boolean bl2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (bl || bl2 || f3 > 0.0f || f4 > 0.0f || f5 > 0.0f || f6 > 0.0f) {
            int n;
            float f9;
            int n2;
            float f10;
            int n3;
            int n4;
            long l = this._getInsets(this.getRawHandle());
            int n5 = (int)(l >> 48) & 0xFFFF;
            int n6 = (int)(l >> 32) & 0xFFFF;
            int n7 = (int)(l >> 16) & 0xFFFF;
            int n8 = (int)l & 0xFFFF;
            if (bl || bl2) {
                if (bl) {
                    n4 = this.screen.toPlatformX(f);
                } else {
                    n4 = this.x;
                    f = this.screen.fromPlatformX(n4);
                }
                if (bl2) {
                    n3 = this.screen.toPlatformY(f2);
                } else {
                    n3 = this.y;
                    f2 = this.screen.fromPlatformY(n3);
                }
            } else {
                n4 = this.x;
                n3 = this.y;
            }
            if (f3 > 0.0f) {
                f10 = f3 - (float)(n5 + n7) / this.platformScaleX;
                n2 = (int)Math.ceil(f3 * this.platformScaleX);
            } else {
                f10 = f5 > 0.0f ? f5 : this.fxReqWidth;
                n2 = n5 + n7 + (int)Math.ceil(f10 * this.platformScaleX);
            }
            this.fxReqWidth = f10;
            if (f4 > 0.0f) {
                f9 = f4 - (float)(n6 + n8) / this.platformScaleY;
                n = (int)Math.ceil(f4 * this.platformScaleY);
            } else {
                f9 = f6 > 0.0f ? f6 : this.fxReqHeight;
                n = n6 + n8 + (int)Math.ceil(f9 * this.platformScaleY);
            }
            this.fxReqHeight = f9;
            long l2 = this._getAnchor(this.getRawHandle());
            int n9 = l2 == Long.MIN_VALUE ? 2 : 1;
            int n10 = (int)(l2 >> 32);
            int n11 = (int)l2;
            int[] arrn = this.notifyMoving(n4, n3, n2, n, f, f2, n10, n11, n9, n5, n6, n7, n8);
            if (arrn != null) {
                n4 = arrn[0];
                n3 = arrn[1];
                n2 = arrn[2];
                n = arrn[3];
            }
            if (!bl) {
                boolean bl3 = bl = n4 != this.x;
            }
            if (!bl2) {
                bl2 = n3 != this.y;
            }
            this.pfReqWidth = (int)Math.ceil(this.fxReqWidth * this.platformScaleX);
            this.pfReqHeight = (int)Math.ceil(this.fxReqHeight * this.platformScaleY);
            this._setBounds(this.getRawHandle(), n4, n3, bl, bl2, n2, n, 0, 0, f7, f8);
        }
    }

    protected int[] notifyMoving(int n, int n2, int n3, int n4, float f, float f2, int n5, int n6, int n7, int n8, int n9, int n10, int n11) {
        if (this.screen == null || !this.screen.containsPlatformRect(n, n2, n3, n4)) {
            float f3;
            float f4 = f3 = this.screen == null ? 0.0f : this.screen.portionIntersectsPlatformRect(n, n2, n3, n4);
            if (f3 < 0.5f) {
                float f5 = (float)n5 / this.platformScaleX;
                float f6 = (float)n6 / this.platformScaleY;
                Screen screen = this.screen;
                int n12 = n;
                int n13 = n2;
                int n14 = n3;
                int n15 = n4;
                for (Screen screen2 : Screen.getScreens()) {
                    int n16;
                    int n17;
                    int n18;
                    int n19;
                    if (screen2 == this.screen) continue;
                    if (n7 == 0) {
                        n19 = n;
                        n18 = n2;
                        n17 = n3;
                        n16 = n4;
                    } else {
                        int n20 = (int)Math.ceil(this.fxReqWidth * screen2.getPlatformScaleX());
                        int n21 = (int)Math.ceil(this.fxReqHeight * screen2.getPlatformScaleY());
                        n17 = n20 + n8 + n10;
                        n16 = n21 + n9 + n11;
                        if (n7 == 1) {
                            n19 = n + n5 - Math.round(f5 * screen2.getPlatformScaleX());
                            n18 = n2 + n6 - Math.round(f6 * screen2.getPlatformScaleY());
                        } else {
                            n19 = screen2.toPlatformX(f);
                            n18 = screen2.toPlatformY(f2);
                        }
                    }
                    float f7 = screen2.portionIntersectsPlatformRect(n19, n18, n17, n16);
                    if (this.screen != null && (!(f7 > 0.6f) || !(f7 > f3))) continue;
                    f3 = f7;
                    screen = screen2;
                    n12 = n19;
                    n13 = n18;
                    n14 = n17;
                    n15 = n16;
                }
                if (screen != this.screen) {
                    this.notifyMoveToAnotherScreen(screen);
                    this.notifyScaleChanged(screen.getPlatformScaleX(), screen.getPlatformScaleY(), screen.getRecommendedOutputScaleX(), screen.getRecommendedOutputScaleY());
                    if (this.view != null) {
                        this.view.updateLocation();
                    }
                    if (n7 == 0) {
                        return null;
                    }
                    return new int[]{n12, n13, n14, n15};
                }
            }
        }
        return null;
    }

    @Override
    protected void notifyResize(int n, int n2, int n3) {
        float f = this.platformScaleX;
        float f2 = this.platformScaleY;
        long l = this._getInsets(this.getRawHandle());
        int n4 = (int)(l >> 48) & 0xFFFF;
        int n5 = (int)(l >> 32) & 0xFFFF;
        int n6 = (int)(l >> 16) & 0xFFFF;
        int n7 = (int)l & 0xFFFF;
        int n8 = n2 - n4 - n6;
        int n9 = n3 - n5 - n7;
        if (n8 != this.pfReqWidth || f != this.platformScaleX) {
            this.fxReqWidth = (float)n8 / this.platformScaleX;
            this.pfReqWidth = n8;
        }
        if (n9 != this.pfReqHeight || f2 != this.platformScaleY) {
            this.fxReqHeight = (float)n9 / this.platformScaleY;
            this.pfReqHeight = n9;
        }
        super.notifyResize(n, n2, n3);
    }

    protected native boolean _setBackground2(long var1, float var3, float var4, float var5);

    @Override
    protected boolean _setBackground(long l, float f, float f2, float f3) {
        if (this.getAppletMode()) {
            return this._setBackground2(l, f, f2, f3);
        }
        return true;
    }

    private native long _getInsets(long var1);

    private native long _getAnchor(long var1);

    @Override
    protected native long _createWindow(long var1, long var3, int var5);

    @Override
    protected native long _createChildWindow(long var1);

    @Override
    protected native boolean _close(long var1);

    @Override
    protected native boolean _setView(long var1, View var3);

    @Override
    protected native boolean _setMenubar(long var1, long var3);

    @Override
    protected native boolean _minimize(long var1, boolean var3);

    @Override
    protected native boolean _maximize(long var1, boolean var3, boolean var4);

    @Override
    protected native void _setBounds(long var1, int var3, int var4, boolean var5, boolean var6, int var7, int var8, int var9, int var10, float var11, float var12);

    @Override
    protected native boolean _setVisible(long var1, boolean var3);

    @Override
    protected native boolean _setResizable(long var1, boolean var3);

    @Override
    protected native boolean _requestFocus(long var1, int var3);

    @Override
    protected native void _setFocusable(long var1, boolean var3);

    @Override
    protected native boolean _setTitle(long var1, String var3);

    @Override
    protected native void _setLevel(long var1, int var3);

    @Override
    protected native void _setAlpha(long var1, float var3);

    @Override
    protected native void _setEnabled(long var1, boolean var3);

    @Override
    protected native boolean _setMinimumSize(long var1, int var3, int var4);

    @Override
    protected native boolean _setMaximumSize(long var1, int var3, int var4);

    @Override
    protected native void _setIcon(long var1, Pixels var3);

    @Override
    protected native void _toFront(long var1);

    @Override
    protected native void _toBack(long var1);

    @Override
    protected native void _enterModal(long var1);

    @Override
    protected native void _enterModalWithWindow(long var1, long var3);

    @Override
    protected native void _exitModal(long var1);

    @Override
    protected native boolean _grabFocus(long var1);

    @Override
    protected native void _ungrabFocus(long var1);

    @Override
    protected native int _getEmbeddedX(long var1);

    @Override
    protected native int _getEmbeddedY(long var1);

    @Override
    protected native void _setCursor(long var1, Cursor var3);

    @Override
    protected void _requestInput(long l, String string, int n, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _releaseInput(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void setDeferredClosing(boolean bl) {
        this.deferredClosing = bl;
        if (!this.deferredClosing && this.closingRequested) {
            this.close();
        }
    }

    @Override
    public void close() {
        if (!this.deferredClosing) {
            super.close();
        } else {
            this.closingRequested = true;
            this.setVisible(false);
        }
    }

    static {
        WinWindow._initIDs();
    }
}

