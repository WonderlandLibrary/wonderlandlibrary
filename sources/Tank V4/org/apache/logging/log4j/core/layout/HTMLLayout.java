package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.helpers.Transform;

@Plugin(
   name = "HTMLLayout",
   category = "Core",
   elementType = "layout",
   printObject = true
)
public final class HTMLLayout extends AbstractStringLayout {
   private static final int BUF_SIZE = 256;
   private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
   private static final String REGEXP;
   private static final String DEFAULT_TITLE = "Log4j Log Messages";
   private static final String DEFAULT_CONTENT_TYPE = "text/html";
   private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
   private final boolean locationInfo;
   private final String title;
   private final String contentType;
   private final String font;
   private final String fontSize;
   private final String headerSize;

   private HTMLLayout(boolean var1, String var2, String var3, Charset var4, String var5, String var6, String var7) {
      super(var4);
      this.locationInfo = var1;
      this.title = var2;
      this.contentType = var3;
      this.font = var5;
      this.fontSize = var6;
      this.headerSize = var7;
   }

   public String toSerializable(LogEvent var1) {
      StringBuilder var2 = new StringBuilder(256);
      var2.append(Constants.LINE_SEP).append("<tr>").append(Constants.LINE_SEP);
      var2.append("<td>");
      var2.append(var1.getMillis() - this.jvmStartTime);
      var2.append("</td>").append(Constants.LINE_SEP);
      String var3 = Transform.escapeHtmlTags(var1.getThreadName());
      var2.append("<td title=\"").append(var3).append(" thread\">");
      var2.append(var3);
      var2.append("</td>").append(Constants.LINE_SEP);
      var2.append("<td title=\"Level\">");
      if (var1.getLevel().equals(Level.DEBUG)) {
         var2.append("<font color=\"#339933\">");
         var2.append(Transform.escapeHtmlTags(String.valueOf(var1.getLevel())));
         var2.append("</font>");
      } else if (var1.getLevel().isAtLeastAsSpecificAs(Level.WARN)) {
         var2.append("<font color=\"#993300\"><strong>");
         var2.append(Transform.escapeHtmlTags(String.valueOf(var1.getLevel())));
         var2.append("</strong></font>");
      } else {
         var2.append(Transform.escapeHtmlTags(String.valueOf(var1.getLevel())));
      }

      var2.append("</td>").append(Constants.LINE_SEP);
      String var4 = Transform.escapeHtmlTags(var1.getLoggerName());
      if (var4.isEmpty()) {
         var4 = "root";
      }

      var2.append("<td title=\"").append(var4).append(" logger\">");
      var2.append(var4);
      var2.append("</td>").append(Constants.LINE_SEP);
      if (this.locationInfo) {
         StackTraceElement var5 = var1.getSource();
         var2.append("<td>");
         var2.append(Transform.escapeHtmlTags(var5.getFileName()));
         var2.append(':');
         var2.append(var5.getLineNumber());
         var2.append("</td>").append(Constants.LINE_SEP);
      }

      var2.append("<td title=\"Message\">");
      var2.append(Transform.escapeHtmlTags(var1.getMessage().getFormattedMessage()).replaceAll(REGEXP, "<br />"));
      var2.append("</td>").append(Constants.LINE_SEP);
      var2.append("</tr>").append(Constants.LINE_SEP);
      if (var1.getContextStack().getDepth() > 0) {
         var2.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
         var2.append(";\" colspan=\"6\" ");
         var2.append("title=\"Nested Diagnostic Context\">");
         var2.append("NDC: ").append(Transform.escapeHtmlTags(var1.getContextStack().toString()));
         var2.append("</td></tr>").append(Constants.LINE_SEP);
      }

      if (var1.getContextMap().size() > 0) {
         var2.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
         var2.append(";\" colspan=\"6\" ");
         var2.append("title=\"Mapped Diagnostic Context\">");
         var2.append("MDC: ").append(Transform.escapeHtmlTags(var1.getContextMap().toString()));
         var2.append("</td></tr>").append(Constants.LINE_SEP);
      }

      Throwable var6 = var1.getThrown();
      if (var6 != null) {
         var2.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
         var2.append(";\" colspan=\"6\">");
         this.appendThrowableAsHTML(var6, var2);
         var2.append("</td></tr>").append(Constants.LINE_SEP);
      }

      return var2.toString();
   }

   public Map getContentFormat() {
      return new HashMap();
   }

   public String getContentType() {
      return this.contentType;
   }

   private void appendThrowableAsHTML(Throwable var1, StringBuilder var2) {
      StringWriter var3 = new StringWriter();
      PrintWriter var4 = new PrintWriter(var3);

      try {
         var1.printStackTrace(var4);
      } catch (RuntimeException var10) {
      }

      var4.flush();
      LineNumberReader var5 = new LineNumberReader(new StringReader(var3.toString()));
      ArrayList var6 = new ArrayList();

      try {
         for(String var7 = var5.readLine(); var7 != null; var7 = var5.readLine()) {
            var6.add(var7);
         }
      } catch (IOException var11) {
         if (var11 instanceof InterruptedIOException) {
            Thread.currentThread().interrupt();
         }

         var6.add(var11.toString());
      }

      boolean var12 = true;
      Iterator var8 = var6.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         if (!var12) {
            var2.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
         } else {
            var12 = false;
         }

         var2.append(Transform.escapeHtmlTags(var9));
         var2.append(Constants.LINE_SEP);
      }

   }

   public byte[] getHeader() {
      StringBuilder var1 = new StringBuilder();
      var1.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
      var1.append("\"http://www.w3.org/TR/html4/loose.dtd\">");
      var1.append(Constants.LINE_SEP);
      var1.append("<html>").append(Constants.LINE_SEP);
      var1.append("<head>").append(Constants.LINE_SEP);
      var1.append("<meta charset=\"").append(this.getCharset()).append("\"/>").append(Constants.LINE_SEP);
      var1.append("<title>").append(this.title).append("</title>").append(Constants.LINE_SEP);
      var1.append("<style type=\"text/css\">").append(Constants.LINE_SEP);
      var1.append("<!--").append(Constants.LINE_SEP);
      var1.append("body, table {font-family:").append(this.font).append("; font-size: ");
      var1.append(this.headerSize).append(";}").append(Constants.LINE_SEP);
      var1.append("th {background: #336699; color: #FFFFFF; text-align: left;}").append(Constants.LINE_SEP);
      var1.append("-->").append(Constants.LINE_SEP);
      var1.append("</style>").append(Constants.LINE_SEP);
      var1.append("</head>").append(Constants.LINE_SEP);
      var1.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">").append(Constants.LINE_SEP);
      var1.append("<hr size=\"1\" noshade>").append(Constants.LINE_SEP);
      var1.append("Log session start time " + new Date() + "<br>").append(Constants.LINE_SEP);
      var1.append("<br>").append(Constants.LINE_SEP);
      var1.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
      var1.append(Constants.LINE_SEP);
      var1.append("<tr>").append(Constants.LINE_SEP);
      var1.append("<th>Time</th>").append(Constants.LINE_SEP);
      var1.append("<th>Thread</th>").append(Constants.LINE_SEP);
      var1.append("<th>Level</th>").append(Constants.LINE_SEP);
      var1.append("<th>Logger</th>").append(Constants.LINE_SEP);
      if (this.locationInfo) {
         var1.append("<th>File:Line</th>").append(Constants.LINE_SEP);
      }

      var1.append("<th>Message</th>").append(Constants.LINE_SEP);
      var1.append("</tr>").append(Constants.LINE_SEP);
      return var1.toString().getBytes(this.getCharset());
   }

   public byte[] getFooter() {
      StringBuilder var1 = new StringBuilder();
      var1.append("</table>").append(Constants.LINE_SEP);
      var1.append("<br>").append(Constants.LINE_SEP);
      var1.append("</body></html>");
      return var1.toString().getBytes(this.getCharset());
   }

   @PluginFactory
   public static HTMLLayout createLayout(@PluginAttribute("locationInfo") String var0, @PluginAttribute("title") String var1, @PluginAttribute("contentType") String var2, @PluginAttribute("charset") String var3, @PluginAttribute("fontSize") String var4, @PluginAttribute("fontName") String var5) {
      Charset var6 = Charsets.getSupportedCharset(var3, Charsets.UTF_8);
      if (var5 == null) {
         var5 = "arial,sans-serif";
      }

      HTMLLayout.FontSize var7 = HTMLLayout.FontSize.getFontSize(var4);
      var4 = var7.getFontSize();
      String var8 = var7.larger().getFontSize();
      boolean var9 = Boolean.parseBoolean(var0);
      if (var1 == null) {
         var1 = "Log4j Log Messages";
      }

      if (var2 == null) {
         var2 = "text/html; charset=" + var6;
      }

      return new HTMLLayout(var9, var1, var2, var6, var5, var4, var8);
   }

   public Serializable toSerializable(LogEvent var1) {
      return this.toSerializable(var1);
   }

   static {
      REGEXP = Constants.LINE_SEP.equals("\n") ? "\n" : Constants.LINE_SEP + "|\n";
   }

   private static enum FontSize {
      SMALLER("smaller"),
      XXSMALL("xx-small"),
      XSMALL("x-small"),
      SMALL("small"),
      MEDIUM("medium"),
      LARGE("large"),
      XLARGE("x-large"),
      XXLARGE("xx-large"),
      LARGER("larger");

      private final String size;
      private static final HTMLLayout.FontSize[] $VALUES = new HTMLLayout.FontSize[]{SMALLER, XXSMALL, XSMALL, SMALL, MEDIUM, LARGE, XLARGE, XXLARGE, LARGER};

      private FontSize(String var3) {
         this.size = var3;
      }

      public String getFontSize() {
         return this.size;
      }

      public static HTMLLayout.FontSize getFontSize(String var0) {
         HTMLLayout.FontSize[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            HTMLLayout.FontSize var4 = var1[var3];
            if (var4.size.equals(var0)) {
               return var4;
            }
         }

         return SMALL;
      }

      public HTMLLayout.FontSize larger() {
         return this.ordinal() < XXLARGE.ordinal() ? values()[this.ordinal() + 1] : this;
      }
   }
}
