// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.util;

import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;

public class PluginType<T>
{
    private final PluginEntry pluginEntry;
    private final Class<T> pluginClass;
    private final String elementName;
    
    public PluginType(final PluginEntry pluginEntry, final Class<T> pluginClass, final String elementName) {
        this.pluginEntry = pluginEntry;
        this.pluginClass = pluginClass;
        this.elementName = elementName;
    }
    
    public Class<T> getPluginClass() {
        return this.pluginClass;
    }
    
    public String getElementName() {
        return this.elementName;
    }
    
    public String getKey() {
        return this.pluginEntry.getKey();
    }
    
    public boolean isObjectPrintable() {
        return this.pluginEntry.isPrintable();
    }
    
    public boolean isDeferChildren() {
        return this.pluginEntry.isDefer();
    }
    
    public String getCategory() {
        return this.pluginEntry.getCategory();
    }
    
    @Override
    public String toString() {
        return "PluginType [pluginClass=" + this.pluginClass + ", key=" + this.pluginEntry.getKey() + ", elementName=" + this.pluginEntry.getName() + ", isObjectPrintable=" + this.pluginEntry.isPrintable() + ", isDeferChildren==" + this.pluginEntry.isDefer() + ", category=" + this.pluginEntry.getCategory() + "]";
    }
}
