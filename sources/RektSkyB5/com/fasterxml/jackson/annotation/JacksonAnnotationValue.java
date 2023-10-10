/*
 * Decompiled with CFR 0.152.
 */
package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;

public interface JacksonAnnotationValue<A extends Annotation> {
    public Class<A> valueFor();
}

