// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;

public class Matrix4fStack extends Matrix4f
{
    private static final long serialVersionUID = 1L;
    private Matrix4f[] mats;
    private int curr;
    
    public Matrix4fStack(final int stackSize) {
        if (stackSize < 1) {
            throw new IllegalArgumentException("stackSize must be >= 1");
        }
        this.mats = new Matrix4f[stackSize - 1];
        for (int i = 0; i < this.mats.length; ++i) {
            this.mats[i] = new Matrix4f();
        }
    }
    
    public Matrix4fStack() {
    }
    
    public Matrix4fStack clear() {
        this.curr = 0;
        this.identity();
        return this;
    }
    
    public Matrix4fStack pushMatrix() {
        if (this.curr == this.mats.length) {
            throw new IllegalStateException("max stack size of " + (this.curr + 1) + " reached");
        }
        this.mats[this.curr++].set(this);
        return this;
    }
    
    public Matrix4fStack popMatrix() {
        if (this.curr == 0) {
            throw new IllegalStateException("already at the bottom of the stack");
        }
        final Matrix4f[] mats = this.mats;
        final int curr = this.curr - 1;
        this.curr = curr;
        this.set(mats[curr]);
        return this;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + this.curr;
        for (int i = 0; i < this.curr; ++i) {
            result = 31 * result + this.mats[i].hashCode();
        }
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (obj instanceof Matrix4fStack) {
            final Matrix4fStack other = (Matrix4fStack)obj;
            if (this.curr != other.curr) {
                return false;
            }
            for (int i = 0; i < this.curr; ++i) {
                if (!this.mats[i].equals(other.mats[i])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(this.curr);
        for (int i = 0; i < this.curr; ++i) {
            out.writeObject(this.mats[i]);
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException {
        super.readExternal(in);
        this.curr = in.readInt();
        this.mats = new Matrix4fStack[this.curr];
        for (int i = 0; i < this.curr; ++i) {
            final Matrix4f m = new Matrix4f();
            m.readExternal(in);
            this.mats[i] = m;
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final Matrix4fStack cloned = (Matrix4fStack)super.clone();
        final Matrix4f[] clonedMats = new Matrix4f[this.mats.length];
        for (int i = 0; i < this.mats.length; ++i) {
            clonedMats[i] = (Matrix4f)this.mats[i].clone();
        }
        cloned.mats = clonedMats;
        return cloned;
    }
}
