package net.minecraft.crash;

import java.util.concurrent.Callable;

class CrashReport.3
implements Callable<String> {
    CrashReport.3() {
    }

    public String call() {
        return System.getProperty((String)"java.version") + ", " + System.getProperty((String)"java.vendor");
    }
}
