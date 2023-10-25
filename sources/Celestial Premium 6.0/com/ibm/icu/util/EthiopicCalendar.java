/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.CECalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.util.Date;
import java.util.Locale;

public final class EthiopicCalendar
extends CECalendar {
    private static final long serialVersionUID = -2438495771339315608L;
    public static final int MESKEREM = 0;
    public static final int TEKEMT = 1;
    public static final int HEDAR = 2;
    public static final int TAHSAS = 3;
    public static final int TER = 4;
    public static final int YEKATIT = 5;
    public static final int MEGABIT = 6;
    public static final int MIAZIA = 7;
    public static final int GENBOT = 8;
    public static final int SENE = 9;
    public static final int HAMLE = 10;
    public static final int NEHASSE = 11;
    public static final int PAGUMEN = 12;
    private static final int JD_EPOCH_OFFSET_AMETE_MIHRET = 1723856;
    private static final int AMETE_MIHRET_DELTA = 5500;
    private static final int AMETE_ALEM = 0;
    private static final int AMETE_MIHRET = 1;
    private static final int AMETE_MIHRET_ERA = 0;
    private static final int AMETE_ALEM_ERA = 1;
    private int eraType = 0;

    public EthiopicCalendar() {
    }

    public EthiopicCalendar(TimeZone zone) {
        super(zone);
    }

    public EthiopicCalendar(Locale aLocale) {
        super(aLocale);
    }

    public EthiopicCalendar(ULocale locale) {
        super(locale);
    }

    public EthiopicCalendar(TimeZone zone, Locale aLocale) {
        super(zone, aLocale);
    }

    public EthiopicCalendar(TimeZone zone, ULocale locale) {
        super(zone, locale);
    }

    public EthiopicCalendar(int year, int month, int date) {
        super(year, month, date);
    }

    public EthiopicCalendar(Date date) {
        super(date);
    }

    public EthiopicCalendar(int year, int month, int date, int hour, int minute, int second) {
        super(year, month, date, hour, minute, second);
    }

    public String getType() {
        if (this.isAmeteAlemEra()) {
            return "ethiopic-amete-alem";
        }
        return "ethiopic";
    }

    public void setAmeteAlemEra(boolean onOff) {
        this.eraType = onOff ? 1 : 0;
    }

    public boolean isAmeteAlemEra() {
        return this.eraType == 1;
    }

    protected int handleGetExtendedYear() {
        int era;
        int eyear = this.newerField(19, 1) == 19 ? this.internalGet(19, 1) : (this.isAmeteAlemEra() ? this.internalGet(1, 5501) - 5500 : ((era = this.internalGet(0, 1)) == 1 ? this.internalGet(1, 1) : this.internalGet(1, 1) - 5500));
        return eyear;
    }

    protected void handleComputeFields(int julianDay) {
        int year;
        int era;
        int[] fields = new int[3];
        EthiopicCalendar.jdToCE(julianDay, this.getJDEpochOffset(), fields);
        if (this.isAmeteAlemEra()) {
            era = 0;
            year = fields[0] + 5500;
        } else if (fields[0] > 0) {
            era = 1;
            year = fields[0];
        } else {
            era = 0;
            year = fields[0] + 5500;
        }
        this.internalSet(19, fields[0]);
        this.internalSet(0, era);
        this.internalSet(1, year);
        this.internalSet(2, fields[1]);
        this.internalSet(5, fields[2]);
        this.internalSet(6, 30 * fields[1] + fields[2]);
    }

    protected int handleGetLimit(int field, int limitType) {
        if (this.isAmeteAlemEra() && field == 0) {
            return 0;
        }
        return super.handleGetLimit(field, limitType);
    }

    protected int getJDEpochOffset() {
        return 1723856;
    }

    public static int EthiopicToJD(long year, int month, int date) {
        return EthiopicCalendar.ceToJD(year, month, date, 1723856);
    }
}

