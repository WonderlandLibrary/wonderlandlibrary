/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.parser.TokenType;

final class LocalVariableTypesCalculator
extends SimpleNodeVisitor {
    private static final Map<Type, LvarType> TO_LVAR_TYPE = new IdentityHashMap<Type, LvarType>();
    private final Compiler compiler;
    private final Map<Label, JumpTarget> jumpTargets = new IdentityHashMap<Label, JumpTarget>();
    private Map<Symbol, LvarType> localVariableTypes = Collections.emptyMap();
    private final Set<Symbol> invalidatedSymbols = new HashSet<Symbol>();
    private final Deque<LvarType> typeStack = new ArrayDeque<LvarType>();
    private boolean reachable = true;
    private Type returnType = Type.UNKNOWN;
    private ReturnNode syntheticReturn;
    private boolean alreadyEnteredTopLevelFunction;
    private final Map<JoinPredecessor, LocalVariableConversion> localVariableConversions = new IdentityHashMap<JoinPredecessor, LocalVariableConversion>();
    private final Map<IdentNode, LvarType> identifierLvarTypes = new IdentityHashMap<IdentNode, LvarType>();
    private final Map<Symbol, SymbolConversions> symbolConversions = new IdentityHashMap<Symbol, SymbolConversions>();
    private final Deque<Label> catchLabels = new ArrayDeque<Label>();

    private static HashMap<Symbol, LvarType> cloneMap(Map<Symbol, LvarType> map) {
        return (HashMap)((HashMap)map).clone();
    }

    private LocalVariableConversion createConversion(Symbol symbol, LvarType branchLvarType, Map<Symbol, LvarType> joinLvarTypes, LocalVariableConversion next) {
        if (this.invalidatedSymbols.contains(symbol)) {
            return next;
        }
        LvarType targetType = joinLvarTypes.get(symbol);
        assert (targetType != null);
        if (targetType == branchLvarType) {
            return next;
        }
        this.symbolIsConverted(symbol, branchLvarType, targetType);
        return new LocalVariableConversion(symbol, branchLvarType.type, targetType.type, next);
    }

    private Map<Symbol, LvarType> getUnionTypes(Map<Symbol, LvarType> types1, Map<Symbol, LvarType> types2) {
        HashMap<Symbol, LvarType> union;
        if (types1 == types2 || types1.isEmpty()) {
            return types2;
        }
        if (types2.isEmpty()) {
            return types1;
        }
        HashSet<Symbol> commonSymbols = new HashSet<Symbol>(types1.keySet());
        commonSymbols.retainAll(types2.keySet());
        int commonSize = commonSymbols.size();
        int types1Size = types1.size();
        int types2Size = types2.size();
        if (commonSize == types1Size && commonSize == types2Size) {
            boolean matches1 = true;
            boolean matches2 = true;
            HashMap<Symbol, LvarType> union2 = null;
            for (Symbol symbol : commonSymbols) {
                LvarType type2;
                LvarType type1 = types1.get(symbol);
                LvarType widest = LocalVariableTypesCalculator.widestLvarType(type1, type2 = types2.get(symbol));
                if (widest != type1 && matches1) {
                    matches1 = false;
                    if (!matches2) {
                        union2 = LocalVariableTypesCalculator.cloneMap(types1);
                    }
                }
                if (widest != type2 && matches2) {
                    matches2 = false;
                    if (!matches1) {
                        union2 = LocalVariableTypesCalculator.cloneMap(types2);
                    }
                }
                if (matches1 || matches2) continue;
                assert (union2 != null);
                union2.put(symbol, widest);
            }
            return matches1 ? types1 : (matches2 ? types2 : union2);
        }
        if (types1Size > types2Size) {
            union = LocalVariableTypesCalculator.cloneMap(types1);
            union.putAll(types2);
        } else {
            union = LocalVariableTypesCalculator.cloneMap(types2);
            union.putAll(types1);
        }
        for (Symbol symbol : commonSymbols) {
            LvarType type1 = types1.get(symbol);
            LvarType type2 = types2.get(symbol);
            union.put(symbol, LocalVariableTypesCalculator.widestLvarType(type1, type2));
        }
        union.keySet().removeAll(this.invalidatedSymbols);
        return union;
    }

    private static void symbolIsUsed(Symbol symbol, LvarType type) {
        if (type != LvarType.UNDEFINED) {
            symbol.setHasSlotFor(type.type);
        }
    }

    private void symbolIsConverted(Symbol symbol, LvarType from, LvarType to) {
        SymbolConversions conversions = this.symbolConversions.get(symbol);
        if (conversions == null) {
            conversions = new SymbolConversions();
            this.symbolConversions.put(symbol, conversions);
        }
        conversions.recordConversion(from, to);
    }

    private static LvarType toLvarType(Type type) {
        assert (type != null);
        LvarType lvarType = TO_LVAR_TYPE.get(type);
        if (lvarType != null) {
            return lvarType;
        }
        assert (type.isObject()) : "Unsupported primitive type: " + type;
        return LvarType.OBJECT;
    }

    private static LvarType widestLvarType(LvarType t1, LvarType t2) {
        if (t1 == t2) {
            return t1;
        }
        if (t1.ordinal() < LvarType.INT.ordinal() || t2.ordinal() < LvarType.INT.ordinal()) {
            return LvarType.OBJECT;
        }
        return LvarType.values()[Math.max(t1.ordinal(), t2.ordinal())];
    }

    LocalVariableTypesCalculator(Compiler compiler) {
        this.compiler = compiler;
    }

    private JumpTarget createJumpTarget(Label label) {
        assert (!this.jumpTargets.containsKey(label));
        JumpTarget jumpTarget = new JumpTarget();
        this.jumpTargets.put(label, jumpTarget);
        return jumpTarget;
    }

    private void doesNotContinueSequentially() {
        this.reachable = false;
        this.localVariableTypes = Collections.emptyMap();
        this.assertTypeStackIsEmpty();
    }

    private boolean pushExpressionType(Expression expr) {
        this.typeStack.push(LocalVariableTypesCalculator.toLvarType(expr.getType()));
        return false;
    }

    @Override
    public boolean enterAccessNode(AccessNode accessNode) {
        this.visitExpression(accessNode.getBase());
        return this.pushExpressionType(accessNode);
    }

    @Override
    public boolean enterBinaryNode(BinaryNode binaryNode) {
        Label joinLabel;
        Expression lhs = binaryNode.lhs();
        LvarType lhsType = !(lhs instanceof IdentNode) || !binaryNode.isTokenType(TokenType.ASSIGN) ? this.visitExpression(lhs) : LvarType.UNDEFINED;
        boolean isLogical = binaryNode.isLogical();
        Label label = joinLabel = isLogical ? new Label("") : null;
        if (isLogical) {
            this.jumpToLabel((JoinPredecessor)((Object)lhs), joinLabel);
        }
        Expression rhs = binaryNode.rhs();
        LvarType rhsType = this.visitExpression(rhs);
        if (isLogical) {
            this.jumpToLabel((JoinPredecessor)((Object)rhs), joinLabel);
        }
        this.joinOnLabel(joinLabel);
        LvarType type = LocalVariableTypesCalculator.toLvarType(binaryNode.setOperands(lhsType.typeExpression, rhsType.typeExpression).getType());
        if (binaryNode.isAssignment() && lhs instanceof IdentNode) {
            if (binaryNode.isSelfModifying()) {
                this.onSelfAssignment((IdentNode)lhs, type);
            } else {
                this.onAssignment((IdentNode)lhs, type);
            }
        }
        this.typeStack.push(type);
        return false;
    }

    @Override
    public boolean enterBlock(Block block) {
        boolean cloned = false;
        for (Symbol symbol : block.getSymbols()) {
            if (!symbol.isBytecodeLocal()) continue;
            if (this.getLocalVariableTypeOrNull(symbol) == null) {
                if (!cloned) {
                    this.cloneOrNewLocalVariableTypes();
                    cloned = true;
                }
                this.localVariableTypes.put(symbol, LvarType.UNDEFINED);
            }
            this.invalidatedSymbols.remove(symbol);
        }
        return true;
    }

    @Override
    public boolean enterBreakNode(BreakNode breakNode) {
        return this.enterJumpStatement(breakNode);
    }

    @Override
    public boolean enterCallNode(CallNode callNode) {
        this.visitExpression(callNode.getFunction());
        this.visitExpressions(callNode.getArgs());
        CallNode.EvalArgs evalArgs = callNode.getEvalArgs();
        if (evalArgs != null) {
            this.visitExpressions(evalArgs.getArgs());
        }
        return this.pushExpressionType(callNode);
    }

    @Override
    public boolean enterContinueNode(ContinueNode continueNode) {
        return this.enterJumpStatement(continueNode);
    }

    private boolean enterJumpStatement(JumpStatement jump) {
        if (!this.reachable) {
            return false;
        }
        this.assertTypeStackIsEmpty();
        this.jumpToLabel(jump, jump.getTargetLabel(this.lc), this.getBreakTargetTypes(jump.getPopScopeLimit(this.lc)));
        this.doesNotContinueSequentially();
        return false;
    }

    @Override
    protected boolean enterDefault(Node node) {
        return this.reachable;
    }

    private void enterDoWhileLoop(WhileNode loopNode) {
        this.assertTypeStackIsEmpty();
        JoinPredecessorExpression test = loopNode.getTest();
        Block body = loopNode.getBody();
        Label continueLabel = loopNode.getContinueLabel();
        Label breakLabel = loopNode.getBreakLabel();
        Map<Symbol, LvarType> beforeLoopTypes = this.localVariableTypes;
        Label repeatLabel = new Label("");
        while (true) {
            this.jumpToLabel(loopNode, repeatLabel, beforeLoopTypes);
            Map<Symbol, LvarType> beforeRepeatTypes = this.localVariableTypes;
            body.accept(this);
            if (this.reachable) {
                this.jumpToLabel(body, continueLabel);
            }
            this.joinOnLabel(continueLabel);
            if (!this.reachable) break;
            this.visitExpressionOnEmptyStack(test);
            this.jumpToLabel(test, breakLabel);
            if (Expression.isAlwaysFalse(test)) break;
            this.jumpToLabel(test, repeatLabel);
            this.joinOnLabel(repeatLabel);
            if (this.localVariableTypes.equals(beforeRepeatTypes)) break;
            this.resetJoinPoint(continueLabel);
            this.resetJoinPoint(breakLabel);
            this.resetJoinPoint(repeatLabel);
        }
        if (Expression.isAlwaysTrue(test)) {
            this.doesNotContinueSequentially();
        }
        this.leaveBreakable(loopNode);
    }

    @Override
    public boolean enterExpressionStatement(ExpressionStatement expressionStatement) {
        if (this.reachable) {
            this.visitExpressionOnEmptyStack(expressionStatement.getExpression());
        }
        return false;
    }

    private void assertTypeStackIsEmpty() {
        assert (this.typeStack.isEmpty());
    }

    @Override
    protected Node leaveDefault(Node node) {
        assert (!(node instanceof Expression));
        assert (!(node instanceof Statement) || this.typeStack.isEmpty());
        return node;
    }

    private LvarType visitExpressionOnEmptyStack(Expression expr) {
        this.assertTypeStackIsEmpty();
        return this.visitExpression(expr);
    }

    private LvarType visitExpression(Expression expr) {
        int stackSize = this.typeStack.size();
        expr.accept(this);
        assert (this.typeStack.size() == stackSize + 1);
        return this.typeStack.pop();
    }

    private void visitExpressions(List<Expression> exprs) {
        for (Expression expr : exprs) {
            if (expr == null) continue;
            this.visitExpression(expr);
        }
    }

    @Override
    public boolean enterForNode(ForNode forNode) {
        if (!this.reachable) {
            return false;
        }
        Expression init = forNode.getInit();
        if (forNode.isForIn()) {
            JoinPredecessorExpression iterable = forNode.getModify();
            this.visitExpression(iterable);
            this.enterTestFirstLoop(forNode, null, init, !this.compiler.useOptimisticTypes() || !forNode.isForEach() && this.compiler.hasStringPropertyIterator(iterable.getExpression()));
        } else {
            if (init != null) {
                this.visitExpressionOnEmptyStack(init);
            }
            this.enterTestFirstLoop(forNode, forNode.getModify(), null, false);
        }
        this.assertTypeStackIsEmpty();
        return false;
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        if (this.alreadyEnteredTopLevelFunction) {
            this.typeStack.push(LvarType.OBJECT);
            return false;
        }
        int pos = 0;
        if (!functionNode.isVarArg()) {
            for (IdentNode param : functionNode.getParameters()) {
                Symbol symbol = param.getSymbol();
                assert (symbol.hasSlot());
                Type callSiteParamType = this.compiler.getParamType(functionNode, pos);
                LvarType paramType = callSiteParamType == null ? LvarType.OBJECT : LocalVariableTypesCalculator.toLvarType(callSiteParamType);
                this.setType(symbol, paramType);
                this.symbolIsUsed(symbol);
                this.setIdentifierLvarType(param, paramType);
                ++pos;
            }
        }
        this.setCompilerConstantAsObject(functionNode, CompilerConstants.THIS);
        if (functionNode.hasScopeBlock() || functionNode.needsParentScope()) {
            this.setCompilerConstantAsObject(functionNode, CompilerConstants.SCOPE);
        }
        if (functionNode.needsCallee()) {
            this.setCompilerConstantAsObject(functionNode, CompilerConstants.CALLEE);
        }
        if (functionNode.needsArguments()) {
            this.setCompilerConstantAsObject(functionNode, CompilerConstants.ARGUMENTS);
        }
        this.alreadyEnteredTopLevelFunction = true;
        return true;
    }

    @Override
    public boolean enterGetSplitState(GetSplitState getSplitState) {
        return this.pushExpressionType(getSplitState);
    }

    @Override
    public boolean enterIdentNode(IdentNode identNode) {
        Symbol symbol = identNode.getSymbol();
        if (symbol.isBytecodeLocal()) {
            this.symbolIsUsed(symbol);
            LvarType type = this.getLocalVariableType(symbol);
            this.setIdentifierLvarType(identNode, type);
            this.typeStack.push(type);
        } else {
            this.pushExpressionType(identNode);
        }
        return false;
    }

    @Override
    public boolean enterIfNode(IfNode ifNode) {
        this.processIfNode(ifNode);
        return false;
    }

    private void processIfNode(IfNode ifNode) {
        boolean reachableFromPass;
        Map<Symbol, LvarType> passLvarTypes;
        if (!this.reachable) {
            return;
        }
        Expression test = ifNode.getTest();
        Block pass = ifNode.getPass();
        Block fail = ifNode.getFail();
        this.visitExpressionOnEmptyStack(test);
        boolean isTestAlwaysTrue = Expression.isAlwaysTrue(test);
        if (Expression.isAlwaysFalse(test)) {
            passLvarTypes = null;
            reachableFromPass = false;
        } else {
            Map<Symbol, LvarType> afterTestLvarTypes = this.localVariableTypes;
            pass.accept(this);
            this.assertTypeStackIsEmpty();
            if (isTestAlwaysTrue) {
                return;
            }
            passLvarTypes = this.localVariableTypes;
            reachableFromPass = this.reachable;
            this.localVariableTypes = afterTestLvarTypes;
            this.reachable = true;
        }
        assert (!isTestAlwaysTrue);
        if (fail != null) {
            fail.accept(this);
            this.assertTypeStackIsEmpty();
        }
        if (this.reachable) {
            if (reachableFromPass) {
                Map<Symbol, LvarType> failLvarTypes = this.localVariableTypes;
                this.localVariableTypes = this.getUnionTypes(passLvarTypes, failLvarTypes);
                this.setConversion(pass, passLvarTypes, this.localVariableTypes);
                this.setConversion((JoinPredecessor)((Object)(fail != null ? fail : ifNode)), failLvarTypes, this.localVariableTypes);
            }
        } else if (reachableFromPass) {
            assert (passLvarTypes != null);
            this.localVariableTypes = passLvarTypes;
            this.reachable = true;
        }
    }

    @Override
    public boolean enterIndexNode(IndexNode indexNode) {
        this.visitExpression(indexNode.getBase());
        this.visitExpression(indexNode.getIndex());
        return this.pushExpressionType(indexNode);
    }

    @Override
    public boolean enterJoinPredecessorExpression(JoinPredecessorExpression joinExpr) {
        Expression expr = joinExpr.getExpression();
        if (expr != null) {
            expr.accept(this);
        } else {
            this.typeStack.push(LvarType.UNDEFINED);
        }
        return false;
    }

    @Override
    public boolean enterJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return this.enterJumpStatement(jumpToInlinedFinally);
    }

    @Override
    public boolean enterLiteralNode(LiteralNode<?> literalNode) {
        List<Expression> expressions;
        if (literalNode instanceof LiteralNode.ArrayLiteralNode && (expressions = ((LiteralNode.ArrayLiteralNode)literalNode).getElementExpressions()) != null) {
            this.visitExpressions(expressions);
        }
        this.pushExpressionType(literalNode);
        return false;
    }

    @Override
    public boolean enterObjectNode(ObjectNode objectNode) {
        for (PropertyNode propertyNode : objectNode.getElements()) {
            Expression value = propertyNode.getValue();
            if (value == null) continue;
            this.visitExpression(value);
        }
        return this.pushExpressionType(objectNode);
    }

    @Override
    public boolean enterPropertyNode(PropertyNode propertyNode) {
        throw new AssertionError();
    }

    @Override
    public boolean enterReturnNode(ReturnNode returnNode) {
        Type returnExprType;
        if (!this.reachable) {
            return false;
        }
        Expression returnExpr = returnNode.getExpression();
        if (returnExpr != null) {
            returnExprType = this.visitExpressionOnEmptyStack(returnExpr).type;
        } else {
            this.assertTypeStackIsEmpty();
            returnExprType = Type.UNDEFINED;
        }
        this.returnType = Type.widestReturnType(this.returnType, returnExprType);
        this.doesNotContinueSequentially();
        return false;
    }

    @Override
    public boolean enterRuntimeNode(RuntimeNode runtimeNode) {
        this.visitExpressions(runtimeNode.getArgs());
        return this.pushExpressionType(runtimeNode);
    }

    @Override
    public boolean enterSplitReturn(SplitReturn splitReturn) {
        this.doesNotContinueSequentially();
        return false;
    }

    @Override
    public boolean enterSwitchNode(SwitchNode switchNode) {
        if (!this.reachable) {
            return false;
        }
        this.visitExpressionOnEmptyStack(switchNode.getExpression());
        List<CaseNode> cases = switchNode.getCases();
        if (cases.isEmpty()) {
            return false;
        }
        boolean isInteger = switchNode.isUniqueInteger();
        Label breakLabel = switchNode.getBreakLabel();
        boolean hasDefault = switchNode.getDefaultCase() != null;
        boolean tagUsed = false;
        for (CaseNode caseNode : cases) {
            Expression test = caseNode.getTest();
            if (!isInteger && test != null) {
                this.visitExpressionOnEmptyStack(test);
                if (!tagUsed) {
                    LocalVariableTypesCalculator.symbolIsUsed(switchNode.getTag(), LvarType.OBJECT);
                    tagUsed = true;
                }
            }
            this.jumpToLabel(caseNode, caseNode.getBody().getEntryLabel());
        }
        if (!hasDefault) {
            this.jumpToLabel(switchNode, breakLabel);
        }
        this.doesNotContinueSequentially();
        Block previousBlock = null;
        for (CaseNode caseNode : cases) {
            Block body = caseNode.getBody();
            Label entryLabel = body.getEntryLabel();
            if (previousBlock != null && this.reachable) {
                this.jumpToLabel(previousBlock, entryLabel);
            }
            this.joinOnLabel(entryLabel);
            assert (this.reachable);
            body.accept(this);
            previousBlock = body;
        }
        if (previousBlock != null && this.reachable) {
            this.jumpToLabel(previousBlock, breakLabel);
        }
        this.leaveBreakable(switchNode);
        return false;
    }

    @Override
    public boolean enterTernaryNode(TernaryNode ternaryNode) {
        Expression test = ternaryNode.getTest();
        JoinPredecessorExpression trueExpr = ternaryNode.getTrueExpression();
        JoinPredecessorExpression falseExpr = ternaryNode.getFalseExpression();
        this.visitExpression(test);
        Map<Symbol, LvarType> testExitLvarTypes = this.localVariableTypes;
        LvarType trueType = !Expression.isAlwaysFalse(test) ? this.visitExpression(trueExpr) : null;
        Map<Symbol, LvarType> trueExitLvarTypes = this.localVariableTypes;
        this.localVariableTypes = testExitLvarTypes;
        LvarType falseType = !Expression.isAlwaysTrue(test) ? this.visitExpression(falseExpr) : null;
        Map<Symbol, LvarType> falseExitLvarTypes = this.localVariableTypes;
        this.localVariableTypes = this.getUnionTypes(trueExitLvarTypes, falseExitLvarTypes);
        this.setConversion(trueExpr, trueExitLvarTypes, this.localVariableTypes);
        this.setConversion(falseExpr, falseExitLvarTypes, this.localVariableTypes);
        this.typeStack.push(trueType != null ? (falseType != null ? LocalVariableTypesCalculator.widestLvarType(trueType, falseType) : trueType) : LocalVariableTypesCalculator.assertNotNull(falseType));
        return false;
    }

    private static <T> T assertNotNull(T t) {
        assert (t != null);
        return t;
    }

    private void enterTestFirstLoop(LoopNode loopNode, JoinPredecessorExpression modify, Expression iteratorValues, boolean iteratorValuesAreObject) {
        JoinPredecessorExpression test = loopNode.getTest();
        if (Expression.isAlwaysFalse(test)) {
            this.visitExpressionOnEmptyStack(test);
            return;
        }
        Label continueLabel = loopNode.getContinueLabel();
        Label breakLabel = loopNode.getBreakLabel();
        Label repeatLabel = modify == null ? continueLabel : new Label("");
        Map<Symbol, LvarType> beforeLoopTypes = this.localVariableTypes;
        while (true) {
            this.jumpToLabel(loopNode, repeatLabel, beforeLoopTypes);
            Map<Symbol, LvarType> beforeRepeatTypes = this.localVariableTypes;
            if (test != null) {
                this.visitExpressionOnEmptyStack(test);
            }
            if (!Expression.isAlwaysTrue(test)) {
                this.jumpToLabel(test, breakLabel);
            }
            if (iteratorValues instanceof IdentNode) {
                IdentNode ident = (IdentNode)iteratorValues;
                this.onAssignment(ident, iteratorValuesAreObject ? LvarType.OBJECT : LocalVariableTypesCalculator.toLvarType(this.compiler.getOptimisticType(ident)));
            }
            Block body = loopNode.getBody();
            body.accept(this);
            if (this.reachable) {
                this.jumpToLabel(body, continueLabel);
            }
            this.joinOnLabel(continueLabel);
            if (!this.reachable) break;
            if (modify != null) {
                this.visitExpressionOnEmptyStack(modify);
                this.jumpToLabel(modify, repeatLabel);
                this.joinOnLabel(repeatLabel);
            }
            if (this.localVariableTypes.equals(beforeRepeatTypes)) break;
            this.resetJoinPoint(continueLabel);
            this.resetJoinPoint(breakLabel);
            this.resetJoinPoint(repeatLabel);
        }
        if (Expression.isAlwaysTrue(test) && iteratorValues == null) {
            this.doesNotContinueSequentially();
        }
        this.leaveBreakable(loopNode);
    }

    @Override
    public boolean enterThrowNode(ThrowNode throwNode) {
        if (!this.reachable) {
            return false;
        }
        this.visitExpressionOnEmptyStack(throwNode.getExpression());
        this.jumpToCatchBlock(throwNode);
        this.doesNotContinueSequentially();
        return false;
    }

    @Override
    public boolean enterTryNode(TryNode tryNode) {
        if (!this.reachable) {
            return false;
        }
        Label catchLabel = new Label("");
        this.catchLabels.push(catchLabel);
        this.jumpToLabel(tryNode, catchLabel);
        Block body = tryNode.getBody();
        body.accept(this);
        this.catchLabels.pop();
        Label endLabel = new Label("");
        boolean canExit = false;
        if (this.reachable) {
            this.jumpToLabel(body, endLabel);
            canExit = true;
        }
        this.doesNotContinueSequentially();
        for (Block inlinedFinally : tryNode.getInlinedFinallies()) {
            Block finallyBody = TryNode.getLabelledInlinedFinallyBlock(inlinedFinally);
            this.joinOnLabel(finallyBody.getEntryLabel());
            if (!this.reachable) continue;
            finallyBody.accept(this);
            assert (!this.reachable);
        }
        this.joinOnLabel(catchLabel);
        for (CatchNode catchNode : tryNode.getCatches()) {
            IdentNode exception = catchNode.getException();
            this.onAssignment(exception, LvarType.OBJECT);
            Expression condition = catchNode.getExceptionCondition();
            if (condition != null) {
                this.visitExpression(condition);
            }
            Map<Symbol, LvarType> afterConditionTypes = this.localVariableTypes;
            Block catchBody = catchNode.getBody();
            this.reachable = true;
            catchBody.accept(this);
            if (this.reachable) {
                this.jumpToLabel(catchBody, endLabel);
                canExit = true;
            }
            this.localVariableTypes = afterConditionTypes;
        }
        this.doesNotContinueSequentially();
        if (canExit) {
            this.joinOnLabel(endLabel);
        }
        return false;
    }

    @Override
    public boolean enterUnaryNode(UnaryNode unaryNode) {
        Expression expr = unaryNode.getExpression();
        LvarType unaryType = LocalVariableTypesCalculator.toLvarType(unaryNode.setExpression(this.visitExpression(expr).typeExpression).getType());
        if (unaryNode.isSelfModifying() && expr instanceof IdentNode) {
            this.onSelfAssignment((IdentNode)expr, unaryType);
        }
        this.typeStack.push(unaryType);
        return false;
    }

    @Override
    public boolean enterVarNode(VarNode varNode) {
        if (!this.reachable) {
            return false;
        }
        Expression init = varNode.getInit();
        if (init != null) {
            this.onAssignment(varNode.getName(), this.visitExpression(init));
        }
        return false;
    }

    @Override
    public boolean enterWhileNode(WhileNode whileNode) {
        if (!this.reachable) {
            return false;
        }
        if (whileNode.isDoWhile()) {
            this.enterDoWhileLoop(whileNode);
        } else {
            this.enterTestFirstLoop(whileNode, null, null, false);
        }
        return false;
    }

    @Override
    public boolean enterWithNode(WithNode withNode) {
        if (this.reachable) {
            this.visitExpression(withNode.getExpression());
            withNode.getBody().accept(this);
        }
        return false;
    }

    private Map<Symbol, LvarType> getBreakTargetTypes(LexicalContextNode target) {
        Map<Symbol, LvarType> types = this.localVariableTypes;
        Iterator<LexicalContextNode> it = this.lc.getAllNodes();
        while (it.hasNext()) {
            LexicalContextNode node = it.next();
            if (node instanceof Block) {
                for (Symbol symbol : ((Block)node).getSymbols()) {
                    if (!this.localVariableTypes.containsKey(symbol)) continue;
                    if (types == this.localVariableTypes) {
                        types = LocalVariableTypesCalculator.cloneMap(this.localVariableTypes);
                    }
                    types.remove(symbol);
                }
            }
            if (node != target) continue;
            break;
        }
        return types;
    }

    private LvarType getLocalVariableType(Symbol symbol) {
        LvarType type = this.getLocalVariableTypeOrNull(symbol);
        assert (type != null);
        return type;
    }

    private LvarType getLocalVariableTypeOrNull(Symbol symbol) {
        return this.localVariableTypes.get(symbol);
    }

    private JumpTarget getOrCreateJumpTarget(Label label) {
        JumpTarget jumpTarget = this.jumpTargets.get(label);
        if (jumpTarget == null) {
            jumpTarget = this.createJumpTarget(label);
        }
        return jumpTarget;
    }

    private void joinOnLabel(Label label) {
        JumpTarget jumpTarget = this.jumpTargets.remove(label);
        if (jumpTarget == null) {
            return;
        }
        assert (!jumpTarget.origins.isEmpty());
        this.reachable = true;
        this.localVariableTypes = this.getUnionTypes(jumpTarget.types, this.localVariableTypes);
        for (JumpOrigin jumpOrigin : jumpTarget.origins) {
            this.setConversion(jumpOrigin.node, jumpOrigin.types, this.localVariableTypes);
        }
    }

    private void jumpToCatchBlock(JoinPredecessor jumpOrigin) {
        Label currentCatchLabel = this.catchLabels.peek();
        if (currentCatchLabel != null) {
            this.jumpToLabel(jumpOrigin, currentCatchLabel);
        }
    }

    private void jumpToLabel(JoinPredecessor jumpOrigin, Label label) {
        this.jumpToLabel(jumpOrigin, label, this.localVariableTypes);
    }

    private void jumpToLabel(JoinPredecessor jumpOrigin, Label label, Map<Symbol, LvarType> types) {
        this.getOrCreateJumpTarget(label).addOrigin(jumpOrigin, types, this);
    }

    @Override
    public Node leaveBlock(Block block) {
        LabelNode labelNode;
        if (this.lc.isFunctionBody()) {
            if (this.reachable) {
                this.createSyntheticReturn(block);
                assert (!this.reachable);
            }
            this.calculateReturnType();
        }
        boolean cloned = false;
        for (Symbol symbol : block.getSymbols()) {
            SymbolConversions conversions;
            if (!symbol.hasSlot()) continue;
            if (symbol.isBytecodeLocal()) {
                if (this.localVariableTypes.containsKey(symbol) && !cloned) {
                    this.localVariableTypes = LocalVariableTypesCalculator.cloneMap(this.localVariableTypes);
                    cloned = true;
                }
                this.invalidateSymbol(symbol);
            }
            if ((conversions = this.symbolConversions.get(symbol)) != null) {
                conversions.calculateTypeLiveness(symbol);
            }
            if (symbol.slotCount() != 0) continue;
            symbol.setNeedsSlot(false);
        }
        if (this.reachable && (labelNode = this.lc.getCurrentBlockLabelNode()) != null) {
            this.jumpToLabel(labelNode, block.getBreakLabel());
        }
        this.leaveBreakable(block);
        return block;
    }

    private void calculateReturnType() {
        if (this.returnType.isUnknown()) {
            this.returnType = Type.OBJECT;
        }
    }

    private void createSyntheticReturn(Block body) {
        FunctionNode functionNode = this.lc.getCurrentFunction();
        long token = functionNode.getToken();
        int finish = functionNode.getFinish();
        List<Statement> statements = body.getStatements();
        int lineNumber = statements.isEmpty() ? functionNode.getLineNumber() : statements.get(statements.size() - 1).getLineNumber();
        IdentNode returnExpr = functionNode.isProgram() ? new IdentNode(token, finish, CompilerConstants.RETURN.symbolName()).setSymbol(LocalVariableTypesCalculator.getCompilerConstantSymbol(functionNode, CompilerConstants.RETURN)) : null;
        this.syntheticReturn = new ReturnNode(lineNumber, token, finish, returnExpr);
        this.syntheticReturn.accept(this);
    }

    private void leaveBreakable(BreakableNode breakable) {
        this.joinOnLabel(breakable.getBreakLabel());
        this.assertTypeStackIsEmpty();
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        FunctionNode newFunction = functionNode;
        SimpleNodeVisitor applyChangesVisitor = new SimpleNodeVisitor(){
            private boolean inOuterFunction = true;
            private final Deque<JoinPredecessor> joinPredecessors = new ArrayDeque<JoinPredecessor>();

            @Override
            protected boolean enterDefault(Node node) {
                if (!this.inOuterFunction) {
                    return false;
                }
                if (node instanceof JoinPredecessor) {
                    this.joinPredecessors.push((JoinPredecessor)((Object)node));
                }
                return this.inOuterFunction;
            }

            @Override
            public boolean enterFunctionNode(FunctionNode fn) {
                if (LocalVariableTypesCalculator.this.compiler.isOnDemandCompilation()) {
                    return false;
                }
                this.inOuterFunction = false;
                return true;
            }

            @Override
            public Node leaveBinaryNode(BinaryNode binaryNode) {
                if (binaryNode.isComparison()) {
                    Expression lhs = binaryNode.lhs();
                    Expression rhs = binaryNode.rhs();
                    TokenType tt = binaryNode.tokenType();
                    switch (tt) {
                        case EQ_STRICT: 
                        case NE_STRICT: {
                            Expression undefinedNode = LocalVariableTypesCalculator.createIsUndefined(binaryNode, lhs, rhs, tt == TokenType.EQ_STRICT ? RuntimeNode.Request.IS_UNDEFINED : RuntimeNode.Request.IS_NOT_UNDEFINED);
                            if (undefinedNode != binaryNode) {
                                return undefinedNode;
                            }
                            if (lhs.getType().isBoolean() == rhs.getType().isBoolean()) break;
                            return new RuntimeNode(binaryNode);
                        }
                    }
                    if (lhs.getType().isObject() && rhs.getType().isObject()) {
                        return new RuntimeNode(binaryNode);
                    }
                } else if (binaryNode.isOptimisticUndecidedType()) {
                    return binaryNode.decideType();
                }
                return binaryNode;
            }

            @Override
            protected Node leaveDefault(Node node) {
                if (node instanceof JoinPredecessor) {
                    JoinPredecessor original = this.joinPredecessors.pop();
                    assert (original.getClass() == node.getClass()) : original.getClass().getName() + "!=" + node.getClass().getName();
                    JoinPredecessor newNode = this.setLocalVariableConversion(original, (JoinPredecessor)((Object)node));
                    if (newNode instanceof LexicalContextNode) {
                        this.lc.replace((LexicalContextNode)((Object)node), (LexicalContextNode)((Object)newNode));
                    }
                    return (Node)((Object)newNode);
                }
                return node;
            }

            @Override
            public Node leaveBlock(Block block) {
                if (this.inOuterFunction && LocalVariableTypesCalculator.this.syntheticReturn != null && this.lc.isFunctionBody()) {
                    ArrayList<Statement> stmts = new ArrayList<Statement>(block.getStatements());
                    stmts.add((ReturnNode)LocalVariableTypesCalculator.this.syntheticReturn.accept(this));
                    return block.setStatements(this.lc, stmts);
                }
                return super.leaveBlock(block);
            }

            @Override
            public Node leaveFunctionNode(FunctionNode nestedFunctionNode) {
                this.inOuterFunction = true;
                FunctionNode newNestedFunction = (FunctionNode)nestedFunctionNode.accept((NodeVisitor)new LocalVariableTypesCalculator(LocalVariableTypesCalculator.this.compiler));
                this.lc.replace(nestedFunctionNode, newNestedFunction);
                return newNestedFunction;
            }

            @Override
            public Node leaveIdentNode(IdentNode identNode) {
                IdentNode original = (IdentNode)this.joinPredecessors.pop();
                Symbol symbol = identNode.getSymbol();
                if (symbol == null) {
                    assert (identNode.isPropertyName());
                    return identNode;
                }
                if (symbol.hasSlot()) {
                    assert (!symbol.isScope() || symbol.isParam());
                    assert (original.getName().equals(identNode.getName()));
                    LvarType lvarType = (LvarType)((Object)LocalVariableTypesCalculator.this.identifierLvarTypes.remove(original));
                    if (lvarType != null) {
                        return this.setLocalVariableConversion(original, identNode.setType(lvarType.type));
                    }
                    assert (LocalVariableTypesCalculator.this.localVariableConversions.get(original) == null);
                } else assert (LocalVariableTypesCalculator.this.identIsDeadAndHasNoLiveConversions(original));
                return identNode;
            }

            @Override
            public Node leaveLiteralNode(LiteralNode<?> literalNode) {
                return literalNode.initialize(this.lc);
            }

            @Override
            public Node leaveRuntimeNode(RuntimeNode runtimeNode) {
                boolean isEqStrict;
                RuntimeNode.Request request = runtimeNode.getRequest();
                boolean bl = isEqStrict = request == RuntimeNode.Request.EQ_STRICT;
                if (isEqStrict || request == RuntimeNode.Request.NE_STRICT) {
                    return LocalVariableTypesCalculator.createIsUndefined(runtimeNode, runtimeNode.getArgs().get(0), runtimeNode.getArgs().get(1), isEqStrict ? RuntimeNode.Request.IS_UNDEFINED : RuntimeNode.Request.IS_NOT_UNDEFINED);
                }
                return runtimeNode;
            }

            private <T extends JoinPredecessor> T setLocalVariableConversion(JoinPredecessor original, T jp) {
                return (T)jp.setLocalVariableConversion(this.lc, (LocalVariableConversion)LocalVariableTypesCalculator.this.localVariableConversions.get(original));
            }
        };
        newFunction = newFunction.setBody(this.lc, (Block)newFunction.getBody().accept(applyChangesVisitor));
        newFunction = newFunction.setReturnType(this.lc, this.returnType);
        newFunction = newFunction.setParameters(this.lc, newFunction.visitParameters(applyChangesVisitor));
        return newFunction;
    }

    private static Expression createIsUndefined(Expression parent, Expression lhs, Expression rhs, RuntimeNode.Request request) {
        if (LocalVariableTypesCalculator.isUndefinedIdent(lhs) || LocalVariableTypesCalculator.isUndefinedIdent(rhs)) {
            return new RuntimeNode(parent, request, lhs, rhs);
        }
        return parent;
    }

    private static boolean isUndefinedIdent(Expression expr) {
        return expr instanceof IdentNode && "undefined".equals(((IdentNode)expr).getName());
    }

    private boolean identIsDeadAndHasNoLiveConversions(IdentNode identNode) {
        LocalVariableConversion conv = this.localVariableConversions.get(identNode);
        return conv == null || !conv.isLive();
    }

    private void onAssignment(IdentNode identNode, LvarType type) {
        LvarType finalType;
        Symbol symbol = identNode.getSymbol();
        assert (symbol != null) : identNode.getName();
        if (!symbol.isBytecodeLocal()) {
            return;
        }
        assert (type != null);
        if (type == LvarType.UNDEFINED && this.getLocalVariableType(symbol) != LvarType.UNDEFINED) {
            finalType = LvarType.OBJECT;
            symbol.setFlag(8192);
        } else {
            finalType = type;
        }
        this.setType(symbol, finalType);
        this.setIdentifierLvarType(identNode, finalType);
        this.jumpToCatchBlock(identNode);
    }

    private void onSelfAssignment(IdentNode identNode, LvarType type) {
        Symbol symbol = identNode.getSymbol();
        assert (symbol != null) : identNode.getName();
        if (!symbol.isBytecodeLocal()) {
            return;
        }
        assert (type != null && type != LvarType.UNDEFINED && type != LvarType.BOOLEAN);
        this.setType(symbol, type);
        this.jumpToCatchBlock(identNode);
    }

    private void resetJoinPoint(Label label) {
        this.jumpTargets.remove(label);
    }

    private void setCompilerConstantAsObject(FunctionNode functionNode, CompilerConstants cc) {
        Symbol symbol = LocalVariableTypesCalculator.getCompilerConstantSymbol(functionNode, cc);
        this.setType(symbol, LvarType.OBJECT);
        this.symbolIsUsed(symbol);
    }

    private static Symbol getCompilerConstantSymbol(FunctionNode functionNode, CompilerConstants cc) {
        return functionNode.getBody().getExistingSymbol(cc.symbolName());
    }

    private void setConversion(JoinPredecessor node, Map<Symbol, LvarType> branchLvarTypes, Map<Symbol, LvarType> joinLvarTypes) {
        if (node == null) {
            return;
        }
        if (branchLvarTypes.isEmpty() || joinLvarTypes.isEmpty()) {
            this.localVariableConversions.remove(node);
        }
        LocalVariableConversion conversion = null;
        if (node instanceof IdentNode) {
            Symbol symbol = ((IdentNode)node).getSymbol();
            conversion = this.createConversion(symbol, branchLvarTypes.get(symbol), joinLvarTypes, null);
        } else {
            for (Map.Entry<Symbol, LvarType> entry : branchLvarTypes.entrySet()) {
                Symbol symbol = entry.getKey();
                LvarType branchLvarType = entry.getValue();
                conversion = this.createConversion(symbol, branchLvarType, joinLvarTypes, conversion);
            }
        }
        if (conversion != null) {
            this.localVariableConversions.put(node, conversion);
        } else {
            this.localVariableConversions.remove(node);
        }
    }

    private void setIdentifierLvarType(IdentNode identNode, LvarType type) {
        assert (type != null);
        this.identifierLvarTypes.put(identNode, type);
    }

    private void setType(Symbol symbol, LvarType type) {
        if (this.getLocalVariableTypeOrNull(symbol) == type) {
            return;
        }
        assert (symbol.hasSlot());
        assert (!symbol.isGlobal());
        this.cloneOrNewLocalVariableTypes();
        this.localVariableTypes.put(symbol, type);
    }

    private void cloneOrNewLocalVariableTypes() {
        this.localVariableTypes = this.localVariableTypes.isEmpty() ? new HashMap() : LocalVariableTypesCalculator.cloneMap(this.localVariableTypes);
    }

    private void invalidateSymbol(Symbol symbol) {
        this.localVariableTypes.remove(symbol);
        this.invalidatedSymbols.add(symbol);
    }

    private void symbolIsUsed(Symbol symbol) {
        LocalVariableTypesCalculator.symbolIsUsed(symbol, this.getLocalVariableType(symbol));
    }

    static {
        for (LvarType lvarType : LvarType.values()) {
            TO_LVAR_TYPE.put(lvarType.type, lvarType);
        }
    }

    private static class SymbolConversions {
        private static final byte I2D = 1;
        private static final byte I2O = 2;
        private static final byte D2O = 4;
        private byte conversions;

        private SymbolConversions() {
        }

        void recordConversion(LvarType from, LvarType to) {
            switch (from) {
                case UNDEFINED: {
                    return;
                }
                case INT: 
                case BOOLEAN: {
                    switch (to) {
                        case DOUBLE: {
                            this.recordConversion((byte)1);
                            return;
                        }
                        case OBJECT: {
                            this.recordConversion((byte)2);
                            return;
                        }
                    }
                    SymbolConversions.illegalConversion(from, to);
                    return;
                }
                case DOUBLE: {
                    if (to == LvarType.OBJECT) {
                        this.recordConversion((byte)4);
                    }
                    return;
                }
            }
            SymbolConversions.illegalConversion(from, to);
        }

        private static void illegalConversion(LvarType from, LvarType to) {
            throw new AssertionError((Object)("Invalid conversion from " + (Object)((Object)from) + " to " + (Object)((Object)to)));
        }

        void recordConversion(byte convFlag) {
            this.conversions = (byte)(this.conversions | convFlag);
        }

        boolean hasConversion(byte convFlag) {
            return (this.conversions & convFlag) != 0;
        }

        void calculateTypeLiveness(Symbol symbol) {
            if (symbol.hasSlotFor(Type.OBJECT)) {
                if (this.hasConversion((byte)4)) {
                    symbol.setHasSlotFor(Type.NUMBER);
                }
                if (this.hasConversion((byte)2)) {
                    symbol.setHasSlotFor(Type.INT);
                }
            }
            if (symbol.hasSlotFor(Type.NUMBER) && this.hasConversion((byte)1)) {
                symbol.setHasSlotFor(Type.INT);
            }
        }
    }

    private static class TypeHolderExpression
    extends Expression {
        private static final long serialVersionUID = 1L;
        private final Type type;

        TypeHolderExpression(Type type) {
            super(0L, 0, 0);
            this.type = type;
        }

        @Override
        public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
            throw new AssertionError();
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public void toString(StringBuilder sb, boolean printType) {
            throw new AssertionError();
        }
    }

    private static enum LvarType {
        UNDEFINED(Type.UNDEFINED),
        BOOLEAN(Type.BOOLEAN),
        INT(Type.INT),
        DOUBLE(Type.NUMBER),
        OBJECT(Type.OBJECT);

        private final Type type;
        private final TypeHolderExpression typeExpression;

        private LvarType(Type type) {
            this.type = type;
            this.typeExpression = new TypeHolderExpression(type);
        }
    }

    private static class JumpTarget {
        private final List<JumpOrigin> origins = new LinkedList<JumpOrigin>();
        private Map<Symbol, LvarType> types = Collections.emptyMap();

        private JumpTarget() {
        }

        void addOrigin(JoinPredecessor originNode, Map<Symbol, LvarType> originTypes, LocalVariableTypesCalculator calc) {
            this.origins.add(new JumpOrigin(originNode, originTypes));
            this.types = calc.getUnionTypes(this.types, originTypes);
        }
    }

    private static class JumpOrigin {
        final JoinPredecessor node;
        final Map<Symbol, LvarType> types;

        JumpOrigin(JoinPredecessor node, Map<Symbol, LvarType> types) {
            this.node = node;
            this.types = types;
        }
    }
}

