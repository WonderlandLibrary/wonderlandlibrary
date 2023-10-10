// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class Hashing
{
    private static final long C1 = -862048943L;
    private static final long C2 = 461845907L;
    private static final int MAX_TABLE_SIZE = 1073741824;
    
    private Hashing() {
    }
    
    static int smear(final int hashCode) {
        return (int)(461845907L * Integer.rotateLeft((int)(hashCode * -862048943L), 15));
    }
    
    static int smearedHash(@CheckForNull final Object o) {
        return smear((o == null) ? 0 : o.hashCode());
    }
    
    static int closedTableSize(int expectedEntries, final double loadFactor) {
        expectedEntries = Math.max(expectedEntries, 2);
        int tableSize = Integer.highestOneBit(expectedEntries);
        if (expectedEntries > (int)(loadFactor * tableSize)) {
            tableSize <<= 1;
            return (tableSize > 0) ? tableSize : 1073741824;
        }
        return tableSize;
    }
    
    static boolean needsResizing(final int size, final int tableSize, final double loadFactor) {
        return size > loadFactor * tableSize && tableSize < 1073741824;
    }
}
