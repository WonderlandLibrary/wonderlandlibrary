/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.sevenz;

import java.util.LinkedList;
import org.apache.commons.compress.archivers.sevenz.BindPair;
import org.apache.commons.compress.archivers.sevenz.Coder;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Folder {
    Coder[] coders;
    long totalInputStreams;
    long totalOutputStreams;
    BindPair[] bindPairs;
    long[] packedStreams;
    long[] unpackSizes;
    boolean hasCrc;
    long crc;
    int numUnpackSubStreams;

    Folder() {
    }

    Iterable<Coder> getOrderedCoders() {
        LinkedList<Coder> l = new LinkedList<Coder>();
        int current = (int)this.packedStreams[0];
        while (current != -1) {
            l.addLast(this.coders[current]);
            int pair = this.findBindPairForOutStream(current);
            current = pair != -1 ? (int)this.bindPairs[pair].inIndex : -1;
        }
        return l;
    }

    int findBindPairForInStream(int index) {
        for (int i = 0; i < this.bindPairs.length; ++i) {
            if (this.bindPairs[i].inIndex != (long)index) continue;
            return i;
        }
        return -1;
    }

    int findBindPairForOutStream(int index) {
        for (int i = 0; i < this.bindPairs.length; ++i) {
            if (this.bindPairs[i].outIndex != (long)index) continue;
            return i;
        }
        return -1;
    }

    long getUnpackSize() {
        if (this.totalOutputStreams == 0L) {
            return 0L;
        }
        for (int i = (int)this.totalOutputStreams - 1; i >= 0; --i) {
            if (this.findBindPairForOutStream(i) >= 0) continue;
            return this.unpackSizes[i];
        }
        return 0L;
    }
}

