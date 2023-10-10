// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.util.FormatUtil;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.VirtualMemory;

@ThreadSafe
public abstract class AbstractVirtualMemory implements VirtualMemory
{
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Swap Used/Avail: ");
        sb.append(FormatUtil.formatBytes(this.getSwapUsed()));
        sb.append("/");
        sb.append(FormatUtil.formatBytes(this.getSwapTotal()));
        sb.append(", Virtual Memory In Use/Max=");
        sb.append(FormatUtil.formatBytes(this.getVirtualInUse()));
        sb.append("/");
        sb.append(FormatUtil.formatBytes(this.getVirtualMax()));
        return sb.toString();
    }
}
