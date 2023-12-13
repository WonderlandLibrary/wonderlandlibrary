/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTSurfaceLock;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.MacOSXPeerInfo;
import org.lwjgl.opengl.PixelFormat;

abstract class MacOSXCanvasPeerInfo
extends MacOSXPeerInfo {
    private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
    public ByteBuffer window_handle;

    protected MacOSXCanvasPeerInfo(PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
        super(pixel_format, attribs, true, true, support_pbuffer, true);
    }

    protected void initHandle(Canvas component) throws LWJGLException {
        boolean forceCALayer = true;
        boolean autoResizable = true;
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.startsWith("1.5") || javaVersion.startsWith("1.6")) {
            forceCALayer = false;
        } else if (javaVersion.startsWith("1.7")) {
            autoResizable = false;
        }
        Insets insets = MacOSXCanvasPeerInfo.getInsets(component);
        int top = insets != null ? insets.top : 0;
        int left = insets != null ? insets.left : 0;
        this.window_handle = MacOSXCanvasPeerInfo.nInitHandle(this.awt_surface.lockAndGetHandle(component), this.getHandle(), this.window_handle, forceCALayer, autoResizable, component.getX() - left, component.getY() - top);
        if (javaVersion.startsWith("1.7")) {
            this.addComponentListener(component);
            MacOSXCanvasPeerInfo.reSetLayerBounds(component, this.getHandle());
        }
    }

    private void addComponentListener(final Canvas component) {
        ComponentListener[] components = component.getComponentListeners();
        for (int i2 = 0; i2 < components.length; ++i2) {
            ComponentListener c2 = components[i2];
            if (c2.toString() != "CanvasPeerInfoListener") continue;
            return;
        }
        ComponentListener comp = new ComponentListener(){

            public void componentHidden(ComponentEvent e2) {
            }

            public void componentMoved(ComponentEvent e2) {
                MacOSXCanvasPeerInfo.reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
            }

            public void componentResized(ComponentEvent e2) {
                MacOSXCanvasPeerInfo.reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
            }

            public void componentShown(ComponentEvent e2) {
            }

            public String toString() {
                return "CanvasPeerInfoListener";
            }
        };
        component.addComponentListener(comp);
    }

    private static native ByteBuffer nInitHandle(ByteBuffer var0, ByteBuffer var1, ByteBuffer var2, boolean var3, boolean var4, int var5, int var6) throws LWJGLException;

    private static native void nSetLayerPosition(ByteBuffer var0, int var1, int var2);

    private static native void nSetLayerBounds(ByteBuffer var0, int var1, int var2, int var3, int var4);

    private static void reSetLayerBounds(Canvas component, ByteBuffer peer_info_handle) {
        Component peer = SwingUtilities.getRoot(component);
        Point rtLoc = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), peer);
        int x2 = (int)rtLoc.getX();
        int y2 = (int)rtLoc.getY();
        Insets insets = MacOSXCanvasPeerInfo.getInsets(component);
        int n2 = insets != null ? insets.left : 0;
        y2 -= insets != null ? insets.top : 0;
        y2 = peer.getHeight() - y2 - component.getHeight();
        MacOSXCanvasPeerInfo.nSetLayerBounds(peer_info_handle, x2 -= n2, y2, component.getWidth(), component.getHeight());
    }

    protected void doUnlock() throws LWJGLException {
        this.awt_surface.unlock();
    }

    private static Insets getInsets(Canvas component) {
        JRootPane c2 = SwingUtilities.getRootPane(component);
        if (c2 != null) {
            return ((Container)c2).getInsets();
        }
        return new Insets(0, 0, 0, 0);
    }
}

