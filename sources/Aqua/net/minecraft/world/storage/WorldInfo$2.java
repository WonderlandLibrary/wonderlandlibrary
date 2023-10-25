package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.world.storage.WorldInfo;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldInfo.2
implements Callable<String> {
    WorldInfo.2() {
    }

    public String call() throws Exception {
        return String.format((String)"ID %02d - %s, ver %d. Features enabled: %b", (Object[])new Object[]{WorldInfo.access$000((WorldInfo)WorldInfo.this).getWorldTypeID(), WorldInfo.access$000((WorldInfo)WorldInfo.this).getWorldTypeName(), WorldInfo.access$000((WorldInfo)WorldInfo.this).getGeneratorVersion(), WorldInfo.access$100((WorldInfo)WorldInfo.this)});
    }
}
