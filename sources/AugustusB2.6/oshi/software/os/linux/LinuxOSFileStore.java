// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import java.util.Iterator;
import oshi.software.os.OSFileStore;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSFileStore;

@ThreadSafe
public class LinuxOSFileStore extends AbstractOSFileStore
{
    private String logicalVolume;
    private String description;
    private String fsType;
    private long freeSpace;
    private long usableSpace;
    private long totalSpace;
    private long freeInodes;
    private long totalInodes;
    
    public LinuxOSFileStore(final String name, final String volume, final String label, final String mount, final String options, final String uuid, final String logicalVolume, final String description, final String fsType, final long freeSpace, final long usableSpace, final long totalSpace, final long freeInodes, final long totalInodes) {
        super(name, volume, label, mount, options, uuid);
        this.logicalVolume = logicalVolume;
        this.description = description;
        this.fsType = fsType;
        this.freeSpace = freeSpace;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
        this.freeInodes = freeInodes;
        this.totalInodes = totalInodes;
    }
    
    @Override
    public String getLogicalVolume() {
        return this.logicalVolume;
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public String getType() {
        return this.fsType;
    }
    
    @Override
    public long getFreeSpace() {
        return this.freeSpace;
    }
    
    @Override
    public long getUsableSpace() {
        return this.usableSpace;
    }
    
    @Override
    public long getTotalSpace() {
        return this.totalSpace;
    }
    
    @Override
    public long getFreeInodes() {
        return this.freeInodes;
    }
    
    @Override
    public long getTotalInodes() {
        return this.totalInodes;
    }
    
    @Override
    public boolean updateAttributes() {
        for (final OSFileStore fileStore : LinuxFileSystem.getFileStoreMatching(this.getName(), null)) {
            if (this.getVolume().equals(fileStore.getVolume()) && this.getMount().equals(fileStore.getMount())) {
                this.logicalVolume = fileStore.getLogicalVolume();
                this.description = fileStore.getDescription();
                this.fsType = fileStore.getType();
                this.freeSpace = fileStore.getFreeSpace();
                this.usableSpace = fileStore.getUsableSpace();
                this.totalSpace = fileStore.getTotalSpace();
                this.freeInodes = fileStore.getFreeInodes();
                this.totalInodes = fileStore.getTotalInodes();
                return true;
            }
        }
        return false;
    }
}
