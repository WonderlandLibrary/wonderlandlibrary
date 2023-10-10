// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import oshi.util.FormatUtil;
import oshi.annotation.concurrent.Immutable;

@Immutable
public class HWPartition
{
    private final String identification;
    private final String name;
    private final String type;
    private final String uuid;
    private final long size;
    private final int major;
    private final int minor;
    private final String mountPoint;
    
    public HWPartition(final String identification, final String name, final String type, final String uuid, final long size, final int major, final int minor, final String mountPoint) {
        this.identification = identification;
        this.name = name;
        this.type = type;
        this.uuid = uuid;
        this.size = size;
        this.major = major;
        this.minor = minor;
        this.mountPoint = mountPoint;
    }
    
    public String getIdentification() {
        return this.identification;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getUuid() {
        return this.uuid;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public int getMajor() {
        return this.major;
    }
    
    public int getMinor() {
        return this.minor;
    }
    
    public String getMountPoint() {
        return this.mountPoint;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getIdentification()).append(": ");
        sb.append(this.getName()).append(" ");
        sb.append("(").append(this.getType()).append(") ");
        sb.append("Maj:Min=").append(this.getMajor()).append(":").append(this.getMinor()).append(", ");
        sb.append("size: ").append(FormatUtil.formatBytesDecimal(this.getSize()));
        sb.append(this.getMountPoint().isEmpty() ? "" : (" @ " + this.getMountPoint()));
        return sb.toString();
    }
}
