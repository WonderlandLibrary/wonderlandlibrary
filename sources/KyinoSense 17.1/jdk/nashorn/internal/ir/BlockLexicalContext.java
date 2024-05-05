/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;

public class BlockLexicalContext
extends LexicalContext {
    private final Deque<List<Statement>> sstack = new ArrayDeque<List<Statement>>();
    protected Statement lastStatement;

    @Override
    public <T extends LexicalContextNode> T push(T node) {
        T pushed = super.push(node);
        if (node instanceof Block) {
            this.sstack.push(new ArrayList());
        }
        return pushed;
    }

    protected List<Statement> popStatements() {
        return this.sstack.pop();
    }

    protected Block afterSetStatements(Block block) {
        return block;
    }

    @Override
    public <T extends Node> T pop(T node) {
        Object expected = node;
        if (node instanceof Block) {
            List<Statement> newStatements = this.popStatements();
            expected = ((Block)node).setStatements(this, newStatements);
            expected = this.afterSetStatements((Block)expected);
            if (!this.sstack.isEmpty()) {
                this.lastStatement = BlockLexicalContext.lastStatement(this.sstack.peek());
            }
        }
        return super.pop(expected);
    }

    public void appendStatement(Statement statement) {
        assert (statement != null);
        this.sstack.peek().add(statement);
        this.lastStatement = statement;
    }

    public Node prependStatement(Statement statement) {
        assert (statement != null);
        this.sstack.peek().add(0, statement);
        return statement;
    }

    public void prependStatements(List<Statement> statements) {
        assert (statements != null);
        this.sstack.peek().addAll(0, statements);
    }

    public Statement getLastStatement() {
        return this.lastStatement;
    }

    private static Statement lastStatement(List<Statement> statements) {
        int s = statements.size();
        return s == 0 ? null : statements.get(s - 1);
    }
}

