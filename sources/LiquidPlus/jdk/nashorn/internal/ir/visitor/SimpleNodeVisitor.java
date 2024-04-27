/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public abstract class SimpleNodeVisitor
extends NodeVisitor<LexicalContext> {
    public SimpleNodeVisitor() {
        super(new LexicalContext());
    }
}

