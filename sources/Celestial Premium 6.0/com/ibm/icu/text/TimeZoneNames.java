/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TimeZoneNamesImpl;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class TimeZoneNames
implements Serializable {
    private static final long serialVersionUID = -9180227029248969153L;
    private static Cache TZNAMES_CACHE = new Cache();
    private static final Factory TZNAMES_FACTORY;
    private static final String FACTORY_NAME_PROP = "com.ibm.icu.text.TimeZoneNames.Factory.impl";
    private static final String DEFAULT_FACTORY_CLASS = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";

    public static TimeZoneNames getInstance(ULocale locale) {
        String key = locale.getBaseName();
        return (TimeZoneNames)TZNAMES_CACHE.getInstance(key, locale);
    }

    public abstract Set<String> getAvailableMetaZoneIDs();

    public abstract Set<String> getAvailableMetaZoneIDs(String var1);

    public abstract String getMetaZoneID(String var1, long var2);

    public abstract String getReferenceZoneID(String var1, String var2);

    public abstract String getMetaZoneDisplayName(String var1, NameType var2);

    public final String getDisplayName(String tzID, NameType type, long date) {
        String name = this.getTimeZoneDisplayName(tzID, type);
        if (name == null) {
            String mzID = this.getMetaZoneID(tzID, date);
            name = this.getMetaZoneDisplayName(mzID, type);
        }
        return name;
    }

    public abstract String getTimeZoneDisplayName(String var1, NameType var2);

    public String getExemplarLocationName(String tzID) {
        return TimeZoneNamesImpl.getDefaultExemplarLocationName(tzID);
    }

    public Collection<MatchInfo> find(CharSequence text, int start, EnumSet<NameType> types) {
        throw new UnsupportedOperationException("The method is not implemented in TimeZoneNames base class.");
    }

    protected TimeZoneNames() {
    }

    static {
        Factory factory = null;
        String classname = ICUConfig.get("com.ibm.icu.text.TimeZoneNames.Factory.impl", "com.ibm.icu.impl.TimeZoneNamesFactoryImpl");
        while (true) {
            try {
                factory = (Factory)Class.forName(classname).newInstance();
                break;
            }
            catch (ClassNotFoundException cnfe) {
            }
            catch (IllegalAccessException iae) {
            }
            catch (InstantiationException instantiationException) {
                // empty catch block
            }
            if (classname.equals("com.ibm.icu.impl.TimeZoneNamesFactoryImpl")) break;
            classname = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
        }
        if (factory == null) {
            factory = new DefaultTimeZoneNames.FactoryImpl();
        }
        TZNAMES_FACTORY = factory;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class DefaultTimeZoneNames
    extends TimeZoneNames {
        private static final long serialVersionUID = -995672072494349071L;
        public static final DefaultTimeZoneNames INSTANCE = new DefaultTimeZoneNames();

        private DefaultTimeZoneNames() {
        }

        @Override
        public Set<String> getAvailableMetaZoneIDs() {
            return Collections.emptySet();
        }

        @Override
        public Set<String> getAvailableMetaZoneIDs(String tzID) {
            return Collections.emptySet();
        }

        @Override
        public String getMetaZoneID(String tzID, long date) {
            return null;
        }

        @Override
        public String getReferenceZoneID(String mzID, String region) {
            return null;
        }

        @Override
        public String getMetaZoneDisplayName(String mzID, NameType type) {
            return null;
        }

        @Override
        public String getTimeZoneDisplayName(String tzID, NameType type) {
            return null;
        }

        @Override
        public Collection<MatchInfo> find(CharSequence text, int start, EnumSet<NameType> nameTypes) {
            return Collections.emptyList();
        }

        public static class FactoryImpl
        extends Factory {
            public TimeZoneNames getTimeZoneNames(ULocale locale) {
                return INSTANCE;
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class Cache
    extends SoftCache<String, TimeZoneNames, ULocale> {
        private Cache() {
        }

        @Override
        protected TimeZoneNames createInstance(String key, ULocale data) {
            return TZNAMES_FACTORY.getTimeZoneNames(data);
        }
    }

    public static abstract class Factory {
        public abstract TimeZoneNames getTimeZoneNames(ULocale var1);
    }

    public static class MatchInfo {
        private NameType _nameType;
        private String _tzID;
        private String _mzID;
        private int _matchLength;

        public MatchInfo(NameType nameType, String tzID, String mzID, int matchLength) {
            if (nameType == null) {
                throw new IllegalArgumentException("nameType is null");
            }
            if (tzID == null && mzID == null) {
                throw new IllegalArgumentException("Either tzID or mzID must be available");
            }
            if (matchLength <= 0) {
                throw new IllegalArgumentException("matchLength must be positive value");
            }
            this._nameType = nameType;
            this._tzID = tzID;
            this._mzID = mzID;
            this._matchLength = matchLength;
        }

        public String tzID() {
            return this._tzID;
        }

        public String mzID() {
            return this._mzID;
        }

        public NameType nameType() {
            return this._nameType;
        }

        public int matchLength() {
            return this._matchLength;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum NameType {
        LONG_GENERIC,
        LONG_STANDARD,
        LONG_DAYLIGHT,
        SHORT_GENERIC,
        SHORT_STANDARD,
        SHORT_DAYLIGHT,
        EXEMPLAR_LOCATION;

    }
}

