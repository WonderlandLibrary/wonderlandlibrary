/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.io.filefilter.IOFileFilter;

public class TrueFileFilter
implements IOFileFilter,
Serializable {
    private static final long serialVersionUID = 8782512160909720199L;
    public static final IOFileFilter TRUE;
    public static final IOFileFilter INSTANCE;

    protected TrueFileFilter() {
    }

    @Override
    public boolean accept(File file) {
        return true;
    }

    @Override
    public boolean accept(File dir, String name) {
        return true;
    }

    static {
        INSTANCE = TRUE = new TrueFileFilter();
    }
}

