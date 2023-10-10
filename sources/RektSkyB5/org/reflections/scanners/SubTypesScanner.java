/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import org.reflections.Store;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.FilterBuilder;

public class SubTypesScanner
extends AbstractScanner {
    public SubTypesScanner() {
        this(true);
    }

    public SubTypesScanner(boolean excludeObjectClass) {
        if (excludeObjectClass) {
            this.filterResultsBy(new FilterBuilder().exclude(Object.class.getName()));
        }
    }

    @Override
    public void scan(Object cls, Store store) {
        String className = this.getMetadataAdapter().getClassName(cls);
        String superclass = this.getMetadataAdapter().getSuperclassName(cls);
        if (this.acceptResult(superclass)) {
            this.put(store, superclass, className);
        }
        for (String anInterface : this.getMetadataAdapter().getInterfacesNames(cls)) {
            if (!this.acceptResult(anInterface)) continue;
            this.put(store, anInterface, className);
        }
    }
}

