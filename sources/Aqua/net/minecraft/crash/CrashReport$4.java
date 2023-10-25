package net.minecraft.crash;

import java.util.concurrent.Callable;

class CrashReport.4
implements Callable<String> {
    CrashReport.4() {
    }

    public String call() {
        return System.getProperty((String)"java.vm.name") + " (" + System.getProperty((String)"java.vm.info") + "), " + System.getProperty((String)"java.vm.vendor");
    }
}
