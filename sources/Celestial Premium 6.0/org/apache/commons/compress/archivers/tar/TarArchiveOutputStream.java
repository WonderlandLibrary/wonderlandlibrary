/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.tar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.CountingOutputStream;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TarArchiveOutputStream
extends ArchiveOutputStream {
    public static final int LONGFILE_ERROR = 0;
    public static final int LONGFILE_TRUNCATE = 1;
    public static final int LONGFILE_GNU = 2;
    public static final int LONGFILE_POSIX = 3;
    public static final int BIGNUMBER_ERROR = 0;
    public static final int BIGNUMBER_STAR = 1;
    public static final int BIGNUMBER_POSIX = 2;
    private long currSize;
    private String currName;
    private long currBytes;
    private final byte[] recordBuf;
    private int assemLen;
    private final byte[] assemBuf;
    private int longFileMode = 0;
    private int bigNumberMode = 0;
    private int recordsWritten;
    private final int recordsPerBlock;
    private final int recordSize;
    private boolean closed = false;
    private boolean haveUnclosedEntry = false;
    private boolean finished = false;
    private final OutputStream out;
    private final ZipEncoding encoding;
    private boolean addPaxHeadersForNonAsciiNames = false;
    private static final ZipEncoding ASCII = ZipEncodingHelper.getZipEncoding("ASCII");

    public TarArchiveOutputStream(OutputStream os) {
        this(os, 10240, 512);
    }

    public TarArchiveOutputStream(OutputStream os, String encoding) {
        this(os, 10240, 512, encoding);
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize) {
        this(os, blockSize, 512);
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize, String encoding) {
        this(os, blockSize, 512, encoding);
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize) {
        this(os, blockSize, recordSize, null);
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize, String encoding) {
        this.out = new CountingOutputStream(os);
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.assemLen = 0;
        this.assemBuf = new byte[recordSize];
        this.recordBuf = new byte[recordSize];
        this.recordSize = recordSize;
        this.recordsPerBlock = blockSize / recordSize;
    }

    public void setLongFileMode(int longFileMode) {
        this.longFileMode = longFileMode;
    }

    public void setBigNumberMode(int bigNumberMode) {
        this.bigNumberMode = bigNumberMode;
    }

    public void setAddPaxHeadersForNonAsciiNames(boolean b) {
        this.addPaxHeadersForNonAsciiNames = b;
    }

    @Override
    @Deprecated
    public int getCount() {
        return (int)this.getBytesWritten();
    }

    @Override
    public long getBytesWritten() {
        return ((CountingOutputStream)this.out).getBytesWritten();
    }

    @Override
    public void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.haveUnclosedEntry) {
            throw new IOException("This archives contains unclosed entries.");
        }
        this.writeEOFRecord();
        this.writeEOFRecord();
        this.padAsNeeded();
        this.out.flush();
        this.finished = true;
    }

    @Override
    public void close() throws IOException {
        if (!this.finished) {
            this.finish();
        }
        if (!this.closed) {
            this.out.close();
            this.closed = true;
        }
    }

    public int getRecordSize() {
        return this.recordSize;
    }

    @Override
    public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
        boolean paxHeaderContainsLinkPath;
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        TarArchiveEntry entry = (TarArchiveEntry)archiveEntry;
        HashMap<String, String> paxHeaders = new HashMap<String, String>();
        String entryName = entry.getName();
        boolean paxHeaderContainsPath = this.handleLongName(entryName, paxHeaders, "path", (byte)76, "file name");
        String linkName = entry.getLinkName();
        boolean bl = paxHeaderContainsLinkPath = linkName != null && linkName.length() > 0 && this.handleLongName(linkName, paxHeaders, "linkpath", (byte)75, "link name");
        if (this.bigNumberMode == 2) {
            this.addPaxHeadersForBigNumbers(paxHeaders, entry);
        } else if (this.bigNumberMode != 1) {
            this.failForBigNumbers(entry);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath && !ASCII.canEncode(entryName)) {
            paxHeaders.put("path", entryName);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsLinkPath && (entry.isLink() || entry.isSymbolicLink()) && !ASCII.canEncode(linkName)) {
            paxHeaders.put("linkpath", linkName);
        }
        if (paxHeaders.size() > 0) {
            this.writePaxHeaders(entryName, paxHeaders);
        }
        entry.writeEntryHeader(this.recordBuf, this.encoding, this.bigNumberMode == 1);
        this.writeRecord(this.recordBuf);
        this.currBytes = 0L;
        this.currSize = entry.isDirectory() ? 0L : entry.getSize();
        this.currName = entryName;
        this.haveUnclosedEntry = true;
    }

    @Override
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (!this.haveUnclosedEntry) {
            throw new IOException("No current entry to close");
        }
        if (this.assemLen > 0) {
            for (int i = this.assemLen; i < this.assemBuf.length; ++i) {
                this.assemBuf[i] = 0;
            }
            this.writeRecord(this.assemBuf);
            this.currBytes += (long)this.assemLen;
            this.assemLen = 0;
        }
        if (this.currBytes < this.currSize) {
            throw new IOException("entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
        }
        this.haveUnclosedEntry = false;
    }

    @Override
    public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
        if (!this.haveUnclosedEntry) {
            throw new IllegalStateException("No current tar entry");
        }
        if (this.currBytes + (long)numToWrite > this.currSize) {
            throw new IOException("request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
        }
        if (this.assemLen > 0) {
            if (this.assemLen + numToWrite >= this.recordBuf.length) {
                int aLen = this.recordBuf.length - this.assemLen;
                System.arraycopy(this.assemBuf, 0, this.recordBuf, 0, this.assemLen);
                System.arraycopy(wBuf, wOffset, this.recordBuf, this.assemLen, aLen);
                this.writeRecord(this.recordBuf);
                this.currBytes += (long)this.recordBuf.length;
                wOffset += aLen;
                numToWrite -= aLen;
                this.assemLen = 0;
            } else {
                System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
                wOffset += numToWrite;
                this.assemLen += numToWrite;
                numToWrite = 0;
            }
        }
        while (numToWrite > 0) {
            if (numToWrite < this.recordBuf.length) {
                System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
                this.assemLen += numToWrite;
                break;
            }
            this.writeRecord(wBuf, wOffset);
            int num = this.recordBuf.length;
            this.currBytes += (long)num;
            numToWrite -= num;
            wOffset += num;
        }
    }

    void writePaxHeaders(String entryName, Map<String, String> headers) throws IOException {
        String name = "./PaxHeaders.X/" + this.stripTo7Bits(entryName);
        if (name.length() >= 100) {
            name = name.substring(0, 99);
        }
        TarArchiveEntry pex = new TarArchiveEntry(name, 120);
        StringWriter w = new StringWriter();
        for (Map.Entry<String, String> h : headers.entrySet()) {
            String key = h.getKey();
            String value = h.getValue();
            int len = key.length() + value.length() + 3 + 2;
            String line = len + " " + key + "=" + value + "\n";
            int actualLength = line.getBytes("UTF-8").length;
            while (len != actualLength) {
                len = actualLength;
                line = len + " " + key + "=" + value + "\n";
                actualLength = line.getBytes("UTF-8").length;
            }
            w.write(line);
        }
        byte[] data = w.toString().getBytes("UTF-8");
        pex.setSize(data.length);
        this.putArchiveEntry(pex);
        this.write(data);
        this.closeArchiveEntry();
    }

    private String stripTo7Bits(String name) {
        int length = name.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            char stripped = (char)(name.charAt(i) & 0x7F);
            if (this.shouldBeReplaced(stripped)) {
                result.append("_");
                continue;
            }
            result.append(stripped);
        }
        return result.toString();
    }

    private boolean shouldBeReplaced(char c) {
        return c == '\u0000' || c == '/' || c == '\\';
    }

    private void writeEOFRecord() throws IOException {
        Arrays.fill(this.recordBuf, (byte)0);
        this.writeRecord(this.recordBuf);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new TarArchiveEntry(inputFile, entryName);
    }

    private void writeRecord(byte[] record) throws IOException {
        if (record.length != this.recordSize) {
            throw new IOException("record to write has length '" + record.length + "' which is not the record size of '" + this.recordSize + "'");
        }
        this.out.write(record);
        ++this.recordsWritten;
    }

    private void writeRecord(byte[] buf, int offset) throws IOException {
        if (offset + this.recordSize > buf.length) {
            throw new IOException("record has length '" + buf.length + "' with offset '" + offset + "' which is less than the record size of '" + this.recordSize + "'");
        }
        this.out.write(buf, offset, this.recordSize);
        ++this.recordsWritten;
    }

    private void padAsNeeded() throws IOException {
        int start = this.recordsWritten % this.recordsPerBlock;
        if (start != 0) {
            for (int i = start; i < this.recordsPerBlock; ++i) {
                this.writeEOFRecord();
            }
        }
    }

    private void addPaxHeadersForBigNumbers(Map<String, String> paxHeaders, TarArchiveEntry entry) {
        this.addPaxHeaderForBigNumber(paxHeaders, "size", entry.getSize(), 0x1FFFFFFFFL);
        this.addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getGroupId(), 0x1FFFFFL);
        this.addPaxHeaderForBigNumber(paxHeaders, "mtime", entry.getModTime().getTime() / 1000L, 0x1FFFFFFFFL);
        this.addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getUserId(), 0x1FFFFFL);
        this.addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor", entry.getDevMajor(), 0x1FFFFFL);
        this.addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor", entry.getDevMinor(), 0x1FFFFFL);
        this.failForBigNumber("mode", entry.getMode(), 0x1FFFFFL);
    }

    private void addPaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, long value, long maxValue) {
        if (value < 0L || value > maxValue) {
            paxHeaders.put(header, String.valueOf(value));
        }
    }

    private void failForBigNumbers(TarArchiveEntry entry) {
        this.failForBigNumber("entry size", entry.getSize(), 0x1FFFFFFFFL);
        this.failForBigNumber("group id", entry.getGroupId(), 0x1FFFFFL);
        this.failForBigNumber("last modification time", entry.getModTime().getTime() / 1000L, 0x1FFFFFFFFL);
        this.failForBigNumber("user id", entry.getUserId(), 0x1FFFFFL);
        this.failForBigNumber("mode", entry.getMode(), 0x1FFFFFL);
        this.failForBigNumber("major device number", entry.getDevMajor(), 0x1FFFFFL);
        this.failForBigNumber("minor device number", entry.getDevMinor(), 0x1FFFFFL);
    }

    private void failForBigNumber(String field, long value, long maxValue) {
        if (value < 0L || value > maxValue) {
            throw new RuntimeException(field + " '" + value + "' is too big ( > " + maxValue + " )");
        }
    }

    private boolean handleLongName(String name, Map<String, String> paxHeaders, String paxHeaderName, byte linkType, String fieldName) throws IOException {
        ByteBuffer encodedName = this.encoding.encode(name);
        int len = encodedName.limit() - encodedName.position();
        if (len >= 100) {
            if (this.longFileMode == 3) {
                paxHeaders.put(paxHeaderName, name);
                return true;
            }
            if (this.longFileMode == 2) {
                TarArchiveEntry longLinkEntry = new TarArchiveEntry("././@LongLink", linkType);
                longLinkEntry.setSize(len + 1);
                this.putArchiveEntry(longLinkEntry);
                this.write(encodedName.array(), encodedName.arrayOffset(), len);
                this.write(0);
                this.closeArchiveEntry();
            } else if (this.longFileMode != 1) {
                throw new RuntimeException(fieldName + " '" + name + "' is too long ( > " + 100 + " bytes)");
            }
        }
        return false;
    }
}

