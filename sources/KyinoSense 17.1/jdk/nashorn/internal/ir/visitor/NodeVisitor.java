/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.EmptyNode;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;

public abstract class NodeVisitor<T extends LexicalContext> {
    protected final T lc;

    public NodeVisitor(T lc) {
        this.lc = lc;
    }

    public T getLexicalContext() {
        return this.lc;
    }

    protected boolean enterDefault(Node node) {
        return true;
    }

    protected Node leaveDefault(Node node) {
        return node;
    }

    public boolean enterAccessNode(AccessNode accessNode) {
        return this.enterDefault(accessNode);
    }

    public Node leaveAccessNode(AccessNode accessNode) {
        return this.leaveDefault(accessNode);
    }

    public boolean enterBlock(Block block) {
        return this.enterDefault(block);
    }

    public Node leaveBlock(Block block) {
        return this.leaveDefault(block);
    }

    public boolean enterBinaryNode(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveBinaryNode(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterBreakNode(BreakNode breakNode) {
        return this.enterDefault(breakNode);
    }

    public Node leaveBreakNode(BreakNode breakNode) {
        return this.leaveDefault(breakNode);
    }

    public boolean enterCallNode(CallNode callNode) {
        return this.enterDefault(callNode);
    }

    public Node leaveCallNode(CallNode callNode) {
        return this.leaveDefault(callNode);
    }

    public boolean enterCaseNode(CaseNode caseNode) {
        return this.enterDefault(caseNode);
    }

    public Node leaveCaseNode(CaseNode caseNode) {
        return this.leaveDefault(caseNode);
    }

    public boolean enterCatchNode(CatchNode catchNode) {
        return this.enterDefault(catchNode);
    }

    public Node leaveCatchNode(CatchNode catchNode) {
        return this.leaveDefault(catchNode);
    }

    public boolean enterContinueNode(ContinueNode continueNode) {
        return this.enterDefault(continueNode);
    }

    public Node leaveContinueNode(ContinueNode continueNode) {
        return this.leaveDefault(continueNode);
    }

    public boolean enterEmptyNode(EmptyNode emptyNode) {
        return this.enterDefault(emptyNode);
    }

    public Node leaveEmptyNode(EmptyNode emptyNode) {
        return this.leaveDefault(emptyNode);
    }

    public boolean enterExpressionStatement(ExpressionStatement expressionStatement) {
        return this.enterDefault(expressionStatement);
    }

    public Node leaveExpressionStatement(ExpressionStatement expressionStatement) {
        return this.leaveDefault(expressionStatement);
    }

    public boolean enterBlockStatement(BlockStatement blockStatement) {
        return this.enterDefault(blockStatement);
    }

    public Node leaveBlockStatement(BlockStatement blockStatement) {
        return this.leaveDefault(blockStatement);
    }

    public boolean enterForNode(ForNode forNode) {
        return this.enterDefault(forNode);
    }

    public Node leaveForNode(ForNode forNode) {
        return this.leaveDefault(forNode);
    }

    public boolean enterFunctionNode(FunctionNode functionNode) {
        return this.enterDefault(functionNode);
    }

    public Node leaveFunctionNode(FunctionNode functionNode) {
        return this.leaveDefault(functionNode);
    }

    public boolean enterGetSplitState(GetSplitState getSplitState) {
        return this.enterDefault(getSplitState);
    }

    public Node leaveGetSplitState(GetSplitState getSplitState) {
        return this.leaveDefault(getSplitState);
    }

    public boolean enterIdentNode(IdentNode identNode) {
        return this.enterDefault(identNode);
    }

    public Node leaveIdentNode(IdentNode identNode) {
        return this.leaveDefault(identNode);
    }

    public boolean enterIfNode(IfNode ifNode) {
        return this.enterDefault(ifNode);
    }

    public Node leaveIfNode(IfNode ifNode) {
        return this.leaveDefault(ifNode);
    }

    public boolean enterIndexNode(IndexNode indexNode) {
        return this.enterDefault(indexNode);
    }

    public Node leaveIndexNode(IndexNode indexNode) {
        return this.leaveDefault(indexNode);
    }

    public boolean enterJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return this.enterDefault(jumpToInlinedFinally);
    }

    public Node leaveJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return this.leaveDefault(jumpToInlinedFinally);
    }

    public boolean enterLabelNode(LabelNode labelNode) {
        return this.enterDefault(labelNode);
    }

    public Node leaveLabelNode(LabelNode labelNode) {
        return this.leaveDefault(labelNode);
    }

    public boolean enterLiteralNode(LiteralNode<?> literalNode) {
        return this.enterDefault(literalNode);
    }

    public Node leaveLiteralNode(LiteralNode<?> literalNode) {
        return this.leaveDefault(literalNode);
    }

    public boolean enterObjectNode(ObjectNode objectNode) {
        return this.enterDefault(objectNode);
    }

    public Node leaveObjectNode(ObjectNode objectNode) {
        return this.leaveDefault(objectNode);
    }

    public boolean enterPropertyNode(PropertyNode propertyNode) {
        return this.enterDefault(propertyNode);
    }

    public Node leavePropertyNode(PropertyNode propertyNode) {
        return this.leaveDefault(propertyNode);
    }

    public boolean enterReturnNode(ReturnNode returnNode) {
        return this.enterDefault(returnNode);
    }

    public Node leaveReturnNode(ReturnNode returnNode) {
        return this.leaveDefault(returnNode);
    }

    public boolean enterRuntimeNode(RuntimeNode runtimeNode) {
        return this.enterDefault(runtimeNode);
    }

    public Node leaveRuntimeNode(RuntimeNode runtimeNode) {
        return this.leaveDefault(runtimeNode);
    }

    public boolean enterSetSplitState(SetSplitState setSplitState) {
        return this.enterDefault(setSplitState);
    }

    public Node leaveSetSplitState(SetSplitState setSplitState) {
        return this.leaveDefault(setSplitState);
    }

    public boolean enterSplitNode(SplitNode splitNode) {
        return this.enterDefault(splitNode);
    }

    public Node leaveSplitNode(SplitNode splitNode) {
        return this.leaveDefault(splitNode);
    }

    public boolean enterSplitReturn(SplitReturn splitReturn) {
        return this.enterDefault(splitReturn);
    }

    public Node leaveSplitReturn(SplitReturn splitReturn) {
        return this.leaveDefault(splitReturn);
    }

    public boolean enterSwitchNode(SwitchNode switchNode) {
        return this.enterDefault(switchNode);
    }

    public Node leaveSwitchNode(SwitchNode switchNode) {
        return this.leaveDefault(switchNode);
    }

    public boolean enterTernaryNode(TernaryNode ternaryNode) {
        return this.enterDefault(ternaryNode);
    }

    public Node leaveTernaryNode(TernaryNode ternaryNode) {
        return this.leaveDefault(ternaryNode);
    }

    public boolean enterThrowNode(ThrowNode throwNode) {
        return this.enterDefault(throwNode);
    }

    public Node leaveThrowNode(ThrowNode throwNode) {
        return this.leaveDefault(throwNode);
    }

    public boolean enterTryNode(TryNode tryNode) {
        return this.enterDefault(tryNode);
    }

    public Node leaveTryNode(TryNode tryNode) {
        return this.leaveDefault(tryNode);
    }

    public boolean enterUnaryNode(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveUnaryNode(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterJoinPredecessorExpression(JoinPredecessorExpression expr) {
        return this.enterDefault(expr);
    }

    public Node leaveJoinPredecessorExpression(JoinPredecessorExpression expr) {
        return this.leaveDefault(expr);
    }

    public boolean enterVarNode(VarNode varNode) {
        return this.enterDefault(varNode);
    }

    public Node leaveVarNode(VarNode varNode) {
        return this.leaveDefault(varNode);
    }

    public boolean enterWhileNode(WhileNode whileNode) {
        return this.enterDefault(whileNode);
    }

    public Node leaveWhileNode(WhileNode whileNode) {
        return this.leaveDefault(whileNode);
    }

    public boolean enterWithNode(WithNode withNode) {
        return this.enterDefault(withNode);
    }

    public Node leaveWithNode(WithNode withNode) {
        return this.leaveDefault(withNode);
    }
}

