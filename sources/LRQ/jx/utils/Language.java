/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.Charsets
 *  kotlin.text.StringsKt
 */
package jx.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jx.utils.LanguageManager;
import kotlin.TypeCastException;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

public final class Language {
    private final HashMap<String, String> translateMap;
    private final String locale;

    private final InputStream find() {
        String str;
        boolean bl;
        Object object;
        List split = StringsKt.split$default((CharSequence)StringsKt.replace$default((String)this.locale, (String)"-", (String)"_", (boolean)false, (int)4, null), (String[])new String[]{"_"}, (boolean)false, (int)0, (int)6, null);
        if (split.size() > 1) {
            object = (String)split.get(0);
            StringBuilder stringBuilder = new StringBuilder();
            bl = false;
            String string = object;
            if (string == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string2 = string.toLowerCase();
            object = (String)split.get(1);
            stringBuilder = stringBuilder.append(string2).append("-");
            bl = false;
            Object object2 = object;
            if (object2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string2 = ((String)object2).toUpperCase();
            str = stringBuilder.append(string2).toString();
            InputStream inputStream = LanguageManager.class.getClassLoader().getResourceAsStream("assets/minecraft/liquidbounce/translations/" + str + "/source.properties");
            if (inputStream != null) {
                object = inputStream;
                bl = false;
                boolean bl2 = false;
                Object it = object;
                boolean bl3 = false;
                return it;
            }
        }
        object = (String)split.get(0);
        bl = false;
        String string = object;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        str = string.toLowerCase();
        InputStream inputStream = LanguageManager.class.getClassLoader().getResourceAsStream("assets/minecraft/liquidbounce/translations/" + str + "/source.properties");
        if (inputStream != null) {
            object = inputStream;
            bl = false;
            boolean bl4 = false;
            Object it = object;
            boolean bl5 = false;
            return it;
        }
        InputStream inputStream2 = LanguageManager.class.getClassLoader().getResourceAsStream("assets/minecraft/liquidbounce/translations/source.properties");
        if (inputStream2 != null) {
            object = inputStream2;
            bl = false;
            boolean bl6 = false;
            Object it = object;
            boolean bl7 = false;
            return it;
        }
        throw (Throwable)new IllegalStateException("Can't find language file! Try sync gitsubmodule if this is a custom build!");
    }

    /*
     * WARNING - void declaration
     */
    private final void read(String locale) {
        Properties prop = new Properties();
        prop.load(new InputStreamReader(this.find(), Charsets.UTF_8));
        Iterator iterator = prop.entrySet().iterator();
        while (iterator.hasNext()) {
            void key;
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator.next();
            boolean bl = false;
            Object k = entry2.getKey();
            entry2 = entry;
            bl = false;
            Object value = entry2.getValue();
            if (!(key instanceof String) || !(value instanceof String)) continue;
            ((Map)this.translateMap).put(key, value);
        }
    }

    public final String get(String key) {
        String string = this.translateMap.get(key);
        if (string == null) {
            string = key;
        }
        return string;
    }

    public final String getLocale() {
        return this.locale;
    }

    public Language(String locale) {
        this.locale = locale;
        this.translateMap = new HashMap();
        this.read(this.locale);
    }
}

