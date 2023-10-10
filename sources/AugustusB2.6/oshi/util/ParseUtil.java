// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.util.Locale;
import java.util.HashMap;
import java.util.TimeZone;
import org.slf4j.LoggerFactory;
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;
import java.util.Iterator;
import java.util.List;
import java.time.format.DateTimeParseException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.math.BigInteger;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ParseUtil
{
    private static final Logger LOG;
    private static final String DEFAULT_LOG_MSG = "{} didn't parse. Returning default. {}";
    private static final Pattern HERTZ_PATTERN;
    private static final Pattern BYTES_PATTERN;
    private static final Pattern UNITS_PATTERN;
    private static final Pattern VALID_HEX;
    private static final Pattern DHMS;
    private static final Pattern UUID_PATTERN;
    private static final Pattern VENDOR_PRODUCT_ID_SERIAL;
    private static final Pattern LSPCI_MACHINE_READABLE;
    private static final Pattern LSPCI_MEMORY_SIZE;
    private static final String HZ = "Hz";
    private static final String KHZ = "kHz";
    private static final String MHZ = "MHz";
    private static final String GHZ = "GHz";
    private static final String THZ = "THz";
    private static final String PHZ = "PHz";
    private static final Map<String, Long> multipliers;
    private static final long EPOCH_DIFF = 11644473600000L;
    private static final int TZ_OFFSET;
    public static final Pattern whitespacesColonWhitespace;
    public static final Pattern whitespaces;
    public static final Pattern notDigits;
    public static final Pattern startWithNotDigits;
    public static final Pattern slash;
    private static final long[] POWERS_OF_TEN;
    private static final DateTimeFormatter CIM_FORMAT;
    
    private ParseUtil() {
    }
    
    public static long parseHertz(final String hertz) {
        final Matcher matcher = ParseUtil.HERTZ_PATTERN.matcher(hertz.trim());
        if (matcher.find() && matcher.groupCount() == 3) {
            final double value = Double.valueOf(matcher.group(1)) * ParseUtil.multipliers.getOrDefault(matcher.group(3), -1L);
            if (value >= 0.0) {
                return (long)value;
            }
        }
        return -1L;
    }
    
    public static int parseLastInt(final String s, final int i) {
        try {
            final String ls = parseLastString(s);
            if (ls.toLowerCase().startsWith("0x")) {
                return Integer.decode(ls);
            }
            return Integer.parseInt(ls);
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return i;
        }
    }
    
    public static long parseLastLong(final String s, final long li) {
        try {
            final String ls = parseLastString(s);
            if (ls.toLowerCase().startsWith("0x")) {
                return Long.decode(ls);
            }
            return Long.parseLong(ls);
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return li;
        }
    }
    
    public static double parseLastDouble(final String s, final double d) {
        try {
            return Double.parseDouble(parseLastString(s));
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return d;
        }
    }
    
    public static String parseLastString(final String s) {
        final String[] ss = ParseUtil.whitespaces.split(s);
        return ss[ss.length - 1];
    }
    
    public static String byteArrayToHexString(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            sb.append(Character.forDigit((b & 0xF0) >>> 4, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString().toUpperCase();
    }
    
    public static byte[] hexStringToByteArray(final String digits) {
        final int len = digits.length();
        if (!ParseUtil.VALID_HEX.matcher(digits).matches() || (len & 0x1) != 0x0) {
            ParseUtil.LOG.warn("Invalid hexadecimal string: {}", digits);
            return new byte[0];
        }
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)(Character.digit(digits.charAt(i), 16) << 4 | Character.digit(digits.charAt(i + 1), 16));
        }
        return data;
    }
    
    public static byte[] asciiStringToByteArray(final String text, final int length) {
        return Arrays.copyOf(text.getBytes(StandardCharsets.US_ASCII), length);
    }
    
    public static byte[] longToByteArray(final long value, final int valueSize, final int length) {
        long val = value;
        final byte[] b = new byte[8];
        for (int i = 7; i >= 0 && val != 0L; val >>>= 8, --i) {
            b[i] = (byte)val;
        }
        return Arrays.copyOfRange(b, 8 - valueSize, 8 + length - valueSize);
    }
    
    public static long strToLong(final String str, final int size) {
        return byteArrayToLong(str.getBytes(StandardCharsets.US_ASCII), size);
    }
    
    public static long byteArrayToLong(final byte[] bytes, final int size) {
        return byteArrayToLong(bytes, size, true);
    }
    
    public static long byteArrayToLong(final byte[] bytes, final int size, final boolean bigEndian) {
        if (size > 8) {
            throw new IllegalArgumentException("Can't convert more than 8 bytes.");
        }
        if (size > bytes.length) {
            throw new IllegalArgumentException("Size can't be larger than array length.");
        }
        long total = 0L;
        for (int i = 0; i < size; ++i) {
            if (bigEndian) {
                total = (total << 8 | (long)(bytes[i] & 0xFF));
            }
            else {
                total = (total << 8 | (long)(bytes[size - i - 1] & 0xFF));
            }
        }
        return total;
    }
    
    public static float byteArrayToFloat(final byte[] bytes, final int size, final int fpBits) {
        return byteArrayToLong(bytes, size) / (float)(1 << fpBits);
    }
    
    public static long unsignedIntToLong(final int unsignedValue) {
        final long longValue = unsignedValue;
        return longValue & 0xFFFFFFFFL;
    }
    
    public static long unsignedLongToSignedLong(final long unsignedValue) {
        return unsignedValue & Long.MAX_VALUE;
    }
    
    public static String hexStringToString(final String hexString) {
        if (hexString.length() % 2 > 0) {
            return hexString;
        }
        final StringBuilder sb = new StringBuilder();
        try {
            for (int pos = 0; pos < hexString.length(); pos += 2) {
                final int charAsInt = Integer.parseInt(hexString.substring(pos, pos + 2), 16);
                if (charAsInt < 32 || charAsInt > 127) {
                    return hexString;
                }
                sb.append((char)charAsInt);
            }
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", hexString, e);
            return hexString;
        }
        return sb.toString();
    }
    
    public static int parseIntOrDefault(final String s, final int defaultInt) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return defaultInt;
        }
    }
    
    public static long parseLongOrDefault(final String s, final long defaultLong) {
        try {
            return Long.parseLong(s);
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return defaultLong;
        }
    }
    
    public static long parseUnsignedLongOrDefault(final String s, final long defaultLong) {
        try {
            return new BigInteger(s).longValue();
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return defaultLong;
        }
    }
    
    public static double parseDoubleOrDefault(final String s, final double defaultDouble) {
        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", s, e);
            return defaultDouble;
        }
    }
    
    public static long parseDHMSOrDefault(final String s, final long defaultLong) {
        final Matcher m = ParseUtil.DHMS.matcher(s);
        if (m.matches()) {
            long milliseconds = 0L;
            if (m.group(1) != null) {
                milliseconds += parseLongOrDefault(m.group(1), 0L) * 86400000L;
            }
            if (m.group(2) != null) {
                milliseconds += parseLongOrDefault(m.group(2), 0L) * 3600000L;
            }
            if (m.group(3) != null) {
                milliseconds += parseLongOrDefault(m.group(3), 0L) * 60000L;
            }
            milliseconds += parseLongOrDefault(m.group(4), 0L) * 1000L;
            if (m.group(5) != null) {
                milliseconds += (long)(1000.0 * parseDoubleOrDefault("0." + m.group(5), 0.0));
            }
            return milliseconds;
        }
        return defaultLong;
    }
    
    public static String parseUuidOrDefault(final String s, final String defaultStr) {
        final Matcher m = ParseUtil.UUID_PATTERN.matcher(s.toLowerCase());
        if (m.matches()) {
            return m.group(1);
        }
        return defaultStr;
    }
    
    public static String getSingleQuoteStringValue(final String line) {
        return getStringBetween(line, '\'');
    }
    
    public static String getDoubleQuoteStringValue(final String line) {
        return getStringBetween(line, '\"');
    }
    
    public static String getStringBetween(final String line, final char c) {
        final int firstOcc = line.indexOf(c);
        if (firstOcc < 0) {
            return "";
        }
        return line.substring(firstOcc + 1, line.lastIndexOf(c)).trim();
    }
    
    public static int getFirstIntValue(final String line) {
        return getNthIntValue(line, 1);
    }
    
    public static int getNthIntValue(final String line, final int n) {
        final String[] split = ParseUtil.notDigits.split(ParseUtil.startWithNotDigits.matcher(line).replaceFirst(""));
        if (split.length >= n) {
            return parseIntOrDefault(split[n - 1], 0);
        }
        return 0;
    }
    
    public static String removeMatchingString(final String original, final String toRemove) {
        if (original == null || original.isEmpty() || toRemove == null || toRemove.isEmpty()) {
            return original;
        }
        int matchIndex = original.indexOf(toRemove, 0);
        if (matchIndex == -1) {
            return original;
        }
        final StringBuilder buffer = new StringBuilder(original.length() - toRemove.length());
        int currIndex = 0;
        do {
            buffer.append(original.substring(currIndex, matchIndex));
            currIndex = matchIndex + toRemove.length();
            matchIndex = original.indexOf(toRemove, currIndex);
        } while (matchIndex != -1);
        buffer.append(original.substring(currIndex));
        return buffer.toString();
    }
    
    public static long[] parseStringToLongArray(final String s, final int[] indices, final int length, final char delimiter) {
        final long[] parsed = new long[indices.length];
        int charIndex = s.length();
        int parsedIndex = indices.length - 1;
        int stringIndex = length - 1;
        int power = 0;
        boolean delimCurrent = false;
        boolean numeric = true;
        boolean numberFound = false;
        boolean dashSeen = false;
        while (--charIndex > 0 && parsedIndex >= 0) {
            final int c = s.charAt(charIndex);
            if (c == delimiter) {
                if (!numberFound && numeric) {
                    numberFound = true;
                }
                if (delimCurrent) {
                    continue;
                }
                if (numberFound && indices[parsedIndex] == stringIndex--) {
                    --parsedIndex;
                }
                delimCurrent = true;
                power = 0;
                dashSeen = false;
                numeric = true;
            }
            else if (indices[parsedIndex] != stringIndex || c == 43 || !numeric) {
                delimCurrent = false;
            }
            else if (c >= 48 && c <= 57 && !dashSeen) {
                if (power > 18 || (power == 17 && c == 57 && parsed[parsedIndex] > 223372036854775807L)) {
                    parsed[parsedIndex] = Long.MAX_VALUE;
                }
                else {
                    final long[] array = parsed;
                    final int n = parsedIndex;
                    array[n] += (c - 48) * ParseUtil.POWERS_OF_TEN[power++];
                }
                delimCurrent = false;
            }
            else if (c == 45) {
                final long[] array2 = parsed;
                final int n2 = parsedIndex;
                array2[n2] *= -1L;
                delimCurrent = false;
                dashSeen = true;
            }
            else {
                if (numberFound) {
                    if (!noLog(s)) {
                        ParseUtil.LOG.error("Illegal character parsing string '{}' to long array: {}", s, s.charAt(charIndex));
                    }
                    return new long[indices.length];
                }
                parsed[parsedIndex] = 0L;
                numeric = false;
            }
        }
        if (parsedIndex > 0) {
            if (!noLog(s)) {
                ParseUtil.LOG.error("Not enough fields in string '{}' parsing to long array: {}", s, indices.length - parsedIndex);
            }
            return new long[indices.length];
        }
        return parsed;
    }
    
    private static boolean noLog(final String s) {
        return s.startsWith("NOLOG: ");
    }
    
    public static int countStringToLongArray(final String s, final char delimiter) {
        int charIndex = s.length();
        int numbers = 0;
        boolean delimCurrent = false;
        boolean numeric = true;
        boolean dashSeen = false;
        while (--charIndex > 0) {
            final int c = s.charAt(charIndex);
            if (c == delimiter) {
                if (delimCurrent) {
                    continue;
                }
                if (numeric) {
                    ++numbers;
                }
                delimCurrent = true;
                dashSeen = false;
                numeric = true;
            }
            else if (c == 43 || !numeric) {
                delimCurrent = false;
            }
            else if (c >= 48 && c <= 57 && !dashSeen) {
                delimCurrent = false;
            }
            else if (c == 45) {
                delimCurrent = false;
                dashSeen = true;
            }
            else {
                if (numbers > 0) {
                    return numbers;
                }
                numeric = false;
            }
        }
        return numbers + 1;
    }
    
    public static String getTextBetweenStrings(final String text, final String before, final String after) {
        String result = "";
        if (text.indexOf(before) >= 0 && text.indexOf(after) >= 0) {
            result = text.substring(text.indexOf(before) + before.length(), text.length());
            result = result.substring(0, result.indexOf(after));
        }
        return result;
    }
    
    public static long filetimeToUtcMs(final long filetime, final boolean local) {
        return filetime / 10000L - 11644473600000L - (local ? ParseUtil.TZ_OFFSET : 0L);
    }
    
    public static String parseMmDdYyyyToYyyyMmDD(final String dateString) {
        try {
            return String.format("%s-%s-%s", dateString.substring(6, 10), dateString.substring(0, 2), dateString.substring(3, 5));
        }
        catch (StringIndexOutOfBoundsException e) {
            return dateString;
        }
    }
    
    public static OffsetDateTime parseCimDateTimeToOffset(final String cimDateTime) {
        try {
            final int tzInMinutes = Integer.parseInt(cimDateTime.substring(22));
            final LocalTime offsetAsLocalTime = LocalTime.MIDNIGHT.plusMinutes(tzInMinutes);
            return OffsetDateTime.parse(cimDateTime.substring(0, 22) + offsetAsLocalTime.format(DateTimeFormatter.ISO_LOCAL_TIME), ParseUtil.CIM_FORMAT);
        }
        catch (IndexOutOfBoundsException | NumberFormatException | DateTimeParseException ex2) {
            final RuntimeException ex;
            final RuntimeException e = ex;
            ParseUtil.LOG.trace("Unable to parse {} to CIM DateTime.", cimDateTime);
            return Constants.UNIX_EPOCH;
        }
    }
    
    public static boolean filePathStartsWith(final List<String> prefixList, final String path) {
        for (final String match : prefixList) {
            if (path.equals(match) || path.startsWith(match + "/")) {
                return true;
            }
        }
        return false;
    }
    
    public static long parseMultipliedToLongs(final String count) {
        final Matcher matcher = ParseUtil.UNITS_PATTERN.matcher(count.trim());
        String[] mem;
        if (matcher.find() && matcher.groupCount() == 3) {
            mem = new String[] { matcher.group(1), matcher.group(3) };
        }
        else {
            mem = new String[] { count };
        }
        double number = parseDoubleOrDefault(mem[0], 0.0);
        if (mem.length == 2 && mem[1] != null && mem[1].length() >= 1) {
            switch (mem[1].charAt(0)) {
                case 'T': {
                    number *= 1.0E12;
                    break;
                }
                case 'G': {
                    number *= 1.0E9;
                    break;
                }
                case 'M': {
                    number *= 1000000.0;
                    break;
                }
                case 'K':
                case 'k': {
                    number *= 1000.0;
                    break;
                }
            }
        }
        return (long)number;
    }
    
    public static long parseDecimalMemorySizeToBinary(final String size) {
        String[] mem = ParseUtil.whitespaces.split(size);
        if (mem.length < 2) {
            final Matcher matcher = ParseUtil.BYTES_PATTERN.matcher(size.trim());
            if (matcher.find() && matcher.groupCount() == 2) {
                mem = new String[] { matcher.group(1), matcher.group(2) };
            }
        }
        long capacity = parseLongOrDefault(mem[0], 0L);
        if (mem.length == 2 && mem[1].length() > 1) {
            switch (mem[1].charAt(0)) {
                case 'T': {
                    capacity <<= 40;
                    break;
                }
                case 'G': {
                    capacity <<= 30;
                    break;
                }
                case 'M': {
                    capacity <<= 20;
                    break;
                }
                case 'K':
                case 'k': {
                    capacity <<= 10;
                    break;
                }
            }
        }
        return capacity;
    }
    
    public static Triplet<String, String, String> parseDeviceIdToVendorProductSerial(final String deviceId) {
        final Matcher m = ParseUtil.VENDOR_PRODUCT_ID_SERIAL.matcher(deviceId);
        if (m.matches()) {
            final String vendorId = "0x" + m.group(1).toLowerCase();
            final String productId = "0x" + m.group(2).toLowerCase();
            final String serial = m.group(4);
            return new Triplet<String, String, String>(vendorId, productId, (!m.group(3).isEmpty() || serial.contains("&")) ? "" : serial);
        }
        return null;
    }
    
    public static long parseLshwResourceString(final String resources) {
        long bytes = 0L;
        final String[] split;
        final String[] resourceArray = split = ParseUtil.whitespaces.split(resources);
        for (final String r : split) {
            if (r.startsWith("memory:")) {
                final String[] mem = r.substring(7).split("-");
                if (mem.length == 2) {
                    try {
                        bytes += Long.parseLong(mem[1], 16) - Long.parseLong(mem[0], 16) + 1L;
                    }
                    catch (NumberFormatException e) {
                        ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", r, e);
                    }
                }
            }
        }
        return bytes;
    }
    
    public static Pair<String, String> parseLspciMachineReadable(final String line) {
        final Matcher matcher = ParseUtil.LSPCI_MACHINE_READABLE.matcher(line);
        if (matcher.matches()) {
            return new Pair<String, String>(matcher.group(1), matcher.group(2));
        }
        return null;
    }
    
    public static long parseLspciMemorySize(final String line) {
        final Matcher matcher = ParseUtil.LSPCI_MEMORY_SIZE.matcher(line);
        if (matcher.matches()) {
            return parseDecimalMemorySizeToBinary(matcher.group(1) + " " + matcher.group(2) + "B");
        }
        return 0L;
    }
    
    public static List<Integer> parseHyphenatedIntList(final String str) {
        final List<Integer> result = new ArrayList<Integer>();
        for (final String s : ParseUtil.whitespaces.split(str)) {
            if (s.contains("-")) {
                final int first = getFirstIntValue(s);
                for (int last = getNthIntValue(s, 2), i = first; i <= last; ++i) {
                    result.add(i);
                }
            }
            else {
                final int only = parseIntOrDefault(s, -1);
                if (only >= 0) {
                    result.add(only);
                }
            }
        }
        return result;
    }
    
    public static byte[] parseIntToIP(final int ip) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ip).array();
    }
    
    public static byte[] parseIntArrayToIP(final int[] ip6) {
        final ByteBuffer bb = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
        for (final int i : ip6) {
            bb.putInt(i);
        }
        return bb.array();
    }
    
    public static int bigEndian16ToLittleEndian(final int port) {
        return (port >> 8 & 0xFF) | (port << 8 & 0xFF00);
    }
    
    public static String parseUtAddrV6toIP(final int[] utAddrV6) {
        if (utAddrV6.length != 4) {
            throw new IllegalArgumentException("ut_addr_v6 must have exactly 4 elements");
        }
        if (utAddrV6[1] == 0 && utAddrV6[2] == 0 && utAddrV6[3] == 0) {
            if (utAddrV6[0] == 0) {
                return "::";
            }
            final byte[] ipv4 = ByteBuffer.allocate(4).putInt(utAddrV6[0]).array();
            try {
                return InetAddress.getByAddress(ipv4).getHostAddress();
            }
            catch (UnknownHostException e) {
                return "unknown";
            }
        }
        final byte[] ipv5 = ByteBuffer.allocate(16).putInt(utAddrV6[0]).putInt(utAddrV6[1]).putInt(utAddrV6[2]).putInt(utAddrV6[3]).array();
        try {
            return InetAddress.getByAddress(ipv5).getHostAddress().replaceAll("((?:(?:^|:)0+\\b){2,}):?(?!\\S*\\b\\1:0+\\b)(\\S*)", "::$2");
        }
        catch (UnknownHostException e) {
            return "unknown";
        }
    }
    
    public static int hexStringToInt(final String hexString, final int defaultValue) {
        if (hexString != null) {
            try {
                if (hexString.startsWith("0x")) {
                    return new BigInteger(hexString.substring(2), 16).intValue();
                }
                return new BigInteger(hexString, 16).intValue();
            }
            catch (NumberFormatException e) {
                ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", hexString, e);
            }
        }
        return defaultValue;
    }
    
    public static long hexStringToLong(final String hexString, final long defaultValue) {
        if (hexString != null) {
            try {
                if (hexString.startsWith("0x")) {
                    return new BigInteger(hexString.substring(2), 16).longValue();
                }
                return new BigInteger(hexString, 16).longValue();
            }
            catch (NumberFormatException e) {
                ParseUtil.LOG.trace("{} didn't parse. Returning default. {}", hexString, e);
            }
        }
        return defaultValue;
    }
    
    public static String removeLeadingDots(final String dotPrefixedStr) {
        int pos;
        for (pos = 0; pos < dotPrefixedStr.length() && dotPrefixedStr.charAt(pos) == '.'; ++pos) {}
        return (pos < dotPrefixedStr.length()) ? dotPrefixedStr.substring(pos) : "";
    }
    
    public static List<String> parseByteArrayToStrings(final byte[] bytes) {
        final List<String> strList = new ArrayList<String>();
        int start = 0;
        int end = 0;
        do {
            if (end == bytes.length || bytes[end] == 0) {
                if (start == end) {
                    break;
                }
                strList.add(new String(bytes, start, end - start, StandardCharsets.UTF_8));
                start = end + 1;
            }
        } while (end++ < bytes.length);
        return strList;
    }
    
    public static Map<String, String> parseByteArrayToStringMap(final byte[] bytes) {
        final Map<String, String> strMap = new LinkedHashMap<String, String>();
        int start = 0;
        int end = 0;
        String key = null;
        do {
            if (end == bytes.length || bytes[end] == 0) {
                if (start == end && key == null) {
                    break;
                }
                strMap.put(key, new String(bytes, start, end - start, StandardCharsets.UTF_8));
                key = null;
                start = end + 1;
            }
            else {
                if (bytes[end] != 61 || key != null) {
                    continue;
                }
                key = new String(bytes, start, end - start, StandardCharsets.UTF_8);
                start = end + 1;
            }
        } while (end++ < bytes.length);
        return strMap;
    }
    
    public static Map<String, String> parseCharArrayToStringMap(final char[] chars) {
        final Map<String, String> strMap = new LinkedHashMap<String, String>();
        int start = 0;
        int end = 0;
        String key = null;
        do {
            if (end == chars.length || chars[end] == '\0') {
                if (start == end && key == null) {
                    break;
                }
                strMap.put(key, new String(chars, start, end - start));
                key = null;
                start = end + 1;
            }
            else {
                if (chars[end] != '=' || key != null) {
                    continue;
                }
                key = new String(chars, start, end - start);
                start = end + 1;
            }
        } while (end++ < chars.length);
        return strMap;
    }
    
    public static <K extends Enum<K>> Map<K, String> stringToEnumMap(final Class<K> clazz, final String values, final char delim) {
        final EnumMap<K, String> map = new EnumMap<K, String>(clazz);
        int start = 0;
        final int len = values.length();
        final EnumSet<K> keys = EnumSet.allOf(clazz);
        int keySize = keys.size();
        for (final K key : keys) {
            final int idx = (--keySize == 0) ? len : values.indexOf(delim, start);
            if (idx < 0) {
                map.put(key, values.substring(start));
                break;
            }
            map.put(key, values.substring(start, idx));
            start = idx;
            while (++start < len && values.charAt(start) == delim) {}
        }
        return map;
    }
    
    static {
        LOG = LoggerFactory.getLogger(ParseUtil.class);
        HERTZ_PATTERN = Pattern.compile("(\\d+(.\\d+)?) ?([kMGT]?Hz).*");
        BYTES_PATTERN = Pattern.compile("(\\d+) ?([kMGT]?B).*");
        UNITS_PATTERN = Pattern.compile("(\\d+(.\\d+)?)[\\s]?([kKMGT])?");
        VALID_HEX = Pattern.compile("[0-9a-fA-F]+");
        DHMS = Pattern.compile("(?:(\\d+)-)?(?:(\\d+):)??(?:(\\d+):)?(\\d+)(?:\\.(\\d+))?");
        UUID_PATTERN = Pattern.compile(".*([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}).*");
        VENDOR_PRODUCT_ID_SERIAL = Pattern.compile(".*(?:VID|VEN)_(\\p{XDigit}{4})&(?:PID|DEV)_(\\p{XDigit}{4})(.*)\\\\(.*)");
        LSPCI_MACHINE_READABLE = Pattern.compile("(.+)\\s\\[(.*?)\\]");
        LSPCI_MEMORY_SIZE = Pattern.compile(".+\\s\\[size=(\\d+)([kKMGT])\\]");
        TZ_OFFSET = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        whitespacesColonWhitespace = Pattern.compile("\\s+:\\s");
        whitespaces = Pattern.compile("\\s+");
        notDigits = Pattern.compile("[^0-9]+");
        startWithNotDigits = Pattern.compile("^[^0-9]*");
        slash = Pattern.compile("\\/");
        (multipliers = new HashMap<String, Long>()).put("Hz", 1L);
        ParseUtil.multipliers.put("kHz", 1000L);
        ParseUtil.multipliers.put("MHz", 1000000L);
        ParseUtil.multipliers.put("GHz", 1000000000L);
        ParseUtil.multipliers.put("THz", 1000000000000L);
        ParseUtil.multipliers.put("PHz", 1000000000000000L);
        POWERS_OF_TEN = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
        CIM_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSSSSZZZZZ", Locale.US);
    }
}
