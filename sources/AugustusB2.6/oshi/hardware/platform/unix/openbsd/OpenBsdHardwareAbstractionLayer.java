// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.openbsd;

import oshi.hardware.GraphicsCard;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.platform.unix.BsdNetworkIF;
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
public final class OpenBsdHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    public ComputerSystem createComputerSystem() {
        return new OpenBsdComputerSystem();
    }
    
    public GlobalMemory createMemory() {
        return new OpenBsdGlobalMemory();
    }
    
    public CentralProcessor createProcessor() {
        return new OpenBsdCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new OpenBsdSensors();
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return OpenBsdPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return OpenBsdHWDiskStore.getDisks();
    }
    
    @Override
    public List<Display> getDisplays() {
        return UnixDisplay.getDisplays();
    }
    
    @Override
    public List<NetworkIF> getNetworkIFs(final boolean includeLocalInterfaces) {
        return BsdNetworkIF.getNetworks(includeLocalInterfaces);
    }
    
    @Override
    public List<UsbDevice> getUsbDevices(final boolean tree) {
        return OpenBsdUsbDevice.getUsbDevices(tree);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return OpenBsdSoundCard.getSoundCards();
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return OpenBsdGraphicsCard.getGraphicsCards();
    }
}
