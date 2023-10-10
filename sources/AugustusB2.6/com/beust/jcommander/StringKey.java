// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

public class StringKey implements FuzzyMap.IKey
{
    private String name;
    
    public StringKey(final String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return 31 + ((this.name == null) ? 0 : this.name.hashCode());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final StringKey stringKey = (StringKey)o;
        if (this.name == null) {
            if (stringKey.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(stringKey.name)) {
            return false;
        }
        return true;
    }
}
