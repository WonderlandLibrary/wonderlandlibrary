/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.options;

import java.io.PrintWriter;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyPermission;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import jdk.nashorn.internal.runtime.QuotedStringTokenizer;
import jdk.nashorn.internal.runtime.options.KeyValueOption;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import jdk.nashorn.internal.runtime.options.Option;
import jdk.nashorn.internal.runtime.options.OptionTemplate;

public final class Options {
    private static final AccessControlContext READ_PROPERTY_ACC_CTXT = Options.createPropertyReadAccCtxt();
    private final String resource;
    private final PrintWriter err;
    private final List<String> files;
    private final List<String> arguments;
    private final TreeMap<String, Option<?>> options;
    private static final String NASHORN_ARGS_PREPEND_PROPERTY = "nashorn.args.prepend";
    private static final String NASHORN_ARGS_PROPERTY = "nashorn.args";
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.internal.runtime.resources.Options";
    private static ResourceBundle bundle = ResourceBundle.getBundle("jdk.nashorn.internal.runtime.resources.Options", Locale.getDefault());
    private static HashMap<Object, Object> usage;
    private static Collection<OptionTemplate> validOptions;
    private static OptionTemplate helpOptionTemplate;
    private static OptionTemplate definePropTemplate;
    private static String definePropPrefix;

    private static AccessControlContext createPropertyReadAccCtxt() {
        Permissions perms = new Permissions();
        perms.add(new PropertyPermission("nashorn.*", "read"));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    public Options(String resource) {
        this(resource, new PrintWriter(System.err, true));
    }

    public Options(String resource, PrintWriter err) {
        this.resource = resource;
        this.err = err;
        this.files = new ArrayList<String>();
        this.arguments = new ArrayList<String>();
        this.options = new TreeMap();
        for (OptionTemplate t : validOptions) {
            if (t.getDefaultValue() == null) continue;
            String v = Options.getStringProperty(t.getKey(), null);
            if (v != null) {
                this.set(t.getKey(), Options.createOption(t, v));
                continue;
            }
            if (t.getDefaultValue() == null) continue;
            this.set(t.getKey(), Options.createOption(t, t.getDefaultValue()));
        }
    }

    public String getResource() {
        return this.resource;
    }

    public String toString() {
        return this.options.toString();
    }

    private static void checkPropertyName(String name) {
        if (!Objects.requireNonNull(name).startsWith("nashorn.")) {
            throw new IllegalArgumentException(name);
        }
    }

    public static boolean getBooleanProperty(final String name, final Boolean defValue) {
        Options.checkPropertyName(name);
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>(){

            @Override
            public Boolean run() {
                try {
                    String property = System.getProperty(name);
                    if (property == null && defValue != null) {
                        return defValue;
                    }
                    return property != null && !"false".equalsIgnoreCase(property);
                }
                catch (SecurityException e) {
                    return false;
                }
            }
        }, READ_PROPERTY_ACC_CTXT);
    }

    public static boolean getBooleanProperty(String name) {
        return Options.getBooleanProperty(name, null);
    }

    public static String getStringProperty(final String name, final String defValue) {
        Options.checkPropertyName(name);
        return AccessController.doPrivileged(new PrivilegedAction<String>(){

            @Override
            public String run() {
                try {
                    return System.getProperty(name, defValue);
                }
                catch (SecurityException e) {
                    return defValue;
                }
            }
        }, READ_PROPERTY_ACC_CTXT);
    }

    public static int getIntProperty(final String name, final int defValue) {
        Options.checkPropertyName(name);
        return AccessController.doPrivileged(new PrivilegedAction<Integer>(){

            @Override
            public Integer run() {
                try {
                    return Integer.getInteger(name, defValue);
                }
                catch (SecurityException e) {
                    return defValue;
                }
            }
        }, READ_PROPERTY_ACC_CTXT);
    }

    public Option<?> get(String key) {
        return this.options.get(this.key(key));
    }

    public boolean getBoolean(String key) {
        Option<?> option = this.get(key);
        return option != null ? (Boolean)option.getValue() : false;
    }

    public int getInteger(String key) {
        Option<?> option = this.get(key);
        return option != null ? (Integer)option.getValue() : 0;
    }

    public String getString(String key) {
        String value;
        Option<?> option = this.get(key);
        if (option != null && (value = (String)option.getValue()) != null) {
            return value.intern();
        }
        return null;
    }

    public void set(String key, Option<?> option) {
        this.options.put(this.key(key), option);
    }

    public void set(String key, boolean option) {
        this.set(key, new Option<Boolean>(option));
    }

    public void set(String key, String option) {
        this.set(key, new Option<String>(option));
    }

    public List<String> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    public List<String> getFiles() {
        return Collections.unmodifiableList(this.files);
    }

    public static Collection<OptionTemplate> getValidOptions() {
        return Collections.unmodifiableCollection(validOptions);
    }

    private String key(String shortKey) {
        String keyPrefix;
        String key = shortKey;
        while (key.startsWith("-")) {
            key = key.substring(1, key.length());
        }
        if ((key = key.replace("-", ".")).startsWith(keyPrefix = this.resource + ".option.")) {
            return key;
        }
        return keyPrefix + key;
    }

    static String getMsg(String msgId, String ... args2) {
        try {
            String msg = bundle.getString(msgId);
            if (args2.length == 0) {
                return msg;
            }
            return new MessageFormat(msg).format(args2);
        }
        catch (MissingResourceException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void displayHelp(IllegalArgumentException e) {
        if (e instanceof IllegalOptionException) {
            OptionTemplate template = ((IllegalOptionException)e).getTemplate();
            if (template.isXHelp()) {
                this.displayHelp(true);
            } else {
                this.err.println(((IllegalOptionException)e).getTemplate());
            }
            return;
        }
        if (e != null && e.getMessage() != null) {
            this.err.println(Options.getMsg("option.error.invalid.option", e.getMessage(), helpOptionTemplate.getShortName(), helpOptionTemplate.getName()));
            this.err.println();
            return;
        }
        this.displayHelp(false);
    }

    public void displayHelp(boolean extended) {
        for (OptionTemplate t : validOptions) {
            if (!extended && t.isUndocumented() || !t.getResource().equals(this.resource)) continue;
            this.err.println(t);
            this.err.println();
        }
    }

    public void process(String[] args2) {
        LinkedList<String> argList = new LinkedList<String>();
        Options.addSystemProperties(NASHORN_ARGS_PREPEND_PROPERTY, argList);
        this.processArgList(argList);
        assert (argList.isEmpty());
        Collections.addAll(argList, args2);
        this.processArgList(argList);
        assert (argList.isEmpty());
        Options.addSystemProperties(NASHORN_ARGS_PROPERTY, argList);
        this.processArgList(argList);
        assert (argList.isEmpty());
    }

    private void processArgList(LinkedList<String> argList) {
        while (!argList.isEmpty()) {
            String arg = argList.remove(0);
            if (arg.isEmpty()) continue;
            if ("--".equals(arg)) {
                this.arguments.addAll(argList);
                argList.clear();
                continue;
            }
            if (!arg.startsWith("-") || arg.length() == 1) {
                this.files.add(arg);
                continue;
            }
            if (arg.startsWith(definePropPrefix)) {
                String value = arg.substring(definePropPrefix.length());
                int eq = value.indexOf(61);
                if (eq != -1) {
                    System.setProperty(value.substring(0, eq), value.substring(eq + 1));
                    continue;
                }
                if (!value.isEmpty()) {
                    System.setProperty(value, "");
                    continue;
                }
                throw new IllegalOptionException(definePropTemplate);
            }
            ParsedArg parg = new ParsedArg(arg);
            if (parg.template.isValueNextArg()) {
                if (argList.isEmpty()) {
                    throw new IllegalOptionException(parg.template);
                }
                parg.value = argList.remove(0);
            }
            if (parg.template.isHelp()) {
                if (!argList.isEmpty()) {
                    OptionTemplate t = new ParsedArg((String)argList.get((int)0)).template;
                    throw new IllegalOptionException(t);
                }
                throw new IllegalArgumentException();
            }
            if (parg.template.isXHelp()) {
                throw new IllegalOptionException(parg.template);
            }
            this.set(parg.template.getKey(), Options.createOption(parg.template, parg.value));
            if (parg.template.getDependency() == null) continue;
            argList.addFirst(parg.template.getDependency());
        }
    }

    private static void addSystemProperties(String sysPropName, List<String> argList) {
        String sysArgs = Options.getStringProperty(sysPropName, null);
        if (sysArgs != null) {
            StringTokenizer st = new StringTokenizer(sysArgs);
            while (st.hasMoreTokens()) {
                argList.add(st.nextToken());
            }
        }
    }

    public OptionTemplate getOptionTemplateByKey(String shortKey) {
        String fullKey = this.key(shortKey);
        for (OptionTemplate t : validOptions) {
            if (!t.getKey().equals(fullKey)) continue;
            return t;
        }
        throw new IllegalArgumentException(shortKey);
    }

    private static OptionTemplate getOptionTemplateByName(String name) {
        for (OptionTemplate t : validOptions) {
            if (!t.nameMatches(name)) continue;
            return t;
        }
        return null;
    }

    private static Option<?> createOption(OptionTemplate t, String value) {
        switch (t.getType()) {
            case "string": {
                return new Option<String>(value);
            }
            case "timezone": {
                return new Option<TimeZone>(TimeZone.getTimeZone(value));
            }
            case "locale": {
                return new Option<Locale>(Locale.forLanguageTag(value));
            }
            case "keyvalues": {
                return new KeyValueOption(value);
            }
            case "log": {
                return new LoggingOption(value);
            }
            case "boolean": {
                return new Option<Boolean>(value != null && Boolean.parseBoolean(value));
            }
            case "integer": {
                try {
                    return new Option<Integer>(value == null ? 0 : Integer.parseInt(value));
                }
                catch (NumberFormatException nfe) {
                    throw new IllegalOptionException(t);
                }
            }
            case "properties": {
                Options.initProps(new KeyValueOption(value));
                return null;
            }
        }
        throw new IllegalArgumentException(value);
    }

    private static void initProps(KeyValueOption kv) {
        for (Map.Entry<String, String> entry : kv.getValues().entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    static {
        validOptions = new TreeSet<OptionTemplate>();
        usage = new HashMap();
        Enumeration<String> keys2 = bundle.getKeys();
        while (keys2.hasMoreElements()) {
            String key = keys2.nextElement();
            StringTokenizer st = new StringTokenizer(key, ".");
            String resource = null;
            String type = null;
            if (st.countTokens() > 0) {
                resource = st.nextToken();
            }
            if (st.countTokens() > 0) {
                type = st.nextToken();
            }
            if ("option".equals(type)) {
                String helpKey = null;
                String xhelpKey = null;
                String definePropKey = null;
                try {
                    helpKey = bundle.getString(resource + ".options.help.key");
                    xhelpKey = bundle.getString(resource + ".options.xhelp.key");
                    definePropKey = bundle.getString(resource + ".options.D.key");
                }
                catch (MissingResourceException missingResourceException) {
                    // empty catch block
                }
                boolean isHelp = key.equals(helpKey);
                boolean isXHelp = key.equals(xhelpKey);
                OptionTemplate t = new OptionTemplate(resource, key, bundle.getString(key), isHelp, isXHelp);
                validOptions.add(t);
                if (isHelp) {
                    helpOptionTemplate = t;
                }
                if (!key.equals(definePropKey)) continue;
                definePropPrefix = t.getName();
                definePropTemplate = t;
                continue;
            }
            if (resource == null || !"options".equals(type)) continue;
            usage.put(resource, bundle.getObject(key));
        }
    }

    private static class ParsedArg {
        OptionTemplate template;
        String value;

        ParsedArg(String argument) {
            QuotedStringTokenizer st = new QuotedStringTokenizer(argument, "=");
            if (!st.hasMoreTokens()) {
                throw new IllegalArgumentException();
            }
            String token = st.nextToken();
            this.template = Options.getOptionTemplateByName(token);
            if (this.template == null) {
                throw new IllegalArgumentException(argument);
            }
            this.value = "";
            if (st.hasMoreTokens()) {
                while (st.hasMoreTokens()) {
                    this.value = this.value + st.nextToken();
                    if (!st.hasMoreTokens()) continue;
                    this.value = this.value + ':';
                }
            } else if ("boolean".equals(this.template.getType())) {
                this.value = "true";
            }
        }
    }

    private static class IllegalOptionException
    extends IllegalArgumentException {
        private final OptionTemplate template;

        IllegalOptionException(OptionTemplate t) {
            this.template = t;
        }

        OptionTemplate getTemplate() {
            return this.template;
        }
    }
}

