// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;

public interface RendererSupport
{
    RendererMap getRendererMap();
    
    void setRenderer(final Class renderedClass, final ObjectRenderer renderer);
}
