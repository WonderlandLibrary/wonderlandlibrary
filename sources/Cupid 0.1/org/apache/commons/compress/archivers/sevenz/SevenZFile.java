package org.apache.commons.compress.archivers.sevenz;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.zip.CRC32;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class SevenZFile implements Closeable {
  static final int SIGNATURE_HEADER_SIZE = 32;
  
  private RandomAccessFile file;
  
  private final Archive archive;
  
  private int currentEntryIndex = -1;
  
  private int currentFolderIndex = -1;
  
  private InputStream currentFolderInputStream = null;
  
  private InputStream currentEntryInputStream = null;
  
  private byte[] password;
  
  static final byte[] sevenZSignature = new byte[] { 55, 122, -68, -81, 39, 28 };
  
  public SevenZFile(File filename, byte[] password) throws IOException {
    boolean succeeded = false;
    this.file = new RandomAccessFile(filename, "r");
    try {
      this.archive = readHeaders(password);
      if (password != null) {
        this.password = new byte[password.length];
        System.arraycopy(password, 0, this.password, 0, password.length);
      } else {
        this.password = null;
      } 
      succeeded = true;
    } finally {
      if (!succeeded)
        this.file.close(); 
    } 
  }
  
  public SevenZFile(File filename) throws IOException {
    this(filename, null);
  }
  
  public void close() throws IOException {
    if (this.file != null)
      try {
        this.file.close();
      } finally {
        this.file = null;
        if (this.password != null)
          Arrays.fill(this.password, (byte)0); 
        this.password = null;
      }  
  }
  
  public SevenZArchiveEntry getNextEntry() throws IOException {
    if (this.currentEntryIndex >= this.archive.files.length - 1)
      return null; 
    this.currentEntryIndex++;
    SevenZArchiveEntry entry = this.archive.files[this.currentEntryIndex];
    buildDecodingStream();
    return entry;
  }
  
  private Archive readHeaders(byte[] password) throws IOException {
    byte[] signature = new byte[6];
    this.file.readFully(signature);
    if (!Arrays.equals(signature, sevenZSignature))
      throw new IOException("Bad 7z signature"); 
    byte archiveVersionMajor = this.file.readByte();
    byte archiveVersionMinor = this.file.readByte();
    if (archiveVersionMajor != 0)
      throw new IOException(String.format("Unsupported 7z version (%d,%d)", new Object[] { Byte.valueOf(archiveVersionMajor), Byte.valueOf(archiveVersionMinor) })); 
    long startHeaderCrc = 0xFFFFFFFFL & Integer.reverseBytes(this.file.readInt());
    StartHeader startHeader = readStartHeader(startHeaderCrc);
    int nextHeaderSizeInt = (int)startHeader.nextHeaderSize;
    if (nextHeaderSizeInt != startHeader.nextHeaderSize)
      throw new IOException("cannot handle nextHeaderSize " + startHeader.nextHeaderSize); 
    this.file.seek(32L + startHeader.nextHeaderOffset);
    byte[] nextHeader = new byte[nextHeaderSizeInt];
    this.file.readFully(nextHeader);
    CRC32 crc = new CRC32();
    crc.update(nextHeader);
    if (startHeader.nextHeaderCrc != crc.getValue())
      throw new IOException("NextHeader CRC mismatch"); 
    ByteArrayInputStream byteStream = new ByteArrayInputStream(nextHeader);
    DataInputStream nextHeaderInputStream = new DataInputStream(byteStream);
    Archive archive = new Archive();
    int nid = nextHeaderInputStream.readUnsignedByte();
    if (nid == 23) {
      nextHeaderInputStream = readEncodedHeader(nextHeaderInputStream, archive, password);
      archive = new Archive();
      nid = nextHeaderInputStream.readUnsignedByte();
    } 
    if (nid == 1) {
      readHeader(nextHeaderInputStream, archive);
      nextHeaderInputStream.close();
    } else {
      throw new IOException("Broken or unsupported archive: no Header");
    } 
    return archive;
  }
  
  private StartHeader readStartHeader(long startHeaderCrc) throws IOException {
    StartHeader startHeader = new StartHeader();
    DataInputStream dataInputStream = null;
    try {
      dataInputStream = new DataInputStream((InputStream)new CRC32VerifyingInputStream(new BoundedRandomAccessFileInputStream(this.file, 20L), 20L, startHeaderCrc));
      startHeader.nextHeaderOffset = Long.reverseBytes(dataInputStream.readLong());
      startHeader.nextHeaderSize = Long.reverseBytes(dataInputStream.readLong());
      startHeader.nextHeaderCrc = 0xFFFFFFFFL & Integer.reverseBytes(dataInputStream.readInt());
      return startHeader;
    } finally {
      if (dataInputStream != null)
        dataInputStream.close(); 
    } 
  }
  
  private void readHeader(DataInput header, Archive archive) throws IOException {
    int nid = header.readUnsignedByte();
    if (nid == 2) {
      readArchiveProperties(header);
      nid = header.readUnsignedByte();
    } 
    if (nid == 3)
      throw new IOException("Additional streams unsupported"); 
    if (nid == 4) {
      readStreamsInfo(header, archive);
      nid = header.readUnsignedByte();
    } 
    if (nid == 5) {
      readFilesInfo(header, archive);
      nid = header.readUnsignedByte();
    } 
    if (nid != 0)
      throw new IOException("Badly terminated header"); 
  }
  
  private void readArchiveProperties(DataInput input) throws IOException {
    int nid = input.readUnsignedByte();
    while (nid != 0) {
      long propertySize = readUint64(input);
      byte[] property = new byte[(int)propertySize];
      input.readFully(property);
      nid = input.readUnsignedByte();
    } 
  }
  
  private DataInputStream readEncodedHeader(DataInputStream header, Archive archive, byte[] password) throws IOException {
    CRC32VerifyingInputStream cRC32VerifyingInputStream;
    readStreamsInfo(header, archive);
    Folder folder = archive.folders[0];
    int firstPackStreamIndex = 0;
    long folderOffset = 32L + archive.packPos + 0L;
    this.file.seek(folderOffset);
    InputStream inputStreamStack = new BoundedRandomAccessFileInputStream(this.file, archive.packSizes[0]);
    for (Coder coder : folder.getOrderedCoders()) {
      if (coder.numInStreams != 1L || coder.numOutStreams != 1L)
        throw new IOException("Multi input/output stream coders are not yet supported"); 
      inputStreamStack = Coders.addDecoder(inputStreamStack, coder, password);
    } 
    if (folder.hasCrc)
      cRC32VerifyingInputStream = new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc); 
    byte[] nextHeader = new byte[(int)folder.getUnpackSize()];
    DataInputStream nextHeaderInputStream = new DataInputStream((InputStream)cRC32VerifyingInputStream);
    try {
      nextHeaderInputStream.readFully(nextHeader);
    } finally {
      nextHeaderInputStream.close();
    } 
    return new DataInputStream(new ByteArrayInputStream(nextHeader));
  }
  
  private void readStreamsInfo(DataInput header, Archive archive) throws IOException {
    int nid = header.readUnsignedByte();
    if (nid == 6) {
      readPackInfo(header, archive);
      nid = header.readUnsignedByte();
    } 
    if (nid == 7) {
      readUnpackInfo(header, archive);
      nid = header.readUnsignedByte();
    } else {
      archive.folders = new Folder[0];
    } 
    if (nid == 8) {
      readSubStreamsInfo(header, archive);
      nid = header.readUnsignedByte();
    } 
    if (nid != 0)
      throw new IOException("Badly terminated StreamsInfo"); 
  }
  
  private void readPackInfo(DataInput header, Archive archive) throws IOException {
    archive.packPos = readUint64(header);
    long numPackStreams = readUint64(header);
    int nid = header.readUnsignedByte();
    if (nid == 9) {
      archive.packSizes = new long[(int)numPackStreams];
      for (int i = 0; i < archive.packSizes.length; i++)
        archive.packSizes[i] = readUint64(header); 
      nid = header.readUnsignedByte();
    } 
    if (nid == 10) {
      archive.packCrcsDefined = readAllOrBits(header, (int)numPackStreams);
      archive.packCrcs = new long[(int)numPackStreams];
      for (int i = 0; i < (int)numPackStreams; i++) {
        if (archive.packCrcsDefined.get(i))
          archive.packCrcs[i] = 0xFFFFFFFFL & Integer.reverseBytes(header.readInt()); 
      } 
      nid = header.readUnsignedByte();
    } 
    if (nid != 0)
      throw new IOException("Badly terminated PackInfo (" + nid + ")"); 
  }
  
  private void readUnpackInfo(DataInput header, Archive archive) throws IOException {
    int nid = header.readUnsignedByte();
    if (nid != 11)
      throw new IOException("Expected kFolder, got " + nid); 
    long numFolders = readUint64(header);
    Folder[] folders = new Folder[(int)numFolders];
    archive.folders = folders;
    int external = header.readUnsignedByte();
    if (external != 0)
      throw new IOException("External unsupported"); 
    for (int i = 0; i < (int)numFolders; i++)
      folders[i] = readFolder(header); 
    nid = header.readUnsignedByte();
    if (nid != 12)
      throw new IOException("Expected kCodersUnpackSize, got " + nid); 
    for (Folder folder : folders) {
      folder.unpackSizes = new long[(int)folder.totalOutputStreams];
      for (int j = 0; j < folder.totalOutputStreams; j++)
        folder.unpackSizes[j] = readUint64(header); 
    } 
    nid = header.readUnsignedByte();
    if (nid == 10) {
      BitSet crcsDefined = readAllOrBits(header, (int)numFolders);
      for (int j = 0; j < (int)numFolders; j++) {
        if (crcsDefined.get(j)) {
          (folders[j]).hasCrc = true;
          (folders[j]).crc = 0xFFFFFFFFL & Integer.reverseBytes(header.readInt());
        } else {
          (folders[j]).hasCrc = false;
        } 
      } 
      nid = header.readUnsignedByte();
    } 
    if (nid != 0)
      throw new IOException("Badly terminated UnpackInfo"); 
  }
  
  private void readSubStreamsInfo(DataInput header, Archive archive) throws IOException {
    for (Folder folder : archive.folders)
      folder.numUnpackSubStreams = 1; 
    int totalUnpackStreams = archive.folders.length;
    int nid = header.readUnsignedByte();
    if (nid == 13) {
      totalUnpackStreams = 0;
      for (Folder folder : archive.folders) {
        long numStreams = readUint64(header);
        folder.numUnpackSubStreams = (int)numStreams;
        totalUnpackStreams = (int)(totalUnpackStreams + numStreams);
      } 
      nid = header.readUnsignedByte();
    } 
    SubStreamsInfo subStreamsInfo = new SubStreamsInfo();
    subStreamsInfo.unpackSizes = new long[totalUnpackStreams];
    subStreamsInfo.hasCrc = new BitSet(totalUnpackStreams);
    subStreamsInfo.crcs = new long[totalUnpackStreams];
    int nextUnpackStream = 0;
    for (Folder folder : archive.folders) {
      if (folder.numUnpackSubStreams != 0) {
        long sum = 0L;
        if (nid == 9)
          for (int i = 0; i < folder.numUnpackSubStreams - 1; i++) {
            long size = readUint64(header);
            subStreamsInfo.unpackSizes[nextUnpackStream++] = size;
            sum += size;
          }  
        subStreamsInfo.unpackSizes[nextUnpackStream++] = folder.getUnpackSize() - sum;
      } 
    } 
    if (nid == 9)
      nid = header.readUnsignedByte(); 
    int numDigests = 0;
    for (Folder folder : archive.folders) {
      if (folder.numUnpackSubStreams != 1 || !folder.hasCrc)
        numDigests += folder.numUnpackSubStreams; 
    } 
    if (nid == 10) {
      BitSet hasMissingCrc = readAllOrBits(header, numDigests);
      long[] missingCrcs = new long[numDigests];
      for (int i = 0; i < numDigests; i++) {
        if (hasMissingCrc.get(i))
          missingCrcs[i] = 0xFFFFFFFFL & Integer.reverseBytes(header.readInt()); 
      } 
      int nextCrc = 0;
      int nextMissingCrc = 0;
      for (Folder folder : archive.folders) {
        if (folder.numUnpackSubStreams == 1 && folder.hasCrc) {
          subStreamsInfo.hasCrc.set(nextCrc, true);
          subStreamsInfo.crcs[nextCrc] = folder.crc;
          nextCrc++;
        } else {
          for (int j = 0; j < folder.numUnpackSubStreams; j++) {
            subStreamsInfo.hasCrc.set(nextCrc, hasMissingCrc.get(nextMissingCrc));
            subStreamsInfo.crcs[nextCrc] = missingCrcs[nextMissingCrc];
            nextCrc++;
            nextMissingCrc++;
          } 
        } 
      } 
      nid = header.readUnsignedByte();
    } 
    if (nid != 0)
      throw new IOException("Badly terminated SubStreamsInfo"); 
    archive.subStreamsInfo = subStreamsInfo;
  }
  
  private Folder readFolder(DataInput header) throws IOException {
    Folder folder = new Folder();
    long numCoders = readUint64(header);
    Coder[] coders = new Coder[(int)numCoders];
    long totalInStreams = 0L;
    long totalOutStreams = 0L;
    for (int i = 0; i < coders.length; i++) {
      coders[i] = new Coder();
      int bits = header.readUnsignedByte();
      int idSize = bits & 0xF;
      boolean isSimple = ((bits & 0x10) == 0);
      boolean hasAttributes = ((bits & 0x20) != 0);
      boolean moreAlternativeMethods = ((bits & 0x80) != 0);
      (coders[i]).decompressionMethodId = new byte[idSize];
      header.readFully((coders[i]).decompressionMethodId);
      if (isSimple) {
        (coders[i]).numInStreams = 1L;
        (coders[i]).numOutStreams = 1L;
      } else {
        (coders[i]).numInStreams = readUint64(header);
        (coders[i]).numOutStreams = readUint64(header);
      } 
      totalInStreams += (coders[i]).numInStreams;
      totalOutStreams += (coders[i]).numOutStreams;
      if (hasAttributes) {
        long propertiesSize = readUint64(header);
        (coders[i]).properties = new byte[(int)propertiesSize];
        header.readFully((coders[i]).properties);
      } 
      if (moreAlternativeMethods)
        throw new IOException("Alternative methods are unsupported, please report. The reference implementation doesn't support them either."); 
    } 
    folder.coders = coders;
    folder.totalInputStreams = totalInStreams;
    folder.totalOutputStreams = totalOutStreams;
    if (totalOutStreams == 0L)
      throw new IOException("Total output streams can't be 0"); 
    long numBindPairs = totalOutStreams - 1L;
    BindPair[] bindPairs = new BindPair[(int)numBindPairs];
    for (int j = 0; j < bindPairs.length; j++) {
      bindPairs[j] = new BindPair();
      (bindPairs[j]).inIndex = readUint64(header);
      (bindPairs[j]).outIndex = readUint64(header);
    } 
    folder.bindPairs = bindPairs;
    if (totalInStreams < numBindPairs)
      throw new IOException("Total input streams can't be less than the number of bind pairs"); 
    long numPackedStreams = totalInStreams - numBindPairs;
    long[] packedStreams = new long[(int)numPackedStreams];
    if (numPackedStreams == 1L) {
      int k;
      for (k = 0; k < (int)totalInStreams && 
        folder.findBindPairForInStream(k) >= 0; k++);
      if (k == (int)totalInStreams)
        throw new IOException("Couldn't find stream's bind pair index"); 
      packedStreams[0] = k;
    } else {
      for (int k = 0; k < (int)numPackedStreams; k++)
        packedStreams[k] = readUint64(header); 
    } 
    folder.packedStreams = packedStreams;
    return folder;
  }
  
  private BitSet readAllOrBits(DataInput header, int size) throws IOException {
    BitSet bits;
    int areAllDefined = header.readUnsignedByte();
    if (areAllDefined != 0) {
      bits = new BitSet(size);
      for (int i = 0; i < size; i++)
        bits.set(i, true); 
    } else {
      bits = readBits(header, size);
    } 
    return bits;
  }
  
  private BitSet readBits(DataInput header, int size) throws IOException {
    BitSet bits = new BitSet(size);
    int mask = 0;
    int cache = 0;
    for (int i = 0; i < size; i++) {
      if (mask == 0) {
        mask = 128;
        cache = header.readUnsignedByte();
      } 
      bits.set(i, ((cache & mask) != 0));
      mask >>>= 1;
    } 
    return bits;
  }
  
  private void readFilesInfo(DataInput header, Archive archive) throws IOException {
    long numFiles = readUint64(header);
    SevenZArchiveEntry[] files = new SevenZArchiveEntry[(int)numFiles];
    for (int i = 0; i < files.length; i++)
      files[i] = new SevenZArchiveEntry(); 
    BitSet isEmptyStream = null;
    BitSet isEmptyFile = null;
    BitSet isAnti = null;
    while (true) {
      int external;
      BitSet timesDefined, attributesDefined;
      byte[] names;
      int k, nextFile, m, nextName, n, propertyType = header.readUnsignedByte();
      if (propertyType == 0)
        break; 
      long size = readUint64(header);
      switch (propertyType) {
        case 14:
          isEmptyStream = readBits(header, files.length);
          continue;
        case 15:
          if (isEmptyStream == null)
            throw new IOException("Header format error: kEmptyStream must appear before kEmptyFile"); 
          isEmptyFile = readBits(header, isEmptyStream.cardinality());
          continue;
        case 16:
          if (isEmptyStream == null)
            throw new IOException("Header format error: kEmptyStream must appear before kAnti"); 
          isAnti = readBits(header, isEmptyStream.cardinality());
          continue;
        case 17:
          external = header.readUnsignedByte();
          if (external != 0)
            throw new IOException("Not implemented"); 
          if ((size - 1L & 0x1L) != 0L)
            throw new IOException("File names length invalid"); 
          names = new byte[(int)(size - 1L)];
          header.readFully(names);
          nextFile = 0;
          nextName = 0;
          for (n = 0; n < names.length; n += 2) {
            if (names[n] == 0 && names[n + 1] == 0) {
              files[nextFile++].setName(new String(names, nextName, n - nextName, "UTF-16LE"));
              nextName = n + 2;
            } 
          } 
          if (nextName != names.length || nextFile != files.length)
            throw new IOException("Error parsing file names"); 
          continue;
        case 18:
          timesDefined = readAllOrBits(header, files.length);
          k = header.readUnsignedByte();
          if (k != 0)
            throw new IOException("Unimplemented"); 
          for (m = 0; m < files.length; m++) {
            files[m].setHasCreationDate(timesDefined.get(m));
            if (files[m].getHasCreationDate())
              files[m].setCreationDate(Long.reverseBytes(header.readLong())); 
          } 
          continue;
        case 19:
          timesDefined = readAllOrBits(header, files.length);
          k = header.readUnsignedByte();
          if (k != 0)
            throw new IOException("Unimplemented"); 
          for (m = 0; m < files.length; m++) {
            files[m].setHasAccessDate(timesDefined.get(m));
            if (files[m].getHasAccessDate())
              files[m].setAccessDate(Long.reverseBytes(header.readLong())); 
          } 
          continue;
        case 20:
          timesDefined = readAllOrBits(header, files.length);
          k = header.readUnsignedByte();
          if (k != 0)
            throw new IOException("Unimplemented"); 
          for (m = 0; m < files.length; m++) {
            files[m].setHasLastModifiedDate(timesDefined.get(m));
            if (files[m].getHasLastModifiedDate())
              files[m].setLastModifiedDate(Long.reverseBytes(header.readLong())); 
          } 
          continue;
        case 21:
          attributesDefined = readAllOrBits(header, files.length);
          k = header.readUnsignedByte();
          if (k != 0)
            throw new IOException("Unimplemented"); 
          for (m = 0; m < files.length; m++) {
            files[m].setHasWindowsAttributes(attributesDefined.get(m));
            if (files[m].getHasWindowsAttributes())
              files[m].setWindowsAttributes(Integer.reverseBytes(header.readInt())); 
          } 
          continue;
        case 24:
          throw new IOException("kStartPos is unsupported, please report");
        case 25:
          throw new IOException("kDummy is unsupported, please report");
      } 
      throw new IOException("Unknown property " + propertyType);
    } 
    int nonEmptyFileCounter = 0;
    int emptyFileCounter = 0;
    for (int j = 0; j < files.length; j++) {
      files[j].setHasStream((isEmptyStream == null) ? true : (!isEmptyStream.get(j)));
      if (files[j].hasStream()) {
        files[j].setDirectory(false);
        files[j].setAntiItem(false);
        files[j].setHasCrc(archive.subStreamsInfo.hasCrc.get(nonEmptyFileCounter));
        files[j].setCrcValue(archive.subStreamsInfo.crcs[nonEmptyFileCounter]);
        files[j].setSize(archive.subStreamsInfo.unpackSizes[nonEmptyFileCounter]);
        nonEmptyFileCounter++;
      } else {
        files[j].setDirectory((isEmptyFile == null) ? true : (!isEmptyFile.get(emptyFileCounter)));
        files[j].setAntiItem((isAnti == null) ? false : isAnti.get(emptyFileCounter));
        files[j].setHasCrc(false);
        files[j].setSize(0L);
        emptyFileCounter++;
      } 
    } 
    archive.files = files;
    calculateStreamMap(archive);
  }
  
  private void calculateStreamMap(Archive archive) throws IOException {
    StreamMap streamMap = new StreamMap();
    int nextFolderPackStreamIndex = 0;
    int numFolders = (archive.folders != null) ? archive.folders.length : 0;
    streamMap.folderFirstPackStreamIndex = new int[numFolders];
    for (int i = 0; i < numFolders; i++) {
      streamMap.folderFirstPackStreamIndex[i] = nextFolderPackStreamIndex;
      nextFolderPackStreamIndex += (archive.folders[i]).packedStreams.length;
    } 
    long nextPackStreamOffset = 0L;
    int numPackSizes = (archive.packSizes != null) ? archive.packSizes.length : 0;
    streamMap.packStreamOffsets = new long[numPackSizes];
    for (int j = 0; j < numPackSizes; j++) {
      streamMap.packStreamOffsets[j] = nextPackStreamOffset;
      nextPackStreamOffset += archive.packSizes[j];
    } 
    streamMap.folderFirstFileIndex = new int[numFolders];
    streamMap.fileFolderIndex = new int[archive.files.length];
    int nextFolderIndex = 0;
    int nextFolderUnpackStreamIndex = 0;
    for (int k = 0; k < archive.files.length; k++) {
      if (!archive.files[k].hasStream() && nextFolderUnpackStreamIndex == 0) {
        streamMap.fileFolderIndex[k] = -1;
      } else {
        if (nextFolderUnpackStreamIndex == 0) {
          for (; nextFolderIndex < archive.folders.length; nextFolderIndex++) {
            streamMap.folderFirstFileIndex[nextFolderIndex] = k;
            if ((archive.folders[nextFolderIndex]).numUnpackSubStreams > 0)
              break; 
          } 
          if (nextFolderIndex >= archive.folders.length)
            throw new IOException("Too few folders in archive"); 
        } 
        streamMap.fileFolderIndex[k] = nextFolderIndex;
        if (archive.files[k].hasStream()) {
          nextFolderUnpackStreamIndex++;
          if (nextFolderUnpackStreamIndex >= (archive.folders[nextFolderIndex]).numUnpackSubStreams) {
            nextFolderIndex++;
            nextFolderUnpackStreamIndex = 0;
          } 
        } 
      } 
    } 
    archive.streamMap = streamMap;
  }
  
  private void buildDecodingStream() throws IOException {
    int folderIndex = this.archive.streamMap.fileFolderIndex[this.currentEntryIndex];
    if (folderIndex < 0) {
      this.currentEntryInputStream = (InputStream)new BoundedInputStream(new ByteArrayInputStream(new byte[0]), 0L);
      return;
    } 
    SevenZArchiveEntry file = this.archive.files[this.currentEntryIndex];
    if (this.currentFolderIndex == folderIndex) {
      drainPreviousEntry();
      file.setContentMethods(this.archive.files[this.currentEntryIndex - 1].getContentMethods());
    } else {
      this.currentFolderIndex = folderIndex;
      if (this.currentFolderInputStream != null) {
        this.currentFolderInputStream.close();
        this.currentFolderInputStream = null;
      } 
      Folder folder = this.archive.folders[folderIndex];
      int firstPackStreamIndex = this.archive.streamMap.folderFirstPackStreamIndex[folderIndex];
      long folderOffset = 32L + this.archive.packPos + this.archive.streamMap.packStreamOffsets[firstPackStreamIndex];
      this.currentFolderInputStream = buildDecoderStack(folder, folderOffset, firstPackStreamIndex, file);
    } 
    BoundedInputStream boundedInputStream = new BoundedInputStream(this.currentFolderInputStream, file.getSize());
    if (file.getHasCrc()) {
      this.currentEntryInputStream = (InputStream)new CRC32VerifyingInputStream((InputStream)boundedInputStream, file.getSize(), file.getCrcValue());
    } else {
      this.currentEntryInputStream = (InputStream)boundedInputStream;
    } 
  }
  
  private void drainPreviousEntry() throws IOException {
    if (this.currentEntryInputStream != null) {
      IOUtils.skip(this.currentEntryInputStream, Long.MAX_VALUE);
      this.currentEntryInputStream.close();
      this.currentEntryInputStream = null;
    } 
  }
  
  private InputStream buildDecoderStack(Folder folder, long folderOffset, int firstPackStreamIndex, SevenZArchiveEntry entry) throws IOException {
    this.file.seek(folderOffset);
    InputStream inputStreamStack = new BoundedRandomAccessFileInputStream(this.file, this.archive.packSizes[firstPackStreamIndex]);
    LinkedList<SevenZMethodConfiguration> methods = new LinkedList<SevenZMethodConfiguration>();
    for (Coder coder : folder.getOrderedCoders()) {
      if (coder.numInStreams != 1L || coder.numOutStreams != 1L)
        throw new IOException("Multi input/output stream coders are not yet supported"); 
      SevenZMethod method = SevenZMethod.byId(coder.decompressionMethodId);
      inputStreamStack = Coders.addDecoder(inputStreamStack, coder, this.password);
      methods.addFirst(new SevenZMethodConfiguration(method, Coders.findByMethod(method).getOptionsFromCoder(coder, inputStreamStack)));
    } 
    entry.setContentMethods(methods);
    if (folder.hasCrc)
      return (InputStream)new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc); 
    return inputStreamStack;
  }
  
  public int read() throws IOException {
    if (this.currentEntryInputStream == null)
      throw new IllegalStateException("No current 7z entry"); 
    return this.currentEntryInputStream.read();
  }
  
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    if (this.currentEntryInputStream == null)
      throw new IllegalStateException("No current 7z entry"); 
    return this.currentEntryInputStream.read(b, off, len);
  }
  
  private static long readUint64(DataInput in) throws IOException {
    long firstByte = in.readUnsignedByte();
    int mask = 128;
    long value = 0L;
    for (int i = 0; i < 8; i++) {
      if ((firstByte & mask) == 0L)
        return value | (firstByte & (mask - 1)) << 8 * i; 
      long nextByte = in.readUnsignedByte();
      value |= nextByte << 8 * i;
      mask >>>= 1;
    } 
    return value;
  }
  
  public static boolean matches(byte[] signature, int length) {
    if (length < sevenZSignature.length)
      return false; 
    for (int i = 0; i < sevenZSignature.length; i++) {
      if (signature[i] != sevenZSignature[i])
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\SevenZFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */