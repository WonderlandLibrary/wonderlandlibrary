package org.apache.commons.compress.archivers.zip;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;
import org.apache.commons.compress.utils.IOUtils;

public class ZipFile implements Closeable {
   private static final int HASH_SIZE = 509;
   static final int NIBLET_MASK = 15;
   static final int BYTE_SHIFT = 8;
   private static final int POS_0 = 0;
   private static final int POS_1 = 1;
   private static final int POS_2 = 2;
   private static final int POS_3 = 3;
   private final List entries;
   private final Map nameMap;
   private final String encoding;
   private final ZipEncoding zipEncoding;
   private final String archiveName;
   private final RandomAccessFile archive;
   private final boolean useUnicodeExtraFields;
   private boolean closed;
   private final byte[] DWORD_BUF;
   private final byte[] WORD_BUF;
   private final byte[] CFH_BUF;
   private final byte[] SHORT_BUF;
   private static final int CFH_LEN = 42;
   private static final long CFH_SIG;
   static final int MIN_EOCD_SIZE = 22;
   private static final int MAX_EOCD_SIZE = 65557;
   private static final int CFD_LOCATOR_OFFSET = 16;
   private static final int ZIP64_EOCDL_LENGTH = 20;
   private static final int ZIP64_EOCDL_LOCATOR_OFFSET = 8;
   private static final int ZIP64_EOCD_CFD_LOCATOR_OFFSET = 48;
   private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26L;
   private final Comparator OFFSET_COMPARATOR;

   public ZipFile(File var1) throws IOException {
      this(var1, "UTF8");
   }

   public ZipFile(String var1) throws IOException {
      this(new File(var1), "UTF8");
   }

   public ZipFile(String var1, String var2) throws IOException {
      this(new File(var1), var2, true);
   }

   public ZipFile(File var1, String var2) throws IOException {
      this(var1, var2, true);
   }

   public ZipFile(File var1, String var2, boolean var3) throws IOException {
      this.entries = new LinkedList();
      this.nameMap = new HashMap(509);
      this.DWORD_BUF = new byte[8];
      this.WORD_BUF = new byte[4];
      this.CFH_BUF = new byte[42];
      this.SHORT_BUF = new byte[2];
      this.OFFSET_COMPARATOR = new Comparator(this) {
         final ZipFile this$0;

         {
            this.this$0 = var1;
         }

         public int compare(ZipArchiveEntry var1, ZipArchiveEntry var2) {
            if (var1 == var2) {
               return 0;
            } else {
               ZipFile.Entry var3 = var1 instanceof ZipFile.Entry ? (ZipFile.Entry)var1 : null;
               ZipFile.Entry var4 = var2 instanceof ZipFile.Entry ? (ZipFile.Entry)var2 : null;
               if (var3 == null) {
                  return 1;
               } else if (var4 == null) {
                  return -1;
               } else {
                  long var5 = ZipFile.OffsetEntry.access$200(var3.getOffsetEntry()) - ZipFile.OffsetEntry.access$200(var4.getOffsetEntry());
                  return var5 == 0L ? 0 : (var5 < 0L ? -1 : 1);
               }
            }
         }

         public int compare(Object var1, Object var2) {
            return this.compare((ZipArchiveEntry)var1, (ZipArchiveEntry)var2);
         }
      };
      this.archiveName = var1.getAbsolutePath();
      this.encoding = var2;
      this.zipEncoding = ZipEncodingHelper.getZipEncoding(var2);
      this.useUnicodeExtraFields = var3;
      this.archive = new RandomAccessFile(var1, "r");
      boolean var4 = false;
      Map var5 = this.populateFromCentralDirectory();
      this.resolveLocalFileHeaderData(var5);
      var4 = true;
      if (!var4) {
         this.closed = true;
         IOUtils.closeQuietly(this.archive);
      }

   }

   public String getEncoding() {
      return this.encoding;
   }

   public void close() throws IOException {
      this.closed = true;
      this.archive.close();
   }

   public static void closeQuietly(ZipFile var0) {
      IOUtils.closeQuietly(var0);
   }

   public Enumeration getEntries() {
      return Collections.enumeration(this.entries);
   }

   public Enumeration getEntriesInPhysicalOrder() {
      ZipArchiveEntry[] var1 = (ZipArchiveEntry[])this.entries.toArray(new ZipArchiveEntry[0]);
      Arrays.sort(var1, this.OFFSET_COMPARATOR);
      return Collections.enumeration(Arrays.asList(var1));
   }

   public ZipArchiveEntry getEntry(String var1) {
      LinkedList var2 = (LinkedList)this.nameMap.get(var1);
      return var2 != null ? (ZipArchiveEntry)var2.getFirst() : null;
   }

   public Iterable getEntries(String var1) {
      List var2 = (List)this.nameMap.get(var1);
      return var2 != null ? var2 : Collections.emptyList();
   }

   public Iterable getEntriesInPhysicalOrder(String var1) {
      ZipArchiveEntry[] var2 = new ZipArchiveEntry[0];
      if (this.nameMap.containsKey(var1)) {
         var2 = (ZipArchiveEntry[])((LinkedList)this.nameMap.get(var1)).toArray(var2);
         Arrays.sort(var2, this.OFFSET_COMPARATOR);
      }

      return Arrays.asList(var2);
   }

   public boolean canReadEntryData(ZipArchiveEntry var1) {
      return ZipUtil.canHandleEntryData(var1);
   }

   public InputStream getInputStream(ZipArchiveEntry var1) throws IOException, ZipException {
      if (!(var1 instanceof ZipFile.Entry)) {
         return null;
      } else {
         ZipFile.OffsetEntry var2 = ((ZipFile.Entry)var1).getOffsetEntry();
         ZipUtil.checkRequestedFeatures(var1);
         long var3 = ZipFile.OffsetEntry.access$000(var2);
         ZipFile.BoundedInputStream var5 = new ZipFile.BoundedInputStream(this, var3, var1.getCompressedSize());
         switch(ZipMethod.getMethodByCode(var1.getMethod())) {
         case STORED:
            return var5;
         case UNSHRINKING:
            return new UnshrinkingInputStream(var5);
         case IMPLODING:
            return new ExplodingInputStream(var1.getGeneralPurposeBit().getSlidingDictionarySize(), var1.getGeneralPurposeBit().getNumberOfShannonFanoTrees(), new BufferedInputStream(var5));
         case DEFLATED:
            var5.addDummy();
            Inflater var6 = new Inflater(true);
            return new InflaterInputStream(this, var5, var6, var6) {
               final Inflater val$inflater;
               final ZipFile this$0;

               {
                  this.this$0 = var1;
                  this.val$inflater = var4;
               }

               public void close() throws IOException {
                  super.close();
                  this.val$inflater.end();
               }
            };
         default:
            throw new ZipException("Found unsupported compression method " + var1.getMethod());
         }
      }
   }

   public String getUnixSymlink(ZipArchiveEntry var1) throws IOException {
      if (var1 != null && var1.isUnixSymlink()) {
         InputStream var2 = null;
         var2 = this.getInputStream(var1);
         byte[] var3 = IOUtils.toByteArray(var2);
         String var4 = this.zipEncoding.decode(var3);
         if (var2 != null) {
            var2.close();
         }

         return var4;
      } else {
         return null;
      }
   }

   protected void finalize() throws Throwable {
      if (!this.closed) {
         System.err.println("Cleaning up unclosed ZipFile for archive " + this.archiveName);
         this.close();
      }

      super.finalize();
   }

   private Map populateFromCentralDirectory() throws IOException {
      HashMap var1 = new HashMap();
      this.positionAtCentralDirectory();
      this.archive.readFully(this.WORD_BUF);
      long var2 = ZipLong.getValue(this.WORD_BUF);
      if (var2 != CFH_SIG && this.startsWithLocalFileHeader()) {
         throw new IOException("central directory is empty, can't expand corrupt archive.");
      } else {
         while(var2 == CFH_SIG) {
            this.readCentralDirectoryEntry(var1);
            this.archive.readFully(this.WORD_BUF);
            var2 = ZipLong.getValue(this.WORD_BUF);
         }

         return var1;
      }
   }

   private void readCentralDirectoryEntry(Map var1) throws IOException {
      this.archive.readFully(this.CFH_BUF);
      byte var2 = 0;
      ZipFile.OffsetEntry var3 = new ZipFile.OffsetEntry();
      ZipFile.Entry var4 = new ZipFile.Entry(var3);
      int var5 = ZipShort.getValue(this.CFH_BUF, var2);
      int var18 = var2 + 2;
      var4.setPlatform(var5 >> 8 & 15);
      var18 += 2;
      GeneralPurposeBit var6 = GeneralPurposeBit.parse(this.CFH_BUF, var18);
      boolean var7 = var6.usesUTF8ForNames();
      ZipEncoding var8 = var7 ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
      var4.setGeneralPurposeBit(var6);
      var18 += 2;
      var4.setMethod(ZipShort.getValue(this.CFH_BUF, var18));
      var18 += 2;
      long var9 = ZipUtil.dosToJavaTime(ZipLong.getValue(this.CFH_BUF, var18));
      var4.setTime(var9);
      var18 += 4;
      var4.setCrc(ZipLong.getValue(this.CFH_BUF, var18));
      var18 += 4;
      var4.setCompressedSize(ZipLong.getValue(this.CFH_BUF, var18));
      var18 += 4;
      var4.setSize(ZipLong.getValue(this.CFH_BUF, var18));
      var18 += 4;
      int var11 = ZipShort.getValue(this.CFH_BUF, var18);
      var18 += 2;
      int var12 = ZipShort.getValue(this.CFH_BUF, var18);
      var18 += 2;
      int var13 = ZipShort.getValue(this.CFH_BUF, var18);
      var18 += 2;
      int var14 = ZipShort.getValue(this.CFH_BUF, var18);
      var18 += 2;
      var4.setInternalAttributes(ZipShort.getValue(this.CFH_BUF, var18));
      var18 += 2;
      var4.setExternalAttributes(ZipLong.getValue(this.CFH_BUF, var18));
      var18 += 4;
      byte[] var15 = new byte[var11];
      this.archive.readFully(var15);
      var4.setName(var8.decode(var15), var15);
      ZipFile.OffsetEntry.access$202(var3, ZipLong.getValue(this.CFH_BUF, var18));
      this.entries.add(var4);
      byte[] var16 = new byte[var12];
      this.archive.readFully(var16);
      var4.setCentralDirectoryExtra(var16);
      this.setSizesAndOffsetFromZip64Extra(var4, var3, var14);
      byte[] var17 = new byte[var13];
      this.archive.readFully(var17);
      var4.setComment(var8.decode(var17));
      if (!var7 && this.useUnicodeExtraFields) {
         var1.put(var4, new ZipFile.NameAndComment(var15, var17));
      }

   }

   private void setSizesAndOffsetFromZip64Extra(ZipArchiveEntry var1, ZipFile.OffsetEntry var2, int var3) throws IOException {
      Zip64ExtendedInformationExtraField var4 = (Zip64ExtendedInformationExtraField)var1.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
      if (var4 != null) {
         boolean var5 = var1.getSize() == 4294967295L;
         boolean var6 = var1.getCompressedSize() == 4294967295L;
         boolean var7 = ZipFile.OffsetEntry.access$200(var2) == 4294967295L;
         var4.reparseCentralDirectoryData(var5, var6, var7, var3 == 65535);
         if (var5) {
            var1.setSize(var4.getSize().getLongValue());
         } else if (var6) {
            var4.setSize(new ZipEightByteInteger(var1.getSize()));
         }

         if (var6) {
            var1.setCompressedSize(var4.getCompressedSize().getLongValue());
         } else if (var5) {
            var4.setCompressedSize(new ZipEightByteInteger(var1.getCompressedSize()));
         }

         if (var7) {
            ZipFile.OffsetEntry.access$202(var2, var4.getRelativeHeaderOffset().getLongValue());
         }
      }

   }

   private void positionAtCentralDirectory() throws IOException {
      this.positionAtEndOfCentralDirectoryRecord();
      boolean var1 = false;
      boolean var2 = this.archive.getFilePointer() > 20L;
      if (var2) {
         this.archive.seek(this.archive.getFilePointer() - 20L);
         this.archive.readFully(this.WORD_BUF);
         var1 = Arrays.equals(ZipArchiveOutputStream.ZIP64_EOCD_LOC_SIG, this.WORD_BUF);
      }

      if (!var1) {
         if (var2) {
            this.skipBytes(16);
         }

         this.positionAtCentralDirectory32();
      } else {
         this.positionAtCentralDirectory64();
      }

   }

   private void positionAtCentralDirectory64() throws IOException {
      this.skipBytes(4);
      this.archive.readFully(this.DWORD_BUF);
      this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
      this.archive.readFully(this.WORD_BUF);
      if (!Arrays.equals(this.WORD_BUF, ZipArchiveOutputStream.ZIP64_EOCD_SIG)) {
         throw new ZipException("archive's ZIP64 end of central directory locator is corrupt.");
      } else {
         this.skipBytes(44);
         this.archive.readFully(this.DWORD_BUF);
         this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
      }
   }

   private void positionAtCentralDirectory32() throws IOException {
      this.skipBytes(16);
      this.archive.readFully(this.WORD_BUF);
      this.archive.seek(ZipLong.getValue(this.WORD_BUF));
   }

   private void positionAtEndOfCentralDirectoryRecord() throws IOException {
      boolean var1 = this.tryToLocateSignature(22L, 65557L, ZipArchiveOutputStream.EOCD_SIG);
      if (!var1) {
         throw new ZipException("archive is not a ZIP archive");
      }
   }

   private boolean tryToLocateSignature(long var1, long var3, byte[] var5) throws IOException {
      boolean var6 = false;
      long var7 = this.archive.length() - var1;
      long var9 = Math.max(0L, this.archive.length() - var3);
      if (var7 >= 0L) {
         for(; var7 >= var9; --var7) {
            this.archive.seek(var7);
            int var11 = this.archive.read();
            if (var11 == -1) {
               break;
            }

            if (var11 == var5[0]) {
               var11 = this.archive.read();
               if (var11 == var5[1]) {
                  var11 = this.archive.read();
                  if (var11 == var5[2]) {
                     var11 = this.archive.read();
                     if (var11 == var5[3]) {
                        var6 = true;
                        break;
                     }
                  }
               }
            }
         }
      }

      if (var6) {
         this.archive.seek(var7);
      }

      return var6;
   }

   private void skipBytes(int var1) throws IOException {
      int var3;
      for(int var2 = 0; var2 < var1; var2 += var3) {
         var3 = this.archive.skipBytes(var1 - var2);
         if (var3 <= 0) {
            throw new EOFException();
         }
      }

   }

   private void resolveLocalFileHeaderData(Map var1) throws IOException {
      ZipFile.Entry var4;
      LinkedList var13;
      for(Iterator var2 = this.entries.iterator(); var2.hasNext(); var13.addLast(var4)) {
         ZipArchiveEntry var3 = (ZipArchiveEntry)var2.next();
         var4 = (ZipFile.Entry)var3;
         ZipFile.OffsetEntry var5 = var4.getOffsetEntry();
         long var6 = ZipFile.OffsetEntry.access$200(var5);
         this.archive.seek(var6 + 26L);
         this.archive.readFully(this.SHORT_BUF);
         int var8 = ZipShort.getValue(this.SHORT_BUF);
         this.archive.readFully(this.SHORT_BUF);
         int var9 = ZipShort.getValue(this.SHORT_BUF);

         int var11;
         for(int var10 = var8; var10 > 0; var10 -= var11) {
            var11 = this.archive.skipBytes(var10);
            if (var11 <= 0) {
               throw new IOException("failed to skip file name in local file header");
            }
         }

         byte[] var14 = new byte[var9];
         this.archive.readFully(var14);
         var4.setExtra(var14);
         ZipFile.OffsetEntry.access$002(var5, var6 + 26L + 2L + 2L + (long)var8 + (long)var9);
         if (var1.containsKey(var4)) {
            ZipFile.NameAndComment var12 = (ZipFile.NameAndComment)var1.get(var4);
            ZipUtil.setNameAndCommentFromExtraFields(var4, ZipFile.NameAndComment.access$400(var12), ZipFile.NameAndComment.access$500(var12));
         }

         String var15 = var4.getName();
         var13 = (LinkedList)this.nameMap.get(var15);
         if (var13 == null) {
            var13 = new LinkedList();
            this.nameMap.put(var15, var13);
         }
      }

   }

   private boolean startsWithLocalFileHeader() throws IOException {
      this.archive.seek(0L);
      this.archive.readFully(this.WORD_BUF);
      return Arrays.equals(this.WORD_BUF, ZipArchiveOutputStream.LFH_SIG);
   }

   static RandomAccessFile access$600(ZipFile var0) {
      return var0.archive;
   }

   static {
      CFH_SIG = ZipLong.getValue(ZipArchiveOutputStream.CFH_SIG);
   }

   private static class Entry extends ZipArchiveEntry {
      private final ZipFile.OffsetEntry offsetEntry;

      Entry(ZipFile.OffsetEntry var1) {
         this.offsetEntry = var1;
      }

      ZipFile.OffsetEntry getOffsetEntry() {
         return this.offsetEntry;
      }

      public int hashCode() {
         return 3 * super.hashCode() + (int)(ZipFile.OffsetEntry.access$200(this.offsetEntry) % 2147483647L);
      }

      public boolean equals(Object var1) {
         if (!super.equals(var1)) {
            return false;
         } else {
            ZipFile.Entry var2 = (ZipFile.Entry)var1;
            return ZipFile.OffsetEntry.access$200(this.offsetEntry) == ZipFile.OffsetEntry.access$200(var2.offsetEntry) && ZipFile.OffsetEntry.access$000(this.offsetEntry) == ZipFile.OffsetEntry.access$000(var2.offsetEntry);
         }
      }
   }

   private static final class NameAndComment {
      private final byte[] name;
      private final byte[] comment;

      private NameAndComment(byte[] var1, byte[] var2) {
         this.name = var1;
         this.comment = var2;
      }

      NameAndComment(byte[] var1, byte[] var2, Object var3) {
         this(var1, var2);
      }

      static byte[] access$400(ZipFile.NameAndComment var0) {
         return var0.name;
      }

      static byte[] access$500(ZipFile.NameAndComment var0) {
         return var0.comment;
      }
   }

   private class BoundedInputStream extends InputStream {
      private long remaining;
      private long loc;
      private boolean addDummyByte;
      final ZipFile this$0;

      BoundedInputStream(ZipFile var1, long var2, long var4) {
         this.this$0 = var1;
         this.addDummyByte = false;
         this.remaining = var4;
         this.loc = var2;
      }

      public int read() throws IOException {
         if (this.remaining-- <= 0L) {
            if (this.addDummyByte) {
               this.addDummyByte = false;
               return 0;
            } else {
               return -1;
            }
         } else {
            RandomAccessFile var1;
            synchronized(var1 = ZipFile.access$600(this.this$0)){}
            ZipFile.access$600(this.this$0).seek((long)(this.loc++));
            return ZipFile.access$600(this.this$0).read();
         }
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (this.remaining <= 0L) {
            if (this.addDummyByte) {
               this.addDummyByte = false;
               var1[var2] = 0;
               return 1;
            } else {
               return -1;
            }
         } else if (var3 <= 0) {
            return 0;
         } else {
            if ((long)var3 > this.remaining) {
               var3 = (int)this.remaining;
            }

            boolean var4 = true;
            RandomAccessFile var5;
            synchronized(var5 = ZipFile.access$600(this.this$0)){}
            ZipFile.access$600(this.this$0).seek(this.loc);
            int var7 = ZipFile.access$600(this.this$0).read(var1, var2, var3);
            if (var7 > 0) {
               this.loc += (long)var7;
               this.remaining -= (long)var7;
            }

            return var7;
         }
      }

      void addDummy() {
         this.addDummyByte = true;
      }
   }

   private static final class OffsetEntry {
      private long headerOffset;
      private long dataOffset;

      private OffsetEntry() {
         this.headerOffset = -1L;
         this.dataOffset = -1L;
      }

      static long access$000(ZipFile.OffsetEntry var0) {
         return var0.dataOffset;
      }

      OffsetEntry(Object var1) {
         this();
      }

      static long access$202(ZipFile.OffsetEntry var0, long var1) {
         return var0.headerOffset = var1;
      }

      static long access$200(ZipFile.OffsetEntry var0) {
         return var0.headerOffset;
      }

      static long access$002(ZipFile.OffsetEntry var0, long var1) {
         return var0.dataOffset = var1;
      }
   }
}
