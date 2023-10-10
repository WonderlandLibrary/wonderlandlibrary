// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.mac;

import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.Pointer;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class CFUtil
{
    private CFUtil() {
    }
    
    public static String cfPointerToString(final Pointer result) {
        return cfPointerToString(result, true);
    }
    
    public static String cfPointerToString(final Pointer result, final boolean returnUnknown) {
        String s = "";
        if (result != null) {
            final CoreFoundation.CFStringRef cfs = new CoreFoundation.CFStringRef(result);
            s = cfs.stringValue();
        }
        if (returnUnknown && s.isEmpty()) {
            return "unknown";
        }
        return s;
    }
}
