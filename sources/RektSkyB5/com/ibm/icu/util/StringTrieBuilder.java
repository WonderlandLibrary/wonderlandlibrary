/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StringTrieBuilder {
    private State state = State.ADDING;
    @Deprecated
    protected StringBuilder strings = new StringBuilder();
    private Node root;
    private HashMap<Node, Node> nodes = new HashMap();
    private ValueNode lookupFinalValueNode = new ValueNode();

    @Deprecated
    protected StringTrieBuilder() {
    }

    @Deprecated
    protected void addImpl(CharSequence s2, int value) {
        if (this.state != State.ADDING) {
            throw new IllegalStateException("Cannot add (string, value) pairs after build().");
        }
        if (s2.length() > 65535) {
            throw new IndexOutOfBoundsException("The maximum string length is 0xffff.");
        }
        this.root = this.root == null ? this.createSuffixNode(s2, 0, value) : this.root.add(this, s2, 0, value);
    }

    @Deprecated
    protected final void buildImpl(Option buildOption) {
        switch (this.state) {
            case ADDING: {
                if (this.root == null) {
                    throw new IndexOutOfBoundsException("No (string, value) pairs were added.");
                }
                if (buildOption == Option.FAST) {
                    this.state = State.BUILDING_FAST;
                    break;
                }
                this.state = State.BUILDING_SMALL;
                break;
            }
            case BUILDING_FAST: 
            case BUILDING_SMALL: {
                throw new IllegalStateException("Builder failed and must be clear()ed.");
            }
            case BUILT: {
                return;
            }
        }
        this.root = this.root.register(this);
        this.root.markRightEdgesFirst(-1);
        this.root.write(this);
        this.state = State.BUILT;
    }

    @Deprecated
    protected void clearImpl() {
        this.strings.setLength(0);
        this.nodes.clear();
        this.root = null;
        this.state = State.ADDING;
    }

    private final Node registerNode(Node newNode) {
        if (this.state == State.BUILDING_FAST) {
            return newNode;
        }
        Node oldNode = this.nodes.get(newNode);
        if (oldNode != null) {
            return oldNode;
        }
        oldNode = this.nodes.put(newNode, newNode);
        assert (oldNode == null);
        return newNode;
    }

    private final ValueNode registerFinalValue(int value) {
        this.lookupFinalValueNode.setFinalValue(value);
        Node oldNode = this.nodes.get(this.lookupFinalValueNode);
        if (oldNode != null) {
            return (ValueNode)oldNode;
        }
        ValueNode newNode = new ValueNode(value);
        oldNode = this.nodes.put(newNode, newNode);
        assert (oldNode == null);
        return newNode;
    }

    private ValueNode createSuffixNode(CharSequence s2, int start, int sValue) {
        ValueNode node = this.registerFinalValue(sValue);
        if (start < s2.length()) {
            int offset = this.strings.length();
            this.strings.append(s2, start, s2.length());
            node = new LinearMatchNode(this.strings, offset, s2.length() - start, node);
        }
        return node;
    }

    @Deprecated
    protected abstract boolean matchNodesCanHaveValues();

    @Deprecated
    protected abstract int getMaxBranchLinearSubNodeLength();

    @Deprecated
    protected abstract int getMinLinearMatch();

    @Deprecated
    protected abstract int getMaxLinearMatchLength();

    @Deprecated
    protected abstract int write(int var1);

    @Deprecated
    protected abstract int write(int var1, int var2);

    @Deprecated
    protected abstract int writeValueAndFinal(int var1, boolean var2);

    @Deprecated
    protected abstract int writeValueAndType(boolean var1, int var2, int var3);

    @Deprecated
    protected abstract int writeDeltaTo(int var1);

    private static enum State {
        ADDING,
        BUILDING_FAST,
        BUILDING_SMALL,
        BUILT;

    }

    private static final class BranchHeadNode
    extends ValueNode {
        private int length;
        private Node next;

        public BranchHeadNode(int len, Node subNode) {
            this.length = len;
            this.next = subNode;
        }

        @Override
        public int hashCode() {
            return (0xECCCCBE + this.length) * 37 + this.next.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            BranchHeadNode o2 = (BranchHeadNode)other;
            return this.length == o2.length && this.next == o2.next;
        }

        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.offset = edgeNumber = this.next.markRightEdgesFirst(edgeNumber);
            }
            return edgeNumber;
        }

        @Override
        public void write(StringTrieBuilder builder) {
            this.next.write(builder);
            if (this.length <= builder.getMinLinearMatch()) {
                this.offset = builder.writeValueAndType(this.hasValue, this.value, this.length - 1);
            } else {
                builder.write(this.length - 1);
                this.offset = builder.writeValueAndType(this.hasValue, this.value, 0);
            }
        }
    }

    private static final class SplitBranchNode
    extends BranchNode {
        private char unit;
        private Node lessThan;
        private Node greaterOrEqual;

        public SplitBranchNode(char middleUnit, Node lessThanNode, Node greaterOrEqualNode) {
            this.hash = ((206918985 + middleUnit) * 37 + lessThanNode.hashCode()) * 37 + greaterOrEqualNode.hashCode();
            this.unit = middleUnit;
            this.lessThan = lessThanNode;
            this.greaterOrEqual = greaterOrEqualNode;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            SplitBranchNode o2 = (SplitBranchNode)other;
            return this.unit == o2.unit && this.lessThan == o2.lessThan && this.greaterOrEqual == o2.greaterOrEqual;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.firstEdgeNumber = edgeNumber;
                edgeNumber = this.greaterOrEqual.markRightEdgesFirst(edgeNumber);
                this.offset = edgeNumber = this.lessThan.markRightEdgesFirst(edgeNumber - 1);
            }
            return edgeNumber;
        }

        @Override
        public void write(StringTrieBuilder builder) {
            this.lessThan.writeUnlessInsideRightEdge(this.firstEdgeNumber, this.greaterOrEqual.getOffset(), builder);
            this.greaterOrEqual.write(builder);
            assert (this.lessThan.getOffset() > 0);
            builder.writeDeltaTo(this.lessThan.getOffset());
            this.offset = builder.write(this.unit);
        }
    }

    private static final class ListBranchNode
    extends BranchNode {
        private Node[] equal;
        private int length;
        private int[] values;
        private char[] units;

        public ListBranchNode(int capacity) {
            this.hash = 0x9DDDDD4 + capacity;
            this.equal = new Node[capacity];
            this.values = new int[capacity];
            this.units = new char[capacity];
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            ListBranchNode o2 = (ListBranchNode)other;
            for (int i2 = 0; i2 < this.length; ++i2) {
                if (this.units[i2] == o2.units[i2] && this.values[i2] == o2.values[i2] && this.equal[i2] == o2.equal[i2]) continue;
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.firstEdgeNumber = edgeNumber;
                int step = 0;
                int i2 = this.length;
                do {
                    Node edge;
                    if ((edge = this.equal[--i2]) != null) {
                        edgeNumber = edge.markRightEdgesFirst(edgeNumber - step);
                    }
                    step = 1;
                } while (i2 > 0);
                this.offset = edgeNumber;
            }
            return edgeNumber;
        }

        @Override
        public void write(StringTrieBuilder builder) {
            int rightEdgeNumber;
            int unitNumber = this.length - 1;
            Node rightEdge = this.equal[unitNumber];
            int n2 = rightEdgeNumber = rightEdge == null ? this.firstEdgeNumber : rightEdge.getOffset();
            do {
                if (this.equal[--unitNumber] == null) continue;
                this.equal[unitNumber].writeUnlessInsideRightEdge(this.firstEdgeNumber, rightEdgeNumber, builder);
            } while (unitNumber > 0);
            unitNumber = this.length - 1;
            if (rightEdge == null) {
                builder.writeValueAndFinal(this.values[unitNumber], true);
            } else {
                rightEdge.write(builder);
            }
            this.offset = builder.write(this.units[unitNumber]);
            while (--unitNumber >= 0) {
                boolean isFinal;
                int value;
                if (this.equal[unitNumber] == null) {
                    value = this.values[unitNumber];
                    isFinal = true;
                } else {
                    assert (this.equal[unitNumber].getOffset() > 0);
                    value = this.offset - this.equal[unitNumber].getOffset();
                    isFinal = false;
                }
                builder.writeValueAndFinal(value, isFinal);
                this.offset = builder.write(this.units[unitNumber]);
            }
        }

        public void add(int c2, int value) {
            this.units[this.length] = (char)c2;
            this.equal[this.length] = null;
            this.values[this.length] = value;
            ++this.length;
            this.hash = (this.hash * 37 + c2) * 37 + value;
        }

        public void add(int c2, Node node) {
            this.units[this.length] = (char)c2;
            this.equal[this.length] = node;
            this.values[this.length] = 0;
            ++this.length;
            this.hash = (this.hash * 37 + c2) * 37 + node.hashCode();
        }
    }

    private static abstract class BranchNode
    extends Node {
        protected int hash;
        protected int firstEdgeNumber;

        @Override
        public int hashCode() {
            return this.hash;
        }
    }

    private static final class DynamicBranchNode
    extends ValueNode {
        private StringBuilder chars = new StringBuilder();
        private ArrayList<Node> equal = new ArrayList();

        public void add(char c2, Node node) {
            int i2 = this.find(c2);
            this.chars.insert(i2, c2);
            this.equal.add(i2, node);
        }

        @Override
        public Node add(StringTrieBuilder builder, CharSequence s2, int start, int sValue) {
            char c2;
            int i2;
            if (start == s2.length()) {
                if (this.hasValue) {
                    throw new IllegalArgumentException("Duplicate string.");
                }
                this.setValue(sValue);
                return this;
            }
            if ((i2 = this.find(c2 = s2.charAt(start++))) < this.chars.length() && c2 == this.chars.charAt(i2)) {
                this.equal.set(i2, this.equal.get(i2).add(builder, s2, start, sValue));
            } else {
                this.chars.insert(i2, c2);
                this.equal.add(i2, builder.createSuffixNode(s2, start, sValue));
            }
            return this;
        }

        @Override
        public Node register(StringTrieBuilder builder) {
            BranchHeadNode head;
            Node subNode = this.register(builder, 0, this.chars.length());
            ValueNode result = head = new BranchHeadNode(this.chars.length(), subNode);
            if (this.hasValue) {
                if (builder.matchNodesCanHaveValues()) {
                    head.setValue(this.value);
                } else {
                    result = new IntermediateValueNode(this.value, builder.registerNode(head));
                }
            }
            return builder.registerNode(result);
        }

        private Node register(StringTrieBuilder builder, int start, int limit) {
            int length = limit - start;
            if (length > builder.getMaxBranchLinearSubNodeLength()) {
                int middle = start + length / 2;
                return builder.registerNode(new SplitBranchNode(this.chars.charAt(middle), this.register(builder, start, middle), this.register(builder, middle, limit)));
            }
            ListBranchNode listNode = new ListBranchNode(length);
            do {
                char c2 = this.chars.charAt(start);
                Node node = this.equal.get(start);
                if (node.getClass() == ValueNode.class) {
                    listNode.add((int)c2, ((ValueNode)node).value);
                    continue;
                }
                listNode.add((int)c2, node.register(builder));
            } while (++start < limit);
            return builder.registerNode(listNode);
        }

        private int find(char c2) {
            int start = 0;
            int limit = this.chars.length();
            while (start < limit) {
                int i2 = (start + limit) / 2;
                char middleChar = this.chars.charAt(i2);
                if (c2 < middleChar) {
                    limit = i2;
                    continue;
                }
                if (c2 == middleChar) {
                    return i2;
                }
                start = i2 + 1;
            }
            return start;
        }
    }

    private static final class LinearMatchNode
    extends ValueNode {
        private CharSequence strings;
        private int stringOffset;
        private int length;
        private Node next;
        private int hash;

        public LinearMatchNode(CharSequence builderStrings, int sOffset, int len, Node nextNode) {
            this.strings = builderStrings;
            this.stringOffset = sOffset;
            this.length = len;
            this.next = nextNode;
        }

        @Override
        public int hashCode() {
            return this.hash;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            LinearMatchNode o2 = (LinearMatchNode)other;
            if (this.length != o2.length || this.next != o2.next) {
                return false;
            }
            int i2 = this.stringOffset;
            int j2 = o2.stringOffset;
            int limit = this.stringOffset + this.length;
            while (i2 < limit) {
                if (this.strings.charAt(i2) != this.strings.charAt(j2)) {
                    return false;
                }
                ++i2;
                ++j2;
            }
            return true;
        }

        @Override
        public Node add(StringTrieBuilder builder, CharSequence s2, int start, int sValue) {
            if (start == s2.length()) {
                if (this.hasValue) {
                    throw new IllegalArgumentException("Duplicate string.");
                }
                this.setValue(sValue);
                return this;
            }
            int limit = this.stringOffset + this.length;
            int i2 = this.stringOffset;
            while (i2 < limit) {
                char newChar;
                if (start == s2.length()) {
                    int prefixLength = i2 - this.stringOffset;
                    LinearMatchNode suffixNode = new LinearMatchNode(this.strings, i2, this.length - prefixLength, this.next);
                    suffixNode.setValue(sValue);
                    this.length = prefixLength;
                    this.next = suffixNode;
                    return this;
                }
                char thisChar = this.strings.charAt(i2);
                if (thisChar != (newChar = s2.charAt(start))) {
                    ValueNode result;
                    Node thisSuffixNode;
                    DynamicBranchNode branchNode = new DynamicBranchNode();
                    if (i2 == this.stringOffset) {
                        if (this.hasValue) {
                            branchNode.setValue(this.value);
                            this.value = 0;
                            this.hasValue = false;
                        }
                        ++this.stringOffset;
                        --this.length;
                        thisSuffixNode = this.length > 0 ? this : this.next;
                        result = branchNode;
                    } else if (i2 == limit - 1) {
                        --this.length;
                        thisSuffixNode = this.next;
                        this.next = branchNode;
                        result = this;
                    } else {
                        int prefixLength = i2 - this.stringOffset;
                        thisSuffixNode = new LinearMatchNode(this.strings, ++i2, this.length - (prefixLength + 1), this.next);
                        this.length = prefixLength;
                        this.next = branchNode;
                        result = this;
                    }
                    ValueNode newSuffixNode = builder.createSuffixNode(s2, start + 1, sValue);
                    branchNode.add(thisChar, thisSuffixNode);
                    branchNode.add(newChar, newSuffixNode);
                    return result;
                }
                ++i2;
                ++start;
            }
            this.next = this.next.add(builder, s2, start, sValue);
            return this;
        }

        @Override
        public Node register(StringTrieBuilder builder) {
            ValueNode result;
            this.next = this.next.register(builder);
            int maxLinearMatchLength = builder.getMaxLinearMatchLength();
            while (this.length > maxLinearMatchLength) {
                int nextOffset = this.stringOffset + this.length - maxLinearMatchLength;
                this.length -= maxLinearMatchLength;
                LinearMatchNode suffixNode = new LinearMatchNode(this.strings, nextOffset, maxLinearMatchLength, this.next);
                suffixNode.setHashCode();
                this.next = builder.registerNode(suffixNode);
            }
            if (this.hasValue && !builder.matchNodesCanHaveValues()) {
                int intermediateValue = this.value;
                this.value = 0;
                this.hasValue = false;
                this.setHashCode();
                result = new IntermediateValueNode(intermediateValue, builder.registerNode(this));
            } else {
                this.setHashCode();
                result = this;
            }
            return builder.registerNode(result);
        }

        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.offset = edgeNumber = this.next.markRightEdgesFirst(edgeNumber);
            }
            return edgeNumber;
        }

        @Override
        public void write(StringTrieBuilder builder) {
            this.next.write(builder);
            builder.write(this.stringOffset, this.length);
            this.offset = builder.writeValueAndType(this.hasValue, this.value, builder.getMinLinearMatch() + this.length - 1);
        }

        private void setHashCode() {
            this.hash = (124151391 + this.length) * 37 + this.next.hashCode();
            if (this.hasValue) {
                this.hash = this.hash * 37 + this.value;
            }
            int limit = this.stringOffset + this.length;
            for (int i2 = this.stringOffset; i2 < limit; ++i2) {
                this.hash = this.hash * 37 + this.strings.charAt(i2);
            }
        }
    }

    private static final class IntermediateValueNode
    extends ValueNode {
        private Node next;

        public IntermediateValueNode(int v2, Node nextNode) {
            this.next = nextNode;
            this.setValue(v2);
        }

        @Override
        public int hashCode() {
            return (0x4EEEEEA + this.value) * 37 + this.next.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            IntermediateValueNode o2 = (IntermediateValueNode)other;
            return this.next == o2.next;
        }

        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.offset = edgeNumber = this.next.markRightEdgesFirst(edgeNumber);
            }
            return edgeNumber;
        }

        @Override
        public void write(StringTrieBuilder builder) {
            this.next.write(builder);
            this.offset = builder.writeValueAndFinal(this.value, false);
        }
    }

    private static class ValueNode
    extends Node {
        protected boolean hasValue;
        protected int value;

        public ValueNode() {
        }

        public ValueNode(int v2) {
            this.hasValue = true;
            this.value = v2;
        }

        public final void setValue(int v2) {
            assert (!this.hasValue);
            this.hasValue = true;
            this.value = v2;
        }

        private void setFinalValue(int v2) {
            this.hasValue = true;
            this.value = v2;
        }

        @Override
        public int hashCode() {
            int hash = 0x111111;
            if (this.hasValue) {
                hash = hash * 37 + this.value;
            }
            return hash;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            ValueNode o2 = (ValueNode)other;
            return this.hasValue == o2.hasValue && (!this.hasValue || this.value == o2.value);
        }

        @Override
        public Node add(StringTrieBuilder builder, CharSequence s2, int start, int sValue) {
            if (start == s2.length()) {
                throw new IllegalArgumentException("Duplicate string.");
            }
            ValueNode node = builder.createSuffixNode(s2, start, sValue);
            node.setValue(this.value);
            return node;
        }

        @Override
        public void write(StringTrieBuilder builder) {
            this.offset = builder.writeValueAndFinal(this.value, true);
        }
    }

    private static abstract class Node {
        protected int offset = 0;

        public abstract int hashCode();

        public boolean equals(Object other) {
            return this == other || this.getClass() == other.getClass();
        }

        public Node add(StringTrieBuilder builder, CharSequence s2, int start, int sValue) {
            return this;
        }

        public Node register(StringTrieBuilder builder) {
            return this;
        }

        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.offset = edgeNumber;
            }
            return edgeNumber;
        }

        public abstract void write(StringTrieBuilder var1);

        public final void writeUnlessInsideRightEdge(int firstRight, int lastRight, StringTrieBuilder builder) {
            if (this.offset < 0 && (this.offset < lastRight || firstRight < this.offset)) {
                this.write(builder);
            }
        }

        public final int getOffset() {
            return this.offset;
        }
    }

    public static enum Option {
        FAST,
        SMALL;

    }
}

