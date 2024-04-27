/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.management;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;

public class MemoryManager
implements Listenable {
    public static float maxMemorySize = 0.0f;
    public static float usedMemorySize = 0.0f;

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        maxMemorySize = (float)memoryUsage.getMax() / 1048576.0f;
        usedMemorySize = (float)memoryUsage.getUsed() / 1048576.0f;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static float getMemory() {
        return maxMemorySize / usedMemorySize;
    }
}

