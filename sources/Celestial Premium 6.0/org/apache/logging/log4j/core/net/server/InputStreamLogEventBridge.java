/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.ObjectReader
 */
package org.apache.logging.log4j.core.net.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.LogEventListener;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.net.server.AbstractLogEventBridge;

public abstract class InputStreamLogEventBridge
extends AbstractLogEventBridge<InputStream> {
    private final int bufferSize;
    private final Charset charset;
    private final String eventEndMarker;
    private final ObjectReader objectReader;

    public InputStreamLogEventBridge(ObjectMapper mapper, int bufferSize, Charset charset, String eventEndMarker) {
        this.bufferSize = bufferSize;
        this.charset = charset;
        this.eventEndMarker = eventEndMarker;
        this.objectReader = mapper.readerFor(Log4jLogEvent.class);
    }

    protected abstract int[] getEventIndices(String var1, int var2);

    @Override
    public void logEvents(InputStream inputStream, LogEventListener logEventListener) throws IOException {
        String workingText = "";
        try {
            int streamReadLength;
            byte[] buffer = new byte[this.bufferSize];
            workingText = "";
            String textRemains = "";
            block2: while ((streamReadLength = inputStream.read(buffer)) != -1) {
                String text = workingText = textRemains + new String(buffer, 0, streamReadLength, this.charset);
                int beginIndex = 0;
                while (true) {
                    int[] pair;
                    int eventStartMarkerIndex;
                    if ((eventStartMarkerIndex = (pair = this.getEventIndices(text, beginIndex))[0]) < 0) {
                        textRemains = text.substring(beginIndex);
                        continue block2;
                    }
                    int eventEndMarkerIndex = pair[1];
                    if (eventEndMarkerIndex <= 0) break;
                    int eventEndXmlIndex = eventEndMarkerIndex + this.eventEndMarker.length();
                    String textEvent = workingText = text.substring(eventStartMarkerIndex, eventEndXmlIndex);
                    Log4jLogEvent logEvent = this.unmarshal(textEvent);
                    logEventListener.log(logEvent);
                    beginIndex = eventEndXmlIndex;
                }
                textRemains = text.substring(beginIndex);
            }
        }
        catch (IOException ex) {
            logger.error(workingText, (Throwable)ex);
        }
    }

    protected Log4jLogEvent unmarshal(String jsonEvent) throws IOException {
        return (Log4jLogEvent)this.objectReader.readValue(jsonEvent);
    }
}

