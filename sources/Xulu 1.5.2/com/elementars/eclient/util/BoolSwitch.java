// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

public class BoolSwitch
{
    boolean value;
    
    public BoolSwitch(final boolean value) {
        this.value = value;
    }
    
    public boolean isValue() {
        return this.value;
    }
    
    public void setValue(final boolean value) {
        this.value = value;
    }
    
    public void toggle() {
        this.value = !this.value;
    }
}
