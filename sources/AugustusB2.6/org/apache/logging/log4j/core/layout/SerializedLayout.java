// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.LogEvent;

@Deprecated
@Plugin(name = "SerializedLayout", category = "Core", elementType = "layout", printObject = true)
public final class SerializedLayout extends AbstractLayout<LogEvent>
{
    private static byte[] serializedHeader;
    
    private SerializedLayout() {
        super(null, null, null);
        SerializedLayout.LOGGER.warn("SerializedLayout is deprecated due to the inherent security weakness in Java Serialization, see https://www.owasp.org/index.php/Deserialization_of_untrusted_data Consider using another layout, e.g. JsonLayout");
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final ObjectOutputStream oos = new PrivateObjectOutputStream(baos)) {
            oos.writeObject(event);
            oos.reset();
        }
        catch (IOException ioe) {
            SerializedLayout.LOGGER.error("Serialization of LogEvent failed.", ioe);
        }
        return baos.toByteArray();
    }
    
    @Override
    public LogEvent toSerializable(final LogEvent event) {
        return event;
    }
    
    @Deprecated
    @PluginFactory
    public static SerializedLayout createLayout() {
        return new SerializedLayout();
    }
    
    @Override
    public byte[] getHeader() {
        return SerializedLayout.serializedHeader;
    }
    
    @Override
    public String getContentType() {
        return "application/octet-stream";
    }
    
    static {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(baos).close();
            SerializedLayout.serializedHeader = baos.toByteArray();
        }
        catch (Exception ex) {
            SerializedLayout.LOGGER.error("Unable to generate Object stream header", ex);
        }
    }
    
    private class PrivateObjectOutputStream extends ObjectOutputStream
    {
        public PrivateObjectOutputStream(final OutputStream os) throws IOException {
            super(os);
        }
        
        @Override
        protected void writeStreamHeader() {
        }
    }
}
