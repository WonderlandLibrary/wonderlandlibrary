// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.diff;

public class KeepCommand<T> extends EditCommand<T>
{
    public KeepCommand(final T object) {
        super(object);
    }
    
    @Override
    public void accept(final CommandVisitor<T> visitor) {
        visitor.visitKeepCommand(this.getObject());
    }
}
