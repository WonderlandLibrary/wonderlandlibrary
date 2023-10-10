package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;

public class BOMInputStream extends ProxyInputStream {
   private final boolean include;
   private final List boms;
   private ByteOrderMark byteOrderMark;
   private int[] firstBytes;
   private int fbLength;
   private int fbIndex;
   private int markFbIndex;
   private boolean markedAtStart;
   private static final Comparator ByteOrderMarkLengthComparator = new Comparator() {
      public int compare(ByteOrderMark var1, ByteOrderMark var2) {
         int var3 = var1.length();
         int var4 = var2.length();
         if (var3 > var4) {
            return -1;
         } else {
            return var4 > var3 ? 1 : 0;
         }
      }

      public int compare(Object var1, Object var2) {
         return this.compare((ByteOrderMark)var1, (ByteOrderMark)var2);
      }
   };

   public BOMInputStream(InputStream var1) {
      this(var1, false, ByteOrderMark.UTF_8);
   }

   public BOMInputStream(InputStream var1, boolean var2) {
      this(var1, var2, ByteOrderMark.UTF_8);
   }

   public BOMInputStream(InputStream var1, ByteOrderMark... var2) {
      this(var1, false, var2);
   }

   public BOMInputStream(InputStream var1, boolean var2, ByteOrderMark... var3) {
      super(var1);
      if (var3 != null && var3.length != 0) {
         this.include = var2;
         Arrays.sort(var3, ByteOrderMarkLengthComparator);
         this.boms = Arrays.asList(var3);
      } else {
         throw new IllegalArgumentException("No BOMs specified");
      }
   }

   public boolean hasBOM() throws IOException {
      return this.getBOM() != null;
   }

   public boolean hasBOM(ByteOrderMark var1) throws IOException {
      if (!this.boms.contains(var1)) {
         throw new IllegalArgumentException("Stream not configure to detect " + var1);
      } else {
         return this.byteOrderMark != null && this.getBOM().equals(var1);
      }
   }

   public ByteOrderMark getBOM() throws IOException {
      if (this.firstBytes == null) {
         this.fbLength = 0;
         int var1 = ((ByteOrderMark)this.boms.get(0)).length();
         this.firstBytes = new int[var1];

         for(int var2 = 0; var2 < this.firstBytes.length; ++var2) {
            this.firstBytes[var2] = this.in.read();
            ++this.fbLength;
            if (this.firstBytes[var2] < 0) {
               break;
            }
         }

         this.byteOrderMark = this.find();
         if (this.byteOrderMark != null && !this.include) {
            if (this.byteOrderMark.length() < this.firstBytes.length) {
               this.fbIndex = this.byteOrderMark.length();
            } else {
               this.fbLength = 0;
            }
         }
      }

      return this.byteOrderMark;
   }

   public String getBOMCharsetName() throws IOException {
      this.getBOM();
      return this.byteOrderMark == null ? null : this.byteOrderMark.getCharsetName();
   }

   private int readFirstBytes() throws IOException {
      this.getBOM();
      return this.fbIndex < this.fbLength ? this.firstBytes[this.fbIndex++] : -1;
   }

   private ByteOrderMark find() {
      Iterator var1 = this.boms.iterator();

      ByteOrderMark var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (ByteOrderMark)var1.next();
      } while(this >= var2);

      return var2;
   }

   public int read() throws IOException {
      int var1 = this.readFirstBytes();
      return var1 >= 0 ? var1 : this.in.read();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;
      int var5 = 0;

      while(var3 > 0 && var5 >= 0) {
         var5 = this.readFirstBytes();
         if (var5 >= 0) {
            var1[var2++] = (byte)(var5 & 255);
            --var3;
            ++var4;
         }
      }

      int var6 = this.in.read(var1, var2, var3);
      return var6 < 0 ? (var4 > 0 ? var4 : -1) : var4 + var6;
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public synchronized void mark(int var1) {
      this.markFbIndex = this.fbIndex;
      this.markedAtStart = this.firstBytes == null;
      this.in.mark(var1);
   }

   public synchronized void reset() throws IOException {
      this.fbIndex = this.markFbIndex;
      if (this.markedAtStart) {
         this.firstBytes = null;
      }

      this.in.reset();
   }

   public long skip(long var1) throws IOException {
      while(var1 > 0L && this.readFirstBytes() >= 0) {
         --var1;
      }

      return this.in.skip(var1);
   }
}
