package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.world.storage.WorldInfo;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldInfo.9
implements Callable<String> {
    WorldInfo.9() {
    }

    public String call() throws Exception {
        return String.format((String)"Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", (Object[])new Object[]{WorldInfo.access$1400((WorldInfo)WorldInfo.this).getName(), WorldInfo.access$1400((WorldInfo)WorldInfo.this).getID(), WorldInfo.access$1500((WorldInfo)WorldInfo.this), WorldInfo.access$1600((WorldInfo)WorldInfo.this)});
    }
}
