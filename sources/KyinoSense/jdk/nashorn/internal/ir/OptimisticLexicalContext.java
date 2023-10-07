/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;

public class OptimisticLexicalContext
extends LexicalContext {
    private final boolean isEnabled;
    private final Deque<List<Assumption>> optimisticAssumptions = new ArrayDeque<List<Assumption>>();

    public OptimisticLexicalContext(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void logOptimisticAssumption(Symbol symbol, Type type) {
        if (this.isEnabled) {
            List<Assumption> peek = this.optimisticAssumptions.peek();
            peek.add(new Assumption(symbol, type));
        }
    }

    public List<Assumption> getOptimisticAssumptions() {
        return Collections.unmodifiableList(this.optimisticAssumptions.peek());
    }

    public boolean hasOptimisticAssumptions() {
        return !this.optimisticAssumptions.isEmpty() && !this.getOptimisticAssumptions().isEmpty();
    }

    @Override
    public <T extends LexicalContextNode> T push(T node) {
        if (this.isEnabled && node instanceof FunctionNode) {
            this.optimisticAssumptions.push(new ArrayList());
        }
        return super.push(node);
    }

    @Override
    public <T extends Node> T pop(T node) {
        T popped = super.pop(node);
        if (this.isEnabled && node instanceof FunctionNode) {
            this.optimisticAssumptions.pop();
        }
        return popped;
    }

    class Assumption {
        Symbol symbol;
        Type type;

        Assumption(Symbol symbol, Type type) {
            this.symbol = symbol;
            this.type = type;
        }

        public String toString() {
            return this.symbol.getName() + "=" + this.type;
        }
    }
}

