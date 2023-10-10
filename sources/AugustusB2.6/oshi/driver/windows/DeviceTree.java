// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows;

import java.util.Queue;
import com.sun.jna.platform.win32.WinNT;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.sun.jna.platform.win32.Cfgmgr32Util;
import java.util.ArrayDeque;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.Memory;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.Pointer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import oshi.util.tuples.Quintet;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Cfgmgr32;
import com.sun.jna.platform.win32.SetupApi;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class DeviceTree
{
    private static final int MAX_PATH = 260;
    private static final SetupApi SA;
    private static final Cfgmgr32 C32;
    
    private DeviceTree() {
    }
    
    public static Quintet<Set<Integer>, Map<Integer, Integer>, Map<Integer, String>, Map<Integer, String>, Map<Integer, String>> queryDeviceTree(final Guid.GUID guidDevInterface) {
        final Map<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
        final Map<Integer, String> nameMap = new HashMap<Integer, String>();
        final Map<Integer, String> deviceIdMap = new HashMap<Integer, String>();
        final Map<Integer, String> mfgMap = new HashMap<Integer, String>();
        final WinNT.HANDLE hDevInfo = DeviceTree.SA.SetupDiGetClassDevs(guidDevInterface, null, null, 18);
        if (!WinBase.INVALID_HANDLE_VALUE.equals(hDevInfo)) {
            try {
                final Memory buf = new Memory(260L);
                final IntByReference size = new IntByReference(260);
                final Queue<Integer> deviceTree = new ArrayDeque<Integer>();
                final SetupApi.SP_DEVINFO_DATA devInfoData = new SetupApi.SP_DEVINFO_DATA();
                devInfoData.cbSize = devInfoData.size();
                for (int i = 0; DeviceTree.SA.SetupDiEnumDeviceInfo(hDevInfo, i, devInfoData); ++i) {
                    deviceTree.add(devInfoData.DevInst);
                    int node = 0;
                    final IntByReference child = new IntByReference();
                    final IntByReference sibling = new IntByReference();
                    while (!deviceTree.isEmpty()) {
                        node = deviceTree.poll();
                        final String deviceId = Cfgmgr32Util.CM_Get_Device_ID(node);
                        deviceIdMap.put(node, deviceId);
                        String name = getDevNodeProperty(node, 13, buf, size);
                        if (name.isEmpty()) {
                            name = getDevNodeProperty(node, 1, buf, size);
                        }
                        if (name.isEmpty()) {
                            name = getDevNodeProperty(node, 8, buf, size);
                            final String svc = getDevNodeProperty(node, 5, buf, size);
                            if (!svc.isEmpty()) {
                                name = name + " (" + svc + ")";
                            }
                        }
                        nameMap.put(node, name);
                        mfgMap.put(node, getDevNodeProperty(node, 12, buf, size));
                        if (0 == DeviceTree.C32.CM_Get_Child(child, node, 0)) {
                            parentMap.put(child.getValue(), node);
                            deviceTree.add(child.getValue());
                            while (0 == DeviceTree.C32.CM_Get_Sibling(sibling, child.getValue(), 0)) {
                                parentMap.put(sibling.getValue(), node);
                                deviceTree.add(sibling.getValue());
                                child.setValue(sibling.getValue());
                            }
                        }
                    }
                }
            }
            finally {
                DeviceTree.SA.SetupDiDestroyDeviceInfoList(hDevInfo);
            }
        }
        final Set<Integer> controllerDevices = deviceIdMap.keySet().stream().filter(k -> !parentMap.containsKey(k)).collect((Collector<? super Object, ?, Set<Integer>>)Collectors.toSet());
        return new Quintet<Set<Integer>, Map<Integer, Integer>, Map<Integer, String>, Map<Integer, String>, Map<Integer, String>>(controllerDevices, parentMap, nameMap, deviceIdMap, mfgMap);
    }
    
    private static String getDevNodeProperty(final int node, final int cmDrp, final Memory buf, final IntByReference size) {
        buf.clear();
        size.setValue((int)buf.size());
        DeviceTree.C32.CM_Get_DevNode_Registry_Property(node, cmDrp, null, buf, size, 0);
        return buf.getWideString(0L);
    }
    
    static {
        SA = SetupApi.INSTANCE;
        C32 = Cfgmgr32.INSTANCE;
    }
}
