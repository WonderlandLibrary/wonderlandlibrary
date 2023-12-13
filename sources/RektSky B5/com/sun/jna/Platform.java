/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Native;

public final class Platform {
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;
    public static final int SOLARIS = 3;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static final boolean RO_FIELDS;
    public static final boolean HAS_BUFFERS;
    public static final boolean HAS_AWT;
    public static final String MATH_LIBRARY_NAME;
    public static final String C_LIBRARY_NAME;
    private static final int osType;

    private Platform() {
    }

    public static final int getOSType() {
        return osType;
    }

    public static final boolean isMac() {
        return osType == 0;
    }

    public static final boolean isLinux() {
        return osType == 1;
    }

    public static final boolean isWindowsCE() {
        return osType == 6;
    }

    public static final boolean isWindows() {
        return osType == 2 || osType == 6;
    }

    public static final boolean isSolaris() {
        return osType == 3;
    }

    public static final boolean isFreeBSD() {
        return osType == 4;
    }

    public static final boolean isOpenBSD() {
        return osType == 5;
    }

    public static final boolean isX11() {
        return !Platform.isWindows() && !Platform.isMac();
    }

    public static final boolean hasRuntimeExec() {
        return !Platform.isWindowsCE() || !"J9".equals(System.getProperty("java.vm.name"));
    }

    public static final boolean is64Bit() {
        String model = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
        if (model != null) {
            return "64".equals(model);
        }
        String arch = System.getProperty("os.arch").toLowerCase();
        if ("x86_64".equals(arch) || "ia64".equals(arch) || "ppc64".equals(arch) || "sparcv9".equals(arch) || "amd64".equals(arch)) {
            return true;
        }
        return Native.POINTER_SIZE == 8;
    }

    public static final boolean isIntel() {
        String arch = System.getProperty("os.arch").toLowerCase().trim();
        return arch.equals("i386") || arch.equals("x86") || arch.equals("x86_64") || arch.equals("amd64");
    }

    public static final boolean isPPC() {
        String arch = System.getProperty("os.arch").toLowerCase().trim();
        return arch.equals("ppc") || arch.equals("ppc64") || arch.equals("powerpc") || arch.equals("powerpc64");
    }

    public static final boolean isARM() {
        String arch = System.getProperty("os.arch").toLowerCase().trim();
        return arch.equals("arm");
    }

    static {
        String osName = System.getProperty("os.name");
        osType = osName.startsWith("Linux") ? 1 : (osName.startsWith("Mac") || osName.startsWith("Darwin") ? 0 : (osName.startsWith("Windows CE") ? 6 : (osName.startsWith("Windows") ? 2 : (osName.startsWith("Solaris") || osName.startsWith("SunOS") ? 3 : (osName.startsWith("FreeBSD") ? 4 : (osName.startsWith("OpenBSD") ? 5 : -1))))));
        boolean hasAWT = false;
        try {
            Class.forName("java.awt.Component");
            hasAWT = true;
        }
        catch (ClassNotFoundException e2) {
            // empty catch block
        }
        HAS_AWT = hasAWT;
        boolean hasBuffers = false;
        try {
            Class.forName("java.nio.Buffer");
            hasBuffers = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        HAS_BUFFERS = hasBuffers;
        boolean bl = RO_FIELDS = osType != 6;
        String string = osType == 2 ? "msvcrt" : (C_LIBRARY_NAME = osType == 6 ? "coredll" : "c");
        MATH_LIBRARY_NAME = osType == 2 ? "msvcrt" : (osType == 6 ? "coredll" : "m");
    }
}

