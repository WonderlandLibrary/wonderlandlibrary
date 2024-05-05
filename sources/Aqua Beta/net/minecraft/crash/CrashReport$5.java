package net.minecraft.crash;

import java.util.concurrent.Callable;

class CrashReport.5
implements Callable<String> {
    CrashReport.5() {
    }

    public String call() {
        Runtime runtime = Runtime.getRuntime();
        long i = runtime.maxMemory();
        long j = runtime.totalMemory();
        long k = runtime.freeMemory();
        long l = i / 1024L / 1024L;
        long i1 = j / 1024L / 1024L;
        long j1 = k / 1024L / 1024L;
        return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
    }
}
