// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.util.FormatUtil;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.hardware.PhysicalMemory;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.GlobalMemory;

@ThreadSafe
public abstract class AbstractGlobalMemory implements GlobalMemory
{
    @Override
    public List<PhysicalMemory> getPhysicalMemory() {
        final List<PhysicalMemory> pmList = new ArrayList<PhysicalMemory>();
        final List<String> dmi = ExecutingCommand.runNative("dmidecode --type 17");
        int bank = 0;
        String bankLabel = "unknown";
        String locator = "";
        long capacity = 0L;
        long speed = 0L;
        String manufacturer = "unknown";
        String memoryType = "unknown";
        for (final String line : dmi) {
            if (line.trim().contains("DMI type 17")) {
                if (bank++ <= 0) {
                    continue;
                }
                if (capacity > 0L) {
                    pmList.add(new PhysicalMemory(bankLabel + locator, capacity, speed, manufacturer, memoryType));
                }
                bankLabel = "unknown";
                locator = "";
                capacity = 0L;
                speed = 0L;
            }
            else {
                if (bank <= 0) {
                    continue;
                }
                final String[] split = line.trim().split(":");
                if (split.length != 2) {
                    continue;
                }
                final String s = split[0];
                switch (s) {
                    case "Bank Locator": {
                        bankLabel = split[1].trim();
                        continue;
                    }
                    case "Locator": {
                        locator = "/" + split[1].trim();
                        continue;
                    }
                    case "Size": {
                        capacity = ParseUtil.parseDecimalMemorySizeToBinary(split[1].trim());
                        continue;
                    }
                    case "Type": {
                        memoryType = split[1].trim();
                        continue;
                    }
                    case "Speed": {
                        speed = ParseUtil.parseHertz(split[1]);
                        continue;
                    }
                    case "Manufacturer": {
                        manufacturer = split[1].trim();
                        continue;
                    }
                }
            }
        }
        if (capacity > 0L) {
            pmList.add(new PhysicalMemory(bankLabel + locator, capacity, speed, manufacturer, memoryType));
        }
        return pmList;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Available: ");
        sb.append(FormatUtil.formatBytes(this.getAvailable()));
        sb.append("/");
        sb.append(FormatUtil.formatBytes(this.getTotal()));
        return sb.toString();
    }
}
