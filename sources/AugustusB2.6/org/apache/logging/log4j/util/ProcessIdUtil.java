// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.lang.reflect.Method;
import java.io.IOException;
import java.io.File;

public class ProcessIdUtil
{
    public static final String DEFAULT_PROCESSID = "-";
    
    public static String getProcessId() {
        try {
            final Class<?> managementFactoryClass = Class.forName("java.lang.management.ManagementFactory");
            final Method getRuntimeMXBean = managementFactoryClass.getDeclaredMethod("getRuntimeMXBean", (Class<?>[])new Class[0]);
            final Class<?> runtimeMXBeanClass = Class.forName("java.lang.management.RuntimeMXBean");
            final Method getName = runtimeMXBeanClass.getDeclaredMethod("getName", (Class<?>[])new Class[0]);
            final Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
            final String name = (String)getName.invoke(runtimeMXBean, new Object[0]);
            return name.split("@")[0];
        }
        catch (Exception ex) {
            try {
                return new File("/proc/self").getCanonicalFile().getName();
            }
            catch (IOException ex2) {
                return "-";
            }
        }
    }
}
