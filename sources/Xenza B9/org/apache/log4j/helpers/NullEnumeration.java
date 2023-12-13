// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.util.NoSuchElementException;
import java.util.Enumeration;

public final class NullEnumeration implements Enumeration
{
    private static final NullEnumeration INSTANCE;
    
    private NullEnumeration() {
    }
    
    public static NullEnumeration getInstance() {
        return NullEnumeration.INSTANCE;
    }
    
    @Override
    public boolean hasMoreElements() {
        return false;
    }
    
    @Override
    public Object nextElement() {
        throw new NoSuchElementException();
    }
    
    static {
        INSTANCE = new NullEnumeration();
    }
}
