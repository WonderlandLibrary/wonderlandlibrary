/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.util.ULocale;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public abstract class LocaleDisplayNames {
    private static final Method FACTORY_DIALECTHANDLING;
    private static final Method FACTORY_DISPLAYCONTEXT;

    public static LocaleDisplayNames getInstance(ULocale locale) {
        return LocaleDisplayNames.getInstance(locale, DialectHandling.STANDARD_NAMES);
    }

    public static LocaleDisplayNames getInstance(Locale locale) {
        return LocaleDisplayNames.getInstance(ULocale.forLocale(locale));
    }

    public static LocaleDisplayNames getInstance(ULocale locale, DialectHandling dialectHandling) {
        LocaleDisplayNames result = null;
        if (FACTORY_DIALECTHANDLING != null) {
            try {
                result = (LocaleDisplayNames)FACTORY_DIALECTHANDLING.invoke(null, new Object[]{locale, dialectHandling});
            }
            catch (InvocationTargetException invocationTargetException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        if (result == null) {
            result = new LastResortLocaleDisplayNames(locale, dialectHandling);
        }
        return result;
    }

    public static LocaleDisplayNames getInstance(ULocale locale, DisplayContext ... contexts) {
        LocaleDisplayNames result = null;
        if (FACTORY_DISPLAYCONTEXT != null) {
            try {
                result = (LocaleDisplayNames)FACTORY_DISPLAYCONTEXT.invoke(null, new Object[]{locale, contexts});
            }
            catch (InvocationTargetException invocationTargetException) {
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
        if (result == null) {
            result = new LastResortLocaleDisplayNames(locale, contexts);
        }
        return result;
    }

    public static LocaleDisplayNames getInstance(Locale locale, DisplayContext ... contexts) {
        return LocaleDisplayNames.getInstance(ULocale.forLocale(locale), contexts);
    }

    public abstract ULocale getLocale();

    public abstract DialectHandling getDialectHandling();

    public abstract DisplayContext getContext(DisplayContext.Type var1);

    public abstract String localeDisplayName(ULocale var1);

    public abstract String localeDisplayName(Locale var1);

    public abstract String localeDisplayName(String var1);

    public abstract String languageDisplayName(String var1);

    public abstract String scriptDisplayName(String var1);

    @Deprecated
    public String scriptDisplayNameInContext(String script) {
        return this.scriptDisplayName(script);
    }

    public abstract String scriptDisplayName(int var1);

    public abstract String regionDisplayName(String var1);

    public abstract String variantDisplayName(String var1);

    public abstract String keyDisplayName(String var1);

    public abstract String keyValueDisplayName(String var1, String var2);

    public List<UiListItem> getUiList(Set<ULocale> localeSet, boolean inSelf, Comparator<Object> collator) {
        return this.getUiListCompareWholeItems(localeSet, UiListItem.getComparator(collator, inSelf));
    }

    public abstract List<UiListItem> getUiListCompareWholeItems(Set<ULocale> var1, Comparator<UiListItem> var2);

    @Deprecated
    protected LocaleDisplayNames() {
    }

    static {
        String implClassName = ICUConfig.get("com.ibm.icu.text.LocaleDisplayNames.impl", "com.ibm.icu.impl.LocaleDisplayNamesImpl");
        Method factoryDialectHandling = null;
        Method factoryDisplayContext = null;
        try {
            Class<?> implClass = Class.forName(implClassName);
            try {
                factoryDialectHandling = implClass.getMethod("getInstance", ULocale.class, DialectHandling.class);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            try {
                factoryDisplayContext = implClass.getMethod("getInstance", ULocale.class, DisplayContext[].class);
            }
            catch (NoSuchMethodException noSuchMethodException) {}
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        FACTORY_DIALECTHANDLING = factoryDialectHandling;
        FACTORY_DISPLAYCONTEXT = factoryDisplayContext;
    }

    private static class LastResortLocaleDisplayNames
    extends LocaleDisplayNames {
        private ULocale locale;
        private DisplayContext[] contexts;

        private LastResortLocaleDisplayNames(ULocale locale, DialectHandling dialectHandling) {
            this.locale = locale;
            DisplayContext context = dialectHandling == DialectHandling.DIALECT_NAMES ? DisplayContext.DIALECT_NAMES : DisplayContext.STANDARD_NAMES;
            this.contexts = new DisplayContext[]{context};
        }

        private LastResortLocaleDisplayNames(ULocale locale, DisplayContext ... contexts) {
            this.locale = locale;
            this.contexts = new DisplayContext[contexts.length];
            System.arraycopy(contexts, 0, this.contexts, 0, contexts.length);
        }

        @Override
        public ULocale getLocale() {
            return this.locale;
        }

        @Override
        public DialectHandling getDialectHandling() {
            DialectHandling result = DialectHandling.STANDARD_NAMES;
            for (DisplayContext context : this.contexts) {
                if (context.type() != DisplayContext.Type.DIALECT_HANDLING || context.value() != DisplayContext.DIALECT_NAMES.ordinal()) continue;
                result = DialectHandling.DIALECT_NAMES;
                break;
            }
            return result;
        }

        @Override
        public DisplayContext getContext(DisplayContext.Type type) {
            DisplayContext result = DisplayContext.STANDARD_NAMES;
            for (DisplayContext context : this.contexts) {
                if (context.type() != type) continue;
                result = context;
                break;
            }
            return result;
        }

        @Override
        public String localeDisplayName(ULocale locale) {
            return locale.getName();
        }

        @Override
        public String localeDisplayName(Locale locale) {
            return ULocale.forLocale(locale).getName();
        }

        @Override
        public String localeDisplayName(String localeId) {
            return new ULocale(localeId).getName();
        }

        @Override
        public String languageDisplayName(String lang) {
            return lang;
        }

        @Override
        public String scriptDisplayName(String script) {
            return script;
        }

        @Override
        public String scriptDisplayName(int scriptCode) {
            return UScript.getShortName(scriptCode);
        }

        @Override
        public String regionDisplayName(String region) {
            return region;
        }

        @Override
        public String variantDisplayName(String variant) {
            return variant;
        }

        @Override
        public String keyDisplayName(String key) {
            return key;
        }

        @Override
        public String keyValueDisplayName(String key, String value) {
            return value;
        }

        @Override
        public List<UiListItem> getUiListCompareWholeItems(Set<ULocale> localeSet, Comparator<UiListItem> comparator) {
            return Collections.emptyList();
        }
    }

    public static class UiListItem {
        public final ULocale minimized;
        public final ULocale modified;
        public final String nameInDisplayLocale;
        public final String nameInSelf;

        public UiListItem(ULocale minimized, ULocale modified, String nameInDisplayLocale, String nameInSelf) {
            this.minimized = minimized;
            this.modified = modified;
            this.nameInDisplayLocale = nameInDisplayLocale;
            this.nameInSelf = nameInSelf;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof UiListItem)) {
                return false;
            }
            UiListItem other = (UiListItem)obj;
            return this.nameInDisplayLocale.equals(other.nameInDisplayLocale) && this.nameInSelf.equals(other.nameInSelf) && this.minimized.equals(other.minimized) && this.modified.equals(other.modified);
        }

        public int hashCode() {
            return this.modified.hashCode() ^ this.nameInDisplayLocale.hashCode();
        }

        public String toString() {
            return "{" + this.minimized + ", " + this.modified + ", " + this.nameInDisplayLocale + ", " + this.nameInSelf + "}";
        }

        public static Comparator<UiListItem> getComparator(Comparator<Object> comparator, boolean inSelf) {
            return new UiListItemComparator(comparator, inSelf);
        }

        private static class UiListItemComparator
        implements Comparator<UiListItem> {
            private final Comparator<Object> collator;
            private final boolean useSelf;

            UiListItemComparator(Comparator<Object> collator, boolean useSelf) {
                this.collator = collator;
                this.useSelf = useSelf;
            }

            @Override
            public int compare(UiListItem o1, UiListItem o2) {
                int result = this.useSelf ? this.collator.compare(o1.nameInSelf, o2.nameInSelf) : this.collator.compare(o1.nameInDisplayLocale, o2.nameInDisplayLocale);
                return result != 0 ? result : o1.modified.compareTo(o2.modified);
            }
        }
    }

    public static enum DialectHandling {
        STANDARD_NAMES,
        DIALECT_NAMES;

    }
}

