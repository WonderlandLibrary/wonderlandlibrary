/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.options.Options;
import jdk.nashorn.internal.runtime.regexp.JdkRegExp;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import jdk.nashorn.internal.runtime.regexp.RegExp;

public class RegExpFactory {
    private static final RegExpFactory instance;
    private static final String JDK = "jdk";
    private static final String JONI = "joni";
    private static final Map<String, RegExp> REGEXP_CACHE;

    public RegExp compile(String pattern, String flags) throws ParserException {
        return new JdkRegExp(pattern, flags);
    }

    public static RegExp create(String pattern, String flags) {
        String key = pattern + "/" + flags;
        RegExp regexp = REGEXP_CACHE.get(key);
        if (regexp == null) {
            regexp = instance.compile(pattern, flags);
            REGEXP_CACHE.put(key, regexp);
        }
        return regexp;
    }

    public static void validate(String pattern, String flags) throws ParserException {
        RegExpFactory.create(pattern, flags);
    }

    public static boolean usesJavaUtilRegex() {
        return instance != null && instance.getClass() == RegExpFactory.class;
    }

    static {
        String impl;
        REGEXP_CACHE = Collections.synchronizedMap(new WeakHashMap());
        switch (impl = Options.getStringProperty("nashorn.regexp.impl", JONI)) {
            case "joni": {
                instance = new JoniRegExp.Factory();
                break;
            }
            case "jdk": {
                instance = new RegExpFactory();
                break;
            }
            default: {
                instance = null;
                throw new InternalError("Unsupported RegExp factory: " + impl);
            }
        }
    }
}

