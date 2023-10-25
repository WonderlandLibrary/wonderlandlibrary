package org.apache.commons.compress.archivers.ar;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

public class ArArchiveInputStream extends ArchiveInputStream {
  private final InputStream input;
  
  private long offset = 0L;
  
  private boolean closed;
  
  private ArArchiveEntry currentEntry = null;
  
  private byte[] namebuffer = null;
  
  private long entryOffset = -1L;
  
  private final byte[] NAME_BUF = new byte[16];
  
  private final byte[] LAST_MODIFIED_BUF = new byte[12];
  
  private final byte[] ID_BUF = new byte[6];
  
  private final byte[] FILE_MODE_BUF = new byte[8];
  
  private final byte[] LENGTH_BUF = new byte[10];
  
  static final String BSD_LONGNAME_PREFIX = "#1/";
  
  public ArArchiveInputStream(InputStream pInput) {
    this.input = pInput;
    this.closed = false;
  }
  
  public ArArchiveEntry getNextArEntry() throws IOException {
    if (this.currentEntry != null) {
      long entryEnd = this.entryOffset + this.currentEntry.getLength();
      IOUtils.skip((InputStream)this, entryEnd - this.offset);
      this.currentEntry = null;
    } 
    if (this.offset == 0L) {
      byte[] arrayOfByte1 = ArchiveUtils.toAsciiBytes("!<arch>\n");
      byte[] arrayOfByte2 = new byte[arrayOfByte1.length];
      int j = IOUtils.readFully((InputStream)this, arrayOfByte2);
      if (j != arrayOfByte1.length)
        throw new IOException("failed to read header. Occured at byte: " + getBytesRead()); 
      for (int k = 0; k < arrayOfByte1.length; k++) {
        if (arrayOfByte1[k] != arrayOfByte2[k])
          throw new IOException("invalid header " + ArchiveUtils.toAsciiString(arrayOfByte2)); 
      } 
    } 
    if (this.offset % 2L != 0L && read() < 0)
      return null; 
    if (this.input.available() == 0)
      return null; 
    IOUtils.readFully((InputStream)this, this.NAME_BUF);
    IOUtils.readFully((InputStream)this, this.LAST_MODIFIED_BUF);
    IOUtils.readFully((InputStream)this, this.ID_BUF);
    int userId = asInt(this.ID_BUF, true);
    IOUtils.readFully((InputStream)this, this.ID_BUF);
    IOUtils.readFully((InputStream)this, this.FILE_MODE_BUF);
    IOUtils.readFully((InputStream)this, this.LENGTH_BUF);
    byte[] expected = ArchiveUtils.toAsciiBytes("`\n");
    byte[] realized = new byte[expected.length];
    int read = IOUtils.readFully((InputStream)this, realized);
    if (read != expected.length)
      throw new IOException("failed to read entry trailer. Occured at byte: " + getBytesRead()); 
    for (int i = 0; i < expected.length; i++) {
      if (expected[i] != realized[i])
        throw new IOException("invalid entry trailer. not read the content? Occured at byte: " + getBytesRead()); 
    } 
    this.entryOffset = this.offset;
    String temp = ArchiveUtils.toAsciiString(this.NAME_BUF).trim();
    if (isGNUStringTable(temp)) {
      this.currentEntry = readGNUStringTable(this.LENGTH_BUF);
      return getNextArEntry();
    } 
    long len = asLong(this.LENGTH_BUF);
    if (temp.endsWith("/")) {
      temp = temp.substring(0, temp.length() - 1);
    } else if (isGNULongName(temp)) {
      int off = Integer.parseInt(temp.substring(1));
      temp = getExtendedName(off);
    } else if (isBSDLongName(temp)) {
      temp = getBSDLongName(temp);
      int nameLen = temp.length();
      len -= nameLen;
      this.entryOffset += nameLen;
    } 
    this.currentEntry = new ArArchiveEntry(temp, len, userId, asInt(this.ID_BUF, true), asInt(this.FILE_MODE_BUF, 8), asLong(this.LAST_MODIFIED_BUF));
    return this.currentEntry;
  }
  
  private String getExtendedName(int offset) throws IOException {
    if (this.namebuffer == null)
      throw new IOException("Cannot process GNU long filename as no // record was found"); 
    for (int i = offset; i < this.namebuffer.length; i++) {
      if (this.namebuffer[i] == 10) {
        if (this.namebuffer[i - 1] == 47)
          i--; 
        return ArchiveUtils.toAsciiString(this.namebuffer, offset, i - offset);
      } 
    } 
    throw new IOException("Failed to read entry: " + offset);
  }
  
  private long asLong(byte[] input) {
    return Long.parseLong(ArchiveUtils.toAsciiString(input).trim());
  }
  
  private int asInt(byte[] input) {
    return asInt(input, 10, false);
  }
  
  private int asInt(byte[] input, boolean treatBlankAsZero) {
    return asInt(input, 10, treatBlankAsZero);
  }
  
  private int asInt(byte[] input, int base) {
    return asInt(input, base, false);
  }
  
  private int asInt(byte[] input, int base, boolean treatBlankAsZero) {
    String string = ArchiveUtils.toAsciiString(input).trim();
    if (string.length() == 0 && treatBlankAsZero)
      return 0; 
    return Integer.parseInt(string, base);
  }
  
  public ArchiveEntry getNextEntry() throws IOException {
    return getNextArEntry();
  }
  
  public void close() throws IOException {
    if (!this.closed) {
      this.closed = true;
      this.input.close();
    } 
    this.currentEntry = null;
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    int toRead = len;
    if (this.currentEntry != null) {
      long entryEnd = this.entryOffset + this.currentEntry.getLength();
      if (len > 0 && entryEnd > this.offset) {
        toRead = (int)Math.min(len, entryEnd - this.offset);
      } else {
        return -1;
      } 
    } 
    int ret = this.input.read(b, off, toRead);
    count(ret);
    this.offset += (ret > 0) ? ret : 0L;
    return ret;
  }
  
  public static boolean matches(byte[] signature, int length) {
    if (length < 8)
      return false; 
    if (signature[0] != 33)
      return false; 
    if (signature[1] != 60)
      return false; 
    if (signature[2] != 97)
      return false; 
    if (signature[3] != 114)
      return false; 
    if (signature[4] != 99)
      return false; 
    if (signature[5] != 104)
      return false; 
    if (signature[6] != 62)
      return false; 
    if (signature[7] != 10)
      return false; 
    return true;
  }
  
  private static final int BSD_LONGNAME_PREFIX_LEN = "#1/".length();
  
  private static final String BSD_LONGNAME_PATTERN = "^#1/\\d+";
  
  private static final String GNU_STRING_TABLE_NAME = "//";
  
  private static final String GNU_LONGNAME_PATTERN = "^/\\d+";
  
  private static boolean isBSDLongName(String name) {
    return (name != null && name.matches("^#1/\\d+"));
  }
  
  private String getBSDLongName(String bsdLongName) throws IOException {
    int nameLen = Integer.parseInt(bsdLongName.substring(BSD_LONGNAME_PREFIX_LEN));
    byte[] name = new byte[nameLen];
    int read = IOUtils.readFully(this.input, name);
    count(read);
    if (read != nameLen)
      throw new EOFException(); 
    return ArchiveUtils.toAsciiString(name);
  }
  
  private static boolean isGNUStringTable(String name) {
    return "//".equals(name);
  }
  
  private ArArchiveEntry readGNUStringTable(byte[] length) throws IOException {
    int bufflen = asInt(length);
    this.namebuffer = new byte[bufflen];
    int read = IOUtils.readFully((InputStream)this, this.namebuffer, 0, bufflen);
    if (read != bufflen)
      throw new IOException("Failed to read complete // record: expected=" + bufflen + " read=" + read); 
    return new ArArchiveEntry("//", bufflen);
  }
  
  private boolean isGNULongName(String name) {
    return (name != null && name.matches("^/\\d+"));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\ar\ArArchiveInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */