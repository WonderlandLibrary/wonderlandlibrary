/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.changes;

import java.util.ArrayList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ChangeSetResults {
    private final List<String> addedFromChangeSet = new ArrayList<String>();
    private final List<String> addedFromStream = new ArrayList<String>();
    private final List<String> deleted = new ArrayList<String>();

    void deleted(String fileName) {
        this.deleted.add(fileName);
    }

    void addedFromStream(String fileName) {
        this.addedFromStream.add(fileName);
    }

    void addedFromChangeSet(String fileName) {
        this.addedFromChangeSet.add(fileName);
    }

    public List<String> getAddedFromChangeSet() {
        return this.addedFromChangeSet;
    }

    public List<String> getAddedFromStream() {
        return this.addedFromStream;
    }

    public List<String> getDeleted() {
        return this.deleted;
    }

    boolean hasBeenAdded(String filename) {
        return this.addedFromChangeSet.contains(filename) || this.addedFromStream.contains(filename);
    }
}

