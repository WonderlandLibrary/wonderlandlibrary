/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import org.reflections.Store;
import org.reflections.scanners.AbstractScanner;
import org.reflections.vfs.Vfs;

public class ResourcesScanner
extends AbstractScanner {
    @Override
    public boolean acceptsInput(String file) {
        return !file.endsWith(".class");
    }

    @Override
    public Object scan(Vfs.File file, Object classObject, Store store) {
        this.put(store, file.getName(), file.getRelativePath());
        return classObject;
    }

    @Override
    public void scan(Object cls, Store store) {
        throw new UnsupportedOperationException();
    }
}

