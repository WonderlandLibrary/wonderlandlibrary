/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ClassLoaderUtil;
import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.util.ICUUncheckedIOException;
import com.ibm.icu.util.VersionInfo;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ICUBinary {
    private static final List<DataFile> icuDataFiles = new ArrayList<DataFile>();
    private static final byte MAGIC1 = -38;
    private static final byte MAGIC2 = 39;
    private static final byte CHAR_SET_ = 0;
    private static final byte CHAR_SIZE_ = 2;
    private static final String MAGIC_NUMBER_AUTHENTICATION_FAILED_ = "ICU data file error: Not an ICU data file";
    private static final String HEADER_AUTHENTICATION_FAILED_ = "ICU data file error: Header authentication failed, please check if you have a valid ICU data file";

    private static void addDataFilesFromPath(String dataPath, List<DataFile> files) {
        int pathStart = 0;
        while (pathStart < dataPath.length()) {
            int sepIndex = dataPath.indexOf(File.pathSeparatorChar, pathStart);
            int pathLimit = sepIndex >= 0 ? sepIndex : dataPath.length();
            String path = dataPath.substring(pathStart, pathLimit).trim();
            if (path.endsWith(File.separator)) {
                path = path.substring(0, path.length() - 1);
            }
            if (path.length() != 0) {
                ICUBinary.addDataFilesFromFolder(new File(path), new StringBuilder(), icuDataFiles);
            }
            if (sepIndex < 0) break;
            pathStart = sepIndex + 1;
        }
    }

    private static void addDataFilesFromFolder(File folder, StringBuilder itemPath, List<DataFile> dataFiles) {
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        int folderPathLength = itemPath.length();
        if (folderPathLength > 0) {
            itemPath.append('/');
            ++folderPathLength;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".txt")) continue;
            itemPath.append(fileName);
            if (file.isDirectory()) {
                ICUBinary.addDataFilesFromFolder(file, itemPath, dataFiles);
            } else if (fileName.endsWith(".dat")) {
                ByteBuffer pkgBytes = ICUBinary.mapFile(file);
                if (pkgBytes != null && DatPackageReader.validate(pkgBytes)) {
                    dataFiles.add(new PackageDataFile(itemPath.toString(), pkgBytes));
                }
            } else {
                dataFiles.add(new SingleDataFile(itemPath.toString(), file));
            }
            itemPath.setLength(folderPathLength);
        }
    }

    static int compareKeys(CharSequence key, ByteBuffer bytes, int offset) {
        int i2 = 0;
        while (true) {
            byte c2;
            if ((c2 = bytes.get(offset)) == 0) {
                if (i2 == key.length()) {
                    return 0;
                }
                return 1;
            }
            if (i2 == key.length()) {
                return -1;
            }
            int diff = key.charAt(i2) - c2;
            if (diff != 0) {
                return diff;
            }
            ++i2;
            ++offset;
        }
    }

    static int compareKeys(CharSequence key, byte[] bytes, int offset) {
        int i2 = 0;
        while (true) {
            byte c2;
            if ((c2 = bytes[offset]) == 0) {
                if (i2 == key.length()) {
                    return 0;
                }
                return 1;
            }
            if (i2 == key.length()) {
                return -1;
            }
            int diff = key.charAt(i2) - c2;
            if (diff != 0) {
                return diff;
            }
            ++i2;
            ++offset;
        }
    }

    public static ByteBuffer getData(String itemPath) {
        return ICUBinary.getData(null, null, itemPath, false);
    }

    public static ByteBuffer getData(ClassLoader loader, String resourceName, String itemPath) {
        return ICUBinary.getData(loader, resourceName, itemPath, false);
    }

    public static ByteBuffer getRequiredData(String itemPath) {
        return ICUBinary.getData(null, null, itemPath, true);
    }

    private static ByteBuffer getData(ClassLoader loader, String resourceName, String itemPath, boolean required) {
        ByteBuffer bytes = ICUBinary.getDataFromFile(itemPath);
        if (bytes != null) {
            return bytes;
        }
        if (loader == null) {
            loader = ClassLoaderUtil.getClassLoader(ICUData.class);
        }
        if (resourceName == null) {
            resourceName = "com/ibm/icu/impl/data/icudt62b/" + itemPath;
        }
        ByteBuffer buffer = null;
        try {
            InputStream is = ICUData.getStream(loader, resourceName, required);
            if (is == null) {
                return null;
            }
            buffer = ICUBinary.getByteBufferFromInputStreamAndCloseStream(is);
        }
        catch (IOException e2) {
            throw new ICUUncheckedIOException(e2);
        }
        return buffer;
    }

    private static ByteBuffer getDataFromFile(String itemPath) {
        for (DataFile dataFile : icuDataFiles) {
            ByteBuffer data = dataFile.getData(itemPath);
            if (data == null) continue;
            return data;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static ByteBuffer mapFile(File path) {
        try {
            FileInputStream file = new FileInputStream(path);
            FileChannel channel = file.getChannel();
            MappedByteBuffer bytes = null;
            try {
                bytes = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
            }
            finally {
                file.close();
            }
            return bytes;
        }
        catch (FileNotFoundException ignored) {
            System.err.println(ignored);
        }
        catch (IOException ignored) {
            System.err.println(ignored);
        }
        return null;
    }

    public static void addBaseNamesInFileFolder(String folder, String suffix, Set<String> names) {
        for (DataFile dataFile : icuDataFiles) {
            dataFile.addBaseNamesInFolder(folder, suffix, names);
        }
    }

    public static VersionInfo readHeaderAndDataVersion(ByteBuffer bytes, int dataFormat, Authenticate authenticate) throws IOException {
        return ICUBinary.getVersionInfoFromCompactInt(ICUBinary.readHeader(bytes, dataFormat, authenticate));
    }

    public static int readHeader(ByteBuffer bytes, int dataFormat, Authenticate authenticate) throws IOException {
        assert (bytes != null && bytes.position() == 0);
        byte magic1 = bytes.get(2);
        byte magic2 = bytes.get(3);
        if (magic1 != -38 || magic2 != 39) {
            throw new IOException(MAGIC_NUMBER_AUTHENTICATION_FAILED_);
        }
        byte isBigEndian = bytes.get(8);
        byte charsetFamily = bytes.get(9);
        byte sizeofUChar = bytes.get(10);
        if (isBigEndian < 0 || 1 < isBigEndian || charsetFamily != 0 || sizeofUChar != 2) {
            throw new IOException(HEADER_AUTHENTICATION_FAILED_);
        }
        bytes.order(isBigEndian != 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        char headerSize = bytes.getChar(0);
        char sizeofUDataInfo = bytes.getChar(4);
        if (sizeofUDataInfo < '\u0014' || headerSize < sizeofUDataInfo + 4) {
            throw new IOException("Internal Error: Header size error");
        }
        byte[] formatVersion = new byte[]{bytes.get(16), bytes.get(17), bytes.get(18), bytes.get(19)};
        if (bytes.get(12) != (byte)(dataFormat >> 24) || bytes.get(13) != (byte)(dataFormat >> 16) || bytes.get(14) != (byte)(dataFormat >> 8) || bytes.get(15) != (byte)dataFormat || authenticate != null && !authenticate.isDataVersionAcceptable(formatVersion)) {
            throw new IOException(HEADER_AUTHENTICATION_FAILED_ + String.format("; data format %02x%02x%02x%02x, format version %d.%d.%d.%d", bytes.get(12), bytes.get(13), bytes.get(14), bytes.get(15), formatVersion[0] & 0xFF, formatVersion[1] & 0xFF, formatVersion[2] & 0xFF, formatVersion[3] & 0xFF));
        }
        bytes.position(headerSize);
        return bytes.get(20) << 24 | (bytes.get(21) & 0xFF) << 16 | (bytes.get(22) & 0xFF) << 8 | bytes.get(23) & 0xFF;
    }

    public static int writeHeader(int dataFormat, int formatVersion, int dataVersion, DataOutputStream dos) throws IOException {
        dos.writeChar(32);
        dos.writeByte(-38);
        dos.writeByte(39);
        dos.writeChar(20);
        dos.writeChar(0);
        dos.writeByte(1);
        dos.writeByte(0);
        dos.writeByte(2);
        dos.writeByte(0);
        dos.writeInt(dataFormat);
        dos.writeInt(formatVersion);
        dos.writeInt(dataVersion);
        dos.writeLong(0L);
        assert (dos.size() == 32);
        return 32;
    }

    public static void skipBytes(ByteBuffer bytes, int skipLength) {
        if (skipLength > 0) {
            bytes.position(bytes.position() + skipLength);
        }
    }

    public static String getString(ByteBuffer bytes, int length, int additionalSkipLength) {
        CharBuffer cs = bytes.asCharBuffer();
        String s2 = cs.subSequence(0, length).toString();
        ICUBinary.skipBytes(bytes, length * 2 + additionalSkipLength);
        return s2;
    }

    public static char[] getChars(ByteBuffer bytes, int length, int additionalSkipLength) {
        char[] dest = new char[length];
        bytes.asCharBuffer().get(dest);
        ICUBinary.skipBytes(bytes, length * 2 + additionalSkipLength);
        return dest;
    }

    public static short[] getShorts(ByteBuffer bytes, int length, int additionalSkipLength) {
        short[] dest = new short[length];
        bytes.asShortBuffer().get(dest);
        ICUBinary.skipBytes(bytes, length * 2 + additionalSkipLength);
        return dest;
    }

    public static int[] getInts(ByteBuffer bytes, int length, int additionalSkipLength) {
        int[] dest = new int[length];
        bytes.asIntBuffer().get(dest);
        ICUBinary.skipBytes(bytes, length * 4 + additionalSkipLength);
        return dest;
    }

    public static long[] getLongs(ByteBuffer bytes, int length, int additionalSkipLength) {
        long[] dest = new long[length];
        bytes.asLongBuffer().get(dest);
        ICUBinary.skipBytes(bytes, length * 8 + additionalSkipLength);
        return dest;
    }

    public static ByteBuffer sliceWithOrder(ByteBuffer bytes) {
        ByteBuffer b2 = bytes.slice();
        return b2.order(bytes.order());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ByteBuffer getByteBufferFromInputStreamAndCloseStream(InputStream is) throws IOException {
        try {
            int avail = is.available();
            byte[] bytes = avail > 32 ? new byte[avail] : new byte[128];
            int length = 0;
            while (true) {
                if (length < bytes.length) {
                    int numRead = is.read(bytes, length, bytes.length - length);
                    if (numRead < 0) break;
                    length += numRead;
                    continue;
                }
                int nextByte = is.read();
                if (nextByte < 0) break;
                int capacity = 2 * bytes.length;
                if (capacity < 128) {
                    capacity = 128;
                } else if (capacity < 16384) {
                    capacity *= 2;
                }
                byte[] newBytes = new byte[capacity];
                System.arraycopy(bytes, 0, newBytes, 0, length);
                bytes = newBytes;
                bytes[length++] = (byte)nextByte;
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, length);
            return byteBuffer;
        }
        finally {
            is.close();
        }
    }

    public static VersionInfo getVersionInfoFromCompactInt(int version) {
        return VersionInfo.getInstance(version >>> 24, version >> 16 & 0xFF, version >> 8 & 0xFF, version & 0xFF);
    }

    public static byte[] getVersionByteArrayFromCompactInt(int version) {
        return new byte[]{(byte)(version >> 24), (byte)(version >> 16), (byte)(version >> 8), (byte)version};
    }

    static {
        String dataPath = ICUConfig.get(ICUBinary.class.getName() + ".dataPath");
        if (dataPath != null) {
            ICUBinary.addDataFilesFromPath(dataPath, icuDataFiles);
        }
    }

    public static interface Authenticate {
        public boolean isDataVersionAcceptable(byte[] var1);
    }

    private static final class PackageDataFile
    extends DataFile {
        private final ByteBuffer pkgBytes;

        PackageDataFile(String item, ByteBuffer bytes) {
            super(item);
            this.pkgBytes = bytes;
        }

        @Override
        ByteBuffer getData(String requestedPath) {
            return DatPackageReader.getData(this.pkgBytes, requestedPath);
        }

        @Override
        void addBaseNamesInFolder(String folder, String suffix, Set<String> names) {
            DatPackageReader.addBaseNamesInFolder(this.pkgBytes, folder, suffix, names);
        }
    }

    private static final class SingleDataFile
    extends DataFile {
        private final File path;

        SingleDataFile(String item, File path) {
            super(item);
            this.path = path;
        }

        @Override
        public String toString() {
            return this.path.toString();
        }

        @Override
        ByteBuffer getData(String requestedPath) {
            if (requestedPath.equals(this.itemPath)) {
                return ICUBinary.mapFile(this.path);
            }
            return null;
        }

        @Override
        void addBaseNamesInFolder(String folder, String suffix, Set<String> names) {
            if (this.itemPath.length() > folder.length() + suffix.length() && this.itemPath.startsWith(folder) && this.itemPath.endsWith(suffix) && this.itemPath.charAt(folder.length()) == '/' && this.itemPath.indexOf(47, folder.length() + 1) < 0) {
                names.add(this.itemPath.substring(folder.length() + 1, this.itemPath.length() - suffix.length()));
            }
        }
    }

    private static abstract class DataFile {
        protected final String itemPath;

        DataFile(String item) {
            this.itemPath = item;
        }

        public String toString() {
            return this.itemPath;
        }

        abstract ByteBuffer getData(String var1);

        abstract void addBaseNamesInFolder(String var1, String var2, Set<String> var3);
    }

    private static final class DatPackageReader {
        private static final int DATA_FORMAT = 1131245124;
        private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();

        private DatPackageReader() {
        }

        static boolean validate(ByteBuffer bytes) {
            try {
                ICUBinary.readHeader(bytes, 1131245124, IS_ACCEPTABLE);
            }
            catch (IOException ignored) {
                return false;
            }
            int count = bytes.getInt(bytes.position());
            if (count <= 0) {
                return false;
            }
            if (bytes.position() + 4 + count * 24 > bytes.capacity()) {
                return false;
            }
            return DatPackageReader.startsWithPackageName(bytes, DatPackageReader.getNameOffset(bytes, 0)) && DatPackageReader.startsWithPackageName(bytes, DatPackageReader.getNameOffset(bytes, count - 1));
        }

        private static boolean startsWithPackageName(ByteBuffer bytes, int start) {
            byte c2;
            int length = "icudt62b".length() - 1;
            for (int i2 = 0; i2 < length; ++i2) {
                if (bytes.get(start + i2) == "icudt62b".charAt(i2)) continue;
                return false;
            }
            return ((c2 = bytes.get(start + length++)) == 98 || c2 == 108) && bytes.get(start + length) == 47;
        }

        static ByteBuffer getData(ByteBuffer bytes, CharSequence key) {
            int index = DatPackageReader.binarySearch(bytes, key);
            if (index >= 0) {
                ByteBuffer data = bytes.duplicate();
                data.position(DatPackageReader.getDataOffset(bytes, index));
                data.limit(DatPackageReader.getDataOffset(bytes, index + 1));
                return ICUBinary.sliceWithOrder(data);
            }
            return null;
        }

        static void addBaseNamesInFolder(ByteBuffer bytes, String folder, String suffix, Set<String> names) {
            int index = DatPackageReader.binarySearch(bytes, folder);
            if (index < 0) {
                index ^= 0xFFFFFFFF;
            }
            int base = bytes.position();
            int count = bytes.getInt(base);
            StringBuilder sb = new StringBuilder();
            while (index < count && DatPackageReader.addBaseName(bytes, index, folder, suffix, sb, names)) {
                ++index;
            }
        }

        private static int binarySearch(ByteBuffer bytes, CharSequence key) {
            int base = bytes.position();
            int count = bytes.getInt(base);
            int start = 0;
            int limit = count;
            while (start < limit) {
                int mid = start + limit >>> 1;
                int nameOffset = DatPackageReader.getNameOffset(bytes, mid);
                int result = ICUBinary.compareKeys(key, bytes, nameOffset += "icudt62b".length() + 1);
                if (result < 0) {
                    limit = mid;
                    continue;
                }
                if (result > 0) {
                    start = mid + 1;
                    continue;
                }
                return mid;
            }
            return ~start;
        }

        private static int getNameOffset(ByteBuffer bytes, int index) {
            int base = bytes.position();
            assert (0 <= index && index < bytes.getInt(base));
            return base + bytes.getInt(base + 4 + index * 8);
        }

        private static int getDataOffset(ByteBuffer bytes, int index) {
            int base = bytes.position();
            int count = bytes.getInt(base);
            if (index == count) {
                return bytes.capacity();
            }
            assert (0 <= index && index < count);
            return base + bytes.getInt(base + 4 + 4 + index * 8);
        }

        static boolean addBaseName(ByteBuffer bytes, int index, String folder, String suffix, StringBuilder sb, Set<String> names) {
            byte b2;
            int offset = DatPackageReader.getNameOffset(bytes, index);
            offset += "icudt62b".length() + 1;
            if (folder.length() != 0) {
                int i2 = 0;
                while (i2 < folder.length()) {
                    if (bytes.get(offset) != folder.charAt(i2)) {
                        return false;
                    }
                    ++i2;
                    ++offset;
                }
                if (bytes.get(offset++) != 47) {
                    return false;
                }
            }
            sb.setLength(0);
            while ((b2 = bytes.get(offset++)) != 0) {
                char c2 = (char)b2;
                if (c2 == '/') {
                    return true;
                }
                sb.append(c2);
            }
            int nameLimit = sb.length() - suffix.length();
            if (sb.lastIndexOf(suffix, nameLimit) >= 0) {
                names.add(sb.substring(0, nameLimit));
            }
            return true;
        }

        private static final class IsAcceptable
        implements Authenticate {
            private IsAcceptable() {
            }

            @Override
            public boolean isDataVersionAcceptable(byte[] version) {
                return version[0] == 1;
            }
        }
    }
}

