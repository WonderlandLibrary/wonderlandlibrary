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
    private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
    private static final String OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    private static final File WORKDIR;

    private static File tmpdir() {
        File f2;
        try {
            f2 = NativeLibraryLoader.toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (f2 != null) {
                logger.debug("-Dio.netty.tmpdir: " + f2);
                return f2;
            }
            f2 = NativeLibraryLoader.toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (f2 != null) {
                logger.debug("-Dio.netty.tmpdir: " + f2 + " (java.io.tmpdir)");
                return f2;
            }
            if (NativeLibraryLoader.isWindows()) {
                f2 = NativeLibraryLoader.toDirectory(System.getenv("TEMP"));
                if (f2 != null) {
                    logger.debug("-Dio.netty.tmpdir: " + f2 + " (%TEMP%)");
                    return f2;
                }
                String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f2 = NativeLibraryLoader.toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f2 != null) {
                        logger.debug("-Dio.netty.tmpdir: " + f2 + " (%USERPROFILE%\\AppData\\Local\\Temp)");
                        return f2;
                    }
                    f2 = NativeLibraryLoader.toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f2 != null) {
                        logger.debug("-Dio.netty.tmpdir: " + f2 + " (%USERPROFILE%\\Local Settings\\Temp)");
                        return f2;
                    }
                }
            } else {
                f2 = NativeLibraryLoader.toDirectory(System.getenv("TMPDIR"));
                if (f2 != null) {
                    logger.debug("-Dio.netty.tmpdir: " + f2 + " ($TMPDIR)");
                    return f2;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        f2 = NativeLibraryLoader.isWindows() ? new File("C:\\Windows\\Temp") : new File("/tmp");
        logger.warn("Failed to get the temporary directory; falling back to: " + f2);
        return f2;
    }

    private static File toDirectory(String path) {
        if (path == null) {
            return null;
        }
        File f2 = new File(path);
        f2.mkdirs();
        if (!f2.isDirectory()) {
            return null;
        }
        try {
            return f2.getAbsoluteFile();
        }
        catch (Exception ignored) {
            return f2;
        }
    }

    private static boolean isWindows() {
        return OSNAME.startsWith("windows");
    }

    private static boolean isOSX() {
        return OSNAME.startsWith("macosx") || OSNAME.startsWith("osx");
    }

    public static void load(String name, ClassLoader loader) {
        String libname = System.mapLibraryName(name);
        String path = NATIVE_RESOURCE_HOME + libname;
        URL url = loader.getResource(path);
        if (url == null && NativeLibraryLoader.isOSX()) {
            url = path.endsWith(".jnilib") ? loader.getResource("META-INF/native/lib" + name + ".dynlib") : loader.getResource("META-INF/native/lib" + name + ".jnilib");
        }
        if (url == null) {
            System.loadLibrary(name);
            return;
        }
        int index = libname.lastIndexOf(46);
        String prefix = libname.substring(0, index);
        String suffix = libname.substring(index, libname.length());
        InputStream in = null;
        OutputStream out = null;
        File tmpFile = null;
        boolean loaded = false;
        try {
            int length;
            tmpFile = File.createTempFile(prefix, suffix, WORKDIR);
            in = url.openStream();
            out = new FileOutputStream(tmpFile);
            byte[] buffer = new byte[8192];
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
            out = null;
            System.load(tmpFile.getPath());
            loaded = true;
        }
        catch (Exception e2) {
            throw (UnsatisfiedLinkError)new UnsatisfiedLinkError("could not load a native library: " + name).initCause(e2);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ignore) {}
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException ignore) {}
            }
            if (tmpFile != null) {
                if (loaded) {
                    tmpFile.deleteOnExit();
                } else if (!tmpFile.delete()) {
                    tmpFile.deleteOnExit();
                }
            }
        }
    }

    private NativeLibraryLoader() {
    }

    static {
        String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
        if (workdir != null) {
            File f2 = new File(workdir);
            f2.mkdirs();
            try {
                f2 = f2.getAbsoluteFile();
            }
            catch (Exception exception) {
                // empty catch block
            }
            WORKDIR = f2;
            logger.debug("-Dio.netty.netty.workdir: " + WORKDIR);
        } else {
            WORKDIR = NativeLibraryLoader.tmpdir();
            logger.debug("-Dio.netty.netty.workdir: " + WORKDIR + " (io.netty.tmpdir)");
        }
    }
}

