package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.world.storage.WorldInfo;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldInfo.4
implements Callable<String> {
    WorldInfo.4() {
    }

    public String call() throws Exception {
        return CrashReportCategory.getCoordinateInfo((double)WorldInfo.access$300((WorldInfo)WorldInfo.this), (double)WorldInfo.access$400((WorldInfo)WorldInfo.this), (double)WorldInfo.access$500((WorldInfo)WorldInfo.this));
    }
}
