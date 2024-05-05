/*
 * Decompiled with CFR 0.152.
 */
package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.scijava.nativelib.DefaultJniExtractor;

public class NativeLibraryUtil {
    private static Architecture architecture = Architecture.UNKNOWN;
    private static final String DELIM = "/";
    private static final String JAVA_TMPDIR = "java.io.tmpdir";
    private static final Logger LOGGER = Logger.getLogger("org.scijava.nativelib.NativeLibraryUtil");

    public static Architecture getArchitecture() {
        Processor processor;
        if (Architecture.UNKNOWN == architecture && Processor.UNKNOWN != (processor = NativeLibraryUtil.getProcessor())) {
            String name = System.getProperty("os.name").toLowerCase();
            if (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0) {
                if (Processor.INTEL_32 == processor) {
                    architecture = Architecture.LINUX_32;
                } else if (Processor.INTEL_64 == processor) {
                    architecture = Architecture.LINUX_64;
                }
            } else if (name.indexOf("win") >= 0) {
                if (Processor.INTEL_32 == processor) {
                    architecture = Architecture.WINDOWS_32;
                } else if (Processor.INTEL_64 == processor) {
                    architecture = Architecture.WINDOWS_64;
                }
            } else if (name.indexOf("mac") >= 0) {
                if (Processor.INTEL_32 == processor) {
                    architecture = Architecture.OSX_32;
                } else if (Processor.INTEL_64 == processor) {
                    architecture = Architecture.OSX_64;
                } else if (Processor.PPC == processor) {
                    architecture = Architecture.OSX_PPC;
                }
            }
        }
        LOGGER.log(Level.FINE, "architecture is " + (Object)((Object)architecture) + " os.name is " + System.getProperty("os.name").toLowerCase());
        return architecture;
    }

    private static Processor getProcessor() {
        Processor processor = Processor.UNKNOWN;
        String arch = System.getProperty("os.arch").toLowerCase();
        if (arch.indexOf("ppc") >= 0) {
            processor = Processor.PPC;
        } else if (arch.indexOf("86") >= 0 || arch.indexOf("amd") >= 0) {
            int bits = 32;
            if (arch.indexOf("64") >= 0) {
                bits = 64;
            }
            processor = 32 == bits ? Processor.INTEL_32 : Processor.INTEL_64;
        }
        LOGGER.log(Level.FINE, "processor is " + (Object)((Object)processor) + " os.arch is " + System.getProperty("os.arch").toLowerCase());
        return processor;
    }

    public static String getPlatformLibraryPath() {
        String path = "META-INF/lib/";
        path = path + NativeLibraryUtil.getArchitecture().name().toLowerCase() + DELIM;
        LOGGER.log(Level.FINE, "platform specific path is " + path);
        return path;
    }

    public static String getPlatformLibraryName(String libName) {
        String name = null;
        switch (NativeLibraryUtil.getArchitecture()) {
            case LINUX_32: 
            case LINUX_64: {
                name = libName + ".so";
                break;
            }
            case WINDOWS_32: 
            case WINDOWS_64: {
                name = libName + ".dll";
                break;
            }
            case OSX_32: 
            case OSX_64: {
                name = "lib" + libName + ".dylib";
            }
        }
        LOGGER.log(Level.FINE, "native library name " + name);
        return name;
    }

    public static String getVersionedLibraryName(Class libraryJarClass, String libName) {
        String version = libraryJarClass.getPackage().getImplementationVersion();
        if (null != version && version.length() > 0) {
            libName = libName + "-" + version;
        }
        return libName;
    }

    public static boolean loadVersionedNativeLibrary(Class libraryJarClass, String libName) {
        libName = NativeLibraryUtil.getVersionedLibraryName(libraryJarClass, libName);
        return NativeLibraryUtil.loadNativeLibrary(libraryJarClass, libName);
    }

    public static boolean loadNativeLibrary(Class libraryJarClass, String libName) {
        boolean success = false;
        if (Architecture.UNKNOWN == NativeLibraryUtil.getArchitecture()) {
            LOGGER.log(Level.WARNING, "No native library available for this platform.");
        } else {
            try {
                String tmpDirectory = System.getProperty(JAVA_TMPDIR);
                DefaultJniExtractor jniExtractor = new DefaultJniExtractor(libraryJarClass, tmpDirectory);
                File extractedFile = jniExtractor.extractJni(NativeLibraryUtil.getPlatformLibraryPath(), libName);
                System.load(extractedFile.getPath());
                success = true;
            }
            catch (IOException e) {
                LOGGER.log(Level.WARNING, "IOException creating DefaultJniExtractor", e);
            }
            catch (SecurityException e) {
                LOGGER.log(Level.WARNING, "Can't load dynamic library", e);
            }
            catch (UnsatisfiedLinkError e) {
                LOGGER.log(Level.WARNING, "Problem with library", e);
            }
        }
        return success;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static enum Processor {
        UNKNOWN,
        INTEL_32,
        INTEL_64,
        PPC;

    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Architecture {
        UNKNOWN,
        LINUX_32,
        LINUX_64,
        WINDOWS_32,
        WINDOWS_64,
        OSX_32,
        OSX_64,
        OSX_PPC;

    }
}

