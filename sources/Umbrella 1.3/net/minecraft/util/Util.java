/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

public class Util {
    private static final String __OBFID = "CL_00001633";

    public static EnumOS getOSType() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.OSX : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public static enum EnumOS {
        LINUX("LINUX", 0),
        SOLARIS("SOLARIS", 1),
        WINDOWS("WINDOWS", 2),
        OSX("OSX", 3),
        UNKNOWN("UNKNOWN", 4);

        private static final EnumOS[] $VALUES;
        private static final String __OBFID = "CL_00001660";

        static {
            $VALUES = new EnumOS[]{LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN};
        }

        private EnumOS(String p_i1357_1_, int p_i1357_2_) {
        }
    }
}

