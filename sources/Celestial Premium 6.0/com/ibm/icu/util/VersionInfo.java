/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.LocaleData;
import com.ibm.icu.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class VersionInfo
implements Comparable<VersionInfo> {
    public static final VersionInfo UNICODE_1_0;
    public static final VersionInfo UNICODE_1_0_1;
    public static final VersionInfo UNICODE_1_1_0;
    public static final VersionInfo UNICODE_1_1_5;
    public static final VersionInfo UNICODE_2_0;
    public static final VersionInfo UNICODE_2_1_2;
    public static final VersionInfo UNICODE_2_1_5;
    public static final VersionInfo UNICODE_2_1_8;
    public static final VersionInfo UNICODE_2_1_9;
    public static final VersionInfo UNICODE_3_0;
    public static final VersionInfo UNICODE_3_0_1;
    public static final VersionInfo UNICODE_3_1_0;
    public static final VersionInfo UNICODE_3_1_1;
    public static final VersionInfo UNICODE_3_2;
    public static final VersionInfo UNICODE_4_0;
    public static final VersionInfo UNICODE_4_0_1;
    public static final VersionInfo UNICODE_4_1;
    public static final VersionInfo UNICODE_5_0;
    public static final VersionInfo UNICODE_5_1;
    public static final VersionInfo UNICODE_5_2;
    public static final VersionInfo UNICODE_6_0;
    public static final VersionInfo UNICODE_6_1;
    public static final VersionInfo UNICODE_6_2;
    public static final VersionInfo ICU_VERSION;
    public static final String ICU_DATA_VERSION_PATH = "51b";
    public static final VersionInfo ICU_DATA_VERSION;
    public static final VersionInfo UCOL_RUNTIME_VERSION;
    public static final VersionInfo UCOL_BUILDER_VERSION;
    public static final VersionInfo UCOL_TAILORINGS_VERSION;
    private static volatile VersionInfo javaVersion;
    private static final VersionInfo UNICODE_VERSION;
    private int m_version_;
    private static final ConcurrentHashMap<Integer, VersionInfo> MAP_;
    private static final int LAST_BYTE_MASK_ = 255;
    private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";

    public static VersionInfo getInstance(String version) {
        int index;
        int length = version.length();
        int[] array = new int[]{0, 0, 0, 0};
        int count = 0;
        for (index = 0; count < 4 && index < length; ++index) {
            char c = version.charAt(index);
            if (c == '.') {
                ++count;
                continue;
            }
            if ((c = (char)(c - 48)) < '\u0000' || c > '\t') {
                throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
            }
            int n = count;
            array[n] = array[n] * 10;
            int n2 = count;
            array[n2] = array[n2] + c;
        }
        if (index != length) {
            throw new IllegalArgumentException("Invalid version number: String '" + version + "' exceeds version format");
        }
        for (int i = 0; i < 4; ++i) {
            if (array[i] >= 0 && array[i] <= 255) continue;
            throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
        }
        return VersionInfo.getInstance(array[0], array[1], array[2], array[3]);
    }

    public static VersionInfo getInstance(int major, int minor, int milli, int micro) {
        VersionInfo tmpvi;
        if (major < 0 || major > 255 || minor < 0 || minor > 255 || milli < 0 || milli > 255 || micro < 0 || micro > 255) {
            throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
        }
        int version = VersionInfo.getInt(major, minor, milli, micro);
        Integer key = version;
        VersionInfo result = MAP_.get(key);
        if (result == null && (tmpvi = MAP_.putIfAbsent(key, result = new VersionInfo(version))) != null) {
            result = tmpvi;
        }
        return result;
    }

    public static VersionInfo getInstance(int major, int minor, int milli) {
        return VersionInfo.getInstance(major, minor, milli, 0);
    }

    public static VersionInfo getInstance(int major, int minor) {
        return VersionInfo.getInstance(major, minor, 0, 0);
    }

    public static VersionInfo getInstance(int major) {
        return VersionInfo.getInstance(major, 0, 0, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static VersionInfo javaVersion() {
        if (javaVersion != null) return javaVersion;
        Class<VersionInfo> class_ = VersionInfo.class;
        synchronized (VersionInfo.class) {
            if (javaVersion != null) return javaVersion;
            String s = System.getProperty("java.version");
            char[] chars = s.toCharArray();
            int r = 0;
            int w = 0;
            int count = 0;
            boolean numeric = false;
            while (r < chars.length) {
                char c;
                if ((c = chars[r++]) < '0' || c > '9') {
                    if (!numeric) continue;
                    if (count == 3) break;
                    numeric = false;
                    chars[w++] = 46;
                    ++count;
                    continue;
                }
                numeric = true;
                chars[w++] = c;
            }
            while (w > 0 && chars[w - 1] == '.') {
                --w;
            }
            String vs = new String(chars, 0, w);
            javaVersion = VersionInfo.getInstance(vs);
            // ** MonitorExit[var0] (shouldn't be in output)
            return javaVersion;
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder(7);
        result.append(this.getMajor());
        result.append('.');
        result.append(this.getMinor());
        result.append('.');
        result.append(this.getMilli());
        result.append('.');
        result.append(this.getMicro());
        return result.toString();
    }

    public int getMajor() {
        return this.m_version_ >> 24 & 0xFF;
    }

    public int getMinor() {
        return this.m_version_ >> 16 & 0xFF;
    }

    public int getMilli() {
        return this.m_version_ >> 8 & 0xFF;
    }

    public int getMicro() {
        return this.m_version_ & 0xFF;
    }

    public boolean equals(Object other) {
        return other == this;
    }

    @Override
    public int compareTo(VersionInfo other) {
        return this.m_version_ - other.m_version_;
    }

    private VersionInfo(int compactversion) {
        this.m_version_ = compactversion;
    }

    private static int getInt(int major, int minor, int milli, int micro) {
        return major << 24 | minor << 16 | milli << 8 | micro;
    }

    public static void main(String[] args) {
        String icuApiVer;
        if (ICU_VERSION.getMajor() <= 4) {
            if (ICU_VERSION.getMinor() % 2 != 0) {
                int major = ICU_VERSION.getMajor();
                int minor = ICU_VERSION.getMinor() + 1;
                if (minor >= 10) {
                    minor -= 10;
                    ++major;
                }
                icuApiVer = "" + major + "." + minor + "M" + ICU_VERSION.getMilli();
            } else {
                icuApiVer = ICU_VERSION.getVersionString(2, 2);
            }
        } else {
            icuApiVer = ICU_VERSION.getMinor() == 0 ? "" + ICU_VERSION.getMajor() + "M" + ICU_VERSION.getMilli() : ICU_VERSION.getVersionString(2, 2);
        }
        System.out.println("International Components for Unicode for Java " + icuApiVer);
        System.out.println("");
        System.out.println("Implementation Version: " + ICU_VERSION.getVersionString(2, 4));
        System.out.println("Unicode Data Version:   " + UNICODE_VERSION.getVersionString(2, 4));
        System.out.println("CLDR Data Version:      " + LocaleData.getCLDRVersion().getVersionString(2, 4));
        System.out.println("Time Zone Data Version: " + TimeZone.getTZDataVersion());
    }

    private String getVersionString(int minDigits, int maxDigits) {
        int numDigits;
        if (minDigits < 1 || maxDigits < 1 || minDigits > 4 || maxDigits > 4 || minDigits > maxDigits) {
            throw new IllegalArgumentException("Invalid min/maxDigits range");
        }
        int[] digits = new int[]{this.getMajor(), this.getMinor(), this.getMilli(), this.getMicro()};
        for (numDigits = maxDigits; numDigits > minDigits && digits[numDigits - 1] == 0; --numDigits) {
        }
        StringBuilder verStr = new StringBuilder(7);
        verStr.append(digits[0]);
        for (int i = 1; i < numDigits; ++i) {
            verStr.append(".");
            verStr.append(digits[i]);
        }
        return verStr.toString();
    }

    static {
        MAP_ = new ConcurrentHashMap();
        UNICODE_1_0 = VersionInfo.getInstance(1, 0, 0, 0);
        UNICODE_1_0_1 = VersionInfo.getInstance(1, 0, 1, 0);
        UNICODE_1_1_0 = VersionInfo.getInstance(1, 1, 0, 0);
        UNICODE_1_1_5 = VersionInfo.getInstance(1, 1, 5, 0);
        UNICODE_2_0 = VersionInfo.getInstance(2, 0, 0, 0);
        UNICODE_2_1_2 = VersionInfo.getInstance(2, 1, 2, 0);
        UNICODE_2_1_5 = VersionInfo.getInstance(2, 1, 5, 0);
        UNICODE_2_1_8 = VersionInfo.getInstance(2, 1, 8, 0);
        UNICODE_2_1_9 = VersionInfo.getInstance(2, 1, 9, 0);
        UNICODE_3_0 = VersionInfo.getInstance(3, 0, 0, 0);
        UNICODE_3_0_1 = VersionInfo.getInstance(3, 0, 1, 0);
        UNICODE_3_1_0 = VersionInfo.getInstance(3, 1, 0, 0);
        UNICODE_3_1_1 = VersionInfo.getInstance(3, 1, 1, 0);
        UNICODE_3_2 = VersionInfo.getInstance(3, 2, 0, 0);
        UNICODE_4_0 = VersionInfo.getInstance(4, 0, 0, 0);
        UNICODE_4_0_1 = VersionInfo.getInstance(4, 0, 1, 0);
        UNICODE_4_1 = VersionInfo.getInstance(4, 1, 0, 0);
        UNICODE_5_0 = VersionInfo.getInstance(5, 0, 0, 0);
        UNICODE_5_1 = VersionInfo.getInstance(5, 1, 0, 0);
        UNICODE_5_2 = VersionInfo.getInstance(5, 2, 0, 0);
        UNICODE_6_0 = VersionInfo.getInstance(6, 0, 0, 0);
        UNICODE_6_1 = VersionInfo.getInstance(6, 1, 0, 0);
        UNICODE_6_2 = VersionInfo.getInstance(6, 2, 0, 0);
        ICU_VERSION = VersionInfo.getInstance(51, 2, 0, 0);
        ICU_DATA_VERSION = VersionInfo.getInstance(51, 2, 0, 0);
        UNICODE_VERSION = UNICODE_6_2;
        UCOL_RUNTIME_VERSION = VersionInfo.getInstance(7);
        UCOL_BUILDER_VERSION = VersionInfo.getInstance(8);
        UCOL_TAILORINGS_VERSION = VersionInfo.getInstance(1);
    }
}

