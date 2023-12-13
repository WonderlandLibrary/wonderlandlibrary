/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import java.lang.annotation.Inherited;
import org.reflections.Store;
import org.reflections.scanners.AbstractScanner;

public class TypeAnnotationsScanner
extends AbstractScanner {
    @Override
    public void scan(Object cls, Store store) {
        String className = this.getMetadataAdapter().getClassName(cls);
        for (String annotationType : this.getMetadataAdapter().getClassAnnotationNames(cls)) {
            if (!this.acceptResult(annotationType) && !annotationType.equals(Inherited.class.getName())) continue;
            this.put(store, annotationType, className);
        }
    }
}

