// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text.diff;

import java.util.ArrayList;
import java.util.List;

public class ReplacementsFinder<T> implements CommandVisitor<T>
{
    private final List<T> pendingInsertions;
    private final List<T> pendingDeletions;
    private int skipped;
    private final ReplacementsHandler<T> handler;
    
    public ReplacementsFinder(final ReplacementsHandler<T> handler) {
        this.pendingInsertions = new ArrayList<T>();
        this.pendingDeletions = new ArrayList<T>();
        this.skipped = 0;
        this.handler = handler;
    }
    
    @Override
    public void visitInsertCommand(final T object) {
        this.pendingInsertions.add(object);
    }
    
    @Override
    public void visitKeepCommand(final T object) {
        if (this.pendingDeletions.isEmpty() && this.pendingInsertions.isEmpty()) {
            ++this.skipped;
        }
        else {
            this.handler.handleReplacement(this.skipped, this.pendingDeletions, this.pendingInsertions);
            this.pendingDeletions.clear();
            this.pendingInsertions.clear();
            this.skipped = 1;
        }
    }
    
    @Override
    public void visitDeleteCommand(final T object) {
        this.pendingDeletions.add(object);
    }
}
