/*
 * Decompiled with CFR 0.152.
 */
package oshi.software.os.mac;

import java.util.ArrayList;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.software.os.mac.local.CentralProcessor;
import oshi.software.os.mac.local.GlobalMemory;
import oshi.util.ExecutingCommand;

public class MacHardwareAbstractionLayer
implements HardwareAbstractionLayer {
    private Processor[] _processors;
    private Memory _memory;

    public Processor[] getProcessors() {
        if (this._processors == null) {
            ArrayList<CentralProcessor> processors = new ArrayList<CentralProcessor>();
            int nbCPU = new Integer(ExecutingCommand.getFirstAnswer("sysctl -n hw.logicalcpu"));
            for (int i2 = 0; i2 < nbCPU; ++i2) {
                processors.add(new CentralProcessor());
            }
            this._processors = processors.toArray(new Processor[0]);
        }
        return this._processors;
    }

    public Memory getMemory() {
        if (this._memory == null) {
            this._memory = new GlobalMemory();
        }
        return this._memory;
    }
}

