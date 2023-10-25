package net.optifine.util;

import java.util.List;
import net.optifine.util.IteratorCache;

/*
 * Exception performing whole class analysis ignored.
 */
public static class IteratorCache.IteratorReadOnly
implements IteratorCache.IteratorReusable<Object> {
    private List<Object> list;
    private int index;
    private boolean hasNext;

    public void setList(List<Object> list) {
        if (this.hasNext) {
            throw new RuntimeException("Iterator still used, oldList: " + this.list + ", newList: " + list);
        }
        this.list = list;
        this.index = 0;
        this.hasNext = list != null && this.index < list.size();
    }

    public Object next() {
        if (!this.hasNext) {
            return null;
        }
        Object object = this.list.get(this.index);
        ++this.index;
        this.hasNext = this.index < this.list.size();
        return object;
    }

    public boolean hasNext() {
        if (!this.hasNext) {
            IteratorCache.access$000((IteratorCache.IteratorReusable)this);
            return false;
        }
        return this.hasNext;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
