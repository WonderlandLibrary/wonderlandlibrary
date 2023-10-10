// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;

public class Matrix3dStack extends Matrix3d
{
    private static final long serialVersionUID = 1L;
    private Matrix3d[] mats;
    private int curr;
    
    public Matrix3dStack(final int stackSize) {
        if (stackSize < 1) {
            throw new IllegalArgumentException("stackSize must be >= 1");
        }
        this.mats = new Matrix3d[stackSize - 1];
        for (int i = 0; i < this.mats.length; ++i) {
            this.mats[i] = new Matrix3d();
        }
    }
    
    public Matrix3dStack() {
    }
    
    public Matrix3dStack clear() {
        this.curr = 0;
        this.identity();
        return this;
    }
    
    public Matrix3dStack pushMatrix() {
        if (this.curr == this.mats.length) {
            throw new IllegalStateException("max stack size of " + (this.curr + 1) + " reached");
        }
        this.mats[this.curr++].set(this);
        return this;
    }
    
    public Matrix3dStack popMatrix() {
        if (this.curr == 0) {
            throw new IllegalStateException("already at the bottom of the stack");
        }
        final Matrix3d[] mats = this.mats;
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
        if (obj instanceof Matrix3dStack) {
            final Matrix3dStack other = (Matrix3dStack)obj;
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
        this.mats = new Matrix3dStack[this.curr];
        for (int i = 0; i < this.curr; ++i) {
            final Matrix3d m = new Matrix3d();
            m.readExternal(in);
            this.mats[i] = m;
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final Matrix3dStack cloned = (Matrix3dStack)super.clone();
        final Matrix3d[] clonedMats = new Matrix3d[this.mats.length];
        for (int i = 0; i < this.mats.length; ++i) {
            clonedMats[i] = (Matrix3d)this.mats[i].clone();
        }
        cloned.mats = clonedMats;
        return cloned;
    }
}
