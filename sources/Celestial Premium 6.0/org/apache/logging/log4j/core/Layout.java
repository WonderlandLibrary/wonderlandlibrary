/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;

public interface Layout<T extends Serializable> {
    public byte[] getFooter();

    public byte[] getHeader();

    public byte[] toByteArray(LogEvent var1);

    public T toSerializable(LogEvent var1);

    public String getContentType();

    public Map<String, String> getContentFormat();
}

