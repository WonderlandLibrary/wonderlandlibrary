/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.duration.impl.DataRecord;
import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import com.ibm.icu.impl.duration.impl.XMLRecordReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ResourceBasedPeriodFormatterDataService
extends PeriodFormatterDataService {
    private Collection<String> availableLocales;
    private PeriodFormatterData lastData = null;
    private String lastLocale = null;
    private Map<String, PeriodFormatterData> cache = new HashMap<String, PeriodFormatterData>();
    private static final String PATH = "data/";
    private static final ResourceBasedPeriodFormatterDataService singleton = new ResourceBasedPeriodFormatterDataService();

    public static ResourceBasedPeriodFormatterDataService getInstance() {
        return singleton;
    }

    private ResourceBasedPeriodFormatterDataService() {
        ArrayList<String> localeNames = new ArrayList<String>();
        InputStream is = ICUData.getRequiredStream(this.getClass(), "data/index.txt");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String string = null;
            while (null != (string = br.readLine())) {
                if ((string = string.trim()).startsWith("#") || string.length() == 0) continue;
                localeNames.add(string);
            }
            br.close();
        }
        catch (IOException e) {
            throw new IllegalStateException("IO Error reading data/index.txt: " + e.toString());
        }
        this.availableLocales = Collections.unmodifiableList(localeNames);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public PeriodFormatterData get(String localeName) {
        int x = localeName.indexOf(64);
        if (x != -1) {
            localeName = localeName.substring(0, x);
        }
        ResourceBasedPeriodFormatterDataService resourceBasedPeriodFormatterDataService = this;
        synchronized (resourceBasedPeriodFormatterDataService) {
            if (this.lastLocale != null && this.lastLocale.equals(localeName)) {
                return this.lastData;
            }
            PeriodFormatterData ld = this.cache.get(localeName);
            if (ld == null) {
                String ln = localeName;
                while (!this.availableLocales.contains(ln)) {
                    int ix = ln.lastIndexOf("_");
                    if (ix > -1) {
                        ln = ln.substring(0, ix);
                        continue;
                    }
                    if (!"test".equals(ln)) {
                        ln = "test";
                        continue;
                    }
                    ln = null;
                    break;
                }
                if (ln == null) throw new MissingResourceException("Duration data not found for  " + localeName, PATH, localeName);
                String name = "data/pfd_" + ln + ".xml";
                try {
                    InputStream is = ICUData.getStream(this.getClass(), name);
                    if (is == null) {
                        throw new MissingResourceException("no resource named " + name, name, "");
                    }
                    DataRecord dr = DataRecord.read(ln, new XMLRecordReader(new InputStreamReader(is, "UTF-8")));
                    if (dr != null) {
                        ld = new PeriodFormatterData(localeName, dr);
                    }
                }
                catch (UnsupportedEncodingException e) {
                    throw new MissingResourceException("Unhandled Encoding for resource " + name, name, "");
                }
                this.cache.put(localeName, ld);
            }
            this.lastData = ld;
            this.lastLocale = localeName;
            return ld;
        }
    }

    @Override
    public Collection<String> getAvailableLocales() {
        return this.availableLocales;
    }
}

