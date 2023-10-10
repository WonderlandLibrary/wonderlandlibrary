// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import com.google.common.base.Preconditions;
import java.util.Queue;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class ConsumingQueueIterator<T> extends AbstractIterator<T>
{
    private final Queue<T> queue;
    
    ConsumingQueueIterator(final Queue<T> queue) {
        this.queue = Preconditions.checkNotNull(queue);
    }
    
    @CheckForNull
    public T computeNext() {
        if (this.queue.isEmpty()) {
            return this.endOfData();
        }
        return this.queue.remove();
    }
}
