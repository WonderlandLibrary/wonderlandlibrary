// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import oshi.hardware.GraphicsCard;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.NetworkIF;
import oshi.hardware.platform.unix.UnixDisplay;
import oshi.hardware.Display;
import oshi.hardware.HWDiskStore;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.ComputerSystem;
import oshi.driver.unix.aix.perfstat.PerfstatDisk;
import oshi.util.Memoizer;
import oshi.driver.unix.aix.Lscfg;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractHardwareAbstractionLayer;

@ThreadSafe
public final class AixHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer
{
    private final Supplier<List<String>> lscfg;
    private final Supplier<Perfstat.perfstat_disk_t[]> diskStats;
    
    public AixHardwareAbstractionLayer() {
        this.lscfg = Memoizer.memoize(Lscfg::queryAllDevices, Memoizer.defaultExpiration());
        this.diskStats = Memoizer.memoize(PerfstatDisk::queryDiskStats, Memoizer.defaultExpiration());
    }
    
    public ComputerSystem createComputerSystem() {
        return new AixComputerSystem(this.lscfg);
    }
    
    public GlobalMemory createMemory() {
        return new AixGlobalMemory(this.lscfg);
    }
    
    public CentralProcessor createProcessor() {
        return new AixCentralProcessor();
    }
    
    public Sensors createSensors() {
        return new AixSensors(this.lscfg);
    }
    
    @Override
    public List<PowerSource> getPowerSources() {
        return AixPowerSource.getPowerSources();
    }
    
    @Override
    public List<HWDiskStore> getDiskStores() {
        return AixHWDiskStore.getDisks(this.diskStats);
    }
    
    @Override
    public List<Display> getDisplays() {
        return UnixDisplay.getDisplays();
    }
    
    @Override
    public List<NetworkIF> getNetworkIFs(final boolean includeLocalInterfaces) {
        return AixNetworkIF.getNetworks(includeLocalInterfaces);
    }
    
    @Override
    public List<UsbDevice> getUsbDevices(final boolean tree) {
        return AixUsbDevice.getUsbDevices(tree, this.lscfg);
    }
    
    @Override
    public List<SoundCard> getSoundCards() {
        return AixSoundCard.getSoundCards(this.lscfg);
    }
    
    @Override
    public List<GraphicsCard> getGraphicsCards() {
        return AixGraphicsCard.getGraphicsCards(this.lscfg);
    }
}
