// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.compiler;

import com.viaversion.viaversion.libs.javassist.compiler.ast.Declarator;
import java.util.HashMap;

public final class SymbolTable extends HashMap<String, Declarator>
{
    private static final long serialVersionUID = 1L;
    private SymbolTable parent;
    
    public SymbolTable() {
        this(null);
    }
    
    public SymbolTable(final SymbolTable p) {
        this.parent = p;
    }
    
    public SymbolTable getParent() {
        return this.parent;
    }
    
    public Declarator lookup(final String name) {
        final Declarator found = ((HashMap<K, Declarator>)this).get(name);
        if (found == null && this.parent != null) {
            return this.parent.lookup(name);
        }
        return found;
    }
    
    public void append(final String name, final Declarator value) {
        this.put(name, value);
    }
}
