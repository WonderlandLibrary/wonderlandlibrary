// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

class CategoryKey
{
    String name;
    int hashCache;
    
    CategoryKey(final String name) {
        this.name = name;
        this.hashCache = name.hashCode();
    }
    
    @Override
    public final int hashCode() {
        return this.hashCache;
    }
    
    @Override
    public final boolean equals(final Object rArg) {
        return this == rArg || (rArg != null && CategoryKey.class == rArg.getClass() && this.name.equals(((CategoryKey)rArg).name));
    }
}
