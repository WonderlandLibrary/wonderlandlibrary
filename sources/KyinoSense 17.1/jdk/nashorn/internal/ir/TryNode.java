/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextStatement;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public final class TryNode
extends LexicalContextStatement
implements JoinPredecessor {
    private static final long serialVersionUID = 1L;
    private final Block body;
    private final List<Block> catchBlocks;
    private final Block finallyBody;
    private final List<Block> inlinedFinallies;
    private final Symbol exception;
    private final LocalVariableConversion conversion;

    public TryNode(int lineNumber, long token, int finish, Block body, List<Block> catchBlocks, Block finallyBody) {
        super(lineNumber, token, finish);
        this.body = body;
        this.catchBlocks = catchBlocks;
        this.finallyBody = finallyBody;
        this.conversion = null;
        this.inlinedFinallies = Collections.emptyList();
        this.exception = null;
    }

    private TryNode(TryNode tryNode, Block body, List<Block> catchBlocks, Block finallyBody, LocalVariableConversion conversion, List<Block> inlinedFinallies, Symbol exception) {
        super(tryNode);
        this.body = body;
        this.catchBlocks = catchBlocks;
        this.finallyBody = finallyBody;
        this.conversion = conversion;
        this.inlinedFinallies = inlinedFinallies;
        this.exception = exception;
    }

    @Override
    public Node ensureUniqueLabels(LexicalContext lc) {
        return new TryNode(this, this.body, this.catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, this.exception);
    }

    @Override
    public boolean isTerminal() {
        if (this.body.isTerminal()) {
            for (Block catchBlock : this.getCatchBlocks()) {
                if (catchBlock.isTerminal()) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterTryNode(this)) {
            Block newFinallyBody = this.finallyBody == null ? null : (Block)this.finallyBody.accept(visitor);
            Block newBody = (Block)this.body.accept(visitor);
            return visitor.leaveTryNode(this.setBody(lc, newBody).setFinallyBody(lc, newFinallyBody).setCatchBlocks(lc, Node.accept(visitor, this.catchBlocks)).setInlinedFinallies(lc, Node.accept(visitor, this.inlinedFinallies)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        sb.append("try ");
    }

    public Block getBody() {
        return this.body;
    }

    public TryNode setBody(LexicalContext lc, Block body) {
        if (this.body == body) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, body, this.catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, this.exception));
    }

    public List<CatchNode> getCatches() {
        ArrayList<CatchNode> catches = new ArrayList<CatchNode>(this.catchBlocks.size());
        for (Block catchBlock : this.catchBlocks) {
            catches.add(TryNode.getCatchNodeFromBlock(catchBlock));
        }
        return Collections.unmodifiableList(catches);
    }

    private static CatchNode getCatchNodeFromBlock(Block catchBlock) {
        return (CatchNode)catchBlock.getStatements().get(0);
    }

    public List<Block> getCatchBlocks() {
        return Collections.unmodifiableList(this.catchBlocks);
    }

    public TryNode setCatchBlocks(LexicalContext lc, List<Block> catchBlocks) {
        if (this.catchBlocks == catchBlocks) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, this.exception));
    }

    public Symbol getException() {
        return this.exception;
    }

    public TryNode setException(LexicalContext lc, Symbol exception) {
        if (this.exception == exception) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, this.catchBlocks, this.finallyBody, this.conversion, this.inlinedFinallies, exception));
    }

    public Block getFinallyBody() {
        return this.finallyBody;
    }

    public Block getInlinedFinally(String labelName) {
        for (Block inlinedFinally : this.inlinedFinallies) {
            LabelNode labelNode = TryNode.getInlinedFinallyLabelNode(inlinedFinally);
            if (!labelNode.getLabelName().equals(labelName)) continue;
            return labelNode.getBody();
        }
        return null;
    }

    private static LabelNode getInlinedFinallyLabelNode(Block inlinedFinally) {
        return (LabelNode)inlinedFinally.getStatements().get(0);
    }

    public static Block getLabelledInlinedFinallyBlock(Block inlinedFinally) {
        return TryNode.getInlinedFinallyLabelNode(inlinedFinally).getBody();
    }

    public List<Block> getInlinedFinallies() {
        return Collections.unmodifiableList(this.inlinedFinallies);
    }

    public TryNode setFinallyBody(LexicalContext lc, Block finallyBody) {
        if (this.finallyBody == finallyBody) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, this.catchBlocks, finallyBody, this.conversion, this.inlinedFinallies, this.exception));
    }

    public TryNode setInlinedFinallies(LexicalContext lc, List<Block> inlinedFinallies) {
        if (this.inlinedFinallies == inlinedFinallies) {
            return this;
        }
        assert (TryNode.checkInlinedFinallies(inlinedFinallies));
        return Node.replaceInLexicalContext(lc, this, new TryNode(this, this.body, this.catchBlocks, this.finallyBody, this.conversion, inlinedFinallies, this.exception));
    }

    private static boolean checkInlinedFinallies(List<Block> inlinedFinallies) {
        if (!inlinedFinallies.isEmpty()) {
            HashSet<String> labels = new HashSet<String>();
            for (Block inlinedFinally : inlinedFinallies) {
                List<Statement> stmts = inlinedFinally.getStatements();
                assert (stmts.size() == 1);
                LabelNode ln = TryNode.getInlinedFinallyLabelNode(inlinedFinally);
                assert (labels.add(ln.getLabelName()));
            }
        }
        return true;
    }

    @Override
    public JoinPredecessor setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return new TryNode(this, this.body, this.catchBlocks, this.finallyBody, conversion, this.inlinedFinallies, this.exception);
    }

    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }
}

