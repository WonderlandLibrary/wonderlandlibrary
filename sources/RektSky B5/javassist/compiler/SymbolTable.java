/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler;

import java.util.HashMap;
import javassist.compiler.ast.Declarator;

public final class SymbolTable
extends HashMap<String, Declarator> {
    private static final long serialVersionUID = 1L;
    private SymbolTable parent;

    public SymbolTable() {
        this((SymbolTable)null);
    }

    public SymbolTable(SymbolTable p2) {
        this.parent = p2;
    }

    public SymbolTable getParent() {
        return this.parent;
    }

    public Declarator lookup(String name) {
        Declarator found = (Declarator)this.get(name);
        if (found == null && this.parent != null) {
            return this.parent.lookup(name);
        }
        return found;
    }

    public void append(String name, Declarator value) {
        this.put(name, value);
    }
}

