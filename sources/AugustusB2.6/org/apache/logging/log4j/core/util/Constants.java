// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.util.PropertiesUtil;

public final class Constants
{
    public static final String LOG4J_LOG_EVENT_FACTORY = "Log4jLogEventFactory";
    public static final String LOG4J_CONTEXT_SELECTOR = "Log4jContextSelector";
    public static final String LOG4J_DEFAULT_STATUS_LEVEL = "Log4jDefaultStatusLevel";
    public static final String JNDI_CONTEXT_NAME = "java:comp/env/log4j/context-name";
    public static final int MILLIS_IN_SECONDS = 1000;
    public static final boolean FORMAT_MESSAGES_IN_BACKGROUND;
    @Deprecated
    public static final boolean FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS;
    public static final boolean IS_WEB_APP;
    public static final boolean ENABLE_THREADLOCALS;
    public static final boolean ENABLE_DIRECT_ENCODERS;
    public static final int INITIAL_REUSABLE_MESSAGE_SIZE;
    public static final int MAX_REUSABLE_MESSAGE_SIZE;
    public static final int ENCODER_CHAR_BUFFER_SIZE;
    public static final int ENCODER_BYTE_BUFFER_SIZE;
    
    private static int size(final String property, final int defaultValue) {
        return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
    }
    
    private Constants() {
    }
    
    static {
        FORMAT_MESSAGES_IN_BACKGROUND = PropertiesUtil.getProperties().getBooleanProperty("log4j.format.msg.async", false);
        FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS = PropertiesUtil.getProperties().getBooleanProperty("log4j2.formatMsgNoLookups", true);
        IS_WEB_APP = org.apache.logging.log4j.util.Constants.IS_WEB_APP;
        ENABLE_THREADLOCALS = org.apache.logging.log4j.util.Constants.ENABLE_THREADLOCALS;
        ENABLE_DIRECT_ENCODERS = PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.direct.encoders", true);
        INITIAL_REUSABLE_MESSAGE_SIZE = size("log4j.initialReusableMsgSize", 128);
        MAX_REUSABLE_MESSAGE_SIZE = size("log4j.maxReusableMsgSize", 518);
        ENCODER_CHAR_BUFFER_SIZE = size("log4j.encoder.charBufferSize", 2048);
        ENCODER_BYTE_BUFFER_SIZE = size("log4j.encoder.byteBufferSize", 8192);
    }
}
