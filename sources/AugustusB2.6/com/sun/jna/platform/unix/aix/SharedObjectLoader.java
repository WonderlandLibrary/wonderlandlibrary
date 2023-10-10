// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.unix.aix;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.sun.jna.Native;

final class SharedObjectLoader
{
    private SharedObjectLoader() {
    }
    
    static Perfstat getPerfstatInstance() {
        final Map<String, Object> options = getOptions();
        try {
            return Native.load("/usr/lib/libperfstat.a(shr_64.o)", Perfstat.class, options);
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            return Native.load("/usr/lib/libperfstat.a(shr.o)", Perfstat.class, options);
        }
    }
    
    private static Map<String, Object> getOptions() {
        final int RTLD_MEMBER = 262144;
        final int RTLD_GLOBAL = 65536;
        final int RTLD_LAZY = 4;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("open-flags", RTLD_MEMBER | RTLD_GLOBAL | RTLD_LAZY);
        return Collections.unmodifiableMap((Map<? extends String, ?>)options);
    }
}
