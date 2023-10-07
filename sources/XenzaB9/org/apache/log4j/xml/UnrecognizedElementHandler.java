// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public interface UnrecognizedElementHandler
{
    boolean parseUnrecognizedElement(final Element element, final Properties props) throws Exception;
}
