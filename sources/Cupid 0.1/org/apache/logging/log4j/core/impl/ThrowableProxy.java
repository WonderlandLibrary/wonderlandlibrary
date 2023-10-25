package org.apache.logging.log4j.core.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import org.apache.logging.log4j.util.StackLocatorUtil;

public class ThrowableProxy implements Serializable {
  static final ThrowableProxy[] EMPTY_ARRAY = new ThrowableProxy[0];
  
  private static final char EOL = '\n';
  
  private static final String EOL_STR = String.valueOf('\n');
  
  private static final long serialVersionUID = -2752771578252251910L;
  
  private final ThrowableProxy causeProxy;
  
  private int commonElementCount;
  
  private final ExtendedStackTraceElement[] extendedStackTrace;
  
  private final String localizedMessage;
  
  private final String message;
  
  private final String name;
  
  private final ThrowableProxy[] suppressedProxies;
  
  private final transient Throwable throwable;
  
  ThrowableProxy() {
    this.throwable = null;
    this.name = null;
    this.extendedStackTrace = ExtendedStackTraceElement.EMPTY_ARRAY;
    this.causeProxy = null;
    this.message = null;
    this.localizedMessage = null;
    this.suppressedProxies = EMPTY_ARRAY;
  }
  
  public ThrowableProxy(Throwable throwable) {
    this(throwable, null);
  }
  
  ThrowableProxy(Throwable throwable, Set<Throwable> visited) {
    this.throwable = throwable;
    this.name = throwable.getClass().getName();
    this.message = throwable.getMessage();
    this.localizedMessage = throwable.getLocalizedMessage();
    Map<String, ThrowableProxyHelper.CacheEntry> map = new HashMap<>();
    Stack<Class<?>> stack = StackLocatorUtil.getCurrentStackTrace();
    this.extendedStackTrace = ThrowableProxyHelper.toExtendedStackTrace(this, stack, map, null, throwable.getStackTrace());
    Throwable throwableCause = throwable.getCause();
    Set<Throwable> causeVisited = new HashSet<>(1);
    this.causeProxy = (throwableCause == null) ? null : new ThrowableProxy(throwable, stack, map, throwableCause, visited, causeVisited);
    this.suppressedProxies = ThrowableProxyHelper.toSuppressedProxies(throwable, visited);
  }
  
  private ThrowableProxy(Throwable parent, Stack<Class<?>> stack, Map<String, ThrowableProxyHelper.CacheEntry> map, Throwable cause, Set<Throwable> suppressedVisited, Set<Throwable> causeVisited) {
    causeVisited.add(cause);
    this.throwable = cause;
    this.name = cause.getClass().getName();
    this.message = this.throwable.getMessage();
    this.localizedMessage = this.throwable.getLocalizedMessage();
    this.extendedStackTrace = ThrowableProxyHelper.toExtendedStackTrace(this, stack, map, parent.getStackTrace(), cause.getStackTrace());
    Throwable causeCause = cause.getCause();
    this.causeProxy = (causeCause == null || causeVisited.contains(causeCause)) ? null : new ThrowableProxy(parent, stack, map, causeCause, suppressedVisited, causeVisited);
    this.suppressedProxies = ThrowableProxyHelper.toSuppressedProxies(cause, suppressedVisited);
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    ThrowableProxy other = (ThrowableProxy)obj;
    if (!Objects.equals(this.causeProxy, other.causeProxy))
      return false; 
    if (this.commonElementCount != other.commonElementCount)
      return false; 
    if (!Objects.equals(this.name, other.name))
      return false; 
    if (!Arrays.equals((Object[])this.extendedStackTrace, (Object[])other.extendedStackTrace))
      return false; 
    if (!Arrays.equals((Object[])this.suppressedProxies, (Object[])other.suppressedProxies))
      return false; 
    return true;
  }
  
  public void formatWrapper(StringBuilder sb, ThrowableProxy cause, String suffix) {
    formatWrapper(sb, cause, null, (TextRenderer)PlainTextRenderer.getInstance(), suffix);
  }
  
  public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, String suffix) {
    formatWrapper(sb, cause, ignorePackages, (TextRenderer)PlainTextRenderer.getInstance(), suffix);
  }
  
  public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
    formatWrapper(sb, cause, ignorePackages, textRenderer, suffix, EOL_STR);
  }
  
  public void formatWrapper(StringBuilder sb, ThrowableProxy cause, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    ThrowableProxyRenderer.formatWrapper(sb, cause, ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  public ThrowableProxy getCauseProxy() {
    return this.causeProxy;
  }
  
  public String getCauseStackTraceAsString(String suffix) {
    return getCauseStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
  }
  
  public String getCauseStackTraceAsString(List<String> packages, String suffix) {
    return getCauseStackTraceAsString(packages, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
  }
  
  public String getCauseStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
    return getCauseStackTraceAsString(ignorePackages, textRenderer, suffix, EOL_STR);
  }
  
  public String getCauseStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    StringBuilder sb = new StringBuilder();
    ThrowableProxyRenderer.formatCauseStackTrace(this, sb, ignorePackages, textRenderer, suffix, lineSeparator);
    return sb.toString();
  }
  
  public int getCommonElementCount() {
    return this.commonElementCount;
  }
  
  void setCommonElementCount(int value) {
    this.commonElementCount = value;
  }
  
  public ExtendedStackTraceElement[] getExtendedStackTrace() {
    return this.extendedStackTrace;
  }
  
  public String getExtendedStackTraceAsString() {
    return getExtendedStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance(), "", EOL_STR);
  }
  
  public String getExtendedStackTraceAsString(String suffix) {
    return getExtendedStackTraceAsString(null, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
  }
  
  public String getExtendedStackTraceAsString(List<String> ignorePackages, String suffix) {
    return getExtendedStackTraceAsString(ignorePackages, (TextRenderer)PlainTextRenderer.getInstance(), suffix, EOL_STR);
  }
  
  public String getExtendedStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
    return getExtendedStackTraceAsString(ignorePackages, textRenderer, suffix, EOL_STR);
  }
  
  public String getExtendedStackTraceAsString(List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    StringBuilder sb = new StringBuilder(1024);
    formatExtendedStackTraceTo(sb, ignorePackages, textRenderer, suffix, lineSeparator);
    return sb.toString();
  }
  
  public void formatExtendedStackTraceTo(StringBuilder sb, List<String> ignorePackages, TextRenderer textRenderer, String suffix, String lineSeparator) {
    ThrowableProxyRenderer.formatExtendedStackTraceTo(this, sb, ignorePackages, textRenderer, suffix, lineSeparator);
  }
  
  public String getLocalizedMessage() {
    return this.localizedMessage;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public String getName() {
    return this.name;
  }
  
  public StackTraceElement[] getStackTrace() {
    return (this.throwable == null) ? null : this.throwable.getStackTrace();
  }
  
  public ThrowableProxy[] getSuppressedProxies() {
    return this.suppressedProxies;
  }
  
  public String getSuppressedStackTrace(String suffix) {
    ThrowableProxy[] suppressed = getSuppressedProxies();
    if (suppressed == null || suppressed.length == 0)
      return ""; 
    StringBuilder sb = (new StringBuilder("Suppressed Stack Trace Elements:")).append('\n');
    for (ThrowableProxy proxy : suppressed)
      sb.append(proxy.getExtendedStackTraceAsString(suffix)); 
    return sb.toString();
  }
  
  public Throwable getThrowable() {
    return this.throwable;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.causeProxy == null) ? 0 : this.causeProxy.hashCode());
    result = 31 * result + this.commonElementCount;
    result = 31 * result + ((this.extendedStackTrace == null) ? 0 : Arrays.hashCode((Object[])this.extendedStackTrace));
    result = 31 * result + ((this.suppressedProxies == null) ? 0 : Arrays.hashCode((Object[])this.suppressedProxies));
    result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
    return result;
  }
  
  public String toString() {
    String msg = this.message;
    return (msg != null) ? (this.name + ": " + msg) : this.name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ThrowableProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */