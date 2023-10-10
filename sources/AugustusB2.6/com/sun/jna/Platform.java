// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;

public final class Platform
{
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;
    public static final int SOLARIS = 3;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static final int AIX = 7;
    public static final int ANDROID = 8;
    public static final int GNU = 9;
    public static final int KFREEBSD = 10;
    public static final int NETBSD = 11;
    public static final boolean RO_FIELDS;
    public static final boolean HAS_BUFFERS;
    public static final boolean HAS_AWT;
    public static final boolean HAS_JAWT;
    public static final String MATH_LIBRARY_NAME;
    public static final String C_LIBRARY_NAME;
    public static final boolean HAS_DLL_CALLBACKS;
    public static final String RESOURCE_PREFIX;
    private static final int osType;
    public static final String ARCH;
    
    private Platform() {
    }
    
    public static final int getOSType() {
        return Platform.osType;
    }
    
    public static final boolean isMac() {
        return Platform.osType == 0;
    }
    
    public static final boolean isAndroid() {
        return Platform.osType == 8;
    }
    
    public static final boolean isLinux() {
        return Platform.osType == 1;
    }
    
    public static final boolean isAIX() {
        return Platform.osType == 7;
    }
    
    public static final boolean isWindowsCE() {
        return Platform.osType == 6;
    }
    
    public static final boolean isWindows() {
        return Platform.osType == 2 || Platform.osType == 6;
    }
    
    public static final boolean isSolaris() {
        return Platform.osType == 3;
    }
    
    public static final boolean isFreeBSD() {
        return Platform.osType == 4;
    }
    
    public static final boolean isOpenBSD() {
        return Platform.osType == 5;
    }
    
    public static final boolean isNetBSD() {
        return Platform.osType == 11;
    }
    
    public static final boolean isGNU() {
        return Platform.osType == 9;
    }
    
    public static final boolean iskFreeBSD() {
        return Platform.osType == 10;
    }
    
    public static final boolean isX11() {
        return !isWindows() && !isMac();
    }
    
    public static final boolean hasRuntimeExec() {
        return !isWindowsCE() || !"J9".equals(System.getProperty("java.vm.name"));
    }
    
    public static final boolean is64Bit() {
        final String model = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if (model != null) {
            return "64".equals(model);
        }
        return "x86-64".equals(Platform.ARCH) || "ia64".equals(Platform.ARCH) || "ppc64".equals(Platform.ARCH) || "ppc64le".equals(Platform.ARCH) || "sparcv9".equals(Platform.ARCH) || "mips64".equals(Platform.ARCH) || "mips64el".equals(Platform.ARCH) || "amd64".equals(Platform.ARCH) || "aarch64".equals(Platform.ARCH) || Native.POINTER_SIZE == 8;
    }
    
    public static final boolean isIntel() {
        return Platform.ARCH.startsWith("x86");
    }
    
    public static final boolean isPPC() {
        return Platform.ARCH.startsWith("ppc");
    }
    
    public static final boolean isARM() {
        return Platform.ARCH.startsWith("arm") || Platform.ARCH.startsWith("aarch");
    }
    
    public static final boolean isSPARC() {
        return Platform.ARCH.startsWith("sparc");
    }
    
    public static final boolean isMIPS() {
        return Platform.ARCH.equals("mips") || Platform.ARCH.equals("mips64") || Platform.ARCH.equals("mipsel") || Platform.ARCH.equals("mips64el");
    }
    
    static String getCanonicalArchitecture(String arch, final int platform) {
        arch = arch.toLowerCase().trim();
        if ("powerpc".equals(arch)) {
            arch = "ppc";
        }
        else if ("powerpc64".equals(arch)) {
            arch = "ppc64";
        }
        else if ("i386".equals(arch) || "i686".equals(arch)) {
            arch = "x86";
        }
        else if ("x86_64".equals(arch) || "amd64".equals(arch)) {
            arch = "x86-64";
        }
        else if ("zarch_64".equals(arch)) {
            arch = "s390x";
        }
        if ("ppc64".equals(arch) && "little".equals(System.getProperty("sun.cpu.endian"))) {
            arch = "ppc64le";
        }
        if ("arm".equals(arch) && platform == 1 && isSoftFloat()) {
            arch = "armel";
        }
        return arch;
    }
    
    static boolean isSoftFloat() {
        try {
            final File self = new File("/proc/self/exe");
            if (self.exists()) {
                final ELFAnalyser ahfd = ELFAnalyser.analyse(self.getCanonicalPath());
                return !ahfd.isArmHardFloat();
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Platform.class.getName()).log(Level.INFO, "Failed to read '/proc/self/exe' or the target binary.", ex);
        }
        catch (SecurityException ex2) {
            Logger.getLogger(Platform.class.getName()).log(Level.INFO, "SecurityException while analysing '/proc/self/exe' or the target binary.", ex2);
        }
        return false;
    }
    
    static String getNativeLibraryResourcePrefix() {
        final String prefix = System.getProperty("jna.prefix");
        if (prefix != null) {
            return prefix;
        }
        return getNativeLibraryResourcePrefix(getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
    }
    
    static String getNativeLibraryResourcePrefix(final int osType, String arch, final String name) {
        arch = getCanonicalArchitecture(arch, osType);
        String osPrefix = null;
        switch (osType) {
            case 8: {
                if (arch.startsWith("arm")) {
                    arch = "arm";
                }
                osPrefix = "android-" + arch;
                break;
            }
            case 2: {
                osPrefix = "win32-" + arch;
                break;
            }
            case 6: {
                osPrefix = "w32ce-" + arch;
                break;
            }
            case 0: {
                osPrefix = "darwin-" + arch;
                break;
            }
            case 1: {
                osPrefix = "linux-" + arch;
                break;
            }
            case 3: {
                osPrefix = "sunos-" + arch;
                break;
            }
            case 4: {
                osPrefix = "freebsd-" + arch;
                break;
            }
            case 5: {
                osPrefix = "openbsd-" + arch;
                break;
            }
            case 11: {
                osPrefix = "netbsd-" + arch;
                break;
            }
            case 10: {
                osPrefix = "kfreebsd-" + arch;
                break;
            }
            default: {
                osPrefix = name.toLowerCase();
                final int space = osPrefix.indexOf(" ");
                if (space != -1) {
                    osPrefix = osPrefix.substring(0, space);
                }
                osPrefix = osPrefix + "-" + arch;
                break;
            }
        }
        return osPrefix;
    }
    
    static {
        final String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux")) {
            if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
                osType = 8;
                System.setProperty("jna.nounpack", "true");
            }
            else {
                osType = 1;
            }
        }
        else if (osName.startsWith("AIX")) {
            osType = 7;
        }
        else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            osType = 0;
        }
        else if (osName.startsWith("Windows CE")) {
            osType = 6;
        }
        else if (osName.startsWith("Windows")) {
            osType = 2;
        }
        else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            osType = 3;
        }
        else if (osName.startsWith("FreeBSD")) {
            osType = 4;
        }
        else if (osName.startsWith("OpenBSD")) {
            osType = 5;
        }
        else if (osName.equalsIgnoreCase("gnu")) {
            osType = 9;
        }
        else if (osName.equalsIgnoreCase("gnu/kfreebsd")) {
            osType = 10;
        }
        else if (osName.equalsIgnoreCase("netbsd")) {
            osType = 11;
        }
        else {
            osType = -1;
        }
        boolean hasBuffers = false;
        try {
            Class.forName("java.nio.Buffer");
            hasBuffers = true;
        }
        catch (ClassNotFoundException ex) {}
        HAS_AWT = (Platform.osType != 6 && Platform.osType != 8 && Platform.osType != 7);
        HAS_JAWT = (Platform.HAS_AWT && Platform.osType != 0);
        HAS_BUFFERS = hasBuffers;
        RO_FIELDS = (Platform.osType != 6);
        C_LIBRARY_NAME = ((Platform.osType == 2) ? "msvcrt" : ((Platform.osType == 6) ? "coredll" : "c"));
        MATH_LIBRARY_NAME = ((Platform.osType == 2) ? "msvcrt" : ((Platform.osType == 6) ? "coredll" : "m"));
        ARCH = getCanonicalArchitecture(System.getProperty("os.arch"), Platform.osType);
        HAS_DLL_CALLBACKS = (Platform.osType == 2 && !Platform.ARCH.startsWith("aarch"));
        RESOURCE_PREFIX = getNativeLibraryResourcePrefix();
    }
}
