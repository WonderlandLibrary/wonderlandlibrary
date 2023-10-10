// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix;

import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.ArrayList;
import java.util.Collections;
import oshi.util.ExecutingCommand;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Xrandr
{
    private static final String[] XRANDR_VERBOSE;
    
    private Xrandr() {
    }
    
    public static List<byte[]> getEdidArrays() {
        final List<String> xrandr = ExecutingCommand.runNative(Xrandr.XRANDR_VERBOSE, null);
        if (xrandr.isEmpty()) {
            return Collections.emptyList();
        }
        final List<byte[]> displays = new ArrayList<byte[]>();
        StringBuilder sb = null;
        for (final String s : xrandr) {
            if (s.contains("EDID")) {
                sb = new StringBuilder();
            }
            else {
                if (sb == null) {
                    continue;
                }
                sb.append(s.trim());
                if (sb.length() < 256) {
                    continue;
                }
                final String edidStr = sb.toString();
                final byte[] edid = ParseUtil.hexStringToByteArray(edidStr);
                if (edid.length >= 128) {
                    displays.add(edid);
                }
                sb = null;
            }
        }
        return displays;
    }
    
    static {
        XRANDR_VERBOSE = new String[] { "xrandr", "--verbose" };
    }
}
