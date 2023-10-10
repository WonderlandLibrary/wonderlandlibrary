// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux;

import oshi.util.ParseUtil;
import oshi.util.Util;
import oshi.util.FileUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Sysfs
{
    private Sysfs() {
    }
    
    public static String querySystemVendor() {
        final String sysVendor = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/sys_vendor").trim();
        if (!sysVendor.isEmpty()) {
            return sysVendor;
        }
        return null;
    }
    
    public static String queryProductModel() {
        final String productName = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_name").trim();
        final String productVersion = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_version").trim();
        if (productName.isEmpty()) {
            if (!productVersion.isEmpty()) {
                return productVersion;
            }
            return null;
        }
        else {
            if (!productVersion.isEmpty() && !"None".equals(productVersion)) {
                return productName + " (version: " + productVersion + ")";
            }
            return productName;
        }
    }
    
    public static String queryProductSerial() {
        final String serial = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_serial");
        if (!serial.isEmpty() && !"None".equals(serial)) {
            return serial;
        }
        return queryBoardSerial();
    }
    
    public static String queryUUID() {
        final String uuid = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/product_uuid");
        if (!uuid.isEmpty() && !"None".equals(uuid)) {
            return uuid;
        }
        return null;
    }
    
    public static String queryBoardVendor() {
        final String boardVendor = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_vendor").trim();
        if (!boardVendor.isEmpty()) {
            return boardVendor;
        }
        return null;
    }
    
    public static String queryBoardModel() {
        final String boardName = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_name").trim();
        if (!boardName.isEmpty()) {
            return boardName;
        }
        return null;
    }
    
    public static String queryBoardVersion() {
        final String boardVersion = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_version").trim();
        if (!boardVersion.isEmpty()) {
            return boardVersion;
        }
        return null;
    }
    
    public static String queryBoardSerial() {
        final String boardSerial = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/board_serial").trim();
        if (!boardSerial.isEmpty()) {
            return boardSerial;
        }
        return null;
    }
    
    public static String queryBiosVendor() {
        final String biosVendor = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/bios_vendor").trim();
        if (biosVendor.isEmpty()) {
            return biosVendor;
        }
        return null;
    }
    
    public static String queryBiosDescription() {
        final String modalias = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/modalias").trim();
        if (!modalias.isEmpty()) {
            return modalias;
        }
        return null;
    }
    
    public static String queryBiosVersion(final String biosRevision) {
        final String biosVersion = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/bios_version").trim();
        if (!biosVersion.isEmpty()) {
            return biosVersion + (Util.isBlank(biosRevision) ? "" : (" (revision " + biosRevision + ")"));
        }
        return null;
    }
    
    public static String queryBiosReleaseDate() {
        final String biosDate = FileUtil.getStringFromFile("/sys/devices/virtual/dmi/id/bios_date").trim();
        if (!biosDate.isEmpty()) {
            return ParseUtil.parseMmDdYyyyToYyyyMmDD(biosDate);
        }
        return null;
    }
}
