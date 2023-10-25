package org.apache.commons.compress.archivers.cpio;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;

public class CpioArchiveOutputStream extends ArchiveOutputStream implements CpioConstants {
  private CpioArchiveEntry entry;
  
  private boolean closed = false;
  
  private boolean finished;
  
  private final short entryFormat;
  
  private final HashMap<String, CpioArchiveEntry> names = new HashMap<String, CpioArchiveEntry>();
  
  private long crc = 0L;
  
  private long written;
  
  private final OutputStream out;
  
  private final int blockSize;
  
  private long nextArtificalDeviceAndInode = 1L;
  
  private final ZipEncoding encoding;
  
  public CpioArchiveOutputStream(OutputStream out, short format) {
    this(out, format, 512, "US-ASCII");
  }
  
  public CpioArchiveOutputStream(OutputStream out, short format, int blockSize) {
    this(out, format, blockSize, "US-ASCII");
  }
  
  public CpioArchiveOutputStream(OutputStream out, short format, int blockSize, String encoding) {
    this.out = out;
    switch (format) {
      case 1:
      case 2:
      case 4:
      case 8:
        break;
      default:
        throw new IllegalArgumentException("Unknown format: " + format);
    } 
    this.entryFormat = format;
    this.blockSize = blockSize;
    this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
  }
  
  public CpioArchiveOutputStream(OutputStream out) {
    this(out, (short)1);
  }
  
  public CpioArchiveOutputStream(OutputStream out, String encoding) {
    this(out, (short)1, 512, encoding);
  }
  
  private void ensureOpen() throws IOException {
    if (this.closed)
      throw new IOException("Stream closed"); 
  }
  
  public void putArchiveEntry(ArchiveEntry entry) throws IOException {
    if (this.finished)
      throw new IOException("Stream has already been finished"); 
    CpioArchiveEntry e = (CpioArchiveEntry)entry;
    ensureOpen();
    if (this.entry != null)
      closeArchiveEntry(); 
    if (e.getTime() == -1L)
      e.setTime(System.currentTimeMillis() / 1000L); 
    short format = e.getFormat();
    if (format != this.entryFormat)
      throw new IOException("Header format: " + format + " does not match existing format: " + this.entryFormat); 
    if (this.names.put(e.getName(), e) != null)
      throw new IOException("duplicate entry: " + e.getName()); 
    writeHeader(e);
    this.entry = e;
    this.written = 0L;
  }
  
  private void writeHeader(CpioArchiveEntry e) throws IOException {
    boolean swapHalfWord;
    switch (e.getFormat()) {
      case 1:
        this.out.write(ArchiveUtils.toAsciiBytes("070701"));
        count(6);
        writeNewEntry(e);
        return;
      case 2:
        this.out.write(ArchiveUtils.toAsciiBytes("070702"));
        count(6);
        writeNewEntry(e);
        return;
      case 4:
        this.out.write(ArchiveUtils.toAsciiBytes("070707"));
        count(6);
        writeOldAsciiEntry(e);
        return;
      case 8:
        swapHalfWord = true;
        writeBinaryLong(29127L, 2, swapHalfWord);
        writeOldBinaryEntry(e, swapHalfWord);
        return;
    } 
    throw new IOException("unknown format " + e.getFormat());
  }
  
  private void writeNewEntry(CpioArchiveEntry entry) throws IOException {
    long inode = entry.getInode();
    long devMin = entry.getDeviceMin();
    inode = devMin = 0L;
    inode = this.nextArtificalDeviceAndInode & 0xFFFFFFFFFFFFFFFFL;
    devMin = this.nextArtificalDeviceAndInode++ >> 32L & 0xFFFFFFFFFFFFFFFFL;
    this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 4294967296L * devMin) + 1L;
    writeAsciiLong(inode, 8, 16);
    writeAsciiLong(entry.getMode(), 8, 16);
    writeAsciiLong(entry.getUID(), 8, 16);
    writeAsciiLong(entry.getGID(), 8, 16);
    writeAsciiLong(entry.getNumberOfLinks(), 8, 16);
    writeAsciiLong(entry.getTime(), 8, 16);
    writeAsciiLong(entry.getSize(), 8, 16);
    writeAsciiLong(entry.getDeviceMaj(), 8, 16);
    writeAsciiLong(devMin, 8, 16);
    writeAsciiLong(entry.getRemoteDeviceMaj(), 8, 16);
    writeAsciiLong(entry.getRemoteDeviceMin(), 8, 16);
    writeAsciiLong((entry.getName().length() + 1), 8, 16);
    writeAsciiLong(entry.getChksum(), 8, 16);
    writeCString(entry.getName());
    pad(entry.getHeaderPadCount());
  }
  
  private void writeOldAsciiEntry(CpioArchiveEntry entry) throws IOException {
    long inode = entry.getInode();
    long device = entry.getDevice();
    inode = device = 0L;
    inode = this.nextArtificalDeviceAndInode & 0x3FFFFL;
    device = this.nextArtificalDeviceAndInode++ >> 18L & 0x3FFFFL;
    this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 262144L * device) + 1L;
    writeAsciiLong(device, 6, 8);
    writeAsciiLong(inode, 6, 8);
    writeAsciiLong(entry.getMode(), 6, 8);
    writeAsciiLong(entry.getUID(), 6, 8);
    writeAsciiLong(entry.getGID(), 6, 8);
    writeAsciiLong(entry.getNumberOfLinks(), 6, 8);
    writeAsciiLong(entry.getRemoteDevice(), 6, 8);
    writeAsciiLong(entry.getTime(), 11, 8);
    writeAsciiLong((entry.getName().length() + 1), 6, 8);
    writeAsciiLong(entry.getSize(), 11, 8);
    writeCString(entry.getName());
  }
  
  private void writeOldBinaryEntry(CpioArchiveEntry entry, boolean swapHalfWord) throws IOException {
    long inode = entry.getInode();
    long device = entry.getDevice();
    inode = device = 0L;
    inode = this.nextArtificalDeviceAndInode & 0xFFFFL;
    device = this.nextArtificalDeviceAndInode++ >> 16L & 0xFFFFL;
    this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 65536L * device) + 1L;
    writeBinaryLong(device, 2, swapHalfWord);
    writeBinaryLong(inode, 2, swapHalfWord);
    writeBinaryLong(entry.getMode(), 2, swapHalfWord);
    writeBinaryLong(entry.getUID(), 2, swapHalfWord);
    writeBinaryLong(entry.getGID(), 2, swapHalfWord);
    writeBinaryLong(entry.getNumberOfLinks(), 2, swapHalfWord);
    writeBinaryLong(entry.getRemoteDevice(), 2, swapHalfWord);
    writeBinaryLong(entry.getTime(), 4, swapHalfWord);
    writeBinaryLong((entry.getName().length() + 1), 2, swapHalfWord);
    writeBinaryLong(entry.getSize(), 4, swapHalfWord);
    writeCString(entry.getName());
    pad(entry.getHeaderPadCount());
  }
  
  public void closeArchiveEntry() throws IOException {
    if (this.finished)
      throw new IOException("Stream has already been finished"); 
    ensureOpen();
    if (this.entry == null)
      throw new IOException("Trying to close non-existent entry"); 
    if (this.entry.getSize() != this.written)
      throw new IOException("invalid entry size (expected " + this.entry.getSize() + " but got " + this.written + " bytes)"); 
    pad(this.entry.getDataPadCount());
    if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum())
      throw new IOException("CRC Error"); 
    this.entry = null;
    this.crc = 0L;
    this.written = 0L;
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    ensureOpen();
    if (off < 0 || len < 0 || off > b.length - len)
      throw new IndexOutOfBoundsException(); 
    if (len == 0)
      return; 
    if (this.entry == null)
      throw new IOException("no current CPIO entry"); 
    if (this.written + len > this.entry.getSize())
      throw new IOException("attempt to write past end of STORED entry"); 
    this.out.write(b, off, len);
    this.written += len;
    if (this.entry.getFormat() == 2)
      for (int pos = 0; pos < len; pos++)
        this.crc += (b[pos] & 0xFF);  
    count(len);
  }
  
  public void finish() throws IOException {
    ensureOpen();
    if (this.finished)
      throw new IOException("This archive has already been finished"); 
    if (this.entry != null)
      throw new IOException("This archive contains unclosed entries."); 
    this.entry = new CpioArchiveEntry(this.entryFormat);
    this.entry.setName("TRAILER!!!");
    this.entry.setNumberOfLinks(1L);
    writeHeader(this.entry);
    closeArchiveEntry();
    int lengthOfLastBlock = (int)(getBytesWritten() % this.blockSize);
    if (lengthOfLastBlock != 0)
      pad(this.blockSize - lengthOfLastBlock); 
    this.finished = true;
  }
  
  public void close() throws IOException {
    if (!this.finished)
      finish(); 
    if (!this.closed) {
      this.out.close();
      this.closed = true;
    } 
  }
  
  private void pad(int count) throws IOException {
    if (count > 0) {
      byte[] buff = new byte[count];
      this.out.write(buff);
      count(count);
    } 
  }
  
  private void writeBinaryLong(long number, int length, boolean swapHalfWord) throws IOException {
    byte[] tmp = CpioUtil.long2byteArray(number, length, swapHalfWord);
    this.out.write(tmp);
    count(tmp.length);
  }
  
  private void writeAsciiLong(long number, int length, int radix) throws IOException {
    String tmpStr;
    StringBuilder tmp = new StringBuilder();
    if (radix == 16) {
      tmp.append(Long.toHexString(number));
    } else if (radix == 8) {
      tmp.append(Long.toOctalString(number));
    } else {
      tmp.append(Long.toString(number));
    } 
    if (tmp.length() <= length) {
      long insertLength = (length - tmp.length());
      for (int pos = 0; pos < insertLength; pos++)
        tmp.insert(0, "0"); 
      tmpStr = tmp.toString();
    } else {
      tmpStr = tmp.substring(tmp.length() - length);
    } 
    byte[] b = ArchiveUtils.toAsciiBytes(tmpStr);
    this.out.write(b);
    count(b.length);
  }
  
  private void writeCString(String str) throws IOException {
    ByteBuffer buf = this.encoding.encode(str);
    int len = buf.limit() - buf.position();
    this.out.write(buf.array(), buf.arrayOffset(), len);
    this.out.write(0);
    count(len + 1);
  }
  
  public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
    if (this.finished)
      throw new IOException("Stream has already been finished"); 
    return new CpioArchiveEntry(inputFile, entryName);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\cpio\CpioArchiveOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */