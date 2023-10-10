// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.common;

import java.util.Arrays;
import oshi.util.GlobalConfig;
import oshi.software.os.OSFileStore;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.os.FileSystem;

@ThreadSafe
public abstract class AbstractFileSystem implements FileSystem
{
    public static final String OSHI_NETWORK_FILESYSTEM_TYPES = "oshi.network.filesystem.types";
    public static final String OSHI_PSEUDO_FILESYSTEM_TYPES = "oshi.pseudo.filesystem.types";
    protected static final List<String> NETWORK_FS_TYPES;
    protected static final List<String> PSEUDO_FS_TYPES;
    
    @Override
    public List<OSFileStore> getFileStores() {
        return this.getFileStores(false);
    }
    
    static {
        NETWORK_FS_TYPES = Arrays.asList(GlobalConfig.get("oshi.network.filesystem.types", "").split(","));
        PSEUDO_FS_TYPES = Arrays.asList(GlobalConfig.get("oshi.pseudo.filesystem.types", "").split(","));
    }
}
