package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
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
   /** @deprecated */
   @Deprecated
   public static final int EFS_FLAG = 2048;
   private static final byte[] EMPTY = new byte[0];
   private ZipArchiveOutputStream.CurrentEntry entry;
   private String comment = "";
   private int level = -1;
   private boolean hasCompressionLevelChanged = false;
   private int method = 8;
   private final List entries = new LinkedList();
   private final CRC32 crc = new CRC32();
   private long written = 0L;
   private long cdOffset = 0L;
   private long cdLength = 0L;
   private static final byte[] ZERO = new byte[]{0, 0};
   private static final byte[] LZERO = new byte[]{0, 0, 0, 0};
   private final Map offsets = new HashMap();
   private String encoding = "UTF8";
   private ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding("UTF8");
   protected final Deflater def;
   private final byte[] buf;
   private final RandomAccessFile raf;
   private final OutputStream out;
   private boolean useUTF8Flag;
   private boolean fallbackToUTF8;
   private ZipArchiveOutputStream.UnicodeExtraFieldPolicy createUnicodeExtraFields;
   private boolean hasUsedZip64;
   private Zip64Mode zip64Mode;
   static final byte[] LFH_SIG;
   static final byte[] DD_SIG;
   static final byte[] CFH_SIG;
   static final byte[] EOCD_SIG;
   static final byte[] ZIP64_EOCD_SIG;
   static final byte[] ZIP64_EOCD_LOC_SIG;
   private static final byte[] ONE;

   public ZipArchiveOutputStream(OutputStream var1) {
      this.def = new Deflater(this.level, true);
      this.buf = new byte[512];
      this.useUTF8Flag = true;
      this.fallbackToUTF8 = false;
      this.createUnicodeExtraFields = ZipArchiveOutputStream.UnicodeExtraFieldPolicy.NEVER;
      this.hasUsedZip64 = false;
      this.zip64Mode = Zip64Mode.AsNeeded;
      this.out = var1;
      this.raf = null;
   }

   public ZipArchiveOutputStream(File var1) throws IOException {
      this.def = new Deflater(this.level, true);
      this.buf = new byte[512];
      this.useUTF8Flag = true;
      this.fallbackToUTF8 = false;
      this.createUnicodeExtraFields = ZipArchiveOutputStream.UnicodeExtraFieldPolicy.NEVER;
      this.hasUsedZip64 = false;
      this.zip64Mode = Zip64Mode.AsNeeded;
      FileOutputStream var2 = null;
      RandomAccessFile var3 = null;

      try {
         var3 = new RandomAccessFile(var1, "rw");
         var3.setLength(0L);
      } catch (IOException var5) {
         IOUtils.closeQuietly(var3);
         var3 = null;
         var2 = new FileOutputStream(var1);
      }

      this.out = var2;
      this.raf = var3;
   }

   public boolean isSeekable() {
      return this.raf != null;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
      this.zipEncoding = ZipEncodingHelper.getZipEncoding(var1);
      if (this.useUTF8Flag && !ZipEncodingHelper.isUTF8(var1)) {
         this.useUTF8Flag = false;
      }

   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setUseLanguageEncodingFlag(boolean var1) {
      this.useUTF8Flag = var1 && ZipEncodingHelper.isUTF8(this.encoding);
   }

   public void setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy var1) {
      this.createUnicodeExtraFields = var1;
   }

   public void setFallbackToUTF8(boolean var1) {
      this.fallbackToUTF8 = var1;
   }

   public void setUseZip64(Zip64Mode var1) {
      this.zip64Mode = var1;
   }

   public void finish() throws IOException {
      if (this.finished) {
         throw new IOException("This archive has already been finished");
      } else if (this.entry != null) {
         throw new IOException("This archive contains unclosed entries.");
      } else {
         this.cdOffset = this.written;
         Iterator var1 = this.entries.iterator();

         while(var1.hasNext()) {
            ZipArchiveEntry var2 = (ZipArchiveEntry)var1.next();
            this.writeCentralFileHeader(var2);
         }

         this.cdLength = this.written - this.cdOffset;
         this.writeZip64CentralDirectory();
         this.writeCentralDirectoryEnd();
         this.offsets.clear();
         this.entries.clear();
         this.def.end();
         this.finished = true;
      }
   }

   public void closeArchiveEntry() throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else if (this.entry == null) {
         throw new IOException("No current entry to close");
      } else {
         if (!ZipArchiveOutputStream.CurrentEntry.access$000(this.entry)) {
            this.write(EMPTY, 0, 0);
         }

         this.flushDeflater();
         Zip64Mode var1 = this.getEffectiveZip64Mode(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
         long var2 = this.written - ZipArchiveOutputStream.CurrentEntry.access$200(this.entry);
         long var4 = this.crc.getValue();
         this.crc.reset();
         boolean var6 = this.handleSizesAndCrc(var2, var4, var1);
         if (this.raf != null) {
            this.rewriteSizesAndCrc(var6);
         }

         this.writeDataDescriptor(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
         this.entry = null;
      }
   }

   private void flushDeflater() throws IOException {
      if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getMethod() == 8) {
         this.def.finish();

         while(!this.def.finished()) {
            this.deflate();
         }
      }

   }

   private boolean handleSizesAndCrc(long var1, long var3, Zip64Mode var5) throws ZipException {
      if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getMethod() == 8) {
         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setSize(ZipArchiveOutputStream.CurrentEntry.access$300(this.entry));
         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setCompressedSize(var1);
         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setCrc(var3);
         this.def.reset();
      } else if (this.raf == null) {
         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCrc() != var3) {
            throw new ZipException("bad CRC checksum for entry " + ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getName() + ": " + Long.toHexString(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCrc()) + " instead of " + Long.toHexString(var3));
         }

         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize() != var1) {
            throw new ZipException("bad size for entry " + ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getName() + ": " + ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize() + " instead of " + var1);
         }
      } else {
         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setSize(var1);
         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setCompressedSize(var1);
         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setCrc(var3);
      }

      boolean var6 = var5 == Zip64Mode.Always || ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize() >= 4294967295L || ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCompressedSize() >= 4294967295L;
      if (var6 && var5 == Zip64Mode.Never) {
         throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry)));
      } else {
         return var6;
      }
   }

   private void rewriteSizesAndCrc(boolean var1) throws IOException {
      long var2 = this.raf.getFilePointer();
      this.raf.seek(ZipArchiveOutputStream.CurrentEntry.access$400(this.entry));
      this.writeOut(ZipLong.getBytes(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCrc()));
      if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry) != null && var1) {
         this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
         this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
      } else {
         this.writeOut(ZipLong.getBytes(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCompressedSize()));
         this.writeOut(ZipLong.getBytes(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize()));
      }

      if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry) != null) {
         this.raf.seek(ZipArchiveOutputStream.CurrentEntry.access$400(this.entry) + 12L + 4L + (long)this.getName(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry)).limit() + 4L);
         this.writeOut(ZipEightByteInteger.getBytes(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize()));
         this.writeOut(ZipEightByteInteger.getBytes(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCompressedSize()));
         if (!var1) {
            this.raf.seek(ZipArchiveOutputStream.CurrentEntry.access$400(this.entry) - 10L);
            this.writeOut(ZipShort.getBytes(10));
            ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
            ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setExtra();
            if (ZipArchiveOutputStream.CurrentEntry.access$500(this.entry)) {
               this.hasUsedZip64 = false;
            }
         }
      }

      this.raf.seek(var2);
   }

   public void putArchiveEntry(ArchiveEntry var1) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         if (this.entry != null) {
            this.closeArchiveEntry();
         }

         this.entry = new ZipArchiveOutputStream.CurrentEntry((ZipArchiveEntry)var1);
         this.entries.add(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
         this.setDefaults(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
         Zip64Mode var2 = this.getEffectiveZip64Mode(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
         this.validateSizeInformation(var2);
         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry) != var2) {
            Zip64ExtendedInformationExtraField var3 = this.getZip64Extra(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
            ZipEightByteInteger var4 = ZipEightByteInteger.ZERO;
            if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getMethod() == 0 && ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize() != -1L) {
               var4 = new ZipEightByteInteger(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize());
            }

            var3.setSize(var4);
            var3.setCompressedSize(var4);
            ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setExtra();
         }

         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getMethod() == 8 && this.hasCompressionLevelChanged) {
            this.def.setLevel(this.level);
            this.hasCompressionLevelChanged = false;
         }

         this.writeLocalFileHeader(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
      }
   }

   private void setDefaults(ZipArchiveEntry var1) {
      if (var1.getMethod() == -1) {
         var1.setMethod(this.method);
      }

      if (var1.getTime() == -1L) {
         var1.setTime(System.currentTimeMillis());
      }

   }

   private void validateSizeInformation(Zip64Mode var1) throws ZipException {
      if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getMethod() == 0 && this.raf == null) {
         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize() == -1L) {
            throw new ZipException("uncompressed size is required for STORED method when not writing to a file");
         }

         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCrc() == -1L) {
            throw new ZipException("crc checksum is required for STORED method when not writing to a file");
         }

         ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).setCompressedSize(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize());
      }

      if ((ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getSize() >= 4294967295L || ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getCompressedSize() >= 4294967295L) && var1 == Zip64Mode.Never) {
         throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry)));
      }
   }

   public void setComment(String var1) {
      this.comment = var1;
   }

   public void setLevel(int var1) {
      if (var1 >= -1 && var1 <= 9) {
         this.hasCompressionLevelChanged = this.level != var1;
         this.level = var1;
      } else {
         throw new IllegalArgumentException("Invalid compression level: " + var1);
      }
   }

   public void setMethod(int var1) {
      this.method = var1;
   }

   public boolean canWriteEntryData(ArchiveEntry var1) {
      if (!(var1 instanceof ZipArchiveEntry)) {
         return false;
      } else {
         ZipArchiveEntry var2 = (ZipArchiveEntry)var1;
         return var2.getMethod() != ZipMethod.IMPLODING.getCode() && var2.getMethod() != ZipMethod.UNSHRINKING.getCode() && ZipUtil.canHandleEntryData(var2);
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.entry == null) {
         throw new IllegalStateException("No current entry");
      } else {
         ZipUtil.checkRequestedFeatures(ZipArchiveOutputStream.CurrentEntry.access$100(this.entry));
         ZipArchiveOutputStream.CurrentEntry.access$002(this.entry, true);
         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry).getMethod() == 8) {
            this.writeDeflated(var1, var2, var3);
         } else {
            this.writeOut(var1, var2, var3);
            this.written += (long)var3;
         }

         this.crc.update(var1, var2, var3);
         this.count(var3);
      }
   }

   private void writeDeflated(byte[] var1, int var2, int var3) throws IOException {
      if (var3 > 0 && !this.def.finished()) {
         ZipArchiveOutputStream.CurrentEntry.access$314(this.entry, (long)var3);
         if (var3 <= 8192) {
            this.def.setInput(var1, var2, var3);
            this.deflateUntilInputIsNeeded();
         } else {
            int var4 = var3 / 8192;

            int var5;
            for(var5 = 0; var5 < var4; ++var5) {
               this.def.setInput(var1, var2 + var5 * 8192, 8192);
               this.deflateUntilInputIsNeeded();
            }

            var5 = var4 * 8192;
            if (var5 < var3) {
               this.def.setInput(var1, var2 + var5, var3 - var5);
               this.deflateUntilInputIsNeeded();
            }
         }
      }

   }

   public void close() throws IOException {
      if (!this.finished) {
         this.finish();
      }

      this.destroy();
   }

   public void flush() throws IOException {
      if (this.out != null) {
         this.out.flush();
      }

   }

   protected final void deflate() throws IOException {
      int var1 = this.def.deflate(this.buf, 0, this.buf.length);
      if (var1 > 0) {
         this.writeOut(this.buf, 0, var1);
         this.written += (long)var1;
      }

   }

   protected void writeLocalFileHeader(ZipArchiveEntry var1) throws IOException {
      boolean var2 = this.zipEncoding.canEncode(var1.getName());
      ByteBuffer var3 = this.getName(var1);
      if (this.createUnicodeExtraFields != ZipArchiveOutputStream.UnicodeExtraFieldPolicy.NEVER) {
         this.addUnicodeExtraFields(var1, var2, var3);
      }

      this.offsets.put(var1, this.written);
      this.writeOut(LFH_SIG);
      this.written += 4L;
      int var4 = var1.getMethod();
      this.writeVersionNeededToExtractAndGeneralPurposeBits(var4, !var2 && this.fallbackToUTF8, this.hasZip64Extra(var1));
      this.written += 4L;
      this.writeOut(ZipShort.getBytes(var4));
      this.written += 2L;
      this.writeOut(ZipUtil.toDosTime(var1.getTime()));
      this.written += 4L;
      ZipArchiveOutputStream.CurrentEntry.access$402(this.entry, this.written);
      byte[] var5;
      if (var4 != 8 && this.raf == null) {
         this.writeOut(ZipLong.getBytes(var1.getCrc()));
         var5 = ZipLong.ZIP64_MAGIC.getBytes();
         if (var1 != null) {
            var5 = ZipLong.getBytes(var1.getSize());
         }

         this.writeOut(var5);
         this.writeOut(var5);
      } else {
         this.writeOut(LZERO);
         if (ZipArchiveOutputStream.CurrentEntry.access$100(this.entry) != null) {
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
         } else {
            this.writeOut(LZERO);
            this.writeOut(LZERO);
         }
      }

      this.written += 12L;
      this.writeOut(ZipShort.getBytes(var3.limit()));
      this.written += 2L;
      var5 = var1.getLocalFileDataExtra();
      this.writeOut(ZipShort.getBytes(var5.length));
      this.written += 2L;
      this.writeOut(var3.array(), var3.arrayOffset(), var3.limit() - var3.position());
      this.written += (long)var3.limit();
      this.writeOut(var5);
      this.written += (long)var5.length;
      ZipArchiveOutputStream.CurrentEntry.access$202(this.entry, this.written);
   }

   private void addUnicodeExtraFields(ZipArchiveEntry var1, boolean var2, ByteBuffer var3) throws IOException {
      if (this.createUnicodeExtraFields == ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS || !var2) {
         var1.addExtraField(new UnicodePathExtraField(var1.getName(), var3.array(), var3.arrayOffset(), var3.limit() - var3.position()));
      }

      String var4 = var1.getComment();
      if (var4 != null && !"".equals(var4)) {
         boolean var5 = this.zipEncoding.canEncode(var4);
         if (this.createUnicodeExtraFields == ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS || !var5) {
            ByteBuffer var6 = this.getEntryEncoding(var1).encode(var4);
            var1.addExtraField(new UnicodeCommentExtraField(var4, var6.array(), var6.arrayOffset(), var6.limit() - var6.position()));
         }
      }

   }

   protected void writeDataDescriptor(ZipArchiveEntry var1) throws IOException {
      if (var1.getMethod() == 8 && this.raf == null) {
         this.writeOut(DD_SIG);
         this.writeOut(ZipLong.getBytes(var1.getCrc()));
         byte var2 = 4;
         if (var1 != null) {
            this.writeOut(ZipLong.getBytes(var1.getCompressedSize()));
            this.writeOut(ZipLong.getBytes(var1.getSize()));
         } else {
            var2 = 8;
            this.writeOut(ZipEightByteInteger.getBytes(var1.getCompressedSize()));
            this.writeOut(ZipEightByteInteger.getBytes(var1.getSize()));
         }

         this.written += (long)(8 + 2 * var2);
      }
   }

   protected void writeCentralFileHeader(ZipArchiveEntry var1) throws IOException {
      this.writeOut(CFH_SIG);
      this.written += 4L;
      long var2 = (Long)this.offsets.get(var1);
      boolean var4 = var1 == null || var1.getCompressedSize() >= 4294967295L || var1.getSize() >= 4294967295L || var2 >= 4294967295L;
      if (var4 && this.zip64Mode == Zip64Mode.Never) {
         throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
      } else {
         this.handleZip64Extra(var1, var2, var4);
         this.writeOut(ZipShort.getBytes(var1.getPlatform() << 8 | (!this.hasUsedZip64 ? 20 : 45)));
         this.written += 2L;
         int var5 = var1.getMethod();
         boolean var6 = this.zipEncoding.canEncode(var1.getName());
         this.writeVersionNeededToExtractAndGeneralPurposeBits(var5, !var6 && this.fallbackToUTF8, var4);
         this.written += 4L;
         this.writeOut(ZipShort.getBytes(var5));
         this.written += 2L;
         this.writeOut(ZipUtil.toDosTime(var1.getTime()));
         this.written += 4L;
         this.writeOut(ZipLong.getBytes(var1.getCrc()));
         if (var1.getCompressedSize() < 4294967295L && var1.getSize() < 4294967295L) {
            this.writeOut(ZipLong.getBytes(var1.getCompressedSize()));
            this.writeOut(ZipLong.getBytes(var1.getSize()));
         } else {
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
            this.writeOut(ZipLong.ZIP64_MAGIC.getBytes());
         }

         this.written += 12L;
         ByteBuffer var7 = this.getName(var1);
         this.writeOut(ZipShort.getBytes(var7.limit()));
         this.written += 2L;
         byte[] var8 = var1.getCentralDirectoryExtra();
         this.writeOut(ZipShort.getBytes(var8.length));
         this.written += 2L;
         String var9 = var1.getComment();
         if (var9 == null) {
            var9 = "";
         }

         ByteBuffer var10 = this.getEntryEncoding(var1).encode(var9);
         this.writeOut(ZipShort.getBytes(var10.limit()));
         this.written += 2L;
         this.writeOut(ZERO);
         this.written += 2L;
         this.writeOut(ZipShort.getBytes(var1.getInternalAttributes()));
         this.written += 2L;
         this.writeOut(ZipLong.getBytes(var1.getExternalAttributes()));
         this.written += 4L;
         this.writeOut(ZipLong.getBytes(Math.min(var2, 4294967295L)));
         this.written += 4L;
         this.writeOut(var7.array(), var7.arrayOffset(), var7.limit() - var7.position());
         this.written += (long)var7.limit();
         this.writeOut(var8);
         this.written += (long)var8.length;
         this.writeOut(var10.array(), var10.arrayOffset(), var10.limit() - var10.position());
         this.written += (long)var10.limit();
      }
   }

   private void handleZip64Extra(ZipArchiveEntry var1, long var2, boolean var4) {
      if (var4) {
         Zip64ExtendedInformationExtraField var5 = this.getZip64Extra(var1);
         if (var1.getCompressedSize() < 4294967295L && var1.getSize() < 4294967295L) {
            var5.setCompressedSize((ZipEightByteInteger)null);
            var5.setSize((ZipEightByteInteger)null);
         } else {
            var5.setCompressedSize(new ZipEightByteInteger(var1.getCompressedSize()));
            var5.setSize(new ZipEightByteInteger(var1.getSize()));
         }

         if (var2 >= 4294967295L) {
            var5.setRelativeHeaderOffset(new ZipEightByteInteger(var2));
         }

         var1.setExtra();
      }

   }

   protected void writeCentralDirectoryEnd() throws IOException {
      this.writeOut(EOCD_SIG);
      this.writeOut(ZERO);
      this.writeOut(ZERO);
      int var1 = this.entries.size();
      if (var1 > 65535 && this.zip64Mode == Zip64Mode.Never) {
         throw new Zip64RequiredException("archive contains more than 65535 entries.");
      } else if (this.cdOffset > 4294967295L && this.zip64Mode == Zip64Mode.Never) {
         throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
      } else {
         byte[] var2 = ZipShort.getBytes(Math.min(var1, 65535));
         this.writeOut(var2);
         this.writeOut(var2);
         this.writeOut(ZipLong.getBytes(Math.min(this.cdLength, 4294967295L)));
         this.writeOut(ZipLong.getBytes(Math.min(this.cdOffset, 4294967295L)));
         ByteBuffer var3 = this.zipEncoding.encode(this.comment);
         this.writeOut(ZipShort.getBytes(var3.limit()));
         this.writeOut(var3.array(), var3.arrayOffset(), var3.limit() - var3.position());
      }
   }

   protected void writeZip64CentralDirectory() throws IOException {
      if (this.zip64Mode != Zip64Mode.Never) {
         if (!this.hasUsedZip64 && (this.cdOffset >= 4294967295L || this.cdLength >= 4294967295L || this.entries.size() >= 65535)) {
            this.hasUsedZip64 = true;
         }

         if (this.hasUsedZip64) {
            long var1 = this.written;
            this.writeOut(ZIP64_EOCD_SIG);
            this.writeOut(ZipEightByteInteger.getBytes(44L));
            this.writeOut(ZipShort.getBytes(45));
            this.writeOut(ZipShort.getBytes(45));
            this.writeOut(LZERO);
            this.writeOut(LZERO);
            byte[] var3 = ZipEightByteInteger.getBytes((long)this.entries.size());
            this.writeOut(var3);
            this.writeOut(var3);
            this.writeOut(ZipEightByteInteger.getBytes(this.cdLength));
            this.writeOut(ZipEightByteInteger.getBytes(this.cdOffset));
            this.writeOut(ZIP64_EOCD_LOC_SIG);
            this.writeOut(LZERO);
            this.writeOut(ZipEightByteInteger.getBytes(var1));
            this.writeOut(ONE);
         }
      }
   }

   protected final void writeOut(byte[] var1) throws IOException {
      this.writeOut(var1, 0, var1.length);
   }

   protected final void writeOut(byte[] var1, int var2, int var3) throws IOException {
      if (this.raf != null) {
         this.raf.write(var1, var2, var3);
      } else {
         this.out.write(var1, var2, var3);
      }

   }

   private void deflateUntilInputIsNeeded() throws IOException {
      while(!this.def.needsInput()) {
         this.deflate();
      }

   }

   private void writeVersionNeededToExtractAndGeneralPurposeBits(int var1, boolean var2, boolean var3) throws IOException {
      byte var4 = 10;
      GeneralPurposeBit var5 = new GeneralPurposeBit();
      var5.useUTF8ForNames(this.useUTF8Flag || var2);
      if (var1 == 8 && this.raf == null) {
         var4 = 20;
         var5.useDataDescriptor(true);
      }

      if (var3) {
         var4 = 45;
      }

      this.writeOut(ZipShort.getBytes(var4));
      this.writeOut(var5.encode());
   }

   public ArchiveEntry createArchiveEntry(File var1, String var2) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         return new ZipArchiveEntry(var1, var2);
      }
   }

   private Zip64ExtendedInformationExtraField getZip64Extra(ZipArchiveEntry var1) {
      if (this.entry != null) {
         ZipArchiveOutputStream.CurrentEntry.access$502(this.entry, !this.hasUsedZip64);
      }

      this.hasUsedZip64 = true;
      Zip64ExtendedInformationExtraField var2 = (Zip64ExtendedInformationExtraField)var1.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
      if (var2 == null) {
         var2 = new Zip64ExtendedInformationExtraField();
      }

      var1.addAsFirstExtraField(var2);
      return var2;
   }

   private Zip64Mode getEffectiveZip64Mode(ZipArchiveEntry var1) {
      return this.zip64Mode == Zip64Mode.AsNeeded && this.raf == null && var1.getMethod() == 8 && var1.getSize() == -1L ? Zip64Mode.Never : this.zip64Mode;
   }

   private ZipEncoding getEntryEncoding(ZipArchiveEntry var1) {
      boolean var2 = this.zipEncoding.canEncode(var1.getName());
      return !var2 && this.fallbackToUTF8 ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
   }

   private ByteBuffer getName(ZipArchiveEntry var1) throws IOException {
      return this.getEntryEncoding(var1).encode(var1.getName());
   }

   void destroy() throws IOException {
      if (this.raf != null) {
         this.raf.close();
      }

      if (this.out != null) {
         this.out.close();
      }

   }

   static {
      LFH_SIG = ZipLong.LFH_SIG.getBytes();
      DD_SIG = ZipLong.DD_SIG.getBytes();
      CFH_SIG = ZipLong.CFH_SIG.getBytes();
      EOCD_SIG = ZipLong.getBytes(101010256L);
      ZIP64_EOCD_SIG = ZipLong.getBytes(101075792L);
      ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(117853008L);
      ONE = ZipLong.getBytes(1L);
   }

   private static final class CurrentEntry {
      private final ZipArchiveEntry entry;
      private long localDataStart;
      private long dataStart;
      private long bytesRead;
      private boolean causedUseOfZip64;
      private boolean hasWritten;

      private CurrentEntry(ZipArchiveEntry var1) {
         this.localDataStart = 0L;
         this.dataStart = 0L;
         this.bytesRead = 0L;
         this.causedUseOfZip64 = false;
         this.entry = var1;
      }

      static boolean access$000(ZipArchiveOutputStream.CurrentEntry var0) {
         return var0.hasWritten;
      }

      static ZipArchiveEntry access$100(ZipArchiveOutputStream.CurrentEntry var0) {
         return var0.entry;
      }

      static long access$200(ZipArchiveOutputStream.CurrentEntry var0) {
         return var0.dataStart;
      }

      static long access$300(ZipArchiveOutputStream.CurrentEntry var0) {
         return var0.bytesRead;
      }

      static long access$400(ZipArchiveOutputStream.CurrentEntry var0) {
         return var0.localDataStart;
      }

      static boolean access$500(ZipArchiveOutputStream.CurrentEntry var0) {
         return var0.causedUseOfZip64;
      }

      CurrentEntry(ZipArchiveEntry var1, Object var2) {
         this(var1);
      }

      static boolean access$002(ZipArchiveOutputStream.CurrentEntry var0, boolean var1) {
         return var0.hasWritten = var1;
      }

      static long access$314(ZipArchiveOutputStream.CurrentEntry var0, long var1) {
         return var0.bytesRead += var1;
      }

      static long access$402(ZipArchiveOutputStream.CurrentEntry var0, long var1) {
         return var0.localDataStart = var1;
      }

      static long access$202(ZipArchiveOutputStream.CurrentEntry var0, long var1) {
         return var0.dataStart = var1;
      }

      static boolean access$502(ZipArchiveOutputStream.CurrentEntry var0, boolean var1) {
         return var0.causedUseOfZip64 = var1;
      }
   }

   public static final class UnicodeExtraFieldPolicy {
      public static final ZipArchiveOutputStream.UnicodeExtraFieldPolicy ALWAYS = new ZipArchiveOutputStream.UnicodeExtraFieldPolicy("always");
      public static final ZipArchiveOutputStream.UnicodeExtraFieldPolicy NEVER = new ZipArchiveOutputStream.UnicodeExtraFieldPolicy("never");
      public static final ZipArchiveOutputStream.UnicodeExtraFieldPolicy NOT_ENCODEABLE = new ZipArchiveOutputStream.UnicodeExtraFieldPolicy("not encodeable");
      private final String name;

      private UnicodeExtraFieldPolicy(String var1) {
         this.name = var1;
      }

      public String toString() {
         return this.name;
      }
   }
}
