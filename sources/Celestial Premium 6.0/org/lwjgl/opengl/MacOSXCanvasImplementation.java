/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTCanvasImplementation;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.MacOSXAWTGLCanvasPeerInfo;
import org.lwjgl.opengl.PeerInfo;
import org.lwjgl.opengl.PixelFormat;

final class MacOSXCanvasImplementation
implements AWTCanvasImplementation {
    MacOSXCanvasImplementation() {
    }

    public PeerInfo createPeerInfo(Canvas component, PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
        try {
            return new MacOSXAWTGLCanvasPeerInfo(component, pixel_format, attribs, true);
        }
        catch (LWJGLException e) {
            return new MacOSXAWTGLCanvasPeerInfo(component, pixel_format, attribs, false);
        }
    }

    public GraphicsConfiguration findConfiguration(GraphicsDevice device, PixelFormat pixel_format) throws LWJGLException {
        return null;
    }
}

