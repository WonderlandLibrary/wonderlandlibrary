/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.duration.impl.DataRecord;
import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import com.ibm.icu.impl.duration.impl.XMLRecordReader;
import com.ibm.icu.util.ICUUncheckedIOException;
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
        catch (IOException e2) {
            throw new IllegalStateException("IO Error reading data/index.txt: " + e2.toString());
        }
        finally {
            try {
                is.close();
            }
            catch (IOException iOException) {}
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
        int x2 = localeName.indexOf(64);
        if (x2 != -1) {
            localeName = localeName.substring(0, x2);
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
                    InputStreamReader reader = new InputStreamReader(ICUData.getRequiredStream(this.getClass(), name), "UTF-8");
                    DataRecord dr = DataRecord.read(ln, new XMLRecordReader(reader));
                    reader.close();
                    if (dr != null) {
                        ld = new PeriodFormatterData(localeName, dr);
                    }
                }
                catch (UnsupportedEncodingException e2) {
                    throw new MissingResourceException("Unhandled encoding for resource " + name, name, "");
                }
                catch (IOException e3) {
                    throw new ICUUncheckedIOException("Failed to close() resource " + name, e3);
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

