/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerWrapperAbstract;
import org.lwjgl.opencl.CLObject;
import org.lwjgl.opencl.CLObjectRetainable;

abstract class CLObjectChild<P extends CLObject>
extends CLObjectRetainable {
    private final P parent;

    protected CLObjectChild(long pointer, P parent) {
        super(pointer);
        if (LWJGLUtil.DEBUG && parent != null && !((PointerWrapperAbstract)parent).isValid()) {
            throw new IllegalStateException("The parent specified is not a valid CL object.");
        }
        this.parent = parent;
    }

    public P getParent() {
        return this.parent;
    }
}

