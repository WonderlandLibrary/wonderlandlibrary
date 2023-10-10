// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import com.sun.jna.platform.mac.IOKit;
import oshi.util.platform.mac.SmcUtil;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class MacSensors extends AbstractSensors
{
    private int numFans;
    
    MacSensors() {
        this.numFans = 0;
    }
    
    public double queryCpuTemperature() {
        final IOKit.IOConnect conn = SmcUtil.smcOpen();
        final double temp = SmcUtil.smcGetFloat(conn, "TC0P");
        SmcUtil.smcClose(conn);
        if (temp > 0.0) {
            return temp;
        }
        return 0.0;
    }
    
    public int[] queryFanSpeeds() {
        final IOKit.IOConnect conn = SmcUtil.smcOpen();
        if (this.numFans == 0) {
            this.numFans = (int)SmcUtil.smcGetLong(conn, "FNum");
        }
        final int[] fanSpeeds = new int[this.numFans];
        for (int i = 0; i < this.numFans; ++i) {
            fanSpeeds[i] = (int)SmcUtil.smcGetFloat(conn, String.format("F%dAc", i));
        }
        SmcUtil.smcClose(conn);
        return fanSpeeds;
    }
    
    public double queryCpuVoltage() {
        final IOKit.IOConnect conn = SmcUtil.smcOpen();
        final double volts = SmcUtil.smcGetFloat(conn, "VC0C") / 1000.0;
        SmcUtil.smcClose(conn);
        return volts;
    }
}
