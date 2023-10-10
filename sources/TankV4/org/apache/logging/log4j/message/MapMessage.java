package org.apache.logging.log4j.message;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.logging.log4j.util.EnglishEnums;

public class MapMessage implements MultiformatMessage {
   private static final long serialVersionUID = -5031471831131487120L;
   private final SortedMap data;

   public MapMessage() {
      this.data = new TreeMap();
   }

   public MapMessage(Map var1) {
      this.data = (SortedMap)(var1 instanceof SortedMap ? (SortedMap)var1 : new TreeMap(var1));
   }

   public String[] getFormats() {
      String[] var1 = new String[MapMessage.MapFormat.values().length];
      int var2 = 0;
      MapMessage.MapFormat[] var3 = MapMessage.MapFormat.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         MapMessage.MapFormat var6 = var3[var5];
         var1[var2++] = var6.name();
      }

      return var1;
   }

   public Object[] getParameters() {
      return this.data.values().toArray();
   }

   public String getFormat() {
      return "";
   }

   public Map getData() {
      return Collections.unmodifiableMap(this.data);
   }

   public void clear() {
      this.data.clear();
   }

   public void put(String var1, String var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("No value provided for key " + var1);
      } else {
         this.validate(var1, var2);
         this.data.put(var1, var2);
      }
   }

   protected void validate(String var1, String var2) {
   }

   public void putAll(Map var1) {
      this.data.putAll(var1);
   }

   public String get(String var1) {
      return (String)this.data.get(var1);
   }

   public String remove(String var1) {
      return (String)this.data.remove(var1);
   }

   public String asString() {
      return this.asString((MapMessage.MapFormat)null);
   }

   public String asString(String var1) {
      try {
         return this.asString((MapMessage.MapFormat)EnglishEnums.valueOf(MapMessage.MapFormat.class, var1));
      } catch (IllegalArgumentException var3) {
         return this.asString();
      }
   }

   private String asString(MapMessage.MapFormat var1) {
      StringBuilder var2 = new StringBuilder();
      if (var1 == null) {
         this.appendMap(var2);
      } else {
         switch(var1) {
         case XML:
            this.asXML(var2);
            break;
         case JSON:
            this.asJSON(var2);
            break;
         case JAVA:
            this.asJava(var2);
            break;
         default:
            this.appendMap(var2);
         }
      }

      return var2.toString();
   }

   public void asXML(StringBuilder var1) {
      var1.append("<Map>\n");
      Iterator var2 = this.data.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append("  <Entry key=\"").append((String)var3.getKey()).append("\">").append((String)var3.getValue()).append("</Entry>\n");
      }

      var1.append("</Map>");
   }

   public String getFormattedMessage() {
      return this.asString();
   }

   public String getFormattedMessage(String[] var1) {
      if (var1 != null && var1.length != 0) {
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            MapMessage.MapFormat[] var6 = MapMessage.MapFormat.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               MapMessage.MapFormat var9 = var6[var8];
               if (var9.name().equalsIgnoreCase(var5)) {
                  return this.asString(var9);
               }
            }
         }

         return this.asString();
      } else {
         return this.asString();
      }
   }

   protected void appendMap(StringBuilder var1) {
      boolean var2 = true;
      Iterator var3 = this.data.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (!var2) {
            var1.append(" ");
         }

         var2 = false;
         var1.append((String)var4.getKey()).append("=\"").append((String)var4.getValue()).append("\"");
      }

   }

   protected void asJSON(StringBuilder var1) {
      boolean var2 = true;
      var1.append("{");
      Iterator var3 = this.data.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (!var2) {
            var1.append(", ");
         }

         var2 = false;
         var1.append("\"").append((String)var4.getKey()).append("\":");
         var1.append("\"").append((String)var4.getValue()).append("\"");
      }

      var1.append("}");
   }

   protected void asJava(StringBuilder var1) {
      boolean var2 = true;
      var1.append("{");
      Iterator var3 = this.data.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (!var2) {
            var1.append(", ");
         }

         var2 = false;
         var1.append((String)var4.getKey()).append("=\"").append((String)var4.getValue()).append("\"");
      }

      var1.append("}");
   }

   public MapMessage newInstance(Map var1) {
      return new MapMessage(var1);
   }

   public String toString() {
      return this.asString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MapMessage var2 = (MapMessage)var1;
         return this.data.equals(var2.data);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.data.hashCode();
   }

   public Throwable getThrowable() {
      return null;
   }

   public static enum MapFormat {
      XML,
      JSON,
      JAVA;

      private static final MapMessage.MapFormat[] $VALUES = new MapMessage.MapFormat[]{XML, JSON, JAVA};
   }
}
