package net.minecraft.crash;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.concurrent.Callable;

class CrashReport.6
implements Callable<String> {
    CrashReport.6() {
    }

    public String call() {
        RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
        List list = runtimemxbean.getInputArguments();
        int i = 0;
        StringBuilder stringbuilder = new StringBuilder();
        for (String s : list) {
            if (!s.startsWith("-X")) continue;
            if (i++ > 0) {
                stringbuilder.append(" ");
            }
            stringbuilder.append(s);
        }
        return String.format((String)"%d total; %s", (Object[])new Object[]{i, stringbuilder.toString()});
    }
}
