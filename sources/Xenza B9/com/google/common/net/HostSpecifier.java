// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.net;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.net.InetAddress;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;

@Beta
public final class HostSpecifier
{
    private final String canonicalForm;
    
    private HostSpecifier(final String canonicalForm) {
        this.canonicalForm = canonicalForm;
    }
    
    public static HostSpecifier fromValid(final String specifier) {
        final HostAndPort parsedHost = HostAndPort.fromString(specifier);
        Preconditions.checkArgument(!parsedHost.hasPort());
        final String host = parsedHost.getHostText();
        InetAddress addr = null;
        try {
            addr = InetAddresses.forString(host);
        }
        catch (final IllegalArgumentException ex) {}
        if (addr != null) {
            return new HostSpecifier(InetAddresses.toUriString(addr));
        }
        final InternetDomainName domain = InternetDomainName.from(host);
        if (domain.hasPublicSuffix()) {
            return new HostSpecifier(domain.toString());
        }
        final String s = "Domain name does not have a recognized public suffix: ";
        final String value = String.valueOf(host);
        throw new IllegalArgumentException((value.length() != 0) ? s.concat(value) : new String(s));
    }
    
    public static HostSpecifier from(final String specifier) throws ParseException {
        try {
            return fromValid(specifier);
        }
        catch (final IllegalArgumentException e) {
            final String s = "Invalid host specifier: ";
            final String value = String.valueOf(specifier);
            final ParseException parseException = new ParseException((value.length() != 0) ? s.concat(value) : new String(s), 0);
            parseException.initCause(e);
            throw parseException;
        }
    }
    
    public static boolean isValid(final String specifier) {
        try {
            fromValid(specifier);
            return true;
        }
        catch (final IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof HostSpecifier) {
            final HostSpecifier that = (HostSpecifier)other;
            return this.canonicalForm.equals(that.canonicalForm);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.canonicalForm.hashCode();
    }
    
    @Override
    public String toString() {
        return this.canonicalForm;
    }
}
