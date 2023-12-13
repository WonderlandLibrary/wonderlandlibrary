/*
 * Decompiled with CFR 0.152.
 */
package oshi.software.os.linux.proc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import oshi.hardware.Memory;

public class GlobalMemory
implements Memory {
    private long totalMemory = 0L;

    public long getAvailable() {
        long returnCurrentUsageMemory = 0L;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("/proc/meminfo"));
        }
        catch (FileNotFoundException e2) {
            return returnCurrentUsageMemory;
        }
        in.useDelimiter("\n");
        while (in.hasNext()) {
            String checkLine = in.next();
            if (!checkLine.startsWith("MemFree:") && !checkLine.startsWith("MemAvailable:")) continue;
            String[] memorySplit = checkLine.split("\\s+");
            returnCurrentUsageMemory = new Long(memorySplit[1]);
            if (memorySplit[2].equals("kB")) {
                returnCurrentUsageMemory *= 1024L;
            }
            if (!memorySplit[0].equals("MemAvailable:")) continue;
            break;
        }
        in.close();
        return returnCurrentUsageMemory;
    }

    public long getTotal() {
        if (this.totalMemory == 0L) {
            Scanner in = null;
            try {
                in = new Scanner(new FileReader("/proc/meminfo"));
            }
            catch (FileNotFoundException e2) {
                this.totalMemory = 0L;
                return this.totalMemory;
            }
            in.useDelimiter("\n");
            while (in.hasNext()) {
                String checkLine = in.next();
                if (!checkLine.startsWith("MemTotal:")) continue;
                String[] memorySplit = checkLine.split("\\s+");
                this.totalMemory = new Long(memorySplit[1]);
                if (!memorySplit[2].equals("kB")) break;
                this.totalMemory *= 1024L;
                break;
            }
            in.close();
        }
        return this.totalMemory;
    }
}

