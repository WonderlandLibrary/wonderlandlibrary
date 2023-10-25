/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

public final class NativeLibraryLoader {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
    private static final File WORKDIR;
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final String OSNAME;

    public static void load(String string, ClassLoader classLoader) {
        boolean bl;
        File file;
        FileOutputStream fileOutputStream;
        block16: {
            String string2 = System.mapLibraryName(string);
            String string3 = NATIVE_RESOURCE_HOME + string2;
            URL uRL = classLoader.getResource(string3);
            if (uRL == null && NativeLibraryLoader.isOSX()) {
                uRL = string3.endsWith(".jnilib") ? classLoader.getResource("META-INF/native/lib" + string + ".dynlib") : classLoader.getResource("META-INF/native/lib" + string + ".jnilib");
            }
            if (uRL == null) {
                System.loadLibrary(string);
                return;
            }
            int n = string2.lastIndexOf(46);
            String string4 = string2.substring(0, n);
            String string5 = string2.substring(n, string2.length());
            InputStream inputStream = null;
            fileOutputStream = null;
            file = null;
            bl = false;
            try {
                int n2;
                file = File.createTempFile(string4, string5, WORKDIR);
                inputStream = uRL.openStream();
                fileOutputStream = new FileOutputStream(file);
                byte[] byArray = new byte[8192];
                while ((n2 = inputStream.read(byArray)) > 0) {
                    ((OutputStream)fileOutputStream).write(byArray, 0, n2);
                }
                fileOutputStream.flush();
                ((OutputStream)fileOutputStream).close();
                fileOutputStream = null;
                System.load(file.getPath());
                bl = true;
                if (inputStream == null) break block16;
            }
            catch (Exception exception) {
                throw (UnsatisfiedLinkError)new UnsatisfiedLinkError("could not load a native library: " + string).initCause(exception);
            }
            try {
                inputStream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        if (fileOutputStream != null) {
            try {
                ((OutputStream)fileOutputStream).close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        if (file != null) {
            if (bl) {
                file.deleteOnExit();
            } else if (!file.delete()) {
                file.deleteOnExit();
            }
        }
    }

    private static File tmpdir() {
        File file;
        try {
            file = NativeLibraryLoader.toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (file != null) {
                logger.debug("-Dio.netty.tmpdir: " + file);
                return file;
            }
            file = NativeLibraryLoader.toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (file != null) {
                logger.debug("-Dio.netty.tmpdir: " + file + " (java.io.tmpdir)");
                return file;
            }
            if (NativeLibraryLoader.isWindows()) {
                file = NativeLibraryLoader.toDirectory(System.getenv("TEMP"));
                if (file != null) {
                    logger.debug("-Dio.netty.tmpdir: " + file + " (%TEMP%)");
                    return file;
                }
                String string = System.getenv("USERPROFILE");
                if (string != null) {
                    file = NativeLibraryLoader.toDirectory(string + "\\AppData\\Local\\Temp");
                    if (file != null) {
                        logger.debug("-Dio.netty.tmpdir: " + file + " (%USERPROFILE%\\AppData\\Local\\Temp)");
                        return file;
                    }
                    file = NativeLibraryLoader.toDirectory(string + "\\Local Settings\\Temp");
                    if (file != null) {
                        logger.debug("-Dio.netty.tmpdir: " + file + " (%USERPROFILE%\\Local Settings\\Temp)");
                        return file;
                    }
                }
            } else {
                file = NativeLibraryLoader.toDirectory(System.getenv("TMPDIR"));
                if (file != null) {
                    logger.debug("-Dio.netty.tmpdir: " + file + " ($TMPDIR)");
                    return file;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        file = NativeLibraryLoader.isWindows() ? new File("C:\\Windows\\Temp") : new File("/tmp");
        logger.warn("Failed to get the temporary directory; falling back to: " + file);
        return file;
    }

    static {
        OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        String string = SystemPropertyUtil.get("io.netty.native.workdir");
        if (string != null) {
            File file = new File(string);
            file.mkdirs();
            try {
                file = file.getAbsoluteFile();
            }
            catch (Exception exception) {
                // empty catch block
            }
            WORKDIR = file;
            logger.debug("-Dio.netty.netty.workdir: " + WORKDIR);
        } else {
            WORKDIR = NativeLibraryLoader.tmpdir();
            logger.debug("-Dio.netty.netty.workdir: " + WORKDIR + " (io.netty.tmpdir)");
        }
    }

    private static boolean isOSX() {
        return OSNAME.startsWith("macosx") || OSNAME.startsWith("osx");
    }

    private static boolean isWindows() {
        return OSNAME.startsWith("windows");
    }

    private static File toDirectory(String string) {
        if (string == null) {
            return null;
        }
        File file = new File(string);
        file.mkdirs();
        if (!file.isDirectory()) {
            return null;
        }
        try {
            return file.getAbsoluteFile();
        }
        catch (Exception exception) {
            return file;
        }
    }

    private NativeLibraryLoader() {
    }
}

