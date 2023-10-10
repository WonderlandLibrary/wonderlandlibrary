/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import org.reflections.Store;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.Utils;

public class TypeElementsScanner
extends AbstractScanner {
    private boolean includeFields = true;
    private boolean includeMethods = true;
    private boolean includeAnnotations = true;
    private boolean publicOnly = true;

    @Override
    public void scan(Object cls, Store store) {
        String className = this.getMetadataAdapter().getClassName(cls);
        if (!this.acceptResult(className)) {
            return;
        }
        this.put(store, className, "");
        if (this.includeFields) {
            for (Object object : this.getMetadataAdapter().getFields(cls)) {
                String fieldName = this.getMetadataAdapter().getFieldName(object);
                this.put(store, className, fieldName);
            }
        }
        if (this.includeMethods) {
            for (Object object : this.getMetadataAdapter().getMethods(cls)) {
                if (this.publicOnly && !this.getMetadataAdapter().isPublic(object)) continue;
                String methodKey = this.getMetadataAdapter().getMethodName(object) + "(" + Utils.join(this.getMetadataAdapter().getParameterNames(object), ", ") + ")";
                this.put(store, className, methodKey);
            }
        }
        if (this.includeAnnotations) {
            for (Object object : this.getMetadataAdapter().getClassAnnotationNames(cls)) {
                this.put(store, className, "@" + object);
            }
        }
    }

    public TypeElementsScanner includeFields() {
        return this.includeFields(true);
    }

    public TypeElementsScanner includeFields(boolean include) {
        this.includeFields = include;
        return this;
    }

    public TypeElementsScanner includeMethods() {
        return this.includeMethods(true);
    }

    public TypeElementsScanner includeMethods(boolean include) {
        this.includeMethods = include;
        return this;
    }

    public TypeElementsScanner includeAnnotations() {
        return this.includeAnnotations(true);
    }

    public TypeElementsScanner includeAnnotations(boolean include) {
        this.includeAnnotations = include;
        return this;
    }

    public TypeElementsScanner publicOnly(boolean only) {
        this.publicOnly = only;
        return this;
    }

    public TypeElementsScanner publicOnly() {
        return this.publicOnly(true);
    }
}

