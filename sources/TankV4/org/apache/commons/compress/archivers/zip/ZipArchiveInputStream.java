package org.apache.commons.compress.archivers.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ZipArchiveInputStream extends ArchiveInputStream {
   private final ZipEncoding zipEncoding;
   private final boolean useUnicodeExtraFields;
   private final InputStream in;
   private final Inflater inf;
   private final ByteBuffer buf;
   private ZipArchiveInputStream.CurrentEntry current;
   private boolean closed;
   private boolean hitCentralDirectory;
   private ByteArrayInputStream lastStoredEntry;
   private boolean allowStoredEntriesWithDataDescriptor;
   private static final int LFH_LEN = 30;
   private static final int CFH_LEN = 46;
   private static final long TWO_EXP_32 = 4294967296L;
   private final byte[] LFH_BUF;
   private final byte[] SKIP_BUF;
   private final byte[] SHORT_BUF;
   private final byte[] WORD_BUF;
   private final byte[] TWO_DWORD_BUF;
   private int entriesRead;
   private static final byte[] LFH;
   private static final byte[] CFH;
   private static final byte[] DD;

   public ZipArchiveInputStream(InputStream var1) {
      this(var1, "UTF8");
   }

   public ZipArchiveInputStream(InputStream var1, String var2) {
      this(var1, var2, true);
   }

   public ZipArchiveInputStream(InputStream var1, String var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public ZipArchiveInputStream(InputStream var1, String var2, boolean var3, boolean var4) {
      this.inf = new Inflater(true);
      this.buf = ByteBuffer.allocate(512);
      this.current = null;
      this.closed = false;
      this.hitCentralDirectory = false;
      this.lastStoredEntry = null;
      this.allowStoredEntriesWithDataDescriptor = false;
      this.LFH_BUF = new byte[30];
      this.SKIP_BUF = new byte[1024];
      this.SHORT_BUF = new byte[2];
      this.WORD_BUF = new byte[4];
      this.TWO_DWORD_BUF = new byte[16];
      this.entriesRead = 0;
      this.zipEncoding = ZipEncodingHelper.getZipEncoding(var2);
      this.useUnicodeExtraFields = var3;
      this.in = new PushbackInputStream(var1, this.buf.capacity());
      this.allowStoredEntriesWithDataDescriptor = var4;
      this.buf.limit(0);
   }

   public ZipArchiveEntry getNextZipEntry() throws IOException {
      boolean var1 = true;
      if (!this.closed && !this.hitCentralDirectory) {
         if (this.current != null) {
            this.closeEntry();
            var1 = false;
         }

         try {
            if (var1) {
               this.readFirstLocalFileHeader(this.LFH_BUF);
            } else {
               this.readFully(this.LFH_BUF);
            }
         } catch (EOFException var16) {
            return null;
         }

         ZipLong var2 = new ZipLong(this.LFH_BUF);
         if (var2.equals(ZipLong.CFH_SIG) || var2.equals(ZipLong.AED_SIG)) {
            this.hitCentralDirectory = true;
            this.skipRemainderOfArchive();
         }

         if (!var2.equals(ZipLong.LFH_SIG)) {
            return null;
         } else {
            byte var3 = 4;
            this.current = new ZipArchiveInputStream.CurrentEntry();
            int var4 = ZipShort.getValue(this.LFH_BUF, var3);
            int var17 = var3 + 2;
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setPlatform(var4 >> 8 & 15);
            GeneralPurposeBit var5 = GeneralPurposeBit.parse(this.LFH_BUF, var17);
            boolean var6 = var5.usesUTF8ForNames();
            ZipEncoding var7 = var6 ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
            ZipArchiveInputStream.CurrentEntry.access$202(this.current, var5.usesDataDescriptor());
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setGeneralPurposeBit(var5);
            var17 += 2;
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setMethod(ZipShort.getValue(this.LFH_BUF, var17));
            var17 += 2;
            long var8 = ZipUtil.dosToJavaTime(ZipLong.getValue(this.LFH_BUF, var17));
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setTime(var8);
            var17 += 4;
            ZipLong var10 = null;
            ZipLong var11 = null;
            if (!ZipArchiveInputStream.CurrentEntry.access$200(this.current)) {
               ZipArchiveInputStream.CurrentEntry.access$100(this.current).setCrc(ZipLong.getValue(this.LFH_BUF, var17));
               var17 += 4;
               var11 = new ZipLong(this.LFH_BUF, var17);
               var17 += 4;
               var10 = new ZipLong(this.LFH_BUF, var17);
               var17 += 4;
            } else {
               var17 += 12;
            }

            int var12 = ZipShort.getValue(this.LFH_BUF, var17);
            var17 += 2;
            int var13 = ZipShort.getValue(this.LFH_BUF, var17);
            var17 += 2;
            byte[] var14 = new byte[var12];
            this.readFully(var14);
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setName(var7.decode(var14), var14);
            byte[] var15 = new byte[var13];
            this.readFully(var15);
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setExtra(var15);
            if (!var6 && this.useUnicodeExtraFields) {
               ZipUtil.setNameAndCommentFromExtraFields(ZipArchiveInputStream.CurrentEntry.access$100(this.current), var14, (byte[])null);
            }

            this.processZip64Extra(var10, var11);
            if (ZipArchiveInputStream.CurrentEntry.access$100(this.current).getCompressedSize() != -1L) {
               if (ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() == ZipMethod.UNSHRINKING.getCode()) {
                  ZipArchiveInputStream.CurrentEntry.access$302(this.current, new UnshrinkingInputStream(new ZipArchiveInputStream.BoundedInputStream(this, this.in, ZipArchiveInputStream.CurrentEntry.access$100(this.current).getCompressedSize())));
               } else if (ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() == ZipMethod.IMPLODING.getCode()) {
                  ZipArchiveInputStream.CurrentEntry.access$302(this.current, new ExplodingInputStream(ZipArchiveInputStream.CurrentEntry.access$100(this.current).getGeneralPurposeBit().getSlidingDictionarySize(), ZipArchiveInputStream.CurrentEntry.access$100(this.current).getGeneralPurposeBit().getNumberOfShannonFanoTrees(), new ZipArchiveInputStream.BoundedInputStream(this, this.in, ZipArchiveInputStream.CurrentEntry.access$100(this.current).getCompressedSize())));
               }
            }

            ++this.entriesRead;
            return ZipArchiveInputStream.CurrentEntry.access$100(this.current);
         }
      } else {
         return null;
      }
   }

   private void readFirstLocalFileHeader(byte[] var1) throws IOException {
      this.readFully(var1);
      ZipLong var2 = new ZipLong(var1);
      if (var2.equals(ZipLong.DD_SIG)) {
         throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.SPLITTING);
      } else {
         if (var2.equals(ZipLong.SINGLE_SEGMENT_SPLIT_MARKER)) {
            byte[] var3 = new byte[4];
            this.readFully(var3);
            System.arraycopy(var1, 4, var1, 0, 26);
            System.arraycopy(var3, 0, var1, 26, 4);
         }

      }
   }

   private void processZip64Extra(ZipLong var1, ZipLong var2) {
      Zip64ExtendedInformationExtraField var3 = (Zip64ExtendedInformationExtraField)ZipArchiveInputStream.CurrentEntry.access$100(this.current).getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
      ZipArchiveInputStream.CurrentEntry.access$402(this.current, var3 != null);
      if (!ZipArchiveInputStream.CurrentEntry.access$200(this.current)) {
         if (var3 == null || !var2.equals(ZipLong.ZIP64_MAGIC) && !var1.equals(ZipLong.ZIP64_MAGIC)) {
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setCompressedSize(var2.getValue());
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setSize(var1.getValue());
         } else {
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setCompressedSize(var3.getCompressedSize().getLongValue());
            ZipArchiveInputStream.CurrentEntry.access$100(this.current).setSize(var3.getSize().getLongValue());
         }
      }

   }

   public ArchiveEntry getNextEntry() throws IOException {
      return this.getNextZipEntry();
   }

   public boolean canReadEntryData(ArchiveEntry var1) {
      if (!(var1 instanceof ZipArchiveEntry)) {
         return false;
      } else {
         ZipArchiveEntry var2 = (ZipArchiveEntry)var1;
         ZipArchiveInputStream var10000;
         if (ZipUtil.canHandleEntryData(var2)) {
            var10000 = this;
            if (var2 != false) {
               boolean var10001 = true;
               return (boolean)var10000;
            }
         }

         var10000 = false;
         return (boolean)var10000;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("The stream is closed");
      } else if (this.current == null) {
         return -1;
      } else if (var2 <= var1.length && var3 >= 0 && var2 >= 0 && var1.length - var2 >= var3) {
         ZipUtil.checkRequestedFeatures(ZipArchiveInputStream.CurrentEntry.access$100(this.current));
         if (ZipArchiveInputStream.CurrentEntry.access$100(this.current) != false) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.DATA_DESCRIPTOR, ZipArchiveInputStream.CurrentEntry.access$100(this.current));
         } else {
            int var4;
            if (ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() == 0) {
               var4 = this.readStored(var1, var2, var3);
            } else if (ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() == 8) {
               var4 = this.readDeflated(var1, var2, var3);
            } else {
               if (ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() != ZipMethod.UNSHRINKING.getCode() && ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() != ZipMethod.IMPLODING.getCode()) {
                  throw new UnsupportedZipFeatureException(ZipMethod.getMethodByCode(ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod()), ZipArchiveInputStream.CurrentEntry.access$100(this.current));
               }

               var4 = ZipArchiveInputStream.CurrentEntry.access$300(this.current).read(var1, var2, var3);
            }

            if (var4 >= 0) {
               ZipArchiveInputStream.CurrentEntry.access$500(this.current).update(var1, var2, var4);
            }

            return var4;
         }
      } else {
         throw new ArrayIndexOutOfBoundsException();
      }
   }

   private int readStored(byte[] var1, int var2, int var3) throws IOException {
      if (ZipArchiveInputStream.CurrentEntry.access$200(this.current)) {
         if (this.lastStoredEntry == null) {
            this.readStoredEntry();
         }

         return this.lastStoredEntry.read(var1, var2, var3);
      } else {
         long var4 = ZipArchiveInputStream.CurrentEntry.access$100(this.current).getSize();
         if (ZipArchiveInputStream.CurrentEntry.access$600(this.current) >= var4) {
            return -1;
         } else {
            int var6;
            if (this.buf.position() >= this.buf.limit()) {
               this.buf.position(0);
               var6 = this.in.read(this.buf.array());
               if (var6 == -1) {
                  return -1;
               }

               this.buf.limit(var6);
               this.count(var6);
               ZipArchiveInputStream.CurrentEntry.access$714(this.current, (long)var6);
            }

            var6 = Math.min(this.buf.remaining(), var3);
            if (var4 - ZipArchiveInputStream.CurrentEntry.access$600(this.current) < (long)var6) {
               var6 = (int)(var4 - ZipArchiveInputStream.CurrentEntry.access$600(this.current));
            }

            this.buf.get(var1, var2, var6);
            ZipArchiveInputStream.CurrentEntry.access$614(this.current, (long)var6);
            return var6;
         }
      }
   }

   private int readDeflated(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.readFromInflater(var1, var2, var3);
      if (var4 <= 0) {
         if (this.inf.finished()) {
            return -1;
         }

         if (this.inf.needsDictionary()) {
            throw new ZipException("This archive needs a preset dictionary which is not supported by Commons Compress.");
         }

         if (var4 == -1) {
            throw new IOException("Truncated ZIP file");
         }
      }

      return var4;
   }

   private int readFromInflater(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;

      do {
         if (this.inf.needsInput()) {
            int var5 = this.fill();
            if (var5 <= 0) {
               if (var5 == -1) {
                  return -1;
               }
               break;
            }

            ZipArchiveInputStream.CurrentEntry.access$714(this.current, (long)this.buf.limit());
         }

         try {
            var4 = this.inf.inflate(var1, var2, var3);
         } catch (DataFormatException var6) {
            throw (IOException)(new ZipException(var6.getMessage())).initCause(var6);
         }
      } while(var4 == 0 && this.inf.needsInput());

      return var4;
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.in.close();
         this.inf.end();
      }

   }

   public long skip(long var1) throws IOException {
      if (var1 >= 0L) {
         long var3;
         int var7;
         for(var3 = 0L; var3 < var1; var3 += (long)var7) {
            long var5 = var1 - var3;
            var7 = this.read(this.SKIP_BUF, 0, (int)((long)this.SKIP_BUF.length > var5 ? var5 : (long)this.SKIP_BUF.length));
            if (var7 == -1) {
               return var3;
            }
         }

         return var3;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < ZipArchiveOutputStream.LFH_SIG.length) {
         return false;
      } else {
         return var0 >= ZipArchiveOutputStream.LFH_SIG || var0 >= ZipArchiveOutputStream.EOCD_SIG || var0 >= ZipArchiveOutputStream.DD_SIG || var0 < ZipLong.SINGLE_SEGMENT_SPLIT_MARKER.getBytes();
      }
   }

   private void closeEntry() throws IOException {
      if (this.closed) {
         throw new IOException("The stream is closed");
      } else if (this.current != null) {
         if (ZipArchiveInputStream.CurrentEntry.access$700(this.current) <= ZipArchiveInputStream.CurrentEntry.access$100(this.current).getCompressedSize() && !ZipArchiveInputStream.CurrentEntry.access$200(this.current)) {
            this.drainCurrentEntryData();
         } else {
            this.skip(Long.MAX_VALUE);
            long var1 = ZipArchiveInputStream.CurrentEntry.access$100(this.current).getMethod() == 8 ? this.getBytesInflated() : ZipArchiveInputStream.CurrentEntry.access$600(this.current);
            int var3 = (int)(ZipArchiveInputStream.CurrentEntry.access$700(this.current) - var1);
            if (var3 > 0) {
               this.pushback(this.buf.array(), this.buf.limit() - var3, var3);
            }
         }

         if (this.lastStoredEntry == null && ZipArchiveInputStream.CurrentEntry.access$200(this.current)) {
            this.readDataDescriptor();
         }

         this.inf.reset();
         this.buf.clear().flip();
         this.current = null;
         this.lastStoredEntry = null;
      }
   }

   private void drainCurrentEntryData() throws IOException {
      long var3;
      for(long var1 = ZipArchiveInputStream.CurrentEntry.access$100(this.current).getCompressedSize() - ZipArchiveInputStream.CurrentEntry.access$700(this.current); var1 > 0L; var1 -= var3) {
         var3 = (long)this.in.read(this.buf.array(), 0, (int)Math.min((long)this.buf.capacity(), var1));
         if (var3 < 0L) {
            throw new EOFException("Truncated ZIP entry: " + ZipArchiveInputStream.CurrentEntry.access$100(this.current).getName());
         }

         this.count(var3);
      }

   }

   private long getBytesInflated() {
      long var1 = this.inf.getBytesRead();
      if (ZipArchiveInputStream.CurrentEntry.access$700(this.current) >= 4294967296L) {
         while(var1 + 4294967296L <= ZipArchiveInputStream.CurrentEntry.access$700(this.current)) {
            var1 += 4294967296L;
         }
      }

      return var1;
   }

   private int fill() throws IOException {
      if (this.closed) {
         throw new IOException("The stream is closed");
      } else {
         int var1 = this.in.read(this.buf.array());
         if (var1 > 0) {
            this.buf.limit(var1);
            this.count(this.buf.limit());
            this.inf.setInput(this.buf.array(), 0, this.buf.limit());
         }

         return var1;
      }
   }

   private void readFully(byte[] var1) throws IOException {
      int var2 = IOUtils.readFully(this.in, var1);
      this.count(var2);
      if (var2 < var1.length) {
         throw new EOFException();
      }
   }

   private void readDataDescriptor() throws IOException {
      this.readFully(this.WORD_BUF);
      ZipLong var1 = new ZipLong(this.WORD_BUF);
      if (ZipLong.DD_SIG.equals(var1)) {
         this.readFully(this.WORD_BUF);
         var1 = new ZipLong(this.WORD_BUF);
      }

      ZipArchiveInputStream.CurrentEntry.access$100(this.current).setCrc(var1.getValue());
      this.readFully(this.TWO_DWORD_BUF);
      ZipLong var2 = new ZipLong(this.TWO_DWORD_BUF, 8);
      if (!var2.equals(ZipLong.CFH_SIG) && !var2.equals(ZipLong.LFH_SIG)) {
         ZipArchiveInputStream.CurrentEntry.access$100(this.current).setCompressedSize(ZipEightByteInteger.getLongValue(this.TWO_DWORD_BUF));
         ZipArchiveInputStream.CurrentEntry.access$100(this.current).setSize(ZipEightByteInteger.getLongValue(this.TWO_DWORD_BUF, 8));
      } else {
         this.pushback(this.TWO_DWORD_BUF, 8, 8);
         ZipArchiveInputStream.CurrentEntry.access$100(this.current).setCompressedSize(ZipLong.getValue(this.TWO_DWORD_BUF));
         ZipArchiveInputStream.CurrentEntry.access$100(this.current).setSize(ZipLong.getValue(this.TWO_DWORD_BUF, 4));
      }

   }

   private void readStoredEntry() throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      int var2 = 0;
      boolean var3 = false;
      int var4 = ZipArchiveInputStream.CurrentEntry.access$400(this.current) ? 20 : 12;

      while(!var3) {
         int var5 = this.in.read(this.buf.array(), var2, 512 - var2);
         if (var5 <= 0) {
            throw new IOException("Truncated ZIP file");
         }

         if (var5 + var2 < 4) {
            var2 += var5;
         } else {
            var3 = this.bufferContainsSignature(var1, var2, var5, var4);
            if (!var3) {
               var2 = this.cacheBytesRead(var1, var2, var5, var4);
            }
         }
      }

      byte[] var6 = var1.toByteArray();
      this.lastStoredEntry = new ByteArrayInputStream(var6);
   }

   private boolean bufferContainsSignature(ByteArrayOutputStream var1, int var2, int var3, int var4) throws IOException {
      boolean var5 = false;
      int var6 = 0;

      for(int var7 = 0; !var5 && var7 < var3 - 4; ++var7) {
         if (this.buf.array()[var7] == LFH[0] && this.buf.array()[var7 + 1] == LFH[1]) {
            if (this.buf.array()[var7 + 2] == LFH[2] && this.buf.array()[var7 + 3] == LFH[3] || this.buf.array()[var7] == CFH[2] && this.buf.array()[var7 + 3] == CFH[3]) {
               var6 = var2 + var3 - var7 - var4;
               var5 = true;
            } else if (this.buf.array()[var7 + 2] == DD[2] && this.buf.array()[var7 + 3] == DD[3]) {
               var6 = var2 + var3 - var7;
               var5 = true;
            }

            if (var5) {
               this.pushback(this.buf.array(), var2 + var3 - var6, var6);
               var1.write(this.buf.array(), 0, var7);
               this.readDataDescriptor();
            }
         }
      }

      return var5;
   }

   private int cacheBytesRead(ByteArrayOutputStream var1, int var2, int var3, int var4) {
      int var5 = var2 + var3 - var4 - 3;
      if (var5 > 0) {
         var1.write(this.buf.array(), 0, var5);
         System.arraycopy(this.buf.array(), var5, this.buf.array(), 0, var4 + 3);
         var2 = var4 + 3;
      } else {
         var2 += var3;
      }

      return var2;
   }

   private void pushback(byte[] var1, int var2, int var3) throws IOException {
      ((PushbackInputStream)this.in).unread(var1, var2, var3);
      this.pushedBackBytes((long)var3);
   }

   private void skipRemainderOfArchive() throws IOException {
      this.realSkip((long)(this.entriesRead * 46 - 30));
      this.findEocdRecord();
      this.realSkip(16L);
      this.readFully(this.SHORT_BUF);
      this.realSkip((long)ZipShort.getValue(this.SHORT_BUF));
   }

   private void findEocdRecord() throws IOException {
      int var1 = -1;
      boolean var2 = false;

      while(var2 || (var1 = this.readOneByte()) > -1) {
         var2 = false;
         if (this != var1) {
            var1 = this.readOneByte();
            if (var1 != ZipArchiveOutputStream.EOCD_SIG[1]) {
               if (var1 == -1) {
                  break;
               }

               var2 = this.isFirstByteOfEocdSig(var1);
            } else {
               var1 = this.readOneByte();
               if (var1 != ZipArchiveOutputStream.EOCD_SIG[2]) {
                  if (var1 == -1) {
                     break;
                  }

                  var2 = this.isFirstByteOfEocdSig(var1);
               } else {
                  var1 = this.readOneByte();
                  if (var1 == -1 || var1 == ZipArchiveOutputStream.EOCD_SIG[3]) {
                     break;
                  }

                  var2 = this.isFirstByteOfEocdSig(var1);
               }
            }
         }
      }

   }

   private void realSkip(long var1) throws IOException {
      if (var1 >= 0L) {
         int var7;
         for(long var3 = 0L; var3 < var1; var3 += (long)var7) {
            long var5 = var1 - var3;
            var7 = this.in.read(this.SKIP_BUF, 0, (int)((long)this.SKIP_BUF.length > var5 ? var5 : (long)this.SKIP_BUF.length));
            if (var7 == -1) {
               return;
            }

            this.count(var7);
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   private int readOneByte() throws IOException {
      int var1 = this.in.read();
      if (var1 != -1) {
         this.count(1);
      }

      return var1;
   }

   static void access$800(ZipArchiveInputStream var0, int var1) {
      var0.count(var1);
   }

   static ZipArchiveInputStream.CurrentEntry access$900(ZipArchiveInputStream var0) {
      return var0.current;
   }

   static void access$1000(ZipArchiveInputStream var0, int var1) {
      var0.count(var1);
   }

   static {
      LFH = ZipLong.LFH_SIG.getBytes();
      CFH = ZipLong.CFH_SIG.getBytes();
      DD = ZipLong.DD_SIG.getBytes();
   }

   private class BoundedInputStream extends InputStream {
      private final InputStream in;
      private final long max;
      private long pos;
      final ZipArchiveInputStream this$0;

      public BoundedInputStream(ZipArchiveInputStream var1, InputStream var2, long var3) {
         this.this$0 = var1;
         this.pos = 0L;
         this.max = var3;
         this.in = var2;
      }

      public int read() throws IOException {
         if (this.max >= 0L && this.pos >= this.max) {
            return -1;
         } else {
            int var1 = this.in.read();
            ++this.pos;
            ZipArchiveInputStream.access$800(this.this$0, 1);
            ZipArchiveInputStream.CurrentEntry.access$708(ZipArchiveInputStream.access$900(this.this$0));
            return var1;
         }
      }

      public int read(byte[] var1) throws IOException {
         return this.read(var1, 0, var1.length);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (this.max >= 0L && this.pos >= this.max) {
            return -1;
         } else {
            long var4 = this.max >= 0L ? Math.min((long)var3, this.max - this.pos) : (long)var3;
            int var6 = this.in.read(var1, var2, (int)var4);
            if (var6 == -1) {
               return -1;
            } else {
               this.pos += (long)var6;
               ZipArchiveInputStream.access$1000(this.this$0, var6);
               ZipArchiveInputStream.CurrentEntry.access$714(ZipArchiveInputStream.access$900(this.this$0), (long)var6);
               return var6;
            }
         }
      }

      public long skip(long var1) throws IOException {
         long var3 = this.max >= 0L ? Math.min(var1, this.max - this.pos) : var1;
         long var5 = this.in.skip(var3);
         this.pos += var5;
         return var5;
      }

      public int available() throws IOException {
         return this.max >= 0L && this.pos >= this.max ? 0 : this.in.available();
      }
   }

   private static final class CurrentEntry {
      private final ZipArchiveEntry entry;
      private boolean hasDataDescriptor;
      private boolean usesZip64;
      private long bytesRead;
      private long bytesReadFromStream;
      private final CRC32 crc;
      private InputStream in;

      private CurrentEntry() {
         this.entry = new ZipArchiveEntry();
         this.crc = new CRC32();
      }

      CurrentEntry(Object var1) {
         this();
      }

      static ZipArchiveEntry access$100(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.entry;
      }

      static boolean access$202(ZipArchiveInputStream.CurrentEntry var0, boolean var1) {
         return var0.hasDataDescriptor = var1;
      }

      static boolean access$200(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.hasDataDescriptor;
      }

      static InputStream access$302(ZipArchiveInputStream.CurrentEntry var0, InputStream var1) {
         return var0.in = var1;
      }

      static boolean access$402(ZipArchiveInputStream.CurrentEntry var0, boolean var1) {
         return var0.usesZip64 = var1;
      }

      static InputStream access$300(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.in;
      }

      static CRC32 access$500(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.crc;
      }

      static long access$600(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.bytesRead;
      }

      static long access$714(ZipArchiveInputStream.CurrentEntry var0, long var1) {
         return var0.bytesReadFromStream += var1;
      }

      static long access$614(ZipArchiveInputStream.CurrentEntry var0, long var1) {
         return var0.bytesRead += var1;
      }

      static long access$700(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.bytesReadFromStream;
      }

      static boolean access$400(ZipArchiveInputStream.CurrentEntry var0) {
         return var0.usesZip64;
      }

      static long access$708(ZipArchiveInputStream.CurrentEntry var0) {
         return (long)(var0.bytesReadFromStream++);
      }
   }
}
