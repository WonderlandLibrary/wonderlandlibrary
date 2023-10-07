// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.builders;

@Deprecated
public class BooleanHolder extends Holder<Boolean>
{
    public BooleanHolder() {
        super(Boolean.FALSE);
    }
    
    @Override
    public void set(final Boolean value) {
        if (value != null) {
            super.set(value);
        }
    }
}
