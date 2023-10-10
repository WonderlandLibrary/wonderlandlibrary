package org.apache.commons.lang3.builder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;

public abstract class ToStringStyle implements Serializable {
   private static final long serialVersionUID = -2587890625525655916L;
   public static final ToStringStyle DEFAULT_STYLE = new ToStringStyle.DefaultToStringStyle();
   public static final ToStringStyle MULTI_LINE_STYLE = new ToStringStyle.MultiLineToStringStyle();
   public static final ToStringStyle NO_FIELD_NAMES_STYLE = new ToStringStyle.NoFieldNameToStringStyle();
   public static final ToStringStyle SHORT_PREFIX_STYLE = new ToStringStyle.ShortPrefixToStringStyle();
   public static final ToStringStyle SIMPLE_STYLE = new ToStringStyle.SimpleToStringStyle();
   private static final ThreadLocal REGISTRY = new ThreadLocal();
   private boolean useFieldNames = true;
   private boolean useClassName = true;
   private boolean useShortClassName = false;
   private boolean useIdentityHashCode = true;
   private String contentStart = "[";
   private String contentEnd = "]";
   private String fieldNameValueSeparator = "=";
   private boolean fieldSeparatorAtStart = false;
   private boolean fieldSeparatorAtEnd = false;
   private String fieldSeparator = ",";
   private String arrayStart = "{";
   private String arraySeparator = ",";
   private boolean arrayContentDetail = true;
   private String arrayEnd = "}";
   private boolean defaultFullDetail = true;
   private String nullText = "<null>";
   private String sizeStartText = "<size=";
   private String sizeEndText = ">";
   private String summaryObjectStartText = "<";
   private String summaryObjectEndText = ">";

   static Map getRegistry() {
      return (Map)REGISTRY.get();
   }

   static void register(Object var0) {
      if (var0 != null) {
         Map var1 = getRegistry();
         if (var1 == null) {
            REGISTRY.set(new WeakHashMap());
         }

         getRegistry().put(var0, (Object)null);
      }

   }

   static void unregister(Object var0) {
      if (var0 != null) {
         Map var1 = getRegistry();
         if (var1 != null) {
            var1.remove(var0);
            if (var1.isEmpty()) {
               REGISTRY.remove();
            }
         }
      }

   }

   protected ToStringStyle() {
   }

   public void appendSuper(StringBuffer var1, String var2) {
      this.appendToString(var1, var2);
   }

   public void appendToString(StringBuffer var1, String var2) {
      if (var2 != null) {
         int var3 = var2.indexOf(this.contentStart) + this.contentStart.length();
         int var4 = var2.lastIndexOf(this.contentEnd);
         if (var3 != var4 && var3 >= 0 && var4 >= 0) {
            String var5 = var2.substring(var3, var4);
            if (this.fieldSeparatorAtStart) {
               this.removeLastFieldSeparator(var1);
            }

            var1.append(var5);
            this.appendFieldSeparator(var1);
         }
      }

   }

   public void appendStart(StringBuffer var1, Object var2) {
      if (var2 != null) {
         this.appendClassName(var1, var2);
         this.appendIdentityHashCode(var1, var2);
         this.appendContentStart(var1);
         if (this.fieldSeparatorAtStart) {
            this.appendFieldSeparator(var1);
         }
      }

   }

   public void appendEnd(StringBuffer var1, Object var2) {
      if (!this.fieldSeparatorAtEnd) {
         this.removeLastFieldSeparator(var1);
      }

      this.appendContentEnd(var1);
      unregister(var2);
   }

   protected void removeLastFieldSeparator(StringBuffer var1) {
      int var2 = var1.length();
      int var3 = this.fieldSeparator.length();
      if (var2 > 0 && var3 > 0 && var2 >= var3) {
         boolean var4 = true;

         for(int var5 = 0; var5 < var3; ++var5) {
            if (var1.charAt(var2 - 1 - var5) != this.fieldSeparator.charAt(var3 - 1 - var5)) {
               var4 = false;
               break;
            }
         }

         if (var4) {
            var1.setLength(var2 - var3);
         }
      }

   }

   public void append(StringBuffer var1, String var2, Object var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else {
         this.appendInternal(var1, var2, var3, this.isFullDetail(var4));
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendInternal(StringBuffer var1, String var2, Object var3, boolean var4) {
      if (var3 != null && !(var3 instanceof Number) && !(var3 instanceof Boolean) && !(var3 instanceof Character)) {
         this.appendCyclicObject(var1, var2, var3);
      } else {
         register(var3);
         if (var3 instanceof Collection) {
            if (var4) {
               this.appendDetail(var1, var2, (Collection)var3);
            } else {
               this.appendSummarySize(var1, var2, ((Collection)var3).size());
            }
         } else if (var3 instanceof Map) {
            if (var4) {
               this.appendDetail(var1, var2, (Map)var3);
            } else {
               this.appendSummarySize(var1, var2, ((Map)var3).size());
            }
         } else if (var3 instanceof long[]) {
            if (var4) {
               this.appendDetail(var1, var2, (long[])((long[])var3));
            } else {
               this.appendSummary(var1, var2, (long[])((long[])var3));
            }
         } else if (var3 instanceof int[]) {
            if (var4) {
               this.appendDetail(var1, var2, (int[])((int[])var3));
            } else {
               this.appendSummary(var1, var2, (int[])((int[])var3));
            }
         } else if (var3 instanceof short[]) {
            if (var4) {
               this.appendDetail(var1, var2, (short[])((short[])var3));
            } else {
               this.appendSummary(var1, var2, (short[])((short[])var3));
            }
         } else if (var3 instanceof byte[]) {
            if (var4) {
               this.appendDetail(var1, var2, (byte[])((byte[])var3));
            } else {
               this.appendSummary(var1, var2, (byte[])((byte[])var3));
            }
         } else if (var3 instanceof char[]) {
            if (var4) {
               this.appendDetail(var1, var2, (char[])((char[])var3));
            } else {
               this.appendSummary(var1, var2, (char[])((char[])var3));
            }
         } else if (var3 instanceof double[]) {
            if (var4) {
               this.appendDetail(var1, var2, (double[])((double[])var3));
            } else {
               this.appendSummary(var1, var2, (double[])((double[])var3));
            }
         } else if (var3 instanceof float[]) {
            if (var4) {
               this.appendDetail(var1, var2, (float[])((float[])var3));
            } else {
               this.appendSummary(var1, var2, (float[])((float[])var3));
            }
         } else if (var3 instanceof boolean[]) {
            if (var4) {
               this.appendDetail(var1, var2, (boolean[])((boolean[])var3));
            } else {
               this.appendSummary(var1, var2, (boolean[])((boolean[])var3));
            }
         } else if (var3.getClass().isArray()) {
            if (var4) {
               this.appendDetail(var1, var2, (Object[])((Object[])var3));
            } else {
               this.appendSummary(var1, var2, (Object[])((Object[])var3));
            }
         } else if (var4) {
            this.appendDetail(var1, var2, var3);
         } else {
            this.appendSummary(var1, var2, var3);
         }

         unregister(var3);
      }
   }

   protected void appendCyclicObject(StringBuffer var1, String var2, Object var3) {
      ObjectUtils.identityToString(var1, var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, Object var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, Collection var3) {
      var1.append(var3);
   }

   protected void appendDetail(StringBuffer var1, String var2, Map var3) {
      var1.append(var3);
   }

   protected void appendSummary(StringBuffer var1, String var2, Object var3) {
      var1.append(this.summaryObjectStartText);
      var1.append(this.getShortClassName(var3.getClass()));
      var1.append(this.summaryObjectEndText);
   }

   public void append(StringBuffer var1, String var2, long var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, long var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, int var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, int var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, short var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, short var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, byte var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, byte var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, char var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, char var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, double var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, double var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, float var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, float var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, boolean var3) {
      this.appendFieldStart(var1, var2);
      this.appendDetail(var1, var2, var3);
      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, boolean var3) {
      var1.append(var3);
   }

   public void append(StringBuffer var1, String var2, Object[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, Object[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Object var5 = var3[var4];
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         if (var5 == null) {
            this.appendNullText(var1, var2);
         } else {
            this.appendInternal(var1, var2, var5, this.arrayContentDetail);
         }
      }

      var1.append(this.arrayEnd);
   }

   protected void reflectionAppendArrayDetail(StringBuffer var1, String var2, Object var3) {
      var1.append(this.arrayStart);
      int var4 = Array.getLength(var3);

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = Array.get(var3, var5);
         if (var5 > 0) {
            var1.append(this.arraySeparator);
         }

         if (var6 == null) {
            this.appendNullText(var1, var2);
         } else {
            this.appendInternal(var1, var2, var6, this.arrayContentDetail);
         }
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, Object[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, long[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, long[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, long[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, int[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, int[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, int[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, short[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, short[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, short[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, byte[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, byte[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, byte[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, char[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, char[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, char[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, double[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, double[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, double[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, float[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, float[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, float[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   public void append(StringBuffer var1, String var2, boolean[] var3, Boolean var4) {
      this.appendFieldStart(var1, var2);
      if (var3 == null) {
         this.appendNullText(var1, var2);
      } else if (var4 == null) {
         this.appendDetail(var1, var2, var3);
      } else {
         this.appendSummary(var1, var2, var3);
      }

      this.appendFieldEnd(var1, var2);
   }

   protected void appendDetail(StringBuffer var1, String var2, boolean[] var3) {
      var1.append(this.arrayStart);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var4 > 0) {
            var1.append(this.arraySeparator);
         }

         this.appendDetail(var1, var2, var3[var4]);
      }

      var1.append(this.arrayEnd);
   }

   protected void appendSummary(StringBuffer var1, String var2, boolean[] var3) {
      this.appendSummarySize(var1, var2, var3.length);
   }

   protected void appendClassName(StringBuffer var1, Object var2) {
      if (this.useClassName && var2 != null) {
         register(var2);
         if (this.useShortClassName) {
            var1.append(this.getShortClassName(var2.getClass()));
         } else {
            var1.append(var2.getClass().getName());
         }
      }

   }

   protected void appendIdentityHashCode(StringBuffer var1, Object var2) {
      if (this.isUseIdentityHashCode() && var2 != null) {
         register(var2);
         var1.append('@');
         var1.append(Integer.toHexString(System.identityHashCode(var2)));
      }

   }

   protected void appendContentStart(StringBuffer var1) {
      var1.append(this.contentStart);
   }

   protected void appendContentEnd(StringBuffer var1) {
      var1.append(this.contentEnd);
   }

   protected void appendNullText(StringBuffer var1, String var2) {
      var1.append(this.nullText);
   }

   protected void appendFieldSeparator(StringBuffer var1) {
      var1.append(this.fieldSeparator);
   }

   protected void appendFieldStart(StringBuffer var1, String var2) {
      if (this.useFieldNames && var2 != null) {
         var1.append(var2);
         var1.append(this.fieldNameValueSeparator);
      }

   }

   protected void appendFieldEnd(StringBuffer var1, String var2) {
      this.appendFieldSeparator(var1);
   }

   protected void appendSummarySize(StringBuffer var1, String var2, int var3) {
      var1.append(this.sizeStartText);
      var1.append(var3);
      var1.append(this.sizeEndText);
   }

   protected String getShortClassName(Class var1) {
      return ClassUtils.getShortClassName(var1);
   }

   protected boolean isUseClassName() {
      return this.useClassName;
   }

   protected void setUseClassName(boolean var1) {
      this.useClassName = var1;
   }

   protected boolean isUseShortClassName() {
      return this.useShortClassName;
   }

   protected void setUseShortClassName(boolean var1) {
      this.useShortClassName = var1;
   }

   protected boolean isUseIdentityHashCode() {
      return this.useIdentityHashCode;
   }

   protected void setUseIdentityHashCode(boolean var1) {
      this.useIdentityHashCode = var1;
   }

   protected boolean isUseFieldNames() {
      return this.useFieldNames;
   }

   protected void setUseFieldNames(boolean var1) {
      this.useFieldNames = var1;
   }

   protected boolean isDefaultFullDetail() {
      return this.defaultFullDetail;
   }

   protected void setDefaultFullDetail(boolean var1) {
      this.defaultFullDetail = var1;
   }

   protected boolean isArrayContentDetail() {
      return this.arrayContentDetail;
   }

   protected void setArrayContentDetail(boolean var1) {
      this.arrayContentDetail = var1;
   }

   protected String getArrayStart() {
      return this.arrayStart;
   }

   protected void setArrayStart(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.arrayStart = var1;
   }

   protected String getArrayEnd() {
      return this.arrayEnd;
   }

   protected void setArrayEnd(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.arrayEnd = var1;
   }

   protected String getArraySeparator() {
      return this.arraySeparator;
   }

   protected void setArraySeparator(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.arraySeparator = var1;
   }

   protected String getContentStart() {
      return this.contentStart;
   }

   protected void setContentStart(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.contentStart = var1;
   }

   protected String getContentEnd() {
      return this.contentEnd;
   }

   protected void setContentEnd(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.contentEnd = var1;
   }

   protected String getFieldNameValueSeparator() {
      return this.fieldNameValueSeparator;
   }

   protected void setFieldNameValueSeparator(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.fieldNameValueSeparator = var1;
   }

   protected String getFieldSeparator() {
      return this.fieldSeparator;
   }

   protected void setFieldSeparator(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.fieldSeparator = var1;
   }

   protected boolean isFieldSeparatorAtStart() {
      return this.fieldSeparatorAtStart;
   }

   protected void setFieldSeparatorAtStart(boolean var1) {
      this.fieldSeparatorAtStart = var1;
   }

   protected boolean isFieldSeparatorAtEnd() {
      return this.fieldSeparatorAtEnd;
   }

   protected void setFieldSeparatorAtEnd(boolean var1) {
      this.fieldSeparatorAtEnd = var1;
   }

   protected String getNullText() {
      return this.nullText;
   }

   protected void setNullText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.nullText = var1;
   }

   protected String getSizeStartText() {
      return this.sizeStartText;
   }

   protected void setSizeStartText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.sizeStartText = var1;
   }

   protected String getSizeEndText() {
      return this.sizeEndText;
   }

   protected void setSizeEndText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.sizeEndText = var1;
   }

   protected String getSummaryObjectStartText() {
      return this.summaryObjectStartText;
   }

   protected void setSummaryObjectStartText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.summaryObjectStartText = var1;
   }

   protected String getSummaryObjectEndText() {
      return this.summaryObjectEndText;
   }

   protected void setSummaryObjectEndText(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.summaryObjectEndText = var1;
   }

   private static final class MultiLineToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      MultiLineToStringStyle() {
         this.setContentStart("[");
         this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
         this.setFieldSeparatorAtStart(true);
         this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
      }

      private Object readResolve() {
         return ToStringStyle.MULTI_LINE_STYLE;
      }
   }

   private static final class SimpleToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      SimpleToStringStyle() {
         this.setUseClassName(false);
         this.setUseIdentityHashCode(false);
         this.setUseFieldNames(false);
         this.setContentStart("");
         this.setContentEnd("");
      }

      private Object readResolve() {
         return ToStringStyle.SIMPLE_STYLE;
      }
   }

   private static final class ShortPrefixToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      ShortPrefixToStringStyle() {
         this.setUseShortClassName(true);
         this.setUseIdentityHashCode(false);
      }

      private Object readResolve() {
         return ToStringStyle.SHORT_PREFIX_STYLE;
      }
   }

   private static final class NoFieldNameToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      NoFieldNameToStringStyle() {
         this.setUseFieldNames(false);
      }

      private Object readResolve() {
         return ToStringStyle.NO_FIELD_NAMES_STYLE;
      }
   }

   private static final class DefaultToStringStyle extends ToStringStyle {
      private static final long serialVersionUID = 1L;

      DefaultToStringStyle() {
      }

      private Object readResolve() {
         return ToStringStyle.DEFAULT_STYLE;
      }
   }
}
