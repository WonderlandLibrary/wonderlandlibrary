// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Queue;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
class ConsumingQueueIterator<T> extends AbstractIterator<T>
{
    private final Queue<T> queue;
    
    ConsumingQueueIterator(final T... elements) {
        Collections.addAll(this.queue = new ArrayDeque<T>(elements.length), elements);
    }
    
    ConsumingQueueIterator(final Queue<T> queue) {
        this.queue = Preconditions.checkNotNull(queue);
    }
    
    public T computeNext() {
        return this.queue.isEmpty() ? this.endOfData() : this.queue.remove();
    }
}
