/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.BreakableNode;
import jdk.nashorn.internal.ir.Flags;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LabelNode;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.LoopNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.SwitchNode;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.Source;

public class LexicalContext {
    private LexicalContextNode[] stack = new LexicalContextNode[16];
    private int[] flags = new int[16];
    private int sp;

    public void setFlag(LexicalContextNode node, int flag) {
        if (flag != 0) {
            assert (flag != 1 || !(node instanceof Block));
            for (int i = this.sp - 1; i >= 0; --i) {
                if (this.stack[i] != node) continue;
                int n = i;
                this.flags[n] = this.flags[n] | flag;
                return;
            }
        }
        assert (false);
    }

    public void setBlockNeedsScope(Block block) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] != block) continue;
            int n = i;
            this.flags[n] = this.flags[n] | 1;
            for (int j = i - 1; j >= 0; --j) {
                if (!(this.stack[j] instanceof FunctionNode)) continue;
                int n2 = j;
                this.flags[n2] = this.flags[n2] | 0x80;
                return;
            }
        }
        assert (false);
    }

    public int getFlags(LexicalContextNode node) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] != node) continue;
            return this.flags[i];
        }
        throw new AssertionError((Object)"flag node not on context stack");
    }

    public Block getFunctionBody(FunctionNode functionNode) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] != functionNode) continue;
            return (Block)this.stack[i + 1];
        }
        throw new AssertionError((Object)(functionNode.getName() + " not on context stack"));
    }

    public Iterator<LexicalContextNode> getAllNodes() {
        return new NodeIterator<LexicalContextNode>(LexicalContextNode.class);
    }

    public FunctionNode getOutermostFunction() {
        return (FunctionNode)this.stack[0];
    }

    public <T extends LexicalContextNode> T push(T node) {
        assert (!this.contains(node));
        if (this.sp == this.stack.length) {
            LexicalContextNode[] newStack = new LexicalContextNode[this.sp * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.sp);
            this.stack = newStack;
            int[] newFlags = new int[this.sp * 2];
            System.arraycopy(this.flags, 0, newFlags, 0, this.sp);
            this.flags = newFlags;
        }
        this.stack[this.sp] = node;
        this.flags[this.sp] = 0;
        ++this.sp;
        return node;
    }

    public boolean isEmpty() {
        return this.sp == 0;
    }

    public int size() {
        return this.sp;
    }

    public <T extends Node> T pop(T node) {
        --this.sp;
        LexicalContextNode popped = this.stack[this.sp];
        this.stack[this.sp] = null;
        if (popped instanceof Flags) {
            return (T)((Node)((Flags)((Object)popped)).setFlag(this, this.flags[this.sp]));
        }
        return (T)((Node)((Object)popped));
    }

    public <T extends LexicalContextNode & Flags<T>> T applyTopFlags(T node) {
        assert (node == this.peek());
        return ((Flags<T>)node).setFlag(this, this.flags[this.sp - 1]);
    }

    public LexicalContextNode peek() {
        return this.stack[this.sp - 1];
    }

    public boolean contains(LexicalContextNode node) {
        for (int i = 0; i < this.sp; ++i) {
            if (this.stack[i] != node) continue;
            return true;
        }
        return false;
    }

    public LexicalContextNode replace(LexicalContextNode oldNode, LexicalContextNode newNode) {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (this.stack[i] != oldNode) continue;
            assert (i == this.sp - 1) : "violation of contract - we always expect to find the replacement node on top of the lexical context stack: " + newNode + " has " + this.stack[i + 1].getClass() + " above it";
            this.stack[i] = newNode;
            break;
        }
        return newNode;
    }

    public Iterator<Block> getBlocks() {
        return new NodeIterator<Block>(Block.class);
    }

    public Iterator<FunctionNode> getFunctions() {
        return new NodeIterator<FunctionNode>(FunctionNode.class);
    }

    public Block getParentBlock() {
        NodeIterator<Block> iter = new NodeIterator<Block>(Block.class, this.getCurrentFunction());
        iter.next();
        return iter.hasNext() ? (Block)iter.next() : null;
    }

    public LabelNode getCurrentBlockLabelNode() {
        assert (this.stack[this.sp - 1] instanceof Block);
        if (this.sp < 2) {
            return null;
        }
        LexicalContextNode parent = this.stack[this.sp - 2];
        return parent instanceof LabelNode ? (LabelNode)parent : null;
    }

    public Iterator<Block> getAncestorBlocks(Block block) {
        Iterator<Block> iter = this.getBlocks();
        while (iter.hasNext()) {
            Block b = iter.next();
            if (block != b) continue;
            return iter;
        }
        throw new AssertionError((Object)"Block is not on the current lexical context stack");
    }

    public Iterator<Block> getBlocks(final Block block) {
        final Iterator<Block> iter = this.getAncestorBlocks(block);
        return new Iterator<Block>(){
            boolean blockReturned = false;

            @Override
            public boolean hasNext() {
                return iter.hasNext() || !this.blockReturned;
            }

            @Override
            public Block next() {
                if (this.blockReturned) {
                    return (Block)iter.next();
                }
                this.blockReturned = true;
                return block;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public FunctionNode getFunction(Block block) {
        NodeIterator<LexicalContextNode> iter = new NodeIterator<LexicalContextNode>(LexicalContextNode.class);
        while (iter.hasNext()) {
            LexicalContextNode next = (LexicalContextNode)iter.next();
            if (next != block) continue;
            while (iter.hasNext()) {
                LexicalContextNode next2 = (LexicalContextNode)iter.next();
                if (!(next2 instanceof FunctionNode)) continue;
                return (FunctionNode)next2;
            }
        }
        assert (false);
        return null;
    }

    public Block getCurrentBlock() {
        return this.getBlocks().next();
    }

    public FunctionNode getCurrentFunction() {
        for (int i = this.sp - 1; i >= 0; --i) {
            if (!(this.stack[i] instanceof FunctionNode)) continue;
            return (FunctionNode)this.stack[i];
        }
        return null;
    }

    public Block getDefiningBlock(Symbol symbol) {
        String name = symbol.getName();
        Iterator<Block> it = this.getBlocks();
        while (it.hasNext()) {
            Block next = it.next();
            if (next.getExistingSymbol(name) != symbol) continue;
            return next;
        }
        throw new AssertionError((Object)("Couldn't find symbol " + name + " in the context"));
    }

    public FunctionNode getDefiningFunction(Symbol symbol) {
        String name = symbol.getName();
        NodeIterator<LexicalContextNode> iter = new NodeIterator<LexicalContextNode>(LexicalContextNode.class);
        while (iter.hasNext()) {
            LexicalContextNode next = (LexicalContextNode)iter.next();
            if (!(next instanceof Block) || ((Block)next).getExistingSymbol(name) != symbol) continue;
            while (iter.hasNext()) {
                LexicalContextNode next2 = (LexicalContextNode)iter.next();
                if (!(next2 instanceof FunctionNode)) continue;
                return (FunctionNode)next2;
            }
            throw new AssertionError((Object)("Defining block for symbol " + name + " has no function in the context"));
        }
        throw new AssertionError((Object)("Couldn't find symbol " + name + " in the context"));
    }

    public boolean isFunctionBody() {
        return this.getParentBlock() == null;
    }

    public boolean isSplitBody() {
        return this.sp >= 2 && this.stack[this.sp - 1] instanceof Block && this.stack[this.sp - 2] instanceof SplitNode;
    }

    public FunctionNode getParentFunction(FunctionNode functionNode) {
        NodeIterator<FunctionNode> iter = new NodeIterator<FunctionNode>(FunctionNode.class);
        while (iter.hasNext()) {
            FunctionNode next = (FunctionNode)iter.next();
            if (next != functionNode) continue;
            return iter.hasNext() ? (FunctionNode)iter.next() : null;
        }
        assert (false);
        return null;
    }

    public int getScopeNestingLevelTo(LexicalContextNode until) {
        LexicalContextNode node;
        assert (until != null);
        int n = 0;
        Iterator<LexicalContextNode> iter = this.getAllNodes();
        while (iter.hasNext() && (node = iter.next()) != until) {
            assert (!(node instanceof FunctionNode));
            if (!(node instanceof WithNode) && (!(node instanceof Block) || !((Block)node).needsScope())) continue;
            ++n;
        }
        return n;
    }

    private BreakableNode getBreakable() {
        NodeIterator<BreakableNode> iter = new NodeIterator<BreakableNode>(BreakableNode.class, this.getCurrentFunction());
        while (iter.hasNext()) {
            BreakableNode next = (BreakableNode)iter.next();
            if (!next.isBreakableWithoutLabel()) continue;
            return next;
        }
        return null;
    }

    public boolean inLoop() {
        return this.getCurrentLoop() != null;
    }

    public LoopNode getCurrentLoop() {
        NodeIterator<LoopNode> iter = new NodeIterator<LoopNode>(LoopNode.class, this.getCurrentFunction());
        return iter.hasNext() ? (LoopNode)iter.next() : null;
    }

    public BreakableNode getBreakable(String labelName) {
        if (labelName != null) {
            LabelNode foundLabel = this.findLabel(labelName);
            if (foundLabel != null) {
                BreakableNode breakable = null;
                NodeIterator<BreakableNode> iter = new NodeIterator<BreakableNode>(BreakableNode.class, foundLabel);
                while (iter.hasNext()) {
                    breakable = (BreakableNode)iter.next();
                }
                return breakable;
            }
            return null;
        }
        return this.getBreakable();
    }

    private LoopNode getContinueTo() {
        return this.getCurrentLoop();
    }

    public LoopNode getContinueTo(String labelName) {
        if (labelName != null) {
            LabelNode foundLabel = this.findLabel(labelName);
            if (foundLabel != null) {
                LoopNode loop = null;
                NodeIterator<LoopNode> iter = new NodeIterator<LoopNode>(LoopNode.class, foundLabel);
                while (iter.hasNext()) {
                    loop = (LoopNode)iter.next();
                }
                return loop;
            }
            return null;
        }
        return this.getContinueTo();
    }

    public Block getInlinedFinally(String labelName) {
        NodeIterator<TryNode> iter = new NodeIterator<TryNode>(TryNode.class);
        while (iter.hasNext()) {
            Block inlinedFinally = ((TryNode)iter.next()).getInlinedFinally(labelName);
            if (inlinedFinally == null) continue;
            return inlinedFinally;
        }
        return null;
    }

    public TryNode getTryNodeForInlinedFinally(String labelName) {
        NodeIterator<TryNode> iter = new NodeIterator<TryNode>(TryNode.class);
        while (iter.hasNext()) {
            TryNode tryNode = (TryNode)iter.next();
            if (tryNode.getInlinedFinally(labelName) == null) continue;
            return tryNode;
        }
        return null;
    }

    public LabelNode findLabel(String name) {
        NodeIterator<LabelNode> iter = new NodeIterator<LabelNode>(LabelNode.class, this.getCurrentFunction());
        while (iter.hasNext()) {
            LabelNode next = (LabelNode)iter.next();
            if (!next.getLabelName().equals(name)) continue;
            return next;
        }
        return null;
    }

    public boolean isExternalTarget(SplitNode splitNode, BreakableNode target) {
        int i = this.sp;
        while (i-- > 0) {
            LexicalContextNode next = this.stack[i];
            if (next == splitNode) {
                return true;
            }
            if (next == target) {
                return false;
            }
            if (!(next instanceof TryNode)) continue;
            for (Block inlinedFinally : ((TryNode)next).getInlinedFinallies()) {
                if (TryNode.getLabelledInlinedFinallyBlock(inlinedFinally) != target) continue;
                return false;
            }
        }
        throw new AssertionError((Object)(target + " was expected in lexical context " + this + " but wasn't"));
    }

    public boolean inUnprotectedSwitchContext() {
        for (int i = this.sp; i > 0; --i) {
            LexicalContextNode next = this.stack[i];
            if (!(next instanceof Block)) continue;
            return this.stack[i - 1] instanceof SwitchNode;
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        for (int i = 0; i < this.sp; ++i) {
            LexicalContextNode node = this.stack[i];
            sb.append(node.getClass().getSimpleName());
            sb.append('@');
            sb.append(Debug.id(node));
            sb.append(':');
            if (node instanceof FunctionNode) {
                FunctionNode fn = (FunctionNode)node;
                Source source = fn.getSource();
                String src = source.toString();
                if (src.contains(File.pathSeparator)) {
                    src = src.substring(src.lastIndexOf(File.pathSeparator));
                }
                src = src + ' ';
                src = src + fn.getLineNumber();
                sb.append(src);
            }
            sb.append(' ');
        }
        sb.append(" ==> ]");
        return sb.toString();
    }

    private class NodeIterator<T extends LexicalContextNode>
    implements Iterator<T> {
        private int index;
        private T next;
        private final Class<T> clazz;
        private LexicalContextNode until;

        NodeIterator(Class<T> clazz) {
            this(clazz, null);
        }

        NodeIterator(Class<T> clazz, LexicalContextNode until) {
            this.index = LexicalContext.this.sp - 1;
            this.clazz = clazz;
            this.until = until;
            this.next = this.findNext();
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public T next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            T lnext = this.next;
            this.next = this.findNext();
            return lnext;
        }

        private T findNext() {
            for (int i = this.index; i >= 0; --i) {
                LexicalContextNode node = LexicalContext.this.stack[i];
                if (node == this.until) {
                    return null;
                }
                if (!this.clazz.isAssignableFrom(node.getClass())) continue;
                this.index = i - 1;
                return (T)node;
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

