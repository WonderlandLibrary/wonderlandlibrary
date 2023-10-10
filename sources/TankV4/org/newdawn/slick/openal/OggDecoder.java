package org.newdawn.slick.openal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class OggDecoder {
   private int convsize = 16384;
   private byte[] convbuffer;

   public OggDecoder() {
      this.convbuffer = new byte[this.convsize];
   }

   public OggData getData(InputStream var1) throws IOException {
      if (var1 == null) {
         throw new IOException("Failed to read OGG, source does not exist?");
      } else {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         OggInputStream var3 = new OggInputStream(var1);
         boolean var4 = false;

         while(!var3.atEnd()) {
            var2.write(var3.read());
         }

         OggData var5 = new OggData();
         var5.channels = var3.getChannels();
         var5.rate = var3.getRate();
         byte[] var6 = var2.toByteArray();
         var5.data = ByteBuffer.allocateDirect(var6.length);
         var5.data.put(var6);
         var5.data.rewind();
         return var5;
      }
   }
}
