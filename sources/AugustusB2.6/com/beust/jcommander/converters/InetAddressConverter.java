// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander.converters;

import java.net.UnknownHostException;
import java.net.InetAddress;
import com.beust.jcommander.IStringConverter;

public class InetAddressConverter implements IStringConverter<InetAddress>
{
    @Override
    public InetAddress convert(final String s) {
        try {
            return InetAddress.getByName(s);
        }
        catch (UnknownHostException cause) {
            throw new IllegalArgumentException(s, cause);
        }
    }
}
