package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.world.storage.WorldInfo;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldInfo.5
implements Callable<String> {
    WorldInfo.5() {
    }

    public String call() throws Exception {
        return String.format((String)"%d game time, %d day time", (Object[])new Object[]{WorldInfo.access$600((WorldInfo)WorldInfo.this), WorldInfo.access$700((WorldInfo)WorldInfo.this)});
    }
}
