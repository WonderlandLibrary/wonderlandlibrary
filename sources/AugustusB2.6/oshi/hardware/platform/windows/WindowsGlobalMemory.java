// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.windows;

import com.sun.jna.platform.win32.VersionHelpers;
import org.slf4j.LoggerFactory;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiUtil;
import oshi.driver.windows.wmi.Win32PhysicalMemory;
import java.util.ArrayList;
import oshi.hardware.PhysicalMemory;
import java.util.List;
import oshi.util.Memoizer;
import oshi.hardware.VirtualMemory;
import oshi.util.tuples.Triplet;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractGlobalMemory;

@ThreadSafe
final class WindowsGlobalMemory extends AbstractGlobalMemory
{
    private static final Logger LOG;
    private static final boolean IS_WINDOWS10_OR_GREATER;
    private final Supplier<Triplet<Long, Long, Long>> availTotalSize;
    private final Supplier<VirtualMemory> vm;
    
    WindowsGlobalMemory() {
        this.availTotalSize = Memoizer.memoize(WindowsGlobalMemory::readPerfInfo, Memoizer.defaultExpiration());
        this.vm = Memoizer.memoize(this::createVirtualMemory);
    }
    
    @Override
    public long getAvailable() {
        return this.availTotalSize.get().getA();
    }
    
    @Override
    public long getTotal() {
        return this.availTotalSize.get().getB();
    }
    
    @Override
    public long getPageSize() {
        return this.availTotalSize.get().getC();
    }
    
    @Override
    public VirtualMemory getVirtualMemory() {
        return this.vm.get();
    }
    
    private VirtualMemory createVirtualMemory() {
        return new WindowsVirtualMemory(this);
    }
    
    @Override
    public List<PhysicalMemory> getPhysicalMemory() {
        final List<PhysicalMemory> physicalMemoryList = new ArrayList<PhysicalMemory>();
        if (WindowsGlobalMemory.IS_WINDOWS10_OR_GREATER) {
            final WbemcliUtil.WmiResult<Win32PhysicalMemory.PhysicalMemoryProperty> bankMap = Win32PhysicalMemory.queryphysicalMemory();
            for (int index = 0; index < bankMap.getResultCount(); ++index) {
                final String bankLabel = WmiUtil.getString(bankMap, Win32PhysicalMemory.PhysicalMemoryProperty.BANKLABEL, index);
                final long capacity = WmiUtil.getUint64(bankMap, Win32PhysicalMemory.PhysicalMemoryProperty.CAPACITY, index);
                final long speed = WmiUtil.getUint32(bankMap, Win32PhysicalMemory.PhysicalMemoryProperty.SPEED, index) * 1000000L;
                final String manufacturer = WmiUtil.getString(bankMap, Win32PhysicalMemory.PhysicalMemoryProperty.MANUFACTURER, index);
                final String memoryType = smBiosMemoryType(WmiUtil.getUint32(bankMap, Win32PhysicalMemory.PhysicalMemoryProperty.SMBIOSMEMORYTYPE, index));
                physicalMemoryList.add(new PhysicalMemory(bankLabel, capacity, speed, manufacturer, memoryType));
            }
        }
        else {
            final WbemcliUtil.WmiResult<Win32PhysicalMemory.PhysicalMemoryPropertyWin8> bankMap2 = Win32PhysicalMemory.queryphysicalMemoryWin8();
            for (int index = 0; index < bankMap2.getResultCount(); ++index) {
                final String bankLabel = WmiUtil.getString(bankMap2, Win32PhysicalMemory.PhysicalMemoryPropertyWin8.BANKLABEL, index);
                final long capacity = WmiUtil.getUint64(bankMap2, Win32PhysicalMemory.PhysicalMemoryPropertyWin8.CAPACITY, index);
                final long speed = WmiUtil.getUint32(bankMap2, Win32PhysicalMemory.PhysicalMemoryPropertyWin8.SPEED, index) * 1000000L;
                final String manufacturer = WmiUtil.getString(bankMap2, Win32PhysicalMemory.PhysicalMemoryPropertyWin8.MANUFACTURER, index);
                final String memoryType = memoryType(WmiUtil.getUint16(bankMap2, Win32PhysicalMemory.PhysicalMemoryPropertyWin8.MEMORYTYPE, index));
                physicalMemoryList.add(new PhysicalMemory(bankLabel, capacity, speed, manufacturer, memoryType));
            }
        }
        return physicalMemoryList;
    }
    
    private static String memoryType(final int type) {
        switch (type) {
            case 1: {
                return "Other";
            }
            case 2: {
                return "DRAM";
            }
            case 3: {
                return "Synchronous DRAM";
            }
            case 4: {
                return "Cache DRAM";
            }
            case 5: {
                return "EDO";
            }
            case 6: {
                return "EDRAM";
            }
            case 7: {
                return "VRAM";
            }
            case 8: {
                return "SRAM";
            }
            case 9: {
                return "RAM";
            }
            case 10: {
                return "ROM";
            }
            case 11: {
                return "Flash";
            }
            case 12: {
                return "EEPROM";
            }
            case 13: {
                return "FEPROM";
            }
            case 14: {
                return "EPROM";
            }
            case 15: {
                return "CDRAM";
            }
            case 16: {
                return "3DRAM";
            }
            case 17: {
                return "SDRAM";
            }
            case 18: {
                return "SGRAM";
            }
            case 19: {
                return "RDRAM";
            }
            case 20: {
                return "DDR";
            }
            case 21: {
                return "DDR2";
            }
            case 22: {
                return "DDR2-FB-DIMM";
            }
            case 24: {
                return "DDR3";
            }
            case 25: {
                return "FBD2";
            }
            default: {
                return "Unknown";
            }
        }
    }
    
    private static String smBiosMemoryType(final int type) {
        switch (type) {
            case 1: {
                return "Other";
            }
            case 3: {
                return "DRAM";
            }
            case 4: {
                return "EDRAM";
            }
            case 5: {
                return "VRAM";
            }
            case 6: {
                return "SRAM";
            }
            case 7: {
                return "RAM";
            }
            case 8: {
                return "ROM";
            }
            case 9: {
                return "FLASH";
            }
            case 10: {
                return "EEPROM";
            }
            case 11: {
                return "FEPROM";
            }
            case 12: {
                return "EPROM";
            }
            case 13: {
                return "CDRAM";
            }
            case 14: {
                return "3DRAM";
            }
            case 15: {
                return "SDRAM";
            }
            case 16: {
                return "SGRAM";
            }
            case 17: {
                return "RDRAM";
            }
            case 18: {
                return "DDR";
            }
            case 19: {
                return "DDR2";
            }
            case 20: {
                return "DDR2 FB-DIMM";
            }
            case 24: {
                return "DDR3";
            }
            case 25: {
                return "FBD2";
            }
            case 26: {
                return "DDR4";
            }
            case 27: {
                return "LPDDR";
            }
            case 28: {
                return "LPDDR2";
            }
            case 29: {
                return "LPDDR3";
            }
            case 30: {
                return "LPDDR4";
            }
            case 31: {
                return "Logical non-volatile device";
            }
            default: {
                return "Unknown";
            }
        }
    }
    
    private static Triplet<Long, Long, Long> readPerfInfo() {
        final Psapi.PERFORMANCE_INFORMATION performanceInfo = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(performanceInfo, performanceInfo.size())) {
            WindowsGlobalMemory.LOG.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return new Triplet<Long, Long, Long>(0L, 0L, 4098L);
        }
        final long pageSize = performanceInfo.PageSize.longValue();
        final long memAvailable = pageSize * performanceInfo.PhysicalAvailable.longValue();
        final long memTotal = pageSize * performanceInfo.PhysicalTotal.longValue();
        return new Triplet<Long, Long, Long>(memAvailable, memTotal, pageSize);
    }
    
    static {
        LOG = LoggerFactory.getLogger(WindowsGlobalMemory.class);
        IS_WINDOWS10_OR_GREATER = VersionHelpers.IsWindows10OrGreater();
    }
}
