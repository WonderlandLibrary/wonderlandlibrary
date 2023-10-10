/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.util.TimeZone;
import java.util.Date;

public class TimeZoneAdapter
extends java.util.TimeZone {
    static final long serialVersionUID = -2040072218820018557L;
    private TimeZone zone;

    public static java.util.TimeZone wrap(TimeZone tz) {
        return new TimeZoneAdapter(tz);
    }

    public TimeZone unwrap() {
        return this.zone;
    }

    public TimeZoneAdapter(TimeZone zone) {
        this.zone = zone;
        super.setID(zone.getID());
    }

    @Override
    public void setID(String ID2) {
        super.setID(ID2);
        this.zone.setID(ID2);
    }

    @Override
    public boolean hasSameRules(java.util.TimeZone other) {
        return other instanceof TimeZoneAdapter && this.zone.hasSameRules(((TimeZoneAdapter)other).zone);
    }

    @Override
    public int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis) {
        return this.zone.getOffset(era, year, month, day, dayOfWeek, millis);
    }

    @Override
    public int getRawOffset() {
        return this.zone.getRawOffset();
    }

    @Override
    public void setRawOffset(int offsetMillis) {
        this.zone.setRawOffset(offsetMillis);
    }

    @Override
    public boolean useDaylightTime() {
        return this.zone.useDaylightTime();
    }

    @Override
    public boolean inDaylightTime(Date date) {
        return this.zone.inDaylightTime(date);
    }

    @Override
    public Object clone() {
        return new TimeZoneAdapter((TimeZone)this.zone.clone());
    }

    public synchronized int hashCode() {
        return this.zone.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TimeZoneAdapter) {
            TimeZone anotherZone = ((TimeZoneAdapter)obj).zone;
            return this.zone.equals(anotherZone);
        }
        return false;
    }

    public String toString() {
        return "TimeZoneAdapter: " + this.zone.toString();
    }
}

