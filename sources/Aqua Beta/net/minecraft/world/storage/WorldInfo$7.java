package net.minecraft.world.storage;

import java.util.concurrent.Callable;
import net.minecraft.world.storage.WorldInfo;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldInfo.7
implements Callable<String> {
    WorldInfo.7() {
    }

    public String call() throws Exception {
        String s = "Unknown?";
        try {
            switch (WorldInfo.access$900((WorldInfo)WorldInfo.this)) {
                case 19132: {
                    s = "McRegion";
                    break;
                }
                case 19133: {
                    s = "Anvil";
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return String.format((String)"0x%05X - %s", (Object[])new Object[]{WorldInfo.access$900((WorldInfo)WorldInfo.this), s});
    }
}
