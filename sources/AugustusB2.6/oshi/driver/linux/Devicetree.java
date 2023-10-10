// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux;

import oshi.util.FileUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Devicetree
{
    private Devicetree() {
    }
    
    public static String queryModel() {
        final String modelStr = FileUtil.getStringFromFile("/sys/firmware/devicetree/base/model");
        if (!modelStr.isEmpty()) {
            return modelStr.replace("Machine: ", "");
        }
        return null;
    }
}
