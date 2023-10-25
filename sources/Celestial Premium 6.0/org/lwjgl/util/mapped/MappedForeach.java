/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.mapped;

import java.util.Iterator;
import org.lwjgl.util.mapped.MappedObject;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class MappedForeach<T extends MappedObject>
implements Iterable<T> {
    final T mapped;
    final int elementCount;

    MappedForeach(T mapped, int elementCount) {
        this.mapped = mapped;
        this.elementCount = elementCount;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            private int index;

            @Override
            public boolean hasNext() {
                return this.index < MappedForeach.this.elementCount;
            }

            @Override
            public T next() {
                ((MappedObject)MappedForeach.this.mapped).setViewAddress(((MappedObject)MappedForeach.this.mapped).getViewAddress(this.index++));
                return MappedForeach.this.mapped;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

