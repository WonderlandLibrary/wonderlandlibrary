/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public interface UnrecognizedElementHandler {
    public boolean parseUnrecognizedElement(Element var1, Properties var2) throws Exception;
}

