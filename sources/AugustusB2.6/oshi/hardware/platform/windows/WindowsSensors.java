// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import org.slf4j.LoggerFactory;
import oshi.driver.windows.wmi.Win32Processor;
import oshi.driver.windows.wmi.Win32Fan;
import oshi.driver.windows.wmi.MSAcpiThermalZoneTemperature;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.COM.COMException;
import oshi.driver.windows.wmi.OhmSensor;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.OhmHardware;
import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class WindowsSensors extends AbstractSensors
{
    private static final Logger LOG;
    private static final String COM_EXCEPTION_MSG = "COM exception: {}";
    
    public double queryCpuTemperature() {
        double tempC = getTempFromOHM();
        if (tempC > 0.0) {
            return tempC;
        }
        tempC = getTempFromWMI();
        return tempC;
    }
    
    private static double getTempFromOHM() {
        final WmiQueryHandler h = Objects.requireNonNull(WmiQueryHandler.createInstance());
        boolean comInit = false;
        try {
            comInit = h.initCOM();
            final WbemcliUtil.WmiResult<OhmHardware.IdentifierProperty> ohmHardware = OhmHardware.queryHwIdentifier(h, "Hardware", "CPU");
            if (ohmHardware.getResultCount() > 0) {
                WindowsSensors.LOG.debug("Found Temperature data in Open Hardware Monitor");
                final String cpuIdentifier = WmiUtil.getString(ohmHardware, OhmHardware.IdentifierProperty.IDENTIFIER, 0);
                if (cpuIdentifier.length() > 0) {
                    final WbemcliUtil.WmiResult<OhmSensor.ValueProperty> ohmSensors = OhmSensor.querySensorValue(h, cpuIdentifier, "Temperature");
                    if (ohmSensors.getResultCount() > 0) {
                        double sum = 0.0;
                        for (int i = 0; i < ohmSensors.getResultCount(); ++i) {
                            sum += WmiUtil.getFloat(ohmSensors, OhmSensor.ValueProperty.VALUE, i);
                        }
                        return sum / ohmSensors.getResultCount();
                    }
                }
            }
        }
        catch (COMException e) {
            WindowsSensors.LOG.warn("COM exception: {}", e.getMessage());
        }
        finally {
            if (comInit) {
                h.unInitCOM();
            }
        }
        return 0.0;
    }
    
    private static double getTempFromWMI() {
        double tempC = 0.0;
        long tempK = 0L;
        final WbemcliUtil.WmiResult<MSAcpiThermalZoneTemperature.TemperatureProperty> result = MSAcpiThermalZoneTemperature.queryCurrentTemperature();
        if (result.getResultCount() > 0) {
            WindowsSensors.LOG.debug("Found Temperature data in WMI");
            tempK = WmiUtil.getUint32asLong(result, MSAcpiThermalZoneTemperature.TemperatureProperty.CURRENTTEMPERATURE, 0);
        }
        if (tempK > 2732L) {
            tempC = tempK / 10.0 - 273.15;
        }
        else if (tempK > 274L) {
            tempC = tempK - 273.0;
        }
        return (tempC < 0.0) ? 0.0 : tempC;
    }
    
    public int[] queryFanSpeeds() {
        int[] fanSpeeds = getFansFromOHM();
        if (fanSpeeds.length > 0) {
            return fanSpeeds;
        }
        fanSpeeds = getFansFromWMI();
        if (fanSpeeds.length > 0) {
            return fanSpeeds;
        }
        return new int[0];
    }
    
    private static int[] getFansFromOHM() {
        final WmiQueryHandler h = Objects.requireNonNull(WmiQueryHandler.createInstance());
        boolean comInit = false;
        try {
            comInit = h.initCOM();
            final WbemcliUtil.WmiResult<OhmHardware.IdentifierProperty> ohmHardware = OhmHardware.queryHwIdentifier(h, "Hardware", "CPU");
            if (ohmHardware.getResultCount() > 0) {
                WindowsSensors.LOG.debug("Found Fan data in Open Hardware Monitor");
                final String cpuIdentifier = WmiUtil.getString(ohmHardware, OhmHardware.IdentifierProperty.IDENTIFIER, 0);
                if (cpuIdentifier.length() > 0) {
                    final WbemcliUtil.WmiResult<OhmSensor.ValueProperty> ohmSensors = OhmSensor.querySensorValue(h, cpuIdentifier, "Fan");
                    if (ohmSensors.getResultCount() > 0) {
                        final int[] fanSpeeds = new int[ohmSensors.getResultCount()];
                        for (int i = 0; i < ohmSensors.getResultCount(); ++i) {
                            fanSpeeds[i] = (int)WmiUtil.getFloat(ohmSensors, OhmSensor.ValueProperty.VALUE, i);
                        }
                        return fanSpeeds;
                    }
                }
            }
        }
        catch (COMException e) {
            WindowsSensors.LOG.warn("COM exception: {}", e.getMessage());
        }
        finally {
            if (comInit) {
                h.unInitCOM();
            }
        }
        return new int[0];
    }
    
    private static int[] getFansFromWMI() {
        final WbemcliUtil.WmiResult<Win32Fan.SpeedProperty> fan = Win32Fan.querySpeed();
        if (fan.getResultCount() > 1) {
            WindowsSensors.LOG.debug("Found Fan data in WMI");
            final int[] fanSpeeds = new int[fan.getResultCount()];
            for (int i = 0; i < fan.getResultCount(); ++i) {
                fanSpeeds[i] = (int)WmiUtil.getUint64(fan, Win32Fan.SpeedProperty.DESIREDSPEED, i);
            }
            return fanSpeeds;
        }
        return new int[0];
    }
    
    public double queryCpuVoltage() {
        double volts = getVoltsFromOHM();
        if (volts > 0.0) {
            return volts;
        }
        volts = getVoltsFromWMI();
        return volts;
    }
    
    private static double getVoltsFromOHM() {
        final WmiQueryHandler h = Objects.requireNonNull(WmiQueryHandler.createInstance());
        boolean comInit = false;
        try {
            comInit = h.initCOM();
            final WbemcliUtil.WmiResult<OhmHardware.IdentifierProperty> ohmHardware = OhmHardware.queryHwIdentifier(h, "Sensor", "Voltage");
            if (ohmHardware.getResultCount() > 0) {
                WindowsSensors.LOG.debug("Found Voltage data in Open Hardware Monitor");
                String cpuIdentifier = null;
                for (int i = 0; i < ohmHardware.getResultCount(); ++i) {
                    final String id = WmiUtil.getString(ohmHardware, OhmHardware.IdentifierProperty.IDENTIFIER, i);
                    if (id.toLowerCase().contains("cpu")) {
                        cpuIdentifier = id;
                        break;
                    }
                }
                if (cpuIdentifier == null) {
                    cpuIdentifier = WmiUtil.getString(ohmHardware, OhmHardware.IdentifierProperty.IDENTIFIER, 0);
                }
                final WbemcliUtil.WmiResult<OhmSensor.ValueProperty> ohmSensors = OhmSensor.querySensorValue(h, cpuIdentifier, "Voltage");
                if (ohmSensors.getResultCount() > 0) {
                    return WmiUtil.getFloat(ohmSensors, OhmSensor.ValueProperty.VALUE, 0);
                }
            }
        }
        catch (COMException e) {
            WindowsSensors.LOG.warn("COM exception: {}", e.getMessage());
        }
        finally {
            if (comInit) {
                h.unInitCOM();
            }
        }
        return 0.0;
    }
    
    private static double getVoltsFromWMI() {
        final WbemcliUtil.WmiResult<Win32Processor.VoltProperty> voltage = Win32Processor.queryVoltage();
        if (voltage.getResultCount() > 1) {
            WindowsSensors.LOG.debug("Found Voltage data in WMI");
            int decivolts = WmiUtil.getUint16(voltage, Win32Processor.VoltProperty.CURRENTVOLTAGE, 0);
            if (decivolts > 0) {
                if ((decivolts & 0x80) != 0x0) {
                    return (decivolts & 0x7F) / 10.0;
                }
                decivolts = WmiUtil.getUint32(voltage, Win32Processor.VoltProperty.VOLTAGECAPS, 0);
                if ((decivolts & 0x1) > 0) {
                    return 5.0;
                }
                if ((decivolts & 0x2) > 0) {
                    return 3.3;
                }
                if ((decivolts & 0x4) > 0) {
                    return 2.9;
                }
            }
        }
        return 0.0;
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsSensors.class);
    }
}
