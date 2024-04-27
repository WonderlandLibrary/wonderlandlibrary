/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;

public abstract class Node
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 1L;
    public static final int NO_LINE_NUMBER = -1;
    public static final long NO_TOKEN = 0L;
    public static final int NO_FINISH = 0;
    protected final int start;
    protected int finish;
    private final long token;

    public Node(long token, int finish) {
        this.token = token;
        this.start = Token.descPosition(token);
        this.finish = finish;
    }

    protected Node(long token, int start, int finish) {
        this.start = start;
        this.finish = finish;
        this.token = token;
    }

    protected Node(Node node) {
        this.token = node.token;
        this.start = node.start;
        this.finish = node.finish;
    }

    public boolean isLoop() {
        return false;
    }

    public boolean isAssignment() {
        return false;
    }

    public Node ensureUniqueLabels(LexicalContext lc) {
        return this;
    }

    public abstract Node accept(NodeVisitor<? extends LexicalContext> var1);

    public final String toString() {
        return this.toString(true);
    }

    public final String toString(boolean includeTypeInfo) {
        StringBuilder sb = new StringBuilder();
        this.toString(sb, includeTypeInfo);
        return sb.toString();
    }

    public void toString(StringBuilder sb) {
        this.toString(sb, true);
    }

    public abstract void toString(StringBuilder var1, boolean var2);

    public int getFinish() {
        return this.finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getStart() {
        return this.start;
    }

    protected Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError((Object)e);
        }
    }

    public final boolean equals(Object other) {
        return this == other;
    }

    public final int hashCode() {
        return Long.hashCode(this.token);
    }

    public int position() {
        return Token.descPosition(this.token);
    }

    public int length() {
        return Token.descLength(this.token);
    }

    public TokenType tokenType() {
        return Token.descType(this.token);
    }

    public boolean isTokenType(TokenType type) {
        return this.tokenType() == type;
    }

    public long getToken() {
        return this.token;
    }

    static <T extends Node> List<T> accept(NodeVisitor<? extends LexicalContext> visitor, List<T> list) {
        int size = list.size();
        if (size == 0) {
            return list;
        }
        ArrayList<Node> newList = null;
        for (int i = 0; i < size; ++i) {
            Node newNode;
            Node node = (Node)list.get(i);
            Node node2 = newNode = node == null ? null : node.accept(visitor);
            if (newNode != node) {
                if (newList == null) {
                    newList = new ArrayList<Node>(size);
                    for (int j = 0; j < i; ++j) {
                        newList.add((Node)list.get(j));
                    }
                }
                newList.add(newNode);
                continue;
            }
            if (newList == null) continue;
            newList.add(node);
        }
        return newList == null ? list : newList;
    }

    static <T extends LexicalContextNode> T replaceInLexicalContext(LexicalContext lc, T oldNode, T newNode) {
        if (lc != null) {
            lc.replace(oldNode, newNode);
        }
        return newNode;
    }
}

