package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.world.gen.layer.IntCache;

class CrashReport.7
implements Callable<String> {
    CrashReport.7() {
    }

    public String call() throws Exception {
        return IntCache.getCacheSizes();
    }
}
