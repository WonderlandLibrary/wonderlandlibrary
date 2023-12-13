/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.scanners;

import java.util.List;
import org.reflections.Store;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.AbstractScanner;

public class MethodParameterScanner
extends AbstractScanner {
    @Override
    public void scan(Object cls, Store store) {
        MetadataAdapter md = this.getMetadataAdapter();
        for (Object method : md.getMethods(cls)) {
            String returnTypeName;
            String signature = md.getParameterNames(method).toString();
            if (this.acceptResult(signature)) {
                this.put(store, signature, md.getMethodFullKey(cls, method));
            }
            if (this.acceptResult(returnTypeName = md.getReturnTypeName(method))) {
                this.put(store, returnTypeName, md.getMethodFullKey(cls, method));
            }
            List<String> parameterNames = md.getParameterNames(method);
            for (int i2 = 0; i2 < parameterNames.size(); ++i2) {
                for (String paramAnnotation : md.getParameterAnnotationNames(method, i2)) {
                    if (!this.acceptResult(paramAnnotation)) continue;
                    this.put(store, paramAnnotation, md.getMethodFullKey(cls, method));
                }
            }
        }
    }
}

