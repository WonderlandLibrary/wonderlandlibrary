// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.diff;

public interface CommandVisitor<T>
{
    void visitInsertCommand(final T p0);
    
    void visitKeepCommand(final T p0);
    
    void visitDeleteCommand(final T p0);
}
