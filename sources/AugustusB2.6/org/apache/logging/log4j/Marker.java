// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.io.Serializable;

public interface Marker extends Serializable
{
    Marker addParents(final Marker... markers);
    
    boolean equals(final Object obj);
    
    String getName();
    
    Marker[] getParents();
    
    int hashCode();
    
    boolean hasParents();
    
    boolean isInstanceOf(final Marker m);
    
    boolean isInstanceOf(final String name);
    
    boolean remove(final Marker marker);
    
    Marker setParents(final Marker... markers);
}
