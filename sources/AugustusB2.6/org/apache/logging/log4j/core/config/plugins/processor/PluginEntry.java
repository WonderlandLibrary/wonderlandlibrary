// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.Serializable;

public class PluginEntry implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String key;
    private String className;
    private String name;
    private boolean printable;
    private boolean defer;
    private transient String category;
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public void setClassName(final String className) {
        this.className = className;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isPrintable() {
        return this.printable;
    }
    
    public void setPrintable(final boolean printable) {
        this.printable = printable;
    }
    
    public boolean isDefer() {
        return this.defer;
    }
    
    public void setDefer(final boolean defer) {
        this.defer = defer;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return "PluginEntry [key=" + this.key + ", className=" + this.className + ", name=" + this.name + ", printable=" + this.printable + ", defer=" + this.defer + ", category=" + this.category + "]";
    }
}
