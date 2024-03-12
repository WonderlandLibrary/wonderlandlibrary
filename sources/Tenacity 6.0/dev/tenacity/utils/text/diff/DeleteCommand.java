// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.diff;

public class DeleteCommand<T> extends EditCommand<T>
{
    public DeleteCommand(final T object) {
        super(object);
    }
    
    @Override
    public void accept(final CommandVisitor<T> visitor) {
        visitor.visitDeleteCommand(this.getObject());
    }
}
