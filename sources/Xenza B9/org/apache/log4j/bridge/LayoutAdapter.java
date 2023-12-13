// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import java.io.Serializable;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Layout;

public class LayoutAdapter implements Layout<String>
{
    private org.apache.log4j.Layout layout;
    
    public static Layout<?> adapt(final org.apache.log4j.Layout layout) {
        if (layout instanceof LayoutWrapper) {
            return ((LayoutWrapper)layout).getLayout();
        }
        if (layout != null) {
            return new LayoutAdapter(layout);
        }
        return null;
    }
    
    private LayoutAdapter(final org.apache.log4j.Layout layout) {
        this.layout = layout;
    }
    
    public org.apache.log4j.Layout getLayout() {
        return this.layout;
    }
    
    @Override
    public byte[] getFooter() {
        return (byte[])((this.layout.getFooter() == null) ? null : this.layout.getFooter().getBytes());
    }
    
    @Override
    public byte[] getHeader() {
        return (byte[])((this.layout.getHeader() == null) ? null : this.layout.getHeader().getBytes());
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        final String result = this.layout.format(new LogEventAdapter(event));
        return (byte[])((result == null) ? null : result.getBytes());
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        return this.layout.format(new LogEventAdapter(event));
    }
    
    @Override
    public String getContentType() {
        return this.layout.getContentType();
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        final byte[] data = this.toByteArray(event);
        destination.writeBytes(data, 0, data.length);
    }
}
