package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import org.apache.logging.log4j.core.util.Transform;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "HtmlLayout", category = "Core", elementType = "layout", printObject = true)
public final class HtmlLayout extends AbstractStringLayout {
  public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";
  
  private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
  
  private static final String REGEXP = Strings.LINE_SEPARATOR.equals("\n") ? "\n" : (Strings.LINE_SEPARATOR + "|\n");
  
  private static final String DEFAULT_TITLE = "Log4j Log Messages";
  
  private static final String DEFAULT_CONTENT_TYPE = "text/html";
  
  private static final String DEFAULT_DATE_PATTERN = "JVM_ELAPSE_TIME";
  
  private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
  
  private final boolean locationInfo;
  
  private final String title;
  
  private final String contentType;
  
  private final String font;
  
  private final String fontSize;
  
  private final String headerSize;
  
  private final DatePatternConverter datePatternConverter;
  
  public enum FontSize {
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
    
    FontSize(String size) {
      this.size = size;
    }
    
    public String getFontSize() {
      return this.size;
    }
    
    public static FontSize getFontSize(String size) {
      for (FontSize fontSize : values()) {
        if (fontSize.size.equals(size))
          return fontSize; 
      } 
      return SMALL;
    }
    
    public FontSize larger() {
      return (ordinal() < XXLARGE.ordinal()) ? values()[ordinal() + 1] : this;
    }
  }
  
  private HtmlLayout(boolean locationInfo, String title, String contentType, Charset charset, String font, String fontSize, String headerSize, String datePattern, String timezone) {
    super(charset);
    this.locationInfo = locationInfo;
    this.title = title;
    this.contentType = addCharsetToContentType(contentType);
    this.font = font;
    this.fontSize = fontSize;
    this.headerSize = headerSize;
    this
      .datePatternConverter = "JVM_ELAPSE_TIME".equals(datePattern) ? null : DatePatternConverter.newInstance(new String[] { datePattern, timezone });
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public boolean isLocationInfo() {
    return this.locationInfo;
  }
  
  public boolean requiresLocation() {
    return this.locationInfo;
  }
  
  private String addCharsetToContentType(String contentType) {
    if (contentType == null)
      return "text/html; charset=" + getCharset(); 
    return contentType.contains("charset") ? contentType : (contentType + "; charset=" + getCharset());
  }
  
  public String toSerializable(LogEvent event) {
    StringBuilder sbuf = getStringBuilder();
    sbuf.append(Strings.LINE_SEPARATOR).append("<tr>").append(Strings.LINE_SEPARATOR);
    sbuf.append("<td>");
    if (this.datePatternConverter == null) {
      sbuf.append(event.getTimeMillis() - this.jvmStartTime);
    } else {
      this.datePatternConverter.format(event, sbuf);
    } 
    sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
    String escapedThread = Transform.escapeHtmlTags(event.getThreadName());
    sbuf.append("<td title=\"").append(escapedThread).append(" thread\">");
    sbuf.append(escapedThread);
    sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
    sbuf.append("<td title=\"Level\">");
    if (event.getLevel().equals(Level.DEBUG)) {
      sbuf.append("<font color=\"#339933\">");
      sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
      sbuf.append("</font>");
    } else if (event.getLevel().isMoreSpecificThan(Level.WARN)) {
      sbuf.append("<font color=\"#993300\"><strong>");
      sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
      sbuf.append("</strong></font>");
    } else {
      sbuf.append(Transform.escapeHtmlTags(String.valueOf(event.getLevel())));
    } 
    sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
    String escapedLogger = Transform.escapeHtmlTags(event.getLoggerName());
    if (Strings.isEmpty(escapedLogger))
      escapedLogger = "root"; 
    sbuf.append("<td title=\"").append(escapedLogger).append(" logger\">");
    sbuf.append(escapedLogger);
    sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
    if (this.locationInfo) {
      StackTraceElement element = event.getSource();
      sbuf.append("<td>");
      sbuf.append(Transform.escapeHtmlTags(element.getFileName()));
      sbuf.append(':');
      sbuf.append(element.getLineNumber());
      sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
    } 
    sbuf.append("<td title=\"Message\">");
    sbuf.append(Transform.escapeHtmlTags(event.getMessage().getFormattedMessage()).replaceAll(REGEXP, "<br />"));
    sbuf.append("</td>").append(Strings.LINE_SEPARATOR);
    sbuf.append("</tr>").append(Strings.LINE_SEPARATOR);
    if (event.getContextStack() != null && !event.getContextStack().isEmpty()) {
      sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
      sbuf.append(";\" colspan=\"6\" ");
      sbuf.append("title=\"Nested Diagnostic Context\">");
      sbuf.append("NDC: ").append(Transform.escapeHtmlTags(event.getContextStack().toString()));
      sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
    } 
    if (event.getContextData() != null && !event.getContextData().isEmpty()) {
      sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
      sbuf.append(";\" colspan=\"6\" ");
      sbuf.append("title=\"Mapped Diagnostic Context\">");
      sbuf.append("MDC: ").append(Transform.escapeHtmlTags(event.getContextData().toMap().toString()));
      sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
    } 
    Throwable throwable = event.getThrown();
    if (throwable != null) {
      sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
      sbuf.append(";\" colspan=\"6\">");
      appendThrowableAsHtml(throwable, sbuf);
      sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
    } 
    return sbuf.toString();
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  private void appendThrowableAsHtml(Throwable throwable, StringBuilder sbuf) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    try {
      throwable.printStackTrace(pw);
    } catch (RuntimeException runtimeException) {}
    pw.flush();
    LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
    ArrayList<String> lines = new ArrayList<>();
    try {
      String line = reader.readLine();
      while (line != null) {
        lines.add(line);
        line = reader.readLine();
      } 
    } catch (IOException ex) {
      if (ex instanceof java.io.InterruptedIOException)
        Thread.currentThread().interrupt(); 
      lines.add(ex.toString());
    } 
    boolean first = true;
    for (String line : lines) {
      if (!first) {
        sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
      } else {
        first = false;
      } 
      sbuf.append(Transform.escapeHtmlTags(line));
      sbuf.append(Strings.LINE_SEPARATOR);
    } 
  }
  
  private StringBuilder appendLs(StringBuilder sbuilder, String s) {
    sbuilder.append(s).append(Strings.LINE_SEPARATOR);
    return sbuilder;
  }
  
  private StringBuilder append(StringBuilder sbuilder, String s) {
    sbuilder.append(s);
    return sbuilder;
  }
  
  public byte[] getHeader() {
    StringBuilder sbuf = new StringBuilder();
    append(sbuf, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
    appendLs(sbuf, "\"http://www.w3.org/TR/html4/loose.dtd\">");
    appendLs(sbuf, "<html>");
    appendLs(sbuf, "<head>");
    append(sbuf, "<meta charset=\"");
    append(sbuf, getCharset().toString());
    appendLs(sbuf, "\"/>");
    append(sbuf, "<title>").append(this.title);
    appendLs(sbuf, "</title>");
    appendLs(sbuf, "<style type=\"text/css\">");
    appendLs(sbuf, "<!--");
    append(sbuf, "body, table {font-family:").append(this.font).append("; font-size: ");
    appendLs(sbuf, this.headerSize).append(";}");
    appendLs(sbuf, "th {background: #336699; color: #FFFFFF; text-align: left;}");
    appendLs(sbuf, "-->");
    appendLs(sbuf, "</style>");
    appendLs(sbuf, "</head>");
    appendLs(sbuf, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">");
    appendLs(sbuf, "<hr size=\"1\" noshade=\"noshade\">");
    appendLs(sbuf, "Log session start time " + new Date() + "<br>");
    appendLs(sbuf, "<br>");
    appendLs(sbuf, "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
    appendLs(sbuf, "<tr>");
    appendLs(sbuf, "<th>Time</th>");
    appendLs(sbuf, "<th>Thread</th>");
    appendLs(sbuf, "<th>Level</th>");
    appendLs(sbuf, "<th>Logger</th>");
    if (this.locationInfo)
      appendLs(sbuf, "<th>File:Line</th>"); 
    appendLs(sbuf, "<th>Message</th>");
    appendLs(sbuf, "</tr>");
    return sbuf.toString().getBytes(getCharset());
  }
  
  public byte[] getFooter() {
    StringBuilder sbuf = new StringBuilder();
    appendLs(sbuf, "</table>");
    appendLs(sbuf, "<br>");
    appendLs(sbuf, "</body></html>");
    return getBytes(sbuf.toString());
  }
  
  @Deprecated
  @PluginFactory
  public static HtmlLayout createLayout(@PluginAttribute("locationInfo") boolean locationInfo, @PluginAttribute(value = "title", defaultString = "Log4j Log Messages") String title, @PluginAttribute("contentType") String contentType, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("fontSize") String fontSize, @PluginAttribute(value = "fontName", defaultString = "arial,sans-serif") String font) {
    FontSize fs = FontSize.getFontSize(fontSize);
    fontSize = fs.getFontSize();
    String headerSize = fs.larger().getFontSize();
    if (contentType == null)
      contentType = "text/html; charset=" + charset; 
    return new HtmlLayout(locationInfo, title, contentType, charset, font, fontSize, headerSize, "JVM_ELAPSE_TIME", null);
  }
  
  public static HtmlLayout createDefaultLayout() {
    return newBuilder().build();
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<HtmlLayout> {
    @PluginBuilderAttribute
    private boolean locationInfo = false;
    
    @PluginBuilderAttribute
    private String title = "Log4j Log Messages";
    
    @PluginBuilderAttribute
    private String contentType = null;
    
    @PluginBuilderAttribute
    private Charset charset = StandardCharsets.UTF_8;
    
    @PluginBuilderAttribute
    private HtmlLayout.FontSize fontSize = HtmlLayout.FontSize.SMALL;
    
    @PluginBuilderAttribute
    private String fontName = "arial,sans-serif";
    
    @PluginBuilderAttribute
    private String datePattern = "JVM_ELAPSE_TIME";
    
    @PluginBuilderAttribute
    private String timezone = null;
    
    public Builder withLocationInfo(boolean locationInfo) {
      this.locationInfo = locationInfo;
      return this;
    }
    
    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }
    
    public Builder withContentType(String contentType) {
      this.contentType = contentType;
      return this;
    }
    
    public Builder withCharset(Charset charset) {
      this.charset = charset;
      return this;
    }
    
    public Builder withFontSize(HtmlLayout.FontSize fontSize) {
      this.fontSize = fontSize;
      return this;
    }
    
    public Builder withFontName(String fontName) {
      this.fontName = fontName;
      return this;
    }
    
    public Builder setDatePattern(String datePattern) {
      this.datePattern = datePattern;
      return this;
    }
    
    public Builder setTimezone(String timezone) {
      this.timezone = timezone;
      return this;
    }
    
    public HtmlLayout build() {
      if (this.contentType == null)
        this.contentType = "text/html; charset=" + this.charset; 
      return new HtmlLayout(this.locationInfo, this.title, this.contentType, this.charset, this.fontName, this.fontSize.getFontSize(), this.fontSize
          .larger().getFontSize(), this.datePattern, this.timezone);
    }
    
    private Builder() {}
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\HtmlLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */