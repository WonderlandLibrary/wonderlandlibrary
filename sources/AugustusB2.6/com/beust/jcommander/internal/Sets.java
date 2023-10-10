// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.internal;

import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;

public class Sets
{
    public static <K> Set<K> newHashSet() {
        return new HashSet<K>();
    }
    
    public static <K> Set<K> newLinkedHashSet() {
        return new LinkedHashSet<K>();
    }
}
