package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.world.storage.WorldInfo;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldInfo.8
implements Callable<String> {
    WorldInfo.8() {
    }

    public String call() throws Exception {
        return String.format((String)"Rain time: %d (now: %b), thunder time: %d (now: %b)", (Object[])new Object[]{WorldInfo.access$1000((WorldInfo)WorldInfo.this), WorldInfo.access$1100((WorldInfo)WorldInfo.this), WorldInfo.access$1200((WorldInfo)WorldInfo.this), WorldInfo.access$1300((WorldInfo)WorldInfo.this)});
    }
}
