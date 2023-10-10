// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.runtime;

public class Cflow extends ThreadLocal<Depth>
{
    @Override
    protected synchronized Depth initialValue() {
        return new Depth();
    }
    
    public void enter() {
        this.get().inc();
    }
    
    public void exit() {
        this.get().dec();
    }
    
    public int value() {
        return this.get().value();
    }
    
    protected static class Depth
    {
        private int depth;
        
        Depth() {
            this.depth = 0;
        }
        
        int value() {
            return this.depth;
        }
        
        void inc() {
            ++this.depth;
        }
        
        void dec() {
            --this.depth;
        }
    }
}
