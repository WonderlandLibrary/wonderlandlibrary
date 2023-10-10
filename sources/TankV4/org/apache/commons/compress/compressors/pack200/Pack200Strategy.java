package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;

public enum Pack200Strategy {
   IN_MEMORY {
      StreamBridge newStreamBridge() {
         return new InMemoryCachingStreamBridge();
      }
   },
   TEMP_FILE {
      StreamBridge newStreamBridge() throws IOException {
         return new TempFileCachingStreamBridge();
      }
   };

   private static final Pack200Strategy[] $VALUES = new Pack200Strategy[]{IN_MEMORY, TEMP_FILE};

   private Pack200Strategy() {
   }

   abstract StreamBridge newStreamBridge() throws IOException;

   Pack200Strategy(Object var3) {
      this();
   }
}
