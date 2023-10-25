/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import org.apache.commons.io.filefilter.IOFileFilter;

public abstract class AbstractFileFilter
implements IOFileFilter {
    @Override
    public boolean accept(File file) {
        return this.accept(file.getParentFile(), file.getName());
    }

    @Override
    public boolean accept(File dir, String name) {
        return this.accept(new File(dir, name));
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}

