/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.HashMap;
import jdk.nashorn.internal.codegen.CompilerConstants;

public class Namespace {
    private final Namespace parent;
    private final HashMap<String, Integer> directory;

    public Namespace() {
        this(null);
    }

    public Namespace(Namespace parent) {
        this.parent = parent;
        this.directory = new HashMap();
    }

    public Namespace getParent() {
        return this.parent;
    }

    public String uniqueName(String base) {
        String truncatedBase = base.length() > 32768 ? base.substring(0, 32768) : base;
        for (Namespace namespace = this; namespace != null; namespace = namespace.getParent()) {
            HashMap<String, Integer> namespaceDirectory = namespace.directory;
            Integer counter = namespaceDirectory.get(truncatedBase);
            if (counter == null) continue;
            int count = counter + 1;
            namespaceDirectory.put(truncatedBase, count);
            return truncatedBase + CompilerConstants.ID_FUNCTION_SEPARATOR.symbolName() + count;
        }
        this.directory.put(truncatedBase, 0);
        return truncatedBase;
    }

    public String toString() {
        return this.directory.toString();
    }
}

