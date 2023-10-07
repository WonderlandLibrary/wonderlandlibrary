// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.or;

import org.apache.logging.log4j.core.util.Loader;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.log4j.spi.RendererSupport;
import java.util.Hashtable;

public class RendererMap
{
    Hashtable map;
    static ObjectRenderer defaultRenderer;
    
    public RendererMap() {
        this.map = new Hashtable();
    }
    
    public static void addRenderer(final RendererSupport repository, final String renderedClassName, final String renderingClassName) {
        StatusLogger.getLogger().debug("Rendering class: [" + renderingClassName + "], Rendered class: [" + renderedClassName + "].");
        final ObjectRenderer renderer = (ObjectRenderer)OptionConverter.instantiateByClassName(renderingClassName, ObjectRenderer.class, null);
        if (renderer == null) {
            StatusLogger.getLogger().error("Could not instantiate renderer [" + renderingClassName + "].");
            return;
        }
        try {
            final Class renderedClass = Loader.loadClass(renderedClassName);
            repository.setRenderer(renderedClass, renderer);
        }
        catch (final ClassNotFoundException e) {
            StatusLogger.getLogger().error("Could not find class [" + renderedClassName + "].", e);
        }
    }
    
    public String findAndRender(final Object o) {
        if (o == null) {
            return null;
        }
        return this.get(o.getClass()).doRender(o);
    }
    
    public ObjectRenderer get(final Object o) {
        if (o == null) {
            return null;
        }
        return this.get(o.getClass());
    }
    
    public ObjectRenderer get(final Class clazz) {
        ObjectRenderer r = null;
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            r = this.map.get(c);
            if (r != null) {
                return r;
            }
            r = this.searchInterfaces(c);
            if (r != null) {
                return r;
            }
        }
        return RendererMap.defaultRenderer;
    }
    
    ObjectRenderer searchInterfaces(final Class c) {
        ObjectRenderer r = this.map.get(c);
        if (r != null) {
            return r;
        }
        final Class[] ia = c.getInterfaces();
        for (int i = 0; i < ia.length; ++i) {
            r = this.searchInterfaces(ia[i]);
            if (r != null) {
                return r;
            }
        }
        return null;
    }
    
    public ObjectRenderer getDefaultRenderer() {
        return RendererMap.defaultRenderer;
    }
    
    public void clear() {
        this.map.clear();
    }
    
    public void put(final Class clazz, final ObjectRenderer or) {
        this.map.put(clazz, or);
    }
    
    static {
        RendererMap.defaultRenderer = new DefaultRenderer();
    }
}
