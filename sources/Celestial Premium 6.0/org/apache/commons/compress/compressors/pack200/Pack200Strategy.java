/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;
import org.apache.commons.compress.compressors.pack200.InMemoryCachingStreamBridge;
import org.apache.commons.compress.compressors.pack200.StreamBridge;
import org.apache.commons.compress.compressors.pack200.TempFileCachingStreamBridge;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum Pack200Strategy {
    IN_MEMORY{

        StreamBridge newStreamBridge() {
            return new InMemoryCachingStreamBridge();
        }
    }
    ,
    TEMP_FILE{

        StreamBridge newStreamBridge() throws IOException {
            return new TempFileCachingStreamBridge();
        }
    };


    abstract StreamBridge newStreamBridge() throws IOException;
}

