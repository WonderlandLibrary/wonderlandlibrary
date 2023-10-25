/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractLayout;

@Plugin(name="SerializedLayout", category="Core", elementType="layout", printObject=true)
public final class SerializedLayout
extends AbstractLayout<LogEvent> {
    private static byte[] header;

    private SerializedLayout() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] toByteArray(LogEvent event) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PrivateObjectOutputStream oos = new PrivateObjectOutputStream(baos);
            try {
                oos.writeObject(event);
                oos.reset();
            }
            finally {
                oos.close();
            }
        }
        catch (IOException ioe) {
            LOGGER.error("Serialization of LogEvent failed.", (Throwable)ioe);
        }
        return baos.toByteArray();
    }

    @Override
    public LogEvent toSerializable(LogEvent event) {
        return event;
    }

    @PluginFactory
    public static SerializedLayout createLayout() {
        return new SerializedLayout();
    }

    @Override
    public byte[] getHeader() {
        return header;
    }

    @Override
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }

    @Override
    public String getContentType() {
        return "application/octet-stream";
    }

    static {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.close();
            header = baos.toByteArray();
        }
        catch (Exception ex) {
            LOGGER.error("Unable to generate Object stream header", (Throwable)ex);
        }
    }

    private class PrivateObjectOutputStream
    extends ObjectOutputStream {
        public PrivateObjectOutputStream(OutputStream os) throws IOException {
            super(os);
        }

        @Override
        protected void writeStreamHeader() {
        }
    }
}

