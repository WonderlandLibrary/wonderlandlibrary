/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

public class FileFileFilter
extends AbstractFileFilter
implements Serializable {
    private static final long serialVersionUID = 5345244090827540862L;
    public static final IOFileFilter FILE = new FileFileFilter();

    protected FileFileFilter() {
    }

    @Override
    public boolean accept(File file) {
        return file.isFile();
    }
}

