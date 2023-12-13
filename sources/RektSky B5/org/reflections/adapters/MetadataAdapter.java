/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.adapters;

import java.util.List;
import org.reflections.vfs.Vfs;

public interface MetadataAdapter<C, F, M> {
    public String getClassName(C var1);

    public String getSuperclassName(C var1);

    public List<String> getInterfacesNames(C var1);

    public List<F> getFields(C var1);

    public List<M> getMethods(C var1);

    public String getMethodName(M var1);

    public List<String> getParameterNames(M var1);

    public List<String> getClassAnnotationNames(C var1);

    public List<String> getFieldAnnotationNames(F var1);

    public List<String> getMethodAnnotationNames(M var1);

    public List<String> getParameterAnnotationNames(M var1, int var2);

    public String getReturnTypeName(M var1);

    public String getFieldName(F var1);

    public C getOrCreateClassObject(Vfs.File var1) throws Exception;

    public String getMethodModifier(M var1);

    public String getMethodKey(C var1, M var2);

    public String getMethodFullKey(C var1, M var2);

    public boolean isPublic(Object var1);

    public boolean acceptsInput(String var1);
}

