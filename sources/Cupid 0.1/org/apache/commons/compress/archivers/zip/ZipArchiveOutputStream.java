package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ZipArchiveOutputStream extends ArchiveOutputStream {
  static final int BUFFER_SIZE = 512;
  
  protected boolean finished = false;
  
  private static final int DEFLATER_BLOCK_SIZE = 8192;
  
  public static final int DEFLATED = 8;
  
  public static final int DEFAULT_COMPRESSION = -1;
  
  public static final int STORED = 0;
  
  static final String DEFAULT_ENCODING = "UTF8";
  
  @Deprecated
  public static final int EFS_FLAG = 2048;
  
  private static final byte[] EMPTY = new byte[0];
  
  private CurrentEntry entry;
  
  private String comment = "";
  
  private int level = -1;
  
  private boolean hasCompressionLevelChanged = false;
  
  private int method = 8;
  
  private final List<ZipArchiveEntry> entries = new LinkedList<ZipArchiveEntry>();
  
  private final CRC32 crc = new CRC32();
  
  private long written = 0L;
  
  private long cdOffset = 0L;
  
  private long cdLength = 0L;
  
  private static final byte[] ZERO = new byte[] { 0, 0 };
  
  private static final byte[] LZERO = new byte[] { 0, 0, 0, 0 };
  
  private final Map<ZipArchiveEntry, Long> offsets = new HashMap<ZipArchiveEntry, Long>();
  
  private String encoding = "UTF8";
  
  private ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding("UTF8");
  
  protected final Deflater def = new Deflater(this.level, true);
  
  private final byte[] buf = new byte[512];
  
  private final RandomAccessFile raf;
  
  private final OutputStream out;
  
  private boolean useUTF8Flag = true;
  
  private boolean fallbackToUTF8 = false;
  
  private UnicodeExtraFieldPolicy createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
  
  private boolean hasUsedZip64 = false;
  
  private Zip64Mode zip64Mode = Zip64Mode.AsNeeded;
  
  public ZipArchiveOutputStream(OutputStream out) {
    this.out = out;
    this.raf = null;
  }
  
  public ZipArchiveOutputStream(File file) throws IOException {
    OutputStream o = null;
    RandomAccessFile _raf = null;
    try {
      _raf = new RandomAccessFile(file, "rw");
      _raf.setLength(0L);
    } catch (IOException e) {
      IOUtils.closeQuietly(_raf);
      _raf = null;
      o = new FileOutputStream(file);
    } 
    this.out = o;
    this.raf = _raf;
  }
  
  public boolean isSeekable() {
    return (this.raf != null);
  }
  
  public void setEncoding(String encoding) {
    this.encoding = encoding;
    this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
    if (this.useUTF8Flag && !ZipEncodingHelper.isUTF8(encoding))
      this.useUTF8Flag = false; 
  }
  
  public String getEncoding() {
    return this.encoding;
  }
  
  public void setUseLanguageEncodingFlag(boolean b) {
    this.useUTF8Flag = (b && ZipEncodingHelper.isUTF8(this.encoding));
  }
  
  public void setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy b) {
    this.createUnicodeExtraFields = b;
  }
  
  public void setFallbackToUTF8(boolean b) {
    this.fallbackToUTF8 = b;
  }
  
  public void setUseZip64(Zip64Mode mode) {
    this.zip64Mode = mode;
  }
  
  public void finish() throws IOException {
    if (this.finished)
      throw new IOException("This archive has already been finished"); 
    if (this.entry != null)
      throw new IOException("This archive contains unclosed entries."); 
    this.cdOffset = this.written;
    for (ZipArchiveEntry ze : this.entries)
      writeCentralFileHeader(ze); 
    this.cdLength = this.written - this.cdOffset;
    writeZip64CentralDirectory();
    writeCentralDirectoryEnd();
    this.offsets.clear();
    this.entries.clear();
    this.def.end();
    this.finished = true;
  }
  
  public void closeArchiveEntry() throws IOException {
    if (this.finished)
      throw new IOException("Stream has already been finished"); 
    if (this.entry == null)
      throw new IOException("No current entry to close"); 
    if (!this.entry.hasWritten)
      write(EMPTY, 0, 0); 
    flushDeflater();
    Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
    long bytesWritten = this.written - this.entry.dataStart;
    long realCrc = this.crc.getValue();
    this.crc.reset();
    boolean actuallyNeedsZip64 = handleSizesAndCrc(bytesWritten, realCrc, effectiveMode);
    if (this.raf != null)
      rewriteSizesAndCrc(actuallyNeedsZip64); 
    writeDataDescriptor(this.entry.entry);
    this.entry = null;
  }
  
  private void flushDeflater() throws IOException {
    if (this.entry.entry.getMethod() == 8) {
      this.def.finish();
      while (!this.def.finished())
        deflate(); 
    } 
  }
  
  private boolean handleSizesAndCrc(long bytesWritten, long crc, Zip64Mode effectiveMode) throws ZipException {
    if (this.entry.entry.getMethod() == 8) {
      this.entry.entry.setSize(this.entry.bytesRead);
      this.entry.entry.setCompressedSize(bytesWritten);
      this.entry.entry.setCrc(crc);
      this.def.reset();
    } else if (this.raf == null) {
      if (this.entry.entry.getCrc() != crc)
        throw new ZipException("bad CRC checksum for entry " + this.entry.entry.getName() + ": " + Long.toHexString(this.entry.entry.getCrc()) + " instead of " + Long.toHexString(crc)); 
      if (this.entry.entry.getSize() != bytesWritten)
        throw new ZipException("bad size for entry " + this.entry.entry.getName() + ": " + this.entry.entry.getSize() + " instead of " + bytesWritten); 
    } else {
      this.entry.entry.setSize(bytesWritten);
      this.entry.entry.setCompressedSize(bytesWritten);
      this.entry.entry.setCrc(crc);
    } 
    boolean actuallyNeedsZip64 = (effectiveMode == Zip64Mode.Always || this.entry.entry.getSize() >= 4294967295L || this.entry.entry.getCompressedSize() >= 4294967295L);
    if (actuallyNeedsZip64 && effectiveMode == Zip64Mode.Never)
      throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry)); 
    return actuallyNeedsZip64;
  }
  
  private void rewriteSizesAndCrc(boolean actuallyNeedsZip64) throws IOException {
    long save = this.raf.getFilePointer();
    this.raf.seek(this.entry.localDataStart);
    writeOut(ZipLong.getBytes(this.entry.entry.getCrc()));
    if (!hasZip64Extra(this.entry.entry) || !actuallyNeedsZip64) {
      writeOut(ZipLong.getBytes(this.entry.entry.getCompressedSize()));
      writeOut(ZipLong.getBytes(this.entry.entry.getSize()));
    } else {
      writeOut(ZipLong.ZIP64_MAGIC.getBytes());
      writeOut(ZipLong.ZIP64_MAGIC.getBytes());
    } 
    if (hasZip64Extra(this.entry.entry)) {
      this.raf.seek(this.entry.localDataStart + 12L + 4L + getName(this.entry.entry).limit() + 4L);
      writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getSize()));
      writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getCompressedSize()));
      if (!actuallyNeedsZip64) {
        this.raf.seek(this.entry.localDataStart - 10L);
        writeOut(ZipShort.getBytes(10));
        this.entry.entry.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        this.entry.entry.setExtra();
        if (this.entry.causedUseOfZip64)
          this.hasUsedZip64 = false; 
      } 
    } 
    this.raf.seek(save);
  }
  
  public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
    if (this.finished)
      throw new IOException("Stream has already been finished"); 
    if (this.entry != null)
      closeArchiveEntry(); 
    this.entry = new CurrentEntry((ZipArchiveEntry)archiveEntry);
    this.entries.add(this.entry.entry);
    setDefaults(this.entry.entry);
    Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
    validateSizeInformation(effectiveMode);
    if (shouldAddZip64Extra(this.entry.entry, effectiveMode)) {
      Zip64ExtendedInformationExtraField z64 = getZip64Extra(this.entry.entry);
      ZipEightByteInteger size = ZipEightByteInteger.ZERO;
      if (this.entry.entry.getMethod() == 0 && this.entry.entry.getSize() != -1L)
        size = new ZipEightByteInteger(this.entry.entry.getSize()); 
      z64.setSize(size);
      z64.setCompressedSize(size);
      this.entry.entry.setExtra();
    } 
    if (this.entry.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
      this.def.setLevel(this.level);
      this.hasCompressionLevelChanged = false;
    } 
    writeLocalFileHeader(this.entry.entry);
  }
  
  private void setDefaults(ZipArchiveEntry entry) {
    if (entry.getMethod() == -1)
      entry.setMethod(this.method); 
    if (entry.getTime() == -1L)
      entry.setTime(System.currentTimeMillis()); 
  }
  
  private void validateSizeInformation(Zip64Mode effectiveMode) throws ZipException {
    if (this.entry.entry.getMethod() == 0 && this.raf == null) {
      if (this.entry.entry.getSize() == -1L)
        throw new ZipException("uncompressed size is required for STORED method when not writing to a file"); 
      if (this.entry.entry.getCrc() == -1L)
        throw new ZipException("crc checksum is required for STORED method when not writing to a file"); 
      this.entry.entry.setCompressedSize(this.entry.entry.getSize());
    } 
    if ((this.entry.entry.getSize() >= 4294967295L || this.entry.entry.getCompressedSize() >= 4294967295L) && effectiveMode == Zip64Mode.Never)
      throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry)); 
  }
  
  private boolean shouldAddZip64Extra(ZipArchiveEntry entry, Zip64Mode mode) {
    return (mode == Zip64Mode.Always || entry.getSize() >= 4294967295L || entry.getCompressedSize() >= 4294967295L || (entry.getSize() == -1L && this.raf != null && mode != Zip64Mode.Never));
  }
  
  public void setComment(String comment) {
    this.comment = comment;
  }
  
  public void setLevel(int level) {
    if (level < -1 || level > 9)
      throw new IllegalArgumentException("Invalid compression level: " + level); 
    this.hasCompressionLevelChanged = (this.level != level);
    this.level = level;
  }
  
  public void setMethod(int method) {
    this.method = method;
  }
  
  public boolean canWriteEntryData(ArchiveEntry ae) {
    if (ae instanceof ZipArchiveEntry) {
      ZipArchiveEntry zae = (ZipArchiveEntry)ae;
      return (zae.getMethod() != ZipMethod.IMPLODING.getCode() && zae.getMethod() != ZipMethod.UNSHRINKING.getCode() && ZipUtil.canHandleEntryData(zae));
    } 
    return false;
  }
  
  public void write(byte[] b, int offset, int length) throws IOException {
    if (this.entry == null)
      throw new IllegalStateException("No current entry"); 
    ZipUtil.checkRequestedFeatures(this.entry.entry);
    this.entry.hasWritten = true;
    if (this.entry.entry.getMethod() == 8) {
      writeDeflated(b, offset, length);
    } else {
      writeOut(b, offset, length);
      this.written += length;
    } 
    this.crc.update(b, offset, length);
    count(length);
  }
  
  private void writeDeflated(byte[] b, int offset, int length) throws IOException {
    if (length > 0 && !this.def.finished()) {
      this.entry.bytesRead += length;
      if (length <= 8192) {
        this.def.setInput(b, offset, length);
        deflateUntilInputIsNeeded();
      } else {
        int fullblocks = length / 8192;
        for (int i = 0; i < fullblocks; i++) {
          this.def.setInput(b, offset + i * 8192, 8192);
          deflateUntilInputIsNeeded();
        } 
        int done = fullblocks * 8192;
        if (done < length) {
          this.def.setInput(b, offset + done, length - done);
          deflateUntilInputIsNeeded();
        } 
      } 
    } 
  }
  
  public void close() throws IOException {
    if (!this.finished)
      finish(); 
    destroy();
  }
  
  public void flush() throws IOException {
    if (this.out != null)
      this.out.flush(); 
  }
  
  static final byte[] LFH_SIG = ZipLong.LFH_SIG.getBytes();
  
  static final byte[] DD_SIG = ZipLong.DD_SIG.getBytes();
  
  static final byte[] CFH_SIG = ZipLong.CFH_SIG.getBytes();
  
  static final byte[] EOCD_SIG = ZipLong.getBytes(101010256L);
  
  static final byte[] ZIP64_EOCD_SIG = ZipLong.getBytes(101075792L);
  
  static final byte[] ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(117853008L);
  
  protected final void deflate() throws IOException {
    int len = this.def.deflate(this.buf, 0, this.buf.length);
    if (len > 0) {
      writeOut(this.buf, 0, len);
      this.written += len;
    } 
  }
  
  protected void writeLocalFileHeader(ZipArchiveEntry ze) throws IOException {
    boolean encodable = this.zipEncoding.canEncode(ze.getName());
    ByteBuffer name = getName(ze);
    if (this.createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER)
      addUnicodeExtraFields(ze, encodable, name); 
    this.offsets.put(ze, Long.valueOf(this.written));
    writeOut(LFH_SIG);
    this.written += 4L;
    int zipMethod = ze.getMethod();
    writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, (!encodable && this.fallbackToUTF8), hasZip64Extra(ze));
    this.written += 4L;
    writeOut(ZipShort.getBytes(zipMethod));
    this.written += 2L;
    writeOut(ZipUtil.toDosTime(ze.getTime()));
    this.written += 4L;
    this.entry.localDataStart = this.written;
    if (zipMethod == 8 || this.raf != null) {
      writeOut(LZERO);
      if (hasZip64Extra(this.entry.entry)) {
        writeOut(ZipLong.ZIP64_MAGIC.getBytes());
        writeOut(ZipLong.ZIP64_MAGIC.getBytes());
      } else {
        writeOut(LZERO);
        writeOut(LZERO);
      } 
    } else {
      writeOut(ZipLong.getBytes(ze.getCrc()));
      byte[] size = ZipLong.ZIP64_MAGIC.getBytes();
      if (!hasZip64Extra(ze))
        size = ZipLong.getBytes(ze.getSize()); 
      writeOut(size);
      writeOut(size);
    } 
    this.written += 12L;
    writeOut(ZipShort.getBytes(name.limit()));
    this.written += 2L;
    byte[] extra = ze.getLocalFileDataExtra();
    writeOut(ZipShort.getBytes(extra.length));
    this.written += 2L;
    writeOut(name.array(), name.arrayOffset(), name.limit() - name.position());
    this.written += name.limit();
    writeOut(extra);
    this.written += extra.length;
    this.entry.dataStart = this.written;
  }
  
  private void addUnicodeExtraFields(ZipArchiveEntry ze, boolean encodable, ByteBuffer name) throws IOException {
    if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !encodable)
      ze.addExtraField(new UnicodePathExtraField(ze.getName(), name.array(), name.arrayOffset(), name.limit() - name.position())); 
    String comm = ze.getComment();
    if (comm != null && !"".equals(comm)) {
      boolean commentEncodable = this.zipEncoding.canEncode(comm);
      if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !commentEncodable) {
        ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
        ze.addExtraField(new UnicodeCommentExtraField(comm, commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position()));
      } 
    } 
  }
  
  protected void writeDataDescriptor(ZipArchiveEntry ze) throws IOException {
    if (ze.getMethod() != 8 || this.raf != null)
      return; 
    writeOut(DD_SIG);
    writeOut(ZipLong.getBytes(ze.getCrc()));
    int sizeFieldSize = 4;
    if (!hasZip64Extra(ze)) {
      writeOut(ZipLong.getBytes(ze.getCompressedSize()));
      writeOut(ZipLong.getBytes(ze.getSize()));
    } else {
      sizeFieldSize = 8;
      writeOut(ZipEightByteInteger.getBytes(ze.getCompressedSize()));
      writeOut(ZipEightByteInteger.getBytes(ze.getSize()));
    } 
    this.written += (8 + 2 * sizeFieldSize);
  }
  
  protected void writeCentralFileHeader(ZipArchiveEntry ze) throws IOException {
    writeOut(CFH_SIG);
    this.written += 4L;
    long lfhOffset = ((Long)this.offsets.get(ze)).longValue();
    boolean needsZip64Extra = (hasZip64Extra(ze) || ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L || lfhOffset >= 4294967295L);
    if (needsZip64Extra && this.zip64Mode == Zip64Mode.Never)
      throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte."); 
    handleZip64Extra(ze, lfhOffset, needsZip64Extra);
    writeOut(ZipShort.getBytes(ze.getPlatform() << 8 | (!this.hasUsedZip64 ? 20 : 45)));
    this.written += 2L;
    int zipMethod = ze.getMethod();
    boolean encodable = this.zipEncoding.canEncode(ze.getName());
    writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, (!encodable && this.fallbackToUTF8), needsZip64Extra);
    this.written += 4L;
    writeOut(ZipShort.getBytes(zipMethod));
    this.written += 2L;
    writeOut(ZipUtil.toDosTime(ze.getTime()));
    this.written += 4L;
    writeOut(ZipLong.getBytes(ze.getCrc()));
    if (ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L) {
      writeOut(ZipLong.ZIP64_MAGIC.getBytes());
      writeOut(ZipLong.ZIP64_MAGIC.getBytes());
    } else {
      writeOut(ZipLong.getBytes(ze.getCompressedSize()));
      writeOut(ZipLong.getBytes(ze.getSize()));
    } 
    this.written += 12L;
    ByteBuffer name = getName(ze);
    writeOut(ZipShort.getBytes(name.limit()));
    this.written += 2L;
    byte[] extra = ze.getCentralDirectoryExtra();
    writeOut(ZipShort.getBytes(extra.length));
    this.written += 2L;
    String comm = ze.getComment();
    if (comm == null)
      comm = ""; 
    ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
    writeOut(ZipShort.getBytes(commentB.limit()));
    this.written += 2L;
    writeOut(ZERO);
    this.written += 2L;
    writeOut(ZipShort.getBytes(ze.getInternalAttributes()));
    this.written += 2L;
    writeOut(ZipLong.getBytes(ze.getExternalAttributes()));
    this.written += 4L;
    writeOut(ZipLong.getBytes(Math.min(lfhOffset, 4294967295L)));
    this.written += 4L;
    writeOut(name.array(), name.arrayOffset(), name.limit() - name.position());
    this.written += name.limit();
    writeOut(extra);
    this.written += extra.length;
    writeOut(commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position());
    this.written += commentB.limit();
  }
  
  private void handleZip64Extra(ZipArchiveEntry ze, long lfhOffset, boolean needsZip64Extra) {
    if (needsZip64Extra) {
      Zip64ExtendedInformationExtraField z64 = getZip64Extra(ze);
      if (ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L) {
        z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
        z64.setSize(new ZipEightByteInteger(ze.getSize()));
      } else {
        z64.setCompressedSize(null);
        z64.setSize(null);
      } 
      if (lfhOffset >= 4294967295L)
        z64.setRelativeHeaderOffset(new ZipEightByteInteger(lfhOffset)); 
      ze.setExtra();
    } 
  }
  
  protected void writeCentralDirectoryEnd() throws IOException {
    writeOut(EOCD_SIG);
    writeOut(ZERO);
    writeOut(ZERO);
    int numberOfEntries = this.entries.size();
    if (numberOfEntries > 65535 && this.zip64Mode == Zip64Mode.Never)
      throw new Zip64RequiredException("archive contains more than 65535 entries."); 
    if (this.cdOffset > 4294967295L && this.zip64Mode == Zip64Mode.Never)
      throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte."); 
    byte[] num = ZipShort.getBytes(Math.min(numberOfEntries, 65535));
    writeOut(num);
    writeOut(num);
    writeOut(ZipLong.getBytes(Math.min(this.cdLength, 4294967295L)));
    writeOut(ZipLong.getBytes(Math.min(this.cdOffset, 4294967295L)));
    ByteBuffer data = this.zipEncoding.encode(this.comment);
    writeOut(ZipShort.getBytes(data.limit()));
    writeOut(data.array(), data.arrayOffset(), data.limit() - data.position());
  }
  
  private static final byte[] ONE = ZipLong.getBytes(1L);
  
  protected void writeZip64CentralDirectory() throws IOException {
    if (this.zip64Mode == Zip64Mode.Never)
      return; 
    if (!this.hasUsedZip64 && (this.cdOffset >= 4294967295L || this.cdLength >= 4294967295L || this.entries.size() >= 65535))
      this.hasUsedZip64 = true; 
    if (!this.hasUsedZip64)
      return; 
    long offset = this.written;
    writeOut(ZIP64_EOCD_SIG);
    writeOut(ZipEightByteInteger.getBytes(44L));
    writeOut(ZipShort.getBytes(45));
    writeOut(ZipShort.getBytes(45));
    writeOut(LZERO);
    writeOut(LZERO);
    byte[] num = ZipEightByteInteger.getBytes(this.entries.size());
    writeOut(num);
    writeOut(num);
    writeOut(ZipEightByteInteger.getBytes(this.cdLength));
    writeOut(ZipEightByteInteger.getBytes(this.cdOffset));
    writeOut(ZIP64_EOCD_LOC_SIG);
    writeOut(LZERO);
    writeOut(ZipEightByteInteger.getBytes(offset));
    writeOut(ONE);
  }
  
  protected final void writeOut(byte[] data) throws IOException {
    writeOut(data, 0, data.length);
  }
  
  protected final void writeOut(byte[] data, int offset, int length) throws IOException {
    if (this.raf != null) {
      this.raf.write(data, offset, length);
    } else {
      this.out.write(data, offset, length);
    } 
  }
  
  private void deflateUntilInputIsNeeded() throws IOException {
    while (!this.def.needsInput())
      deflate(); 
  }
  
  private void writeVersionNeededToExtractAndGeneralPurposeBits(int zipMethod, boolean utfFallback, boolean zip64) throws IOException {
    int versionNeededToExtract = 10;
    GeneralPurposeBit b = new GeneralPurposeBit();
    b.useUTF8ForNames((this.useUTF8Flag || utfFallback));
    if (zipMethod == 8 && this.raf == null) {
      versionNeededToExtract = 20;
      b.useDataDescriptor(true);
    } 
    if (zip64)
      versionNeededToExtract = 45; 
    writeOut(ZipShort.getBytes(versionNeededToExtract));
    writeOut(b.encode());
  }
  
  public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
    if (this.finished)
      throw new IOException("Stream has already been finished"); 
    return new ZipArchiveEntry(inputFile, entryName);
  }
  
  private Zip64ExtendedInformationExtraField getZip64Extra(ZipArchiveEntry ze) {
    if (this.entry != null)
      this.entry.causedUseOfZip64 = !this.hasUsedZip64; 
    this.hasUsedZip64 = true;
    Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
    if (z64 == null)
      z64 = new Zip64ExtendedInformationExtraField(); 
    ze.addAsFirstExtraField(z64);
    return z64;
  }
  
  private boolean hasZip64Extra(ZipArchiveEntry ze) {
    return (ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID) != null);
  }
  
  private Zip64Mode getEffectiveZip64Mode(ZipArchiveEntry ze) {
    if (this.zip64Mode != Zip64Mode.AsNeeded || this.raf != null || ze.getMethod() != 8 || ze.getSize() != -1L)
      return this.zip64Mode; 
    return Zip64Mode.Never;
  }
  
  private ZipEncoding getEntryEncoding(ZipArchiveEntry ze) {
    boolean encodable = this.zipEncoding.canEncode(ze.getName());
    return (!encodable && this.fallbackToUTF8) ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
  }
  
  private ByteBuffer getName(ZipArchiveEntry ze) throws IOException {
    return getEntryEncoding(ze).encode(ze.getName());
  }
  
  void destroy() throws IOException {
    if (this.raf != null)
      this.raf.close(); 
    if (this.out != null)
      this.out.close(); 
  }
  
  public static final class UnicodeExtraFieldPolicy {
    public static final UnicodeExtraFieldPolicy ALWAYS = new UnicodeExtraFieldPolicy("always");
    
    public static final UnicodeExtraFieldPolicy NEVER = new UnicodeExtraFieldPolicy("never");
    
    public static final UnicodeExtraFieldPolicy NOT_ENCODEABLE = new UnicodeExtraFieldPolicy("not encodeable");
    
    private final String name;
    
    private UnicodeExtraFieldPolicy(String n) {
      this.name = n;
    }
    
    public String toString() {
      return this.name;
    }
  }
  
  private static final class CurrentEntry {
    private final ZipArchiveEntry entry;
    
    private long localDataStart;
    
    private long dataStart;
    
    private long bytesRead;
    
    private boolean causedUseOfZip64;
    
    private boolean hasWritten;
    
    private CurrentEntry(ZipArchiveEntry entry) {
      this.localDataStart = 0L;
      this.dataStart = 0L;
      this.bytesRead = 0L;
      this.causedUseOfZip64 = false;
      this.entry = entry;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\ZipArchiveOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */