// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

public final class Unit
{
    public static final Unit INSTANCE;
    
    @Override
    public final String toString() {
        return "kotlin.Unit";
    }
    
    private Unit() {
    }
    
    static {
        INSTANCE = new Unit();
    }
}
