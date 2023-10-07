/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Terminal;

public abstract class Statement
extends Node
implements Terminal {
    private static final long serialVersionUID = 1L;
    private final int lineNumber;

    public Statement(int lineNumber, long token, int finish) {
        super(token, finish);
        this.lineNumber = lineNumber;
    }

    protected Statement(int lineNumber, long token, int start, int finish) {
        super(token, start, finish);
        this.lineNumber = lineNumber;
    }

    protected Statement(Statement node) {
        super(node);
        this.lineNumber = node.lineNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    public boolean hasGoto() {
        return false;
    }

    public final boolean hasTerminalFlags() {
        return this.isTerminal() || this.hasGoto();
    }
}

