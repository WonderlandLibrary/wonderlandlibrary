// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import com.sun.jna.Platform;
import com.sun.jna.win32.W32APITypeMapper;
import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.SetupApi;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import oshi.jna.platform.windows.PowrProf;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import oshi.hardware.PowerSource;
import com.sun.jna.platform.win32.Guid;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractPowerSource;

@ThreadSafe
public final class WindowsPowerSource extends AbstractPowerSource
{
    private static final Guid.GUID GUID_DEVCLASS_BATTERY;
    private static final int CHAR_WIDTH;
    private static final boolean X64;
    private static final int BATTERY_SYSTEM_BATTERY = Integer.MIN_VALUE;
    private static final int BATTERY_IS_SHORT_TERM = 536870912;
    private static final int BATTERY_POWER_ON_LINE = 1;
    private static final int BATTERY_DISCHARGING = 2;
    private static final int BATTERY_CHARGING = 4;
    private static final int BATTERY_CAPACITY_RELATIVE = 1073741824;
    private static final int IOCTL_BATTERY_QUERY_TAG = 2703424;
    private static final int IOCTL_BATTERY_QUERY_STATUS = 2703436;
    private static final int IOCTL_BATTERY_QUERY_INFORMATION = 2703428;
    
    public WindowsPowerSource(final String psName, final String psDeviceName, final double psRemainingCapacityPercent, final double psTimeRemainingEstimated, final double psTimeRemainingInstant, final double psPowerUsageRate, final double psVoltage, final double psAmperage, final boolean psPowerOnLine, final boolean psCharging, final boolean psDischarging, final PowerSource.CapacityUnits psCapacityUnits, final int psCurrentCapacity, final int psMaxCapacity, final int psDesignCapacity, final int psCycleCount, final String psChemistry, final LocalDate psManufactureDate, final String psManufacturer, final String psSerialNumber, final double psTemperature) {
        super(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    public static List<PowerSource> getPowerSources() {
        return Arrays.asList(getPowerSource("System Battery"));
    }
    
    private static WindowsPowerSource getPowerSource(final String name) {
        final String psName = name;
        String psDeviceName = "unknown";
        double psRemainingCapacityPercent = 1.0;
        double psTimeRemainingEstimated = -1.0;
        double psTimeRemainingInstant = 0.0;
        int psPowerUsageRate = 0;
        double psVoltage = -1.0;
        double psAmperage = 0.0;
        boolean psPowerOnLine = false;
        boolean psCharging = false;
        boolean psDischarging = false;
        PowerSource.CapacityUnits psCapacityUnits = PowerSource.CapacityUnits.RELATIVE;
        int psCurrentCapacity = 0;
        int psMaxCapacity = 1;
        int psDesignCapacity = 1;
        int psCycleCount = -1;
        String psChemistry = "unknown";
        LocalDate psManufactureDate = null;
        String psManufacturer = "unknown";
        String psSerialNumber = "unknown";
        double psTemperature = 0.0;
        final int size = new PowrProf.SystemBatteryState().size();
        final Memory mem = new Memory(size);
        if (0 == PowrProf.INSTANCE.CallNtPowerInformation(5, null, 0, mem, size)) {
            final PowrProf.SystemBatteryState batteryState = new PowrProf.SystemBatteryState(mem);
            if (batteryState.batteryPresent > 0) {
                if (batteryState.acOnLine == 0 && batteryState.charging == 0 && batteryState.discharging > 0) {
                    psTimeRemainingEstimated = batteryState.estimatedTime;
                }
                else if (batteryState.charging > 0) {
                    psTimeRemainingEstimated = -2.0;
                }
                psMaxCapacity = batteryState.maxCapacity;
                psCurrentCapacity = batteryState.remainingCapacity;
                psRemainingCapacityPercent = Math.min(1.0, psCurrentCapacity / (double)psMaxCapacity);
                psPowerUsageRate = batteryState.rate;
            }
        }
        final WinNT.HANDLE hdev = SetupApi.INSTANCE.SetupDiGetClassDevs(WindowsPowerSource.GUID_DEVCLASS_BATTERY, null, null, 18);
        if (!WinBase.INVALID_HANDLE_VALUE.equals(hdev)) {
            boolean batteryFound = false;
            for (int idev = 0; !batteryFound && idev < 100; ++idev) {
                final SetupApi.SP_DEVICE_INTERFACE_DATA did = new SetupApi.SP_DEVICE_INTERFACE_DATA();
                did.cbSize = did.size();
                if (SetupApi.INSTANCE.SetupDiEnumDeviceInterfaces(hdev, null, WindowsPowerSource.GUID_DEVCLASS_BATTERY, idev, did)) {
                    final IntByReference requiredSize = new IntByReference(0);
                    SetupApi.INSTANCE.SetupDiGetDeviceInterfaceDetail(hdev, did, null, 0, requiredSize, null);
                    if (122 == Kernel32.INSTANCE.GetLastError()) {
                        final Memory pdidd = new Memory(requiredSize.getValue());
                        pdidd.setInt(0L, 4 + (WindowsPowerSource.X64 ? 4 : WindowsPowerSource.CHAR_WIDTH));
                        if (SetupApi.INSTANCE.SetupDiGetDeviceInterfaceDetail(hdev, did, pdidd, (int)pdidd.size(), requiredSize, null)) {
                            final String devicePath = (WindowsPowerSource.CHAR_WIDTH > 1) ? pdidd.getWideString(4L) : pdidd.getString(4L);
                            final WinNT.HANDLE hBattery = Kernel32.INSTANCE.CreateFile(devicePath, -1073741824, 3, null, 3, 128, null);
                            if (!WinBase.INVALID_HANDLE_VALUE.equals(hBattery)) {
                                final PowrProf.BATTERY_QUERY_INFORMATION bqi = new PowrProf.BATTERY_QUERY_INFORMATION();
                                final IntByReference dwWait = new IntByReference(0);
                                final IntByReference dwTag = new IntByReference();
                                final IntByReference dwOut = new IntByReference();
                                if (Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703424, dwWait.getPointer(), 4, dwTag.getPointer(), 4, dwOut, null)) {
                                    bqi.BatteryTag = dwTag.getValue();
                                    if (bqi.BatteryTag > 0) {
                                        bqi.InformationLevel = PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatteryInformation.ordinal();
                                        bqi.write();
                                        final PowrProf.BATTERY_INFORMATION bi = new PowrProf.BATTERY_INFORMATION();
                                        if (Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703428, bqi.getPointer(), bqi.size(), bi.getPointer(), bi.size(), dwOut, null)) {
                                            bi.read();
                                            if (0x0 != (bi.Capabilities & Integer.MIN_VALUE) && 0x0 == (bi.Capabilities & 0x20000000)) {
                                                if (0x0 == (bi.Capabilities & 0x40000000)) {
                                                    psCapacityUnits = PowerSource.CapacityUnits.MWH;
                                                }
                                                psChemistry = Native.toString(bi.Chemistry, StandardCharsets.US_ASCII);
                                                psDesignCapacity = bi.DesignedCapacity;
                                                psMaxCapacity = bi.FullChargedCapacity;
                                                psCycleCount = bi.CycleCount;
                                                final PowrProf.BATTERY_WAIT_STATUS bws = new PowrProf.BATTERY_WAIT_STATUS();
                                                bws.BatteryTag = bqi.BatteryTag;
                                                bws.write();
                                                final PowrProf.BATTERY_STATUS bs = new PowrProf.BATTERY_STATUS();
                                                if (Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703436, bws.getPointer(), bws.size(), bs.getPointer(), bs.size(), dwOut, null)) {
                                                    bs.read();
                                                    if (0x0 != (bs.PowerState & 0x1)) {
                                                        psPowerOnLine = true;
                                                    }
                                                    if (0x0 != (bs.PowerState & 0x2)) {
                                                        psDischarging = true;
                                                    }
                                                    if (0x0 != (bs.PowerState & 0x4)) {
                                                        psCharging = true;
                                                    }
                                                    psCurrentCapacity = bs.Capacity;
                                                    psVoltage = ((bs.Voltage > 0) ? (bs.Voltage / 1000.0) : bs.Voltage);
                                                    psPowerUsageRate = bs.Rate;
                                                    if (psVoltage > 0.0) {
                                                        psAmperage = psPowerUsageRate / psVoltage;
                                                    }
                                                }
                                            }
                                            psDeviceName = batteryQueryString(hBattery, dwTag.getValue(), PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatteryDeviceName.ordinal());
                                            psManufacturer = batteryQueryString(hBattery, dwTag.getValue(), PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatteryManufactureName.ordinal());
                                            psSerialNumber = batteryQueryString(hBattery, dwTag.getValue(), PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatterySerialNumber.ordinal());
                                            bqi.InformationLevel = PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatteryManufactureDate.ordinal();
                                            bqi.write();
                                            final PowrProf.BATTERY_MANUFACTURE_DATE bmd = new PowrProf.BATTERY_MANUFACTURE_DATE();
                                            if (Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703428, bqi.getPointer(), bqi.size(), bmd.getPointer(), bmd.size(), dwOut, null)) {
                                                bmd.read();
                                                if (bmd.Year > 1900 && bmd.Month > 0 && bmd.Day > 0) {
                                                    psManufactureDate = LocalDate.of(bmd.Year, bmd.Month, bmd.Day);
                                                }
                                            }
                                            bqi.InformationLevel = PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatteryTemperature.ordinal();
                                            bqi.write();
                                            final IntByReference tempK = new IntByReference();
                                            if (Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703428, bqi.getPointer(), bqi.size(), tempK.getPointer(), 4, dwOut, null)) {
                                                psTemperature = tempK.getValue() / 10.0 - 273.15;
                                            }
                                            bqi.InformationLevel = PowrProf.BATTERY_QUERY_INFORMATION_LEVEL.BatteryEstimatedTime.ordinal();
                                            if (psPowerUsageRate != 0) {
                                                bqi.AtRate = psPowerUsageRate;
                                            }
                                            bqi.write();
                                            final IntByReference tr = new IntByReference();
                                            if (Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703428, bqi.getPointer(), bqi.size(), tr.getPointer(), 4, dwOut, null)) {
                                                psTimeRemainingInstant = tr.getValue();
                                            }
                                            if (psTimeRemainingInstant < 0.0 && psPowerUsageRate != 0) {
                                                psTimeRemainingInstant = (psMaxCapacity - psCurrentCapacity) * 3600.0 / psPowerUsageRate;
                                                if (psTimeRemainingInstant < 0.0) {
                                                    psTimeRemainingInstant *= -1.0;
                                                }
                                            }
                                            batteryFound = true;
                                        }
                                    }
                                }
                                Kernel32.INSTANCE.CloseHandle(hBattery);
                            }
                        }
                    }
                }
                else if (259 == Kernel32.INSTANCE.GetLastError()) {
                    break;
                }
            }
            SetupApi.INSTANCE.SetupDiDestroyDeviceInfoList(hdev);
        }
        return new WindowsPowerSource(psName, psDeviceName, psRemainingCapacityPercent, psTimeRemainingEstimated, psTimeRemainingInstant, psPowerUsageRate, psVoltage, psAmperage, psPowerOnLine, psCharging, psDischarging, psCapacityUnits, psCurrentCapacity, psMaxCapacity, psDesignCapacity, psCycleCount, psChemistry, psManufactureDate, psManufacturer, psSerialNumber, psTemperature);
    }
    
    private static String batteryQueryString(final WinNT.HANDLE hBattery, final int tag, final int infoLevel) {
        final PowrProf.BATTERY_QUERY_INFORMATION bqi = new PowrProf.BATTERY_QUERY_INFORMATION();
        bqi.BatteryTag = tag;
        bqi.InformationLevel = infoLevel;
        bqi.write();
        final IntByReference dwOut = new IntByReference();
        boolean ret = false;
        long bufSize = 0L;
        Memory nameBuf;
        do {
            bufSize += 256L;
            nameBuf = new Memory(bufSize);
            ret = Kernel32.INSTANCE.DeviceIoControl(hBattery, 2703428, bqi.getPointer(), bqi.size(), nameBuf, (int)nameBuf.size(), dwOut, null);
        } while (!ret && bufSize < 4096L);
        return (WindowsPowerSource.CHAR_WIDTH > 1) ? nameBuf.getWideString(0L) : nameBuf.getString(0L);
    }
    
    static {
        GUID_DEVCLASS_BATTERY = Guid.GUID.fromString("{72631E54-78A4-11D0-BCF7-00AA00B7B32A}");
        CHAR_WIDTH = ((W32APITypeMapper.DEFAULT == W32APITypeMapper.UNICODE) ? 2 : 1);
        X64 = Platform.is64Bit();
    }
}
