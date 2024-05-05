package net.minecraft.crash;

import java.util.concurrent.Callable;

class CrashReport.2
implements Callable<String> {
    CrashReport.2() {
    }

    public String call() {
        return System.getProperty((String)"os.name") + " (" + System.getProperty((String)"os.arch") + ") version " + System.getProperty((String)"os.version");
    }
}
