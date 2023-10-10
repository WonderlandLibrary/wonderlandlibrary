package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.Priority;

@Plugin(
   name = "SyslogLayout",
   category = "Core",
   elementType = "layout",
   printObject = true
)
public class SyslogLayout extends AbstractStringLayout {
   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
   private final Facility facility;
   private final boolean includeNewLine;
   private final String escapeNewLine;
   private final SimpleDateFormat dateFormat;
   private final String localHostname;

   protected SyslogLayout(Facility var1, boolean var2, String var3, Charset var4) {
      super(var4);
      this.dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
      this.localHostname = this.getLocalHostname();
      this.facility = var1;
      this.includeNewLine = var2;
      this.escapeNewLine = var3 == null ? null : Matcher.quoteReplacement(var3);
   }

   public String toSerializable(LogEvent var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("<");
      var2.append(Priority.getPriority(this.facility, var1.getLevel()));
      var2.append(">");
      this.addDate(var1.getMillis(), var2);
      var2.append(" ");
      var2.append(this.localHostname);
      var2.append(" ");
      String var3 = var1.getMessage().getFormattedMessage();
      if (null != this.escapeNewLine) {
         var3 = NEWLINE_PATTERN.matcher(var3).replaceAll(this.escapeNewLine);
      }

      var2.append(var3);
      if (this.includeNewLine) {
         var2.append("\n");
      }

      return var2.toString();
   }

   private String getLocalHostname() {
      try {
         InetAddress var1 = InetAddress.getLocalHost();
         return var1.getHostName();
      } catch (UnknownHostException var2) {
         LOGGER.error((String)"Could not determine local host name", (Throwable)var2);
         return "UNKNOWN_LOCALHOST";
      }
   }

   private synchronized void addDate(long var1, StringBuilder var3) {
      int var4 = var3.length() + 4;
      var3.append(this.dateFormat.format(new Date(var1)));
      if (var3.charAt(var4) == '0') {
         var3.setCharAt(var4, ' ');
      }

   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap();
      var1.put("structured", "false");
      var1.put("formatType", "logfilepatternreceiver");
      var1.put("dateFormat", this.dateFormat.toPattern());
      var1.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
      return var1;
   }

   @PluginFactory
   public static SyslogLayout createLayout(@PluginAttribute("facility") String var0, @PluginAttribute("newLine") String var1, @PluginAttribute("newLineEscape") String var2, @PluginAttribute("charset") String var3) {
      Charset var4 = Charsets.getSupportedCharset(var3);
      boolean var5 = Boolean.parseBoolean(var1);
      Facility var6 = Facility.toFacility(var0, Facility.LOCAL0);
      return new SyslogLayout(var6, var5, var2, var4);
   }

   public Serializable toSerializable(LogEvent var1) {
      return this.toSerializable(var1);
   }
}
