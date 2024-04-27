/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.Flags;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.Terminal;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

@Immutable
public class Block
extends Node
implements BreakableNode,
Terminal,
Flags<Block> {
    private static final long serialVersionUID = 1L;
    protected final List<Statement> statements;
    protected final Map<String, Symbol> symbols;
    private final Label entryLabel;
    private final Label breakLabel;
    protected final int flags;
    private final LocalVariableConversion conversion;
    public static final int NEEDS_SCOPE = 1;
    public static final int IS_TERMINAL = 4;
    public static final int IS_GLOBAL_SCOPE = 8;

    public Block(long token, int finish, Statement ... statements) {
        super(token, finish);
        this.statements = Arrays.asList(statements);
        this.symbols = new LinkedHashMap<String, Symbol>();
        this.entryLabel = new Label("block_entry");
        this.breakLabel = new Label("block_break");
        int len = statements.length;
        this.flags = len > 0 && statements[len - 1].hasTerminalFlags() ? 4 : 0;
        this.conversion = null;
    }

    public Block(long token, int finish, List<Statement> statements) {
        this(token, finish, statements.toArray(new Statement[statements.size()]));
    }

    private Block(Block block, int finish, List<Statement> statements, int flags, Map<String, Symbol> symbols, LocalVariableConversion conversion) {
        super(block);
        this.statements = statements;
        this.flags = flags;
        this.symbols = new LinkedHashMap<String, Symbol>(symbols);
        this.entryLabel = new Label(block.entryLabel);
        this.breakLabel = new Label(block.breakLabel);
        this.finish = finish;
        this.conversion = conversion;
    }

    public boolean isGlobalScope() {
        return this.getFlag(8);
    }

    public boolean hasSymbols() {
        return !this.symbols.isEmpty();
    }

    public Block replaceSymbols(LexicalContext lc, Map<Symbol, Symbol> replacements) {
        if (this.symbols.isEmpty()) {
            return this;
        }
        LinkedHashMap<String, Symbol> newSymbols = new LinkedHashMap<String, Symbol>(this.symbols);
        for (Map.Entry<String, Symbol> entry : newSymbols.entrySet()) {
            Symbol newSymbol = replacements.get(entry.getValue());
            assert (newSymbol != null) : "Missing replacement for " + entry.getKey();
            entry.setValue(newSymbol);
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags, newSymbols, this.conversion));
    }

    public Block copyWithNewSymbols() {
        return new Block(this, this.finish, this.statements, this.flags, new LinkedHashMap<String, Symbol>(this.symbols), this.conversion);
    }

    @Override
    public Node ensureUniqueLabels(LexicalContext lc) {
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags, this.symbols, this.conversion));
    }

    @Override
    public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterBlock(this)) {
            return visitor.leaveBlock(this.setStatements(lc, Node.accept(visitor, this.statements)));
        }
        return this;
    }

    public List<Symbol> getSymbols() {
        return this.symbols.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<Symbol>(this.symbols.values()));
    }

    public Symbol getExistingSymbol(String name) {
        return this.symbols.get(name);
    }

    public boolean isCatchBlock() {
        return this.statements.size() == 1 && this.statements.get(0) instanceof CatchNode;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        for (Node node : this.statements) {
            node.toString(sb, printType);
            sb.append(';');
        }
    }

    public boolean printSymbols(PrintWriter stream) {
        ArrayList<Symbol> values2 = new ArrayList<Symbol>(this.symbols.values());
        Collections.sort(values2, new Comparator<Symbol>(){

            @Override
            public int compare(Symbol s0, Symbol s1) {
                return s0.getName().compareTo(s1.getName());
            }
        });
        for (Symbol symbol : values2) {
            symbol.print(stream);
        }
        return !values2.isEmpty();
    }

    public Block setIsTerminal(LexicalContext lc, boolean isTerminal) {
        return isTerminal ? this.setFlag(lc, 4) : this.clearFlag(lc, 4);
    }

    @Override
    public int getFlags() {
        return this.flags;
    }

    @Override
    public boolean isTerminal() {
        return this.getFlag(4);
    }

    public Label getEntryLabel() {
        return this.entryLabel;
    }

    @Override
    public Label getBreakLabel() {
        return this.breakLabel;
    }

    @Override
    public Block setLocalVariableConversion(LexicalContext lc, LocalVariableConversion conversion) {
        if (this.conversion == conversion) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags, this.symbols, conversion));
    }

    @Override
    public LocalVariableConversion getLocalVariableConversion() {
        return this.conversion;
    }

    public List<Statement> getStatements() {
        return Collections.unmodifiableList(this.statements);
    }

    public int getStatementCount() {
        return this.statements.size();
    }

    public int getFirstStatementLineNumber() {
        if (this.statements == null || this.statements.isEmpty()) {
            return -1;
        }
        return this.statements.get(0).getLineNumber();
    }

    public Statement getLastStatement() {
        return this.statements.isEmpty() ? null : this.statements.get(this.statements.size() - 1);
    }

    public Block setStatements(LexicalContext lc, List<Statement> statements) {
        if (this.statements == statements) {
            return this;
        }
        int lastFinish = 0;
        if (!statements.isEmpty()) {
            lastFinish = statements.get(statements.size() - 1).getFinish();
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, Math.max(this.finish, lastFinish), statements, this.flags, this.symbols, this.conversion));
    }

    public void putSymbol(Symbol symbol) {
        this.symbols.put(symbol.getName(), symbol);
    }

    public boolean needsScope() {
        return (this.flags & 1) == 1;
    }

    @Override
    public Block setFlags(LexicalContext lc, int flags) {
        if (this.flags == flags) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, flags, this.symbols, this.conversion));
    }

    @Override
    public Block clearFlag(LexicalContext lc, int flag) {
        return this.setFlags(lc, this.flags & ~flag);
    }

    @Override
    public Block setFlag(LexicalContext lc, int flag) {
        return this.setFlags(lc, this.flags | flag);
    }

    @Override
    public boolean getFlag(int flag) {
        return (this.flags & flag) == flag;
    }

    public Block setNeedsScope(LexicalContext lc) {
        if (this.needsScope()) {
            return this;
        }
        return Node.replaceInLexicalContext(lc, this, new Block(this, this.finish, this.statements, this.flags | 1, this.symbols, this.conversion));
    }

    public int nextSlot() {
        int next = 0;
        for (Symbol symbol : this.getSymbols()) {
            if (!symbol.hasSlot()) continue;
            next += symbol.slotCount();
        }
        return next;
    }

    @Override
    public boolean isBreakableWithoutLabel() {
        return false;
    }

    @Override
    public List<Label> getLabels() {
        return Collections.unmodifiableList(Arrays.asList(this.entryLabel, this.breakLabel));
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        return LexicalContextNode.Acceptor.accept(this, visitor);
    }
}

