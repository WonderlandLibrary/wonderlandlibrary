// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Iterator;

public interface BidirectionalIterator<K> extends Iterator<K>
{
    K previous();
    
    boolean hasPrevious();
}
