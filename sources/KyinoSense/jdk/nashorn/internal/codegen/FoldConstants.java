/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name="fold")
final class FoldConstants
extends SimpleNodeVisitor
implements Loggable {
    private final DebugLogger log;

    FoldConstants(Compiler compiler) {
        this.log = this.initLogger(compiler.getContext());
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    @Override
    public DebugLogger initLogger(Context context) {
        return context.getLogger(this.getClass());
    }

    @Override
    public Node leaveUnaryNode(UnaryNode unaryNode) {
        LiteralNode<?> literalNode = new UnaryNodeConstantEvaluator(unaryNode).eval();
        if (literalNode != null) {
            this.log.info("Unary constant folded ", unaryNode, " to ", literalNode);
            return literalNode;
        }
        return unaryNode;
    }

    @Override
    public Node leaveBinaryNode(BinaryNode binaryNode) {
        LiteralNode<?> literalNode = new BinaryNodeConstantEvaluator(binaryNode).eval();
        if (literalNode != null) {
            this.log.info("Binary constant folded ", binaryNode, " to ", literalNode);
            return literalNode;
        }
        return binaryNode;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        return functionNode;
    }

    @Override
    public Node leaveIfNode(IfNode ifNode) {
        Expression test = ifNode.getTest();
        if (test instanceof LiteralNode.PrimitiveLiteralNode) {
            boolean isTrue = ((LiteralNode.PrimitiveLiteralNode)test).isTrue();
            Block executed = isTrue ? ifNode.getPass() : ifNode.getFail();
            Block dropped = isTrue ? ifNode.getFail() : ifNode.getPass();
            ArrayList<Statement> statements = new ArrayList<Statement>();
            if (executed != null) {
                statements.addAll(executed.getStatements());
            }
            if (dropped != null) {
                FoldConstants.extractVarNodesFromDeadCode(dropped, statements);
            }
            if (statements.isEmpty()) {
                return new EmptyNode(ifNode);
            }
            return BlockStatement.createReplacement(ifNode, ifNode.getFinish(), statements);
        }
        return ifNode;
    }

    @Override
    public Node leaveTernaryNode(TernaryNode ternaryNode) {
        Expression test = ternaryNode.getTest();
        if (test instanceof LiteralNode.PrimitiveLiteralNode) {
            return (((LiteralNode.PrimitiveLiteralNode)test).isTrue() ? ternaryNode.getTrueExpression() : ternaryNode.getFalseExpression()).getExpression();
        }
        return ternaryNode;
    }

    @Override
    public Node leaveSwitchNode(SwitchNode switchNode) {
        return switchNode.setUniqueInteger(this.lc, FoldConstants.isUniqueIntegerSwitchNode(switchNode));
    }

    private static boolean isUniqueIntegerSwitchNode(SwitchNode switchNode) {
        HashSet<Integer> alreadySeen = new HashSet<Integer>();
        for (CaseNode caseNode : switchNode.getCases()) {
            Expression test = caseNode.getTest();
            if (test == null || FoldConstants.isUniqueIntegerLiteral(test, alreadySeen)) continue;
            return false;
        }
        return true;
    }

    private static boolean isUniqueIntegerLiteral(Expression expr, Set<Integer> alreadySeen) {
        Object value;
        if (expr instanceof LiteralNode && (value = ((LiteralNode)expr).getValue()) instanceof Integer) {
            return alreadySeen.add((Integer)value);
        }
        return false;
    }

    static void extractVarNodesFromDeadCode(Node deadCodeRoot, final List<Statement> statements) {
        deadCodeRoot.accept(new SimpleNodeVisitor(){

            @Override
            public boolean enterVarNode(VarNode varNode) {
                statements.add(varNode.setInit(null));
                return false;
            }

            @Override
            public boolean enterFunctionNode(FunctionNode functionNode) {
                return false;
            }
        });
    }

    private static class BinaryNodeConstantEvaluator
    extends ConstantEvaluator<BinaryNode> {
        BinaryNodeConstantEvaluator(BinaryNode parent) {
            super(parent);
        }

        @Override
        protected LiteralNode<?> eval() {
            LiteralNode<?> result = this.reduceTwoLiterals();
            if (result != null) {
                return result;
            }
            result = this.reduceOneLiteral();
            if (result != null) {
                return result;
            }
            return null;
        }

        private LiteralNode<?> reduceOneLiteral() {
            return null;
        }

        private LiteralNode<?> reduceTwoLiterals() {
            double value;
            if (!(((BinaryNode)this.parent).lhs() instanceof LiteralNode) || !(((BinaryNode)this.parent).rhs() instanceof LiteralNode)) {
                return null;
            }
            LiteralNode lhs = (LiteralNode)((BinaryNode)this.parent).lhs();
            LiteralNode rhs = (LiteralNode)((BinaryNode)this.parent).rhs();
            if (lhs instanceof LiteralNode.ArrayLiteralNode || rhs instanceof LiteralNode.ArrayLiteralNode) {
                return null;
            }
            Type widest = Type.widest(lhs.getType(), rhs.getType());
            boolean isInteger = widest.isInteger();
            switch (((BinaryNode)this.parent).tokenType()) {
                case DIV: {
                    value = lhs.getNumber() / rhs.getNumber();
                    break;
                }
                case ADD: {
                    if ((lhs.isString() || rhs.isNumeric()) && (rhs.isString() || rhs.isNumeric())) {
                        Object res = ScriptRuntime.ADD(lhs.getObject(), rhs.getObject());
                        if (res instanceof Number) {
                            value = ((Number)res).doubleValue();
                            break;
                        }
                        assert (res instanceof CharSequence) : res + " was not a CharSequence, it was a " + res.getClass();
                        return LiteralNode.newInstance(this.token, this.finish, res.toString());
                    }
                    return null;
                }
                case MUL: {
                    value = lhs.getNumber() * rhs.getNumber();
                    break;
                }
                case MOD: {
                    value = lhs.getNumber() % rhs.getNumber();
                    break;
                }
                case SUB: {
                    value = lhs.getNumber() - rhs.getNumber();
                    break;
                }
                case SHR: {
                    long result = JSType.toUint32(lhs.getInt32() >>> rhs.getInt32());
                    return LiteralNode.newInstance(this.token, this.finish, JSType.toNarrowestNumber(result));
                }
                case SAR: {
                    return LiteralNode.newInstance(this.token, this.finish, lhs.getInt32() >> rhs.getInt32());
                }
                case SHL: {
                    return LiteralNode.newInstance(this.token, this.finish, lhs.getInt32() << rhs.getInt32());
                }
                case BIT_XOR: {
                    return LiteralNode.newInstance(this.token, this.finish, lhs.getInt32() ^ rhs.getInt32());
                }
                case BIT_AND: {
                    return LiteralNode.newInstance(this.token, this.finish, lhs.getInt32() & rhs.getInt32());
                }
                case BIT_OR: {
                    return LiteralNode.newInstance(this.token, this.finish, lhs.getInt32() | rhs.getInt32());
                }
                case GE: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.GE(lhs.getObject(), rhs.getObject()));
                }
                case LE: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.LE(lhs.getObject(), rhs.getObject()));
                }
                case GT: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.GT(lhs.getObject(), rhs.getObject()));
                }
                case LT: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.LT(lhs.getObject(), rhs.getObject()));
                }
                case NE: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.NE(lhs.getObject(), rhs.getObject()));
                }
                case NE_STRICT: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.NE_STRICT(lhs.getObject(), rhs.getObject()));
                }
                case EQ: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.EQ(lhs.getObject(), rhs.getObject()));
                }
                case EQ_STRICT: {
                    return LiteralNode.newInstance(this.token, this.finish, ScriptRuntime.EQ_STRICT(lhs.getObject(), rhs.getObject()));
                }
                default: {
                    return null;
                }
            }
            if (isInteger &= JSType.isStrictlyRepresentableAsInt(value)) {
                return LiteralNode.newInstance(this.token, this.finish, (int)value);
            }
            return LiteralNode.newInstance(this.token, this.finish, value);
        }
    }

    private static class UnaryNodeConstantEvaluator
    extends ConstantEvaluator<UnaryNode> {
        UnaryNodeConstantEvaluator(UnaryNode parent) {
            super(parent);
        }

        @Override
        protected LiteralNode<?> eval() {
            LiteralNode<Serializable> literalNode;
            Expression rhsNode = ((UnaryNode)this.parent).getExpression();
            if (!(rhsNode instanceof LiteralNode)) {
                return null;
            }
            if (rhsNode instanceof LiteralNode.ArrayLiteralNode) {
                return null;
            }
            LiteralNode rhs = (LiteralNode)rhsNode;
            Type rhsType = rhs.getType();
            boolean rhsInteger = rhsType.isInteger() || rhsType.isBoolean();
            switch (((UnaryNode)this.parent).tokenType()) {
                case ADD: {
                    if (rhsInteger) {
                        literalNode = LiteralNode.newInstance(this.token, this.finish, rhs.getInt32());
                        break;
                    }
                    if (rhsType.isLong()) {
                        literalNode = LiteralNode.newInstance(this.token, this.finish, rhs.getLong());
                        break;
                    }
                    literalNode = LiteralNode.newInstance(this.token, this.finish, rhs.getNumber());
                    break;
                }
                case SUB: {
                    if (rhsInteger && rhs.getInt32() != 0) {
                        literalNode = LiteralNode.newInstance(this.token, this.finish, -rhs.getInt32());
                        break;
                    }
                    if (rhsType.isLong() && rhs.getLong() != 0L) {
                        literalNode = LiteralNode.newInstance(this.token, this.finish, -rhs.getLong());
                        break;
                    }
                    literalNode = LiteralNode.newInstance(this.token, this.finish, -rhs.getNumber());
                    break;
                }
                case NOT: {
                    literalNode = LiteralNode.newInstance(this.token, this.finish, !rhs.getBoolean());
                    break;
                }
                case BIT_NOT: {
                    literalNode = LiteralNode.newInstance(this.token, this.finish, ~rhs.getInt32());
                    break;
                }
                default: {
                    return null;
                }
            }
            return literalNode;
        }
    }

    static abstract class ConstantEvaluator<T extends Node> {
        protected T parent;
        protected final long token;
        protected final int finish;

        protected ConstantEvaluator(T parent) {
            this.parent = parent;
            this.token = ((Node)parent).getToken();
            this.finish = ((Node)parent).getFinish();
        }

        protected abstract LiteralNode<?> eval();
    }
}

