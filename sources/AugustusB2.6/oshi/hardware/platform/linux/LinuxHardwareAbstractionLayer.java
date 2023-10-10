// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import oshi.hardware.GraphicsCard;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.NetworkIF;
import oshi.hardware.platform.unix.UnixDisplay;
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
public final class LinuxHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    public ComputerSystem createComputerSystem() {
        return new LinuxComputerSystem();
    }
    
    public GlobalMemory createMemory() {
        return new LinuxGlobalMemory();
    }
    
    public CentralProcessor createProcessor() {
        return new LinuxCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new LinuxSensors();
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return LinuxPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return LinuxHWDiskStore.getDisks();
    }
    
    @Override
    public List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        return LinuxLogicalVolumeGroup.getLogicalVolumeGroups();
    }
    
    @Override
    public List<Display> getDisplays() {
        return UnixDisplay.getDisplays();
    }
    
    @Override
    public List<NetworkIF> getNetworkIFs(final boolean includeLocalInterfaces) {
        return LinuxNetworkIF.getNetworks(includeLocalInterfaces);
    }
    
    @Override
    public List<UsbDevice> getUsbDevices(final boolean tree) {
        return LinuxUsbDevice.getUsbDevices(tree);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return LinuxSoundCard.getSoundCards();
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return LinuxGraphicsCard.getGraphicsCards();
    }
}
