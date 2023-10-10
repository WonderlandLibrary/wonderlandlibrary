// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.solaris;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class SolarisSensors extends AbstractSensors
{
    public double queryCpuTemperature() {
        double maxTemp = 0.0;
        for (final String line : ExecutingCommand.runNative("/usr/sbin/prtpicl -v -c temperature-sensor")) {
            if (line.trim().startsWith("Temperature:")) {
                final int temp = ParseUtil.parseLastInt(line, 0);
                if (temp <= maxTemp) {
                    continue;
                }
                maxTemp = temp;
            }
        }
        if (maxTemp > 1000.0) {
            maxTemp /= 1000.0;
        }
        return maxTemp;
    }
    
    public int[] queryFanSpeeds() {
        final List<Integer> speedList = new ArrayList<Integer>();
        for (final String line : ExecutingCommand.runNative("/usr/sbin/prtpicl -v -c fan")) {
            if (line.trim().startsWith("Speed:")) {
                speedList.add(ParseUtil.parseLastInt(line, 0));
            }
        }
        final int[] fans = new int[speedList.size()];
        for (int i = 0; i < speedList.size(); ++i) {
            fans[i] = speedList.get(i);
        }
        return fans;
    }
    
    public double queryCpuVoltage() {
        double voltage = 0.0;
        for (final String line : ExecutingCommand.runNative("/usr/sbin/prtpicl -v -c voltage-sensor")) {
            if (line.trim().startsWith("Voltage:")) {
                voltage = ParseUtil.parseDoubleOrDefault(line.replace("Voltage:", "").trim(), 0.0);
                break;
            }
        }
        return voltage;
    }
}
