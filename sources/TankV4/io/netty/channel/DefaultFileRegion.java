package io.netty.channel;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class DefaultFileRegion extends AbstractReferenceCounted implements FileRegion {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultFileRegion.class);
   private final FileChannel file;
   private final long position;
   private final long count;
   private long transfered;

   public DefaultFileRegion(FileChannel var1, long var2, long var4) {
      if (var1 == null) {
         throw new NullPointerException("file");
      } else if (var2 < 0L) {
         throw new IllegalArgumentException("position must be >= 0 but was " + var2);
      } else if (var4 < 0L) {
         throw new IllegalArgumentException("count must be >= 0 but was " + var4);
      } else {
         this.file = var1;
         this.position = var2;
         this.count = var4;
      }
   }

   public long position() {
      return this.position;
   }

   public long count() {
      return this.count;
   }

   public long transfered() {
      return this.transfered;
   }

   public long transferTo(WritableByteChannel var1, long var2) throws IOException {
      long var4 = this.count - var2;
      if (var4 >= 0L && var2 >= 0L) {
         if (var4 == 0L) {
            return 0L;
         } else {
            long var6 = this.file.transferTo(this.position + var2, var4, var1);
            if (var6 > 0L) {
               this.transfered += var6;
            }

            return var6;
         }
      } else {
         throw new IllegalArgumentException("position out of range: " + var2 + " (expected: 0 - " + (this.count - 1L) + ')');
      }
   }

   protected void deallocate() {
      try {
         this.file.close();
      } catch (IOException var2) {
         if (logger.isWarnEnabled()) {
            logger.warn("Failed to close a file.", (Throwable)var2);
         }
      }

   }
}
