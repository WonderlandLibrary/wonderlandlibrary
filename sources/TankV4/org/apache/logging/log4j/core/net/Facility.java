package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.util.EnglishEnums;

public enum Facility {
   KERN(0),
   USER(1),
   MAIL(2),
   DAEMON(3),
   AUTH(4),
   SYSLOG(5),
   LPR(6),
   NEWS(7),
   UUCP(8),
   CRON(9),
   AUTHPRIV(10),
   FTP(11),
   NTP(12),
   LOG_AUDIT(13),
   LOG_ALERT(14),
   CLOCK(15),
   LOCAL0(16),
   LOCAL1(17),
   LOCAL2(18),
   LOCAL3(19),
   LOCAL4(20),
   LOCAL5(21),
   LOCAL6(22),
   LOCAL7(23);

   private final int code;
   private static final Facility[] $VALUES = new Facility[]{KERN, USER, MAIL, DAEMON, AUTH, SYSLOG, LPR, NEWS, UUCP, CRON, AUTHPRIV, FTP, NTP, LOG_AUDIT, LOG_ALERT, CLOCK, LOCAL0, LOCAL1, LOCAL2, LOCAL3, LOCAL4, LOCAL5, LOCAL6, LOCAL7};

   private Facility(int var3) {
      this.code = var3;
   }

   public static Facility toFacility(String var0) {
      return toFacility(var0, (Facility)null);
   }

   public static Facility toFacility(String var0, Facility var1) {
      return (Facility)EnglishEnums.valueOf(Facility.class, var0, var1);
   }

   public int getCode() {
      return this.code;
   }

   public boolean isEqual(String var1) {
      return this.name().equalsIgnoreCase(var1);
   }
}
