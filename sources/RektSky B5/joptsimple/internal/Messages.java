/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Messages {
    private Messages() {
        throw new UnsupportedOperationException();
    }

    public static String message(Locale locale, String bundleName, Class<?> type, String key, Object ... args) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        String template = bundle.getString(type.getName() + '.' + key);
        MessageFormat format = new MessageFormat(template);
        format.setLocale(locale);
        return format.format(args);
    }
}

