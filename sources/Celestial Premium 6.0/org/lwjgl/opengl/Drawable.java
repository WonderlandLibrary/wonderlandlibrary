/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;

public interface Drawable {
    public boolean isCurrent() throws LWJGLException;

    public void makeCurrent() throws LWJGLException;

    public void releaseContext() throws LWJGLException;

    public void destroy();

    public void setCLSharingProperties(PointerBuffer var1) throws LWJGLException;
}

