/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.BasicPeriodFormatter;
import com.ibm.icu.impl.duration.BasicPeriodFormatterService;
import com.ibm.icu.impl.duration.PeriodFormatter;
import com.ibm.icu.impl.duration.PeriodFormatterFactory;
import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import java.util.Locale;

public class BasicPeriodFormatterFactory
implements PeriodFormatterFactory {
    private final PeriodFormatterDataService ds;
    private PeriodFormatterData data;
    private Customizations customizations;
    private boolean customizationsInUse;
    private String localeName;

    BasicPeriodFormatterFactory(PeriodFormatterDataService ds) {
        this.ds = ds;
        this.customizations = new Customizations();
        this.localeName = Locale.getDefault().toString();
    }

    public static BasicPeriodFormatterFactory getDefault() {
        return (BasicPeriodFormatterFactory)BasicPeriodFormatterService.getInstance().newPeriodFormatterFactory();
    }

    @Override
    public PeriodFormatterFactory setLocale(String localeName) {
        this.data = null;
        this.localeName = localeName;
        return this;
    }

    @Override
    public PeriodFormatterFactory setDisplayLimit(boolean display) {
        this.updateCustomizations().displayLimit = display;
        return this;
    }

    public boolean getDisplayLimit() {
        return this.customizations.displayLimit;
    }

    @Override
    public PeriodFormatterFactory setDisplayPastFuture(boolean display) {
        this.updateCustomizations().displayDirection = display;
        return this;
    }

    public boolean getDisplayPastFuture() {
        return this.customizations.displayDirection;
    }

    @Override
    public PeriodFormatterFactory setSeparatorVariant(int variant) {
        this.updateCustomizations().separatorVariant = (byte)variant;
        return this;
    }

    public int getSeparatorVariant() {
        return this.customizations.separatorVariant;
    }

    @Override
    public PeriodFormatterFactory setUnitVariant(int variant) {
        this.updateCustomizations().unitVariant = (byte)variant;
        return this;
    }

    public int getUnitVariant() {
        return this.customizations.unitVariant;
    }

    @Override
    public PeriodFormatterFactory setCountVariant(int variant) {
        this.updateCustomizations().countVariant = (byte)variant;
        return this;
    }

    public int getCountVariant() {
        return this.customizations.countVariant;
    }

    @Override
    public PeriodFormatter getFormatter() {
        this.customizationsInUse = true;
        return new BasicPeriodFormatter(this, this.localeName, this.getData(), this.customizations);
    }

    private Customizations updateCustomizations() {
        if (this.customizationsInUse) {
            this.customizations = this.customizations.copy();
            this.customizationsInUse = false;
        }
        return this.customizations;
    }

    PeriodFormatterData getData() {
        if (this.data == null) {
            this.data = this.ds.get(this.localeName);
        }
        return this.data;
    }

    PeriodFormatterData getData(String locName) {
        return this.ds.get(locName);
    }

    static class Customizations {
        boolean displayLimit = true;
        boolean displayDirection = true;
        byte separatorVariant = (byte)2;
        byte unitVariant = 0;
        byte countVariant = 0;

        Customizations() {
        }

        public Customizations copy() {
            Customizations result = new Customizations();
            result.displayLimit = this.displayLimit;
            result.displayDirection = this.displayDirection;
            result.separatorVariant = this.separatorVariant;
            result.unitVariant = this.unitVariant;
            result.countVariant = this.countVariant;
            return result;
        }
    }
}

