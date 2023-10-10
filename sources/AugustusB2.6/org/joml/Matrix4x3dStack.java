// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;

public class Matrix4x3dStack extends Matrix4x3d
{
    private static final long serialVersionUID = 1L;
    private Matrix4x3d[] mats;
    private int curr;
    
    public Matrix4x3dStack(final int stackSize) {
        if (stackSize < 1) {
            throw new IllegalArgumentException("stackSize must be >= 1");
        }
        this.mats = new Matrix4x3d[stackSize - 1];
        for (int i = 0; i < this.mats.length; ++i) {
            this.mats[i] = new Matrix4x3d();
        }
    }
    
    public Matrix4x3dStack() {
    }
    
    public Matrix4x3dStack clear() {
        this.curr = 0;
        this.identity();
        return this;
    }
    
    public Matrix4x3dStack pushMatrix() {
        if (this.curr == this.mats.length) {
            throw new IllegalStateException("max stack size of " + (this.curr + 1) + " reached");
        }
        this.mats[this.curr++].set(this);
        return this;
    }
    
    public Matrix4x3dStack popMatrix() {
        if (this.curr == 0) {
            throw new IllegalStateException("already at the bottom of the stack");
        }
        final Matrix4x3d[] mats = this.mats;
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
        if (obj instanceof Matrix4x3dStack) {
            final Matrix4x3dStack other = (Matrix4x3dStack)obj;
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
        this.mats = new Matrix4x3dStack[this.curr];
        for (int i = 0; i < this.curr; ++i) {
            final Matrix4x3d m = new Matrix4x3d();
            m.readExternal(in);
            this.mats[i] = m;
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final Matrix4x3dStack cloned = (Matrix4x3dStack)super.clone();
        final Matrix4x3d[] clonedMats = new Matrix4x3d[this.mats.length];
        for (int i = 0; i < this.mats.length; ++i) {
            clonedMats[i] = (Matrix4x3d)this.mats[i].clone();
        }
        cloned.mats = clonedMats;
        return cloned;
    }
}
