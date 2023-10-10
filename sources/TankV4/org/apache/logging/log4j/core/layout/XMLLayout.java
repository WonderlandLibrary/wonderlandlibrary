package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.helpers.Throwables;
import org.apache.logging.log4j.core.helpers.Transform;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

@Plugin(
   name = "XMLLayout",
   category = "Core",
   elementType = "layout",
   printObject = true
)
public class XMLLayout extends AbstractStringLayout {
   private static final String XML_NAMESPACE = "http://logging.apache.org/log4j/2.0/events";
   private static final String ROOT_TAG = "Events";
   private static final int DEFAULT_SIZE = 256;
   private static final String DEFAULT_EOL = "\r\n";
   private static final String COMPACT_EOL = "";
   private static final String DEFAULT_INDENT = "  ";
   private static final String COMPACT_INDENT = "";
   private static final String DEFAULT_NS_PREFIX = "log4j";
   private static final String[] FORMATS = new String[]{"xml"};
   private final boolean locationInfo;
   private final boolean properties;
   private final boolean complete;
   private final String namespacePrefix;
   private final String eol;
   private final String indent1;
   private final String indent2;
   private final String indent3;

   protected XMLLayout(boolean var1, boolean var2, boolean var3, boolean var4, String var5, Charset var6) {
      super(var6);
      this.locationInfo = var1;
      this.properties = var2;
      this.complete = var3;
      this.eol = var4 ? "" : "\r\n";
      this.indent1 = var4 ? "" : "  ";
      this.indent2 = this.indent1 + this.indent1;
      this.indent3 = this.indent2 + this.indent1;
      this.namespacePrefix = (Strings.isEmpty(var5) ? "log4j" : var5) + ":";
   }

   public String toSerializable(LogEvent var1) {
      StringBuilder var2 = new StringBuilder(256);
      var2.append(this.indent1);
      var2.append('<');
      if (!this.complete) {
         var2.append(this.namespacePrefix);
      }

      var2.append("Event logger=\"");
      String var3 = var1.getLoggerName();
      if (var3.isEmpty()) {
         var3 = "root";
      }

      var2.append(Transform.escapeHtmlTags(var3));
      var2.append("\" timestamp=\"");
      var2.append(var1.getMillis());
      var2.append("\" level=\"");
      var2.append(Transform.escapeHtmlTags(String.valueOf(var1.getLevel())));
      var2.append("\" thread=\"");
      var2.append(Transform.escapeHtmlTags(var1.getThreadName()));
      var2.append("\">");
      var2.append(this.eol);
      Message var4 = var1.getMessage();
      if (var4 != null) {
         boolean var5 = false;
         if (var4 instanceof MultiformatMessage) {
            String[] var6 = ((MultiformatMessage)var4).getFormats();
            String[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String var10 = var7[var9];
               if (var10.equalsIgnoreCase("XML")) {
                  var5 = true;
                  break;
               }
            }
         }

         var2.append(this.indent2);
         var2.append('<');
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("Message>");
         if (var5) {
            var2.append(((MultiformatMessage)var4).getFormattedMessage(FORMATS));
         } else {
            var2.append("<![CDATA[");
            Transform.appendEscapingCDATA(var2, var1.getMessage().getFormattedMessage());
            var2.append("]]>");
         }

         var2.append("</");
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("Message>");
         var2.append(this.eol);
      }

      if (var1.getContextStack().getDepth() > 0) {
         var2.append(this.indent2);
         var2.append('<');
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("NDC><![CDATA[");
         Transform.appendEscapingCDATA(var2, var1.getContextStack().toString());
         var2.append("]]></");
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("NDC>");
         var2.append(this.eol);
      }

      Throwable var11 = var1.getThrown();
      if (var11 != null) {
         List var12 = Throwables.toStringList(var11);
         var2.append(this.indent2);
         var2.append('<');
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("Throwable><![CDATA[");
         Iterator var15 = var12.iterator();

         while(var15.hasNext()) {
            String var17 = (String)var15.next();
            Transform.appendEscapingCDATA(var2, var17);
            var2.append(this.eol);
         }

         var2.append("]]></");
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("Throwable>");
         var2.append(this.eol);
      }

      if (this.locationInfo) {
         StackTraceElement var13 = var1.getSource();
         var2.append(this.indent2);
         var2.append('<');
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("LocationInfo class=\"");
         var2.append(Transform.escapeHtmlTags(var13.getClassName()));
         var2.append("\" method=\"");
         var2.append(Transform.escapeHtmlTags(var13.getMethodName()));
         var2.append("\" file=\"");
         var2.append(Transform.escapeHtmlTags(var13.getFileName()));
         var2.append("\" line=\"");
         var2.append(var13.getLineNumber());
         var2.append("\"/>");
         var2.append(this.eol);
      }

      if (this.properties && var1.getContextMap().size() > 0) {
         var2.append(this.indent2);
         var2.append('<');
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("Properties>");
         var2.append(this.eol);
         Iterator var14 = var1.getContextMap().entrySet().iterator();

         while(var14.hasNext()) {
            Entry var16 = (Entry)var14.next();
            var2.append(this.indent3);
            var2.append('<');
            if (!this.complete) {
               var2.append(this.namespacePrefix);
            }

            var2.append("Data name=\"");
            var2.append(Transform.escapeHtmlTags((String)var16.getKey()));
            var2.append("\" value=\"");
            var2.append(Transform.escapeHtmlTags(String.valueOf(var16.getValue())));
            var2.append("\"/>");
            var2.append(this.eol);
         }

         var2.append(this.indent2);
         var2.append("</");
         if (!this.complete) {
            var2.append(this.namespacePrefix);
         }

         var2.append("Properties>");
         var2.append(this.eol);
      }

      var2.append(this.indent1);
      var2.append("</");
      if (!this.complete) {
         var2.append(this.namespacePrefix);
      }

      var2.append("Event>");
      var2.append(this.eol);
      return var2.toString();
   }

   public byte[] getHeader() {
      if (!this.complete) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("<?xml version=\"1.0\" encoding=\"");
         var1.append(this.getCharset().name());
         var1.append("\"?>");
         var1.append(this.eol);
         var1.append('<');
         var1.append("Events");
         var1.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
         var1.append(this.eol);
         return var1.toString().getBytes(this.getCharset());
      }
   }

   public byte[] getFooter() {
      return !this.complete ? null : ("</Events>" + this.eol).getBytes(this.getCharset());
   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap();
      var1.put("xsd", "log4j-events.xsd");
      var1.put("version", "2.0");
      return var1;
   }

   public String getContentType() {
      return "text/xml; charset=" + this.getCharset();
   }

   @PluginFactory
   public static XMLLayout createLayout(@PluginAttribute("locationInfo") String var0, @PluginAttribute("properties") String var1, @PluginAttribute("complete") String var2, @PluginAttribute("compact") String var3, @PluginAttribute("namespacePrefix") String var4, @PluginAttribute("charset") String var5) {
      Charset var6 = Charsets.getSupportedCharset(var5, Charsets.UTF_8);
      boolean var7 = Boolean.parseBoolean(var0);
      boolean var8 = Boolean.parseBoolean(var1);
      boolean var9 = Boolean.parseBoolean(var2);
      boolean var10 = Boolean.parseBoolean(var3);
      return new XMLLayout(var7, var8, var9, var10, var4, var6);
   }

   public Serializable toSerializable(LogEvent var1) {
      return this.toSerializable(var1);
   }
}
