/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class OverlayBundle
extends ResourceBundle {
    private String[] baseNames;
    private Locale locale;
    private ResourceBundle[] bundles;

    public OverlayBundle(String[] baseNames, Locale locale) {
        this.baseNames = baseNames;
        this.locale = locale;
        this.bundles = new ResourceBundle[baseNames.length];
    }

    @Override
    protected Object handleGetObject(String key) throws MissingResourceException {
        Object o = null;
        for (int i = 0; i < this.bundles.length; ++i) {
            block3: {
                this.load(i);
                try {
                    o = this.bundles[i].getObject(key);
                }
                catch (MissingResourceException e) {
                    if (i != this.bundles.length - 1) break block3;
                    throw e;
                }
            }
            if (o != null) break;
        }
        return o;
    }

    @Override
    public Enumeration<String> getKeys() {
        int i = this.bundles.length - 1;
        this.load(i);
        return this.bundles[i].getKeys();
    }

    private void load(int i) throws MissingResourceException {
        block9: {
            if (this.bundles[i] == null) {
                boolean tryWildcard = false;
                try {
                    this.bundles[i] = ResourceBundle.getBundle(this.baseNames[i], this.locale);
                    if (this.bundles[i].getLocale().equals(this.locale)) {
                        return;
                    }
                    if (this.locale.getCountry().length() != 0 && i != this.bundles.length - 1) {
                        tryWildcard = true;
                    }
                }
                catch (MissingResourceException e) {
                    if (i == this.bundles.length - 1) {
                        throw e;
                    }
                    tryWildcard = true;
                }
                if (tryWildcard) {
                    Locale wildcard = new Locale("xx", this.locale.getCountry(), this.locale.getVariant());
                    try {
                        this.bundles[i] = ResourceBundle.getBundle(this.baseNames[i], wildcard);
                    }
                    catch (MissingResourceException e) {
                        if (this.bundles[i] != null) break block9;
                        throw e;
                    }
                }
            }
        }
    }
}

