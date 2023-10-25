package org.apache.commons.compress.archivers.cpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

public class CpioArchiveInputStream extends ArchiveInputStream implements CpioConstants {
  private boolean closed = false;
  
  private CpioArchiveEntry entry;
  
  private long entryBytesRead = 0L;
  
  private boolean entryEOF = false;
  
  private final byte[] tmpbuf = new byte[4096];
  
  private long crc = 0L;
  
  private final InputStream in;
  
  private final byte[] TWO_BYTES_BUF = new byte[2];
  
  private final byte[] FOUR_BYTES_BUF = new byte[4];
  
  private final byte[] SIX_BYTES_BUF = new byte[6];
  
  private final int blockSize;
  
  private final ZipEncoding encoding;
  
  public CpioArchiveInputStream(InputStream in) {
    this(in, 512, "US-ASCII");
  }
  
  public CpioArchiveInputStream(InputStream in, String encoding) {
    this(in, 512, encoding);
  }
  
  public CpioArchiveInputStream(InputStream in, int blockSize) {
    this(in, blockSize, "US-ASCII");
  }
  
  public CpioArchiveInputStream(InputStream in, int blockSize, String encoding) {
    this.in = in;
    this.blockSize = blockSize;
    this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
  }
  
  public int available() throws IOException {
    ensureOpen();
    if (this.entryEOF)
      return 0; 
    return 1;
  }
  
  public void close() throws IOException {
    if (!this.closed) {
      this.in.close();
      this.closed = true;
    } 
  }
  
  private void closeEntry() throws IOException {
    while (skip(2147483647L) == 2147483647L);
  }
  
  private void ensureOpen() throws IOException {
    if (this.closed)
      throw new IOException("Stream closed"); 
  }
  
  public CpioArchiveEntry getNextCPIOEntry() throws IOException {
    ensureOpen();
    if (this.entry != null)
      closeEntry(); 
    readFully(this.TWO_BYTES_BUF, 0, this.TWO_BYTES_BUF.length);
    if (CpioUtil.byteArray2long(this.TWO_BYTES_BUF, false) == 29127L) {
      this.entry = readOldBinaryEntry(false);
    } else if (CpioUtil.byteArray2long(this.TWO_BYTES_BUF, true) == 29127L) {
      this.entry = readOldBinaryEntry(true);
    } else {
      System.arraycopy(this.TWO_BYTES_BUF, 0, this.SIX_BYTES_BUF, 0, this.TWO_BYTES_BUF.length);
      readFully(this.SIX_BYTES_BUF, this.TWO_BYTES_BUF.length, this.FOUR_BYTES_BUF.length);
      String magicString = ArchiveUtils.toAsciiString(this.SIX_BYTES_BUF);
      if (magicString.equals("070701")) {
        this.entry = readNewEntry(false);
      } else if (magicString.equals("070702")) {
        this.entry = readNewEntry(true);
      } else if (magicString.equals("070707")) {
        this.entry = readOldAsciiEntry();
      } else {
        throw new IOException("Unknown magic [" + magicString + "]. Occured at byte: " + getBytesRead());
      } 
    } 
    this.entryBytesRead = 0L;
    this.entryEOF = false;
    this.crc = 0L;
    if (this.entry.getName().equals("TRAILER!!!")) {
      this.entryEOF = true;
      skipRemainderOfLastBlock();
      return null;
    } 
    return this.entry;
  }
  
  private void skip(int bytes) throws IOException {
    if (bytes > 0)
      readFully(this.FOUR_BYTES_BUF, 0, bytes); 
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    ensureOpen();
    if (off < 0 || len < 0 || off > b.length - len)
      throw new IndexOutOfBoundsException(); 
    if (len == 0)
      return 0; 
    if (this.entry == null || this.entryEOF)
      return -1; 
    if (this.entryBytesRead == this.entry.getSize()) {
      skip(this.entry.getDataPadCount());
      this.entryEOF = true;
      if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum())
        throw new IOException("CRC Error. Occured at byte: " + getBytesRead()); 
      return -1;
    } 
    int tmplength = (int)Math.min(len, this.entry.getSize() - this.entryBytesRead);
    if (tmplength < 0)
      return -1; 
    int tmpread = readFully(b, off, tmplength);
    if (this.entry.getFormat() == 2)
      for (int pos = 0; pos < tmpread; pos++)
        this.crc += (b[pos] & 0xFF);  
    this.entryBytesRead += tmpread;
    return tmpread;
  }
  
  private final int readFully(byte[] b, int off, int len) throws IOException {
    int count = IOUtils.readFully(this.in, b, off, len);
    count(count);
    if (count < len)
      throw new EOFException(); 
    return count;
  }
  
  private long readBinaryLong(int length, boolean swapHalfWord) throws IOException {
    byte[] tmp = new byte[length];
    readFully(tmp, 0, tmp.length);
    return CpioUtil.byteArray2long(tmp, swapHalfWord);
  }
  
  private long readAsciiLong(int length, int radix) throws IOException {
    byte[] tmpBuffer = new byte[length];
    readFully(tmpBuffer, 0, tmpBuffer.length);
    return Long.parseLong(ArchiveUtils.toAsciiString(tmpBuffer), radix);
  }
  
  private CpioArchiveEntry readNewEntry(boolean hasCrc) throws IOException {
    CpioArchiveEntry ret;
    if (hasCrc) {
      ret = new CpioArchiveEntry((short)2);
    } else {
      ret = new CpioArchiveEntry((short)1);
    } 
    ret.setInode(readAsciiLong(8, 16));
    long mode = readAsciiLong(8, 16);
    if (CpioUtil.fileType(mode) != 0L)
      ret.setMode(mode); 
    ret.setUID(readAsciiLong(8, 16));
    ret.setGID(readAsciiLong(8, 16));
    ret.setNumberOfLinks(readAsciiLong(8, 16));
    ret.setTime(readAsciiLong(8, 16));
    ret.setSize(readAsciiLong(8, 16));
    ret.setDeviceMaj(readAsciiLong(8, 16));
    ret.setDeviceMin(readAsciiLong(8, 16));
    ret.setRemoteDeviceMaj(readAsciiLong(8, 16));
    ret.setRemoteDeviceMin(readAsciiLong(8, 16));
    long namesize = readAsciiLong(8, 16);
    ret.setChksum(readAsciiLong(8, 16));
    String name = readCString((int)namesize);
    ret.setName(name);
    if (CpioUtil.fileType(mode) == 0L && !name.equals("TRAILER!!!"))
      throw new IOException("Mode 0 only allowed in the trailer. Found entry name: " + name + " Occured at byte: " + getBytesRead()); 
    skip(ret.getHeaderPadCount());
    return ret;
  }
  
  private CpioArchiveEntry readOldAsciiEntry() throws IOException {
    CpioArchiveEntry ret = new CpioArchiveEntry((short)4);
    ret.setDevice(readAsciiLong(6, 8));
    ret.setInode(readAsciiLong(6, 8));
    long mode = readAsciiLong(6, 8);
    if (CpioUtil.fileType(mode) != 0L)
      ret.setMode(mode); 
    ret.setUID(readAsciiLong(6, 8));
    ret.setGID(readAsciiLong(6, 8));
    ret.setNumberOfLinks(readAsciiLong(6, 8));
    ret.setRemoteDevice(readAsciiLong(6, 8));
    ret.setTime(readAsciiLong(11, 8));
    long namesize = readAsciiLong(6, 8);
    ret.setSize(readAsciiLong(11, 8));
    String name = readCString((int)namesize);
    ret.setName(name);
    if (CpioUtil.fileType(mode) == 0L && !name.equals("TRAILER!!!"))
      throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + name + " Occured at byte: " + getBytesRead()); 
    return ret;
  }
  
  private CpioArchiveEntry readOldBinaryEntry(boolean swapHalfWord) throws IOException {
    CpioArchiveEntry ret = new CpioArchiveEntry((short)8);
    ret.setDevice(readBinaryLong(2, swapHalfWord));
    ret.setInode(readBinaryLong(2, swapHalfWord));
    long mode = readBinaryLong(2, swapHalfWord);
    if (CpioUtil.fileType(mode) != 0L)
      ret.setMode(mode); 
    ret.setUID(readBinaryLong(2, swapHalfWord));
    ret.setGID(readBinaryLong(2, swapHalfWord));
    ret.setNumberOfLinks(readBinaryLong(2, swapHalfWord));
    ret.setRemoteDevice(readBinaryLong(2, swapHalfWord));
    ret.setTime(readBinaryLong(4, swapHalfWord));
    long namesize = readBinaryLong(2, swapHalfWord);
    ret.setSize(readBinaryLong(4, swapHalfWord));
    String name = readCString((int)namesize);
    ret.setName(name);
    if (CpioUtil.fileType(mode) == 0L && !name.equals("TRAILER!!!"))
      throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + name + "Occured at byte: " + getBytesRead()); 
    skip(ret.getHeaderPadCount());
    return ret;
  }
  
  private String readCString(int length) throws IOException {
    byte[] tmpBuffer = new byte[length - 1];
    readFully(tmpBuffer, 0, tmpBuffer.length);
    this.in.read();
    return this.encoding.decode(tmpBuffer);
  }
  
  public long skip(long n) throws IOException {
    if (n < 0L)
      throw new IllegalArgumentException("negative skip length"); 
    ensureOpen();
    int max = (int)Math.min(n, 2147483647L);
    int total = 0;
    while (total < max) {
      int len = max - total;
      if (len > this.tmpbuf.length)
        len = this.tmpbuf.length; 
      len = read(this.tmpbuf, 0, len);
      if (len == -1) {
        this.entryEOF = true;
        break;
      } 
      total += len;
    } 
    return total;
  }
  
  public ArchiveEntry getNextEntry() throws IOException {
    return getNextCPIOEntry();
  }
  
  private void skipRemainderOfLastBlock() throws IOException {
    long readFromLastBlock = getBytesRead() % this.blockSize;
    long remainingBytes = (readFromLastBlock == 0L) ? 0L : (this.blockSize - readFromLastBlock);
    while (remainingBytes > 0L) {
      long skipped = skip(this.blockSize - readFromLastBlock);
      if (skipped <= 0L)
        break; 
      remainingBytes -= skipped;
    } 
  }
  
  public static boolean matches(byte[] signature, int length) {
    if (length < 6)
      return false; 
    if (signature[0] == 113 && (signature[1] & 0xFF) == 199)
      return true; 
    if (signature[1] == 113 && (signature[0] & 0xFF) == 199)
      return true; 
    if (signature[0] != 48)
      return false; 
    if (signature[1] != 55)
      return false; 
    if (signature[2] != 48)
      return false; 
    if (signature[3] != 55)
      return false; 
    if (signature[4] != 48)
      return false; 
    if (signature[5] == 49)
      return true; 
    if (signature[5] == 50)
      return true; 
    if (signature[5] == 55)
      return true; 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\cpio\CpioArchiveInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */