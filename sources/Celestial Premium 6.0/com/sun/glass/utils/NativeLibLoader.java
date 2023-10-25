/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class NativeLibLoader {
    private static final HashSet<String> loaded = new HashSet();
    private static boolean verbose = false;
    private static boolean usingModules = false;
    private static File libDir = null;
    private static String libPrefix = "";
    private static String libSuffix = "";

    public static synchronized void loadLibrary(String string) {
        if (!loaded.contains(string)) {
            StackWalker stackWalker = AccessController.doPrivileged(() -> StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE));
            Class<?> class_ = stackWalker.getCallerClass();
            NativeLibLoader.loadLibraryInternal(string, null, class_);
            loaded.add(string);
        }
    }

    public static synchronized void loadLibrary(String string, List<String> list) {
        if (!loaded.contains(string)) {
            StackWalker stackWalker = AccessController.doPrivileged(() -> StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE));
            Class<?> class_ = stackWalker.getCallerClass();
            NativeLibLoader.loadLibraryInternal(string, list, class_);
            loaded.add(string);
        }
    }

    private static String[] initializePath(String string) {
        String string2 = System.getProperty(string, "");
        String string3 = File.pathSeparator;
        int n = string2.length();
        int n2 = string2.indexOf(string3);
        int n3 = 0;
        while (n2 >= 0) {
            ++n3;
            n2 = string2.indexOf(string3, n2 + 1);
        }
        String[] arrstring = new String[n3 + 1];
        n2 = 0;
        n3 = 0;
        int n4 = string2.indexOf(string3);
        while (n4 >= 0) {
            if (n4 - n2 > 0) {
                arrstring[n3++] = string2.substring(n2, n4);
            } else if (n4 - n2 == 0) {
                arrstring[n3++] = ".";
            }
            n2 = n4 + 1;
            n4 = string2.indexOf(string3, n2);
        }
        arrstring[n3] = string2.substring(n2, n);
        return arrstring;
    }

    private static void loadLibraryInternal(String string, List<String> list, Class class_) {
        try {
            NativeLibLoader.loadLibraryFullPath(string);
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            if (verbose && !usingModules) {
                System.err.println("WARNING: " + unsatisfiedLinkError);
            }
            if (NativeLibLoader.loadLibraryFromResource(string, list, class_)) {
                return;
            }
            String[] arrstring = NativeLibLoader.initializePath("java.library.path");
            for (int i = 0; i < arrstring.length; ++i) {
                try {
                    Object object = arrstring[i];
                    if (!((String)object).endsWith(File.separator)) {
                        object = (String)object + File.separator;
                    }
                    String string2 = System.mapLibraryName(string);
                    File file = new File((String)object + string2);
                    System.load(file.getAbsolutePath());
                    if (verbose) {
                        System.err.println("Loaded " + file.getAbsolutePath() + " from java.library.path");
                    }
                    return;
                }
                catch (UnsatisfiedLinkError unsatisfiedLinkError2) {
                    continue;
                }
            }
            try {
                System.loadLibrary(string);
                if (verbose) {
                    System.err.println("System.loadLibrary(" + string + ") succeeded");
                }
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError3) {
                if ("ios".equals(System.getProperty("os.name").toLowerCase(Locale.ROOT)) && string.contains("-")) {
                    string = string.replace("-", "_");
                    System.loadLibrary(string);
                    return;
                }
                throw unsatisfiedLinkError3;
            }
        }
    }

    private static boolean loadLibraryFromResource(String string, List<String> list, Class class_) {
        return NativeLibLoader.installLibraryFromResource(string, list, class_, true);
    }

    private static boolean installLibraryFromResource(String string, List<String> list, Class class_, boolean bl) {
        try {
            Object object;
            Object object2;
            if (list != null) {
                object2 = list.iterator();
                while (object2.hasNext()) {
                    object = (String)object2.next();
                    boolean bl2 = NativeLibLoader.installLibraryFromResource((String)object, null, class_, false);
                }
            }
            if ((object = class_.getResourceAsStream((String)(object2 = "/" + System.mapLibraryName(string)))) != null) {
                String string2 = NativeLibLoader.cacheLibrary((InputStream)object, (String)object2, class_);
                if (bl) {
                    System.load(string2);
                    if (verbose) {
                        System.err.println("Loaded library " + (String)object2 + " from resource");
                    }
                } else if (verbose) {
                    System.err.println("Unpacked library " + (String)object2 + " from resource");
                }
                return true;
            }
        }
        catch (Throwable throwable) {
            System.err.println("Loading library " + string + " from resource failed: " + throwable);
            throwable.printStackTrace();
        }
        return false;
    }

    private static String cacheLibrary(InputStream inputStream, String string, Class class_) throws IOException {
        Object object;
        Object object2;
        String string2 = System.getProperty("javafx.version", "versionless");
        Object object3 = System.getProperty("javafx.cachedir", "");
        if (((String)object3).isEmpty()) {
            object3 = System.getProperty("user.home") + "/.openjfx/cache/" + string2;
        }
        File file = new File((String)object3);
        boolean bl = true;
        if (file.exists()) {
            if (!file.isDirectory()) {
                System.err.println("Cache exists but is not a directory: " + file);
                bl = false;
            }
        } else if (!file.mkdirs()) {
            System.err.println("Can not create cache at " + file);
            bl = false;
        }
        if (!file.canRead()) {
            bl = false;
        }
        if (!bl) {
            object2 = System.getProperty("user.name", "anonymous");
            String string3 = System.getProperty("java.io.tmpdir") + "/.openjfx_" + (String)object2 + "/cache/" + string2;
            file = new File(string3);
            if (file.exists()) {
                if (!file.isDirectory()) {
                    throw new IOException("Cache exists but is not a directory: " + file);
                }
            } else if (!file.mkdirs()) {
                throw new IOException("Can not create cache at " + file);
            }
        }
        object2 = new File(file, string);
        boolean bl2 = true;
        if (((File)object2).exists()) {
            try {
                DigestInputStream digestInputStream = new DigestInputStream(inputStream, MessageDigest.getInstance("MD5"));
                digestInputStream.getMessageDigest().reset();
                byte[] arrby = new byte[4096];
                while (digestInputStream.read(arrby) != -1) {
                }
                object = digestInputStream.getMessageDigest().digest();
                inputStream.close();
                inputStream = class_.getResourceAsStream(string);
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                object = new byte[1];
            }
            byte[] arrby = NativeLibLoader.calculateCheckSum((File)object2);
            if (!Arrays.equals((byte[])object, arrby)) {
                Files.delete(((File)object2).toPath());
            } else {
                bl2 = false;
            }
        }
        if (bl2) {
            object = ((File)object2).toPath();
            Files.copy(inputStream, (Path)object, new CopyOption[0]);
        }
        object = ((File)object2).getAbsolutePath();
        return object;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    static byte[] calculateCheckSum(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file);){
            byte[] arrby;
            try (DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, MessageDigest.getInstance("MD5"));){
                digestInputStream.getMessageDigest().reset();
                byte[] arrby2 = new byte[4096];
                while (digestInputStream.read(arrby2) != -1) {
                }
                arrby = digestInputStream.getMessageDigest().digest();
            }
            return arrby;
        }
        catch (IOException | IllegalArgumentException | SecurityException | NoSuchAlgorithmException exception) {
            return new byte[0];
        }
    }

    private static void loadLibraryFullPath(String string) {
        try {
            Object object;
            Class<NativeLibLoader> class_;
            if (usingModules) {
                throw new UnsatisfiedLinkError("ignored");
            }
            if (libDir == null) {
                class_ = NativeLibLoader.class;
                object = "NativeLibLoader.class";
                String string2 = class_.getResource((String)object).toString();
                if (string2.startsWith("jrt:")) {
                    usingModules = true;
                    throw new UnsatisfiedLinkError("ignored");
                }
                if (!string2.startsWith("jar:file:") || string2.indexOf(33) == -1) {
                    throw new UnsatisfiedLinkError("Invalid URL for class: " + string2);
                }
                String string3 = string2.substring(4, string2.lastIndexOf(33));
                int n = Math.max(string3.lastIndexOf(47), string3.lastIndexOf(92));
                String string4 = System.getProperty("os.name");
                String string5 = null;
                if (string4.startsWith("Windows")) {
                    string5 = "../bin";
                } else if (string4.startsWith("Mac")) {
                    string5 = ".";
                } else if (string4.startsWith("Linux")) {
                    string5 = ".";
                }
                String string6 = string3.substring(0, n) + "/" + string5;
                libDir = new File(new URI(string6).getPath());
                if (string4.startsWith("Windows")) {
                    libPrefix = "";
                    libSuffix = ".dll";
                } else if (string4.startsWith("Mac")) {
                    libPrefix = "lib";
                    libSuffix = ".dylib";
                } else if (string4.startsWith("Linux")) {
                    libPrefix = "lib";
                    libSuffix = ".so";
                }
            }
            object = new File(libDir, libPrefix + string + libSuffix);
            class_ = ((File)object).getCanonicalPath();
            System.load((String)((Object)class_));
            if (verbose) {
                System.err.println("Loaded " + ((File)object).getAbsolutePath() + " from relative path");
            }
        }
        catch (Exception exception) {
            throw (UnsatisfiedLinkError)new UnsatisfiedLinkError().initCause(exception);
        }
    }

    static {
        Object object = AccessController.doPrivileged(() -> {
            verbose = Boolean.getBoolean("javafx.verbose");
            return null;
        });
    }
}

