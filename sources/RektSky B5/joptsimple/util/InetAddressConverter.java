/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import joptsimple.internal.Messages;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class InetAddressConverter
implements ValueConverter<InetAddress> {
    @Override
    public InetAddress convert(String value) {
        try {
            return InetAddress.getByName(value);
        }
        catch (UnknownHostException e2) {
            throw new ValueConversionException(this.message(value));
        }
    }

    @Override
    public Class<InetAddress> valueType() {
        return InetAddress.class;
    }

    @Override
    public String valuePattern() {
        return null;
    }

    private String message(String value) {
        return Messages.message(Locale.getDefault(), "joptsimple.ExceptionMessages", InetAddressConverter.class, "message", value);
    }
}

