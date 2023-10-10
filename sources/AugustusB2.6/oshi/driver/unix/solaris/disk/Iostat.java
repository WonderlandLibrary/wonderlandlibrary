// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.solaris.disk;

import java.util.Iterator;
import oshi.util.ParseUtil;
import oshi.util.tuples.Quintet;
import java.util.Set;
import java.util.List;
import oshi.util.ExecutingCommand;
import java.util.HashMap;
import java.util.Map;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Iostat
{
    private static final String IOSTAT_ER_DETAIL = "iostat -Er";
    private static final String IOSTAT_ER = "iostat -er";
    private static final String IOSTAT_ERN = "iostat -ern";
    private static final String DEVICE_HEADER = "device";
    
    private Iostat() {
    }
    
    public static Map<String, String> queryPartitionToMountMap() {
        final Map<String, String> deviceMap = new HashMap<String, String>();
        final List<String> mountNames = ExecutingCommand.runNative("iostat -er");
        final List<String> mountPoints = ExecutingCommand.runNative("iostat -ern");
        for (int i = 0; i < mountNames.size() && i < mountPoints.size(); ++i) {
            final String disk = mountNames.get(i);
            final String[] diskSplit = disk.split(",");
            if (diskSplit.length >= 5 && !"device".equals(diskSplit[0])) {
                final String mount = mountPoints.get(i);
                final String[] mountSplit = mount.split(",");
                if (mountSplit.length >= 5 && !"device".equals(mountSplit[4])) {
                    deviceMap.put(diskSplit[0], mountSplit[4]);
                }
            }
        }
        return deviceMap;
    }
    
    public static Map<String, Quintet<String, String, String, String, Long>> queryDeviceStrings(final Set<String> diskSet) {
        final Map<String, Quintet<String, String, String, String, Long>> deviceParamMap = new HashMap<String, Quintet<String, String, String, String, Long>>();
        final List<String> iostat = ExecutingCommand.runNative("iostat -Er");
        String diskName = null;
        String model = "";
        String vendor = "";
        String product = "";
        String serial = "";
        long size = 0L;
        for (final String line : iostat) {
            final String[] split2;
            final String[] split = split2 = line.split(",");
            for (String keyValue : split2) {
                keyValue = keyValue.trim();
                if (diskSet.contains(keyValue)) {
                    if (diskName != null) {
                        deviceParamMap.put(diskName, new Quintet<String, String, String, String, Long>(model, vendor, product, serial, size));
                    }
                    diskName = keyValue;
                    model = "";
                    vendor = "";
                    product = "";
                    serial = "";
                    size = 0L;
                }
                else if (keyValue.startsWith("Model:")) {
                    model = keyValue.replace("Model:", "").trim();
                }
                else if (keyValue.startsWith("Serial No:")) {
                    serial = keyValue.replace("Serial No:", "").trim();
                }
                else if (keyValue.startsWith("Vendor:")) {
                    vendor = keyValue.replace("Vendor:", "").trim();
                }
                else if (keyValue.startsWith("Product:")) {
                    product = keyValue.replace("Product:", "").trim();
                }
                else if (keyValue.startsWith("Size:")) {
                    String[] bytes = keyValue.split("<");
                    if (bytes.length > 1) {
                        bytes = ParseUtil.whitespaces.split(bytes[1]);
                        size = ParseUtil.parseLongOrDefault(bytes[0], 0L);
                    }
                }
            }
            if (diskName != null) {
                deviceParamMap.put(diskName, new Quintet<String, String, String, String, Long>(model, vendor, product, serial, size));
            }
        }
        return deviceParamMap;
    }
}
