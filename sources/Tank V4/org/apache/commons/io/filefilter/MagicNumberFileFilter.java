package org.apache.commons.io.filefilter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {
   private static final long serialVersionUID = -547733176983104172L;
   private final byte[] magicNumbers;
   private final long byteOffset;

   public MagicNumberFileFilter(byte[] var1) {
      this(var1, 0L);
   }

   public MagicNumberFileFilter(String var1) {
      this(var1, 0L);
   }

   public MagicNumberFileFilter(String var1, long var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The magic number cannot be null");
      } else if (var1.length() == 0) {
         throw new IllegalArgumentException("The magic number must contain at least one byte");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("The offset cannot be negative");
      } else {
         this.magicNumbers = var1.getBytes();
         this.byteOffset = var2;
      }
   }

   public MagicNumberFileFilter(byte[] var1, long var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The magic number cannot be null");
      } else if (var1.length == 0) {
         throw new IllegalArgumentException("The magic number must contain at least one byte");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("The offset cannot be negative");
      } else {
         this.magicNumbers = new byte[var1.length];
         System.arraycopy(var1, 0, this.magicNumbers, 0, var1.length);
         this.byteOffset = var2;
      }
   }

   public boolean accept(File var1) {
      if (var1 != null && var1.isFile() && var1.canRead()) {
         RandomAccessFile var2 = null;

         boolean var5;
         label26: {
            try {
               byte[] var3 = new byte[this.magicNumbers.length];
               var2 = new RandomAccessFile(var1, "r");
               var2.seek(this.byteOffset);
               int var4 = var2.read(var3);
               if (var4 != this.magicNumbers.length) {
                  var5 = false;
                  break label26;
               }

               var5 = Arrays.equals(this.magicNumbers, var3);
            } catch (IOException var7) {
               IOUtils.closeQuietly((Closeable)var2);
               return false;
            }

            IOUtils.closeQuietly((Closeable)var2);
            return var5;
         }

         IOUtils.closeQuietly((Closeable)var2);
         return var5;
      } else {
         return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(super.toString());
      var1.append("(");
      var1.append(new String(this.magicNumbers));
      var1.append(",");
      var1.append(this.byteOffset);
      var1.append(")");
      return var1.toString();
   }
}
