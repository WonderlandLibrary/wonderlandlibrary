/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import org.apache.commons.io.comparator.AbstractFileComparator;

public class CompositeFileComparator
extends AbstractFileComparator
implements Serializable {
    private static final long serialVersionUID = -2224170307287243428L;
    private static final Comparator<?>[] NO_COMPARATORS = new Comparator[0];
    private final Comparator<File>[] delegates;

    public CompositeFileComparator(Comparator<File> ... delegates) {
        if (delegates == null) {
            this.delegates = NO_COMPARATORS;
        } else {
            this.delegates = new Comparator[delegates.length];
            System.arraycopy(delegates, 0, this.delegates, 0, delegates.length);
        }
    }

    public CompositeFileComparator(Iterable<Comparator<File>> delegates) {
        if (delegates == null) {
            this.delegates = NO_COMPARATORS;
        } else {
            ArrayList<Comparator<File>> list = new ArrayList<Comparator<File>>();
            for (Comparator<File> comparator : delegates) {
                list.add(comparator);
            }
            this.delegates = list.toArray(new Comparator[list.size()]);
        }
    }

    @Override
    public int compare(File file1, File file2) {
        Comparator<File> delegate;
        int result = 0;
        Comparator<File>[] comparatorArray = this.delegates;
        int n2 = comparatorArray.length;
        for (int i2 = 0; i2 < n2 && (result = (delegate = comparatorArray[i2]).compare(file1, file2)) == 0; ++i2) {
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append('{');
        for (int i2 = 0; i2 < this.delegates.length; ++i2) {
            if (i2 > 0) {
                builder.append(',');
            }
            builder.append(this.delegates[i2]);
        }
        builder.append('}');
        return builder.toString();
    }
}

