// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.util.FormatUtil;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.HWDiskStore;

@ThreadSafe
public abstract class AbstractHWDiskStore implements HWDiskStore
{
    private final String name;
    private final String model;
    private final String serial;
    private final long size;
    
    protected AbstractHWDiskStore(final String name, final String model, final String serial, final long size) {
        this.name = name;
        this.model = model;
        this.serial = serial;
        this.size = size;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getModel() {
        return this.model;
    }
    
    @Override
    public String getSerial() {
        return this.serial;
    }
    
    @Override
    public long getSize() {
        return this.size;
    }
    
    @Override
    public String toString() {
        final boolean readwrite = this.getReads() > 0L || this.getWrites() > 0L;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(": ");
        sb.append("(model: ").append(this.getModel());
        sb.append(" - S/N: ").append(this.getSerial()).append(") ");
        sb.append("size: ").append((this.getSize() > 0L) ? FormatUtil.formatBytesDecimal(this.getSize()) : "?").append(", ");
        sb.append("reads: ").append(readwrite ? Long.valueOf(this.getReads()) : "?");
        sb.append(" (").append(readwrite ? FormatUtil.formatBytes(this.getReadBytes()) : "?").append("), ");
        sb.append("writes: ").append(readwrite ? Long.valueOf(this.getWrites()) : "?");
        sb.append(" (").append(readwrite ? FormatUtil.formatBytes(this.getWriteBytes()) : "?").append("), ");
        sb.append("xfer: ").append(readwrite ? Long.valueOf(this.getTransferTime()) : "?");
        return sb.toString();
    }
}
