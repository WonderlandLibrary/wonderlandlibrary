// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.MissingResourceException;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedMessage implements Message, LoggerNameAwareMessage
{
    private static final long serialVersionUID = 3893703791567290742L;
    private String baseName;
    private transient ResourceBundle resourceBundle;
    private final Locale locale;
    private transient StatusLogger logger;
    private String loggerName;
    private String key;
    private String[] stringArgs;
    private transient Object[] argArray;
    private String formattedMessage;
    private transient Throwable throwable;
    
    public LocalizedMessage(final String messagePattern, final Object[] arguments) {
        this((ResourceBundle)null, null, messagePattern, arguments);
    }
    
    public LocalizedMessage(final String baseName, final String key, final Object[] arguments) {
        this(baseName, null, key, arguments);
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key, final Object[] arguments) {
        this(bundle, null, key, arguments);
    }
    
    public LocalizedMessage(final String baseName, final Locale locale, final String key, final Object[] arguments) {
        this.logger = StatusLogger.getLogger();
        this.key = key;
        this.argArray = arguments;
        this.throwable = null;
        this.baseName = baseName;
        this.resourceBundle = null;
        this.locale = locale;
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final Locale locale, final String key, final Object[] arguments) {
        this.logger = StatusLogger.getLogger();
        this.key = key;
        this.argArray = arguments;
        this.throwable = null;
        this.baseName = null;
        this.resourceBundle = bundle;
        this.locale = locale;
    }
    
    public LocalizedMessage(final Locale locale, final String key, final Object[] arguments) {
        this((ResourceBundle)null, locale, key, arguments);
    }
    
    public LocalizedMessage(final String messagePattern, final Object arg) {
        this((ResourceBundle)null, null, messagePattern, new Object[] { arg });
    }
    
    public LocalizedMessage(final String baseName, final String key, final Object arg) {
        this(baseName, null, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key) {
        this(bundle, null, key, new Object[0]);
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key, final Object arg) {
        this(bundle, null, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final String baseName, final Locale locale, final String key, final Object arg) {
        this(baseName, locale, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final Locale locale, final String key, final Object arg) {
        this(bundle, locale, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final Locale locale, final String key, final Object arg) {
        this((ResourceBundle)null, locale, key, new Object[] { arg });
    }
    
    public LocalizedMessage(final String messagePattern, final Object arg1, final Object arg2) {
        this((ResourceBundle)null, null, messagePattern, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final String baseName, final String key, final Object arg1, final Object arg2) {
        this(baseName, null, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final String key, final Object arg1, final Object arg2) {
        this(bundle, null, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final String baseName, final Locale locale, final String key, final Object arg1, final Object arg2) {
        this(baseName, locale, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final ResourceBundle bundle, final Locale locale, final String key, final Object arg1, final Object arg2) {
        this(bundle, locale, key, new Object[] { arg1, arg2 });
    }
    
    public LocalizedMessage(final Locale locale, final String key, final Object arg1, final Object arg2) {
        this((ResourceBundle)null, locale, key, new Object[] { arg1, arg2 });
    }
    
    @Override
    public void setLoggerName(final String name) {
        this.loggerName = name;
    }
    
    @Override
    public String getLoggerName() {
        return this.loggerName;
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        ResourceBundle bundle = this.resourceBundle;
        if (bundle == null) {
            if (this.baseName != null) {
                bundle = this.getResourceBundle(this.baseName, this.locale, false);
            }
            else {
                bundle = this.getResourceBundle(this.loggerName, this.locale, true);
            }
        }
        final String myKey = this.getFormat();
        final String msgPattern = (bundle == null || !bundle.containsKey(myKey)) ? myKey : bundle.getString(myKey);
        final Object[] array = (this.argArray == null) ? this.stringArgs : this.argArray;
        final FormattedMessage msg = new FormattedMessage(msgPattern, array);
        this.formattedMessage = msg.getFormattedMessage();
        this.throwable = msg.getThrowable();
        return this.formattedMessage;
    }
    
    @Override
    public String getFormat() {
        return this.key;
    }
    
    @Override
    public Object[] getParameters() {
        if (this.argArray != null) {
            return this.argArray;
        }
        return this.stringArgs;
    }
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    protected ResourceBundle getResourceBundle(final String rbBaseName, final Locale resourceBundleLocale, final boolean loop) {
        ResourceBundle rb = null;
        if (rbBaseName == null) {
            return null;
        }
        try {
            if (resourceBundleLocale != null) {
                rb = ResourceBundle.getBundle(rbBaseName, resourceBundleLocale);
            }
            else {
                rb = ResourceBundle.getBundle(rbBaseName);
            }
        }
        catch (MissingResourceException ex) {
            if (!loop) {
                this.logger.debug("Unable to locate ResourceBundle " + rbBaseName);
                return null;
            }
        }
        String substr = rbBaseName;
        int i;
        while (rb == null && (i = substr.lastIndexOf(46)) > 0) {
            substr = substr.substring(0, i);
            try {
                if (resourceBundleLocale != null) {
                    rb = ResourceBundle.getBundle(substr, resourceBundleLocale);
                }
                else {
                    rb = ResourceBundle.getBundle(substr);
                }
            }
            catch (MissingResourceException ex2) {
                this.logger.debug("Unable to locate ResourceBundle " + substr);
            }
        }
        return rb;
    }
    
    @Override
    public String toString() {
        return this.getFormattedMessage();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        this.getFormattedMessage();
        out.writeUTF(this.formattedMessage);
        out.writeUTF(this.key);
        out.writeUTF(this.baseName);
        out.writeInt(this.argArray.length);
        this.stringArgs = new String[this.argArray.length];
        int i = 0;
        for (final Object obj : this.argArray) {
            this.stringArgs[i] = obj.toString();
            ++i;
        }
        out.writeObject(this.stringArgs);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.formattedMessage = in.readUTF();
        this.key = in.readUTF();
        this.baseName = in.readUTF();
        in.readInt();
        this.stringArgs = (String[])in.readObject();
        this.logger = StatusLogger.getLogger();
        this.resourceBundle = null;
        this.argArray = null;
    }
}
