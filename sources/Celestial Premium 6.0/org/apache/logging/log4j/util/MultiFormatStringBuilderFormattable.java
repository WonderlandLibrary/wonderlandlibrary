/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import org.apache.logging.log4j.message.MultiformatMessage;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public interface MultiFormatStringBuilderFormattable
extends MultiformatMessage,
StringBuilderFormattable {
    public void formatTo(String[] var1, StringBuilder var2);
}

