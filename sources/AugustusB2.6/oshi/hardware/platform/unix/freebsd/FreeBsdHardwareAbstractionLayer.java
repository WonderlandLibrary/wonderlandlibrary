// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

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
public final class FreeBsdHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    public ComputerSystem createComputerSystem() {
        return new FreeBsdComputerSystem();
    }
    
    public GlobalMemory createMemory() {
        return new FreeBsdGlobalMemory();
    }
    
    public CentralProcessor createProcessor() {
        return new FreeBsdCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new FreeBsdSensors();
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return FreeBsdPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return FreeBsdHWDiskStore.getDisks();
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
        return FreeBsdUsbDevice.getUsbDevices(tree);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return FreeBsdSoundCard.getSoundCards();
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return FreeBsdGraphicsCard.getGraphicsCards();
    }
}
