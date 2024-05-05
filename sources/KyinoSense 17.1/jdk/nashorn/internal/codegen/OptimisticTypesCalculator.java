/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

final class OptimisticTypesCalculator
extends SimpleNodeVisitor {
    final Compiler compiler;
    final Deque<BitSet> neverOptimistic = new ArrayDeque<BitSet>();

    OptimisticTypesCalculator(Compiler compiler) {
        this.compiler = compiler;
    }

    @Override
    public boolean enterAccessNode(AccessNode accessNode) {
        this.tagNeverOptimistic(accessNode.getBase());
        return true;
    }

    @Override
    public boolean enterPropertyNode(PropertyNode propertyNode) {
        if (propertyNode.getKeyName().equals("__proto__")) {
            this.tagNeverOptimistic(propertyNode.getValue());
        }
        return super.enterPropertyNode(propertyNode);
    }

    @Override
    public boolean enterBinaryNode(BinaryNode binaryNode) {
        if (binaryNode.isAssignment()) {
            Symbol symbol;
            Expression lhs = binaryNode.lhs();
            if (!binaryNode.isSelfModifying()) {
                this.tagNeverOptimistic(lhs);
            }
            if (lhs instanceof IdentNode && (symbol = ((IdentNode)lhs).getSymbol()).isInternal() && !binaryNode.rhs().isSelfModifying()) {
                this.tagNeverOptimistic(binaryNode.rhs());
            }
        } else if (binaryNode.isTokenType(TokenType.INSTANCEOF)) {
            this.tagNeverOptimistic(binaryNode.lhs());
            this.tagNeverOptimistic(binaryNode.rhs());
        }
        return true;
    }

    @Override
    public boolean enterCallNode(CallNode callNode) {
        this.tagNeverOptimistic(callNode.getFunction());
        return true;
    }

    @Override
    public boolean enterCatchNode(CatchNode catchNode) {
        this.tagNeverOptimistic(catchNode.getExceptionCondition());
        return true;
    }

    @Override
    public boolean enterExpressionStatement(ExpressionStatement expressionStatement) {
        Expression expr = expressionStatement.getExpression();
        if (!expr.isSelfModifying()) {
            this.tagNeverOptimistic(expr);
        }
        return true;
    }

    @Override
    public boolean enterForNode(ForNode forNode) {
        if (forNode.isForIn()) {
            this.tagNeverOptimistic(forNode.getModify());
        } else {
            this.tagNeverOptimisticLoopTest(forNode);
        }
        return true;
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        if (!this.neverOptimistic.isEmpty() && this.compiler.isOnDemandCompilation()) {
            return false;
        }
        this.neverOptimistic.push(new BitSet());
        return true;
    }

    @Override
    public boolean enterIfNode(IfNode ifNode) {
        this.tagNeverOptimistic(ifNode.getTest());
        return true;
    }

    @Override
    public boolean enterIndexNode(IndexNode indexNode) {
        this.tagNeverOptimistic(indexNode.getBase());
        return true;
    }

    @Override
    public boolean enterTernaryNode(TernaryNode ternaryNode) {
        this.tagNeverOptimistic(ternaryNode.getTest());
        return true;
    }

    @Override
    public boolean enterUnaryNode(UnaryNode unaryNode) {
        if (unaryNode.isTokenType(TokenType.NOT) || unaryNode.isTokenType(TokenType.NEW)) {
            this.tagNeverOptimistic(unaryNode.getExpression());
        }
        return true;
    }

    @Override
    public boolean enterVarNode(VarNode varNode) {
        this.tagNeverOptimistic(varNode.getName());
        return true;
    }

    @Override
    public boolean enterWhileNode(WhileNode whileNode) {
        this.tagNeverOptimisticLoopTest(whileNode);
        return true;
    }

    @Override
    protected Node leaveDefault(Node node) {
        if (node instanceof Optimistic) {
            return this.leaveOptimistic((Optimistic)((Object)node));
        }
        return node;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        this.neverOptimistic.pop();
        return functionNode;
    }

    @Override
    public Node leaveIdentNode(IdentNode identNode) {
        Symbol symbol = identNode.getSymbol();
        if (symbol == null) {
            assert (identNode.isPropertyName());
            return identNode;
        }
        if (symbol.isBytecodeLocal()) {
            return identNode;
        }
        if (symbol.isParam() && this.lc.getCurrentFunction().isVarArg()) {
            return identNode.setType(identNode.getMostPessimisticType());
        }
        assert (symbol.isScope());
        return this.leaveOptimistic(identNode);
    }

    private Expression leaveOptimistic(Optimistic opt) {
        int pp = opt.getProgramPoint();
        if (UnwarrantedOptimismException.isValid(pp) && !this.neverOptimistic.peek().get(pp)) {
            return (Expression)((Object)opt.setType(this.compiler.getOptimisticType(opt)));
        }
        return (Expression)((Object)opt);
    }

    private void tagNeverOptimistic(Expression expr) {
        int pp;
        if (expr instanceof Optimistic && UnwarrantedOptimismException.isValid(pp = ((Optimistic)((Object)expr)).getProgramPoint())) {
            this.neverOptimistic.peek().set(pp);
        }
    }

    private void tagNeverOptimisticLoopTest(LoopNode loopNode) {
        JoinPredecessorExpression test = loopNode.getTest();
        if (test != null) {
            this.tagNeverOptimistic(test.getExpression());
        }
    }
}

