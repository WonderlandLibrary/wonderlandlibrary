// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class LockCounter
{
    private int lockCount;
    
    public boolean lock() {
        ++this.lockCount;
        return this.lockCount == 1;
    }
    
    public boolean unlock() {
        if (this.lockCount <= 0) {
            return false;
        }
        --this.lockCount;
        return this.lockCount == 0;
    }
    
    public boolean isLocked() {
        return this.lockCount > 0;
    }
    
    public int getLockCount() {
        return this.lockCount;
    }
    
    @Override
    public String toString() {
        return "lockCount: " + this.lockCount;
    }
}
