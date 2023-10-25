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
  
  abstract StreamBridge newStreamBridge() throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\pack200\Pack200Strategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */