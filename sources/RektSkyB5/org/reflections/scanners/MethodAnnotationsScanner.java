/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import org.reflections.Store;
import org.reflections.scanners.AbstractScanner;

public class MethodAnnotationsScanner
extends AbstractScanner {
    @Override
    public void scan(Object cls, Store store) {
        for (Object method : this.getMetadataAdapter().getMethods(cls)) {
            for (String methodAnnotation : this.getMetadataAdapter().getMethodAnnotationNames(method)) {
                if (!this.acceptResult(methodAnnotation)) continue;
                this.put(store, methodAnnotation, this.getMetadataAdapter().getMethodFullKey(cls, method));
            }
        }
    }
}

