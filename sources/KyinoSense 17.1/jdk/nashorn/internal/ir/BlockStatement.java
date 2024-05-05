/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.List;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public class BlockStatement
extends Statement {
    private static final long serialVersionUID = 1L;
    private final Block block;

    public BlockStatement(Block block) {
        this(block.getFirstStatementLineNumber(), block);
    }

    public BlockStatement(int lineNumber, Block block) {
        super(lineNumber, block.getToken(), block.getFinish());
        this.block = block;
    }

    private BlockStatement(BlockStatement blockStatement, Block block) {
        super(blockStatement);
        this.block = block;
    }

    public static BlockStatement createReplacement(Statement stmt, List<Statement> newStmts) {
        return BlockStatement.createReplacement(stmt, stmt.getFinish(), newStmts);
    }

    public static BlockStatement createReplacement(Statement stmt, int finish, List<Statement> newStmts) {
        return new BlockStatement(stmt.getLineNumber(), new Block(stmt.getToken(), finish, newStmts));
    }

    @Override
    public boolean isTerminal() {
        return this.block.isTerminal();
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterBlockStatement(this)) {
            return visitor.leaveBlockStatement(this.setBlock((Block)this.block.accept(visitor)));
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        this.block.toString(sb, printType);
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockStatement setBlock(Block block) {
        if (this.block == block) {
            return this;
        }
        return new BlockStatement(this, block);
    }
}

