/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Transform;

public interface StringTransform
extends Transform<String, String> {
    @Override
    public String transform(String var1);
}

