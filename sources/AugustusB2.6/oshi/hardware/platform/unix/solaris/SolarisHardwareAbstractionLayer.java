// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import oshi.hardware.GraphicsCard;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.NetworkIF;
import oshi.hardware.platform.unix.UnixDisplay;
import oshi.hardware.Display;
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
public final class SolarisHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    public ComputerSystem createComputerSystem() {
        return new SolarisComputerSystem();
    }
    
    public GlobalMemory createMemory() {
        return new SolarisGlobalMemory();
    }
    
    public CentralProcessor createProcessor() {
        return new SolarisCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new SolarisSensors();
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return SolarisPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return SolarisHWDiskStore.getDisks();
    }
    
    @Override
    public List<Display> getDisplays() {
        return UnixDisplay.getDisplays();
    }
    
    @Override
    public List<NetworkIF> getNetworkIFs(final boolean includeLocalInterfaces) {
        return SolarisNetworkIF.getNetworks(includeLocalInterfaces);
    }
    
    @Override
    public List<UsbDevice> getUsbDevices(final boolean tree) {
        return SolarisUsbDevice.getUsbDevices(tree);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return SolarisSoundCard.getSoundCards();
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return SolarisGraphicsCard.getGraphicsCards();
    }
}
