// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class ActionResult<T>
{
    private final EnumActionResult type;
    private final T result;
    
    public ActionResult(final EnumActionResult typeIn, final T resultIn) {
        this.type = typeIn;
        this.result = resultIn;
    }
    
    public EnumActionResult getType() {
        return this.type;
    }
    
    public T getResult() {
        return this.result;
    }
}
