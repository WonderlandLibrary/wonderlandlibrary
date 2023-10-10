// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.message;

import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.message.ThreadInformation;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.management.ThreadInfo;
import org.apache.logging.log4j.message.ThreadDumpMessage;

public class ExtendedThreadInfoFactory implements ThreadDumpMessage.ThreadInfoFactory
{
    public ExtendedThreadInfoFactory() {
        final Method[] methods = ThreadInfo.class.getMethods();
        boolean basic = true;
        for (final Method method : methods) {
            if (method.getName().equals("getLockInfo")) {
                basic = false;
                break;
            }
        }
        if (basic) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
        final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        final ThreadInfo[] array = bean.dumpAllThreads(true, true);
        final Map<ThreadInformation, StackTraceElement[]> threads = new HashMap<ThreadInformation, StackTraceElement[]>(array.length);
        for (final ThreadInfo info : array) {
            threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
        }
        return threads;
    }
}
