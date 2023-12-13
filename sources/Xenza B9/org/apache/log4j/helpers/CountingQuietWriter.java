// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.helpers;

import java.io.IOException;
import org.apache.log4j.spi.ErrorHandler;
import java.io.Writer;

public class CountingQuietWriter extends QuietWriter
{
    protected long count;
    
    public CountingQuietWriter(final Writer writer, final ErrorHandler eh) {
        super(writer, eh);
    }
    
    public long getCount() {
        return this.count;
    }
    
    public void setCount(final long count) {
        this.count = count;
    }
    
    @Override
    public void write(final String string) {
        try {
            this.out.write(string);
            this.count += string.length();
        }
        catch (final IOException e) {
            this.errorHandler.error("Write failure.", e, 1);
        }
    }
}
