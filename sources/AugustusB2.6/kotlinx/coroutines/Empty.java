// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

final class Empty implements Incomplete
{
    private final boolean isActive;
    
    @Override
    public final NodeList getList() {
        return null;
    }
    
    @Override
    public final String toString() {
        return "Empty{" + (this.isActive ? "Active" : "New") + '}';
    }
    
    @Override
    public final boolean isActive() {
        return this.isActive;
    }
    
    public Empty(final boolean isActive) {
        this.isActive = isActive;
    }
}
