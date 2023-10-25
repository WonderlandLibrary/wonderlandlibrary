package org.apache.commons.compress.archivers.sevenz;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;

public class SevenZOutputFile implements Closeable {
  private final RandomAccessFile file;
  
  private final List<SevenZArchiveEntry> files = new ArrayList<SevenZArchiveEntry>();
  
  private int numNonEmptyStreams = 0;
  
  private final CRC32 crc32 = new CRC32();
  
  private final CRC32 compressedCrc32 = new CRC32();
  
  private long fileBytesWritten = 0L;
  
  private boolean finished = false;
  
  private CountingOutputStream currentOutputStream;
  
  private CountingOutputStream[] additionalCountingStreams;
  
  private Iterable<? extends SevenZMethodConfiguration> contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
  
  private final Map<SevenZArchiveEntry, long[]> additionalSizes = (Map)new HashMap<SevenZArchiveEntry, long>();
  
  public SevenZOutputFile(File filename) throws IOException {
    this.file = new RandomAccessFile(filename, "rw");
    this.file.seek(32L);
  }
  
  public void setContentCompression(SevenZMethod method) {
    setContentMethods(Collections.singletonList(new SevenZMethodConfiguration(method)));
  }
  
  public void setContentMethods(Iterable<? extends SevenZMethodConfiguration> methods) {
    this.contentMethods = reverse(methods);
  }
  
  public void close() throws IOException {
    if (!this.finished)
      finish(); 
    this.file.close();
  }
  
  public SevenZArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
    SevenZArchiveEntry entry = new SevenZArchiveEntry();
    entry.setDirectory(inputFile.isDirectory());
    entry.setName(entryName);
    entry.setLastModifiedDate(new Date(inputFile.lastModified()));
    return entry;
  }
  
  public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
    SevenZArchiveEntry entry = (SevenZArchiveEntry)archiveEntry;
    this.files.add(entry);
  }
  
  public void closeArchiveEntry() throws IOException {
    if (this.currentOutputStream != null) {
      this.currentOutputStream.flush();
      this.currentOutputStream.close();
    } 
    SevenZArchiveEntry entry = this.files.get(this.files.size() - 1);
    if (this.fileBytesWritten > 0L) {
      entry.setHasStream(true);
      this.numNonEmptyStreams++;
      entry.setSize(this.currentOutputStream.getBytesWritten());
      entry.setCompressedSize(this.fileBytesWritten);
      entry.setCrcValue(this.crc32.getValue());
      entry.setCompressedCrcValue(this.compressedCrc32.getValue());
      entry.setHasCrc(true);
      if (this.additionalCountingStreams != null) {
        long[] sizes = new long[this.additionalCountingStreams.length];
        for (int i = 0; i < this.additionalCountingStreams.length; i++)
          sizes[i] = this.additionalCountingStreams[i].getBytesWritten(); 
        this.additionalSizes.put(entry, sizes);
      } 
    } else {
      entry.setHasStream(false);
      entry.setSize(0L);
      entry.setCompressedSize(0L);
      entry.setHasCrc(false);
    } 
    this.currentOutputStream = null;
    this.additionalCountingStreams = null;
    this.crc32.reset();
    this.compressedCrc32.reset();
    this.fileBytesWritten = 0L;
  }
  
  public void write(int b) throws IOException {
    getCurrentOutputStream().write(b);
  }
  
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    if (len > 0)
      getCurrentOutputStream().write(b, off, len); 
  }
  
  public void finish() throws IOException {
    if (this.finished)
      throw new IOException("This archive has already been finished"); 
    this.finished = true;
    long headerPosition = this.file.getFilePointer();
    ByteArrayOutputStream headerBaos = new ByteArrayOutputStream();
    DataOutputStream header = new DataOutputStream(headerBaos);
    writeHeader(header);
    header.flush();
    byte[] headerBytes = headerBaos.toByteArray();
    this.file.write(headerBytes);
    CRC32 crc32 = new CRC32();
    this.file.seek(0L);
    this.file.write(SevenZFile.sevenZSignature);
    this.file.write(0);
    this.file.write(2);
    ByteArrayOutputStream startHeaderBaos = new ByteArrayOutputStream();
    DataOutputStream startHeaderStream = new DataOutputStream(startHeaderBaos);
    startHeaderStream.writeLong(Long.reverseBytes(headerPosition - 32L));
    startHeaderStream.writeLong(Long.reverseBytes(0xFFFFFFFFL & headerBytes.length));
    crc32.reset();
    crc32.update(headerBytes);
    startHeaderStream.writeInt(Integer.reverseBytes((int)crc32.getValue()));
    startHeaderStream.flush();
    byte[] startHeaderBytes = startHeaderBaos.toByteArray();
    crc32.reset();
    crc32.update(startHeaderBytes);
    this.file.writeInt(Integer.reverseBytes((int)crc32.getValue()));
    this.file.write(startHeaderBytes);
  }
  
  private OutputStream getCurrentOutputStream() throws IOException {
    if (this.currentOutputStream == null)
      this.currentOutputStream = setupFileOutputStream(); 
    return (OutputStream)this.currentOutputStream;
  }
  
  private CountingOutputStream setupFileOutputStream() throws IOException {
    if (this.files.isEmpty())
      throw new IllegalStateException("No current 7z entry"); 
    OutputStream outputStream = new OutputStreamWrapper();
    ArrayList<CountingOutputStream> moreStreams = new ArrayList<CountingOutputStream>();
    boolean first = true;
    for (SevenZMethodConfiguration m : getContentMethods(this.files.get(this.files.size() - 1))) {
      CountingOutputStream countingOutputStream;
      if (!first) {
        CountingOutputStream cos = new CountingOutputStream(outputStream);
        moreStreams.add(cos);
        countingOutputStream = cos;
      } 
      outputStream = Coders.addEncoder((OutputStream)countingOutputStream, m.getMethod(), m.getOptions());
      first = false;
    } 
    if (!moreStreams.isEmpty())
      this.additionalCountingStreams = moreStreams.<CountingOutputStream>toArray(new CountingOutputStream[moreStreams.size()]); 
    return new CountingOutputStream(outputStream) {
        public void write(int b) throws IOException {
          super.write(b);
          SevenZOutputFile.this.crc32.update(b);
        }
        
        public void write(byte[] b) throws IOException {
          super.write(b);
          SevenZOutputFile.this.crc32.update(b);
        }
        
        public void write(byte[] b, int off, int len) throws IOException {
          super.write(b, off, len);
          SevenZOutputFile.this.crc32.update(b, off, len);
        }
      };
  }
  
  private Iterable<? extends SevenZMethodConfiguration> getContentMethods(SevenZArchiveEntry entry) {
    Iterable<? extends SevenZMethodConfiguration> ms = entry.getContentMethods();
    return (ms == null) ? this.contentMethods : ms;
  }
  
  private void writeHeader(DataOutput header) throws IOException {
    header.write(1);
    header.write(4);
    writeStreamsInfo(header);
    writeFilesInfo(header);
    header.write(0);
  }
  
  private void writeStreamsInfo(DataOutput header) throws IOException {
    if (this.numNonEmptyStreams > 0) {
      writePackInfo(header);
      writeUnpackInfo(header);
    } 
    writeSubStreamsInfo(header);
    header.write(0);
  }
  
  private void writePackInfo(DataOutput header) throws IOException {
    header.write(6);
    writeUint64(header, 0L);
    writeUint64(header, 0xFFFFFFFFL & this.numNonEmptyStreams);
    header.write(9);
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.hasStream())
        writeUint64(header, entry.getCompressedSize()); 
    } 
    header.write(10);
    header.write(1);
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.hasStream())
        header.writeInt(Integer.reverseBytes((int)entry.getCompressedCrcValue())); 
    } 
    header.write(0);
  }
  
  private void writeUnpackInfo(DataOutput header) throws IOException {
    header.write(7);
    header.write(11);
    writeUint64(header, this.numNonEmptyStreams);
    header.write(0);
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.hasStream())
        writeFolder(header, entry); 
    } 
    header.write(12);
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.hasStream()) {
        long[] moreSizes = this.additionalSizes.get(entry);
        if (moreSizes != null)
          for (long s : moreSizes)
            writeUint64(header, s);  
        writeUint64(header, entry.getSize());
      } 
    } 
    header.write(10);
    header.write(1);
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.hasStream())
        header.writeInt(Integer.reverseBytes((int)entry.getCrcValue())); 
    } 
    header.write(0);
  }
  
  private void writeFolder(DataOutput header, SevenZArchiveEntry entry) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int numCoders = 0;
    for (SevenZMethodConfiguration m : getContentMethods(entry)) {
      numCoders++;
      writeSingleCodec(m, bos);
    } 
    writeUint64(header, numCoders);
    header.write(bos.toByteArray());
    for (int i = 0; i < numCoders - 1; i++) {
      writeUint64(header, (i + 1));
      writeUint64(header, i);
    } 
  }
  
  private void writeSingleCodec(SevenZMethodConfiguration m, OutputStream bos) throws IOException {
    byte[] id = m.getMethod().getId();
    byte[] properties = Coders.findByMethod(m.getMethod()).getOptionsAsProperties(m.getOptions());
    int codecFlags = id.length;
    if (properties.length > 0)
      codecFlags |= 0x20; 
    bos.write(codecFlags);
    bos.write(id);
    if (properties.length > 0) {
      bos.write(properties.length);
      bos.write(properties);
    } 
  }
  
  private void writeSubStreamsInfo(DataOutput header) throws IOException {
    header.write(8);
    header.write(0);
  }
  
  private void writeFilesInfo(DataOutput header) throws IOException {
    header.write(5);
    writeUint64(header, this.files.size());
    writeFileEmptyStreams(header);
    writeFileEmptyFiles(header);
    writeFileAntiItems(header);
    writeFileNames(header);
    writeFileCTimes(header);
    writeFileATimes(header);
    writeFileMTimes(header);
    writeFileWindowsAttributes(header);
    header.write(0);
  }
  
  private void writeFileEmptyStreams(DataOutput header) throws IOException {
    boolean hasEmptyStreams = false;
    for (SevenZArchiveEntry entry : this.files) {
      if (!entry.hasStream()) {
        hasEmptyStreams = true;
        break;
      } 
    } 
    if (hasEmptyStreams) {
      header.write(14);
      BitSet emptyStreams = new BitSet(this.files.size());
      for (int i = 0; i < this.files.size(); i++)
        emptyStreams.set(i, !((SevenZArchiveEntry)this.files.get(i)).hasStream()); 
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      writeBits(out, emptyStreams, this.files.size());
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeFileEmptyFiles(DataOutput header) throws IOException {
    int j;
    boolean hasEmptyFiles = false;
    int emptyStreamCounter = 0;
    BitSet emptyFiles = new BitSet(0);
    for (int i = 0; i < this.files.size(); i++) {
      if (!((SevenZArchiveEntry)this.files.get(i)).hasStream()) {
        boolean isDir = ((SevenZArchiveEntry)this.files.get(i)).isDirectory();
        emptyFiles.set(emptyStreamCounter++, !isDir);
        j = hasEmptyFiles | (!isDir ? 1 : 0);
      } 
    } 
    if (j != 0) {
      header.write(15);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      writeBits(out, emptyFiles, emptyStreamCounter);
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeFileAntiItems(DataOutput header) throws IOException {
    boolean hasAntiItems = false;
    BitSet antiItems = new BitSet(0);
    int antiItemCounter = 0;
    for (int i = 0; i < this.files.size(); i++) {
      if (!((SevenZArchiveEntry)this.files.get(i)).hasStream()) {
        boolean isAnti = ((SevenZArchiveEntry)this.files.get(i)).isAntiItem();
        antiItems.set(antiItemCounter++, isAnti);
        hasAntiItems |= isAnti;
      } 
    } 
    if (hasAntiItems) {
      header.write(16);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      writeBits(out, antiItems, antiItemCounter);
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeFileNames(DataOutput header) throws IOException {
    header.write(17);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    out.write(0);
    for (SevenZArchiveEntry entry : this.files) {
      out.write(entry.getName().getBytes("UTF-16LE"));
      out.writeShort(0);
    } 
    out.flush();
    byte[] contents = baos.toByteArray();
    writeUint64(header, contents.length);
    header.write(contents);
  }
  
  private void writeFileCTimes(DataOutput header) throws IOException {
    int numCreationDates = 0;
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.getHasCreationDate())
        numCreationDates++; 
    } 
    if (numCreationDates > 0) {
      header.write(18);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      if (numCreationDates != this.files.size()) {
        out.write(0);
        BitSet cTimes = new BitSet(this.files.size());
        for (int i = 0; i < this.files.size(); i++)
          cTimes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasCreationDate()); 
        writeBits(out, cTimes, this.files.size());
      } else {
        out.write(1);
      } 
      out.write(0);
      for (SevenZArchiveEntry entry : this.files) {
        if (entry.getHasCreationDate())
          out.writeLong(Long.reverseBytes(SevenZArchiveEntry.javaTimeToNtfsTime(entry.getCreationDate()))); 
      } 
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeFileATimes(DataOutput header) throws IOException {
    int numAccessDates = 0;
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.getHasAccessDate())
        numAccessDates++; 
    } 
    if (numAccessDates > 0) {
      header.write(19);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      if (numAccessDates != this.files.size()) {
        out.write(0);
        BitSet aTimes = new BitSet(this.files.size());
        for (int i = 0; i < this.files.size(); i++)
          aTimes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasAccessDate()); 
        writeBits(out, aTimes, this.files.size());
      } else {
        out.write(1);
      } 
      out.write(0);
      for (SevenZArchiveEntry entry : this.files) {
        if (entry.getHasAccessDate())
          out.writeLong(Long.reverseBytes(SevenZArchiveEntry.javaTimeToNtfsTime(entry.getAccessDate()))); 
      } 
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeFileMTimes(DataOutput header) throws IOException {
    int numLastModifiedDates = 0;
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.getHasLastModifiedDate())
        numLastModifiedDates++; 
    } 
    if (numLastModifiedDates > 0) {
      header.write(20);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      if (numLastModifiedDates != this.files.size()) {
        out.write(0);
        BitSet mTimes = new BitSet(this.files.size());
        for (int i = 0; i < this.files.size(); i++)
          mTimes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasLastModifiedDate()); 
        writeBits(out, mTimes, this.files.size());
      } else {
        out.write(1);
      } 
      out.write(0);
      for (SevenZArchiveEntry entry : this.files) {
        if (entry.getHasLastModifiedDate())
          out.writeLong(Long.reverseBytes(SevenZArchiveEntry.javaTimeToNtfsTime(entry.getLastModifiedDate()))); 
      } 
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeFileWindowsAttributes(DataOutput header) throws IOException {
    int numWindowsAttributes = 0;
    for (SevenZArchiveEntry entry : this.files) {
      if (entry.getHasWindowsAttributes())
        numWindowsAttributes++; 
    } 
    if (numWindowsAttributes > 0) {
      header.write(21);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(baos);
      if (numWindowsAttributes != this.files.size()) {
        out.write(0);
        BitSet attributes = new BitSet(this.files.size());
        for (int i = 0; i < this.files.size(); i++)
          attributes.set(i, ((SevenZArchiveEntry)this.files.get(i)).getHasWindowsAttributes()); 
        writeBits(out, attributes, this.files.size());
      } else {
        out.write(1);
      } 
      out.write(0);
      for (SevenZArchiveEntry entry : this.files) {
        if (entry.getHasWindowsAttributes())
          out.writeInt(Integer.reverseBytes(entry.getWindowsAttributes())); 
      } 
      out.flush();
      byte[] contents = baos.toByteArray();
      writeUint64(header, contents.length);
      header.write(contents);
    } 
  }
  
  private void writeUint64(DataOutput header, long value) throws IOException {
    int firstByte = 0;
    int mask = 128;
    int i;
    for (i = 0; i < 8; i++) {
      if (value < 1L << 7 * (i + 1)) {
        firstByte = (int)(firstByte | value >>> 8 * i);
        break;
      } 
      firstByte |= mask;
      mask >>>= 1;
    } 
    header.write(firstByte);
    for (; i > 0; i--) {
      header.write((int)(0xFFL & value));
      value >>>= 8L;
    } 
  }
  
  private void writeBits(DataOutput header, BitSet bits, int length) throws IOException {
    int cache = 0;
    int shift = 7;
    for (int i = 0; i < length; i++) {
      cache |= (bits.get(i) ? 1 : 0) << shift;
      if (--shift < 0) {
        header.write(cache);
        shift = 7;
        cache = 0;
      } 
    } 
    if (shift != 7)
      header.write(cache); 
  }
  
  private static <T> Iterable<T> reverse(Iterable<T> i) {
    LinkedList<T> l = new LinkedList<T>();
    for (T t : i)
      l.addFirst(t); 
    return l;
  }
  
  private class OutputStreamWrapper extends OutputStream {
    private OutputStreamWrapper() {}
    
    public void write(int b) throws IOException {
      SevenZOutputFile.this.file.write(b);
      SevenZOutputFile.this.compressedCrc32.update(b);
      SevenZOutputFile.this.fileBytesWritten++;
    }
    
    public void write(byte[] b) throws IOException {
      write(b, 0, b.length);
    }
    
    public void write(byte[] b, int off, int len) throws IOException {
      SevenZOutputFile.this.file.write(b, off, len);
      SevenZOutputFile.this.compressedCrc32.update(b, off, len);
      SevenZOutputFile.this.fileBytesWritten += len;
    }
    
    public void flush() throws IOException {}
    
    public void close() throws IOException {}
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\SevenZOutputFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */