package org.apache.logging.log4j.core.impl;

import java.io.Serializable;
import java.util.Objects;
import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
import org.apache.logging.log4j.core.pattern.TextRenderer;

public final class ExtendedStackTraceElement implements Serializable {
  static final ExtendedStackTraceElement[] EMPTY_ARRAY = new ExtendedStackTraceElement[0];
  
  private static final long serialVersionUID = -2171069569241280505L;
  
  private final ExtendedClassInfo extraClassInfo;
  
  private final StackTraceElement stackTraceElement;
  
  public ExtendedStackTraceElement(StackTraceElement stackTraceElement, ExtendedClassInfo extraClassInfo) {
    this.stackTraceElement = stackTraceElement;
    this.extraClassInfo = extraClassInfo;
  }
  
  public ExtendedStackTraceElement(String declaringClass, String methodName, String fileName, int lineNumber, boolean exact, String location, String version) {
    this(new StackTraceElement(declaringClass, methodName, fileName, lineNumber), new ExtendedClassInfo(exact, location, version));
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (!(obj instanceof ExtendedStackTraceElement))
      return false; 
    ExtendedStackTraceElement other = (ExtendedStackTraceElement)obj;
    if (!Objects.equals(this.extraClassInfo, other.extraClassInfo))
      return false; 
    if (!Objects.equals(this.stackTraceElement, other.stackTraceElement))
      return false; 
    return true;
  }
  
  public String getClassName() {
    return this.stackTraceElement.getClassName();
  }
  
  public boolean getExact() {
    return this.extraClassInfo.getExact();
  }
  
  public ExtendedClassInfo getExtraClassInfo() {
    return this.extraClassInfo;
  }
  
  public String getFileName() {
    return this.stackTraceElement.getFileName();
  }
  
  public int getLineNumber() {
    return this.stackTraceElement.getLineNumber();
  }
  
  public String getLocation() {
    return this.extraClassInfo.getLocation();
  }
  
  public String getMethodName() {
    return this.stackTraceElement.getMethodName();
  }
  
  public StackTraceElement getStackTraceElement() {
    return this.stackTraceElement;
  }
  
  public String getVersion() {
    return this.extraClassInfo.getVersion();
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { this.extraClassInfo, this.stackTraceElement });
  }
  
  public boolean isNativeMethod() {
    return this.stackTraceElement.isNativeMethod();
  }
  
  void renderOn(StringBuilder output, TextRenderer textRenderer) {
    render(this.stackTraceElement, output, textRenderer);
    textRenderer.render(" ", output, "Text");
    this.extraClassInfo.renderOn(output, textRenderer);
  }
  
  private void render(StackTraceElement stElement, StringBuilder output, TextRenderer textRenderer) {
    String fileName = stElement.getFileName();
    int lineNumber = stElement.getLineNumber();
    textRenderer.render(getClassName(), output, "StackTraceElement.ClassName");
    textRenderer.render(".", output, "StackTraceElement.ClassMethodSeparator");
    textRenderer.render(stElement.getMethodName(), output, "StackTraceElement.MethodName");
    if (stElement.isNativeMethod()) {
      textRenderer.render("(Native Method)", output, "StackTraceElement.NativeMethod");
    } else if (fileName != null && lineNumber >= 0) {
      textRenderer.render("(", output, "StackTraceElement.Container");
      textRenderer.render(fileName, output, "StackTraceElement.FileName");
      textRenderer.render(":", output, "StackTraceElement.ContainerSeparator");
      textRenderer.render(Integer.toString(lineNumber), output, "StackTraceElement.LineNumber");
      textRenderer.render(")", output, "StackTraceElement.Container");
    } else if (fileName != null) {
      textRenderer.render("(", output, "StackTraceElement.Container");
      textRenderer.render(fileName, output, "StackTraceElement.FileName");
      textRenderer.render(")", output, "StackTraceElement.Container");
    } else {
      textRenderer.render("(", output, "StackTraceElement.Container");
      textRenderer.render("Unknown Source", output, "StackTraceElement.UnknownSource");
      textRenderer.render(")", output, "StackTraceElement.Container");
    } 
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    renderOn(sb, (TextRenderer)PlainTextRenderer.getInstance());
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\ExtendedStackTraceElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */