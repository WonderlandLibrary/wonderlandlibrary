/*
 * Decompiled with CFR 0.152.
 */
package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;
import org.scijava.nativelib.BaseJniExtractor;

public class DefaultJniExtractor
extends BaseJniExtractor {
    private File nativeDir;

    public DefaultJniExtractor() throws IOException {
        super(null);
        this.init("tmplib");
    }

    public DefaultJniExtractor(Class libraryJarClass, String tmplib) throws IOException {
        super(libraryJarClass);
        this.init(tmplib);
    }

    void init(String tmplib) throws IOException {
        this.nativeDir = new File(System.getProperty("java.library.tmpdir", tmplib));
        this.nativeDir.mkdirs();
        if (!this.nativeDir.isDirectory()) {
            throw new IOException("Unable to create native library working directory " + this.nativeDir);
        }
    }

    public File getJniDir() {
        return this.nativeDir;
    }

    public File getNativeDir() {
        return this.nativeDir;
    }
}

