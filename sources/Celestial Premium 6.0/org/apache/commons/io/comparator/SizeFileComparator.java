/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.AbstractFileComparator;
import org.apache.commons.io.comparator.ReverseComparator;

public class SizeFileComparator
extends AbstractFileComparator
implements Serializable {
    private static final long serialVersionUID = -1201561106411416190L;
    public static final Comparator<File> SIZE_COMPARATOR = new SizeFileComparator();
    public static final Comparator<File> SIZE_REVERSE = new ReverseComparator(SIZE_COMPARATOR);
    public static final Comparator<File> SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
    public static final Comparator<File> SIZE_SUMDIR_REVERSE = new ReverseComparator(SIZE_SUMDIR_COMPARATOR);
    private final boolean sumDirectoryContents;

    public SizeFileComparator() {
        this.sumDirectoryContents = false;
    }

    public SizeFileComparator(boolean sumDirectoryContents) {
        this.sumDirectoryContents = sumDirectoryContents;
    }

    @Override
    public int compare(File file1, File file2) {
        long size1 = 0L;
        size1 = file1.isDirectory() ? (this.sumDirectoryContents && file1.exists() ? FileUtils.sizeOfDirectory(file1) : 0L) : file1.length();
        long size2 = 0L;
        size2 = file2.isDirectory() ? (this.sumDirectoryContents && file2.exists() ? FileUtils.sizeOfDirectory(file2) : 0L) : file2.length();
        long result = size1 - size2;
        if (result < 0L) {
            return -1;
        }
        if (result > 0L) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
    }
}

