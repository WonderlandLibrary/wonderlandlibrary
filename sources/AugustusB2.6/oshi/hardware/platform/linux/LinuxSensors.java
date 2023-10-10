// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import java.util.List;
import java.util.ArrayList;
import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import java.io.File;
import java.nio.file.Paths;
import java.io.FileFilter;
import java.io.IOException;
import oshi.util.FileUtil;
import java.util.HashMap;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class LinuxSensors extends AbstractSensors
{
    private static final String TEMP = "temp";
    private static final String FAN = "fan";
    private static final String VOLTAGE = "in";
    private static final String[] SENSORS;
    private static final String HWMON = "hwmon";
    private static final String HWMON_PATH = "/sys/class/hwmon/hwmon";
    private static final String THERMAL_ZONE = "thermal_zone";
    private static final String THERMAL_ZONE_PATH = "/sys/class/thermal/thermal_zone";
    private static final boolean IS_PI;
    private final Map<String, String> sensorsMap;
    
    LinuxSensors() {
        this.sensorsMap = new HashMap<String, String>();
        if (!LinuxSensors.IS_PI) {
            this.populateSensorsMapFromHwmon();
            if (!this.sensorsMap.containsKey("temp")) {
                this.populateSensorsMapFromThermalZone();
            }
        }
    }
    
    private void populateSensorsMapFromHwmon() {
        for (final String sensorPrefix : LinuxSensors.SENSORS) {
            final String sensor = sensorPrefix;
            final String prefix;
            this.getSensorFilesFromPath("/sys/class/hwmon/hwmon", sensor, f -> {
                try {
                    return f.getName().startsWith(prefix) && f.getName().endsWith("_input") && FileUtil.getIntFromFile(f.getCanonicalPath()) > 0;
                }
                catch (IOException e) {
                    return false;
                }
            });
        }
    }
    
    private void populateSensorsMapFromThermalZone() {
        this.getSensorFilesFromPath("/sys/class/thermal/thermal_zone", "temp", f -> f.getName().equals("temp"));
    }
    
    private void getSensorFilesFromPath(final String sensorPath, final String sensor, final FileFilter sensorFileFilter) {
        for (int i = 0; Paths.get(sensorPath + i, new String[0]).toFile().isDirectory(); ++i) {
            final String path = sensorPath + i;
            final File dir = new File(path);
            final File[] matchingFiles = dir.listFiles(sensorFileFilter);
            if (matchingFiles != null && matchingFiles.length > 0) {
                this.sensorsMap.put(sensor, String.format("%s/%s", path, sensor));
            }
        }
    }
    
    public double queryCpuTemperature() {
        if (LinuxSensors.IS_PI) {
            return queryCpuTemperatureFromVcGenCmd();
        }
        final String tempStr = this.sensorsMap.get("temp");
        if (tempStr != null) {
            long millidegrees = 0L;
            if (tempStr.contains("hwmon")) {
                millidegrees = FileUtil.getLongFromFile(String.format("%s1_input", tempStr));
                if (millidegrees > 0L) {
                    return millidegrees / 1000.0;
                }
                long sum = 0L;
                int count = 0;
                for (int i = 2; i <= 6; ++i) {
                    millidegrees = FileUtil.getLongFromFile(String.format("%s%d_input", tempStr, i));
                    if (millidegrees > 0L) {
                        sum += millidegrees;
                        ++count;
                    }
                }
                if (count > 0) {
                    return sum / (count * 1000.0);
                }
            }
            else if (tempStr.contains("thermal_zone")) {
                millidegrees = FileUtil.getLongFromFile(tempStr);
                if (millidegrees > 0L) {
                    return millidegrees / 1000.0;
                }
            }
        }
        return 0.0;
    }
    
    private static double queryCpuTemperatureFromVcGenCmd() {
        final String tempStr = ExecutingCommand.getFirstAnswer("vcgencmd measure_temp");
        if (tempStr.startsWith("temp=")) {
            return ParseUtil.parseDoubleOrDefault(tempStr.replaceAll("[^\\d|\\.]+", ""), 0.0);
        }
        return 0.0;
    }
    
    public int[] queryFanSpeeds() {
        if (!LinuxSensors.IS_PI) {
            final String fanStr = this.sensorsMap.get("fan");
            if (fanStr != null) {
                final List<Integer> speeds = new ArrayList<Integer>();
                int fan = 1;
                while (true) {
                    final String fanPath = String.format("%s%d_input", fanStr, fan);
                    if (!new File(fanPath).exists()) {
                        break;
                    }
                    speeds.add(FileUtil.getIntFromFile(fanPath));
                    ++fan;
                }
                final int[] fanSpeeds = new int[speeds.size()];
                for (int i = 0; i < speeds.size(); ++i) {
                    fanSpeeds[i] = speeds.get(i);
                }
                return fanSpeeds;
            }
        }
        return new int[0];
    }
    
    public double queryCpuVoltage() {
        if (LinuxSensors.IS_PI) {
            return queryCpuVoltageFromVcGenCmd();
        }
        final String voltageStr = this.sensorsMap.get("in");
        if (voltageStr != null) {
            return FileUtil.getIntFromFile(String.format("%s1_input", voltageStr)) / 1000.0;
        }
        return 0.0;
    }
    
    private static double queryCpuVoltageFromVcGenCmd() {
        final String voltageStr = ExecutingCommand.getFirstAnswer("vcgencmd measure_volts core");
        if (voltageStr.startsWith("volt=")) {
            return ParseUtil.parseDoubleOrDefault(voltageStr.replaceAll("[^\\d|\\.]+", ""), 0.0);
        }
        return 0.0;
    }
    
    static {
        SENSORS = new String[] { "temp", "fan", "in" };
        IS_PI = (queryCpuTemperatureFromVcGenCmd() > 0.0);
    }
}
