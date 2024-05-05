/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.Condition;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.codegen.MethodEmitter;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.parser.TokenType;

final class BranchOptimizer {
    private final CodeGenerator codegen;
    private final MethodEmitter method;

    BranchOptimizer(CodeGenerator codegen, MethodEmitter method) {
        this.codegen = codegen;
        this.method = method;
    }

    void execute(Expression node, Label label, boolean state) {
        this.branchOptimizer(node, label, state);
    }

    private void branchOptimizer(UnaryNode unaryNode, Label label, boolean state) {
        if (unaryNode.isTokenType(TokenType.NOT)) {
            this.branchOptimizer(unaryNode.getExpression(), label, !state);
        } else {
            this.loadTestAndJump(unaryNode, label, state);
        }
    }

    private void branchOptimizer(BinaryNode binaryNode, Label label, boolean state) {
        Expression lhs = binaryNode.lhs();
        Expression rhs = binaryNode.rhs();
        switch (binaryNode.tokenType()) {
            case AND: {
                if (state) {
                    Label skip = new Label("skip");
                    this.optimizeLogicalOperand(lhs, skip, false, false);
                    this.optimizeLogicalOperand(rhs, label, true, true);
                    this.method.label(skip);
                } else {
                    this.optimizeLogicalOperand(lhs, label, false, false);
                    this.optimizeLogicalOperand(rhs, label, false, true);
                }
                return;
            }
            case OR: {
                if (state) {
                    this.optimizeLogicalOperand(lhs, label, true, false);
                    this.optimizeLogicalOperand(rhs, label, true, true);
                } else {
                    Label skip = new Label("skip");
                    this.optimizeLogicalOperand(lhs, skip, true, false);
                    this.optimizeLogicalOperand(rhs, label, false, true);
                    this.method.label(skip);
                }
                return;
            }
            case EQ: 
            case EQ_STRICT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.EQ : Condition.NE, true, label);
                return;
            }
            case NE: 
            case NE_STRICT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.NE : Condition.EQ, true, label);
                return;
            }
            case GE: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.GE : Condition.LT, false, label);
                return;
            }
            case GT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.GT : Condition.LE, false, label);
                return;
            }
            case LE: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.LE : Condition.GT, true, label);
                return;
            }
            case LT: {
                this.codegen.loadComparisonOperands(binaryNode);
                this.method.conditionalJump(state ? Condition.LT : Condition.GE, true, label);
                return;
            }
        }
        this.loadTestAndJump(binaryNode, label, state);
    }

    private void optimizeLogicalOperand(Expression expr, Label label, boolean state, boolean isRhs) {
        JoinPredecessorExpression jpexpr = (JoinPredecessorExpression)expr;
        if (LocalVariableConversion.hasLiveConversion(jpexpr)) {
            Label after = new Label("after");
            this.branchOptimizer(jpexpr.getExpression(), after, !state);
            this.method.beforeJoinPoint(jpexpr);
            this.method._goto(label);
            this.method.label(after);
            if (isRhs) {
                this.method.beforeJoinPoint(jpexpr);
            }
        } else {
            this.branchOptimizer(jpexpr.getExpression(), label, state);
        }
    }

    private void branchOptimizer(Expression node, Label label, boolean state) {
        if (node instanceof BinaryNode) {
            this.branchOptimizer((BinaryNode)node, label, state);
            return;
        }
        if (node instanceof UnaryNode) {
            this.branchOptimizer((UnaryNode)node, label, state);
            return;
        }
        this.loadTestAndJump(node, label, state);
    }

    private void loadTestAndJump(Expression node, Label label, boolean state) {
        this.codegen.loadExpressionAsBoolean(node);
        if (state) {
            this.method.ifne(label);
        } else {
            this.method.ifeq(label);
        }
    }
}

