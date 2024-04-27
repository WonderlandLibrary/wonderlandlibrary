/*
 * Decompiled with CFR 0.152.
 */
package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;

public interface JniExtractor {
    public File extractJni(String var1, String var2) throws IOException;

    public void extractRegistered() throws IOException;
}

