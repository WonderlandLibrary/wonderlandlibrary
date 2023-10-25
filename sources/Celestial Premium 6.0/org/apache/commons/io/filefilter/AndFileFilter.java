/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.ConditionalFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

public class AndFileFilter
extends AbstractFileFilter
implements ConditionalFileFilter,
Serializable {
    private static final long serialVersionUID = 7215974688563965257L;
    private final List<IOFileFilter> fileFilters;

    public AndFileFilter() {
        this.fileFilters = new ArrayList<IOFileFilter>();
    }

    public AndFileFilter(List<IOFileFilter> fileFilters) {
        this.fileFilters = fileFilters == null ? new ArrayList<IOFileFilter>() : new ArrayList<IOFileFilter>(fileFilters);
    }

    public AndFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        this.fileFilters = new ArrayList<IOFileFilter>(2);
        this.addFileFilter(filter1);
        this.addFileFilter(filter2);
    }

    @Override
    public void addFileFilter(IOFileFilter ioFileFilter) {
        this.fileFilters.add(ioFileFilter);
    }

    @Override
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList(this.fileFilters);
    }

    @Override
    public boolean removeFileFilter(IOFileFilter ioFileFilter) {
        return this.fileFilters.remove(ioFileFilter);
    }

    @Override
    public void setFileFilters(List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll(fileFilters);
    }

    @Override
    public boolean accept(File file) {
        if (this.fileFilters.isEmpty()) {
            return false;
        }
        for (IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean accept(File file, String name) {
        if (this.fileFilters.isEmpty()) {
            return false;
        }
        for (IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file, name)) continue;
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (this.fileFilters != null) {
            for (int i = 0; i < this.fileFilters.size(); ++i) {
                IOFileFilter filter;
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append((filter = this.fileFilters.get(i)) == null ? "null" : filter.toString());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}

