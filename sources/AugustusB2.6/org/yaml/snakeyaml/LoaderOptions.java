// 
// Decompiled by Procyon v0.5.36
// 

package org.yaml.snakeyaml;

public class LoaderOptions
{
    private boolean allowDuplicateKeys;
    private boolean wrappedToRootException;
    private int maxAliasesForCollections;
    private boolean allowRecursiveKeys;
    
    public LoaderOptions() {
        this.allowDuplicateKeys = true;
        this.wrappedToRootException = false;
        this.maxAliasesForCollections = 50;
        this.allowRecursiveKeys = false;
    }
    
    public boolean isAllowDuplicateKeys() {
        return this.allowDuplicateKeys;
    }
    
    public void setAllowDuplicateKeys(final boolean allowDuplicateKeys) {
        this.allowDuplicateKeys = allowDuplicateKeys;
    }
    
    public boolean isWrappedToRootException() {
        return this.wrappedToRootException;
    }
    
    public void setWrappedToRootException(final boolean wrappedToRootException) {
        this.wrappedToRootException = wrappedToRootException;
    }
    
    public int getMaxAliasesForCollections() {
        return this.maxAliasesForCollections;
    }
    
    public void setMaxAliasesForCollections(final int maxAliasesForCollections) {
        this.maxAliasesForCollections = maxAliasesForCollections;
    }
    
    public void setAllowRecursiveKeys(final boolean allowRecursiveKeys) {
        this.allowRecursiveKeys = allowRecursiveKeys;
    }
    
    public boolean getAllowRecursiveKeys() {
        return this.allowRecursiveKeys;
    }
}
