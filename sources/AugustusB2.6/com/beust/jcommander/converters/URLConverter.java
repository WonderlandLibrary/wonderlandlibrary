// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.net.MalformedURLException;
import com.beust.jcommander.ParameterException;
import java.net.URL;

public class URLConverter extends BaseConverter<URL>
{
    public URLConverter(final String s) {
        super(s);
    }
    
    @Override
    public URL convert(final String spec) {
        try {
            return new URL(spec);
        }
        catch (MalformedURLException ex) {
            throw new ParameterException(this.getErrorString(spec, "a RFC 2396 and RFC 2732 compliant URL"));
        }
    }
}
