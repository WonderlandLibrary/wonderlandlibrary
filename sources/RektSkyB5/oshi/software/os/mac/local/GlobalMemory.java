/*
 * Decompiled with CFR 0.152.
 */
package oshi.software.os.mac.local;

import oshi.hardware.Memory;
import oshi.util.ExecutingCommand;

public class GlobalMemory
implements Memory {
    private long totalMemory = 0L;

    public long getAvailable() {
        long returnCurrentUsageMemory = 0L;
        for (String line : ExecutingCommand.runNative("vm_stat")) {
            String[] memorySplit;
            if (line.startsWith("Pages free:")) {
                memorySplit = line.split(":\\s+");
                returnCurrentUsageMemory += new Long(memorySplit[1].replace(".", "")).longValue();
                continue;
            }
            if (!line.startsWith("Pages speculative:")) continue;
            memorySplit = line.split(":\\s+");
            returnCurrentUsageMemory += new Long(memorySplit[1].replace(".", "")).longValue();
        }
        return returnCurrentUsageMemory *= 4096L;
    }

    public long getTotal() {
        if (this.totalMemory == 0L) {
            this.totalMemory = new Long(ExecutingCommand.getFirstAnswer("sysctl -n hw.memsize"));
        }
        return this.totalMemory;
    }
}

