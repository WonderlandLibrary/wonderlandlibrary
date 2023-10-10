// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class EdidUtil
{
    private static final Logger LOG;
    
    private EdidUtil() {
    }
    
    public static String getManufacturerID(final byte[] edid) {
        final String temp = String.format("%8s%8s", Integer.toBinaryString(edid[8] & 0xFF), Integer.toBinaryString(edid[9] & 0xFF)).replace(' ', '0');
        EdidUtil.LOG.debug("Manufacurer ID: {}", temp);
        return String.format("%s%s%s", (char)(64 + Integer.parseInt(temp.substring(1, 6), 2)), (char)(64 + Integer.parseInt(temp.substring(7, 11), 2)), (char)(64 + Integer.parseInt(temp.substring(12, 16), 2))).replace("@", "");
    }
    
    public static String getProductID(final byte[] edid) {
        return Integer.toHexString(ByteBuffer.wrap(Arrays.copyOfRange(edid, 10, 12)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
    }
    
    public static String getSerialNo(final byte[] edid) {
        if (EdidUtil.LOG.isDebugEnabled()) {
            EdidUtil.LOG.debug("Serial number: {}", Arrays.toString(Arrays.copyOfRange(edid, 12, 16)));
        }
        return String.format("%s%s%s%s", getAlphaNumericOrHex(edid[15]), getAlphaNumericOrHex(edid[14]), getAlphaNumericOrHex(edid[13]), getAlphaNumericOrHex(edid[12]));
    }
    
    private static String getAlphaNumericOrHex(final byte b) {
        return Character.isLetterOrDigit((char)b) ? String.format("%s", (char)b) : String.format("%02X", b);
    }
    
    public static byte getWeek(final byte[] edid) {
        return edid[16];
    }
    
    public static int getYear(final byte[] edid) {
        final byte temp = edid[17];
        EdidUtil.LOG.debug("Year-1990: {}", (Object)temp);
        return temp + 1990;
    }
    
    public static String getVersion(final byte[] edid) {
        return edid[18] + "." + edid[19];
    }
    
    public static boolean isDigital(final byte[] edid) {
        return 1 == (edid[20] & 0xFF) >> 7;
    }
    
    public static int getHcm(final byte[] edid) {
        return edid[21];
    }
    
    public static int getVcm(final byte[] edid) {
        return edid[22];
    }
    
    public static byte[][] getDescriptors(final byte[] edid) {
        final byte[][] desc = new byte[4][18];
        for (int i = 0; i < desc.length; ++i) {
            System.arraycopy(edid, 54 + 18 * i, desc[i], 0, 18);
        }
        return desc;
    }
    
    public static int getDescriptorType(final byte[] desc) {
        return ByteBuffer.wrap(Arrays.copyOfRange(desc, 0, 4)).getInt();
    }
    
    public static String getTimingDescriptor(final byte[] desc) {
        final int clock = ByteBuffer.wrap(Arrays.copyOfRange(desc, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        final int hActive = (desc[2] & 0xFF) + ((desc[4] & 0xF0) << 4);
        final int vActive = (desc[5] & 0xFF) + ((desc[7] & 0xF0) << 4);
        return String.format("Clock %dMHz, Active Pixels %dx%d ", clock, hActive, vActive);
    }
    
    public static String getDescriptorRangeLimits(final byte[] desc) {
        return String.format("Field Rate %d-%d Hz vertical, %d-%d Hz horizontal, Max clock: %d MHz", desc[5], desc[6], desc[7], desc[8], desc[9] * 10);
    }
    
    public static String getDescriptorText(final byte[] desc) {
        return new String(Arrays.copyOfRange(desc, 4, 18), StandardCharsets.US_ASCII).trim();
    }
    
    public static String toString(final byte[] edid) {
        final StringBuilder sb = new StringBuilder();
        sb.append("  Manuf. ID=").append(getManufacturerID(edid));
        sb.append(", Product ID=").append(getProductID(edid));
        sb.append(", ").append(isDigital(edid) ? "Digital" : "Analog");
        sb.append(", Serial=").append(getSerialNo(edid));
        sb.append(", ManufDate=").append(getWeek(edid) * 12 / 52 + 1).append('/').append(getYear(edid));
        sb.append(", EDID v").append(getVersion(edid));
        final int hSize = getHcm(edid);
        final int vSize = getVcm(edid);
        sb.append(String.format("%n  %d x %d cm (%.1f x %.1f in)", hSize, vSize, hSize / 2.54, vSize / 2.54));
        final byte[][] descriptors;
        final byte[][] desc = descriptors = getDescriptors(edid);
        for (final byte[] b : descriptors) {
            switch (getDescriptorType(b)) {
                case 255: {
                    sb.append("\n  Serial Number: ").append(getDescriptorText(b));
                    break;
                }
                case 254: {
                    sb.append("\n  Unspecified Text: ").append(getDescriptorText(b));
                    break;
                }
                case 253: {
                    sb.append("\n  Range Limits: ").append(getDescriptorRangeLimits(b));
                    break;
                }
                case 252: {
                    sb.append("\n  Monitor Name: ").append(getDescriptorText(b));
                    break;
                }
                case 251: {
                    sb.append("\n  White Point Data: ").append(ParseUtil.byteArrayToHexString(b));
                    break;
                }
                case 250: {
                    sb.append("\n  Standard Timing ID: ").append(ParseUtil.byteArrayToHexString(b));
                    break;
                }
                default: {
                    if (getDescriptorType(b) <= 15 && getDescriptorType(b) >= 0) {
                        sb.append("\n  Manufacturer Data: ").append(ParseUtil.byteArrayToHexString(b));
                        break;
                    }
                    sb.append("\n  Preferred Timing: ").append(getTimingDescriptor(b));
                    break;
                }
            }
        }
        return sb.toString();
    }
    
    static {
        LOG = LoggerFactory.getLogger(EdidUtil.class);
    }
}
