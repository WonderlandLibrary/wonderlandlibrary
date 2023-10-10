package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

public class VTimeZone extends BasicTimeZone {
   private static final long serialVersionUID = -6851467294127795902L;
   private BasicTimeZone tz;
   private List vtzlines;
   private String olsonzid = null;
   private String tzurl = null;
   private Date lastmod = null;
   private static String ICU_TZVERSION;
   private static final String ICU_TZINFO_PROP = "X-TZINFO";
   private static final int DEF_DSTSAVINGS = 3600000;
   private static final long DEF_TZSTARTTIME = 0L;
   private static final long MIN_TIME = Long.MIN_VALUE;
   private static final long MAX_TIME = Long.MAX_VALUE;
   private static final String COLON = ":";
   private static final String SEMICOLON = ";";
   private static final String EQUALS_SIGN = "=";
   private static final String COMMA = ",";
   private static final String NEWLINE = "\r\n";
   private static final String ICAL_BEGIN_VTIMEZONE = "BEGIN:VTIMEZONE";
   private static final String ICAL_END_VTIMEZONE = "END:VTIMEZONE";
   private static final String ICAL_BEGIN = "BEGIN";
   private static final String ICAL_END = "END";
   private static final String ICAL_VTIMEZONE = "VTIMEZONE";
   private static final String ICAL_TZID = "TZID";
   private static final String ICAL_STANDARD = "STANDARD";
   private static final String ICAL_DAYLIGHT = "DAYLIGHT";
   private static final String ICAL_DTSTART = "DTSTART";
   private static final String ICAL_TZOFFSETFROM = "TZOFFSETFROM";
   private static final String ICAL_TZOFFSETTO = "TZOFFSETTO";
   private static final String ICAL_RDATE = "RDATE";
   private static final String ICAL_RRULE = "RRULE";
   private static final String ICAL_TZNAME = "TZNAME";
   private static final String ICAL_TZURL = "TZURL";
   private static final String ICAL_LASTMOD = "LAST-MODIFIED";
   private static final String ICAL_FREQ = "FREQ";
   private static final String ICAL_UNTIL = "UNTIL";
   private static final String ICAL_YEARLY = "YEARLY";
   private static final String ICAL_BYMONTH = "BYMONTH";
   private static final String ICAL_BYDAY = "BYDAY";
   private static final String ICAL_BYMONTHDAY = "BYMONTHDAY";
   private static final String[] ICAL_DOW_NAMES = new String[]{"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
   private static final int[] MONTHLENGTH = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   private static final int INI = 0;
   private static final int VTZ = 1;
   private static final int TZI = 2;
   private static final int ERR = 3;
   private transient boolean isFrozen = false;

   public static VTimeZone create(String var0) {
      VTimeZone var1 = new VTimeZone(var0);
      var1.tz = (BasicTimeZone)TimeZone.getTimeZone(var0, 0);
      var1.olsonzid = var1.tz.getID();
      return var1;
   }

   public static VTimeZone create(Reader var0) {
      VTimeZone var1 = new VTimeZone();
      return var1 == var0 ? var1 : null;
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.tz.getOffset(var1, var2, var3, var4, var5, var6);
   }

   public void getOffset(long var1, boolean var3, int[] var4) {
      this.tz.getOffset(var1, var3, var4);
   }

   /** @deprecated */
   public void getOffsetFromLocal(long var1, int var3, int var4, int[] var5) {
      this.tz.getOffsetFromLocal(var1, var3, var4, var5);
   }

   public int getRawOffset() {
      return this.tz.getRawOffset();
   }

   public boolean inDaylightTime(Date var1) {
      return this.tz.inDaylightTime(var1);
   }

   public void setRawOffset(int var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
      } else {
         this.tz.setRawOffset(var1);
      }
   }

   public boolean useDaylightTime() {
      return this.tz.useDaylightTime();
   }

   public boolean observesDaylightTime() {
      return this.tz.observesDaylightTime();
   }

   public boolean hasSameRules(TimeZone var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof VTimeZone ? this.tz.hasSameRules(((VTimeZone)var1).tz) : this.tz.hasSameRules(var1);
      }
   }

   public String getTZURL() {
      return this.tzurl;
   }

   public void setTZURL(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
      } else {
         this.tzurl = var1;
      }
   }

   public Date getLastModified() {
      return this.lastmod;
   }

   public void setLastModified(Date var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen VTimeZone instance.");
      } else {
         this.lastmod = var1;
      }
   }

   public void write(Writer var1) throws IOException {
      BufferedWriter var2 = new BufferedWriter(var1);
      if (this.vtzlines != null) {
         Iterator var3 = this.vtzlines.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (var4.startsWith("TZURL:")) {
               if (this.tzurl != null) {
                  var2.write("TZURL");
                  var2.write(":");
                  var2.write(this.tzurl);
                  var2.write("\r\n");
               }
            } else if (var4.startsWith("LAST-MODIFIED:")) {
               if (this.lastmod != null) {
                  var2.write("LAST-MODIFIED");
                  var2.write(":");
                  var2.write(getUTCDateTimeString(this.lastmod.getTime()));
                  var2.write("\r\n");
               }
            } else {
               var2.write(var4);
               var2.write("\r\n");
            }
         }

         var2.flush();
      } else {
         String[] var5 = null;
         if (this.olsonzid != null && ICU_TZVERSION != null) {
            var5 = new String[]{"X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "]"};
         }

         this.writeZone(var1, this.tz, var5);
      }

   }

   public void write(Writer var1, long var2) throws IOException {
      TimeZoneRule[] var4 = this.tz.getTimeZoneRules(var2);
      RuleBasedTimeZone var5 = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule)var4[0]);

      for(int var6 = 1; var6 < var4.length; ++var6) {
         var5.addTransitionRule(var4[var6]);
      }

      String[] var7 = null;
      if (this.olsonzid != null && ICU_TZVERSION != null) {
         var7 = new String[]{"X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "/Partial@" + var2 + "]"};
      }

      this.writeZone(var1, var5, var7);
   }

   public void writeSimple(Writer var1, long var2) throws IOException {
      TimeZoneRule[] var4 = this.tz.getSimpleTimeZoneRulesNear(var2);
      RuleBasedTimeZone var5 = new RuleBasedTimeZone(this.tz.getID(), (InitialTimeZoneRule)var4[0]);

      for(int var6 = 1; var6 < var4.length; ++var6) {
         var5.addTransitionRule(var4[var6]);
      }

      String[] var7 = null;
      if (this.olsonzid != null && ICU_TZVERSION != null) {
         var7 = new String[]{"X-TZINFO:" + this.olsonzid + "[" + ICU_TZVERSION + "/Simple@" + var2 + "]"};
      }

      this.writeZone(var1, var5, var7);
   }

   public TimeZoneTransition getNextTransition(long var1, boolean var3) {
      return this.tz.getNextTransition(var1, var3);
   }

   public TimeZoneTransition getPreviousTransition(long var1, boolean var3) {
      return this.tz.getPreviousTransition(var1, var3);
   }

   public boolean hasEquivalentTransitions(TimeZone var1, long var2, long var4) {
      return this == var1 ? true : this.tz.hasEquivalentTransitions(var1, var2, var4);
   }

   public TimeZoneRule[] getTimeZoneRules() {
      return this.tz.getTimeZoneRules();
   }

   public TimeZoneRule[] getTimeZoneRules(long var1) {
      return this.tz.getTimeZoneRules(var1);
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   private VTimeZone() {
   }

   private VTimeZone(String var1) {
      super(var1);
   }

   private boolean parse() {
      if (this.vtzlines != null && this.vtzlines.size() != 0) {
         String var1 = null;
         byte var2 = 0;
         boolean var3 = false;
         String var4 = null;
         String var5 = null;
         String var6 = null;
         String var7 = null;
         boolean var8 = false;
         LinkedList var9 = null;
         ArrayList var10 = new ArrayList();
         int var11 = 0;
         int var12 = 0;
         long var13 = Long.MAX_VALUE;
         Iterator var15 = this.vtzlines.iterator();

         do {
            String var16;
            int var17;
            TimeZoneRule var20;
            int var35;
            int var43;
            do {
               if (!var15.hasNext()) {
                  if (var10.size() == 0) {
                     return false;
                  }

                  InitialTimeZoneRule var29 = new InitialTimeZoneRule(getDefaultTZName(var1, false), var11, var12);
                  RuleBasedTimeZone var30 = new RuleBasedTimeZone(var1, var29);
                  var17 = -1;
                  int var31 = 0;

                  for(int var32 = 0; var32 < var10.size(); ++var32) {
                     var20 = (TimeZoneRule)var10.get(var32);
                     if (var20 instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)var20).getEndYear() == Integer.MAX_VALUE) {
                        ++var31;
                        var17 = var32;
                     }
                  }

                  if (var31 > 2) {
                     return false;
                  }

                  if (var31 == 1) {
                     if (var10.size() == 1) {
                        var10.clear();
                     } else {
                        AnnualTimeZoneRule var33 = (AnnualTimeZoneRule)var10.get(var17);
                        int var40 = var33.getRawOffset();
                        var35 = var33.getDSTSavings();
                        Date var41 = var33.getFirstStart(var11, var12);
                        Date var44 = var41;

                        for(var43 = 0; var43 < var10.size(); ++var43) {
                           if (var17 != var43) {
                              TimeZoneRule var45 = (TimeZoneRule)var10.get(var43);
                              Date var26 = var45.getFinalStart(var40, var35);
                              if (var26.after(var44)) {
                                 var44 = var33.getNextStart(var26.getTime(), var45.getRawOffset(), var45.getDSTSavings(), false);
                              }
                           }
                        }

                        Object var47;
                        if (var44 == var41) {
                           var47 = new TimeArrayTimeZoneRule(var33.getName(), var33.getRawOffset(), var33.getDSTSavings(), new long[]{var41.getTime()}, 2);
                        } else {
                           int[] var46 = Grego.timeToFields(var44.getTime(), (int[])null);
                           var47 = new AnnualTimeZoneRule(var33.getName(), var33.getRawOffset(), var33.getDSTSavings(), var33.getRule(), var33.getStartYear(), var46[0]);
                        }

                        var10.set(var17, var47);
                     }
                  }

                  Iterator var34 = var10.iterator();

                  while(var34.hasNext()) {
                     var20 = (TimeZoneRule)var34.next();
                     var30.addTransitionRule(var20);
                  }

                  this.tz = var30;
                  this.setID(var1);
                  return true;
               }

               var16 = (String)var15.next();
               var17 = var16.indexOf(":");
            } while(var17 < 0);

            String var18 = var16.substring(0, var17);
            String var19 = var16.substring(var17 + 1);
            switch(var2) {
            case 0:
               if (var18.equals("BEGIN") && var19.equals("VTIMEZONE")) {
                  var2 = 1;
               }
               break;
            case 1:
               if (var18.equals("TZID")) {
                  var1 = var19;
               } else if (var18.equals("TZURL")) {
                  this.tzurl = var19;
               } else if (var18.equals("LAST-MODIFIED")) {
                  this.lastmod = new Date(parseDateTimeString(var19, 0));
               } else if (var18.equals("BEGIN")) {
                  boolean var38 = var19.equals("DAYLIGHT");
                  if (!var19.equals("STANDARD") && !var38) {
                     var2 = 3;
                  } else if (var1 == null) {
                     var2 = 3;
                  } else {
                     var9 = null;
                     var8 = false;
                     var4 = null;
                     var5 = null;
                     var6 = null;
                     var3 = var38;
                     var2 = 2;
                  }
               } else if (var18.equals("END")) {
               }
               break;
            case 2:
               if (var18.equals("DTSTART")) {
                  var7 = var19;
               } else if (var18.equals("TZNAME")) {
                  var6 = var19;
               } else if (var18.equals("TZOFFSETFROM")) {
                  var4 = var19;
               } else if (var18.equals("TZOFFSETTO")) {
                  var5 = var19;
               } else if (var18.equals("RDATE")) {
                  if (var8) {
                     var2 = 3;
                  } else {
                     if (var9 == null) {
                        var9 = new LinkedList();
                     }

                     java.util.StringTokenizer var36 = new java.util.StringTokenizer(var19, ",");

                     while(var36.hasMoreTokens()) {
                        String var37 = var36.nextToken();
                        var9.add(var37);
                     }
                  }
               } else if (var18.equals("RRULE")) {
                  if (!var8 && var9 != null) {
                     var2 = 3;
                  } else {
                     if (var9 == null) {
                        var9 = new LinkedList();
                     }

                     var8 = true;
                     var9.add(var19);
                  }
               } else if (var18.equals("END")) {
                  if (var7 != null && var4 != null && var5 != null) {
                     if (var6 == null) {
                        var6 = getDefaultTZName(var1, var3);
                     }

                     var20 = null;
                     boolean var21 = false;
                     boolean var22 = false;
                     boolean var23 = false;
                     boolean var24 = false;
                     long var25 = 0L;

                     try {
                        var35 = offsetStrToMillis(var4);
                        int var39 = offsetStrToMillis(var5);
                        int var42;
                        if (var3) {
                           if (var39 - var35 > 0) {
                              var42 = var35;
                              var43 = var39 - var35;
                           } else {
                              var42 = var39 - 3600000;
                              var43 = 3600000;
                           }
                        } else {
                           var42 = var39;
                           var43 = 0;
                        }

                        var25 = parseDateTimeString(var7, var35);
                        Date var27 = null;
                        if (var8) {
                           var20 = createRuleByRRULE(var6, var42, var43, var25, var9, var35);
                        } else {
                           var20 = createRuleByRDATE(var6, var42, var43, var25, var9, var35);
                        }

                        if (var20 != null) {
                           var27 = var20.getFirstStart(var35, 0);
                           if (var27.getTime() < var13) {
                              var13 = var27.getTime();
                              if (var43 > 0) {
                                 var11 = var35;
                                 var12 = 0;
                              } else if (var35 - var39 == 3600000) {
                                 var11 = var35 - 3600000;
                                 var12 = 3600000;
                              } else {
                                 var11 = var35;
                                 var12 = 0;
                              }
                           }
                        }
                     } catch (IllegalArgumentException var28) {
                     }

                     if (var20 == null) {
                        var2 = 3;
                     } else {
                        var10.add(var20);
                        var2 = 1;
                     }
                  } else {
                     var2 = 3;
                  }
               }
            }
         } while(var2 != 3);

         this.vtzlines = null;
         return false;
      } else {
         return false;
      }
   }

   private static String getDefaultTZName(String var0, boolean var1) {
      return var1 ? var0 + "(DST)" : var0 + "(STD)";
   }

   private static TimeZoneRule createRuleByRRULE(String var0, int var1, int var2, long var3, List var5, int var6) {
      if (var5 != null && var5.size() != 0) {
         String var7 = (String)var5.get(0);
         long[] var8 = new long[1];
         int[] var9 = parseRRULE(var7, var8);
         if (var9 == null) {
            return null;
         } else {
            int var10 = var9[0];
            int var11 = var9[1];
            int var12 = var9[2];
            int var13 = var9[3];
            int var14;
            int var15;
            int var16;
            int var17;
            int var18;
            if (var5.size() == 1) {
               if (var9.length > 4) {
                  if (var9.length != 10 || var10 == -1 || var11 == 0) {
                     return null;
                  }

                  var14 = 31;
                  int[] var25 = new int[7];

                  for(var16 = 0; var16 < 7; ++var16) {
                     var25[var16] = var9[3 + var16];
                     var25[var16] = var25[var16] > 0 ? var25[var16] : MONTHLENGTH[var10] + var25[var16] + 1;
                     var14 = var25[var16] < var14 ? var25[var16] : var14;
                  }

                  for(var16 = 1; var16 < 7; ++var16) {
                     boolean var26 = false;

                     for(var18 = 0; var18 < 7; ++var18) {
                        if (var25[var18] == var14 + var16) {
                           var26 = true;
                           break;
                        }
                     }

                     if (!var26) {
                        return null;
                     }
                  }

                  var13 = var14;
               }
            } else {
               label250: {
                  if (var10 != -1 && var11 != 0 && var13 != 0) {
                     if (var5.size() > 7) {
                        return null;
                     }

                     var14 = var10;
                     var15 = var9.length - 3;
                     var16 = 31;

                     for(var17 = 0; var17 < var15; ++var17) {
                        var18 = var9[3 + var17];
                        var18 = var18 > 0 ? var18 : MONTHLENGTH[var10] + var18 + 1;
                        var16 = var18 < var16 ? var18 : var16;
                     }

                     var17 = -1;
                     var18 = 1;

                     while(true) {
                        if (var18 < var5.size()) {
                           var7 = (String)var5.get(var18);
                           long[] var19 = new long[1];
                           int[] var20 = parseRRULE(var7, var19);
                           if (var19[0] > var8[0]) {
                              var8 = var19;
                           }

                           if (var20[0] != -1 && var20[1] != 0 && var20[3] != 0) {
                              int var21 = var20.length - 3;
                              if (var15 + var21 > 7) {
                                 return null;
                              }

                              if (var20[1] != var11) {
                                 return null;
                              }

                              int var22;
                              if (var20[0] != var10) {
                                 if (var17 == -1) {
                                    var22 = var20[0] - var10;
                                    if (var22 != -11 && var22 != -1) {
                                       if (var22 != 11 && var22 != 1) {
                                          return null;
                                       }

                                       var17 = var20[0];
                                    } else {
                                       var17 = var20[0];
                                       var14 = var17;
                                       var16 = 31;
                                    }
                                 } else if (var20[0] != var10 && var20[0] != var17) {
                                    return null;
                                 }
                              }

                              if (var20[0] == var14) {
                                 for(var22 = 0; var22 < var21; ++var22) {
                                    int var23 = var20[3 + var22];
                                    var23 = var23 > 0 ? var23 : MONTHLENGTH[var20[0]] + var23 + 1;
                                    var16 = var23 < var16 ? var23 : var16;
                                 }
                              }

                              var15 += var21;
                              ++var18;
                              continue;
                           }

                           return null;
                        }

                        if (var15 != 7) {
                           return null;
                        }

                        var10 = var14;
                        var13 = var16;
                        break label250;
                     }
                  }

                  return null;
               }
            }

            int[] var24 = Grego.timeToFields(var3 + (long)var6, (int[])null);
            var15 = var24[0];
            if (var10 == -1) {
               var10 = var24[1];
            }

            if (var11 == 0 && var12 == 0 && var13 == 0) {
               var13 = var24[2];
            }

            var16 = var24[5];
            var17 = Integer.MAX_VALUE;
            if (var8[0] != Long.MIN_VALUE) {
               Grego.timeToFields(var8[0], var24);
               var17 = var24[0];
            }

            DateTimeRule var27 = null;
            if (var11 == 0 && var12 == 0 && var13 != 0) {
               var27 = new DateTimeRule(var10, var13, var16, 0);
            } else if (var11 != 0 && var12 != 0 && var13 == 0) {
               var27 = new DateTimeRule(var10, var12, var11, var16, 0);
            } else {
               if (var11 == 0 || var12 != 0 || var13 == 0) {
                  return null;
               }

               var27 = new DateTimeRule(var10, var13, var11, true, var16, 0);
            }

            return new AnnualTimeZoneRule(var0, var1, var2, var27, var15, var17);
         }
      } else {
         return null;
      }
   }

   private static int[] parseRRULE(String var0, long[] var1) {
      int var2 = -1;
      int var3 = 0;
      int var4 = 0;
      int[] var5 = null;
      long var6 = Long.MIN_VALUE;
      boolean var8 = false;
      boolean var9 = false;
      java.util.StringTokenizer var10 = new java.util.StringTokenizer(var0, ";");

      while(var10.hasMoreTokens()) {
         String var13 = var10.nextToken();
         int var14 = var13.indexOf("=");
         if (var14 == -1) {
            var9 = true;
            break;
         }

         String var11 = var13.substring(0, var14);
         String var12 = var13.substring(var14 + 1);
         if (var11.equals("FREQ")) {
            if (!var12.equals("YEARLY")) {
               var9 = true;
               break;
            }

            var8 = true;
         } else if (var11.equals("UNTIL")) {
            try {
               var6 = parseDateTimeString(var12, 0);
            } catch (IllegalArgumentException var19) {
               var9 = true;
               break;
            }
         } else if (var11.equals("BYMONTH")) {
            if (var12.length() > 2) {
               var9 = true;
               break;
            }

            try {
               var2 = Integer.parseInt(var12) - 1;
               if (var2 < 0 || var2 >= 12) {
                  var9 = true;
                  break;
               }
            } catch (NumberFormatException var20) {
               var9 = true;
               break;
            }
         } else {
            int var17;
            int var26;
            if (!var11.equals("BYDAY")) {
               if (var11.equals("BYMONTHDAY")) {
                  java.util.StringTokenizer var25 = new java.util.StringTokenizer(var12, ",");
                  var26 = var25.countTokens();
                  var5 = new int[var26];
                  var17 = 0;

                  while(var25.hasMoreTokens()) {
                     try {
                        var5[var17++] = Integer.parseInt(var25.nextToken());
                     } catch (NumberFormatException var21) {
                        var9 = true;
                        break;
                     }
                  }
               }
            } else {
               int var15 = var12.length();
               if (var15 >= 2 && var15 <= 4) {
                  if (var15 > 2) {
                     byte var16 = 1;
                     if (var12.charAt(0) == '+') {
                        var16 = 1;
                     } else if (var12.charAt(0) == '-') {
                        var16 = -1;
                     } else if (var15 == 4) {
                        var9 = true;
                        break;
                     }

                     try {
                        var17 = Integer.parseInt(var12.substring(var15 - 3, var15 - 2));
                        if (var17 == 0 || var17 > 4) {
                           var9 = true;
                           break;
                        }

                        var4 = var17 * var16;
                     } catch (NumberFormatException var22) {
                        var9 = true;
                        break;
                     }

                     var12 = var12.substring(var15 - 2);
                  }

                  for(var26 = 0; var26 < ICAL_DOW_NAMES.length && !var12.equals(ICAL_DOW_NAMES[var26]); ++var26) {
                  }

                  if (var26 < ICAL_DOW_NAMES.length) {
                     var3 = var26 + 1;
                     continue;
                  }

                  var9 = true;
                  break;
               }

               var9 = true;
               break;
            }
         }
      }

      if (var9) {
         return null;
      } else if (!var8) {
         return null;
      } else {
         var1[0] = var6;
         int[] var23;
         if (var5 == null) {
            var23 = new int[4];
            var23[3] = 0;
         } else {
            var23 = new int[3 + var5.length];

            for(int var24 = 0; var24 < var5.length; ++var24) {
               var23[3 + var24] = var5[var24];
            }
         }

         var23[0] = var2;
         var23[1] = var3;
         var23[2] = var4;
         return var23;
      }
   }

   private static TimeZoneRule createRuleByRDATE(String var0, int var1, int var2, long var3, List var5, int var6) {
      long[] var7;
      if (var5 != null && var5.size() != 0) {
         var7 = new long[var5.size()];
         int var8 = 0;

         String var10;
         try {
            for(Iterator var9 = var5.iterator(); var9.hasNext(); var7[var8++] = parseDateTimeString(var10, var6)) {
               var10 = (String)var9.next();
            }
         } catch (IllegalArgumentException var11) {
            return null;
         }
      } else {
         var7 = new long[]{var3};
      }

      return new TimeArrayTimeZoneRule(var0, var1, var2, var7, 2);
   }

   private void writeZone(Writer var1, BasicTimeZone var2, String[] var3) throws IOException {
      this.writeHeader(var1);
      if (var3 != null && var3.length > 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4] != null) {
               var1.write(var3[var4]);
               var1.write("\r\n");
            }
         }
      }

      long var47 = Long.MIN_VALUE;
      String var6 = null;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      int var11 = 0;
      int var12 = 0;
      int var13 = 0;
      int var14 = 0;
      long var15 = 0L;
      long var17 = 0L;
      int var19 = 0;
      AnnualTimeZoneRule var20 = null;
      String var21 = null;
      int var22 = 0;
      int var23 = 0;
      int var24 = 0;
      int var25 = 0;
      int var26 = 0;
      int var27 = 0;
      int var28 = 0;
      int var29 = 0;
      long var30 = 0L;
      long var32 = 0L;
      int var34 = 0;
      AnnualTimeZoneRule var35 = null;
      int[] var36 = new int[6];
      boolean var37 = false;

      while(true) {
         TimeZoneTransition var38 = var2.getNextTransition(var47, false);
         if (var38 == null) {
            break;
         }

         var37 = true;
         var47 = var38.getTime();
         String var39 = var38.getTo().getName();
         boolean var40 = var38.getTo().getDSTSavings() != 0;
         int var41 = var38.getFrom().getRawOffset() + var38.getFrom().getDSTSavings();
         int var42 = var38.getFrom().getDSTSavings();
         int var43 = var38.getTo().getRawOffset() + var38.getTo().getDSTSavings();
         Grego.timeToFields(var38.getTime() + (long)var41, var36);
         int var44 = Grego.getDayOfWeekInMonth(var36[0], var36[1], var36[2]);
         int var45 = var36[0];
         boolean var46 = false;
         if (var40) {
            if (var20 == null && var38.getTo() instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)var38.getTo()).getEndYear() == Integer.MAX_VALUE) {
               var20 = (AnnualTimeZoneRule)var38.getTo();
            }

            if (var19 > 0) {
               if (var45 == var10 + var19 && var39.equals(var6) && var7 == var41 && var9 == var43 && var11 == var36[1] && var12 == var36[3] && var13 == var44 && var14 == var36[5]) {
                  var17 = var47;
                  ++var19;
                  var46 = true;
               }

               if (!var46) {
                  if (var19 == 1) {
                     writeZonePropsByTime(var1, true, var6, var7, var9, var15, true);
                  } else {
                     writeZonePropsByDOW(var1, true, var6, var7, var9, var11, var13, var12, var15, var17);
                  }
               }
            }

            if (!var46) {
               var6 = var39;
               var7 = var41;
               var8 = var42;
               var9 = var43;
               var10 = var45;
               var11 = var36[1];
               var12 = var36[3];
               var13 = var44;
               var14 = var36[5];
               var17 = var47;
               var15 = var47;
               var19 = 1;
            }

            if (var35 != null && var20 != null) {
               break;
            }
         } else {
            if (var35 == null && var38.getTo() instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)var38.getTo()).getEndYear() == Integer.MAX_VALUE) {
               var35 = (AnnualTimeZoneRule)var38.getTo();
            }

            if (var34 > 0) {
               if (var45 == var25 + var34 && var39.equals(var21) && var22 == var41 && var24 == var43 && var26 == var36[1] && var27 == var36[3] && var28 == var44 && var29 == var36[5]) {
                  var32 = var47;
                  ++var34;
                  var46 = true;
               }

               if (!var46) {
                  if (var34 == 1) {
                     writeZonePropsByTime(var1, false, var21, var22, var24, var30, true);
                  } else {
                     writeZonePropsByDOW(var1, false, var21, var22, var24, var26, var28, var27, var30, var32);
                  }
               }
            }

            if (!var46) {
               var21 = var39;
               var22 = var41;
               var23 = var42;
               var24 = var43;
               var25 = var45;
               var26 = var36[1];
               var27 = var36[3];
               var28 = var44;
               var29 = var36[5];
               var32 = var47;
               var30 = var47;
               var34 = 1;
            }

            if (var35 != null && var20 != null) {
               break;
            }
         }
      }

      if (!var37) {
         int var48 = var2.getOffset(0L);
         boolean var49 = var48 != var2.getRawOffset();
         writeZonePropsByTime(var1, var49, getDefaultTZName(var2.getID(), var49), var48, var48, 0L - (long)var48, false);
      } else {
         if (var19 > 0) {
            if (var20 == null) {
               if (var19 == 1) {
                  writeZonePropsByTime(var1, true, var6, var7, var9, var15, true);
               } else {
                  writeZonePropsByDOW(var1, true, var6, var7, var9, var11, var13, var12, var15, var17);
               }
            } else if (var19 == 1) {
               writeFinalRule(var1, true, var20, var7 - var8, var8, var15);
            } else if (var12 == var20.getRule()) {
               writeZonePropsByDOW(var1, true, var6, var7, var9, var11, var13, var12, var15, Long.MAX_VALUE);
            } else {
               writeZonePropsByDOW(var1, true, var6, var7, var9, var11, var13, var12, var15, var17);
               writeFinalRule(var1, true, var20, var7 - var8, var8, var15);
            }
         }

         if (var34 > 0) {
            if (var35 == null) {
               if (var34 == 1) {
                  writeZonePropsByTime(var1, false, var21, var22, var24, var30, true);
               } else {
                  writeZonePropsByDOW(var1, false, var21, var22, var24, var26, var28, var27, var30, var32);
               }
            } else if (var34 == 1) {
               writeFinalRule(var1, false, var35, var22 - var23, var23, var30);
            } else if (var27 == var35.getRule()) {
               writeZonePropsByDOW(var1, false, var21, var22, var24, var26, var28, var27, var30, Long.MAX_VALUE);
            } else {
               writeZonePropsByDOW(var1, false, var21, var22, var24, var26, var28, var27, var30, var32);
               writeFinalRule(var1, false, var35, var22 - var23, var23, var30);
            }
         }
      }

      writeFooter(var1);
   }

   private static void writeZonePropsByTime(Writer var0, boolean var1, String var2, int var3, int var4, long var5, boolean var7) throws IOException {
      beginZoneProps(var0, var1, var2, var3, var4, var5);
      if (var7) {
         var0.write("RDATE");
         var0.write(":");
         var0.write(getDateTimeString(var5 + (long)var3));
         var0.write("\r\n");
      }

      endZoneProps(var0, var1);
   }

   private static void writeZonePropsByDOM(Writer var0, boolean var1, String var2, int var3, int var4, int var5, int var6, long var7, long var9) throws IOException {
      beginZoneProps(var0, var1, var2, var3, var4, var7);
      beginRRULE(var0, var5);
      var0.write("BYMONTHDAY");
      var0.write("=");
      var0.write(Integer.toString(var6));
      if (var9 != Long.MAX_VALUE) {
         appendUNTIL(var0, getDateTimeString(var9 + (long)var3));
      }

      var0.write("\r\n");
      endZoneProps(var0, var1);
   }

   private static void writeZonePropsByDOW(Writer var0, boolean var1, String var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10) throws IOException {
      beginZoneProps(var0, var1, var2, var3, var4, var8);
      beginRRULE(var0, var5);
      var0.write("BYDAY");
      var0.write("=");
      var0.write(Integer.toString(var6));
      var0.write(ICAL_DOW_NAMES[var7 - 1]);
      if (var10 != Long.MAX_VALUE) {
         appendUNTIL(var0, getDateTimeString(var10 + (long)var3));
      }

      var0.write("\r\n");
      endZoneProps(var0, var1);
   }

   private static void writeZonePropsByDOW_GEQ_DOM(Writer var0, boolean var1, String var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10) throws IOException {
      if (var6 % 7 == 1) {
         writeZonePropsByDOW(var0, var1, var2, var3, var4, var5, (var6 + 6) / 7, var7, var8, var10);
      } else if (var5 != 1 && (MONTHLENGTH[var5] - var6) % 7 == 6) {
         writeZonePropsByDOW(var0, var1, var2, var3, var4, var5, -1 * ((MONTHLENGTH[var5] - var6 + 1) / 7), var7, var8, var10);
      } else {
         beginZoneProps(var0, var1, var2, var3, var4, var8);
         int var12 = var6;
         int var13 = 7;
         int var14;
         int var15;
         if (var6 <= 0) {
            var14 = 1 - var6;
            var13 -= var14;
            var15 = var5 - 1 < 0 ? 11 : var5 - 1;
            writeZonePropsByDOW_GEQ_DOM_sub(var0, var15, -var14, var7, var14, Long.MAX_VALUE, var3);
            var12 = 1;
         } else if (var6 + 6 > MONTHLENGTH[var5]) {
            var14 = var6 + 6 - MONTHLENGTH[var5];
            var13 -= var14;
            var15 = var5 + 1 > 11 ? 0 : var5 + 1;
            writeZonePropsByDOW_GEQ_DOM_sub(var0, var15, 1, var7, var14, Long.MAX_VALUE, var3);
         }

         writeZonePropsByDOW_GEQ_DOM_sub(var0, var5, var12, var7, var13, var10, var3);
         endZoneProps(var0, var1);
      }

   }

   private static void writeZonePropsByDOW_GEQ_DOM_sub(Writer var0, int var1, int var2, int var3, int var4, long var5, int var7) throws IOException {
      int var8 = var2;
      boolean var9 = var1 == 1;
      if (var2 < 0 && !var9) {
         var8 = MONTHLENGTH[var1] + var2 + 1;
      }

      beginRRULE(var0, var1);
      var0.write("BYDAY");
      var0.write("=");
      var0.write(ICAL_DOW_NAMES[var3 - 1]);
      var0.write(";");
      var0.write("BYMONTHDAY");
      var0.write("=");
      var0.write(Integer.toString(var8));

      for(int var10 = 1; var10 < var4; ++var10) {
         var0.write(",");
         var0.write(Integer.toString(var8 + var10));
      }

      if (var5 != Long.MAX_VALUE) {
         appendUNTIL(var0, getDateTimeString(var5 + (long)var7));
      }

      var0.write("\r\n");
   }

   private static void writeZonePropsByDOW_LEQ_DOM(Writer var0, boolean var1, String var2, int var3, int var4, int var5, int var6, int var7, long var8, long var10) throws IOException {
      if (var6 % 7 == 0) {
         writeZonePropsByDOW(var0, var1, var2, var3, var4, var5, var6 / 7, var7, var8, var10);
      } else if (var5 != 1 && (MONTHLENGTH[var5] - var6) % 7 == 0) {
         writeZonePropsByDOW(var0, var1, var2, var3, var4, var5, -1 * ((MONTHLENGTH[var5] - var6) / 7 + 1), var7, var8, var10);
      } else if (var5 == 1 && var6 == 29) {
         writeZonePropsByDOW(var0, var1, var2, var3, var4, 1, -1, var7, var8, var10);
      } else {
         writeZonePropsByDOW_GEQ_DOM(var0, var1, var2, var3, var4, var5, var6 - 6, var7, var8, var10);
      }

   }

   private static void writeFinalRule(Writer var0, boolean var1, AnnualTimeZoneRule var2, int var3, int var4, long var5) throws IOException {
      DateTimeRule var7 = toWallTimeRule(var2.getRule(), var3, var4);
      int var8 = var7.getRuleMillisInDay();
      if (var8 < 0) {
         var5 += (long)(0 - var8);
      } else if (var8 >= 86400000) {
         var5 -= (long)(var8 - 86399999);
      }

      int var9 = var2.getRawOffset() + var2.getDSTSavings();
      switch(var7.getDateRuleType()) {
      case 0:
         writeZonePropsByDOM(var0, var1, var2.getName(), var3 + var4, var9, var7.getRuleMonth(), var7.getRuleDayOfMonth(), var5, Long.MAX_VALUE);
         break;
      case 1:
         writeZonePropsByDOW(var0, var1, var2.getName(), var3 + var4, var9, var7.getRuleMonth(), var7.getRuleWeekInMonth(), var7.getRuleDayOfWeek(), var5, Long.MAX_VALUE);
         break;
      case 2:
         writeZonePropsByDOW_GEQ_DOM(var0, var1, var2.getName(), var3 + var4, var9, var7.getRuleMonth(), var7.getRuleDayOfMonth(), var7.getRuleDayOfWeek(), var5, Long.MAX_VALUE);
         break;
      case 3:
         writeZonePropsByDOW_LEQ_DOM(var0, var1, var2.getName(), var3 + var4, var9, var7.getRuleMonth(), var7.getRuleDayOfMonth(), var7.getRuleDayOfWeek(), var5, Long.MAX_VALUE);
      }

   }

   private static DateTimeRule toWallTimeRule(DateTimeRule var0, int var1, int var2) {
      if (var0.getTimeRuleType() == 0) {
         return var0;
      } else {
         int var3 = var0.getRuleMillisInDay();
         if (var0.getTimeRuleType() == 2) {
            var3 += var1 + var2;
         } else if (var0.getTimeRuleType() == 1) {
            var3 += var2;
         }

         boolean var4 = true;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = true;
         byte var8 = 0;
         if (var3 < 0) {
            var8 = -1;
            var3 += 86400000;
         } else if (var3 >= 86400000) {
            var8 = 1;
            var3 -= 86400000;
         }

         int var10 = var0.getRuleMonth();
         int var11 = var0.getRuleDayOfMonth();
         int var12 = var0.getRuleDayOfWeek();
         int var13 = var0.getDateRuleType();
         if (var8 != 0) {
            if (var13 == 1) {
               int var9 = var0.getRuleWeekInMonth();
               if (var9 > 0) {
                  var13 = 2;
                  var11 = 7 * (var9 - 1) + 1;
               } else {
                  var13 = 3;
                  var11 = MONTHLENGTH[var10] + 7 * (var9 + 1);
               }
            }

            var11 += var8;
            if (var11 == 0) {
               --var10;
               var10 = var10 < 0 ? 11 : var10;
               var11 = MONTHLENGTH[var10];
            } else if (var11 > MONTHLENGTH[var10]) {
               ++var10;
               var10 = var10 > 11 ? 0 : var10;
               var11 = 1;
            }

            if (var13 != 0) {
               var12 += var8;
               if (var12 < 1) {
                  var12 = 7;
               } else if (var12 > 7) {
                  var12 = 1;
               }
            }
         }

         DateTimeRule var14;
         if (var13 == 0) {
            var14 = new DateTimeRule(var10, var11, var3, 0);
         } else {
            var14 = new DateTimeRule(var10, var11, var12, var13 == 2, var3, 0);
         }

         return var14;
      }
   }

   private static void beginZoneProps(Writer var0, boolean var1, String var2, int var3, int var4, long var5) throws IOException {
      var0.write("BEGIN");
      var0.write(":");
      if (var1) {
         var0.write("DAYLIGHT");
      } else {
         var0.write("STANDARD");
      }

      var0.write("\r\n");
      var0.write("TZOFFSETTO");
      var0.write(":");
      var0.write(millisToOffset(var4));
      var0.write("\r\n");
      var0.write("TZOFFSETFROM");
      var0.write(":");
      var0.write(millisToOffset(var3));
      var0.write("\r\n");
      var0.write("TZNAME");
      var0.write(":");
      var0.write(var2);
      var0.write("\r\n");
      var0.write("DTSTART");
      var0.write(":");
      var0.write(getDateTimeString(var5 + (long)var3));
      var0.write("\r\n");
   }

   private static void endZoneProps(Writer var0, boolean var1) throws IOException {
      var0.write("END");
      var0.write(":");
      if (var1) {
         var0.write("DAYLIGHT");
      } else {
         var0.write("STANDARD");
      }

      var0.write("\r\n");
   }

   private static void beginRRULE(Writer var0, int var1) throws IOException {
      var0.write("RRULE");
      var0.write(":");
      var0.write("FREQ");
      var0.write("=");
      var0.write("YEARLY");
      var0.write(";");
      var0.write("BYMONTH");
      var0.write("=");
      var0.write(Integer.toString(var1 + 1));
      var0.write(";");
   }

   private static void appendUNTIL(Writer var0, String var1) throws IOException {
      if (var1 != null) {
         var0.write(";");
         var0.write("UNTIL");
         var0.write("=");
         var0.write(var1);
      }

   }

   private void writeHeader(Writer var1) throws IOException {
      var1.write("BEGIN");
      var1.write(":");
      var1.write("VTIMEZONE");
      var1.write("\r\n");
      var1.write("TZID");
      var1.write(":");
      var1.write(this.tz.getID());
      var1.write("\r\n");
      if (this.tzurl != null) {
         var1.write("TZURL");
         var1.write(":");
         var1.write(this.tzurl);
         var1.write("\r\n");
      }

      if (this.lastmod != null) {
         var1.write("LAST-MODIFIED");
         var1.write(":");
         var1.write(getUTCDateTimeString(this.lastmod.getTime()));
         var1.write("\r\n");
      }

   }

   private static void writeFooter(Writer var0) throws IOException {
      var0.write("END");
      var0.write(":");
      var0.write("VTIMEZONE");
      var0.write("\r\n");
   }

   private static String getDateTimeString(long var0) {
      int[] var2 = Grego.timeToFields(var0, (int[])null);
      StringBuilder var3 = new StringBuilder(15);
      var3.append(numToString(var2[0], 4));
      var3.append(numToString(var2[1] + 1, 2));
      var3.append(numToString(var2[2], 2));
      var3.append('T');
      int var4 = var2[5];
      int var5 = var4 / 3600000;
      var4 %= 3600000;
      int var6 = var4 / '\uea60';
      var4 %= 60000;
      int var7 = var4 / 1000;
      var3.append(numToString(var5, 2));
      var3.append(numToString(var6, 2));
      var3.append(numToString(var7, 2));
      return var3.toString();
   }

   private static String getUTCDateTimeString(long var0) {
      return getDateTimeString(var0) + "Z";
   }

   private static long parseDateTimeString(String var0, int var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      boolean var8 = false;
      boolean var9 = false;
      if (var0 != null) {
         int var10 = var0.length();
         if ((var10 == 15 || var10 == 16) && var0.charAt(8) == 'T') {
            label69: {
               if (var10 == 16) {
                  if (var0.charAt(15) != 'Z') {
                     break label69;
                  }

                  var8 = true;
               }

               try {
                  var2 = Integer.parseInt(var0.substring(0, 4));
                  var3 = Integer.parseInt(var0.substring(4, 6)) - 1;
                  var4 = Integer.parseInt(var0.substring(6, 8));
                  var5 = Integer.parseInt(var0.substring(9, 11));
                  var6 = Integer.parseInt(var0.substring(11, 13));
                  var7 = Integer.parseInt(var0.substring(13, 15));
               } catch (NumberFormatException var12) {
                  break label69;
               }

               int var11 = Grego.monthLength(var2, var3);
               if (var2 >= 0 && var3 >= 0 && var3 <= 11 && var4 >= 1 && var4 <= var11 && var5 >= 0 && var5 < 24 && var6 >= 0 && var6 < 60 && var7 >= 0 && var7 < 60) {
                  var9 = true;
               }
            }
         }
      }

      if (!var9) {
         throw new IllegalArgumentException("Invalid date time string format");
      } else {
         long var13 = Grego.fieldsToDay(var2, var3, var4) * 86400000L;
         var13 += (long)(var5 * 3600000 + var6 * '\uea60' + var7 * 1000);
         if (!var8) {
            var13 -= (long)var1;
         }

         return var13;
      }
   }

   private static int offsetStrToMillis(String var0) {
      boolean var1 = false;
      byte var2 = 0;
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6;
      if (var0 != null) {
         var6 = var0.length();
         if (var6 == 5 || var6 == 7) {
            label44: {
               char var7 = var0.charAt(0);
               if (var7 == '+') {
                  var2 = 1;
               } else {
                  if (var7 != '-') {
                     break label44;
                  }

                  var2 = -1;
               }

               try {
                  var3 = Integer.parseInt(var0.substring(1, 3));
                  var4 = Integer.parseInt(var0.substring(3, 5));
                  if (var6 == 7) {
                     var5 = Integer.parseInt(var0.substring(5, 7));
                  }
               } catch (NumberFormatException var9) {
                  break label44;
               }

               var1 = true;
            }
         }
      }

      if (!var1) {
         throw new IllegalArgumentException("Bad offset string");
      } else {
         var6 = var2 * ((var3 * 60 + var4) * 60 + var5) * 1000;
         return var6;
      }
   }

   private static String millisToOffset(int var0) {
      StringBuilder var1 = new StringBuilder(7);
      if (var0 >= 0) {
         var1.append('+');
      } else {
         var1.append('-');
         var0 = -var0;
      }

      int var5 = var0 / 1000;
      int var4 = var5 % 60;
      var5 = (var5 - var4) / 60;
      int var3 = var5 % 60;
      int var2 = var5 / 60;
      var1.append(numToString(var2, 2));
      var1.append(numToString(var3, 2));
      var1.append(numToString(var4, 2));
      return var1.toString();
   }

   private static String numToString(int var0, int var1) {
      String var2 = Integer.toString(var0);
      int var3 = var2.length();
      if (var3 >= var1) {
         return var2.substring(var3 - var1, var3);
      } else {
         StringBuilder var4 = new StringBuilder(var1);

         for(int var5 = var3; var5 < var1; ++var5) {
            var4.append('0');
         }

         var4.append(var2);
         return var4.toString();
      }
   }

   public boolean isFrozen() {
      return this.isFrozen;
   }

   public TimeZone freeze() {
      this.isFrozen = true;
      return this;
   }

   public TimeZone cloneAsThawed() {
      VTimeZone var1 = (VTimeZone)super.cloneAsThawed();
      var1.tz = (BasicTimeZone)this.tz.cloneAsThawed();
      var1.isFrozen = false;
      return var1;
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   static {
      try {
         ICU_TZVERSION = TimeZone.getTZDataVersion();
      } catch (MissingResourceException var1) {
         ICU_TZVERSION = null;
      }

   }
}
