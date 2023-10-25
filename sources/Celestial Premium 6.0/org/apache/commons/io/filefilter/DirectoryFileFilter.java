/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

public class DirectoryFileFilter
extends AbstractFileFilter
implements Serializable {
    private static final long serialVersionUID = -5148237843784525732L;
    public static final IOFileFilter DIRECTORY;
    public static final IOFileFilter INSTANCE;

    protected DirectoryFileFilter() {
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory();
    }

    static {
        INSTANCE = DIRECTORY = new DirectoryFileFilter();
    }
}

