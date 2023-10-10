// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import oshi.hardware.NetworkIF;
import java.util.List;
import oshi.util.Memoizer;
import oshi.hardware.Sensors;
import oshi.hardware.GlobalMemory;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.HardwareAbstractionLayer;

@ThreadSafe
public abstract class AbstractHardwareAbstractionLayer implements HardwareAbstractionLayer
{
    private final Supplier<ComputerSystem> computerSystem;
    private final Supplier<CentralProcessor> processor;
    private final Supplier<GlobalMemory> memory;
    private final Supplier<Sensors> sensors;
    
    public AbstractHardwareAbstractionLayer() {
        this.computerSystem = Memoizer.memoize(this::createComputerSystem);
        this.processor = Memoizer.memoize(this::createProcessor);
        this.memory = Memoizer.memoize(this::createMemory);
        this.sensors = Memoizer.memoize(this::createSensors);
    }
    
    @Override
    public ComputerSystem getComputerSystem() {
        return this.computerSystem.get();
    }
    
    protected abstract ComputerSystem createComputerSystem();
    
    @Override
    public CentralProcessor getProcessor() {
        return this.processor.get();
    }
    
    protected abstract CentralProcessor createProcessor();
    
    @Override
    public GlobalMemory getMemory() {
        return this.memory.get();
    }
    
    protected abstract GlobalMemory createMemory();
    
    @Override
    public Sensors getSensors() {
        return this.sensors.get();
    }
    
    protected abstract Sensors createSensors();
    
    @Override
    public List<NetworkIF> getNetworkIFs() {
        return this.getNetworkIFs(false);
    }
}
