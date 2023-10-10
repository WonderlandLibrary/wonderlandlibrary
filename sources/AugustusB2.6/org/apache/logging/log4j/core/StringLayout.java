// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import java.nio.charset.Charset;

public interface StringLayout extends Layout<String>
{
    Charset getCharset();
}
