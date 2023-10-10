/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode.analysis;

import javassist.bytecode.analysis.Type;

public class Frame {
    private Type[] locals;
    private Type[] stack;
    private int top;
    private boolean jsrMerged;
    private boolean retMerged;

    public Frame(int locals, int stack) {
        this.locals = new Type[locals];
        this.stack = new Type[stack];
    }

    public Type getLocal(int index) {
        return this.locals[index];
    }

    public void setLocal(int index, Type type) {
        this.locals[index] = type;
    }

    public Type getStack(int index) {
        return this.stack[index];
    }

    public void setStack(int index, Type type) {
        this.stack[index] = type;
    }

    public void clearStack() {
        this.top = 0;
    }

    public int getTopIndex() {
        return this.top - 1;
    }

    public int localsLength() {
        return this.locals.length;
    }

    public Type peek() {
        if (this.top < 1) {
            throw new IndexOutOfBoundsException("Stack is empty");
        }
        return this.stack[this.top - 1];
    }

    public Type pop() {
        if (this.top < 1) {
            throw new IndexOutOfBoundsException("Stack is empty");
        }
        return this.stack[--this.top];
    }

    public void push(Type type) {
        this.stack[this.top++] = type;
    }

    public Frame copy() {
        Frame frame = new Frame(this.locals.length, this.stack.length);
        System.arraycopy(this.locals, 0, frame.locals, 0, this.locals.length);
        System.arraycopy(this.stack, 0, frame.stack, 0, this.stack.length);
        frame.top = this.top;
        return frame;
    }

    public Frame copyStack() {
        Frame frame = new Frame(this.locals.length, this.stack.length);
        System.arraycopy(this.stack, 0, frame.stack, 0, this.stack.length);
        frame.top = this.top;
        return frame;
    }

    public boolean mergeStack(Frame frame) {
        boolean changed = false;
        if (this.top != frame.top) {
            throw new RuntimeException("Operand stacks could not be merged, they are different sizes!");
        }
        for (int i2 = 0; i2 < this.top; ++i2) {
            if (this.stack[i2] == null) continue;
            Type prev = this.stack[i2];
            Type merged = prev.merge(frame.stack[i2]);
            if (merged == Type.BOGUS) {
                throw new RuntimeException("Operand stacks could not be merged due to differing primitive types: pos = " + i2);
            }
            this.stack[i2] = merged;
            if (merged.equals(prev) && !merged.popChanged()) continue;
            changed = true;
        }
        return changed;
    }

    public boolean merge(Frame frame) {
        boolean changed = false;
        for (int i2 = 0; i2 < this.locals.length; ++i2) {
            if (this.locals[i2] != null) {
                Type merged;
                Type prev = this.locals[i2];
                this.locals[i2] = merged = prev.merge(frame.locals[i2]);
                if (merged.equals(prev) && !merged.popChanged()) continue;
                changed = true;
                continue;
            }
            if (frame.locals[i2] == null) continue;
            this.locals[i2] = frame.locals[i2];
            changed = true;
        }
        return changed |= this.mergeStack(frame);
    }

    public String toString() {
        int i2;
        StringBuffer buffer = new StringBuffer();
        buffer.append("locals = [");
        for (i2 = 0; i2 < this.locals.length; ++i2) {
            buffer.append(this.locals[i2] == null ? "empty" : this.locals[i2].toString());
            if (i2 >= this.locals.length - 1) continue;
            buffer.append(", ");
        }
        buffer.append("] stack = [");
        for (i2 = 0; i2 < this.top; ++i2) {
            buffer.append(this.stack[i2]);
            if (i2 >= this.top - 1) continue;
            buffer.append(", ");
        }
        buffer.append("]");
        return buffer.toString();
    }

    boolean isJsrMerged() {
        return this.jsrMerged;
    }

    void setJsrMerged(boolean jsrMerged) {
        this.jsrMerged = jsrMerged;
    }

    boolean isRetMerged() {
        return this.retMerged;
    }

    void setRetMerged(boolean retMerged) {
        this.retMerged = retMerged;
    }
}

