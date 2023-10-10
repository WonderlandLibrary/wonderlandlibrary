/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.compress.archivers.sevenz;

import java.util.BitSet;
import org.apache.commons.compress.archivers.sevenz.Folder;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.StreamMap;
import org.apache.commons.compress.archivers.sevenz.SubStreamsInfo;

class Archive {
    long packPos;
    long[] packSizes;
    BitSet packCrcsDefined;
    long[] packCrcs;
    Folder[] folders;
    SubStreamsInfo subStreamsInfo;
    SevenZArchiveEntry[] files;
    StreamMap streamMap;

    Archive() {
    }

    public String toString() {
        return "Archive with packed streams starting at offset " + this.packPos + ", " + Archive.lengthOf(this.packSizes) + " pack sizes, " + Archive.lengthOf(this.packCrcs) + " CRCs, " + Archive.lengthOf(this.folders) + " folders, " + Archive.lengthOf(this.files) + " files and " + this.streamMap;
    }

    private static String lengthOf(long[] a2) {
        return a2 == null ? "(null)" : String.valueOf(a2.length);
    }

    private static String lengthOf(Object[] a2) {
        return a2 == null ? "(null)" : String.valueOf(a2.length);
    }
}

