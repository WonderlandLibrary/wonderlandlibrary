// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import oshi.hardware.GraphicsCard;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.NetworkIF;
import oshi.hardware.Display;
import oshi.hardware.LogicalVolumeGroup;
import oshi.hardware.HWDiskStore;
import oshi.hardware.PowerSource;
import java.util.List;
import oshi.hardware.Sensors;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.ComputerSystem;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHardwareAbstractionLayer;

@ThreadSafe
public final class WindowsHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    public ComputerSystem createComputerSystem() {
        return new WindowsComputerSystem();
    }
    
    public GlobalMemory createMemory() {
        return new WindowsGlobalMemory();
    }
    
    public CentralProcessor createProcessor() {
        return new WindowsCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new WindowsSensors();
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return WindowsPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return WindowsHWDiskStore.getDisks();
    }
    
    @Override
    public List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        return WindowsLogicalVolumeGroup.getLogicalVolumeGroups();
    }
    
    @Override
    public List<Display> getDisplays() {
        return WindowsDisplay.getDisplays();
    }
    
    @Override
    public List<NetworkIF> getNetworkIFs(final boolean includeLocalInterfaces) {
        return WindowsNetworkIF.getNetworks(includeLocalInterfaces);
    }
    
    @Override
    public List<UsbDevice> getUsbDevices(final boolean tree) {
        return WindowsUsbDevice.getUsbDevices(tree);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return WindowsSoundCard.getSoundCards();
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return WindowsGraphicsCard.getGraphicsCards();
    }
}
