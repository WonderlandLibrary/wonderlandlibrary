// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import org.apache.logging.log4j.message.MultiformatMessage;

public interface MultiFormatStringBuilderFormattable extends MultiformatMessage, StringBuilderFormattable
{
    void formatTo(final String[] formats, final StringBuilder buffer);
}
