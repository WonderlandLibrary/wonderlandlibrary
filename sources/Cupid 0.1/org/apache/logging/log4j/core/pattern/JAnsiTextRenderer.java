package org.apache.logging.log4j.core.pattern;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.status.StatusLogger;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiRenderer;

public final class JAnsiTextRenderer implements TextRenderer {
  public static final Map<String, AnsiRenderer.Code[]> DefaultExceptionStyleMap;
  
  static final Map<String, AnsiRenderer.Code[]> DefaultMessageStyleMap;
  
  private static final Map<String, Map<String, AnsiRenderer.Code[]>> PrefedinedStyleMaps;
  
  private final String beginToken;
  
  private final int beginTokenLen;
  
  private final String endToken;
  
  private final int endTokenLen;
  
  private final Map<String, AnsiRenderer.Code[]> styleMap;
  
  private static void put(Map<String, AnsiRenderer.Code[]> map, String name, AnsiRenderer.Code... codes) {
    map.put(name, codes);
  }
  
  static {
    Map<String, Map<String, AnsiRenderer.Code[]>> tempPreDefs = new HashMap<>();
    Map<String, AnsiRenderer.Code[]> map = (Map)new HashMap<>();
    put(map, "Prefix", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "Name", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE });
    put(map, "NameMessageSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE });
    put(map, "Message", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD });
    put(map, "At", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "CauseLabel", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "Text", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "More", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "Suppressed", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "StackTraceElement.ClassName", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.ClassMethodSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.MethodName", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.NativeMethod", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.FileName", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "StackTraceElement.LineNumber", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "StackTraceElement.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "StackTraceElement.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "StackTraceElement.UnknownSource", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "ExtraClassInfo.Inexact", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "ExtraClassInfo.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "ExtraClassInfo.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "ExtraClassInfo.Location", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "ExtraClassInfo.Version", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    DefaultExceptionStyleMap = Collections.unmodifiableMap((Map)map);
    tempPreDefs.put("Spock", DefaultExceptionStyleMap);
    map = (Map)new HashMap<>();
    put(map, "Prefix", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "Name", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW, AnsiRenderer.Code.BOLD });
    put(map, "NameMessageSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
    put(map, "Message", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD });
    put(map, "At", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "CauseLabel", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "Text", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "More", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "Suppressed", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "StackTraceElement.ClassName", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE });
    put(map, "StackTraceElement.ClassMethodSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.MethodName", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.NativeMethod", new AnsiRenderer.Code[] { AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW });
    put(map, "StackTraceElement.FileName", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "StackTraceElement.LineNumber", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "StackTraceElement.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "StackTraceElement.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "StackTraceElement.UnknownSource", new AnsiRenderer.Code[] { AnsiRenderer.Code.RED });
    put(map, "ExtraClassInfo.Inexact", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "ExtraClassInfo.Container", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "ExtraClassInfo.ContainerSeparator", new AnsiRenderer.Code[] { AnsiRenderer.Code.WHITE });
    put(map, "ExtraClassInfo.Location", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    put(map, "ExtraClassInfo.Version", new AnsiRenderer.Code[] { AnsiRenderer.Code.YELLOW });
    tempPreDefs.put("Kirk", (Map)Collections.unmodifiableMap((Map)map));
    Map<String, AnsiRenderer.Code[]> temp = (Map)new HashMap<>();
    DefaultMessageStyleMap = Collections.unmodifiableMap((Map)temp);
    PrefedinedStyleMaps = Collections.unmodifiableMap(tempPreDefs);
  }
  
  public JAnsiTextRenderer(String[] formats, Map<String, AnsiRenderer.Code[]> defaultStyleMap) {
    Map<String, AnsiRenderer.Code[]> map;
    String tempBeginToken = "@|";
    String tempEndToken = "|@";
    if (formats.length > 1) {
      String allStylesStr = formats[1];
      String[] allStyleAssignmentsArr = allStylesStr.split(" ");
      map = (Map)new HashMap<>(allStyleAssignmentsArr.length + defaultStyleMap.size());
      map.putAll((Map)defaultStyleMap);
      for (String styleAssignmentStr : allStyleAssignmentsArr) {
        String[] styleAssignmentArr = styleAssignmentStr.split("=");
        if (styleAssignmentArr.length != 2) {
          StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", 
              getClass().getSimpleName(), styleAssignmentStr);
        } else {
          String styleName = styleAssignmentArr[0];
          String codeListStr = styleAssignmentArr[1];
          String[] codeNames = codeListStr.split(",");
          if (codeNames.length == 0) {
            StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", 
                
                getClass().getSimpleName(), styleAssignmentStr);
          } else {
            String predefinedMapName;
            Map<String, AnsiRenderer.Code[]> predefinedMap;
            AnsiRenderer.Code[] codes;
            int i;
            switch (styleName) {
              case "BeginToken":
                tempBeginToken = codeNames[0];
                break;
              case "EndToken":
                tempEndToken = codeNames[0];
                break;
              case "StyleMapName":
                predefinedMapName = codeNames[0];
                predefinedMap = PrefedinedStyleMaps.get(predefinedMapName);
                if (predefinedMap != null) {
                  map.putAll((Map)predefinedMap);
                  break;
                } 
                StatusLogger.getLogger().warn("Unknown predefined map name {}, pick one of {}", predefinedMapName, null);
                break;
              default:
                codes = new AnsiRenderer.Code[codeNames.length];
                for (i = 0; i < codes.length; i++)
                  codes[i] = toCode(codeNames[i]); 
                map.put(styleName, codes);
                break;
            } 
          } 
        } 
      } 
    } else {
      map = defaultStyleMap;
    } 
    this.styleMap = map;
    this.beginToken = tempBeginToken;
    this.endToken = tempEndToken;
    this.beginTokenLen = tempBeginToken.length();
    this.endTokenLen = tempEndToken.length();
  }
  
  public Map<String, AnsiRenderer.Code[]> getStyleMap() {
    return this.styleMap;
  }
  
  private void render(Ansi ansi, AnsiRenderer.Code code) {
    if (code.isColor()) {
      if (code.isBackground()) {
        ansi.bg(code.getColor());
      } else {
        ansi.fg(code.getColor());
      } 
    } else if (code.isAttribute()) {
      ansi.a(code.getAttribute());
    } 
  }
  
  private void render(Ansi ansi, AnsiRenderer.Code... codes) {
    for (AnsiRenderer.Code code : codes)
      render(ansi, code); 
  }
  
  private String render(String text, String... names) {
    Ansi ansi = Ansi.ansi();
    for (String name : names) {
      AnsiRenderer.Code[] codes = this.styleMap.get(name);
      if (codes != null) {
        render(ansi, codes);
      } else {
        render(ansi, toCode(name));
      } 
    } 
    return ansi.a(text).reset().toString();
  }
  
  public void render(String input, StringBuilder output, String styleName) throws IllegalArgumentException {
    output.append(render(input, new String[] { styleName }));
  }
  
  public void render(StringBuilder input, StringBuilder output) throws IllegalArgumentException {
    int i = 0;
    while (true) {
      int j = input.indexOf(this.beginToken, i);
      if (j == -1) {
        if (i == 0) {
          output.append(input);
          return;
        } 
        output.append(input.substring(i, input.length()));
        return;
      } 
      output.append(input.substring(i, j));
      int k = input.indexOf(this.endToken, j);
      if (k == -1) {
        output.append(input);
        return;
      } 
      j += this.beginTokenLen;
      String spec = input.substring(j, k);
      String[] items = spec.split(" ", 2);
      if (items.length == 1) {
        output.append(input);
        return;
      } 
      String replacement = render(items[1], items[0].split(","));
      output.append(replacement);
      i = k + this.endTokenLen;
    } 
  }
  
  private AnsiRenderer.Code toCode(String name) {
    return AnsiRenderer.Code.valueOf(name.toUpperCase(Locale.ENGLISH));
  }
  
  public String toString() {
    return "JAnsiMessageRenderer [beginToken=" + this.beginToken + ", beginTokenLen=" + this.beginTokenLen + ", endToken=" + this.endToken + ", endTokenLen=" + this.endTokenLen + ", styleMap=" + this.styleMap + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\JAnsiTextRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */