/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.serializers;

import java.io.File;
import java.io.InputStream;
import org.reflections.Reflections;

public interface Serializer {
    public Reflections read(InputStream var1);

    public File save(Reflections var1, String var2);

    public String toString(Reflections var1);
}

