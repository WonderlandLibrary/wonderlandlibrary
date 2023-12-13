// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.net;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.net.InetAddress;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class HostSpecifier
{
    private final String canonicalForm;
    
    private HostSpecifier(final String canonicalForm) {
        this.canonicalForm = canonicalForm;
    }
    
    public static HostSpecifier fromValid(final String specifier) {
        final HostAndPort parsedHost = HostAndPort.fromString(specifier);
        Preconditions.checkArgument(!parsedHost.hasPort());
        final String host = parsedHost.getHost();
        InetAddress addr = null;
        try {
            addr = InetAddresses.forString(host);
        }
        catch (IllegalArgumentException ex) {}
        if (addr != null) {
            return new HostSpecifier(InetAddresses.toUriString(addr));
        }
        final InternetDomainName domain = InternetDomainName.from(host);
        if (domain.hasPublicSuffix()) {
            return new HostSpecifier(domain.toString());
        }
        throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + host);
    }
    
    public static HostSpecifier from(final String specifier) throws ParseException {
        try {
            return fromValid(specifier);
        }
        catch (IllegalArgumentException e) {
            final ParseException parseException = new ParseException("Invalid host specifier: " + specifier, 0);
            parseException.initCause(e);
            throw parseException;
        }
    }
    
    public static boolean isValid(final String specifier) {
        try {
            fromValid(specifier);
            return true;
        }
        catch (IllegalArgumentException e) {
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
