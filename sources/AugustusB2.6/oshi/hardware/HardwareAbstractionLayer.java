// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

import java.util.Collections;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface HardwareAbstractionLayer
{
    ComputerSystem getComputerSystem();
    
    CentralProcessor getProcessor();
    
    GlobalMemory getMemory();
    
    List<PowerSource> getPowerSources();
    
    List<HWDiskStore> getDiskStores();
    
    default List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        return Collections.emptyList();
    }
    
    List<NetworkIF> getNetworkIFs();
    
    List<NetworkIF> getNetworkIFs(final boolean p0);
    
    List<Display> getDisplays();
    
    Sensors getSensors();
    
    List<UsbDevice> getUsbDevices(final boolean p0);
    
    List<SoundCard> getSoundCards();
    
    List<GraphicsCard> getGraphicsCards();
}
