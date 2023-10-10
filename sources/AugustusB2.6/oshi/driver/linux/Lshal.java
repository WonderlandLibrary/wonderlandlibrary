// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Lshal
{
    private Lshal() {
    }
    
    public static String querySerialNumber() {
        final String marker = "system.hardware.serial =";
        for (final String checkLine : ExecutingCommand.runNative("lshal")) {
            if (checkLine.contains(marker)) {
                return ParseUtil.getSingleQuoteStringValue(checkLine);
            }
        }
        return null;
    }
    
    public static String queryUUID() {
        final String marker = "system.hardware.uuid =";
        for (final String checkLine : ExecutingCommand.runNative("lshal")) {
            if (checkLine.contains(marker)) {
                return ParseUtil.getSingleQuoteStringValue(checkLine);
            }
        }
        return null;
    }
}
