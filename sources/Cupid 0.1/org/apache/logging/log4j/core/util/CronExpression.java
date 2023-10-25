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
  
  protected static final Integer ALL_SPEC = Integer.valueOf(99);
  
  protected static final Integer NO_SPEC = Integer.valueOf(98);
  
  protected static final Map<String, Integer> monthMap = new HashMap<>(20);
  
  protected static final Map<String, Integer> dayMap = new HashMap<>(60);
  
  private final String cronExpression;
  
  static {
    monthMap.put("JAN", Integer.valueOf(0));
    monthMap.put("FEB", Integer.valueOf(1));
    monthMap.put("MAR", Integer.valueOf(2));
    monthMap.put("APR", Integer.valueOf(3));
    monthMap.put("MAY", Integer.valueOf(4));
    monthMap.put("JUN", Integer.valueOf(5));
    monthMap.put("JUL", Integer.valueOf(6));
    monthMap.put("AUG", Integer.valueOf(7));
    monthMap.put("SEP", Integer.valueOf(8));
    monthMap.put("OCT", Integer.valueOf(9));
    monthMap.put("NOV", Integer.valueOf(10));
    monthMap.put("DEC", Integer.valueOf(11));
    dayMap.put("SUN", Integer.valueOf(1));
    dayMap.put("MON", Integer.valueOf(2));
    dayMap.put("TUE", Integer.valueOf(3));
    dayMap.put("WED", Integer.valueOf(4));
    dayMap.put("THU", Integer.valueOf(5));
    dayMap.put("FRI", Integer.valueOf(6));
    dayMap.put("SAT", Integer.valueOf(7));
  }
  
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
  
  public static final int MAX_YEAR = Calendar.getInstance().get(1) + 100;
  
  public static final Calendar MIN_CAL = Calendar.getInstance();
  
  static {
    MIN_CAL.set(1970, 0, 1);
  }
  
  public static final Date MIN_DATE = MIN_CAL.getTime();
  
  public CronExpression(String cronExpression) throws ParseException {
    if (cronExpression == null)
      throw new IllegalArgumentException("cronExpression cannot be null"); 
    this.cronExpression = cronExpression.toUpperCase(Locale.US);
    buildExpression(this.cronExpression);
  }
  
  public boolean isSatisfiedBy(Date date) {
    Calendar testDateCal = Calendar.getInstance(getTimeZone());
    testDateCal.setTime(date);
    testDateCal.set(14, 0);
    Date originalDate = testDateCal.getTime();
    testDateCal.add(13, -1);
    Date timeAfter = getTimeAfter(testDateCal.getTime());
    return (timeAfter != null && timeAfter.equals(originalDate));
  }
  
  public Date getNextValidTimeAfter(Date date) {
    return getTimeAfter(date);
  }
  
  public Date getNextInvalidTimeAfter(Date date) {
    long difference = 1000L;
    Calendar adjustCal = Calendar.getInstance(getTimeZone());
    adjustCal.setTime(date);
    adjustCal.set(14, 0);
    Date lastDate = adjustCal.getTime();
    while (difference == 1000L) {
      Date newDate = getTimeAfter(lastDate);
      if (newDate == null)
        break; 
      difference = newDate.getTime() - lastDate.getTime();
      if (difference == 1000L)
        lastDate = newDate; 
    } 
    return new Date(lastDate.getTime() + 1000L);
  }
  
  public TimeZone getTimeZone() {
    if (this.timeZone == null)
      this.timeZone = TimeZone.getDefault(); 
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
    } catch (ParseException pe) {
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
      if (this.seconds == null)
        this.seconds = new TreeSet<>(); 
      if (this.minutes == null)
        this.minutes = new TreeSet<>(); 
      if (this.hours == null)
        this.hours = new TreeSet<>(); 
      if (this.daysOfMonth == null)
        this.daysOfMonth = new TreeSet<>(); 
      if (this.months == null)
        this.months = new TreeSet<>(); 
      if (this.daysOfWeek == null)
        this.daysOfWeek = new TreeSet<>(); 
      if (this.years == null)
        this.years = new TreeSet<>(); 
      int exprOn = 0;
      StringTokenizer exprsTok = new StringTokenizer(expression, " \t", false);
      while (exprsTok.hasMoreTokens() && exprOn <= 6) {
        String expr = exprsTok.nextToken().trim();
        if (exprOn == 3 && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(","))
          throw new ParseException("Support for specifying 'L' and 'LW' with other days of the month is not implemented", -1); 
        if (exprOn == 5 && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(","))
          throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1); 
        if (exprOn == 5 && expr.indexOf('#') != -1 && expr.indexOf('#', expr.indexOf('#') + 1) != -1)
          throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1); 
        StringTokenizer vTok = new StringTokenizer(expr, ",");
        while (vTok.hasMoreTokens()) {
          String v = vTok.nextToken();
          storeExpressionVals(0, v, exprOn);
        } 
        exprOn++;
      } 
      if (exprOn <= 5)
        throw new ParseException("Unexpected end of expression.", expression
            .length()); 
      if (exprOn <= 6)
        storeExpressionVals(0, "*", 6); 
      TreeSet<Integer> dow = getSet(5);
      TreeSet<Integer> dom = getSet(3);
      boolean dayOfMSpec = !dom.contains(NO_SPEC);
      boolean dayOfWSpec = !dow.contains(NO_SPEC);
      if ((!dayOfMSpec || dayOfWSpec) && (
        !dayOfWSpec || dayOfMSpec))
        throw new ParseException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.", 0); 
    } catch (ParseException pe) {
      throw pe;
    } catch (Exception e) {
      throw new ParseException("Illegal cron expression format (" + e
          .toString() + ")", 0);
    } 
  }
  
  protected int storeExpressionVals(int pos, String s, int type) throws ParseException {
    int incr = 0;
    int i = skipWhiteSpace(pos, s);
    if (i >= s.length())
      return i; 
    char c = s.charAt(i);
    if (c >= 'A' && c <= 'Z' && !s.equals("L") && !s.equals("LW") && !s.matches("^L-[0-9]*[W]?")) {
      String sub = s.substring(i, i + 3);
      int sval = -1;
      int eval = -1;
      if (type == 4) {
        sval = getMonthNumber(sub) + 1;
        if (sval <= 0)
          throw new ParseException("Invalid Month value: '" + sub + "'", i); 
        if (s.length() > i + 3) {
          c = s.charAt(i + 3);
          if (c == '-') {
            i += 4;
            sub = s.substring(i, i + 3);
            eval = getMonthNumber(sub) + 1;
            if (eval <= 0)
              throw new ParseException("Invalid Month value: '" + sub + "'", i); 
          } 
        } 
      } else if (type == 5) {
        sval = getDayOfWeekNumber(sub);
        if (sval < 0)
          throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i); 
        if (s.length() > i + 3) {
          c = s.charAt(i + 3);
          switch (c) {
            case '-':
              i += 4;
              sub = s.substring(i, i + 3);
              eval = getDayOfWeekNumber(sub);
              if (eval < 0)
                throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i); 
              break;
            case '#':
              try {
                i += 4;
                this.nthdayOfWeek = Integer.parseInt(s.substring(i));
                if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5)
                  throw new Exception(); 
              } catch (Exception e) {
                throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
              } 
              break;
            case 'L':
              this.lastdayOfWeek = true;
              i++;
              break;
          } 
        } 
      } else {
        throw new ParseException("Illegal characters for this position: '" + sub + "'", i);
      } 
      if (eval != -1)
        incr = 1; 
      addToSet(sval, eval, incr, type);
      return i + 3;
    } 
    switch (c) {
      case '?':
        i++;
        if (i + 1 < s.length() && s
          .charAt(i) != ' ' && s.charAt(i + 1) != '\t')
          throw new ParseException("Illegal character after '?': " + s
              .charAt(i), i); 
        if (type != 5 && type != 3)
          throw new ParseException("'?' can only be specfied for Day-of-Month or Day-of-Week.", i); 
        if (type == 5 && !this.lastdayOfMonth) {
          int val = ((Integer)this.daysOfMonth.last()).intValue();
          if (val == 98)
            throw new ParseException("'?' can only be specfied for Day-of-Month -OR- Day-of-Week.", i); 
        } 
        addToSet(98, -1, 0, type);
        return i;
      case '*':
      case '/':
        if (c == '*' && i + 1 >= s.length()) {
          addToSet(99, -1, incr, type);
          return i + 1;
        } 
        if (c == '/' && (i + 1 >= s
          .length() || s.charAt(i + 1) == ' ' || s
          .charAt(i + 1) == '\t'))
          throw new ParseException("'/' must be followed by an integer.", i); 
        if (c == '*')
          i++; 
        c = s.charAt(i);
        if (c == '/') {
          i++;
          if (i >= s.length())
            throw new ParseException("Unexpected end of string.", i); 
          incr = getNumericValue(s, i);
          i++;
          if (incr > 10)
            i++; 
          if (incr > 59 && (type == 0 || type == 1))
            throw new ParseException("Increment > 60 : " + incr, i); 
          if (incr > 23 && type == 2)
            throw new ParseException("Increment > 24 : " + incr, i); 
          if (incr > 31 && type == 3)
            throw new ParseException("Increment > 31 : " + incr, i); 
          if (incr > 7 && type == 5)
            throw new ParseException("Increment > 7 : " + incr, i); 
          if (incr > 12 && type == 4)
            throw new ParseException("Increment > 12 : " + incr, i); 
        } else {
          incr = 1;
        } 
        addToSet(99, -1, incr, type);
        return i;
      case 'L':
        i++;
        if (type == 3)
          this.lastdayOfMonth = true; 
        if (type == 5)
          addToSet(7, 7, 0, type); 
        if (type == 3 && s.length() > i) {
          c = s.charAt(i);
          if (c == '-') {
            ValueSet vs = getValue(0, s, i + 1);
            this.lastdayOffset = vs.value;
            if (this.lastdayOffset > 30)
              throw new ParseException("Offset from last day must be <= 30", i + 1); 
            i = vs.pos;
          } 
          if (s.length() > i) {
            c = s.charAt(i);
            if (c == 'W') {
              this.nearestWeekday = true;
              i++;
            } 
          } 
        } 
        return i;
    } 
    if (c >= '0' && c <= '9') {
      int val = Integer.parseInt(String.valueOf(c));
      i++;
      if (i >= s.length()) {
        addToSet(val, -1, -1, type);
      } else {
        c = s.charAt(i);
        if (c >= '0' && c <= '9') {
          ValueSet vs = getValue(val, s, i);
          val = vs.value;
          i = vs.pos;
        } 
        i = checkNext(i, s, val, type);
        return i;
      } 
    } else {
      throw new ParseException("Unexpected character: " + c, i);
    } 
    return i;
  }
  
  protected int checkNext(int pos, String s, int val, int type) throws ParseException {
    TreeSet<Integer> set;
    int v, v2, end = -1;
    int i = pos;
    if (i >= s.length()) {
      addToSet(val, end, -1, type);
      return i;
    } 
    char c = s.charAt(pos);
    if (c == 'L') {
      if (type == 5) {
        if (val < 1 || val > 7)
          throw new ParseException("Day-of-Week values must be between 1 and 7", -1); 
        this.lastdayOfWeek = true;
      } else {
        throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
      } 
      TreeSet<Integer> treeSet = getSet(type);
      treeSet.add(Integer.valueOf(val));
      i++;
      return i;
    } 
    if (c == 'W') {
      if (type == 3) {
        this.nearestWeekday = true;
      } else {
        throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
      } 
      if (val > 31)
        throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i); 
      TreeSet<Integer> treeSet = getSet(type);
      treeSet.add(Integer.valueOf(val));
      i++;
      return i;
    } 
    switch (c) {
      case '#':
        if (type != 5)
          throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i); 
        i++;
        try {
          this.nthdayOfWeek = Integer.parseInt(s.substring(i));
          if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5)
            throw new Exception(); 
        } catch (Exception e) {
          throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
        } 
        set = getSet(type);
        set.add(Integer.valueOf(val));
        i++;
        return i;
      case '-':
        i++;
        c = s.charAt(i);
        v = Integer.parseInt(String.valueOf(c));
        end = v;
        i++;
        if (i >= s.length()) {
          addToSet(val, end, 1, type);
          return i;
        } 
        c = s.charAt(i);
        if (c >= '0' && c <= '9') {
          ValueSet vs = getValue(v, s, i);
          end = vs.value;
          i = vs.pos;
        } 
        if (i < s.length() && (c = s.charAt(i)) == '/') {
          i++;
          c = s.charAt(i);
          int j = Integer.parseInt(String.valueOf(c));
          i++;
          if (i >= s.length()) {
            addToSet(val, end, j, type);
            return i;
          } 
          c = s.charAt(i);
          if (c >= '0' && c <= '9') {
            ValueSet vs = getValue(j, s, i);
            int v3 = vs.value;
            addToSet(val, end, v3, type);
            i = vs.pos;
          } else {
            addToSet(val, end, j, type);
          } 
          return i;
        } 
        addToSet(val, end, 1, type);
        return i;
      case '/':
        i++;
        c = s.charAt(i);
        v2 = Integer.parseInt(String.valueOf(c));
        i++;
        if (i >= s.length()) {
          addToSet(val, end, v2, type);
          return i;
        } 
        c = s.charAt(i);
        if (c >= '0' && c <= '9') {
          ValueSet vs = getValue(v2, s, i);
          int v3 = vs.value;
          addToSet(val, end, v3, type);
          i = vs.pos;
          return i;
        } 
        throw new ParseException("Unexpected character '" + c + "' after '/'", i);
    } 
    addToSet(val, end, 0, type);
    i++;
    return i;
  }
  
  public String getCronExpression() {
    return this.cronExpression;
  }
  
  public String getExpressionSummary() {
    StringBuilder buf = new StringBuilder();
    buf.append("seconds: ");
    buf.append(getExpressionSetSummary(this.seconds));
    buf.append("\n");
    buf.append("minutes: ");
    buf.append(getExpressionSetSummary(this.minutes));
    buf.append("\n");
    buf.append("hours: ");
    buf.append(getExpressionSetSummary(this.hours));
    buf.append("\n");
    buf.append("daysOfMonth: ");
    buf.append(getExpressionSetSummary(this.daysOfMonth));
    buf.append("\n");
    buf.append("months: ");
    buf.append(getExpressionSetSummary(this.months));
    buf.append("\n");
    buf.append("daysOfWeek: ");
    buf.append(getExpressionSetSummary(this.daysOfWeek));
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
    buf.append(getExpressionSetSummary(this.years));
    buf.append("\n");
    return buf.toString();
  }
  
  protected String getExpressionSetSummary(Set<Integer> set) {
    if (set.contains(NO_SPEC))
      return "?"; 
    if (set.contains(ALL_SPEC))
      return "*"; 
    StringBuilder buf = new StringBuilder();
    Iterator<Integer> itr = set.iterator();
    boolean first = true;
    while (itr.hasNext()) {
      Integer iVal = itr.next();
      String val = iVal.toString();
      if (!first)
        buf.append(","); 
      buf.append(val);
      first = false;
    } 
    return buf.toString();
  }
  
  protected String getExpressionSetSummary(ArrayList<Integer> list) {
    if (list.contains(NO_SPEC))
      return "?"; 
    if (list.contains(ALL_SPEC))
      return "*"; 
    StringBuilder buf = new StringBuilder();
    Iterator<Integer> itr = list.iterator();
    boolean first = true;
    while (itr.hasNext()) {
      Integer iVal = itr.next();
      String val = iVal.toString();
      if (!first)
        buf.append(","); 
      buf.append(val);
      first = false;
    } 
    return buf.toString();
  }
  
  protected int skipWhiteSpace(int i, String s) {
    for (; i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t'); i++);
    return i;
  }
  
  protected int findNextWhiteSpace(int i, String s) {
    for (; i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t'); i++);
    return i;
  }
  
  protected void addToSet(int val, int end, int incr, int type) throws ParseException {
    TreeSet<Integer> set = getSet(type);
    switch (type) {
      case 0:
      case 1:
        if ((val < 0 || val > 59 || end > 59) && val != 99)
          throw new ParseException("Minute and Second values must be between 0 and 59", -1); 
        break;
      case 2:
        if ((val < 0 || val > 23 || end > 23) && val != 99)
          throw new ParseException("Hour values must be between 0 and 23", -1); 
        break;
      case 3:
        if ((val < 1 || val > 31 || end > 31) && val != 99 && val != 98)
          throw new ParseException("Day of month values must be between 1 and 31", -1); 
        break;
      case 4:
        if ((val < 1 || val > 12 || end > 12) && val != 99)
          throw new ParseException("Month values must be between 1 and 12", -1); 
        break;
      case 5:
        if ((val == 0 || val > 7 || end > 7) && val != 99 && val != 98)
          throw new ParseException("Day-of-Week values must be between 1 and 7", -1); 
        break;
    } 
    if ((incr == 0 || incr == -1) && val != 99) {
      if (val != -1) {
        set.add(Integer.valueOf(val));
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
      case 1:
        if (stopAt == -1)
          stopAt = 59; 
        if (startAt == -1 || startAt == 99)
          startAt = 0; 
        break;
      case 2:
        if (stopAt == -1)
          stopAt = 23; 
        if (startAt == -1 || startAt == 99)
          startAt = 0; 
        break;
      case 3:
        if (stopAt == -1)
          stopAt = 31; 
        if (startAt == -1 || startAt == 99)
          startAt = 1; 
        break;
      case 4:
        if (stopAt == -1)
          stopAt = 12; 
        if (startAt == -1 || startAt == 99)
          startAt = 1; 
        break;
      case 5:
        if (stopAt == -1)
          stopAt = 7; 
        if (startAt == -1 || startAt == 99)
          startAt = 1; 
        break;
      case 6:
        if (stopAt == -1)
          stopAt = MAX_YEAR; 
        if (startAt == -1 || startAt == 99)
          startAt = 1970; 
        break;
    } 
    int max = -1;
    if (stopAt < startAt) {
      switch (type) {
        case 0:
          max = 60;
          break;
        case 1:
          max = 60;
          break;
        case 2:
          max = 24;
          break;
        case 4:
          max = 12;
          break;
        case 5:
          max = 7;
          break;
        case 3:
          max = 31;
          break;
        case 6:
          throw new IllegalArgumentException("Start year must be less than stop year");
        default:
          throw new IllegalArgumentException("Unexpected type encountered");
      } 
      stopAt += max;
    } 
    int i;
    for (i = startAt; i <= stopAt; i += incr) {
      if (max == -1) {
        set.add(Integer.valueOf(i));
      } else {
        int i2 = i % max;
        if (i2 == 0 && (type == 4 || type == 5 || type == 3))
          i2 = max; 
        set.add(Integer.valueOf(i2));
      } 
    } 
  }
  
  TreeSet<Integer> getSet(int type) {
    switch (type) {
      case 0:
        return this.seconds;
      case 1:
        return this.minutes;
      case 2:
        return this.hours;
      case 3:
        return this.daysOfMonth;
      case 4:
        return this.months;
      case 5:
        return this.daysOfWeek;
      case 6:
        return this.years;
    } 
    return null;
  }
  
  protected ValueSet getValue(int v, String s, int i) {
    char c = s.charAt(i);
    StringBuilder s1 = new StringBuilder(String.valueOf(v));
    while (c >= '0' && c <= '9') {
      s1.append(c);
      i++;
      if (i >= s.length())
        break; 
      c = s.charAt(i);
    } 
    ValueSet val = new ValueSet();
    val.pos = (i < s.length()) ? i : (i + 1);
    val.value = Integer.parseInt(s1.toString());
    return val;
  }
  
  protected int getNumericValue(String s, int i) {
    int endOfVal = findNextWhiteSpace(i, s);
    String val = s.substring(i, endOfVal);
    return Integer.parseInt(val);
  }
  
  protected int getMonthNumber(String s) {
    Integer integer = monthMap.get(s);
    if (integer == null)
      return -1; 
    return integer.intValue();
  }
  
  protected int getDayOfWeekNumber(String s) {
    Integer integer = dayMap.get(s);
    if (integer == null)
      return -1; 
    return integer.intValue();
  }
  
  public Date getTimeAfter(Date afterTime) {
    Calendar cl = new GregorianCalendar(getTimeZone());
    afterTime = new Date(afterTime.getTime() + 1000L);
    cl.setTime(afterTime);
    cl.set(14, 0);
    boolean gotOne = false;
    while (!gotOne) {
      if (cl.get(1) > 2999)
        return null; 
      int sec = cl.get(13);
      int min = cl.get(12);
      SortedSet<Integer> st = this.seconds.tailSet(Integer.valueOf(sec));
      if (st != null && st.size() != 0) {
        sec = ((Integer)st.first()).intValue();
      } else {
        sec = ((Integer)this.seconds.first()).intValue();
        min++;
        cl.set(12, min);
      } 
      cl.set(13, sec);
      min = cl.get(12);
      int hr = cl.get(11);
      int t = -1;
      st = this.minutes.tailSet(Integer.valueOf(min));
      if (st != null && st.size() != 0) {
        t = min;
        min = ((Integer)st.first()).intValue();
      } else {
        min = ((Integer)this.minutes.first()).intValue();
        hr++;
      } 
      if (min != t) {
        cl.set(13, 0);
        cl.set(12, min);
        setCalendarHour(cl, hr);
        continue;
      } 
      cl.set(12, min);
      hr = cl.get(11);
      int day = cl.get(5);
      t = -1;
      st = this.hours.tailSet(Integer.valueOf(hr));
      if (st != null && st.size() != 0) {
        t = hr;
        hr = ((Integer)st.first()).intValue();
      } else {
        hr = ((Integer)this.hours.first()).intValue();
        day++;
      } 
      if (hr != t) {
        cl.set(13, 0);
        cl.set(12, 0);
        cl.set(5, day);
        setCalendarHour(cl, hr);
        continue;
      } 
      cl.set(11, hr);
      day = cl.get(5);
      int mon = cl.get(2) + 1;
      t = -1;
      int tmon = mon;
      boolean dayOfMSpec = !this.daysOfMonth.contains(NO_SPEC);
      boolean dayOfWSpec = !this.daysOfWeek.contains(NO_SPEC);
      if (dayOfMSpec && !dayOfWSpec) {
        st = this.daysOfMonth.tailSet(Integer.valueOf(day));
        if (this.lastdayOfMonth) {
          if (!this.nearestWeekday) {
            t = day;
            day = getLastDayOfMonth(mon, cl.get(1));
            day -= this.lastdayOffset;
            if (t > day) {
              mon++;
              if (mon > 12) {
                mon = 1;
                tmon = 3333;
                cl.add(1, 1);
              } 
              day = 1;
            } 
          } else {
            t = day;
            day = getLastDayOfMonth(mon, cl.get(1));
            day -= this.lastdayOffset;
            Calendar tcal = Calendar.getInstance(getTimeZone());
            tcal.set(13, 0);
            tcal.set(12, 0);
            tcal.set(11, 0);
            tcal.set(5, day);
            tcal.set(2, mon - 1);
            tcal.set(1, cl.get(1));
            int ldom = getLastDayOfMonth(mon, cl.get(1));
            int dow = tcal.get(7);
            if (dow == 7 && day == 1) {
              day += 2;
            } else if (dow == 7) {
              day--;
            } else if (dow == 1 && day == ldom) {
              day -= 2;
            } else if (dow == 1) {
              day++;
            } 
            tcal.set(13, sec);
            tcal.set(12, min);
            tcal.set(11, hr);
            tcal.set(5, day);
            tcal.set(2, mon - 1);
            Date nTime = tcal.getTime();
            if (nTime.before(afterTime)) {
              day = 1;
              mon++;
            } 
          } 
        } else if (this.nearestWeekday) {
          t = day;
          day = ((Integer)this.daysOfMonth.first()).intValue();
          Calendar tcal = Calendar.getInstance(getTimeZone());
          tcal.set(13, 0);
          tcal.set(12, 0);
          tcal.set(11, 0);
          tcal.set(5, day);
          tcal.set(2, mon - 1);
          tcal.set(1, cl.get(1));
          int ldom = getLastDayOfMonth(mon, cl.get(1));
          int dow = tcal.get(7);
          if (dow == 7 && day == 1) {
            day += 2;
          } else if (dow == 7) {
            day--;
          } else if (dow == 1 && day == ldom) {
            day -= 2;
          } else if (dow == 1) {
            day++;
          } 
          tcal.set(13, sec);
          tcal.set(12, min);
          tcal.set(11, hr);
          tcal.set(5, day);
          tcal.set(2, mon - 1);
          Date nTime = tcal.getTime();
          if (nTime.before(afterTime)) {
            day = ((Integer)this.daysOfMonth.first()).intValue();
            mon++;
          } 
        } else if (st != null && st.size() != 0) {
          t = day;
          day = ((Integer)st.first()).intValue();
          int lastDay = getLastDayOfMonth(mon, cl.get(1));
          if (day > lastDay) {
            day = ((Integer)this.daysOfMonth.first()).intValue();
            mon++;
          } 
        } else {
          day = ((Integer)this.daysOfMonth.first()).intValue();
          mon++;
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
        if (this.lastdayOfWeek) {
          int dow = ((Integer)this.daysOfWeek.first()).intValue();
          int cDow = cl.get(7);
          int daysToAdd = 0;
          if (cDow < dow)
            daysToAdd = dow - cDow; 
          if (cDow > dow)
            daysToAdd = dow + 7 - cDow; 
          int lDay = getLastDayOfMonth(mon, cl.get(1));
          if (day + daysToAdd > lDay) {
            cl.set(13, 0);
            cl.set(12, 0);
            cl.set(11, 0);
            cl.set(5, 1);
            cl.set(2, mon);
            continue;
          } 
          while (day + daysToAdd + 7 <= lDay)
            daysToAdd += 7; 
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
          int dow = ((Integer)this.daysOfWeek.first()).intValue();
          int cDow = cl.get(7);
          int daysToAdd = 0;
          if (cDow < dow) {
            daysToAdd = dow - cDow;
          } else if (cDow > dow) {
            daysToAdd = dow + 7 - cDow;
          } 
          boolean dayShifted = false;
          if (daysToAdd > 0)
            dayShifted = true; 
          day += daysToAdd;
          int weekOfMonth = day / 7;
          if (day % 7 > 0)
            weekOfMonth++; 
          daysToAdd = (this.nthdayOfWeek - weekOfMonth) * 7;
          day += daysToAdd;
          if (daysToAdd < 0 || day > 
            getLastDayOfMonth(mon, cl
              .get(1))) {
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
          int cDow = cl.get(7);
          int dow = ((Integer)this.daysOfWeek.first()).intValue();
          st = this.daysOfWeek.tailSet(Integer.valueOf(cDow));
          if (st != null && st.size() > 0)
            dow = ((Integer)st.first()).intValue(); 
          int daysToAdd = 0;
          if (cDow < dow)
            daysToAdd = dow - cDow; 
          if (cDow > dow)
            daysToAdd = dow + 7 - cDow; 
          int lDay = getLastDayOfMonth(mon, cl.get(1));
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
      } else {
        throw new UnsupportedOperationException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
      } 
      cl.set(5, day);
      mon = cl.get(2) + 1;
      int year = cl.get(1);
      t = -1;
      if (year > MAX_YEAR)
        return null; 
      st = this.months.tailSet(Integer.valueOf(mon));
      if (st != null && st.size() != 0) {
        t = mon;
        mon = ((Integer)st.first()).intValue();
      } else {
        mon = ((Integer)this.months.first()).intValue();
        year++;
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
      st = this.years.tailSet(Integer.valueOf(year));
      if (st != null && st.size() != 0) {
        t = year;
        year = ((Integer)st.first()).intValue();
      } else {
        return null;
      } 
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
    if (cal.get(11) != hour && hour != 24)
      cal.set(11, hour + 1); 
  }
  
  protected Date getTimeBefore(Date targetDate) {
    Calendar cl = Calendar.getInstance(getTimeZone());
    cl.setTime(targetDate);
    cl.set(14, 0);
    Date targetDateNoMs = cl.getTime();
    Date start = targetDateNoMs;
    long minIncrement = findMinIncrement();
    while (true) {
      Date prevCheckDate = new Date(start.getTime() - minIncrement);
      Date prevFireTime = getTimeAfter(prevCheckDate);
      if (prevFireTime == null || prevFireTime.before(MIN_DATE))
        return null; 
      start = prevCheckDate;
      if (prevFireTime.compareTo(targetDateNoMs) < 0)
        return prevFireTime; 
    } 
  }
  
  public Date getPrevFireTime(Date targetDate) {
    return getTimeBefore(targetDate);
  }
  
  private long findMinIncrement() {
    if (this.seconds.size() != 1)
      return (minInSet(this.seconds) * 1000); 
    if (((Integer)this.seconds.first()).intValue() == 99)
      return 1000L; 
    if (this.minutes.size() != 1)
      return (minInSet(this.minutes) * 60000); 
    if (((Integer)this.minutes.first()).intValue() == 99)
      return 60000L; 
    if (this.hours.size() != 1)
      return (minInSet(this.hours) * 3600000); 
    if (((Integer)this.hours.first()).intValue() == 99)
      return 3600000L; 
    return 86400000L;
  }
  
  private int minInSet(TreeSet<Integer> set) {
    int previous = 0;
    int min = Integer.MAX_VALUE;
    boolean first = true;
    for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext(); ) {
      int value = ((Integer)iterator.next()).intValue();
      if (first) {
        previous = value;
        first = false;
        continue;
      } 
      int diff = value - previous;
      if (diff < min)
        min = diff; 
    } 
    return min;
  }
  
  public Date getFinalFireTime() {
    return null;
  }
  
  protected boolean isLeapYear(int year) {
    return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
  }
  
  protected int getLastDayOfMonth(int monthNum, int year) {
    switch (monthNum) {
      case 1:
        return 31;
      case 2:
        return isLeapYear(year) ? 29 : 28;
      case 3:
        return 31;
      case 4:
        return 30;
      case 5:
        return 31;
      case 6:
        return 30;
      case 7:
        return 31;
      case 8:
        return 31;
      case 9:
        return 30;
      case 10:
        return 31;
      case 11:
        return 30;
      case 12:
        return 31;
    } 
    throw new IllegalArgumentException("Illegal month number: " + monthNum);
  }
  
  private class ValueSet {
    public int value;
    
    public int pos;
    
    private ValueSet() {}
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\CronExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */