package org.apache.commons.compress.archivers.arj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ArjArchiveInputStream extends ArchiveInputStream {
  private static final int ARJ_MAGIC_1 = 96;
  
  private static final int ARJ_MAGIC_2 = 234;
  
  private final DataInputStream in;
  
  private final String charsetName;
  
  private final MainHeader mainHeader;
  
  private LocalFileHeader currentLocalFileHeader = null;
  
  private InputStream currentInputStream = null;
  
  public ArjArchiveInputStream(InputStream inputStream, String charsetName) throws ArchiveException {
    this.in = new DataInputStream(inputStream);
    this.charsetName = charsetName;
    try {
      this.mainHeader = readMainHeader();
      if ((this.mainHeader.arjFlags & 0x1) != 0)
        throw new ArchiveException("Encrypted ARJ files are unsupported"); 
      if ((this.mainHeader.arjFlags & 0x4) != 0)
        throw new ArchiveException("Multi-volume ARJ files are unsupported"); 
    } catch (IOException ioException) {
      throw new ArchiveException(ioException.getMessage(), ioException);
    } 
  }
  
  public ArjArchiveInputStream(InputStream inputStream) throws ArchiveException {
    this(inputStream, "CP437");
  }
  
  public void close() throws IOException {
    this.in.close();
  }
  
  private int read8(DataInputStream dataIn) throws IOException {
    int value = dataIn.readUnsignedByte();
    count(1);
    return value;
  }
  
  private int read16(DataInputStream dataIn) throws IOException {
    int value = dataIn.readUnsignedShort();
    count(2);
    return Integer.reverseBytes(value) >>> 16;
  }
  
  private int read32(DataInputStream dataIn) throws IOException {
    int value = dataIn.readInt();
    count(4);
    return Integer.reverseBytes(value);
  }
  
  private String readString(DataInputStream dataIn) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nextByte;
    while ((nextByte = dataIn.readUnsignedByte()) != 0)
      buffer.write(nextByte); 
    if (this.charsetName != null)
      return new String(buffer.toByteArray(), this.charsetName); 
    return new String(buffer.toByteArray());
  }
  
  private void readFully(DataInputStream dataIn, byte[] b) throws IOException {
    dataIn.readFully(b);
    count(b.length);
  }
  
  private byte[] readHeader() throws IOException {
    boolean found = false;
    byte[] basicHeaderBytes = null;
    while (true) {
      int first = 0;
      int second = read8(this.in);
      do {
        first = second;
        second = read8(this.in);
      } while (first != 96 && second != 234);
      int basicHeaderSize = read16(this.in);
      if (basicHeaderSize == 0)
        return null; 
      if (basicHeaderSize <= 2600) {
        basicHeaderBytes = new byte[basicHeaderSize];
        readFully(this.in, basicHeaderBytes);
        long basicHeaderCrc32 = read32(this.in) & 0xFFFFFFFFL;
        CRC32 crc32 = new CRC32();
        crc32.update(basicHeaderBytes);
        if (basicHeaderCrc32 == crc32.getValue())
          found = true; 
      } 
      if (found)
        return basicHeaderBytes; 
    } 
  }
  
  private MainHeader readMainHeader() throws IOException {
    byte[] basicHeaderBytes = readHeader();
    if (basicHeaderBytes == null)
      throw new IOException("Archive ends without any headers"); 
    DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
    int firstHeaderSize = basicHeader.readUnsignedByte();
    byte[] firstHeaderBytes = new byte[firstHeaderSize - 1];
    basicHeader.readFully(firstHeaderBytes);
    DataInputStream firstHeader = new DataInputStream(new ByteArrayInputStream(firstHeaderBytes));
    MainHeader hdr = new MainHeader();
    hdr.archiverVersionNumber = firstHeader.readUnsignedByte();
    hdr.minVersionToExtract = firstHeader.readUnsignedByte();
    hdr.hostOS = firstHeader.readUnsignedByte();
    hdr.arjFlags = firstHeader.readUnsignedByte();
    hdr.securityVersion = firstHeader.readUnsignedByte();
    hdr.fileType = firstHeader.readUnsignedByte();
    hdr.reserved = firstHeader.readUnsignedByte();
    hdr.dateTimeCreated = read32(firstHeader);
    hdr.dateTimeModified = read32(firstHeader);
    hdr.archiveSize = 0xFFFFFFFFL & read32(firstHeader);
    hdr.securityEnvelopeFilePosition = read32(firstHeader);
    hdr.fileSpecPosition = read16(firstHeader);
    hdr.securityEnvelopeLength = read16(firstHeader);
    pushedBackBytes(20L);
    hdr.encryptionVersion = firstHeader.readUnsignedByte();
    hdr.lastChapter = firstHeader.readUnsignedByte();
    if (firstHeaderSize >= 33) {
      hdr.arjProtectionFactor = firstHeader.readUnsignedByte();
      hdr.arjFlags2 = firstHeader.readUnsignedByte();
      firstHeader.readUnsignedByte();
      firstHeader.readUnsignedByte();
    } 
    hdr.name = readString(basicHeader);
    hdr.comment = readString(basicHeader);
    int extendedHeaderSize = read16(this.in);
    if (extendedHeaderSize > 0) {
      hdr.extendedHeaderBytes = new byte[extendedHeaderSize];
      readFully(this.in, hdr.extendedHeaderBytes);
      long extendedHeaderCrc32 = 0xFFFFFFFFL & read32(this.in);
      CRC32 crc32 = new CRC32();
      crc32.update(hdr.extendedHeaderBytes);
      if (extendedHeaderCrc32 != crc32.getValue())
        throw new IOException("Extended header CRC32 verification failure"); 
    } 
    return hdr;
  }
  
  private LocalFileHeader readLocalFileHeader() throws IOException {
    byte[] basicHeaderBytes = readHeader();
    if (basicHeaderBytes == null)
      return null; 
    DataInputStream basicHeader = new DataInputStream(new ByteArrayInputStream(basicHeaderBytes));
    int firstHeaderSize = basicHeader.readUnsignedByte();
    byte[] firstHeaderBytes = new byte[firstHeaderSize - 1];
    basicHeader.readFully(firstHeaderBytes);
    DataInputStream firstHeader = new DataInputStream(new ByteArrayInputStream(firstHeaderBytes));
    LocalFileHeader localFileHeader = new LocalFileHeader();
    localFileHeader.archiverVersionNumber = firstHeader.readUnsignedByte();
    localFileHeader.minVersionToExtract = firstHeader.readUnsignedByte();
    localFileHeader.hostOS = firstHeader.readUnsignedByte();
    localFileHeader.arjFlags = firstHeader.readUnsignedByte();
    localFileHeader.method = firstHeader.readUnsignedByte();
    localFileHeader.fileType = firstHeader.readUnsignedByte();
    localFileHeader.reserved = firstHeader.readUnsignedByte();
    localFileHeader.dateTimeModified = read32(firstHeader);
    localFileHeader.compressedSize = 0xFFFFFFFFL & read32(firstHeader);
    localFileHeader.originalSize = 0xFFFFFFFFL & read32(firstHeader);
    localFileHeader.originalCrc32 = 0xFFFFFFFFL & read32(firstHeader);
    localFileHeader.fileSpecPosition = read16(firstHeader);
    localFileHeader.fileAccessMode = read16(firstHeader);
    pushedBackBytes(20L);
    localFileHeader.firstChapter = firstHeader.readUnsignedByte();
    localFileHeader.lastChapter = firstHeader.readUnsignedByte();
    readExtraData(firstHeaderSize, firstHeader, localFileHeader);
    localFileHeader.name = readString(basicHeader);
    localFileHeader.comment = readString(basicHeader);
    ArrayList<byte[]> extendedHeaders = (ArrayList)new ArrayList<byte>();
    int extendedHeaderSize;
    while ((extendedHeaderSize = read16(this.in)) > 0) {
      byte[] extendedHeaderBytes = new byte[extendedHeaderSize];
      readFully(this.in, extendedHeaderBytes);
      long extendedHeaderCrc32 = 0xFFFFFFFFL & read32(this.in);
      CRC32 crc32 = new CRC32();
      crc32.update(extendedHeaderBytes);
      if (extendedHeaderCrc32 != crc32.getValue())
        throw new IOException("Extended header CRC32 verification failure"); 
      extendedHeaders.add(extendedHeaderBytes);
    } 
    localFileHeader.extendedHeaders = extendedHeaders.<byte[]>toArray(new byte[extendedHeaders.size()][]);
    return localFileHeader;
  }
  
  private void readExtraData(int firstHeaderSize, DataInputStream firstHeader, LocalFileHeader localFileHeader) throws IOException {
    if (firstHeaderSize >= 33) {
      localFileHeader.extendedFilePosition = read32(firstHeader);
      if (firstHeaderSize >= 45) {
        localFileHeader.dateTimeAccessed = read32(firstHeader);
        localFileHeader.dateTimeCreated = read32(firstHeader);
        localFileHeader.originalSizeEvenForVolumes = read32(firstHeader);
        pushedBackBytes(12L);
      } 
      pushedBackBytes(4L);
    } 
  }
  
  public static boolean matches(byte[] signature, int length) {
    return (length >= 2 && (0xFF & signature[0]) == 96 && (0xFF & signature[1]) == 234);
  }
  
  public String getArchiveName() {
    return this.mainHeader.name;
  }
  
  public String getArchiveComment() {
    return this.mainHeader.comment;
  }
  
  public ArjArchiveEntry getNextEntry() throws IOException {
    if (this.currentInputStream != null) {
      IOUtils.skip(this.currentInputStream, Long.MAX_VALUE);
      this.currentInputStream.close();
      this.currentLocalFileHeader = null;
      this.currentInputStream = null;
    } 
    this.currentLocalFileHeader = readLocalFileHeader();
    if (this.currentLocalFileHeader != null) {
      this.currentInputStream = (InputStream)new BoundedInputStream(this.in, this.currentLocalFileHeader.compressedSize);
      if (this.currentLocalFileHeader.method == 0)
        this.currentInputStream = (InputStream)new CRC32VerifyingInputStream(this.currentInputStream, this.currentLocalFileHeader.originalSize, this.currentLocalFileHeader.originalCrc32); 
      return new ArjArchiveEntry(this.currentLocalFileHeader);
    } 
    this.currentInputStream = null;
    return null;
  }
  
  public boolean canReadEntryData(ArchiveEntry ae) {
    return (ae instanceof ArjArchiveEntry && ((ArjArchiveEntry)ae).getMethod() == 0);
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    if (this.currentLocalFileHeader == null)
      throw new IllegalStateException("No current arj entry"); 
    if (this.currentLocalFileHeader.method != 0)
      throw new IOException("Unsupported compression method " + this.currentLocalFileHeader.method); 
    return this.currentInputStream.read(b, off, len);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\arj\ArjArchiveInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */