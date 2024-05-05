/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.StringCompanionObject
 *  kotlin.text.StringsKt
 */
package jx.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jx.utils.Language;
import jx.utils.RegexUtils;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.StringsKt;

public final class LanguageManager {
    private static final String key = "%";
    private static final String defaultLocale = "en_us";
    private static Language language;
    private static final HashMap<String, String> cachedStrings;
    private static final Pattern pattern;
    public static final LanguageManager INSTANCE;

    public final String getKey() {
        return key;
    }

    public final String getDefaultLocale() {
        return defaultLocale;
    }

    public final Language getLanguage() {
        return language;
    }

    private final void setLanguage(Language value) {
        cachedStrings.clear();
        language = value;
    }

    public final String replace(String text) {
        if (!text.equals(key)) {
            return text;
        }
        if (cachedStrings.containsKey(text)) {
            String string = cachedStrings.get(text);
            if (string == null) {
                Intrinsics.throwNpe();
            }
            return string;
        }
        Matcher matcher = pattern.matcher(text);
        String result = text;
        String[] $this$forEach$iv = RegexUtils.INSTANCE.match(matcher);
        boolean $i$f$forEach = false;
        String[] stringArray = $this$forEach$iv;
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            String converted;
            String element$iv;
            String it = element$iv = stringArray[i];
            boolean bl = false;
            String string = it;
            int n2 = 1;
            int n3 = it.length() - 1;
            boolean bl2 = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String spliced = string2.substring(n2, n3);
            if (!(spliced.equals(converted = INSTANCE.get(spliced)) ^ true)) continue;
            result = StringsKt.replace$default((String)result, (String)it, (String)converted, (boolean)false, (int)4, null);
        }
        ((Map)cachedStrings).put(text, result);
        return result;
    }

    public final String get(String key) {
        return language.get(key);
    }

    /*
     * WARNING - void declaration
     */
    public final String getAndFormat(String key, Object ... argsIn) {
        List args = CollectionsKt.toMutableList((Collection)ArraysKt.toList((Object[])argsIn));
        Iterable $this$forEachIndexed$iv = args;
        boolean $i$f$forEachIndexed = false;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            void arg;
            int n = index$iv++;
            boolean bl = false;
            if (n < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int n2 = n;
            Object t = item$iv;
            int index = n2;
            boolean bl2 = false;
            if (!(arg instanceof String)) continue;
            args.set(index, INSTANCE.replace((String)arg));
        }
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        String string = this.get(key);
        Collection $this$toTypedArray$iv = args;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        Object[] objectArray = thisCollection$iv.toArray(new Object[0]);
        if (objectArray == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        Object[] objectArray2 = Arrays.copyOf(objectArray, objectArray.length);
        boolean bl = false;
        return String.format(string, Arrays.copyOf(objectArray2, objectArray2.length));
    }

    private LanguageManager() {
    }

    static {
        LanguageManager languageManager;
        INSTANCE = languageManager = new LanguageManager();
        key = key;
        defaultLocale = defaultLocale;
        language = new Language(defaultLocale);
        cachedStrings = new HashMap();
        pattern = Pattern.compile(key + "[A-Za-z0-9.]*" + key);
    }
}

