// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util.internal;

import java.net.URI;
import java.io.File;
import org.apache.logging.log4j.core.util.Source;

public class LastModifiedSource extends Source
{
    private volatile long lastModified;
    
    public LastModifiedSource(final File file) {
        super(file);
        this.lastModified = 0L;
    }
    
    public LastModifiedSource(final URI uri) {
        this(uri, 0L);
    }
    
    public LastModifiedSource(final URI uri, final long lastModifiedMillis) {
        super(uri);
        this.lastModified = lastModifiedMillis;
    }
    
    public long getLastModified() {
        return this.lastModified;
    }
    
    public void setLastModified(final long lastModified) {
        this.lastModified = lastModified;
    }
}
