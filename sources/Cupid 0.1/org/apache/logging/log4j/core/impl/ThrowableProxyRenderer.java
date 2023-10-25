package org.apache.logging.log4j.core.impl;

import java.util.List;
import org.apache.logging.log4j.core.pattern.TextRenderer;

class ThrowableProxyRenderer {
  private static final String TAB = "\t";
  
  private static final String CAUSED_BY_LABEL = "Caused by: ";
  
  private static final String SUPPRESSED_LABEL = "Suppressed: ";
  
  private static final String WRAPPED_BY_LABEL = "Wrapped by: ";
  
  static void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    Throwable caused = (cause.getCauseProxy() != null) ? cause.getCauseProxy().getThrowable() : null;
    if (caused != null) {
      formatWrapper(sb, cause.getCauseProxy(), ignorePackages, textRenderer, suffix, lineSeparator);
      sb.append("Wrapped by: ");
      renderSuffix(suffix, sb, textRenderer);
    } 
    renderOn(cause, sb, textRenderer);
    renderSuffix(suffix, sb, textRenderer);
    textRenderer.render(lineSeparator, sb, "Text");
    formatElements(sb, "", cause.getCommonElementCount(), cause
        .getThrowable().getStackTrace(), cause.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  private static void formatCause(StringBuilder sb, String prefix, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    formatThrowableProxy(sb, prefix, "Caused by: ", cause, ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  private static void formatThrowableProxy(StringBuilder sb, String prefix, String causeLabel, ThrowableProxy throwableProxy, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    if (throwableProxy == null)
      return; 
    textRenderer.render(prefix, sb, "Prefix");
    textRenderer.render(causeLabel, sb, "CauseLabel");
    renderOn(throwableProxy, sb, textRenderer);
    renderSuffix(suffix, sb, textRenderer);
    textRenderer.render(lineSeparator, sb, "Text");
    formatElements(sb, prefix, throwableProxy.getCommonElementCount(), throwableProxy
        .getStackTrace(), throwableProxy.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
    formatSuppressed(sb, prefix + "\t", throwableProxy.getSuppressedProxies(), ignorePackages, textRenderer, suffix, lineSeparator);
    formatCause(sb, prefix, throwableProxy.getCauseProxy(), ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  private static void formatSuppressed(StringBuilder sb, String prefix, ThrowableProxy[] suppressedProxies, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    if (suppressedProxies == null)
      return; 
    for (ThrowableProxy suppressedProxy : suppressedProxies)
      formatThrowableProxy(sb, prefix, "Suppressed: ", suppressedProxy, ignorePackages, textRenderer, suffix, lineSeparator); 
  }
  
  private static void formatElements(StringBuilder sb, String prefix, int commonCount, StackTraceElement[] causedTrace, ExtendedStackTraceElement[] extStackTrace, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    if (ignorePackages == null || ignorePackages.isEmpty()) {
      for (ExtendedStackTraceElement element : extStackTrace)
        formatEntry(element, sb, prefix, textRenderer, suffix, lineSeparator); 
    } else {
      int count = 0;
      for (int i = 0; i < extStackTrace.length; i++) {
        if (!ignoreElement(causedTrace[i], ignorePackages)) {
          if (count > 0) {
            appendSuppressedCount(sb, prefix, count, textRenderer, suffix, lineSeparator);
            count = 0;
          } 
          formatEntry(extStackTrace[i], sb, prefix, textRenderer, suffix, lineSeparator);
        } else {
          count++;
        } 
      } 
      if (count > 0)
        appendSuppressedCount(sb, prefix, count, textRenderer, suffix, lineSeparator); 
    } 
    if (commonCount != 0) {
      textRenderer.render(prefix, sb, "Prefix");
      textRenderer.render("\t... ", sb, "More");
      textRenderer.render(Integer.toString(commonCount), sb, "More");
      textRenderer.render(" more", sb, "More");
      renderSuffix(suffix, sb, textRenderer);
      textRenderer.render(lineSeparator, sb, "Text");
    } 
  }
  
  private static void renderSuffix(String suffix, StringBuilder sb, TextRenderer textRenderer) {
    if (!suffix.isEmpty()) {
      textRenderer.render(" ", sb, "Suffix");
      textRenderer.render(suffix, sb, "Suffix");
    } 
  }
  
  private static void appendSuppressedCount(StringBuilder sb, String prefix, int count, TextRenderer textRenderer, String suffix, String lineSeparator) {
    textRenderer.render(prefix, sb, "Prefix");
    if (count == 1) {
      textRenderer.render("\t... ", sb, "Suppressed");
    } else {
      textRenderer.render("\t... suppressed ", sb, "Suppressed");
      textRenderer.render(Integer.toString(count), sb, "Suppressed");
      textRenderer.render(" lines", sb, "Suppressed");
    } 
    renderSuffix(suffix, sb, textRenderer);
    textRenderer.render(lineSeparator, sb, "Text");
  }
  
  private static void formatEntry(ExtendedStackTraceElement extStackTraceElement, StringBuilder sb, String prefix, TextRenderer textRenderer, String suffix, String lineSeparator) {
    textRenderer.render(prefix, sb, "Prefix");
    textRenderer.render("\tat ", sb, "At");
    extStackTraceElement.renderOn(sb, textRenderer);
    renderSuffix(suffix, sb, textRenderer);
    textRenderer.render(lineSeparator, sb, "Text");
  }
  
  private static boolean ignoreElement(StackTraceElement element, List<String> ignorePackages) {
    if (ignorePackages != null) {
      String className = element.getClassName();
      for (String pkg : ignorePackages) {
        if (className.startsWith(pkg))
          return true; 
      } 
    } 
    return false;
  }
  
  static void formatExtendedStackTraceTo(ThrowableProxy src, StringBuilder sb, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    textRenderer.render(src.getName(), sb, "Name");
    textRenderer.render(": ", sb, "NameMessageSeparator");
    textRenderer.render(src.getMessage(), sb, "Message");
    renderSuffix(suffix, sb, textRenderer);
    textRenderer.render(lineSeparator, sb, "Text");
    StackTraceElement[] causedTrace = (src.getThrowable() != null) ? src.getThrowable().getStackTrace() : null;
    formatElements(sb, "", 0, causedTrace, src.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
    formatSuppressed(sb, "\t", src.getSuppressedProxies(), ignorePackages, textRenderer, suffix, lineSeparator);
    formatCause(sb, "", src.getCauseProxy(), ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  static void formatCauseStackTrace(ThrowableProxy src, StringBuilder sb, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    ThrowableProxy causeProxy = src.getCauseProxy();
    if (causeProxy != null) {
      formatWrapper(sb, causeProxy, ignorePackages, textRenderer, suffix, lineSeparator);
      sb.append("Wrapped by: ");
      renderSuffix(suffix, sb, textRenderer);
    } 
    renderOn(src, sb, textRenderer);
    renderSuffix(suffix, sb, textRenderer);
    textRenderer.render(lineSeparator, sb, "Text");
    formatElements(sb, "", 0, src.getStackTrace(), src.getExtendedStackTrace(), ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  private static void renderOn(ThrowableProxy src, StringBuilder output, TextRenderer textRenderer) {
    String msg = src.getMessage();
    textRenderer.render(src.getName(), output, "Name");
    if (msg != null) {
      textRenderer.render(": ", output, "NameMessageSeparator");
      textRenderer.render(msg, output, "Message");
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ThrowableProxyRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */