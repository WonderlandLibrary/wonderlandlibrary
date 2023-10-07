/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.spi;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;

public interface RendererSupport {
    public RendererMap getRendererMap();

    public void setRenderer(Class var1, ObjectRenderer var2);
}

