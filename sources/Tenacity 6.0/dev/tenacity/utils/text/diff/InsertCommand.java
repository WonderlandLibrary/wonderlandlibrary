// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.diff;

public class InsertCommand<T> extends EditCommand<T>
{
    public InsertCommand(final T object) {
        super(object);
    }
    
    @Override
    public void accept(final CommandVisitor<T> visitor) {
        visitor.visitInsertCommand(this.getObject());
    }
}
