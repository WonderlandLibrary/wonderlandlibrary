/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.debug;

import java.util.List;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BlockStatement;
import jdk.nashorn.internal.ir.BreakNode;
import jdk.nashorn.internal.ir.CaseNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.ContinueNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.ExpressionStatement;
import jdk.nashorn.internal.ir.ForNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.JoinPredecessorExpression;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.ThrowNode;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.WhileNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

public final class PrintVisitor
extends SimpleNodeVisitor {
    private static final int TABWIDTH = 4;
    private final StringBuilder sb;
    private int indent;
    private final String EOLN = System.lineSeparator();
    private final boolean printLineNumbers;
    private final boolean printTypes;
    private int lastLineNumber = -1;

    public PrintVisitor() {
        this(true, true);
    }

    public PrintVisitor(boolean printLineNumbers, boolean printTypes) {
        this.sb = new StringBuilder();
        this.printLineNumbers = printLineNumbers;
        this.printTypes = printTypes;
    }

    public PrintVisitor(Node root) {
        this(root, true, true);
    }

    public PrintVisitor(Node root, boolean printLineNumbers, boolean printTypes) {
        this(printLineNumbers, printTypes);
        this.visit(root);
    }

    private void visit(Node root) {
        root.accept(this);
    }

    public String toString() {
        return this.sb.append(this.EOLN).toString();
    }

    private void indent() {
        for (int i = this.indent; i > 0; --i) {
            this.sb.append(' ');
        }
    }

    @Override
    public boolean enterDefault(Node node) {
        node.toString(this.sb, this.printTypes);
        return false;
    }

    @Override
    public boolean enterContinueNode(ContinueNode node) {
        node.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(node);
        return false;
    }

    @Override
    public boolean enterBreakNode(BreakNode node) {
        node.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(node);
        return false;
    }

    @Override
    public boolean enterThrowNode(ThrowNode node) {
        node.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(node);
        return false;
    }

    @Override
    public boolean enterBlock(Block block) {
        this.sb.append(' ');
        this.sb.append('{');
        this.indent += 4;
        List<Statement> statements = block.getStatements();
        for (Statement statement : statements) {
            if (this.printLineNumbers) {
                int lineNumber = statement.getLineNumber();
                this.sb.append('\n');
                if (lineNumber != this.lastLineNumber) {
                    this.indent();
                    this.sb.append("[|").append(lineNumber).append("|];").append('\n');
                }
                this.lastLineNumber = lineNumber;
            }
            this.indent();
            statement.accept(this);
            int lastIndex = this.sb.length() - 1;
            char lastChar = this.sb.charAt(lastIndex);
            while (Character.isWhitespace(lastChar) && lastIndex >= 0) {
                lastChar = this.sb.charAt(--lastIndex);
            }
            if (lastChar != '}' && lastChar != ';') {
                this.sb.append(';');
            }
            if (statement.hasGoto()) {
                this.sb.append(" [GOTO]");
            }
            if (!statement.isTerminal()) continue;
            this.sb.append(" [TERMINAL]");
        }
        this.indent -= 4;
        this.sb.append(this.EOLN);
        this.indent();
        this.sb.append('}');
        this.printLocalVariableConversion(block);
        return false;
    }

    @Override
    public boolean enterBlockStatement(BlockStatement statement) {
        statement.getBlock().accept(this);
        return false;
    }

    @Override
    public boolean enterBinaryNode(BinaryNode binaryNode) {
        binaryNode.lhs().accept(this);
        this.sb.append(' ');
        this.sb.append((Object)binaryNode.tokenType());
        this.sb.append(' ');
        binaryNode.rhs().accept(this);
        return false;
    }

    @Override
    public boolean enterJoinPredecessorExpression(JoinPredecessorExpression expr) {
        expr.getExpression().accept(this);
        this.printLocalVariableConversion(expr);
        return false;
    }

    @Override
    public boolean enterIdentNode(IdentNode identNode) {
        identNode.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(identNode);
        return true;
    }

    private void printLocalVariableConversion(JoinPredecessor joinPredecessor) {
        LocalVariableConversion.toString(joinPredecessor.getLocalVariableConversion(), this.sb);
    }

    @Override
    public boolean enterUnaryNode(final UnaryNode unaryNode) {
        unaryNode.toString(this.sb, new Runnable(){

            @Override
            public void run() {
                unaryNode.getExpression().accept(PrintVisitor.this);
            }
        }, this.printTypes);
        return false;
    }

    @Override
    public boolean enterExpressionStatement(ExpressionStatement expressionStatement) {
        expressionStatement.getExpression().accept(this);
        return false;
    }

    @Override
    public boolean enterForNode(ForNode forNode) {
        forNode.toString(this.sb, this.printTypes);
        forNode.getBody().accept(this);
        return false;
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        functionNode.toString(this.sb, this.printTypes);
        this.enterBlock(functionNode.getBody());
        return false;
    }

    @Override
    public boolean enterIfNode(IfNode ifNode) {
        ifNode.toString(this.sb, this.printTypes);
        ifNode.getPass().accept(this);
        Block fail = ifNode.getFail();
        if (fail != null) {
            this.sb.append(" else ");
            fail.accept(this);
        }
        if (ifNode.getLocalVariableConversion() != null) {
            assert (fail == null);
            this.sb.append(" else ");
            this.printLocalVariableConversion(ifNode);
            this.sb.append(";");
        }
        return false;
    }

    @Override
    public boolean enterLabelNode(LabelNode labeledNode) {
        this.indent -= 4;
        this.indent();
        this.indent += 4;
        labeledNode.toString(this.sb, this.printTypes);
        labeledNode.getBody().accept(this);
        this.printLocalVariableConversion(labeledNode);
        return false;
    }

    @Override
    public boolean enterSplitNode(SplitNode splitNode) {
        splitNode.toString(this.sb, this.printTypes);
        this.sb.append(this.EOLN);
        this.indent += 4;
        this.indent();
        return true;
    }

    @Override
    public Node leaveSplitNode(SplitNode splitNode) {
        this.sb.append("</split>");
        this.sb.append(this.EOLN);
        this.indent -= 4;
        this.indent();
        return splitNode;
    }

    @Override
    public boolean enterSwitchNode(SwitchNode switchNode) {
        switchNode.toString(this.sb, this.printTypes);
        this.sb.append(" {");
        List<CaseNode> cases = switchNode.getCases();
        for (CaseNode caseNode : cases) {
            this.sb.append(this.EOLN);
            this.indent();
            caseNode.toString(this.sb, this.printTypes);
            this.printLocalVariableConversion(caseNode);
            this.indent += 4;
            caseNode.getBody().accept(this);
            this.indent -= 4;
            this.sb.append(this.EOLN);
        }
        if (switchNode.getLocalVariableConversion() != null) {
            this.sb.append(this.EOLN);
            this.indent();
            this.sb.append("default: ");
            this.printLocalVariableConversion(switchNode);
            this.sb.append("{}");
        }
        this.sb.append(this.EOLN);
        this.indent();
        this.sb.append("}");
        return false;
    }

    @Override
    public boolean enterTryNode(TryNode tryNode) {
        tryNode.toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(tryNode);
        tryNode.getBody().accept(this);
        List<Block> catchBlocks = tryNode.getCatchBlocks();
        for (Block catchBlock : catchBlocks) {
            CatchNode catchNode = (CatchNode)catchBlock.getStatements().get(0);
            catchNode.toString(this.sb, this.printTypes);
            catchNode.getBody().accept(this);
        }
        Block finallyBody = tryNode.getFinallyBody();
        if (finallyBody != null) {
            this.sb.append(" finally ");
            finallyBody.accept(this);
        }
        for (Block inlinedFinally : tryNode.getInlinedFinallies()) {
            inlinedFinally.accept(this);
        }
        return false;
    }

    @Override
    public boolean enterVarNode(VarNode varNode) {
        this.sb.append(varNode.isConst() ? "const " : (varNode.isLet() ? "let " : "var "));
        varNode.getName().toString(this.sb, this.printTypes);
        this.printLocalVariableConversion(varNode.getName());
        Expression init = varNode.getInit();
        if (init != null) {
            this.sb.append(" = ");
            init.accept(this);
        }
        return false;
    }

    @Override
    public boolean enterWhileNode(WhileNode whileNode) {
        this.printLocalVariableConversion(whileNode);
        if (whileNode.isDoWhile()) {
            this.sb.append("do");
            whileNode.getBody().accept(this);
            this.sb.append(' ');
            whileNode.toString(this.sb, this.printTypes);
        } else {
            whileNode.toString(this.sb, this.printTypes);
            whileNode.getBody().accept(this);
        }
        return false;
    }

    @Override
    public boolean enterWithNode(WithNode withNode) {
        withNode.toString(this.sb, this.printTypes);
        withNode.getBody().accept(this);
        return false;
    }
}

