package net.minecraft.util;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ClassInheritanceMultiMap;

/*
 * Exception performing whole class analysis ignored.
 */
class ClassInheritanceMultiMap.1
implements Iterable<S> {
    final /* synthetic */ Class val$clazz;

    ClassInheritanceMultiMap.1(Class clazz) {
        this.val$clazz = clazz;
    }

    public Iterator<S> iterator() {
        List list = (List)ClassInheritanceMultiMap.access$000((ClassInheritanceMultiMap)ClassInheritanceMultiMap.this).get((Object)ClassInheritanceMultiMap.this.initializeClassLookup(this.val$clazz));
        if (list == null) {
            return Iterators.emptyIterator();
        }
        Iterator iterator = list.iterator();
        return Iterators.filter((Iterator)iterator, (Class)this.val$clazz);
    }
}
