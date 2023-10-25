/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package org.apache.logging.log4j.core.net.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
import org.apache.logging.log4j.core.net.server.InputStreamLogEventBridge;

public class XmlInputStreamLogEventBridge
extends InputStreamLogEventBridge {
    private static final String EVENT_END = "</Event>";
    private static final String EVENT_START_NS_N = "<Event>";
    private static final String EVENT_START_NS_Y = "<Event ";

    public XmlInputStreamLogEventBridge() {
        this(1024, Charset.defaultCharset());
    }

    public XmlInputStreamLogEventBridge(int bufferSize, Charset charset) {
        super((ObjectMapper)new Log4jXmlObjectMapper(), bufferSize, charset, EVENT_END);
    }

    @Override
    protected int[] getEventIndices(String text, int beginIndex) {
        int start = text.indexOf(EVENT_START_NS_Y, beginIndex);
        int startLen = EVENT_START_NS_Y.length();
        if (start < 0) {
            start = text.indexOf(EVENT_START_NS_N, beginIndex);
            startLen = EVENT_START_NS_N.length();
        }
        int end = start < 0 ? -1 : text.indexOf(EVENT_END, start + startLen);
        return new int[]{start, end};
    }
}

