/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.spi;

import org.apache.log4j.spi.ThrowableRenderer;

public interface ThrowableRendererSupport {
    public ThrowableRenderer getThrowableRenderer();

    public void setThrowableRenderer(ThrowableRenderer var1);
}

