package net.optifine.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import net.optifine.util.IteratorCache;

public class IteratorCache {
    private static Deque<IteratorReusable<Object>> dequeIterators = new ArrayDeque();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Iterator<Object> getReadOnly(List list) {
        Deque<IteratorReusable<Object>> deque = dequeIterators;
        synchronized (deque) {
            IteratorReusable iteratorreusable = (IteratorReusable)dequeIterators.pollFirst();
            if (iteratorreusable == null) {
                iteratorreusable = new IteratorReadOnly();
            }
            iteratorreusable.setList(list);
            return iteratorreusable;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void finished(IteratorReusable<Object> iterator) {
        Deque<IteratorReusable<Object>> deque = dequeIterators;
        synchronized (deque) {
            if (dequeIterators.size() <= 1000) {
                iterator.setList(null);
                dequeIterators.addLast(iterator);
            }
        }
    }

    static /* synthetic */ void access$000(IteratorReusable x0) {
        IteratorCache.finished((IteratorReusable<Object>)x0);
    }

    static {
        for (int i = 0; i < 1000; ++i) {
            IteratorReadOnly iteratorcache$iteratorreadonly = new IteratorReadOnly();
            dequeIterators.add((Object)iteratorcache$iteratorreadonly);
        }
    }
}
