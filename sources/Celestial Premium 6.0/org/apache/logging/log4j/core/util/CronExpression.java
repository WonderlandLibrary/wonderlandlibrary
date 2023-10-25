/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;

public final class CronExpression {
    protected static final int SECOND = 0;
    protected static final int MINUTE = 1;
    protected static final int HOUR = 2;
    protected static final int DAY_OF_MONTH = 3;
    protected static final int MONTH = 4;
    protected static final int DAY_OF_WEEK = 5;
    protected static final int YEAR = 6;
    protected static final int ALL_SPEC_INT = 99;
    protected static final int NO_SPEC_INT = 98;
    protected static final Integer ALL_SPEC = 99;
    protected static final Integer NO_SPEC = 98;
    protected static final Map<String, Integer> monthMap = new HashMap<String, Integer>(20);
    protected static final Map<String, Integer> dayMap = new HashMap<String, Integer>(60);
    private final String cronExpression;
    private TimeZone timeZone = null;
    protected transient TreeSet<Integer> seconds;
    protected transient TreeSet<Integer> minutes;
    protected transient TreeSet<Integer> hours;
    protected transient TreeSet<Integer> daysOfMonth;
    protected transient TreeSet<Integer> months;
    protected transient TreeSet<Integer> daysOfWeek;
    protected transient TreeSet<Integer> years;
    protected transient boolean lastdayOfWeek = false;
    protected transient int nthdayOfWeek = 0;
    protected transient boolean lastdayOfMonth = false;
    protected transient boolean nearestWeekday = false;
    protected transient int lastdayOffset = 0;
    protected transient boolean expressionParsed = false;
    public static final int MAX_YEAR;
    public static final Calendar MIN_CAL;
    public static final Date MIN_DATE;

    public CronExpression(String cronExpression) throws ParseException {
        if (cronExpression == null) {
            throw new IllegalArgumentException("cronExpression cannot be null");
        }
        this.cronExpression = cronExpression.toUpperCase(Locale.US);
        this.buildExpression(this.cronExpression);
    }

    public boolean isSatisfiedBy(Date date) {
        Calendar testDateCal = Calendar.getInstance(this.getTimeZone());
        testDateCal.setTime(date);
        testDateCal.set(14, 0);
        Date originalDate = testDateCal.getTime();
        testDateCal.add(13, -1);
        Date timeAfter = this.getTimeAfter(testDateCal.getTime());
        return timeAfter != null && timeAfter.equals(originalDate);
    }

    public Date getNextValidTimeAfter(Date date) {
        return this.getTimeAfter(date);
    }

    public Date getNextInvalidTimeAfter(Date date) {
        Date newDate;
        long difference = 1000L;
        Calendar adjustCal = Calendar.getInstance(this.getTimeZone());
        adjustCal.setTime(date);
        adjustCal.set(14, 0);
        Date lastDate = adjustCal.getTime();
        while (difference == 1000L && (newDate = this.getTimeAfter(lastDate)) != null) {
            difference = newDate.getTime() - lastDate.getTime();
            if (difference != 1000L) continue;
            lastDate = newDate;
        }
        return new Date(lastDate.getTime() + 1000L);
    }

    public TimeZone getTimeZone() {
        if (this.timeZone == null) {
            this.timeZone = TimeZone.getDefault();
        }
        return this.timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String toString() {
        return this.cronExpression;
    }

    public static boolean isValidExpression(String cronExpression) {
        try {
            new CronExpression(cronExpression);
        }
        catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static void validateExpression(String cronExpression) throws ParseException {
        new CronExpression(cronExpression);
    }

    protected void buildExpression(String expression) throws ParseException {
        this.expressionParsed = true;
        try {
            boolean dayOfWSpec;
            int exprOn;
            if (this.seconds == null) {
                this.seconds = new TreeSet();
            }
            if (this.minutes == null) {
                this.minutes = new TreeSet();
            }
            if (this.hours == null) {
                this.hours = new TreeSet();
            }
            if (this.daysOfMonth == null) {
                this.daysOfMonth = new TreeSet();
            }
            if (this.months == null) {
                this.months = new TreeSet();
            }
            if (this.daysOfWeek == null) {
                this.daysOfWeek = new TreeSet();
            }
            if (this.years == null) {
                this.years = new TreeSet();
            }
            StringTokenizer exprsTok = new StringTokenizer(expression, " \t", false);
            for (exprOn = 0; exprsTok.hasMoreTokens() && exprOn <= 6; ++exprOn) {
                String expr = exprsTok.nextToken().trim();
                if (exprOn == 3 && expr.indexOf(76) != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw new ParseException("Support for specifying 'L' and 'LW' with other days of the month is not implemented", -1);
                }
                if (exprOn == 5 && expr.indexOf(76) != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1);
                }
                if (exprOn == 5 && expr.indexOf(35) != -1 && expr.indexOf(35, expr.indexOf(35) + 1) != -1) {
                    throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1);
                }
                StringTokenizer vTok = new StringTokenizer(expr, ",");
                while (vTok.hasMoreTokens()) {
                    String v = vTok.nextToken();
                    this.storeExpressionVals(0, v, exprOn);
                }
            }
            if (exprOn <= 5) {
                throw new ParseException("Unexpected end of expression.", expression.length());
            }
            if (exprOn <= 6) {
                this.storeExpressionVals(0, "*", 6);
            }
            TreeSet<Integer> dow = this.getSet(5);
            TreeSet<Integer> dom = this.getSet(3);
            boolean dayOfMSpec = !dom.contains(NO_SPEC);
            boolean bl = dayOfWSpec = !dow.contains(NO_SPEC);
            if (!(dayOfMSpec && !dayOfWSpec || dayOfWSpec && !dayOfMSpec)) {
                throw new ParseException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.", 0);
            }
        }
        catch (ParseException pe) {
            throw pe;
        }
        catch (Exception e) {
            throw new ParseException("Illegal cron expression format (" + e.toString() + ")", 0);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected int storeExpressionVals(int pos, String s, int type) throws ParseException {
        int incr = 0;
        int i = this.skipWhiteSpace(pos, s);
        if (i >= s.length()) {
            return i;
        }
        char c = s.charAt(i);
        if (!(c < 'A' || c > 'Z' || s.equals("L") || s.equals("LW") || s.matches("^L-[0-9]*[W]?"))) {
            String sub = s.substring(i, i + 3);
            int sval = -1;
            int eval = -1;
            if (type == 4) {
                sval = this.getMonthNumber(sub) + 1;
                if (sval <= 0) {
                    throw new ParseException("Invalid Month value: '" + sub + "'", i);
                }
                if (s.length() > i + 3 && (c = s.charAt(i + 3)) == '-' && (eval = this.getMonthNumber(sub = s.substring(i += 4, i + 3)) + 1) <= 0) {
                    throw new ParseException("Invalid Month value: '" + sub + "'", i);
                }
            } else {
                if (type != 5) throw new ParseException("Illegal characters for this position: '" + sub + "'", i);
                sval = this.getDayOfWeekNumber(sub);
                if (sval < 0) {
                    throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    switch (c) {
                        case '-': {
                            sub = s.substring(i += 4, i + 3);
                            eval = this.getDayOfWeekNumber(sub);
                            if (eval < 0) {
                                throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
                            }
                            break;
                        }
                        case '#': {
                            try {
                                this.nthdayOfWeek = Integer.parseInt(s.substring(i += 4));
                                if (this.nthdayOfWeek < 1) throw new Exception();
                                if (this.nthdayOfWeek > 5) {
                                    throw new Exception();
                                }
                                break;
                            }
                            catch (Exception e) {
                                throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
                            }
                        }
                        case 'L': {
                            this.lastdayOfWeek = true;
                            ++i;
                            break;
                        }
                    }
                }
            }
            if (eval != -1) {
                incr = 1;
            }
            this.addToSet(sval, eval, incr, type);
            return i + 3;
        }
        switch (c) {
            case '?': {
                int val;
                if (++i + 1 < s.length() && s.charAt(i) != ' ' && s.charAt(i + 1) != '\t') {
                    throw new ParseException("Illegal character after '?': " + s.charAt(i), i);
                }
                if (type != 5 && type != 3) {
                    throw new ParseException("'?' can only be specfied for Day-of-Month or Day-of-Week.", i);
                }
                if (type == 5 && !this.lastdayOfMonth && (val = this.daysOfMonth.last().intValue()) == 98) {
                    throw new ParseException("'?' can only be specfied for Day-of-Month -OR- Day-of-Week.", i);
                }
                this.addToSet(98, -1, 0, type);
                return i;
            }
            case '*': 
            case '/': {
                if (c == '*' && i + 1 >= s.length()) {
                    this.addToSet(99, -1, incr, type);
                    return i + 1;
                }
                if (c == '/') {
                    if (i + 1 >= s.length()) throw new ParseException("'/' must be followed by an integer.", i);
                    if (s.charAt(i + 1) == ' ') throw new ParseException("'/' must be followed by an integer.", i);
                    if (s.charAt(i + 1) == '\t') {
                        throw new ParseException("'/' must be followed by an integer.", i);
                    }
                }
                if (c == '*') {
                    ++i;
                }
                if ((c = s.charAt(i)) == '/') {
                    if (++i >= s.length()) {
                        throw new ParseException("Unexpected end of string.", i);
                    }
                    incr = this.getNumericValue(s, i);
                    ++i;
                    if (incr > 10) {
                        ++i;
                    }
                    if (incr > 59) {
                        if (type == 0) throw new ParseException("Increment > 60 : " + incr, i);
                        if (type == 1) {
                            throw new ParseException("Increment > 60 : " + incr, i);
                        }
                    }
                    if (incr > 23 && type == 2) {
                        throw new ParseException("Increment > 24 : " + incr, i);
                    }
                    if (incr > 31 && type == 3) {
                        throw new ParseException("Increment > 31 : " + incr, i);
                    }
                    if (incr > 7 && type == 5) {
                        throw new ParseException("Increment > 7 : " + incr, i);
                    }
                    if (incr > 12 && type == 4) {
                        throw new ParseException("Increment > 12 : " + incr, i);
                    }
                } else {
                    incr = 1;
                }
                this.addToSet(99, -1, incr, type);
                return i;
            }
            case 'L': {
                ++i;
                if (type == 3) {
                    this.lastdayOfMonth = true;
                }
                if (type == 5) {
                    this.addToSet(7, 7, 0, type);
                }
                if (type != 3) return i;
                if (s.length() <= i) return i;
                c = s.charAt(i);
                if (c == '-') {
                    ValueSet vs = this.getValue(0, s, i + 1);
                    this.lastdayOffset = vs.value;
                    if (this.lastdayOffset > 30) {
                        throw new ParseException("Offset from last day must be <= 30", i + 1);
                    }
                    i = vs.pos;
                }
                if (s.length() <= i) return i;
                c = s.charAt(i);
                if (c != 'W') return i;
                this.nearestWeekday = true;
                ++i;
                return i;
            }
        }
        if (c < '0') throw new ParseException("Unexpected character: " + c, i);
        if (c > '9') throw new ParseException("Unexpected character: " + c, i);
        int val = Integer.parseInt(String.valueOf(c));
        if (++i < s.length()) {
            c = s.charAt(i);
            if (c < '0') return this.checkNext(i, s, val, type);
            if (c > '9') return this.checkNext(i, s, val, type);
            ValueSet vs = this.getValue(val, s, i);
            val = vs.value;
            i = vs.pos;
            return this.checkNext(i, s, val, type);
        }
        this.addToSet(val, -1, -1, type);
        return i;
    }

    protected int checkNext(int pos, String s, int val, int type) throws ParseException {
        int end = -1;
        int i = pos;
        if (i >= s.length()) {
            this.addToSet(val, end, -1, type);
            return i;
        }
        char c = s.charAt(pos);
        if (c == 'L') {
            if (type == 5) {
                if (val < 1 || val > 7) {
                    throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
                }
            } else {
                throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
            }
            this.lastdayOfWeek = true;
            TreeSet<Integer> set = this.getSet(type);
            set.add(val);
            return ++i;
        }
        if (c == 'W') {
            if (type != 3) {
                throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
            }
            this.nearestWeekday = true;
            if (val > 31) {
                throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
            }
            TreeSet<Integer> set = this.getSet(type);
            set.add(val);
            return ++i;
        }
        switch (c) {
            case '#': {
                if (type != 5) {
                    throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i);
                }
                ++i;
                try {
                    this.nthdayOfWeek = Integer.parseInt(s.substring(i));
                    if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5) {
                        throw new Exception();
                    }
                }
                catch (Exception e) {
                    throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
                }
                TreeSet<Integer> set = this.getSet(type);
                set.add(val);
                return ++i;
            }
            case '-': {
                int v;
                c = s.charAt(++i);
                end = v = Integer.parseInt(String.valueOf(c));
                if (++i >= s.length()) {
                    this.addToSet(val, end, 1, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = this.getValue(v, s, i);
                    end = vs.value;
                    i = vs.pos;
                }
                if (i < s.length() && (c = s.charAt(i)) == '/') {
                    c = s.charAt(++i);
                    int v2 = Integer.parseInt(String.valueOf(c));
                    if (++i >= s.length()) {
                        this.addToSet(val, end, v2, type);
                        return i;
                    }
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        ValueSet vs = this.getValue(v2, s, i);
                        int v3 = vs.value;
                        this.addToSet(val, end, v3, type);
                        i = vs.pos;
                    } else {
                        this.addToSet(val, end, v2, type);
                    }
                    return i;
                }
                this.addToSet(val, end, 1, type);
                return i;
            }
            case '/': {
                c = s.charAt(++i);
                int v2 = Integer.parseInt(String.valueOf(c));
                if (++i >= s.length()) {
                    this.addToSet(val, end, v2, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = this.getValue(v2, s, i);
                    int v3 = vs.value;
                    this.addToSet(val, end, v3, type);
                    i = vs.pos;
                    return i;
                }
                throw new ParseException("Unexpected character '" + c + "' after '/'", i);
            }
        }
        this.addToSet(val, end, 0, type);
        return ++i;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public String getExpressionSummary() {
        StringBuilder buf = new StringBuilder();
        buf.append("seconds: ");
        buf.append(this.getExpressionSetSummary(this.seconds));
        buf.append("\n");
        buf.append("minutes: ");
        buf.append(this.getExpressionSetSummary(this.minutes));
        buf.append("\n");
        buf.append("hours: ");
        buf.append(this.getExpressionSetSummary(this.hours));
        buf.append("\n");
        buf.append("daysOfMonth: ");
        buf.append(this.getExpressionSetSummary(this.daysOfMonth));
        buf.append("\n");
        buf.append("months: ");
        buf.append(this.getExpressionSetSummary(this.months));
        buf.append("\n");
        buf.append("daysOfWeek: ");
        buf.append(this.getExpressionSetSummary(this.daysOfWeek));
        buf.append("\n");
        buf.append("lastdayOfWeek: ");
        buf.append(this.lastdayOfWeek);
        buf.append("\n");
        buf.append("nearestWeekday: ");
        buf.append(this.nearestWeekday);
        buf.append("\n");
        buf.append("NthDayOfWeek: ");
        buf.append(this.nthdayOfWeek);
        buf.append("\n");
        buf.append("lastdayOfMonth: ");
        buf.append(this.lastdayOfMonth);
        buf.append("\n");
        buf.append("years: ");
        buf.append(this.getExpressionSetSummary(this.years));
        buf.append("\n");
        return buf.toString();
    }

    protected String getExpressionSetSummary(Set<Integer> set) {
        if (set.contains(NO_SPEC)) {
            return "?";
        }
        if (set.contains(ALL_SPEC)) {
            return "*";
        }
        StringBuilder buf = new StringBuilder();
        Iterator<Integer> itr = set.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Integer iVal = itr.next();
            String val = iVal.toString();
            if (!first) {
                buf.append(",");
            }
            buf.append(val);
            first = false;
        }
        return buf.toString();
    }

    protected String getExpressionSetSummary(ArrayList<Integer> list) {
        if (list.contains(NO_SPEC)) {
            return "?";
        }
        if (list.contains(ALL_SPEC)) {
            return "*";
        }
        StringBuilder buf = new StringBuilder();
        Iterator<Integer> itr = list.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Integer iVal = itr.next();
            String val = iVal.toString();
            if (!first) {
                buf.append(",");
            }
            buf.append(val);
            first = false;
        }
        return buf.toString();
    }

    protected int skipWhiteSpace(int i, String s) {
        while (i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t')) {
            ++i;
        }
        return i;
    }

    protected int findNextWhiteSpace(int i, String s) {
        while (i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t')) {
            ++i;
        }
        return i;
    }

    protected void addToSet(int val, int end, int incr, int type) throws ParseException {
        TreeSet<Integer> set = this.getSet(type);
        switch (type) {
            case 0: 
            case 1: {
                if (val >= 0 && val <= 59 && end <= 59 || val == 99) break;
                throw new ParseException("Minute and Second values must be between 0 and 59", -1);
            }
            case 2: {
                if (val >= 0 && val <= 23 && end <= 23 || val == 99) break;
                throw new ParseException("Hour values must be between 0 and 23", -1);
            }
            case 3: {
                if (val >= 1 && val <= 31 && end <= 31 || val == 99 || val == 98) break;
                throw new ParseException("Day of month values must be between 1 and 31", -1);
            }
            case 4: {
                if (val >= 1 && val <= 12 && end <= 12 || val == 99) break;
                throw new ParseException("Month values must be between 1 and 12", -1);
            }
            case 5: {
                if (val != 0 && val <= 7 && end <= 7 || val == 99 || val == 98) break;
                throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
            }
        }
        if ((incr == 0 || incr == -1) && val != 99) {
            if (val != -1) {
                set.add(val);
            } else {
                set.add(NO_SPEC);
            }
            return;
        }
        int startAt = val;
        int stopAt = end;
        if (val == 99 && incr <= 0) {
            incr = 1;
            set.add(ALL_SPEC);
        }
        switch (type) {
            case 0: 
            case 1: {
                if (stopAt == -1) {
                    stopAt = 59;
                }
                if (startAt != -1 && startAt != 99) break;
                startAt = 0;
                break;
            }
            case 2: {
                if (stopAt == -1) {
                    stopAt = 23;
                }
                if (startAt != -1 && startAt != 99) break;
                startAt = 0;
                break;
            }
            case 3: {
                if (stopAt == -1) {
                    stopAt = 31;
                }
                if (startAt != -1 && startAt != 99) break;
                startAt = 1;
                break;
            }
            case 4: {
                if (stopAt == -1) {
                    stopAt = 12;
                }
                if (startAt != -1 && startAt != 99) break;
                startAt = 1;
                break;
            }
            case 5: {
                if (stopAt == -1) {
                    stopAt = 7;
                }
                if (startAt != -1 && startAt != 99) break;
                startAt = 1;
                break;
            }
            case 6: {
                if (stopAt == -1) {
                    stopAt = MAX_YEAR;
                }
                if (startAt != -1 && startAt != 99) break;
                startAt = 1970;
                break;
            }
        }
        int max = -1;
        if (stopAt < startAt) {
            switch (type) {
                case 0: {
                    max = 60;
                    break;
                }
                case 1: {
                    max = 60;
                    break;
                }
                case 2: {
                    max = 24;
                    break;
                }
                case 4: {
                    max = 12;
                    break;
                }
                case 5: {
                    max = 7;
                    break;
                }
                case 3: {
                    max = 31;
                    break;
                }
                case 6: {
                    throw new IllegalArgumentException("Start year must be less than stop year");
                }
                default: {
                    throw new IllegalArgumentException("Unexpected type encountered");
                }
            }
            stopAt += max;
        }
        for (int i = startAt; i <= stopAt; i += incr) {
            if (max == -1) {
                set.add(i);
                continue;
            }
            int i2 = i % max;
            if (i2 == 0 && (type == 4 || type == 5 || type == 3)) {
                i2 = max;
            }
            set.add(i2);
        }
    }

    TreeSet<Integer> getSet(int type) {
        switch (type) {
            case 0: {
                return this.seconds;
            }
            case 1: {
                return this.minutes;
            }
            case 2: {
                return this.hours;
            }
            case 3: {
                return this.daysOfMonth;
            }
            case 4: {
                return this.months;
            }
            case 5: {
                return this.daysOfWeek;
            }
            case 6: {
                return this.years;
            }
        }
        return null;
    }

    protected ValueSet getValue(int v, String s, int i) {
        char c = s.charAt(i);
        StringBuilder s1 = new StringBuilder(String.valueOf(v));
        while (c >= '0' && c <= '9') {
            s1.append(c);
            if (++i >= s.length()) break;
            c = s.charAt(i);
        }
        ValueSet val = new ValueSet();
        val.pos = i < s.length() ? i : i + 1;
        val.value = Integer.parseInt(s1.toString());
        return val;
    }

    protected int getNumericValue(String s, int i) {
        int endOfVal = this.findNextWhiteSpace(i, s);
        String val = s.substring(i, endOfVal);
        return Integer.parseInt(val);
    }

    protected int getMonthNumber(String s) {
        Integer integer = monthMap.get(s);
        if (integer == null) {
            return -1;
        }
        return integer;
    }

    protected int getDayOfWeekNumber(String s) {
        Integer integer = dayMap.get(s);
        if (integer == null) {
            return -1;
        }
        return integer;
    }

    public Date getTimeAfter(Date afterTime) {
        GregorianCalendar cl = new GregorianCalendar(this.getTimeZone());
        afterTime = new Date(afterTime.getTime() + 1000L);
        cl.setTime(afterTime);
        cl.set(14, 0);
        boolean gotOne = false;
        while (!gotOne) {
            boolean dayOfWSpec;
            if (cl.get(1) > 2999) {
                return null;
            }
            int sec = cl.get(13);
            int min = cl.get(12);
            SortedSet<Integer> st = this.seconds.tailSet(sec);
            if (st != null && st.size() != 0) {
                sec = st.first();
            } else {
                sec = this.seconds.first();
                cl.set(12, ++min);
            }
            cl.set(13, sec);
            min = cl.get(12);
            int hr = cl.get(11);
            int t = -1;
            st = this.minutes.tailSet(min);
            if (st != null && st.size() != 0) {
                t = min;
                min = st.first();
            } else {
                min = this.minutes.first();
                ++hr;
            }
            if (min != t) {
                cl.set(13, 0);
                cl.set(12, min);
                this.setCalendarHour(cl, hr);
                continue;
            }
            cl.set(12, min);
            hr = cl.get(11);
            int day = cl.get(5);
            t = -1;
            st = this.hours.tailSet(hr);
            if (st != null && st.size() != 0) {
                t = hr;
                hr = st.first();
            } else {
                hr = this.hours.first();
                ++day;
            }
            if (hr != t) {
                cl.set(13, 0);
                cl.set(12, 0);
                cl.set(5, day);
                this.setCalendarHour(cl, hr);
                continue;
            }
            cl.set(11, hr);
            day = cl.get(5);
            int mon = cl.get(2) + 1;
            t = -1;
            int tmon = mon;
            boolean dayOfMSpec = !this.daysOfMonth.contains(NO_SPEC);
            boolean bl = dayOfWSpec = !this.daysOfWeek.contains(NO_SPEC);
            if (dayOfMSpec && !dayOfWSpec) {
                int dow;
                int ldom;
                st = this.daysOfMonth.tailSet(day);
                if (this.lastdayOfMonth) {
                    if (!this.nearestWeekday) {
                        t = day;
                        day = this.getLastDayOfMonth(mon, cl.get(1));
                        if (t > (day -= this.lastdayOffset)) {
                            if (++mon > 12) {
                                mon = 1;
                                tmon = 3333;
                                ((Calendar)cl).add(1, 1);
                            }
                            day = 1;
                        }
                    } else {
                        t = day;
                        day = this.getLastDayOfMonth(mon, cl.get(1));
                        Calendar tcal = Calendar.getInstance(this.getTimeZone());
                        tcal.set(13, 0);
                        tcal.set(12, 0);
                        tcal.set(11, 0);
                        tcal.set(5, day -= this.lastdayOffset);
                        tcal.set(2, mon - 1);
                        tcal.set(1, cl.get(1));
                        ldom = this.getLastDayOfMonth(mon, cl.get(1));
                        dow = tcal.get(7);
                        if (dow == 7 && day == 1) {
                            day += 2;
                        } else if (dow == 7) {
                            --day;
                        } else if (dow == 1 && day == ldom) {
                            day -= 2;
                        } else if (dow == 1) {
                            ++day;
                        }
                        tcal.set(13, sec);
                        tcal.set(12, min);
                        tcal.set(11, hr);
                        tcal.set(5, day);
                        tcal.set(2, mon - 1);
                        Date nTime = tcal.getTime();
                        if (nTime.before(afterTime)) {
                            day = 1;
                            ++mon;
                        }
                    }
                } else if (this.nearestWeekday) {
                    t = day;
                    day = this.daysOfMonth.first();
                    Calendar tcal = Calendar.getInstance(this.getTimeZone());
                    tcal.set(13, 0);
                    tcal.set(12, 0);
                    tcal.set(11, 0);
                    tcal.set(5, day);
                    tcal.set(2, mon - 1);
                    tcal.set(1, cl.get(1));
                    ldom = this.getLastDayOfMonth(mon, cl.get(1));
                    dow = tcal.get(7);
                    if (dow == 7 && day == 1) {
                        day += 2;
                    } else if (dow == 7) {
                        --day;
                    } else if (dow == 1 && day == ldom) {
                        day -= 2;
                    } else if (dow == 1) {
                        ++day;
                    }
                    tcal.set(13, sec);
                    tcal.set(12, min);
                    tcal.set(11, hr);
                    tcal.set(5, day);
                    tcal.set(2, mon - 1);
                    Date nTime = tcal.getTime();
                    if (nTime.before(afterTime)) {
                        day = this.daysOfMonth.first();
                        ++mon;
                    }
                } else if (st != null && st.size() != 0) {
                    int lastDay;
                    t = day;
                    day = st.first();
                    if (day > (lastDay = this.getLastDayOfMonth(mon, cl.get(1)))) {
                        day = this.daysOfMonth.first();
                        ++mon;
                    }
                } else {
                    day = this.daysOfMonth.first();
                    ++mon;
                }
                if (day != t || mon != tmon) {
                    cl.set(13, 0);
                    cl.set(12, 0);
                    cl.set(11, 0);
                    cl.set(5, day);
                    cl.set(2, mon - 1);
                    continue;
                }
            } else if (dayOfWSpec && !dayOfMSpec) {
                int daysToAdd;
                int cDow;
                if (this.lastdayOfWeek) {
                    int lDay;
                    int dow = this.daysOfWeek.first();
                    cDow = cl.get(7);
                    daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    }
                    if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }
                    if (day + daysToAdd > (lDay = this.getLastDayOfMonth(mon, cl.get(1)))) {
                        cl.set(13, 0);
                        cl.set(12, 0);
                        cl.set(11, 0);
                        cl.set(5, 1);
                        cl.set(2, mon);
                        continue;
                    }
                    while (day + daysToAdd + 7 <= lDay) {
                        daysToAdd += 7;
                    }
                    day += daysToAdd;
                    if (daysToAdd > 0) {
                        cl.set(13, 0);
                        cl.set(12, 0);
                        cl.set(11, 0);
                        cl.set(5, day);
                        cl.set(2, mon - 1);
                        continue;
                    }
                } else if (this.nthdayOfWeek != 0) {
                    int dow = this.daysOfWeek.first();
                    cDow = cl.get(7);
                    daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    } else if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }
                    boolean dayShifted = false;
                    if (daysToAdd > 0) {
                        dayShifted = true;
                    }
                    int weekOfMonth = (day += daysToAdd) / 7;
                    if (day % 7 > 0) {
                        ++weekOfMonth;
                    }
                    if ((daysToAdd = (this.nthdayOfWeek - weekOfMonth) * 7) < 0 || (day += daysToAdd) > this.getLastDayOfMonth(mon, cl.get(1))) {
                        cl.set(13, 0);
                        cl.set(12, 0);
                        cl.set(11, 0);
                        cl.set(5, 1);
                        cl.set(2, mon);
                        continue;
                    }
                    if (daysToAdd > 0 || dayShifted) {
                        cl.set(13, 0);
                        cl.set(12, 0);
                        cl.set(11, 0);
                        cl.set(5, day);
                        cl.set(2, mon - 1);
                        continue;
                    }
                } else {
                    int lDay;
                    int cDow2 = cl.get(7);
                    int dow = this.daysOfWeek.first();
                    st = this.daysOfWeek.tailSet(cDow2);
                    if (st != null && st.size() > 0) {
                        dow = st.first();
                    }
                    daysToAdd = 0;
                    if (cDow2 < dow) {
                        daysToAdd = dow - cDow2;
                    }
                    if (cDow2 > dow) {
                        daysToAdd = dow + (7 - cDow2);
                    }
                    if (day + daysToAdd > (lDay = this.getLastDayOfMonth(mon, cl.get(1)))) {
                        cl.set(13, 0);
                        cl.set(12, 0);
                        cl.set(11, 0);
                        cl.set(5, 1);
                        cl.set(2, mon);
                        continue;
                    }
                    if (daysToAdd > 0) {
                        cl.set(13, 0);
                        cl.set(12, 0);
                        cl.set(11, 0);
                        cl.set(5, day + daysToAdd);
                        cl.set(2, mon - 1);
                        continue;
                    }
                }
            } else {
                throw new UnsupportedOperationException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
            }
            cl.set(5, day);
            mon = cl.get(2) + 1;
            int year = cl.get(1);
            t = -1;
            if (year > MAX_YEAR) {
                return null;
            }
            st = this.months.tailSet(mon);
            if (st != null && st.size() != 0) {
                t = mon;
                mon = st.first();
            } else {
                mon = this.months.first();
                ++year;
            }
            if (mon != t) {
                cl.set(13, 0);
                cl.set(12, 0);
                cl.set(11, 0);
                cl.set(5, 1);
                cl.set(2, mon - 1);
                cl.set(1, year);
                continue;
            }
            cl.set(2, mon - 1);
            year = cl.get(1);
            t = -1;
            st = this.years.tailSet(year);
            if (st == null || st.size() == 0) {
                return null;
            }
            t = year;
            year = st.first();
            if (year != t) {
                cl.set(13, 0);
                cl.set(12, 0);
                cl.set(11, 0);
                cl.set(5, 1);
                cl.set(2, 0);
                cl.set(1, year);
                continue;
            }
            cl.set(1, year);
            gotOne = true;
        }
        return cl.getTime();
    }

    protected void setCalendarHour(Calendar cal, int hour) {
        cal.set(11, hour);
        if (cal.get(11) != hour && hour != 24) {
            cal.set(11, hour + 1);
        }
    }

    protected Date getTimeBefore(Date targetDate) {
        Date prevFireTime;
        Date targetDateNoMs;
        Calendar cl = Calendar.getInstance(this.getTimeZone());
        cl.setTime(targetDate);
        cl.set(14, 0);
        Date start = targetDateNoMs = cl.getTime();
        long minIncrement = this.findMinIncrement();
        do {
            Date prevCheckDate;
            if ((prevFireTime = this.getTimeAfter(prevCheckDate = new Date(start.getTime() - minIncrement))) == null || prevFireTime.before(MIN_DATE)) {
                return null;
            }
            start = prevCheckDate;
        } while (prevFireTime.compareTo(targetDateNoMs) >= 0);
        return prevFireTime;
    }

    public Date getPrevFireTime(Date targetDate) {
        return this.getTimeBefore(targetDate);
    }

    private long findMinIncrement() {
        if (this.seconds.size() != 1) {
            return this.minInSet(this.seconds) * 1000;
        }
        if (this.seconds.first() == 99) {
            return 1000L;
        }
        if (this.minutes.size() != 1) {
            return this.minInSet(this.minutes) * 60000;
        }
        if (this.minutes.first() == 99) {
            return 60000L;
        }
        if (this.hours.size() != 1) {
            return this.minInSet(this.hours) * 3600000;
        }
        if (this.hours.first() == 99) {
            return 3600000L;
        }
        return 86400000L;
    }

    private int minInSet(TreeSet<Integer> set) {
        int previous = 0;
        int min = Integer.MAX_VALUE;
        boolean first = true;
        for (int value : set) {
            if (first) {
                previous = value;
                first = false;
                continue;
            }
            int diff = value - previous;
            if (diff >= min) continue;
            min = diff;
        }
        return min;
    }

    public Date getFinalFireTime() {
        return null;
    }

    protected boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    protected int getLastDayOfMonth(int monthNum, int year) {
        switch (monthNum) {
            case 1: {
                return 31;
            }
            case 2: {
                return this.isLeapYear(year) ? 29 : 28;
            }
            case 3: {
                return 31;
            }
            case 4: {
                return 30;
            }
            case 5: {
                return 31;
            }
            case 6: {
                return 30;
            }
            case 7: {
                return 31;
            }
            case 8: {
                return 31;
            }
            case 9: {
                return 30;
            }
            case 10: {
                return 31;
            }
            case 11: {
                return 30;
            }
            case 12: {
                return 31;
            }
        }
        throw new IllegalArgumentException("Illegal month number: " + monthNum);
    }

    static {
        monthMap.put("JAN", 0);
        monthMap.put("FEB", 1);
        monthMap.put("MAR", 2);
        monthMap.put("APR", 3);
        monthMap.put("MAY", 4);
        monthMap.put("JUN", 5);
        monthMap.put("JUL", 6);
        monthMap.put("AUG", 7);
        monthMap.put("SEP", 8);
        monthMap.put("OCT", 9);
        monthMap.put("NOV", 10);
        monthMap.put("DEC", 11);
        dayMap.put("SUN", 1);
        dayMap.put("MON", 2);
        dayMap.put("TUE", 3);
        dayMap.put("WED", 4);
        dayMap.put("THU", 5);
        dayMap.put("FRI", 6);
        dayMap.put("SAT", 7);
        MAX_YEAR = Calendar.getInstance().get(1) + 100;
        MIN_CAL = Calendar.getInstance();
        MIN_CAL.set(1970, 0, 1);
        MIN_DATE = MIN_CAL.getTime();
    }

    private class ValueSet {
        public int value;
        public int pos;

        private ValueSet() {
        }
    }
}

