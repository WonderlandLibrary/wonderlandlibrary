/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.BreakableStatement;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class SwitchNode
extends BreakableStatement {
    private static final long serialVersionUID = 1L;
    private final Expression expression;
    private final List<CaseNode> cases;
    private final int defaultCaseIndex;
    private final boolean uniqueInteger;
    private final Symbol tag;

    public SwitchNode(int lineNumber, long token, int finish, Expression expression, List<CaseNode> cases, CaseNode defaultCase) {
        super(lineNumber, token, finish, new Label("switch_break"));
        this.expression = expression;
        this.cases = cases;
        this.defaultCaseIndex = defaultCase == null ? -1 : cases.indexOf(defaultCase);
        this.uniqueInteger = false;
        this.tag = null;
    }

    private SwitchNode(SwitchNode switchNode, Expression expression, List<CaseNode> cases, int defaultCaseIndex, LocalVariableConversion conversion, boolean uniqueInteger, Symbol tag) {
        super(switchNode, conversion);
        this.expression = expression;
        this.cases = cases;
        this.defaultCaseIndex = defaultCaseIndex;
        this.tag = tag;
        this.uniqueInteger = uniqueInteger;
    }

    @Override
    public Node ensureUniqueLabels(LexicalContext lc) {
        ArrayList<CaseNode> newCases = new ArrayList<CaseNode>();
        for (CaseNode caseNode : this.cases) {
            newCases.add(new CaseNode(caseNode, caseNode.getTest(), caseNode.getBody(), caseNode.getLocalVariableConversion()));
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, newCases, this.defaultCaseIndex, this.conversion, this.uniqueInteger, this.tag));
    }

    @Override
    public boolean isTerminal() {
        if (!this.cases.isEmpty() && this.defaultCaseIndex != -1) {
            for (CaseNode caseNode : this.cases) {
                if (caseNode.isTerminal()) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterSwitchNode(this)) {
            return visitor.leaveSwitchNode(this.setExpression(lc, (Expression)this.expression.accept(visitor)).setCases(lc, Node.accept(visitor, this.cases), this.defaultCaseIndex));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        sb.append("switch (");
        this.expression.toString(sb, printType);
        sb.append(')');
    }

    public CaseNode getDefaultCase() {
        return this.defaultCaseIndex == -1 ? null : this.cases.get(this.defaultCaseIndex);
    }

    public List<CaseNode> getCases() {
        return Collections.unmodifiableList(this.cases);
    }

    public SwitchNode setCases(LexicalContext lc, List<CaseNode> cases) {
        return this.setCases(lc, cases, this.defaultCaseIndex);
    }

    private SwitchNode setCases(LexicalContext lc, List<CaseNode> cases, int defaultCaseIndex) {
        if (this.cases == cases) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, cases, defaultCaseIndex, this.conversion, this.uniqueInteger, this.tag));
    }

    public SwitchNode setCases(LexicalContext lc, List<CaseNode> cases, CaseNode defaultCase) {
        return this.setCases(lc, cases, defaultCase == null ? -1 : cases.indexOf(defaultCase));
    }

    public Expression getExpression() {
        return this.expression;
    }

    public SwitchNode setExpression(LexicalContext lc, Expression expression) {
        if (this.expression == expression) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, expression, this.cases, this.defaultCaseIndex, this.conversion, this.uniqueInteger, this.tag));
    }

    public Symbol getTag() {
        return this.tag;
    }

    public SwitchNode setTag(LexicalContext lc, Symbol tag) {
        if (this.tag == tag) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, this.cases, this.defaultCaseIndex, this.conversion, this.uniqueInteger, tag));
    }

    public boolean isUniqueInteger() {
        return this.uniqueInteger;
    }

    public SwitchNode setUniqueInteger(LexicalContext lc, boolean uniqueInteger) {
        if (this.uniqueInteger == uniqueInteger) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, this.cases, this.defaultCaseIndex, this.conversion, uniqueInteger, this.tag));
    }

    @Override
    JoinPredecessor setLocalVariableConversionChanged(LexicalContext lc, LocalVariableConversion conversion) {
        return Node.replaceInLexicalContext(lc, this, new SwitchNode(this, this.expression, this.cases, this.defaultCaseIndex, conversion, this.uniqueInteger, this.tag));
    }
}

