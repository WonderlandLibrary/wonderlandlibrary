package net.optifine.util;

public class MemoryMonitor {
    private static long startTimeMs = System.currentTimeMillis();
    private static long startMemory = MemoryMonitor.getMemoryUsed();
    private static long lastTimeMs = startTimeMs;
    private static long lastMemory = startMemory;
    private static boolean gcEvent = false;
    private static int memBytesSec = 0;
    private static long MB = 0x100000L;

    public static void update() {
        long i = System.currentTimeMillis();
        long j = MemoryMonitor.getMemoryUsed();
        boolean bl = gcEvent = j < lastMemory;
        if (gcEvent) {
            long l = lastMemory - startMemory;
            long k = lastTimeMs - startTimeMs;
            double d0 = (double)k / 1000.0;
            int i1 = (int)((double)l / d0);
            if (i1 > 0) {
                memBytesSec = i1;
            }
            startTimeMs = i;
            startMemory = j;
        }
        lastTimeMs = i;
        lastMemory = j;
    }

    private static long getMemoryUsed() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static long getStartTimeMs() {
        return startTimeMs;
    }

    public static long getStartMemoryMb() {
        return startMemory / MB;
    }

    public static boolean isGcEvent() {
        return gcEvent;
    }

    public static long getAllocationRateMb() {
        return (long)memBytesSec / MB;
    }
}
