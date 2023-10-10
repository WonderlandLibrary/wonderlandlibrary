// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.net.URISyntaxException;
import com.beust.jcommander.ParameterException;
import java.net.URI;

public class URIConverter extends BaseConverter<URI>
{
    public URIConverter(final String s) {
        super(s);
    }
    
    @Override
    public URI convert(final String str) {
        try {
            return new URI(str);
        }
        catch (URISyntaxException ex) {
            throw new ParameterException(this.getErrorString(str, "a RFC 2396 and RFC 2732 compliant URI"));
        }
    }
}
