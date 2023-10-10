// 
// Decompiled by Procyon v0.5.36
// 

package oshi;

public enum PlatformEnum
{
    MACOS("macOS"), 
    LINUX("Linux"), 
    WINDOWS("Windows"), 
    SOLARIS("Solaris"), 
    FREEBSD("FreeBSD"), 
    OPENBSD("OpenBSD"), 
    WINDOWSCE("Windows CE"), 
    AIX("AIX"), 
    ANDROID("Android"), 
    GNU("GNU"), 
    KFREEBSD("kFreeBSD"), 
    NETBSD("NetBSD"), 
    UNKNOWN("Unknown");
    
    private final String name;
    
    private PlatformEnum(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static String getName(final int osType) {
        return getValue(osType).getName();
    }
    
    public static PlatformEnum getValue(final int osType) {
        if (osType < 0 || osType >= PlatformEnum.UNKNOWN.ordinal()) {
            return PlatformEnum.UNKNOWN;
        }
        return values()[osType];
    }
    
    private static /* synthetic */ PlatformEnum[] $values() {
        return new PlatformEnum[] { PlatformEnum.MACOS, PlatformEnum.LINUX, PlatformEnum.WINDOWS, PlatformEnum.SOLARIS, PlatformEnum.FREEBSD, PlatformEnum.OPENBSD, PlatformEnum.WINDOWSCE, PlatformEnum.AIX, PlatformEnum.ANDROID, PlatformEnum.GNU, PlatformEnum.KFREEBSD, PlatformEnum.NETBSD, PlatformEnum.UNKNOWN };
    }
    
    static {
        $VALUES = $values();
    }
}
