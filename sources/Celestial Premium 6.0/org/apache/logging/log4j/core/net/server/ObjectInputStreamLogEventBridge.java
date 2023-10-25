/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LogEventListener;
import org.apache.logging.log4j.core.net.server.AbstractLogEventBridge;

public class ObjectInputStreamLogEventBridge
extends AbstractLogEventBridge<ObjectInputStream> {
    @Override
    public void logEvents(ObjectInputStream inputStream, LogEventListener logEventListener) throws IOException {
        try {
            logEventListener.log((LogEvent)inputStream.readObject());
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    public ObjectInputStream wrapStream(InputStream inputStream) throws IOException {
        return new ObjectInputStream(inputStream);
    }
}

