/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.Namespace;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.GetSplitState;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.JumpStatement;
import jdk.nashorn.internal.ir.JumpToInlinedFinally;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.SetSplitState;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.SplitReturn;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;

final class SplitIntoFunctions
extends NodeVisitor<BlockLexicalContext> {
    private static final int FALLTHROUGH_STATE = -1;
    private static final int RETURN_STATE = 0;
    private static final int BREAK_STATE = 1;
    private static final int FIRST_JUMP_STATE = 2;
    private static final String THIS_NAME = CompilerConstants.THIS.symbolName();
    private static final String RETURN_NAME = CompilerConstants.RETURN.symbolName();
    private static final String RETURN_PARAM_NAME = RETURN_NAME + "-in";
    private final Deque<FunctionState> functionStates = new ArrayDeque<FunctionState>();
    private final Deque<SplitState> splitStates = new ArrayDeque<SplitState>();
    private final Namespace namespace;
    private boolean artificialBlock = false;
    private int nextFunctionId = -2;

    public SplitIntoFunctions(Compiler compiler) {
        super(new BlockLexicalContext(){

            @Override
            protected Block afterSetStatements(Block block) {
                for (Statement stmt : block.getStatements()) {
                    assert (!(stmt instanceof SplitNode));
                }
                return block;
            }
        });
        this.namespace = new Namespace(compiler.getScriptEnvironment().getNamespace());
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        this.functionStates.push(new FunctionState(functionNode));
        return true;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        this.functionStates.pop();
        return functionNode;
    }

    @Override
    protected Node leaveDefault(Node node) {
        if (node instanceof Statement) {
            this.appendStatement((Statement)node);
        }
        return node;
    }

    @Override
    public boolean enterSplitNode(SplitNode splitNode) {
        ++this.getCurrentFunctionState().splitDepth;
        this.splitStates.push(new SplitState(splitNode));
        return true;
    }

    @Override
    public Node leaveSplitNode(SplitNode splitNode) {
        Statement splitStateHandler;
        SplitState parentSplit;
        FunctionState fnState = this.getCurrentFunctionState();
        String name = splitNode.getName();
        Block body = splitNode.getBody();
        int firstLineNumber = body.getFirstStatementLineNumber();
        long token = body.getToken();
        int finish = body.getFinish();
        FunctionNode originalFn = fnState.fn;
        assert (originalFn == ((BlockLexicalContext)this.lc).getCurrentFunction());
        boolean isProgram = originalFn.isProgram();
        long newFnToken = Token.toDesc(TokenType.FUNCTION, this.nextFunctionId--, 0);
        FunctionNode fn = new FunctionNode(originalFn.getSource(), body.getFirstStatementLineNumber(), newFnToken, finish, 0L, this.namespace, SplitIntoFunctions.createIdent(name), originalFn.getName() + "$" + name, isProgram ? Collections.singletonList(SplitIntoFunctions.createReturnParamIdent()) : Collections.emptyList(), FunctionNode.Kind.NORMAL, 529).setBody(this.lc, body).setCompileUnit(this.lc, splitNode.getCompileUnit());
        IdentNode thisIdent = SplitIntoFunctions.createIdent(THIS_NAME);
        CallNode callNode = new CallNode(firstLineNumber, token, finish, new AccessNode(0L, 0, (Expression)fn, "call"), isProgram ? Arrays.asList(thisIdent, SplitIntoFunctions.createReturnIdent()) : Collections.singletonList(thisIdent), false);
        SplitState splitState = this.splitStates.pop();
        --fnState.splitDepth;
        boolean hasReturn = splitState.hasReturn;
        if (hasReturn && fnState.splitDepth > 0 && (parentSplit = this.splitStates.peek()) != null) {
            parentSplit.hasReturn = true;
        }
        Expression callWithReturn = hasReturn || isProgram ? new BinaryNode(Token.recast(token, TokenType.ASSIGN), SplitIntoFunctions.createReturnIdent(), callNode) : callNode;
        this.appendStatement(new ExpressionStatement(firstLineNumber, token, finish, callWithReturn));
        List<JumpStatement> jumpStatements = splitState.jumpStatements;
        int jumpCount = jumpStatements.size();
        if (jumpCount > 0) {
            ArrayList<CaseNode> cases = new ArrayList<CaseNode>(jumpCount + (hasReturn ? 1 : 0));
            if (hasReturn) {
                SplitIntoFunctions.addCase(cases, 0, SplitIntoFunctions.createReturnFromSplit());
            }
            int i = 2;
            for (JumpStatement jump : jumpStatements) {
                SplitIntoFunctions.addCase(cases, i++, this.enblockAndVisit(jump));
            }
            splitStateHandler = new SwitchNode(-1, token, finish, GetSplitState.INSTANCE, cases, null);
        } else {
            splitStateHandler = null;
        }
        if (splitState.hasBreak) {
            splitStateHandler = SplitIntoFunctions.makeIfStateEquals(firstLineNumber, token, finish, 1, this.enblockAndVisit(new BreakNode(-1, token, finish, null)), splitStateHandler);
        }
        if (hasReturn && jumpCount == 0) {
            splitStateHandler = SplitIntoFunctions.makeIfStateEquals(-1, token, finish, 0, SplitIntoFunctions.createReturnFromSplit(), splitStateHandler);
        }
        if (splitStateHandler != null) {
            this.appendStatement(splitStateHandler);
        }
        return splitNode;
    }

    private static void addCase(List<CaseNode> cases, int i, Block body) {
        cases.add(new CaseNode(0L, 0, SplitIntoFunctions.intLiteral(i), body));
    }

    private static LiteralNode<Number> intLiteral(int i) {
        return LiteralNode.newInstance(0L, 0, i);
    }

    private static Block createReturnFromSplit() {
        return new Block(0L, 0, SplitIntoFunctions.createReturnReturn());
    }

    private static ReturnNode createReturnReturn() {
        return new ReturnNode(-1, 0L, 0, SplitIntoFunctions.createReturnIdent());
    }

    private static IdentNode createReturnIdent() {
        return SplitIntoFunctions.createIdent(RETURN_NAME);
    }

    private static IdentNode createReturnParamIdent() {
        return SplitIntoFunctions.createIdent(RETURN_PARAM_NAME);
    }

    private static IdentNode createIdent(String name) {
        return new IdentNode(0L, 0, name);
    }

    private Block enblockAndVisit(JumpStatement jump) {
        this.artificialBlock = true;
        Block block = (Block)new Block(0L, 0, jump).accept(this);
        this.artificialBlock = false;
        return block;
    }

    private static IfNode makeIfStateEquals(int lineNumber, long token, int finish, int value, Block pass, Statement fail) {
        return new IfNode(lineNumber, token, finish, new BinaryNode(Token.recast(token, TokenType.EQ_STRICT), GetSplitState.INSTANCE, SplitIntoFunctions.intLiteral(value)), pass, fail == null ? null : new Block(0L, 0, fail));
    }

    @Override
    public boolean enterVarNode(VarNode varNode) {
        if (!this.inSplitNode()) {
            return super.enterVarNode(varNode);
        }
        assert (!varNode.isBlockScoped());
        Expression init = varNode.getInit();
        this.getCurrentFunctionState().varStatements.add(varNode.setInit(null));
        if (init != null) {
            long token = Token.recast(varNode.getToken(), TokenType.ASSIGN);
            new ExpressionStatement(varNode.getLineNumber(), token, varNode.getFinish(), new BinaryNode(token, varNode.getName(), varNode.getInit())).accept(this);
        }
        return false;
    }

    @Override
    public Node leaveBlock(Block block) {
        if (!this.artificialBlock) {
            if (((BlockLexicalContext)this.lc).isFunctionBody()) {
                ((BlockLexicalContext)this.lc).prependStatements(this.getCurrentFunctionState().varStatements);
            } else if (((BlockLexicalContext)this.lc).isSplitBody()) {
                this.appendSplitReturn(-1, -1);
                if (this.getCurrentFunctionState().fn.isProgram()) {
                    ((BlockLexicalContext)this.lc).prependStatement(new ExpressionStatement(-1, 0L, 0, new BinaryNode(Token.toDesc(TokenType.ASSIGN, 0, 0), SplitIntoFunctions.createReturnIdent(), SplitIntoFunctions.createReturnParamIdent())));
                }
            }
        }
        return block;
    }

    @Override
    public Node leaveBreakNode(BreakNode breakNode) {
        return this.leaveJumpNode(breakNode);
    }

    @Override
    public Node leaveContinueNode(ContinueNode continueNode) {
        return this.leaveJumpNode(continueNode);
    }

    @Override
    public Node leaveJumpToInlinedFinally(JumpToInlinedFinally jumpToInlinedFinally) {
        return this.leaveJumpNode(jumpToInlinedFinally);
    }

    private JumpStatement leaveJumpNode(JumpStatement jump) {
        if (this.inSplitNode()) {
            SplitState splitState = this.getCurrentSplitState();
            SplitNode splitNode = splitState.splitNode;
            if (((BlockLexicalContext)this.lc).isExternalTarget(splitNode, jump.getTarget(this.lc))) {
                this.appendSplitReturn(splitState.getSplitStateIndex(jump), jump.getLineNumber());
                return jump;
            }
        }
        this.appendStatement(jump);
        return jump;
    }

    private void appendSplitReturn(int splitState, int lineNumber) {
        this.appendStatement(new SetSplitState(splitState, lineNumber));
        if (this.getCurrentFunctionState().fn.isProgram()) {
            this.appendStatement(SplitIntoFunctions.createReturnReturn());
        } else {
            this.appendStatement(SplitReturn.INSTANCE);
        }
    }

    @Override
    public Node leaveReturnNode(ReturnNode returnNode) {
        if (this.inSplitNode()) {
            this.appendStatement(new SetSplitState(0, returnNode.getLineNumber()));
            this.getCurrentSplitState().hasReturn = true;
        }
        this.appendStatement(returnNode);
        return returnNode;
    }

    private void appendStatement(Statement statement) {
        ((BlockLexicalContext)this.lc).appendStatement(statement);
    }

    private boolean inSplitNode() {
        return this.getCurrentFunctionState().splitDepth > 0;
    }

    private FunctionState getCurrentFunctionState() {
        return this.functionStates.peek();
    }

    private SplitState getCurrentSplitState() {
        return this.splitStates.peek();
    }

    private static class SplitState {
        final SplitNode splitNode;
        boolean hasReturn;
        boolean hasBreak;
        final List<JumpStatement> jumpStatements = new ArrayList<JumpStatement>();

        int getSplitStateIndex(JumpStatement jump) {
            if (jump instanceof BreakNode && jump.getLabelName() == null) {
                this.hasBreak = true;
                return 1;
            }
            int i = 0;
            for (JumpStatement exJump : this.jumpStatements) {
                if (jump.getClass() == exJump.getClass() && Objects.equals(jump.getLabelName(), exJump.getLabelName())) {
                    return i + 2;
                }
                ++i;
            }
            this.jumpStatements.add(jump);
            return i + 2;
        }

        SplitState(SplitNode splitNode) {
            this.splitNode = splitNode;
        }
    }

    private static class FunctionState {
        final FunctionNode fn;
        final List<Statement> varStatements = new ArrayList<Statement>();
        int splitDepth;

        FunctionState(FunctionNode fn) {
            this.fn = fn;
        }
    }
}

