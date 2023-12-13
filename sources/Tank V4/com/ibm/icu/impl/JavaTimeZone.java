package com.ibm.icu.impl;

import com.ibm.icu.util.TimeZone;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeSet;

public class JavaTimeZone extends TimeZone {
   private static final long serialVersionUID = 6977448185543929364L;
   private static final TreeSet AVAILABLESET = new TreeSet();
   private java.util.TimeZone javatz;
   private transient Calendar javacal;
   private static Method mObservesDaylightTime;
   private transient boolean isFrozen;

   public JavaTimeZone() {
      this(java.util.TimeZone.getDefault(), (String)null);
   }

   public JavaTimeZone(java.util.TimeZone var1, String var2) {
      this.isFrozen = false;
      if (var2 == null) {
         var2 = var1.getID();
      }

      this.javatz = var1;
      this.setID(var2);
      this.javacal = new GregorianCalendar(this.javatz);
   }

   public static JavaTimeZone createTimeZone(String var0) {
      java.util.TimeZone var1 = null;
      if (AVAILABLESET.contains(var0)) {
         var1 = java.util.TimeZone.getTimeZone(var0);
      }

      if (var1 == null) {
         boolean[] var2 = new boolean[1];
         String var3 = TimeZone.getCanonicalID(var0, var2);
         if (var2[0] && AVAILABLESET.contains(var3)) {
            var1 = java.util.TimeZone.getTimeZone(var3);
         }
      }

      return var1 == null ? null : new JavaTimeZone(var1, var0);
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.javatz.getOffset(var1, var2, var3, var4, var5, var6);
   }

   public void getOffset(long var1, boolean var3, int[] var4) {
      Calendar var5;
      synchronized(var5 = this.javacal){}
      if (var3) {
         int[] var6 = new int[6];
         Grego.timeToFields(var1, var6);
         int var11 = var6[5];
         int var10 = var11 % 1000;
         var11 /= 1000;
         int var9 = var11 % 60;
         var11 /= 60;
         int var8 = var11 % 60;
         int var7 = var11 / 60;
         this.javacal.clear();
         this.javacal.set(var6[0], var6[1], var6[2], var7, var8, var9);
         this.javacal.set(14, var10);
         int var12 = this.javacal.get(6);
         int var13 = this.javacal.get(11);
         int var14 = this.javacal.get(12);
         int var15 = this.javacal.get(13);
         int var16 = this.javacal.get(14);
         if (var6[4] != var12 || var7 != var13 || var8 != var14 || var9 != var15 || var10 != var16) {
            int var17 = Math.abs(var12 - var6[4]) > 1 ? 1 : var12 - var6[4];
            int var18 = (((var17 * 24 + var13 - var7) * 60 + var14 - var8) * 60 + var15 - var9) * 1000 + var16 - var10;
            this.javacal.setTimeInMillis(this.javacal.getTimeInMillis() - (long)var18 - 1L);
         }
      } else {
         this.javacal.setTimeInMillis(var1);
      }

      var4[0] = this.javacal.get(15);
      var4[1] = this.javacal.get(16);
   }

   public int getRawOffset() {
      return this.javatz.getRawOffset();
   }

   public boolean inDaylightTime(Date var1) {
      return this.javatz.inDaylightTime(var1);
   }

   public void setRawOffset(int var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen JavaTimeZone instance.");
      } else {
         this.javatz.setRawOffset(var1);
      }
   }

   public boolean useDaylightTime() {
      return this.javatz.useDaylightTime();
   }

   public boolean observesDaylightTime() {
      if (mObservesDaylightTime != null) {
         try {
            return (Boolean)mObservesDaylightTime.invoke(this.javatz, (Object[])null);
         } catch (IllegalAccessException var2) {
         } catch (IllegalArgumentException var3) {
         } catch (InvocationTargetException var4) {
         }
      }

      return super.observesDaylightTime();
   }

   public int getDSTSavings() {
      return this.javatz.getDSTSavings();
   }

   public java.util.TimeZone unwrap() {
      return this.javatz;
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   public int hashCode() {
      return super.hashCode() + this.javatz.hashCode();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.javacal = new GregorianCalendar(this.javatz);
   }

   public boolean isFrozen() {
      return this.isFrozen;
   }

   public TimeZone freeze() {
      this.isFrozen = true;
      return this;
   }

   public TimeZone cloneAsThawed() {
      JavaTimeZone var1 = (JavaTimeZone)super.cloneAsThawed();
      var1.javatz = (java.util.TimeZone)this.javatz.clone();
      var1.javacal = (GregorianCalendar)this.javacal.clone();
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
      String[] var0 = java.util.TimeZone.getAvailableIDs();

      for(int var1 = 0; var1 < var0.length; ++var1) {
         AVAILABLESET.add(var0[var1]);
      }

      try {
         mObservesDaylightTime = java.util.TimeZone.class.getMethod("observesDaylightTime", (Class[])null);
      } catch (NoSuchMethodException var2) {
      } catch (SecurityException var3) {
      }

   }
}
