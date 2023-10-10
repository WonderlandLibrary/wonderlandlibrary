// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

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
public final class MacHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    public ComputerSystem createComputerSystem() {
        return new MacComputerSystem();
    }
    
    public GlobalMemory createMemory() {
        return new MacGlobalMemory();
    }
    
    public CentralProcessor createProcessor() {
        return new MacCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new MacSensors();
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return MacPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return MacHWDiskStore.getDisks();
    }
    
    @Override
    public List<LogicalVolumeGroup> getLogicalVolumeGroups() {
        return MacLogicalVolumeGroup.getLogicalVolumeGroups();
    }
    
    @Override
    public List<Display> getDisplays() {
        return MacDisplay.getDisplays();
    }
    
    @Override
    public List<NetworkIF> getNetworkIFs(final boolean includeLocalInterfaces) {
        return MacNetworkIF.getNetworks(includeLocalInterfaces);
    }
    
    @Override
    public List<UsbDevice> getUsbDevices(final boolean tree) {
        return MacUsbDevice.getUsbDevices(tree);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return MacSoundCard.getSoundCards();
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return MacGraphicsCard.getGraphicsCards();
    }
}
