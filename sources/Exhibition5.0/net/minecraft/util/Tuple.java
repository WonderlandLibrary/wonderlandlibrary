// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

public class Tuple
{
    private Object a;
    private Object b;
    private static final String __OBFID = "CL_00001502";
    
    public Tuple(final Object p_i1555_1_, final Object p_i1555_2_) {
        this.a = p_i1555_1_;
        this.b = p_i1555_2_;
    }
    
    public Object getFirst() {
        return this.a;
    }
    
    public Object getSecond() {
        return this.b;
    }
}
