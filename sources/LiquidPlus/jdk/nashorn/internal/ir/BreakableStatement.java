/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextStatement;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
abstract class BreakableStatement
extends LexicalContextStatement
implements BreakableNode {
    private static final long serialVersionUID = 1L;
    protected final Label breakLabel;
    final LocalVariableConversion conversion;

    protected BreakableStatement(int lineNumber, long token, int finish, Label breakLabel) {
        super(lineNumber, token, finish);
        this.breakLabel = breakLabel;
        this.conversion = null;
    }

    protected BreakableStatement(BreakableStatement breakableNode, LocalVariableConversion conversion) {
        super(breakableNode);
        this.breakLabel = new Label(breakableNode.getBreakLabel());
        this.conversion = conversion;
    }

    @Override
    public boolean isBreakableWithoutLabel() {
        return true;
    }

    @Override
    public Label getBreakLabel() {
        return this.breakLabel;
    }

    @Override
    public List<Label> getLabels() {
        return Collections.unmodifiableList(Collections.singletonList(this.breakLabel));
    }

    @Override
    public JoinPredecessor setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return this.setLocalVariableConversionChanged(lc, conversion);
    }

    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }

    abstract JoinPredecessor setLocalVariableConversionChanged(LexicalContext var1, LocalVariableConversion var2);
}

