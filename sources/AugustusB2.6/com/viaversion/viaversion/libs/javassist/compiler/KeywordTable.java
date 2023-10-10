// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.compiler;

import java.util.HashMap;

public final class KeywordTable extends HashMap<String, Integer>
{
    private static final long serialVersionUID = 1L;
    
    public int lookup(final String name) {
        return this.containsKey(name) ? ((HashMap<K, Integer>)this).get(name) : -1;
    }
    
    public void append(final String name, final int t) {
        this.put(name, t);
    }
}
