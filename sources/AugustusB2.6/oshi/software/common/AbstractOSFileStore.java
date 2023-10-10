// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.common;

import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.os.OSFileStore;

@ThreadSafe
public abstract class AbstractOSFileStore implements OSFileStore
{
    private String name;
    private String volume;
    private String label;
    private String mount;
    private String options;
    private String uuid;
    
    protected AbstractOSFileStore(final String name, final String volume, final String label, final String mount, final String options, final String uuid) {
        this.name = name;
        this.volume = volume;
        this.label = label;
        this.mount = mount;
        this.options = options;
        this.uuid = uuid;
    }
    
    protected AbstractOSFileStore() {
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getVolume() {
        return this.volume;
    }
    
    @Override
    public String getLabel() {
        return this.label;
    }
    
    @Override
    public String getMount() {
        return this.mount;
    }
    
    @Override
    public String getOptions() {
        return this.options;
    }
    
    @Override
    public String getUUID() {
        return this.uuid;
    }
    
    @Override
    public String toString() {
        return "OSFileStore [name=" + this.getName() + ", volume=" + this.getVolume() + ", label=" + this.getLabel() + ", logicalVolume=" + this.getLogicalVolume() + ", mount=" + this.getMount() + ", description=" + this.getDescription() + ", fsType=" + this.getType() + ", options=\"" + this.getOptions() + "\", uuid=" + this.getUUID() + ", freeSpace=" + this.getFreeSpace() + ", usableSpace=" + this.getUsableSpace() + ", totalSpace=" + this.getTotalSpace() + ", freeInodes=" + this.getFreeInodes() + ", totalInodes=" + this.getTotalInodes() + "]";
    }
}
