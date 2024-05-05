/*
 * Decompiled with CFR 0.152.
 */
package org.scijava.nativelib;

import java.io.IOException;
import org.scijava.nativelib.DefaultJniExtractor;
import org.scijava.nativelib.JniExtractor;
import org.scijava.nativelib.WebappJniExtractor;

public class NativeLoader {
    private static JniExtractor jniExtractor = null;

    public static void loadLibrary(String libname) throws IOException {
        System.load(jniExtractor.extractJni("", libname).getAbsolutePath());
    }

    public static void extractRegistered() throws IOException {
        jniExtractor.extractRegistered();
    }

    public static JniExtractor getJniExtractor() {
        return jniExtractor;
    }

    public static void setJniExtractor(JniExtractor jniExtractor) {
        NativeLoader.jniExtractor = jniExtractor;
    }

    static {
        try {
            jniExtractor = NativeLoader.class.getClassLoader() == ClassLoader.getSystemClassLoader() ? new DefaultJniExtractor() : new WebappJniExtractor("Classloader");
        }
        catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}

