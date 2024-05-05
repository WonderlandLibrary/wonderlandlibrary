/*
 * Decompiled with CFR 0.152.
 */
package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;
import org.scijava.nativelib.BaseJniExtractor;

public class WebappJniExtractor
extends BaseJniExtractor {
    private File nativeDir = new File(System.getProperty("java.library.tmpdir", "tmplib"));
    private File jniSubDir;

    public WebappJniExtractor(String classloaderName) throws IOException {
        File trialJniSubDir;
        this.nativeDir.mkdirs();
        if (!this.nativeDir.isDirectory()) {
            throw new IOException("Unable to create native library working directory " + this.nativeDir);
        }
        long now = System.currentTimeMillis();
        int attempt = 0;
        while (!(trialJniSubDir = new File(this.nativeDir, classloaderName + "." + now + "." + attempt)).mkdir()) {
            if (trialJniSubDir.exists()) {
                ++attempt;
                continue;
            }
            throw new IOException("Unable to create native library working directory " + trialJniSubDir);
        }
        this.jniSubDir = trialJniSubDir;
        this.jniSubDir.deleteOnExit();
    }

    protected void finalize() throws Throwable {
        File[] files = this.jniSubDir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            files[i].delete();
        }
        this.jniSubDir.delete();
    }

    public File getJniDir() {
        return this.jniSubDir;
    }

    public File getNativeDir() {
        return this.nativeDir;
    }
}

