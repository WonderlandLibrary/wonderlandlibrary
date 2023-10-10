// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.text.ParseException;
import java.util.Locale;
import java.util.Date;
import java.util.Calendar;
import java.util.TreeSet;
import java.util.TimeZone;
import java.util.Map;

public final class CronExpression
{
    protected static final int SECOND = 0;
    protected static final int MINUTE = 1;
    protected static final int HOUR = 2;
    protected static final int DAY_OF_MONTH = 3;
    protected static final int MONTH = 4;
    protected static final int DAY_OF_WEEK = 5;
    protected static final int YEAR = 6;
    protected static final int ALL_SPEC_INT = 99;
    protected static final int NO_SPEC_INT = 98;
    protected static final Integer ALL_SPEC;
    protected static final Integer NO_SPEC;
    protected static final Map<String, Integer> monthMap;
    protected static final Map<String, Integer> dayMap;
    private final String cronExpression;
    private TimeZone timeZone;
    protected transient TreeSet<Integer> seconds;
    protected transient TreeSet<Integer> minutes;
    protected transient TreeSet<Integer> hours;
    protected transient TreeSet<Integer> daysOfMonth;
    protected transient TreeSet<Integer> months;
    protected transient TreeSet<Integer> daysOfWeek;
    protected transient TreeSet<Integer> years;
    protected transient boolean lastdayOfWeek;
    protected transient int nthdayOfWeek;
    protected transient boolean lastdayOfMonth;
    protected transient boolean nearestWeekday;
    protected transient int lastdayOffset;
    protected transient boolean expressionParsed;
    public static final int MAX_YEAR;
    public static final Calendar MIN_CAL;
    public static final Date MIN_DATE;
    
    public CronExpression(final String cronExpression) throws ParseException {
        this.timeZone = null;
        this.lastdayOfWeek = false;
        this.nthdayOfWeek = 0;
        this.lastdayOfMonth = false;
        this.nearestWeekday = false;
        this.lastdayOffset = 0;
        this.expressionParsed = false;
        if (cronExpression == null) {
            throw new IllegalArgumentException("cronExpression cannot be null");
        }
        this.buildExpression(this.cronExpression = cronExpression.toUpperCase(Locale.US));
    }
    
    public boolean isSatisfiedBy(final Date date) {
        final Calendar testDateCal = Calendar.getInstance(this.getTimeZone());
        testDateCal.setTime(date);
        testDateCal.set(14, 0);
        final Date originalDate = testDateCal.getTime();
        testDateCal.add(13, -1);
        final Date timeAfter = this.getTimeAfter(testDateCal.getTime());
        return timeAfter != null && timeAfter.equals(originalDate);
    }
    
    public Date getNextValidTimeAfter(final Date date) {
        return this.getTimeAfter(date);
    }
    
    public Date getNextInvalidTimeAfter(final Date date) {
        long difference = 1000L;
        final Calendar adjustCal = Calendar.getInstance(this.getTimeZone());
        adjustCal.setTime(date);
        adjustCal.set(14, 0);
        Date lastDate = adjustCal.getTime();
        while (difference == 1000L) {
            final Date newDate = this.getTimeAfter(lastDate);
            if (newDate == null) {
                break;
            }
            difference = newDate.getTime() - lastDate.getTime();
            if (difference != 1000L) {
                continue;
            }
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
    
    public void setTimeZone(final TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    @Override
    public String toString() {
        return this.cronExpression;
    }
    
    public static boolean isValidExpression(final String cronExpression) {
        try {
            new CronExpression(cronExpression);
        }
        catch (ParseException pe) {
            return false;
        }
        return true;
    }
    
    public static void validateExpression(final String cronExpression) throws ParseException {
        new CronExpression(cronExpression);
    }
    
    protected void buildExpression(final String expression) throws ParseException {
        this.expressionParsed = true;
        try {
            if (this.seconds == null) {
                this.seconds = new TreeSet<Integer>();
            }
            if (this.minutes == null) {
                this.minutes = new TreeSet<Integer>();
            }
            if (this.hours == null) {
                this.hours = new TreeSet<Integer>();
            }
            if (this.daysOfMonth == null) {
                this.daysOfMonth = new TreeSet<Integer>();
            }
            if (this.months == null) {
                this.months = new TreeSet<Integer>();
            }
            if (this.daysOfWeek == null) {
                this.daysOfWeek = new TreeSet<Integer>();
            }
            if (this.years == null) {
                this.years = new TreeSet<Integer>();
            }
            int exprOn = 0;
            for (StringTokenizer exprsTok = new StringTokenizer(expression, " \t", false); exprsTok.hasMoreTokens() && exprOn <= 6; ++exprOn) {
                final String expr = exprsTok.nextToken().trim();
                if (exprOn == 3 && expr.indexOf(76) != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw new ParseException("Support for specifying 'L' and 'LW' with other days of the month is not implemented", -1);
                }
                if (exprOn == 5 && expr.indexOf(76) != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1);
                }
                if (exprOn == 5 && expr.indexOf(35) != -1 && expr.indexOf(35, expr.indexOf(35) + 1) != -1) {
                    throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1);
                }
                final StringTokenizer vTok = new StringTokenizer(expr, ",");
                while (vTok.hasMoreTokens()) {
                    final String v = vTok.nextToken();
                    this.storeExpressionVals(0, v, exprOn);
                }
            }
            if (exprOn <= 5) {
                throw new ParseException("Unexpected end of expression.", expression.length());
            }
            if (exprOn <= 6) {
                this.storeExpressionVals(0, "*", 6);
            }
            final TreeSet<Integer> dow = this.getSet(5);
            final TreeSet<Integer> dom = this.getSet(3);
            final boolean dayOfMSpec = !dom.contains(CronExpression.NO_SPEC);
            final boolean dayOfWSpec = !dow.contains(CronExpression.NO_SPEC);
            if ((!dayOfMSpec || dayOfWSpec) && (!dayOfWSpec || dayOfMSpec)) {
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
    
    protected int storeExpressionVals(final int pos, final String s, final int type) throws ParseException {
        int incr = 0;
        int i = this.skipWhiteSpace(pos, s);
        if (i >= s.length()) {
            return i;
        }
        char c = s.charAt(i);
        if (c >= 'A' && c <= 'Z' && !s.equals("L") && !s.equals("LW") && !s.matches("^L-[0-9]*[W]?")) {
            String sub = s.substring(i, i + 3);
            int sval = -1;
            int eval = -1;
            if (type == 4) {
                sval = this.getMonthNumber(sub) + 1;
                if (sval <= 0) {
                    throw new ParseException("Invalid Month value: '" + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    if (c == '-') {
                        i += 4;
                        sub = s.substring(i, i + 3);
                        eval = this.getMonthNumber(sub) + 1;
                        if (eval <= 0) {
                            throw new ParseException("Invalid Month value: '" + sub + "'", i);
                        }
                    }
                }
            }
            else {
                if (type != 5) {
                    throw new ParseException("Illegal characters for this position: '" + sub + "'", i);
                }
                sval = this.getDayOfWeekNumber(sub);
                if (sval < 0) {
                    throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    switch (c) {
                        case '-': {
                            i += 4;
                            sub = s.substring(i, i + 3);
                            eval = this.getDayOfWeekNumber(sub);
                            if (eval < 0) {
                                throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
                            }
                            break;
                        }
                        case '#': {
                            try {
                                i += 4;
                                this.nthdayOfWeek = Integer.parseInt(s.substring(i));
                                if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5) {
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
                if (++i + 1 < s.length() && s.charAt(i) != ' ' && s.charAt(i + 1) != '\t') {
                    throw new ParseException("Illegal character after '?': " + s.charAt(i), i);
                }
                if (type != 5 && type != 3) {
                    throw new ParseException("'?' can only be specfied for Day-of-Month or Day-of-Week.", i);
                }
                if (type == 5 && !this.lastdayOfMonth) {
                    final int val = this.daysOfMonth.last();
                    if (val == 98) {
                        throw new ParseException("'?' can only be specfied for Day-of-Month -OR- Day-of-Week.", i);
                    }
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
                if (c == '/' && (i + 1 >= s.length() || s.charAt(i + 1) == ' ' || s.charAt(i + 1) == '\t')) {
                    throw new ParseException("'/' must be followed by an integer.", i);
                }
                if (c == '*') {
                    ++i;
                }
                c = s.charAt(i);
                if (c == '/') {
                    if (++i >= s.length()) {
                        throw new ParseException("Unexpected end of string.", i);
                    }
                    incr = this.getNumericValue(s, i);
                    ++i;
                    if (incr > 10) {
                        ++i;
                    }
                    if (incr > 59 && (type == 0 || type == 1)) {
                        throw new ParseException("Increment > 60 : " + incr, i);
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
                }
                else {
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
                if (type == 3 && s.length() > i) {
                    c = s.charAt(i);
                    if (c == '-') {
                        final ValueSet vs = this.getValue(0, s, i + 1);
                        this.lastdayOffset = vs.value;
                        if (this.lastdayOffset > 30) {
                            throw new ParseException("Offset from last day must be <= 30", i + 1);
                        }
                        i = vs.pos;
                    }
                    if (s.length() > i) {
                        c = s.charAt(i);
                        if (c == 'W') {
                            this.nearestWeekday = true;
                            ++i;
                        }
                    }
                }
                return i;
            }
            default: {
                if (c < '0' || c > '9') {
                    throw new ParseException("Unexpected character: " + c, i);
                }
                int val = Integer.parseInt(String.valueOf(c));
                if (++i >= s.length()) {
                    this.addToSet(val, -1, -1, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    final ValueSet vs2 = this.getValue(val, s, i);
                    val = vs2.value;
                    i = vs2.pos;
                }
                i = this.checkNext(i, s, val, type);
                return i;
            }
        }
    }
    
    protected int checkNext(final int pos, final String s, final int val, final int type) throws ParseException {
        int end = -1;
        int i = pos;
        if (i >= s.length()) {
            this.addToSet(val, end, -1, type);
            return i;
        }
        char c = s.charAt(pos);
        if (c == 'L') {
            if (type != 5) {
                throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
            }
            if (val < 1 || val > 7) {
                throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
            }
            this.lastdayOfWeek = true;
            final TreeSet<Integer> set = this.getSet(type);
            set.add(val);
            return ++i;
        }
        else if (c == 'W') {
            if (type != 3) {
                throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
            }
            this.nearestWeekday = true;
            if (val > 31) {
                throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
            }
            final TreeSet<Integer> set = this.getSet(type);
            set.add(val);
            return ++i;
        }
        else {
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
                    final TreeSet<Integer> set = this.getSet(type);
                    set.add(val);
                    return ++i;
                }
                case '-': {
                    ++i;
                    c = s.charAt(i);
                    final int v = end = Integer.parseInt(String.valueOf(c));
                    if (++i >= s.length()) {
                        this.addToSet(val, end, 1, type);
                        return i;
                    }
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        final ValueSet vs = this.getValue(v, s, i);
                        end = vs.value;
                        i = vs.pos;
                    }
                    if (i >= s.length() || (c = s.charAt(i)) != '/') {
                        this.addToSet(val, end, 1, type);
                        return i;
                    }
                    ++i;
                    c = s.charAt(i);
                    final int v2 = Integer.parseInt(String.valueOf(c));
                    if (++i >= s.length()) {
                        this.addToSet(val, end, v2, type);
                        return i;
                    }
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        final ValueSet vs2 = this.getValue(v2, s, i);
                        final int v3 = vs2.value;
                        this.addToSet(val, end, v3, type);
                        i = vs2.pos;
                    }
                    else {
                        this.addToSet(val, end, v2, type);
                    }
                    return i;
                }
                case '/': {
                    ++i;
                    c = s.charAt(i);
                    final int v2 = Integer.parseInt(String.valueOf(c));
                    if (++i >= s.length()) {
                        this.addToSet(val, end, v2, type);
                        return i;
                    }
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        final ValueSet vs2 = this.getValue(v2, s, i);
                        final int v3 = vs2.value;
                        this.addToSet(val, end, v3, type);
                        i = vs2.pos;
                        return i;
                    }
                    throw new ParseException("Unexpected character '" + c + "' after '/'", i);
                }
                default: {
                    this.addToSet(val, end, 0, type);
                    return ++i;
                }
            }
        }
    }
    
    public String getCronExpression() {
        return this.cronExpression;
    }
    
    public String getExpressionSummary() {
        final StringBuilder buf = new StringBuilder();
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
    
    protected String getExpressionSetSummary(final Set<Integer> set) {
        if (set.contains(CronExpression.NO_SPEC)) {
            return "?";
        }
        if (set.contains(CronExpression.ALL_SPEC)) {
            return "*";
        }
        final StringBuilder buf = new StringBuilder();
        final Iterator<Integer> itr = set.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            final Integer iVal = itr.next();
            final String val = iVal.toString();
            if (!first) {
                buf.append(",");
            }
            buf.append(val);
            first = false;
        }
        return buf.toString();
    }
    
    protected String getExpressionSetSummary(final ArrayList<Integer> list) {
        if (list.contains(CronExpression.NO_SPEC)) {
            return "?";
        }
        if (list.contains(CronExpression.ALL_SPEC)) {
            return "*";
        }
        final StringBuilder buf = new StringBuilder();
        final Iterator<Integer> itr = list.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            final Integer iVal = itr.next();
            final String val = iVal.toString();
            if (!first) {
                buf.append(",");
            }
            buf.append(val);
            first = false;
        }
        return buf.toString();
    }
    
    protected int skipWhiteSpace(int i, final String s) {
        while (i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t')) {
            ++i;
        }
        return i;
    }
    
    protected int findNextWhiteSpace(int i, final String s) {
        while (i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t')) {
            ++i;
        }
        return i;
    }
    
    protected void addToSet(final int val, final int end, int incr, final int type) throws ParseException {
        final TreeSet<Integer> set = this.getSet(type);
        switch (type) {
            case 0:
            case 1: {
                if ((val < 0 || val > 59 || end > 59) && val != 99) {
                    throw new ParseException("Minute and Second values must be between 0 and 59", -1);
                }
                break;
            }
            case 2: {
                if ((val < 0 || val > 23 || end > 23) && val != 99) {
                    throw new ParseException("Hour values must be between 0 and 23", -1);
                }
                break;
            }
            case 3: {
                if ((val < 1 || val > 31 || end > 31) && val != 99 && val != 98) {
                    throw new ParseException("Day of month values must be between 1 and 31", -1);
                }
                break;
            }
            case 4: {
                if ((val < 1 || val > 12 || end > 12) && val != 99) {
                    throw new ParseException("Month values must be between 1 and 12", -1);
                }
                break;
            }
            case 5: {
                if ((val == 0 || val > 7 || end > 7) && val != 99 && val != 98) {
                    throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
                }
                break;
            }
        }
        if ((incr == 0 || incr == -1) && val != 99) {
            if (val != -1) {
                set.add(val);
            }
            else {
                set.add(CronExpression.NO_SPEC);
            }
            return;
        }
        int startAt = val;
        int stopAt = end;
        if (val == 99 && incr <= 0) {
            incr = 1;
            set.add(CronExpression.ALL_SPEC);
        }
        switch (type) {
            case 0:
            case 1: {
                if (stopAt == -1) {
                    stopAt = 59;
                }
                if (startAt == -1 || startAt == 99) {
                    startAt = 0;
                    break;
                }
                break;
            }
            case 2: {
                if (stopAt == -1) {
                    stopAt = 23;
                }
                if (startAt == -1 || startAt == 99) {
                    startAt = 0;
                    break;
                }
                break;
            }
            case 3: {
                if (stopAt == -1) {
                    stopAt = 31;
                }
                if (startAt == -1 || startAt == 99) {
                    startAt = 1;
                    break;
                }
                break;
            }
            case 4: {
                if (stopAt == -1) {
                    stopAt = 12;
                }
                if (startAt == -1 || startAt == 99) {
                    startAt = 1;
                    break;
                }
                break;
            }
            case 5: {
                if (stopAt == -1) {
                    stopAt = 7;
                }
                if (startAt == -1 || startAt == 99) {
                    startAt = 1;
                    break;
                }
                break;
            }
            case 6: {
                if (stopAt == -1) {
                    stopAt = CronExpression.MAX_YEAR;
                }
                if (startAt == -1 || startAt == 99) {
                    startAt = 1970;
                    break;
                }
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
            }
            else {
                int i2 = i % max;
                if (i2 == 0 && (type == 4 || type == 5 || type == 3)) {
                    i2 = max;
                }
                set.add(i2);
            }
        }
    }
    
    TreeSet<Integer> getSet(final int type) {
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
            default: {
                return null;
            }
        }
    }
    
    protected ValueSet getValue(final int v, final String s, int i) {
        char c = s.charAt(i);
        final StringBuilder s2 = new StringBuilder(String.valueOf(v));
        while (c >= '0' && c <= '9') {
            s2.append(c);
            if (++i >= s.length()) {
                break;
            }
            c = s.charAt(i);
        }
        final ValueSet val = new ValueSet();
        val.pos = ((i < s.length()) ? i : (i + 1));
        val.value = Integer.parseInt(s2.toString());
        return val;
    }
    
    protected int getNumericValue(final String s, final int i) {
        final int endOfVal = this.findNextWhiteSpace(i, s);
        final String val = s.substring(i, endOfVal);
        return Integer.parseInt(val);
    }
    
    protected int getMonthNumber(final String s) {
        final Integer integer = CronExpression.monthMap.get(s);
        if (integer == null) {
            return -1;
        }
        return integer;
    }
    
    protected int getDayOfWeekNumber(final String s) {
        final Integer integer = CronExpression.dayMap.get(s);
        if (integer == null) {
            return -1;
        }
        return integer;
    }
    
    public Date getTimeAfter(Date afterTime) {
        final Calendar cl = new GregorianCalendar(this.getTimeZone());
        afterTime = new Date(afterTime.getTime() + 1000L);
        cl.setTime(afterTime);
        cl.set(14, 0);
        boolean gotOne = false;
        while (!gotOne) {
            if (cl.get(1) > 2999) {
                return null;
            }
            int sec = cl.get(13);
            int min = cl.get(12);
            SortedSet<Integer> st = this.seconds.tailSet(sec);
            if (st != null && st.size() != 0) {
                sec = st.first();
            }
            else {
                sec = this.seconds.first();
                ++min;
                cl.set(12, min);
            }
            cl.set(13, sec);
            min = cl.get(12);
            int hr = cl.get(11);
            int t = -1;
            st = this.minutes.tailSet(min);
            if (st != null && st.size() != 0) {
                t = min;
                min = st.first();
            }
            else {
                min = this.minutes.first();
                ++hr;
            }
            if (min != t) {
                cl.set(13, 0);
                cl.set(12, min);
                this.setCalendarHour(cl, hr);
            }
            else {
                cl.set(12, min);
                hr = cl.get(11);
                int day = cl.get(5);
                t = -1;
                st = this.hours.tailSet(hr);
                if (st != null && st.size() != 0) {
                    t = hr;
                    hr = st.first();
                }
                else {
                    hr = this.hours.first();
                    ++day;
                }
                if (hr != t) {
                    cl.set(13, 0);
                    cl.set(12, 0);
                    cl.set(5, day);
                    this.setCalendarHour(cl, hr);
                }
                else {
                    cl.set(11, hr);
                    day = cl.get(5);
                    int mon = cl.get(2) + 1;
                    t = -1;
                    int tmon = mon;
                    final boolean dayOfMSpec = !this.daysOfMonth.contains(CronExpression.NO_SPEC);
                    final boolean dayOfWSpec = !this.daysOfWeek.contains(CronExpression.NO_SPEC);
                    if (dayOfMSpec && !dayOfWSpec) {
                        st = this.daysOfMonth.tailSet(day);
                        if (this.lastdayOfMonth) {
                            if (!this.nearestWeekday) {
                                t = day;
                                day = this.getLastDayOfMonth(mon, cl.get(1));
                                day -= this.lastdayOffset;
                                if (t > day) {
                                    if (++mon > 12) {
                                        mon = 1;
                                        tmon = 3333;
                                        cl.add(1, 1);
                                    }
                                    day = 1;
                                }
                            }
                            else {
                                t = day;
                                day = this.getLastDayOfMonth(mon, cl.get(1));
                                day -= this.lastdayOffset;
                                final Calendar tcal = Calendar.getInstance(this.getTimeZone());
                                tcal.set(13, 0);
                                tcal.set(12, 0);
                                tcal.set(11, 0);
                                tcal.set(5, day);
                                tcal.set(2, mon - 1);
                                tcal.set(1, cl.get(1));
                                final int ldom = this.getLastDayOfMonth(mon, cl.get(1));
                                final int dow = tcal.get(7);
                                if (dow == 7 && day == 1) {
                                    day += 2;
                                }
                                else if (dow == 7) {
                                    --day;
                                }
                                else if (dow == 1 && day == ldom) {
                                    day -= 2;
                                }
                                else if (dow == 1) {
                                    ++day;
                                }
                                tcal.set(13, sec);
                                tcal.set(12, min);
                                tcal.set(11, hr);
                                tcal.set(5, day);
                                tcal.set(2, mon - 1);
                                final Date nTime = tcal.getTime();
                                if (nTime.before(afterTime)) {
                                    day = 1;
                                    ++mon;
                                }
                            }
                        }
                        else if (this.nearestWeekday) {
                            t = day;
                            day = this.daysOfMonth.first();
                            final Calendar tcal = Calendar.getInstance(this.getTimeZone());
                            tcal.set(13, 0);
                            tcal.set(12, 0);
                            tcal.set(11, 0);
                            tcal.set(5, day);
                            tcal.set(2, mon - 1);
                            tcal.set(1, cl.get(1));
                            final int ldom = this.getLastDayOfMonth(mon, cl.get(1));
                            final int dow = tcal.get(7);
                            if (dow == 7 && day == 1) {
                                day += 2;
                            }
                            else if (dow == 7) {
                                --day;
                            }
                            else if (dow == 1 && day == ldom) {
                                day -= 2;
                            }
                            else if (dow == 1) {
                                ++day;
                            }
                            tcal.set(13, sec);
                            tcal.set(12, min);
                            tcal.set(11, hr);
                            tcal.set(5, day);
                            tcal.set(2, mon - 1);
                            final Date nTime = tcal.getTime();
                            if (nTime.before(afterTime)) {
                                day = this.daysOfMonth.first();
                                ++mon;
                            }
                        }
                        else if (st != null && st.size() != 0) {
                            t = day;
                            day = st.first();
                            final int lastDay = this.getLastDayOfMonth(mon, cl.get(1));
                            if (day > lastDay) {
                                day = this.daysOfMonth.first();
                                ++mon;
                            }
                        }
                        else {
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
                    }
                    else {
                        if (!dayOfWSpec || dayOfMSpec) {
                            throw new UnsupportedOperationException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
                        }
                        if (this.lastdayOfWeek) {
                            final int dow2 = this.daysOfWeek.first();
                            final int cDow = cl.get(7);
                            int daysToAdd = 0;
                            if (cDow < dow2) {
                                daysToAdd = dow2 - cDow;
                            }
                            if (cDow > dow2) {
                                daysToAdd = dow2 + (7 - cDow);
                            }
                            final int lDay = this.getLastDayOfMonth(mon, cl.get(1));
                            if (day + daysToAdd > lDay) {
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
                        }
                        else if (this.nthdayOfWeek != 0) {
                            final int dow2 = this.daysOfWeek.first();
                            final int cDow = cl.get(7);
                            int daysToAdd = 0;
                            if (cDow < dow2) {
                                daysToAdd = dow2 - cDow;
                            }
                            else if (cDow > dow2) {
                                daysToAdd = dow2 + (7 - cDow);
                            }
                            boolean dayShifted = false;
                            if (daysToAdd > 0) {
                                dayShifted = true;
                            }
                            day += daysToAdd;
                            int weekOfMonth = day / 7;
                            if (day % 7 > 0) {
                                ++weekOfMonth;
                            }
                            daysToAdd = (this.nthdayOfWeek - weekOfMonth) * 7;
                            day += daysToAdd;
                            if (daysToAdd < 0 || day > this.getLastDayOfMonth(mon, cl.get(1))) {
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
                        }
                        else {
                            final int cDow2 = cl.get(7);
                            int dow3 = this.daysOfWeek.first();
                            st = this.daysOfWeek.tailSet(cDow2);
                            if (st != null && st.size() > 0) {
                                dow3 = st.first();
                            }
                            int daysToAdd = 0;
                            if (cDow2 < dow3) {
                                daysToAdd = dow3 - cDow2;
                            }
                            if (cDow2 > dow3) {
                                daysToAdd = dow3 + (7 - cDow2);
                            }
                            final int lDay = this.getLastDayOfMonth(mon, cl.get(1));
                            if (day + daysToAdd > lDay) {
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
                    }
                    cl.set(5, day);
                    mon = cl.get(2) + 1;
                    int year = cl.get(1);
                    t = -1;
                    if (year > CronExpression.MAX_YEAR) {
                        return null;
                    }
                    st = this.months.tailSet(mon);
                    if (st != null && st.size() != 0) {
                        t = mon;
                        mon = st.first();
                    }
                    else {
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
                    }
                    else {
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
                        }
                        else {
                            cl.set(1, year);
                            gotOne = true;
                        }
                    }
                }
            }
        }
        return cl.getTime();
    }
    
    protected void setCalendarHour(final Calendar cal, final int hour) {
        cal.set(11, hour);
        if (cal.get(11) != hour && hour != 24) {
            cal.set(11, hour + 1);
        }
    }
    
    protected Date getTimeBefore(final Date targetDate) {
        final Calendar cl = Calendar.getInstance(this.getTimeZone());
        cl.setTime(targetDate);
        cl.set(14, 0);
        Date start;
        final Date targetDateNoMs = start = cl.getTime();
        final long minIncrement = this.findMinIncrement();
        Date prevFireTime;
        do {
            final Date prevCheckDate = new Date(start.getTime() - minIncrement);
            prevFireTime = this.getTimeAfter(prevCheckDate);
            if (prevFireTime == null || prevFireTime.before(CronExpression.MIN_DATE)) {
                return null;
            }
            start = prevCheckDate;
        } while (prevFireTime.compareTo(targetDateNoMs) >= 0);
        return prevFireTime;
    }
    
    public Date getPrevFireTime(final Date targetDate) {
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
    
    private int minInSet(final TreeSet<Integer> set) {
        int previous = 0;
        int min = Integer.MAX_VALUE;
        boolean first = true;
        for (final int value : set) {
            if (first) {
                previous = value;
                first = false;
            }
            else {
                final int diff = value - previous;
                if (diff >= min) {
                    continue;
                }
                min = diff;
            }
        }
        return min;
    }
    
    public Date getFinalFireTime() {
        return null;
    }
    
    protected boolean isLeapYear(final int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    
    protected int getLastDayOfMonth(final int monthNum, final int year) {
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
            default: {
                throw new IllegalArgumentException("Illegal month number: " + monthNum);
            }
        }
    }
    
    static {
        ALL_SPEC = 99;
        NO_SPEC = 98;
        monthMap = new HashMap<String, Integer>(20);
        dayMap = new HashMap<String, Integer>(60);
        CronExpression.monthMap.put("JAN", 0);
        CronExpression.monthMap.put("FEB", 1);
        CronExpression.monthMap.put("MAR", 2);
        CronExpression.monthMap.put("APR", 3);
        CronExpression.monthMap.put("MAY", 4);
        CronExpression.monthMap.put("JUN", 5);
        CronExpression.monthMap.put("JUL", 6);
        CronExpression.monthMap.put("AUG", 7);
        CronExpression.monthMap.put("SEP", 8);
        CronExpression.monthMap.put("OCT", 9);
        CronExpression.monthMap.put("NOV", 10);
        CronExpression.monthMap.put("DEC", 11);
        CronExpression.dayMap.put("SUN", 1);
        CronExpression.dayMap.put("MON", 2);
        CronExpression.dayMap.put("TUE", 3);
        CronExpression.dayMap.put("WED", 4);
        CronExpression.dayMap.put("THU", 5);
        CronExpression.dayMap.put("FRI", 6);
        CronExpression.dayMap.put("SAT", 7);
        MAX_YEAR = Calendar.getInstance().get(1) + 100;
        (MIN_CAL = Calendar.getInstance()).set(1970, 0, 1);
        MIN_DATE = CronExpression.MIN_CAL.getTime();
    }
    
    private class ValueSet
    {
        public int value;
        public int pos;
    }
}
